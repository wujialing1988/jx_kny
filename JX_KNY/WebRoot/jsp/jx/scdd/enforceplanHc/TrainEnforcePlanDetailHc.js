/**
 * 货车检修计划详细
 */
Ext.onReady(function(){
	
Ext.namespace('TrainEnforcePlanDetailHc');                       // 定义命名空间

TrainEnforcePlanDetailHc.planIdx = '###' ;						 // 货车检修计划

//定义全局变量保存查询条件
TrainEnforcePlanDetailHc.searchParam = {} ;
TrainEnforcePlanDetailHc.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetailHc!findTrainEnforcePlanDetailHcList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetailHc!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetailHc!logicDelete.action',            //删除数据的请求URL
    singleSelect: false, saveFormColNum:1,
	tbar:['add','delete'],
	fields: [{
		header:'计划ID', dataIndex:'planIdx', hidden:true,width: 120,editor: {xtype:'hidden'}
	}, {
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}, {
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{
			fieldLabel: '车辆车型', id:"trainType_comb", 
			allowBlank:false ,
			hiddenName: "trainTypeIDX",
			xtype: "Base_combo",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            queryParams: {'vehicleType':'10'},// 根据车辆类型查询所对应的车辆种类
		    displayField: "typeCode", valueField: "idx",
		    returnField: [{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
            pageSize: 0, minListWidth: 150,
            editable:false,
			listeners : {   
                "select" : function() {   
                	
                }   
            }
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ 			
			id:'trainTypeShortNameId', xtype:"hidden"
		},searcher:{}
	},{
		header:'计划台数', dataIndex:'planCount', editor:{    
			fieldLabel:'计划台数',
	        xtype:'numberfield',
	        allowDecimals:false,
	        allowNegative:false,
	        minValue:1,
	        maxValue:99,
	        allowBlank:false
	        }, 
	        renderer: function(value, metaData, record, rowIndex, colIndex, store) {
	        	return value ;
			},
	        searcher: {anchor:'98%'}
	},{
		header:'在修台数', dataIndex:'zxcounts', editor:{  xtype:"hidden"},searcher:{}
	},{
		header:'修竣台数', dataIndex:'xjcounts', editor:{ xtype:"hidden" },searcher:{}
	}],
	// 必须要选中列检所才能添加
	beforeShowSaveWin: function(){
		if(TrainEnforcePlanDetailHc.planIdx == '###'){
			MyExt.Msg.alert("请先选择月计划！");
			return false;
		}
	},  
	// 保存之前存状态
	beforeSaveFn: function(data){ 
		if(Ext.isEmpty(data.planIdx)){
			data.planIdx = TrainEnforcePlanDetailHc.planIdx ;
		}
		return true; 
	},
	searchFn: function(searchParam){ 
		TrainEnforcePlanDetailHc.searchParam = searchParam ;
        TrainEnforcePlanDetailHc.grid.store.load();
	},
	afterSaveSuccessFn: function(result, response, options){
		TrainEnforcePlanDetailHc.grid.saveWin.hide();
        TrainEnforcePlanDetailHc.grid.store.reload();
        alertSuccess();
    },
    afterShowEditWin: function(record, rowIndex){    	
    	Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));
    },
    afterShowSaveWin: function(){
    	Ext.getCmp("trainType_comb").clearValue();
    }
});


//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainEnforcePlanDetailHc.grid });
	
//查询前添加过滤条件
TrainEnforcePlanDetailHc.grid.store.on('beforeload' , function(){
		this.baseParams.planIdx = TrainEnforcePlanDetailHc.planIdx ;
});

});