/**
 * 作业节点前后置关系 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpNodeSeq');                       //定义命名空间
PartsRdpNodeSeq.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpNodeSeq!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpNodeSeq!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpNodeSeq!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'流程节点主键', dataIndex:'wPNodeIDX', editor:{  maxLength:50 }
	},{
		header:'前置流程节点主键', dataIndex:'preWPNodeIDX', editor:{  maxLength:50 }
	},{
		header:'类型', dataIndex:'seqClass', editor:{  maxLength:20 }
	},{
		header:'延隔时间', dataIndex:'beforeDelayTime', hidden:true, editor:{ xtype:'true', maxLength:12 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpNodeSeq.grid });
});