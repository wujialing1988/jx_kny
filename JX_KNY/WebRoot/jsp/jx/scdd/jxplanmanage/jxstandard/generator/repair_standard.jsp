<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修标准维护</title>
	<%@include file="/frame/jspf/header.jspf" %>
	<script type="text/javascript">
		var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>"; // 10 货车 20 客车  30 柴油发电机组
	</script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="repair_standard_grid<%=(null==request.getParameter("vehicleType"))?"30":request.getParameter("vehicleType")%>.js"></script>
	<script type="text/javascript" src="repair_standard_tree.js"></script>
	<script type="text/javascript" src="repair_standard.js"></script>
  </head>
  
  <body>
  </body>
</html>
