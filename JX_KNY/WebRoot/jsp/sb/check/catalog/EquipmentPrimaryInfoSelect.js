Ext.onReady(function(){
	
	Ext.ns('EquipmentPrimaryInfoSelect');
	
	/** **************** 定义查询表单开始 **************** */
	EquipmentPrimaryInfoSelect.searchForm = new Ext.form.FormPanel({
		region: 'north', height: 50, baseCls: 'x-plain',
		padding: 10, fieldWidth: 100,
		layout: 'column',
		defaults: {columnWidth: .33, baseCls: 'x-plain', layout: 'form', defaults: {
			xtype: 'textfield', width: 140, enableKeyEvents: true, listeners: {
				keyup: function(me, e) {
					if (e.ENTER === e.keyCode) {
						EquipmentPrimaryInfoSelect.grid.store.load();
					}
				}
			}
		}},
		items: [{
			items: [{
				name: 'className', fieldLabel: '设备类别'
			}]
		}, {
			items: [{
				name: 'usePerson', fieldLabel: '包机人'
			}]
		}, {
			layout: 'column',
			defaults: {
				columnWidth: .5, layout: 'form', baseCls: 'x-plain',
				defaults: {
					 xtype: 'button', iconCls: 'searchIcon', width: 70, style: 'margin-left:40px;'
				}
			},
			items: [{
				items: [{
					text: '查询', handler: function() {
						EquipmentPrimaryInfoSelect.grid.store.load();
					}
				}]
			}, {
				items: [{
					text: '重置', iconCls: 'resetIcon', handler: function() {
						EquipmentPrimaryInfoSelect.searchForm.getForm().reset();
						Ext.getCmp('id_equipment_query').reset();
						EquipmentPrimaryInfoSelect.grid.store.load();
					}
				}]
			}]
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义设备选择表格开始 **************** */
	EquipmentPrimaryInfoSelect.grid = new Ext.yunda.Grid({
		region: 'center',
		loadURL: ctx + '/equipmentPrimaryInfo!pageQuery.action',
		storeAutoLoad : false, 
		viewConfig : false,
		tbar : [{
			text: '返回', iconCls: 'backIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}, '-', new Ext.form.TwinTriggerField({
			id: 'id_equipment_query',
		    trigger1Class:'x-form-clear-trigger',
		    trigger2Class:'x-form-search-trigger',
//		    hideTrigger1:true,
		    onTrigger1Click: function(e) {
		    	this.reset();
		    	EquipmentPrimaryInfoSelect.grid.store.load();
		    },
		    onTrigger2Click: function(e) {
		    	EquipmentPrimaryInfoSelect.grid.store.load();
		    },
		    width: 180, emptyText: '设备名称或编号快速检索', enableKeyEvents: true, listeners: {
				// 输入完成后按回车键查询
				keyup: function( me, e ) {
					if (e.ENTER == e.keyCode || Ext.isEmpty(me.getValue())) {
						EquipmentPrimaryInfoSelect.grid.store.load({
							callback: function() {
								me.selectText();
							}
						});
					}
				}
			}
		}), '<span style="color:gray;">输入完成后按回车键查询！</span>', '->', '-', {
			xtype: 'checkbox', checked: true, boxLabel: '新购', inputValue: DYNAMIC_NEW_BUY,
			listeners: {
				check: function( me, checked ) {
					EquipmentPrimaryInfoSelect.grid.store.load();
				}
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			xtype: 'checkbox', checked: true, boxLabel: '调入', inputValue: DYNAMIC_IN,
			listeners: {
				check: function( me, checked ) {
					EquipmentPrimaryInfoSelect.grid.store.load();
				}
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;'],	
		fields : [{
			header: "idx", dataIndex: "idx",  hidden: true
		},{
			header: "单位名称", dataIndex: "orgName", width: 160
		},{
			header: "设备名称", dataIndex: "equipmentName", width: 200
		},{
			header: "设备编号", dataIndex: "equipmentCode", width: 140
		},{
			header: '设置地点', dataIndex: 'usePlace'
		},{
			header: "类别(编号)", dataIndex: "className", hidden: true, renderer: function(value, metaData, record) {
				if (Ext.isEmpty(record.get('classCode'))) {
					return value;
				}
				return value + "(" + record.get('classCode') + ")";
			}, width: 180
		},{
			header: "类别编码", dataIndex: "classCode", hidden: true
		},{
			header: "型号", dataIndex: "model"
		},{
			header: "规格", dataIndex: "specification"
		},{
			header: "机械系数", dataIndex: "mechanicalCoefficient", width: 60, align: 'center'
		},{
			header: "电气系数", dataIndex: "electricCoefficient", width: 60, align: 'center'
		},{
			header: "包机人", dataIndex: "usePerson"
		},{
			header: "固资编号", dataIndex: "fixedAssetNo"
		},{
			header: "固资原值", dataIndex: "fixedAssetValue"
		},{
			header: "使用年月", dataIndex: "useDate", xtype: "datecolumn", format: 'Y-m-d'
		},{
			header: "设置地点", dataIndex: "usePlace", hidden: true
		},{
			header: "制造工厂", dataIndex: "makeFactory"
		},{
			header: "制造年月", dataIndex: "makeDate", xtype: "datecolumn", format: 'Y-m-d'
		}],
		toEditFn : function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			// 保存设备点检目录
			PointCheckCatalog.saveFn([record.id], function(){
				// 重新加载设备信息列表，过滤掉已被添加的设备信息
				EquipmentPrimaryInfoSelect.grid.store.reload();
			});
		}
	});
	EquipmentPrimaryInfoSelect.grid.store.on('beforeload', function() {
		var whereList = [];
		// 过滤掉已被添加的设备信息
		whereList.push({
			compare : Condition.SQL, sql : "IDX NOT IN (SELECT EQUIPMENT_IDX FROM E_SBJX_POINT_CHECK_CATALOG WHERE RECORD_STATUS = '0' )"
		});
		// 只查询“新购”和“调入”的设备
		var tbar = EquipmentPrimaryInfoSelect.grid.getTopToolbar();
		var dynamicCheckbox = tbar.findByType('checkbox');
		var propValues = [];
		for (var i in dynamicCheckbox) {
			if (dynamicCheckbox[i].checked) {
				propValues.push(dynamicCheckbox[i].getRawValue());
			}
		}
		whereList.push({
			compare : Condition.IN, propName : 'dynamic', propValues : propValues
		});
		// 查询设备名称或设备类别
		var searchValue = Ext.getCmp('id_equipment_query').getValue();
		if (!Ext.isEmpty(searchValue)) {
			whereList.push({
				compare : Condition.SQL, sql : "(EQUIPMENT_NAME LIKE '%"+ searchValue +"%' OR CLASS_NAME LIKE '%"+ searchValue +"%' OR EQUIPMENT_CODE LIKE '%"+ searchValue +"%' OR USE_PERSON LIKE '%"+ searchValue +"%')"
			});
		}
		// 查询表格条件
		var searchParams = EquipmentPrimaryInfoSelect.searchForm.getForm().getValues();
		MyJson.deleteBlankProp(searchParams);
		for (var prop in searchParams) {
			if ('className' === prop) {
				whereList.push({compare : Condition.SQL, sql: "(CLASS_NAME LIKE '%" + searchParams[prop] + "%' OR CLASS_CODE LIKE '%" + searchParams[prop] + "%')"});
				continue;
			}
			if ('usePerson' === prop) {
				whereList.push({compare : Condition.SQL, sql: "(USE_PERSON LIKE '%" + searchParams[prop] + "%' OR USE_PERSON_ID LIKE '%" + searchParams[prop] + "%')"});
				continue;
			}
			whereList.push({propName: prop, propValue: searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	/** **************** 定义设备选择表格结束 **************** */
	
	EquipmentPrimaryInfoSelect.win = new Ext.Window({
		title : '点检设备选择<span style = "color:green; font-weight:normal;">（可双击一条记录进行快速添加！）</span>',
		maximized : true,
		width : 1000, height : 600,
		modal : true,
		layout : 'border',
		closeAction : 'hide',
		items : [EquipmentPrimaryInfoSelect.grid, EquipmentPrimaryInfoSelect.searchForm],
		buttonAlign : 'center',
		buttons : [{
			text : '添加', iconCls : 'addIcon', handler : function() {
				var sm = EquipmentPrimaryInfoSelect.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				// 巡检设备idx主键数组
				var equipmentIds = [];
				var selections = sm.getSelections();
				for (var i = 0; i < selections.length; i++) {
					equipmentIds.push(selections[i].id);
				}
				// 保存设备点检目录
				PointCheckCatalog.saveFn(equipmentIds, function(){
					// 重新加载设备信息列表，过滤掉已被添加的设备信息
					EquipmentPrimaryInfoSelect.grid.store.reload();
				});
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners : {
			show : function() {
				EquipmentPrimaryInfoSelect.grid.store.load();
			}
		}
	});
	
});