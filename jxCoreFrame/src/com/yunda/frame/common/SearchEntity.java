package com.yunda.frame.common;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.criterion.Order;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类用于实体类查询条件包装
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-8-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class SearchEntity<T> {
	/** 包含查询条件的实体类对象  */
	private T entity;
	/** 分页查询开始行数  */
	private Integer start;
	/** 该页最大记录总数  */
	private Integer limit;
	/** 描述排序规则的数组  */
	private Order[] orders;
	
	/**
	 * <li>说明：空构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public SearchEntity(){}
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-22
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public SearchEntity(T entity){
		setEntity(entity);
	}
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-22
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public SearchEntity(T entity, Order[] orders){
		setEntity(entity);
		if (orders == null) {
			Order[] orderArray = new Order[ 1];
			orderArray[ 0 ] = Order.desc("updateTime");
		}
		setOrders(orders);		
	}	
	/**
	 * <li>说明：带参数构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param entity 包含查询条件的实体类对象
	 * @param start 分页查询开始行数
	 * @param limit 该页最大记录总数
	 * @param orders 排序规则的数组
	 */	
	public SearchEntity(T entity, Integer start, Integer limit, Order[] orders){
		setEntity(entity);
//		setStart(start);
//		setLimit(limit);
		setStart(start == null || start < 0 ? 0 : start);
		setLimit(limit == null || limit < 0 ? Page.PAGE_SIZE : limit);
		//如果没有设置排序，则默认设置按idx主键降序排列
		if (orders == null) {
			Order[] orderArray = new Order[ 1];
			orderArray[ 0 ] = Order.desc("updateTime");
		}
		setOrders(orders);
	}
	
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Order[] getOrders() {
		return orders;
	}
	public void setOrders(Order[] orders) {
		this.orders = orders;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
}
