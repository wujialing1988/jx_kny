<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<html>
  <head>
    <title>处理意见页面</title>
    <script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
	<script type="text/javascript">
		var idx = '${ param.idx}';
		var workitemId = '${ param.workItemID}';
		var actInstId = '${ param.actInstID}';
		var rdpIdx = '${ param.rdpIdx }';
		var processInstID = '${param.processInstID}';	
		var allowTP = "${ param.tp }";
		<%-- 页面上必须有newTp这个参数,根据值提了票才会修改相关数据状态 --%>
		var newTp = "${ param.newTP }";
		var tpType = '${ param.tpType}';
		var tpTypeName = '${ param.tpTypeName}';
		var sourceIdx = '${param.sourceIdx}';//故障提票主键
		sourceIdx = '8a8284ba4124761b0141258f26b9007c';
		var signType = '';
	</script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/jxgc/DisposeOpinionCombo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/base/lcgd/DisposeOpinion.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc">
  </body>
</html>