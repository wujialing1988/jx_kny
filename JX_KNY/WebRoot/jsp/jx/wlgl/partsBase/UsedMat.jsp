<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>常用物料清单</title>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/component/PersonSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/instock/MatTypeListForMatIn.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/partsBase/UsedMatPerson.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/partsBase/UsedMatDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/partsBase/UsedMat.js"></script>
<script type="text/javascript">
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	<%-- 图片虚拟路径 --%>
	var imgpathx = imgpath + '/pjgl.gif';
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