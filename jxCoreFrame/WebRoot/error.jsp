<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>		
		<title>公共出错页面！</title>
		
	</head>
	<body>
		<div xtype="panel" region="center" autoScroll="true" border="false" buttonAlign="center">
			<s:actionerror/>
			<s:actionmessage/>
		</div>
	</body>
</html>