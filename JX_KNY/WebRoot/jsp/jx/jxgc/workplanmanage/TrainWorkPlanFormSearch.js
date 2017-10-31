Ext.onReady(function(){
Ext.namespace('TrainWorkPlanForm'); 
var processTips;

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
    	items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90,
	            columnWidth: 0.5,
		        items: [{ 
					id:"trainType_comb",	
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX", 
					returnField: [{widgetId:"trainTypeShortName_Id",propertyName:"shortName"}],
					xtype: "Base_combo",
					business: 'trainType',
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 200,
					editable:true,
					allowBlank: false,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();
			                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
			                rc_comb.store.proxy = new Ext.data.HttpProxy( {   
					            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(rc_comb.queryParams)+'&manager='+rc_comb.business 
					        });          				        
					        //修程下拉选择框默认选中第一条记录
							rc_comb.store.on("load",function(store, records){ 
								if(records.length > 0){
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
								}
							});
							rc_comb.store.load();
							//重新加载流程选择数据
		                    var processSelect = TrainWorkPlanForm.form.getForm().findField("processIDX");
		                    jx.jxgc.JobProcessDefSelect.trainTypeIDX = this.getValue();
		                    
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
				id: "psId", xtype: "DeportSelect2_comboTree",
				hiddenName: "dID", fieldLabel: "配属段", allowBlank: false,
				returnField: [{widgetId:"psNAME",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
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
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				minLength : 4, 
				maxLength : 5,
				forceSelection:false,    				
				xtype: "Base_combo",
				business: 'trainNoSelect',				
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
						{name:"leaveDate", type:"date", dateFormat: 'time'},
						"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse",
						"trainUseName","bId","dId","bName","dName","bShortName","dShortName"],	
				queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				returnField: [
		              {widgetId:"deportIDX", propertyName:"dId"},//配属段ID
		              {widgetId:"psNAME", propertyName:"dName"},//配属段名称 -
		              {widgetId:"wxname", propertyName:"dName"}
				],
				editable:true,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					},
//					"blur": function(){
//						/** 
//						 *  当车号控件失去焦点时,触发获取当前用户所选或者填写的内容
//						 *  如trainNo_comb的value与hiddenName的value相同,则可得知车号是由用户通过下拉进行选择的
//						 *  如果两者不相同,或者hiddenName的value为空,则可认为车号是用户通过键盘录入的
//						 */
//						//如果显示值不为空,而hidden中的值与显示值不等,则使用显示值给hidden赋值
//						if(Ext.get("trainNo_comb").dom.value!=null&&Ext.get("trainNo_comb").dom.value!=""){
//							if(Ext.get("trainNo").dom.value != Ext.get("trainNo_comb").dom.value){
//								//用户在控件中手动录入数据后执行
//								//1. 将显示值与隐藏待提交值保持一致
//								Ext.get("trainNo").dom.value = Ext.get("trainNo_comb").dom.value;
//							} 
//							//如果
//							else {
//								/**
//								 * 当选择车号后,自动带出配属局和配属段的内容
//								 */
//								var values = TrainWorkPlanForm.form.getForm().getValues();
//								//2. 带出配属段的Id及简称回显
//								var d_id = Ext.get("psId").dom.value;
//								var d_name = values.dNAME;
//								Ext.getCmp("psId").clearValue(); //清空配属段控件
//								Ext.getCmp("psId").setDisplayValue(d_id,d_name);
//								
//								Ext.getCmp("wxname").setValue(d_name);
//								Ext.getCmp("wxid").setDisplayValue(d_id,d_name);	//设置默认委修段
//							}
//						}
//					},
					"select" : function(){
						
						var values = TrainWorkPlanForm.form.getForm().getValues();
						//带出配属段的Id及简称回显
						var d_id = Ext.getCmp("deportIDX").getValue();
						var d_name = values.dNAME;
						if (Ext.isArray(d_name) && d_name.length > 0)
							d_name = d_name[0];
						Ext.getCmp("psId").clearValue(); //清空配属段控件
						Ext.getCmp("psId").setDisplayValue(d_id,d_name);
						Ext.getCmp("wxid").setDisplayValue(d_id,d_name);	//设置默认委修段
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
				id: "wxid", xtype: "DeportSelect2_comboTree",
				hiddenName: "delegateDID", fieldLabel: "委修段", allowBlank: false,
				returnField: [{widgetId:"wxname",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
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
        },{ id: "psNAME", name: "dNAME", fieldLabel: "dNAME", xtype:"hidden"},
        { id:'repairClassName_Id', name: "repairClassName", xtype:"hidden"},
	    { id:'repairtimeName_Id', name: "repairtimeName", xtype:"hidden"},
	    { name: "processIDX", xtype:"hidden"},
	    { name: "ratedWorkDay", xtype:"hidden"},
	    { id: "deportIDX", xtype:"hidden"},
	    { name: "enforcePlanDetailIDX", xtype:"hidden"},
	    { id: "wxname", name: "delegateDName", xtype:"hidden"}]
	}]
	}]
});
TrainWorkPlanForm.win = new Ext.Window({
    title:"机车检修作业计划", width: (TrainWorkPlanForm.labelWidth + TrainWorkPlanForm.fieldWidth + 8) * 2 + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
    items:TrainWorkPlanForm.form, 
    buttons: [{
        text: "生成作业计划", iconCls: "saveIcon", handler: function() {
	        var form = TrainWorkPlanForm.form.getForm(); 
	        if (!form.isValid()) return;
	        TrainWorkPlanForm.enableColumns(['trainTypeIDX','trainNo','repairClassIDX','repairtimeIDX']);
	        var data = form.getValues();
	        delete data.deportIDX;
	        if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_comb").dom.value)) {
				data.trainNo = Ext.get("trainNo_comb").dom.value;
			}
	        showtip();
	        var cfg = {
	            scope: this, url: ctx + '/trainWorkPlan!generateWorkPlan.action', jsonData: data, timeout: 600000,
	            success: function(response, options){
	                var result = Ext.util.JSON.decode(response.responseText);
	                var entity = result.entity;
	                if (result.errMsg == null && entity != null) {
	                    alertSuccess();
	                    hidetip();	                    
	                    TrainWorkPlanForm.win.hide();
	                    var args = [];
						args.push(entity.idx);
						args.push(entity.trainTypeShortName + "|" + entity.trainNo);
						args.push(entity.repairClassName + "|" + entity.repairtimeName);
						args.push(new Date(entity.planBeginTime).format('Y-m-d H:i'));
						args.push(new Date(entity.planEndTime).format('Y-m-d H:i'));
	                    TrainWorkPlanWin.showWin(args.join(","));
	                    VTrainWorkPlan.store.reload();
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