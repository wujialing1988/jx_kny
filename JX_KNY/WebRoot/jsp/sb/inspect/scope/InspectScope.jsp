<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/OrderManager.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %> 
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@page import="com.yunda.sb.repair.scope.entity.RepairScope" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备巡检标准</title>
		<script type="text/javascript">
			/** 检修类型 - 机械 */
			var REPAIR_TYPE_JX = '<%= RepairScope.REPAIR_TYPE_JX %>';
			/** 检修类型 - 电气 */
			var REPAIR_TYPE_DQ = '<%= RepairScope.REPAIR_TYPE_DQ %>';
			/** 检修类型 - 其它  */
			var REPAIR_TYPE_QT = '<%= RepairScope.REPAIR_TYPE_QT %>';
		</script>
		<script type="text/javascript" src="InspectScopeImport.js"></script>
		<script type="text/javascript" src="InspectScopeSelect.js"></script>
		<script type="text/javascript" src="InspectScope.js"></script>
	</head>
	<body>
	</body>
</html>
