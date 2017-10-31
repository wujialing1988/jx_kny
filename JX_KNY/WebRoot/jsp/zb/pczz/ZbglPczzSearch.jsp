<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.pczz.entity.ZbglPczz" %>
<%@page import="com.yunda.zb.pczz.entity.ZbglPczzWI" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普查整治单</title>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzSearch.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzItemSearch.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/pczz/ZbglPczzWI.js"></script>
<script language="javascript">
    var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	<%-- 图片虚拟路径 --%>
	var imgpathx = imgpath + '/pjgl.gif';
	<%-- 任务状态-待发布 --%>
	var STATUS_TORELEASE = '<%=ZbglPczz.STATUS_TORELEASE%>';
	<%-- 任务状态-发布 --%>
	var STATUS_RELEASED = '<%=ZbglPczz.STATUS_RELEASED%>';
	<%-- 任务状态-归档 --%>
	var STATUS_COMPLETE = '<%=ZbglPczz.STATUS_COMPLETE%>';
	
	var STATUS_TORELEASE_CH = '<%=ZbglPczz.STATUS_TORELEASE_CH%>';	
	var STATUS_RELEASED_CH = '<%=ZbglPczz.STATUS_RELEASED_CH%>';
	var STATUS_COMPLETE_CH = '<%=ZbglPczz.STATUS_COMPLETE_CH%>';
	
	
    //整备任务单状态
	var RDPWI_STATUS_TODO = '<%=ZbglPczzWI.STATUS_TODO%>';
	var RDPWI_STATUS_HANDLING = '<%=ZbglPczzWI.STATUS_HANDLING%>';
	var RDPWI_STATUS_HANDLED = '<%=ZbglPczzWI.STATUS_HANDLED%>';
	
    var RDPWI_STATUS_TODO_CH = '<%=ZbglPczzWI.STATUS_TODO_CH%>';
	var RDPWI_STATUS_HANDLING_CH = '<%=ZbglPczzWI.STATUS_HANDLING_CH%>';
	var RDPWI_STATUS_HANDLED_CH = '<%=ZbglPczzWI.STATUS_HANDLED_CH%>';
	
	
		
</script>
</head>
<body>
</body>
</html>