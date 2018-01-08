<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.freight.zb.plan.entity.ZbglRdpPlan" %>
<%@page import="com.yunda.frame.util.EntityUtil" %>


<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列检计划</title>
<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/vis_common.css" rel="stylesheet" type="text/css">
<style type="text/css">

</style>

<script type="text/javascript">

	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"10":request.getParameter("vehicleType")%>";<%-- 客货类型 --%>

	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
	
	var processTips;

	function showtip(){
		var el;
		if(WorkStationEmp.selectWin.isVisible()){
			el = WorkStationEmp.selectWin.getEl();
		}else{
			el = Ext.getBody();
		}
		processTips = new Ext.LoadMask(el,{
			msg:"正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	}
	
	function hidetip(){
		processTips.hide();
	}
	
	var trainStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_USE %>;
	var trainStateRepair = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_REPAIR %>;
	var trainStateSpare = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_LIEJIAN %>;
	var trainStateDetain = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_DETAIN %>;
	
</script>

<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/freight/base/ztgz/ZtgzRecord.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/base/ztgz/ZtgzList.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/base/ztgz/ZtgzDesign.js"></script>

</head>
<body>

</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>