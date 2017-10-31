<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page import="com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan" %>
<%@page import="com.yunda.base.context.SystemContext" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修材料消耗维护</title>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script type="text/javascript">
	
	//兑现单状态
	var rdp_status_new = '<%=TrainWorkPlan.STATUS_NEW%>';//新兑现
	var rdp_status_handling = '<%=TrainWorkPlan.STATUS_HANDLING%>';//在修
	var rdp_status_handled = '<%=TrainWorkPlan.STATUS_HANDLED%>';//修竣	
	
	var dataParam = {};
	var isQuery = "false";//非统计页面 
	var team = {};
	jQuery.ajax({
		url: ctx + "/omOrganizationSelect!getCurrentTeam.action",
		type:"post",
		dataType:"json",
		success:function(data){
			team = data.team;
			if(team != {})
				teamOrgId = team.orgid;
		}
	});
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/RowEditorGrid.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/matconsume/MatTypeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/matconsume/MatConsumeInfo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/matconsume/TrainComm.js"></script>
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
