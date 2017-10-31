Ext.onReady(function() {
	Ext.ns('QuickNotice');
	
	/** **************** 定义全局变量开始 **************** */
	QuickNotice.labelWidth = 100;
	QuickNotice.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * @param equipmentCode 设备编码
	 */
	QuickNotice.quickSubmit = function(equipmentCode) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/faultOrder!getEquipmentByCode.action',
			params: {equipmentCode: equipmentCode},
			scope : FaultOrder.grid,
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	QuickNotice.equipment = result.equipment;
		        	QuickNotice.win.show();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
					var tbar = FaultOrder.grid.getTopToolbar();
					tbar.findByType('textfield')[0].focus();
					tbar.findByType('textfield')[0].selectText();
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
		    }
		});
	}
	/**
	 * 保存故障提票
	 */
	QuickNotice.saveFn = function() {
		var form = QuickNotice.saveForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var values = form.getValues();
		MyJson.deleteBlankProp(values);

		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/faultOrder!saveOrUpdate.action',
			jsonData: values,
			scope : QuickNotice.win,
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	alertSuccess();
		        	this.hide();
		        	FaultOrder.grid.store.reload();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
		    }
		});
	
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	QuickNotice.saveForm = new Ext.form.FormPanel({
		baseCls: 'x-plain',
		labelWidth: QuickNotice.labelWidth,
		padding: 10, layout: 'column',
		defaults: {
			layout: 'form', columnWidth: .5, baseCls: 'x-plain',
			defaults: {
				xtype: 'textfield', width: QuickNotice.fieldWidth
			}
		},
		items: [{
			items: [{
				name: 'faultOrderNo', fieldLabel: '提票单号', maxLength: 20, allowBlank: false
			}, {
		    	xtype: 'OmEmployee_SelectWin', fieldLabel: '提票人',
		    	displayField:'empname', valueField: 'empid', allowBlank : false,
		    	editable: false, width: 140, hiddenName: 'submitEmpId',
		    	returnField :[{widgetId: "id_submit_emp_q", propertyName: "empname"}]
			}]
		}, {
			items: [{
				hiddenName: 'faultLevel', fieldLabel: '故障等级', maxLength: 10,
				xtype: 'combo', 
				typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			    	fields: [ 'k', 'v' ],
			        data: [[
			            FAULT_LEVEL_YB, FAULT_LEVEL_YB		// 一般
	                 ],  [
	                    FAULT_LEVEL_ZD, FAULT_LEVEL_ZD		// 重大
	                 ], [
	                    FAULT_LEVEL_TD, FAULT_LEVEL_TD		// 特大
	                ]]
			    }),
			    value: FAULT_LEVEL_YB,
			    valueField: 'k', displayField: 'v'
			}, {
				name: 'faultOccurTime', xtype: 'my97date', format: 'Y-m-d H:i', fieldLabel: '故障发现时间', allowBlank: false
			}]
		}, {
			columnWidth: 1, defaults: {
				xtype: 'textarea', width: 402
			},
			items: [{
				name: 'faultPlace', fieldLabel: '故障部位及意见', maxLength: 300
			}]
		}, {
			columnWidth: 1, defaults: {
				xtype: 'textarea', width: 402
			},
			items: [{
				name: 'faultPhenomenon', fieldLabel: '故障现象', maxLength: 300
			}, {
				name: 'causeAnalysis', fieldLabel: '原因分析', maxLength: 500, hidden: true
			}]
		}, {
			columnWidth: 1, defaults: {xtype: 'hidden'},
			items: [{
				name: 'idx', fieldLabel: 'idx主键'
			}, {
				id: 'id_equipment_idx_q', name: 'equipmentIdx', fieldLabel: '设备idx主键'
			}, {
				id: 'id_equipment_code_q', name: 'equipmentCode', fieldLabel: '设备编码'
			}, {
				id: 'id_submit_emp_q', name: 'submitEmp', fieldLabel: '提报人'
			}, {
				name: 'state', value: STATE_XJ, fieldLabel: '提票状态'
			}, {
				name: 'equipmentName', fieldLabel: '设备名称'
			}]
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	QuickNotice.win = new Ext.Window({
        title:"新增", width: 560, height: 520, plain:true, closeAction:"hide",
        buttonAlign:'center', items:QuickNotice.saveForm, autoHeight:true, modal: true,
        buttons: [{
			text : '保存', iconCls : 'saveIcon', handler : function() {
				QuickNotice.saveFn();
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function() {
				QuickNotice.saveForm.getForm().reset();
				var equipment = QuickNotice.equipment;
				QuickNotice.saveForm.findByType('OmEmployee_SelectWin')[0].setDisplayValue(empid, uname);
				QuickNotice.saveForm.find('name', 'submitEmp')[0].setValue(uname);
				// 自动生成提票单编号
				FaultOrder.createFaultOrderNo(QuickNotice.saveForm.find('name', 'faultOrderNo')[0]);
				QuickNotice.saveForm.find('name', 'equipmentName')[0].setValue(equipment.equipmentName);
				Ext.getCmp('id_equipment_idx_q').setValue(equipment.idx);
				Ext.getCmp('id_equipment_code_q').setValue(equipment.equipmentCode);
				// 设置窗口的title
				this.setTitle('故障提票&nbsp;<span style="font-weight:normal;">（' +  equipment.equipmentName + '：' + equipment.equipmentCode + '）</span>');
			},
			hide: function() {
				var tbar = FaultOrder.grid.getTopToolbar();
				tbar.findByType('textfield')[0].focus();
				tbar.findByType('textfield')[0].selectText();
			}
		}
	});
})