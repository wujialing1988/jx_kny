/**
 * 配件检修工序实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpTecWP');                       //定义命名空间
PartsRdpTecWP.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpTecWP!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpTecWP!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpTecWP!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'作业节点主键', dataIndex:'rdpNodeIDX', editor:{  maxLength:50 }
	},{
		header:'工艺卡主键', dataIndex:'tecCardIDX', editor:{  maxLength:50 }
	},{
		header:'工序主键', dataIndex:'wPIDX', editor:{  maxLength:50 }
	},{
		header:'工序编号', dataIndex:'wPNo', editor:{  maxLength:30 }
	},{
		header:'工序名称', dataIndex:'wPName', editor:{  maxLength:50 }
	},{
		header:'工序描述', dataIndex:'wPDesc', editor:{  maxLength:500 }
	},{
		header:'顺序号', dataIndex:'seqNo', editor:{ xtype:'numberfield', maxLength:3 }
	},{
		header:'状态', dataIndex:'status', editor:{  maxLength:20 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpTecWP.grid });
});