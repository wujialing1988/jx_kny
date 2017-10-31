package com.yunda.zb.zbfw.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.zb.zbfw.entity.ZbfwTrainCenter;
import com.yunda.zb.zbfw.entity.ZbfwTrainCenterDTO;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbfwTrianCenterManager业务类,范围活和车型车号绑定的中间表 
 * <li>创建人：林欢
 * <li>创建日期：2016-07-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbfwTrainCenterManager")
public class ZbfwTrainCenterManager extends JXBaseManager<ZbfwTrainCenter, ZbfwTrainCenter>{
    
    /** JczlTrain业务类,机车信息 */
    @Resource
    private JczlTrainManager jczlTrainManager;

    /**
     * <li>说明：：获取绑定车型信息
     * <li>创建人：林欢
     * <li>创建日期：2016-7-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page<ZbfwTrainCenterDTO> findZbfwTrainInfo(SearchEntity<ZbfwTrainCenter> searchEntity) throws SecurityException, NoSuchFieldException {
        ZbfwTrainCenter entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("select t.* from zb_zbfw_train_center t ");
        
        sb.append(" where t.zbfw_IDX = '").append(entity.getZbfwIDX()).append("'");
        //判断车号是否传递
        if (StringUtils.isNotBlank(entity.getTrainNo())) {
            sb.append(" and t.Train_No like '%").append(entity.getTrainNo()).append("%'");
        }
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = ZbfwTrainCenterDTO.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY t.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY t.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY t.idx");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, ZbfwTrainCenterDTO.class);
    }

    /**
     * <li>说明：保存范围和车型车号中间表关系
     * <li>创建人：林欢
     * <li>创建日期：2016-07-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwTrainCenterArray 传入数据
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveOrUpdateZbfwTrainCenterInfo(ZbfwTrainCenter[] zbfwTrainCenterArray) throws BusinessException, NoSuchFieldException {
        
        //获取配置文件中的did
        String jXConfigDID = JXConfig.getInstance().getDid();
        
        //遍历数组循环保存
        for (ZbfwTrainCenter center : zbfwTrainCenterArray) {
            //通过车型车号查询机车配属段信息
            String did = jczlTrainManager.getJczlTrainDIDByTrainNoAndTrainTpyeIDX(center.getTrainNo(),center.getTrainTypeIDX());
            if (StringUtils.isNotBlank(did)) {
                //如果和配置文件中配置的本段配属段编码一致，说明是本段否则为非本段
                if (jXConfigDID.equals(did)) {
                    center.setIsThisSite("0");
                }else {
                    center.setIsThisSite("1");
                }
            }else {
                center.setIsThisSite("1");
            }
            
            this.saveOrUpdate(center);
        }
        
    }

    /**
     * <li>说明：通过车型车号查询范围和车型车号中间表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体
     * @throws Exception 
     */
    public ZbfwTrainCenter findZbfwIDXByTrainNoAndTrainTypeIDX(String trainNo, String trainTypeIDX) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from ZbfwTrainCenter a ");
        sb.append(" where a.trainNo = '").append(trainNo).append("'");
        sb.append(" and a.trainTypeIDX = '").append(trainTypeIDX).append("'");
        
        return this.findSingle(sb.toString());
    }

    /**
     * <li>说明：更新范围和车型车号中间表关系
     * <li>创建人：林欢
     * <li>创建日期：2016-08-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 范围流程主键idx
     * @param zbfwTrianCenterIDX 中间表idx
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateZbfwTrainCenterInfo(String zbfwIDX, String zbfwTrianCenterIDX,String fwName) {
        //获取中间表对象
        ZbfwTrainCenter zbfwTrainCenter = this.getModelById(zbfwTrianCenterIDX);
        //修改范围主键idx
        zbfwTrainCenter.setZbfwIDX(zbfwIDX);
        zbfwTrainCenter.setFwName(fwName);
        this.getDaoUtils().update(zbfwTrainCenter);
    }

    /**
     * <li>说明：根据车型车号查询中间表对象
     * <li>创建人：林欢
     * <li>创建日期：2016-08-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainNo 车号
     * @param trainTypeIDX 车型idx 
     * @return ZbfwTrainCenter 中间表对象
     * @throws JsonMappingException
     * @throws IOException
     */
    public ZbfwTrainCenter findZbfwTrainCenterByTrainNoAndTrainTypeIDX(String trainNo, String trainTypeIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbfwTrainCenter a where 1=1");
        //车型
        if (StringUtils.isNotBlank(trainNo)) {
            sb.append(" and a.trainNo = '").append(trainNo).append("'");
        }
        //车号
        if (StringUtils.isNotBlank(trainNo)) {
            sb.append(" and a.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        return this.findSingle(sb.toString());
    }

}