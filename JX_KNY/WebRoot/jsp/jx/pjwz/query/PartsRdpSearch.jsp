<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.entity.PartsRdp" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件检修结果查询</title>

<link href="css/PartsRdpSearch.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript">

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
	
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>

<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsDismantleHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsInstallHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecWS.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecCardDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpTecCard.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpQR.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpRecordCardDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpRecordCard.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpNotice.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpExpendMatQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpInfo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpPrinter.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/query/PartsRdpSearch.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>
