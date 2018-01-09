<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var empID = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<title>编辑信息维护</title>
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/PassengerTransport/i18n-lang-OperationSafetyRecordQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/employee/OmEmployeeMultSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/passenger/Operationsafetyrecord/OperationSafetyRecordQuery.js"></script><!-- 运行安全记录基本信息 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>