<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %><%-- 人员选择控件 --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消息记录表</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script><%-- 人员选择控件 --%>
<script language="javascript" src="<%=ctx %>/frame/baseapp/message/Message.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>