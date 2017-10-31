<%@ page import="com.yunda.frame.common.JXConfig" %>
<%@ page import="com.yunda.frame.common.SubSystem" %>
<%@ page import="com.yunda.frame.common.SubSysInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yunda.frame.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ctx = request.getContextPath(); //应用程序根目录
// 从后台加密的参数经过请求后会自动解密,要对获取到的参数再次加密;
String queryParams =java.net.URLEncoder.encode(request.getParameter("queryParams"), "UTF-8");
%>
<html>
  	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="<%=ctx %>/frame/yhgl/css/login.css" />
		<script language="javascript">
			var ctx = "<%=ctx%>";
		</script>
  	</head>
  	<script language="javascript">
   // 提交登录表单方法
   function loginSubmit(){
   		loginForm.submit();
   }
</script>
	<body onload="loginSubmit()">
		<!-- 页面中间（主内容区域） -->
		<div id="content" align="center">
			<form id="loginForm" name="loginForm" method="POST" action="<%=ctx %>/asynchronousLogin!asynchronousLogin.action">
				<!-- 隐藏域 -->
				<input type="hidden" id=queryParams name="queryParams" value="<%= queryParams %>"/>
			</form>
		</div>
	</body>
</html>