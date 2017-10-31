<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<%@page import="com.yunda.jxpz.utils.SystemConfigUtil"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard" %>
<%--<%@include file="/frame/jspf/EosDictEntry.jspf" %>--%>
<%@include file="/frame/jspf/Attachment.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>子流程信息</title>
		<script language="javascript">	
			var ctx = '<%=ctx %>';
			var processInstID = '${param.processInstID}';
			var rdpId = '${param.rdpId}';
			var activitydefId = '${param.activitydefId}';
			var processDefID = '${param.processdefId}';
			var activityinstid = '${param.activityinstid}';  //活动实例id
			var STATUS_NEW = '<%=WorkCard.STATUS_NEW%>';//状态为待开放
			var STATUS_OPEN = '<%=WorkCard.STATUS_OPEN%>';//状态为已开放
			var STATUS_HANDLING = '<%=WorkCard.STATUS_HANDLING%>';//状态为处理中
			var STATUS_HANDLED = '<%=WorkCard.STATUS_HANDLED%>';//状态为已处理
			var STATUS_FINISHED = '<%=WorkCard.STATUS_FINISHED%>';
			var STATUS_TERMINATED = '<%=WorkCard.STATUS_TERMINATED%>';//状态为终止
			var synSiteID = '<%=JXConfig.getInstance().getSynSiteID()%>'
			var partsRdp = '<%=com.yunda.jx.jxgc.basic.Rdp.TYPE_PARTS %>';
			var trainRdp = '<%=com.yunda.jx.jxgc.basic.Rdp.TYPE_TRAIN %>';
		
			//工序卡检验项目对象
			var objList = <%=JSONUtil.write(WorkSeqManager.getWorkSeqQualityControl("null"))%>
			var fields = [{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'作业卡主键', dataIndex:'workCardIDX',hidden:true, editor:{  maxLength:50 }
			},{
				header:'工步主键', dataIndex:'workStepIDX',hidden:true, editor:{  maxLength:50 }
			},{
				header:'作业任务编码', dataIndex:'workTaskCode',hidden:true, editor:{  maxLength:50 }
			},{
				header:'检测/修项目', dataIndex:'workTaskName', editor:{  maxLength:50 }
			},{
				header:'作业类型', dataIndex:'workTaskType',hidden:true, editor:{  maxLength:50 }
			},{
				header:'检修内容', dataIndex:'repairContent',hidden:true, editor:{  maxLength:500 }
			},{
				header:'技术要求或标准规定', dataIndex:'repairStandard',width:200, editor:{ maxLength:1000 }
			},{
				header:'检修方法', dataIndex:'repairMethod',hidden:true, editor:{  maxLength:500 }
			},{
				header:'检修结果主键', dataIndex:'repairResultIdx',hidden:true, editor:{  maxLength:50 }
			},{
				header:'检修代码', dataIndex:'resultCode',hidden:true, editor:{  maxLength:500 }
			},{
				header:'作业结果', dataIndex:'resultName', editor:{  maxLength:50 }
			},{
				header:'施修人员', dataIndex:'repairWorker',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
			},{
				header:'操作者', dataIndex:'repairWorkerName',width:60, editor:{  maxLength:50 }
			},{
				header:'状态', dataIndex:'status',hidden:true, editor:{  maxLength:64 }
			},{
				header:'备注', dataIndex:'remarks',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
			}];
				var quaFields = [];
				for( var i = 0; i < objList.length; i++) { //循环创建grid列头信息
					var object = objList[ i ];
					quaFields.push({header: object.checkItemName, dataIndex: object.checkItemCode,width:60, editor:{ },sortable: false});
				}
			var isparts = '${ param.isparts }';
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
		<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/qualitycontrol/TrainQRWorker.js"></script> <!-- 机作人员记录JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/qualitycontrol/TrainQRWorkTask.js"></script> <!-- 机作业项记录JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/qualitycontrol/TrainQRDetectResult.js"></script> <!-- 检测结果记录JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/qualitycontrol/TrainWordCardView_Comm.js"></script> <!-- 作业工单显示界面信息JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/subFlowInfo.js"></script>
		
	</head>
	<body>
	</body>
</html>