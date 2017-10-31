/**
 * 配件检修工艺 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpTec');                       //定义命名空间
PartsRdpTec.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpTec!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpTec!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpTec!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'工艺主键', dataIndex:'tecIDX', editor:{  maxLength:50 }
	},{
		header:'工艺编号', dataIndex:'tecNo', editor:{  maxLength:30 }
	},{
		header:'工艺名称', dataIndex:'tecName', editor:{  maxLength:50 }
	},{
		header:'工艺描述', dataIndex:'tecDesc', editor:{  maxLength:500 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpTec.grid });
});