<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生产计划</title>

<link rel="stylesheet" type="text/css" href="/WebReport/ReportServer?op=resource&resource=/com/fr/web/core/css/page.css"/>   

</head>
<body>
<iframe id="inner_frame" width="100%" height="95%" frameborder="0"></iframe>  
</body>
<script type="text/javascript">
	var idx = '${param.idx}';
	var url = getReportEffectivePath("/scdd/ProducePlan_JWD.cpt&idx="+idx);
       
	window.onload = function(){
		document.getElementById("inner_frame").src = encodeURI(url);
	}
</script>
</html>
