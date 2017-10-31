/**
 * 物料消耗情况 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpExpendMatQuery');                       // 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpExpendMatQuery.searchParams = {};
	PartsRdpExpendMatQuery.rdpIDX = "###";				// 作业主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义物料消耗表格开始 ************** */
	PartsRdpExpendMatQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpExpendMat!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpExpendMat!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpExpendMat!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'工艺卡主键', dataIndex:'rdpTecCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode',width: 30, editor:{  maxLength:50, disabled: true }
		},{
			header:'物料描述', dataIndex:'matDesc',width: 100, editor:{  maxLength:100, disabled: true }
		},{
			header:'消耗数量', dataIndex:'qty',width: 30, editor:{ xtype:'numberfield', vtype:'positiveInt', maxLength:6}
		},{
			header:'单位', dataIndex:'unit',width: 20, editor:{  maxLength:20, disabled: true }
		},{
			header:'单价', dataIndex:'price',width: 20, editor:{ xtype:'numberfield', maxLength:6, disabled: true }
		},{
			header:'消耗人ID', dataIndex:'handleEmpId', hidden:true, editor:{disabled: true }
		},{
			header:'消耗人', dataIndex:'handleEmpName',width: 30, editor:{disabled: true }
		},{
			header:'消耗班组', dataIndex:'handleOrgId', hidden:true, editor:{disabled: true }
		},{
			header:'消耗班组名称', dataIndex:'handleOrgName', hidden:true, editor:{disabled: true }
		},{
			header:'消耗班组序列', dataIndex:'handleOrgSeq', hidden:true, editor:{disabled: true }
		}]
	});
	PartsRdpExpendMatQuery.grid.un('rowdblclick', PartsRdpExpendMatQuery.grid.toEditFn, PartsRdpExpendMatQuery.grid);
	PartsRdpExpendMatQuery.grid.store.setDefaultSort('matCode', 'ASC');
	// 列表数据容器加载时的过滤条件设置
	PartsRdpExpendMatQuery.grid.store.on('beforeload', function() {
		var searchParams = PartsRdpExpendMatQuery.searchParams;
		searchParams.rdpIDX = PartsRdpExpendMatQuery.rdpIDX;							// 作业主键
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义物料消耗表格结束 ************** */
	
});