Ext.onReady(function(){

	Ext.ns("DeviceList")
	
	var classId = null;
	var events = [];
	var grid = grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/deviceInfo!pageList.action',                 //装载列表数据的请求URL
	    tbar: ['refresh'],
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'设备编码', dataIndex:'deviceInfoCode', editor:{  allowBlank: false,maxLength:50 }
		},{
			header:'设备名称', dataIndex:'deviceInfoName', editor:{  allowBlank: false,maxLength:50 }
		},{
			header:'设备分类编码', dataIndex:'deviceTypeCode',hidden:true, editor:{id:"deviceTypeCode"}
		},{
			header:'设备分类名称', dataIndex:'deviceTypeName', editor:{}, hidden: true
		},{
			header:'设备描述', dataIndex:'deviceInfoDesc', editor:{  maxLength:500 }
		}],
		toEditFn: function(g, rowIndex){
			//var record = this.store.getAt(rowIndex);
			//Unknow.showWin(record.get("idx"));
		}
	});
	grid.on("render", function(){
		this.topToolbar.setVisible(false);
	});
	grid.store.on("beforeload", function(){
		var sp = {};
		if(classId)
			sp.deviceTypeCode = classId;
		this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
	
	grid.on("rowclick", function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		for(var i = 0; i < events.length; i++){
			events[i](record, grid);
		}
	});
	
	DeviceList.grid = grid;
	
	DeviceList.reload = function(_classId){
		if(_classId == classId) return;
		classId = _classId;
		grid.store.load();
	};
	
	DeviceList.regGridClick = function(callback){
		events.push(callback);
	};
});