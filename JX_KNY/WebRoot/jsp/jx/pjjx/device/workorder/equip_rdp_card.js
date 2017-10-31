Ext.onReady(function(){
	
	Ext.ns("EquipCard");
	var grid, rdpIdx;
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpEquipCard!pageList.action",
			storeAutoLoad: false,
			singleSelect:true,
			viewConfig: null,
			fields:[{
				dataIndex: "idx", hidden:true
			},{
				header: "工单编号", dataIndex: "equipCardNo"
			},{
				header: "工单描述", dataIndex: "equipCardDesc"
			},{
				header: "作业开始时间", dataIndex: "workStartTime", width: 130,
				xtype: "datecolumn", editor:{my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i"}
			},{
				header: "作业结束时间", dataIndex: "workEndTime", width: 130,
				xtype: "datecolumn", editor:{my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i"}
			},{
				header: "数据生成时间", dataIndex: "dataGenTime", width: 130,
				xtype: "datecolumn", editor:{my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i"}
			},{
				header: "作业结果", dataIndex: "workResult"
			},{
				header: "备注", dataIndex: "remarks"
			}],
			toEditFn: function(){}
		});
		
		grid.store.on("beforeload", function(){
			if(!rdpIdx) return false;
			var sp = {};
			sp.rdpIDX = rdpIdx;
			this.baseParams.entityJson = Ext.util.JSON.encode(sp);
		});
		
		grid.on("render", function(){
			this.topToolbar.setVisible(false);
		});
		grid.on("rowclick", function(grid, rowIndex){
			var record = grid.store.getAt(rowIndex);
			EquipCardDI.reload(record.get("idx"));
		});
		return grid;
		
	}
	
	EquipCard.createGrid = createGrid;
	
	EquipCard.reload = function(_rdpIdx){
		if(rdpIdx === _rdpIdx) return;
		rdpIdx = _rdpIdx;
		grid.store.load();
		EquipCardDI.clear();
	};
});