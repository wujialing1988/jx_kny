/**
 * 使用人确认主js文件，本文件构建主体页面布局
 */
Ext.onReady(function(){
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'fit',
		items: {
			border: false,
			xtype: "tabpanel", activeTab: 0,
			defaults: {xtype: 'panel', layout: 'fit'},
			items: [{
				title: "设备巡检",
				items: InspectPlanConfirm.grid
			}, {
				title: "计划检修",
				items: RepairConfirm.grid
			}, {
				title: "故障处理",
				items: FaultOrderConfirm.grid
			}]
		}
	});
	
});