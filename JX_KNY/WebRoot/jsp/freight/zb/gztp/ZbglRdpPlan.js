/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpPlan');
	
	/**
	 * 实现业务处理，列检计划列表选中时触发调用
	 */
	ZbglRdpPlan.rowclickCallback = function(rec){
		// 为车辆列表查询设置参数，ZbglRdpPlanRecord.js
		ZbglRdpPlanRecord.rdpPlanIdx = rec.idx;
		ZbglRdpPlanRecord.rdpPlanRec = rec;
		ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.reload();
	};
	
	ZbglRdpPlan.queryTimeout;
	
	/**
	 * 列检计划列表
	 */
	ZbglRdpPlan.ZbglRdpPlanGrid = new Ext.yunda.Grid({
		title : i18n.TruckFaultReg.inspectionPlanList,
		region : 'north',
		height : 300,
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		singleSelect: true,
	    tbar:[
	    	{	
				xtype:'textfield', id:'rail_way_Time', enableKeyEvents:true, emptyText:i18n.TruckFaultReg.quickSearch, listeners: {
		    		keyup: function(filed, e) {
						if (ZbglRdpPlan.queryTimeout) {
		    				clearTimeout(ZbglRdpPlan.queryTimeout);
		    			}
		    			
						ZbglRdpPlan.queryTimeout = setTimeout(function(){
		    				ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
		    			}, 1000);
		    		}
				}	
			}
	    ],
		fields: [{
	   			header:i18n.TruckFaultReg.id, dataIndex:'idx', hidden:true
			},{
				header:i18n.TruckFaultReg.train, dataIndex:'railwayTime', width:150
			},{
				header:'<div>' + i18n.TruckFaultReg.track + '<span style="color:green;">' + i18n.TruckFaultReg.VehicleNumber + '</span></div>', dataIndex:'rdpNum', width:150,
			        renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						var trackName = Ext.isEmpty(record.get('trackName'))?'':record.get('trackName');
						return trackName+":【"+value+"】";
					}
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
	
	/**
	 * 单击车次列检计划列表后，当重写rowclickCallback方法后执行该方法的业务逻辑
	 */
	ZbglRdpPlan.ZbglRdpPlanGrid.on("rowclick", function(grid, rowIndex, e){
		// 获取当前选中行数据，并调用业务处理方法	
		var sm = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().getSelections();
		ZbglRdpPlan.rowclickCallback(sm[0].data);
	});
	
	// 数据加载前
	ZbglRdpPlan.ZbglRdpPlanGrid.store.on('beforeload', function() {
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('rail_way_Time').getValue())){
			whereList.push({ propName: 'railwayTime', propValue: Ext.getCmp('rail_way_Time').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'rdpPlanStatus', propValue:STATUS_HANDLING, compare: Condition.EQ});
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'siteID', propValue:siteId, compare: Condition.EQ});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 添加加载结束事件
	ZbglRdpPlan.ZbglRdpPlanGrid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().selectFirstRow(); // 默认选中第一行
			// 获取当前选中行数据，并调用业务处理方法	
			var sm = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().getSelections();
			ZbglRdpPlan.rowclickCallback(sm[0].data);
		}else{
				
		}
	});
});