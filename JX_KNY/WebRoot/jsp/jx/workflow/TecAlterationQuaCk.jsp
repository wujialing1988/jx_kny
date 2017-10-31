<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<html>
  <head>
    <title>签名页面</title>
    <script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
	<script type="text/javascript">
		var idx = '${ param.idx}';
		var workitemId = '${ param.workItemID}';
		var actInstId = '${ param.actInstID}';
		var tecIdx = '${ param.sourceIdx }';
		var processInstID = '${param.processInstID}';
		var dict = {};//字典数据的存放
		jQuery.ajax({
			url: ctx + "/workTask!getWorkTaskType.action",
			type:"post",
			data:{dictTypeId:"JCZL_FAULT_TYPE"},
			dataType:"json",
			success:function(data){				
				dict = data;
			}
		});
	</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/jx/workflow/TecAlterationQuaCk.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc">
  </body>
</html>