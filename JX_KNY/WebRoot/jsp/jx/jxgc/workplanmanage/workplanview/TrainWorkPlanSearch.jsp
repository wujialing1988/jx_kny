<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.frame.common.JXConfig" %>


<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业工单查询</title>
<script type="text/javascript">
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//处理中
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//已处理
	var rdp_status_nullify = '<%=TrainWorkPlan.STATUS_NULLIFY%>';//终止
	
	var siteID ='<%=JXConfig.getInstance().getSynSiteID()%>';
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/QualityControlResult.js"></script> <!-- 质量检查JS --> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkPlanWorkTask.js"></script> <!-- 机车作业任务记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkPlanDetectResult.js"></script> <!-- 检测结果记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWordCardView_Comm.js"></script> <!-- 作业工单显示界面信息JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkPlanWorkCard.js"></script> <!-- 作业工单查询 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkPlanComm.js"></script> <!-- 机车检修作业计划详细信息展示 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkPlanSearch.js"></script> <!-- 机车检修作业计划查询 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>