/**
 * 作业人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpWorker');                       //定义命名空间
PartsRdpWorker.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpWorker!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpWorker!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpWorker!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'作业人员', dataIndex:'workEmpID', editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'作业人员名称', dataIndex:'workEmpName', editor:{  maxLength:25 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpWorker.grid });
});