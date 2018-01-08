<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@ page import="com.yunda.jx.jxgc.producttaskmanage.entity.DisposeOpinion"%>
<html>
  <head>
    <title>提票-车间调度审核</title>
    <script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
	<script type="text/javascript">
		var idx = '${ param.idx}';
		var workitemId = '${ param.workItemID}';
		var actInstId = '${ param.actInstID}';
		var rdpIdx = '${ param.rdpIdx }';
		var processInstID = '${param.processInstID}';	
		//var allowTP = "${ param.tp }";
		<%-- 页面上必须有newTp这个参数,根据值提了票才会修改相关数据状态 --%>
		//var newTp = "${ param.newTP }";
		//var tpType = '${ param.tpType}';
		//var tpTypeName = '${ param.tpTypeName}';
		var sourceIdx = '${param.sourceIdx}';//故障提票主键
		//sourceIdx = '8a8284ba4124761b0141258f26b9007c';
		var signType = "<%=DisposeOpinion.SIGN_TYPE_FAULT_VERTIFY%>";
	</script>
	<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/base/lcgd/DisposeOpinionView.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/base/lcgd/DisposeOpinion.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/base/lcgd/FaultInfo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/TaskConfirm/TP_SchedulingExamine.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc">
  </body>
</html>