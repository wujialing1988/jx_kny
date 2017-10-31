package com.yunda.jx.pjwz.partsBase.outsourcinglist.manager;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.entity.PartsOutsourcingList;

@Service(value="partsOutsourcingListManager")
public class PartsOutsourcingListManager extends JXBaseManager<PartsOutsourcingList, PartsOutsourcingList> {
    
    /**
     * <li>方法说明：查询委外修配件列表 
     * <li>方法名称：findPageDataList
     * <li>@param searchJson
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page
     * <li>创建人：张凡
     * <li>创建时间：2013-10-23 上午10:37:05
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public Page findPageDataList(String searchJson,int start,int limit, Order[] orders) throws BusinessException{
        String q_sql = SqlMapUtil.getSql("pjwl-query:findOutsourcingList_select1");
        
        String fromSql = SqlMapUtil.getSql("pjwl-query:findOutsourcingList_from1");
        StringBuffer sql_order =  new StringBuffer(" order by t.update_Time DESC");
        String totalSql = "select count(1) " + fromSql;
        String sql =q_sql+ fromSql + sql_order;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    
    /**
     * 重写保存验证方法
     */
    @Override
    public String[] validateUpdate(PartsOutsourcingList t){
        if(null == t.getMadeFactoryID() || null == t.getPartsTypeIDX()){
            return new String[]{"参数不完整，请重新输入！"};
        }
        
        String hql = "From PartsOutsourcingList Where recordStatus = 0 And partsTypeIDX = ? And madeFactoryID = ?";
        PartsOutsourcingList entity = (PartsOutsourcingList) this.daoUtils.findSingle(hql, new Object[]{ t.getPartsTypeIDX(), t.getMadeFactoryID() });
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[]{"不可以添加重复的数据！"};
        }
        
        return null;
    }
    /**
     * 
     * <li>方法说明：新增委外配件型号列表 
     * <li>方法名称：saveNewList
     * <li>@param idxs
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:13:31
     * <li>修改人：
     * <li>修改内容：
     */
    public int saveNewList(String idxs){
        
        String sql = SqlMapUtil.getSql("pjwl-parts-type:newOutSourcingList")
            .replace("操作员", SystemContext.getAcOperator().getOperatorid().toString())
            .replace("配件型号主键", "'" + idxs.replace(",", "','") + "'")
            .replace("站点", JXSystemProperties.SYN_SITEID);
            
        return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>方法说明：更新生产厂家 
     * <li>方法名称：updateSetMadeFactory
     * <li>@param idxs
     * <li>@param factoryId
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:43:06
     * <li>修改人：
     * <li>修改内容：
     */
    public int updateSetMadeFactory(String idxs, String factoryId){
        
        String sql = SqlMapUtil.getSql("pjwl-parts-type:updateOutSourcingMadeFactory")
        .replace("操作员", SystemContext.getAcOperator().getOperatorid().toString())
        .replace("列表主键", "'" + idxs.replace(",", "','") + "'")
        .replace("工厂ID", factoryId);
        
        return daoUtils.executeSql(sql);
    }
}
