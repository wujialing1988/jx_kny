<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode" %>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业计划编制</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.css" rel="stylesheet" type="text/css">

<style type="text/css">
	.previewIcon{background-image:url(<%= request.getContextPath() %>/frame/resources/images/toolbar/preview.png) !important;}
</style>
<script type="text/javascript">
	var delayList = <%=JSONUtil.write(NodeCaseDelayManager.getDalayType())%>   //延期原因类型
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	var workPlanIDX = '';
	var workPlanEntity = {};
	
	Ext.onReady(function(){	
	Ext.namespace('TrainWorkPlanGanttSearch'); 
		TrainWorkPlanGanttSearch.workPlanIDX = '';		
	});	
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js">
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<%@include file="/jsp/jx/jxgc/workplanmanage/WorkPlanGantt.jspf" %>
<script language="javascript" src="TrainWorkPlanWin.js"></script>
<script language="javascript" src="TrainWorkPlanForm.js"></script>
<script language="javascript" src="TrainPlanSelect.js"></script>


<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/qcresult/QCResult.js"></script>

<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/defineworkcard/WorkEdit_DetectItemSearch.js"></script><!-- 自定义作业工单-检测项 -->
<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/defineworkcard/WorkEdit_WorkStepEdit_QrKeySearch.js"></script><!-- 自定义作业工单-检测/修项目新增编辑操作 -->
<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/defineworkcard/WorkEdit_WorkStep_QrKeySearch.js"></script><!-- 自定义作业工单-检测/修项目Grid -->
<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/defineworkcard/Train_Work_Edit_QrKeySearch.js"></script><!-- 自定义作业工单表单 -->	
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/WorkCardEditSearch.js"></script><!-- 作业工单列表 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/JobProcessNodeSearch.js"></script><!-- 新增编辑节点表单 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/JobProcessNodeRelSearch.js"></script><!-- 节点前置关系列表 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/JobNodeExtConfigSearch.js"></script><!-- 扩展配置窗口-->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workplanmanage/NodeAndWorkCardEditSearch.js"></script><!-- 节点编辑主页面窗口 -->


<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/TrainWorkPlanWinSearch.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/ChildNodeListGrid.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/JobProcessNodeRelEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/NodeAndWorkCardEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/JobProcessNodeEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/WorkCardEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/JobNodeExtConfigEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/TrainWorkPlanEditWin.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplan/NodeCaseDelayNew.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainWorkPlan.js"></script>
<script type="text/javascript">
	var NODE_STATUS_WKG = '<%= JobProcessNode.STATUS_UNSTART %>'		// 未开工
	var NODE_STATUS_YKG = '<%= JobProcessNode.STATUS_GOING %>'			// 已开工
	var NODE_STATUS_YWG = '<%= JobProcessNode.STATUS_COMPLETE %>'		// 已完工
	
	var PLAN_STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>'				// 待修
	var PLAN_STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>'	// 在修
	var SPLAN_TATUS_HANDLED = '<%= TrainWorkPlan.STATUS_HANDLED %>'		// 修竣
</script>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>