<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修当日生产动态</title>
<script type="text/javascript">
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>'; // 委修段下拉树
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/WorkPlanRepairReport.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/ExpandInformation.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainInplan.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/WorkPlanWartoutTrain.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainWorkPlanDynamic.js"></script> 
<script language="javascript" src="TrainWorkPlanTheDynamic.js"></script> 

</head>
<body>
</body>
</html>
