package com.yunda.flow.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <li>类型名称：com.yunda.flow.util.SpringContextUtil
 * <li>说明：获取Spring Application context
 * <li>创建人： 赵宏波
 * <li>创建日期：2011-5-4
 * <li>修改人： 
 * <li>修改日期：
 */
public class SpringContextUtil {
	
	private static String[] springConfigFiles = new String[] {"classpath:application*.xml"};
	
	/**
	 * <li>方法名：getApplicationContext
	 * <li>@return
	 * <li>返回类型：ApplicationContext
	 * <li>说明：从classpath获取 Application context
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static ApplicationContext getApplicationContext() {
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfigFiles);
		return context;
	}

	/**
	 * <li>方法名：getWebApplicationContext
	 * <li>@param servletCocntext
	 * <li>@return
	 * <li>返回类型：ApplicationContext
	 * <li>说明： 应用从WebContext中获取 Application Context用于注入
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static ApplicationContext getWebApplicationContext(ServletContext servletCocntext) {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletCocntext);
		return wac;
	}
	
	/* *
	 * @param args
	 */
//	public static void main(String[] args) {
//		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//		System.out.println(ctx.getBean("leaveManager") == null);
//	}
	
	
}
