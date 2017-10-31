<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI" %>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI"%>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem" %>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager"%>
<%@page import="com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI"%>
<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件检修任务处理</title>
<style type="text/css">
	.moveUpIcon {background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_up.png) !important;}
	.moveDownIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_down.png) !important;}
	.moveTopIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_top.png) !important;}
	.moveBottomIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_bottom.png) !important;}
</style>
<script type="text/javascript">
	// 获取需要指派的质量检查项配置
	var objList = <%=JSONUtil.write(QCItemManager.getQCContent(QCItem.CONST_INT_IS_ASSIGN_Y))%>
	
	// 限定人员选择控件的组织机构到当前登录人员所在班组
	var systemOrgid = "${sessionScope.org.orgid}";						// 当前登录用户所在班组ID
	var systemOrgname = "${sessionScope.org.orgname}"; 					// 当前登录用户所在班组名称 
	
	
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/PartsCheckItemDataSelect.js"></script>
<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/TeamEmployeeSelect.js"></script>

<%-- 检修记录工单处理 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/recordinst/KshTestPartsRdpRecordDI.js"></script>
<%--<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/recordinst/PartsRdpRecordDI.js"></script>--%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/recordinst/PartsRdpRecordRI.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/recordinst/RecordCardProcess.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/recordinst/PartsRdpRecordCard.js"></script>

<%-- 检修工艺工单处理 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/tecinst/PartsRdpTecWS.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/expendmat/PartsRdpExpendMat.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/tecinst/TecCardProcess.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/tecinst/PartsRdpTecCard.js"></script>

<%-- 提票工单处理 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpnotice/NoticeProcess.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpnotice/PartsRdpNotice.js"></script>

<%-- 物料消耗情况 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/expendmat/PartsRdpExpendMatQuery.js"></script>

<%-- 配件检修任务处理主界面 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpBatchProcess.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpProcess.js"></script>
<script type="text/javascript">
	
	var PARTS_RDP_STATUS_WKF = '<%=IPartsRdpStatus.CONST_STR_STATUS_WKF%>';			// 作业工单状态 - 未开放
	var PARTS_RDP_STATUS_DLQ = '<%=IPartsRdpStatus.CONST_STR_STATUS_DLQ%>';			// 作业工单状态 - 待领取
	var PARTS_RDP_STATUS_DCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_DCL%>';			// 作业工单状态 - 待处理
	var PARTS_RDP_STATUS_YCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_YCL%>';			// 作业工单状态 - 已处理
	<%-- 已处理状态包含以下状态 --%>
	var PARTS_RDP_STATUS_XJ = '<%=IPartsRdpStatus.CONST_STR_STATUS_XJ%>';			// 作业工单状态 - 修竣
	var PARTS_RDP_STATUS_ZLJYZ = '<%=IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ%>';		// 作业工单状态 - 质量检验中
	
	var TEC_WP_STATUS_WCL = '<%=PartsRdpTecWS.CONST_STR_STATUS_WCL%>';				// 配件检修工序实例状态 - 未处理
	var TEC_WP_STATUS_YCL = '<%=PartsRdpTecWS.CONST_STR_STATUS_YCL%>';				// 配件检修工序实例状态 - 已处理
	
	var RECORD_RI_STATUS_WCL = '<%= PartsRdpRecordRI.CONST_STR_STATUS_WCL %>';			// 配件检修检测项实例状态 - 未处理
	var RECORD_RI_STATUS_YCL = '<%= PartsRdpRecordRI.CONST_STR_STATUS_YCL %>';			// 配件检修检测项实例状态 - 已处理
	var REPAIR_RESULT_HG = '<%= PartsRdpRecordRI.CONST_STR_REPAIR_RESULT_HG %>';		// 配件检修检测项检测结果 - 合格
	var REPAIR_RESULT_LH = '<%= PartsRdpRecordRI.CONST_STR_REPAIR_RESULT_LH %>';		// 配件检修检测项检测结果 - 良好
	
	<%-- 质量检查是否指派 --%>
	var IS_ASSIGN_Y = '<%= QCItem.CONST_INT_IS_ASSIGN_Y %>'							// 是否指派 - 是
	var IS_ASSIGN_N = '<%= QCItem.CONST_INT_IS_ASSIGN_N %>'							// 是否指派 - 否
	
	<%-- 检测项是否必填 --%>
	var IS_BLANK_YES = '<%= RecordDI.CONST_INT_IS_BLANK_YES %>'							// 是否必填 - 是
	var IS_BLANK_NO = '<%= RecordDI.CONST_INT_IS_BLANK_NO %>'							// 是否必填 - 否
	
	<%-- 回退标识 --%>
	var IS_BACK_YES = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_YES %>'					// 回退标识 - 是
	var IS_BACK_NO = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_NO %>'						// 回退标识 - 否
	
	var RDP_STATUS_WFXF = '<%= IPartsRdpStatus.CONST_STR_STATUS_WFXF %>'				// 作业计划单状态 - 无法修复
	var RDP_STATUS_JXDYS = '<%= IPartsRdpStatus.CONST_STR_STATUS_JXDYS %>'				// 作业计划单状态 - 修竣待验收
	
	
	// 定义不同业务模块的业务代码，用以区分是哪个模块的批量销活处理
	var BUSINESS_CODE_TEC_CARD = 'TEC_CARD';											// 检修作业工单 批量销活业务代码
	var BUSINESS_CODE_RECORD_CARD = 'RECORD_CARD';										// 检修记录工单 批量销活业务代码
	var BUSINESS_CODE_NOTICE = 'NOTICE';												// 提票工单 批量销活业务代码
	
	//定义检测项结果数据来源
	var HANDINPUT = '<%= PartsRdpRecordDI.HANDINPUT %>'				// 代表该条数据通过手工录入的方式 值：0
	var VISUALINPUT = '<%= PartsRdpRecordDI.VISUALINPUT %>'				// 代表该条数据通过可视化系统选择的方式 值：1
	
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