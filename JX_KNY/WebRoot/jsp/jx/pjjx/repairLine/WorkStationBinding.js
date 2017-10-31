/**
 * 配件检修人员绑定工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkStationBinding');                       //定义命名空间
WorkStationBinding.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workStationBinding!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workStationBinding!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/workStationBinding!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位主键', dataIndex:'workStationIdx', editor:{  maxLength:50 }
	},{
		header:'人员主键', dataIndex:'empId', editor:{ xtype:'numberfield', maxLength:18 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:WorkStationBinding.grid });
});