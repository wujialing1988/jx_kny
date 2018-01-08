<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanYear"%>
<%@page import="com.yunda.sb.repair.plan.entity.RepairPlanMonth"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>设备检修月计划（new）</title>
		<style>
			/* 时间周期的样式定义 */
			.peroid {
				font-size: small;
				background-color: #000;
				margin-top:10px;
				color: #FFF;
			}
		</style>
		<style type="text/css">
		    body, html {
		      font-family: sans-serif;
		    }
		
		    /* alternating column backgrounds */
		    .vis.timeline .timeaxis .grid.odd {
		      background: #f5f5f5;
		    }
		
		    /* gray background in weekends, white text color */
		    .vis.timeline .timeaxis .grid.saturday,
		    .vis.timeline .timeaxis .grid.sunday {
		      background: gray;
		    }
		    .vis.timeline .timeaxis .text.saturday,
		    .vis.timeline .timeaxis .text.sunday {
		      color: white;
		    }
		    
		    
			/** ******** 已开工显示样式定义开始 ******** */
			.vis.timeline .item.yxf {
				color: #FFF;
				background-color: rgb(95, 228, 70);
		      	border-color: black;
		    }
			/** ******** 已开工显示样式定义结束 ******** */
	
		    /* ******** 条目被选选择时的样式定义开始 ******** */
		    .vis.timeline .item.selected {
		      background-color: rgb(51, 153, 255);
		      border-color: black;
		      color: black;
		      box-shadow: 0 0 10px gray;
		    }
    		/* ******** 条目被选选择时的样式定义结束 ******** */
		</style>
		
		<script type="text/javascript">
			/** 修程  - 小修【1】 */
			var REPAIR_CLASS_SMALL = '<%= RepairPlanYear.REPAIR_CLASS_SMALL%>';
			/** 修程  - 中修【2】 */
			var REPAIR_CLASS_MEDIUM = '<%= RepairPlanYear.REPAIR_CLASS_MEDIUM%>';
			/** 修程  - 项修【3】 */
			var REPAIR_CLASS_SUBJECT = '<%= RepairPlanYear.REPAIR_CLASS_SUBJECT%>';
			
			/** 计划状态 - 未下发【0】 */
			var PLAN_STATUS_WXF = '<%= RepairPlanMonth.PLAN_STATUS_WXF%>';
			/** 计划状态 - 已下发【0】 */
			var PLAN_STATUS_YXF = '<%= RepairPlanMonth.PLAN_STATUS_YXF%>';
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
		<script type="text/javascript" src="<%= ctx %>/jsp/sb/base/js/SingleFieldCombo.js"></script>
		<script type="text/javascript" src="CreatePlanMonth.js"></script>
		<script type="text/javascript" src="VRepairPlanMonth.js"></script>
	</head>
	<body>
	</body>
</html>
