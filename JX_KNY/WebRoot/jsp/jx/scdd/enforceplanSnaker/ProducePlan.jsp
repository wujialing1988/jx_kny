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
<iframe name="inner_frame" width=100% height=95%></iframe>  
<form id="reportForm" method="POST" action="" target="inner_frame">
</form>  
</body>
<script type="text/javascript">
	var idx = '${param.idx}';
	var reportTotalStr = '${param.totalStr}';
	var url = getReportEffectivePath("/scdd/ProducePlan.cpt&idx="+idx
					        	+"&totalStr="+reportTotalStr);
       
	window.onload = function(){
		document.getElementById("reportForm").action = encodeURI(url);
		document.getElementById("reportForm").submit();
	}
</script>
</html>
