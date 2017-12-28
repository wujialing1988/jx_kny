<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";
</script>
<title>扣车登记</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-TruckDetainReg.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/freight/zb/detain/dj/DetainTrainDj.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>