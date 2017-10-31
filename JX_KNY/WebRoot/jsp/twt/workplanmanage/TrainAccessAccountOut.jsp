<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>机车出段(台位图)</title>
<style type="text/css">
	//表单控件隐藏时css样式，用于解决隐藏时有时控件无法显示的bug
	.my-disabled {
		color:gray;cursor:default;
	}
	.my-disabled .x-form-trigger-over {
	  	background-position:0 0 !important;
	  	border-bottom: 1px solid #B5B8C8;
	}
	.my-disabled .x-form-trigger-click{
 	 	background-position:0 0 !important;
  		border-bottom: 1px solid #B5B8C8;
	}
</style>
<%
	String idx = request.getParameter("idx");									// 获取URL传入的参数idx主键
	if (null == idx) idx = "8a8284f24d5f9ba2014d5fbee7a50006";			// *开发测试默认值
	String trainTypeShortName = request.getParameter("trainTypeShortName");		// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "HXN5";		// *开发测试默认值
	String trainNo = request.getParameter("trainNo");							// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "161";								// *开发测试默认值
%>
<script type="text/javascript">
	var idx = '<%= idx %>';
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainAccessAccountOut.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>