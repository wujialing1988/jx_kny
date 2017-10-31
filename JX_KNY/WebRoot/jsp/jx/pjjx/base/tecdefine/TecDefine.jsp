<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjjx.base.tecdefine.manager.TecCardWSManager"%>
<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修工艺维护</title>
<script type="text/javascript">
var objList = <%=JSONUtil.write(TecCardWSManager.getQCContent())%>
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/MatTypeList.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/TecCardMat.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/TecCardWS.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/TecCardConfig.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/TecCard.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/tecdefine/TecDefine.js"></script> 
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>