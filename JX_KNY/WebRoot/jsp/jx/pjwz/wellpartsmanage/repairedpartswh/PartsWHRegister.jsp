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
<title>修竣配件入库</title>
<style type="text/css">
	.x-grid-col {
		background:#CFE8EE;
	}
</style>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var PARTS_STATUS_LH = "<%=PartsAccount.PARTS_STATUS_LH%>";   			// 良好
	var PARTS_STATUS_WWX = "<%=PartsAccount.PARTS_STATUS_WWX%>" ;				// 委外修
	var PARTS_STATUS_XJ = "<%=PartsAccount.PARTS_STATUS_XJ%>";   			// 修竣
	var BILLTYPE_SELF = "<%=PartsWHRegister.BILLTYPE_SELF%>" ;				// 单据类型---自修
	var BILLTYPE_OUTSOURCING = "<%=PartsWHRegister.BILLTYPE_OUTSOURCING%>" ;				// 单据类型----委外修
	var MANAGE_DEPT_TYPE_ORG = '<%=PartsAccount.MANAGE_DEPT_TYPE_ORG%>';  //责任部门类型--机构
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/component/PartsExtendNoSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsAccountForPartsWHRegister.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegisterDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegister.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>