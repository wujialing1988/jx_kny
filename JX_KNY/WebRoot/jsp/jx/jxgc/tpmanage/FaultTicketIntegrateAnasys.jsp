<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提票综合分析</title>
<script type="text/javascript">
	var PLAN_STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>'				// 待修
	var PLAN_STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>'	// 在修
	var SPLAN_TATUS_HANDLED = '<%= TrainWorkPlan.STATUS_HANDLED %>'		// 修竣
	
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var type = "";
	
	var systemOrgname = "${ sessionScope.overseaName}"; 
	var systemOrgid = "${ sessionScope.oversea}";
	var empId = '${sessionScope.emp.empid}'; //用户ID
	var empName = '${sessionScope.emp.empname}';//用户名
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/tpmanage/TrainFormTreeSelectWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="FaultTicketIntegrateAnasys.js"></script>
</head>
<body>
</body>
</html>