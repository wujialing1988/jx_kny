<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@page import="com.yunda.frame.common.JXConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>站点配置</title>
		<script type="text/javascript">
			//var systemOrgname = "<%=Constants.ORG_ROOT_NAME %>"; 
			var systemOrgname = "<%=JXConfig.getInstance().getOrgTopRootName() %>"; 
			var systemOrgid = "0";
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-WorkPlace.js"></script>
		<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/basejs/OmOrganizationTreeWin.js"></script>		
		<script language="javascript" src="<%=ctx %>/jsp/jxpz/workplace/WorkPlace.js"></script>
	</head>
	<body>
	</body>
</html>