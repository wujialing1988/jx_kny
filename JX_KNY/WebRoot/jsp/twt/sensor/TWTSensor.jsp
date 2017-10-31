<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %> 
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>传感器注册</title>
<script type="text/javascript">		
	var siteId = '<%=EntityUtil.findSysSiteId("")%>';
	var siteName = '<%=EntityUtil.findSysSiteName(EntityUtil.findSysSiteId(""),"")%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/twt/sensor/TWTSensorSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/sensor/TWTSensor.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>