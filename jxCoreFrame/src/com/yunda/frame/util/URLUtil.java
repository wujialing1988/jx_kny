package com.yunda.frame.util;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 对于URL的一些常用的工具方法
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class URLUtil {
	/** 描述web-inf物理全路径字符串 */
	private static String WEBINF_REAL_PATH = null;
	/** 描述web应用根目录物理全路径字符串 */
	private static String WEBAPP_REAL_PATH = null;

	/**
	 * <li>说明：返回web-inf目录的物理路径，前提是该类必须运行在一个web应用中
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return String:描述web-inf物理全路径字符串
	 * @throws 
	 */
	public static final String getWebInfRealPath() {
		if (null == WEBINF_REAL_PATH) {
			String classFilePath = URLUtil.class.getResource("/").getPath();
			WEBINF_REAL_PATH = classFilePath.substring(0, classFilePath.indexOf("classes"));
		}
		return WEBINF_REAL_PATH;
	}

	/**
	 * <li>说明：返回web应用目录的物理路径，前提是该类必须运行在一个web应用中
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return String: 描述web应用根目录物理全路径字符串
	 * @throws 抛出异常列表
	 */
	public static final String getWebappRealPath() {
		if (null == WEBAPP_REAL_PATH) {
			String webappPath = getWebInfRealPath();
			WEBAPP_REAL_PATH = webappPath.substring(0, webappPath.indexOf("WEB-INF"));
		}
		return WEBAPP_REAL_PATH;
	}
}
