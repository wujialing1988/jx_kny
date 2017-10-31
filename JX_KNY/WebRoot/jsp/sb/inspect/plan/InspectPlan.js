var now = new Date();
now.setMonth(now.getMonth() - 1);
var lastMonth = now.format('Y-m-d');

Ext.onReady(function() {
	
	Ext.ns('InspectPlan');
	
	/** **************** 定义私有变量开始 **************** */
	var dom, myChart;
	var option = {
		series: [{
			type: 'liquidFill',
			name: '巡检进度',
			radius: '90%',
			outline: {
				show: false
			},
//	        shape: 'path://M367.855,428.202c-3.674-1.385-7.452-1.966-11.146-1.794c0.659-2.922,0.844-5.85,0.58-8.719 c-0.937-10.407-7.663-19.864-18.063-23.834c-10.697-4.043-22.298-1.168-29.902,6.403c3.015,0.026,6.074,0.594,9.035,1.728 c13.626,5.151,20.465,20.379,15.32,34.004c-1.905,5.02-5.177,9.115-9.22,12.05c-6.951,4.992-16.19,6.536-24.777,3.271 c-13.625-5.137-20.471-20.371-15.32-34.004c0.673-1.768,1.523-3.423,2.526-4.992h-0.014c0,0,0,0,0,0.014 c4.386-6.853,8.145-14.279,11.146-22.187c23.294-61.505-7.689-130.278-69.215-153.579c-61.532-23.293-130.279,7.69-153.579,69.202 c-6.371,16.785-8.679,34.097-7.426,50.901c0.026,0.554,0.079,1.121,0.132,1.688c4.973,57.107,41.767,109.148,98.945,130.793 c58.162,22.008,121.303,6.529,162.839-34.465c7.103-6.893,17.826-9.444,27.679-5.719c11.858,4.491,18.565,16.6,16.719,28.643 c4.438-3.126,8.033-7.564,10.117-13.045C389.751,449.992,382.411,433.709,367.855,428.202z',
			data: [{
				name: '完成度',
				itemStyle: {
					normal: {
//						color: Constants.YclColor
					}
				}
			}],
			label: {
				normal:{
					textStyle: {
						fontSize: 50,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局变量开始 **************** */
	InspectPlan.searchParams = {};
	InspectPlan.labelWidth = 100;
	InspectPlan.fieldWidth = 180;
	InspectPlan.isSaveAndAdd =  false;
	
	// 机车检修作业计划基本信息显示模板
	InspectPlan.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td><span>计划名称：</span>{routeName}【{periodType}】</td>',
			'</tr>',
			'<tr>',
			'<td><span>计划编制人：</span>{partrolWorker}</td>',
			'</tr>',
			'<tr>',
			'<td><span>计划日期：</span>{planStartDate} 至 {planEndDate}</td>',
			'</tr>',
			'<tr>',
			'<td><span>处理状态：</span>{state}</td>',
			'</tr>',
		'</table>'
	);
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 重新加载树
	 */ 
	InspectPlan.reloadTree = function(path) {
		InspectPlan.tree.root.reload(function() {
//			if (!path) InspectPlan.tree.getSelectionModel().select(InspectPlan.tree.root);
		});
//		path = InspectPlan.path || path;
		if (path == undefined || path == "" || path == "###") {
			InspectPlan.tree.root.expand();
		} else {
			// 展开树到指定节点
			InspectPlan.tree.expandPath(path);
			InspectPlan.tree.selectPath(path);
		}
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	InspectPlan.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			url : ctx + "/inspectPlan!tree.action"
		}),
		tbar : ['-', {
			xtype: 'checkbox', checked: true, boxLabel: '周检', inputValue: '周检',
			listeners: {
				check: function(me) {
					InspectPlan.tree.root.reload();
				}
			}
		}, '&nbsp;', {
			xtype: 'checkbox', checked: true, boxLabel: '半月检', inputValue: '半月检',
			listeners: {
				check: function(me) {
					InspectPlan.tree.root.reload();
				}
			}
		}, '&nbsp;', {
			xtype: 'checkbox', checked: true, boxLabel: '月检', inputValue: '月检',
			listeners: {
				check: function(me) {
					InspectPlan.tree.root.reload();
				}
			}
		}, '&nbsp;', {
			xtype: 'checkbox', checked: true, boxLabel: '季检', inputValue: '季检',
			listeners: {
				check: function(me) {
					InspectPlan.tree.root.reload();
				}
			}
		}, '&nbsp;', {
			xtype: 'checkbox', checked: true, boxLabel: '临时', inputValue: '临时',
			listeners: {
				check: function(me) {
					InspectPlan.tree.root.reload();
				}
			}
		}],
		root : new Ext.tree.AsyncTreeNode({
			text : "设备巡检计划",
			id : "root",
			expanded : true
		}),
		rootVisible: true,
		autoScroll : true,
		animate : true,
		border : false,
		listeners : {
			expandnode: function( node ) {
				if (!node) return;
				if (Ext.isEmpty(node.firstChild)) {
					InspectPlanEquipment.grid.store.removeAll();
					return;
				}
				// 重新选择到树节点
				if (InspectPlan.path) {
					// 展开树到指定节点
					InspectPlan.tree.expandPath(InspectPlan.path);
					InspectPlan.tree.selectPath(InspectPlan.path);
				} else {
					// 树数据首次加载时，选择第一个树子节点
					this.selModel.select(node.firstChild);
				}
			},
			beforeload: function(node) {
				// 按巡检类型进行过滤
				var tbar = InspectPlan.tree.getTopToolbar();
				var checkboxs = tbar.findByType('checkbox');
				var periodTypes = [-1];
				for (var i = 0; i < checkboxs.length; i++) {
					if (checkboxs[i].checked) {
						periodTypes.push(checkboxs[i].getRawValue());
					}
				}
				this.loader.baseParams.periodTypes = Ext.encode(periodTypes);
				// 查询条件
				var form = InspectPlan.searchForm.getForm();
				var entityJson = form.getValues();
				MyJson.deleteBlankProp(entityJson);
				this.loader.baseParams.entityJson = Ext.encode(entityJson);
			}
		}
	});
	InspectPlan.tree.getSelectionModel().on('selectionchange', function(me, node) {
		if (!node) return;
		if ('root' == node.id) {
			MyExt.Msg.alert('不可以选择根结点，请重新选择！');
			InspectPlan.tree.getSelectionModel().clearSelections();
			InspectPlanEquipment.grid.store.removeAll();
			return;
		}
		// 声明一个InspectRoute.path，用于在重新加载树以后，能够再次选择之间的树节点
		InspectPlan.path = node.getPath();
		// 如果是单选，则重新加载巡检目录明细表格数据源
		InspectPlanEquipment.planIdx= node.id;
		InspectPlanEquipment.grid.store.load();
		// 显示巡检目录明细展示面板
		if (Ext.getCmp('id_main_view_left_1').hidden) {
			Ext.getCmp('id_main_view_left_1').show();
		}
		// 渲染巡检目录明细展示信息
		InspectPlan.tpl.overwrite(Ext.get('id_inspect_route_info'), node.attributes);
		
		// 设置左下方巡检进度“水球图”数据
		var yxjCount = node.attributes.yxjCount;
		var wxjCount = node.attributes.wxjCount;
		option.series[0].data[0].value = yxjCount / (yxjCount + wxjCount);
		// 计算是否已经延期
		var planEndDate = node.attributes.planEndDate;
		var delayDays = com.yunda.Common.getDelayDays(new Date(planEndDate), wxjCount);
		if (0 < delayDays) {
			option.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
		} else {
			option.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
		}
		if (option && typeof option === "object") {
			myChart.setOption(option, true);
		}
	});
	/** **************** 定义设备类别树结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	InspectPlan.searchForm = new Ext.form.FormPanel({
		padding: 10, labelWidth: 100,
		layout: 'column', 
		defaults: {
			layout: 'form', columnWidth: .33, defaults: {
				xtype: 'textfield', width: 140, enableKeyEvents: true,
				listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							var form = InspectPlan.searchForm.getForm();
							if (form.isValid) {
								InspectPlan.tree.root.reload( function() {
									this.focus();
									this.selectText();
								}, me);
							}
						}
					}
				}
			}
		},
		items: [{
			items: [{
				name: 'routeName', fieldLabel: '计划名称'
			}]
		}, {
			items: [{
				hiddenName : 'state', fieldLabel : '计划状态',
				xtype: 'combo', maxLength: 10,
				typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			    	fields: [ 'k', 'v' ],
			        data: [[
			            '', '全部'		// 未处理
	                 ],  [
	                      STATE_WCL, STATE_WCL		// 未处理
	                      ],  [
	                    STATE_YCL, STATE_YCL		// 已处理
	                ]]
			    }),
			    value: STATE_WCL,
			    valueField: 'k', displayField: 'v',
			    listeners: {
			    	select: function() {
			    		InspectPlan.tree.root.reload();
			    	}
			    }
			}]
		}, {
			items: [{
				xtype: 'compositefield', fieldLabel: '计划日期', combineErrors: false, anchor:'100%',
				items: [{
					xtype:'my97date', id: 'planStartDate', format:'Y-m-d', value:lastMonth, width: 100,
					// 日期校验器
					vtype:'dateRange', allowBlank: false,
					dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					xtype:'my97date', id: 'planEndDate', format:'Y-m-d', width: 100,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
				}]
			}]
		}]
	});
	/** **************** 定义查询表单结束 **************** */

	InspectPlan.viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>', frame: true, collapsible: true,
			layout: 'fit',
			items: InspectPlan.searchForm,
			buttonAlign: 'center',
			buttons: [{
				text: '查询', iconCls: 'searchIcon', handler: function() {
					var form = InspectPlan.searchForm.getForm();
					if (!form.isValid()) {
						return;
					}
					InspectPlan.tree.root.reload();
				}
			}, {
				text: '重置', iconCls: 'resetIcon', handler: function() {
					InspectPlan.searchForm.getForm().reset();
					InspectPlan.tree.root.reload();
				}
			}]
		}, {
			region: 'center', layout: 'border', border: false,
			items: [{
				id: 'id_main_view_left',
//				collapsible: true, collapseMode: 'mini', /*collapsed: true,*/
				region: 'west', width: 310,
				layout: 'border', defaults: {border: false},
				items: [{
					region: 'center', layout: 'fit',
					items: InspectPlan.tree,
					tbar: ['-', {
						text: '删除', iconCls: 'deleteIcon', handler: function() {
							var sm = InspectPlan.tree.getSelectionModel();
							var node = sm.getSelectedNode();
							if (Ext.isEmpty(node)) {
								MyExt.Msg.alert('尚未选择任何记录！');
								return;
							}
							Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn) {
								if ('yes' == btn) {
									if(self.loadMask)    self.loadMask.show();
									Ext.Ajax.request({
										url : ctx + '/inspectPlan!logicDelete.action',
										params: {ids : [node.id]},
										scope: InspectPlan.tree,
									    success: function(response, options) {
									        if(self.loadMask)    self.loadMask.hide();
									        var result = Ext.util.JSON.decode(response.responseText);
									        if (result.errMsg == null) {       //操作成功     
									        	alertSuccess();
									        	InspectPlan.path = null;
									        	// 执行回调函数
									        	this.root.reload();
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
							});
						}
					}, {
						text: '刷新', iconCls: 'refreshIcon', handler: function() {
							self.location.reload();
						}
					}]
				}, {
					id: 'id_main_view_left_1', 
					region: 'south', height: 350, hidden: true,
					frame: true, collapsible: true,
					title: '巡检计划基本信息',
					layout: 'border',
					items: [{
						region: 'center', xtype: 'container',
						html: '<div id="id_inspect_route_info"></div>'
					}, {
						region: 'south', height: 220,
						html: '<div id="piechart_equipment" style="width:300px;height:230px;"></div>',
						listeners: {
							afterrender: function() {
								dom = document.getElementById("piechart_equipment");
								myChart = echarts.init(dom);
							}
						}
					}],
					listeners: {
						show: function() {
							InspectPlan.viewport.doLayout();
						}
					}
				}],
				listeners: {
					show: function() {
						InspectPlan.viewport.doLayout();
					},
					hide: function() {
						Ext.getCmp('id_main_view_left_1').hide();
						InspectPlan.viewport.doLayout();
					}
				},
				split: true
			}, {
				region: 'center', layout : 'fit',
				items: InspectPlanEquipment.grid
			}]
		}]
		
	});
	
});