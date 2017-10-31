/**
 * 提票例外放行 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglTpExceptionJDJK');                       //定义命名空间
ZbglTpExceptionJDJK.searchParam = {};
ZbglTpExceptionJDJK.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTpException!findTpExceptionPageList.action',                 //装载列表数据的请求URL
    viewConfig: null,
    storeAutoLoad: false,
    tbar:[],    
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'tpIDX'}),
	{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true
	},{
		header:'JT6的ID', dataIndex:'tpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
	},{
		header:'提票人', dataIndex:'noticePersonName', editor:{  maxLength:25 }, width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'故障部件', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	},{
		header:'放行原因', dataIndex:'exceptionReason', editor:{  maxLength:300 }
	},{
		header:'处理人编码', dataIndex:'handlePersonId', hidden:true
	},{
		header:'处理人名称', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'处理时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	}],
	toEditFn: function(grid, rowIndex, e) {}
});
ZbglTpExceptionJDJK.grid.store.on("beforeload", function(){
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglTpExceptionJDJK.searchParam);		
});
});