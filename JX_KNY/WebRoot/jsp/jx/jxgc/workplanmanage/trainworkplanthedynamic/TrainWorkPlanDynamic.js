/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanDynamic');                       // 定义命名空间
	TrainWorkPlanDynamic.idx = '';
	TrainWorkPlanDynamic.labelWidth = 110;
	TrainWorkPlanDynamic.fieldWidth = 60;
	TrainWorkPlanDynamic.searchParam ="";
	/*** 定义全局函数开始 ***/
	//保存前触发函数
	TrainWorkPlanDynamic.beforeSaveFn = function(data){		
		var form = TrainWorkPlanTheDynamic.searchForm.getForm();		
        var planGenerateDate = new Date().format('Y-m-d');
        if(null != form) planGenerateDate = form.getValues().planGenerateDate;
		if(Ext.isEmpty(data.idx)) data.planGenerateDate = planGenerateDate;		
		return true;
	}

   // 定义表格
	TrainWorkPlanDynamic.grid = new Ext.yunda.Grid({	
		loadURL: ctx + "/trainWorkPlanDynamic!pageQuery.action",
		saveURL: ctx + "/trainWorkPlanDynamic!saveOrUpdate.action",
	    deleteURL: ctx + "/trainWorkPlanDynamic!logicDelete.action",                  //删除数据的请求URL
		storeAutoLoad: false,
		saveFormColNum:2,fieldWidth: 200, 
		beforeSaveFn: TrainWorkPlanDynamic.beforeSaveFn,   
		tbar: [ 'add','delete'],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true, editor: {xtype:'hidden' }
		}, {
			header: '车型', dataIndex: 'trainTypeShortName',  width: 40  ,
			editor: {
				id:"trainType_comb",
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName", 
				returnField: [{widgetId:"trainTypeIDX_Id",propertyName:"typeID"}],
				xtype: "Base_combo",
				business: 'trainType',
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true,
				allowBlank: false, 
				"select" : function() {   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_comb");   
	                trainNo_comb.reset();  
	                trainNo_comb.clearValue(); 
	                trainNo_comb.queryParams.trainTypeIDX = Ext.getCmp("trainTypeIDX_Id").getValue();
	                trainNo_comb.cascadeStore();
                }
			}
		}, {
			header: '车号', dataIndex: 'trainNo',width: 40,
			editor: {	
			id:"trainNo_comb",	
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				minLength : 4, 
				maxLength : 5,
				forceSelection:false,    				
				xtype: "Base_combo",
				business: 'trainNoSelect',				
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
						{name:"leaveDate", type:"date", dateFormat: 'time'},
						"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse",
						"trainUseName","bId","dId","bName","dName","bShortName","dShortName"],	
				queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				returnField: [
		              {widgetId:"deportIDX", propertyName:"dId"},//配属段ID
		              {widgetId:"psNAME", propertyName:"dName"},//配属段名称 -
		              {widgetId:"wxname", propertyName:"dName"}
				],
				editable:true,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeIdx =  Ext.getCmp("trainTypeIDX_Id").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
			}
		}, {
			header: '修程', dataIndex: 'repairClassName', width: 40 
			, editor: { 
				id:"rc_comb",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassName", 
				returnField: [{widgetId:"repairClassIDX_Id",propertyName:"xcID"}],
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcName",
				pageSize: 0, minListWidth: 200,
				allowBlank: false
			}
		}, {
			header: '委修段', dataIndex: 'delegateDID' , width: 60
			, editor: { 
			   id: "wxid", xtype: "DeportSelect2_comboTree",
				hiddenName: "delegateDID", fieldLabel: "委修段",
				returnField: [{widgetId:"wxname",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf" 
			  },
			renderer: function(value, mateData, record) {
				return record.get('delegateDName');
			}
		}, {
			header: '入段日期', dataIndex: 'inTime', xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '上台日期', dataIndex: 'beginTime',	xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '当日计划', dataIndex: 'nodeNames', width: 160, editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: '当前位置', dataIndex: 'workStationName', width: 160, editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: '明日计划', dataIndex: 'tomorrowNodeNames',width: 160,  editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: '计划交车', dataIndex: 'planEndTime', xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '备注 ', dataIndex: 'remarks', width: 120,  editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: 'workPlanIDX', dataIndex: 'workPlanIDX', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'repairClassIDX',  dataIndex: 'repairClassIDX', hidden: true,
			editor: { id:"repairClassIDX_Id" , xtype:'hidden' }
		}, {
			header: 'trainTypeIDX',dataIndex: 'trainTypeIDX', hidden: true,
			editor: { id:'trainTypeIDX_Id', xtype:'hidden'  }
		}, {
			header: 'planGenerateDate', dataIndex: 'planGenerateDate', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'saveStatus', dataIndex: 'saveStatus', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'delegateDName', dataIndex: 'delegateDName', hidden: true,
			editor: {  id: "wxname" , xtype:'hidden' }
		}],
	    searchFn: function(searchParam){
	    	TrainWorkPlanDynamic.searchParam = searchParam;
	    	this.store.load();
	    },
	    afterShowSaveWin: function(record, rowIndex){
	    	Ext.getCmp('wxid').clearValue(); 
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('wxid').setDisplayValue(record.data.delegateDID,record.data.delegateDName);
	    },  
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        this.saveWin.hide();
	        alertSuccess();
	    }
	});
	TrainWorkPlanDynamic.grid.store.on('beforeload', function() {
		var searchParam = TrainWorkPlanDynamic.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "trainTypeShortName";
		this.baseParams.dir = "ASC";
	});

	
//	// 以下代码用于重置“委修段”字段
//	TrainWorkPlanDynamic.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
//		var record = TrainWorkPlanDynamic.grid.store.getAt(rowIndex);
//		Ext.getCmp('wxid').setDisplayValue(record.get('delegateDID'),
//				record.get('delegateDName'));
//	});
//	
//	TrainWorkPlanDynamic.grid.rowEditor.on('afteredit', function(me, rowIndex) {
//		Ext.getCmp('trainType_comb').clearValue();
//		Ext.getCmp("rc_comb").clearValue();
//		Ext.getCmp('trainNo_comb').clearValue();
//		Ext.getCmp('wxid').clearValue();
//		Ext.getCmp('wxname').setValue('');
//	});
//	TrainWorkPlanDynamic.grid.rowEditor.on('canceledit', function(me, rowIndex) {
//		Ext.getCmp('trainType_comb').clearValue();
//		Ext.getCmp("rc_comb").clearValue();
//		Ext.getCmp('trainNo_comb').clearValue();
//		Ext.getCmp('wxid').clearValue();
//		Ext.getCmp('wxname').setValue('');
//	});
});