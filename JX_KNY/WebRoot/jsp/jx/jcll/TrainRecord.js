/** 机车主要部件定义维护*/
Ext.onReady(function(){
	Ext.namespace('TrainRecord');                       //定义命名空间
	
	var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });

	// 机车检修记录单结果双击显示界面
	TrainRecord.toEditFn = function(grid, rowIndex, e){		
		if(this.searchWin != null)  this.searchWin.hide();  
		var record = grid.store.getAt(rowIndex);
	    // 检修记录单界面显示信息
		TrainRecordRdpPlan.setParams(record.json);
		TrainRecordRdpPlan.saveWin.show();
	} 
	
		
	// 车型列表
	TrainRecord.trainTypeGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/trainRecord!pageList.action",
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
				TrainRecord.TrainRecordGrid.getStore().load();
			});
	//移除侦听器
	TrainRecord.trainTypeGrid.un('rowdblclick', TrainRecord.trainTypeGrid.toEditFn, TrainRecord.trainTypeGrid);
	
	// 机车履历列表
	TrainRecord.TrainRecordGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlan!pageQuery.action',                 //装载列表数据的请求URL 
	    tbar: [{text : "刷新", iconCls : "refreshIcon",  width: 40,
				handler : function(){
					TrainRecord.TrainRecordGrid.store.load();
				}
			}],
	    fields: [{
	   			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
			},{
				header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, searcher: {anchor:'98%'}
			},{
				header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
			},{
				header:'修程主键', dataIndex:'repairClassIDX',hidden:true, editor:{  maxLength:8 }
			},{
				header:'修程', dataIndex:'repairClassName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
			},{
				header:'修次主键', dataIndex:'repairtimeIDX',hidden:true, editor:{  maxLength:8 }
			},{
				header:'修次', dataIndex:'repairtimeName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
			},{
				header:'备注', dataIndex:'remarks', hidden:true,editor:{ xtype:'textarea', maxLength:1000 }
			},{
				header:'日历', dataIndex:'workCalendarIDX',hidden:true, editor:{  maxLength:50 }
			},{
				header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
			},{
				header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true,editor:{ xtype:'my97date' }
			},{
				header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn', hidden:true,editor:{ xtype:'my97date' }
			},{
				header:'修峻时间', dataIndex:'endTime', xtype:'datecolumn',  editor:{ xtype:'my97date' }
			}],
			toEditFn: function(grid, rowIndex, e){
				var record = grid.store.getAt(rowIndex);	
		        TrainRecord.toEditFn(grid, rowIndex, e);
		    }
	});
	
	// 数据加载前
	TrainRecord.TrainRecordGrid.store.on('beforeload', function() {
		var sm = TrainRecord.trainTypeGrid.getSelectionModel();
		var searchParams = {};
		searchParams.vehicleType = vehicleType ;
		if (sm.getCount() > 0) {
			var records = sm.getSelections();
			searchParams.trainTypeIDX = records[0].data.trainTypeIDX;
			searchParams.trainNo = records[0].data.trainNo;
		}	
	
		var whereList = []; 
		for(prop in searchParams){
			whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.EQ, stringLike: false});	
		}
		whereList.push({propName:"workPlanStatus", propValue:"COMPLETE", compare:Condition.EQ});	
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	
	//tab选项卡布局
	TrainRecord.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "TrainRecordTab", title: '车辆检修履历', layout:'fit', items: [TrainRecord.TrainRecordGrid]
	    }]
	});
	//机车组成页面
	TrainRecord.TrainRecordPanel =  new Ext.Panel( {
	    layout : 'border',
	    items : [ {
	        title: '车型车号', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        collapsible : true,
	        autoScroll: true, layout: 'fit', items : [ TrainRecord.trainTypeGrid ]
	    }, {
	        region : 'center', layout: 'fit', bodyBorder: false, items: [ TrainRecord.tabs ]
	    } ]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainRecord.TrainRecordPanel });
});