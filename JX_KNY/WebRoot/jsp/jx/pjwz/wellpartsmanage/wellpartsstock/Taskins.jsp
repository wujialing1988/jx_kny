<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.Taskins" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>立体仓库操作指令生成</title>
</head>
<script type="text/javascript">
	var EXEC_FLAG_WZX = '<%= Taskins.CONST_STR_EXEC_FLAG_WZX %>'		// 未执行
	var EXEC_FLAG_YZX = '<%= Taskins.CONST_STR_EXEC_FLAG_YZX %>'		// 已执行
	var EXEC_FLAG_ZX  = '<%= Taskins.CONST_STR_EXEC_FLAG_ZX %>'		// 正在执行
	var EXEC_FLAG_ZTZX = '<%= Taskins.CONST_STR_EXEC_FLAG_ZTZX %>'		// 暂停执行
	
	
</script>
<body>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/wellpartsmanage/wellpartsstock/Taskins.js"></script>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>