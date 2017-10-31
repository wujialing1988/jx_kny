<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修材料消耗维护</title>
<script type="text/javascript">
	
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//在修
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//修竣	
	
	var dataParam = {};
	var isQuery = "true";//统计页面 
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/matconsume/TrainRdp.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>
