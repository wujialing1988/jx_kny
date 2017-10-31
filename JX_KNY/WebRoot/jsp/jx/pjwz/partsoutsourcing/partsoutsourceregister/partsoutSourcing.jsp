<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %>
<%@page import="com.yunda.base.context.SystemContext" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var empOrg = '<%= SystemContext.getOmOrganization().getOrgname()%>';
	var empOrgId = '<%= SystemContext.getOmOrganization().getOrgid()%>';
	var PARTS_STATUS_FZC="<%=PartsAccount.PARTS_STATUS_FZC%>" ;				// 非在册
	var PARTS_STATUS_DX="<%=PartsAccount.PARTS_STATUS_DX%>" ;				// 待修
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件委外登记</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingTypeTreeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainRCCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/RCRTCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsAccountForOutSourcing.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/partsoutSourcing.js"></script>
</head>
<body>
</body>
</html>