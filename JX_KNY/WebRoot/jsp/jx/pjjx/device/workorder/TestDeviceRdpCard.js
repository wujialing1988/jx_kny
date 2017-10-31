Ext.onReady(function(){

	Ext.ns("TestDeviceRdpCard")
	
	TestDeviceRdpCard.grid = grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpEquipCard!pageList.action',                 //装载列表数据的请求URL
	    tbar: ['refresh'],
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'工单编号', dataIndex:'equipCardNo'
		},{
			header:'工单描述', dataIndex:'equipCardDesc'
		},{
			header:'设备分类编码', dataIndex:'deviceTypeCode'
		},{
			header:'设备分类名称', dataIndex:'deviceTypeName'
		},{
			header:'设备编码', dataIndex:'deviceInfoCode'
		},{
			header:'作业人', dataIndex:'workEmpID', hidden:true
		},{
			header:'作业人名称', dataIndex:'workEmpName'
		},{
			header:'作业开始时间', dataIndex:'workStartTime', xtype:'datecolumn', format: "Y-m-d H:i:s"
		},{
			header:'作业结束时间', dataIndex:'workEndTime', xtype:'datecolumn', format: "Y-m-d H:i:s"
		},{
			header:'数据生成时间', dataIndex:'dataGenTime', xtype:'datecolumn', format: "Y-m-d H:i:s"
		},{
			header:'作业结果', dataIndex:'workResult'
		},{
			header:'备注', dataIndex:'remarks'
		},{
			header:'状态', dataIndex:'status',
			renderer: function(v){
	            switch(v){
	                case '01':
	                    return "未处理";
	                case '02':
	                    return "已处理";
	                default:
	                    return v;
	            }
	        }
		},{
			header:'规格型号', dataIndex:'specificationModel'
		},{
			header:'配件名称', dataIndex:'partsName'
		},{
			header:'配件编号', dataIndex:'partsNo'
		}],
		toEditFn: function(g, rowIndex){}
	});
	
	TestDeviceRdpCard.grid.on("rowclick", function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		TestDeviceRdpCardDi.rdpEquipCardIDX = record.get("idx");
		TestDeviceRdpCardDi.grid.store.load();
	});
});