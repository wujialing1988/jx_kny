<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>调度监控</title>
    <style type="text/css">
    	span{
			color:red;
		}
		.span{
			color:green;
		}
		input{
			color:red;
		}
		label{
			color:red;
		}
		dd-drag-ghost a span {
			color: red;
		}
    </style>
    <script type="text/javascript">
    	function checkVal(val, rtn){
			if(isNaN(val)){
				return trn;
			}
			if(parseInt(val) < 10){
				return 10;
			}
			if(parseInt(val) > 86400){
				return 86400;
			}
			return parseInt(val);
        }
    	var ts = checkVal('${ param.ts }', 3600);
    	var ps = checkVal('${ param.ps }', 600);
    	var cs = checkVal('${ param.cs }', 1200);
    	var mode = "${ param.mode == 'flow' ? 'flow' : 'gantt' }";

    	var content = null;

    	if(mode == 'flow'){
    	 content=	new Ext.Panel({	       		
				id:"flowFrame",
				region:"center",
				layout:"fit"
			});
			
        }else{
			content = new Ext.Panel({
				region : "center",
				layout:"fit",
				id:"gantt"
			});
        }
    	
	    var isDelayTrain = <%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.IS_DELAY_TRAIN%>;//是否延迟交车-是
		var noDelayTrain = <%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.NO_DELAY_TRAIN%>;//是否延迟交车-否
		//兑现单状态
		var rdp_status_new = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.STATUS_NEW%>';//新兑现
		var rdp_status_handling = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.STATUS_HANDLING%>';//处理中
		var rdp_status_handled = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.STATUS_HANDLED%>';//已处理
		var rdp_status_nullify = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.STATUS_NULLIFY%>';//终止
		//检修活动类型
		var type_project = <%=com.yunda.jx.jxgc.producttaskmanage.entity.RepairActivity.TYPE_PROJECT%>;//检修项目类型
		var type_tp = <%=com.yunda.jx.jxgc.producttaskmanage.entity.RepairActivity.TYPE_TP%>;//提票类型
		var type_tec_reform = <%=com.yunda.jx.jxgc.producttaskmanage.entity.RepairActivity.TYPE_TEC_REFORM%>;//技术改造活动类型
		var faultNotice_status_draft = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_DRAFT%>;//提票状态-未处理
		var trainTypeIDX = "";//车型主键
		var trainNo = "";//车号
    </script>
    <script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>    
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/flowFrame.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/LED_DDJK.js"></script>
  </head>
  
  <body>
  </body>
</html>
