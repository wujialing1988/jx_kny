package com.yunda.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.yunda.base.BaseActionSupport;

/**
 * 
 * <li>类型名称：
 * <li>说明：异常拦截处理器，用户提交请求，服务器端的Action进行处理；当处理过程出现异常；
 * <li>本拦截器扑获Action抛出的所有类型的异常，根据用户请求的日志，系统根据情况返回到
 * <li>用户的请求页面或专门的异常显示页面
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人： 
 * <li>修改日期：
 */
@SuppressWarnings("serial")
public class ExceptionInterceptor extends AbstractInterceptor {
	@SuppressWarnings("unused")
	private final String REQUESTMETHOD = "requestMethod";
	@SuppressWarnings("unused")
	private final String ACTIONCLASS = "actionClass";
	private final String GOLBERROR = "globerror";
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName()); 
	/**
	 * 
	 */
	@SuppressWarnings("finally")
	public String intercept(ActionInvocation invocation) throws Exception{
		String result = null;
		String errorMessage = null;  
		String uri = null;		
		
		if(!(invocation.getAction() instanceof BaseActionSupport)){
			try{
				result = invocation.invoke();
			}catch(DataAccessException ex){
				errorMessage = "数据库操作失败！";
				logger.error(errorMessage, ex);
			}catch(NullPointerException ex){
				errorMessage = "调用了未经初始化的对象或者是不存在的对象！";
				logger.error(errorMessage, ex);
			}catch(IOException ex){
				errorMessage = "IO异常！";
				logger.error(errorMessage, ex);
			}catch(ClassNotFoundException ex){
				errorMessage = "指定的类不存在！";
				logger.error(errorMessage, ex);
			}catch(ArithmeticException ex){
				errorMessage = "数学运算异常！";
				logger.error(errorMessage, ex);
			}catch(ArrayIndexOutOfBoundsException ex){
				errorMessage = "数组下标越界!";
				logger.error(errorMessage, ex);
			}catch(IllegalArgumentException ex){
				errorMessage = "方法的参数错误！";
				logger.error(errorMessage, ex);
			}catch(ClassCastException ex){
				errorMessage = "类型强制转换错误！";
				logger.error(errorMessage, ex);
			}catch(SecurityException ex){
				errorMessage = "违背安全原则异常！";
				logger.error(errorMessage, ex);
			}catch(CannotCreateTransactionException ex){
				errorMessage = "操作数据库异常，连接失败！";
				logger.error(errorMessage, ex);
			}catch(SQLException ex){
				errorMessage = "操作数据库异常！";
				logger.error(errorMessage, ex);
			}catch(NoSuchMethodError ex){
				errorMessage = "方法末找到异常！";
				logger.error(errorMessage, ex);
			}catch(InternalError ex){
				errorMessage = "Java虚拟机发生了内部错误";
				logger.error(errorMessage, ex);
			}catch(Exception ex){
				errorMessage = ex.getMessage();
				logger.error(errorMessage, ex);
			}finally{				
				if(result == null){
					result = GOLBERROR;
				}
				return result;
			}
			
		}else{
			BaseActionSupport obj = (BaseActionSupport)invocation.getAction();
			HttpServletRequest request = obj.getRequest();
			
			try{
				result = invocation.invoke();
			}catch(DataAccessException ex){
					errorMessage = "数据库操作失败！";
					logger.error(errorMessage, ex);
				}catch(NullPointerException ex){
					errorMessage = "调用了未经初始化的对象或者是不存在的对象！";
					logger.error(errorMessage, ex);
				}catch(IOException ex){
					errorMessage = "IO异常！";
					logger.error(errorMessage, ex);
				}catch(ClassNotFoundException ex){
					errorMessage = "指定的类不存在！";
					logger.error(errorMessage, ex);
				}catch(ArithmeticException ex){
					errorMessage = "数学运算异常！";
					logger.error(errorMessage, ex);
				}catch(ArrayIndexOutOfBoundsException ex){
					errorMessage = "数组下标越界!";
					logger.error(errorMessage, ex);
				}catch(IllegalArgumentException ex){
					errorMessage = "方法的参数错误！";
					logger.error(errorMessage, ex);
				}catch(ClassCastException ex){
					errorMessage = "类型强制转换错误！";
					logger.error(errorMessage, ex);
				}catch(SecurityException ex){
					errorMessage = "违背安全原则异常！";
					logger.error(errorMessage, ex);
				}catch(SQLException ex){
					errorMessage = "操作数据库异常！";
					logger.error(errorMessage, ex);
				}catch(NoSuchMethodError ex){
					errorMessage = "方法末找到异常！";
					logger.error(errorMessage, ex);
				}catch(InternalError ex){
					errorMessage = "Java虚拟机发生了内部错误";
					logger.error(errorMessage, ex);
				}catch(Exception ex){
					errorMessage = ex.getMessage();
					logger.error(errorMessage, ex);
				}finally{
					request.getSession().setAttribute(SystemConstants.errorFlag,"Error");
				}
			
			try{
				if( errorMessage != null ){
					uri = getMethodByRequestURI(request);
					if(obj.getNavigationMap().get(uri) != null){																
						result = obj.renderJSPPage(obj.getNavigationMap().get(uri).toString());
						obj.addActionError(errorMessage);
					}else{			
						result = GOLBERROR;
						// add the exception message for ajax
					}
					obj.ajaxMessage(errorMessage, "text/xml");
				}
			}catch(Exception e){
				e.printStackTrace();
				if(obj.getNavigationMap().get(uri) != null){							
					result = obj.renderJSPPage(obj.getNavigationMap().get(uri).toString());
					obj.addActionError(e.getMessage());
				}else{
					result = GOLBERROR;
				}
				obj.ajaxMessage(errorMessage, "text/xml");
			}finally{
				/*if(result == null){
					result = GOLBERROR;
				}*/
				return result;
			}
		}
	}
	
	/**
	 * 
	 * <li>方法名：getMethodByRequestURI
	 * <li>@param request
	 * <li>@return 返回请求的方法名
	 * <li>返回类型：String
	 * <li>说明：根据用户请求的url，获取用户请求的方法名
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	private String getMethodByRequestURI(HttpServletRequest request){		
		String uri = request.getRequestURI();
		if(uri.indexOf("!") != -1){
			uri = uri.split("!")[1];
			uri = uri.replace(".action", "");
		}
		
		return uri;
		
	}
}
