<%@include file="/frame/jspf/header.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业计划测试页面</title>
<script type="text/javascript">
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	var workPlanIDX = '';
	var workPlanEntity = {};
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>

<%@include file="/jsp/jx/jxgc/workplanmanage/WorkPlanGantt.jspf" %> 

<script language="javascript" src="TrainWorkPlanWin.js"></script>
<script language="javascript" src="TrainWorkPlanForm.js"></script>
<script language="javascript" src="TrainWorkPlan.js"></script>
</head>
<body>
</body>
</html>
<% } catch(Exception ex){ex.printStackTrace();} %>