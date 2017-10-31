/**
 * 整备作业项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglRdpWidi');//定义命名空间
	
	/* ************* 定义全局变量开始 ************* */
	ZbglRdpWidi.labelWidth = 100;
	ZbglRdpWidi.fieldWidth = 150;
	ZbglRdpWidi.searchParams = {};
	ZbglRdpWidi.rdpWiIDX = "";								// 整备范围主键
	var saveParams = {};
	/* ************* 定义全局变量结束 ************* */
  
	ZbglRdpWidi.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglRdpWidi!pageList.action',                 //装载列表数据的请求URL
	    saveWinWidth: 800,
	    saveWinHeight: 600,
	    storeAutoLoad: false,
	    singleSelect: true,
	    title:'数据项',
	    tbar:[],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'diCode',width:30
		},{
			header:'名称', dataIndex:'diName',width:30
		},{
			header:'结果', dataIndex:'diResult',width:30
		}],
		toEditFn: function(grid, rowIndex, e) {}
	});
	
	// 表格数据加载前的参数设置
 	ZbglRdpWidi.grid.store.on('beforeload', function(){
		ZbglRdpWidi.searchParams.rdpWiIDX = ZbglRdpWidi.rdpWiIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdpWidi.searchParams);
	});
	// 设置默认排序
	ZbglRdpWidi.grid.store.setDefaultSort('seqNo', 'ASC');
});