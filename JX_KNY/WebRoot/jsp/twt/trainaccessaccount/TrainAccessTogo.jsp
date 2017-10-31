<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%@page import="com.yunda.frame.common.JXConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>确定入段去向</title>
<script language="javascript">
	var siteID = '<%=EntityUtil.findSysSiteId("")%>';
	var TRAINTOGO_ZB = '<%=TrainAccessAccount.TRAINTOGO_ZB%>';		
	var dictName = EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",TRAINTOGO_ZB);
	
	var trainTypeIDX = '${ param.trainTypeIDX}';
	var trainTypeShortName = '${ param.trainTypeShortName}';
	var trainNo = '${ param.trainNo}';
	var idx = '${ param.idx}';
	var inTime = '${ param.inTime}';
	var planOutTime = '${ param.planOutTime}';
	var systemType = '<%=JXConfig.getInstance().getSystemType()%>';
	var isZB = !Ext.isEmpty(systemType) && systemType == 'zb' ? true : false;
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="TrainAccessTogo.js"></script>
<script language="javascript">
	Ext.onReady(function(){
		var form = TrainAccessTogo.saveForm.getForm();
		form.reset();
		if (Ext.isEmpty(trainTypeShortName) ||　trainTypeShortName　== 'null') {
			form.findField("trainTypeShortName").enable();
		} else {
			form.findField("trainTypeShortName").setDisplayValue(trainTypeShortName, trainTypeShortName);
			form.findField("trainTypeShortName").disable();
		}
		
		if (Ext.isEmpty(trainNo) ||　trainNo　== 'null') {
			form.findField("trainNo").enable();
		} else {
			form.findField("trainNo").setDisplayValue(trainNo, trainNo);
			form.findField("trainNo").disable();
		}
		if (!Ext.isEmpty(trainTypeIDX) &&　trainTypeIDX　!= 'null')
			form.findField("trainTypeIDX").setValue(trainTypeIDX);
		
		form.findField("idx").setValue(idx);
		form.findField("inTime").setValue(inTime);
		form.findField("planOutTime").setValue(planOutTime);
		if (!Ext.isEmpty(dictName) && isZB)
			form.findField("toGo").setDisplayValue(TRAINTOGO_ZB, dictName);
		TrainAccessTogo.afterSaveSuccessFn = function(result, response, options) {
			alertSuccess();
			TrainAccessTogo.closeWin();
		}
		TrainAccessTogo.afterSaveFailFn = function(result, response, options) {
			alertFail(result.errMsg);
		}
		
		var viewport = new Ext.Viewport({ layout:'fit', items:TrainAccessTogo.saveForm });
	});
</script>
</head>
<body>
</body>
</html>