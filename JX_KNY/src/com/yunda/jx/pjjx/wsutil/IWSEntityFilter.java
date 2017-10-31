package com.yunda.jx.pjjx.wsutil;


/**
 * <li>说明：WEB服务查询实体默认过滤
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-15
 * <li>成都运达科技股份有限公司
 * @param <T> 处理类型
 */
public interface IWSEntityFilter<T> {
	
	/**
	 * <li>方法说明： 过滤处理
	 * <li>方法名：handle
	 * @param t 实体
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-15
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void handle(T t);
}
