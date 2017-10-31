Ext.onReady(function(){
	
	Ext.ns("C1C6");
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
				header: "车型", dataIndex: "trainType"
			},{
				header: "车号", dataIndex: "trainNo"
			},{
				header: "新造", dataIndex: "newRunningKm"
			},
			// 修改20160830 by wujl c1 c2辅修 c3小修 c4 c5中修 c6大修
			{
				header: "C1", dataIndex: "c1"
			},{
				header: "C2", dataIndex: "c1"
			},{
				header: "C3", dataIndex: "c2"
			},{
				header: "C4", dataIndex: "c3"
			},{
				header: "C5", dataIndex: "c3"
			},{
				header: "C6", dataIndex: "c4"
			}
			,{
				header: "走行公里", dataIndex: "recentlyRunningKm"
			},{
				header: "下次修程", dataIndex: "repairClassName"
			},{
				header: "下次修次", dataIndex: "repairOrderName"
			},{
				dataIndex: "repairClass", hidden: true
			},{
				dataIndex: "repairOrder", hidden: true
			},{
				dataIndex:"trainTypeIdx", hidden:true
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
			whereList.push({propName: "repairType", propValue: c1c6_val, stringLike: false});
			
			if(typeof(beforeloadset) === 'function'){
				beforeloadset(whereList);
			}
			
			for(var i in this.sp){
				whereList.push({propName: i, propValue: this.sp[i]});
			}
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
		grid.store.reload();
		//C1C6.grid = grid;
		return grid;
	}
	C1C6.createGrid = createGrid;
});