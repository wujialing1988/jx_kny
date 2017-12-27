Ext.onReady(function(){
	if(typeof(RepairStandard) === 'undefined')
		Ext.ns("RepairStandard");
	
	
	//tab选项卡布局
	RepairStandard.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "zxTab", title: '走行公里', layout:'fit', items: [RepairStandard.grid]
	    },{  
	       id: "zxTimeTab", title: '时间维度', layout:'fit', items: [RepairStandardTime.grid]
	    }]
	});
	
	new Ext.Viewport({
		layout: "border",
		items: [{
			region: "west",
			width: 150,
			layout: "fit",
			items: RepairStandard.tree
		},{
			region: "center",
			layout: "fit",
			items: RepairStandard.tabs
		}]
		
	});
});