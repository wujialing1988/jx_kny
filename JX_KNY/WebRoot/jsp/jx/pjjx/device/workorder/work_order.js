Ext.onReady(function(){
	
	new Ext.Viewport({
		layout: "fit",
		items:[{
			layout: "border",
			border: false,
			items: [{
				region: "west",
				width: 200,
				layout: "fit",
				border: false,
				items: DeviceClass.classTree
			},{
				region: "center",
				layout: "fit",
				border: false,
				items:[{
					layout: "border",
					items:[{
						title: "设备列表",
						region: "west",
						layout: "fit",
						width: 500,
						items: DeviceList.grid
					},{
						region: "center",
						layout: "fit",
						items: RdpTab.createTab()
					}]
				}]
			}]
		}]
	});
});