<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page import="com.yunda.zb.schedule.entity.ScheduleProposeAndApprove"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调度命令单审批</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var STATUS_DSP = '<%= ScheduleProposeAndApprove.STATUS_DSP%>';
	var STATUS_YSP = '<%= ScheduleProposeAndApprove.STATUS_YSP%>';
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="ScheduleProposeAndApprove.js"></script>
</head>
<body>
</body>
</html>