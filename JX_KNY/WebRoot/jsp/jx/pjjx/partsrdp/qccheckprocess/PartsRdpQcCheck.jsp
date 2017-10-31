<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR" %>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager"%>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件检修质量检验</title>
<script type="text/javascript">
	// 获取所有的质量检查项配置
	var objList = <%=JSONUtil.write(QCItemManager.getQCContent(null))%>
</script>
<style type="text/css">
	.moveUpIcon {background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_up.png) !important;}
	.moveDownIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_down.png) !important;}
	.moveTopIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_top.png) !important;}
	.moveBottomIcon{background-image:url(<%=request.getContextPath()%>/frame/resources/images/toolbar/move_bottom.png) !important;}
</style>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpRecordDI.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpRecordRI.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheckSub.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheckProcess.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheckBJ.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheckCJ.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheck.js"></script>
<script type="text/javascript">
	<%-- 抽检/必检 --%>
	var CHECK_WAY_CJ = '<%= QCItem.CONST_INT_CHECK_WAY_CJ%>'			// 抽检
	var CHECK_WAY_BJ = '<%= QCItem.CONST_INT_CHECK_WAY_BJ%>'			// 必检
	
	<%-- 质量检查姿态 --%>
	var QC_STATUS_DCL = '<%= PartsRdpQR.CONST_STR_STATUS_DCL%>'			// 待处理
	var QC_STATUS_YCL = '<%= PartsRdpQR.CONST_STR_STATUS_YCL%>'			// 已处理
	
	var PARTS_RDP_STATUS_DLQ = '<%=IPartsRdpStatus.CONST_STR_STATUS_DLQ%>';			// 作业工单状态 - 待领取
	var PARTS_RDP_STATUS_DCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_DCL%>';			// 作业工单状态 - 待处理
	var PARTS_RDP_STATUS_YCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_YCL%>';			// 作业工单状态 - 已处理
	<%-- 已处理状态包含以下状态 --%>
	var PARTS_RDP_STATUS_XJ = '<%=IPartsRdpStatus.CONST_STR_STATUS_XJ%>';			// 作业工单状态 - 修竣
	var PARTS_RDP_STATUS_ZLJYZ = '<%=IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ%>';		// 作业工单状态 - 质量检验中
	
	<%-- 检测项是否必填 --%>
	var IS_BLANK_YES = '<%= RecordDI.CONST_INT_IS_BLANK_YES %>'							// 是否必填 - 是
	var IS_BLANK_NO = '<%= RecordDI.CONST_INT_IS_BLANK_NO %>'							// 是否必填 - 否
	
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