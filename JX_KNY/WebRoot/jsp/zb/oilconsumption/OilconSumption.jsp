<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机油消耗台账</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/zb/oilconsumption/OilconSumption.js"></script>
</head>
<body>
</body>
</html>