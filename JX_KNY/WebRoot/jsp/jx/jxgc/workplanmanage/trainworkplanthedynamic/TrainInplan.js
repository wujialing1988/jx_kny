/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainInplan');                       // 定义命名空间
	TrainInplan.idx = '';
	TrainInplan.labelWidth = 110;
	TrainInplan.fieldWidth = 60;
	TrainInplan.searchParam = '';
	/*** 定义全局函数开始 ***/
	//保存前触发函数
	TrainInplan.beforeSaveFn = function(data){		
		var form = TrainWorkPlanTheDynamic.searchForm.getForm();		
        var planGenerateDate = new Date().format('Y-m-d');
        if(null != form) planGenerateDate = form.getValues().planGenerateDate;
		if(Ext.isEmpty(data.idx)) data.planGenerateDate = planGenerateDate;		
		return true;
	}

   // 定义表格
	TrainInplan.grid = new Ext.yunda.Grid({
		loadURL: ctx + "/trainInPlan!pageQuery.action",
		saveURL: ctx + "/trainInPlan!saveOrUpdate.action",
	    deleteURL: ctx + "/trainInPlan!logicDelete.action",                  //删除数据的请求URL
		storeAutoLoad: false,
	 	saveFormColNum:2,fieldWidth: 200,   
	    singleSelect: true,
		beforeSaveFn: TrainInplan.beforeSaveFn,  
		tbar: ['add','delete'],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true, editor: { id:"idx", xtype:'hidden' }
		}, {
			header: '车型', dataIndex: 'trainTypeShortName',  width: 40  ,
			editor: {
				id:"trainType_comb_t",	
					fieldLabel: "车型",
					hiddenName: "trainTypeShortName", 
					returnField: [{widgetId:"trainTypeIDX_Id_t",propertyName:"typeID"}],
					xtype: "Base_combo",
					business: 'trainType',
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					displayField: "shortName", valueField: "shortName",
					pageSize: 0, minListWidth: 200,
					editable:true,
					allowBlank: false , 
					"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = Ext.getCmp("trainNo_comb_t");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = Ext.getCmp("trainTypeIDX_Id_t").getValue();
		                trainNo_comb.cascadeStore();
	                } }
		}, {
			header: '车号', dataIndex: 'trainNo',width: 40,
			editor: {	
			id:"trainNo_comb_t",	
				fieldLabel: "车号",
				hiddenName: "trainNo", allowBlank: false, 
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
						var trainTypeIdx =  Ext.getCmp("trainTypeIDX_Id_t").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
			}
		}, {
			header: '修程', dataIndex: 'repairClassName', width: 80 
			, editor: { 
				id:"rc_comb_t",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassName", 
				returnField: [{widgetId:"repairClassIDX_Id_t",propertyName:"xcID"}],
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcName",
				pageSize: 0, minListWidth: 200,
				allowBlank: false
			}
		}, {
			header: '委修段', dataIndex: 'usedDID' , width: 80
			, editor: { 
			   id: "wxid_t", xtype: "DeportSelect2_comboTree",
				hiddenName: "usedDID", fieldLabel: "委修段",// allowBlank: false,
				returnField: [{widgetId:"wxname_t",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf" 
			  },
			renderer: function(value, mateData, record) {
				return record.get('usedDName');
			}
		}, {
			header: '到段计划', dataIndex: 'planStartDate', xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '预报不良状态', dataIndex: 'remarks', width: 200, editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: 'trainEnforcePlanIDX', dataIndex: 'trainEnforcePlanIDX', hidden: true,
			editor: {  xtype:'hidden'}
		}, {
			header: 'repairClassIDX',  dataIndex: 'repairClassIDX', hidden: true,
			editor: { id:"repairClassIDX_Id_t", xtype:'hidden'}
		}, {
			header: 'trainTypeIDX',dataIndex: 'trainTypeIDX', hidden: true,
			editor: { id:'trainTypeIDX_Id_t' , xtype:'hidden'}
		}, {
			header: 'planGenerateDate', dataIndex: 'planGenerateDate', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: 'saveStatus', dataIndex: 'saveStatus', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'usedDName', dataIndex: 'usedDName', hidden: true,
			editor: {  id: "wxname_t", xtype:'hidden' }
		}]
		,
	    searchFn: function(searchParam){
	    	TrainInplan.searchParam = searchParam;
	    	this.store.load();
	    },
	    afterShowSaveWin: function(record, rowIndex){
	    	Ext.getCmp('wxid_t').clearValue(); 
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('wxid_t').setDisplayValue(record.data.usedDID,record.data.usedDName);
	    },  
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        this.saveWin.hide();
	        alertSuccess();
	    }
	});
	TrainInplan.grid.store.on('beforeload', function() {
		var searchParam = TrainInplan.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "trainTypeShortName";
		this.baseParams.dir = "ASC";
	});
	
//	// 以下代码用于重置“委修段”字段
//	TrainInplan.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
//		var record = TrainInplan.grid.store.getAt(rowIndex);
//		Ext.getCmp('wxid_t').setDisplayValue(record.get('usedDID'),
//				record.get('usedDName'));
//	});
//	
//	TrainInplan.grid.rowEditor.on('afteredit', function(me, rowIndex) {
//			Ext.getCmp('trainType_comb_t').clearValue();
//			Ext.getCmp("rc_comb_t").clearValue();
//			Ext.getCmp('trainNo_comb_t').clearValue();
//			Ext.getCmp('wxid_t').clearValue();
//			Ext.getCmp('wxname_t').setValue('');
//		});
//	TrainInplan.grid.rowEditor.on('canceledit', function(me, rowIndex) {
//		Ext.getCmp('trainType_comb_t').clearValue();
//		Ext.getCmp("rc_comb_t").clearValue();
//		Ext.getCmp('trainNo_comb_t').clearValue();
//		Ext.getCmp('wxid_t').clearValue();
//		Ext.getCmp('wxname_t').setValue('');
//	});
	
});