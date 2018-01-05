<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作组管理</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/basesManagement/i18n-lang-WorkGroup.js"></script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/basesManagement/i18n-lang-Organization.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDutyWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/EmployeeWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroup_Form.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroup_List.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroup_Tab.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroup_Tree.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkGroup.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>