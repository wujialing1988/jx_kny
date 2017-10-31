/**
 * 车辆状态跟踪
 */
Ext.onReady(function(){
	
	Ext.ns('ZtgzList');
	
	/** **************** 定义全局变量开始 **************** */
	ZtgzList.labelWidth = 55;
	
	ZtgzList.queryTimeout;
	
	/**
	 * 车辆列表
	 */
	ZtgzList.Grid = new Ext.yunda.Grid({
		loadURL: ctx + "/jczlTrain!pageQuery.action",
		singleSelect: true, 
		saveFormColNum:2,
		labelWidth:120,
	    tbar:[
	    	{	
				xtype:'textfield', id:'query_train_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
		    		keyup: function(filed, e) {
		    			if (ZtgzList.queryTimeout) {
		    				clearTimeout(ZtgzList.queryTimeout);
		    			}
		    			
		    			ZtgzList.queryTimeout = setTimeout(function(){
		    				ZtgzList.Grid.store.load();
		    			}, 1000);
		    		}
				}	
				
			}
	    ],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{ 			
					 xtype:"hidden"
			},searcher:{}
		},{
			header:'车号', dataIndex:'trainNo', editor:{ minLength:4 , maxLength:5 , allowBlank: false},
			searcher: {xtype: 'textfield'}
		},{
			header:'当前车辆状态', dataIndex:'trainState',renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				var trainState = record.get('trainState');
				return ZtgzList.getTrainStateName(trainState);
			},
			editor:{xtype: "hidden"},searcher:{disabled: true}
		}],
		beforeShowEditWin: function(record, rowIndex){  
			return false;
		}		
	});
	
	
	// 数据加载前
	ZtgzList.Grid.store.on('beforeload', function() {
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('query_train_no').getValue())){
			var query = Ext.getCmp('query_train_no').getValue();
			var sql = " (TRAIN_TYPE_SHORTNAME LIKE '%" + query + "%'"
					+ " OR TRAIN_NO LIKE '%" + query + "%')";
			whereList.push({ sql: sql, compare: Condition.SQL});
		}
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 添加加载结束事件
	ZtgzList.Grid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			ZtgzList.Grid.getSelectionModel().selectFirstRow();
			ZtgzList.reloadRecords();
		}else{
			ZtgzRecord.trainTypeIDX = "###" ; // 车型主键
			ZtgzRecord.trainNo = "###" ; // 车号
			ZtgzRecord.Grid.store.reload();
		}
	});
	
	// 单击刷新状态跟踪列表
	ZtgzList.Grid.on("rowclick", function(grid, rowIndex, e){
		ZtgzList.reloadRecords();
	});
	
	// 刷新纪录
	ZtgzList.reloadRecords = function(){
		var records = ZtgzList.Grid.getSelectionModel().getSelections();
		ZtgzRecord.trainTypeIDX = records[0].data.trainTypeIDX ; // 车型主键
		ZtgzRecord.trainNo = records[0].data.trainNo ; // 车号
		ZtgzRecord.Grid.store.reload();
	};
	
	/**
	 * 车辆状态获取
	 */
	ZtgzList.getTrainStateName = function(trainState){
			switch(trainState){
				case trainStateRepair:
					return "检修";
				case trainStateUse:
					return "运用";
				case trainStateSpare:
					return "列检";
				case trainStateDetain:
					return "扣车";					
				default:
					return "";
			}
	}
	

});