/**
 * TODO 未见使用，J_JCGY_DUTY表也被删除，待确定无用后删除
 * 责任划分 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('Duty');                       //定义命名空间
Duty.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/duty!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/duty!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/duty!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'责任编码（主键）', dataIndex:'dutyID', hidden:true, editor:{ xtype:'textfield' }
	},{
		header:'责任名称', dataIndex:'dutyName', editor:{   }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:Duty.grid });
});