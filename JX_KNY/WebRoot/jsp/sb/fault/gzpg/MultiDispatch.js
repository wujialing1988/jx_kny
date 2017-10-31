Ext.onReady(function() {
	Ext.ns('MultiDispatch');
	
	MultiDispatch.win = new Ext.Window({
		width: 520, height: 130,
		title: '调度派工',
		closeAction: 'hide',
		layout: 'fit',
		items: [{
			xtype: 'form', style: 'padding: 15px;',
			baseCls: 'plain',
			items: [{
			    xtype: 'OmEmployee_MultSelectWin', fieldLabel: '修理人',
			    displayField:'empname', valueField: 'empid',
			    editable: false, width: 280, hiddenName: 'repairEmpId',
			    returnField :[{widgetId: "id_repair_emp_m", propertyName: "empname"}]
			}, {
				id: 'id_repair_emp_m', xtype: 'hidden', fieldLabel: '主修人名称', name: 'repairEmp'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '派工', iconCls: 'saveIcon', handler: function() {
				var form = MultiDispatch.win.findByType('form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var ids = [];
				var sm = FaultOrder.grid.getSelectionModel();
				var selections = sm.getSelections();
				for (var i = 0; i < selections.length; i++) {
					ids.push(selections[i].id);
				}
				// 保存派工信息
				Dispatch.saveRepairEmp(ids, form.getValues(), function() {
					MultiDispatch.win.hide();
				});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function() {
				if (!Dispatch.win.hidden) {
					Dispatch.win.hide();
				}
			}
		}
	});
	
});