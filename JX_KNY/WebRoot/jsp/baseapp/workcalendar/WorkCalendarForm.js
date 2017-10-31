Ext.onReady(function(){
	Ext.namespace('WorkCalendarForm');
	WorkCalendarForm.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
	
	WorkCalendarForm.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 10px',
		defaults : { anchor : '98%'},
		labelWidth:95,
		autoScroll : true,
		buttonAlign : 'center', 
		items: [{
			xtype:'fieldset',
			title: '基本信息',
			collapsible: false,
			autoHeight:true,
			frame : true,
			items : []
		}]
	});
	
	Ext.getCmp("aas").renderTo  = WorkCalendarForm.baseForm;
});