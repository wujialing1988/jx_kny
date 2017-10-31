/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QCItem');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	QCItem.firstLoad = true;						// 定义一个标识变量，该变量用以标识QCItem.grid.store的load事件仅在页面初始化时才生效，详见QCItem.grid.store.on('load', function() {})
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 该方法用于根据【质量检查项表格】的表记录，对【质量检查人员】表格的显示（或隐藏）和数据容器的加载
	QCItem.showQCEmpViewFn = function(data) {
		// 如果是“是否指派（是）”，则隐藏【质量检查人员】表格
		if (data.isAssign == IS_ASSIGN_Y) {
			QCEmpView.grid.hide();
			QCEmpView.grid.getStore().removeAll();
		// 如果是“是否指派（否）”，则显示【质量检查人员】表格
		} else {
			QCEmpView.grid.show();
			QCEmpView.qCItemIDX = data.idx;
			QCEmpView.grid.store.load();
		}
	}
	
	 // 手动排序 
    QCItem.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/qCItem!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义质量检查项表格开始 ************** */
	QCItem.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/qCItem!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/qCItem!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/qCItem!logicDelete.action',            //删除数据的请求URL
	    tbar: ['add', 'delete', 'refresh', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				QCItem.moveOrder(QCItem.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				QCItem.moveOrder(QCItem.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				QCItem.moveOrder(QCItem.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				QCItem.moveOrder(QCItem.grid, ORDER_TYPE_BOT);
			}
		}],
	    title: '质量检查项',
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, editor:{ disabled: false }
		},{
			header:'检查项编码', dataIndex:'qCItemNo', editor:{ allowBlank: false, maxLength: 50 }
		},{
			header:'检查项名称', dataIndex:'qCItemName', editor:{ allowBlank: false, maxLength: 50 }
		},{
			header:'抽检\必检', dataIndex:'checkWay', editor:{ 
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[CHECK_WAY_CJ, "抽检"], [CHECK_WAY_BJ, "必检"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:CHECK_WAY_CJ,
 				allowBlank: false 
        	}, renderer: function(v) {
        		if (v == CHECK_WAY_CJ) return "抽检";
        		if (v == CHECK_WAY_BJ) return "必检";
        	}
		},{
			header:'是否指派', dataIndex:'isAssign', editor:{ 
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_ASSIGN_N, "否"], [IS_ASSIGN_Y, "是"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:IS_ASSIGN_N,
 				allowBlank: false 
        	}, renderer: function(v) {
        		if (v == IS_ASSIGN_N) return "否";
        		if (v == IS_ASSIGN_Y) return "是";
        	}
		}], 
	    saveFn: function(rowEditor, changes, record, rowIndex){
	    	if (changes.isAssign == IS_ASSIGN_Y && !QCItem.isAddFn) {
	    		Ext.Msg.confirm("提示" ,"指派后，将会删除该检查项关联的检查人员，是否继续？" ,function(btn){ 
		    		if(btn == "yes"){
						if(self.loadMask)   self.loadMask.show();
						var cfg = {
				            scope: QCItem.grid, url: QCItem.grid.saveURL, jsonData: record.data,
				            success: function(response, options){
				                if(QCItem.grid.loadMask)   self.loadMask.hide();
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                    QCItem.grid.afterSaveSuccessFn(result, response, options);
				                } else {
				                    QCItem.grid.afterSaveFailFn(result, response, options);
				                }
				            }
				        };
						Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		    		} else {
		    			QCItem.grid.store.reload();
		    		}
	    		})
	    	} else {
				if(self.loadMask)   self.loadMask.show();
				var cfg = {
		            scope: QCItem.grid, url: this.saveURL, jsonData: record.data,
		            success: function(response, options){
		                if(this.loadMask)   self.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    this.afterSaveSuccessFn(result, response, options);
		                } else {
		                    this.afterSaveFailFn(result, response, options);
		                }
		            }
		        };
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    	}
	    },
		listeners:{
			// 行单击的事件监听函数
			rowclick: function (grid, rowIndex ) {
				// 如果是“已指派”的质量检查项，则隐藏质量检查人员grid
				QCItem.showQCEmpViewFn(this.store.getAt(rowIndex).data);
			}
		},
		// 新增时启用“质量检查项编码”和“质量检查项名称”
		afterAddButtonFn: function(){
			this.enableColumns(['qCItemNo', 'qCItemName']);
			QCItem.isAddFn = true;
			QCItem.showQCEmpViewFn({isAssign:IS_ASSIGN_Y});
	    },
		// “质量检查项编码”和“质量检查项名称”不可以编辑
		beforeEditFn: function(rowEditor, rowIndex){
			this.disableColumns(['qCItemNo', 'qCItemName']);
			QCItem.isAddFn = false;
	        return true;
	    }
	});
	// 默认以“顺序号”升序排序
	QCItem.grid.store.setDefaultSort('seqNo', 'ASC');
	// 数据加载完成后的页面初始化处理
	QCItem.grid.store.on('load', function(store, records, options) {
		if (this.getCount() > 0) {
			var sm = QCItem.grid.getSelectionModel();
			if (!QCItem.isAddFn) {
				// 如果不是新增操作触发的数据加载，则默认选择第一条记录
				sm.selectRow(0);
			} else {
				// 选择新增的几率
				sm.selectRow(records.length - 1);
			}
			QCItem.showQCEmpViewFn(sm.getSelections()[0].data);
		}		
	});
	/** ************** 定义质量检查项表格结束 ************** */
	
	//页面自适应布局
	new Ext.Viewport({
		layout:'border', 
		items:[{
			region:'west',
			width:550,
			layout:'fit',
			items:QCItem.grid,
			split: true
		}, {
			region:'center',
			layout:'fit',
			items:QCEmpView.grid
		}]
	});
	
});