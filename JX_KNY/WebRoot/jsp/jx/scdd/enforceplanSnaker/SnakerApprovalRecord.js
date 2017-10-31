Ext.onReady(function(){
	
	Ext.namespace('SnakerApprovalRecord');
	SnakerApprovalRecord.searchParams = {};	
	
	// 流程实例ID
	SnakerApprovalRecord.processInstID = "";
	// 流程模板ID
	SnakerApprovalRecord.processId = "";
	
	// 流程审批记录列表
	SnakerApprovalRecord.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/snakerApprovalRecord!pageList.action', //装载列表数据的请求URL
	    viewConfig:{
			forceFit:true
		},
	    storeAutoLoad:false,	//设置grid的store为手动加载(不设置false会引起参数排序失效)
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},
		/*{ header:'节点名称', dataIndex:'taskName'},*/
		{ header:'审批人', dataIndex:'approvalUserName'},
		{ header:'审批时间', dataIndex:'approvalDate',xtype:'datecolumn'},
		{ header:'审批意见', dataIndex:'opinions'},
		{ header:'审批结果', dataIndex:'opinionType',renderer : function(v){
				if(v == '01')return "<font color='green' style='font-weight:bold;'>通过</font>";
				else if(v == '02')return "<font color='red' style='font-weight:bold;'>不通过</font>";
				else if(v == status_detail_complete) return "<font color='orange'  style='font-weight:bold;'>驳回</font>";
				else return "";
			}}
		],
		tbar:[],
		beforeShowEditWin: function(record, rowIndex){  
			return false;
		}
	});
	
	SnakerApprovalRecord.grid.store.on("beforeload", function(){
		if(Ext.isEmpty(SnakerApprovalRecord.processInstID)){
			SnakerApprovalRecord.processInstID = "AAA";
		}
		SnakerApprovalRecord.searchParams.processInstID = SnakerApprovalRecord.processInstID;
		this.baseParams.entityJson = Ext.util.JSON.encode(SnakerApprovalRecord.searchParams);
	});

});