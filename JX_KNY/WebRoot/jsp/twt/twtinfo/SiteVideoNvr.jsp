<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频监控信息维护</title>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/twt/twtinfo/SiteVideoNvrChanel.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/twt/twtinfo/SiteVideoNvr.js"></script>
<script type="text/javascript">
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	var deleteIcon = imgpath + '/delete.gif';
	var addIcon = imgpath + '/add.gif';
</script>
</head>
<body>
</body>
</html>