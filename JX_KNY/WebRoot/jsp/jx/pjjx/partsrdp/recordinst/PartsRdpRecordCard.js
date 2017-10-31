/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordCard');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpRecordCard.rdpIDX;									// 作业主键
	PartsRdpRecordCard.status = PARTS_RDP_STATUS_DCL;					// 默认查询“待处理”的检修作业工单
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【销活】按钮触发的函数处理
	PartsRdpRecordCard.finishBatchJobFn = function() {
	    var sm = PartsRdpRecordCard.grid.getSelectionModel();
	    if (sm.getCount() <= 0) {
	    	MyExt.Msg.alert("尚未选择任何记录！");	
	    	return;
	    }
	    
	    // 动态渲染需指派的质量检查人员设置组件
	    Ext.getCmp('panel_Assign').removeAll();
	    
	    // 拼接所有已选择记录工单的质量检查项内容
	    var qcContents = "";
	    var records = sm.getSelections();
	    for (var i = 0; i < records.length; i++) {
	    	var record = records[i];
	    	qcContents += record.get('qcContent');
	    }
	    PartsRdpBatchProcess.qcItemNos = new Array();
	    if (!Ext.isEmpty(qcContents)) {
		    for (var i = 0; i < objList.length; i++) {
	        	var qcItemName = objList[i].qCItemName;
	        	if (qcContents.indexOf(qcItemName) >= 0) {
	        		
			    	PartsRdpBatchProcess.qcItemNos.push(objList[i].qCItemNo);
			    	
	        		var panel = {};
					panel.items=[];
					var item = {}					// 人员姓名
					item.xtype = 'TeamEmployee_SelectWin';
					item.editable = false; 
					item.allowBlank = false;
					item.fieldLabel = objList[i].qCItemName;
					item.hiddenName = RecordCardProcess.nameFlag + objList[i].qCItemNo;
					item.valueField = 'empname'; 
					item.displayField = 'empname';
					item.returnField = [{widgetId:RecordCardProcess.idFlag + objList[i].qCItemNo + "_A", propertyName:"empid"}]
					panel.items.push(item);
					
					item = {};						// 人员ID
					item.xtype = 'hidden';
					item.name = RecordCardProcess.idFlag + objList[i].qCItemNo;
					item.id = RecordCardProcess.idFlag + objList[i].qCItemNo + "_A";
					panel.items.push(item); 
					
					Ext.getCmp('panel_Assign').add(panel);
					Ext.getCmp('panel_Assign').doLayout();
	        	}
	        }
	    }
	    
		// 获取被选中要进行销活的作业工单的idx主键
		PartsRdpBatchProcess.ids = $yd.getSelectedIdx(PartsRdpRecordCard.grid);
		
		// 显示处理窗口
		PartsRdpBatchProcess.assignWin.show();
			
	};
	
	// 【领活】按钮触发的函数处理
	PartsRdpRecordCard.receiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpRecordCard.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpRecordCard.grid,
		        	url: ctx + '/partsRdpRecordCard!receiveBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpRecordCard.grid)}
		        }));
	        }
	    });  
	};
	
	// 【撤销领活】按钮触发的函数处理
	PartsRdpRecordCard.cancelReceiveBatchJobFn = function() {
		if (!$yd.isSelectedRecord(PartsRdpRecordCard.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认撤销领活？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpRecordCard.grid,
		        	url: ctx + '/partsRdpRecordCard!cancelBatchJob.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpRecordCard.grid)}
		        }));
	        }
	    }); 
	};
	
	// 查询配件检修单详情前同步更新可视化系统的检修项结果
	PartsRdpRecordCard.synPartsCheckItemDataAndSavePartsRdpRecordDI = function() {
        Ext.Ajax.request({
			url: ctx + "/partsCheckItemData!synPartsCheckItemDataAndSavePartsRdpRecordDI.action",
			params: { 
				rdpRecordCardRdpIDX: PartsRdpRecordCard.rdpIDX
			},
			success: function(r){
				var retn = Ext.util.JSON.decode(r.responseText);
				if(retn.success){
					PartsRdpRecordDI.grid.store.load();
					MyExt.Msg.alert("可视化数据同步更新成功！");
				}else{
					alertFail(retn.errMsg);
				}
			},
			failure: function(){
				alertFail("请求超时！");
			}
		});
	};
	/** ************** 定义全局函数结束 ************** */
	
	PartsRdpRecordCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpRecordCard!pageQuery.action',            //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpRecordCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpRecordCard!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    saveForm: RecordCardProcess.saveForm,			// 定义文件RecordCardProcessWin.js
	    saveWin: RecordCardProcess.win,					// 定义文件RecordCardProcessWin.js
	    tbar:[{
	    	xtype:'label', text:'状态：'
    	}, {
    		xtype: 'radio', boxLabel: '待领取', name: 'status_r', inputValue: PARTS_RDP_STATUS_DLQ, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordCard.grid.store.load();
					Ext.getCmp('receiveBtn_RecordCard').show();
					Ext.getCmp('finishBtn_RecordCard').hide();
					Ext.getCmp('cancelBtn_RecordCard').hide();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '待处理', checked: true, name: 'status_r', inputValue: PARTS_RDP_STATUS_DCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordCard.grid.store.load();
					Ext.getCmp('receiveBtn_RecordCard').hide();
					Ext.getCmp('finishBtn_RecordCard').show();
					Ext.getCmp('cancelBtn_RecordCard').show();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '已处理', name: 'status_r', inputValue: PARTS_RDP_STATUS_YCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordCard.grid.store.load();
					Ext.getCmp('receiveBtn_RecordCard').hide();
					Ext.getCmp('finishBtn_RecordCard').hide();
					Ext.getCmp('cancelBtn_RecordCard').hide();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '所有', name: 'status_r', inputValue: PARTS_RDP_STATUS_WKF, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpRecordCard.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpRecordCard.grid.store.load();
					Ext.getCmp('receiveBtn_RecordCard').hide();
					Ext.getCmp('finishBtn_RecordCard').hide();
					Ext.getCmp('cancelBtn_RecordCard').hide();
    			}
    		}
    	}, '&nbsp;', '-', {
			text:'领活', id: 'receiveBtn_RecordCard', iconCls:'startIcon', hidden: true, handler: PartsRdpRecordCard.receiveBatchJobFn
		}, {
			text:'销活', id:'finishBtn_RecordCard', iconCls:'checkIcon', handler: PartsRdpRecordCard.finishBatchJobFn
		}, {
			text:'撤销领活', id: 'cancelBtn_RecordCard', iconCls:'closeIcon', handler: PartsRdpRecordCard.cancelReceiveBatchJobFn
		}],
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
			header:'记录卡编号', dataIndex:'recordCardNo', width: 40, editor:{  maxLength:30 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PartsRdpRecordCard.grid.toEditFn(\""+ PartsRdpRecordCard.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', width: 100, editor:{  maxLength:500 }
		},{
			header:'作业人ID', dataIndex:'workEmpID', hidden:true, editor:{  maxLength:100 }
		},{
			header:'作业人', dataIndex:'workEmpName', width: 55, editor:{  maxLength:250 }
		},{
			header:'作业开始时间', dataIndex:'workStartTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, editor:{ xtype:'my97date' }
		},{
			header:'作业结束时间', dataIndex:'workEndTime', xtype:'datecolumn', format:'Y-m-d H:i', width: 40, editor:{ xtype:'my97date' }
		},{
			header:'质量检验', dataIndex:'qcContent', width: 55, hidden: true, editor:{  maxLength:100 }
		},{
			header:'领活人ID', dataIndex:'handleEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'领活人', dataIndex:'handleEmpName', hidden: true, width: 30, editor:{  maxLength:25 }
		},{
			header:'状态', dataIndex:'status', width: 30, align:'center', editor:{  maxLength:20 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == PARTS_RDP_STATUS_DLQ) return "待领取";				
				if (value == PARTS_RDP_STATUS_DCL) return "待处理";				
				if (value == PARTS_RDP_STATUS_XJ) return "修竣";				
				if (value == PARTS_RDP_STATUS_ZLJYZ) return "质量检验中";		
				return "错误！未知状态";
			}
		},{
			header:'回退标识', dataIndex:'isBack', hidden: false, width: 25, align:'center', editor:{ xtype:'numberfield', maxLength:1 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (IS_BACK_YES == value) return "是";
				if (IS_BACK_NO == value) return "否";
				if (Ext.isEmpty(value)) return "否";
				return "错误！未知状态";
			}
		},{
			header:'回退次数', dataIndex:'backCount', hidden: false, width: 25, align:'center', editor:{ xtype:'numberfield', maxLength:2 }
		},{
			header:'检修情况描述', dataIndex:'remarks', hidden: true
		}],
		// 重写表格的行双击事件，修改为双击弹出【检修作业工单】处理窗口
		toEditFn: function(grid, rowIndex, e){
	        var record = this.store.getAt(rowIndex);
	        
	        //可视化数据同步
	        PartsRdpRecordCard.synPartsCheckItemDataAndSavePartsRdpRecordDI();
	        
			// 显示【检修作业工单】处理窗口
	        RecordCardProcess.win.show();
	        
	    	// 调用【检修作业工单】处理窗口的初始化函数，初始化【检修作业工单】处理窗口的各个字段值
	    	RecordCardProcess.initFn(record);
	    	// 记录当前处理的【检修作业工单】的索引值，用以在处理窗口点击【上一工单】【下一工单】进行工单的快速切换
	    	RecordCardProcess.index = parseInt(rowIndex);
	    	
	        // 设置【配件编号】【配件名称】【规格型号】【扩展编号】等固定字段值
	    	RecordCardProcess.baseForm.find('name', 'partsNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsNo')[0].getValue());
	    	RecordCardProcess.baseForm.find('name', 'partsName')[0].setValue(PartsRdpProcess.baseForm.find('name', 'partsName')[0].getValue());
	    	RecordCardProcess.baseForm.find('name', 'specificationModel')[0].setValue(PartsRdpProcess.baseForm.find('name', 'specificationModel')[0].getValue());
	    	RecordCardProcess.baseForm.find('name', 'extendNo')[0].setValue(PartsRdpProcess.baseForm.find('name', 'extendNo')[0].getValue());
	    
	    	//设置检修项配件查询条件
	    	PartsRdpRecordDI.partsIDValue = PartsRdpProcess.baseForm.find('name', 'partsNo')[0].getValue();
	    }
	});
	
	// 默认按顺序号正序排序
	PartsRdpRecordCard.grid.store.setDefaultSort('seqNo', "ASC");
	
	// 【配件检验工艺工单】表格加载数据时的数据过滤
	PartsRdpRecordCard.grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpIDX = PartsRdpRecordCard.rdpIDX;				// 作业主键
		searchParams.status = PartsRdpRecordCard.status;				// 状态
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
//	PartsRdpRecordCard.grid.store.on('load', function() {
//		if (PartsRdpRecordCard.grid.store.getCount() > 0) {
//			PartsRdpRecordCard.grid.getSelectionModel().selectAll();
//		}
//	})
	
});