/**
 * <li>文件名：PurviewUtil.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-29
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.util;

import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.frame.yhgl.entity.AcOperator;

/**
 * <li>类型名称：
 * <li>说明：权限工具类
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-29
 * <li>修改人： 
 * <li>修改日期：
 */
public class PurviewUtil {

	/**
	 * 
	 * <li>方法名：isSuperUsers
	 * <li>@param acOperator 操作员
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：是否是超级用户
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-4-29
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static boolean isSuperUsers(AcOperator acOperator) {
		try {
			if (acOperator.getUserid().equals(
					LoginPurviewCheckFilter.SUPER_NAME) 
					|| acOperator.getUserid().equals(LoginPurviewCheckFilter.SYS_ADMIN)) {
				return true;
			}
		/*	if (!acOperator.getPassword().equals(
					LoginPurviewCheckFilter.SUPER_PASSWORD)) {
				return false;
			}*/
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}