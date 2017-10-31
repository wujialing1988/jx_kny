<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.entity.PartsWHRegister" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修竣配件入库查询</title>
	<script type="text/javascript">
		var empId = '${sessionScope.emp.empid}';
		var empName = '${sessionScope.emp.empname}';
		var BILLTYPE_SELF = "<%=PartsWHRegister.BILLTYPE_SELF%>" ;				 // 单据类型---自修
		var BILLTYPE_OUTSOURCING = "<%=PartsWHRegister.BILLTYPE_OUTSOURCING%>" ; // 单据类型----委外修
	</script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegisterSearch.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>