Ext.onReady(function() {
	Ext.ns('InspectScope');
	
	/** **************** 定义全局变量开始 **************** */
	InspectScope.searchParams = {
		repairType: REPAIR_TYPE_JX
	};
	InspectScope.seqNo = null;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 手动排序 
    InspectScope.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/inspectScope!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.success == true) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
		            this.afterDeleteFn();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	InspectScope.tree = new Ext.tree.TreePanel({
		title : '设备类别树',
		loader : new Ext.tree.TreeLoader({
			url : ctx + "/classification!findTree.action"
		}),
		root : new Ext.tree.AsyncTreeNode({
			id : "root", text : "设备类别", expanded : true
		}),
//		rootVisible: false,
		autoScroll : true,
		animate : true,
		border : false,
		listeners : {
			"beforeload" : function(node) {
				this.loader.baseParams.parentCode = node.id;
				// Modified by hetao on 2017-02-22 对于同类型不同设备存在不同巡检范围的情况，修改设备类别树可以查询末级（叶子节点）设备类别下的设备信息，用于个性化维护该设备的巡检范围
				this.loader.baseParams.findEquipment = true;
			}
		}
	});
	InspectScope.tree.getSelectionModel().on('selectionchange', function(me, node) {
		if (!node) return;
		if ('root' == node.id) {
			MyExt.Msg.alert('不可以选择根结点，请重新选择！');
			InspectScope.tree.getSelectionModel().clearSelections();
			InspectScope.grid.getTopToolbar().findByType('checkbox')[0].disable();
			InspectScope.grid.getTopToolbar().findByType('checkbox')[1].disable();
			return;
		}
		InspectScope.grid.getTopToolbar().findByType('checkbox')[0].enable();
		InspectScope.grid.getTopToolbar().findByType('checkbox')[1].enable();
		InspectScope.searchParams.classCode = node.id;
		InspectScope.grid.store.load();
	});
	/** **************** 定义设备类别树结束 **************** */
	
	/** **************** 定义巡检目录列表开始 **************** */
	InspectScope.grid = new Ext.yunda.RowEditorGrid({
		loadURL : ctx + '/inspectScope!pageQuery.action',
		saveURL : ctx + '/inspectScope!saveOrUpdate.action',
		deleteURL : ctx + '/inspectScope!logicDelete.action',
		storeAutoLoad : false, viewConfig : null,
		tbar : ['add', 'delete', 'refresh', '-', {
			text : '选择新增', iconCls : 'addIcon', handler : function() {
				InspectScopeSelect.repairType = InspectScope.searchParams.repairType;
				InspectScopeSelect.win.show();
			}
		}, '&nbsp;&nbsp;', '-', {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械', checked: true, disabled: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectScope.searchParams.repairType = me.getRawValue();
						InspectScope.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气', disabled: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectScope.searchParams.repairType = me.getRawValue();
						InspectScope.grid.store.load();
					}
				}
			}
		}, '->', {
			// Added by hetao on 2016-09-21 数据库增加字段“CLASS_NAME_PY”后，历史数据中该字段为空，所以该方法用于初始化历史数据
			xtype: 'button', text: '初始化', hidden: true, id: 'btn_init_class_name_py', handler: function() {
				$yd.request({
					url: ctx + '/inspectScope!initClassNamePY.action'
				});
			}, tooltip: '用于初始化设备类别首拼！',
			listeners: {
				render: function() {
					this.btnEl.setStyle('color', 'gray');
				}
			}
		}, '-', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				InspectScope.moveOrder(InspectScope.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				InspectScope.moveOrder(InspectScope.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				InspectScope.moveOrder(InspectScope.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				InspectScope.moveOrder(InspectScope.grid, ORDER_TYPE_BOT);
			}
		}],
		fields : [ {
			dataIndex : 'idx', header : 'idx主键', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'repairType', header : '巡检类型', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'classCode', header : '设备类别编码', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'className', header : '设备类别名称', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'seqNo', header : '顺序号', width: 50, hidden : false, editor : {
				allowBlank : false, maxLength : 2, xtype : 'numberfield', vtype : 'positiveInt',
				disabled: true, id: 'id_seq_no'
			}
		}, {
			dataIndex : 'checkItem', header : '检查项目', width : 440, editor : {
				allowBlank : false, maxLength : 100
			}
		}, {
			dataIndex : 'checkStandard', header : '检查标准', width : 540, editor : {
				maxLength : 200,
				xtype : 'textarea'
			}
		}, {
			dataIndex : 'remarks', header : '备注', width : 300, editor : {
				maxLength : 200,
				xtype : 'textarea'
			}
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true, editor : {
				xtype : 'hidden'
			}
		} ],
		beforeAddButtonFn : function() {
			var sm = InspectScope.tree.getSelectionModel();
			var node = sm.getSelectedNode();
			if (Ext.isEmpty(node)) {
				MyExt.Msg.alert('请先选择要进行维护的设备类别！');
				return false;
			}
			return true;   	
	    },
	    afterAddButtonFn: function() {
	    	var seqNo = !Ext.isEmpty(InspectScope.seqNo) ? InspectScope.seqNo : InspectScope.grid.store.getTotalCount() + 1;
	    	Ext.getCmp('id_seq_no').setValue(seqNo);
	    },
	    beforeSaveFn: function(rowEditor, changes, record, rowIndex) {
	    	if (Ext.isEmpty(record.data.classCode)) {
	    		var sm = InspectScope.tree.getSelectionModel();
	    		var node = sm.getSelectedNode();
	    		// 设置巡检标准的设备类别编码
	    		record.data.classCode = node.id;
	    		// 设置巡检标准的检修类型
	    		record.data.repairType = InspectScope.searchParams.repairType;
	    	}
	        return true;
	    },
	    cancelFn: function(rowEditor, pressed) {
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	            InspectScope.seqNo = null;
	        }
	    }
	});
	InspectScope.grid.store.on('beforeload', function() {
		// 隐藏行编辑插件
		InspectScope.grid.rowEditor.slideHide();
		var sm = InspectScope.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (Ext.isEmpty(node)) {
			MyExt.Msg.alert('请先选择要进行维护的设备类别！');
			return false;
		}
		var whereList = [];
		for (var prop in InspectScope.searchParams) {
			if ('classCode' == prop) {
				whereList.push({propName : prop, propValue : InspectScope.searchParams[prop], stringLike : false});
				continue;
			}
			whereList.push({propName : prop, propValue : InspectScope.searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});
	InspectScope.grid.store.on('load', function(me, records, options ) {
		InspectScope.seqNo = null;
		
		// 如果有记录的设备名称首拼字段没有值，则显示【初始化】按钮
		for (var i = 0; i < records.length; i++) {
			if (Ext.isEmpty(records[i].get('classNamePY'))) {
				Ext.getCmp('btn_init_class_name_py').show();
				break;
			}
		}
	});
	InspectScope.grid.getSelectionModel().on('selectionchange', function(sm) {
		if (sm.getCount() > 0 && !Ext.isEmpty(sm.getSelections()[0].get('seqNo'))) {
			InspectScope.seqNo = sm.getSelections()[0].get('seqNo');
		}
	})
	/** **************** 定义巡检目录列表结束 **************** */

	var viewport = new Ext.Viewport({
		layout : 'border',
		defaults: {layout: 'fit'},
		items : [ {
			region : 'west',
			width : 300,
			items: InspectScope.tree,
			split: true,
			// Modified by hetao on 2016-09-22 增加设备巡检标准导入功能
			bbar: ['-', {
				text: '下载导入模板', iconCls: 'page_excelIcon', handler: function() {
					var url = ctx + '/jsp/sb/inspect/scope/设备巡检标准_导入模板.xls';
					window.open(url, '_self', 'width=0,height=0');
				}
			}, {
				text: '导入巡检标准', iconCls: 'page_excelIcon', handler: function() {
					InspectScopeImport.win.show();
				}
			}]
		}, {
			region : 'center',
			items: InspectScope.grid
		}]
	});
	
});