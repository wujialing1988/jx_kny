<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.pczz.entity.ZbglPczz" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普查整治单</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/ExportFile.js"></script>
<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzItemToTraininfoWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/TrainNoSelectWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczz.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzItem.js"></script>
<script language="javascript">
	<%-- 任务状态-待发布 --%>
	var STATUS_TORELEASE = '<%=ZbglPczz.STATUS_TORELEASE%>';
	<%-- 任务状态-发布 --%>
	var STATUS_RELEASED = '<%=ZbglPczz.STATUS_RELEASED%>';
	<%-- 任务状态-归档 --%>
	var STATUS_COMPLETE = '<%=ZbglPczz.STATUS_COMPLETE%>';
	var STATUS_TORELEASE_CH = '<%=ZbglPczz.STATUS_TORELEASE_CH%>';	
	var STATUS_RELEASED_CH = '<%=ZbglPczz.STATUS_RELEASED_CH%>';
	var STATUS_COMPLETE_CH = '<%=ZbglPczz.STATUS_COMPLETE_CH%>';	

	<%-- 作业情况:已完毕 --%>
	var WORK_STATUS_END = <%=ZbglPczz.WORK_STATUS_END%>;
	<%-- 作业情况:作业中 --%>
	var WORK_STATUS_ONGOING = <%=ZbglPczz.WORK_STATUS_ONGOING%>;
	
	<%-- 提票附件上传目录 --%>
	var UPLOADPATH_PCZZ = '<%=ZbConstants.UPLOADPATH_PCZZ%>';
</script>
</head>
<body>
</body>
</html>