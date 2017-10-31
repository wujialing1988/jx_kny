/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('NoticeProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	NoticeProcess.labelWidth = 80;
	NoticeProcess.fieldWidth = 140;
	NoticeProcess.index = -1;				// 用以记录当前正在处理的【检修提票单】索引
	NoticeProcess.idx = "####";				// 用以记录当前正在处理的【检修提票单】主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	// 页面上“其他处理人员”字段不显示当前用户
	// 格式化人员名称字段（删除当前用户名称）
	NoticeProcess.baseFormatWorkEmpName = function(workEmpName) {
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
	
	// 设置【检修提票单处理】窗口的表单数据
	NoticeProcess.initFn = function(record) {
    	// 设置【工单编号】、【工单描述】字段值
        NoticeProcess.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        NoticeProcess.saveForm.getForm().loadRecord(record);
        
        // 待处理的工单，如果【完工时间】为空，则设置完工时间为当前系统时间
        if (PARTS_RDP_STATUS_DCL == record.get('status')) {
        	var workEndTime = NoticeProcess.saveForm.find('name', 'workEndTime')[0].getValue();
        	if ("" == workEndTime || null == workEndTime || undefined ==  workEndTime) {
        		NoticeProcess.saveForm.find('name', 'workEndTime')[0].setValue(new Date().format('Y-m-d H:i'));
        	}
        }
    	
        // 重置【其他处理人员】控件
		Ext.getCmp('workerEmpName_Notice').queryParams.rdpIDX = record.get('rdpIDX');
    	Ext.getCmp('workerEmpName_Notice').clearValue();
		// 设置【其他处理人员】的字段值
    	var workEmpName = NoticeProcess.baseFormatWorkEmpName(record.get('workEmpName'));
    	Ext.getCmp('workerEmpName_Notice').setDisplayValue(workEmpName, workEmpName);
    	
    	// 设置当前正在处理的【检修提票单】主键
    	NoticeProcess.idx = record.get('idx');
    	
    	var rdpIDX = record.get('rdpIDX');						// 作业主键
    	var rdpNodeIDX = record.get('rdpNodeIDX');				// 作业节点主键
    	
    	// 加载【检修检测项】表格数据
    	PartsRdpRecordRI.rdpNoticeIDX = NoticeProcess.idx;
    	PartsRdpRecordRI.grid.store.load();
    	
		// 根据记录状态激活（禁用）相应的功能处理按钮
    	if (PARTS_RDP_STATUS_DLQ == record.get('status')) {
    		NoticeProcess.initByStatus_DLQ_Fn();
    	} else if (PARTS_RDP_STATUS_DCL == record.get('status')) {
    		NoticeProcess.initByStatus_DCL_Fn();
    	} else {
    		NoticeProcess.initByStatus_YCL_Fn();
    	}
    	
	}
	
	// “待领取”状态的功能启用函数
	NoticeProcess.initByStatus_DLQ_Fn = function() {
		Ext.getCmp('lhBtn_Notice').show();				// 显示【领活】按钮
		Ext.getCmp('xhBtn_Notice').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_Notice').hide();				// 隐藏【暂存】按钮
		// 
		NoticeProcess.saveForm.find('name', 'workStartTime')[0].disable();
		NoticeProcess.saveForm.find('name', 'workStartTime')[0].clearInvalid();
		NoticeProcess.saveForm.find('name', 'workEndTime')[0].disable();
		NoticeProcess.saveForm.find('name', 'workEndTime')[0].clearInvalid();
		NoticeProcess.saveForm.find('name', 'solution')[0].disable();
		Ext.getCmp('workerEmpName_Notice').disable();
	}
	
	// “待处理”状态的功能启用函数
	NoticeProcess.initByStatus_DCL_Fn = function() {
		Ext.getCmp('lhBtn_Notice').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_Notice').show();				// 显示【销活】按钮
		Ext.getCmp('zcBtn_Notice').show();				// 显示【暂存】按钮
		// 
		NoticeProcess.saveForm.find('name', 'workStartTime')[0].enable();
		NoticeProcess.saveForm.find('name', 'workEndTime')[0].enable();
		NoticeProcess.saveForm.find('name', 'solution')[0].enable();							// 启用“备注”
		
		Ext.getCmp('workerEmpName_Notice').enable();
	}
	
	// “已处理”状态的功能启用函数
	NoticeProcess.initByStatus_YCL_Fn = function() {
		Ext.getCmp('lhBtn_Notice').hide();				// 隐藏【领活】按钮
		Ext.getCmp('xhBtn_Notice').hide();				// 隐藏【销活】按钮
		Ext.getCmp('zcBtn_Notice').hide();				// 隐藏【暂存】按钮
		// 
		NoticeProcess.saveForm.find('name', 'solution')[0].disable();
		NoticeProcess.saveForm.find('name', 'workStartTime')[0].disable();
		NoticeProcess.saveForm.find('name', 'workEndTime')[0].disable();
		Ext.getCmp('workerEmpName_Notice').disable();
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	NoticeProcess.previousNoticeFn = function() {
		if (NoticeProcess.index <= 0) {
			return false;
		} 
		NoticeProcess.index = parseInt(NoticeProcess.index) - 1;
		var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
		NoticeProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	NoticeProcess.nextNoticeFn = function() {
		if (NoticeProcess.index == PartsRdpNotice.grid.store.getCount() - 1) {
			return false;
		}
		NoticeProcess.index = parseInt(NoticeProcess.index) + 1;
		var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
		NoticeProcess.initFn(record);
		return true;
	}
	
	// 【领活】按钮触发的函数处理
	NoticeProcess.receiveJobFn = function() {
		Ext.Msg.confirm("提示  ", "是否确认领活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpNotice.grid,
		        	url: ctx + '/partsRdpNotice!receiveJob.action',
					params: {id: NoticeProcess.idx},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 领活成功后的页面页面操作功能启用
				            NoticeProcess.initByStatus_DCL_Fn();
				            
				            var workStartTime = new Date(result.entity.workStartTime).format('Y-m-d H:i');
				            NoticeProcess.saveForm.find('name', 'workStartTime')[0].setValue(workStartTime);
				            NoticeProcess.saveForm.find('name', 'workEndTime')[0].setValue(workStartTime);
				            NoticeProcess.saveForm.find('name', 'status')[0].setValue(PARTS_RDP_STATUS_DCL);
				            // 设置【检修提票单表格】相应记录的状态为“待处理”
				            var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
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
	NoticeProcess.saveTemporaryFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = NoticeProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = NoticeProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		
		var form = NoticeProcess.saveForm.getForm();
//		if (!form.isValid()) {
//			return
//		}
		var entityJson = form.getValues();
		entityJson.idx = NoticeProcess.idx;
    	self.loadMask.show();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: PartsRdpNotice.grid,
        	url: ctx + '/partsRdpNotice!saveTemporary.action',
			params: {entityJson: Ext.util.JSON.encode(entityJson)},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            
		            var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
		            if (null != result.entity.workEndTime) {
			            var workEndTime = new Date(result.entity.workEndTime).format('Y-m-d H:i');
//			            NoticeProcess.saveForm.find('name', 'workEndTime')[0].setValue(workEndTime);
			            record.data.workEndTime = workEndTime;
		            }
		            var workEndTime = new Date(result.entity.workEndTime).format('Y-m-d H:i');
		            // 设置【检修提票单表格】相应记录的状态为“待处理”
		            record.data.workEmpID = result.entity.workEmpID;
		            record.data.workEmpName = result.entity.workEmpName;
		            record.data.solution = result.entity.solution;
		            
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
	}
	
	// 【销活】按钮触发的函数处理
	NoticeProcess.finishJobFn = function(){
		// 验证“开工时间”不能大于“完工时间”
		var startDateValue = NoticeProcess.saveForm.find('name', 'workStartTime')[0].getValue();
		var endDateValue = NoticeProcess.saveForm.find('name', 'workEndTime')[0].getValue();
		
		var startDate =new Date(startDateValue);
		var endDate = new Date(endDateValue);
		if (endDate != null && startDate > endDate) {
			MyExt.Msg.alert("开工时间不能大于完工时间！");
			return;
		}
		
		
		var form = NoticeProcess.saveForm.getForm();
		if (!form.isValid()) {
			return
		}
		var entityJson = form.getValues();
		entityJson.idx = NoticeProcess.idx;
		Ext.Msg.confirm("提示", "是否确认销活？", function(btn){
	        if(btn == 'yes') {
	        	self.loadMask.show();
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpNotice.grid,
		        	url: ctx + '/partsRdpNotice!finishJob.action',
					params: {entityJson: Ext.util.JSON.encode(entityJson)},
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            if (!NoticeProcess.nextNoticeFn()) {
				            	NoticeProcess.win.hide();
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

	// 【检修提票单处理】窗口 - 【检修提票单】基本信息表单
	NoticeProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:NoticeProcess.labelWidth,
		border:false, baseCls:'x-plain',
		labelAlign:"left",
		layout:"column", 
		bodyStyle:'padding: 0 10px;',
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.3,
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
				fieldLabel:"提票单编号", name:'noticeNo'
			}]
		}, {
			items:[{
				fieldLabel:"提报人", name:'noticeEmpName'
			}]
		}, {
			columnWidth:1,
			items:[{
				fieldLabel:"问题描述", name:'noticeDesc'
			}]
		}]
							
	});
	
	// 【检修提票单处理】窗口 - 【检修提票单】保存表单
	NoticeProcess.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 10px 10px;', labelWidth:NoticeProcess.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: {
				width:NoticeProcess.fieldWidth
			}
		},
		items:[{
			columnWidth:1,
			items:[{
				name:'solution', fieldLabel:"处理结果描述", xtype:'textarea', anchor: '90%', height: 65, allowBlank: true, maxLength: 500
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
			columnWidth:1,
			items:[{
			  	rootText: '其他处理人员', fieldLabel: '其他处理人员', 
				xtype: 'Base_multyComboTree', id: 'workerEmpName_Notice', 
				business: 'partsRdpWorker', 
			  	hiddenName: 'workEmpName', selectNodeModel: 'all',
			  	returnField: [{widgetId:"workEmpID_Notice",propertyName:"id"}], 
			  	valueField:'text', displayField:'text', 
			  	queryParams:{workEmpID: empid},
			  	anchor:'60%'
			}, {
				id:'workEmpID_Notice', name:'workEmpID', xtype:'hidden', fieldLabel:'其他处理人员'
			}, {
				name:'status', xtype:'hidden', fieldLabel:'状态'
			}]
		}]
	})
	
	// 【检修提票单处理】窗口
	NoticeProcess.win = new Ext.Window({
		title:"检修提票单处理",
		width:900, height:400, layout:"fit",
		closeAction:'hide',
		plain: true, modal: true,
		items:[{
			xtype:"panel",
			border: false,
			layout:"border",
			tbar:[ {
				text:'领活', id:'lhBtn_Notice',iconCls:'startIcon', handler: NoticeProcess.receiveJobFn
			},{
				text:'销活', id:'xhBtn_Notice', iconCls:'checkIcon', handler: NoticeProcess.finishJobFn
			}, {
				text:'暂存', id:'zcBtn_Notice', iconCls:'saveIcon', handler:NoticeProcess.saveTemporaryFn
			}, {
				text:'关闭', iconCls:'closeIcon', handler:function(){
					this.findParentByType('window').hide();
				}
			}, '->', {
				text:'上一工单', iconCls:'moveUpIcon', handler: function(){
					if (!NoticeProcess.previousNoticeFn()) {
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!NoticeProcess.nextNoticeFn()) {
						MyExt.Msg.alert('已经是最后一条工单');
					}
				}
			}],
			defaults:{layout:'fit'},
			items:[
				{
					title:"提票工单基本信息",
					region:"north",
					height:125,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[NoticeProcess.baseForm]
				}, {
					region:"center", height:185, 
					frame: true,
					items:NoticeProcess.saveForm
				}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpNotice.grid.store.reload();
			}
		}
	})
})