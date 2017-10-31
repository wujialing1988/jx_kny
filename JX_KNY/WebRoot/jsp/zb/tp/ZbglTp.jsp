<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JT6提票</title>
<script type="text/javascript">	
	var partsBuildUpTypeIdx = "";//组成型号主键	
	var partsBuildUpTypeName = "";//组成型号名称
	
	/** 全局变量 S */
	var trainNo = '';
	var trainTypeIDX = '';
	var trainTypeShortName = '';
	var dId = '';
	var dName = '';	
	var warningDesc = '';
	var OTHERID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.OTHERID%>";//”其它“故障主键ID
	var CUSTOMID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.CUSTOMID%>";//*自定义故障现象主键（-1111）
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
    /** 全局变量 E */
	
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
	
	var tpStatus = [[STATUS_INIT,STATUS_INIT_CH],[STATUS_DRAFT,STATUS_DRAFT_CH]];
	
	var UPLOADPATH_TP = '<%=ZbConstants.UPLOADPATH_TP%>';
	
	/**检修类型：碎修*/
    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
    /**检修类型：临修*/
    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
    
    /** 子系统编码：00货车列检，01客车列检，10货车计划修，11客车计划修 */
    var subSysCode = '<%=request.getParameter("subSysCode") %>';
    /** 车辆类型：10货车，20客车 */
    var vehicleType = '<%=request.getParameter("vehicleType") %>';
</script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="ZbglTpWin.js"></script>
<script language="javascript" src="ZbglTp.js"></script>
</head>
<body>
</body>
</html>