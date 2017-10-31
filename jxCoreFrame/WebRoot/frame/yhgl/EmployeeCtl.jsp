<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员控制</title>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroupTreeWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/OrganizationTreeWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/EmployeeCtl.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>