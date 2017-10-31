package com.yunda.frame.common.hibernate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类用于单表动态查询的条件包装，简化动态多条件查询
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class QueryCriteria<T> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/** 查询实体类  */
	private Class entityClass;
	/** 查询条件列表 */
	private List<Condition> whereList;	
	/** 描述排序规则的列表  */
	private List<Order> orders;	
	/** 分页查询开始行数  */
	private Integer start;
	/** 该页最大记录总数  */
	private Integer limit;
	/** 执行查询前是否要过滤掉与业务无关的条件 */
	private boolean filterInvaild = true;
	
	/**
	 * <li>说明：空构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-10
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public QueryCriteria(){}
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param clazz 查询实体类
	 */	
	public QueryCriteria(Class entityClass){
		this.setEntityClass(entityClass);
	}	
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param clazz 查询实体类
	 * @param whereList 查询条件列表
	 */	
	public QueryCriteria(Class entityClass, List<Condition> whereList){
		this.setEntityClass(entityClass);
		this.setWhereList(whereList);
	}	
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param clazz 查询实体类
	 * @param whereList 查询条件列表
	 * @param orders 排序规则的数组
	 * @param start 分页查询开始行数
	 * @param limit 该页最大记录总数
	 */	
	public QueryCriteria(Class entityClass, List<Condition> whereList, List<Order> orders, Integer start, Integer limit){
		this.setEntityClass(entityClass);
		this.setWhereList(whereList);
		this.setStart(start);
		this.setLimit(limit);
		this.setOrders(orders);
	}
	/**
	 * <li>说明：添加查询条件表达式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Condition condition 查询条件表达式
	 * @return QueryCriteria this
	 * @throws  
	 */
	public QueryCriteria addCondition(Condition condition){
		if(condition == null)	return this;
		if(whereList == null)	whereList = new ArrayList<Condition>();
		whereList.add(condition);
		return this;
	}
	/**
	 * <li>说明：构件QBC查询器并返回
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Session session：hibernate会话连接对象
	 * @return Criteria：QBC查询对象
	 * @throws ParseException 
	 * @throws NoSuchFieldException 
	 * @throws ParseException 
	 * @throws 抛出异常列表
	 */
	public Criteria getCriteria(Session session) throws NoSuchFieldException, ParseException {
		Criteria criteria = session.createCriteria(this.entityClass);
		if (this.whereList != null && this.whereList.size() > 0) {
			for (Condition condition : this.whereList) {
				if(this.filterInvaild){
					String name = condition.getPropName();
					if(EntityUtil.CREATOR.equals(name) || EntityUtil.CREATE_TIME.equals(name)
							|| EntityUtil.UPDATOR.equals(name) || EntityUtil.UPDATE_TIME.equals(name)){
						continue;
					}			
				}
				criteria.add(condition.getExpression(this.entityClass));	//组装查询条件
			}
		}
		if(this.filterInvaild && EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)){
//			过滤逻辑删除记录
			Condition noDelete = new Condition(EntityUtil.RECORD_STATUS, Condition.EQ, Constants.NO_DELETE);
			criteria.add(noDelete.getExpression(this.entityClass));
		}		
		if (this.orders != null && this.orders.size() > 0){
			for (Order order : orders) {
				criteria.addOrder(order);
			}			
		}
		if(EntityUtil.contains(entityClass, EntityUtil.UPDATE_TIME))	criteria.addOrder(Order.desc(EntityUtil.UPDATE_TIME));
		return criteria;
	}
	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return DetachedCriteria:离线状态的hibernate查询对象
	 * @throws ParseException 
	 * @throws NoSuchFieldException 
	 */
	public DetachedCriteria getDetachedCriteria() throws NoSuchFieldException, ParseException{
		DetachedCriteria dc = DetachedCriteria.forClass(this.entityClass);
		if (this.whereList != null && this.whereList.size() > 0) {
			for (Condition condition : this.whereList) {
				if(this.filterInvaild){
					String name = condition.getPropName();
					if(EntityUtil.CREATOR.equals(name) || EntityUtil.CREATE_TIME.equals(name)
							|| EntityUtil.UPDATOR.equals(name) || EntityUtil.UPDATE_TIME.equals(name)){
						continue;
					}			
				}
				dc.add(condition.getExpression(this.entityClass));	//组装查询条件
			}
		}
		if(this.filterInvaild && EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)){
//			过滤逻辑删除记录
			Condition noDelete = new Condition(EntityUtil.RECORD_STATUS, Condition.EQ, Constants.NO_DELETE);
			dc.add(noDelete.getExpression(this.entityClass));
		}		
		if (this.orders != null && this.orders.size() > 0){
			for (Order order : orders) {
				dc.addOrder(order);
			}			
		}
		if(EntityUtil.contains(entityClass, EntityUtil.UPDATE_TIME))	dc.addOrder(Order.desc(EntityUtil.UPDATE_TIME));
		return dc;
	}	
	/**
	 * <li>说明：不使用查询缓存，获取当前查询器的结果集列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @return 查询结果集
	 */
	public List<T> list(Session session) throws NoSuchFieldException, ParseException{
		return getList(session, false);
	}
	/**
	 * <li>说明：强制使用查询缓存，获取当前查询器的结果集列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @return 查询结果集
	 */
	public List<T> cacheList(Session session) throws NoSuchFieldException, ParseException{
		return getList(session, true);
	}	
	/**
	 * <li>说明：获取当前查询器的不分页结果集列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @param enableCache 是否使用查询缓存
	 * @return 不分页的查询结果集
	 * @throws NoSuchFieldException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<T> getList(Session session, boolean enableCache) throws NoSuchFieldException, ParseException{
		Criteria criteria = null;
		try {
			criteria = getCriteria(session);
			criteria.setCacheable(enableCache);
			return (List<T>)getCriteria(session).list();
		} catch (HibernateException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} catch (NoSuchFieldException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} catch (ParseException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} finally {
			if(criteria != null)	criteria.setCacheable(false);
		}
	}
	/**
	 * <li>说明：不使用查询缓存，获取当前查询器的结果集分页列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @return 查询分页结果集
	 */
	public Page<T> page(Session session) throws NoSuchFieldException, ParseException{
		return getPage(session, false);
	}
	/**
	 * <li>说明：强制使用查询缓存，获取当前查询器的结果集分页列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @return 查询分页结果集
	 */
	public Page<T> cachePage(Session session) throws NoSuchFieldException, ParseException{
		return getPage(session, true);
	}
	/**
	 * <li>说明：获取当前查询器的结果集分页列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param session：hibernate会话连接对象
	 * @param enableCache 是否使用查询缓存
	 * @return Page: 查询分页结果集
	 */	
	@SuppressWarnings("unchecked")
	public Page<T> getPage(Session session, boolean enableCache) throws NoSuchFieldException, ParseException{
		Criteria criteria = null;
		try {
			criteria = getCriteria(session);
			criteria.setCacheable(enableCache);
			int total = ((Integer)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			List<T> list = (List<T>)criteria.setProjection(null).setFirstResult(this.start).setMaxResults(this.limit).list();
			return new Page<T>(total, list);
		} catch (HibernateException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} catch (NoSuchFieldException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} catch (ParseException e) {
			ExceptionUtil.process(e, logger);
			throw e;
		} finally {
			if(criteria != null)	criteria.setCacheable(false);
		}
	}
	/**
	 * <li>说明：过滤掉与业务无关的查询条件，追加逻辑删除条件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return Searcher:当前对象引用
	 * @throws 
	 */
	public QueryCriteria filterInvaildCondition(){
		if (this.whereList == null){
			this.whereList = new ArrayList<Condition>();
		}
		for (int i = this.whereList.size() - 1; i >= 0; i--) {
			Condition c = this.whereList.get(i);
			String name = c.getPropName();
//			过滤掉creator、createTime、updator、updateTime的查询条件
			if(EntityUtil.CREATOR.equals(name) || EntityUtil.CREATE_TIME.equals(name)
					|| EntityUtil.UPDATOR.equals(name) || EntityUtil.UPDATE_TIME.equals(name)){
				this.whereList.remove(i);	
			}
		}
//		过滤逻辑删除记录
		Condition noDelete = new Condition(EntityUtil.RECORD_STATUS, Condition.EQ, Constants.NO_DELETE);
		this.whereList.add(noDelete);
		return this;
	}
	
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public List<Condition> getWhereList() {
		return whereList;
	}
	public void setWhereList(List<Condition> whereList) {
		this.whereList = whereList;
	}
	public Class getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
	public boolean isFilterInvaild() {
		return filterInvaild;
	}
	public void setFilterInvaild(boolean filterInvaild) {
		this.filterInvaild = filterInvaild;
	}
}
