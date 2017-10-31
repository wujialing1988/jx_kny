
Ext.onReady(function(){

	function borderLayout(form, grid){
		return {
			xtype: "panel",
			layout: "border",
			items: [{
				region: "north",
				minHeight: 100,
				autoHeight: true,
				baseCls: "x-plain",
				items: form
			},{
				region: "center",
				layout:"fit",
				items: grid
			}]
		};
	}
	
	var tab = new Ext.TabPanel({
		activeTab: 0,
		items: [{
			title: "未启动",
			layout:"fit",
			items: borderLayout(NotStart.form, NotStart.grid)
		},{
			title: "已启动",
			layout:"fit",
			items: []
		}],			
		listeners: {
			tabchange: function(tab, panel){
				if(panel.items.length == 0){
					panel.add(borderLayout(Started.form, Started.grid));
					panel.doLayout();
				}
				panel.doLayout();
			},
			resize: function(){
				this.activeTab.doLayout();
			}
		}
	});
	
	new Ext.Viewport({
		layout:"fit",
		items: tab
	});
});