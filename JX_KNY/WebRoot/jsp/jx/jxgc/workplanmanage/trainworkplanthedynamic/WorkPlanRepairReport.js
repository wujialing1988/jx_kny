/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkPlanRepairReport');                       // 定义命名空间
	WorkPlanRepairReport.idx = '';
	WorkPlanRepairReport.labelWidth = 110;
	WorkPlanRepairReport.fieldWidth = 60;
	/*** 定义全局函数开始 ***/
	//保存前触发函数
	WorkPlanRepairReport.beforeSaveFn = function(data){		
		var form = TrainWorkPlanTheDynamic.searchForm.getForm();		
        var planGenerateDate = new Date().format('Y-m-d');
        if(null != form) planGenerateDate = form.getValues().planGenerateDate;
		if(Ext.isEmpty(data.idx)) data.planGenerateDate = planGenerateDate;		
		return true;
	}
	// 生成动态
    WorkPlanRepairReport.planGenerateFn = function(){
		 // Ajax请求
		Ext.Msg.confirm("提示  ", "确认生成修峻待待离段机车信息？  ", function(btn) {
			if ('yes' == btn) {
				Ext.Ajax.request({
					url: ctx + '/workPlanRepairReport!insertWorkPlanRepairReport.action',
					params:{planGenerateDate: WorkPlanRepairReport.planGenerateDate},
					//请求成功后的回调函数
				    success: function(response, options){
				    	 var result = Ext.util.JSON.decode(response.responseText);
				    	 if (result.errMsg == null && result.success == true) {
		               		alertSuccess();
							WorkPlanRepairReport.grid.store.reload();
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
	WorkPlanRepairReport.grid = new Ext.yunda.Grid({	
		loadURL: ctx + "/workPlanRepairReport!pageQuery.action",
		saveURL: ctx + "/workPlanRepairReport!saveOrUpdate.action",
	    deleteURL: ctx + "/workPlanRepairReport!logicDelete.action",                  //删除数据的请求URL
		storeAutoLoad: false,
		saveFormColNum:2,fieldWidth: 200,   
	    singleSelect: true,
		beforeSaveFn: WorkPlanRepairReport.beforeSaveFn,  
		tbar: [		'add','delete'	],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: '车型', dataIndex: 'trainTypeShortName',  width: 40  ,
			editor: {
				id:"trainType_comb_r",	
					fieldLabel: "车型",
					hiddenName: "trainTypeShortName", 
					returnField: [{widgetId:"trainTypeIDX_Id_r",propertyName:"typeID"}],
					xtype: "Base_combo",
					business: 'trainType',
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					displayField: "shortName", valueField: "shortName",
					pageSize: 0, minListWidth: 200,
					editable:true,
					allowBlank: false }
			}, {
			header: '修程', dataIndex: 'repairClassName', width: 40 
			, editor: { 
				id:"rc_comb_r",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassName", 
				returnField: [{widgetId:"repairClassIDX_Id_r",propertyName:"xcID"}],
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcName",
				pageSize: 0, minListWidth: 200,
				allowBlank: false
			}
		}, {
			header: '完成数量 ', dataIndex: 'count',  width: 40, editor:{ 	
	        		maxLength: 6, 
	        		vtype: "nonNegativeInt", 
	        		allowBlank: false
				}
		}, {
			header: '计划数量 ', dataIndex: 'yearPlanCount',  width: 40, editor:{ 	
	        		maxLength: 6, 
	        		vtype: "nonNegativeInt", 
	        		allowBlank: false
				}

		}, {
			header: 'trainTypeIDX',dataIndex: 'trainTypeIDX', hidden: true,
			editor: { id:'trainTypeIDX_Id_r', xtype:'hidden' }
		}, {
			header: 'repairClassIDX',  dataIndex: 'repairClassIDX', hidden: true,
			editor: { id:"repairClassIDX_Id_r", xtype:'hidden'}
		}, {
			header: 'planGenerateDate', dataIndex: 'planGenerateDate', hidden: true, editor: {  xtype:'hidden'}
		}, {
			header: 'saveStatus', dataIndex: 'saveStatus', hidden: true, editor: {  xtype:'hidden' }
		}],
	    searchFn: function(searchParam){
	    	WorkPlanRepairReport.searchParam = searchParam;
	    	this.store.load();
	    },
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        this.saveWin.hide();
	        alertSuccess();
	    }
	});
	WorkPlanRepairReport.grid.store.on('beforeload', function() {
		var searchParam = WorkPlanRepairReport.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "trainTypeShortName";
		this.baseParams.dir = "ASC";
	});
//	WorkPlanRepairReport.grid.rowEditor.on('afteredit', function(me, rowIndex) {
//			Ext.getCmp('trainType_comb_r').clearValue();
//			Ext.getCmp("rc_comb_r").clearValue();
//		});
//	WorkPlanRepairReport.grid.rowEditor.on('canceledit', function(me, rowIndex) {
//		Ext.getCmp('trainType_comb_r').clearValue();
//		Ext.getCmp("rc_comb_r").clearValue();
//	});
	
});