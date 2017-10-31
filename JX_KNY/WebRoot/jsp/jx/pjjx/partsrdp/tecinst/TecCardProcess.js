/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('TecCardProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	TecCardProcess.labelWidth = 100;
	TecCardProcess.fieldWidth = 140;
	TecCardProcess.index = -1;				// 用以记录当前正在处理的【检修工艺工单】索引
	TecCardProcess.idx = "####";				// 用以记录当前正在处理的【检修工艺工单】主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	// 页面上“其他处理人员”字段不显示当前用户
	// 格式化人员名称字段（删除当前用户名称）
	TecCardProcess.baseFormatWorkEmpName = function(workEmpName) {
		if(Ext.isEmpty(workEmpName)) {
			return "";
		}
		// 最后一个分号的位置索引
		var index = workEmpName.lastIndexOf(";");
		if (index < 0) {
			return "";
		}
		return workEmpName.substring(0, index)
	}
	
	// 设置【检修工艺工单处理】窗口的表单数据
	TecCardProcess.initFn = function(record) {
    	// 设置【工单编号】、【工单描述】字段值
        TecCardProcess.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        TecCardProcess.saveForm.getForm().loadRecord(record);
        
        // 待处理的工单，如果【完工时间】为空，则设置完工时间为当前系统时间
        if (PARTS_RDP_STATUS_DCL == record.get('status')) {
        	var workEndTime = TecCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
        	if ("" == workEndTime || null == workEndTime || undefined ==  workEndTime) {
        		TecCardProcess.saveForm.find('name', 'workEndTime')[0].setValue(new Date().format('Y-m-d H:i'));
        	}
        }
    	
        // 重置【其他处理人员】控件
    	Ext.getCmp('workerEmpName_TecCard').clearValue();
		// 设置【其他处理人员】的字段值
    	var workEmpName = TecCardProcess.baseFormatWorkEmpName(record.get('workEmpName'));
    	Ext.getCmp('workerEmpName_TecCard').setDisplayValue(workEmpName, workEmpName);
    	
    	// 设置当前正在处理的【检修工艺工单】主键
    	TecCardProcess.idx = record.get('idx');
    	
    	var rdpIDX = record.get('rdpIDX');						// 作业主键
    	var rdpNodeIDX = record.get('rdpNodeIDX');				// 作业节点主键
    	var tecCardIDX = record.get('tecCardIDX');				// 工艺卡主键
    	
    	// 设置【作业任务】表格加载数据的过滤参数（PartsRdpTecWS.js）
//    	PartsRdpTecWS.rdpIDX = rdpIDX;							// 作业主键
//		PartsRdpTecWS.rdpNodeIDX = rdpNodeIDX;					// 作业节点主键
		PartsRdpTecWS.rdpTecCardIDX = TecCardProcess.idx;		// 工艺工单主键
		PartsRdpTecWS.wsParentIDX = "ROOT_0";
		
		PartsRdpTecWS.grid.store.load();
		PartsRdpTecWS.tree.root.setText(record.get('tecCardNo'));
        PartsRdpTecWS.tree.root.reload();
        PartsRdpTecWS.tree.getRootNode().expand();
		
    	// 设置【物料消耗】表格加载数据的过滤参数（PartsRdpExpendMat.js）
    	PartsRdpExpendMat.rdpIDX = rdpIDX;						// 作业主键
		PartsRdpExpendMat.rdpTecCardIDX = TecCardProcess.idx;	// 工艺单主键
		PartsRdpExpendMat.tecCardIDX = tecCardIDX;				// 工艺卡主键
		PartsRdpExpendMat.grid.store.load();
		
		// 根据记录状态激活（禁用）相应的功能处理按钮
    	if (PARTS_RDP_STATUS_DLQ == record.get('status')) {
    		TecCardProcess.initByStatus_DLQ_Fn();
    	} else if (PARTS_RDP_STATUS_DCL == record.get('status')) {
    		TecCardProcess.initByStatus_DCL_Fn();
    	} else {
    		TecCardProcess.initByStatus_YCL_Fn();
    	}
	}
	
	// “待领取”状态的功能启用函数
	TecCardProcess.initByStatus_DLQ_Fn = function() {
		Ext.getCmp('lhBtn_TecCard').show();				// 显示【领活】按钮
		Ext.getCmp('xhBtn_TecCard').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_TecCard').hide();				// 隐藏【暂存】按钮
		// 
		TecCardProcess.saveForm.find('name', 'workStartTime')[0].disable();
		TecCardProcess.saveForm.find('name', 'workStartTime')[0].clearInvalid();
		TecCardProcess.saveForm.find('name', 'workEndTime')[0].disable();
		TecCardProcess.saveForm.find('name', 'workEndTime')[0].clearInvalid();
		Ext.getCmp('workerEmpName_TecCard').disable();
		// 隐藏【作业任务】tab的“批量处理”功能按钮
		PartsRdpTecWS.grid.getTopToolbar().get(3).setVisible(false);
		// 禁用【物料消耗】tab的“新增”，“删除”功能按钮
		PartsRdpExpendMat.grid.getTopToolbar().get(0).disable();
		PartsRdpExpendMat.grid.getTopToolbar().get(1).disable();
		// “待领取”状态的工单，默认加载“未处理”的作业任务
		PartsRdpTecWS.grid.getTopToolbar().get(1).setValue(TEC_WP_STATUS_WCL);
		PartsRdpTecWS.grid.store.load();
	}
	
	// “待处理”状态的功能启用函数
	TecCardProcess.initByStatus_DCL_Fn = function() {
		Ext.getCmp('lhBtn_TecCard').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_TecCard').show();				// 显示【销活】按钮
		Ext.getCmp('zcBtn_TecCard').show();				// 显示【暂存】按钮
		// 
		TecCardProcess.saveForm.find('name', 'workStartTime')[0].enable();
		TecCardProcess.saveForm.find('name', 'workEndTime')[0].enable();
//		TecCardProcess.saveForm.find('name', 'workEndTime')[0].setValue(new Date().format('Y-m-d H:i'));
		Ext.getCmp('workerEmpName_TecCard').enable();
		// 隐藏【作业任务】tab的“批量处理”功能按钮
		PartsRdpTecWS.grid.getTopToolbar().get(3).setVisible(true);
		// 启用【物料消耗】tab的“新增”，“删除”功能按钮
		PartsRdpExpendMat.grid.getTopToolbar().get(0).enable();
		PartsRdpExpendMat.grid.getTopToolbar().get(1).enable();
	}
	
	// “已处理”状态的功能启用函数
	TecCardProcess.initByStatus_YCL_Fn = function() {
		Ext.getCmp('lhBtn_TecCard').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_TecCard').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_TecCard').hide();				// 隐藏【暂存】按钮
		// 
		TecCardProcess.saveForm.find('name', 'workStartTime')[0].disable();
		TecCardProcess.saveForm.find('name', 'workEndTime')[0].disable();
		Ext.getCmp('workerEmpName_TecCard').disable();
		// 隐藏【作业任务】tab的“批量处理”功能按钮
		PartsRdpTecWS.grid.getTopToolbar().get(3).setVisible(false);
		// 禁用【物料消耗】tab的“新增”，“删除”功能按钮
		PartsRdpExpendMat.grid.getTopToolbar().get(0).disable();
		PartsRdpExpendMat.grid.getTopToolbar().get(1).disable();
		// “已处理”状态的工单，默认加载“已处理”的作业任务
		PartsRdpTecWS.grid.getTopToolbar().get(1).setValue(TEC_WP_STATUS_YCL);
		PartsRdpTecWS.grid.store.load();
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	TecCardProcess.previousTecCardFn = function() {
		if (TecCardProcess.index <= 0) {
			return false;
		} 
		TecCardProcess.index = parseInt(TecCardProcess.index) - 1;
		var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
		TecCardProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	TecCardProcess.nextTecCardFn = function() {
		if (TecCardProcess.index == PartsRdpTecCard.grid.store.getCount() - 1) {
			return false;
		}
		TecCardProcess.index = parseInt(TecCardProcess.index) + 1;
		var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
		TecCardProcess.initFn(record);
		return true;
	}
	
	// 【领活】按钮触发的函数处理
	TecCardProcess.receiveJobFn = function() {
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpTecCard.grid,
		        	url: ctx + '/partsRdpTecCard!receiveJob.action',
					params: {id: TecCardProcess.idx},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 领活成功后的页面页面操作功能启用
				            TecCardProcess.initByStatus_DCL_Fn();
				            
				            var workStartTime = new Date(result.entity.workStartTime).format('Y-m-d H:i');
				            TecCardProcess.saveForm.find('name', 'workStartTime')[0].setValue(workStartTime);
				            TecCardProcess.saveForm.find('name', 'workEndTime')[0].setValue(workStartTime);
				            // 设置【检修工艺工单表格】相应记录的状态为“待处理”
				            var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
				            record.data.status = PARTS_RDP_STATUS_DCL;
				            record.data.workStartTime = workStartTime;
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    }
		        }));
	        }
	    }); 
	}
	
	// 【暂存】按钮触发的函数处理
	TecCardProcess.saveTemporaryFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = TecCardProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = TecCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		
		var form = TecCardProcess.saveForm.getForm();
