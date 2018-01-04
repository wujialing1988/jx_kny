<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>常用部门字典</title>
<script type="text/javascript">
    	var orgdegree = '<%=Constants.TEAM_LEVEL %>';
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-OrgDicType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jxpz/orgdic/OrgDicItemChooseTeam.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jxpz/orgdic/OrgDicItem.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jxpz/orgdic/OrgDicType.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>