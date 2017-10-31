Ext.onReady(function(){
	Ext.namespace("UI");
	
	UI.labelWidth = 85;
	UI.baseForm = new Ext.form.FormPanel({
		layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: UI.labelWidth, 
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
	    buttonAlign: "center",autoScroll:true,
	    items:[{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "trainTypeShortName", fieldLabel: "车型",    anchor: "90%" , readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "trainNo", fieldLabel: "车号",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
	            ]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "repairClassName", fieldLabel: "修程",    anchor: "90%" , readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "repairTimeName", fieldLabel: "修次",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
	            ]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "planBeginTimeStr", fieldLabel: "计划开始时间",    anchor: "90%" , readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "planEndTimeStr", fieldLabel: "计划结束时间",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
	            ]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "realBeginTimeStr", fieldLabel: "实际开始时间",    anchor: "90%" , readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "delayTime", fieldLabel: "延误时间(分)",  maxLength: 5, vtype:"positiveInt",  anchor: "90%", id:"delay_Time", allowBlank:false }
	            ]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                {
	        			allowBlank:false,
	        			id:"delay_Type",
	        			xtype: 'EosDictEntry_combo',
	        			hiddenName: 'delayType',
	        			dicttypeid:'JXGC_WORK_SEQ_DELAY',			
	        			displayField:'dictname',valueField:'dictid',
	        			anchor: "90%" ,editable:true,
	        			fieldLabel: "延误类型"
	                }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: UI.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "delayReason", fieldLabel: "延误原因",  maxLength: 200, xtype:"textarea", anchor: "90%", id:"delay_Reason", allowBlank:false},
					{ name: "delayIdx", fieldLabel: "delayIdx", xtype:"hidden", id:"delayIdx"},
					{ name: "tecProcessCaseIDX", fieldLabel: "tecProcessCaseIDX", xtype:"hidden"},
					{ name: "rdpIDX", fieldLabel: "rdpIDX", xtype:"hidden"},
					{ name: "idx", fieldLabel: "idx", xtype:"hidden"},
					{ name: "nodeIDX", fieldLabel: "nodeIDX", xtype:"hidden"}
	            ]
	       }]
	    }]
	});
	
	UI.handlerWin = new Ext.Window({
		title:"延误原因编辑", width:600, height:300, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
		items: UI.baseForm , modal:true,
		buttons: [{
    		text: "保存", iconCls: "saveIcon", handler:function(){ 
				PutOff.saveFn();
			}
    	},{
			text: "关闭", iconCls:"closeIcon", handler:function(){ UI.handlerWin.hide();}
		}]
	});
});