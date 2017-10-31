/**
 * 设备分类 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('DeviceType');                       //定义命名空间
DeviceType.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/deviceType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/deviceType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/deviceType!delete.action',            //删除数据的请求URL
    tbar: ['add','delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'设备分类编码', dataIndex:'deviceTypeCode', editor:{ allowBlank: false, maxLength:50 }
	},{
		header:'设备分类名称', dataIndex:'deviceTypeName', editor:{  allowBlank: false,maxLength:50 }
	},{
		header:'设备分类描述', dataIndex:'deviceTypeDesc', editor:{  maxLength:500 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:DeviceType.grid });
});