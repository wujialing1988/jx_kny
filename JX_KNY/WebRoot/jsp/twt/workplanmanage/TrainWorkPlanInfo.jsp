<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/EdoProject.jspf"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>查看机车信息(台位图)</title>
<%
	String trainTypeIDX = request.getParameter("trainTypeIDX");				// 获取URL传入的参数车型主键
	if (null == trainTypeIDX) trainTypeIDX = "207G";				// *开发测试默认值
	String trainTypeShortName = request.getParameter("trainTypeShortName");	// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "SS4G";	// *开发测试默认值
	String trainNo = request.getParameter("trainNo");						// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "0146";							// *开发测试默认值	
%>
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

<script type="text/javascript">
	var trainTypeIDX = '<%= trainTypeIDX %>';
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
	
	var STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>';					// 状态-未启动
	var STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>';		// 状态-处理中
</script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainWorkPlanInfo.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>