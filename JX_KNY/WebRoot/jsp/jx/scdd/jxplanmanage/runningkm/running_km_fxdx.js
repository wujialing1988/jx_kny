Ext.onReady(function(){
	
	Ext.ns("FXDX");
	
	var grid = new Ext.yunda.Grid({
		
		loadURL: ctx + "/runningKM!pageQuery.action",
		saveURL: ctx,
		viewConfig: null,
		singleSelect: true,
		storeAutoLoad: false,
		tbar: ["add", {
			text: "历史数据",
			iconCls: "gridIcon",
			handler: function(){
				var reocrd;
				if(record = $yd.getSingleRecord(grid)){
					var trainType = record.get("trainType");
					var trainNo = record.get("trainNo");
					FXDXHistory.show(trainType, trainNo);
				}
			}
		}, {
			text: "导入走行",
			iconCls: "uploadIcon",
			handler: function(){
				reg.showUpload(grid);
			}
		}/*,statistics*/, template, {
			text: "计算下次修程",
			iconCls: "application-vnd-ms-excel",
			handler: function(){
				computeXC.call(this, grid, fxdx_val);
			}
		}, "refresh"],
		fields: [{
			dataIndex:"idx", hidden:true
		},{
			dataIndex:"repairType", hidden:true
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
			header: "最近", dataIndex: "recentlyRunningKm"
		},{
			header: "开始日期", dataIndex: "beginDate", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "结束日期", dataIndex: "endDate", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "登记日期", dataIndex: "regDate", xtype: "datecolumn", editor: {xtype: "my97date"}
		},{
			header: "下次修程", dataIndex: "repairClassName"
		},{
			header: "下次修次", dataIndex: "repairOrderName"
		},{
			dataIndex: "repairClass", hidden: true
		},{
			dataIndex: "repairOrder", hidden: true
		},{
			header: "撤销", width: 50,
			renderer: function(){
				return "<div class='undo' title='撤销' onclick='undo(false)'></div>";
			}
		},{
			header: "修后清零", width:70,
			renderer: function(){
				return "<div class='clear' title='修后清零' onclick='clearRKM(false)'></div>";
			}
		}],
		addButtonFn: function(){
			Edit.showWin(false, callback);
		},
		toEditFn: function(grid, rowIndex, e){
	        var record = this.store.getAt(rowIndex);
	        Edit.showWin(false, callback, record);
		},
		searchFn: function(sp){
			this.store.sp = MyJson.deleteBlankProp(sp);
			this.store.load();
		}
	});
	grid.store.setDefaultSort("beginDate", "desc");
	grid.store.on("beforeload", function(){
		var whereList = [];
		whereList.push({propName: "repairType", propValue: fxdx_val, stringLike: false});
		for(var i in this.sp){
			whereList.push({propName: i, propValue: this.sp[i]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	function callback(){
		grid.store.reload();
	}
	callback();
	FXDX.grid = grid;
});