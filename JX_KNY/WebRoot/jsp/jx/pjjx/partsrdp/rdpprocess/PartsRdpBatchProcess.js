/**
 * 批量销活 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpBatchProcess');			// 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpBatchProcess.noAssignWinWidth = 680;
	PartsRdpBatchProcess.noAssignWinHeight = 280;
	PartsRdpBatchProcess.assignWinWidth = 680;
	PartsRdpBatchProcess.assignWinHeight = 320;
	
	PartsRdpBatchProcess.labelWidth = 100;
	PartsRdpBatchProcess.fieldWidth = 140;
	PartsRdpBatchProcess.businessCode = '';
	PartsRdpBatchProcess.ids = [];					// 批量销活的作业工单idx主键数组
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义有指派批量销活处理窗口开始 ************** */
	// 有指派批量销活处理窗口【确认】按钮触发的函数处理
	PartsRdpBatchProcess.assignConfirmFn = function() {
		var form = Ext.getCmp('assign_processForm').getForm();
		if (!form.isValid()) {
			return;
		}
		
		var entityJson = form.getValues();
		
		// 需指派的质量检查项参与人员处理
		var qcEmps = [];				// 指派的质量检查项人员对象数组
		for (var i = 0; i < PartsRdpBatchProcess.qcItemNos.length; i++) {
			var qcEmp = {};
			var qcItemNo = PartsRdpBatchProcess.qcItemNos[i]			
			qcEmp.qcItemNo = qcItemNo;												// 质量检查项编码
			qcEmp.empName = entityJson[RecordCardProcess.nameFlag + qcItemNo];		// 质量检查人员名称
			qcEmp.empId = entityJson[RecordCardProcess.idFlag + qcItemNo];			// 质量检查人员ID
			qcEmps.push(qcEmp);
		}
		
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			scope: PartsRdpRecordCard.grid,
        	url: ctx + '/partsRdpRecordCard!finishBatchJob.action',
        	jsonData: Ext.util.JSON.encode(Ext.getCmp('assign_processForm').getForm().getValues()),
			params: {ids: PartsRdpBatchProcess.ids, entityJson: Ext.util.JSON.encode(entityJson), qcEmpJson: Ext.util.JSON.encode(qcEmps)},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
		            PartsRdpBatchProcess.assignWin.hide();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
		
	}
	// 有指派批量销活处理窗口
	PartsRdpBatchProcess.assignWin = new Ext.Window({
		title:"批量销活",
		width: PartsRdpBatchProcess.assignWinWidth,
		height: PartsRdpBatchProcess.assignWinHeight,
		layout:"border",
		closeAction: 'hide',
		plain:true, modal: true,
		defaults: {
			xtype:"form",  frame:true, padding:"0 10", labelWidth: PartsRdpBatchProcess.labelWidth
		},
		items:[{
			collapsible:true,
			region:"north",
			title:"配件检修作业单计划信息",
			layout:"column",
			height:148,
			defaults:{
				xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
				defaults:{xtype:"textfield", anchor:"100%", style: 'border:none; background:none;', readOnly: true}
			},
			items:[{
				items:[{
					fieldLabel:"配件编号", name:"partsNo"
				}, {
					fieldLabel:"配件名称", name:"partsName"
				}, {
					fieldLabel:"下车车型号", name:"trainType"
				}, {
					fieldLabel:"计划开始时间", name:"planStartTime"
				}]
			}, {
				items:[{
					fieldLabel:"扩展编号", name:"extendNo"
				}, {
					fieldLabel:"规格型号", name:"specificationModel"
				}, {
					fieldLabel:"下车修程", name:"repair"
				}, {
					fieldLabel:"计划结束时间", name:"planEndTime"
				}]
			}]
		}, {
			id:'assign_processForm',
			padding:"10",
			region:"center",
			items:[{
			  	rootText: '其他处理人员', fieldLabel: '其他处理人员', 
				xtype: 'Base_multyComboTree', id: 'workerEmpName_assign', 
				business: 'partsRdpWorker', 
			  	hiddenName: 'workEmpName', selectNodeModel: 'all',
			  	returnField: [{widgetId:"workEmpID_assign",propertyName:"id"}], 
			  	valueField:'text', displayField:'text', 
			  	queryParams:{workEmpID: empid},
			  	allowBlank: true,
			  	anchor:'80%'
			}, {
				id:'workEmpID_assign', name:'workEmpID', xtype:'hidden', fieldLabel:'其他处理人员'
			}, {
				// 根据质量检查项基础配置动态生成的表单组件
				xtype:'panel', id:'panel_Assign', layout:'column', defaults:{layout: 'form', columnWidth:.5, defaults: {width:PartsRdpBatchProcess.fieldWidth}},
				items: []
			}]
		}], 
		buttonAlign: 'center',
		buttons: [{
			text:'确认', handler: PartsRdpBatchProcess.assignConfirmFn
		}, {
			text:'取消', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function(window) {
				// 对【配件检修作业单计划信息】表单进行赋值
				var properties = PartsRdpProcess.baseForm.getForm().getValues()
				for (var prop in properties) {
					window.find('name', prop)[0].setValue(properties[prop]);
				}
			}
		}
	});
	/** ************** 定义有指派批量销活处理窗口结束 ************** */
	
	/** ************** 定义无指派批量销活处理窗口开始 ************** */
	// 无指派批量销活处理窗口【确认】按钮触发的函数处理
	PartsRdpBatchProcess.noAssignConfirmFn = function() {
		var url = null;
		var scope = null;
		// 如果是【检修作业工单】的批量销活
		if (BUSINESS_CODE_TEC_CARD == PartsRdpBatchProcess.businessCode) {
	        	scope = PartsRdpTecCard.grid;
	        	url = ctx + '/partsRdpTecCard!finishBatchJob.action';
	        	
		// 如果是【检修记录工单】的批量销活
		} else if (BUSINESS_CODE_RECORD_CARD == PartsRdpBatchProcess.businessCode) {
	        	scope = PartsRdpRecordCard.grid;
	        	url = ctx + '/partsRdpRecordCard!finishBatchJob.action';
	        	
		// 如果是【提票工单】的批量销活
		} else if (BUSINESS_CODE_NOTICE == PartsRdpBatchProcess.businessCode) {
	        	scope = PartsRdpNotice.grid;
	        	url = ctx + '/partsRdpNotice!finishBatchJob.action';
		} 
		self.loadMask.show();
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			scope: scope,
        	url: url,
        	jsonData: Ext.util.JSON.encode(Ext.getCmp('noAssign_processForm').getForm().getValues()),
			params: {ids: PartsRdpBatchProcess.ids},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
		            PartsRdpBatchProcess.noAssignWin.hide();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
	}
	// 无指派批量销活处理窗口
	PartsRdpBatchProcess.noAssignWin = new Ext.Window({
		title:"批量销活",
		width: PartsRdpBatchProcess.noAssignWinWidth,
		height: PartsRdpBatchProcess.noAssignWinHeight,
		layout:"border",
		closeAction: 'hide',
		plain:true, modal: true,
		defaults: {
			xtype:"form",  frame:true, padding:"0 10", labelWidth: PartsRdpBatchProcess.labelWidth
		},
		items:[{
			collapsible:true,
			region:"north",
			title:"配件检修作业单计划信息",
			layout:"column",
			height:148,
			defaults:{
				xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
				defaults:{xtype:"textfield", anchor:"100%", style: 'border:none; background:none;', readOnly: true}
			},
			items:[{
				items:[{
					fieldLabel:"配件编号", name:"partsNo"
				}, {
					fieldLabel:"配件名称", name:"partsName"
				}, {
					fieldLabel:"下车车型号", name:"trainType"
				}, {
					fieldLabel:"计划开始时间", name:"planStartTime"
				}]
			}, {
				items:[{
					fieldLabel:"扩展编号", name:"extendNo"
				}, {
					fieldLabel:"规格型号", name:"specificationModel"
				}, {
					fieldLabel:"下车修程", name:"repair"
				}, {
					fieldLabel:"计划结束时间", name:"planEndTime"
				}]
			}]
		}, {
			id:'noAssign_processForm',
			padding:"10",
			region:"center",
			items:[{
			  	rootText: '其他处理人员', fieldLabel: '其他处理人员', 
				xtype: 'Base_multyComboTree', id: 'workerEmpName_noAssign', 
				business: 'partsRdpWorker', 
			  	hiddenName: 'workEmpName', selectNodeModel: 'all',
			  	returnField: [{widgetId:"workEmpID_noAssign",propertyName:"id"}], 
			  	valueField:'text', displayField:'text', 
			  	queryParams:{workEmpID: empid},
			  	allowBlank: true,
			  	anchor:'80%'
			}, {
				id:'workEmpID_noAssign', name:'workEmpID', xtype:'hidden', fieldLabel:'其他处理人员'
			}]
		}], 
		buttonAlign: 'center',
		buttons: [{
			text:'确认', handler: PartsRdpBatchProcess.noAssignConfirmFn
		}, {
			text:'取消', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function(window) {
				// 对【配件检修作业单计划信息】表单进行赋值
				var properties = PartsRdpProcess.baseForm.getForm().getValues()
				for (var prop in properties) {
					window.find('name', prop)[0].setValue(properties[prop]);
				}
			}
		}
	});
	/** ************** 定义无指派批量销活处理窗口结束 ************** */
	
});