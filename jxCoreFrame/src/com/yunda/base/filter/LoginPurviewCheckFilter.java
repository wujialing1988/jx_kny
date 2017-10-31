/**
 * <li>文件名：LoginPurviewCheckFilter.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-12-8
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.base.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;

/**
 * 
 * <li>类型名称：
 * <li>说明：验证是否登录的过滤器
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-12-8
 * <li>修改人： 
 * <li>修改日期：
 */
public class LoginPurviewCheckFilter implements Filter {
	public static String SUPER_NAME;    //超级用户名
	public static String SUPER_PASSWORD;//超级用户密码
	public static String SYS_ADMIN;//超级用户名，AcOperator
	
	public static String USERS_SESSION_NAME ="users";
	//public static String USERS_SESSION_DISMODULEFUN ="disModuleFun"; //用户禁用的功能
	public static String MAIN_JSP;
	public static String RELOGIN_JSP;
	public static String USE_WORK_FLOW = "false";//是否使用流程引擎
	
	/**
	 * 登录前允许访问的url
	 */
	public static Map<String, String> urlMap;
	@SuppressWarnings("unused")
	private String disallowUrl = "/disallow.jsp";
		
	public LoginPurviewCheckFilter() {
		super();
	}

	public void init(FilterConfig config) throws ServletException {
		SUPER_NAME = config.getInitParameter("superName");
		SUPER_PASSWORD = config.getInitParameter("superPassword");
		SYS_ADMIN = config.getInitParameter("sysAdmin");
		if(config.getInitParameter("useWorkFlow") != null){
			USE_WORK_FLOW = config.getInitParameter("useWorkFlow");
		}

		MAIN_JSP = config.getInitParameter("mainJsp");
		RELOGIN_JSP = config.getInitParameter("reloginJsp");
		if(RELOGIN_JSP == null){
			System.out.println("登陆的JSP地址未配置！");
		}
		urlMap = new HashMap<String, String>();
		
		String beforeLoginUrl = config.getInitParameter("beforeLoginUrl");
		if(beforeLoginUrl == null){
			System.out.println("请配置登陆前允许访问的URL，多个URL可用逗号间隔！");
		}
		String [] urls = beforeLoginUrl.split(",");
		for (String url : urls) {
			url = url.trim();
			if(! "".equals(url)){
				urlMap.put(url, url.toLowerCase());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		//设置登录页面地址（因有多个登录页面，根据不同用户进行登录页面设置）-----
		String reloginJsp = RELOGIN_JSP;
		if(httpRequest.getSession().getAttribute("reloginJsp") != null){
			reloginJsp = (String) httpRequest.getSession().getAttribute("reloginJsp");
		}
		httpRequest.getSession().setAttribute("reloginJsp", reloginJsp);
		//设置登录页面地址（因有多个登录页面，根据不同用户进行登录页面设置）-----
		
		String contextPath = httpRequest.getContextPath();
		String strPath = httpRequest.getRequestURL().toString();
		
		strPath = strPath.substring(strPath.indexOf(contextPath) + contextPath.length());		
		if(strPath.indexOf("?") != -1){
			strPath = strPath.substring(0,strPath.indexOf("?"));
		}
		
		//从Session中获取当前登录用户
		AcOperator acOperator = (AcOperator) httpRequest.getSession().getAttribute(
				LoginPurviewCheckFilter.USERS_SESSION_NAME);
		if(acOperator == null){//未登录
			if(urlMap.get(strPath) == null){
				String fromMobile = StringUtil.nvlTrim(httpRequest.getParameter("_fromMobile"), "false");
				boolean isFromMobile = Boolean.valueOf(fromMobile);
				if(isFromMobile){
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("success", false);
					map.put("_sessionStatus", "offline");
					JSONUtil.write(httpResponse, map);
					return;
				}
				
				String currentLogin = httpRequest.getParameter("currentLogin");
				StringBuffer url = new StringBuffer();
				url.append(contextPath + disallowUrl);
				url.append("?checkRight=false&message=还未登录或会话失效，请登录！");
				if(StringUtils.isNotBlank(currentLogin) && "true".equals(currentLogin.toLowerCase())){
					//设置为当前页面登录，登录后转向登录前的URL
					httpRequest.getSession().setAttribute("currentLogin", currentLogin);
					httpRequest.getSession().setAttribute("fromUrl", httpRequest.getHeader("Referer"));
				}
				httpResponse.sendRedirect(url.toString());
				return;
			}else{
				if(strPath.endsWith(contextPath + reloginJsp) == true){//登录提示信息
					String message = httpRequest.getParameter("message");
					if(message != null && ! message.equals("")){
						httpRequest.setAttribute("message", message);
					}
				}
			}
		}else{//已登录
			if(StringUtils.isNotBlank(USE_WORK_FLOW) && "true".equalsIgnoreCase(USE_WORK_FLOW)){
				try {
					//清空当前页面登录，登录后转向首页
					httpRequest.getSession().setAttribute("currentLogin", null);
					httpRequest.getSession().setAttribute("fromUrl", null);
//					2012-11-29：liuxb修改，解决在系统中第一次打开流程图报异常的问题（session无效）
//					//设置BPS当前活动用户
//					Object bpsSerobj = BeanUtils.invokePrivateMethod("com.eos.workflow.api.BPSServiceClientFactory", "getLoginManager", null);
//					//登录BPS
//					Class clazz = bpsSerobj.getClass();
//					Method m = clazz.getDeclaredMethod("login",HttpServletRequest.class,String.class,String.class);
//					m.invoke(bpsSerobj, new Object[]{httpRequest, acOperator.getUserid(), acOperator.getOperatorname()});
//					//设置BPS当前活动用户
//					BeanUtils.invokePrivateMethod(bpsSerobj, "setCurrentUser", new String[]{acOperator.getUserid(), acOperator.getOperatorname()});
				} catch (Exception ex) {
					System.out.println("未使用BPS时，不用管该错误" + ex.getMessage());
				}
			}
			
			if(strPath.endsWith(".action") == true){//Action需要验证功能权限
				String keyUrl = strPath.substring(0,strPath.lastIndexOf(".action"));
				if(keyUrl.lastIndexOf("/") != -1){
					keyUrl = keyUrl.substring(keyUrl.lastIndexOf("/")+1);
				}
				/*用户禁用的功能
			 	Object obj = httpRequest.getSession().getAttribute(USERS_SESSION_DISMODULEFUN);
				if(obj != null){
					Map<String,String> disModuleFunMap = (Map<String,String>)(obj);
					for (String key : disModuleFunMap.keySet()) {
						System.out.println("key:"+key);
					}
					if(disModuleFunMap.containsKey(keyUrl)){//用于事后控制
						String message = "您没有<" + disModuleFunMap.get(keyUrl)+ ">权限，请与管理员联系！";
						String url = contextPath + this.disallowUrl + "?message="+message;
						httpResponse.sendRedirect(YDStringUtil.toUTF8(url));
						return;
					}
				}*/
				String action = keyUrl;
				if(action.indexOf("!") != -1){//获得Action
					action = action.substring(0, action.indexOf("!"));
				}
//				httpRequest.setAttribute(PurviewTag.PURVIEW_ACTION_NAME, action);//设置访问的action名
				httpRequest.setAttribute("PURVIEW_ACTION", action);//设置访问的action名
			}
		}
		//将Session中的用户相关信息设置到SystemContext上下文用户会话环境中
		SystemContext.setSessionInfo(httpRequest.getSession());
		filterChain.doFilter(httpRequest,httpResponse);
	}
	
	public void destroy() {

	}
}