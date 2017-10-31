<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/OrderManager.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %> 
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanYear" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备检修范围</title>
		<script type="text/javascript">
			/** 检修类型 - 机械 */
			var REPAIR_TYPE_JX = '<%= RepairScope.REPAIR_TYPE_JX %>';
			/** 检修类型 - 电气 */
			var REPAIR_TYPE_DQ = '<%= RepairScope.REPAIR_TYPE_DQ %>';
			/** 检修类型 - 其它  */
			var REPAIR_TYPE_QT = '<%= RepairScope.REPAIR_TYPE_QT %>';
			
			/** 修程  - 小修【1】 */
			var REPAIR_CLASS_SMALL = '<%= RepairPlanYear.REPAIR_CLASS_SMALL %>';
			/** 修程  - 中修【2】 */
			var REPAIR_CLASS_MEDIUM = '<%= RepairPlanYear.REPAIR_CLASS_MEDIUM %>';	
			/** 修程  - 项修【3】 */
			var REPAIR_CLASS_SUBJECT = '<%= RepairPlanYear.REPAIR_CLASS_SUBJECT %>';	
		</script>
		<script type="text/javascript" src="<%= ctx %>/frame/resources/ext-3.4.0/grid/CheckColumn.js"></script>
		<script type="text/javascript" src="RepairScopeImport.js"></script>
		<script type="text/javascript" src="RepairScopeRiskWarning.js"></script>
		<script type="text/javascript" src="RepairScopeSelect.js"></script>
		<script type="text/javascript" src="RepairScopeDetails.js"></script>
		<script type="text/javascript" src="RepairScope.js"></script>
	</head>
	<body>
	</body>
</html>
