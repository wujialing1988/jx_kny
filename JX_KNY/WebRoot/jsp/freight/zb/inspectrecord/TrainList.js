/**
 * 货车客车巡检信息录入, 车次列表
 */
Ext.onReady(function(){
	
	Ext.ns('TrainList');
	
	/**
	 * 车次列表
	 */
	TrainList.grid = new Ext.yunda.Grid({
		title : '车次列表',
		height : 300,
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		singleSelect: true,
	    tbar:[
	    	{	
				xtype:'textfield', id:'rail_way_Time', enableKeyEvents:true, emptyText:'输入车次快速检索', listeners: {
		    		keyup: function(filed, e) {
						if (e.ENTER == e.keyCode) {
							TrainList.grid.store.load();
						}
					}
				}	
			}
	    ],
		fields: [{
	   			header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'车次', dataIndex:'railwayTime', width:150
			},{
				header:'作业班组', dataIndex:'workTeamID',hidden:true
			},{
	   			header:'作业班组名称', dataIndex:'workTeamName', hidden:true
			},{
	   			header:'作业班组序列', dataIndex:'workTeamSeq', hidden:true
			},{
				header:'股道', dataIndex:'trackNo',hidden:true
			},{
	   			header:'股道名称', dataIndex:'trackName', hidden:true
			},{
	   			header:'作业方式编码', dataIndex:'workTypeCode', hidden:true
			},{
				header:'作业方式', dataIndex:'workType',hidden:true
			},{
	   			header:'作业性质编码', dataIndex:'workNatureCode', hidden:true
			},{
				header:'作业性质', dataIndex:'workNature',hidden:true
			},{
				header:'技检时间(分钟)', dataIndex:'checkTime',hidden:true
			},{
				header:'计划开始时间', dataIndex:'planStartTime', xtype:'datecolumn', hidden:true
			},{
				header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true
			},{
				header:'接入方向', dataIndex:'comeDirectionNo',hidden:true
			},{
	   			header:'接入方向名称', dataIndex:'comeDirectionName', hidden:true
			},{
				header:'发出方向', dataIndex:'toDirectionNo',hidden:true
			},{
	   			header:'发出方向名称', dataIndex:'toDirectionName', hidden:true
			},{
	   			header:'白夜班编码', dataIndex:'dayNightTypeNo', hidden:true
			},{
				header:'白夜班', dataIndex:'dayNightTypeName',hidden:true
			},{
	   			header:'班次编码', dataIndex:'classNo', hidden:true
			},{
				header:'班次', dataIndex:'className',hidden:true
			},{
				header:'状态', dataIndex:'rdpPlanStatus',hidden:true
			}],
			beforeShowEditWin: function(record, rowIndex){
				return false;
			}
	});
	
	TrainList.grid.on("rowclick", function(grid, rowIndex, e){
		// 获取当前选中行数据，并调用业务处理方法	
		var sm = TrainList.grid.getSelectionModel().getSelections();
		TrainInspectRecord.trainIdx = sm[0].id;
		TrainInspectRecord.grid.store.load();
	});
	
	// 数据加载前
	TrainList.grid.store.on('beforeload', function() {
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('rail_way_Time').getValue())){
			whereList.push({ propName: 'railwayTime', propValue: Ext.getCmp('rail_way_Time').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	TrainList.grid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			TrainList.grid.getSelectionModel().selectFirstRow(); // 默认选中第一行
			// 获取当前选中行数据，并调用业务处理方法	
			var sm = TrainList.grid.getSelectionModel().getSelections();
			TrainInspectRecord.trainIdx = sm[0].id;
			TrainInspectRecord.grid.store.load();
		}
	});
});