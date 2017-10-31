
<jsp:directive.page import="java.net.URLDecoder"/><%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>摄像头绑定(台位图)</title>
<style type="text/css">
	.previewIcon{background-image:url(<%= request.getContextPath() %>/frame/resources/images/toolbar/preview.png) !important;}
</style>
<%
	String siteID = request.getParameter("siteID");				// 获取URL传入的参数站场ID
	if (null == siteID) siteID = "A";				// *开发测试默认值
	String videoCode = request.getParameter("videoCode");		// 获取URL传入的参数视频点编码
	if (null == videoCode) videoCode = "1";			// *开发测试默认值
	String videoName = request.getParameter("videoName");		// 获取URL传入的参数视频点编码
	if (null == videoName) videoName = "摄像头01";	// *开发测试默认值
%>
<script type="text/javascript">
	var siteID = '<%= java.net.URLDecoder.decode(siteID, "utf-8") %>';
	var videoCode = '<%= java.net.URLDecoder.decode(videoCode, "utf-8") %>';
	var videoName = '<%= java.net.URLDecoder.decode(videoName, "utf-8")%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/twtinfo/SiteVideoBind.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>