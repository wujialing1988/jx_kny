<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jcgy_xc</title>
<script type="text/javascript">
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"10":request.getParameter("vehicleType")%>";<%-- 客货类型 --%>
</script>

<script language="javascript" src="<%=ctx %>/jsp/jx/base/jcgy/XC.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>