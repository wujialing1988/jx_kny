Ext.onReady(function(){
	
	new Ext.Viewport({
		layout:"fit",
		items: [{
			layout: "border",
			items:[{
				region: "north",
				layout: "fit",
				baseCls:"x-plain",
				items: Planning.form,
				height: 40
			},{
				region: "center",
				layout: "fit",
				items: Planning.grid
			}]
		}]
	});
});