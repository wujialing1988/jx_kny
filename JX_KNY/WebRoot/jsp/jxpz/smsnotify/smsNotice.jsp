<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jxpz.smsnotify.entity.SmsNotice" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信通知</title>
<script type="text/javascript">
var smsnotice_type_work_card_ch = '<%=SmsNotice.SMSNOTICE_TYPE_WORK_CARD_CH%>';
var smsnotice_type_fault_ch = '<%=SmsNotice.SMSNOTICE_TYPE_FAULT_CH%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jxpz/smsnotify/smsNotice.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>