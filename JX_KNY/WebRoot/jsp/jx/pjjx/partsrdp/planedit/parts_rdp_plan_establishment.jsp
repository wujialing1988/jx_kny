<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>配件检修作业计划编制</title>
	<%@include file="/frame/jspf/header.jspf" %>
	<%@include file="/frame/jspf/TreeFilter.jspf" %>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript">
		var NOT_START = "<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_WQD %>";
		var STARTED = "<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_JXZ %>";
		var partsStatus = "<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_DX %>";
		var manageDeptType = "<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_ORG %>";//责任部门类型为机构【不在库】
	</script>
	<script type="text/javascript" src="<%=ctx %>/jsp/cmps/fastform.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/cmps/editgrid.js"></script>
	<script type="text/javascript" src="parts_plan_common.js"></script>
	<script type="text/javascript" src="not_start_grid.js"></script>
	<script type="text/javascript" src="started_grid.js"></script>
	<script type="text/javascript" src="new_parts_plan.js"></script>
	<script type="text/javascript" src="new_parts_repair_select_win.js"></script>
	<script type="text/javascript" src="parts_plan_edit.js"></script>
	<script type="text/javascript" src="parts_plan_edit_baseform.js"></script>
	<script type="text/javascript" src="parts_plan_edit_gant.js"></script>
	<script type="text/javascript" src="parts_plan_edit_job_order.js"></script>
	<script type="text/javascript" src="parts_plan_edit_job_order_node.js"></script>
	<script type="text/javascript" src="parts_plan_edit_node_station_choose.js"></script>
	<script type="text/javascript" src="parts_rdp_plan_establishment.js"></script>
  </head>
  
  <body>
  </body>
</html>
