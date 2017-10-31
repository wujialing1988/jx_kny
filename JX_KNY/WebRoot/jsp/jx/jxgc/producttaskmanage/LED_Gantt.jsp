<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/EdoProject.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>甘特图查看</title>
<script language="javascript">
var rdpIdx = '<%=request.getParameter("rdpIDX")%>';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/producttaskmanage/LED_Gantt.js"></script>
</head>
<body>
<div id="toolbar"></div>
<div id="view"></div>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>