<%@page import="com.yunda.frame.util.StringUtil"%>
<%@page import="com.yunda.frame.util.DateUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@page import="com.yunda.frame.baseapp.upload.entity.Attachment"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>

<%String ctx = request.getContextPath(); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>JS文档编辑</title>
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/base/css/lrtk.css" type="text/css" media="screen" />
		<script src="<%=ctx %>/jsp/sb/base/js/jquery.min.js"></script>
		<script src="<%=ctx %>/jsp/sb/base/js/jquery.flexslider-min.js"></script>
		<script type="text/javascript">
		var openDocObj = new ActiveXObject("SharePoint.OpenDocuments.2");
		function openWord(){
			openDocObj.EditDocument("http://10.2.4.67:8082/CoreFrame/pdf/test.docx");
		}
		</script>
	</head>
	<body style="text-align:center;padding-top:10px;">
		<input type="button" value="打开Word" onclick="openWord();">
	</body>
</html>