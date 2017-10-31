<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动信息</title>
<script language="javascript">	
	var ctx = '<%=ctx %>';
	var processInstID = '${param.processInstID}';
	var rdpId = '${param.rdpId}';
	var activitydefId = '${param.activitydefId}';
	var processDefID = '${param.processdefId}';
	var activityinstid = '${param.activityinstid}';  //活动实例id
	var isparts = '${param.isparts}';  //是否为配件
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/activityInfo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/WIParticipantInfo.js"></script>

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>