<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType"%>
<%@page import="com.yunda.jx.pjwz.partsBase.entity.PartsType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组成型号(已作废)</title>
<script language="javascript">
	//状态
	var status_new = <%=BuildUpType.NEW_STATUS%>;
	var status_use = <%=BuildUpType.USE_STATUS%>;
	var status_nullify = <%=BuildUpType.NULLIFY_STATUS%>;
	//组成类型
	var type_train = <%=BuildUpType.TYPE_TRAIN%>;
	var type_parts = <%=BuildUpType.TYPE_PARTS%>;
	//是否为缺省组成
	var isDefault_yes = <%=BuildUpType.ISDEFAULT_YES%>;
	var isDefault_no = <%=BuildUpType.ISDEFAULT_NO%>;
	//配件启用状态
	var partsStatus_use = <%=PartsType.STATUS_USE%>;
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsTypeAndQuotaSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/BuildUpType.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>