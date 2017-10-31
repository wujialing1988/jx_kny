<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf"%> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanMonth"%>
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanYear" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检修作业工单处理</title>
		<style type="text/css">
			.finshedCell {
				background-color: rgb(95, 228, 70);
			}
			.processCell {
				background-color: rgb(239, 239, 239);
			}
			
			#repair_task_base_info table {
				width: 100%;
				font-size: 12px;
			}
			
			#repair_task_base_info table tr td {
				padding: 3px 0;
				width: 23.33%;
			}
			
			#repair_task_base_info table tr td span {
				color: gray;
				width: 100px;
				text-align: right;
				display: inline-block;
			}
		</style>
		<script type="text/javascript">
		
			/** 修程  - 小修【1】 */
			var REPAIR_CLASS_SMALL = '<%= RepairPlanYear.REPAIR_CLASS_SMALL%>';
			/** 修程  - 中修【2】 */
			var REPAIR_CLASS_MEDIUM = '<%= RepairPlanYear.REPAIR_CLASS_MEDIUM%>';
			/** 修程  - 项修【3】 */
			var REPAIR_CLASS_SUBJECT = '<%= RepairPlanYear.REPAIR_CLASS_SUBJECT%>';
			
			/** 计划状态 - 未下发【0】 */
			var PLAN_STATUS_WXF = '<%= RepairPlanMonth.PLAN_STATUS_WXF%>';
			/** 计划状态 - 已下发【1】 */
			var PLAN_STATUS_YXF = '<%= RepairPlanMonth.PLAN_STATUS_YXF%>';
			
			/** 检修类型 - 机械 */
			var REPAIR_TYPE_JX = '<%= RepairScope.REPAIR_TYPE_JX %>';
			/** 检修类型 - 电气 */
			var REPAIR_TYPE_DQ = '<%= RepairScope.REPAIR_TYPE_DQ %>';
			/** 检修类型 - 其它  */
			var REPAIR_TYPE_QT = '<%= RepairScope.REPAIR_TYPE_QT %>';
			
			// Ext图表flash资源
			Ext.chart.Chart.CHART_URL = ctx + '/frame/resources/ext-3.4.0/resources/charts.swf';
		</script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts-liquidfill.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/pinyin.js"></script>
		<script type="text/javascript" src="<%= ctx %>/frame/resources/ext-3.4.0/grid/RowExpander.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SingleFieldCombo.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SimpleEntityCombo.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/ImageView.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/Common.js"></script>
		<script type="text/javascript" src="RepairWorkOrderStuff.js"></script>
		<script type="text/javascript" src="RepairWorkOrder.js"></script>
		<script type="text/javascript" src="RepairScopeCase.js"></script>
		<script type="text/javascript" src="RepairTaskList.js"></script>
	</head>
	<body>
	</body>
</html>
