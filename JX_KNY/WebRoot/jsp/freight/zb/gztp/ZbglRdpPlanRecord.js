/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {

	Ext.ns('ZbglRdpPlanRecord');
	
	/** 查询列检计划车辆列表的参数 */
	ZbglRdpPlanRecord.rdpPlanIdx = "###" ; // 列检计划ID
	ZbglRdpPlanRecord.rdpPlanRec;
	/**
	 * 实现业务处理，车辆列表选中时触发调用
	 */
	ZbglRdpPlanRecord.rowclickCallback = function(rec){
		// 故障部件下拉树参数设置
		var vehicleComponent = Ext.getCmp("vehicle_component");
		vehicleComponent.rootText = rec.trainTypeCode ? rec.trainTypeCode : '无';
		vehicleComponent.queryParams = {shortName : rec.trainTypeCode};
		
		// 作业范围下拉树参数设置
		var scopeWork = Ext.getCmp("scope_work");
		scopeWork.rootText = ("专业类型");
		scopeWork.queryParams = {planIdx : ZbglRdpPlanRecord.rdpPlanIdx};
		
		// 为表单设置初始值
		GztpTicket.formValues = {
			railwayTime: ZbglRdpPlanRecord.rdpPlanRec.railwayTime,
			railwayTimeShow: ZbglRdpPlanRecord.rdpPlanRec.railwayTime,
			vehicleTypeCode: rec.trainTypeCode,
			vehicleTypeCodeShow: rec.trainTypeCode,
			trainNo: rec.trainNo,
			trainNoShow: rec.trainNo,
			handleType: HANDLE_TYPE_REG,
			faultNoticeStatus: STATUS_OVER,
			// 隐藏域值
			rdpPlanIdx: ZbglRdpPlanRecord.rdpPlanRec.idx,
			vehicleTypeIdx: rec.trainTypeIdx,
			rdpRecordPlanIdx: rec.idx,
			rdpNum: ZbglRdpPlanRecord.rdpPlanRec.rdpNum,
			rdpIdx: rec.rdpIdx
		}
		GztpTicket.resetForm();
		
		GztpTicket.searchParam.rdpRecordPlanIdx = rec.idx;
		GztpTicket.grid.store.reload();
		MatTypeUseList.grid.store.removeAll();
		
	};
	
	
	// 填写车号窗口 
	ZbglRdpPlanRecord.writeTrainNoWin = new Ext.Window({
		title:i18n.TruckFaultReg.enterTrainNum, width:400, height:250, plain:true, closeAction:"hide", buttonAlign:'center',padding:15,
    	maximizable:false,  modal:true,
    	items:[{
        	xtype: 'form',
        	defaultType: "textfield",
        	layout: "form",
        	baseCls: "x-plain",
            items: [
                { 
                	id:'writeTrainNo',
                	fieldLabel: '车号',
                	name:'trainNo',
                	allowBlank:false,
                	maxLength:20
                }
            ]
		}],
    	buttons: [{
			text : i18n.TruckFaultReg.ok,iconCls : "saveIcon", handler: function(){
				var idx = $yd.getSelectedIdx(ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid)[0];
				var writeTrainNo = Ext.getCmp("writeTrainNo").getValue();
				if(Ext.isEmpty(writeTrainNo)){
					return ;
				}
				var entityJson = {
					idx:idx,
					trainNo:writeTrainNo
				}
				Ext.Ajax.request({
					url: ctx + "/zbglRdpPlanRecord!writeTrainNo.action",
					params: {entityJson:Ext.util.JSON.encode(entityJson)},
					success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
							alertSuccess();
							Ext.getCmp("writeTrainNo").reset();
							ZbglRdpPlanRecord.writeTrainNoWin.hide();
							ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.reload();
						}else{
							alertFail(result.errMsg);
						}
					},
					failure: function(response, options){
						MyExt.Msg.alert(i18n.TruckFaultReg.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		},{
	        text: i18n.TruckFaultReg.close, iconCls: "closeIcon", scope: this, handler: function(){ ZbglRdpPlanRecord.writeTrainNoWin.hide(); }
		}]
	});	

	/**
	 * 列检计划车辆列表
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid = new Ext.yunda.Grid({
		title : i18n.TruckFaultReg.trainList,
		region : 'center',
		loadURL : ctx + "/zbglRdpPlanRecord!pageList.action",
		singleSelect : true,
		tbar : null,
		page: false,
		pageSize: 100,
		storeAutoLoad: false,
		fields : [{
				header : i18n.TruckFaultReg.id,
				dataIndex : 'idx',
				hidden : true
			}, {
				header : '<div>' + i18n.TruckFaultReg.number + '<span style="color:green;">' + i18n.TruckFaultReg.trainTypeNumber + '</span></div>',
				dataIndex : 'seqNum',
				renderer: function(value, metaData, record, rowIndex, colIndex, store) {
					var trainTypeCode = Ext.isEmpty(record.data.trainTypeCode) ? "" : record.data.trainTypeCode ;
					var trainNo =  Ext.isEmpty(record.data.trainNo)?"":record.data.trainNo;
					if (Ext.isEmpty(trainTypeCode) && Ext.isEmpty(trainNo)) {
						return i18n.TruckFaultReg.the + value + i18n.TruckFaultReg.unrecorded;
					}
					var trainInfo = trainTypeCode + ' ' +trainNo ;
					return i18n.TruckFaultReg.the + value + i18n.TruckFaultReg.is + trainInfo+"】";
				}
			}, {
				header : i18n.TruckFaultReg.trainTypeName,
				dataIndex : 'trainTypeName',
				hidden : true
			},
			{header : i18n.TruckFaultReg.trainCode2,dataIndex : 'trainTypeCode',hidden : true},
			{header : i18n.TruckFaultReg.trainStatus,dataIndex : 'rdpRecordStatus',hidden : true},
			{header : i18n.TruckFaultReg.trainIspectionID,dataIndex : 'rdpIdx',hidden : true},
			{header : i18n.TruckFaultReg.trainTypeID,dataIndex : 'trainTypeIdx',hidden : true},
			{header : i18n.TruckFaultReg.setupIdx,dataIndex : 'rdpIdx',hidden : true},
			{
				header : i18n.TruckFaultReg.trainNumber1,
				dataIndex : 'trainNo',
				hidden : true
			}
		],
		beforeShowEditWin: function(record, rowIndex){
			return false;
		},
		toEditFn: function(grid, rowIndex, e){
			ZbglRdpPlanRecord.writeTrainNoWin.show();
		}
	});
	
	/**
	 * 单击车辆列表后，当重写rowclickCallback方法后执行该方法的业务逻辑
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.on("rowclick", function(grid, rowIndex, e){
		// 获取当前选中行数据，并调用业务处理方法
		var sm = ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getSelectionModel().getSelections();
		ZbglRdpPlanRecord.rowclickCallback(sm[0].data);
	});
			
	// 默认排序		
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.setDefaultSort("seqNum", "ASC");
			
	// 数据加载前
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpPlanIdx = ZbglRdpPlanRecord.rdpPlanIdx;
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});

	// 添加加载结束事件
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().addListener('load',
		function(me, records, options) {
			if (records.length > 0) {
				ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getSelectionModel().selectFirstRow(); // 默认选中第一行
				// 获取当前选中行数据，并调用业务处理方法
				var sm = ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getSelectionModel().getSelections();
				ZbglRdpPlanRecord.rowclickCallback(sm[0].data);
			}else{
				
			}
		}
	);

});