<%--TODO 待确定是否删除--%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/TreeGrid.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.RepairActivity" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>
<%try{ %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调度监控</title>
<script language="javascript">	
	var isDelayTrain = <%=TrainWorkPlan.IS_DELAY_TRAIN%>;//是否延迟交车-是
	var noDelayTrain = <%=TrainWorkPlan.NO_DELAY_TRAIN%>;//是否延迟交车-否
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//处理中
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//已处理
	var rdp_status_nullify = '<%=TrainWorkPlan.STATUS_NULLIFY%>';//终止
	//检修活动类型
	var type_project = <%=RepairActivity.TYPE_PROJECT%>;//检修项目类型
	var type_tp = <%=RepairActivity.TYPE_TP%>;//提票类型
	var type_tec_reform = <%=RepairActivity.TYPE_TEC_REFORM%>;//技术改造活动类型
	var ctx = '<%=ctx %>';
	var faultNotice_status_draft = <%=FaultTicket.STATUS_DRAFT%>;//提票状态-未处理
	var trainTypeIDX = "";//车型主键
	var trainNo = "";//车号
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<%--<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/DDJK_EnforcePlanDetail.js"></script>
--%><script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/flowFrame.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/DDJK.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>