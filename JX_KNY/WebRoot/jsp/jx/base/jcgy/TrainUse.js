/**
 *TODO 未调用， 待确定无用后删除
 * Jcgy_train_use 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainUse');                       //定义命名空间
TrainUse.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainUse!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainUse!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainUse!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车用途代码', dataIndex:'useID', hidden:true, editor:{ xtype:'textfield' }
	},{
		header:'机车用途名称', dataIndex:'useName', editor:{   }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainUse.grid });
});