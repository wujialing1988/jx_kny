Ext.onReady(function() {
	Ext.ns('RepairScope');
	
	/** **************** 定义全局变量开始 **************** */
	RepairScope.searchParams = {
		repairType: REPAIR_TYPE_JX
	};
	RepairScope.seqNo = null;
	RepairScope.labelWidth = 100;
	RepairScope.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 手动排序 
    RepairScope.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/repairScope!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.success == true) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	RepairScope.tree = new Ext.tree.TreePanel({
		title: '设备类别树',
		loader : new Ext.tree.TreeLoader({
			url : ctx + "/classification!findTree.action"
		}),
		root : new Ext.tree.AsyncTreeNode({
			id : "root", text : "设备类别", expanded : true
		}),
		autoScroll : true,
		animate : true,
		listeners : {
			"beforeload" : function(node) {
				this.loader.baseParams.parentCode = node.id;
				// Modified by hetao on 2017-02-16 对于同类型不同设备存在不同检修范围的情况，修改设备类别树可以查询末级（叶子节点）设备类别下的设备信息，用于个性化维护该设备的检修范围
				this.loader.baseParams.findEquipment = true;
			}
		}});
	RepairScope.tree.getSelectionModel().on('selectionchange', function(me, node) {
		if (!node) return;
		if ('root' == node.id) {
			MyExt.Msg.alert('不可以选择根结点，请重新选择！');
			RepairScope.tree.getSelectionModel().clearSelections();
			RepairScope.grid.getTopToolbar().findByType('checkbox')[0].disable();
			RepairScope.grid.getTopToolbar().findByType('checkbox')[1].disable();
			RepairScope.grid.getTopToolbar().findByType('combo')[0].disable();
			return;
		}
		RepairScope.grid.getTopToolbar().findByType('checkbox')[0].enable();
		RepairScope.grid.getTopToolbar().findByType('checkbox')[1].enable();
		RepairScope.grid.getTopToolbar().findByType('combo')[0].enable();
		RepairScope.searchParams.classCode = node.id;
		RepairScope.grid.store.load();
	});
	/** **************** 定义设备类别树结束 **************** */
	
	/** **************** 定义编辑表单开始 **************** */
	RepairScope.saveForm = new Ext.form.FormPanel({
		padding: 10, baseCls: 'x-plain', labelWidth: RepairScope.labelWidth,
		layout: 'column',
		defaults: {
			layout: 'form', columnWidth: 1, baseCls: 'x-plain', defaults: {
			xtype: 'textfield', width: RepairScope.fieldWidth
		}},
		items: [{
			items: [{
				fieldLabel: '检修范围名称', name: 'repairScopeName', allowBlank: false, maxLength: 200,
				width: 403
			}]
		}, {
			items: [{
				fieldLabel: '检修类别', xtype: 'checkboxgroup', items: [{
					xtype: 'checkbox', name: 'repairClassSmall', boxLabel: '小修', inputValue: 'true', checked: true
				}, {
					xtype: 'checkbox', name: 'repairClassMedium', boxLabel: '中修', inputValue: 'true'	
				}, {
					xtype: 'checkbox', name: 'repairClassSubject', boxLabel: '项修', inputValue: 'true'	
				}], 
				allowBlank: false, width: 403
			}]
		}, {
			columnWidth: 1,
			items: [{
				xtype: 'textarea', fieldLabel: '备注', name: 'remark', width: 403, maxLength: 500
			}]
		}, {
			columnWidth: 1, defaults: {xtype: 'hidden'},
			items: [{
				fieldLabel: 'idx主键', name: 'idx'
			}, {
				fieldLabel: '设备类别编码', name: 'classCode'
			}, {
				fieldLabel: '设备类别名称', name: 'className'
			}, {
				fieldLabel: '设备类别首拼', name: 'classNamePY'
			}, {
				fieldLabel: '检修类型', name: 'repairType'
			}, {
				fieldLabel: '序号', name: 'seqNo', readOnly: true, style: 'background:none;border:none;'
			}]
		}]
	});
	
	RepairScope.saveWin = new Ext.Window({
        title:"新增", width: 576, plain:true, closeAction:"hide",
        buttonAlign:'center', items:RepairScope.saveForm, autoHeight:true, modal: true,
        buttons: [{
        	text: "保存", iconCls: "saveIcon", handler: function() {
        		RepairScope.grid.saveFn(false);
        	}
        }, {
        	text: "保存并新增", iconCls: "addIcon", handler: function() {
        		RepairScope.grid.saveFn(true);
        	}
        }, {
        	text: "关闭", iconCls: "closeIcon", handler: function() {
        		this.findParentByType('window').hide();
        	}
        }],
        listeners: {
        	beforeshow: function() {
        		var sm = RepairScope.tree.getSelectionModel();
    			var node = sm.getSelectedNode();
    			if (Ext.isEmpty(node)) {
    				MyExt.Msg.alert('请先选择要进行维护的设备类别！');
    				return false;
    			}
    			return true;   	 
        	},
        	show: function() {
        		// 重置保存表单
        		RepairScope.saveForm.getForm().reset();
        		var seqNo = !Ext.isEmpty(RepairScope.seqNo) ? RepairScope.seqNo : RepairScope.grid.store.getTotalCount() + 1;
        		RepairScope.saveForm.find('name', 'seqNo')[0].setValue(seqNo);
        		RepairScope.saveWin.setTitle('新增&nbsp;' + '-&nbsp;<span style="color:green;">' + seqNo + '</span>');
    	    	// 设置设备类别编码
        		RepairScope.saveForm.find('name', 'classCode')[0].setValue(RepairScope.searchParams.classCode);
    	    	// 设置检修类型
        		RepairScope.saveForm.find('name', 'repairType')[0].setValue(RepairScope.searchParams.repairType);
        	}
        }
    });
	/** **************** 定义编辑表单结束 **************** */
	
	/** **************** 定义巡检目录列表开始 **************** */
	var sm, cm;
	sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect:false,			// 支持多选
		listeners: {
			rowselect: function(sm, rowIndex, record) {
				if (1 == sm.getCount()) {
					RepairScopeDetails.grid.store.load();
				} else {
					RepairScopeDetails.grid.store.removeAll();
				}
			},
			rowdeselect: function(sm, rowIndex, record) {
				if (1 == sm.getCount()) {
					RepairScopeDetails.grid.store.load();
				} else {
					RepairScopeDetails.grid.store.removeAll();
				}
			},
			selectionchange: function(sm) {
				if (1 == sm.getCount()) {
					RepairScope.seqNo = sm.getSelections()[0].get('seqNo');
				} else {
					RepairScope.seqNo = null;
				}
			}
		}
	}),
	cm = new Ext.grid.ColumnModel({
		defaults: {
			sortable: false // columns are not sortable by default           
        },
		columns: [
		          new Ext.grid.RowNumberer(), sm,{
			dataIndex : 'idx', header : 'idx主键', hidden : true
		}, {
			dataIndex : 'classCode', header : '设备类别编码', hidden : true
		}, {
			dataIndex : 'className', header : '设备类别名称', hidden : true
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true
		}, {
			dataIndex : 'seqNo', header : '序号', width: 50, hidden : false
		}, {
			dataIndex : 'repairScopeName', header : '检修范围名称', width : 200
		}, {
			xtype: 'checkcolumn',
			dataIndex : 'repairClassSmall', header : '小修', width : 40, align: 'center',
		    renderer: function(value, metaData, record, rowIndex, colIndex, store) {
		    	metaData.css = 'editCell';
				metaData.attr = 'style=border-right:1px solid #00f;';
				if (value) return '<span style="color:red;">&radic;</span>';
			}
		}, {
			xtype: 'checkcolumn',
			dataIndex : 'repairClassMedium', header : '中修', width : 40, align: 'center',
		    renderer: function(value, metaData, record, rowIndex, colIndex, store) {
		    	metaData.css = 'editCell';
				if (value) return '<span style="color:red;">&radic;</span>';
			}
		}, {
			xtype: 'checkcolumn',
			dataIndex : 'repairClassSubject', header : '项修', width : 40, align: 'center',
		    renderer: function(value, metaData, record, rowIndex, colIndex, store) {
		    	metaData.css = 'editCell';
				if (value) return '<span style="color:red;">&radic;</span>';
			}
		}, {
			dataIndex : 'repairType', header : '检修类型', renderer: function(v) {
				if (REPAIR_TYPE_JX == v) return '机械';
				if (REPAIR_TYPE_DQ == v) return '电气';
				if (REPAIR_TYPE_QT == v) return '其它';
				return '错误！未知类型';
			}
		}, {
			dataIndex : 'remark', header : '备注', width : 300
		} ]
	}),
	store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: false,
        remoteSort: true,
        url: ctx + '/repairScope!pageQuery.action',
        fields: ['idx', 'classCode', 'className', 'classNamePY', 'seqNo', 'repairType', 'repairScopeName', 'repairClassSmall', 'repairClassMedium', 'repairClassSubject', 'remark'],
        listeners: {
        	beforeload : function() {
        		var sm = RepairScope.tree.getSelectionModel();
        		var node = sm.getSelectedNode();
        		if (Ext.isEmpty(node)) {
        			MyExt.Msg.alert('请先选择要进行维护的设备类别！');
        			return false;
        		}
        		var whereList = [];
        		for (var prop in RepairScope.searchParams) {
        			if ('classCode' == prop) {
        				whereList.push({propName : prop, propValue : RepairScope.searchParams[prop], stringLike : false});
        				continue;
        			}
        			whereList.push({propName : prop, propValue : RepairScope.searchParams[prop]});
        		}
        		// 根据检修类型过滤
        		var repairType = RepairScope.grid.getTopToolbar().findByType('combo')[0].getValue();
        		if (!Ext.isEmpty(repairType)) {
        			var sql = null;
        			switch (repairType) {
					case REPAIR_CLASS_SMALL:
						sql = "repair_class_small = 1";
						break;
					case REPAIR_CLASS_MEDIUM:
						sql = "repair_class_medium = 1";
						break;
					case REPAIR_CLASS_SUBJECT:
						sql = "REPAIR_CLASS_SUBJECT = 1";
						break;
					default:
						break;
					}
        			whereList.push({compare: Condition.SQL, sql: sql});
        		}
        		this.baseParams.whereListJson = Ext.encode(whereList);
        		this.baseParams.sort = "seqNo";
        		this.baseParams.dir = "ASC";
        	},
        	load : function(me, records, options ) {
        		RepairScope.seqNo = null;
        		var sm = RepairScope.grid.getSelectionModel();
        		if (1 != sm.getCount()) {
        			RepairScopeDetails.grid.store.removeAll();
        		}
        	}
        }
    }); 
	RepairScope.grid = new Ext.grid.EditorGridPanel({
		border: false,
		saveURL : ctx + '/repairScope!saveOrUpdate.action',
		deleteURL : ctx + '/repairScope!logicDelete.action',
		saveForm: RepairScope.saveForm,
		saveWin: RepairScope.saveWin,
		entity: 'com.yunda.sb.repair.scope.entity.RepairScope',
		sm: sm,
		cm: cm,
		loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        store: store,
        remoteSort: false,
        clicksToEdit: 1,
		saveFn: function(isSaveAndAdd) {
	        //表单验证是否通过
	        var form = this.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        var data = form.getValues();
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: this, url: this.saveURL, jsonData: data,
	            success: function(response, options) {
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                	this.afterSaveSuccessFn(result, response, options);
	                	if (isSaveAndAdd) {
	                		 //连续添加处理方法
	                		 this.afterAddSuccessFn(result, response, options);
	                	}
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            },
	            failure: function() {
	            	alertFail("请求失败！请刷新页面后再试");
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    
		},
		afterSaveFailFn: function(result, response, options) {
	    	this.store.reload();
	        alertFail(result.errMsg);
	    },
		afterSaveSuccessFn: function(result, response, options) {
	    	// 如果是【保存】则默认回显数据保存成功后的实体idx主键
	    	if (this.saveForm.find('name', 'idx')[0]) this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        this.store.reload();
	        alertSuccess();
	    },
	    afterAddSuccessFn: function(result, response, options) {
	    	var seqNo = result.entity.seqNo + 1;
	    	this.saveForm.find('name', 'idx')[0].reset();
	    	this.saveForm.find('name', 'repairScopeName')[0].reset();
	    	this.saveForm.find('name', 'remark')[0].reset();
	    	this.saveForm.find('name', 'seqNo')[0].setValue(seqNo);
	    	this.saveWin.setTitle('新增&nbsp;' + '-&nbsp;<span style="color:green;">' + seqNo + '</span>');
	    },
	    toEditFn: function(grid, rowIndex, e) {
	        var record = this.store.getAt(rowIndex);
	        this.saveWin.show();
	        this.saveForm.getForm().reset();
	        this.saveForm.getForm().loadRecord(record);
	        //显示编辑窗口后触发函数（可能需要对某些特殊控件赋值等操作）
	        this.afterShowEditWin(record, rowIndex);        
	    },
	    afterShowEditWin: function(record, rowIndex) {
	    	this.saveWin.setTitle('编辑&nbsp;' + '-&nbsp;<span style="color:green;">' + record.get('seqNo') + '</span>');
	    },
	    deleteButtonFn: function() {                         //点击删除按钮触发的函数，默认执行删除操作
	        if(this.saveWin)    this.saveWin.hide();
	        if(this.searchWin)  this.searchWin.hide();        
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        $yd.confirmAndDelete({
	            scope: this, url: this.deleteURL, params: {ids: $yd.getSelectedIdx(this, this.storeId)}
	        });
	    },
        tbar : [{
        	text: '新增', iconCls: 'addIcon', handler: function() {
        		RepairScope.saveWin.show();
        	}
        }, {
        	text: '删除', iconCls: 'deleteIcon', handler: function() {
        		RepairScope.grid.deleteButtonFn();
        	}
        }, {
        	text: '刷新', iconCls: 'refreshIcon', handler: function() {
        		self.location.reload();
        	}
        }, '-', {
			text : '选择新增', iconCls : 'addIcon', handler : function() {
				RepairScopeSelect.win.show();
			}, tooltip: '点我，点我有惊喜！'
		}, '-', {
			text: '安全风险点', iconCls: 'warningIcon', handler: function() {
				var sm = RepairScope.grid.getSelectionModel();
				if (1 != sm.getCount()) {
					MyExt.Msg.alert('请选择<span style="color:red;text-decoration:underline;font-weight:bold;">1</span>条检修范围进行安全风险点维护！');
					return;
				}
				// 安全风险点维护窗口
				RepairScopeRiskWarning.scopeIdx = sm.getSelections()[0].id;
				RepairScopeRiskWarning.win.show();
			}
		}, '&nbsp;&nbsp;', '-', {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械', checked: true, disabled: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScope.searchParams.repairType = me.getRawValue();
						RepairScope.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气', disabled: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScope.searchParams.repairType = me.getRawValue();
						RepairScope.grid.store.load();
					}
				}
			}
		}, {
			// Modified by hetao on 2016-07-24 暂时不处理“其它”类型的检修范围
			/*xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_QT, boxLabel: '其它',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScope.searchParams.repairType = me.getRawValue();
						RepairScope.grid.store.load();
					}
				}
			}*/
		}, '-', {
			xtype: 'combo', maxLength: 10, width: 60,
			typeAhead: true,
		    triggerAction: 'all',
		    lazyRender:true,
		    disabled: true,
		    mode: 'local',
		    store: new Ext.data.ArrayStore({
		    	fields: [ 'k', 'v' ],
		        data: [['', '全部'], [REPAIR_CLASS_SMALL, '小修'], [REPAIR_CLASS_MEDIUM, '中修'], [REPAIR_CLASS_SUBJECT, '项修']]
		    }),
		    value: '',
		    valueField: 'k', displayField: 'v',
		    listeners: {
		    	select: function() {
		    		RepairScope.grid.store.load();
		    	}
		    }
		
		}, '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				RepairScope.moveOrder(RepairScope.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				RepairScope.moveOrder(RepairScope.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				RepairScope.moveOrder(RepairScope.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				RepairScope.moveOrder(RepairScope.grid, ORDER_TYPE_BOT);
			}
		}],
        bbar: $yd.createPagingToolbar({pageSize: 50, store: store}),
        listeners: {
        	rowdblclick: function(grid, rowIndex, e) {
        		this.toEditFn(grid, rowIndex, e);
        	},
        	beforeedit: function(e) {
//        		return false;
        	},
        	afteredit: function(e) {
        		e.grid.fireEvent('update', this, e.record);
        	},
        	update: function( me, record, operation ) {
        		var values = record.getChanges();
        		
        		// 获取最后一次编辑的字段名称
        		var fieldName;
        		for (var prop in values) {
        			fieldName = prop;
        		}
        		values.idx = record.id;
        			
        		// 提交后台进行数据存储
        		$yd.request({
        			scope: this,
        			url: ctx + '/sbCommon!saveChange.action',
        			params: {
        				className: this.entity,
        				fieldName:  fieldName,
        				entityJson: Ext.encode(values)	
        			}
        		}, function(result) {
        				// 如果不提交，record.getChanges()方法会始终记录所有的记录变更
//        				record.commit();
    			});
        	}
        }
    });
	/** **************** 定义巡检目录列表结束 **************** */

	var viewport = new Ext.Viewport({
		layout : 'border',
		defaults: {layout: 'fit'},
		items : [ {
			title : '设备类别树',
			region : 'west',
			width : 300,
			items: RepairScope.tree,
			collapsible: true,
			split: true,
			bbar: ['-', {
				text: '下载导入模板', iconCls: 'page_excelIcon', handler: function() {
					var url = ctx + '/jsp/sb/repair/scope/设备检修范围_导入模板.xls';
					window.open(url, '_self', 'width=0,height=0');
				}
			}, {
				text: '导入检修范围', iconCls: 'page_excelIcon', handler: function() {
					RepairScopeImport.win.show();
				}
			}]
		}, {
			region : 'center',
			items: RepairScope.grid
		}, {
			region : 'east',
			width: 500,
			items: RepairScopeDetails.grid,
			split: true
		} ]
	});
	
});