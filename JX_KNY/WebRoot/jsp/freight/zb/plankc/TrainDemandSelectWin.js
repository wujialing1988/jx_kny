/**
 * 组织列表选择
 */
Ext.onReady(function(){
	
	Ext.namespace('TrainDemandSelectWin');                       //定义命名空间
	
	TrainDemandSelectWin.queryTimeout;							// 延迟刷新
	
	// 定义表格开始
	TrainDemandSelectWin.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainDemand!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    saveForm: TrainDemandSelectWin.saveForm,
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
	    tbar : ['&nbsp;&nbsp;',
	    {
	    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:i18n.PassengerInspectionPlan.quickSearch, width:200,
	    	listeners: {
	    		keyup: function(filed, e) {
	    			if (TrainDemandSelectWin.queryTimeout) {
	    				clearTimeout(TrainDemandSelectWin.queryTimeout);
	    			}
	    			
	    			TrainDemandSelectWin.queryTimeout = setTimeout(function(){
						TrainDemandSelectWin.grid.store.load();
	    			}, 1000);
	    		}
			}
	    },'->','<span style="color:grey;">' + i18n.PassengerInspectionPlan.doubleQuick + '&nbsp;&nbsp;</span>'],   	   
		fields: [{
			header:i18n.PassengerInspectionPlan.marshallingCode, dataIndex:'marshallingCode',hidden:true ,width: 40, searcher: { hidden: false }
		},{
			header:i18n.PassengerInspectionPlan.marshalling, dataIndex:'marshallingTrainCountStr', width: 80 ,editor: {allowBlank:false}
		},{
			header:i18n.PassengerInspectionPlan.trainCount, dataIndex:'trainCount', width: 30, editor: {xtype:"hidden", allowBlank:false, maxLength:20 },searcher: { hidden: true }
		},{
			header:i18n.PassengerInspectionPlan.routingCode, dataIndex:'routingCode',hidden:true , width: 60, 
			searcher: {xtype: 'textfield'}
		},{
			header:i18n.PassengerInspectionPlan.runningDate, dataIndex:'runningDate', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:i18n.PassengerInspectionPlan.trainNumber, dataIndex:'strips', width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var backStrips = record.get('backStrips');
				return value + (Ext.isEmpty(backStrips)? "": "/"+ backStrips);
			}
		},{
			header:i18n.PassengerInspectionPlan.backTrain, dataIndex:'backStrips',hidden:true , width: 40, editor:{ }
		},{
			header:i18n.PassengerInspectionPlan.station, dataIndex:'startingStation', width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var leaveOffStation =  record.get('leaveOffStation');
				return value + "-"+ leaveOffStation;
			}
		},{
			header:i18n.PassengerInspectionPlan.leaveForStation, dataIndex:'leaveOffStation', hidden:true , width: 40, editor:{  }
		},{
			header:i18n.PassengerInspectionPlan.departureTime, dataIndex:'departureTime',hidden:true , width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalTime =  record.get('arrivalTime');
				return value + "~"+ arrivalTime;
			},
			searcher:{disabled: true}
		},{
			header:i18n.PassengerInspectionPlan.arrivalTime, dataIndex:'arrivalTime' , hidden:true , width: 40, editor:{  }
		},{
			header:i18n.PassengerInspectionPlan.duration, dataIndex:'duration',hidden:true , width: 40, editor:{  }, 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
	
		},{
			header:i18n.PassengerInspectionPlan.trainInspectorName, dataIndex:'trainInspectorName',hidden:true , width: 50, editor:{  }, 
			searcher:{disabled: true}
		
		},{
			header:i18n.PassengerInspectionPlan.departureBackTime, dataIndex:'departureBackTime',hidden:true , width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalBackTime =  record.get('arrivalBackTime');
				return value + "~"+ arrivalBackTime;
			}, 
			searcher:{disabled: true}
		},{
			header:i18n.PassengerInspectionPlan.arrivalBackTime, dataIndex:'arrivalBackTime' ,hidden:true, width: 40, editor:{  }
		},{
			header:i18n.PassengerInspectionPlan.kilo, dataIndex:'kilometers', hidden:true , width: 50,editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:i18n.PassengerInspectionPlan.remark, dataIndex:'remark', hidden:true , width: 260, editor:{ xtype:'textarea', maxLength:1000 },
			searcher:{ disabled:true }
		},{
			header:i18n.PassengerInspectionPlan.routingIdx, dataIndex:'routingIdx',hidden:true ,searcher: { hidden: true }
		},{
			header:i18n.PassengerInspectionPlan.marshallingIdx, dataIndex:'marshallingIdx',hidden:true ,searcher: { hidden: true }
		},{
			header:i18n.PassengerInspectionPlan.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		},{
			header:i18n.PassengerInspectionPlan.trainInspectorID, dataIndex:'trainInspectorID', hidden:true,
			searcher:{disabled: true}
		}],
		beforeShowEditWin: function(record, rowIndex){  
			return false;
		}
	});
	//查询前添加过滤条件
	TrainDemandSelectWin.grid.store.on('beforeload' , function(){
		var whereList = [];
		var queryKey = Ext.getCmp('query_input').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (STRIPS LIKE '%" + queryKey + "%'"
					+ " OR BACK_STRIPS LIKE '%" + queryKey + "%') ";
		    whereList.push({ sql: sql, compare: Condition.SQL});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	
		//定义选择窗口
	TrainDemandSelectWin.selectWin = new Ext.Window({
		title:i18n.PassengerInspectionPlan.marshallingTask, width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:TrainDemandSelectWin.grid,modal:true,
    	buttons: [{
			text : i18n.PassengerInspectionPlan.save,iconCls : "saveIcon", handler: function(){
				TrainDemandSelectWin.submit(); 
			}
		},{
	        text: i18n.PassengerInspectionPlan.close, iconCls: "closeIcon", scope: this, handler: function(){ TrainDemandSelectWin.selectWin.hide(); }
		}]
	});
	
	TrainDemandSelectWin.grid.on("dblclick", function(grid, rowIndex, e){
		TrainDemandSelectWin.submit();
	});
	
	// 覆盖该方法
	TrainDemandSelectWin.submit = function(){
		
	}
});