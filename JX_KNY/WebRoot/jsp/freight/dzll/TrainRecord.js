/** 机车主要部件定义维护*/
Ext.onReady(function(){
	Ext.namespace('TrainRecord');                       //定义命名空间
	
	var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });

	// 车型列表
	TrainRecord.trainTypeGrid = new Ext.yunda.Grid({
		loadURL: ctx + "//jczlTrain!pageList.action",
		singleSelect: true, 
		tbar:[{	
			xtype:'textfield', id:'train_type_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索', listeners: {
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
						TrainRecord.trainTypeGrid.store.load();
					}
	    		}
			}
				
		},{
			text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
			handler : function(){
				TrainRecord.trainTypeGrid.store.load();
			}
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				Ext.getCmp('train_type_no').reset();
				TrainRecord.trainTypeGrid.store.load();
			}
		}],
			fields: [{
				header:'车型', dataIndex:'trainTypeShortName', editor:{ }, sortable: true
			},{
				header:'车号', dataIndex:'trainNo', editor:{ }
	        },{
	            header:'车型主键', dataIndex:'trainTypeIDX', hidden:true          
			}]
	});
	
	// 数据加载前
	TrainRecord.trainTypeGrid.store.on('beforeload', function() {
		var searchParams = {};
		if(!Ext.isEmpty(Ext.getCmp('train_type_no'))){
			searchParams.trainNo = Ext.getCmp('train_type_no').getValue();
		}
		searchParams.vehicleType = vehicleType ;
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	//单击车型记录过滤机车和虚拟组成列表
	TrainRecord.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		TrianWorkPlanRecord.load(record.get('trainTypeIDX'),record.get('trainNo'));
	});
	//移除侦听器
	TrainRecord.trainTypeGrid.un('rowdblclick', TrainRecord.trainTypeGrid.toEditFn, TrainRecord.trainTypeGrid);
	
	//tab选项卡布局
	TrainRecord.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "TrainJXTab", title: '车辆检修履历', layout:'fit', items:TrianWorkPlanRecord.mainPanel
	    }]
	});
	//机车组成页面
	TrainRecord.TrainRecordPanel =  new Ext.Panel( {
	    layout : 'border',
	    items : [ {
	        title: '车辆车号', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        collapsible : true,
	        autoScroll: true, layout: 'fit', items : [ TrainRecord.trainTypeGrid ]
	    }, {
	        region : 'center', layout: 'fit', bodyBorder: false, items: [ TrainRecord.tabs ]
	    } ]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainRecord.TrainRecordPanel });
});