/**
 * 机车备用明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainSpareDetail');                       //定义命名空间
TrainSpareDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainSpareDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainSpareDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainSpareDetail!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型名称', dataIndex:'trainTypeName', editor:{  maxLength:20 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'新造后走行公里', dataIndex:'achieveKM', editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'备用类型', dataIndex:'spareType', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20 }
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'命令日期', dataIndex:'orderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'部备用', dataIndex:'ministrySpare', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'局备用', dataIndex:'bureauSpare', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'段备用', dataIndex:'depotSpare', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'使用别', dataIndex:'trainUse', editor:{  maxLength:50 }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'申请人', dataIndex:'applyPerson', editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'申请人名称', dataIndex:'applyPersonName', editor:{  maxLength:25 }
	},{
		header:'申请时间', dataIndex:'applyTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	}]
});
//页面自适应布局
});