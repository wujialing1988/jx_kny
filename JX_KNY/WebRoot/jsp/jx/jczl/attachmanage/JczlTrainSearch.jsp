<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车明细</title>
<%
OmOrganization parentOrg = OmOrganizationSelectManager.getOrgById(systemOrg.getParentorgid());
%>
<script>
	var orgId = '${sessionScope.org.orgid}';
	var orgName = '${sessionScope.org.orgname}';
	var trainStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_USE %>;
	var trainStateRepair = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_REPAIR %>;
	var trainStateSpare = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_SPARE %>;
	var assetStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_USE %>;
	var assetStateScrap = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_SCRAP %>;
	
	var pGree = "<%=parentOrg.getOrgdegree()%>";
	var pOrgId = null ;
	if( pGree == "branch"){ //铁路局标识
		pOrgId = "<%=parentOrg.getOrgid()%>";
	}
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/JczlTrainSearch.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>