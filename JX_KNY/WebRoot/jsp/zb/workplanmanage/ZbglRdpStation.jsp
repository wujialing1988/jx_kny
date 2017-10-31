<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/vis.jspf" %> 
<%@page import="com.yunda.zb.rdp.zbbill.entity.ZbglRdp" %>
<%@page import="com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode" %>

<%
try{
	String trainTypeShortName = request.getParameter("trainTypeShortName");	// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "SS4B";	// *开发测试默认值
	String trainNo = request.getParameter("trainNo");						// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "0009";							// *开发测试默认值	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>整备作业编制查看</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.css" rel="stylesheet" type="text/css">
 
<script type="text/javascript">
	var NODE_STATUS_UNSTART = '<%= ZbglRdpNode.STATUS_UNSTART %>'		// 未处理
	var NODE_STATUS_GOING = '<%= ZbglRdpNode.STATUS_GOING %>'			// 处理中
	var NODE_STATUS_COMPLETE = '<%= ZbglRdpNode.STATUS_COMPLETE %>'		// 已处理
	
	var PLAN_STATUS_HANDLING = '<%= ZbglRdp.STATUS_HANDLING %>'		// 整备中
	var PLAN_STATUS_HANDLED = '<%= ZbglRdp.STATUS_HANDLED %>'	// 整备完成
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/workplanmanage/ZbglRdpStation.js"></script> 


</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>