<%@include file="/frame/jspf/header.jspf"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page import="com.yunda.sb.fault.entity.FaultOrder" %> 
<%@page import="com.yunda.sb.inspect.plan.entity.InspectPlanEquipment" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>使用人确认</title>
<link rel="stylesheet" href="<%= ctx %>/jsp/sb/base/css/ext-yunda-tpl.css" type="text/css"/>
<script type="text/javascript">
			/** 检修类型 - 机械 */
			var REPAIR_TYPE_JX = '<%= RepairScope.REPAIR_TYPE_JX %>';
			/** 检修类型 - 电气 */
			var REPAIR_TYPE_DQ = '<%= RepairScope.REPAIR_TYPE_DQ %>';
			/** 提票单状态 - 已处理 */
			var STATE_YCL = '<%= FaultOrder.STATE_YCL %>';
			/** 巡检结果 - 已巡检 */
			var CHECK_RESULT_YXJ = '<%= InspectPlanEquipment.CHECK_RESULT_YXJ %>';
		</script>
<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SingleFieldCombo.js"></script>
<script type="text/javascript"  src="./repair/RepairRecord.js"></script>
<script type="text/javascript"  src="./repair/RepairConfirm.js"></script>
<script type="text/javascript"  src="./faultorder/FaultOrderConfirm.js"></script>
<script type="text/javascript"  src="./inspect/InspectPlanRecord.js"></script>
<script type="text/javascript"  src="./inspect/InspectPlanConfirm.js"></script>
<script type="text/javascript"  src="UserConfirm.js"></script>
</head>
<body>
</body>
</html>