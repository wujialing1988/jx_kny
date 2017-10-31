<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>故障提票统计</title>
		<script type="text/javascript">
			// Ext图表flash资源
			Ext.chart.Chart.CHART_URL = ctx + '/frame/resources/ext-3.4.0/resources/charts.swf';
		</script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts-liquidfill.js"></script>
		<script type="text/javascript" src="GztpStatistics.js"></script>
	</head>
	<body>
	</body>
</html>
