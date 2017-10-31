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
				  xtype: 'OmEmployee_MultSelectWin', 
				  displayField:'empname', valueField: 'empid',
				  editable: false, maxLength:30, anchor: '95%'
			}
		}, {
			xtype: 'hidden', name: 'macInspectEmp', id: 'id_mac_inspect_emp'
		}, {
			xtype: 'hidden', name: 'elcInspectEmp', id: 'id_elc_inspect_emp'
		}]
	});
	
	/**
	 *  设备巡检人员保存窗口
	 */
	InspectEmp.win = new Ext.Window({
		title: '设备巡检人员',
		width: 340, height: 160,
		modal: true,
		layout: 'fit',
		items: InspectEmp.form,
		closeAction: 'hide',
		listeners: {
			beforeshow: function() {
				// 如果“机械系数”和“电气系数”都为0，则默认显示【机械维修班组】和【电气维修班组】
				var macField = {fieldLabel: '机械巡检人员', hiddenName: 'macInspectEmpid', returnField: [{widgetId: "id_mac_inspect_emp", propertyName: "empname"}]};
				var elcField = {fieldLabel: '电气巡检人员', hiddenName: 'elcInspectEmpid', returnField: [{widgetId: "id_elc_inspect_emp", propertyName: "empname"}]};
				if (Ext.isEmpty(InspectEmp.mac) && Ext.isEmpty(InspectEmp.elc)) {
					InspectEmp.form.findByType('container')[0].add(macField);
					InspectEmp.form.findByType('container')[0].add(elcField);
				} else {
					if (InspectEmp.mac) {
						InspectEmp.form.findByType('container')[0].add(macField);
					} 
					if (InspectEmp.elc) {
						InspectEmp.form.findByType('container')[0].add(elcField);
					}
				}
				InspectEmp.form.doLayout();
			},
			show: function() {
				if (null != InspectEmp.record) {
//					InspectEmp.form.getForm().loadRecord(InspectEmp.record);
					var data = InspectEmp.record.data;
					if (InspectEmp.form.find('hiddenName', 'macInspectEmpid')[0]) {
						InspectEmp.form.find('hiddenName', 'macInspectEmpid')[0].setDisplayValue(data.macInspectEmpid, data.macInspectEmp);
						Ext.getCmp('id_mac_inspect_emp').setValue(data.macInspectEmp);
					}
					if (InspectEmp.form.find('hiddenName', 'elcInspectEmpid')[0]) {
						InspectEmp.form.find('hiddenName', 'elcInspectEmpid')[0].setDisplayValue(data.elcInspectEmpid, data.elcInspectEmp);
						Ext.getCmp('id_elc_inspect_emp').setValue(data.elcInspectEmp);
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
				if (Ext.isEmpty(values.macInspectEmp) && Ext.isEmpty(values.elcInspectEmp)) {
					MyExt.Msg.alert('请选择设备巡检人员！');
					return;
				}
				$yd.request({
					scope: InspectRouteDetails.grid,
					url: ctx + '/inspectRouteDetails!saveInspectEmp.action',
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