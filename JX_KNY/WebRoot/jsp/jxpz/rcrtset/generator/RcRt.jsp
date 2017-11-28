<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<html>
  <head>
    <title>修程对应修次</title>
	<script type="text/javascript">
		var vehicleType="<%=(null==request.getParameter("vehicleType"))?"10":request.getParameter("vehicleType")%>";<%-- 客货类型 --%>
	</script>
	
	<script type="text/javascript" src="<%=ctx %>/jsp/jxpz/rcrtset/generator/RcRt.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc">
  </body>
</html>