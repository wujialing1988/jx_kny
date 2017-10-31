/**
 * 物料库存台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MATSTOCK');                       //定义命名空间
MATSTOCK.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/mATSTOCK!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/mATSTOCK!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/mATSTOCK!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{ xtype:'true', maxLength:50 }
	},{
		header:'库房名称', dataIndex:'whName', editor:{  maxLength:50 }
	},{
		header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50 }
	},{
		header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:100 }
	},{
		header:'单位', dataIndex:'unit', editor:{  maxLength:20 }
	},{
		header:'数量', dataIndex:'qty', editor:{ xtype:'numberfield' }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:MATSTOCK.grid });
});