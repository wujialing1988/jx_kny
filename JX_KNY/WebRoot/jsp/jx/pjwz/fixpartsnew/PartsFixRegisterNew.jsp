<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/pjwz/partbase/component/PartsTypeTreeSelect.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件上车登记</title>
 <style type="text/css">
	.checkQty_css {
		color:green;
		font-weight:bold;
	}
	.unCheckQty_css {
		color:red;
		font-weight:bold;
	}
	.x-grid-col {
	background:#CFE8EE;
	}
</style>

<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var PARTS_STATUS_LH = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_LH%>';  //配件状态--良好
	var MANAGE_DEPT_TYPE_ORG = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_ORG%>';  //责任部门类型--机构
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/fixparts/PartsAccountForPartsFixRegister.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/fixpartsnew/PartsFixRegisterNewDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/fixpartsnew/PartsFixRegisterNew.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>