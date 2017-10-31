/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('RecordCardProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	RecordCardProcess.labelWidth = 100;
	RecordCardProcess.fieldWidth = 140;
	RecordCardProcess.index = -1;				// 用以记录当前正在处理的【检修记录单】索引
	RecordCardProcess.idx = "####";				// 用以记录当前正在处理的【检修记录单】主键
	
	RecordCardProcess.idFlag = 'qcEmpID_';					// 人员ID前缀
	RecordCardProcess.nameFlag = 'qcEmpName_';				// 人员名称前缀
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	// 设置指派的质量检查人员信息
	RecordCardProcess.setQcEmpFn = function(idx) {
		Ext.Ajax.request({
			url: ctx + '/partsRdpQCParticipant!getModelByRdpRecordCardIDX.action',
			params: {rdpRecordCardIDX: idx},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
		        	var qcParticipantList = result.qcParticipantList;
		        	for (var j = 0; j < RecordCardProcess.qcItemNos.length; j++) {
			        	for (var i = 0; i < qcParticipantList.length; i++) {
			        		if (RecordCardProcess.qcItemNos[j] == qcParticipantList[i].qCItemNo) {
			        			var qcItemNo = RecordCardProcess.qcItemNos[j];			// 质量检查项编码
			        			var qcEmpId = qcParticipantList[i].qCEmpID;				// 质量检查人员编码
			        			var qcEmpName = qcParticipantList[i].qCEmpName;			// 质量检查人员名称
			        			
			        			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].setDisplayValue(qcEmpName, qcEmpName);
			        			RecordCardProcess.saveForm.find('name', RecordCardProcess.idFlag + qcItemNo)[0].setValue(qcEmpId);
			        		}
			        	}
		        		
		        	}
		        	
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	// 页面上“其他处理人员”字段不显示当前用户
	// 格式化人员名称字段（删除当前用户名称）
	RecordCardProcess.baseFormatWorkEmpName = function(workEmpName) {
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
	
	// 设置【检修记录单处理】窗口的表单数据
	RecordCardProcess.initFn = function(record) {
		
    	// 设置【工单编号】、【工单描述】字段值
        RecordCardProcess.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        RecordCardProcess.saveForm.getForm().reset();
        RecordCardProcess.saveForm.getForm().loadRecord(record);
        
        // 待处理的工单，如果【完工时间】为空，则设置完工时间为当前系统时间
        if (PARTS_RDP_STATUS_DCL == record.get('status')) {
        	var workEndTime = RecordCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
        	if ("" == workEndTime || null == workEndTime || undefined ==  workEndTime) {
        		RecordCardProcess.saveForm.find('name', 'workEndTime')[0].setValue(new Date().format('Y-m-d H:i'));
        	}
        }
        
        // 动态渲染需指派的质量检查人员设置组件
        // 移除动态渲染panel面板中的所有组件
        Ext.getCmp('panel_Core').removeAll();
        
        // 获取记录工单的所有质量检查项如：“互检|质检|工长检”
        var qcContent = record.get('qcContent');
        RecordCardProcess.qcItemNos = new Array();
        if (!Ext.isEmpty(qcContent)) {
	        for (var i = 0; i < objList.length; i++) {
	        	var qcItemName = objList[i].qCItemName;
	        	if (qcContent.indexOf(qcItemName) >= 0) {
	        		
	        		RecordCardProcess.qcItemNos.push(objList[i].qCItemNo);
	        		
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
					item.returnField = [{widgetId:RecordCardProcess.idFlag + objList[i].qCItemNo + "_B", propertyName:"empid"}]
					panel.items.push(item);
					
					item = {};						// 人员ID
					item.xtype = 'hidden';
					item.name = RecordCardProcess.idFlag + objList[i].qCItemNo;
					item.id = RecordCardProcess.idFlag + objList[i].qCItemNo + "_B";
					panel.items.push(item); 
					
					Ext.getCmp('panel_Core').add(panel);
					Ext.getCmp('panel_Core').doLayout();
	        	}
	        }
        }
    	
        // 重置【其他处理人员】控件
		Ext.getCmp('workerEmpName_RecordCard').queryParams.rdpIDX = record.get('rdpIDX');
    	Ext.getCmp('workerEmpName_RecordCard').clearValue();
		// 设置【其他处理人员】的字段值
    	var workEmpName = RecordCardProcess.baseFormatWorkEmpName(record.get('workEmpName'));
    	Ext.getCmp('workerEmpName_RecordCard').setDisplayValue(workEmpName, workEmpName);
    	
    	// 设置当前正在处理的【检修记录单】主键
    	RecordCardProcess.idx = record.get('idx');
    	
		// 设置指派的质量检查人员信息
		RecordCardProcess.setQcEmpFn(RecordCardProcess.idx);
    	
    	// 加载【检修检测项】表格数据
    	PartsRdpRecordRI.rdpRecordCardIDX = RecordCardProcess.idx;
    	PartsRdpRecordRI.grid.store.load();
    	
		// 根据记录状态激活（禁用）相应的功能处理按钮
    	if (PARTS_RDP_STATUS_DLQ == record.get('status')) {
    		RecordCardProcess.initByStatus_DLQ_Fn();
    	} else if (PARTS_RDP_STATUS_DCL == record.get('status')) {
    		RecordCardProcess.initByStatus_DCL_Fn();
    	} else {
    		RecordCardProcess.initByStatus_YCL_Fn();
    	}
    	
	}
	
	// “待领取”状态的功能启用函数
	RecordCardProcess.initByStatus_DLQ_Fn = function() {
		Ext.getCmp('lhBtn_RecordCard').show();				// 显示【领活】按钮
		Ext.getCmp('xhBtn_RecordCard').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_RecordCard').hide();				// 隐藏【暂存】按钮
		// 
		RecordCardProcess.saveForm.find('name', 'workStartTime')[0].disable();
		RecordCardProcess.saveForm.find('name', 'workStartTime')[0].clearInvalid();
		RecordCardProcess.saveForm.find('name', 'workEndTime')[0].disable();
		RecordCardProcess.saveForm.find('name', 'workEndTime')[0].clearInvalid();
		RecordCardProcess.saveForm.find('name', 'remarks')[0].disable();
		// 禁用需指派的质量检查项人员组件
		for (var i = 0; i < RecordCardProcess.qcItemNos.length; i++) {
			var qcItemNo = RecordCardProcess.qcItemNos[i];
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].disable();
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].clearInvalid();
		}
		Ext.getCmp('workerEmpName_RecordCard').disable();
		
		// 禁用检修检测项和数据项的处理
		PartsRdpRecordRI.form.find('name', 'repairResult')[0].disable();					// 启用“检修检测项结果”
		PartsRdpRecordRI.form.find('name', 'remarks')[0].disable();							// 启用“备注”
		Ext.getCmp('btn_tj').disable();														// 启用“提交”
		Ext.getCmp('btn_zc').disable();														// 启用“暂存”		
	}
	
	// “待处理”状态的功能启用函数
	RecordCardProcess.initByStatus_DCL_Fn = function() {
		Ext.getCmp('lhBtn_RecordCard').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_RecordCard').show();				// 显示【销活】按钮
		Ext.getCmp('zcBtn_RecordCard').show();				// 显示【暂存】按钮
		// 
		RecordCardProcess.saveForm.find('name', 'workStartTime')[0].enable();
		RecordCardProcess.saveForm.find('name', 'workEndTime')[0].enable();
		
		// 启用需指派的质量检查项人员组件
		// 禁用需指派的质量检查项人员组件
		for (var i = 0; i < RecordCardProcess.qcItemNos.length; i++) {
			var qcItemNo = RecordCardProcess.qcItemNos[i];
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].enable();
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].clearInvalid();
		}
		RecordCardProcess.saveForm.find('name', 'remarks')[0].enable();							// 启用“备注”
		
		Ext.getCmp('workerEmpName_RecordCard').enable();
		
		// 启用检修检测项和数据项的处理
		PartsRdpRecordRI.form.find('name', 'repairResult')[0].enable();				// 启用“检修检测项结果”
		PartsRdpRecordRI.form.find('name', 'remarks')[0].enable();							// 启用“备注”
		Ext.getCmp('btn_tj').enable();														// 启用“提交”
		Ext.getCmp('btn_zc').enable();														// 启用“暂存”	
		
		
	}
	
	// “已处理”状态的功能启用函数
	RecordCardProcess.initByStatus_YCL_Fn = function() {
		Ext.getCmp('lhBtn_RecordCard').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_RecordCard').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_RecordCard').hide();				// 隐藏【暂存】按钮
		// 
		RecordCardProcess.saveForm.find('name', 'remarks')[0].disable();
		// 禁用需指派的质量检查项人员组件
		for (var i = 0; i < RecordCardProcess.qcItemNos.length; i++) {
			var qcItemNo = RecordCardProcess.qcItemNos[i];
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].disable();
			RecordCardProcess.saveForm.find('hiddenName', RecordCardProcess.nameFlag + qcItemNo)[0].clearInvalid();
		}
		RecordCardProcess.saveForm.find('name', 'workStartTime')[0].disable();
		RecordCardProcess.saveForm.find('name', 'workEndTime')[0].disable();
		Ext.getCmp('workerEmpName_RecordCard').disable();
		
		// 禁用检修检测项和数据项的处理
		PartsRdpRecordRI.form.find('name', 'repairResult')[0].disable();				// 启用“检修检测项结果”
		PartsRdpRecordRI.form.find('name', 'remarks')[0].disable();							// 启用“备注”
		Ext.getCmp('btn_tj').disable();														// 启用“提交”
		Ext.getCmp('btn_zc').disable();														// 启用“暂存”		
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	RecordCardProcess.previousRecordCardFn = function() {
		if (RecordCardProcess.index <= 0) {
			return false;
		} 
		RecordCardProcess.index = parseInt(RecordCardProcess.index) - 1;
		var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
		RecordCardProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	RecordCardProcess.nextRecordCardFn = function() {
		if (RecordCardProcess.index == PartsRdpRecordCard.grid.store.getCount() - 1) {
			return false;
		}
		RecordCardProcess.index = parseInt(RecordCardProcess.index) + 1;
		var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
		RecordCardProcess.initFn(record);
		return true;
	}
	
	// 【领活】按钮触发的函数处理
	RecordCardProcess.receiveJobFn = function() {
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpRecordCard.grid,
		        	url: ctx + '/partsRdpRecordCard!receiveJob.action',
					params: {id: RecordCardProcess.idx},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 领活成功后的页面页面操作功能启用
				            RecordCardProcess.initByStatus_DCL_Fn();
				            
				            var workStartTime = new Date(result.entity.workStartTime).format('Y-m-d H:i');
				            RecordCardProcess.saveForm.find('name', 'workStartTime')[0].setValue(workStartTime);
				            RecordCardProcess.saveForm.find('name', 'workEndTime')[0].setValue(workStartTime);
				            RecordCardProcess.saveForm.find('name', 'status')[0].setValue(PARTS_RDP_STATUS_DCL);
				            // 设置【检修记录单表格】相应记录的状态为“待处理”
				            var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
				            record.data.status = PARTS_RDP_STATUS_DCL;
				            record.data.workStartTime = workStartTime;
		           			// 重新加载【检修检测项】表格
				            PartsRdpRecordRI.grid.store.reload();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    }
		        }));
	        }
	    }); 
	}
	
	// 【暂存】按钮触发的函数处理
	RecordCardProcess.saveTemporaryFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = RecordCardProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = RecordCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		var form = RecordCardProcess.saveForm.getForm();
		var entityJson = form.getValues();
		entityJson.idx = RecordCardProcess.idx;
		
		// 需指派的质量检查项参与人员处理
		var qcEmps = [];				// 指派的质量检查项人员对象数组
		for (var i = 0; i < RecordCardProcess.qcItemNos.length; i++) {
			var qcEmp = {};
			var qcItemNo = RecordCardProcess.qcItemNos[i]			
			qcEmp.qcItemNo = qcItemNo;												// 质量检查项编码
			qcEmp.empName = entityJson[RecordCardProcess.nameFlag + qcItemNo];		// 质量检查人员名称
			qcEmp.empId = entityJson[RecordCardProcess.idFlag + qcItemNo];			// 质量检查人员ID
			qcEmps.push(qcEmp);
		}
		
    	self.loadMask.show();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: PartsRdpRecordCard.grid,
        	url: ctx + '/partsRdpRecordCard!saveTemporary.action',
			params: {entityJson: Ext.util.JSON.encode(entityJson), qcEmpJson: Ext.util.JSON.encode(qcEmps)},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            
		            /** 页面暂存信息开始 */
		            var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
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
		            record.data.workEmpID = result.entity.workEmpID;
		            record.data.workEmpName = result.entity.workEmpName;
		            record.data.remarks = result.entity.remarks;
		            /** 页面暂存信息结束 */
		            
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
	}
	
	// 【销活】按钮触发的函数处理
	RecordCardProcess.finishJobFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = RecordCardProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = RecordCardProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		
		var form = RecordCardProcess.saveForm.getForm();
		if (!form.isValid()) {
			return
		}
		var entityJson = form.getValues();
		entityJson.idx = RecordCardProcess.idx;
		
		// 需指派的质量检查项参与人员处理
		var qcEmps = [];				// 指派的质量检查项人员对象数组
		for (var i = 0; i < RecordCardProcess.qcItemNos.length; i++) {
			var qcEmp = {};
			var qcItemNo = RecordCardProcess.qcItemNos[i]			
			qcEmp.qcItemNo = qcItemNo;												// 质量检查项编码
			qcEmp.empName = entityJson[RecordCardProcess.nameFlag + qcItemNo];		// 质量检查人员名称
			qcEmp.empId = entityJson[RecordCardProcess.idFlag + qcItemNo];			// 质量检查人员ID
			qcEmps.push(qcEmp);
		}
		
		Ext.Msg.confirm("提示", "是否确认销活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpRecordCard.grid,
		        	url: ctx + '/partsRdpRecordCard!finishJob.action',
					params: {entityJson: Ext.util.JSON.encode(entityJson), qcEmpJson: Ext.util.JSON.encode(qcEmps)},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            
				            /** 页面暂存信息开始 */
				            // 工单状态
				            var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
				            record.data.status = result.entity.status;
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
				            record.data.workEmpID = result.entity.workEmpID;
				            record.data.workEmpName = result.entity.workEmpName;
				            record.data.remarks = result.entity.remarks;
				            /** 页面暂存信息结束 */
				            
				            if (!RecordCardProcess.nextRecordCardFn()) {
				            	RecordCardProcess.win.hide();
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

	// 【检修记录单处理】窗口 - 【检修记录单】基本信息表单
	RecordCardProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:RecordCardProcess.labelWidth - 20,
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
				fieldLabel:"工单编号", name:'recordCardNo'
			}]
		}, {
			columnWidth:0.6,
			items:[{
				fieldLabel:"工单描述", name:'recordCardDesc'
			}]
		}, {
			items:[{
				fieldLabel:"质量检查", name:'qcContent'
			}]
		}]
							
	});
	
	// 【检修记录单处理】窗口 - 【检修记录单】保存表单
	RecordCardProcess.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 5 10px;', labelWidth:RecordCardProcess.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25,
			defaults: {
				width:RecordCardProcess.fieldWidth
			}
		},
		items:[{
			columnWidth:1,
			items:[{
				name:'remarks', fieldLabel:"检修情况描述", xtype:'textarea', anchor: '90%', height: 45, allowBlank: true, maxLength: 500
			}]
		}, {
			items:[{
				name:'workStartTime', xtype:"my97date", format:'Y-m-d H:i', fieldLabel:"开工时间", allowBlank: false
			}]
		}, {
			items:[{
				name:'workEndTime', xtype:"my97date", format:'Y-m-d H:i', fieldLabel:"完工时间", allowBlank: false
			}]
		}, {
			columnWidth:0.5,
			items:[{
			  	rootText: '其他处理人员', fieldLabel: '其他处理人员', 
				xtype: 'Base_multyComboTree', id: 'workerEmpName_RecordCard', 
				business: 'partsRdpWorker', 
			  	hiddenName: 'workEmpName', selectNodeModel: 'all',
			  	returnField: [{widgetId:"workEmpID_RecordCard",propertyName:"id"}], 
			  	valueField:'text', displayField:'text', 
			  	queryParams:{workEmpID: empid},
			  	anchor:'80%'
			}, {
				id:'workEmpID_RecordCard', name:'workEmpID', xtype:'hidden', fieldLabel:'其他处理人员'
			}, {
				name:'status', xtype:'hidden', fieldLabel:'状态'
			}]
		}, {
			// 根据质量检查项基础配置动态生成的表单组件
			columnWidth:1,
			xtype:'panel', id:'panel_Core', layout:'column', defaults:{layout: 'form', columnWidth:.25, defaults: {width:RecordCardProcess.fieldWidth}},
			items:RecordCardProcess.qcItems
		}]
	})
	
	// 【检修记录单处理】窗口
	RecordCardProcess.win = new Ext.Window({
		title:"检修记录单处理",
		width:1200, height:700, layout:"fit",
		closeAction:'hide',
		plain: true, modal: true, maximized: true,
		items:[{
			xtype:"panel",
			border: false,
			layout:"border",
			tbar:[ {
				text:'领活', id:'lhBtn_RecordCard',iconCls:'startIcon', handler: RecordCardProcess.receiveJobFn
			},{
				text:'销活', id:'xhBtn_RecordCard', iconCls:'checkIcon', handler: RecordCardProcess.finishJobFn
			}, {
				text:'暂存', id:'zcBtn_RecordCard', iconCls:'saveIcon', handler:RecordCardProcess.saveTemporaryFn
			}, {
				text:'关闭', iconCls:'closeIcon', handler:function(){
					this.findParentByType('window').hide();
				}
			}, '->', {
				text:'上一工单', iconCls:'moveUpIcon', handler: function(){
					if (!RecordCardProcess.previousRecordCardFn()) {
						//可视化数据同步
	        			PartsRdpRecordCard.synPartsCheckItemDataAndSavePartsRdpRecordDI();
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!RecordCardProcess.nextRecordCardFn()) {
						//可视化数据同步
	        			PartsRdpRecordCard.synPartsCheckItemDataAndSavePartsRdpRecordDI();
						MyExt.Msg.alert('已经是最后一条工单');
					}
			//可视化数据同步
	        PartsRdpRecordCard.synPartsCheckItemDataAndSavePartsRdpRecordDI();
				}
			}],
			items:[{
				xtype:"container", autoEl:"div", region:"north", height:220, layout:"border",
				defaults:{layout:'fit'},
				items:[{
					title:"记录工单基本信息",
					region:"north",
					height:95,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[RecordCardProcess.baseForm]
				}, {
					region:"center",
					frame:true,
					items:RecordCardProcess.saveForm
				}]
			}, {
				title:'检修检测项',				
				xtype:"panel",  region:"center", layout:'border',
				defaults: {xtype:"panel", layout:'fit'},
				items:[{
					region:'west',
					width: 600,
					items:[PartsRdpRecordRI.grid]
				}, {
					region:'center',
					layout:'border',
					items:[{
						region:'north',
						layout:'fit',
						height: 150,
						frame:true,
						items:[PartsRdpRecordRI.form]
					}, {
						region:'center',
						border: false,
						layout:'fit',
						items:[PartsRdpRecordDI.grid]
					}], 
					buttonAlign: 'center',
					buttons: [{
						text:'提交', id:'btn_tj', iconCls: '', handler: function() {
							PartsRdpRecordRI.submitFn(false);
						}
					}, {
						text:'暂存', id:'btn_zc', iconCls: '', handler: function() {
							PartsRdpRecordRI.submitFn(true);
						}
					}]
				}]
			}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpRecordCard.grid.store.reload();
			}
		}
	})
})