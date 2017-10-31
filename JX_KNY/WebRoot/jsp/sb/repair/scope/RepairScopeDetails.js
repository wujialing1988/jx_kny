Ext.onReady(function() {
	Ext.ns('RepairScopeDetails');
	
	/** **************** 定义全局变量开始 **************** */
	RepairScopeDetails.searchParams = {};
	RepairScopeDetails.seqNo = null;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 手动排序 
    RepairScopeDetails.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/repairScopeDetails!moveOrder.action',
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
	
	/** **************** 定义巡检目录列表开始 **************** */
	RepairScopeDetails.grid = new Ext.yunda.RowEditorGrid({
		loadURL : ctx + '/repairScopeDetails!pageQuery.action',
		saveURL : ctx + '/repairScopeDetails!saveOrUpdate.action',
		deleteURL : ctx + '/repairScopeDetails!logicDelete.action',
		storeAutoLoad : false,
		tbar : ['add', 'delete', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				RepairScopeDetails.moveOrder(RepairScopeDetails.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				RepairScopeDetails.moveOrder(RepairScopeDetails.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				RepairScopeDetails.moveOrder(RepairScopeDetails.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				RepairScopeDetails.moveOrder(RepairScopeDetails.grid, ORDER_TYPE_BOT);
			}
		}],
		fields : [ {
			dataIndex : 'idx', header : 'idx主键', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'scopeIdx', header : '范围主键', hidden : true, editor : {
				xtype : 'hidden'
			}
		}, {
			dataIndex : 'seqNo', header : '序号', width: 90, hidden : false, editor : {
				allowBlank : false, maxLength : 2, xtype : 'numberfield', vtype : 'positiveInt',
				disabled: true, id: 'id_seq_no'
			}
		}, {
			dataIndex : 'workContent', header : '作业内容', width : 240, editor : {
				allowBlank : false, maxLength : 200
			}
		}, {
			dataIndex : 'processStandard', header : '工艺标准', width : 540, editor : {
				maxLength : 500,
				xtype : 'textarea'
			}
		} ],
		beforeAddButtonFn : function() {
			var sm = RepairScope.grid.getSelectionModel();
			if (1 != sm.getCount()) {
				MyExt.Msg.alert('请选择<span style="color:red;">1</span>条检修范围进行作业内容维护！');
				return false;
			}
			return true;   	
	    },
	    afterAddButtonFn: function() {
	    	var seqNo = !Ext.isEmpty(RepairScopeDetails.seqNo) ? RepairScopeDetails.seqNo : RepairScopeDetails.grid.store.getTotalCount() + 1;
	    	Ext.getCmp('id_seq_no').setValue(seqNo);
	    },
	    beforeSaveFn: function(rowEditor, changes, record, rowIndex) {
	    	if (Ext.isEmpty(record.data.scopeIdx)) {
	    		record.data.scopeIdx = RepairScopeDetails.searchParams.scopeIdx;
	    	}
	        return true;
	    },
	    cancelFn: function(rowEditor, pressed) {
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	            RepairScopeDetails.seqNo = null;
	        }
	    }
	});
	RepairScopeDetails.grid.store.on('beforeload', function() {
		// 根据检修范围idx主键获取作业内容
		var sm = RepairScope.grid.getSelectionModel();
		if (1 !== sm.getCount()) {
			return false;
		}
		RepairScopeDetails.searchParams.scopeIdx = sm.getSelections()[0].id;
		
		var whereList = [];
		for (var prop in RepairScopeDetails.searchParams) {
			if ('scopeIdx' == prop) {
				whereList.push({propName : prop, propValue : RepairScopeDetails.searchParams[prop], stringLike : false});
				continue;
			}
			whereList.push({propName : prop, propValue : RepairScopeDetails.searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});
	RepairScopeDetails.grid.store.on('load', function(me, records, options ) {
		RepairScopeDetails.seqNo = null;
	});
	RepairScopeDetails.grid.getSelectionModel().on('selectionchange', function(sm) {
		if (sm.getCount() > 0 && !Ext.isEmpty(sm.getSelections()[0].get('seqNo'))) {
			RepairScopeDetails.seqNo = sm.getSelections()[0].get('seqNo');
		} 
	})
	/** **************** 定义巡检目录列表结束 **************** */
	
});