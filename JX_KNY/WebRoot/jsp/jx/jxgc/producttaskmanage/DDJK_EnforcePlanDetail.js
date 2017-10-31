/**
 * 机车施修计划明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlanDetail');                       //定义命名空间
TrainEnforcePlanDetail.searchParams = {};						//全局查询参数集

//机车施修计划明细列表
TrainEnforcePlanDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetail!logicDelete.action',            //删除数据的请求URL
    viewConfig: null,
    tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden: true	    
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{id: 'trainTypeShortName', xtype: 'hidden' },
		searcher:{xtype: 'textfield'}
	},{
		header:'车号', dataIndex:'trainNo'
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true
	},{
		header:'修程', dataIndex:'repairClassName', editor:{  xtype:'hidden' }
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true
	},{
		header:'修次', dataIndex:'repairtimeName', editor:{  xtype: 'hidden' }
	},{
		header:'计划开始日期', dataIndex:'planStartDate', xtype:'datecolumn', 
		editor:{ id: 'planStartDate_detailId', xtype:'my97date', initNow: false, allowBlank: false, validator: TrainEnforcePlanDetail.checkStartDate},
		searcher:{disabled: true}
	},{
		header:'计划结束日期', dataIndex:'planEndDate', xtype:'datecolumn', hidden: true, 
		editor:{ id: 'planEndDate_detailId', xtype:'my97date', initNow: false, allowBlank: false, validator: TrainEnforcePlanDetail.checkEndDate },
		searcher:{disabled: true}
	},{
		header:'计划状态', dataIndex:'planStatus', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'实际开始日期', dataIndex:'realStartDate', xtype:'datecolumn', hidden: true
	},{
		header:'实际结束日期', dataIndex:'realEndDate', xtype:'datecolumn', hidden: true
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden: true, editor:{ xtype:'hidden'}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'配属单位', dataIndex:'holdOrgName'
	},{
		header:'备注', dataIndex:'remarks',hidden: true
	}]
});

TrainEnforcePlanDetail.grid.store.on("beforeload", function(){
//	TrainEnforcePlanDetail.searchParams.trainEnforcePlanIDX = TrainEnforcePlan.idx;	
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainEnforcePlanDetail.searchParams);
});
//移除侦听器
TrainEnforcePlanDetail.grid.un('rowdblclick', TrainEnforcePlanDetail.grid.toEditFn, TrainEnforcePlanDetail.grid);
});