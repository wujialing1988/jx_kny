Ext.onReady(function(){

	Ext.ns("TestDeviceRdpCardDi")
	TestDeviceRdpCardDi.rdpEquipCardIDX;//定义设备工单实例主键
	
	TestDeviceRdpCardDi.grid = grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpEquipDI!pageList.action',                 //装载列表数据的请求URL
	    tbar: ['refresh'],
	    storeAutoLoad: false,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'设备工单实例主键', dataIndex:'rdpEquipCardIDX', hidden:true
		},{
			header:'检测项编号', dataIndex:'dataItemNo'
		},{
			header:'检测项名称', dataIndex:'dataItemName'
		},{
			header:'检测项描述', dataIndex:'dataItemDesc'
		},{
			header:'设备编码', dataIndex:'deviceInfoCode'
		},{
			header:'单位', dataIndex:'unit'
		},{
			header:'检测值', dataIndex:'itemValue'
		}],
		toEditFn: function(g, rowIndex){}
	});
	TestDeviceRdpCardDi.grid.store.on("beforeload", function(){
		var sp = {};
		sp.rdpEquipCardIDX = TestDeviceRdpCardDi.rdpEquipCardIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
});