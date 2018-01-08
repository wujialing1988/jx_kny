<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件周转情况统计</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsmanage/PartsAccountReport.js"></script>
<script type="text/javascript">
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