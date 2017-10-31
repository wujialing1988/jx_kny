package com.yunda.jxpz.datacollect.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.datacollect.entity.DataCollect;
import com.yunda.jxpz.datacollect.entity.DataCollectId;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DataCollect业务类,常用数据收藏夹
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="dataCollectManager")
public class DataCollectManager extends JXBaseManager<DataCollect, DataCollect>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据收藏者主键查询常用数据收藏夹列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @param collectEmpId 收藏者id
     * @return Page 收藏夹分类列表对象
     * @throws BusinessException
     */
    public Page<DataCollect> pageListByEmpId(final SearchEntity<DataCollect> searchEntity, String collectEmpId ) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from DataCollect where 1=1 ");
        if (!StringUtil.isNullOrBlank(collectEmpId)) {
            hql.append(" and id.collectEmpId ='").append(collectEmpId).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));
        String totalHql = "select count(*) " + hql.toString();
        return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * 
     * <li>说明：保存前验证唯一性
     * <li>创建人：程梅
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param collectId 需验证的常用数据收藏信息
     * @return String[] 错误提示
     */
    public String[] validateSave(DataCollectId collectId) {
        List<String> errMsg = new ArrayList<String>();
        if(null != collectId){
            String hql = "Select count(*) From DataCollect where id.dataEntity='"+collectId.getDataEntity()+"' and id.dataIdx='"+collectId.getDataIdx()
            +"' and id.collectEmpId='"+collectId.getCollectEmpId()+"'";
            int count = this.daoUtils.getCount(hql);
            if(count > 0){
                errMsg.add("此数据已被收藏！");
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    /**
     * 
     * <li>说明：保存常用数据收藏信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param collectId 常用数据收藏信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveDataCollect(DataCollectId collectId) throws BusinessException, NoSuchFieldException {
        if(null != collectId){
            DataCollect collect = new DataCollect() ;
            collect.setId(collectId);
            this.daoUtils.getHibernateTemplate().save(collect);
        }
    }
}