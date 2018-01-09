<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	.checkQty_css {
		color:green;
	}
	.unCheckQty_css {
		color:red;
	}
	.x-grid-col {
		background:#CFE8EE;
	}
</style>
<title>配件委外登记</title>
	<script type="text/javascript">
		var empId = '${sessionScope.emp.empid}';
		var empName = '${sessionScope.emp.empname}';
		var teamOrgId = '${sessionScope.org.orgid}'; <%-- 当前登录人员班组ID --%>
		var teamOrgName = '${sessionScope.org.orgname}'; <%-- 当前登录人员班组名称 --%>
		var teamOrgSeq = '${sessionScope.org.orgseq}'; <%-- 当前登录人员班组序列 --%>
		var PARTS_STATUS_DX="<%=PartsAccount.PARTS_STATUS_DX%>" ;				// 待修
	</script>
	<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
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
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script> 
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/out/PartsUnloadRegisterForOutSourcing.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/out/PartsoutSourcingDetail.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/out/PartsoutSourcing.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>