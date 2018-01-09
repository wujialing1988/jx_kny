<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.freight.zb.plan.entity.ZbglRdpPlan" %>
<%@page import="com.yunda.frame.util.EntityUtil" %>


<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列检计划</title>
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<style type="text/css">
		
	.statusStyle{
		width: 50px;
		height: 30px;
		background-color:blue;
		border-radius:50%;
	}
	
	.spanScore {display:block;text-align:center;}
        .x-grid3-row td, .x-grid3-summary-row td{
            padding-right: 0px;
            padding-left: 0px;
            padding-top: 0px;
            padding-bottom: 0px;
        }
        .x-grid3-row {
            border-right-width: 0px;
            border-left-width: 0px;
            border-top-width: 0px;
            border-bottom-width: 0px;
        }
        .rowspan-grid .x-grid3-body .x-grid3-row {
            border:none;
            cursor:default;
            width:100%;
        }
        .rowspan-grid .x-grid3-header .x-grid3-cell{
           
            border-left: 2px solid #fff;
        }
        .rowspan-grid .x-grid3-body .x-grid3-row{
            overflow: hidden;
            border-right: 1px solid #ccc;
        }
        .rowspan-grid .x-grid3-body .x-grid3-cell {
            border: 1px solid #ccc;
            border-top:none;
            border-right:none;
        }
        .rowspan-grid .x-grid3-body .x-grid3-cell-first {
           
            border-left: 1px solid #fff;
        }
        .rowspan-grid .x-grid3-body .rowspan-unborder {
           
            border-bottoRowspanView.js
        .rowspan-grid .x-grid3-body .rowspan {
            border-bottom: 1px solid #ccc;
        }
        
        body .x-panel {
            margin-bottom:20px;
        }
        .icon-grid {
            background-image:url(../shared/icons/fam/grid.png) !important;
        }
        .icon-clear-group {
            background-image:url(../shared/icons/fam/control_rewind.png) !important;
        }
        #button-grid .x-panel-body {
            border:1px solid #99bbe8;
            border-top:0 none;
        }
        .add {
            background-image:url(../shared/icons/fam/add.gif) !important;
        }
        .option {
            background-image:url(../shared/icons/fam/plugin.gif) !important;
        }
        .remove {
            background-image:url(../shared/icons/fam/delete.gif) !important;
        }
        .save {
            background-image:url(../shared/icons/save.gif) !important;
        }        

</style>

<script type="text/javascript">

	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"10":request.getParameter("vehicleType")%>";<%-- 客货类型 --%>

	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
	
	var processTips;

	function showtip(){
		var el;
		el = Ext.getBody();
		processTips = new Ext.LoadMask(el,{
			msg:"正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	}
	
	function hidetip(){
		processTips.hide();
	}
	
	// 状态
	var STATUS_UNRELEASED = '<%= ZbglRdpPlan.STATUS_UNRELEASED %>'		// 未启动
	var STATUS_HANDLING = '<%= ZbglRdpPlan.STATUS_HANDLING %>'			// 已启动
	var STATUS_INTERRUPT = '<%= ZbglRdpPlan.STATUS_INTERRUPT %>'		// 中断
	var STATUS_DELAY = '<%= ZbglRdpPlan.STATUS_DELAY %>'				// 延期
	var STATUS_HANDLED = '<%= ZbglRdpPlan.STATUS_HANDLED %>'			// 已完工
	
	// 站点ID
	var siteID =  '<%= EntityUtil.findSysSiteId(null) %>';

</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-TrainInspectionPlan.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/PassengerTransport/i18n-lang-PassengerInspectionPlan.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/plankc/TrainDemandSelectWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/plankc/ZbglRdpPlanRecord.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/plankc/ZbglRdpPlanRecordCL.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/plankc/ZbglRdpPlan.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/plankc/ZbglRdpPlanDesign.js"></script>

</head>
<body>

</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>