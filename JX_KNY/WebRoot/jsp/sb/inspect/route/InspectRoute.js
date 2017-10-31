Ext.onReady(function() {
	Ext.ns('InspectRoute');
	
	/** **************** 定义全局变量开始 **************** */
	InspectRoute.searchParams = {};
	InspectRoute.labelWidth = 90;
	InspectRoute.fieldWidth = 202;
	InspectRoute.isSaveAndAdd =  false;
	// 重新加载树
	InspectRoute.reloadTree = function(path) {		
		InspectRoute.tree.root.reload(function() {
//			if (!path) InspectRoute.tree.getSelectionModel().select(InspectRoute.tree.root);
		});
		if (path == undefined || path == "" || path == "###") {
			InspectRoute.tree.root.expand();
		} else {
			// 展开树到指定节点
			InspectRoute.tree.expandPath(path);
			InspectRoute.tree.selectPath(path);
		}
	}
	
	// 机车检修作业计划基本信息显示模板
	InspectRoute.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td><span>巡检目录：</span>{routeName}</td>',
			'</tr>',
			'<tr>',
			'<td><span>计划编制人：</span>{partrolWorker}</td>',
			'</tr>',
			'<tr>',
			'<td><span>巡检周期：</span>{periodType}</td>',
			'</tr>',
			'<tr>',
			'<td><span>计划有效期：</span>{planPublishDate}<span style="width:20px;text-align:right;">&nbsp;至&nbsp;</span>{expiryDate}</td>',
			'</tr>',
			'<tr>',
			'<td><span>状态：</span>{state}</td>',
			'</tr>',
		'</table>'
	);
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 保存巡检目录
	InspectRoute.saveFn = function() {
		var form = InspectRoute.saveForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var values = form.getValues();
		
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/inspectRoute!saveOrUpdate.action',
			jsonData : values,
			scope : InspectRoute.saveForm,
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            var idx = result.entity.idx;
		            if (InspectRoute.isSaveAndAdd) {
		            	this.find('name', 'idx')[0].reset();
		            	this.find('name', 'routeName')[0].reset();
		            } else {
		            	this.find('name', 'idx')[0].setValue(idx);
//		            	this.findParentByType('window').hide();
		            }
		            InspectRoute.reloadTree(InspectRoute.path);
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
	};
	// 删除巡检目录
	InspectRoute.deleteFn = function() {
		var sm = InspectRoute.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (!node) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		var ids = [node.id];

		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/inspectRoute!logicDelete.action',
			params : {ids : ids},
			success: function(response, options) {
				if(self.loadMask)    self.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) {       //操作成功     
					alertSuccess();
					InspectRoute.reloadTree(InspectRoute.path);
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
	};
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	InspectRoute.saveForm = new Ext.form.FormPanel({
		baseCls: "x-plain",
		padding : 10, labelWidth : InspectRoute.labelWidth,
		defaults : {xtype : 'textfield', width : InspectRoute.fieldWidth},
		items : [{
			name : 'idx', fieldLabel : 'idx主键', hidden : true
		}, {
			name : 'routeName', fieldLabel : '巡检目录名称', allowBlank : false, maxLength : 50
		}, {
			name : 'partrolWorker', fieldLabel : '计划编制人', id : 'partrolWorker_id', hidden : true
		}, {
		    xtype: 'OmEmployee_SelectWin', fieldLabel: '计划编制人',
		    displayField:'empname', valueField: 'empid', allowBlank : false,
		    editable: false, width: 140, hiddenName: 'partrolWorkerId',
		    returnField :[{widgetId: "partrolWorker_id", propertyName: "empname"}]
		}, {
			hiddenName : 'periodType', fieldLabel : '巡检周期',
			xtype: 'combo', maxLength: 10,
			typeAhead: true,
		    triggerAction: 'all',
		    lazyRender:true,
		    mode: 'local',
		    store: new Ext.data.ArrayStore({
		    	fields: [ 'k', 'v' ],
		        data: [[
		            PERIOD_TYPE_ZJ, PERIOD_TYPE_ZJ_CH		// 周检
                 ],  [
                    PERIOD_TYPE_BYJ, PERIOD_TYPE_BYJ_CH		// 半月检
                 ], [
                    PERIOD_TYPE_YJ, PERIOD_TYPE_YJ_CH		// 月检
                 ], [
                    PERIOD_TYPE_JJ, PERIOD_TYPE_JJ_CH		// 季检	
                ]]
		    }),
		    value: PERIOD_TYPE_ZJ,
		    valueField: 'k', displayField: 'v'
		}, {
			xtype: 'compositefield', fieldLabel: '计划有效期', combineErrors: false, anchor:'100%',
			items: [{
				xtype:'my97date', id: 'planPublishDate', name: 'planPublishDate', format:'Y-m-d', width: 90,
				// 日期校验器
				vtype:'dateRange',
				dateRange:{startDate: 'planPublishDate', endDate: 'expiryDate'}
			}, {
				xtype: 'label',
				text: '至',
				style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
			}, {							
				xtype:'my97date', id: 'expiryDate', name: 'expiryDate', format:'Y-m-d', width: 90, initNow : false,
				// 日期校验器
				vtype:'dateRange',
				dateRange:{startDate: 'planPublishDate', endDate: 'expiryDate'}
			}]
		
//		}, {
//			name : 'planPublishDate', fieldLabel : '有效期', xtype : 'my97date', format : 'Y-m-d'
//		}, {
//			name : 'expiryDate', fieldLabel : '至', xtype : 'my97date', format : 'Y-m-d', initNow : false
		}, {
			fieldLabel : '状态', xtype: 'radiogroup',
            items: [
                {boxLabel: '立即启用', name: 'state', inputValue: STATE_QY, checked: true},
                {boxLabel: '暂不启用', name: 'state', inputValue: STATE_WQY}
            ]
		}]
	});
	
	InspectRoute.win = new Ext.Window({
		title : '新增巡检目录',
		width : 350, height : 240,
		modal : true, plain: true,
		closeAction : 'hide',
		layout : 'fit',
		items : InspectRoute.saveForm,
		buttonAlign : 'center',
		buttons : [{
			text : '保存', iconCls : 'saveIcon', handler : function() {
				InspectRoute.isSaveAndAdd =  false;
				InspectRoute.saveFn();
			}
		}, {
			text : '保存并新增', iconCls : 'addIcon', handler : function() {
				InspectRoute.isSaveAndAdd =  true;
				InspectRoute.saveFn();
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners : {
			show : function() {
				var form = InspectRoute.saveForm.getForm();
				form.reset();
				// 编辑巡检目录
				if (InspectRoute.editNode) {
					this.setTitle('编辑巡检目录');
					var data = InspectRoute.editNode.attributes;
					form.setValues(data);
					InspectRoute.saveForm.find('xtype', 'OmEmployee_SelectWin')[0].setDisplayValue(data.partrolWorkerId, data.partrolWorker);
					
					if (!Ext.isEmpty(data.planPublishDate)) {
						Ext.getCmp('planPublishDate').setValue(new Date(data.planPublishDate));
					}
					if (!Ext.isEmpty(data.expiryDate)) {
						Ext.getCmp('expiryDate').setValue(new Date(data.expiryDate));
					}
				} else {
					Ext.getCmp('partrolWorker_id').setValue(uname);
					InspectRoute.saveForm.find('xtype', 'OmEmployee_SelectWin')[0].setDisplayValue(empid, uname);
				}
			}
		}
	});
	/** **************** 定义保存表单结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	InspectRoute.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			url : ctx + "/inspectRoute!tree.action"
		}),
		plugins : ['multifilter'],
		tbar : [{
			text : '新增', iconCls : 'addIcon', handler : function() {
				InspectRoute.editNode = null;
				InspectRoute.win.show();
			}
		}, {
			text : '删除', iconCls : 'deleteIcon', handler : function() {
				Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn) {
					if ('yes' == btn) {
						InspectRoute.deleteFn();
					}
				});
			}
		}],
		root : new Ext.tree.AsyncTreeNode({
			text : "设备巡检目录",
			id : "root",
			expanded : true
		}),
//		selModel: new Ext.tree.MultiSelectionModel(),
		rootVisible: false,
		autoScroll : true,
		animate : true,
		border : false,
		listeners : {
			expandnode: function( node ) {
				if (!node) return;
				if (Ext.isEmpty(node.firstChild)) {
					InspectRouteDetails.grid.store.removeAll();
					return;
				}
				this.selModel.select(node.firstChild);
			},
			dblclick : function( node, e ) {
				InspectRoute.editNode = node;
				InspectRoute.win.show();
			},
			beforeload: function(node) {
//				var tbar = Ext.getCmp('id_main_view_left').getTopToolbar();
//				var checkboxes = tbar.findByType('checkbox');
//				var states = [-1];
//				for (var i = 0; i < checkboxes.length - 1; i++) {
//					if (checkboxes[i].checked) {
//						states.push(checkboxes[i].getRawValue());
//					}
//				}
//				var expirible = checkboxes[2].checked;
//				this.loader.baseParams.states = states.join(',');
//				this.loader.baseParams.expirible = expirible;
			}
		}
	});
	InspectRoute.tree.getSelectionModel().on('selectionchange', function(me, node) {
		if (!node) return;
		// 声明一个InspectRoute.path，用于在重新加载树以后，能够再次选择之间的树节点
		InspectRoute.path = node.getPath();
		// 如果是单选，则重新加载巡检目录明细表格数据源
		InspectRouteDetails.routeIdx= node.id;
		InspectRouteDetails.grid.store.load();
		// 显示巡检目录明细展示面板
		if (Ext.getCmp('id_main_view_left_1').hidden) {
			Ext.getCmp('id_main_view_left_1').show();
		}
		// 渲染巡检目录明细展示信息
		var data = node.attributes;
		var entity = {
			routeName : data.routeName,
			partrolWorker : data.partrolWorker
		};
		// 计划发布日期
		entity.planPublishDate =  new Date(data.planPublishDate).format('Y-m-d');
		// 计划发布日期
		if (!Ext.isEmpty(data.expiryDate)) {
			entity.expiryDate =  new Date(data.expiryDate).format('Y-m-d');
			if (data.expiryDate < (new Date()).valueOf()) {
				entity.state = '<span style="color:red;display:inline;font-weight:bold;">已过期</span>';
			}
		} else {
			entity.expiryDate = "永久";
		}
		
		// 状态（1启用、0未启用）
		if (STATE_WQY == data.state) {
			entity.state = entity.state || STATE_WQY_CH;
		} else if (STATE_QY == data.state) {
			entity.state = entity.state || STATE_QY_CH;
		}
		// 巡检周期（1：周检；2：半月检；3：月检）
		if (PERIOD_TYPE_ZJ == data.periodType) {
			entity.periodType = PERIOD_TYPE_ZJ_CH;
		} else if (PERIOD_TYPE_BYJ == data.periodType) {
			entity.periodType = PERIOD_TYPE_BYJ_CH;
		} else if (PERIOD_TYPE_YJ == data.periodType) {
			entity.periodType = PERIOD_TYPE_YJ_CH;
		} else if (PERIOD_TYPE_JJ == data.periodType) {
			entity.periodType = PERIOD_TYPE_JJ_CH;
		}
		InspectRoute.tpl.overwrite(Ext.get('id_inspect_route_info'), entity);
	});
	/** **************** 定义设备类别树结束 **************** */
	

	InspectRoute.viewport = new Ext.Viewport({
		layout: 'border', 
		items: [{
			id: 'id_main_view_left',
			title : '设备巡检目录',
			collapsible: true, collapseMode: 'mini', /*collapsed: true,*/
			region: 'west', width: 310,
			layout: 'border', defaults: {border: false},
//			tools: [{
//				id: 'refresh', handler: function() {
//					InspectRoute.tree.root.reload();
//				}
//			}],
//			tbar: ['-', {
//				xtype: 'checkbox', boxLabel: '已启用&nbsp;&nbsp;', inputValue: STATE_QY, checked: true, listeners: {
//					check: function(me, checked) {
//						InspectRoute.tree.root.reload();
//					}
//				}
//			}, {
//				xtype: 'checkbox', boxLabel: '未启用&nbsp;&nbsp;', inputValue: STATE_WQY, listeners: {
//					check: function(me, checked) {
//						InspectRoute.tree.root.reload();
//					}
//				}
//			}, {
//				xtype: 'checkbox', boxLabel: '已过期&nbsp;&nbsp;', listeners: {
//					check: function(me, checked) {
//						InspectRoute.tree.root.reload();
//					}
//				}
//			}],
			items: [{
				region: 'center', layout: 'fit',
				items: InspectRoute.tree
			}, {
				id: 'id_main_view_left_1', 
				region: 'south', height: 180, hidden: true,
				frame: true, collapsible: true,
				title: '作业计划基本信息',
				html: '<div id="id_inspect_route_info"></div>',
				listeners: {
					show: function() {
						InspectRoute.viewport.doLayout();
					}
				}
			}],
			listeners: {
				show: function() {
					InspectRoute.viewport.doLayout();
				},
				hide: function() {
					Ext.getCmp('id_main_view_left_1').hide();
					InspectRoute.viewport.doLayout();
				}
			},
			bbar: ['-', {
				text : '生成临时设备巡检计划', iconCls : 'addIcon', handler : function() {
					// 验证巡检目录下是否有巡检设备
					if (InspectRouteDetails.grid.store.getTotalCount() <= 0) {
						Ext.Msg.confirm('提示', '该巡检目录下没有任何巡检设备信息，是否仍要继续？', function(btn) {
							if ('yes' == btn) {
								InspectPlan.win.show();
							}
						});
					} else {
						InspectPlan.win.show();
					}
				}
			}],
			split: true
		}, {
			region: 'center', layout : 'fit',
			items: InspectRouteDetails.grid
		}]
		
	});
	
});