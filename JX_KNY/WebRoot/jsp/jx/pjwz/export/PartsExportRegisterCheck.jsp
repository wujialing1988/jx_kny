<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.entity.PartsWHRegister" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件调出登记确认</title>
	<script type="text/javascript">
		var status_wait = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_WAIT%>';  //单据状态--未登帐
		var status_ed = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_ED%>';  //单据状态--已登帐/待确认
		var status_checked = '<%=com.yunda.jx.pjwz.common.PjwzConstants.STATUS_CHECKED%>';  //单据状态--已确认交接
	</script>
	<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script> 
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/export/PartsExportRegisterCheck.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>