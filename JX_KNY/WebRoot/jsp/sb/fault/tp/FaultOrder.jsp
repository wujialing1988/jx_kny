<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.fault.entity.FaultOrder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>故障提票</title>
		<script type="text/javascript">
		/** 提票单状态 - 新建 */
		var STATE_XJ = '<%= FaultOrder.STATE_XJ %>';
		/** 提票单状态 - 已派工（调度派工） */
		var STATE_YPG = '<%= FaultOrder.STATE_YPG %>';
		/** 提票单状态 - 处理中（工长派工） */
		var STATE_CLZ = '<%= FaultOrder.STATE_CLZ %>';
		/** 提票单状态 - 已处理 */
		var STATE_YCL = '<%= FaultOrder.STATE_YCL %>';
		/** 提票单状态 - 退回 */
		var STATE_TH = '<%= FaultOrder.STATE_TH %>';
		
		/** 故障等级 - 一般 */
		var FAULT_LEVEL_YB = '<%= FaultOrder.FAULT_LEVEL_YB %>';
		/** 故障等级 - 重大 */
		var FAULT_LEVEL_ZD = '<%= FaultOrder.FAULT_LEVEL_ZD %>';
		/** 故障等级 - 特大 */
		var FAULT_LEVEL_TD = '<%= FaultOrder.FAULT_LEVEL_TD %>';
		
		var now = new Date();
		now.setMonth(now.getMonth() - 1);
		var lastMonth = now.format('Y-m-d');
		</script>
		<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SimpleEntityCombo.js"></script>
	    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	    <script type="text/javascript" src="<%=ctx %>/jsp/sb/base/js/ImageView.js"></script>
		<script type="text/javascript" src="QuickNotice.js"></script>
		<script type="text/javascript" src="FaultOrder.js"></script>
	</head>
	<body>
	</body>
</html>
