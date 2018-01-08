<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationWorker" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修流水线</title>
<script language="javascript">
	//流水线新增、启用、作废状态
	var status_new = <%=RepairLine.NEW_STATUS%>;
	var status_use = <%=RepairLine.USE_STATUS%>;
	var status_nullify = <%=RepairLine.NULLIFY_STATUS%>;
	//工位新增、启用、作废状态
	var stationStatus_new = <%=WorkStation.NEW_STATUS%>;
	var stationStatus_use = <%=WorkStation.USE_STATUS%>;
	var stationStatus_nullify = <%=WorkStation.NULLIFY_STATUS%>;
	//工位作业人员新增、启用、作废状态
	var workerStatus_new = <%=WorkStationWorker.NEW_STATUS%>;
	var workerStatus_use = <%=WorkStationWorker.USE_STATUS%>;
	var workerStatus_nullify = <%=WorkStationWorker.NULLIFY_STATUS%>;
	//流水线类型
	var type = <%=RepairLine.TYPE_TRAIN%>;
	//var type_parts = <%=RepairLine.TYPE_PARTS%>;	
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/dispatchmanage/StationUnionWorker.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/dispatchmanage/WorkStation.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/dispatchmanage/RepairLine.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>