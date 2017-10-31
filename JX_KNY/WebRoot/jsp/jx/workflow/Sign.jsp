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
		var rdpIdx = '${ param.rdpIdx }';
		var processInstID = '${param.processInstID}';
		var sxIdx = '<%=com.yunda.jx.jxgc.zb.rdp.entity.ZbRdp.TYPE_TJ %>';
		var ctrl = '${param.ctrl}';
	<%--
		var jt6 ="<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.FAULT_JT_6%>";
		var jt28 ="<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.FAULT_JT_28%>";
	--%>
		var allowTP = "${ param.tp }";
		<%-- 页面上必须有newTp这个参数,根据值提了票才会修改相关数据状态 --%>
		var newTp = "${ param.newTP }";
		var tpType = '${ param.tpType}';
		var tpTypeName = '${ param.tpTypeName}';
		var tbar = null;
		var outOrg = "${ not empty param.out ? 'block' : 'none' }";<%-- 标识机车出段 --%>

		if(allowTP){
			tbar = [{
				text : '故障提票',
				iconCls : 'wrenchIcon',
				handler : function(){
					var trainTypeIdx = Sign.baseForm.find('name','trainTypeIdx')[0].getValue();
					var typeNameAndTrainNo = Sign.baseForm.find('name','trainType')[0].getValue();
					var trainTypeName = typeNameAndTrainNo.split('|')[0];
					var trainNo = typeNameAndTrainNo.split('|')[1];
					parent.Sxtp.showTpWin(trainTypeIdx, trainNo, trainTypeName, tpTypeName, tpType);
				}
			}];
		}

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
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/jx/workflow/Sign.js"></script>
  </head>
  <body style="margin:10px;" class="x-window-mc">
  </body>
</html>