<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构人员管理</title>
<script language="javascript" src="<%=ctx %>/frame/yhgl/PositionWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/EmployeeWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDuty_Form.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDuty_List.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDuty_Tab.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDuty_Tree.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDuty.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>