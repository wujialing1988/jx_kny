/**
 * 编组需求计划维护
 */
Ext.onReady(function(){
		
	Ext.namespace('TrainDemand');                       //定义命名空间
	
	//定义全局变量保存查询条件
	TrainDemand.searchParam = {} ;
	TrainDemand.isSaveAndAdd = false;
    TrainDemand.routingIdx = '';
	TrainDemand.runningDate = '';
	TrainDemand.routingReturnFn = function(grid, rowIndex, e) {
		var record = grid.store.getAt(rowIndex);
		var form = TrainDemand.saveForm;
		form.find("name", "routingIdx")[0].setValue(record.get("idx"));
		form.find("name", "routingCode")[0].setValue(record.get("routingCode"));
		form.find("name", "startingStation")[0].setValue(record.get("startingStation"));
		form.find("name", "leaveOffStation")[0].setValue(record.get("leaveOffStation"));
		form.find("name", "kilometers")[0].setValue(record.get("kilometers"));
		form.find("name", "duration")[0].setValue(record.get("duration"));
		form.find("name", "departureTime")[0].setValue(record.get("departureTime"));
		form.find("name", "arrivalTime")[0].setValue(record.get("arrivalTime"));
		form.find("name", "departureBackTime")[0].setValue(record.get("departureBackTime"));
		form.find("name", "arrivalBackTime")[0].setValue(record.get("arrivalBackTime"));
		var runningDate =  Ext.getCmp("runningDate_id").getValue();   
		if(record.get("idx")!= TrainDemand.routingIdx || new Date(TrainDemand.runningDate).getTime() !=  new Date(runningDate).getTime()){
		    TrainInspectorMultSelect.routingIdx = record.get("idx");
			TrainInspectorMultSelect.runningDate = runningDate;
		    TrainDemand.routingIdx = record.get("idx");
			TrainDemand.runningDate = runningDate;
			//重新加载库位下拉数据
	        var marshallingComb = Ext.getCmp("marshalling_comb");   
	        marshallingComb.reset();  
	        marshallingComb.clearValue(); 
	        marshallingComb.queryParams = {"routingIdx": record.get("idx"),"runningDate": runningDate};
	        marshallingComb.cascadeStore();
	        Ext.getCmp("TrainInspector_MultSelectWin_Id").setDisplayValue('', '');
		}
	}
    /** ************** 定义节点编辑表单开始 ************** */
	TrainDemand.saveForm = new Ext.form.FormPanel({
		padding: "10px", frame: true, labelWidth: TrainDemand.labelWidth,
		layout:"column",
		defaults: {
			layout:"form",
			columnWidth:0.5
		},
		items:[{
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				xtype:"routingDefSelect",fieldLabel: '交路', id:"routingCode_id",	
				name: 'routingCode', editable:false, allowBlank: false,
				returnFn: TrainDemand.routingReturnFn
        	}, {
				name:"strips", fieldLabel:"上行车次"
        	}, {
				fieldLabel: "编组", id:"marshalling_comb", 
				allowBlank:false ,
				name: "marshallingTrainCountStr",
				xtype: "Base_combo",
			    business: 'marshalling',
			    entity:'com.yunda.passenger.marshalling.entity.marshalling',
	            fields:['idx','marshallingCode','marshallingTrainCountStr'],
			    displayField: "marshallingTrainCountStr", valueField: "idx",
			    returnField: [{widgetId:"marshallingCodeID",propertyName:"marshallingCode"},
			    			{widgetId:"marshallingIdx",propertyName:"idx"}],
	            pageSize: 0, minListWidth: 150,
	            editable:true,
				listeners : { 
	            	"beforequery" : function(){
	            		var routingIdx=  Ext.getCmp("routingIdx").getValue();          		
	            		var runningDate=  Ext.getCmp("runningDate_id").getValue();          		
						if(routingIdx == "" || routingIdx == null){
							MyExt.Msg.alert("请先选择交路！");
							return false;
						}
						
						if(runningDate == "" || runningDate == null){
							MyExt.Msg.alert("请先出车日期！");
							return false;
						}
	            	}	            
	            }
		
			}]
		}, {
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				fieldLabel: '发车日期', xtype:"my97date", name: 'runningDate',id:'runningDate_id',  width: 80, format: "Y-m-d",
	        	my97cfg: {dateFmt:"yyyy-MM-dd"},
				listeners : { 
	            	"blur" : function(){
		            	//重新加载库位下拉数据
				        var routingIdx= Ext.getCmp("routingIdx").getValue(); 
				        TrainInspectorMultSelect.routingIdx = routingIdx;
						TrainInspectorMultSelect.runningDate = this.getValue();
						if(new Date(TrainDemand.runningDate).getTime() !=  new Date(this.getValue()).getTime()){
					        TrainDemand.routingIdx = routingIdx;
							TrainDemand.runningDate = this.getValue();
			            	//重新加载库位下拉数据
					        var marshallingComb = Ext.getCmp("marshalling_comb");   
					        marshallingComb.reset();  
					        marshallingComb.clearValue(); 
					        marshallingComb.queryParams = {"routingIdx": routingIdx, "runningDate": this.getValue()};
					        marshallingComb.cascadeStore();	
					        Ext.getCmp("TrainInspector_MultSelectWin_Id").setDisplayValue('', '');
						}
	            	}
				}
			}, {
				name:"backStrips", fieldLabel:"下行车次"
			}, {
				id: 'TrainInspector_MultSelectWin_Id',xtype: 'TrainInspector_MultSelectWin', fieldLabel: '乘务检测员',
			  	hiddenName: 'trainInspectorName', displayField:'empname', valueField: 'empname',
			 	returnField :[{widgetId: "empid", propertyName: "empid"}],
			  	editable: false, width: 50,
				listeners : { 
	            	"beforequery" : function(){
	            		var routingIdx =  Ext.getCmp("routingIdx").getValue();          		
	            		var runningDate =  Ext.getCmp("runningDate_id").getValue();          		
						if(routingIdx == "" || routingIdx == null){
							MyExt.Msg.alert("请先选择交路！");
							return false;
						}
						if(runningDate == "" || runningDate == null){
							MyExt.Msg.alert("请先出车日期！");
							return false;
						}
	            	}	            
	            }
		
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea", name:"remark", fieldLabel:"备注", maxLength:500, anchor:"97%", height: 55
			}]
		}, {
			columnWidth:0.5,defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				fieldLabel: '出发时间', xtype:"my97date", name: 'departureTime',width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"}, initNow: false	, readOnly:true	
			}, {
				name:"startingStation", fieldLabel:"出发地", readOnly:true
			}, {
				fieldLabel: '返程发车时间', xtype:"my97date", name: 'departureBackTime', readOnly:true	,  width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"},initNow: false
    	  	}, {
				name:"kilometers", xtype: 'numberfield', fieldLabel:"行程（KM）", readOnly:true
			}]
    	},{
    		columnWidth:0.5,defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
    		items:[{
				fieldLabel: '到达时间', xtype:"my97date", allowBlank: false, name: 'arrivalTime', width: 80, format: "H:i",
			        	my97cfg: {dateFmt:"HH:mm"}	, readOnly:true	, initNow: false
			}, {
				name:"leaveOffStation", fieldLabel:"前往地", readOnly:true
			}, {
				fieldLabel: '返程到达时间', xtype:"my97date", name: 'arrivalBackTime', width: 80, format: "H:i", readOnly:true	, 
	        	my97cfg: {dateFmt:"HH:mm"}	
			}]
		}, {
			// 【作业节点】保存表单的隐藏字段
			columnWidth:1,
			defaultType:'hidden',
			items:[
				{ fieldLabel:"idx主键", name:"idx" },
				{ name: "routingIdx",id:"routingIdx"},
				{ name: "trainInspectorID", id: "empid"},
				{ name: "duration"},
				{ name: "empID",value:empId},
				{ name: "empName",value:empName },
				{ fieldLabel:"marshallingCode", id:"marshallingCodeID", name:"marshallingCode" },
				{ fieldLabel:"", id:"marshallingIdx", name:"marshallingIdx" }
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				TrainDemand.isSaveAndAdd = false;
				TrainDemand.grid.saveFn();
			}
		}, {
			text: '保存并新增', iconCls: 'addIcon', handler: function() {
				TrainDemand.isSaveAndAdd = true;
				TrainDemand.grid.saveFn();
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	// 定义表格开始
	TrainDemand.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainDemand!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/trainDemand!saveOrUpdateDemand.action',             //保存数据的请求URL
	    deleteURL: ctx + '/trainDemand!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    saveForm: TrainDemand.saveForm,
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
		fields: [{
			header:'编组编号', dataIndex:'marshallingCode',hidden:true ,width: 40, searcher: { hidden: false }
		},{
			header:'编组', dataIndex:'marshallingTrainCountStr', width: 180 ,editor: {allowBlank:false}
		},{
			header:'车辆数', dataIndex:'trainCount', width: 40, editor: {xtype:"hidden", allowBlank:false, maxLength:20 },searcher: { hidden: true }
		},{
			header:'交路', dataIndex:'routingCode', width: 80, 
			searcher: {xtype: 'textfield'}
		},{
			header:'发车时间', dataIndex:'runningDate', xtype:'datecolumn', format: "Y-m-d H:i",width: 75
		},{
			header:'车次', dataIndex:'strips', width: 70, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var backStrips = record.get('backStrips');
				return value + (Ext.isEmpty(backStrips)? "": "/"+ backStrips);
			}
		},{
			header:'返程车次', dataIndex:'backStrips',hidden:true , width: 40, editor:{ }
		},{
			header:'地点', dataIndex:'startingStation', width: 70, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var leaveOffStation =  record.get('leaveOffStation');
				return value + "-"+ leaveOffStation;
			}
		},{
			header:'前往地', dataIndex:'leaveOffStation', hidden:true , width: 40, editor:{  }
		},{
			header:'出发时间', dataIndex:'departureTime', width: 75, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalTime =  record.get('arrivalTime');
				return value + "~"+ arrivalTime;
			},
			searcher:{disabled: true}
		},{
			header:'到达时间', dataIndex:'arrivalTime' , hidden:true , width: 75, editor:{  }
		},{
			header:'历时', dataIndex:'duration', width: 50, editor:{  }, 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
	
		},{
			header:'乘务检查员', dataIndex:'trainInspectorName', width: 80, editor:{  }, 
			searcher:{disabled: true}
		
		},{
			header:'返程出发时间', dataIndex:'departureBackTime', width: 75, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				var arrivalBackTime =  record.get('arrivalBackTime');
				return value + "~"+ arrivalBackTime;
			}, 
			searcher:{disabled: true}
		},{
			header:'返程到达时间', dataIndex:'arrivalBackTime' ,hidden:true, width: 40, editor:{  }
		},{
			header:'走行（KM）', dataIndex:'kilometers',  width: 60,editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:'备注', dataIndex:'remark',  width: 50,hidden:true, editor:{ xtype:'textarea', maxLength:1000 },
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
		// 保存成功后的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
            // 回显字段值
            var entity = result.entity;
	    	if (TrainDemand.isSaveAndAdd) {
			    this.saveForm.getForm().reset();
		    	this.afterShowSaveWin();
	    	} else {
	            this.saveForm.find('name', 'idx')[0].setValue(entity.idx);
	    	}
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp("TrainInspector_MultSelectWin_Id").setDisplayValue(record.get('trainInspectorID'), record.get('trainInspectorName'));
//	    	var duration = record.get('duration');
	    },
	    // 重新保存方法，完善对“duration（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var runningDate = data.runningDate;
			var departureTime = data.departureTime;
			data.runningDate = runningDate + ' '+departureTime;
