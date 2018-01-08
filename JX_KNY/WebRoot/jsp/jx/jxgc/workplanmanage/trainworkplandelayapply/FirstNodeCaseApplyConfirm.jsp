<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeUpdateApply" %>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业计划编制</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<link href="<%=ctx%>/jsp/jx/pjwz/query/css/PartsRdpSearch.css" rel="stylesheet" type="text/css"/>

<style type="text/css">
	.previewIcon{background-image:url(<%= request.getContextPath() %>/frame/resources/images/toolbar/preview.png) !important;}
</style>
<script type="text/javascript">
	

	// 申请延期状态
	var EDIT_STATUS_WAIT = '<%= JobProcessNodeUpdateApply.EDIT_STATUS_WAIT %>'		// 待段调度确认
	var EDIT_STATUS_ON = '<%= JobProcessNodeUpdateApply.EDIT_STATUS_ON %>'		// 确认
	var EDIT_STATUS_UN = '<%= JobProcessNodeUpdateApply.EDIT_STATUS_UN %>'		// 拒绝或取消
	
	var PLAN_STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>'				// 待修
	var PLAN_STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>'	// 在修
	var SPLAN_TATUS_HANDLED = '<%= TrainWorkPlan.STATUS_HANDLED %>'		// 修竣
    
	// 自定义vtype，验证日期范围
	Ext.applyIf(Ext.form.VTypes, {
		dateRange: function(_v, field) {
			if (field.dateRange) {
				// 开始日期
				var startId = field.dateRange.startDate;
				this.startField = Ext.getCmp(startId);
				var startDate = this.startField.getValue();
				// 结束日期
				var endId = field.dateRange.endDate;
				this.endField = Ext.getCmp(endId);
				var endDate = this.endField.getValue();
				if (Ext.isEmpty(startDate) || Ext.isEmpty(endDate)) {
					return true;
				}
				return startDate <= endDate ? true : false
			}
		},
		// 验证失败信息
		dateRangeText: "开始日期不能大于结束日期"
	});
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>

<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplandelayapply/FirstNodeCaseApplyConfirm.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplandelayapply/TrainWorkPlanRecordsForApply.js"></script>


</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>