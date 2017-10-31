Ext.onReady(function() {
	Ext.ns('InspectScopeSelect');
	
	/** **************** 定义全局变量开始 **************** */
	InspectScopeSelect.checked = true;		// 默认只显示同类型的检查项目
	
	/**
	 * 保存选择新增的设备巡检标准
	 * @param classCode 设备类别编码
	 * @param datas 选择新增的设备巡检标准json对象数组
	 */
	InspectScopeSelect.saveFn = function(classCode, datas) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/inspectScope!save.action',
			jsonData: Ext.encode(datas),
			params: {classCode: classCode, repairType: InspectScopeSelect.repairType},
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            InspectScopeSelect.grid.store.reload({
		            	// 添加完成后重新加载设备巡检目录表格
		            	callback: function() {
				            InspectScope.grid.store.reload();
		            	}
		            });
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
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义巡检目录列表开始 **************** */
	InspectScopeSelect.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/inspectScope!pageSelectQuery.action',
		pageSize: 50,
		storeAutoLoad : false,
		tbar : ['快捷查询：', {
			xtype : 'textfield', emptyText : '支持设备类别首拼快捷检索', width : 180,
			enableKeyEvents : true, listeners : {
				keyup : function( me, e ) {
					InspectScopeSelect.classNamePY = me.getValue();
					InspectScopeSelect.grid.store.load();
				}
			}
		}, '&nbsp;&nbsp;', '-', {
			xtype: 'radio', name: 'repairType2', inputValue: REPAIR_TYPE_JX, boxLabel: '机械', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectScopeSelect.repairType = me.getRawValue();
						InspectScopeSelect.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType2', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectScopeSelect.repairType = me.getRawValue();
						InspectScopeSelect.grid.store.load();
					}
				}
			}
		}, '->', {
			xtype : 'checkbox', boxLabel : '只显示同类型的设备检查项目', checked : true,
			listeners : {
				check : function( me, checked ) {
					InspectScopeSelect.checked = checked
					InspectScopeSelect.grid.store.load();
				}
			}
		}],
		fields : [ {
			dataIndex : 'seqNo', header : '顺序号', hidden : true
		}, {
			dataIndex : 'classCode', header : '设备类别编码', hidden : true
		}, {
			dataIndex : 'className', header : '设备类别名称', width: 200, renderer: function(v, m, r) {
				return v + '(' + r.get('classCode') + ')';
			}
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true
		}, {
			dataIndex : 'checkItem', header : '检查项目', width : 280
		}, {
			dataIndex : 'checkItemPY', header : '检查项目拼音', width : 240, hidden : true
		}, {
			dataIndex : 'checkStandard', header : '检查标准', width : 220
		}, {
			dataIndex : 'remarks', header : '备注', width : 300, hidden : true
		}],
		// 双击进行快速新增
		toEditFn : function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			InspectScopeSelect.saveFn(InspectScopeSelect.classCode, [record.data]);
		}
	});
	InspectScopeSelect.grid.store.on('beforeload', function() {
		this.baseParams.checked = InspectScopeSelect.checked;
		this.baseParams.entityJson = Ext.encode({
			classCode : InspectScopeSelect.classCode,
			classNamePY : InspectScopeSelect.classNamePY,
			repairType : InspectScopeSelect.repairType
		});
	});
	/** **************** 定义巡检目录列表结束 **************** */

	InspectScopeSelect.win = new Ext.Window({
		title : '巡检标准选择<span style = "color:green; font-weight:normal;">（可双击一条记录进行快速添加！）</span>',
		width : 750, height : 450,
		closeAction : 'hide',
		modal : true,
		layout : 'fit', 
		items : InspectScopeSelect.grid,
		listeners : {
			beforeshow : function(me) {
				var sm = InspectScope.tree.getSelectionModel();
				var node = sm.getSelectedNode();
				if (Ext.isEmpty(node)) {
					MyExt.Msg.alert('请先选择要进行维护的设备类别！');
					return false;
				}
				InspectScopeSelect.classCode = node.id;
				// 隐藏行编辑插件
				InspectScope.grid.rowEditor.slideHide();
				return true;
			},
			show : function() {
				var tbar = InspectScopeSelect.grid.getTopToolbar();
				tbar.find('inputValue', InspectScopeSelect.repairType)[0].setValue(InspectScopeSelect.repairType);
				InspectScopeSelect.grid.store.load();
			}
		},
		buttonAlign : 'center',
		buttons : [{
			text : '添加', iconCls : 'addIcon', handler : function() {
				var sm = InspectScopeSelect.grid.getSelectionModel();
				var selections = sm.getSelections();
				if (0 >= selections.length) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				var datas = [];
				for (var i = 0; i < selections.length; i++) {
					datas.push(selections[i].data);
				}
				InspectScopeSelect.saveFn(InspectScopeSelect.classCode, datas);
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});