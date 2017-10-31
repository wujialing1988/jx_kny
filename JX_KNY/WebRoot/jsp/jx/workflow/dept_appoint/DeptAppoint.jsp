<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<html>
  <head>
    <title>调度派工</title>
	<script type="text/javascript">
		var idx = '${ param.idx}';
		var workitemId = '${ param.workItemID}';
		var actInstId = '${ param.actInstID}';
		var sourceIdx = '${ param.sourceIdx}';
		var processInstID = '${ param.processInstID}';
		var showTPInfo = "${ param.showInfo }";
		var orgTeam = '${ sessionScope.treamName}';
	</script>
    <script type="text/javascript" src="<%=ctx %>/jsp/jx/workflow/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="DeptAppoint.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc" onselectstart="return false">
  </body>
</html>