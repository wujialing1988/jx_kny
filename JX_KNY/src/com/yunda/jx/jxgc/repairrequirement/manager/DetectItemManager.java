package com.yunda.jx.jxgc.repairrequirement.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.DetectItem;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DetectItem业务类,检测项
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="detectItemManager")
public class DetectItemManager extends JXBaseManager<DetectItem, DetectItem>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：通过工步主键查询检测项
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String workStepIDX：关联外键ID ；
     * @return List<DetectItem> 检测项列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<DetectItem> getModelList(String workStepIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from DetectItem t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(workStepIDX)){
            hql.append(" and t.workStepIDX = '").append(workStepIDX).append("'");
        }
        return this.daoUtils.find(hql.toString());
    }
        
    /* (non-Javadoc)
     * @see com.yunda.jx.common.JXBaseManager#findPageList(com.yunda.jx.common.SearchEntity)
     */
    @SuppressWarnings("unchecked")
	@Override
	public Page<DetectItem> findPageList(final SearchEntity<DetectItem> searchEntity) throws BusinessException {
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<DetectItem>)template.execute(new HibernateCallback(){
			public Page<DetectItem> doInHibernate(Session s){
				try {
					DetectItem entity = searchEntity.getEntity();
					//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
					if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
						BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);
					}					
					//过滤掉idx、siteID、creator、createTime、updator、updateTime的查询条件
					Example exp = Example.create(entity)
						.excludeProperty(EntityUtil.IDX)
//						.excludeProperty(EntityUtil.SITE_ID)//由于多站点数据过滤需求，siteID视为业务字段
						.excludeProperty(EntityUtil.CREATOR)
						.excludeProperty(EntityUtil.CREATE_TIME)
						.excludeProperty(EntityUtil.UPDATOR)
						.excludeProperty(EntityUtil.UPDATE_TIME)
						//.enableLike()
						.enableLike(MatchMode.EXACT);	//使用全等匹配条件
						//.enableLike(MatchMode.ANYWHERE);
					//查询总记录数
					int total = ((Integer)s.createCriteria(entity.getClass())
						.add(exp)
						.setProjection(Projections.rowCount())
						.uniqueResult())
						.intValue();
					//分页列表
					Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
						.setFirstResult(searchEntity.getStart())
						.setMaxResults(searchEntity.getLimit());
					//设置排序规则
					Order[] orders = searchEntity.getOrders();
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}					
					}
					//如果该实体类存在修改时间字段，则追加按修改时间倒序
					if(EntityUtil.contains(entity.getClass(), EntityUtil.UPDATE_TIME)){
						criteria.addOrder(Order.desc("updateTime"));
					}					
					return new Page<DetectItem>(total, criteria.list());
				} catch (Exception e) {
					ExceptionUtil.process(e,logger);
				}
				return null;
			}
		});
    }
}