Ext.onReady(function() {
	
	Ext.ns('Dispatch')
	
	// 机车检修作业计划基本信息显示模板
	Dispatch.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td><span>提票单号：</span>{faultOrderNo}</td>','<td><span>故障发现时间：</span>{faultOccurTime}</td>',
			'</tr>',
			'<tr>',
			'<td><span>提票人：</span>{submitEmp}</td>','<td><span>故障等级：</span>{faultLevel}</td>',
			'</tr>',
			'<tr>',
			'<td><span>故障设备：</span>{equipmentName}</td>',
			'</tr>',
			'<tr>',
			'<td colspan="2"><span>发生地点及部位：</span>{faultPlace}</td>',
			'</tr>',
			'<tr>',
			'<td colspan="2"><span>故障现象：</span>{faultPhenomenon}</td>',
			'</tr>',
		'</table>'
	);
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 保存调度派工信息
	 * @param ids 故障提票idx主键数组
	 * @param 主修班组json对象{repairTeamId: "384", repairTeam: "交接组"} 
	 */
	Dispatch.saveRepairEmp = function(ids, data, callback) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/faultOrder!saveRepairEmp.action',
			jsonData: Ext.encode(data),
			params: {ids : Ext.encode(ids)},
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	alertSuccess();
		        	// 执行回调函数
		        	if (callback) callback();
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
	
	Dispatch.win = new Ext.Window({
		width: 680, height: 360,
		title: '调度派工',
		closeAction: 'hide',
		layout: 'border',
		items: [{
			frame: true,
			region: 'center',
			layout: 'fit',
//			autoScroll: true,
			items: [{
				xtype: 'fieldset', title: '故障提票信息',
				autoScroll: true,
				html: '<div id="id_inspect_route_info" class="tpl-2col-table">ddd</div>',
			}]
		}, {
			region: 'south', height: 48,
			split: true,
			xtype: 'form', style: 'padding: 10 0 0 130px;',
			baseCls: 'plain',
			items: [{
			    xtype: 'OmEmployee_MultSelectWin', fieldLabel: '修理人',
			    displayField:'empname', valueField: 'empid',
			    editable: false, width: 280, hiddenName: 'repairEmpId',
			    returnField :[{widgetId: "id_repair_emp", propertyName: "empname"}]
			}, {
				id: 'id_repair_emp', xtype: 'hidden', fieldLabel: '主修人名称', name: 'repairEmp'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '派工', iconCls: 'saveIcon', handler: function() {
				var form = Dispatch.win.findByType('form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				// 保存派工信息
				Dispatch.saveRepairEmp([Dispatch.record.id], form.getValues(), function() {
					Dispatch.win.hide();
				});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function() {
				if (!MultiDispatch.win.hidden) {
					MultiDispatch.win.hide();
				}
			}
		}
	});
	
});