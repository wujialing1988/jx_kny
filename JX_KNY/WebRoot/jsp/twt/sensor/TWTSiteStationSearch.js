/**
 * 台位信息查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('TWTSiteStationSearch'); // 定义命名空间
	TWTSiteStationSearch.labelWidth = 70;
	TWTSiteStationSearch.fieldWidth = 100;
	TWTSiteStationSearch.stationCode = "";
	// 绑定传感器点击事件
	TWTSiteStationSearch.showSensor = function(idx) {
		var record = TWTSiteStationSearch.grid.store.getAt(idx);
		TWTSiteStationSearch.stationCode = record.get("stationCode");
		TWTSiteStationSearch.sensorWin.show();
		// 设置台位信息
		TWTSiteStationSearch.stationForm.find('fieldLabel', '台位编号')[0]
				.setValue(record.get("stationCode"));
		TWTSiteStationSearch.stationForm.find('fieldLabel', '台位名称')[0]
				.setValue(record.get("stationName"));
		TWTSiteStationSearch.sensorGrid.store.load();
	};
	TWTSiteStationSearch.searchForm = new Ext.form.FormPanel({
		baseCls : "x-plain",
		align : "center",
		defaultType : "textfield",
		defaults : {
			anchor : "98%"
		},
		layout : "form",
		border : false,
		style : "padding:10px",
		labelWidth : TWTSiteStationSearch.labelWidth,
		buttonAlign : "center",
		items : [{
			xtype : "panel",
			border : false,
			baseCls : "x-plain",
			layout : "column",
			align : "center",
			items : [{
						baseCls : "x-plain",
						align : "center",
						layout : "form",
						defaultType : "textfield",
						labelWidth : TWTSiteStationSearch.labelWidth,
						columnWidth : 0.25,
						items : [{
									id : "PartsUnloadRegister_takeOverEmp",
									fieldLabel : "台位编号",
									name : "stationCode"
								}

						]
					}, {
						baseCls : "x-plain",
						align : "center",
						layout : "form",
						defaultType : "textfield",
						labelWidth : TWTSiteStationSearch.labelWidth,
						columnWidth : 0.25,
						items : [{
									id : "partsName",
									name : "stationName",
									fieldLabel : "台位名称"
								}]
					}, {
						baseCls : "x-plain",
						align : "center",
						layout : "form",
						defaultType : "textfield",
						labelWidth : TWTSiteStationSearch.labelWidth,
						columnWidth : 0.3,
						items : [{
									xtype : 'checkbox',
									id : 'unBindSensor',
									boxLabel : '显示未绑定传感器台位',
									handler : function() {
										TWTSiteStationSearch.grid.store.load();
									}
								}]
					}, {
						baseCls : "x-plain",
						align : "center",
						layout : "form",
						defaultType : "textfield",
						labelWidth : TWTSiteStationSearch.labelWidth,
						columnWidth : 0.08,
						items : [{
							xtype : 'button',
							iconCls : "searchIcon",
							text : '查询',
							handler : function() {
								TWTSiteStationSearch.searchParam = TWTSiteStationSearch.searchForm
										.getForm().getValues();
								TWTSiteStationSearch.searchParam = MyJson
										.deleteBlankProp(TWTSiteStationSearch.searchParam);
								TWTSiteStationSearch.grid.store.load();
							}
						}

						]
					}, {
						baseCls : "x-plain",
						align : "center",
						layout : "form",
						defaultType : "textfield",
						labelWidth : TWTSiteStationSearch.labelWidth,
						columnWidth : 0.08,
						items : [{
							xtype : 'button',
							iconCls : "resetIcon",
							text : '重置',
							handler : function() {
								TWTSiteStationSearch.searchForm.getForm()
										.reset();
								TWTSiteStationSearch.searchParam = {};
								TWTSiteStationSearch.grid.store.load();
							}
						}]
					}]
		}]
	});
	// 台位信息列表
	TWTSiteStationSearch.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/tWTSensor!getSiteStationList.action', // 装载列表数据的请求URL
		singleSelect : true,
		fields : [{
					header : 'idx主键',
					dataIndex : 'idx',
					hidden : true,
					editor : {
						xtype : 'hidden'
					}
				}, {
					header : '台位编号',
					dataIndex : 'stationCode',
					editor : {
						maxLength : 20
					}
				}, {
					header : '台位名称',
					dataIndex : 'stationName',
					editor : {
						maxLength : 20
					}
				}, {
					header : '绑定传感器',
					dataIndex : 'boxCode',
					editor : {
						maxLength : 50
					},
					renderer : function(v, metadata, record, rowIndex,
							colIndex, store) {
						if (Ext.isEmpty(v)) {
							return "";
						}
						var html = "";
						html = "<span><a href='#' onclick='TWTSiteStationSearch.showSensor("
								+ rowIndex + ")'>" + v + "</a></span>";
						return html;
					}
				}],
		tbar : []
	});
	// 移除事件
	TWTSiteStationSearch.grid.un('rowdblclick',
			TWTSiteStationSearch.grid.toEditFn, TWTSiteStationSearch.grid);
	TWTSiteStationSearch.grid.store.on('beforeload', function() {
				var searchParams = TWTSiteStationSearch.searchForm.getForm()
						.getValues();
				delete searchParams.unBindSensor;
				var searchParams = MyJson.deleteBlankProp(searchParams);
				this.baseParams.entityJson = Ext.encode(searchParams);
				if (Ext.getCmp("unBindSensor").checked) {
					this.baseParams.unBindSensor = "true";
				} else
					this.baseParams.unBindSensor = "false";
			});
	// 台位信息显示表单
	TWTSiteStationSearch.stationForm = new Ext.form.FormPanel({
				style : 'padding-left:5px;',
				layout : 'column',
				labelWidth : 70,
				defaults : {
					xtype : 'container',
					columnWidth : .3,
					layout : 'form',
					defaults : {
						xtype : 'textfield',
						readOnly : true,
						width : TWTSiteStationSearch.fieldWidth,
						style : 'background:none; border: none;',
						anchor : '100%'
					}
				},
				items : [{
							items : [{
										fieldLabel : '台位编号'
									}]
						}, {
							items : [{
										fieldLabel : '台位名称'
									}]
						}]
			});
	// 传感器信息列表
	TWTSiteStationSearch.sensorGrid = new Ext.yunda.Grid({
				loadURL : ctx + '/tWTSensor!pageList.action', // 装载列表数据的请求URL
				singleSelect : true,
				fields : [{
							header : 'idx主键',
							dataIndex : 'idx',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						}, {
							header : '集线盒编号',
							dataIndex : 'boxCode',
							editor : {
								maxLength : 20
							}
						}, {
							header : '传感器编号',
							dataIndex : 'sensorCode',
							editor : {
								maxLength : 20
							}
						}, {
							header : '最小感应门限(mm)',
							dataIndex : 'minLimit',
							editor : {
								xtype : 'numberfield',
								maxLength : 10
							}
						}, {
							header : '最大感应门限(mm)',
							dataIndex : 'maxLimit',
							editor : {
								xtype : 'numberfield',
								maxLength : 10
							}
						}, {
							header : '检测周期',
							dataIndex : 'checkCycle',
							editor : {
								xtype : 'numberfield',
								maxLength : 10
							}
						}, {
							header : '安装位置',
							dataIndex : 'location',
							editor : {
								maxLength : 50
							}
						}],
				tbar : []
			});
	// 移除事件
	TWTSiteStationSearch.sensorGrid.un('rowdblclick',
			TWTSiteStationSearch.sensorGrid.toEditFn,
			TWTSiteStationSearch.sensorGrid);
	TWTSiteStationSearch.sensorGrid.store.on('beforeload', function() {
				var searchParams = {
					stationCode : TWTSiteStationSearch.stationCode
				};
				searchParams = MyJson.deleteBlankProp(searchParams);
				this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			});
	TWTSiteStationSearch.sensorWin = new Ext.Window({
				title : "传感器绑定信息查看",
				width : 800,
				height : 400,
				plain : true,
				closeAction : "hide",
				buttonAlign : 'center',
				layout : 'fit',
				maximizable : false,
				items : [{
							region : 'center',
							layout : 'fit',
							bodyBorder : false,
							items : {
								xtype : "panel",
								layout : "border",
								items : [{
											region : 'north',
											layout : "fit",
											height : 50,
											frame : true,
											items : [TWTSiteStationSearch.stationForm]
										}, {
											region : 'center',
											layout : 'fit',
											bodyBorder : false,
											items : [TWTSiteStationSearch.sensorGrid]
										}]
							}
						}],
				modal : true,
				buttons : [{
							text : "关闭",
							iconCls : "closeIcon",
							scope : this,
							handler : function() {
								TWTSiteStationSearch.sensorWin.hide();
							}
						}]
			});
	// 页面自适应布局
	var viewport = new Ext.Viewport({
				layout : 'border',
				defaults : {
					layout : 'fit'
				},
				items : [{
							region : 'north',
							height : 110,
							title : '查询	',
							collapsible : true,
							frame : true,
							items : TWTSiteStationSearch.searchForm
						}, {
							region : 'center',
							items : TWTSiteStationSearch.grid

						}]
			});
});