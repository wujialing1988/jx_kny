<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料信息维护</title>
<!-- 上面引用了RowEditorGrid.jspf 此处无需使用，页面js报错<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/RowEditorGrid.js"></script> -->
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/MatTypeList.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>