/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkTicket');                       //定义命名空间
	
	TrainWorkTicket.rpdIdx ;								// 作业计划
	
	/** **************** 定义全局函数开始 **************** */
	TrainWorkTicket.showTrainWorkTicketDetail = function(idx, rowIndex){
		// 当前记录单索引
        TrainWorkTicketDetail.index = rowIndex;
        TrainWorkTicketDetail.win.show();
	}
	/** **************** 定义全局函数结束 **************** */
	
	TrainWorkTicket.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultTicket!pageList.action',                 //装载列表数据的请求URL	    
	    storeAutoLoad: false,
	    tbar: null,
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业计划主键', dataIndex:'workPlanIDX', hidden:true
		},{
			header:'流程节点', dataIndex:'nodeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'单号', dataIndex:'ticketCode', width:60,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	     		var id = record.id;
	  			html = "<span><a href='#' onclick='TrainWorkTicket.showTrainWorkTicketDetail(\""+ id +"\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'提票类型', dataIndex:'type', width:60
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', hidden:true,width:60
		},{
			header:'常见故障', dataIndex:'faultName',hidden:true,  width:60
		},{
			header:'不良状态描述', dataIndex:'faultDesc', width:280
		},{
			header:'标签id', dataIndex:'reasonAnalysisId', hidden:true
		},{
			header:'标签', dataIndex:'reasonAnalysis', width: 280
		},{
			header:'提报人ID', dataIndex:'ticketEmpId', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'提报人', dataIndex:'ticketEmp', width:30
		},{
			header:'提报时间', dataIndex:'ticketTime', format:'Y-m-d', width:50, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'施修方案', dataIndex:'faultReason', width:100
		},{
			header:'处理人ID', dataIndex:'repairEmpID', hidden:true
		},{
			header:'处理人', dataIndex:'repairEmp', width:60
		},{
			header:'处理完成时间', dataIndex:'completeTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i'	
		},{
			header:'销票人ID', dataIndex:'completeEmpID', hidden:true
		},{
			header:'销票人', dataIndex:'completeEmp', width:30, hidden:true
		},{
			header:'状态', dataIndex:'status', width:30, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == STATUS_DRAFT) return "未处理";				
				if (value == STATUS_OPEN) return "处理中";				
				if (value == STATUS_OVER) return "已处理"		
				return "错误！未知状态"
			}	
		}],
		
		toEditFn: Ext.emptyFn
	});
	// 默认按提票单编号正序排序
	TrainWorkTicket.grid.store.setDefaultSort('ticketCode', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	TrainWorkTicket.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.encode({
			workPlanIDX: TrainWorkTicket.rpdIdx
		});
	});
	
});