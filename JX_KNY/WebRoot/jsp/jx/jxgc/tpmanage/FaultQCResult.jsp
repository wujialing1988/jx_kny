<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemDefine" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提票质量检查</title>
<script type="text/javascript">	    
    var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var img = '<%=ctx %>/frame/resources/images/toolbar/pjgl.gif';<%-- 图片虚拟路径 --%>
	var CONST_INT_CHECK_WAY_CJ = <%=TPQCItemDefine.CONST_INT_CHECK_WAY_CJ%>;
	var CONST_INT_CHECK_WAY_BJ = <%=TPQCItemDefine.CONST_INT_CHECK_WAY_BJ%>;
	
	// 客货类型
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";	
	
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/tpmanage/FaultQCResultWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/tpmanage/FaultQCResult.js"></script>
</head>
<body>
</body>
</html>