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
	    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:'输入车次快速检索...', width:200,
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
	    },'->','<span style="color:grey;">双击快速选择数据！&nbsp;&nbsp;</span>'],   	   
		fields: [{
			header:'编组编号', dataIndex:'marshallingCode',hidden:true ,width: 40, searcher: { hidden: false }
		},{
			header:'编组', dataIndex:'marshallingTrainCountStr', width: 80 ,editor: {allowBlank:false}
		},{
			header:'车辆总数', dataIndex:'trainCount', width: 30, editor: {xtype:"hidden", allowBlank:false, maxLength:20 },searcher: { hidden: true }
		},{
			header:'交路编号', dataIndex:'routingCode',hidden:true , width: 60, 
			searcher: {xtype: 'textfield'}
		},{
			header:'发车时间', dataIndex:'runningDate', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:'车次', dataIndex:'strips', width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var backStrips = record.get('backStrips');
				return value + (Ext.isEmpty(backStrips)? "": "/"+ backStrips);
			}
		},{
			header:'返程车次', dataIndex:'backStrips',hidden:true , width: 40, editor:{ }
		},{
			header:'地点', dataIndex:'startingStation', width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var leaveOffStation =  record.get('leaveOffStation');
				return value + "-"+ leaveOffStation;
			}
		},{
			header:'前往地', dataIndex:'leaveOffStation', hidden:true , width: 40, editor:{  }
		},{
			header:'出发时间', dataIndex:'departureTime',hidden:true , width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalTime =  record.get('arrivalTime');
				return value + "~"+ arrivalTime;
			},
			searcher:{disabled: true}
		},{
			header:'到达时间', dataIndex:'arrivalTime' , hidden:true , width: 40, editor:{  }
		},{
			header:'历时', dataIndex:'duration',hidden:true , width: 40, editor:{  }, 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
	
		},{
			header:'乘务检测员', dataIndex:'trainInspectorName',hidden:true , width: 50, editor:{  }, 
			searcher:{disabled: true}
		
		},{
			header:'返程出发时间', dataIndex:'departureBackTime',hidden:true , width: 60, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalBackTime =  record.get('arrivalBackTime');
				return value + "~"+ arrivalBackTime;
			}, 
			searcher:{disabled: true}
		},{
			header:'返程到达时间', dataIndex:'arrivalBackTime' ,hidden:true, width: 40, editor:{  }
		},{
			header:'走行（KM）', dataIndex:'kilometers', hidden:true , width: 50,editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:'备注', dataIndex:'remark', hidden:true , width: 260, editor:{ xtype:'textarea', maxLength:1000 },
			searcher:{ disabled:true }
		},{
			header:'交路维护主键ID', dataIndex:'routingIdx',hidden:true ,searcher: { hidden: true }
		},{
			header:'编组维护主键ID', dataIndex:'marshallingIdx',hidden:true ,searcher: { hidden: true }
		},{
			header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		},{
			header:'乘务检测员id ', dataIndex:'trainInspectorID', hidden:true,
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
		title:"编组任务选择", width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:TrainDemandSelectWin.grid,modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				TrainDemandSelectWin.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ TrainDemandSelectWin.selectWin.hide(); }
		}]
	});
	
	TrainDemandSelectWin.grid.on("dblclick", function(grid, rowIndex, e){
		TrainDemandSelectWin.submit();
	});
	
	// 覆盖该方法
	TrainDemandSelectWin.submit = function(){
		
	}
});