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
				   title:i18n.TrainGxgl.prompt,
				   msg: i18n.TrainGxgl.toSearch,
				   buttons: Ext.MessageBox.OK,
				   icon: Ext.MessageBox.INFO
				});
			}
		}],
		title:i18n.TrainGxgl.search, region: 'north', labelWidth: TrainGxgl.labelWidth,
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
				fieldLabel: i18n.TrainGxgl.typeCode, name: 'typeCode'
			}]
		}, {
			items:[{
				fieldLabel: i18n.TrainGxgl.typeName, name: 'typeName'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: i18n.TrainGxgl.search, iconCls: 'searchIcon', handler: function() {
				TrainGxgl.grid.store.load();
			}
		}, {
			text: i18n.TrainGxgl.reset, iconCls: 'resetIcon', handler: function() {
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
			'-', '<span style="color:gray;">*&nbsp;'+i18n.TrainGxgl.choiceEdit+'</span>'
		],
		fields : [{
			header : i18n.TrainGxgl.idx,dataIndex : 'idx',hidden : true
		}, {
			header : i18n.TrainGxgl.edit, dataIndex : '', editor : { xtype : 'hidden' },
			searchor : { xtype : 'hidden' },
			renderer : function(record) {
				var html = "";
				html = "<span><a href='#' onclick='TrainGxgl.configJcgx()'>"+i18n.TrainGxgl.setBuild+"</a></span>";
				return html;
			}
		}, {
			header : i18n.TrainGxgl.typeCode, dataIndex : 'typeCode'
		}, {
			header : i18n.TrainGxgl.typeName, dataIndex : 'typeName'
		},{
			header : i18n.TrainGxgl.shortName, dataIndex:'shortName'
		}, {
			header : i18n.TrainGxgl.vehicleKindName, dataIndex : 'vehicleKindName'
		},{
			header:i18n.TrainGxgl.vehicleType, dataIndex:'vehicleType', hidden : true
		},{
			header:i18n.TrainGxgl.description, dataIndex:'description'
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
			MyExt.Msg.alert(i18n.TrainGxgl.description);
			return;
		}
		if (sm.getCount() > 1) {
			MyExt.Msg.alert(i18n.TrainGxgl.onlyOne);
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