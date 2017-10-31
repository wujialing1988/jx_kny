package com.yunda.jx.base.jcgy.manager;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.EquipFault;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipFault业务类,故障现象编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="equipFaultManager")
public class EquipFaultManager extends JXBaseManager<EquipFault, EquipFault>{
    
	/**
	 * <li>方法名称：page
	 * <li>方法说明：故障现象查询方法 
	 * <li>创建人：张凡
	 * <li>创建时间：2012-12-3 下午02:30:21
	 * <li>修改人：
	 * <li>修改内容：
	 * @param queryValue queryValue
	 * @param queryParams queryParams
	 * @param start 开始索引
	 * @param limit 结束索引
	 * @return Map<String,Object>
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object> page(String queryValue,Map queryParams, int start, int limit) throws ClassNotFoundException {
        StringBuffer hql = new StringBuffer();
       
        hql.append(" select new EquipFault(e.FaultID,e.FaultName) from EquipFault e where 1=1 ");
        if (!StringUtil.isNullOrBlank(queryValue)) {
            hql.append(" and e.FaultName like '%").append(queryValue).append("%'");
        }
        if (!queryParams.isEmpty()) {
            Set<Map.Entry<String, String>> set = queryParams.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (!StringUtil.isNullOrBlank(value)) {
                    hql.append(" and e.").append(key).append(" = '").append(value).append("'");
                }
            }
        }
        
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    /**
     * 
     * <li>说明：获取故障现象列表(工位终端同时调用方法)
     * <li>创建人：程锐
     * <li>创建日期：2013-1-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 故障现象实体对象
     * @param fixPlaceIdx 位置主键
     * @param buildUpTypeIdx 组成主键
     * @return void
     * @throws Exception
     */
    public Page<EquipFault> faultList(SearchEntity<EquipFault> searchEntity, String flbm) throws BusinessException {        
        String hql =
            "from EquipFault where FaultID not in (select faultId from JcxtflFault where 1 = 1 ";
        if(!StringUtil.isNullOrBlank(flbm)){
            hql+= " and flbm = '" + flbm + "'";
        }
        hql+= ")";
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getFaultName())) {
            hql += " and FaultName like '%" + searchEntity.getEntity().getFaultName() + "%'";
        }
        Order[] orders = searchEntity.getOrders();
        hql += HqlUtil.getOrderHql(orders);
        String totalHql = "select count(*) " + hql;
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }     
    /**
     * 
     * <li>说明：查询故障现象列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param faultName 故障现象名称
     * @param start 开始索引
     * @param limit 结束索引
     * @return 故障现象列表
     */
    @SuppressWarnings("unchecked")
	public Page queryFaultList(String faultName,int start, int limit) {
    	EquipFault entity = new EquipFault();
		if(!StringUtil.isNullOrBlank(faultName)){
			entity.setFaultName(faultName);
		}
		SearchEntity<EquipFault> searchEntity = new SearchEntity<EquipFault>(entity, start, limit, null);
		Page page = faultList(searchEntity, null) ;
		return page;
    }
    
    /**
     * <li>说明：重写保存方法，因为业务实体设计没有设置常规的idx字段作为主键
     * <li>创建人：何涛
     * <li>创建日期：2015-05-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 故障现象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(EquipFault t) throws BusinessException, NoSuchFieldException {
        EquipFault entity = this.getModelById(t.getFaultID());
        if (null == entity) {
            this.insert(t);
            return;
        }
        entity.setFaultName(t.getFaultName());
        entity.setFaultTypeID(t.getFaultTypeID());
        super.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：重写分页查询方法
     * <li>创建人：何涛
     * <li>创建日期：2015-05-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return Page 
     * @throws BusinessException
     */
    @Override
    public Page<EquipFault> findPageList(SearchEntity<EquipFault> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("From EquipFault Where 0 = 0");
        EquipFault entity = searchEntity.getEntity();
        // 查询条件 - 故障编号
        if (!StringUtil.isNullOrBlank(entity.getFaultID())) {
            sb.append(" And FaultID Like '%").append(entity.getFaultID()).append("%'");
        }
        // 查询条件 - 故障名称
        if (!StringUtil.isNullOrBlank(entity.getFaultName())) {
            sb.append(" And FaultName Like '%").append(entity.getFaultName()).append("%'");
        }
        // 查询条件 - 故障类别
        if (!StringUtil.isNullOrBlank(entity.getFaultTypeID())) {
            sb.append(" And FaultTypeID Like '%").append(entity.getFaultTypeID()).append("%'");
        }
        if (null != searchEntity.getOrders()) {
            sb.append(" Order By ").append(searchEntity.getOrders()[0]);
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
}