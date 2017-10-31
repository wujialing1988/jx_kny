/**
 * 配件检修工艺工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpTecCard');                       //定义命名空间
	
	PartsRdpTecCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpTecCard!pageList.action',                 //装载列表数据的请求URL
	    
	    storeAutoLoad: false,
	    tbar: ['search', '->', 'refresh'],
	    
	    searchOrder: ['tecCardNo', 'tecCardDesc'],
	    
	    searchFn: function(searchParam){
	    	searchParam.rdpIDX = PartsRdpTecCard.rdpIDX;
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
			header:'工艺实例主键', dataIndex:'rdpTecIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'工艺卡主键', dataIndex:'tecCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'顺序号', hidden:true, dataIndex:'seqNo', width: 22, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'工艺卡编号', dataIndex:'tecCardNo', width: 50, editor:{  maxLength:30 }
		},{
			header:'工艺卡描述', dataIndex:'tecCardDesc', width: 140, editor:{  maxLength:500 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpTecCard.grid.toEditFn(\""+ PartsRdpTecCard.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true, editor:{  maxLength:100 }
		},{
			header:'作业人', dataIndex:'workEmpName', editor:{  maxLength:250 }
		},{
			header:'作业开始时间', dataIndex:'workStartTime', width: 50, xtype:'datecolumn', format:'Y-m-d H:i', editor:{ xtype:'my97date' }
		},{
			header:'作业结束时间', dataIndex:'workEndTime', width: 50, xtype:'datecolumn', format:'Y-m-d H:i', editor:{ xtype:'my97date' }
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'领活人', dataIndex:'handleEmpName', hidden:true,width: 30, editor:{  maxLength:25 }
		},{
			header:'状态', dataIndex:'status', width: 30, editor:{  maxLength:20 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_WKF) return "未开放";		
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态"
			}
		}],
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】处理窗口
		toEditFn: function(grid, rowIndex, e){
	        // 当前工单记录索引
	        PartsRdpTecCardDetail.index = rowIndex;
//	        // 当前工单记录
//	        PartsRdpTecCardDetail.record = this.store.getAt(rowIndex);
	        // 显示窗口
	        PartsRdpTecCardDetail.win.show();
	    }
	});
	// 默认按顺序号正序排序
	PartsRdpTecCard.grid.store.setDefaultSort('tecCardNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpTecCard.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.util.JSON.encode({
			rdpIDX: PartsRdpTecCard.rdpIDX
		});
	});
	
});