<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统角色管理</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/basesManagement/i18n-lang-SysRole.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/AllotPowerTree.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/AllotPower.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/AllotPowerMain.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/SysRole.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>