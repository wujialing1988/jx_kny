<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/pjwz/partbase/component/PartsTypeTreeSelect.jspf" %>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %>
<%@page import="com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.entity.PartsWHRegister" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件校验登记</title>
<style type="text/css">
	.x-grid-col {
		background:#CFE8EE;
	}
</style>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var PARTS_STATUS_DJY = "<%=PartsAccount.PARTS_STATUS_DJY%>";			// 待校验
	var BILLTYPE_SELF = "<%=PartsWHRegister.BILLTYPE_SELF%>" ;				// 单据类型---自修
	var BILLTYPE_OUTSOURCING = "<%=PartsWHRegister.BILLTYPE_OUTSOURCING%>" ;				// 单据类型----委外修
	var MANAGE_DEPT_TYPE_ORG = '<%=PartsAccount.MANAGE_DEPT_TYPE_ORG%>';  //责任部门类型--机构
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/component/PartsExtendNoSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsCheck/PartsAccountForPartsCheckRegister.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsCheck/PartsCheckRegisterDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsCheck/PartsCheckRegister.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>