<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>机车信息</title>

<script type="text/javascript">
	var siteID = '<%=EntityUtil.findSysSiteId("")%>';
	var TRAINTOGO_ZB = '<%=TrainAccessAccount.TRAINTOGO_ZB%>';		
	var dictName = EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",TRAINTOGO_ZB);
	/**检修类型：碎修*/
    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
    /**检修类型：临修*/
    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
    
    //提票状态
	var STATUS_INIT = '<%=ZbglTp.STATUS_INIT%>';
	var STATUS_DRAFT = '<%=ZbglTp.STATUS_DRAFT%>';
	var STATUS_OPEN = '<%=ZbglTp.STATUS_OPEN%>';
	var STATUS_OVER = '<%=ZbglTp.STATUS_OVER%>';
	var STATUS_CHECK = '<%=ZbglTp.STATUS_CHECK%>';
	
	var STATUS_INIT_CH = '<%=ZbglTp.STATUS_INIT_CH%>';	
	var STATUS_DRAFT_CH = '<%=ZbglTp.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=ZbglTp.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=ZbglTp.STATUS_OVER_CH%>';
	var STATUS_CHECK_CH = '<%=ZbglTp.STATUS_CHECK_CH%>';
	
	var trainTypeIDX = '${ param.trainTypeIDX}';
	var trainTypeShortName = '${ param.trainTypeShortName}';
	var trainNo = '${ param.trainNo}';
	var idx = '${ param.idx}';
</script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainTpInfo.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>