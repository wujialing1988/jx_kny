/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsAcountTurnOver');                       //定义命名空间
	
	/** **************** 定义全局函数开始 **************** */

	/** **************** 定义全局函数结束 **************** */
	
	PartsAcountTurnOver.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsManageLog!pageQuery.action',                 //装载列表数据的请求URL	    
	    storeAutoLoad: false,
	    tbar: null,
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'执行操作', dataIndex:'eventType'
		},{
			header:'操作内容', dataIndex:'eventDesc'
		},{
			header:'操作人ID', dataIndex:'operatorId', hidden:true
		},{
			header:'操作人', dataIndex:'operator', width:60
		},{
			header:'操作时间', dataIndex:'eventTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i:s'
	
		}],
		
		toEditFn: Ext.emptyFn
	});
	// 默认按提票单编号正序排序
	PartsAcountTurnOver.grid.store.setDefaultSort('eventTime', "desc");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsAcountTurnOver.grid.store.on('beforeload', function() {
//		var sqlStr = "  PARTS_ACCOUNT_IDX in(select idx from PJWZ_PARTS_ACCOUNT  where parts_no='" + PartsAcountTurnOver.partsNo +"') ";
//		var whereList = [
//		 {sql: sqlStr, compare: Condition.eq}
//		]
		var whereList = [
		 {propName:"partsAccountIdx", propValue:PartsAcountTurnOver.partsAccountIdx,compare:Condition.EQ,stringLike:false}
		]
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
});