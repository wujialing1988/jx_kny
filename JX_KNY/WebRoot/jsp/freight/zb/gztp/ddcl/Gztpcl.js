/**
 * 列检_故障提票调度处理
 */
Ext.onReady(function(){
	Ext.namespace('Gztpcl');                       //定义命名空间
	
	Gztpcl.westPanel = new Ext.Panel({
		region : 'east',
	    layout : 'border',
		width:800,
		title : i18n.FaultReportHandle.faultReHandle,
		collapsed: true,
        animCollapse: true,
        collapsible: true, 
	    items : [{
					region: 'north',
					height: 425,
					items: GztpclTicket.saveForm
				},{
					region: 'center',
					layout: 'fit',
					items:MatTypeUseList.grid
				}]
	});
	
	Gztpcl.mainPanel = new Ext.Panel({
		border : false,
	    layout : 'border',
	    items : [Gztpcl.westPanel, GztpclTicket.grid]
	});
	
	//页面自适应布局
	new Ext.Viewport({ layout : 'fit', items : Gztpcl.mainPanel});
});