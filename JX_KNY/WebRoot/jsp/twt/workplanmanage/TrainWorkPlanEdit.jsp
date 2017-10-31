<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>编辑机车作业计划(台位图)</title>
<%
	String trainTypeIDX = request.getParameter("trainTypeIDX");				// 获取URL传入的参数车型主键
	if (null == trainTypeIDX) trainTypeIDX = "161";				// *开发测试默认值
	String trainTypeShortName = request.getParameter("trainTypeShortName");	// 获取URL传入的参数车型简称
	if (null == trainTypeShortName) trainTypeShortName = "HXN5";	// *开发测试默认值
	String trainNo = request.getParameter("trainNo");						// 获取URL传入的参数车号
	if (null == trainNo) trainNo = "0009";							// *开发测试默认值	
%>
<script type="text/javascript">
	var trainTypeIDX = '<%= trainTypeIDX %>';
	var trainTypeShortName = '<%= trainTypeShortName %>';
	var trainNo = '<%= trainNo %>';
	
	var STATUS_NEW = '<%= TrainWorkPlan.STATUS_NEW %>';					// 状态-未启动
	var STATUS_HANDLING = '<%= TrainWorkPlan.STATUS_HANDLING %>';		// 状态-处理中
	
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	var workPlanIDX = '';
	var workPlanEntity = {};
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<%@include file="/jsp/jx/jxgc/workplanmanage/WorkPlanGantt.jspf" %>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/TrainWorkPlanWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainWorkPlanEdit.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>