<jsp:directive.page import="com.yunda.jx.wlgl.movestock.entity.MatMoveStock"/>
<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消耗配件移入</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/movestock/MatMoveStockDetailQuery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/movestock/MatMoveStockMoveIn.js"></script>
<script type="text/javascript">
	var STATUS_ZC = '<%= MatMoveStock.STATUS_ZC %>'   		// 暂存
	var STATUS_IN = '<%= MatMoveStock.STATUS_IN %>'		// 转入
	var STATUS_OUT = '<%= MatMoveStock.STATUS_OUT %>'		// 转出
	var STATUS_ALL = '<%= MatMoveStock.STATUS_ALL %>'		// 所有
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