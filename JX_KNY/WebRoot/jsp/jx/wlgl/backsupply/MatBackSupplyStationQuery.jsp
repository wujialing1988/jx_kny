<jsp:directive.page import="com.yunda.jx.wlgl.backsupply.entity.MatBackSupplyStation"/>
<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>质量反馈单查询</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/backsupply/MatBackSupplyStationQuery.js"></script>
<script type="text/javascript">
	var STATUS_ZC = '<%= MatBackSupplyStation.STATUS_ZC %>'   		// 暂存
	var STATUS_DZ = '<%= MatBackSupplyStation.STATUS_DZ %>'		// 登帐
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>