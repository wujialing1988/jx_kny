<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>机车构型管理</title>
		
		<style type="text/css">	
			<%-- 修改网页链接<a>访问后、鼠标移上时的字体样式 --%>
			a:VISITED { color:blue; }
			a:HOVER { color:green; font-weight:bold; }
		</style>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TrainGxgl.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jcbm/jcgx/JcgxBuild.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jcbm/jxctfl/JcxtflBuild.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jcbm/JcxtflFault.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jcbm/TrainGxgl.js"></script>
	</head>
	<body>
	</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>