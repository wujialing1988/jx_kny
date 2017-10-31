<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构人员管理</title>
<script type="text/javascript">
	var saveFnEmpId = "";		//用于在新增或编辑人员信息后，记录人员empid，传递给操作员表单作为参数
</script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/WorkDutyWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/PositionWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/EmployeeWidget.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/OrganizationTreeAdjust.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_Form.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_List.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_Tab.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization_Tree.js"></script>
<script language="javascript" src="<%=ctx %>/frame/yhgl/Organization.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>