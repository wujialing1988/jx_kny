Ext.onReady(function(){
	Ext.ns('PointCheckScope');
	
	/** **************** 定义全局变量开始 **************** */
	PointCheckScope.searchParams = {};
	PointCheckScope.seqNo = null;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 手动排序 
    PointCheckScope.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/pointCheckScope!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.success == true) {        // 操作成功 
		            alertSuccess();
		            this.store.reload(); 
		            this.afterDeleteFn();
		        } else {                            // 操作失败                    
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	PointCheckScope.tree = new Ext.tree.TreePanel({
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
				// Modified by hetao on 2017-02-24 对于同类型不同设备存在不同点检范围的情况，修改设备类别树可以查询末级（叶子节点）设备类别下的设备信息，用于个性化维护该设备的点检范围
				this.loader.baseParams.findEquipment = true;
			}
		}
	});
	PointCheckScope.tree.getSelectionModel().on('selectionchange', function(me, node){
		if (!node) return;
		if ('root' == node.id) {
			MyExt.Msg.alert('不可以选择根结点，请重新选择！');
			PointCheckScope.tree.getSelectionModel().clearSelections();
			return;
		}
		PointCheckScope.searchParams.classCode = node.id;
		PointCheckScope.grid.store.load();
	});
	/** **************** 定义设备类别树结束 **************** */
	
	/** **************** 定义点检范围列表开始 **************** */
	PointCheckScope.grid = new Ext.yunda.RowEditorGrid({
		loadURL : ctx + '/pointCheckScope!pageQuery.action',
		saveURL : ctx + '/pointCheckScope!saveOrUpdate.action',
		deleteURL : ctx + '/pointCheckScope!delete.action',
		storeAutoLoad : false, viewConfig : null,
		tbar : ['add', 'delete', 'refresh', '-', {
			text : '选择新增', iconCls : 'addIcon', handler : function() {
				PointCheckScopeSelect.win.show();
			}
		}, '->', {
			// Added by hetao on 2016-09-21 数据库增加字段“CLASS_NAME_PY”后，历史数据中该字段为空，所以该方法用于初始化历史数据
			xtype: 'button', text: '初始化', hidden: true, id: 'btn_init_class_name_py', handler: function() {
				$yd.request({
					url: ctx + '/pointCheckScope!initClassNamePY.action'
				});
			}, tooltip: '用于初始化设备类别首拼！',
			listeners: {
				render: function() {
					this.btnEl.setStyle('color', 'gray');
				}
			}
		}, '-', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				PointCheckScope.moveOrder(PointCheckScope.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				PointCheckScope.moveOrder(PointCheckScope.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				PointCheckScope.moveOrder(PointCheckScope.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				PointCheckScope.moveOrder(PointCheckScope.grid, ORDER_TYPE_BOT);
			}
		}],
		fields : [ {
			dataIndex : 'idx', header : 'idx主键', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'classCode', header : '设备类型编码', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'className', header : '设备类型名称', hidden : true, editor : {
				xtype : 'hidden'
			}
		},{
			dataIndex : 'seqNo', header : '顺序号', width: 50, hidden : false, editor : {
				allowBlank : false, maxLength : 2, xtype : 'numberfield', vtype : 'positiveInt',
				disabled: true, id: 'id_seq_no'
			}
		}, {
			dataIndex : 'checkContent', header : '点检内容', width : 500, editor : {
				allowBlank : false, maxLength : 50
			}
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true, editor : {
				xtype : 'hidden'
			}
		} ],
		beforeAddButtonFn : function(){
			var sm = PointCheckScope.tree.getSelectionModel();
			var node = sm.getSelectedNode();
			if (Ext.isEmpty(node)) {
				MyExt.Msg.alert('请先选择要进行维护的设备类别！');
				return false;
			}
			return true;   	
	    },
	    afterAddButtonFn: function() {
	    	var seqNo = !Ext.isEmpty(PointCheckScope.seqNo) ? PointCheckScope.seqNo : PointCheckScope.grid.store.getTotalCount() + 1;
	    	Ext.getCmp('id_seq_no').setValue(seqNo);
	    },
	    beforeSaveFn: function(rowEditor, changes, record, rowIndex){
	    	if (Ext.isEmpty(record.data.classCode)) {
	    		var sm = PointCheckScope.tree.getSelectionModel();
	    		var node = sm.getSelectedNode();
	    		record.data.classCode = node.id;
	    	}
	        return true;
	    },
	    cancelFn: function(rowEditor, pressed){
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	            PointCheckScope.seqNo = null;
	        }
	    }
	});
	PointCheckScope.grid.store.on('beforeload', function(){
		var sm = PointCheckScope.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (Ext.isEmpty(node)) {
			MyExt.Msg.alert('请先选择要进行维护的设备类别！');
			return false;
		}
		var whereList = [];
		for (var prop in PointCheckScope.searchParams) {
			if ('classCode' == prop) {
				whereList.push({propName : prop, propValue : PointCheckScope.searchParams[prop], stringLike : false});
				continue;
			}
			whereList.push({propName : prop, propValue : PointCheckScope.searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});
	PointCheckScope.grid.store.on('load', function(me, records, options ){
		PointCheckScope.seqNo = null;
		
		// 如果有记录的设备名称首拼字段没有值，则显示【初始化】按钮
		for (var i = 0; i < records.length; i++) {
			if (Ext.isEmpty(records[i].get('classNamePY'))) {
				Ext.getCmp('btn_init_class_name_py').show();
				break;
			}
		}
	});
	PointCheckScope.grid.getSelectionModel().on('selectionchange', function(sm) {
		if (sm.getCount() > 0 && !Ext.isEmpty(sm.getSelections()[0].get('seqNo'))) {
			PointCheckScope.seqNo = sm.getSelections()[0].get('seqNo');
		}
	})
	/** **************** 定义点检范围列表结束 **************** */

	var viewport = new Ext.Viewport({
		layout : 'border',
		defaults: {layout: 'fit'},
		items : [ {
			region : 'west',
			width : 300,
			items: PointCheckScope.tree,
			split: true,
			bbar: ['-', {
				text: '下载导入模板', iconCls: 'page_excelIcon', handler: function() {
					var url = ctx + '/jsp/sb/check/scope/设备点检项目_导入模板.xls';
					window.open(url, '_self', 'width=0,height=0');
				}
			}, {
				text: '导入点检项目', iconCls: 'page_excelIcon', handler: function() {
					PointCheckScopeImport.win.show();
				}
			}]
		}, {
			region : 'center',
			items: PointCheckScope.grid
		}]
	});
	
});