<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ 
	String empname = (String)request.getParameter("empname");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修工时明细</title>

<link rel="stylesheet" type="text/css" href="/WebReport/ReportServer?op=resource&resource=/com/fr/web/core/css/page.css"/> 
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workdaycount/workDayCount_view.js"></script>
</head>
<body>
</body>
<script type="text/javascript">
var empname_v = '<%=empname%>';
var orgseq = '${param.orgseq}';
var beginDate = '${param.beginDate}';
var endDate = '${param.endDate}';
</script> 
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>