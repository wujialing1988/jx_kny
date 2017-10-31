Ext.onReady(function() {
	Ext.ns('RepairPlanPublish');
	/** **************** 定义私有变量开始 **************** */
	var labelWidth = 100, fieldWidth = 140, form;
	/** **************** 定义私有变量结束 **************** */
	
	// 设置页面遮罩效果
	self.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	
	form = new Ext.form.FormPanel({
		padding: 20, labelWidth: labelWidth,
		baseCls: 'x-plain',
		items: [{
			xtype: 'container', layout: 'form', defaults: {
				width: fieldWidth, allowBlank: false, 
				anchor: '90%',
				xtype: 'OmOrganizationCustom_comboTree',
				displayField: 'text', queryHql: '[degree]tream',
				selectNodeModel:'leaf' , 
				width: 140, editable: false
			},
			items: []
		}, {
			fieldLabel: '是否需要验收', xtype: 'radiogroup', items: [{
				id: 'cb_col_1', boxLabel: '是', inputValue: 1, checked: true
			}, {
				id: 'cb_col_2', boxLabel: '否', inputValue: 0
			}], defaults:{
				name: 'isNeedAcceptance'
			}
		}]
	});
	
	RepairPlanPublish.win = new Ext.Window({
		title: "检修作业班组",
		width: 400,
		height: 170,
		closeAction: "hide",
		modal: true,
		layout: "fit",
		items: form,
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				var elc, mac, elcTeam, macTeam;
				var f = form.getForm();
				if (!f.isValid()) {
					return;
				}
				if (form.find('hiddenName', 'elc').length > 0) {
					elc = form.find('hiddenName', 'elc')[0];
					elcTeam = { orgId : elc.getValue(), orgName : elc.getRawValue() };
				}
				if (form.find('hiddenName', 'mac').length > 0) {
					mac = form.find('hiddenName', 'mac')[0];
					macTeam = { orgId : mac.getValue(), orgName : mac.getRawValue() }
				}
				// 是否需要验收
				var isNeedAcceptance = form.findByType('radiogroup')[0].getValue().getRawValue();
        		// 提交后台进行数据存储
        		$yd.request({
        			url : ctx + '/repairTaskList!insert4Publish.action',
        			scope : RepairPlanMonth.grid,
        			params: {
        				planMonthIdxs: Ext.encode(RepairPlanPublish.planMonthIdxs),
        				isNeedAcceptance: isNeedAcceptance,
        				elcTeam : Ext.encode(elcTeam),		// 电气检修班组
        				macTeam : Ext.encode(macTeam)		// 机械检修班组
        			}
        		},
        		// Ajax请求成功后的回调函数
        		function(result) {
        			this.store.load({
        				callback: function() {
        					alertSuccess('操作成功！');
        					// 隐藏窗口
        					RepairPlanPublish.win.hide();
        					// 显示“撤销计划”操作按钮
        					Ext.getCmp('id_rescind_btn').show();
        				}
        			});
        		});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			beforeshow: function() {
				if (Ext.isEmpty(RepairPlanPublish.planMonthIdxs)) {
					MyExt.Msg.alert('请选择要下发的月检修计划！');
					return false;
				}
				// 如果“机械系数”和“电气系数”都为0，则默认显示【机械维修班组】和【电气维修班组】
				if (Ext.isEmpty(RepairPlanPublish.mac) && Ext.isEmpty(RepairPlanPublish.elc)) {
					form.findByType('container')[0].add({ fieldLabel: '机械维修班组', hiddenName: 'mac' });
					form.findByType('container')[0].add({ fieldLabel: '电气维修班组', hiddenName: 'elc' });
				} else {
					if (RepairPlanPublish.mac) {
						form.findByType('container')[0].add({ fieldLabel: '机械维修班组', hiddenName: 'mac' });
					} 
					if (RepairPlanPublish.elc) {
						form.findByType('container')[0].add({ fieldLabel: '电气维修班组', hiddenName: 'elc' });
					}
				}
				form.doLayout();
			},
			show: function() {
				// 设置默认的设备检修班组
				$yd.request({
					url: ctx + '/repairPlanMonth!defaultRepairRaskListTeam.action',
					jsonData: RepairPlanPublish.planMonthIdxs
				}, function(result) {
					var teamElc = result.teamElc;
					if (!Ext.isEmpty(teamElc) && 0 < form.find('hiddenName', 'elc').length) {
						form.find('hiddenName', 'elc')[0].setDisplayValue(teamElc.orgId, teamElc.orgName);
					}
					var teamMac = result.teamMac;
					if (!Ext.isEmpty(teamMac) && 0 < form.find('hiddenName', 'mac').length) {
						form.find('hiddenName', 'mac')[0].setDisplayValue(teamMac.orgId, teamMac.orgName);
					}
				});
				// 小修的默认不需要验收
				if (1 == RepairPlanPublish.repairClass) {
					form.findByType('radiogroup')[0].setValue('cb_col_2', true)
				}
			},
			hide: function() {
				// 隐藏窗口时，重置表单状态，主要是重置“是否需要验收”字段
				form.getForm().reset();
				form.findByType('container')[0].removeAll();
			}
		}
	});
});