<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@ page import="com.yunda.frame.yhgl.entity.AcFunction"%>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>应用功能管理</title>
<script language="javascript">
	var FUNC_TYPE_PAGE = '<%= AcFunction.FUNC_TYPE_PAGE %>';			// 应用功能类型 - 前端功能
	var FUNC_TYPE_REPORT = '<%= AcFunction.FUNC_TYPE_REPORT %>';		// 应用功能类型 - 报表功能
	var FUNC_TYPE_SERVICE = '<%= AcFunction.FUNC_TYPE_SERVICE %>';		// 应用功能类型 - 后台服务
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/basesManagement/i18n-lang-SysFunction.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/SysFunction.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>