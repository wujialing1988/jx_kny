/**
 * 机车报废台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainScrap');                       //定义命名空间
TrainScrap.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainScrap!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainScrap!saveOrUpdateScrap.action',             //保存数据的请求URL
    saveFormColNum:2,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"trainId_scp", xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeName', editor:{ id:"trainTypeName_scp", maxLength:20 ,disabled:true}
	},{
		header:'车号', dataIndex:'trainNo', editor:{id:"trainNo_scp",  maxLength:50 ,disabled:true}
	},{
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20 }
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'部命令日期', dataIndex:'ministryOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'局命令日期', dataIndex:'bureauOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'报废日期', dataIndex:'orderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}],
	afterSaveSuccessFn: function(result, response, options){
        JczlTrain.grid.store.reload();
        alertSuccess();
        TrainScrap.grid.saveWin.hide();
    }
});
//创建调出窗口
TrainScrap.grid.createSaveWin();

});