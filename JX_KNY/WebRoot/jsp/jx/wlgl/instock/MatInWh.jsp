<jsp:directive.page import="com.yunda.jx.wlgl.IBillStatus"/><%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料入库单</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/instock/MatTypeListForMatIn.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/instock/MatInWhDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/instock/MatInWh.js"></script>
<script type="text/javascript">
	var STATUS_ZC = '<%= IBillStatus.CONST_STR_STATUS_ZC %>'   		// 暂存
	var STATUS_DZ = '<%= IBillStatus.CONST_STR_STATUS_DZ %>'		// 登帐
	var STATUS_SY = '<%= IBillStatus.CONST_STR_STATUS_SY %>'		// 登帐
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