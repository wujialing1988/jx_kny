<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	STATUS_USE = '<%= Warehouse.STATUS_USE %>'
	var objList = <%=JSONUtil.write(MatTypeListManager.getMatType())%>
</script>
<title>班次维护</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ChooseMatTypeList.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/WhMatQuota.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ClassOrganizationSelectWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ClassOrganization.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ClassMaintain.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ClassOrganizationUser.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classmaintain/ClassMaintainDesign.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>