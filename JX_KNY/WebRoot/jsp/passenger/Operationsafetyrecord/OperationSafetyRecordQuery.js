/**
 * 编组信息维护
 */
Ext.onReady(function(){
	Ext.namespace('OperationSafetyRecord');                       //定义命名空间
	
	//定义全局变量保存查询条件
	OperationSafetyRecord.searchParam = {} ;
	OperationSafetyRecord.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/operationSafetyRecord!pageList.action',                 //装载列表数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
	    isEdit:false,
		fields: [{
			header:i18n.OperationSafetyRecordQuery.planTrainNum, dataIndex:'strips',width: 60,editor: {
				fieldLabel: i18n.OperationSafetyRecordQuery.planTrainNum, id:"strips_comb", 
				allowBlank:false ,
				name: "strips",
				xtype: "Base_combo",
			    entity:'com.yunda.passenger.traindemand.entity.TrainDemand',
	            fields:['idx','strips','runningDate'],
			    displayField: "strips", valueField: "strips",
			    returnField:[{widgetId:"trainDemandID", propertyName:"idx"}, 
			    			 {widgetId:"runningDateID", propertyName:'runningDate'}], 
	            pageSize: 0, minListWidth: 150,
	            editable:true,
				listeners : { 
	            	"select" : function(value,record,index){
		            	//重新加载库位下拉数据
				         var runningDate= new Date(record.get('runningDate')).format('Y-m-d H:i');
				         Ext.getCmp("runningDateID").setValue(runningDate);
				         Ext.getCmp("runningDateID").enable();
	            	}
				}
            }
		},{
			header:i18n.OperationSafetyRecordQuery.content, dataIndex:'content',width: 120,editor:{ xtype:'textarea', maxLength:1000 }
		},{
			header:i18n.OperationSafetyRecordQuery.runningDate, dataIndex:'runningDate', xtype:'datecolumn', format: "Y-m-d H:i",width: 100,
			editor: {
				id:'runningDateID', name:'runningDate', xtype:"my97date", format: "Y-m-d H:i",
	        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, disabled:true,  initNow: false },searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.reportEmpName, dataIndex:'reportEmpName', width: 60 ,
			editor: {id: 'OmEmployee_MultSelectWin_Id',xtype: 'OmEmployee_MultSelectWin', fieldLabel: i18n.OperationSafetyRecordQuery.reportEmpName,
				 	hiddenName: 'reportEmpName', displayField:'empname', valueField: 'empname',
				 	returnField :[{widgetId: "reportEmpID", propertyName: "empid"}],
			  		editable: false, width: 50, allowBlank:false
		  	 }, searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.empName, dataIndex:'empName', width: 30, editor: {name:"empName",value:empName, xtype:"hidden" }, searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.reportEmpID, dataIndex:'reportEmpID',hidden:true ,editor: {id:'reportEmpID', name:'reportEmpID', xtype:"hidden" },searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.empID, dataIndex:'empID',hidden:true ,editor: { name:"empID", value:empID, xtype:"hidden" },searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.trainDemandIDX, dataIndex:'trainDemandIDX',hidden:true ,editor: { id:"trainDemandID", name:"trainDemandIDX", xtype:"hidden" },searcher: { hidden: true }
		},{
			header:i18n.OperationSafetyRecordQuery.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		}], 
		// 重新保存方法，完善对“duration（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var runningDate = data.runningDate;
			return true; 
		},
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp("OmEmployee_MultSelectWin_Id").setDisplayValue(record.get('reportEmpID'), record.get('reportEmpName'));
	    },
		searchFn: function(searchParam){ 
			OperationSafetyRecord.searchParam = searchParam ;
	        OperationSafetyRecord.grid.store.load();
		}
	});
	//查询前添加过滤条件
	OperationSafetyRecord.grid.store.on('beforeload' , function(){
		var searchParam = OperationSafetyRecord.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout:'fit',
		items:[{
			 layout: 'fit',  title: i18n.OperationSafetyRecordQuery.title,  bodyBorder: false, split: true,width: 600,minSize: 400, maxSize: 800, 
	     	 collapsible : true,   items:[OperationSafetyRecord.grid]
		}] 
	});


});