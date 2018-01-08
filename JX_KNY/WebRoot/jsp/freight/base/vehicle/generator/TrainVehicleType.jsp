<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.freight.base.vehicle.entity.TrainVehicleType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";
	var vehicleKindCombo = vehicleType == '<%= TrainVehicleType.TYPE_FREIGHT %>' ? 'VEHICLE_KIND_FREIGHT' : 'VEHICLE_KIND_PASSENGER' ;
</script>
<title>柴油发电机组型号维护</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/i18n-lang-TrainVehicleTypeGenerator.js"></script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/freight/base/vehicle/generator/TrainVehicleType.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>