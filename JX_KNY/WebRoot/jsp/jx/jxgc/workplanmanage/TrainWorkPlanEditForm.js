Ext.onReady(function(){
Ext.namespace('TrainWorkPlanEditForm'); 
var processTips;

function showtip(){
	var el;
	if(TrainWorkPlanEditForm.win.isVisible()){
		el = TrainWorkPlanEditForm.win.getEl();
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


TrainWorkPlanEditForm.labelWidth = 90;
TrainWorkPlanEditForm.fieldWidth = 150;
TrainWorkPlanEditForm.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",
	layout: "form",		border: false,	labelWidth: TrainWorkPlanEditForm.labelWidth,
	defaults:{
		anchor: "99%" ,
		defaults: {
			defaults: {				
				anchor: "95%", width: TrainWorkPlanEditForm.fieldWidth
			}
		}
	},
	items:[{
		xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
    	items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", labelWidth:TrainWorkPlanEditForm.labelWidth, columnWidth: 0.5,
		        items: [{ 
		        	style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "车型", name: 'trainTypeShortName'
				},{ 
		        	style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "修程", name: 'repairClassName'
				},{
					xtype: "DeportSelect2_comboTree",
					hiddenName: "dID", fieldLabel: "配属段", allowBlank: false,
					returnField: [{widgetId:"psNAME",propertyName:"delegateDName"}],
				    selectNodeModel: "leaf"
				},{				
					name: "planBeginTime", fieldLabel: "开始时间", xtype:"my97date",format: "Y-m-d H:i",
		        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				},{
		        	style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "作业流程", name: 'processName'							
				}]
			},{		
				baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanEditForm.labelWidth, columnWidth: 0.5, 
		    	items:[{ 
		        	style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "车号", name: 'trainNo'
				},{ 
		        	style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "修次", name: 'repairtimeName'
				},{
					xtype: "DeportSelect2_comboTree",
					hiddenName: "delegateDID", fieldLabel: "委修段", allowBlank: false,
					returnField: [{widgetId:"wxname",propertyName:"delegateDName"}],
				    selectNodeModel: "leaf"
				},{
					xtype: 'Base_combo',hiddenName: 'workCalendarIDX', fieldLabel: '日历',
					entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo', allowBlank: false,
					fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
				    queryHql: 'from WorkCalendarInfo where recordStatus = 0'
				},{
					style: 'border:none; background:none;',  xtype:"textfield", readOnly: true,
					fieldLabel: "完成时间", name: "planEndTime"
				}]
			},{
				baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90,
		        columnWidth: 1, 
		        items: [{
		        	name: "remarks", fieldLabel: "备注", anchor: "98%", xtype:"textarea", maxLength:1000  
		        }]
			},{ name: "idx", xtype:"hidden"},
			{ id: "psNAME", name: "dNAME", fieldLabel: "dNAME", xtype:"hidden"},
			{ id: "wxname", name: "delegateDName", xtype:"hidden"},
			{ id: "detainIdx", name: "detainIdx", xtype:"hidden"}]
	}]
});
TrainWorkPlanEditForm.reloadParent = function() {
	WorkPlanGantt.loadFn();
}
TrainWorkPlanEditForm.win = new Ext.Window({
    title:"机车检修作业计划", width: (TrainWorkPlanEditForm.labelWidth + TrainWorkPlanEditForm.fieldWidth + 8) * 2 + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
    items:TrainWorkPlanEditForm.form, 
    buttons: [{
        text: "确定", iconCls: "saveIcon", handler: function() {
	        var form = TrainWorkPlanEditForm.form.getForm(); 
	        if (!form.isValid()) return;
	        var data = form.getValues();	        
	        showtip();
	        var cfg = {
	            scope: this, url: ctx + '/trainWorkPlan!generateWorkPlan.action', jsonData: data, timeout: 600000,
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    alertSuccess();
	                    hidetip();
	                    TrainWorkPlanEditForm.win.hide();
						TrainWorkPlanEditForm.reloadParent();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    
        }
    }, {
        text: "取消", iconCls: "closeIcon", handler: function(){ TrainWorkPlanEditForm.win.hide(); }
    }]
});
TrainWorkPlanEditForm.showWin = function() {
	var form = TrainWorkPlanEditForm.form.getForm();
	form.reset();
	var cfg = {
        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
        params: {workPlanIDX: workPlanIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {
            	var record = new Ext.data.Record();
                var entity = result.entity;
                if (entity.workPlanStatus != WORKPLAN_STATUS_NEW) {
                	MyExt.Msg.alert("作业计划已启动，不能编辑！");
                	return;
                }
                for(var i in entity){
                	if(i == 'planBeginTime' ){
                		record.set(i, new Date(entity[i]));
                		continue;	                    		
                	}
                	if(i == 'planEndTime'){
                		record.set(i, new Date(entity[i]).format('Y-m-d H:i'));
                		continue;	                    		
                	}
                	if (i == 'dID' || i == 'delegateDID') 
                		continue;
                	record.set(i, entity[i]);
                }           	
            	form.loadRecord(record);                
                var cfg = {
			        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
        			params: {infoIdx: entity.workCalendarIDX},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.defInfo != null) {
			                form.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
			            }
			        }
			    };
    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    			TrainWorkPlanEditForm.win.show();
    			form.findField("dID").setDisplayValue(entity.dID, entity.dNAME);
                form.findField("delegateDID").setDisplayValue(entity.delegateDID, entity.delegateDName);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	
}
});