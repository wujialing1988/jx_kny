<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.inspect.route.entity.InspectRoute"%>
<%@page import="com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备巡检线路</title>
		<style type="text/css">
			#id_inspect_route_info table {
				padding-left:10px;
			 	width: 100%;
			 	font-size: 12px;
			}
			#id_inspect_route_info table tr td {
    			padding: 3px 0;
    			width: 100%;
			}
			#id_inspect_route_info table tr td span {
				color: gray;
				width: 80px;
				text-align: right;
				display: inline-block;
			}
		</style>
		
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
			
			/** 状态：未启用（0） */
			var STATE_WQY = '<%= InspectRoute.STATE_WQY %>';
			/** 状态：未启用（0） */
			var STATE_WQY_CH = '<%= InspectRoute.STATE_WQY_CH %>';
			/** 状态：启用（1） */
			var STATE_QY = '<%= InspectRoute.STATE_QY %>';
			/** 状态：启用（1） */
			var STATE_QY_CH = '<%= InspectRoute.STATE_QY_CH %>';
			
			/** 设备动态-调入1 */
			var DYNAMIC_IN = '<%= EquipmentPrimaryInfo.DYNAMIC_IN %>';
		    /** 设备动态-新购3 */
			var DYNAMIC_NEW_BUY = '<%= EquipmentPrimaryInfo.DYNAMIC_NEW_BUY %>';
		</script>
	    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/employee/OmEmployeeMultSelect.js"></script>
	    <script type="text/javascript" src="<%=ctx %>/jsp/sb/base/js/staticCombo.js"></script>
		<script type="text/javascript" src="InspectPlan.js"></script>
		<script type="text/javascript" src="EquipmentPrimaryInfoSelect.js"></script>
		<script type="text/javascript" src="InspectEmp.js"></script>
		<script type="text/javascript" src="InspectRouteDetails.js"></script>
		<script type="text/javascript" src="InspectRoute.js"></script>
	</head>
	<body>
	</body>
</html>
