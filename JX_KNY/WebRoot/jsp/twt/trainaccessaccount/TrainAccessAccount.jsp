<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%@page import="com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager" %>
<%@page import="com.yunda.frame.common.JXConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车出入段台账</title>
<script language="javascript">
	var siteID = '<%=EntityUtil.findSysSiteId("")%>';
	var TRAINSTATUS_DAIJIAN = '<%=TrainAccessAccount.TRAINSTATUS_DAIJIAN%>'; 
	var TRAINSTATUS_ZHENGZAIJIANCHA = '<%=TrainAccessAccount.TRAINSTATUS_ZHENGZAIJIANCHA%>';
	var TRAINSTATUS_LIANGHAO = '<%=TrainAccessAccount.TRAINSTATUS_LIANGHAO%>';
	var TRAINSTATUS_FEIYUNYONG = '<%=TrainAccessAccount.TRAINSTATUS_FEIYUNYONG%>';
	
	var TRAINTOGO_ZB = '<%=TrainAccessAccount.TRAINTOGO_ZB%>';		
	var dictName = EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",TRAINTOGO_ZB);
	
	var systemType = '<%=JXConfig.getInstance().getSystemType()%>';
	var isZB = !Ext.isEmpty(systemType) && systemType == 'zb' ? true : false;
	
	// 客货类型
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";	
	
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script type="text/javascript" src="<%=ctx %>/jsp/cmps/editgrid.js"></script>
<script language="javascript" src="ZbfwChoiceFormWin.js"></script>
<script language="javascript" src="TrainAccessTogo.js"></script>
<script language="javascript" src="TrainAccessAccount.js"></script>
<script language="javascript" src="TrainAccessAccountGroupInPlan.js"></script>
</head>
<body>
</body>
</html>