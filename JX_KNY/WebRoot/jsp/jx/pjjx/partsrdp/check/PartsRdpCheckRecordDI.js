/**
 * 配件检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordDI');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpRecordDI.labelWidth = 100;
	PartsRdpRecordDI.fieldWidth = 140;
	PartsRdpRecordDI.rdpRecordRIIDX = "###";				// 记录卡实例主键
	PartsRdpRecordDI.searchParams = {};						// 查询实体
	/** ************** 定义全局变量结束 ************** */
	
	
	// 数据容器
	PartsRdpRecordDI.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:false, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/partsRdpRecordDI!pageList.action',
		fields:['idx','rdpRecordRIIDX','recordDIIDX','dataItemNo','dataItemName','isBlank','seqNo', 'dataItemResult']
	});
	PartsRdpRecordDI.store.setDefaultSort('seqNo', 'ASC');
	// 行选择模式
    PartsRdpRecordDI.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
    // 单据明细表格
    PartsRdpRecordDI.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: PartsRdpRecordDI.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: PartsRdpRecordDI.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'rdpRecordRIIDX', hidden:true, header:'检修检测项实例主键'
	        },{
	        	sortable:false, dataIndex:'recordDIIDX', hidden:true, hidden:true, header:'检测项主键'
	        },{	
	        	sortable:false, dataIndex:'dataItemNo', header:'检测项编号', hidden:true
	        },{	
	        	sortable:false, dataIndex:'seqNo', header:'顺序号', width: 10
	        },{	
	        	sortable:false, dataIndex:'dataItemName', header:'检测项名称', width: 50
	        },{	
	        	sortable:false, dataIndex:'dataItemResult', header:'检测结果', width: 30
	        },{
	        	sortable:false, dataIndex:'isBlank', header:'是否必填', width: 15, renderer: function(v){
				if (v == IS_BLANK_YES) return '是';
				if (v == IS_BLANK_NO) return '否';
			}
	        }])
    });
	PartsRdpRecordDI.grid.store.on("beforeload", function(){
		var searchParams = {rdpRecordRIIDX: PartsRdpRecordDI.rdpRecordRIIDX};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);  
	});
	
});