<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %> 
<%@include file="/jsp/jx/pjwz/partbase/component/PartsTypeTreeSelect.jspf" %>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var PARTS_STATUS_DX="<%=PartsAccount.PARTS_STATUS_DX%>";//待修
	var PARTS_STATUS_DBF="<%=PartsAccount.PARTS_STATUS_DBF%>";//待报废
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件报废</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainRCCombo.js"></script>  
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/RCRTCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsscrap/PartsAccountForScrapRegister.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsscrap/PartsScrapRegisterDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsscrap/PartsScrapRegister.js"></script>
</head>
<body>
</body>
</html>