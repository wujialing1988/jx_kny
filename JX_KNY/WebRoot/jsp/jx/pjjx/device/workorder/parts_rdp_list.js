Ext.onReady(function(){
	
	Ext.ns("PartsRdpList")
	
	var classId = null;
	var grid;
	function createGrid(){
		grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/partsRdp!pageQuery.action',                //装载列表数据的请求URL
		    tbar:["refresh"],
		    singleSelect: true,
		    storeAutoLoad: false,
		    viewConfig: null,
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'配件名称', dataIndex:'partsName'
			},{
				header:'配件编号', dataIndex:'partsNo'
			},{
				header:'规格型号', dataIndex:'specificationModel'
			},{
				header:'开始时间', dataIndex:'realStartTime', editor: {xtype: "my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i"}
			},{
				header:'结束时间', dataIndex:'realEndTime', editor: {xtype: "my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i"}
			},{
				header:'下车车型', dataIndex:'unloadTrainType', width: 70
			},{
				header:'下车车号', dataIndex:'unloadTrainNo', width: 70
			},{
				header:'下车修程', dataIndex:'unloadRepairClass', width: 70
			},{
				header:'下车修次', dataIndex:'unloadRepairTime', width: 70
			}],
			toEditFn: function(grid, rowIndex){
				var record = grid.store.getAt(rowIndex);
				EquipWin.showWin(record.get("idx"));
			}
		});
		grid.store.on("beforeload", function(){
			var whereList = [];
			if(classId){
				var s = "exists (select 1 from pjjx_parts_rdp_" +
						"equip_card c where c.rdp_idx = th" +
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
	
	PartsRdpList.createGrid = createGrid;
	
	PartsRdpList.reload = function(_classId){
		if(grid == undefined) return;
		
		if(_classId == classId) return;
		classId = _classId;
		grid.store.load();
	}
});