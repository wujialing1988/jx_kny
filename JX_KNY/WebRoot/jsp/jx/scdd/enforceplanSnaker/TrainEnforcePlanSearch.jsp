<%@page import="com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail" %>
<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>生产计划查询</title>
<script>
//* 计划状态-编制(初始状态) */	
var STATUS_PLAN = '<%=TrainEnforcePlanDetail.STATUS_PLAN%>';
//* 计划状态-已兑现(执行中) */
var STATUS_REDEMPTION = '<%=TrainEnforcePlanDetail.STATUS_REDEMPTION%>';
//* 计划状态-检修完成 */
var STATUS_COMPLETE = '<%=TrainEnforcePlanDetail.STATUS_COMPLETE%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlanSearch.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>