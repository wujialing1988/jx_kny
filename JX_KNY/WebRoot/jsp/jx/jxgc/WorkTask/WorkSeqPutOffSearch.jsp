<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工序延误查询</title>
<script language="javascript">	
	//机车检修作业计划状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//处理中
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//已处理
	var rdp_status_nullify = '<%=TrainWorkPlan.STATUS_NULLIFY%>';//终止
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="WorkSeqPutOffHis.js"></script>
<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/WorkTask/WorkSeqPutOff_UI.js"></script>
<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/WorkTask/WorkSeqPutOffSearch.js"></script>
</head>
<body>

</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>