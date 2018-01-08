<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var out="<%=PartsOutsourcing.PARTS_STATUS_WW%>";//委外
	var back="<%=PartsOutsourcing.PARTS_STATUS_YFH%>";//已返回
	var takeOverType="<%=PartsOutsourcing.TAKE_OVER_TYPE_WH%>";//接收部门--库房
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>委外配件回段</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
    <script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingBack.js"></script>
</head>
<body>
</body>
</html>