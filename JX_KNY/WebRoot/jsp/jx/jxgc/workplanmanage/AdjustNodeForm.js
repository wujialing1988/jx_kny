Ext.onReady(function(){
Ext.namespace('AdjustNodeForm'); 
var processTips;

function showtip(){
	var el;
	if(AdjustNodeForm.win.isVisible()){
		el = AdjustNodeForm.win.getEl();
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


AdjustNodeForm.labelWidth = 90;
AdjustNodeForm.fieldWidth = 150;
AdjustNodeForm.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",
	layout: "form",		border: false,	labelWidth: AdjustNodeForm.labelWidth,
	defaults:{
		anchor: "99%" ,
		defaults: {
			defaults: {				
				anchor: "95%", width: AdjustNodeForm.fieldWidth
			}
		}
	},
	items:[{
		xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
    	items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", labelWidth:AdjustNodeForm.labelWidth, columnWidth: 1,
		        items: [{				
					name: "realBeginTime", fieldLabel: "实际开始时间", xtype:"my97date",format: "Y-m-d H:i",
		        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				},{				
					name: "realEndTime", fieldLabel: "实际完成时间", xtype:"my97date",format: "Y-m-d H:i",
		        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				}]
			},{ name: "idx", xtype:"hidden"}]
	}]
});
AdjustNodeForm.reloadParent = function(nodeIdx) {
	WorkPlanGantt.loadFn("expanded", nodeIdx);
}
AdjustNodeForm.win = new Ext.Window({
    title:"调整实际时间", width: (AdjustNodeForm.labelWidth + AdjustNodeForm.fieldWidth + 8) * 1 + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
    items:AdjustNodeForm.form, 
    buttons: [{
        text: "确定", iconCls: "saveIcon", handler: function() {
	        var form = AdjustNodeForm.form.getForm(); 
	        if (!form.isValid()) return;
	        var data = form.getValues();	        
	        showtip();
	        var cfg = {
	            scope: this, url: ctx + '/jobProcessNode!updateRealTime.action', jsonData: data, timeout: 600000,
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    alertSuccess();
	                    hidetip();
	                    AdjustNodeForm.win.hide();
						AdjustNodeForm.reloadParent(data.idx);
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    
        }
    }, {
        text: "取消", iconCls: "closeIcon", handler: function(){ AdjustNodeForm.win.hide(); }
    }]
});
AdjustNodeForm.showWin = function(nodeIDX) {
	var form = AdjustNodeForm.form.getForm();
	form.reset();
	var cfg = {
        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
        params: {nodeIDX: nodeIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {
            	var record = new Ext.data.Record();
                var entity = result.entity;
                for(var i in entity){
                	if(i == 'realBeginTime' && !Ext.isEmpty(entity[i])){
                		record.set(i, new Date(entity[i]));
                		continue;	                    		
                	}
                	if(i == 'realEndTime' && !Ext.isEmpty(entity[i])){
                		record.set(i, new Date(entity[i]).format('Y-m-d H:i'));
                		continue;	                    		
                	}
                	record.set(i, entity[i]);
                }           	
            	form.loadRecord(record);
    			AdjustNodeForm.win.show();
    			if (entity.status == NODE_STATUS_GOING)
    				form.findField("realEndTime").disable();
    			else
    				form.findField("realEndTime").enable();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	
}
});