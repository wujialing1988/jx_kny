/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordCard');                       //定义命名空间
	
	PartsRdpRecordCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpRecordCard!pageList.action',            //装载列表数据的请求URL
	    
	    storeAutoLoad: false,
	    tbar: ['search', '->', 'refresh'],
	    
	    searchOrder: ['recordCardNo', 'recordCardDesc'],
	    
	    searchFn: function(searchParam){
	    	searchParam.rdpIDX = PartsRdpRecordCard.rdpIDX;
			this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
	        });	
	    },
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'记录单主键', dataIndex:'rdpRecordIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'记录卡主键', dataIndex:'recordCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width: 20, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'记录卡编号', dataIndex:'recordCardNo', width: 35, editor:{  maxLength:30 }
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', width: 100, editor:{  maxLength:500 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpRecordCard.grid.toEditFn(\""+ PartsRdpRecordCard.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true, editor:{  maxLength:100 }
		},{
			header:'作业人', dataIndex:'workEmpName', width: 55, editor:{  maxLength:250 }
		},{
			header:'作业开始时间', dataIndex:'workStartTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, editor:{ xtype:'my97date' }
		},{
			header:'作业结束时间', dataIndex:'workEndTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, editor:{ xtype:'my97date' }
		},{
			header:'质量检验', dataIndex:'qcContent', width: 55, hidden: false, editor:{  maxLength:100 }
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'领活人', dataIndex:'handleEmpName', hidden: true, width: 30, editor:{  maxLength:25 }
		},{
			header:'状态', dataIndex:'status', width: 30, align:'center', editor:{  maxLength:20 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_WKF) return "未开放";				
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态";
			}
		},{
			header:'回退标识', dataIndex:'isBack', hidden: true, width: 25, align:'center', editor:{ xtype:'numberfield', maxLength:1 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (IS_BACK_YES == value) return "是";
				if (IS_BACK_NO == value) return "否";
				if (Ext.isEmpty(value)) return "否";
				return "错误！未知状态" + value;
			}
		},{
			header:'回退次数', dataIndex:'backCount', hidden: false, width: 25, align:'center', editor:{ xtype:'numberfield', maxLength:2 }
		},{
			header:'检修情况描述', dataIndex:'remarks', hidden: true
		}],
		
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】详情窗口
		toEditFn: function(grid, rowIndex, e){
	        // 当前工单记录索引
	        PartsRdpRecordCardDetail.index = rowIndex;
//	        // 当前工单记录
//	        PartsRdpRecordCardDetail.record = this.store.getAt(rowIndex);
	        // 显示窗口
	        PartsRdpRecordCardDetail.win.show();
	    }
	});
	
	// 默认按顺序号正序排序
	PartsRdpRecordCard.grid.store.setDefaultSort('recordCardNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpRecordCard.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.util.JSON.encode({
			rdpIDX: PartsRdpRecordCard.rdpIDX
		});
	});
	
});