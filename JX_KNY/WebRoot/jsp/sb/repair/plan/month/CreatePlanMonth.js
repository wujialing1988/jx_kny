/** 
 * 生成设备检修月计划 
 */
Ext.onReady(function() {
	Ext.ns('CreatePlanMonth');
	
	/**
	 * 生成月计划
	 * @param month 计划月份
	 * @param year 计划年度
	 */
	function insertMonthPlan(month, year) {
		// 提交后台进行数据存储
 		$yd.request({
 			url : ctx + '/repairPlanMonth!insertMonthPlan.action',
 			scope : VRepairPlanMonth.store,
 			params: {
 				month: month, year: year
 			}
 		},
 		// Ajax请求成功后的回调函数
 		function(result) {
 			// 生成下月计划成功后，自动查询下月的检修计划
 			var now = new Date();
 			var year = now.getFullYear();
 			var month = Ext.getCmp('id_plan_month').getValue();
 			Ext.getCmp('plan_year_combo').setValue(year);
 			Ext.getCmp('plan_month_combo').setValue(month);
 			
			// 根据查询条件选择的年度、月份重新定位VIS视窗
			var startDate = new Date(year, month - 1, 1);
			var endDate = new Date(year, month - 1, getMonthDays(month));
			if (Ext.isEmpty(month)) {
				startDate = new Date(year, 0, 1);
				endDate = new Date(year, 11, getMonthDays(11));
			}
			if (VRepairPlanMonth.timeline) VRepairPlanMonth.timeline.setWindow(startDate, endDate);
 			
 			this.load({
 				callback: function() {
 					alertSuccess('操作成功！');
 					CreatePlanMonth.win.hide();
 				}
 			});
 		});
	}
	
	/**
	 * 生成月计划窗口定义
	 */
	CreatePlanMonth.win = new Ext.Window({
		title: '生成月计划',
		height: 140, width: 320,
		plain: true,
		modal: true,
		closeAction: 'hide',
		buttonAlign: 'center',
		layout: 'fit',
		items: [{
			xtype: 'form', padding: 10,
			baseCls: 'plain',
			labelWidth: 100,
			items: [{
				width: 140,
				allowBlank: false,
				fieldLabel: '月份',
				hiddenName: 'planMonth',
				id: 'id_plan_month',
				xtype: 'combo',
				store: function() {
					var data = [];
					var year = new Date().getFullYear();
					for (var i = 1; i <= 12; i++) {
						data.push([i, i + '月']);
					}
					return new Ext.data.SimpleStore({
						fields: ['k', 'v'],
						data: data
					})
				}(),
				valueField:'k', displayField:'v',
				value: new Date().getMonth() + 1,
				editable: false,
				triggerAction:'all',
				mode:'local'
			}, {
				xtype:'label',
				html:['<span style="color:gray;font-weight:normal;">请确认选择计划月份！</span>'],
				style:'margin-top:105px;padding-left:105px;padding-top:20px;'
			}]
		}],
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				// 计划月份
				var month = Ext.getCmp('id_plan_month').getValue();
				// 计划年度
				var year = new Date().getFullYear();
				insertMonthPlan(month, year);
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});