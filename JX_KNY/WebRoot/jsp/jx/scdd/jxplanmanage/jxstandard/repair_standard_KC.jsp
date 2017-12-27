<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修标准维护</title>
	<%@include file="/frame/jspf/header.jspf" %>
	<script type="text/javascript">
		var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>"; // 10 货车 20 客车		
	</script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="repair_standard_grid_KC.js"></script>
	<script type="text/javascript" src="repair_standard_grid_time.js"></script>
	<script type="text/javascript" src="repair_standard_tree_KC.js"></script>
	<script type="text/javascript" src="repair_standard_KC.js"></script>
  </head>
  
  <body>
  </body>
</html>
