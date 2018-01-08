<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanYear"%>
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanMonth"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备检修周计划（new）</title>
		<script type="text/javascript">
		
			/** 修程  - 小修【1】 */
			var REPAIR_CLASS_SMALL = '<%= RepairPlanYear.REPAIR_CLASS_SMALL%>';
			/** 修程  - 中修【2】 */
			var REPAIR_CLASS_MEDIUM = '<%= RepairPlanYear.REPAIR_CLASS_MEDIUM%>';
			/** 修程  - 项修【3】 */
			var REPAIR_CLASS_SUBJECT = '<%= RepairPlanYear.REPAIR_CLASS_SUBJECT%>';
			
			/** 计划状态 - 未下发【0】 */
			var PLAN_STATUS_WXF = '<%= RepairPlanMonth.PLAN_STATUS_WXF%>';
			/** 计划状态 - 已下发【0】 */
			var PLAN_STATUS_YXF = '<%= RepairPlanMonth.PLAN_STATUS_YXF%>';
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SingleFieldCombo.js"></script>
		<script type="text/javascript" src="RepairPlanPublish.js"></script>
		<script type="text/javascript" src="RepairPlanMonth.js"></script>
	</head>
	<body>
	</body>
</html>
