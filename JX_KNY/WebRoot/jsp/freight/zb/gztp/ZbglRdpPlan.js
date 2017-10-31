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
		title : '列检计划列表',
		region : 'north',
		height : 300,
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		singleSelect: true,
	    tbar:[
	    	{	
				xtype:'textfield', id:'rail_way_Time', enableKeyEvents:true, emptyText:'输入车次快速检索', listeners: {
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
	   			header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'车次', dataIndex:'railwayTime', width:150
			},{
				header:'<div>股道<span style="color:green;">【车辆数】</span></div>', dataIndex:'rdpNum', width:150,
			        renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						var trackName = Ext.isEmpty(record.get('trackName'))?'':record.get('trackName');
						return trackName+":【"+value+"】";
					}
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