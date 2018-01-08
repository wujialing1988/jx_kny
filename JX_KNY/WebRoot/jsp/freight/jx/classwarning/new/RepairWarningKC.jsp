<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

	var processTips;

	function showtip(){
		var el = Ext.getBody();
		processTips = new Ext.LoadMask(el,{
			msg:"正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	}
	
	function hidetip(){
		processTips.hide();
	}
	
</script>
<title>货车修程预警</title>
<style type="text/css">
	.Warning_row_yellow{
		background:yellow;
	}
	.Warning_row_red{
		background: red;
	}
	.Warning_row_highlight{
		background:silver;
	}	
	.Warning_row_orange{
		background:#FF7F00;
	}
</style>

<link rel="stylesheet" href="<%= ctx %>/frame/resources/ext-3.4.0/ux/ColumnHeaderGroup/ColumnHeaderGroup.css" type="text/css"/>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script type="text/javascript" src="<%= ctx %>/frame/resources/ext-3.4.0/ux/ColumnHeaderGroup/ColumnHeaderGroup.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/freight/jx/classwarning/new/RepairWarningKC.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>