<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page import="com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan" %>
<%@page import="com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>机车施修计划待办</title>
		<script language="javascript">
		    //计划状态-编制完成
			var status_completePlan = <%=TrainEnforcePlan.STATUS_COMPLETEPLAN%>;
			//计划状态
			var status_detail_formation  = <%=TrainEnforcePlanDetail.STATUS_PLAN%>; //-编制
			var status_detail_redemption = <%=TrainEnforcePlanDetail.STATUS_REDEMPTION%>; //-已兑现
			var status_detail_complete   = <%=TrainEnforcePlanDetail.STATUS_COMPLETE%>; //-检修完成
			//流水线类型-修机车流水线
			var repairLine_type = <%=RepairLine.TYPE_TRAIN%>;
			//获取当前用户
			var empId = '${sessionScope.emp.empid}';
			var empname = '${sessionScope.emp.empname}';
			var orgId = '${sessionScope.org.orgid}';
			var orgName = '${sessionScope.org.orgname}';
			var orgseq = '${sessionScope.org.orgseq}';
			//当前单位（段）
			var overseaName = '${sessionScope.overseaName}';//段名
			var oversea = '${sessionScope.oversea}';//段id	
			var overseaSeq = '${sessionScope.orgDep.orgseq}';//段序列
			var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
			//兑现单状态
			var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
			var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//处理中
			var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//已处理
		</script>
		<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
		<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BureauSelect.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/scdd/enforceplanSnaker/DominateSectionWidget.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlanDetailwTodo.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/enforceplanSnaker/SnakerApprovalRecord.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/cmps/fastform.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/scdd/jxplanmanage/planning/planning_edit_details_rkm.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_common.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_fxdx.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_c1c6.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlanTaskTodo.js"></script>
	</head>
	<body>
	</body>
</html>