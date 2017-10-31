Ext.onReady(function(){
	
	function borderLayout(xcType, obj){
		var grid = obj.createGrid();
		grid.searchForm =  createSearchForm(xcType, grid);
		return {
			xtype: "panel",
			layout: "border",
			items: [{
				region: "north",
				minHeight: 100,
				autoHeight: true,
				baseCls: "x-plain",
				items: grid.searchForm
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
			title: c1c6_val,
			layout:"fit",
			items: borderLayout(c1c6_val, C1C6)
		},{
			title: fxdx_val,
			layout:"fit",
			items: borderLayout(fxdx_val, FXDX)
		}],			
		listeners: {
			tabchange: function(tab, panel){
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