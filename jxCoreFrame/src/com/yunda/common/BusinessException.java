package com.yunda.common;

/**
 * 
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人：
 * <li>修改日期：
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
	/**
	 * 
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public BusinessException(String frdMessage) {
		super(createFriendlyErrMsg(frdMessage));
	}

	/**
	 * 
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public BusinessException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public BusinessException(Throwable throwable, String frdMessage) {
		super(throwable);
	}

	/**
	 * 
	 * <li>方法名：createFriendlyErrMsg
	 * <li>
	 * 
	 * @param msgBody
	 *            <li>
	 * @return
	 *            <li>返回类型：String
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	private static String createFriendlyErrMsg(String msgBody) {

		String prefixStr = "";
		String suffixStr = "";

		StringBuffer friendlyErrMsg = new StringBuffer("");

		friendlyErrMsg.append(prefixStr);
		friendlyErrMsg.append(msgBody);
		friendlyErrMsg.append(suffixStr);

		return friendlyErrMsg.toString();
	}
}
