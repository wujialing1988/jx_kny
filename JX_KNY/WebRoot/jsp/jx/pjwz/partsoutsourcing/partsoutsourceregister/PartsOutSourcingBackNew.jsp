<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page import="com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing" %>
<%@include file="/jsp/jx/pjwz/partbase/component/PartsTypeTreeSelect.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
	var out="<%=PartsOutsourcing.PARTS_STATUS_WW%>";//委外
	var back="<%=PartsOutsourcing.PARTS_STATUS_YFH%>";//已返回
	var takeOverType="<%=PartsOutsourcing.TAKE_OVER_TYPE_ORG%>";//接收部门--责任部门
		var teamOrgId = '${sessionScope.org.orgid}'; <%-- 当前登录人员班组ID --%>
		var teamOrgName = '${sessionScope.org.orgname}'; <%-- 当前登录人员班组名称 --%>
		var teamOrgSeq = '${sessionScope.org.orgseq}'; <%-- 当前登录人员班组序列 --%>
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>委外配件回段(新)</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingBackNew.js"></script>
</head>
<body>
</body>
</html>