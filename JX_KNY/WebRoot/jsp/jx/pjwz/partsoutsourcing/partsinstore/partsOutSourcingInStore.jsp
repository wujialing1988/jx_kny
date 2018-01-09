<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var out="<%=PartsOutsourcing.PARTS_STATUS_WW%>";//委外
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修竣委外配件入库</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingTypeTreeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsinstore/partsoutSourcingSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsinstore/partsOutSourcingInStoreDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/partsinstore/partsOutSourcingInStore.js"></script>
</head>
<body>
</body>
</html>