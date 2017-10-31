<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.fault.entity.FaultOrder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%
		// 暂时保留通过url传参自动过滤设备的处理
		String equipmentCode = request.getParameter("equipmentCode");
	%>
	<title>提票调度派工</title>
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
			
			// 暂时保留通过url传参自动过滤设备的处理
			var equipmentCode = '<%= equipmentCode %>';
		</script>
		<link rel="stylesheet" href="<%= ctx %>/jsp/sb/base/css/ext-yunda-tpl.css" type="text/css"/>
		<script type="text/javascript" src="<%=ctx %>/frame/resources/ext-3.4.0/grid/RightMenu.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationWin.js"></script>
	    <script type="text/javascript" src="<%=ctx %>/jsp/sb/base/js/ImageView.js"></script>
		<script type="text/javascript" src="MultiDispatch.js"></script>
		<script type="text/javascript" src="Dispatch.js"></script>
		<script type="text/javascript" src="FaultOrder.js"></script>
	</head>
	<body>
	</body>
</html>
