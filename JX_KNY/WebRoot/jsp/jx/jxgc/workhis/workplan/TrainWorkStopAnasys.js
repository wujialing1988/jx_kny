/**
 * 机车检修作业计划列表
 */
Ext.onReady(function(){
	Ext.ns('TrainWorkStopAnasys');                       //定义命名空间
	
	
	/** **************** 定义全局函数开始 **************** */
	//停时图
	TrainWorkStopAnasys.showStopAnasys = function(args){
		var idx = args.split(',')[0];			// 兑现单idx主键
		var startTime = args.split(',')[1];		// 开始时间
		var lastTime = args.split(',')[2];		// 结束时间	
		var trainTypeAndNo = args.split(',')[3];		// 车型车号	
		var reportUrl = "/jxgc/TrainsTopAnasys.cpt?ctx=" + ctx.substring(1);
		$.ajax({
			type:'GET',
			url: ctx + "/trainWorkPlanHis!getMinAndMaxRealTime.action",
			async:false,
			data: {"workPlanIDX":idx},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (null != result.mkinAndMaxRealTime) {
		        	var  mkinAndMaxRealTime =  result.mkinAndMaxRealTime;
		        	if(mkinAndMaxRealTime[0]){
		        		realBeginTime = new Date(mkinAndMaxRealTime[0]).format('Y-m-d');
		        		startTime = startTime <= realBeginTime? startTime:realBeginTime;	
		        	}
		        	if(mkinAndMaxRealTime[1]){
			        	realEndTime = new Date(mkinAndMaxRealTime[1]).format('Y-m-d');
						lastTime = lastTime<=  realEndTime?realEndTime:lastTime;
		        	}
    			}
			 
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	 	 var dataUrl = reportUrl + "&workPlanIdx=" + idx + "&startTime=" + startTime +"&lastTime=" + lastTime + "&trainTypeAndNo=" + trainTypeAndNo;
	  	 window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("停时图"));     		
	}
});