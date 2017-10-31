/**
 * 配件检修工序实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpTecWS');                       //定义命名空间
	
	PartsRdpTecWS.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpTecWS!pageList.action',                 //装载列表数据的请求URL
	    
		storeAutoLoad: false,
		tbar: null,
		
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true
		},{
			header:'工艺卡主键', dataIndex:'rdpTecCardIDX', hidden:true
		},{
			header:'上级工序主键', dataIndex:'wsParentIDX', hidden:true
		},{
			header:'工序主键', dataIndex:'wsIDX', hidden:true
		},{
			header:'顺序号', dataIndex:'seqNo', width: 15, hidden:true
		},{
			header:'工序编号', dataIndex:'wsNo', width: 30
		},{
			header:'工序名称', dataIndex:'wsName', width: 60
		},{
			header:'工序描述', dataIndex:'wsDesc'
		},{
			header:'状态', dataIndex:'status', width: 20,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				if (value == TEC_WP_STATUS_WCL) return "未处理";
				if (value == TEC_WP_STATUS_YCL) return "已处理";
				return "错误！未知状态";
			}
		}],
		
		toEditFn: Ext.emptyFn
	});
	
	PartsRdpTecWS.grid.store.setDefaultSort('wsNo', 'ASC');
	
	// 列表数据容器加载时的过滤条件设置
	PartsRdpTecWS.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.util.JSON.encode({
			rdpTecCardIDX: PartsRdpTecWS.rdpTecCardIDX
		});
	});
	
});