/**
 * 配件检修工艺工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpTecCard');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpTecCard.rdpIDX = "###";									// 作业主键
	PartsRdpTecCard.status = PARTS_RDP_STATUS_DCL;					// 默认查询“待处理”的检修作业工单
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【销活】按钮触发的函数处理
	PartsRdpTecCard.finishBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpTecCard.grid)) {
			return;
		}
		
		// 获取被选中要进行销活的作业工单的idx主键
		PartsRdpBatchProcess.ids = $yd.getSelectedIdx(PartsRdpTecCard.grid);
		PartsRdpBatchProcess.businessCode = BUSINESS_CODE_TEC_CARD;
		
		// 显示处理窗口
		PartsRdpBatchProcess.noAssignWin.show();
	};
	
	// 【领活】按钮触发的函数处理
	PartsRdpTecCard.receiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpTecCard.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpTecCard.grid,
		        	url: ctx + '/partsRdpTecCard!receiveBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpTecCard.grid)}
		        }));
	        }
	    });  
	};
	
	// 【撤销领活】按钮触发的函数处理
	PartsRdpTecCard.cancelReceiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpTecCard.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认撤销领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpTecCard.grid,
		        	url: ctx + '/partsRdpTecCard!cancelBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpTecCard.grid)}
		        }));
	        }
	    }); 
	};
	
	/** ************** 定义全局函数结束 ************** */
	
	PartsRdpTecCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpTecCard!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpTecCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpTecCard!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    saveForm: TecCardProcess.saveForm,			// 定义文件TecCardProcessWin.js
	    saveWin: TecCardProcess.win,				// 定义文件TecCardProcessWin.js
	    tbar:[{
	    	xtype:'label', text:'状态：'
    	}, {
    		xtype: 'radio', boxLabel: '待领取', name: 'status_t', inputValue: PARTS_RDP_STATUS_DLQ, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecCard.grid.store.load();
					Ext.getCmp('receiveBtn_TecCard').show();
					Ext.getCmp('finishBtn_TecCard').hide();
					Ext.getCmp('cancelBtn_TecCard').hide();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '待处理', checked: true, name: 'status_t', inputValue: PARTS_RDP_STATUS_DCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecCard.grid.store.load();
					Ext.getCmp('receiveBtn_TecCard').hide();
					Ext.getCmp('finishBtn_TecCard').show();
					Ext.getCmp('cancelBtn_TecCard').show();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '已处理', name: 'status_t', inputValue: PARTS_RDP_STATUS_YCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecCard.grid.store.load();
					Ext.getCmp('receiveBtn_TecCard').hide();
					Ext.getCmp('finishBtn_TecCard').hide();
					Ext.getCmp('cancelBtn_TecCard').hide();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '所有', name: 'status_t', inputValue: PARTS_RDP_STATUS_WKF, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecCard.grid.store.load();
					Ext.getCmp('receiveBtn_TecCard').hide();
					Ext.getCmp('finishBtn_TecCard').hide();
					Ext.getCmp('cancelBtn_TecCard').hide();
    			}
    		}
	    }, '&nbsp;', '-', {
			text:'领活', id: 'receiveBtn_TecCard', iconCls:'startIcon', hidden: true, handler: PartsRdpTecCard.receiveBatchJobFn
		}, {
			text:'销活', id:'finishBtn_TecCard', iconCls:'checkIcon', handler: PartsRdpTecCard.finishBatchJobFn
		}, {
			text:'撤销领活', id: 'cancelBtn_TecCard', iconCls:'closeIcon', handler: PartsRdpTecCard.cancelReceiveBatchJobFn
		}],
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
			header:'工艺卡编号', dataIndex:'tecCardNo', width: 40, editor:{  maxLength:30 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpTecCard.grid.toEditFn(\""+ PartsRdpTecCard.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'工艺卡描述', dataIndex:'tecCardDesc', width: 140, editor:{  maxLength:500 }
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
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态"
			}
		}],
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】处理窗口
		toEditFn: function(grid, rowIndex, e){
			// 显示【检修作业工单】处理窗口
	        this.saveWin.show();
	        var record = this.store.getAt(rowIndex);
	        
	    	// 调用【检修作业工单】处理窗口的初始化函数，初始化【检修作业工单】处理窗口的各个字段值
	    	TecCardProcess.initFn(record);
	    	// 记录当前处理的【检修作业工单】的索引值，用以在处理窗口点击【上一工单】【下一工单】进行工单的快速切换
	    	TecCardProcess.index = parseInt(rowIndex);
	    	
	        // 设置【配件编号】【配件名称】【规格型号】【扩展编号】等固定字段值
	    	TecCardProcess.baseForm.find('name', 'partsNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsNo')[0].getValue());
	    	TecCardProcess.baseForm.find('name', 'partsName')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsName')[0].getValue());
	    	TecCardProcess.baseForm.find('name', 'specificationModel')[0].setValue(PartsRdpProcess.baseForm.find('name', 'specificationModel')[0].getValue());
	    	TecCardProcess.baseForm.find('name', 'extendNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'extendNo')[0].getValue());
	    }
	});
	// 默认按顺序号正序排序
	PartsRdpTecCard.grid.store.setDefaultSort('seqNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpTecCard.grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpIDX = PartsRdpTecCard.rdpIDX;				// 作业主键
		searchParams.status = PartsRdpTecCard.status;				// 状态
		var whereList = []; 
		for(prop in searchParams){
			if('status' == prop){
				// 当前登录用户只能处理自己领取的工单
				if (PARTS_RDP_STATUS_DCL == searchParams[prop]) {
					whereList.push({propName:"handleEmpID", propValue:empid, compare:Condition.eq, stringLike: false});
				}
				if (PARTS_RDP_STATUS_WKF == searchParams[prop]) {
					whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.NE});
				} else {
					whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.LLIKE});
				}
				continue;
			}
			whereList.push({propName:prop, propValue:searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
//	// 表格加载数据成功后，自动选择所有的记录
//	PartsRdpTecCard.grid.store.on('load', function() {
//		if (PartsRdpTecCard.grid.store.getCount() > 0) {
//			PartsRdpTecCard.grid.getSelectionModel().selectAll();
//		}
//	})
	
});