//		if (!form.isValid()) {
//			return
//		}
		var entityJson = form.getValues();
		entityJson.idx = TecCardProcess.idx;
    	self.loadMask.show();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: PartsRdpTecCard.grid,
        	url: ctx + '/partsRdpTecCard!saveTemporary.action',
			params: {entityJson: Ext.util.JSON.encode(entityJson)},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            
		            /** 页面暂存信息开始 */
		            var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
		            // 开始时间
		            if (null != result.entity.workStartTime) {
			            var workStartTime = new Date(result.entity.workStartTime).format('Y-m-d H:i');
			            record.data.workStartTime = workStartTime;
		            } else {
		            	record.data.workStartTime = null;
		            }
		            // 结束时间
		            if (null != result.entity.workEndTime) {
			            var workEndTime = new Date(result.entity.workEndTime).format('Y-m-d H:i');
			            record.data.workEndTime = workEndTime;
		            } else {
		            	record.data.workEndTime = null;
		            }
		            // 设置【检修工艺工单表格】相应记录的状态为“待处理”
		            record.data.workEmpID = result.entity.workEmpID;
		            record.data.workEmpName = result.entity.workEmpName;
		            /** 页面暂存信息结束 */
		            
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
	}
	// 【销活】按钮触发的函数处理
	TecCardProcess.finishJobFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = TecCardProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = TecCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		
		var form = TecCardProcess.saveForm.getForm();
		if (!form.isValid()) {
			return
		}
		var entityJson = form.getValues();
		entityJson.idx = TecCardProcess.idx;
		Ext.Msg.confirm("提示  ", "是否确认销活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpTecCard.grid,
		        	url: ctx + '/partsRdpTecCard!finishJob.action',
					params: {entityJson: Ext.util.JSON.encode(entityJson)},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            
				            /** 页面暂存信息开始 */
				            
				            // 工单状态
				            var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
				            record.data.status = PARTS_RDP_STATUS_XJ;
				            // 开始时间
				            if (null != result.entity.workEndTime) {
					            var workEndTime = new Date(result.entity.workEndTime).format('Y-m-d H:i');
					            record.data.workEndTime = workEndTime;
				            } else {
				            	record.data.workEndTime = null;
				            }
				            // 结束时间
				            if (null != result.entity.workStartTime) {
					            var workStartTime = new Date(result.entity.workStartTime).format('Y-m-d H:i');
					            record.data.workStartTime = workStartTime;
				            } else {
				            	record.data.workStartTime = null;
				            }
				            // 设置【检修工艺工单表格】相应记录的状态为“待处理”
				            record.data.workEmpID = result.entity.workEmpID;
				            record.data.workEmpName = result.entity.workEmpName;
				            /** 页面暂存信息结束 */
				            
				            if (!TecCardProcess.nextTecCardFn()) {
				            	TecCardProcess.win.hide();
				            }
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    }
		        }));
	        }
	    }); 
	}
	/** ************** 定义全函数量结束 ************** */

	// 【检修工艺工单处理】窗口 - 【检修工艺工单】基本信息表单
	TecCardProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:TecCardProcess.labelWidth,
		border:false, baseCls:'x-plain',
		labelAlign:"left",
		layout:"column", 
		bodyStyle:'padding: 0 10px;',
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.2,
			defaults:{
				style: 'border:none;background:none', xtype:"textfield", anchor:'100%', readOnly: true
			}
		},
		items:[{
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}]
		}, {
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}]
		}, {
			columnWidth:0.4,
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}]
		}, {
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}]
		}, {
			items:[{
				fieldLabel:"工单编号", name:'tecCardNo'
			}]
		}, {
			columnWidth:0.6,
			items:[{
				fieldLabel:"工单描述", name:'tecCardDesc'
			}]
		}]
							
	});
	
	// 【检修工艺工单处理】窗口 - 【检修工艺工单】保存表单
	TecCardProcess.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 5 10px;', labelWidth:TecCardProcess.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25,
			defaults: {
				xtype:"my97date", format:'Y-m-d H:i', allowBlank: false, width:TecCardProcess.fieldWidth
			}
		},
		items:[{
			items:[{
				name:'workStartTime', fieldLabel:"开工时间"
			}]
		}, {
			items:[{
				name:'workEndTime', fieldLabel:"完工时间"
			}]
		}, {
			columnWidth:0.5,
			items:[{
			  	rootText: '其他处理人员', fieldLabel: '其他处理人员', 
				xtype: 'Base_multyComboTree', id: 'workerEmpName_TecCard', 
				business: 'partsRdpWorker', 
			  	hiddenName: 'workEmpName', selectNodeModel: 'all',
			  	returnField: [{widgetId:"workEmpID_TecCard",propertyName:"id"}], 
			  	valueField:'text', displayField:'text', 
			  	queryParams:{workEmpID: empid},
			  	allowBlank: true,
			  	anchor:'80%'
			}, {
				id:'workEmpID_TecCard', name:'workEmpID', xtype:'hidden', fieldLabel:'其他处理人员'
			}]
		}]
	})
	
	// 【检修工艺工单处理】窗口
	TecCardProcess.win = new Ext.Window({
		title:"检修工艺工单处理",
		width:1200, height:700, layout:"fit",
		closeAction:'hide',
		plain: true, modal: true, maximized: true,
		items:[{
			xtype:"panel",
			border: false,
			layout:"border",
			tbar:[ {
				text:'领活', id:'lhBtn_TecCard',iconCls:'startIcon', handler: TecCardProcess.receiveJobFn
			},{
				text:'销活', id:'xhBtn_TecCard', iconCls:'checkIcon', handler: TecCardProcess.finishJobFn
			}, {
				text:'暂存', id:'zcBtn_TecCard', iconCls:'saveIcon', handler:TecCardProcess.saveTemporaryFn
			}, {
				text:'关闭', iconCls:'closeIcon', handler:function(){
					this.findParentByType('window').hide();
				}
			}, '->', {
				text:'上一工单', iconCls:'moveUpIcon', handler: function(){
					if (!TecCardProcess.previousTecCardFn()) {
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!TecCardProcess.nextTecCardFn()) {
						MyExt.Msg.alert('已经是最后一条工单');
					}
				}
			}],
			items:[{
				xtype:"container", autoEl:"div", region:"north", height:150, layout:"border",
				defaults:{layout:'fit'},
				items:[{
					title:"工艺工单基本信息",
					region:"north",
					height:95,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[TecCardProcess.baseForm]
				}, {
					region:"center",  
					frame:true,
					items:TecCardProcess.saveForm
				}]
			}, {
				xtype:"tabpanel", activeTab:0, region:"center",
				defaults: {xtype:"panel", layout:'fit'},
				items:[{
					title:"作业任务", layout:'border', 
					items:[{
						title : '<span style="font-weight:normal">工序树</span>',
				        iconCls : 'icon-expand-all',
				        tools : [ {
				            id : 'refresh',
				            handler: function() {
				            	PartsRdpTecWS.tree.root.reload();
				            }
				        }],
				        collapsible:true,
						region:'west', layout:'fit', width: 200,
						items:[PartsRdpTecWS.tree]
					}, {
						region:'center', layout:'fit',
						border:false,
						items:[PartsRdpTecWS.grid]
					}]
				}, {
					title:"物料消耗", items:PartsRdpExpendMat.grid
				}]
			}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpTecCard.grid.store.reload();
			}
		}
	})
})