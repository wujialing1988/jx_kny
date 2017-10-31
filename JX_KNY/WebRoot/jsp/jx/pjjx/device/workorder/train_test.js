Ext.onReady(function(){
	
	Ext.ns("TrainTest");
	
	var win, grid, initialized = false;
	var rdpIdx;
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/trainDeviceDetectDI!queryList.action",
			storeAutoLoad: false,
			singleSelect:true,
			fields:[{
				dataIndex: "idx", hidden:true
			},{
				header: "检测项编号", dataIndex: "itemNo"
			},{
				header: "检测项名称", dataIndex: "itemName"
			},{
				header: "检测项描述", dataIndex: "itemDesc"
			},{
				header: "单位", dataIndex: "itemUnit", width: 50
			},{
				header: "检测值", dataIndex: "itemValue"
			}]
		});
		
		grid.store.on("beforeload", function(){
			if(!rdpIdx) return false;
			this.baseParams.rdpIdx = rdpIdx;
			this.baseParams.whereListJson = Ext.util.JSON.encode([{sql: "sdf", compare: Condition.SQL}]);
		});
		
		grid.on("render", function(){
			this.topToolbar.setVisible(false);
		});
	}
	
	function createWin(){
		win = new Ext.Window({
			title:'查看整车试验项',
			//plain:true,
			modal: true,
			closeAction:'hide',
			layout:'fit',
			width:900,
			height:450,
			border: true,
			items: grid,
			buttonAlign: 'center',
			buttons:[{
				text: "关闭",
				iconCls: "closeIcon",
				handler: function(){
					win.hide();
				}
			}]
		});
	}
	
	function init(){
		createGrid();
		createWin();
		initialized = true;
	}
	
	TrainTest.showWin = function(_rdpIdx){
		rdpIdx = _rdpIdx;
		if(initialized == false) init();
		win.show();
		grid.store.load();
	};
});