<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车交接操作</title>
<script type="text/javascript">
	var operatorid = '${sessionScope.users.operatorid}';
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandover/HandOverOperateForm.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandover/ZbglHoModelItemResultSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandover/HandOverModelList.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/trainhandover/ZbglHoCase.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>