<%@ page import="com.yunda.frame.common.JXConfig" %>
<%@ page import="com.yunda.frame.common.SubSystem" %>
<%@ page import="com.yunda.frame.common.SubSysInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="com.yunda.frame.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ctx = request.getContextPath(); //应用程序根目录

String basePath =request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ctx+"/";

String appFooter = JXConfig.getInstance().getAppFooter(); //版权信息
/******根据请求参数中的appid（默认为JCJX），获取appid指定的子系统名称****/
String urlAppParam = "JCJX"; 
String appId = "JCJX"; //目标子系统id
String appname = JXConfig.getInstance().getAppName();
String queryString = request.getQueryString(); //获得请求参数
if(!StringUtil.isNullOrBlank(queryString)&&queryString.toUpperCase().startsWith("APPID")){
	int index = queryString.indexOf("&");//查找第2参数标识
	if(index == -1) urlAppParam = queryString.toUpperCase(); //从URL参数中截取appid=xxx
	else urlAppParam = queryString.substring(0,index).toUpperCase(); //从URL参数中截取appid=xxx
	urlAppParam = urlAppParam.toUpperCase().replace("APPID=",""); //移除参数名称，只留下参数值
	SubSystem sys = JXConfig.getInstance().getSubSystem(); //读取JXConfig配置文件，获取子系统信息
	if(sys!=null){
		List<SubSysInfo> list = sys.getSubSysInfo();
		//遍历子系统
		for(SubSysInfo info : list){
			if(urlAppParam.equals(info.getAppid().toUpperCase())){
				appId = info.getAppid().toUpperCase();
				appname = info.getAppname(); //当前URL参数中的APPID与配置文件中某个子系统匹配，则获取该子系统名称
				
			}
		}
	}
}
/**************************************************************/

String userErrMsg = request.getParameter("message1");
String pwdErrMsg = request.getParameter("message2");

// 国际化
 ResourceBundle bundle = ResourceBundle.getBundle("i18n", request.getLocale());
%>
<html>
  	<head>
  		<title><s:text name="login.title"></s:text></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="<%=ctx %>/frame/yhgl/css/login.css" />
		<script language="javascript">
			var ctx = "<%=ctx%>";
		</script>
		<!-- jQuery 2.2.3 -->
<script src="<%=ctx %>/pages/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
		<script type="text/javascript" language="javascript" src="<%=ctx %>/frame/yhgl/js/login/login.js"></script>
		<script type="text/javascript">
			// 切换中英文
			function changeLanguage(localStr){
				//alert(local);
				$.ajax({
					url: ctx + "/login!changeLanguage.action",
					data:{local: localStr},
					type:"post",
					dataType:"json",
					success:function(data){
						window.location.reload();
					}
				});
			}
		</script>
  	</head>

	<body>
		<!-- 页面头部 -->
		<div id="header">
			<div class="headerLeft">
				<div class="headerText"><s:text name="common.sysName"></s:text></div>
			</div>
			<!-- 头部右上角按钮 -->
			<div class="headerRight">
				<!-- 支持按钮 -->
				<div class="headerSupportBtn" title="点击获取系统所需安装的插件" onblur="this.className='headerSupportBtn_mov';" onmousemove="this.className='headerSupportBtn_mov'" onmouseout="this.className='headerSupportBtn'" onclick="showSupport();"></div>

				<!-- 中间的竖分割线 -->
				<div class="headerBtnCutting"></div>

				<!-- 关于按钮 -->
				<div class="headerAboutBtn" title="点击查看系统关于信息" onblur="this.className='headerAboutBtn_mov';" onmousemove="this.className='headerAboutBtn_mov'" onmouseout="this.className='headerAboutBtn'" onclick="showAbout();"></div>
			</div>
		</div>

		<!-- 页面中间（主内容区域） -->
		<div id="content" align="center">

			<form id="loginForm" name="loginForm" method="POST" action="<%=ctx %>/login!login.action">
				<!-- 隐藏域 -->
				<input type="hidden" id="appId" name="appId" value="<%= appId %>"/>

				<div class="loginDiv">
					<!-- ico -->
					<div class="icoDiv">
						<%--
						<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="144" height="156">
							<param name="movie" value="<%=ctx %>/images/login/login_ico.swf" />
							<param name="quality" value="high" />
							<embed src="<%=ctx %>/images/login/login_ico.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="144" height="156"></embed>
						</object>
						  --%>
					</div>

					<!-- 登录表单 -->
					<div class="fromDiv">
						<div class="loginInputDiv">
							<input type="text" id="userIdInput" name="userId" class="textInput" title="请输入您的用户名" tabindex="1" onfocus="this.className='textInput_ov';" onblur="this.className='textInput';" onmousemove="this.className='textInput_ov'" onmouseout="this.className='textInput'" tabindex="1"/>
						</div>
						<div class="loginInputDiv">
							<input type="password" id="passWordInput" name="passWord" class="textInput" onfocus="this.className='textInput_ov';" onblur="this.className='textInput';" onmousemove="this.className='textInput_ov'" onmouseout="this.className='textInput'" title="请填写您的密码" tabindex="2"/>
						</div>

						<!-- 提交和重置按钮 -->
						<div class="loginBtnDiv">
							<!-- 提交按钮 -->
							<div id="subBtn" class="subBtn" onclick="loginSubmit();" xtype="button" onMouseOut="btnShow('subBtn','subBtn')" onMouseOver="btnShow('subBtn','subBtn_mov');" title="请点击登录系统"></div>

							<!-- 重置按钮 -->
							<div id="resetBtn" class="resetBtn" onclick="loginReset();" xtype="button" onMouseOut="btnShow('resetBtn','resetBtn')" onMouseOver="btnShow('resetBtn','resetBtn_mov');" title="点击清空用户名、密码"></div>
						</div>

					</div>

					<!-- 提示信息 -->
					<!-- 这里通过变量来决定是否显示该信息 -->
					<div class="msgDiv" align="left">
						<div id="msgIco"></div>
						<div class="msgText" id="megText">
						<% if((userErrMsg != null && !"".equals(userErrMsg) && !"null".equals(userErrMsg.toUpperCase()))|| (pwdErrMsg != null && !"".equals(pwdErrMsg) && !"null".equals(pwdErrMsg.toUpperCase()))) {
								if(userErrMsg != null && !"".equals(userErrMsg) && !"null".equals(userErrMsg.toUpperCase()) ) { 
						%>
									<script type="text/javascript" language="javascript">
										document.getElementById("msgIco").className = "msgIco";
									</script>
									账号或密码输入错误！
							<% }
							} %>		
						</div>			
					</div>

				</div>
			</form>
		
		</div>

		<!-- 页面底部 -->
		<div id="footer" align="center">
			 <%--<a href="<%=basePath%>login!changeLanguage.action?local=zh_CN">中文</a> <a href="<%=basePath%>login!changeLanguage.action?local=en_US">English</a><br/>--%>
			<s:text name="login.copyright"></s:text>
			 <!-- <a href="#" onclick="changeLanguage('zh_CN');">中文</a> <a href="#" onclick="changeLanguage('en_US');">English</a> -->
		</div>
		<script type="text/javascript" language="javascript">
			document.getElementById('userIdInput').focus();
		</script>
	</body>
</html>