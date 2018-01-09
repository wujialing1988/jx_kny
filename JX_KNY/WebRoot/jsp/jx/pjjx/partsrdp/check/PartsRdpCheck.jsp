<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI" %>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI"%>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem" %>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修竣配件合格验收</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var teamOrgName = '${sessionScope.org.orgname}'; <%-- 当前登录人员部门名称 --%>
	var PARTS_STATUS_ZC = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_ZC%>';  //配件状态--在册
	var STATUS_DYS = '<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_DYS%>';  //兑现单状态--待验收
	var STATUS_JXHG = '<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_JXHG%>';  //兑现单状态--检修合格
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var imgpathx = imgpath + '/delete.gif';
	var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
	var isClickForeman = false;//是否已点击“已派工”tab， 默认为false未点击，为true已点击
	
</script>


<script type="text/javascript">
	// 获取需要指派的质量检查项配置
	var objList = <%=JSONUtil.write(QCItemManager.getQCContent(QCItem.CONST_INT_IS_ASSIGN_Y))%>
	
	// 限定人员选择控件的组织机构到当前登录人员所在班组
	var systemOrgid = "${sessionScope.org.orgid}";						// 当前登录用户所在班组ID
	var systemOrgname = "${sessionScope.org.orgname}"; 					// 当前登录用户所在班组名称 
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpTree.js"></script>
<%-- 检修工艺工单处理--%> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckTecWS.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/TecCardCheck.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckTecCard.js"></script>

<%-- 检修记录工单处理 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckRecordDI.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckRecordRI.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/RecordCardCheck.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckRecordCard.js"></script>

<%-- 提票工单处理 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/NoticeCheck.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckNotice.js"></script>

<%-- 物料消耗情况 --%>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheckExpendMatQuery.js"></script>

<%-- 修竣配件合格验收主界面 --%>

<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/check/PartsRdpCheck.js"></script>
<script type="text/javascript">
	
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