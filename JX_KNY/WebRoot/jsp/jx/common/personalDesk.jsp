<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义桌面</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<%= ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/common/personalDesk.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>