Ext.onReady(function(){
	
	Ext.ns("TrainRdpList")
	
	var classId = null;
	
	var grid;
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/trainWorkPlan!pageQuery.action',                //装载列表数据的请求URL
		    tbar:["refresh"],
		    singleSelect: true,
		    storeAutoLoad: false,
		    viewConfig: null,
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'车型', dataIndex:'trainTypeShortName'
			},{
				header:'车号', dataIndex:'trainNo'
			},{
				header:'修程', dataIndex:'repairClassName'
			},{
				header:'修次', dataIndex:'repairtimeName'
			},{
				header:'开始时间', dataIndex:'beginTime', xtype: 'datecolumn', editor:{xtype: "my97date"}
			},{
				header:'结束时间', dataIndex:'endTime', xtype: 'datecolumn', editor:{xtype: "my97date"}
			},{
				header:'备注', dataIndex:'remarks'
			}],
			toEditFn: function(grid, rowIndex){
				var record = grid.store.getAt(rowIndex);
				TrainTest.showWin(record.get("idx"));
			}
		});
		grid.store.on("beforeload", function(){
			var whereList = [];
			if(classId){
				var s = "exists (select 1 from jxgc_train_devi" +
						"ce_detect c where c.rdp_idx = th" +
						"is_.idx and c.device_type_code = '" + classId +
						"' and record_status = 0)";
				whereList.push({sql: s, compare: Condition.SQL});
			}else{
				return false;
			}
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
		return grid;
	}
	
	TrainRdpList.createGrid = createGrid;
	
	TrainRdpList.reload = function(_classId){
		if(_classId == classId) return;
		classId = _classId;
		grid.store.load();
	}
});