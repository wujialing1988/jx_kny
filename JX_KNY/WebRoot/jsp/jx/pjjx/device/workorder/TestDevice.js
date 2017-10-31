Ext.onReady(function(){
	
	new Ext.Viewport({
		layout: "fit",
		items:[{
			layout: "border",
			border: false,
			items: [{
				region: "north",
				height: 200,
				layout: "fit",
				border: false,
				items: TestDeviceRdpCard.grid
			},{
				region: "center",
				layout: "fit",
				border: false,
				items: TestDeviceRdpCardDi.grid
			}]
		}]
	});
});