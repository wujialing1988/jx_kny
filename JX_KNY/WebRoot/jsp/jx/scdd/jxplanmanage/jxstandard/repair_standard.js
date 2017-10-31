Ext.onReady(function(){
	if(typeof(RepairStandard) === 'undefined')
		Ext.ns("RepairStandard");
	
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
			items: RepairStandard.grid
		}]
		
	});
});