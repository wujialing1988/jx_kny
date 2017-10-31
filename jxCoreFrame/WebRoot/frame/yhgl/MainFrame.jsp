<%@ page import="com.yunda.frame.common.JXConfig" %>
<%@ page import="com.yunda.frame.common.SubSystem" %>
<%@ page import="com.yunda.frame.common.SubSysInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yunda.frame.util.StringUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.yunda.base.filter.LoginPurviewCheckFilter"%>
<%@ page import="com.yunda.frame.yhgl.entity.AcFunction"%>


<%
	//应用程序根目录
	String ctx = request.getContextPath();
	String superUsers = (String)session.getAttribute("superUsers");
	
	String browserLang = request.getLocale().toString().toLowerCase();
	
	/******根据请求参数中的appid（默认为JCJX），获取appid指定的子系统名称****/
	String appId = (String)session.getAttribute("appId");//目标子系统id
	String appname = JXConfig.getInstance().getAppName();
	String systemType = JXConfig.getInstance().getSystemType();
	String queryString = request.getQueryString(); //获得请求参数
	if(!StringUtil.isNullOrBlank(queryString)&&queryString.toUpperCase().startsWith("APPID")){
		SubSystem sys = JXConfig.getInstance().getSubSystem(); //读取JXConfig配置文件，获取子系统信息
		if(sys!=null){
			List<SubSysInfo> list = sys.getSubSysInfo();
			//遍历子系统
			for(SubSysInfo info : list){
				if(appId.equals(info.getAppid().toUpperCase())){
					appId = info.getAppid().toUpperCase();
					appname = info.getAppname(); //当前URL参数中的APPID与配置文件中某个子系统匹配，则获取该子系统名称
				}
			}
		}
	}
	/**************************************************************/
