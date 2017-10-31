<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ 
	//应用程序根目录
	String ctx = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支持</title>
<link href="<%=ctx%>/frame/resources/ext-3.4.0/resources/css/ext-all.css" rel="stylesheet" type="text/css">
<link href="<%=ctx%>/frame/resources/css/iconExt.jsp" rel="stylesheet" type="text/css">
<script language="javascript">
var ctx = "<%=ctx%>";
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-base-debug.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-all-debug.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-zh_cn.js"></script>
<script language="javascript" src="<%=ctx%>/frame/yhgl/js/login/support.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>