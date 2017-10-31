/**
 * 配件故障分类编码 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('EquipFaultType');                       //定义命名空间
EquipFaultType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/equipFaultType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/equipFaultType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/equipFaultType!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'配件故障分类代码', dataIndex:'FaultTypeID', hidden:true, editor:{ xtype:'textfield' }
	},{
		header:'配件故障分类名称', dataIndex:'FaultTypeName', editor:{   }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:EquipFaultType.grid });
});