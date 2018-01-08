<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.wellpartsmanage.wellpartsregister.entity.WellPartsRegister" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>良好配件登记查询</title>
	<script type="text/javascript">
		var empId = '${sessionScope.emp.empid}';
		var empName = '${sessionScope.emp.empname}';
		var ACCEPT_DEPT_TYPE_ORG = '<%=WellPartsRegister.ACCEPT_DEPT_TYPE_ORG%>';
		var ACCEPT_DEPT_TYPE_WH = <%=WellPartsRegister.ACCEPT_DEPT_TYPE_WH%>;
	</script>
	<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
	<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsCheck/PartsCheckRegisterSearch.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>