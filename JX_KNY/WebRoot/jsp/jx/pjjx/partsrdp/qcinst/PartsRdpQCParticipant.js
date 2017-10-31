/**
 * 质量可检查人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpQCParticipant');                       //定义命名空间
PartsRdpQCParticipant.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpQCParticipant!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpQCParticipant!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpQCParticipant!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'记录卡实例主键', dataIndex:'rdpRecordCardIDX', editor:{  maxLength:50 }
	},{
		header:'检查项编码', dataIndex:'qCItemNo', editor:{  maxLength:50 }
	},{
		header:'检验人员', dataIndex:'qCEmpID', editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'检验人员名称', dataIndex:'qCEmpName', editor:{  maxLength:25 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpQCParticipant.grid });
});