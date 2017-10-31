<%@ page language="java"  pageEncoding="utf-8"%>
<%@page import="com.yunda.base.context.SystemContext"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String userID = SystemContext.getOmEmployee().getUserid();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
    <script type="text/javascript">
    	var basePath = '<%=basePath%>'; 
    	var userID = '<%=userID%>'; 
    </script>
  </head>
  <body>
   	<iframe id="inner_frame" width="100%" height="100%" frameborder='0' src=""></iframe>
   	<script type="text/javascript">
		
		window.onload = function(){
			document.getElementById("inner_frame").src = basePath + '/jsp/twt/html5/index.html?basePath='+basePath+'&userID='+userID;
		}
	</script>
  </body>
</html>
