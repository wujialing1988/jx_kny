<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生产停时分析</title>
<script language="javascript">
	<%
		Calendar clr = Calendar.getInstance();
		clr.set(Calendar.DAY_OF_MONTH,1);
		clr.set(Calendar.MONTH,clr.get(Calendar.MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	%>
	<%-- 从服务器取得本月的字符串yyyy-MM --%>
	var nextMonth="<%=sdf.format(clr.getTime()) %>";
	
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/ProfessionalType_check.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/faultmanager/statisticalanalysis/proTypeMonth.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>