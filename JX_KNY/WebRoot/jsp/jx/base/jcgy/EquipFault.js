/**
 * 故障现象编码 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('EquipFault');                       //定义命名空间
	EquipFault.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/equipFault!pageList.action',                //装载列表数据的请求URL
	    saveURL: ctx + '/equipFault!saveOrUpdate.action',            //保存数据的请求URL
	    deleteURL: ctx + '/equipFault!delete.action',           	 //删除数据的请求URL
		fields: [{
			header:'故障编号', dataIndex:'FaultID', editor:{ allowBlank:false }
		},{
			header:'故障名称', dataIndex:'FaultName', editor:{ allowBlank:false }
		},{
			header:'故障类别', dataIndex:'FaultTypeID', editor:{   }
		}],
		storeId:'FaultID'
	});
	
	EquipFault.grid.store.setDefaultSort('FaultName', 'ASC');
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:EquipFault.grid });
});