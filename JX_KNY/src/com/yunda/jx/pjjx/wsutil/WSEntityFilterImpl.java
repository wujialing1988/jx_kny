package com.yunda.jx.pjjx.wsutil;


/**
 * <li>说明：抽象WS实体查询过滤实现
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-15
 * <li>成都运达科技股份有限公司
 *	@param <T>
 */
public abstract class WSEntityFilterImpl<T> implements IWSEntityFilter<T>{

	/**
	 * 默认过滤参数
	 */
	public Object[] params = null;
    
    /**
    * <li>方法说明：处理方法
    * <li>方法名称：handle
    * @param t 泛型对象
    */
	@Override
	public abstract void handle(T t);
}
