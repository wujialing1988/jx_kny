/**
 * 关联作业工位定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WpNodeStationDef');                       //定义命名空间
WpNodeStationDef.nodeIDX= "" ;//流程节点主键
WpNodeStationDef.searchParam = {};
WpNodeStationDef.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/wpNodeStationDef!findPageListForWPNode.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/wpNodeStationDef!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/wpNodeStationDef!logicDelete.action',            //删除数据的请求URL
    tbar:[{text : "选择工位",
			iconCls : "addIcon",
			handler : function(){
				PartsWorkStation.selectWin.show();
				PartsWorkStation.workStationName = Ext.getCmp("workStationNameId").getValue();				
				PartsWorkStation.grid.getStore().load();
				
			}},'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位编码', dataIndex:'workStationCode', editor:{  maxLength:50 }
	},{
		header:'工位名称', dataIndex:'workStationName', editor:{  maxLength:100 }
	},{
		header:'流水线名称', dataIndex:'repairLineName', editor:{  maxLength:100 }
	}]
});
WpNodeStationDef.grid.un('rowdblclick', WpNodeStationDef.grid.toEditFn, WpNodeStationDef.grid);
//查询前添加过滤条件
WpNodeStationDef.grid.store.on('beforeload' , function(){
	var searchParam = WpNodeStationDef.searchParam;
	searchParam.nodeIDX = WpNodeStationDef.nodeIDX ;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});