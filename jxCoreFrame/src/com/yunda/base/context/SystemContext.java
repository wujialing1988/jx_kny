package com.yunda.base.context;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;

/**
 * <li>标题: CoreFrame
 * <li>说明：框架上下文环境,提供和层次容器无关的运行期数据访问。
 * 提供接口访问用户与Web服务器会话过程的常用数据信息，如HttpSeesion中的用户、组织机构、权限等。
 * 便于在各层次进行调用（如业务层、数据访问层获取操作员、组织等）。
 * 不建议直接使用该类，在业务层、数据访问层等要获取该类的实例对象应该通过SystemContext的接口访问才能保证线程安全。
 * 已包含信息：key：org员工对应机构，key：orgDep用户所属部门（段），key：emp员工对象，key：users操作员对象(LoginPurviewCheckFilter.USERS_SESSION_NAME)
 * <li>版权: Copyright (c) 2008 运达科技公司
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-23
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * @author 运达科技公司
 * @version 1.0
 */
public class SystemContext {
	/** key：org员工对应机构  */
	public static final String KEY_ORG = "org";
	/**  key：emp员工对象  */
	public static final String KEY_EMP = "emp";
	/**  key：orgDep用户所属部门（段）  */
	public static final String KEY_ORGDEP = "orgDep";
	/** 隶属每个线程的局部变量，使得每个用户线程的数据保持隔离，保证线程安全 */
	private static final ThreadLocal<UserSession> threadLocal = new ThreadLocal<UserSession>();
	/** 日志工具 */
	private static Logger logger = Logger.getLogger(SystemContext.class.getName());

	/**
	 * <li>说明：将当前请求中的用户信息保存到当前线程的用户会话对象中
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 */		
	public static void setSessionInfo(HttpSession session){
		getUserSeesion().setWebAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, session.getAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME));
		getUserSeesion().setWebAttribute(KEY_ORG, session.getAttribute(KEY_ORG));
		getUserSeesion().setWebAttribute(KEY_EMP, session.getAttribute(KEY_EMP));
		getUserSeesion().setWebAttribute(KEY_ORGDEP, session.getAttribute(KEY_ORGDEP));
	}
	/**
	 * <li>说明：获取当前线程的登录操作员对象
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return AcOperator 操作员对象
	 */		
	public static AcOperator getAcOperator(){
		Object obj = getUserSeesion().getWebAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);
		if(obj == null)	return null;
		return (AcOperator)obj;
	}
	/**
	 * <li>说明：在当前线程上下文中绑定操作员对象，该方法目前只考虑针对工位终端的业务调用场景（禁止随意调用）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param AcOperator operator 操作员对象
	 * @return void
	 * @throws 抛出异常列表
	 */
	public static void setAcOperator(AcOperator operator){
		getUserSeesion().setWebAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, operator);
	}
	/**
	 * <li>说明：在当前线程上下文中绑定人员员对象，该方法目前只考虑针对工位终端的业务调用场景（禁止随意调用）
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return OmEmployee 人员对象
	 */
	public static void setOmEmployee(OmEmployee employee){
		getUserSeesion().setWebAttribute(KEY_EMP, employee);	
	}
	/**
	 * <li>说明：获取当前线程的人员
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return OmEmployee 人员对象
	 */
	public static OmEmployee getOmEmployee(){
		Object obj = getUserSeesion().getWebAttribute(KEY_EMP);
		if(obj == null)	return null;
		return (OmEmployee)obj;		
	}	
	/**
	 * <li>说明：获取当前线程的人员所属部门（段）
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return OmOrganization 用户所属部门（段）
	 */
	public static OmOrganization getOrgDep(){
		Object obj = getUserSeesion().getWebAttribute(KEY_ORGDEP);
		if(obj == null)	return null;
		return (OmOrganization)obj;		
	}
	/**
	 * <li>说明：获取当前线程的人员对应的组织机构
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return OmOrganization 组织结构
	 */		
	public static OmOrganization getOmOrganization(){
		Object obj = getUserSeesion().getWebAttribute(KEY_ORG);
		if(obj == null)	return null;
		return (OmOrganization)obj;		
	}
	/**
	 * <li>说明：获取当前线程的用户会话对象，若不存在则新建用户会话对象返回
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return UserSession 用户会话对象
	 */	
	public static UserSession getUserSeesion(){
		UserSession userSession = threadLocal.get();
		if (userSession == null) {
			userSession = new UserSession();
			threadLocal.set(userSession);
			logger.info("当前线程ID" + Thread.currentThread().getId() + "：新建用户会话上下文环境");
		}
		return userSession;
	}
	/**
	 * <li>说明：释放资源
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 */	
	public static void release(){
		UserSession userSession = threadLocal.get();
		if (userSession != null) {
			userSession.release();
			threadLocal.remove();
			logger.info("当前线程ID" + Thread.currentThread().getId() + "：释放用户会话上下文环境");
		}		
	}
}
