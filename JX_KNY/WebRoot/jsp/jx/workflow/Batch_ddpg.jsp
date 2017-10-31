<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%--@include file="/jsp/jx/include/MultiSelect.jspf" --%> 
<html>
  <head>
    <title>批量调度派工</title>	
    <%--<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>--%>
    <script type="text/javascript" src="jquery-1.4.2.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/jx/workflow/Batch_ddpg.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc" onselectstart="return false">
  </body>
</html>