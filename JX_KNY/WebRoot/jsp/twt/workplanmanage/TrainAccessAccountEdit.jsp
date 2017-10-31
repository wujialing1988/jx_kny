<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%@page import="com.yunda.frame.common.JXConfig" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>机车入段编辑</title>
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
	if (null == idx) idx = "8a8283a7545f9b20015460778cba0003";			// *开发测试默认值								
	String trainTypeIDX = request.getParameter("trainTypeIDX");							// 获取URL传入的参数车型主键
	if (null == trainTypeIDX) trainTypeIDX = "207";			// *开发测试默认值
	String trainTypeShortName = request.getParameter("trainTypeShortName");		// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "SS4B";		// *开发测试默认值
	String trainNo = request.getParameter("trainNo");							// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "0091";								// *开发测试默认值
%>
<script type="text/javascript">
    var TRAINTOGO_ZB = '<%=TrainAccessAccount.TRAINTOGO_ZB%>';		
	var trainTypeIDX = '<%= trainTypeIDX %>';
	var idx = '<%= idx %>';
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainAccessAccountEdit.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>