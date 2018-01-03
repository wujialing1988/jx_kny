/**
 * 货车客车巡检信息录入, 车次列表
 */
Ext.onReady(function(){
	
	Ext.ns('TrainList');
	
	/**
	 * 车次列表
	 */
	TrainList.grid = new Ext.yunda.Grid({
		title : i18n.TruckFaultReg.TrainList,
		height : 300,
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		singleSelect: true,
	    tbar:[
	    	{	
				xtype:'textfield', id:'rail_way_Time', enableKeyEvents:true, emptyText:i18n.TruckFaultReg.quickSearch, listeners: {
		    		keyup: function(filed, e) {
						if (e.ENTER == e.keyCode) {
							TrainList.grid.store.load();
						}
					}
				}	
			}
	    ],
		fields: [{
	   			header:i18n.TruckFaultReg.id, dataIndex:'idx', hidden:true
			},{
				header:i18n.TruckFaultReg.train, dataIndex:'railwayTime', width:150
			},{
				header:i18n.TruckFaultReg.workGroup, dataIndex:'workTeamID',hidden:true
			},{
	   			header:i18n.TruckFaultReg.workGroupName, dataIndex:'workTeamName', hidden:true
			},{
	   			header:i18n.TruckFaultReg.workGroupSequence, dataIndex:'workTeamSeq', hidden:true
			},{
				header:i18n.TruckFaultReg.track, dataIndex:'trackNo',hidden:true
			},{
	   			header:i18n.TruckFaultReg.trackName, dataIndex:'trackName', hidden:true
			},{
	   			header:i18n.TruckFaultReg.workWayCode, dataIndex:'workTypeCode', hidden:true
			},{
				header:i18n.TruckFaultReg.workWay, dataIndex:'workType',hidden:true
			},{
	   			header:i18n.TruckFaultReg.workPropertyCode, dataIndex:'workNatureCode', hidden:true
			},{
				header:i18n.TruckFaultReg.workProperty, dataIndex:'workNature',hidden:true
			},{
				header:i18n.TruckFaultReg.checkTimeMin, dataIndex:'checkTime',hidden:true
			},{
				header:i18n.TruckFaultReg.plannedStartTime, dataIndex:'planStartTime', xtype:'datecolumn', hidden:true
			},{
				header:i18n.TruckFaultReg.plannedEndTime, dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true
			},{
				header:i18n.TruckFaultReg.inDirection, dataIndex:'comeDirectionNo',hidden:true
			},{
	   			header:i18n.TruckFaultReg.inDirectionName, dataIndex:'comeDirectionName', hidden:true
			},{
				header:i18n.TruckFaultReg.outDirection, dataIndex:'toDirectionNo',hidden:true
			},{
	   			header:i18n.TruckFaultReg.outDirectionName, dataIndex:'toDirectionName', hidden:true
			},{
	   			header:i18n.TruckFaultReg.dayNightCode, dataIndex:'dayNightTypeNo', hidden:true
			},{
				header:i18n.TruckFaultReg.dayNightName, dataIndex:'dayNightTypeName',hidden:true
			},{
	   			header:i18n.TruckFaultReg.classCode, dataIndex:'classNo', hidden:true
			},{
				header:i18n.TruckFaultReg.className, dataIndex:'className',hidden:true
			},{
				header:i18n.TruckFaultReg.status, dataIndex:'rdpPlanStatus',hidden:true
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