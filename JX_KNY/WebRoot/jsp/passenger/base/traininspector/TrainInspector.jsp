<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息维护</title>
<script type="text/javascript">
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TrainInspector.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/passenger/base/traininspector/EmpSelectEmp.js"></script>
<script language="javascript" src="<%=ctx %>//jsp/passenger/base/traininspector/TrainInspector.js"></script><!-- 交路基本信息 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>