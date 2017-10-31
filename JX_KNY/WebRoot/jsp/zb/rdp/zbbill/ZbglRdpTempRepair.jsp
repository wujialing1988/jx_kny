<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %> 
<%@page import="com.yunda.zb.common.ZbConstants" %>
<%@page import="com.yunda.frame.util.EntityUtil" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>转临修</title>
<script type="text/javascript">		
	//提票状态
	var STATUS_INIT = '<%=ZbglTp.STATUS_INIT%>';
	var STATUS_DRAFT = '<%=ZbglTp.STATUS_DRAFT%>';
	var STATUS_OPEN = '<%=ZbglTp.STATUS_OPEN%>';
	var STATUS_OVER = '<%=ZbglTp.STATUS_OVER%>';
	
	var STATUS_INIT_CH = '<%=ZbglTp.STATUS_INIT_CH%>';	
	var STATUS_DRAFT_CH = '<%=ZbglTp.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=ZbglTp.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=ZbglTp.STATUS_OVER_CH%>';
	
	var REPAIRRESULT_ZLX = <%=ZbglTp.REPAIRRESULT_ZLX%>;
	
	var empname ='${ sessionScope.emp.empname}';
	
	var UPLOADPATH_TP = '<%=ZbConstants.UPLOADPATH_TP%>';
	
	var siteID = '<%=EntityUtil.findSysSiteId("")%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->
<script language="javascript" src="TempRepairWin.js"></script>
<script language="javascript" src="ZbglRdp.js"></script>
<script language="javascript" src="ZbglRdpTempRepair.js"></script>
</head>
<body>
</body>
</html>