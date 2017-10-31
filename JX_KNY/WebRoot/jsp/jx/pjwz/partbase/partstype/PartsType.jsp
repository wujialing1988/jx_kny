<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page import="com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>互换配件规格型号</title>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/partstype/PartsType.js"></script>
<script type="text/javascript">
	var status_add = '<%=PartsType.STATUS_ADD%>';   //新增
	var status_use = '<%=PartsType.STATUS_USE%>';   //启用
	var status_invalid = '<%=PartsType.STATUS_INVALID%>';  //作废
	var yes = '<%=Constants.YES%>';    //是
	var no = '<%=Constants.NO%>';     //否
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
