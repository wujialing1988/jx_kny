<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修工艺流程</title>
    <style type="text/css">
    	#info{
    		width:100%;
    		height:60px;
    	}
    	span{
    		color:#0080FF;
    	}
    </style>
    <script language="javascript" src="${ pageContext.request.contextPath }/frame/resources/jquery/jquery.js"></script>
	<script type="text/javascript">
		var rdpIdx = '${ param.rdpIdx }';
		var partsRdpIdx = '${ param.partsRdpIdx }';
		var processInstID = '${ param.processInstID }';
		var processID = '${ param.processID }';
		var processDefName = '${ param.processDefName }';
		var ctx = '${ pageContext.request.contextPath }';
		var zb = '${ param.zb }';
		var pageSrc;
		if(processDefName){
			pageSrc = ctx + "/wfprocessdefine!lookOverWorkFlow.action?defineName="+ processDefName;
		}else{
		 	pageSrc = ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID=" + processInstID 
		 					+"&processID=" + processID + "&rdpId="+ (rdpIdx || partsRdpIdx) + "&zb=" + zb + "&isparts=" + partsRdpIdx;
		}
		function getAction(){
			if(zb){
				return 'zbRdp';
			}else{
				return 'trainEnforcePlanRdp';
			}
		}
		onload = function(){
			jQuery("#pageFrame").attr("src",pageSrc);
			
			if(rdpIdx && rdpIdx != 'null'){
				jQuery.ajax({
					url:"${ pageContext.request.contextPath }/"  + getAction() + "!getBeseInfo.action",
					type:"post",
					dataType:"json",
					data:{rdpId:rdpIdx},
					success:function(data){
						jQuery("#trainType").html(data.trainTypeShortName);
						jQuery("#trainNo").html(data.trainNo);
						jQuery("#nameplateNo").html(data.nameplateNo);
						jQuery("#repairClassName").html(data.repairClassName);
						jQuery("#repairTimeName").html(data.repairtimeName);
						if(data.transInTime != ""){
							jQuery("#transInTime").html(new Date(parseInt(data.transInTime)).format("yyyy-MM-dd hh:mm"));
						}
						if(data.planTrainTime){
							jQuery("#planTrainTime").html(new Date(data.planTrainTime).format("yyyy-MM-dd hh:mm"));
						}
					}
				});
				jQuery("#pageFrame").height(jQuery("body").height() - jQuery("#trainInfo").height());
				
			}else if(partsRdpIdx){
				
				jQuery.ajax({
					url:"${ pageContext.request.contextPath }/partsEnforcePlanRdp!getBeseInfo.action",
					type:"post",
					dataType:"json",
					data:{rdpId:partsRdpIdx},
					success:function(data){
						jQuery("#partsName").html(data.partsName);
						jQuery("#nameplateNo").html(data.nameplateNo);
						jQuery("#specificationModel").html(data.specificationModel);
						jQuery("#pRepairClassName").html(data.repairClassName);
						jQuery("#pRepairTimeName").html(data.repairtimeName);
						if(data.planBeginTime != ""){
							jQuery("#planBeginTime").html(new Date(parseInt(data.planBeginTime)).format("yyyy-MM-dd hh:mm"));
						}
						if(data.planEndTime){
							jQuery("#planEndTime").html(new Date(data.planEndTime).format("yyyy-MM-dd hh:mm"));
						}
					}
				});
				jQuery("#pageFrame").height(jQuery("body").height() - jQuery("#partsInfo").height());
				
			}else{
				jQuery("#pageFrame").height(jQuery("body").height());
			}
		}
		Date.prototype.format = function(format){
			var o = {
				"M+" :  this.getMonth()+1,  //month
			 	"d+" :  this.getDate(),     //day
			  	"h+" :  this.getHours(),    //hour
			    "m+" :  this.getMinutes(),  //minute
			    "s+" :  this.getSeconds(), //second
			    "q+" :  Math.floor((this.getMonth()+3)/3),  //quarter
			    "S"  :  this.getMilliseconds() //millisecond
			}
			if(/(y+)/.test(format)) {
				format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
			}
			 
			for(var k in o) {
				if(new RegExp("("+ k +")").test(format)) {
					format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
			    }
			}
			return format;
		}
				
	</script>
  </head>
  
  <body style="overflow:hidden">
  	<div id="trainInfo" style = "display:${ (empty param.rdpIdx or param.rdpIdx == 'null') ? 'none':'black' }">
  		<table width="100%">
	  		<tr>
	  			<td>车型：<span id="trainType"></span></td>
	  			<td>车号：<span id="trainNo"></span></td>
	  			<td>修程：<span id="repairClassName"></span></td>
	  			<td>修次：<span id="repairTimeName"></span></td>
	  		</tr>
  			<tr>
	  			<td colspan="2">检修开始时间：<span id="transInTime"></span></td>
	  			<td colspan="2">检修结束时间：<span id="planTrainTime"></span></td>
	  		</tr>
  		</table>
  		<hr>
  	</div>
  	<div id="partsInfo" style="display:${ empty param.partsRdpIdx ? 'none':'black' }">
  		<table width="100%">
	  		<tr>	  			
	  			<td>配件名称：<span id="partsName"></span></td>
	  			<td>规格型号：<span id="specificationModel"></span></td>
	  			<td colspan="2">配件编号：<span id="nameplateNo"></span></td>
	  		</tr>
  			<tr>
  				<td>修程：<span id="pRepairClassName"></span></td>
	  			<td>修次：<span id="pRepairTimeName"></span></td>
	  			<td>检修开始时间：<span id="planBeginTime"></span></td>
	  			<td>检修结束时间：<span id="planEndTime"></span></td>
	  		</tr>
  		</table>
  		<hr>
  	</div>
  	
    <iframe id="pageFrame"  frameborder="no" style="width:100%"></iframe>
  </body>
</html>
