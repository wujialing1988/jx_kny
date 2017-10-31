<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>质量检查维护</title>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_Tree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/jcqcitemdefine/JCQCItemEmpDefine.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/jcqcitemdefine/JCQCItemEmpOrgDefine.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/jcqcitemdefine/JCQCEmpView.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/jcqcitemdefine/JCQCItemDefine.js"></script>
<script type="text/javascript">
	var IS_ASSIGN_N = '<%= JCQCItemDefine.CONST_INT_IS_ASSIGN_N %>';			// 是否指派 - 否
	var IS_ASSIGN_Y = '<%= JCQCItemDefine.CONST_INT_IS_ASSIGN_Y %>';			// 是否指派 - 是
	
	var CHECK_WAY_CJ = '<%= JCQCItemDefine.CONST_INT_CHECK_WAY_CJ%>'			// 抽检
	var CHECK_WAY_BJ = '<%= JCQCItemDefine.CONST_INT_CHECK_WAY_BJ%>'			// 必检
	
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
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