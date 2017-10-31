package com.yunda.zb.zbfw.manager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.rdp.manager.ZbFwVehicleRelationManager;
import com.yunda.freight.zb.rdp.manager.ZbFwWorkNatureRelationManager;
import com.yunda.zb.zbfw.entity.ZbFw;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFw业务类,整备范围
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbFwManager")
public class ZbFwManager extends JXBaseManager<ZbFw, ZbFw> implements IbaseCombo{
    /** ZbglJobProcessNodeDefManager业务类，整备作业流程节点*/
    @Resource
    private ZbglJobProcessNodeDefManager zbglJobProcessNodeDefManager ;
    
    /** 列检范围与车型关联 **/
    @Resource
    private ZbFwVehicleRelationManager zbFwVehicleRelationManager ;
    
    /** 列检范围与作业性质关联 **/
    @Resource
    private ZbFwWorkNatureRelationManager zbFwWorkNatureRelationManager ;
    
    
    /**
	 * <li>说明：批量设置承修车型
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @param searchJson 询条件JSON字符串
	 * @param start  分页起始页
	 * @param limit  每页分页大小
	 * @param order  排序对象数组
	 * @return 返回数据分页列表
	 * @throws BusinessException,NoSuchFieldException
	 */	
    public Page<ZbFw> getZbFwList(ZbFw entity, String searchJson, int start, int limit,Order[] order){
    	String selectSql = "select a.idx as \"idx\"," + 
    	               "a.FW_Code as  fwCode, "+
    	               "a.FW_Name as  fwName, "+
    	               "a.FW_Desc as  fwDesc, "+
    	               "a.siteID as   siteID,  "+
    	               "a.siteName as siteName, "+
                       "a.record_status as recordStatus, "+
    	               "b.CX_PYM as trainTypeShortName, "+
    	               "b.CX_BM as trainTypeIDX ";
    	String fromSql = " from ZB_ZBFW a left join(SELECT zbfw_idx,wm_concat(cx_pym) AS CX_PYM, wm_concat(cx_bm) AS CX_BM FROM zb_zbfw_cx WHERE record_status = 0 GROUP BY zbfw_idx) b ON a.IDX = b.zbfw_idx";
    	StringBuffer awhere =  new StringBuffer(); 
        awhere.append(" where a.record_status = 0 ");
    	SearchEntity<ZbFw> searchEntity = new SearchEntity<ZbFw>(entity, start,limit, order);
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getFwName())){
              awhere.append(" and a.FW_Name like '%").append(searchEntity.getEntity().getFwName()).append("%' ");
          }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getFwDesc())){
              awhere.append(" and a.FW_Desc like '%").append(searchEntity.getEntity().getFwDesc()).append("%' ");
          }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainTypeIDX())){
              awhere.append(" and b.CX_BM like '%").append(searchEntity.getEntity().getTrainTypeIDX()).append("%' ");
         }
       
        String totalSql = "select count(*) " + fromSql;   
        totalSql += awhere;
        String querySql = selectSql + fromSql + awhere;
        return super.findPageList(totalSql, querySql, start , limit, null, order);
    }
    

    /**
     * <li>说明：验证整备范围编码是否重复、车型是 是否重复
     * <li>创建人：王利成
     * <li>创建日期：2015-3-20
     * <li>修改人： 林欢
     * <li>修改日期：2016-7-27
     * <li>修改内容：由于3.2.10需求改动，针对同一车型可以添加多个整备范围
     * @param zbFw  整备范围
     * @return String[]
     * @throws BusinessException
     */
    public String[] validateUpdate(ZbFw zbFw) throws BusinessException{
       //获取整备范围记录集合 
       ZbFw searchEntity = new ZbFw();
       List<ZbFw> entityList = this.findList(searchEntity);
       if (null != entityList && entityList.size() > 0) {
            for(ZbFw zf : entityList) {
                if (zf.getIdx().equals(zbFw.getIdx())) {
                    continue;
                }
                if (zf.getFwCode().equals(zbFw.getFwCode())) {
                    return new String[]{"整备范围编码【" +zbFw.getFwCode() + "】已经存在，不能重复添加！"};
                }
                
//                if (!StringUtil.isNullOrBlank(zf.getTrainTypeIDX()) && zf.getTrainTypeIDX().equals(zbFw.getTrainTypeIDX())) {
//                    return new String[]{"车型【" + zbFw.getTrainTypeShortName() + "】已有整备范围，不能重复添加！"};
//                }
            }
        }
        return null;
     }
    /**
     * <li>说明：逻辑删除作业项目、数据项，物理删除前后置关系、扩展配置
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的实体主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        super.logicDelete(ids);
        for (Serializable idx : ids) {
            // 级联删除下属作业流程节点
            List<ZbglJobProcessNodeDef> nodeList = this.zbglJobProcessNodeDefManager.getModelsByZbfwIDX(idx);
            if (null != nodeList && nodeList.size() > 0) {
                this.zbglJobProcessNodeDefManager.logicDelete(nodeList);
            }
        }
    }
    
    /**
     * <li>说明：获取车型对应的整备范围
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 林欢
     * <li>修改日期：2016-8-17
     * <li>修改内容：同一车型可能会有多个范围
     * @param trainTypeIDX 车型IDX
     * @return 整备范围
     */
    @SuppressWarnings("unchecked")
    public List<ZbFw> getZbFwByTrain(String trainTypeIDX) {
        if (StringUtil.isNullOrBlank(trainTypeIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainTypeIDX", trainTypeIDX);
        return getZbFw(paramMap);
    }
    
    /**
     * <li>说明：获取整备范围
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 林欢
     * <li>修改日期：2016-8-17
     * <li>修改内容：同一车型可能会有多个范围
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 整备范围
     */
    private List<ZbFw> getZbFw(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbFw where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }

    /**
     * 
     * <li>说明：根据车型车号过滤整备范围流程
     * <li>创建人：林欢
     * <li>创建日期：2016-8-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramsMap 查询参数 （trainNo，trainTypeIDX）
     * @param start  分页起始页
     * @param limit  每页分页大小
     * @param order  排序对象数组
     * @return 返回数据分页列表
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page<ZbFw> findZbFwNotInTrainNoAndTrainTypeIDX(Map<String, Object> paramsMap, Integer start, Integer limit, Order[] orders) throws SecurityException, NoSuchFieldException {
            StringBuilder sb = new StringBuilder();
            
            sb.append(" select * from zb_zbfw a where a.idx not in ( ");
            sb.append(" select b.zbfw_idx from zb_zbfw_train_center b  ");
            sb.append(" where b.train_type_idx = '").append(paramsMap.get("trainTypeIDX").toString()).append("'");
            sb.append(" and b.train_no = '").append(paramsMap.get("trainNo").toString()).append("'");
            sb.append(" ) and a.record_status = 0 ");
            
            // 排序处理
            if (null != orders && orders.length > 0) {
                String[] order = orders[0].toString().split(" ");
                String sort = order[0];

                //前台传递过来的排序方式 desc或者asc
                String dir = order[1];
                Class clazz = ZbFw.class;
                //通过传递过来需要排序的字段反射字段对象
                Field field = clazz.getDeclaredField(sort);
                //获取字段上，标签上的列名
                Column annotation = field.getAnnotation(Column.class);
                if (null != annotation) {
                    sb.append(" order by a.").append(annotation.name()).append(" ").append(dir);
                } else {
                    sb.append(" order by a.").append(sort).append(" ").append(dir);
                }
            } 
            //此处的总数别名必须是ROWCOUNT，封装方法有规定
            String totalSql = " select count(*) as rowcount " + sb.substring(sb.indexOf(" from "));
            return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbFw.class);
    } 
    
    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：林欢
     * <li>创建日期：2016-8-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
            //获取车型查询条件
            String trainTypeIDX = CommonUtil.getMapValue(queryParamsMap, "trainTypeIDX");
            
            StringBuffer sb = new StringBuffer();
            
            sb.append(" select * from zb_zbfw a where a.record_status = 0  ");
            sb.append(" and a.train_type_idx = '").append(trainTypeIDX).append("'");
            
            String totalSql = " select count(*) as rowcount " + sb.substring(sb.indexOf(" from "));
            Page page = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbFw.class);
            
            map = page.extjsStore();
        }
        return map;
    }


    /**
     * <li>说明：设置适用车型
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 范围ID集合
     * @param trianCodes 适用车型code集合
     * @param trianTypes 适用车型名称集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void setTrainVehicle(String idxs, String trianCodes, String trianTypes) throws BusinessException, NoSuchFieldException {
        
        String[] idxArray = idxs.split(",");
        String[] trianCodeArray = trianCodes.split(",");
        String[] trianTypeArray = trianTypes.split(",");
        for (String idx : idxArray) {
            ZbFw fw = this.getModelById(idx);
            if(fw != null){
                fw.setTrainVehicleCode(trianCodes);
                fw.setTrainVehicleName(trianTypes);
                this.saveOrUpdate(fw);
                zbFwVehicleRelationManager.saveVehicleRelations(idx,trianCodeArray,trianTypeArray);
            }
        }
        
    }


    /**
     * <li>说明：保存列检范围
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fw 列检范围
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveZbFw(ZbFw fw) throws BusinessException, NoSuchFieldException {
        // 判断作业性质是否改变
        if(!StringUtil.isNullOrBlank(fw.getIdx())){
            int count = getZbFwByIdxAndWorkNatureCode(fw.getIdx(), fw.getWorkNatureCode());
            if(count == 0){
                // 如果改变，则清空适用车型
                fw.setTrainVehicleCode(null);
                fw.setTrainVehicleName(null);
                zbFwVehicleRelationManager.deleteVehicleRelations(fw.getIdx());
            }
        }
        this.saveOrUpdate(fw);
        // 保存作业性质和列检范围关联
        if(!StringUtil.isNullOrBlank(fw.getWorkNatureCode())){
            String[] WorkNatureCodeArray = fw.getWorkNatureCode().split(";");
            String[] workNatureArray = fw.getWorkNature().split(";");
            zbFwWorkNatureRelationManager.saveWorkNatureRelations(fw.getIdx(),WorkNatureCodeArray,workNatureArray);
        }
    }
    
    /**
     * <li>说明：通过idx和作业性质编码查询其数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx
     * @param workNatureCode
     * @return
     */
    public int getZbFwByIdxAndWorkNatureCode(String idx , String workNatureCode){
        StringBuffer hql = new StringBuffer(" select count(*) from ZbFw where recordStatus = 0 and idx = ? and workNatureCode = ? ");
        return this.daoUtils.getCount(hql.toString(), new Object[]{idx,workNatureCode});
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param vehicleCode 车型编码
     * @param workNatrueCode 作业性质编码
     * @param vehicleType 客货类型
     * @return
     */
    public ZbFw getZbfwByVehicleCodeAndWorkNatrue(String vehicleCode,String workNatrueCode, String vehicleType) throws BusinessException, NoSuchFieldException {
        StringBuffer hql = new StringBuffer(" select f From ZbFw f,ZbFwVehicleRelation vr,ZbFwWorkNatureRelation nr where f.recordStatus = 0 ");
        hql.append(" and f.vehicleType = ? and f.idx = vr.zbfwIdx and f.idx = nr.zbfwIdx and vr.trainVehicleCode = ? and nr.workNatureCode = ? ");
        ZbFw fw = (ZbFw)this.daoUtils.findSingle(hql.toString(), new Object[]{vehicleType,vehicleCode,workNatrueCode});
        return fw ;
    }
    
    /**
     * <li>说明：通过车型查询适用范围
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param vehicleCode   车型编码
     * @param vehicleType   类型
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public ZbFw getZbfwByVehicleCode(String vehicleCode,String vehicleType) throws BusinessException, NoSuchFieldException {
        StringBuffer hql = new StringBuffer(" select f From ZbFw f,ZbFwVehicleRelation vr where f.recordStatus = 0 ");
        hql.append(" and f.vehicleType = ? and f.idx = vr.zbfwIdx and vr.trainVehicleCode = ? ");
        ZbFw fw = (ZbFw)this.daoUtils.findSingle(hql.toString(), new Object[]{vehicleType,vehicleCode});
        return fw ;
    }
    
}