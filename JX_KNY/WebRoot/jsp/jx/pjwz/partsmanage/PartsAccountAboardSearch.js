/**
 * 配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('PartsAccountSearch'); // 定义命名空间
	PartsAccountSearch.fieldWidth = 120;
	PartsAccountSearch.labelWidth = 90;
	PartsAccountSearch.searchParam = {};
	// 显示扩展编号详情的函数
	PartsAccountSearch.searchExtendNo = function(rowIndex, formColNum) {
		var extendNoJson = PartsAccountSearch.grid.store.getAt(rowIndex)
				.get("extendNoJson");
		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson,
				formColNum);
	}
	//规格型号选择控件赋值函数
	PartsAccountSearch.callReturnFn=function(node,e){
	  PartsAccountSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  PartsAccountSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	PartsAccountSearch.getCurrentMonth = function(arg) {
		var Nowdate = new Date();// 获取当前date
		var currentYear = Nowdate.getFullYear();// 获取年度
		var currentMonth = Nowdate.getMonth();// 获取当前月度
		var currentDay = Nowdate.getDate();// 获取当前日
		var MonthFirstDay = new Date(currentYear - 1, currentMonth, currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	PartsAccountSearch.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/partsAccount!pageQuery.action', // 装载列表数据的请求URL
		border : false,
		viewConfig : null,
		tbar : null,
		fields : [{
					header : 'idx主键',
					dataIndex : 'idx',
					hidden : true,
					editor : {
						xtype : 'hidden'
					}
				}, {
					header : '配件型号表主键',
					dataIndex : 'partsTypeIDX',
					hidden : true,
					editor : {
						maxLength : 50
					}
				}, {
					header : '配件编号',
					dataIndex : 'partsNo',
					width : 150,
					editor : {
						allowBlank : false,
						maxLength : 50
					}
				}, {
					header : '配件识别码',
					dataIndex : 'identificationCode',
					width : 150,
					editor : {
						allowBlank : false,
						maxLength : 50
					}
				}, {
					header : '扩展编号',
					dataIndex : 'extendNoJson',
					searcher : {
						disabled : true
					},
					width : 150,
					renderer : function(v, metadata, record, rowIndex,
							colIndex, store) {
						var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin
								.getExtendNo(v);
						if (Ext.isEmpty(extendNo)) {
							return "";
						}
						var html = "";
						html = "<span><a href='#' onclick='PartsAccountSearch.searchExtendNo("
								+ rowIndex
								+ ", 1)'>"
								+ extendNo
								+ "</a></span>";
						return html;
					}
				}, {
					header : '配件名称',
					dataIndex : 'partsName',
					width : 150,
					editor : {
						maxLength : 100
					}
				}, {
					header : '规格型号',
					dataIndex : 'specificationModel',
					width : 200,
					editor : {
						maxLength : 100
					}
				}, {
					header : '配件状态',
					dataIndex : 'partsStatusName',
					editor : {
						maxLength : 50
					}
				}, {
					header : '状态修改日期',
					dataIndex : 'partsStatusUpdateDate',
					xtype : 'datecolumn',
					editor : {
						xtype : 'my97date'
					}
				}, {
					header : '计量单位',
					dataIndex : 'unit',
					hidden : true,
					editor : {
						maxLength : 20
					}
				}, {
					header : '生产厂家主键',
					dataIndex : 'madeFactoryIdx',
					hidden : true,
					editor : {
						maxLength : 5
					}
				}, {
					header : '生产厂家',
					dataIndex : 'madeFactoryName',
					hidden : true,
					editor : {
						maxLength : 50
					}
				}, {
					header : '出厂日期',
					dataIndex : 'factoryDate',
					hidden : true,
					xtype : 'datecolumn',
					editor : {
						xtype : 'my97date'
					}
				}, {
					header : '存放地点',
					dataIndex : 'location',
					editor : {
						maxLength : 100
					}
				}, {
					header : '详细配置',
					dataIndex : 'configDetail',
					hidden : true,
					editor : {
						maxLength : 200
					}
				}, {
					header : '责任部门ID',
					dataIndex : 'manageDeptId',
					hidden : true,
					editor : {
						maxLength : 50
					}
				}, {
					header : '责任部门',
					dataIndex : 'manageDept',
					editor : {
						maxLength : 50
					}
				}, {
					header : '责任部门类型',
					dataIndex : 'manageDeptType',
					hidden : true,
					editor : {
						xtype : 'numberfield',
						maxLength : 1
					}
				}, {
					header : '配件状态编码',
					dataIndex : 'partsStatus',
					hidden : true,
					editor : {
						maxLength : 50
					}
				}, {
					header : '是否新品',
					dataIndex : 'isNewParts',
					editor : {
						maxLength : 10
					}
				}, {
					header : '配件旧编号',
					dataIndex : 'oldPartsNo',
					editor : {
						maxLength : 50
					}
				}, {
					header : '下车车型',
					dataIndex : 'unloadTrainType',
					editor : {
						maxLength : 50
					}
				}, {
					header : '下车车型编码',
					dataIndex : 'unloadTrainTypeIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '下车车号',
					dataIndex : 'unloadTrainNo',
					editor : {
						maxLength : 50
					}
				}, {
					header : '下车修程编码',
					dataIndex : 'unloadRepairClassIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '下车修程',
					dataIndex : 'unloadRepairClass',
					editor : {
						maxLength : 50
					}
				}, {
					header : '下车修次编码',
					dataIndex : 'unloadRepairTimeIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '下车修次',
					dataIndex : 'unloadRepairTime',
					editor : {
						maxLength : 50
					}
				}, {
					header : '下车日期',
					dataIndex : 'unloadDate',
					xtype : 'datecolumn',
					editor : {
						xtype : 'my97date'
					}
				}, {
					header : '下车位置',
					dataIndex : 'unloadPlace',
					editor : {
						maxLength : 100
					}
				}, {
					header : '下车原因',
					dataIndex : 'unloadReason',
					editor : {
						maxLength : 100
					}
				}, {
					header : '上车车型编码',
					dataIndex : 'aboardTrainTypeIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '上车车型',
					dataIndex : 'aboardTrainType',
					editor : {
						maxLength : 50
					}
				}, {
					header : '上车车号',
					dataIndex : 'aboardTrainNo',
					editor : {
						maxLength : 50
					}
				}, {
					header : '上车修程编码',
					dataIndex : 'aboardRepairClassIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '上车修程',
					dataIndex : 'aboardRepairClass',
					editor : {
						maxLength : 50
					}
				}, {
					header : '上车修次编码',
					dataIndex : 'aboardRepairTimeIdx',
					hidden : true,
					editor : {
						maxLength : 8
					}
				}, {
					header : '上车修次',
					dataIndex : 'aboardRepairTime',
					editor : {
						maxLength : 50
					}
				}, {
					header : '上车日期',
					dataIndex : 'aboardDate',
					xtype : 'datecolumn',
					editor : {
						xtype : 'my97date'
					}
				}, {
					header : '上车位置',
					dataIndex : 'aboardPlace',
					editor : {
						maxLength : 100
					}
				}]
	});
	PartsAccountSearch.grid.store.setDefaultSort('partsStatusUpdateDate',
			'DESC');// 设置默认排序
	PartsAccountSearch.grid.un("rowdblclick", PartsAccountSearch.grid.toEditFn,
			PartsAccountSearch.grid);
	PartsAccountSearch.grid.store.on('beforeload', function() {
		PartsAccountSearch.searchParam = PartsAccountSearch.searchForm
				.getForm().getValues();
		var searchParam = PartsAccountSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		var sqlStr = " parts_status ='"+PARTS_STATUS_YSC+"' ";
		var whereList = [
		 {sql: sqlStr, compare: Condition.SQL} //查询状态为【已上车】的数据
		]
		for (prop in searchParam) {
			if (prop == 'aboardTrainTypeIdx' && searchParam[prop] != null) {
				whereList.push({
							propName : prop,
							propValue : searchParam[prop],
							compare : Condition.EQ,
							stringLike : false
						});
				continue;
			}
			if (prop == 'specificationModel' && searchParam[prop] != null) {
				whereList.push({
							propName : prop,
							propValue : searchParam[prop],
							compare : Condition.LIKE
						});
				continue;
			}
			if ('aboardDate' == prop) {
				var getDateVal_v = searchParam[prop];
				getDateVal_v = getDateVal_v.toString();
				var getDateVal = getDateVal_v.split(",");
				if (getDateVal instanceof Array) {
					if (getDateVal.length == 2) {
						if (getDateVal[1] != "" && getDateVal[1] != "undefind") {
							whereList.push({
										propName : 'aboardDate',
										propValue : getDateVal[0],
										compare : 4
									});
							whereList.push({
										propName : 'aboardDate',
										propValue : getDateVal[1] + ' 23:59:59',
										compare : 6
									});
						} else {
							whereList.push({
										propName : 'aboardDate',
										propValue : getDateVal[0],
										compare : 4
									});
						}
					} else {
						whereList.push({
									propName : 'aboardDate',
									propValue : getDateVal[0] + ' 23:59:59',
									compare : 6
								});
					}
				}
				continue;
			}else {
				whereList.push({
							propName : prop,
							propValue : searchParam[prop],
							compare : Condition.EQ
						});
				continue;
			}
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	PartsAccountSearch.searchForm = new Ext.form.FormPanel({
		baseCls : "x-plain",
		align : "center",
		defaultType : "textfield",
		defaults : {
			anchor : "98%"
		},
		layout : "form",
		border : false,
		style : "padding:10px",
		labelWidth : PartsAccountSearch.labelWidth,
		buttonAlign : "center",
		items : [{
			xtype : "panel",
			border : false,
			baseCls : "x-plain",
			layout : "column",
			align : "center",
			items : [{
						columnWidth : .33,
						labelWidth : PartsAccountSearch.labelWidth,
						layout : 'form',
						baseCls : 'x-plain',
						items : [{
									id : "trainType_comb_s",
									xtype : "TrainType_combo",
									fieldLabel : "上车车型",
									hiddenName : "aboardTrainTypeIdx",
									displayField : "shortName",
									valueField : "typeID",
									width : 140,
									pageSize : 20,
									minListWidth : 200, // isCx:'no',
									editable : false,
									listeners : {
										"select" : function() {
											// 重新加载修程下拉数据
											var rc_comb_s = Ext
													.getCmp("rc_comb_s");
											rc_comb_s.reset();
											rc_comb_s.clearValue();
											rc_comb_s.getStore().removeAll();
											rc_comb_s.queryParams = {
												"TrainTypeIdx" : this
														.getValue()
											};
											rc_comb_s.cascadeStore();
										}
									}
								}]
					}, {
						columnWidth : .33,
						layout : 'form',
						labelWidth : PartsAccountSearch.labelWidth,
						baseCls : 'x-plain',
						items : [{
									xtype : 'textfield',
									fieldLabel : '上车车号',
									name : "aboardTrainNo"
								}

						]
					}, {
						columnWidth : .33,
						layout : 'form',
						labelWidth : PartsAccountSearch.labelWidth,
						baseCls : 'x-plain',
						items : [{
									id : "rc_comb_s",
									xtype : "Base_combo",
									business : 'trainRC',
									entity : 'com.yunda.jx.base.jcgy.entity.TrainRC',
									fields : ['xcID', 'xcName'],
									fieldLabel : "上车修程",
									hiddenName : "aboardRepairClassIdx",
									displayField : "xcName",
									valueField : "xcID",
									pageSize : 20,
									minListWidth : 200,
									queryHql : 'from UndertakeRc',
									width : 140,
									editable : false
								}]
					},{
				baseCls : "x-plain",
				align : "center",
				layout : "form",
				defaultType : "textfield",
				labelWidth : 100,
				columnWidth : 0.6,
				items : [{
							xtype : 'compositefield',
							fieldLabel : '上车日期',
							combineErrors : false,
							items : [{
										id : "aboardDate",
										name : "aboardDate",
										xtype : "my97date",
										format : "Y-m-d",
										value : PartsAccountSearch
												.getCurrentMonth(),
										width : 150
									}, {
										xtype : 'label',
										text : '结束：'
									}, {
										id : "aboardDate_end",
										name : "aboardDate",
										xtype : "my97date",
										format : "Y-m-d",
										width : 150
									}]
						}]
			},{
							columnWidth : 0.3,
							labelWidth : PartsAccountSearch.labelWidth,
							layout : 'form',
							baseCls : 'x-plain',
							defaultType : "textfield",
							items : [{
								xtype : "PartsTypeTreeSelect",
								fieldLabel : '配件规格型号',
								id : 'PartsTypeTreeSelect_select',
								hiddenName : 'specificationModel',
								editable : false,
								width : PartsAccountSearch.fieldWidth,
								returnFn: PartsAccountSearch.callReturnFn
							},{ id:"partsTypeIDX", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true}]
						}]
		}],
		buttons : [{
			text : "查询",
			iconCls : "searchIcon",
			handler : function() {
				PartsAccountSearch.searchParam = PartsAccountSearch.searchForm
						.getForm().getValues();
				PartsAccountSearch.searchParam = MyJson
						.deleteBlankProp(PartsAccountSearch.searchParam);
				PartsAccountSearch.grid
						.searchFn(PartsAccountSearch.searchParam);
			}
		}, {
			text : "重置",
			iconCls : "resetIcon",
			handler : function() {
				PartsAccountSearch.searchForm.getForm().reset();
				Ext.getCmp("trainType_comb_s").clearValue();
				Ext.getCmp("rc_comb_s").clearValue();
				PartsAccountSearch.searchParam = {};
				PartsAccountSearch.grid.store.load();
			}
		}]
	});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [{
							layout : 'border',
							frame : true,
							items : [{
										region : 'north',
										collapsible : true,
										height : 160,
										title : '查询',
										items : [PartsAccountSearch.searchForm]
									}, {
										region : 'center',
										border : false,
										frame : true,
										layout : 'fit',
										items : [PartsAccountSearch.grid]
									}]
						}]
			});
});