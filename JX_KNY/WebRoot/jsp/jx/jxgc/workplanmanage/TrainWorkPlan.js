/**
 * 机车检修作业计划测试grid 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainWorkPlan');                       //定义命名空间
TrainWorkPlan.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainWorkPlan!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    tbar:[{
    	text: '新增作业计划', iconCls:"addIcon", handler: function() {
    		TrainWorkPlanForm.showWin();
    	}
    },{
    	text: '编辑作业计划', iconCls:"addIcon", handler: function() {
    		var grid = TrainWorkPlan.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		TrainWorkPlanWin.showWin(data[0].data.idx);
    	}
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', width: 400
	},{
		header:'作业流程主键', dataIndex:'processIDX', editor:{  maxLength:50 }
	},{
		header:'作业流程名称', dataIndex:'processName', editor:{  maxLength:50 }
	},{
		header:'额定工期（小时）', dataIndex:'ratedWorkDay', editor:{ xtype:'numberfield', maxLength:12 }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'修程主键', dataIndex:'repairClassIDX', editor:{  maxLength:8 }
	},{
		header:'修程名称', dataIndex:'repairClassName', editor:{  maxLength:50 }
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', editor:{  maxLength:8 }
	},{
		header:'修次名称', dataIndex:'repairtimeName', editor:{  maxLength:50 }
	},{
		header:'机车作业计划状态', dataIndex:'workPlanStatus', editor:{  maxLength:64 }
	},{
		header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际完成时间', dataIndex:'endTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'计划生成时间', dataIndex:'workPlanTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'日历', dataIndex:'workCalendarIDX', editor:{  maxLength:50 }
	},{
		header:'配属段ID', dataIndex:'dID', editor:{  maxLength:20 }
	},{
		header:'配属段名称', dataIndex:'dNAME', editor:{  maxLength:50 }
	},{
		header:'委托维修段ID', dataIndex:'delegateDID', editor:{  maxLength:20 }
	},{
		header:'委托维修段名称', dataIndex:'delegateDName', editor:{  maxLength:50 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainWorkPlan.grid });
});