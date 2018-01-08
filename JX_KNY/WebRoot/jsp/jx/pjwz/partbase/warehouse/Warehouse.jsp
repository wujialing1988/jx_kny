<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>库房</title>
<script type="text/javascript">
var STATUS_NO = '<%=WarehouseLocation.STATUS_NO%>'; //未满
var STATUS_YES = '<%=WarehouseLocation.STATUS_YES%>';//已满
 var orgId = '${sessionScope.org.orgid}';
 var orgName = '${sessionScope.org.orgname}';
 var orgCode = '${sessionScope.org.orgcode}';
 //alert(orgId+','+orgName);
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/warehouse/StoreKeeper.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/warehouse/WarehouseLocation.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/warehouse/Warehouse.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>

