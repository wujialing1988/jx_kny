/**
 * 列检_故障提票
 */
Ext.onReady(function(){
	Ext.namespace('Gztp');                       //定义命名空间
	
	Gztp.westPanel = new Ext.Panel({
		//border : false,
		region : 'west',
	    layout : 'border',
	    width : 342,
	    items : [ZbglRdpPlan.ZbglRdpPlanGrid, ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid]
	});
	
	Gztp.centerPanel = new Ext.Panel({
		//border : false,
		region : 'center',
	    layout : 'border',
		defaults: {
			xtype: 'container', layout: 'fit'
		},	    
	    items : [
	    	{
				region: 'center',
				layout : 'border',
				items: [{
					region: 'north',
					height: 270,
					items: GztpTicket.saveForm
				},{
					region: 'center',
					layout: 'fit',
					items:MatTypeUseList.grid
				}]
			}, {
				region: 'south',
				height: 250,
				items: GztpTicket.grid
			}]
	});
	
	Gztp.mainPanel = new Ext.Panel({
		border : false,
	    layout : 'border',
	    items : [Gztp.westPanel, Gztp.centerPanel]
	});
	
	//页面自适应布局
	new Ext.Viewport({ layout : 'fit', items : Gztp.mainPanel});
});