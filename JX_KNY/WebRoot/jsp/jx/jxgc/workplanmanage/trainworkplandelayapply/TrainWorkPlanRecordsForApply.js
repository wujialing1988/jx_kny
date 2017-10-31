/** 机车主要部件定义维护*/
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanRecords');                       //定义命名空间
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanRecords.labelWidth = 55;

//	TrainWorkPlanRecords.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义函数开始 **************** */
	TrainWorkPlanRecords.setInfoForm = function(){
		var form = TrainWorkPlanRecords.infoForm.getForm();		
		var sm = TrainWorkPlanRecords.trainTypeGrid.getSelectionModel();
		if (sm.getCount() > 0) {			
			var records = sm.getSelections();
			FirstNodeCaseApplyConfirm.workPlanIDX = records[0].data.idx;
		 	FirstNodeCaseApplyConfirm.processIDX = records[0].data.processIDX;
		 	FirstNodeCaseApplyConfirm.workPlanStatus = records[0].data.workPlanStatus;
			form.reset();
			form.findField("trainTypeAndNo").setValue(records[0].data.trainTypeShortName + "|" + records[0].data.trainNo);
			var xcxc = records[0].data.repairClassName;
			if (!Ext.isEmpty(records[0].data.repairtimeName))
				xcxc+= "|" + records[0].data.repairtimeName;
			form.findField("xcxc").setValue(xcxc);
			form.findField("dNAME").setValue(records[0].data.dNAME);
			form.findField("delegateDName").setValue(records[0].data.delegateDName);
			if(records[0].data.workPlanStatus == PLAN_STATUS_NEW){
				form.findField("workPlanStatus").setValue("未启动");
			}else if(records[0].data.workPlanStatus == PLAN_STATUS_HANDLING){
				form.findField("workPlanStatus").setValue("在修");
			}
			
			// 计划时间
			var planBeginTime1 = new Date(records[0].data.planBeginTime).format('Y-m-d');
			var planEndTime1 = new Date(records[0].data.planEndTime).format('Y-m-d');
			// 实际时间
			var beginTime  = records[0].data.beginTime;
			var beginTime1 = '';
			if(!Ext.isEmpty(beginTime)){
				beginTime1 = beginTime.format('Y-m-d');
			}
			var endTime  = records[0].data.endTime;
			if(!Ext.isEmpty(beginTime)){
				beginTime1 = beginTime1 + (Ext.isEmpty(endTime)? " ":("~ " + endTime.format('Y-m-d')));
			}
			form.findField("planTime").setValue(planBeginTime1 + "~ " + planEndTime1);
			form.findField("realTime").setValue(beginTime1);
		}		
	}
	/** **************** 定义函数开始 **************** */
	
	// 车型列表
	TrainWorkPlanRecords.trainTypeGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/trainWorkPlanEditNew!queryTrainWorkPlan.action",
		singleSelect: true, 
		tbar:[{	
			xtype:'textfield', id:'train_type_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索', listeners: {
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
//					if (e.getKey() == e.ENTER){
						TrainWorkPlanRecords.trainTypeGrid.store.load();
//					}
	    		}
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
				header:'修次', dataIndex:'repairtimeName', hidden:true,editor:{  maxLength:50 }, searcher: {anchor:'98%'}
			},{
				header:'备注', dataIndex:'remarks', hidden:true,editor:{ xtype:'textarea', maxLength:1000 }
			},{
				header:'日历', dataIndex:'workCalendarIDX',hidden:true, editor:{  maxLength:50 }
			},{
				header:'配属段ID', dataIndex:'dID', hidden:true,editor:{  maxLength:20 }
			},{
				header:'配属段', dataIndex:'dNAME',hidden:true, editor:{  maxLength:50 }
			},{
				header:'委托维修段ID', dataIndex:'delegateDID', hidden:true,editor:{  maxLength:20 }
			},{
				header:'委修段', dataIndex:'delegateDName',hidden:true,editor:{  maxLength:50 }
			},{
				header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
			},{
				header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true,editor:{ xtype:'my97date' }
			},{
				header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn', hidden:true,editor:{ xtype:'my97date' }
			},{
				header:'修峻时间', dataIndex:'endTime', xtype:'datecolumn',  hidden:true,editor:{ xtype:'my97date' }
			},{
				header:'状态', dataIndex:'workPlanStatus', hidden:true
			},{
				header:'流程idx', dataIndex:'processIDX', hidden:true
			}]
	});
	
	// 数据加载前
	TrainWorkPlanRecords.trainTypeGrid.store.on('beforeload', function() {
		var searchParams = {};
		if(!Ext.isEmpty(Ext.getCmp('train_type_no'))){
			searchParams.trainNo = Ext.getCmp('train_type_no').getValue();
		}
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 添加加载结束事件
	TrainWorkPlanRecords.trainTypeGrid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			TrainWorkPlanRecords.trainTypeGrid.getSelectionModel().selectFirstRow();
			TrainWorkPlanRecords.setInfoForm();
			FirstNodeCaseApplyConfirm.grid.store.reload();			
		}
	});	
	
	//单击车型记录过滤机车和虚拟组成列表
	TrainWorkPlanRecords.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
       	TrainWorkPlanRecords.setInfoForm();
		FirstNodeCaseApplyConfirm.grid.store.reload();
	});
	//移除侦听器
	TrainWorkPlanRecords.trainTypeGrid.un('rowdblclick', TrainWorkPlanRecords.trainTypeGrid.toEditFn, TrainWorkPlanRecords.trainTypeGrid);
	
	TrainWorkPlanRecords.infoForm = new Ext.form.FormPanel({
			labelWidth:TrainWorkPlanRecords.labelWidth, border: false,
			labelAlign:"left", layout:"column",
			bodyStyle:"padding:10px;",
			defaults:{
				xtype:"container", autoEl:"div", layout:"form", columnWidth:1, 
				defaults:{
					style: 'border:none; background:none;', 
					xtype:"textfield", readOnly: true,
					anchor:"100%"
				}
			},
			items:[{
		        items: [
		        	{ fieldLabel:"车型车号", name:"trainTypeAndNo"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"修程修次", name:"xcxc"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"委修段", name:"delegateDName"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"配属段", name:"dNAME"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"计划时间", name:"planTime"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"实际时间", name:"realTime"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"检修状态", name:"workPlanStatus"}
		        ]
			}]
		});
		
	//机车详情页面
	TrainWorkPlanRecords.TrainRecordPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
//	        collapsible : true, minSize: 160, maxSize: 400, split: true, autoScroll: true,
	        layout: 'fit', items : [ TrainWorkPlanRecords.trainTypeGrid ]
	    }, {
	         region : 'south',   height: 200,  layout: 'fit', bodyBorder: false, items: [TrainWorkPlanRecords.infoForm]
	    }]
	});
	
	
	//页面适应布局
	TrainWorkPlanRecords.viewport = new Ext.Viewport({
		layout:'fit', 
		items:[{
			layout: 'border',
			items: [{ 	region: 'center', border: false, layout: 'fit', 
				 items:FirstNodeCaseApplyConfirm.grid
			},{ 		
		     	 region: 'west',  title: '机车检修计划',  bodyBorder: false, split: true, width: 240, layout: 'fit',   minSize: 160, maxSize: 400, 
		     	 collapsible : true,   items:[TrainWorkPlanRecords.TrainRecordPanel]
			}]
		}]
	});

});