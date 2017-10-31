<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>业务字典管理</title>
<script language="javascript" src="<%=ctx %>/frame/yhgl/SysEosDictEntry.js"></script><!-- 业务字典明细 -->
<script language="javascript" src="<%=ctx %>/frame/yhgl/SysEosDictType.js"></script> <!-- 业务字典分类 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>