<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修标准维护</title>
	<%@include file="/frame/jspf/header.jspf" %>
	<script type="text/javascript">
		var reportType="<%=(null==request.getParameter("reportType"))?"":request.getParameter("reportType")%>"; // 报表类型	
	</script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="reportView.js"></script>
  </head>
  
  <body>
  </body>
</html>
