package com.yunda.jx.jczl.undertakemanage.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainTypeRC;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrainTypeRC业务类,承修车型对应修程
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "undertakeTrainTypeRCManager")
public class UndertakeTrainTypeRCManager extends JXBaseManager<UndertakeTrainTypeRC, UndertakeTrainTypeRC> {
    
    /**
     * <li>说明：确定该业务类是否使用查询缓存
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return true使用查询缓存，false不使用
     */
    @Override
    protected boolean enableCache() {
        return true;
    }
    
    /**
     * <li>说明：新增修改保存前的实体对象前的批量验证业务
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 实体集合
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    public String[] validateUpdate(UndertakeTrainTypeRC[] entityList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for (UndertakeTrainTypeRC obj : entityList) {
            if (!StringUtil.isNullOrBlank(obj.getUndertakeTrainTypeIDX()) && !StringUtil.isNullOrBlank(obj.getRcIDX())) {
                List<UndertakeTrainTypeRC> countList = this.getModelList(obj.getUndertakeTrainTypeIDX(), obj.getRcIDX());
                if (countList.size() > 0) {
                    errMsg.add("【" + obj.getTrainShortName() + "】车型的【" + obj.getXcName() + "】修程，已经存在！");
                }
            }
            if (errMsg.size() > 0) {
                String[] errArray = new String[errMsg.size()];
                errMsg.toArray(errArray);
                return errArray;
            }
        }
        return null;
    }
    
    /**
     * <li>说明：查询承修车型对应修程
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询的Json对象
     * @param start 分页开始
     * @param limit 分页结束
     * @param orders 排序对象
     * @return Page分页列表
     * @throws BusinessException
     */
    public Page<UndertakeTrainTypeRC> findPageLinkList(String searchJson, int start, int limit, Order[] orders) throws BusinessException {
        StringBuffer topHql = new StringBuffer();
        topHql.append("select t.idx,x.xc_name as \"xcName\",");
        topHql.append("t.RC_IDX as \"rcIDX\" ");
        
        StringBuffer hqlTmp = new StringBuffer();
        hqlTmp.append(" from JCZL_UNDERTAKE_TRAIN_TYPE_RC t , j_jcgy_xc x ");
        hqlTmp.append(" where 1=1 and t.rc_idx=x.xc_id and t.record_status=0 ");
        
        StringBuffer awhere = new StringBuffer();
        awhere.append(" order by t.update_Time DESC");
        String totalSql = "select count(t.idx) " + hqlTmp + awhere;
        String hql = topHql.toString() + hqlTmp.toString() + awhere.toString();
        return super.findPageList(totalSql, hql, start, limit, searchJson, orders);
    }
    
    /**
     * <li>说明：批量设置承修车型对应的修程
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param objList 对象集合
     * @return 返回错误信息列表
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveOrUpdateList(UndertakeTrainTypeRC[] objList) throws BusinessException, NoSuchFieldException {
        String[] errMsg = this.validateUpdate(objList); // 验证
        List<UndertakeTrainTypeRC> entityList = new ArrayList<UndertakeTrainTypeRC>();
        if (errMsg == null || errMsg.length < 1) {
            for (UndertakeTrainTypeRC t : objList) { // 循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
    
    /**
     * <li>说明：查询承修车型对应的修程
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
     * @param trainIDX 车型（主键）
     * @return list集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<UndertakeTrainTypeRC> getModelList(String trainIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("From UndertakeTrainTypeRC t where t.recordStatus=0 and t.undertakeTrainTypeIDX = '").append(trainIDX).append("'");
        return daoUtils.find(enableCache(), hql.toString());
    }
    
    /**
     * <li>说明：通过车型和修程查询承修车型对应的修程
     * <li>创建人：王治龙
     * <li>创建日期：2012-11-7
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询：
     * @param trainIDX 车型（主键）
     * @param rcIDX 修程编码（主键）
     * @return List<RCRT> list集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<UndertakeTrainTypeRC> getModelList(String trainIDX, String rcIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("From UndertakeTrainTypeRC t where t.recordStatus=0 ");
        if (!StringUtil.isNullOrBlank(trainIDX)) {
            hql.append("and t.undertakeTrainTypeIDX = '").append(trainIDX).append("'");
        }
        if (!StringUtil.isNullOrBlank(rcIDX)) {
            hql.append("and t.rcIDX = '").append(rcIDX).append("'");
        }
        return daoUtils.find(enableCache(), hql.toString());
    }
    
    /**
     * <li>说明：根据承修车型主键获取修程名称，多个修程之间用逗号分隔
     * <li>创建人：何涛
     * <li>创建日期：2015-8-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param undertakeTrainTypeIDX 承修车型主键
     * @return 修程名称，多个修程之间用逗号分隔
     */
    @SuppressWarnings("unchecked")
    public String getRcGroupByUndertakeTrainTypeIDX(String undertakeTrainTypeIDX) {
        // 关联查询修程名称
        String sql =
            "SELECT B.XC_NAME AS xcName from JCZL_UNDERTAKE_TRAIN_TYPE_RC A, J_JCGY_XC B WHERE A.RECORD_STATUS = 0 AND A.RC_IDX = B.XC_ID AND A.Undertake_Train_Type_IDX = '"
                + undertakeTrainTypeIDX + "'";
        List list = this.daoUtils.executeSqlQuery(sql);
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 多个修程之间以逗号进行分隔
        for (Object o : list) {
            sb.append(",").append((String) o);
        }
        return sb.substring(1);
    }
    
}
