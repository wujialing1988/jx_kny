package com.yunda.frame.util;

import java.security.MessageDigest;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.yunda.common.BusinessException;

public class LogonUtil {
	
	/**
	 * 
	 * <li>方法名：getPassword
	 * <li>@param password 明文密码
	 * <li>@return 加密后的密码
	 * <li>@throws Exception
	 * <li>返回类型：String
	 * <li>说明：获取加密后的密码
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getPassword(String password) throws Exception {
		try {
			if (password == null) return null;
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(password.getBytes());
			byte digest[] = alg.digest();
			
			if (digest == null) return "";
			else return (new Base64Encoder()).encode(digest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}	
}
