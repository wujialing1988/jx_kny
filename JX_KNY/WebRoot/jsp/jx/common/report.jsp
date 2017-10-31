<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String url = request.getParameter("url");
	String title = request.getParameter("title");
	String param = request.getParameter("param");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=title%></title>
		<!-- Modified by hetao on 2016-04-11 未查询到该文件，因此暂时注释掉
		<link rel="stylesheet" type="text/css" href="/WebReport/ReportServer?op=resource&resource=/com/fr/web/core/css/page.css"/>   
	 	-->
	</head>
	<body>
		<iframe id="inner_frame" width="100%" height="95%" frameborder='0'></iframe>
	</body>
	<script type="text/javascript">
		var url = '<%=url %>';
		var param = '<%=param %>';
		if(param != "" && param != 'null'){
			url = url + param ; 
		}
		url = getReportEffectivePath(url);
		window.onload = function(){
			document.getElementById("inner_frame").src = encodeURI(url);
		}
	</script>
</html>