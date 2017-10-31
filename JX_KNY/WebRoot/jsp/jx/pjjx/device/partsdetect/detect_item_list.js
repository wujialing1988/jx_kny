Ext.onReady(function(){
	
	Ext.ns("DetectItem");
	
	var grid, searchParams;
	var rdpIdx;
	
	function handleSearchParams(sp, keys){
		if(searchParams == undefined) return;
		for(var i = 0; i < keys.length; i++){
			if(searchParams[keys[i]]){
				sp[keys[i]] = searchParams[keys[i]];
			}
		}
	}
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/partsRdpUnionDevice!findDetectItemList.action',                //装载列表数据的请求URL
		    tbar:["refresh"],
		    singleSelect: true,
		    storeAutoLoad: false,
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'工单描述', dataIndex:'equipCardDesc'
			},{
				header:'检测项名称', dataIndex:'dataItemName'
			},{
				header:'检测值', dataIndex:'itemValue', width: 100
			},{
				header:'检测值单位', dataIndex:'unit', width: 60
			},{
				header:'检测项描述', dataIndex:'dataItemDesc'
			}],
			toEditFn: function(grid, rowIndex){}
		});
		
		grid.store.on("beforeload", function(){
			var s = searchParams;
			if(s.rdpIdx == undefined) return false;
			var sp = {};
			if(s.workOrderDesc)
				sp.equipCardDesc = s.workOrderDesc;
			if(s.detectItemName)
				sp.dataItemName = s.detectItemName;
			if(s.detectItemValue)
				sp.itemValue = s.detectItemValue;
			if(s.rdpIdx)
				sp.rdpIdx = s.rdpIdx;
			
			this.baseParams.searchJson = Ext.util.JSON.encode(sp);
		});
		
		grid.on("render", function(){
			this.topToolbar.setVisible(false);
		});
		return grid;
	}
	
	DetectItem.createGrid = createGrid;
	
	DetectItem.reload = function(sp){
		searchParams = MyJson.clone(sp);
		//grid.store.load();//由rdpFilter执行查询
	}
	
	DetectItem.rdpFilter = function(_rdpIdx){
		searchParams.rdpIdx = _rdpIdx;
		grid.store.load();
	}
	
	DetectItem.clear = function(){
		grid.store.removeAll();
		delete searchParams.rdpIdx;
	}
});