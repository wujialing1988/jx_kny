<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>生产任务计划</title>
    <script language="javascript">
	    //计划状态-编制完成
		var status_completePlan = '<%=com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail.STATUS_COMPLETE%>';
		var status_redemption = '<%=com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail.STATUS_REDEMPTION%>';
		//计划状态-编制(初始状态)
		var status_Plan = '<%=com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail.STATUS_PLAN%>';
		//获取当前用户
		var empId = '${sessionScope.emp.empid}';
		var empname = '${sessionScope.emp.empname}';
		//当前单位（段）
		var overseaName = '${sessionScope.overseaName}';//段名
		var oversea = '${sessionScope.oversea}';//段id	
		var overseaSeq = '${sessionScope.orgDep.orgseq}';//段序列
	</script>
	<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainRCCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/RCRTCombo.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/enforceplan/EnforcePlan.js"></script>
  </head>
  
  <body>
    
  </body>
</html>
