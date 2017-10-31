<%@include file="/frame/jspf/header.jspf"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备点检目录</title>
	<style type="text/css">
		#inspect-info {
			padding-left: 5px;
			height: 25px;
			line-height: 25px;
		}
		.count_tip {
			margin: 0 2px;
			background: rgb(233, 107, 24);
			color: #fff;
			min-width: 17px;
			height: 17px;
			text-align: center;
			line-height: 17px;
			border-radius: 8px;
			display: inline-block;
			font-size: small;
			box-shadow: 1px 1px 2px #2F98D3;
		}
	</style>	
	<script type="text/javascript">			
		/** 设备动态-调入1 */
		var DYNAMIC_IN = '<%= EquipmentPrimaryInfo.DYNAMIC_IN %>';
	    /** 设备动态-新购3 */
		var DYNAMIC_NEW_BUY = '<%= EquipmentPrimaryInfo.DYNAMIC_NEW_BUY %>';
	</script>
	<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/sb/base/js/Echart-3/echarts-liquidfill.js"></script>
	<script type="text/javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
	<script type="text/javascript" src="PointCheckCatalog.js"></script>
	<script type="text/javascript" src="PointCheckStatistics.js"></script>
	<script type="text/javascript" src="EquipmentPrimaryInfoSelect.js"></script>
</head>


<body>
</body>
</html>
