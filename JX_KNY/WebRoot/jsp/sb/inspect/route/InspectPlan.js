/**
 * 生成临时设备巡检计划
 */
Ext.onReady(function() {
	
	Ext.ns('InspectPlan');
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 手动启动设备巡检计划，用于临时巡检需求
	 */
	InspectPlan.startUp = function() {
		var form = InspectPlan.win.findByType('form')[0].getForm();
		if (!form.isValid()) {
			return;
		}
		var values = form.getValues();
		values.routeIdx = InspectPlan.routeIdx;

        if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/inspectPlan!startUp.action',
			jsonData : values,
			scope : InspectPlan.win,
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.hide();
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
	
	InspectPlan.win = new Ext.Window({
		title: '临时设备巡检计划',
		width: 320, height: 165,
		closeAction: 'hide',
		plain: true, modal: true,
		layout: 'fit',
		items: [{
			xtype: 'form',
			padding: 10,
			labelWidth: 100,
			baseCls: 'x-plain',
			defaults: {
				xtype: 'my97date', format: 'Y-m-d', initNow: false, width: 160, allowBlank: false
			},
			items: [{
				fieldLabel: '巡检计划名称', name: 'routeName', xtype: 'textfield' 
			}, {
				fieldLabel: '计划开始日期', id: 'planStartDate', initNow: true, vtype:'dateRange',
				dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
			}, {
				fieldLabel: '计划结束日期', id: 'planEndDate', vtype:'dateRange',
				dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '生成计划', iconCls: 'saveIcon', handler: function() {
				InspectPlan.startUp();
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			beforeshow: function() {
				// 验证是否选择了巡检目录
				var sm = InspectRoute.tree.getSelectionModel();
				var node = sm.getSelectedNode();
				if (!node) {
					MyExt.Msg.alert('请选择一条巡检目录进行临时计划生成！');
					return false;
				}
				InspectPlan.routeIdx = node.id;
				InspectPlan.routeName = node.attributes["routeName"];
				return true;
			},
			show: function() {
				this.find('name', 'routeName')[0].setValue(InspectPlan.routeName);
			}
		}
	});
	
});