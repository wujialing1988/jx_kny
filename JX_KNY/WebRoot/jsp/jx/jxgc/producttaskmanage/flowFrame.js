/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainEnforcePlanRdp');                       //定义命名空间
	TrainEnforcePlanRdp.idx = "";                        //兑现单主键
	TrainEnforcePlanRdp.searchParams = {};
	TrainEnforcePlanRdp.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding :5px',
		defaults : { anchor : '98%'},
        labelAlign : 'right',
		labelWidth:100,
		buttonAlign : 'center',
		html: "<label><div id='info'></div></label>"
	});
	
	function append(v1, v2){
		if(v1 && v2){
			return v1 + " | " + v2;
		}else if(v1){
			return v1 + " | 无";
		}else if(v2){
			return "无 | " + v2;
		}else{
			return "无  | 无";
		}
	}
	
	function getValue(v){
		if(v){
			return v;
		}else{
			return "";
		}
	}
	TrainEnforcePlanRdp.LoadInfo = function(record){
		var inTime = planTime = "";
		if(record.get("transInTime")!=null){
			inTime = "计划开始时间：" + (new Date(record.get("transInTime")).format("Y-m-d H:i"));
		}
		if(record.get("planTrainTime")!=null){
			planTime = "计划结束时间：" + (new Date(record.get("planTrainTime")).format("Y-m-d H:i"));
		}
		var train="车型车号：" + append(record.get("trainTypeShortName"), record.get("trainNo"));
		var repair = "修程修次: " + append(record.get("repairClassName"), record.get("repairtimeName"));
		var dept = "承修部门：" + getValue(record.get("undertakeDepName"));
		var html = train  + "&nbsp;&nbsp" + 
					repair + "&nbsp;&nbsp" + dept + "<br><br>" +
					inTime + "&nbsp;&nbsp" + planTime;
		
		jQuery("#info").html(html);
		jQuery("#flowBaseInfoForm").html(html);
	}
	
	TrainEnforcePlanRdp.bottomForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding :5px',
		defaults : { anchor : '98%'},
		labelWidth:105,
		buttonAlign : 'center', 
		items: [
					{
						align : 'left',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'left',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : 1,
								html: "<span id='train' style='font-size:14px;'></span>"
							}
						]
					},{
						align : 'left',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'left',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : 1,
								html: "<span id='change' style='font-size:14px;'></span>"
							}
						]
					}
		]
	});
});	
