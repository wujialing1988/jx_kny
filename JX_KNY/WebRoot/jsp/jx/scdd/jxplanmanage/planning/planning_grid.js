Ext.onReady(function(){
	if(typeof(Planning) === 'undefined')
		Ext.ns("Planning");
	var grid = new Ext.yunda.Grid({
		loadURL: ctx,
		saveURL: ctx,
		deleteURL: ctx,
		storeAutoLoad: false,
		fields: [{
			dataIndex: "idx", hidden: true, editor: {xtype: "hidden"}
		},{
			header: "计划名称", dataIndex: ""
		},{
			header: "编制日期", dataIndex: "", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "计划开始日期", dataIndex: "", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "计划结束日期", dataIndex: "", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "编制人", dataIndex: ""
		},{
			header: "编制单位", dataIndex: ""
		}],
		addButtonFn: function(){
			PlanningEdit.showWin();
		}
	});
	
	Planning.grid = grid;
});