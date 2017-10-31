/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpNotice');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpNotice.rdpIDX = "###";									// 作业主键
	PartsRdpNotice.status = PARTS_RDP_STATUS_DCL;					// 默认查询“待处理”的检修作业工单
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【销活】按钮触发的函数处理
	PartsRdpNotice.finishBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpNotice.grid)) {
			return;
		}
		
		// 获取被选中要进行销活的作业工单的idx主键
		PartsRdpBatchProcess.ids = $yd.getSelectedIdx(PartsRdpNotice.grid);
		PartsRdpBatchProcess.businessCode = BUSINESS_CODE_NOTICE;
		
		// 显示处理窗口
		PartsRdpBatchProcess.noAssignWin.show();
	};
	
	// 【领活】按钮触发的函数处理
	PartsRdpNotice.receiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpNotice.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpNotice.grid,
		        	url: ctx + '/partsRdpNotice!receiveBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpNotice.grid)}
		        }));
	        }
	    });  
	};
	
	// 【撤销领活】按钮触发的函数处理
	PartsRdpNotice.cancelReceiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpNotice.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认撤销领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpNotice.grid,
		        	url: ctx + '/partsRdpNotice!cancelBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpNotice.grid)}
		        }));
	        }
	    }); 
	};
	
	/** ************** 定义全局函数结束 ************** */
	
	PartsRdpNotice.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpNotice!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpNotice!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpNotice!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    saveForm: NoticeProcess.saveForm,			// 定义文件NoticeProcessWin.js
	    saveWin: NoticeProcess.win,					// 定义文件NoticeProcessWin.js
	    tbar:[{
	    	xtype:'label', text:'状态：'
    	}, {
    		xtype: 'radio', boxLabel: '待领取', name: 'status_n', inputValue: PARTS_RDP_STATUS_DLQ, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpNotice.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpNotice.grid.store.load();
					Ext.getCmp('receiveBtn_Notice').show();
					Ext.getCmp('finishBtn_Notice').hide();
					Ext.getCmp('cancelBtn_Notice').hide();
    			}
    		}
    	}, '&nbsp;' , {
    		xtype: 'radio', boxLabel: '待处理', checked: true, name: 'status_n', inputValue: PARTS_RDP_STATUS_DCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpNotice.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpNotice.grid.store.load();
					Ext.getCmp('receiveBtn_Notice').hide();
					Ext.getCmp('finishBtn_Notice').show();
					Ext.getCmp('cancelBtn_Notice').show();
    			}
    		}
    	},'&nbsp;',  {
    		xtype: 'radio', boxLabel: '已处理', name: 'status_n', inputValue: PARTS_RDP_STATUS_YCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpNotice.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpNotice.grid.store.load();
					Ext.getCmp('receiveBtn_Notice').hide();
					Ext.getCmp('finishBtn_Notice').hide();
					Ext.getCmp('cancelBtn_Notice').hide();
    			}
    		}
    	},'&nbsp;',  {
    		xtype: 'radio', boxLabel: '所有', name: 'status_n', inputValue: PARTS_RDP_STATUS_WKF, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpNotice.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpNotice.grid.store.load();
					Ext.getCmp('receiveBtn_Notice').hide();
					Ext.getCmp('finishBtn_Notice').hide();
					Ext.getCmp('cancelBtn_Notice').hide();
    			}
    		}
	    }, '&nbsp;', '-', {
			text:'领活', id: 'receiveBtn_Notice', iconCls:'startIcon', hidden: true, handler: PartsRdpNotice.receiveBatchJobFn
		}, {
			text:'销活', id:'finishBtn_Notice', iconCls:'checkIcon', handler: PartsRdpNotice.finishBatchJobFn
		}, {
			text:'撤销领活', id: 'cancelBtn_Notice', iconCls:'closeIcon', handler: PartsRdpNotice.cancelReceiveBatchJobFn
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'提票单编号', dataIndex:'noticeNo', width:60, editor:{  maxLength:50 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpNotice.grid.toEditFn(\""+ PartsRdpNotice.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'问题描述', dataIndex:'noticeDesc', editor:{  maxLength:500 }
		},{
			header:'提报人ID', dataIndex:'noticeEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'提报人', dataIndex:'noticeEmpName', width:30, editor:{  maxLength:25 }
		},{
			header:'提报时间', dataIndex:'noticeTime', format:'Y-m-d', width:40, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'处理结果描述', dataIndex:'solution', editor:{  maxLength:500 }
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true, editor:{  maxLength:100 }
		},{
			header:'作业人', dataIndex:'workEmpName', width:60, editor:{  maxLength:250 }
		},{
			header:'作业开始时间', dataIndex:'workStartTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i', editor:{ xtype:'my97date' }
		},{
			header:'作业结束时间', dataIndex:'workEndTime', width:60, xtype:'datecolumn', format:'Y-m-d H:i', editor:{ xtype:'my97date' }
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'领活人', dataIndex:'handleEmpName', width:30, editor:{  maxLength:25 }
		},{
			header:'状态', dataIndex:'status', width:25, editor:{  maxLength:20 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态"
			}
		},{
			header:'回退标识', dataIndex:'isBack', width:30, hidden:true, editor:{ xtype:'numberfield', maxLength:1 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (IS_BACK_YES == value) return "是";
				if (IS_BACK_NO == value) return "否";
				if (Ext.isEmpty(value)) return "否";
				return "错误！未知状态";
			}
		},{
			header:'回退次数', dataIndex:'backCount', width:30, hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
		}],
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】处理窗口
		toEditFn: function(grid, rowIndex, e){
			// 显示【检修作业工单】处理窗口
	        this.saveWin.show();
	        var record = this.store.getAt(rowIndex);
	        
	    	// 调用【检修作业工单】处理窗口的初始化函数，初始化【检修作业工单】处理窗口的各个字段值
	    	NoticeProcess.initFn(record);
	    	// 记录当前处理的【检修作业工单】的索引值，用以在处理窗口点击【上一工单】【下一工单】进行工单的快速切换
	    	NoticeProcess.index = parseInt(rowIndex);
	    	
	        // 设置【配件编号】【配件名称】【规格型号】【扩展编号】等固定字段值
	    	NoticeProcess.baseForm.find('name', 'partsNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsNo')[0].getValue());
	    	NoticeProcess.baseForm.find('name', 'partsName')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsName')[0].getValue());
	    	NoticeProcess.baseForm.find('name', 'specificationModel')[0].setValue(PartsRdpProcess.baseForm.find('name', 'specificationModel')[0].getValue());
	    	NoticeProcess.baseForm.find('name', 'extendNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'extendNo')[0].getValue());
	    }
	});
	// 默认按提票单编号正序排序
	PartsRdpNotice.grid.store.setDefaultSort('noticeNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpNotice.grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpIDX = PartsRdpNotice.rdpIDX;				// 作业主键
		searchParams.status = PartsRdpNotice.status;				// 状态
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
//	PartsRdpNotice.grid.store.on('load', function() {
//		if (PartsRdpNotice.grid.store.getCount() > 0) {
//			PartsRdpNotice.grid.getSelectionModel().selectAll();
//		}
//	})
	
});