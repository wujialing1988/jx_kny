/**
 * 机务设备检测数据项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpEquipDI');                       //定义命名空间
PartsRdpEquipDI.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpEquipDI!pageList.action',                 //装载列表数据的请求URL
    tbar: null,
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'设备工单实例主键', dataIndex:'rdpEquipCardIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'数据项定义主键', dataIndex:'equipDataItemIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'检测项编号', dataIndex:'dataItemNo', editor:{  maxLength:30 }
	},{
		header:'检测项名称', dataIndex:'dataItemName', editor:{  maxLength:50 }
	},{
		header:'检测项描述', dataIndex:'dataItemDesc', editor:{  maxLength:500 }
	},{
		header:'单位', dataIndex:'unit', editor:{ maxLength:20 }
	},{
		header:'检测值', dataIndex:'itemValue', editor:{ xtype:'numberfield' }
	},{
		header:'顺序号', dataIndex:'seqNo',hidden:true, editor:{ xtype:'numberfield', maxLength:3 }
	}]
});
//移除侦听器
PartsRdpEquipDI.grid.un('rowdblclick', PartsRdpEquipDI.grid.toEditFn, PartsRdpEquipDI.grid);
//查询前添加过滤条件
PartsRdpEquipDI.grid.store.on('beforeload' , function(){
	var searchParam = {} ;
	searchParam.rdpEquipCardIDX = PartsRdpEquipCard.idx ; //设备工单实例主键
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});