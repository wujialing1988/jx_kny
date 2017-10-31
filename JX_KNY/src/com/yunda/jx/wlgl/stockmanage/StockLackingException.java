/**
 * 
 */
package com.yunda.jx.wlgl.stockmanage;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：消耗配件库存不足异常
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 上午11:38:29
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class StockLackingException extends RuntimeException {

	private static final long serialVersionUID = -4441631365947055307L;

	public StockLackingException() {
	}

	/**
	 * @param message
	 */
	public StockLackingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StockLackingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StockLackingException(String message, Throwable cause) {
		super(message, cause);
	}

}
