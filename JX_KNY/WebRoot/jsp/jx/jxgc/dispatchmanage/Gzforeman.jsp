<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationWorker" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工长默认派工</title>
<script language="javascript">
	//工位作业人员新增、启用、作废状态
	var workerStatus_new = <%=WorkStationWorker.NEW_STATUS%>;
	var workerStatus_use = <%=WorkStationWorker.USE_STATUS%>;
	var workerStatus_nullify = <%=WorkStationWorker.NULLIFY_STATUS%>;
	var workStationIDX = "";//工位关联班组主键
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/dispatchmanage/WorkStationWorker.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/dispatchmanage/Gzforeman.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>