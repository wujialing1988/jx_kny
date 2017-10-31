<%@include file="/frame/jspf/header.jspf" %> 
<%@page import="com.yunda.jx.base.jcgy.entity.FaultMethod"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理方法维护</title>
<script language="javascript" src="<%=ctx %>/jsp/jx/base/jcgy/FaultMethod.js"></script>
<script type="text/javascript">
	var FLAG_INSERT = '<%= FaultMethod.CONST_STR_FLAG_INSERT %>';
	var FLAG_UPDATE = '<%= FaultMethod.CONST_STR_FLAG_UPDATE %>';
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