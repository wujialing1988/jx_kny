<%@include file="/frame/jspf/header.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作日历</title>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/baseapp/workcalendar/WorkCalendarInfo.js"></script>
<script type="text/javascript">
	var IS_DEFAULT_NO = '<%= WorkCalendarInfo.CONST_STR_IS_DEFAULT_NO %>';		// 设为当前默认日历 - 否
	var IS_DEFAULT_YES = '<%= WorkCalendarInfo.CONST_STR_IS_DEFAULT_YES %>';	// 设为当前默认日历 - 是
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