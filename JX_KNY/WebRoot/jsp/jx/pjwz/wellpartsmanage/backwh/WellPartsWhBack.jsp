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
<title>良好配件退库</title>
	<script type="text/javascript">
		var empId = '${sessionScope.emp.empid}';
		var empName = '${sessionScope.emp.empname}';
		var PARTS_STATUS_LH= '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_LH%>';  //配件状态--良好
		var MANAGE_DEPT_TYPE_WH = <%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_WH%>;  //责任部门类型--库房
		var MANAGE_DEPT_TYPE_ORG = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_ORG%>';  //责任部门类型--机构
		var img = '<%=ctx %>/frame/resources/images/toolbar/application_view_list.png';//图片目录
		var teamId = '${sessionScope.org.orgid}';
		var teamName = '${sessionScope.org.orgname}';
	</script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsMadeFactorySelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainRCCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/RCRTCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/wellpartsmanage/backwh/PartsAccountForWellPartsWhBack.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/wellpartsmanage/backwh/WellPartsWhBackDetail.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/wellpartsmanage/backwh/WellPartsWhBack.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>