//			delete data.duration_m;
			return true; 
		},
	    createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
				width: 650, height: 400,
				closeAction: 'hide',
				layout: 'fit',
				modal: false,
				items: [TrainDemand.saveForm]
	       	 });
	    },
		searchFn: function(searchParam){ 
			TrainDemand.searchParam = searchParam ;
	        TrainDemand.grid.store.load();
		},
		afterDeleteFn: function(){ 
			// MarshallingTrainDemand.grid.store.load();
		}
	});
	//查询前添加过滤条件
	TrainDemand.grid.store.on('beforeload' , function(){
		var searchParam = TrainDemand.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	// 添加加载结束事件
	TrainDemand.grid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			TrainDemand.grid.getSelectionModel().selectFirstRow();
			var records = TrainDemand.grid.getSelectionModel().getSelections();
			MarshallingTrainDemand.trainDemandIdx = records[0].data.idx ;
			MarshallingTrainDemand.grid.store.load();
		}else{
			MarshallingTrainDemand.trainDemandIdx = '###';
			MarshallingTrainDemand.grid.store.load();
		}
	});
	
	// 单击事件
	TrainDemand.grid.on("rowclick", function(grid, rowIndex, e){
		var records = TrainDemand.grid.getSelectionModel().getSelections();
		MarshallingTrainDemand.trainDemandIdx = records[0].data.idx ;
		MarshallingTrainDemand.grid.store.load();
	});	
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout:'fit',
		items:[{
			layout: 'border',
			items: [{
				 region: 'center', layout: 'fit',  title: '编组需求',  bodyBorder: false, split: true,
		     	 items:[TrainDemand.grid]
	    	},{ 		
		     	 region: 'east', bodyBorder: false,title: '编组车辆查看', width: 400, minSize: 400, maxSize: 800,  collapsible : true,  
	       		 layout: 'fit', items : [MarshallingTrainDemand.grid]
			}]
		}] 
	});


});
