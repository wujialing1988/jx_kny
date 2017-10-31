<%@include file="/frame/jspf/header.jspf" %>  
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>流程图查看</title>
		<script language="javascript">	
			var ctx = '<%=ctx %>';
			var processInstID = '${param.processInstID}';
			var rdpId = '${param.rdpId}';
		</script>
		<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/flowFrame.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/flow_view.js"></script>
	</head>
	<body>
	</body>
</html>
