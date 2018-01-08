<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode" %>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.entity.PartsRdp" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业计划编制</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.css" rel="stylesheet" type="text/css">
<link href="<%=ctx%>/jsp/jx/pjwz/query/css/PartsRdpSearch.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	#id_jx_record_card_detail {
		height:100%;
		overflow: scroll;
	}
	#id_jx_record_card_detail table {
		width: 100%;
		border-right:1px solid rgb(208,208,208);
		border-bottom:1px solid rgb(208,208,208);
	}
	#id_jx_record_card_detail table thead tr td {
		background:url(<%=ctx%>/jsp/jx/pjwz/query/css/images/table_thead_bg.gif) repeat;
		/*text-align:center;*/
		padding: 2px 2px 2px 5px;
		height:23px;
	}
	#id_jx_record_card_detail table tr td {
		font-size: 12px;
		padding: 2px 2px 2px 5px;
		height:23px;
		border-left:1px solid rgb(208,208,208);
		border-top:1px solid rgb(208,208,208);
	}
	#id_jx_record_card_detail table table {
	padding: 0px 0px 0px 0px;
		width: 100%;
		height:100%;
		border: none;
	}
	

	#id_record_card_detail {
		height:100%;
		overflow: scroll;
	}
	#id_record_card_detail table {
		width: 100%;
		border-right:1px solid rgb(208,208,208);
		border-bottom:1px solid rgb(208,208,208);
	}
	#id_record_card_detail table thead tr td {
		background:url(<%=ctx%>/jsp/jx/pjwz/query/css/images/table_thead_bg.gif) repeat;
		/*text-align:center;*/
		padding: 2px 2px 2px 5px;
		height:23px;
	}
	#id_record_card_detail table tr td {
		font-size: 12px;
		padding: 2px 2px 2px 5px;
		height:23px;
		border-left:1px solid rgb(208,208,208);
		border-top:1px solid rgb(208,208,208);
	}
	
	#id_record_card_detail table table {
	padding: 0px 0px 0px 0px;
		width: 100%;
		height:100%;
		border: none;
	}
</style>

<style type="text/css">
	.previewIcon{background-image:url(<%= request.getContextPath() %>/frame/resources/images/toolbar/preview.png) !important;}
</style>
<script type="text/javascript">
	var delayList = <%=JSONUtil.write(NodeCaseDelayManager.getDalayType())%>
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	var workPlanIDX = '';
	var workPlanEntity = {};
		//机车检修提票状态
	var STATUS_DRAFT = '<%=FaultTicket.STATUS_DRAFT%>';//未处理
	var STATUS_OPEN = '<%=FaultTicket.STATUS_OPEN%>';//处理中
	var STATUS_OVER = '<%=FaultTicket.STATUS_OVER%>';//已处理
	
	 var PARTS_RDP_STATUS_WKF = '<%=IPartsRdpStatus.CONST_STR_STATUS_WKF%>';			// 作业工单状态 - 未开放
	var PARTS_RDP_STATUS_DLQ = '<%=IPartsRdpStatus.CONST_STR_STATUS_DLQ%>';			// 作业工单状态 - 待领取
	var PARTS_RDP_STATUS_DCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_DCL%>';			// 作业工单状态 - 待处理
	var PARTS_RDP_STATUS_YCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_YCL%>';			// 作业工单状态 - 已处理
	<%-- 已处理状态包含以下状态 --%>
	var PARTS_RDP_STATUS_XJ = '<%=IPartsRdpStatus.CONST_STR_STATUS_XJ%>';			// 作业工单状态 - 修竣
	var PARTS_RDP_STATUS_ZLJYZ = '<%=IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ%>';		// 作业工单状态 - 质量检验中
    
    <%-- 回退标识 --%>
	var IS_BACK_YES = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_YES %>'					// 回退标识 - 是
	var IS_BACK_NO = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_NO %>'					
    
    var TEC_WP_STATUS_WCL = '<%= PartsRdpTecWS.CONST_STR_STATUS_WCL %>';
    var TEC_WP_STATUS_YCL = '<%= PartsRdpTecWS.CONST_STR_STATUS_YCL %>';
    
    <%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx%>/frame/resources/images/toolbar';
	<%-- 打印图标虚拟路径 --%>
	var printerImg = imgpath + '/printer.png';
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<%@include file="/jsp/jx/jxgc/workplanmanage/WorkPlanGantt.jspf" %>


<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>


<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsDismantleHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsInstallHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecWS.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecCardDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecCard.js"></script>

<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsmanage/PartsAcountTurnOver.js"></script><!-- 配件周转历史记录 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsDismantleHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsInstallHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpExpendMatQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecordCardNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecord.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsAccountOrRdp.js"></script>

<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/turnover/PartsZzjhQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workhis/workplan/TrainWorkTicketDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workhis/workplan/TrainWorkTicket.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkCardNew.js"></script> <!-- 机车检修记录卡展示 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/PartsFixRegisterHis.js"></script> <!-- 上车配件JS --> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/PartsUnloadRegisterHis.js"></script> <!-- 下车配件JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/QualityControlResultHis.js"></script> <!-- 质量检查JS --> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanWorkTaskHis.js"></script> <!-- 机车作业任务记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanDetectResultHis.js"></script> <!-- 检测结果记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanGanttHis.js"></script> <!-- 机车检修计划甘特图JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWordCardView_CommHis.js"></script> <!-- 作业工单显示界面信息JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanWorkCard.js"></script> <!-- 作业工单查询 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanCommHis.js"></script> <!-- 机车检修作业计划详细信息展示 -->

<script language="javascript" src="TrainWorkPlanWinQuery.js"></script>
<script language="javascript" src="VTrainAccessAccountQuery.js"></script>
<script language="javascript" src="TrainWorkPlanQuery.js"></script>
<script type="text/javascript">
	var NODE_STATUS_WKG = '<%= JobProcessNode.STATUS_UNSTART %>'		// 未开工
	var NODE_STATUS_YKG = '<%= JobProcessNode.STATUS_GOING %>'			// 已开工
	var NODE_STATUS_YWG = '<%= JobProcessNode.STATUS_COMPLETE %>'		// 已完工
	
	var PLAN_STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>'				// 待修
	var PLAN_STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>'	// 在修
	var SPLAN_TATUS_HANDLED = '<%= TrainWorkPlan.STATUS_HANDLED %>'		// 修竣
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>