/**
 * <li>文件名：LoginAction.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-12-8
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.action;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.opensymphony.xwork2.interceptor.I18nInterceptor;
import com.yunda.base.BaseAction;
import com.yunda.base.context.SystemContext;
import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.common.BusinessException;
import com.yunda.common.PropertiesUtil;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.AcOperatorrole;
import com.yunda.frame.yhgl.entity.AcRole;
import com.yunda.frame.yhgl.entity.LoginLog;
import com.yunda.frame.yhgl.entity.OmEmpgroup;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmEmporg;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.entity.OmPartyrole;
import com.yunda.frame.yhgl.entity.OmPosition;
import com.yunda.frame.yhgl.manager.AcMenuManager;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.AcOperatorroleManager;
import com.yunda.frame.yhgl.manager.AcRoleManager;
import com.yunda.frame.yhgl.manager.LoginLogManager;
import com.yunda.frame.yhgl.manager.OmEmpgroupManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmEmporgManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.frame.yhgl.manager.OmPartyroleManager;
import com.yunda.frame.yhgl.manager.OmPositionManager;
import com.yunda.util.PurviewUtil;
import com.yunda.util.YDStringUtil;

/**
 * 
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-12-8
 * <li>修改人：
 * <li>修改日期：
 */
@SuppressWarnings("serial")
public class LoginAction extends
		BaseAction<AcOperator, AcOperator, AcOperatorManager> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 应用ID标识
	 */
	private String appId;
	
	/* start 登录信息 */
	private String userId;

	private String passWord;
	
	private String loginJsp = "/login.jsp";

	public String getAppId() {
		return appId.toLowerCase();
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/* end 登录信息 */

	/**
	 * 员工
	 */
	private OmEmployeeManager omEmployeeManager;

	/**
	 * 机构
	 */
	private OmOrganizationManager omOrganizationManager;

	/**
	 * 角色
	 */
	private AcRoleManager acRoleManager;

	/**
	 * 操作员对应角色
	 */
	private AcOperatorroleManager acOperatorroleManager;

	/**
	 * 人员岗位
	 */
	private OmPositionManager omPositionManager;

	/**
	 * 组织对象角色
	 */
	private OmPartyroleManager omPartyroleManager;

	/**
	 * 员工对应机构
	 */
	private OmEmporgManager omEmporgManager;

	/**
	 * 员工对应的工作组
	 */
	private OmEmpgroupManager omEmpgroupManager;
	
	/**
	 * 系统菜单
	 */
	private AcMenuManager acMenuManager;

    /**
     * 登录日志
     */
    private LoginLogManager loginLogManager;
	
	public LoginAction() {
		super();
		navigationMap.put("relogin", loginJsp);
		navigationMap.put("login", loginJsp);
		navigationMap.put("toCardLogin", "/card_login.jsp");
		navigationMap.put("logout", "/exit_system.jsp");
	}

	/**
	 * 
	 * <li>方法名：relogin
	 * <li>
	 * 
	 * @return
	 * <li>
	 * @throws Exception
	 *             <li>返回类型：String
	 *             <li>说明：重新登录
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-12-11
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public String relogin() throws Exception {
        // 获取session中的国际化设置
        Locale locale = (Locale)getRequest().getSession().getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
	    //获取当前登录操作员
        AcOperator acOperator = SystemContext.getAcOperator();
		String url = (String)getSession().getAttribute("reloginJsp");
		getSession().invalidate();
		//注销时，清除SystemContext环境中用户会话及绑定数据信息，刘晓斌2012-8-24修改
		SystemContext.release();
        // 重新设置locale，否则会被清除
        if(locale != null){
            getRequest().getSession().setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE,locale);
        }
		if("true".equalsIgnoreCase(LoginPurviewCheckFilter.USE_WORK_FLOW)){
			//退出BPS
			super.logoutBps();
		}
		//更新登录日志记录
        if(acOperator != null){
            LoginLog log = new LoginLog();
            log.setOperatoridx(acOperator.getOperatorid().toString());
            log.setLoginOutTime(new Date());//注销时间
            log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
            log.setLoginClient(LoginLog.CLIENT_WEB);//web端登录
            this.loginLogManager.saveOrUpdate(log);//更新登录日志
        }
		return renderJSPPage(url);
	}
	
	/**
	 * 
	 * <li>方法名：relogin
	 * <li>
	 * 
	 * @return
	 * <li>
	 * @throws Exception
	 *             <li>返回类型：String
	 *             <li>说明：重新登录
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-12-11
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public String reloginToHomePage() throws Exception {
		getSession().invalidate();
		//注销时，清除SystemContext环境中用户会话及绑定数据信息，刘晓斌2012-8-24修改
		SystemContext.release();
		if("true".equalsIgnoreCase(LoginPurviewCheckFilter.USE_WORK_FLOW)){
			//退出BPS
			super.logoutBps();
		}
//		if(!StringUtils.isBlank(appId)){
//			loginJsp = "/" + appId.toLowerCase() + ".jsp";
//			//LoginPurviewCheckFilter.RELOGIN_JSP = loginJsp;
//			getSession().setAttribute("reloginJsp", loginJsp);
//		}
		getSession().setAttribute("reloginJsp", "/secondlevel.jsp");
		return renderJSPPage("/secondlevel.jsp");
	}

	/**
	 * 
	 * <li>方法名：logout
	 * @throws Exception
	 * <li>返回类型：String
	 * <li>说明：注销
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-12-11
	 * <li>修改人：程梅
	 * <li>修改内容：注销时更新登录日志
	 */
	public String logout() throws Exception {
		try {
		    //获取当前登录操作员
            AcOperator acOperator = SystemContext.getAcOperator();
			getSession().invalidate();
			//注销时，清除SystemContext环境中用户会话及绑定数据信息，刘晓斌2012-8-24修改
			SystemContext.release();
			//退出BPS
			if("true".equalsIgnoreCase(LoginPurviewCheckFilter.USE_WORK_FLOW)){
				//退出BPS
				super.logoutBps();
			}
			//更新登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(acOperator.getOperatorid().toString());
            log.setLoginOutTime(new Date());//注销时间
            log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
            log.setLoginClient(LoginLog.CLIENT_WEB);//web端登录
            this.loginLogManager.saveOrUpdate(log);//更新登录日志
            
			this.ajaxMessage("注销成功！");
		} catch (Exception e) {
			this.ajaxMessage(e.getMessage());
			e.printStackTrace();
		}
		return null;
		//return renderJSPPage("/exit_system.jsp");
	}

	public String toCardLogin() throws Exception {
		return renderJSPPage("/card_login.jsp");
	}

	/**
	 * <li>说明：default.jsp页面轮询调用，将当前页面所属用户即userid传入后台跟session中的操作员id进行比对，
	 *          如果不相同，则将页面返回到登录页去
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void loginChecking() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String userid = getParameter("userid");
			AcOperator acOperator = (AcOperator) getSession().getAttribute(
					LoginPurviewCheckFilter.USERS_SESSION_NAME);
//			System.out.println("当前登录人ID:"+userid + "\t当前Session用户ID："+acOperator.getUserid());
			if(acOperator!=null && acOperator.getUserid()!=null && !acOperator.getUserid().equals(userid)){
				map.put("relogin", true);
			}
			map.put("success", true);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * 
	 * <li>方法名：login
	 * <li>
	 * 
	 * @return
	 * <li>
	 * @throws Exception
	 * <li>返回类型：String
	 * <li>说明：登录
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-12-11
	 * <li>修改人：程梅
	 * <li>修改内容：登录时记录登录日志
	 */
	@SuppressWarnings("unchecked")
	public String login() throws Exception {
		//从Session中获取当前登录用户
		AcOperator acOperator = (AcOperator) getSession().getAttribute(
				LoginPurviewCheckFilter.USERS_SESSION_NAME);
		try{
			if(!StringUtil.isNullOrBlank(appId)){
				String _loginJsp = loginJsp+"?appId="+appId;
				getSession().setAttribute("reloginJsp", _loginJsp);
				System.out.println("appId:"+appId);
				getSession().setAttribute("appId", appId);
			} else {
				this.setAppId("JCJX");//默认为机车检修子系统
				String _loginJsp = loginJsp+"?appId="+appId;
				getSession().setAttribute("reloginJsp", _loginJsp);
				getSession().setAttribute("appId", appId);
			}
	
			if(acOperator!=null && acOperator.getUserid()!=null && !acOperator.getUserid().equals(userId)){
				getSession().invalidate();				
				//注销时，清除SystemContext环境中用户会话及绑定数据信息，刘晓斌2012-8-24修改
				SystemContext.release();
				//退出BPS
				if("true".equalsIgnoreCase(LoginPurviewCheckFilter.USE_WORK_FLOW)){
					//退出BPS
					super.logoutBps();
				}
				if(getSession().isNew()){
					acOperator = null;
				}
			}
			if (acOperator == null) {//未登录或Session丢失，重新登录
				acOperator = new AcOperator();
				acOperator.setUserid(userId);
				acOperator.setPassword(passWord);
			
				entity = acOperator;
				//未填写用户名或密码
				 if(StringUtils.isBlank(userId)){
						getResponse().sendRedirect(
								getRequest().getContextPath() + loginJsp + "?appId="+appId+"&message1=" + YDStringUtil.toUTF8("用户名不能为空"));
						
						return renderJSPPage(loginJsp);
				 }else if(StringUtils.isBlank(passWord)){
					 getResponse().sendRedirect(
								getRequest().getContextPath() + loginJsp + "?appId="+appId+"&message2="+ YDStringUtil.toUTF8("密码不能为空"));
						
						return renderJSPPage(loginJsp);
					 
				 }else{
	
					acOperator = (AcOperator) this.manager.findLoginAcOprator(userId,
							LogonUtil.getPassword(passWord));
					
					if (acOperator == null) {// 非有效用户
		
						getResponse().sendRedirect(
								getRequest().getContextPath() + loginJsp + "?appId="+appId+"&message1=" + YDStringUtil.toUTF8("用户名或密码错误"));
		
						return renderJSPPage(loginJsp);
						
					} else {
                        //新增登录日志记录
                        LoginLog log = new LoginLog();
                        log.setOperatoridx(acOperator.getOperatorid().toString());
                        log.setUserid(userId);
                        log.setOperatorname(acOperator.getOperatorname());
                        log.setLoginInTime(new Date());
                        log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
                        log.setLoginClient(LoginLog.CLIENT_WEB);//web端登录
                        String ip = getRequest().getRemoteHost();
                        if(ip.indexOf(".") == -1 ) ip = InetAddress.getLocalHost().getHostAddress();
//                        log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
                        log.setIp(ip);
                        this.loginLogManager.saveOrUpdate(log);//新增登录日志
                        
                        // 查询该操作员对应的权限
						//首页修改密码按钮是否显示
						boolean changePwdButtonCmmd = Boolean.parseBoolean(PropertiesUtil.getInstance().getPropValue("purviewcmmd", "changePwdButtonCmmd"));
						getSession().setAttribute("changePwdButtonCmmd", changePwdButtonCmmd);
						
						// 查询操作员对应员工信息、机构
						OmEmployee omEmp = this.omEmployeeManager
								.findOmEmployee(acOperator.getOperatorid());
		
						if (omEmp == null) {
							omEmp = new OmEmployee();
							omEmp.setOperatorid(acOperator.getOperatorid());
						} else {
							OmOrganization omOrg = (OmOrganization) this.omOrganizationManager
									.findSingle(" FROM OmOrganization WHERE orgid = (select id.orgid from OmEmporg where id.empid = '"+omEmp.getEmpid()+"')");
		
							// ///////////////////////// 添加登录员工对应机构到Session
							getSession().setAttribute("org", omOrg);
						}
						//设置用户所属段信息(2 表示段)
						if(omEmp!=null){
							if(omEmp.getOrgid()!=null){
								List<OmOrganization> omOrgDeps = omOrganizationManager.findLoginOmOrgList(omEmp.getOrgid(), "oversea");
								if(omOrgDeps!=null&&omOrgDeps.size()>0){
									getSession().setAttribute("orgDep", omOrgDeps.get(0));
								}
								//将人员说在单位的每一级都放在session，以便后面使用
								List<Object[]> empAllOrg = omOrganizationManager.getEmpAllOrg(omEmp.getOrgid()+"");
								for(int i = empAllOrg.size()-1; i >= 0;i--){
									getSession().setAttribute(empAllOrg.get(i)[2] + "", empAllOrg.get(i)[0] + "");
									getSession().setAttribute(empAllOrg.get(i)[2] + "Name", empAllOrg.get(i)[1] + "");
								}
							}
							
						}
						// ///////////////////////// 添加登录员工信息对应机构到Session
						getSession().setAttribute("emp", omEmp);
						
						/** 获取用户所属岗位开始 **/
						OmPosition position = omPositionManager.findByEmpid(omEmp.getEmpid());
						if(position!=null&&!StringUtil.isNullOrBlank(position.getPosiname())){
							getSession().setAttribute("posiname", position.getPosiname());
						}
						/** 获取用户所属岗位结束 **/
		
						// 查询操作员对应所有角色
						List<AcRole> acRoles = this.findAcOperatorRoles(acOperator, omEmp);
						// ///////////////////////// 添加登录操作员对应所有角色到Session
						getSession().setAttribute("roles", acRoles);
						
						if (PurviewUtil.isSuperUsers(acOperator)) {// 超级管理员配置于web.xml
							acOperator.setOperatorname("超级管理员");
						}
					}
				}
//				 getSession().setAttribute("superUsers", "superUsers");
				// 系统超级用户
				if (LoginPurviewCheckFilter.SYS_ADMIN.equals(acOperator
						.getUserid()) || LoginPurviewCheckFilter.SUPER_NAME.equals(acOperator.getUserid())) {
					getSession().setAttribute("superUsers", "superUsers");
				}
				
				// /////////////////////// 添加登录操作员信息到Session
				getSession().setAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, acOperator);
			}
			//super.loginBps(acOperator); //登录BPS工作流引擎
		}catch(Exception ex){
			System.out.println(ex.getMessage()  + "---------Exception");
			if(ex.getMessage().indexOf("JDBC begin failed") > -1 || ex.getMessage().indexOf("Cannot open") > -1){
				getRequest().setAttribute("errorInfo", "数据库连接失败：" + ex.getMessage());
				return renderJSPPage(loginJsp);
			}
			this.addActionMessage(ex.getMessage());
			ex.printStackTrace();
			// 执行失败，返回异常描述
		}
		
		//设置为当前页面登录，登录后转向登录前的URL
		String loginIsCurrent = getParameter("loginIsCurrent");//登录页面是否是top页面
		String currentLogin = (String)getSession().getAttribute("currentLogin");//是否在当前页面登录
		String fromUrl = (String)getSession().getAttribute("fromUrl");//请求前的页面Url
		String loginedclose = getParameter("loginedclose");
		if(StringUtils.isNotBlank(loginedclose) && "true".equals(loginedclose.toLowerCase())){ //登录后关闭
			return renderJSPPage("/closeWin.jsp");
		} else if(StringUtils.isNotBlank(loginIsCurrent) && !"true".equals(loginIsCurrent) 
				&& StringUtils.isBlank(currentLogin) && "true".equals(currentLogin.toLowerCase())
				&& StringUtils.isNotBlank(fromUrl)){
			getResponse().sendRedirect(YDStringUtil.toUTF8(fromUrl));
			return null;
		}
//		getResponse().sendRedirect(getRequest().getContextPath() + "/frame/yhgl/MainFrame.jsp" + (!StringUtil.isNullOrBlank(appId) ? "?appId=" + appId : ""));
		if(StringUtil.isNullOrBlank((String)getSession().getAttribute("appId"))){
			String _loginJsp = loginJsp+"?appId="+appId;
			getSession().setAttribute("reloginJsp", _loginJsp);
			getSession().setAttribute("appId", appId);
		}
		getResponse().sendRedirect(getRequest().getContextPath() + "/frame/yhgl/MainFrame.jsp?appId=" + appId );
		return null;
	}
	
	/**
	 * <li>方法名：findAcOperatorRoles
	 * 
	 * @param acOperator
	 *            登录操作员
	 *            <li>
	 * @param omEmployee
	 *            操作员对应员工信息
	 *            <li>
	 * @return 操作员所有角色
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：List<AcRole>
	 *             <li>说明：获取操作员所有角色
	 *             (包括操作员自身赋予的角色，所在机构赋予的角色、拥有的职务赋予的角色、所在岗位赋予的角色、所在工作组赋予的角色)的集合
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-2-16
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	private List<AcRole> findAcOperatorRoles(AcOperator acOperator,
			OmEmployee omEmployee) throws Exception {

		// 查询操作员自身角色，添加到acRoles
		List<AcOperatorrole> acOperatorroles = this.acOperatorroleManager
				.findAcOperatorRolesByAcOperator(acOperator);

		if (omEmployee.getEmpid() != null) {
			// OmEmpposition omEmpPosition = new OmEmpposition();
			// omEmpPosition.getOmEmployee().setEmpid(omEmployee.getEmpid());

			// 查询员工对应的岗位
			List<OmPosition> empPositions = this.omPositionManager
					.findOmPositionByEmp(omEmployee);

			// 查询职务与角色关系，添加到acRoles
			List<OmPartyrole> partyRoles1 = this.omPartyroleManager
					.findOmPartyRolesByEmpPositions(empPositions, "duty");

			// 查询员工对应的机构
			List<OmEmporg> empOrgs = this.omEmporgManager
					.findEmpOrgsByEmp(omEmployee);

			// 查询所在机构角色，添加到acRoles
			List<OmPartyrole> partyRoles2 = this.omPartyroleManager
					.findOmPartyRolesByOmEmporgs(empOrgs);

			// 查询组织与角色关系，添加到acRoles
			List<OmPartyrole> partyRoles3 = this.omPartyroleManager
					.findOmPartyRolesByEmpPositions(empPositions, "position");

			// 查询员工对应的工作组
			List<OmEmpgroup> empGroups = this.omEmpgroupManager
					.findEmpGroupsByEmp(omEmployee);

			// 查询组织与角色关系，添加到acRoles
			List<OmPartyrole> partyRoles4 = this.omPartyroleManager
					.findOmPartyRolesByEmpGroups(empGroups);

			return this.acRoleManager.findAllRoles(acOperatorroles,
					partyRoles1, partyRoles2, partyRoles3, partyRoles4);
		}
		return this.acRoleManager.findAllRoles(acOperatorroles);
	}
	/**
	 * <li>说明：接收各种终端（手机、平板、工位终端）的登录系统请求，返回用户验证结果；若验证成功则session有效，并绑定上下文用户信息。<br/>
	 * 返回JSON数据{"success":true};true登录成功，false登录失败
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void terminalLogin() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", false);
		
		//如果是终端是PDA，传递PDA，如果是PAD，传递PAD
		String loginClient = getRequest().getParameter("loginClient");
		
		//登录位置
		String loginLocation = getRequest().getParameter("loginLocation");
		
		
		try {
			if(StringUtils.isBlank(userId) || StringUtils.isBlank(passWord)){
				map.put("errMsg", "用户名或密码不能为空");
				return;
			}
			
			String encryptPassword = LogonUtil.getPassword(passWord);
			AcOperator ac = (AcOperator) this.manager.findLoginAcOprator(userId, encryptPassword);
			
			if (ac == null || !userId.equals(ac.getUserid()) || !encryptPassword.equals(ac.getPassword()) ) {// 非有效用户
				map.put("errMsg", "用户名或密码错误");				
				return;
			} else {
			    //新增登录日志记录
                LoginLog log = new LoginLog();
                log.setOperatoridx(ac.getOperatorid().toString());
                log.setUserid(userId);
                log.setOperatorname(ac.getOperatorname());
                log.setLoginInTime(new Date());
                log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
                
                //判断是否传递
                if(StringUtils.isNotBlank(loginClient)){
                	log.setLoginClient(loginClient);//终端登录
                }else {
                	log.setLoginClient(LoginLog.CLIENT_PAD);//PAD登录
				}
                //判断是否传递
                if(StringUtils.isNotBlank(loginLocation)){
                	log.setLoginLocation(loginLocation);//登录位置
                }
                
                String remoteHost = getRequest().getRemoteHost();
                log.setIp(remoteHost) ;
//                log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
                this.loginLogManager.saveOrUpdate(log);//新增登录日志
                
				if (PurviewUtil.isSuperUsers(ac)) {// 超级管理员配置于web.xml，不允许超级管理员通过该接口进行登录
					map.put("errMsg", "系统不允许该用户从这个终端入口进行登录");
					return;
				}				
				// 查询操作员对应员工信息、机构
				OmEmployee omEmp = this.omEmployeeManager.findOmEmployee(ac.getOperatorid());

				if (omEmp == null) {
					omEmp = new OmEmployee();
					omEmp.setOperatorid(ac.getOperatorid());
				} else {
					OmOrganization omOrg = (OmOrganization) this.omOrganizationManager
							.findSingle(" FROM OmOrganization WHERE orgid = (select id.orgid from OmEmporg where id.empid = '"+omEmp.getEmpid()+"')");
					getSession().setAttribute("org", omOrg);
                    map.put("org", omOrg) ;
				}
				//设置用户所属段信息(2 表示段)
				if(omEmp != null){
					if(omEmp.getOrgid()!=null){
						List<OmOrganization> omOrgDeps = omOrganizationManager.findLoginOmOrgList(omEmp.getOrgid(), "oversea");
						if(omOrgDeps!=null&&omOrgDeps.size()>0){
							getSession().setAttribute("orgDep", omOrgDeps.get(0));
                             map.put("orgDep", omOrgDeps.get(0)) ;
						}
						//将人员说在单位的每一级都放在session，以便后面使用
						List<Object[]> empAllOrg = omOrganizationManager.getEmpAllOrg(omEmp.getOrgid()+"");
						for(int i = empAllOrg.size()-1; i >= 0;i--){
							getSession().setAttribute(empAllOrg.get(i)[2] + "", empAllOrg.get(i)[0] + "");
							getSession().setAttribute(empAllOrg.get(i)[2] + "Name", empAllOrg.get(i)[1] + "");
						}
					}
				}
				getSession().setAttribute("emp", omEmp);
                map.put("emp", omEmp) ;
                
                // iPad二维码扫描规格
                String qRCodeRule = JXConfig.getInstance().getQRCodeRule();
                map.put("qRCodeRule", qRCodeRule);
			}
//			 添加登录操作员信息到Session
			getSession().setAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, ac);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * <li>说明：接收各种终端（手机、平板、工位终端）的退出系统请求，使session失效，并清除上下文用户信息。<br/>
	 * 返回JSON数据{"success":true};true退出成功，false退出失败
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void terminalLogout() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", false);
		try {
		    //获取当前登录操作员
            AcOperator acOperator = SystemContext.getAcOperator();
//			删除登录操作员信息到Session
			getSession().removeAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);
			SystemContext.release();
			//更新登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(acOperator.getOperatorid().toString());
            log.setLoginOutTime(new Date());//注销时间
            log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
            log.setLoginClient(LoginLog.CLIENT_PAD);//PAD登录
            this.loginLogManager.saveOrUpdate(log);//更新登录日志
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>心跳检测，用于测试客户端和服务器连接是否正常
	 * <li>创建人：何涛
	 * <li>创建日期：2015-03-17
	 * <li>修改人：伍佳灵
	 * <li>修改日期：2016-5-30
	 * <li>修改内容：增加检修系统PAD应用更新信息获取功能（用于客户端自动更新）
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void heartbeat() throws JsonMappingException, IOException {
        Map<String, Object> map = null;
        try {
            String os = getRequest().getParameter("os");
            String version = getRequest().getParameter("version");
            // 获取检修系统PAD应用更新信息
            Map<String, Object> updateInfo = this.getUpdateInfo(os, version);
            if (null == updateInfo) {
                map = new HashMap<String, Object>();
            } else {
                map = updateInfo;
            }
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取检修系统PAD应用更新信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param os PAD操作系统类型：ios,android
     * @param currentVersion 检修系统PAD版本号
     * @return 检修系统PAD应用更新信息
     * @throws IOException 
     */
    private Map<String, Object> getUpdateInfo(String os, String currentVersion) throws IOException {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
            p.load(is);
            is.close();
        } catch (Exception e) {
            return null;
        } finally {
            if (null != is) {
                is.close();
            }
        }
        // 系统部署配置时的最新PAD应用的版本号
        String latestVersion = p.getProperty(os + "_version");
        // 系统部署配置PAD应更新时链接的地址
        String url = p.getProperty(os + "_url");
        if (null == latestVersion || null == url) {
            return null;
        }
        // 当前版本号
        BigInteger currentVersionNum = new BigInteger(currentVersion.replaceAll("[^0-9]", ""));
        // 最新版本号
        BigInteger latestVersionNum = new BigInteger(latestVersion.replaceAll("[^0-9]", ""));
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("isupdate", latestVersionNum.compareTo(currentVersionNum) == 1);
        result.put("url", url);
        return result;
    }
    
	/**
	 * <li>说明：接收客户终端请求，返回success:true表示网络连接正常(服务器可正常访问)，用于心跳包定时请求，定期检测客户端与服务端的连接状态是否正常
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	public void enableConnection() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		JSONUtil.write(this.getResponse(), map);
	}
	
	
	
	 /**
     * <li>说明：关闭注销PDA登录
     * <li>创建人：刘国栋
     * <li>创建日期：2016-5-11-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 操作成功与否
     */
    public void closePDA() throws JsonMappingException, IOException {
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", false);
		try {
		    //获取当前登录操作员
            AcOperator acOperator = SystemContext.getAcOperator();
//			删除Session中的登录操作员信息
			getSession().removeAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);
			SystemContext.release();
			//更新注销关闭时间到登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(acOperator.getOperatorid().toString());
            log.setLoginOutTime(new Date());//注销时间
            log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
            log.setLoginClient(LoginLog.CLIENT_PDA);//PDA登录
            loginLogManager.saveOrUpdate(log);//更新登录日志
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
    
    /**
     * <li>说明：修改语言设置
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void changeLanguage() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String local = getRequest().getParameter("local");
            if(!StringUtil.isNullOrBlank(local)){
                String loc[] = local.split("_");
                Locale locale = new Locale(loc[0],loc[1]);
                getRequest().getSession().setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale);
            }
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
	
	public OmEmployeeManager getOmEmployeeManager() {
		return omEmployeeManager;
	}

	public void setOmEmployeeManager(OmEmployeeManager omEmployeeManager) {
		this.omEmployeeManager = omEmployeeManager;
	}

	public OmOrganizationManager getOmOrganizationManager() {
		return omOrganizationManager;
	}

	public void setOmOrganizationManager(
			OmOrganizationManager omOrganizationManager) {
		this.omOrganizationManager = omOrganizationManager;
	}

	public AcRoleManager getAcRoleManager() {
		return acRoleManager;
	}

	public void setAcRoleManager(AcRoleManager acRoleManager) {
		this.acRoleManager = acRoleManager;
	}

	public AcOperatorroleManager getAcOperatorroleManager() {
		return acOperatorroleManager;
	}

	public void setAcOperatorroleManager(
			AcOperatorroleManager acOperatorroleManager) {
		this.acOperatorroleManager = acOperatorroleManager;
	}

	public OmPositionManager getOmPositionManager() {
		return omPositionManager;
	}

	public void setOmPositionManager(OmPositionManager omPositionManager) {
		this.omPositionManager = omPositionManager;
	}

	public OmPartyroleManager getOmPartyroleManager() {
		return omPartyroleManager;
	}

	public void setOmPartyroleManager(OmPartyroleManager omPartyroleManager) {
		this.omPartyroleManager = omPartyroleManager;
	}

	public OmEmporgManager getOmEmporgManager() {
		return omEmporgManager;
	}

	public void setOmEmporgManager(OmEmporgManager omEmporgManager) {
		this.omEmporgManager = omEmporgManager;
	}

	public OmEmpgroupManager getOmEmpgroupManager() {
		return omEmpgroupManager;
	}

	public void setOmEmpgroupManager(OmEmpgroupManager omEmpgroupManager) {
		this.omEmpgroupManager = omEmpgroupManager;
	}

	public AcMenuManager getAcMenuManager() {
		return acMenuManager;
	}

	public void setAcMenuManager(AcMenuManager acMenuManager) {
		this.acMenuManager = acMenuManager;
	}

    
    public LoginLogManager getLoginLogManager() {
        return loginLogManager;
    }

    
    public void setLoginLogManager(LoginLogManager loginLogManager) {
        this.loginLogManager = loginLogManager;
    }
    
}