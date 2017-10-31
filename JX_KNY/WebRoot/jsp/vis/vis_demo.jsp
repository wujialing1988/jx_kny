<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图</title>

<style type="text/css">

	#ida {
		background-image: 
	}
	
</style>
<script type="text/javascript">
</script>

<script language="javascript" src="<%= ctx %>/jsp/vis/vis_demo.js"></script>

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>