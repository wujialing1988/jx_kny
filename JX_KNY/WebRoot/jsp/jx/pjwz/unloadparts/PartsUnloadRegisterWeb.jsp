<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下车配件登记</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var STATUS_WAIT = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_WAIT%>';  //状态--未登记
	var STATUS_ED = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_ED%>';  //状态--已登记
	var IS_IN_RANGE_NO = '<%=com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister.IS_IN_RANGE_NO%>';  //范围外下车
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//处理中
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//已处理
	var rdp_status_nullify = '<%=TrainWorkPlan.STATUS_NULLIFY%>';//终止
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainRCCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/RCRTCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterEd.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterWait.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterWeb.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>