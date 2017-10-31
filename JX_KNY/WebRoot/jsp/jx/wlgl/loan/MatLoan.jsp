<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager"%>

<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>借出归还</title>
<script type="text/javascript">
	var objList = <%=JSONUtil.write(MatTypeListManager.getMatType())%>
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
 <script language="javascript" src="<%=ctx%>/jsp/jx/wlgl/loan/MatLoanBack.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/wlgl/loan/MatLoan.js"></script> 

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>