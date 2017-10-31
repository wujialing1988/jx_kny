<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件保有量统计报表</title>

<link rel="stylesheet" type="text/css" href="/WebReport/ReportServer?op=resource&resource=/com/fr/web/core/css/page.css"/>   

</head>
<body>
<iframe name="inner_frame" frameborder=0 width=100% height=100%></iframe>  
<form id="reportForm" method="POST" action="" target="inner_frame">
</form>  
</body>
<script type="text/javascript">
	var ctx = '<%=ctx%>';
	ctx = ctx.substring(1);
	var checkurl = getReportEffectivePath("/jxgc/TrainWorkPlanToday.cpt?ctx=" + ctx);
	window.onload = function(){
		document.getElementById("reportForm").action = encodeURI(checkurl);
		document.getElementById("reportForm").submit();
	}
</script>
</html>
