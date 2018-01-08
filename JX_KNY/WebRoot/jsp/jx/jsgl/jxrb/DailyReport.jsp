<%@include file="/frame/jspf/header.jspf"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
try {
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>机车检修日报</title>
		<style type="text/css">
			#id_train_work_plan_info table {
				padding-left:30px;
			 	width: 100%;
			 	font-size: 12px;
			}
			#id_train_work_plan_info table tr td {
    			padding: 3px 0;
    			width: 100%;
			}
			#id_train_work_plan_info table tr td span {
				color: gray;
				width: 90px;
				text-align: right;
				display: inline-block;
			}
			
			#id_train_work_plan_base_info table {
				padding-left:30px;
			 	width: 100%;
			 	font-size: 12px;
			}
			#id_train_work_plan_base_info table tr td {
    			padding: 3px 0;
    			width: 50%;
			}
			#id_train_work_plan_base_info table tr td span {
				color: gray;
				width: 90px;
				text-align: right;
				display: inline-block;
			}
		</style>
		
		<script language="javascript">
			var INITIALIZE = '<%=TrainWorkPlan.STATUS_NEW%>';  				// 状态-初始化
			var ONGOING = '<%=TrainWorkPlan.STATUS_HANDLING%>';  			// 状态-处理中
			var COMPLETE = '<%=TrainWorkPlan.STATUS_HANDLED%>';  			// 状态-已处理
			var TERMINATED = '<%=TrainWorkPlan.STATUS_NULLIFY%>';  			// 状态-终止
			
			var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
			
			// 自定义vtype，验证日期范围
			Ext.applyIf(Ext.form.VTypes, {
				dateRange: function(_v, field) {
					if (field.dateRange) {
						// 开始日期
						var startId = field.dateRange.startDate;
						this.startField = Ext.getCmp(startId);
						var startDate = this.startField.getValue();
						// 结束日期
						var endId = field.dateRange.endDate;
						this.endField = Ext.getCmp(endId);
						var endDate = this.endField.getValue();
						if (Ext.isEmpty(startDate) || Ext.isEmpty(endDate)) {
							return true;
						}
						return startDate <= endDate ? true : false
					}
				},
				// 验证失败信息
				dateRangeText: "开始日期不能大于结束日期"
			});
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
    	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/jsgl/jxrb/TrainWorkPlan.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/jsgl/jxrb/DailyReportAdd.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/jsgl/jxrb/DailyReport.js"></script>
	</head>
	<body>
	</body>
</html>
<%
} catch (Exception e) {
    e.printStackTrace();
}
%>
