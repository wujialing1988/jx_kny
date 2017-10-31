Ext.onReady(function(){
	
	Ext.ns("EquipCardDI");
	
	var grid, cardIdx;
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpEquipDI!pageList.action",
			storeAutoLoad: false,
			singleSelect:true,
			viewConfig: null,
			fields:[{
				dataIndex: "idx", hidden:true
			},{
				header: "检测项编号", dataIndex: "dataItemNo"
			},{
				header: "检测项名称", dataIndex: "dataItemName"
			},{
				header: "检测项描述", dataIndex: "dataItemDesc"
			},{
				header: "单位", dataIndex: "unit", width: 50
			},{
				header: "检测值", dataIndex: "itemValue"
			}]
		});
		
		grid.store.on("beforeload", function(){
			if(!cardIdx) return false;
			var sp = {};
			sp.rdpEquipCardIDX = cardIdx;
			this.baseParams.entityJson = Ext.util.JSON.encode(sp);
		});
		
		grid.on("render", function(){
			this.topToolbar.setVisible(false);
		});
		return grid;
	}
	
	EquipCardDI.createGrid = createGrid;
	
	EquipCardDI.clear = function(){
		grid.store.removeAll();
	}
	
	EquipCardDI.reload = function(_cardIdx){
		cardIdx = _cardIdx;
		grid.store.load();
	}
});