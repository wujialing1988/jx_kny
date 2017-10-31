<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/EdoProject.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.frame.common.JXConfig" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.entity.PartsRdp" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>


<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业记录当查询</title>

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
<script type="text/javascript">
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//在修
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//修竣
	var rdp_status_nullify = '<%=TrainWorkPlan.STATUS_NULLIFY%>';//终止
	//机车检修提票状态
	var STATUS_DRAFT = '<%=FaultTicket.STATUS_DRAFT%>';//未处理
	var STATUS_OPEN = '<%=FaultTicket.STATUS_OPEN%>';//处理中
	var STATUS_OVER = '<%=FaultTicket.STATUS_OVER%>';//已处理
	
	var siteID ='<%=JXConfig.getInstance().getSynSiteID()%>';
/** 配件检修作业状态 */
    var STATUS_WQD = '<%= PartsRdp.STATUS_WQD%>';		/* 状态-未启动 */
    var STATUS_JXZ = '<%= PartsRdp.STATUS_JXZ%>';		/* 状态-检修中 */
    var STATUS_DYS = '<%= PartsRdp.STATUS_DYS%>';		/* 状态-待验收 */
    var STATUS_YZZ = '<%= PartsRdp.STATUS_YZZ%>';		/* 状态-已终止 */
    var STATUS_WFXF = '<%= PartsRdp.STATUS_WFXF%>';		/* 状态-无法修复 */
    var STATUS_JXHG = '<%= PartsRdp.STATUS_JXHG%>';		/* 状态-检修合格 */
    
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
	<%-- 停时图路径 --%>
	var diaImg = '<%=ctx%>/jsp/jx/jxgc/workhis/workplan/diagramImage.jpg';
	var zajcIdx = '';	
	var trainTypeAndNo = '';		
    var repairClassAndTime = '';	
</script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/print/TrainWorkPlanWorkCard.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/print/TrainWorkPlanPrint.js"></script> <!-- 机车检修作业计划查询 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>