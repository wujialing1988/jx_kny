/**
 * 质量检查结果 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpQR');                       //定义命名空间
PartsRdpQR.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpQR!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpQR!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpQR!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'关联主键', dataIndex:'relIDX', editor:{  maxLength:50 }
	},{
		header:'记录卡实例主键', dataIndex:'rdpRecordCardIDX', editor:{  maxLength:50 }
	},{
		header:'质量检查项主键', dataIndex:'qCItemIDX', editor:{  maxLength:50 }
	},{
		header:'检查项编码', dataIndex:'qCItemNo', editor:{  maxLength:50 }
	},{
		header:'检查项名称', dataIndex:'qCItemName', editor:{  maxLength:50 }
	},{
		header:'检验方式', dataIndex:'checkWay', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'是否指派', dataIndex:'isAssign', editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'顺序号', dataIndex:'seqNo', editor:{ xtype:'numberfield', maxLength:3 }
	},{
		header:'检验人员', dataIndex:'qREmpID', editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'检验人员名称', dataIndex:'qREmpName', editor:{  maxLength:25 }
	},{
		header:'检验结果', dataIndex:'qRResult', editor:{  maxLength:50 }
	},{
		header:'检验时间', dataIndex:'qRTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'状态', dataIndex:'status', editor:{  maxLength:20 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpQR.grid });
});