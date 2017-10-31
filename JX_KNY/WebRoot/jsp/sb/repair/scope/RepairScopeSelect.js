Ext.onReady(function() {
	Ext.ns('RepairScopeSelect');
	
	/** **************** 定义全局变量开始 **************** */
	RepairScopeSelect.checked = true;		// 默认只显示同类型的检修范围
	RepairScopeSelect.searchParams = {
		repairType: REPAIR_TYPE_JX
	};
	
	RepairScopeSelect.saveFn = function(classCode, datas) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/repairScope!save.action',
			jsonData: Ext.encode(datas),
			params: {classCode: classCode},
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            RepairScopeSelect.grid.store.reload({
		            	// 添加完成后重新加载设备巡检目录表格
		            	callback: function() {
				            RepairScope.grid.store.reload();
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
	RepairScopeSelect.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/repairScope!pageQuery.action',
		pageSize: 50,
		storeAutoLoad : false,
		tbar : ['快捷查询：', {
			xtype : 'textfield', emptyText : '支持设备类别首拼快捷检索', width : 180,
			enableKeyEvents : true, listeners : {
				keyup : function( me, e ) {
					RepairScopeSelect.searchParams.classNamePY = me.getValue();
					RepairScopeSelect.grid.store.load();
				}
			}
		}, '&nbsp;&nbsp;', '-', {
			xtype: 'radio', name: 'repairType2', inputValue: REPAIR_TYPE_JX, boxLabel: '机械', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScopeSelect.searchParams.repairType = me.getRawValue();
						RepairScopeSelect.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType2', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScopeSelect.searchParams.repairType = me.getRawValue();
						RepairScopeSelect.grid.store.load();
					}
				}
			}
		}/*, {
			// Modified by hetao on 2016-07-24 暂时不处理“其它”类型的检修范围
			xtype: 'radio', name: 'repairType2', inputValue: REPAIR_TYPE_QT, boxLabel: '其它',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScopeSelect.searchParams.repairType = me.getRawValue();
						RepairScopeSelect.grid.store.load();
					}
				}
			}
		}*/, '->', {
			xtype : 'checkbox', boxLabel : '只显示同类型的设备检修范围', checked : true,
			listeners : {
				check : function( me, checked ) {
					RepairScopeSelect.checked = checked
					RepairScopeSelect.grid.store.load();
				}
			}
		}],
		fields : [ {
			dataIndex : 'idx', header : 'idx主键', hidden : true
		}, {
			dataIndex : 'classCode', header : '设备类别编码', hidden : true
		}, {
			dataIndex : 'className', header : '设备类别名称', width: 200, renderer: function(v, m, r) {
				return v + '(' + r.get('classCode') + ')';
			}
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true
		}, {
			dataIndex : 'seqNo', header : '序号', width: 50, hidden : false
		}, {
			dataIndex : 'repairType', header : '检修类型', renderer: function(v) {
				if (REPAIR_TYPE_JX == v) return '机械';
				if (REPAIR_TYPE_DQ == v) return '电气';
				if (REPAIR_TYPE_QT == v) return '其它';
				return '错误！未知类型';
			}
		}, {
			dataIndex : 'repairScopeName', header : '检修范围名称', width : 200
		}, {
			dataIndex : 'repairClassSmall', header : '小修', width : 40, align: 'center', renderer: function(v) {
				if (1 == v) return '<span style="color:red;">&radic;</span>';
				return '';
			}
		}, {
			dataIndex : 'repairClassMedium', header : '中修', width : 40, align: 'center', renderer: function(v) {
				if (1 == v) return '<span style="color:red;">&radic;</span>';
				return '';
			}
		}, {
			dataIndex : 'repairClassSubject', header : '大修', width : 40, align: 'center', renderer: function(v) {
				if (1 == v) return '<span style="color:red;">&radic;</span>';
				return '';
			}
		}, {
			dataIndex : 'remark', header : '备注', width : 300, hidden: true, editor : {
				maxLength : 200,
				xtype : 'textarea'
			}
		} ],
		// 双击进行快速新增
		toEditFn : function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			RepairScopeSelect.saveFn(RepairScopeSelect.searchParams.classCode, [record.data]);
		}
	});	
	RepairScopeSelect.grid.store.on('beforeload', function() {
		var whereList = [];
		// 查询条件
		for (var prop in RepairScopeSelect.searchParams) {
			if ('classCode' == prop) {
				whereList.push({propName : prop, propValue : RepairScopeSelect.searchParams[prop], stringLike : false, compare: Condition.NE});
				continue;
			}
			// Modified by hetao on 2016-11-01 增加设备类别名称、类别编码的查询、增加设备类别编码的显示
			if ('classNamePY' == prop) {
				var sql = "(CLASS_NAME_PY LIKE '%" + RepairScopeSelect.searchParams[prop] + "%' OR CLASS_CODE LIKE '%" + RepairScopeSelect.searchParams[prop] + "%' OR CLASS_NAME LIKE '%" + RepairScopeSelect.searchParams[prop] + "%')";
				whereList.push({compare: Condition.SQL, sql: sql});
				continue;
			}
			whereList.push({propName : prop, propValue : RepairScopeSelect.searchParams[prop]});
		}
		// 过滤已经添加了的设备检修范围
		var sql = "REPAIR_SCOPE_NAME NOT IN (SELECT REPAIR_SCOPE_NAME FROM E_REPAIR_SCOPE WHERE RECORD_STATUS = 0 AND CLASS_CODE = '" + RepairScopeSelect.searchParams.classCode + "' AND REPAIR_TYPE ='"+ RepairScopeSelect.searchParams.repairType +"')";
		whereList.push({compare: Condition.SQL, sql: sql});
		//
		if (RepairScopeSelect.checked) {
			var sql = "CLASS_CODE IN (SELECT CLASS_CODE FROM E_EQUIPMENT_CLASSES WHERE RECORD_STATUS = 0 AND PARENT_IDX  = (SELECT PARENT_IDX FROM E_EQUIPMENT_CLASSES WHERE CLASS_CODE = '" + RepairScopeSelect.searchParams.classCode + "'))";
			whereList.push({compare: Condition.SQL, sql: sql});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	/** **************** 定义巡检目录列表结束 **************** */

	RepairScopeSelect.win = new Ext.Window({
		title : '检修范围选择<span style = "color:green; font-weight:normal;">（可双击一条记录进行快速添加！）</span>',
		width : 850, height : 450,
		closeAction : 'hide',
		modal : true,
		layout : 'fit', 
		items : RepairScopeSelect.grid,
		listeners : {
			beforeshow : function(me) {
				var sm = RepairScope.tree.getSelectionModel();
				var node = sm.getSelectedNode();
				if (Ext.isEmpty(node)) {
					MyExt.Msg.alert('请先选择要进行维护的设备类别！');
					return false;
				}
				RepairScopeSelect.searchParams.classCode = node.id;
				return true;
			},
			show : function() {
				RepairScopeSelect.grid.store.load();
			}
		},
		buttonAlign : 'center',
		buttons : [{
			text : '添加', iconCls : 'addIcon', handler : function() {
				var sm = RepairScopeSelect.grid.getSelectionModel();
				var selections = sm.getSelections();
				if (0 >= selections.length) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				var datas = [];
				for (var i = 0; i < selections.length; i++) {
					datas.push(selections[i].data);
				}
				RepairScopeSelect.saveFn(RepairScopeSelect.searchParams.classCode, datas);
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});