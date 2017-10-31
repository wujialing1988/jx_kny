<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%try{ 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入厂细录</title>
<script language="javascript">
var sourceIdx = '${param.sourceIdx}';
var workItemID = '${param.workItemID}';//工作项ID
var processInstID = '${param.processInstID}';//流程实例ID
var processTaskID = '${param.idx}';//流程任务ID
var rdpIdx = "${param.rdpIdx}";//兑现单主键
var actInstId = '${ param.actInstID}';//活动实例ID
	
//获取当前单位
var orgId = '${sessionScope.org.orgid}';
var orgName = '${sessionScope.org.orgname}';
var orgSeq = '${sessionScope.org.orgseq}';

var empId = '${sessionScope.emp.empid}';
var empname = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/TaskConfirm/JxgcRecheckDetail.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>