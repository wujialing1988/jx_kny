<%@include file="/frame/jspf/header.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>软件管理</title>
<style type="text/css">
	#main {
		width: 221;
		position: relative;
		top:30%;
		left:43%;
	}
	#main div {
		margin-bottom: 10px;
	}
	a {
		text-decoration: none;
	}
	img {
		border-style: none
	}
	
</style>
</head>
<body>
	<div id="main">
		<!-- 下载安装chrome浏览器开始 -->
		<div id="Chrome_Setup">
			<a href="${pageContext.request.contextPath}/frame/installfiles/Chrome_Setup.exe">
            	<img src="${pageContext.request.contextPath}/frame/installfiles/images/installchrome.gif" alt="下载安装chrome浏览器"/>
       	 	</a>
		</div>
		<!-- 下载安装chrome浏览器结束 -->
		<!-- 下载安装SilverLight开始 -->
		<div id="silverlight">
			<a href="${pageContext.request.contextPath}/frame/installfiles/silverlight.exe">
	            <img src="${pageContext.request.contextPath}/frame/installfiles/images/installsilverlight.gif" alt="下载安装Silverlight"/>
	        </a>
		</div>
		<!-- 下载安装SilverLight结束 -->
		<!-- 下载安装视频监控插件开始 
		<div id="WebComponents">
			<a href="${pageContext.request.contextPath}/frame/installfiles/WebComponents.exe">
            	<img src="${pageContext.request.contextPath}/frame/installfiles/images/installvideoplugin.gif" alt="下载安装视频监控插件"/>
       	 	</a>
		</div>
		-->
		<!-- 下载安装视频监控插件结束 -->
		<!-- 下载安装消息推送客户端开始 -->
		<div id="WebComponents">
			<a href="${pageContext.request.contextPath}/frame/installfiles/MessagePropelling.exe">
            	<img src="${pageContext.request.contextPath}/frame/installfiles/images/messagepropelling.gif" alt="下载消息推送客户端"/>
       	 	</a>
		</div>
		<!-- 下载安装消息推送客户端结束 -->
	</div>
	<script type="text/javascript" src="<%= ctx %>/frame/resources/jquery/jquery.js"></script>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>