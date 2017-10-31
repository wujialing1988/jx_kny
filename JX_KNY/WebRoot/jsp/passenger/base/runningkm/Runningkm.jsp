<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script type="text/javascript">
	
</script>
<title>客车走行公里维护</title>
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
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/passenger/base/runningkm/Runningkm.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>