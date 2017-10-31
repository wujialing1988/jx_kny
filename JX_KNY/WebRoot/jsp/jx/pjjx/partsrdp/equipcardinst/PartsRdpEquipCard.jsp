<jsp:directive.page import="com.yunda.jx.pjjx.partsrdp.equipcardinst.entity.PartsRdpEquipCard"/>
<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机务设备工单</title>
<script type="text/javascript">
	var STATUS_WCL = '<%=PartsRdpEquipCard.STATUS_WCL%>';		// 设备工单状态 - 未处理
	var STATUS_YCL = '<%=PartsRdpEquipCard.STATUS_YCL%>';		// 设备工单状态 - 已处理
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/equipcardinst/PartsRdpEquipDI.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/equipcardinst/PartsRdpEquipCard.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>