Ext.onReady(function() {
	Ext.ns('InspectEmp');
	
	/**
	 * 设备巡检人员保存表单
	 */
	InspectEmp.form = new Ext.form.FormPanel({
		padding: 20, baseCls: 'plain',
		items: [{
			xtype: 'container', layout: 'form', 
			defaults: {
				xtype: "OmEmployee_MultSelectWin",
				maxLength:30,
				anchor: '95%'
			},
			// Modifieb by hetao on 2016-08-15 通过设备的机械、电气系数动态设置表单组件
			items: [/*{
				fieldLabel: '机械维修人员', hiddenName: 'entrustMacInspectEmpid', returnField: [{widgetId: "id_entrust_mac_inspect_emp", propertyName: "empname"}]
			}, {
				fieldLabel: '电气维修人员', hiddenName: 'entrustElcInspectEmpid', returnField: [{widgetId: "id_entrust_elc_inspect_emp", propertyName: "empname"}]
			}*/]
		}, {
			xtype: 'hidden', name: 'entrustMacInspectEmp', id: 'id_entrust_mac_inspect_emp', disabled: true
		}, {
			xtype: 'hidden', name: 'entrustElcInspectEmp', id: 'id_entrust_elc_inspect_emp', disabled: true
		}]
	});
	
	/**
	 *  设备巡检人员保存窗口
	 */
	InspectEmp.win = new Ext.Window({
		title: '委托巡检人员',
		width: 340, height: 160,
		modal: true,
		layout: 'fit',
		items: InspectEmp.form,
		closeAction: 'hide',
		listeners: {
			beforeshow: function() {
				// 如果“机械系数”和“电气系数”都为0，则默认显示【机械维修班组】和【电气维修班组】
				var macField = {fieldLabel: '机械维修人员', hiddenName: 'entrustMacInspectEmpid', returnField: [{widgetId: "id_entrust_mac_inspect_emp", propertyName: "empname"}]};
				var elcField = {fieldLabel: '电气维修人员', hiddenName: 'entrustElcInspectEmpid', returnField: [{widgetId: "id_entrust_elc_inspect_emp", propertyName: "empname"}]};
				if (Ext.isEmpty(InspectEmp.mac) && Ext.isEmpty(InspectEmp.elc)) {
					InspectEmp.form.findByType('container')[0].add(macField);
					Ext.getCmp('id_entrust_mac_inspect_emp').enable();
					InspectEmp.form.findByType('container')[0].add(elcField);
					Ext.getCmp('id_entrust_elc_inspect_emp').enable();
				} else {
					if (InspectEmp.mac) {
						InspectEmp.form.findByType('container')[0].add(macField);
						Ext.getCmp('id_entrust_mac_inspect_emp').enable();
					} 
					if (InspectEmp.elc) {
						InspectEmp.form.findByType('container')[0].add(elcField);
						Ext.getCmp('id_entrust_elc_inspect_emp').enable();
					}
				}
				InspectEmp.form.doLayout();
			},
			show: function() {
				if (null != InspectEmp.record) {
//					InspectEmp.form.getForm().loadRecord(InspectEmp.record);
					var data = InspectEmp.record.data;
					if (InspectEmp.form.find('hiddenName', 'entrustMacInspectEmpid')[0]) {
						InspectEmp.form.find('hiddenName', 'entrustMacInspectEmpid')[0].setDisplayValue(data.entrustMacInspectEmpid, data.entrustMacInspectEmp);
						Ext.getCmp('id_entrust_mac_inspect_emp').setValue(data.entrustMacInspectEmp);
					}
					if (InspectEmp.form.find('hiddenName', 'entrustElcInspectEmpid')[0]) {
						InspectEmp.form.find('hiddenName', 'entrustElcInspectEmpid')[0].setDisplayValue(data.entrustElcInspectEmpid, data.entrustElcInspectEmp);
						Ext.getCmp('id_entrust_elc_inspect_emp').enable();
						Ext.getCmp('id_entrust_elc_inspect_emp').setValue(data.entrustElcInspectEmp);
					}
				}
			},
			hide: function() {
				// 隐藏窗口时，重置表单状态，主要是重置“是否需要验收”字段
				InspectEmp.form.getForm().reset();
				InspectEmp.form.findByType('container')[0].removeAll();
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				var form = InspectEmp.form.getForm();
				if (!form.isValid()) {
					return;
				}
				var values = form.getValues();
				MyJson.deleteBlankProp(values);
				if (Ext.isEmpty(values.entrustMacInspectEmp) && Ext.isEmpty(values.entrustElcInspectEmp)) {
					MyExt.Msg.alert('请选择委托设备巡检人员！');
					return;
				}
				$yd.request({
					scope: InspectPlanEquipment.grid,
					url: ctx + '/inspectPlanEquipmentEmp!saveEntrustInspectEmp.action',
					params: {
						ids: Ext.encode(InspectEmp.ids),
						entityJson: Ext.encode(values)
					}
				}, function() {
					this.store.reload({
						callback: function() {
							alertSuccess();
							// Modified by hetao on 2017-03-10 保存成功后隐藏人员设置窗口
							InspectEmp.win.hide();
						}
					});
				});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
});