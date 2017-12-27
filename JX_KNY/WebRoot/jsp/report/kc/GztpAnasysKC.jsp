<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客车故障登记</title>
<script type="text/javascript">
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var systemOrgname = "${ sessionScope.overseaName}"; 
	var systemOrgid = "${ sessionScope.oversea}";
	var empId = '${sessionScope.emp.empid}'; //用户ID
	var empName = '${sessionScope.emp.empname}';//用户名
</script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="GztpAnasysKC.js"></script>
</head>
<body>
</body>
</html>