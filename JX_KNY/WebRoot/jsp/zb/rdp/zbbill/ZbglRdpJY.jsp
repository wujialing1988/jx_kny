<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@page import="com.yunda.zb.common.ZbConstants" %> 
<%@page import="com.yunda.zb.rdp.zbbill.entity.ZbglRdp" %> 
<%@page import="com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.common.JXConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车整备合格交验</title>
<link rel="stylesheet" href="<%=ctx%>/frame/resources/lightbox/css/lightbox.min.css">
<style type="text/css">
	.RdpWi_row_green{
		background: green;
	}
	.RdpWi_row_red{
		background: red;
	}
</style>
<script type="text/javascript">
	//检修类型
	var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
	var REPAIRCLASS_LX = '<%=ZbConstants.REPAIRCLASS_LX%>';
	//整备单状态
	var STATUS_HANDLING = '<%=ZbglRdp.STATUS_HANDLING%>';
	var STATUS_HANDLED = '<%=ZbglRdp.STATUS_HANDLED%>';
	//整备任务单状态
	var RDPWI_STATUS_TODO = '<%=ZbglRdpWi.STATUS_TODO%>';
	var RDPWI_STATUS_HANDLING = '<%=ZbglRdpWi.STATUS_HANDLING%>';
	var RDPWI_STATUS_HANDLED = '<%=ZbglRdpWi.STATUS_HANDLED%>';
	//整备任务单类型
	var WICLASS_ZBFW = '<%=ZbglRdpWi.WICLASS_ZBFW%>';
	var WICLASS_ZLCS = '<%=ZbglRdpWi.WICLASS_ZLCS%>';
	var WICLASS_YJ = '<%=ZbglRdpWi.WICLASS_YJ%>';
	var WICLASS_ZBFW_CH = '<%=ZbglRdpWi.WICLASS_ZBFW_CH%>';
	var WICLASS_ZLCS_CH = '<%=ZbglRdpWi.WICLASS_ZLCS_CH%>';
	var WICLASS_YJ_CH = '<%=ZbglRdpWi.WICLASS_YJ_CH%>';
	//提票状态
	var STATUS_INIT = '<%=ZbglTp.STATUS_INIT%>';
	var STATUS_DRAFT = '<%=ZbglTp.STATUS_DRAFT%>';
	var STATUS_OPEN = '<%=ZbglTp.STATUS_OPEN%>';
	var STATUS_OVER = '<%=ZbglTp.STATUS_OVER%>';
	var STATUS_CHECK = '<%=ZbglTp.STATUS_CHECK%>';
	
	var STATUS_INIT_CH = '<%=ZbglTp.STATUS_INIT_CH%>';	
	var STATUS_DRAFT_CH = '<%=ZbglTp.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=ZbglTp.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=ZbglTp.STATUS_OVER_CH%>';
	var STATUS_CHECK_CH = '<%=ZbglTp.STATUS_CHECK_CH%>';
	
	var UPLOADPATH_TP = '<%=ZbConstants.UPLOADPATH_TP%>';
	
	//任务类型
	var TASKTYPE_SANDING = '<%=ZbConstants.TASKTYPE_SANDING%>';
	var TASKTYPE_HANDOVER = '<%=ZbConstants.TASKTYPE_HANDOVER%>';
	var TASKTYPE_RDP = '<%=ZbConstants.TASKTYPE_RDP%>';
	var TASKTYPE_TP = '<%=ZbConstants.TASKTYPE_TP%>';
	var TASKTYPE_LWFX = '<%=ZbConstants.TASKTYPE_LWFX%>';
	var TASKTYPE_PCZZ = '<%=ZbConstants.TASKTYPE_PCZZ%>';
	var TASKTYPE_CLEAN = '<%=ZbConstants.TASKTYPE_CLEAN%>';
	
	var empname ='${ sessionScope.emp.empname}';
	var siteID ='<%=JXConfig.getInstance().getSynSiteID()%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/trainonsand/ZbglSandingSearch.js"></script><!--机车上砂表单  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/trainhandover/ZbglHoCaseItemForCheck.js"></script><!--机车交接列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/rdp/zbtaskbill/ZbglRdpWi.js"></script><!--整备任务单列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/rdp/zbtaskbill/ZbglRdpWidi.js"></script><!--数据项列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/rdp/zbtaskbill/ZbglRdpWiAudio.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/rdp/zbtaskbill/ImgDataView.js"></script><!--图片列表列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/ZbglTpJY.js"></script><!--提票单列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/ZbglTpException.js"></script><!--提票例外放行列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzWISearch.js"></script><!--普查整治列表  -->
<script language="javascript" src="<%=ctx %>/jsp/zb/trainclean/ZbglCleanFormJY.js"></script><!--机车保洁表单  -->
<script language="javascript" src="ZbglRdpJYWin.js"></script><!--交验窗口  -->
<script language="javascript" src="ZbglRdpJY.js"></script><!--主界面列表  -->
</head>
<body>
    <script language="javascript" src= "<%=ctx%>/frame/resources/lightbox/js/lightbox-plus-jquery.min.js"></script>
</body>
</html>