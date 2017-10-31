<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>临碎修提票查询</title>
<%
	String trainTypeIDX = request.getParameter("trainTypeIDX");				// 获取URL传入的参数车型主键
	if (null == trainTypeIDX) trainTypeIDX = "232";				// *开发测试默认值
	String trainTypeShortName = request.getParameter("trainTypeShortName");	// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "HXD2";	// *开发测试默认值
	String trainNo = request.getParameter("trainNo");						// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "7001";							// *开发测试默认值	
%>
<script language="javascript">
	var siteID = '<%=EntityUtil.findSysSiteId("")%>';
	/**检修类型：碎修*/
    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
    /**检修类型：临修*/
    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
    
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
	var tpStatus = [[STATUS_INIT,STATUS_INIT_CH],[STATUS_DRAFT,STATUS_DRAFT_CH],[STATUS_OPEN,STATUS_OPEN_CH],[STATUS_OVER,STATUS_OVER_CH],[STATUS_CHECK,STATUS_CHECK_CH]];
	
	var UPLOADPATH_TP = '<%=ZbConstants.UPLOADPATH_TP%>';
	var UPLOADPATH_TP_IMG = '<%=ZbConstants.UPLOADPATH_TP_IMG%>';
	
	var trainTypeIDX = '<%= trainTypeIDX %>';
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
	
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->
<script language="javascript" src="TWTZbglTpSearch.js"></script>
<script language="javascript" src="ZbglTpAudio.js"></script>
</head>
<body>
</body>
</html>