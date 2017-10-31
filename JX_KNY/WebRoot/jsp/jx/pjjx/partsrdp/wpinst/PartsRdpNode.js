/**
 * 配件检修作业节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpNode');                       //定义命名空间
PartsRdpNode.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpNode!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpNode!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpNode!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'流程节点主键', dataIndex:'wPNodeIDX', editor:{  maxLength:50 }
	},{
		header:'上级作业节点', dataIndex:'parentWPNodeIDX', editor:{  maxLength:50 }
	},{
		header:'节点名称', dataIndex:'wPNodeName', editor:{  maxLength:50 }
	},{
		header:'节点描述', dataIndex:'wPNodeDesc', editor:{  maxLength:500 }
	},{
		header:'工期', dataIndex:'ratedPeriod', editor:{ xtype:'numberfield', maxLength:6 }
	},{
		header:'顺序号', dataIndex:'seqNo', editor:{ xtype:'numberfield', maxLength:3 }
	},{
		header:'是否叶子节点', dataIndex:'isLeaf', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'计划开始时间', dataIndex:'planStartTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'计划结束时间', dataIndex:'planEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际开始时间', dataIndex:'realStartTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际结束时间', dataIndex:'realEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'状态', dataIndex:'status', editor:{  maxLength:20 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpNode.grid });
});