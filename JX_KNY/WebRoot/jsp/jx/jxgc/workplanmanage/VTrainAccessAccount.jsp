<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在修机车分类信息</title>

<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<%= ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.js"></script>
<script type="text/javascript">
	Ext.onReady(function(){
		new Ext.Viewport({
			layout:'fit',
			items:VTrainAccessAccount.panel
		});
	});
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>