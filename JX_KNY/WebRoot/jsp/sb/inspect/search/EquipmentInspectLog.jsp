<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page import="com.yunda.sb.inspect.plan.entity.InspectPlanEquipment"%>
<%@page import="com.yunda.sb.inspect.plan.entity.InspectPlan"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备巡检统计</title>
		<link href="../css/inspect.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">
			/** 检修类型 - 机械 */
			var REPAIR_TYPE_JX = '<%= RepairScope.REPAIR_TYPE_JX %>';
			/** 检修类型 - 电气 */
			var REPAIR_TYPE_DQ = '<%= RepairScope.REPAIR_TYPE_DQ %>';
			/** 检修类型 - 其它  */
			var REPAIR_TYPE_QT = '<%= RepairScope.REPAIR_TYPE_QT %>';
			
			/** 巡检结果 - 未巡检 */
			var CHECK_RESULT_WXJ = '<%= InspectPlanEquipment.CHECK_RESULT_WXJ %>';
			/** 巡检结果 - 未巡检 */
			var CHECK_RESULT_YXJ = '<%= InspectPlanEquipment.CHECK_RESULT_YXJ %>';
			
			/** 计划状态 - 未处理 */
			var STATE_WCL = '<%= InspectPlan.STATE_WCL %>';
			/** 计划状态 - 已处理 */
			var STATE_YCL = '<%= InspectPlan.STATE_YCL %>';
			
			// Ext图表flash资源
			Ext.chart.Chart.CHART_URL = ctx + '/frame/resources/ext-3.4.0/resources/charts.swf';
			
			var reloadStartDate = '<%=request.getParameter("startDate") == null ? "" : request.getParameter("startDate") %>';
			var reloadEndDate = '<%=request.getParameter("endDate") == null ? "" : request.getParameter("endDate") %>';
		</script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts-liquidfill.js"></script>
	    <script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/ImageView.js"></script>
	    <script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/Common.js"></script>
		<script type="text/javascript" src="../plan/InspectRecord.js"></script>
		<script type="text/javascript" src="InspectPlanEquipment.js"></script>
		<script type="text/javascript" src="EquipmentInspectLog.js"></script>
	</head>
	<body>
	</body>
</html>
