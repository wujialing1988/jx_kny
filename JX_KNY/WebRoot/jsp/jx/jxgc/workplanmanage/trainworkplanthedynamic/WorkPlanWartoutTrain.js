/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkPlanWartoutTrain');                       // 定义命名空间
	WorkPlanWartoutTrain.idx = '';
	WorkPlanWartoutTrain.labelWidth = 110;
	WorkPlanWartoutTrain.fieldWidth = 60;
	/*** 定义全局函数开始 ***/
	//保存前触发函数
	WorkPlanWartoutTrain.beforeSaveFn = function(data){		
		var form = TrainWorkPlanTheDynamic.searchForm.getForm();		
        var planGenerateDate = new Date().format('Y-m-d');
        if(null != form) planGenerateDate = form.getValues().planGenerateDate;
		if(Ext.isEmpty(data.idx)) data.planGenerateDate = planGenerateDate;		
		return true;
	}
	// 生成动态
    WorkPlanWartoutTrain.planGenerateFn = function(){
		 // Ajax请求
		Ext.Msg.confirm("提示  ", "确认生成今日修峻待离段机车信息？  ", function(btn) {
			if ('yes' == btn) {
				Ext.Ajax.request({
					url: ctx + '/workPlanWartoutTrain!insertTheWartoutTrain.action',
					params:{planGenerateDate: WorkPlanWartoutTrain.planGenerateDate},
					//请求成功后的回调函数
				    success: function(response, options){
				    	 var result = Ext.util.JSON.decode(response.responseText);
				    	 if (result.errMsg == null && result.success == true) {
		               		alertSuccess();
							WorkPlanWartoutTrain.grid.store.reload();
			            } else {
			                alertFail(result.errMsg);
			            }
				        
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		});
	}
   // 定义表格
	WorkPlanWartoutTrain.grid = new Ext.yunda.Grid({	
		loadURL: ctx + "/workPlanWartoutTrain!pageQuery.action",
		saveURL: ctx + "/workPlanWartoutTrain!saveOrUpdate.action",
	    deleteURL: ctx + "/workPlanWartoutTrain!logicDelete.action",                  //删除数据的请求URL
		storeAutoLoad: false,
		saveFormColNum:2,fieldWidth: 200,   
		beforeSaveFn: WorkPlanWartoutTrain.beforeSaveFn,  
		tbar: [
//			{
//			text: "更新",
//			iconCls: "editIcon",
//			handler: function() {
//				WorkPlanWartoutTrain.planGenerateFn(); }
//		},
		'add','delete'
		
		],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: '车型', dataIndex: 'trainTypeShortName',  width: 40  ,
			editor: {
				id:"trainType_comb_w",	
					fieldLabel: "车型",
					hiddenName: "trainTypeShortName", 
					returnField: [{widgetId:"trainTypeIDX_Id_w",propertyName:"typeID"}],
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
		                var trainNo_comb = Ext.getCmp("trainNo_comb_w");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = Ext.getCmp("trainTypeIDX_Id_w").getValue();
		                trainNo_comb.cascadeStore();
	                } }
		}, {
			header: '车号', dataIndex: 'trainNo',width: 40,
			editor: {	
			id:"trainNo_comb_w",	
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
						var trainTypeIdx =  Ext.getCmp("trainTypeIDX_Id_w").getValue();
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
				id:"rc_comb_w",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassName", 
				returnField: [{widgetId:"repairClassIDX_Id_w",propertyName:"xcID"}],
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
			   id: "wxid_w", xtype: "DeportSelect2_comboTree",
				name: "delegateDID", fieldLabel: "委修段",
				returnField: [{widgetId:"wxname_w",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf" 
			  },
			renderer: function(value, mateData, record) {
				return record.get('delegateDName');
			}
		}, {
			header: '入段日期', dataIndex: 'inTime', xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '修峻日期', dataIndex: 'endTime',	xtype:'datecolumn', format: "Y-m-d",  width: 80
		}, {
			header: '备注 ', dataIndex: 'remarks', width: 110, editor: {  xtype:'textarea', maxLength:500 }
		}, {
			header: 'workPlanIDX', dataIndex: 'workPlanIDX', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: 'repairClassIDX',  dataIndex: 'repairClassIDX', hidden: true,
			editor: { id:"repairClassIDX_Id_w", xtype:'hidden'}
		}, {
			header: 'trainTypeIDX',dataIndex: 'trainTypeIDX', hidden: true,
			editor: { id:'trainTypeIDX_Id_w' , xtype:'hidden'}
		}, {
			header: 'planGenerateDate', dataIndex: 'planGenerateDate', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: 'saveStatus', dataIndex: 'saveStatus', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'delegateDName', dataIndex: 'delegateDName', hidden: true,
			editor: {  id: "wxname_w" , xtype:'hidden'}
		}],
	    searchFn: function(searchParam){
	    	WorkPlanWartoutTrain.searchParam = searchParam;
	    	this.store.load();
	    },
	    afterShowSaveWin: function(record, rowIndex){
	    	Ext.getCmp('wxid_w').clearValue(); 
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('wxid_w').setDisplayValue(record.data.delegateDID,record.data.delegateDName);
	    },  
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        this.saveWin.hide();
	        alertSuccess();
	    }
	});
	WorkPlanWartoutTrain.grid.store.on('beforeload', function() {
		var searchParam = WorkPlanWartoutTrain.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "trainTypeShortName";
		this.baseParams.dir = "ASC";
	});
	
//
//	// 以下代码用于重置“委修段”字段
//	WorkPlanWartoutTrain.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
//		var record = WorkPlanWartoutTrain.grid.store.getAt(rowIndex);
//		Ext.getCmp('wxid_w').setDisplayValue(record.get('delegateDID'),
//				record.get('delegateDName'));
//	});
//	WorkPlanWartoutTrain.grid.rowEditor.on('afteredit', function(me, rowIndex) {
//			Ext.getCmp('trainType_comb_w').clearValue();
//			Ext.getCmp("rc_comb_w").clearValue();
//			Ext.getCmp('trainNo_comb_w').clearValue();
//			Ext.getCmp('wxid_w').clearValue();
//			Ext.getCmp('wxname_w').setValue('');
//		});
//	WorkPlanWartoutTrain.grid.rowEditor.on('canceledit', function(me, rowIndex) {
//		Ext.getCmp('trainType_comb_w').clearValue();
//		Ext.getCmp("rc_comb_w").clearValue();
//		Ext.getCmp('trainNo_comb_w').clearValue();
//		Ext.getCmp('wxid_w').clearValue();
//		Ext.getCmp('wxname_w').setValue('');
//	});
	
	
});