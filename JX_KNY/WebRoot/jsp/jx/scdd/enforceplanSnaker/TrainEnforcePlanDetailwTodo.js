/**
 * 机车施修计划明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlanDetailTodo');                       //定义命名空间
TrainEnforcePlanDetailTodo.searchParams = {};						//全局查询参数集


//机车施修计划明细列表
TrainEnforcePlanDetailTodo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetail!logicDelete.action',            //删除数据的请求URL
    viewConfig: null,
    storeAutoLoad:false,	//设置grid的store为手动加载(不设置false会引起参数排序失效)
    searchFormColNum: 2,
    saveFormColNum: 2, 
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{ header:'配属局名称', dataIndex:'bShortName', hidden:true, editor:{maxLength:50,xtype:'hidden'}},
	{ header:'配属段名称', dataIndex:'dShortName', hidden:true, editor:{ maxLength:50,xtype:'hidden'}},
	{
		header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'计划', dataIndex:'planStatus', width:65, renderer : function(v){
						if(v == status_detail_formation)return "未执行";
						else if(v == status_detail_redemption)return "<font color='orange' style='font-weight:bold;'>执行中</font>";
						else if(v == status_detail_complete) return "<font color='green'  style='font-weight:bold;'>已完成</font>";
						else return "";
					}
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden: true 
	},
	{ header:'工作号', dataIndex:'workNumber',width:65, hidden : true, editor:{
		maxLength:50,allowBlank: false},
		searcher:{disabled: true}},	
	{
		header:'车型', dataIndex:'trainTypeShortName', width:65, editor:{
		xtype: 'hidden' },
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'车号', dataIndex:'trainNo', width:65,
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'配属局ID', dataIndex:'bid', hidden: true
		},{
		header:'配属段ID', dataIndex:'did', hidden: true 
	},{
		header:'配属局', dataIndex:'bName', width:100,
		searcher:{ xtype: "textfield"}
	},{
		header:'配属段', dataIndex:'dNAME', width:100,
		searcher:{ xtype: "textfield" }
	}/*,{
		header:'支配单位', dataIndex:'usedDName',width:100, 
		searcher:{ xtype: "textfield" }
	}*/,{
		header:'BPS流程定义', dataIndex:'bpsProcessDefName', hidden:true, editor:{ xtype:"hidden" }, searcher: {disabled: true}
	},{
		header:'检修流程名称', dataIndex:'processName', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'BPS流程名称', dataIndex:'bpsProcessChName', hidden: true, editor:{  xtype:'hidden' }
	},{
		header:'bpsTemplateIdx', dataIndex:'bpsTemplateIdx', hidden: true, editor:{  xtype:'hidden' }
	},{
		header:'工艺流程主键', dataIndex:'processIDX', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true
	},{
		header:'修程', dataIndex:'repairClassName', width:65,
		searcher:{anchor:'98%'}
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true
	},{
		header:'修次', dataIndex:'repairtimeName', width:65
	},{
		header:'走行公里',dataIndex:'runningKM',maxLength:8
	},{
		header:'计划修车日期', dataIndex:'planStartDate', width:90, xtype:'datecolumn', 
		searcher:{id:'planStartDate_searchId',disabled: true}
	},{
		header:'计划交车日期', dataIndex:'planEndDate', width:90,xtype:'datecolumn',
		searcher:{disabled: true}
	},{
		header:'委修单位ID', dataIndex:'usedDId', hidden: true
	},{
		header:'委修单位', dataIndex:'usedDName'
	},{
		header:'不良状态预报', dataIndex:'remarks', 
		searcher:{disabled: true}
	}],
	searchOrder:[
		'trainTypeShortName','trainNo','bShortName','dShortName','usedDShortName','repairClassName'
	],
	tbar:[],
	beforeShowEditWin: function(record, rowIndex){  
		return false;
	}
});

TrainEnforcePlanDetailTodo.grid.store.on("beforeload", function(){
	TrainEnforcePlanDetailTodo.searchParams.trainEnforcePlanIDX = TrainEnforcePlanTaskTodo.idx;	
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainEnforcePlanDetailTodo.searchParams);
});


});