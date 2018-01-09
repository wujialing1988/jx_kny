<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下车配件登记确认</title>
	<script type="text/javascript">
		var empId = '${sessionScope.emp.empid}';
		var empName = '${sessionScope.emp.empname}';
		var teamOrgId = '${sessionScope.org.orgid}'; <%-- 当前登录人员班组ID --%>
		var teamOrgName = '${sessionScope.org.orgname}'; <%-- 当前登录人员班组名称 --%>
		var teamOrgSeq = '${sessionScope.org.orgseq}'; <%-- 当前登录人员班组序列 --%>
		var status_wait = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_WAIT%>';  //单据状态--未登帐
		var status_ed = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_ED%>';  //单据状态--已登帐/待确认
		var status_checked = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_CHECKED%>';  //单据状态--已确认交接
	</script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterCheck.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>