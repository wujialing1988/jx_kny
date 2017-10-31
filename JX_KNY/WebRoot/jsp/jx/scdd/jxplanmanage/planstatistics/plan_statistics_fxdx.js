Ext.onReady(function(){
	
	Ext.ns("FXDX");
	
	//beforeloadset用于对查询进行过滤，初步由planning.jsp页面调用使用
	function createGrid(beforeloadset){
		
		var grid = new Ext.yunda.Grid({
			
			loadURL: ctx + "/runningKM!pageQuery.action",
			saveURL: ctx,
			viewConfig: null,
			storeAutoLoad: false,
			tbar: ["refresh"],
			fields: [{
				dataIndex:"idx", hidden:true
			},{
				dataIndex:"trainTypeIdx", hidden:true
			},{
				header: "车型", dataIndex: "trainType"
			},{
				header: "车号", dataIndex: "trainNo"
			},{
				header: "新造", dataIndex: "newRunningKm"
			},{
				header: "辅修", dataIndex: "c1"
			},{
				header: "小修", dataIndex: "c2"
			},{
				header: "中修", dataIndex: "c3"
			},{
				header: "大修", dataIndex: "c4"
			},{
				header: "走行公里", dataIndex: "recentlyRunningKm"
			},{
				header: "下次修程", dataIndex: "repairClassName"
			},{
				header: "下次修次", dataIndex: "repairOrderName"
			},{
				dataIndex: "repairClass", hidden: true
			},{
				dataIndex: "repairOrder", hidden: true
			}],
			toEditFn: function(){},
			searchFn: function(sp){
				this.store.sp = MyJson.deleteBlankProp(sp);
				this.store.load();
			}
		});
		grid.store.setDefaultSort("c1", "desc");
		grid.store.on("beforeload", function(){
			var whereList = [];
			whereList.push({propName: "repairType", propValue: fxdx_val, stringLike: false});
			if(typeof(beforeloadset) === 'function'){
				beforeloadset(whereList);
			}
			for(var i in this.sp){
				whereList.push({propName: i, propValue: this.sp[i]});
			}
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
		grid.store.reload();
		//FXDX.grid = grid;
		return grid;
	}
	FXDX.createGrid = createGrid;
});