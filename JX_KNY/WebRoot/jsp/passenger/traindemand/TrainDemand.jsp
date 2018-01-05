<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
</script>
<title>编组需求维护</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/PassengerTransport/i18n-lang-MarshallingInfoMaintain.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/PassengerTransport/i18n-lang-MarshallingDemandMaintain.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/passenger/traindemand/TrainInspectorMultSelect.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/passenger/routing/routingDefSelect.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/passenger/marshalling/JczlTrainServiceWin.js"></script><!-- 车辆窗口 -->
<script language="javascript" src="<%=ctx%>/jsp/passenger/traindemand/MarshallingTrainDemand.js"></script><!--列车需求编组车辆信息 -->
<script language="javascript" src="<%=ctx%>/jsp/passenger/traindemand/TrainDemand.js"></script><!-- 列车需求维护信息 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>