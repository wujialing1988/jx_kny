function uploadSuccess(){
	C1C6.grid.store.reload();
	FXDX.grid.store.reload();
}

Ext.apply(Ext.form.VTypes, {
	trainNo: function(val, field){
		return /^\d{4}[AB]?$/.test(val);
	},
	trainNoText: "请输入正确车号"
});

Ext.onReady(function(){
	
	function borderLayout(xcType, grid){
		grid.searchForm =  createSearchForm(xcType);
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
			items: borderLayout(c1c6_val, C1C6.grid)
		},{
			title: fxdx_val,
			layout:"fit",
			items: borderLayout(fxdx_val, FXDX.grid)
		}],			
		listeners: {
			tabchange: function(tab, panel){
				panel.doLayout();
			},
			resize: function(){
				this.activeTab.doLayout();
				hideClearLayout();
			}
		}
	});
	
	new Ext.Viewport({
		layout:"fit",
		items: tab
	});
	
});