Ext.onReady(function(){
	
	Ext.ns("DeviceRdp");
	
	var grid, searchParams;
	var rowclickCallback = [];
	function createGrid(){
		grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/partsRdpUnionDevice!findPageList.action',                //装载列表数据的请求URL
		    tbar:["refresh"],
		    singleSelect: true,
		    storeAutoLoad: false,
		    viewConfig: null,
			fields: [{
				header:'rdpIdx', dataIndex:'rdpIdx', hidden:true
			},{
				header:'设备编号', dataIndex:'deviceInfoCode'
			},{
				header:'设备名称', dataIndex:'deviceInfoName'
			},{
				header:'配件名称', dataIndex:'partsName'
			},{
				header:'配件编号', dataIndex:'partsNo'
			},{
				header:'规格型号', dataIndex:'specificationModel'
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
				//var record = grid.store.getAt(rowIndex);
				//EquipWin.showWin(record.get("idx"));
			}
		});
		
		grid.store.on("beforeload", function(){
			var sp = MyJson.clone(searchParams || {});
			sp.startTime = setDate(sp.startTime);
			sp.endTime = setDate(sp.endTime);
			this.baseParams.searchJson = Ext.util.JSON.encode(sp);
		});
		
		grid.store.on("load", function(){
			if(this.getCount() > 0){
				
				rowclickHandle(grid, 0);
				grid.getSelectionModel().selectRow(0, true);
			}else{
				DeviceRdp.noData();
			}
		});
		
		grid.on("rowclick", rowclickHandle);
		
		grid.on("render", function(){
			this.topToolbar.setVisible(false);
		});
		return grid;
	}
	
	function rowclickHandle(grid, rowIndex){
		for(var i = 0; i < rowclickCallback.length; i++){
			rowclickCallback[i](grid, rowIndex);
		}
	}
	
	function setDate(text){
		var reg = /\d{4}\-\d{2}\-\d{2} \d{2}:\d{2}/; 
		if(reg.test(text)){
			return text + ":00";
		}
		return text
	}
	
	DeviceRdp.createGrid = createGrid;
	
	DeviceRdp.reload = function(sp){
		searchParams = MyJson.clone(sp);
		grid.store.load();
	}
	
	DeviceRdp.rowClickCallback = function(callback){
		rowclickCallback.push(callback);
	}
	
	/**
	 * 此方法用做被覆盖
	 */
	DeviceRdp.noData = function(){ }
});