%>
<%
	try {
%>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title><%=appname %></title>
	<link href="<%=ctx%>/frame/yhgl/css/top.css" rel="stylesheet" type="text/css" />
	<link href="<%=ctx%>/frame/yhgl/css/main-page.css" rel="stylesheet" type="text/css" />
	<link href="<%=ctx%>/frame/resources/ext-3.4.0/resources/css/ext-all.css" rel="stylesheet" type="text/css">
	<link href="<%=ctx%>/frame/resources/css/iconExt.jsp" rel="stylesheet" type="text/css">
	<script language="javascript">
		var ctx = "<%=ctx%>";
		var superUsers = "<%=superUsers%>";
		var _tempAppId = "<%=appId%>";
		var systemType = "<%=systemType%>";
		
		var FUNC_TYPE_REPORT = '<%= AcFunction.FUNC_TYPE_REPORT %>';		// 应用功能类型 - 报表功能
		
		var browserLang = '<%=browserLang %>' ;
		
	</script>
	<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-base.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-all.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-<%=browserLang %>.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/i18n/i18n-lang-<%=browserLang %>.js"></script>
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/testtree1.js"></script>
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/testtree.js"></script>
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/NewPassWord.js"></script>
	
	<script language="javascript" src="<%=ctx%>/frame/resources/js/MyJson.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-yunda.js"></script>
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/AcPersonalDeskLayout.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/cmps/editgrid.js"></script>
	
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/js/login/top.js"></script>
	<script type="text/javascript" language="javascript" src="<%=ctx%>/frame/yhgl/js/login/main.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/MyExt.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-yunda.js"></script>
	<script type="text/javascript" src="<%=ctx%>/frame/baseapp/message/OnlineMessage.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/yhgl/MainFrame.js"></script>
	<script language="javascript">
		Ext.onReady(function() {
			Ext.namespace("login");
			/**
			 * 启动轮询，每5秒一次检查当前登录人与session中的用户是否一致，不一致则跳转到登陆页面
			 */
			login.failCount = 0;
			var delay = new Ext.util.DelayedTask(function(){
				login.checking = {
					run: function(){
						if(login.failCount > 2){
                			Ext.TaskMgr.stop(login.checking);
                			return;
            			}
            			
            			Ext.Ajax.request({
			                url: ctx + '/login!loginChecking.action?userid=${sessionScope.users.userid}',
					        success: function(response, options){
					            var result = Ext.util.JSON.decode(response.responseText);
			                    if(result.success != true){         //操作失败
			                        login.failCount += 1;
			                        return;
			                    }
			                    if(login.failCount > 0) login.failCount -= 1;
			                    var msgs = result.relogin;
			                    if(msgs == null || msgs.length < 1)    return;
						        if(msgs==true){
						        	Ext.TaskMgr.stop(login.checking);
					        		var _t = ctx + '/login.jsp' + "?appId="+_tempAppId;
					        		window.location.href = _t;
						        }                      
					        },
					        //请求失败后的回调函数
					        failure: function(response, options){
			                    login.failCount += 1;
					        }            
			            });
					},
        			interval: 10000 //10秒一次
				}
				Ext.TaskMgr.start(login.checking);
			});
			//开关，设为true时，在IE同一会话中，登录的第2个用户会将头一个用户给注销并返回登录界面
			if(true){
				delay.delay(3000);
			}
		});
	</script>
</head>

<body class="body">
	<div id="north">
		<!-- 页面头部 -->
		<table id="sysTitle" class="sysTitle" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top" rowspan="2">
					<!-- 标题文字：系统名称 -->
					<div class="sysTitleText"><%= appname%></div>
				</td>
				<td valign="top">
					<!-- 右上角按钮 -->
					<div class="sysTitleBar">
						<div class="sysTitleBarLeft"></div>
						<!-- 修改密码 -->
						<div id="sysTitleBarEditPassword" class="sysTitleBarEditPassword"
							onMouseOut="btnShow('sysTitleBarEditPassword','sysTitleBarEditPassword')"
							onMouseOver="btnShow('sysTitleBarEditPassword','sysTitleBarEditPassword_mov');"
							title="点击修改您当前的登录密码"
							onClick="newPwd.showWin()"></div>
						<!-- 切换用户 -->
						<div class="sysTitleBarSplit"></div>
						<div id="sysTitleBarLoginOut" class="sysTitleBarLoginOut"
							onMouseOut="btnShow('sysTitleBarLoginOut','sysTitleBarLoginOut')"
							onMouseOver="btnShow('sysTitleBarLoginOut','sysTitleBarLoginOut_mov');"
							title="点击退出当前用户"
							onClick="Ext.Msg.confirm('消息' ,'确认【注销】当前用户:【${sessionScope.users.operatorname}】？' ,function(btn){if(btn=='yes'){document.location.replace('${pageContext.request.contextPath}/login!relogin.action');}});"></div>
						<div class="sysTitleBarSplit"></div>
						<!-- 设置主页 -->
						<div id="acPersonalDeskLayout" class="acPersonalDeskLayout"
							onMouseOut="btnShow('acPersonalDeskLayout','acPersonalDeskLayout')"
							onMouseOver="btnShow('acPersonalDeskLayout','acPersonalDeskLayout_mov');"
							title="点击修改您当前的设置主页"
							onClick="AcPersonalDeskLayout.showWin()"></div>
						<!-- 隐藏/显示标题栏 -->
						<div class="sysTitleBarSplit"></div>
						<div id="sysTitleBarHiddenHeader" class="sysTitleBarHiddenHeader"
							onMouseOut="btnShow('sysTitleBarHiddenHeader','sysTitleBarHiddenHeader')"
							onMouseOver="btnShow('sysTitleBarHiddenHeader','sysTitleBarHiddenHeader_mov');"
							title="点击可以隐藏标题栏" onClick="Ext.getCmp('sysMainNorth').collapse();"></div>
						<div class="sysTitleBarRight"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<!-- 右下角用户名 class="sysTitleUserInfo showText"-->
					<div class="sysTitleUserInfo showText">${sessionScope.users.operatorname}&nbsp;${sessionScope.org.orgname}&nbsp;${sessionScope.posiname}</div>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
<%
	//AcOperator acOperator = (AcOperator) session.getAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);//从Session中获取当前登录用户
		//System.out.println(acOperator.getOperatorname());
		//OmEmployee omEmployee = (OmEmployee) session.getAttribute("emp"); //从Session中获取当前登录人员
		//System.out.println(omEmployee.getEmpcode()+":"+omEmployee.getEmpname());
		//OmOrganization organization = (OmOrganization) session.getAttribute("org"); //从Session中获取当前登录人员所属机构
		//System.out.println(organization.getOrgcode()+":"+organization.getOrgname());
		//List<AcRole> acRoles = (List<AcRole>) getSession().setAttribute("roles"); //从Session中获取当前登录人员的全部角色
	} catch (Exception e) {
		e.printStackTrace();
	}
%>