Ext.onReady(function(){
	
	new Ext.Viewport({
		layout: "fit",
		border: false,
		items: {
			layout: "border",
			items:[{
				region: "north",
				height: 60,
				border: false,
				baseCls: "x-plain",
				items: Searcher.createForm(),
				listeners:{
					resize: function(){
						this.doLayout();
					}
				}
			},{
				region: "center",
				layout: "fit",
				items:{
					layout: "column",
					columnWidth: .5,
					border: false,
					items:[{
						columnWidth: .5,
						layout: "fit",
						title: "设备/机车任务单",
						items: DeviceRdp.createGrid()
					},{
						columnWidth: .5,
						layout: "fit",
						title: "检测项",
						items: DetectItem.createGrid()
					}],
					listeners:{
						"resize" : function(){
							this.items.items[0].setHeight(this.getHeight());
							this.items.items[1].setHeight(this.getHeight());
						}
					}
				}
			}]
		}
	});
});