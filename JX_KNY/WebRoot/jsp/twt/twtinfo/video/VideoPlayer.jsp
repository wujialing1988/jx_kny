<%@include file="/frame/jspf/header.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频播放</title>
<%
	String siteID = request.getParameter("siteID");				// 获取URL传入的参数站场ID
	if (null == siteID) siteID = "";				// *开发测试默认值
	String videoCode = request.getParameter("videoCode");		// 获取URL传入的参数视频点编码
	if (null == videoCode) videoCode = "";			// *开发测试默认值
	
	String videoNvrIDX = request.getParameter("videoNvrIDX");		// 获取URL传入的参数NVR主键
	if (null == videoNvrIDX) videoNvrIDX = "";			// *开发测试默认值
	String chanelID = request.getParameter("chanelID");				// 获取URL传入的参数NVR主键
	if (null == chanelID) chanelID = "";			// *开发测试默认值
	
	/////////////////////// 视频预览类型 ///////////////////////
	// 常量定义
	String previewType_0 = "0"; 		// 台位图上右键【视频预览】类型
	String previewType_1 = "1"; 		// 台位图上右键视频监控绑定页面【视频预览】类型
	
	String previewType = request.getParameter("previewType");	// 视频预览类型
	if (null == previewType) previewType = previewType_0;	// 视频预览类型默认为“台位图上右键【视频预览】类型”
%>
<script type="text/javascript">
	var siteID = '<%= java.net.URLDecoder.decode(siteID, "utf-8") %>';
	var videoCode = '<%= java.net.URLDecoder.decode(videoCode, "utf-8") %>';
	
	var videoNvrIDX = '<%= java.net.URLDecoder.decode(videoNvrIDX, "utf-8") %>';
	var chanelID = '<%= java.net.URLDecoder.decode(chanelID, "utf-8") %>';
	
	/////////////////////// 视频预览类型 ///////////////////////
	var previewType = '<%= java.net.URLDecoder.decode(previewType, "utf-8") %>';
	var previewType_0 = '<%= previewType_0 %>';
	var previewType_1 = '<%= previewType_1 %>';
</script>
<script type="text/javascript" src="<%= ctx %>/jsp/twt/twtinfo/video/cn/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctx %>/jsp/twt/twtinfo/video/codebase/webVideoCtrl.js"></script>
<script type="text/javascript" src="<%= ctx %>/jsp/twt/twtinfo/video/VideoPlayer.js"></script>
</head>
<body>
	<div id="divPlugin" class="plugin"></div>
</body>
</html>