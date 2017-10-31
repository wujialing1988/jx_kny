/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpNoticeNew');                       //定义命名空间
	
	/** **************** 定义全局函数开始 **************** */
	PartsRdpNoticeNew.showRdpNoticeDetail = function(idx, rowIndex){
		// 当前记录单索引
        PartsRdpNoticeDetail.index = rowIndex;
        PartsRdpNoticeDetail.win.show();
	}
	/** **************** 定义全局函数结束 **************** */
	
	PartsRdpNoticeNew.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpNotice!pageList.action',                 //装载列表数据的请求URL
	    
	    storeAutoLoad: false,
	    tbar: null,
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'提票单编号', dataIndex:'noticeNo', width:60,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	     		var id = record.id;
	  			html = "<span><a href='#' onclick='PartsRdpNoticeNew.showRdpNoticeDetail(\""+ id +"\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'问题描述', dataIndex:'noticeDesc'
		},{
			header:'提报人ID', dataIndex:'noticeEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'提报人', dataIndex:'noticeEmpName', width:30
		},{
			header:'提报时间', dataIndex:'noticeTime', format:'Y-m-d', width:40, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'处理结果描述', dataIndex:'solution'
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true
		},{
			header:'处理人', dataIndex:'workEmpName', width:60
		},{
			header:'处理时间', dataIndex:'workStartTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i'
		},{
			header:'作业结束时间', dataIndex:'workEndTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i', hidden:true
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true
		},{
			header:'领活人', dataIndex:'handleEmpName', width:30, hidden:true
		},{
			header:'状态', dataIndex:'status', width:25, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态"
			}
		},{
			header:'回退标识', dataIndex:'isBack', width:30, hidden:true, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (IS_BACK_YES == value) return "是";
				if (IS_BACK_NO == value) return "否";
				if (Ext.isEmpty(value)) return "否";
				return "错误！未知状态";
			}
		},{
			header:'回退次数', dataIndex:'backCount', width:30, hidden:true
			
		}],
		
		toEditFn: Ext.emptyFn
	});
	// 默认按提票单编号正序排序
	PartsRdpNoticeNew.grid.store.setDefaultSort('noticeNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpNoticeNew.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.encode({
			rdpIDX: PartsRdpNoticeNew.rdpIDX
		});
	});
	
});