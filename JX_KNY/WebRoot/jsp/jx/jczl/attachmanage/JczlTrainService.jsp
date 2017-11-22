<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车信息维护</title>
<script type="text/javascript">


	var haveResume = <%=com.yunda.frame.common.Constants.YES %>;
	var notHaveResume = <%=com.yunda.frame.common.Constants.NO %>;	
	var trainStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_USE %>;
	var trainStateRepair = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_REPAIR %>;
	var trainStateSpare = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_LIEJIAN %>;
	var trainStateDetain = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_DETAIN %>;
	var assetStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_USE %>;
	var assetStateScrap = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_SCRAP %>;
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/i18n-lang-JczlTrainServiceNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/GyjcFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BureauSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect2.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/JczlTrainServiceNew.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>