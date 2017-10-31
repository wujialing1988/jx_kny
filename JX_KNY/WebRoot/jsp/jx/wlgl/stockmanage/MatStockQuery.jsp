<jsp:directive.page import="com.yunda.jx.wlgl.stockmanage.entity.MatStockQuery"/><%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消耗配件库存查询</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/stockmanage/MatStockQuery.js"></script>
<script type="text/javascript">
	var STATUS_ALL = '<%= MatStockQuery.CONST_INT_STATUS_ALL %>'   		// 所有
	var STATUS_NOR = '<%= MatStockQuery.CONST_INT_STATUS_NOR %>'		// 正常
	var STATUS_MIN = '<%= MatStockQuery.CONST_INT_STATUS_MIN %>'		// 低于最小保有量
	var STATUS_MAX = '<%= MatStockQuery.CONST_INT_STATUS_MAX %>'		// 高于最大保有量
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