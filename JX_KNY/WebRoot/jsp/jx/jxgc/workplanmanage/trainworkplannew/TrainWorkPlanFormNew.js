Ext.onReady(function(){
Ext.namespace('TrainWorkPlanForm'); 
var processTips;
TrainWorkPlanForm.trainTypeIDX = "";
 TrainWorkPlanForm.rcIDX = "";
function showtip(){
	var el;
	if(TrainWorkPlanForm.win.isVisible()){
		el = TrainWorkPlanForm.win.getEl();
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
	
	/**
	 * 默认设置作业流程
	 * @param trainTypeIDX 
	 * @param rcIDX 
	 */
	TrainWorkPlanForm.getProcessInfo = function(trainTypeIDX, rcIDX){
		if (Ext.isEmpty(trainTypeIDX) || Ext.isEmpty(rcIDX)) {
			return;
		}
		// Ajax
        //if(self.loadMask) self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/jobProcessDef!getModelsByTrainTypeIDXAndRcIDX.action',
			async:false,
			params: {trainTypeIDX: trainTypeIDX, rcIDX: rcIDX},
		 	success: function(response,options){
		        var result = Ext.util.JSON.decode(response.responseText); 
//		 		var result = Ext.util.JSON.decode(response);
	        	var processDefList = result.processDefList;
	        	if (Ext.isEmpty(processDefList)) {
			        TrainWorkPlanForm.form.find("name", "processIDX")[0].reset();
					TrainWorkPlanForm.form.find("name", "processName")[0].reset();	
	        	} else {
			        TrainWorkPlanForm.form.find("name", "processIDX")[0].setValue(processDefList[0].idx);
					TrainWorkPlanForm.form.find("name", "processName")[0].setValue(processDefList[0].processName);	
	        	}		    
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	
TrainWorkPlanForm.processReturnFn = function(grid, rowIndex, e) {
	var record = grid.store.getAt(rowIndex);
	var form = TrainWorkPlanForm.form;
	form.find("name", "processIDX")[0].setValue(record.get("idx"));
	form.find("name", "processName")[0].setValue(record.get("processName"));
	
}

	TrainWorkPlanForm.labelWidth = 90;
	TrainWorkPlanForm.fieldWidth = 150;
	TrainWorkPlanForm.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	
		layout: "form",		border: false,	labelWidth: TrainWorkPlanForm.labelWidth,
		defaults:{
			anchor: "95%",
			defaults: {
				defaults: {				
					anchor: "95%", width: TrainWorkPlanForm.fieldWidth
				}
			}
		},
		items:[{
			xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	    	items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90,
	            columnWidth: 0.5,
		        items: [{ 
					id:"trainType_comb",	
    				fieldLabel: "车型",
    				xtype: "Base_combo",
    				hiddenName: "trainTypeIDX",
    			    business: 'trainVehicleType',
    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                    fields:['idx','typeName','typeCode'],
                    returnField:[{widgetId:"trainTypeShortName_Id",propertyName:"typeCode"}],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "typeCode", valueField: "idx",
                    pageSize: 20, minListWidth: 200,
                    allowBlank: false,
                    disabled:false,
                    editable:false,	
					listeners : {   
			        	"select" : function() {
			        		//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.queryParams.vehicleType = vehicleType;
			                trainNo_comb.cascadeStore();
			                
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();
			                rc_comb.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
			                rc_comb.store.proxy = new Ext.data.HttpProxy( {   
					            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(rc_comb.queryParams)+'&manager='+rc_comb.business 
					        });    
					        
					        //修程下拉选择框默认选中第一条记录
							rc_comb.store.on("load",function(store, records){ 
								if(records.length > 0){
//									TrainWorkPlanForm.getProcessInfo(Ext.getCmp("trainType_comb").getValue(), Ext.getCmp("rc_comb").getValue());
				                   	rc_comb.setDisplayValue(records[0].get('xcID'),records[0].get('xcName'));
							    	Ext.getCmp("repairClassName_Id").setValue(records[0].get('xcName'));
							    	//重新加载修次数据
		    	                	var rt_comb = Ext.getCmp("rt_comb");
		    	                	rt_comb.clearValue();
		    	                 	rt_comb.reset();
		    	                 	rt_comb.getStore().removeAll();
		    	                    rt_comb.queryParams = {"rcIDX":rc_comb.getValue()};
		    	                    rt_comb.store.proxy = new Ext.data.HttpProxy( {   
		    				            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(rt_comb.queryParams)+'&manager='+rt_comb.business 
		    				        });
		    						 //修次下拉选择框默认选中第一条记录
		    						rt_comb.getStore().on("load",function(store, records){ 
		    							if(records.length > 0){
		    						    	rt_comb.setDisplayValue(records[0].get('repairtimeIDX'),records[0].get('repairtimeName'));
		    						    	Ext.getCmp("repairtimeName_Id").setValue(records[0].get('repairtimeName'));
		    							}
		    						});
		    						rt_comb.getStore().load();
		    						jx.jxgc.JobProcessDefSelect.rcIDX = rc_comb.getValue();
		    						TrainWorkPlanForm.rcIDX = rc_comb.getValue();
								}
							});
							rc_comb.store.load();
							//重新加载流程选择数据
		                    var processSelect = TrainWorkPlanForm.form.getForm().findField("processIDX");
		                    jx.jxgc.JobProcessDefSelect.trainTypeIDX = this.getValue();

		                    //重新加载作业流程数据
					        TrainWorkPlanForm.form.find("name", "processIDX")[0].setValue("");
							TrainWorkPlanForm.form.find("name", "processName")[0].setValue("");	
			        	},
			        	"blur": function(){
			        		TrainWorkPlanForm.trainTypeIDX = Ext.getCmp("trainType_comb").getValue();
			        		TrainWorkPlanForm.rcIDX = Ext.getCmp("rc_comb").getValue();
			        		// 默认设置作业流程
							TrainWorkPlanForm.getProcessInfo(Ext.getCmp("trainType_comb").getValue(), Ext.getCmp("rc_comb").getValue());
			        	}
			        		
			    	}
				},{ id: 'trainTypeShortName_Id', name: "trainTypeShortName", fieldLabel: "trainTypeShortName", xtype:"hidden"},
			{
				id:"rc_comb",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassIDX", 
				returnField: [{widgetId:"repairClassName_Id",propertyName:"xcName"}],
				business: 'trainRC',
				queryParams: {'vehicleType':vehicleType},// 表示客货类型
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcID",
				pageSize: 0, minListWidth: 200,
				allowBlank: false,
				listeners : {   
	            	"select" : function() {   
	            		//重新加载修次数据
	                	var rt_comb = Ext.getCmp("rt_comb");
	                	rt_comb.clearValue();
	                 	rt_comb.reset();
	                    rt_comb.queryParams = {"rcIDX":this.getValue()};
	                    rt_comb.cascadeStore();
	                    jx.jxgc.JobProcessDefSelect.rcIDX = this.getValue();
	                    TrainWorkPlanForm.rcIDX = this.getValue();	                    
	                    // 默认设置作业流程
						TrainWorkPlanForm.getProcessInfo(Ext.getCmp("trainType_comb").getValue(), this.getValue());
	            	},
	            	"beforequery" : function(){
	            		var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();          		
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
	            	}	            
	            }
			},{				
				name: "planBeginTime", fieldLabel: "开始时间", anchor: "95%", xtype:"my97date",format: "Y-m-d H:i",
	        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
	        	
			},{
				xtype:"JobProcessDefSelect",fieldLabel: '作业流程',
				name: 'processName', editable:false, allowBlank: false,
				returnFn: TrainWorkPlanForm.processReturnFn			
			}]
	},{		
		baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90, columnWidth: 0.5, 
    	items:[{
				id:"trainNo_comb",	
				fieldLabel: "车号",
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                allowBlank: false,
                disabled:false,
                editable:true,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					},
					"select" : function(){
						
					}
				}
			},{
				id:"rt_comb",
				xtype: "Base_combo",
				fieldLabel: "修次",
				hiddenName: "repairtimeIDX", 
				returnField: [{widgetId:"repairtimeName_Id",propertyName:"repairtimeName"}],
				displayField: "repairtimeName",
				valueField: "repairtimeIDX",
				fields: ["repairtimeIDX","repairtimeName"],
				business: 'rcRt',
				pageSize: 0,
				minListWidth: 200,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择修次前先选修程
	            		var rcIdx =  Ext.getCmp("rc_comb").getValue();
						if(rcIdx == "" || rcIdx == null){
							MyExt.Msg.alert("请先选择修程！");
							return false;
						}
	            	}
				}
			},{
				  xtype: 'Base_combo',hiddenName: 'workCalendarIDX', fieldLabel: '日历',
				  entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo', allowBlank: false,
				  fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
				  queryHql: 'from WorkCalendarInfo where recordStatus = 0'
				  
			}]
	},{
		baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90,
        columnWidth: 1, 
        items: [{
        	name: "remarks", fieldLabel: "备注", anchor: "98%", xtype:"textarea", maxLength:1000  
        },
        { id:'repairClassName_Id', name: "repairClassName", xtype:"hidden"},
	    { id:'repairtimeName_Id', name: "repairtimeName", xtype:"hidden"},
	    { name: "processIDX", xtype:"hidden"},
	    { name: "ratedWorkDay", xtype:"hidden"},
	    { name: "enforcePlanDetailIDX", xtype:"hidden"},
	    { id: "wxname", name: "delegateDName", xtype:"hidden"},
	    { id: "vehicleType", name: "vehicleType", xtype:"hidden",value:vehicleType}]
	}]
	}]
});
TrainWorkPlanForm.win = new Ext.Window({
    title:"车辆检修作业计划", width: (TrainWorkPlanForm.labelWidth + TrainWorkPlanForm.fieldWidth + 8) * 2 + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, modal:true,
    items:TrainWorkPlanForm.form, 
    listeners: {
			beforeshow: function(window) {//设置初始时间为当前日期第二天的7：30
				var day = new Date().format('Y-m-d');
				var start = new Date(new Date(day).getTime()+ 1000*60*60*23 + 1000*60*60*+0.5).format('Y-m-d H:i');	
	        	TrainWorkPlanForm.form.find("name", "planBeginTime")[0].setValue(start);
			}
    },
    buttons: [{
        text: "生成作业计划", iconCls: "saveIcon", handler: function() {
	        var form = TrainWorkPlanForm.form.getForm(); 
	        console.debug(Ext.get("trainNo_comb").dom.value);
	        if (!form.isValid()) return;
	        TrainWorkPlanForm.enableColumns(['trainTypeIDX','trainNo','repairClassIDX','repairtimeIDX']);
	        var data = form.getValues();
	        delete data.deportIDX;

			//由于可输入，所以直接获取控件输入框值
			data.trainNo = Ext.get("trainNo_comb").dom.value;
			// 设置客货类型
			if(Ext.isEmpty(data.vehicleType)){
				data.vehicleType = vehicleType ;		
			}
	        showtip();
	        var cfg = {
	            scope: this, url: ctx + '/trainWorkPlanEditNew!generateWorkPlan.action', jsonData: data, timeout: 600000,
	            success: function(response, options){
	                var result = Ext.util.JSON.decode(response.responseText);
	                var entity = result.entity;
	                if (result.errMsg == null && entity != null) {
	                    alertSuccess();
	                    hidetip();	
	                    TrainWorkPlanEdit.store.reload();
	                    TrainWorkPlanForm.win.hide();

	                    var args = [];
						args.push(entity.idx);
						args.push(entity.trainTypeShortName + "|" + entity.trainNo);
						args.push(entity.repairClassName + "|" + entity.repairtimeName);
						args.push(new Date(entity.planBeginTime).format('Y-m-d H:i'));
						args.push(new Date(entity.planEndTime).format('Y-m-d H:i'));
//						 if(!(typeof(TrainWorkPlanWinNew)=="undefined")){
						
	                     TrainWorkPlanWinNew.showWin(args.join(","));
	                    if (!Ext.isEmpty(entity.enforcePlanDetailIDX))
	                    	TrainPlanSelect.win.hide();
	                } else {
	                	hidetip();
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    
        }
    }, {
        text: "取消", iconCls: "closeIcon", handler: function(){ TrainWorkPlanForm.win.hide(); }
    }]
});
TrainWorkPlanForm.showWin = function() {
	TrainWorkPlanForm.enableColumns(['trainTypeIDX','trainNo','repairClassIDX','repairtimeIDX']);
	var form = TrainWorkPlanForm.form.getForm();
	form.reset();
	var componentArray = ["Base_combo","DeportSelect2_comboTree"];
    for(var j = 0; j < componentArray.length; j++){
    	var component = TrainWorkPlanForm.win.findByType(componentArray[j]);
    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
    		continue;
    	}else{
    		for(var i = 0; i < component.length; i++){
                component[ i ].clearValue();
            }
    	}	                    
    }
	var cfg = {
        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.defInfo != null) {
                form.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	TrainWorkPlanForm.win.show();
}
TrainWorkPlanForm.disableColumns = function(dataIndexAry){                
    if(Ext.isEmpty(dataIndexAry) || !Ext.isArray(dataIndexAry))   return;
    for(var i = 0; i < dataIndexAry.length; i++){
        var field = TrainWorkPlanForm.form.getForm().findField(dataIndexAry[ i ]);
        if(Ext.isEmpty(field))  continue;
        field.disable();
    }        
}
TrainWorkPlanForm.enableColumns = function(dataIndexAry){               
    if(Ext.isEmpty(dataIndexAry) || !Ext.isArray(dataIndexAry))   return;
    for(var i = 0; i < dataIndexAry.length; i++){
        var field = TrainWorkPlanForm.form.getForm().findField(dataIndexAry[ i ]);
        if(Ext.isEmpty(field))  continue;
        field.enable();
    }
}
});