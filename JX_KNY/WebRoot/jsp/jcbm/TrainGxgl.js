Ext.onReady(function() {
	
	Ext.namespace('TrainGxgl');
	/** **************** 定义全局变量开始 **************** */
	TrainGxgl.labelWidth = 100;
	TrainGxgl.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** 查询表单 */
	TrainGxgl.searchForm = new Ext.form.FormPanel({
		tools: [{
			id: 'help', handler: function() {
				Ext.Msg.show({
				   title:'提示',
				   msg: '输入查询条件后按回车键可以进行快捷查询！',
				   buttons: Ext.MessageBox.OK,
				   icon: Ext.MessageBox.INFO
				});
			}
		}],
		title: '查询', region: 'north', labelWidth: TrainGxgl.labelWidth,
		height: 110, padding: 10, frame: true, collapsible: true,
		layout: 'column',
		defaults: {
			columnWidth: .5, layout: 'form',
			defaults: {
				xtype: 'textfield', width: TrainGxgl.fieldWidth, enableKeyEvents: true,
				listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.getKey()) {
							TrainGxgl.grid.store.load();
						}
					}
				}
			}
		},
		items: [{
			items:[{
				fieldLabel: '车辆车型代码', name: 'typeCode'
			}]
		}, {
			items:[{
				fieldLabel: '车辆车型名称', name: 'typeName'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				TrainGxgl.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				this.findParentByType('form').getForm().reset();
				TrainGxgl.grid.store.load();
			}
		}]
	});
	
	/** 承修车型列表 */
	TrainGxgl.grid = new Ext.yunda.Grid({
		region : 'center',
		singleSelect : true,
		loadURL : ctx + "/trainVehicleType!pageList.action",
		tbar: ['refresh',
			'-', '<span style="color:gray;">*&nbsp;选择并双击一条记录进行机车构型设置</span>'
		],
		fields : [{
			header : '主键',dataIndex : 'idx',hidden : true
		}, {
			header : '操作', dataIndex : '', editor : { xtype : 'hidden' },
			searchor : { xtype : 'hidden' },
			renderer : function(record) {
				var html = "";
				html = "<span><a href='#' onclick='TrainGxgl.configJcgx()'>设置构型</a></span>";
				return html;
			}
		}, {
			header : '车辆车型代码', dataIndex : 'typeCode'
		}, {
			header : '车辆车型名称', dataIndex : 'typeName'
		},{
			header : '简称', dataIndex:'shortName'
		}, {
			header : '车型种类', dataIndex : 'vehicleKindName'
		},{
			header:'客货类型', dataIndex:'vehicleType', hidden : true
		},{
			header:'描述', dataIndex:'description'
		}],
		// 双击记录进行机车构型配置
		toEditFn : function() {
			TrainGxgl.configJcgx();
		}
	});
	TrainGxgl.grid.store.on('beforeload', function() {
		var searchParams = TrainGxgl.searchForm.getForm().getValues();
		this.baseParams.entityJson = Ext.encode(MyJson.deleteBlankProp(searchParams));
	});

	/** 配置车辆构型 */
	TrainGxgl.configJcgx = function() {
		var sm = TrainGxgl.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		if (sm.getCount() > 1) {
			MyExt.Msg.alert('请只选择一条记录设置构型');
			return;
		}
		var record = sm.getSelections()[0];
		JcgxBuild.shortName = record.get('typeCode');
		JcgxBuild.win.show();
	};

	// 页面自适应布局
	new Ext.Viewport({
		layout : 'border', 
		items : [TrainGxgl.searchForm, TrainGxgl.grid]
	});

});