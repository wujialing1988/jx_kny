<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>互换配件信息</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsTypeAndQuotaSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/GyjcFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/OrgSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsmanage/PartsAccount.js"></script>
<script type="text/javascript">
var empId = '${sessionScope.emp.empid}';
var empName = '${sessionScope.emp.empname}';
var orgId = '${sessionScope.org.orgid}';
var orgName = '${sessionScope.org.orgname}';
var overseaName = '${sessionScope.overseaName}';//段名
var oversea = '${sessionScope.oversea}';//段id
var branchName = '${sessionScope.branchName}';//局名
var branch = '${sessionScope.branch}';//局id

</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>