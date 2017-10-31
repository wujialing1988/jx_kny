<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生产计划汇总</title>

<link rel="stylesheet" type="text/css" href="/WebReport/ReportServer?op=resource&resource=/com/fr/web/core/css/page.css"/>   

</head>
<body>
<iframe name="inner_frame" width=100% height=95%></iframe>  
<form id="reportForm" method="POST" action="" target="inner_frame">
</form>  
</body>
<script type="text/javascript">
	var trainType = '${param.trainType}' ; 
	var trainNo = '${param.trainNo}' ; 
	var bShortName = '${param.bShortName}' ; 
	var dShortName = '${param.dShortName}' ; 
	var undertakeOrgName = '${param.undertakeOrgName}' ; 
	var planStartDate = '${param.planStartDate}' ; 
	var planStartDate_End = '${param.planStartDate_End}' ; 
	var planEndDate = '${param.planEndDate}' ; 
	var planEndDate_End = '${param.planEndDate_End}' ; 
	var startPlanDate= '${param.startPlanDate}' ; 
	var endPlanDate= '${param.endPlanDate}' ; 
	var planStatus = '${param.planStatus}' ; //报表状态
        var url = getReportEffectivePath("/scdd/ProducePlanGather_JWD.cpt"
        +"&planStatus="+planStatus+"&trainType="+trainType+"&trainNo="+trainNo+"&bShortName="+bShortName
        +"&dShortName"+dShortName+"&undertakeOrgName="+undertakeOrgName
        +"&planStartDate="+planStartDate+"&planStartDate_End="+planStartDate_End
        +"&planEndDate="+planEndDate+"&planEndDate_End="+planEndDate_End
        +"&startPlanDate="+startPlanDate+"&endPlanDate="+endPlanDate);
       
	window.onload = function(){
		document.getElementById("reportForm").action = encodeURI(url);
		document.getElementById("reportForm").submit();
	}
</script>
</html>
