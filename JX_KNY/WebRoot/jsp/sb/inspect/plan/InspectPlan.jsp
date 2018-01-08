<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page import="com.yunda.sb.inspect.plan.entity.InspectPlan"%>
<%@page import="com.yunda.sb.inspect.route.entity.InspectRoute"%>
<%@page import="com.yunda.sb.inspect.plan.entity.InspectPlanEquipment"%>
<%@page import="com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备巡检计划</title>
		<link href="../css/inspect.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">
			/** 巡检周期：周检（1） */
			var PERIOD_TYPE_ZJ = '<%= InspectRoute.PERIOD_TYPE_ZJ %>';
			/** 巡检周期：周检（1） */
			var PERIOD_TYPE_ZJ_CH = '<%= InspectRoute.PERIOD_TYPE_ZJ_CH %>';
			/** 巡检周期：半月检（2） */
			var PERIOD_TYPE_BYJ = '<%= InspectRoute.PERIOD_TYPE_BYJ %>';
			/** 巡检周期：半月检（2） */
			var PERIOD_TYPE_BYJ_CH = '<%= InspectRoute.PERIOD_TYPE_BYJ_CH %>';
			/** 巡检周期：月检（3） */
			var PERIOD_TYPE_YJ = '<%= InspectRoute.PERIOD_TYPE_YJ %>';
			/** 巡检周期：月检（3） */
			var PERIOD_TYPE_YJ_CH = '<%= InspectRoute.PERIOD_TYPE_YJ_CH %>';
			/** 巡检周期：季检（4） */
			var PERIOD_TYPE_JJ = '<%= InspectRoute.PERIOD_TYPE_JJ %>';
			/** 巡检周期：季检（4） */
			var PERIOD_TYPE_JJ_CH = '<%= InspectRoute.PERIOD_TYPE_JJ_CH %>';
		
			/** 处理状态 - 未处理 */
			var STATE_WCL = '<%= InspectPlan.STATE_WCL%>';
			/** 处理状态 - 已处理 */
			var STATE_YCL = '<%= InspectPlan.STATE_YCL%>';
			
			/** 设备动态-调入1 */
			var DYNAMIC_IN = '<%= EquipmentPrimaryInfo.DYNAMIC_IN %>';
		    /** 设备动态-新购3 */
			var DYNAMIC_NEW_BUY = '<%= EquipmentPrimaryInfo.DYNAMIC_NEW_BUY %>';
			
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
			
			// Ext图表flash资源
			Ext.chart.Chart.CHART_URL = ctx + '/frame/resources/ext-3.4.0/resources/charts.swf';
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts-liquidfill.js"></script>
	    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/employee/OmEmployeeMultSelect.js"></script>
	    <script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/ImageView.js"></script>
	    <script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/staticCombo.js"></script>
	    <script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/Common.js"></script>
		<script type="text/javascript" src="EquipmentPrimaryInfoSelect.js"></script>
		<script type="text/javascript" src="InspectRecord.js"></script>
		<script type="text/javascript" src="InspectEmp.js"></script>
		<script type="text/javascript" src="InspectPlanEquipment.js"></script>
		<script type="text/javascript" src="InspectPlan.js"></script>
	</head>
	<body>
	</body>
</html>
