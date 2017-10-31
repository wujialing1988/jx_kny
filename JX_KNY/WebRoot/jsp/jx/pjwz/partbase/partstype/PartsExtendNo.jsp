<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件扩展编号设置</title>
<script language="javascript">
	var IS_USED = '<%=PartsExtendNo.IS_USED%>';
	var NO_USED = '<%=PartsExtendNo.NO_USED%>';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/partstype/PartsExtendNo.js"></script>
</head>
<body>
</body>
</html>