<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/sb/base/jsp/constant.jsp" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanYear"%>
<%@page import="com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设备检修年计划</title>
		<style type="text/css">
			.unEditCell {
				background-color:rgb(95, 228, 70);
			}
		</style>
		<script type="text/javascript">
			/** 修程  - 未设置【-1】 */
			var REPAIR_CLASS_NOSET = '<%= RepairPlanYear.REPAIR_CLASS_NOSET%>';
			/** 修程  - 小修【1】 */
			var REPAIR_CLASS_SMALL = '<%= RepairPlanYear.REPAIR_CLASS_SMALL%>';
			/** 修程  - 中修【2】 */
			var REPAIR_CLASS_MEDIUM = '<%= RepairPlanYear.REPAIR_CLASS_MEDIUM%>';
			/** 修程  - 项修【3】 */
			var REPAIR_CLASS_SUBJECT = '<%= RepairPlanYear.REPAIR_CLASS_SUBJECT%>';
			
			/** 设备动态-调入1 */
			var DYNAMIC_IN = '<%= EquipmentPrimaryInfo.DYNAMIC_IN %>';
		    /** 设备动态-新购3 */
			var DYNAMIC_NEW_BUY = '<%= EquipmentPrimaryInfo.DYNAMIC_NEW_BUY %>';
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/Common.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SingleFieldCombo.js"></script>
		<script type="text/javascript" src="EquipmentPrimaryInfoSelect.js"></script>
		<script type="text/javascript" src="RepairPlanYear.js"></script>
	</head>
	<body>
	</body>
</html>
