<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件定额</title>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/partsquota/PartsQuota_chooseType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/partsquota/PartsQuota.js"></script>
<script type="text/javascript">
	var overseaName = '${sessionScope.overseaName}';//段名
	var oversea = '${sessionScope.oversea}';//段id
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