/**
 * 
 */
package com.yunda.flow.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;


/**
 * <li>类型名称：com.yunda.flow.util.HttpUtil
 * <li>说明：Servlet常用操作
 * <li>创建人： 赵宏波
 * <li>创建日期：2011-5-4
 * <li>修改人： 
 * <li>修改日期：
 */
public class HttpUtil {
	
	public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
	public static final String DEFAULT_CHARACTER_ENCODING = "utf-8";
	
	/**
	 * <li>方法名：getParameterNameValueMap
	 * <li>@param request
	 * <li>@return
	 * <li>@throws IOException
	 * <li>返回类型：Map<String,String[]>
	 * <li>说明：返回所有参数名称，值的map 
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getParameterNameValueMap(HttpServletRequest request) throws IOException {
		Enumeration<String> names = request.getParameterNames();
		Map<String, String[]> map = new HashMap<String, String[]>();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			if (values != null) {
				if (values[0] != null) {
					if (!"".equals(values[0])) {
						map.put(name, values);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * <li>方法名：serializable
	 * <li>@param request
	 * <li>@param obj
	 * <li>@throws ServletException
	 * <li>@throws IOException
	 * <li>返回类型：void
	 * <li>说明：将表单数据序列化为bean
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void serializable(HttpServletRequest request, Object obj) throws ServletException, IOException {
		try {
			Map<String, String[]> map = HttpUtil.getParameterNameValueMap(request);
			ConvertUtils.register(new SqlTimestampConverter(), java.sql.Timestamp.class); 
			BeanUtils.populate(obj, map);
		} catch (InvocationTargetException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException ile) {
			ile.printStackTrace();
		}
	}
	
}
