<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRule" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提票过程角色配置</title>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_Tree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/tpRoleDefine/SysRoleView.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/tpRoleDefine/FaultTicketRoleDefineView.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/base/tpRoleDefine/FaultTicketRuleDefine.js"></script>
<script type="text/javascript">
	var IS_N = '<%= FaultTicketCheckRule.CONST_INT_IS_N %>';			// 是否- 否
	var IS_Y = '<%= FaultTicketCheckRule.CONST_INT_IS_Y %>';			// 是否 - 是
	
	
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