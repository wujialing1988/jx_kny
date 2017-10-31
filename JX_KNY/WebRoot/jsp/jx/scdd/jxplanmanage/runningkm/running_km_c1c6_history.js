Ext.onReady(function(){
	
	Ext.ns("C1C6History");
	
	var grid, win, initialized = false, trainType = trainNo = undefined;
	
	function resetDate(){
		var date = new Date();
		Ext.getCmp("c1c6_history_end_date_id").setValue(date);
		date.setDate(date.getDate() - 30);
		Ext.getCmp("c1c6_history_begin_date_id").setValue(date);
	}
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/runningKM!findHistoryPageQuery.action",
			saveURL: ctx,
			viewConfig: null,
			singleSelect: true,
			storeAutoLoad: false,
			tbar: ["开始日期：",{
				xtype: "my97date",
				id: "c1c6_history_begin_date_id",
				my97cfg:{
					maxDate: '#F{$dp.$D(\'c1c6_history_end_date_id\')||%y-%M-%d}'
				},
				initNow: false
			}, "至：",{
				xtype: "my97date",
				my97cfg:{
					minDate: '#F{$dp.$D(\'c1c6_history_begin_date_id\')}',
					maxDate: '%y-%M-%d'
				},
				id: "c1c6_history_end_date_id"
			},{
				text: "查询",
				iconCls: "searchIcon",
				handler: function(){
					grid.store.load();
				}
			},{
				text: "重置",
				iconCls: "resetIcon",
				handler: function(){
					resetDate();
					grid.store.load();
				}
			}],
			fields: [{
				dataIndex:"idx", hidden:true
			},{
				dataIndex:"repairType", hidden:true
			},{
				dataIndex:"trainTypeIdx", hidden:true
			/*},{
				header: "车型", dataIndex: "trainType"
			},{
				header: "车号", dataIndex: "trainNo"*/
			},{
				header: "开始日期", dataIndex: "beginDate", xtype: "datecolumn", editor: {xtype: "my97date"}
			},{
				header: "结束日期", dataIndex: "endDate", xtype: "datecolumn", editor: {xtype: "my97date"}
			},{
				header: "登记日期", dataIndex: "regDate", xtype: "datecolumn", editor: {xtype: "my97date"}
			},{
				header: "本次走行", dataIndex: "recentlyRunningKm"
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
			],
			toEditFn: function(grid, rowIndex, e){ },
			searchFn: function(sp){
				this.store.sp = MyJson.deleteBlankProp(sp);
				this.store.load();
			}
		});
		grid.store.setDefaultSort("beginDate", "desc");
		grid.store.on("beforeload", function(){
			var whereList = [];
			whereList.push({propName: "repairType", propValue: c1c6_val, stringLike: false});
			whereList.push({propName: "trainType", propValue: trainType, stringLike: false});
			whereList.push({propName: "trainNo", propValue: trainNo, stringLike: false});
			
			var begin = Ext.getCmp("c1c6_history_begin_date_id").getValue();			
			var end = Ext.getCmp("c1c6_history_end_date_id").getValue();
			
			if(begin){
				whereList.push({propName: "beginDate", propValue: begin, compare: Condition.GE});
			}
			if(end){
				whereList.push({propName: "beginDate", propValue: end, compare: Condition.LE});
			}
			
			for(var i in this.sp){
				whereList.push({propName: i, propValue: this.sp[i]});
			}
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "历史走行数据查看",width:900, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			items: grid,
			buttons:[{
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					win.hide();
		        }
		    }]
		});
	}
	
	function initialize(){
		createGrid();
		createWin();
		initialized = true;
	}
	
	C1C6History.show = function(_trainType, _trainNo){
		trainType = _trainType;
		trainNo = _trainNo;
		if(initialized == false) initialize();
		win.show();
		win.setTitle(trainType + "/" + trainNo + " 历史走行数据查看");
		
		resetDate();
		grid.store.load();
	}
});