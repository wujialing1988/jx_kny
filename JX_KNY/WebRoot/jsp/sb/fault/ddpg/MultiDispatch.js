Ext.onReady(function() {
	Ext.ns('MultiDispatch');
	
	MultiDispatch.win = new Ext.Window({
		width: 340, height: 160,
		title: '调度批量派工',
		closeAction: 'hide',
		layout: 'fit',
		items: [{
			xtype: 'form', padding: 20, baseCls: 'plain',
			items: [{
				xtype: 'container', layout: 'form',
				defaults: {},
				items: [{
						xtype: 'OmOrganizationCustom_comboTree', fieldLabel: '主修班组',
						selectNodeModel:'exceptRoot' , width: 140, editable: false,
						hiddenName: 'repairTeamId', queryHql: '[degree]tream',
						returnField: [{widgetId: "id_repair_team", propertyName: 'text'}]
					}, {
					xtype: 'OmOrganization_Win', rootId: 0, rootText: '铁路总公司', queryHql: '[degree]tream',
					displayField: 'orgname', width: 140, editable: false, valueField: 'id',
					allowBlank: true, fieldLabel: '辅修班组', hiddenName: 'assistRepairTeamId',
					returnField: [{widgetId: "id_assist_repair_team_m", propertyName: 'text'}]
				}]
			}, {
				id: 'id_repair_team_m', xtype: 'hidden', fieldLabel: '主修班组名称', name: 'repairTeam'
			}, {
				id: 'id_assist_repair_team_m', xtype: 'hidden', fieldLabel: '辅班组名称', name: 'assistRepairTeam'
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
				Dispatch.saveRepairTeam(ids, form.getValues(), function() {
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