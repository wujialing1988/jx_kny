/**
 * 设备故障统计查询
 */
Ext.onReady(function() {
	
	// 自定义命名空间
	Ext.ns('FaultStatistics');

	/** **************** 定义局部变量开始 **************** */
	var comboWidth = 100;
	/** **************** 定义局部变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	FaultStatistics.showDetail = function(record, rowIndex) {
		if (Ext.isEmpty(record)) {
			record = FaultStatistics.grid.getSelectionModel().getSelections()[0]
			rowIndex = FaultStatistics.grid.store.indexOfId(record.id);
		}
		FaultStatisticsDetail.record = record;
		FaultStatistics.rowIndex = rowIndex;
		if (FaultStatisticsDetail.win.hidden) {
			FaultStatisticsDetail.win.show();
		} else {
			FaultStatisticsDetail.initFn()
		}
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义统计表格开始 **************** */
	FaultStatistics.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/faultOrder!queryPageStatistics.action',
		
		// 设置表格store的idProperty为：“equipmentIdx”
		storeId: 'equipmentIdx',
		
		singleSelect: true,
		
		// 自定义表格工具栏
		tbar: ['refresh', '-', '<span style="color:gray;">双击一条记录查看设备故障详情！</span>', '->', '-', '统计日期：', {
			xtype: 'combo',
			id: 'id_statistics_date',
			store: new Ext.data.ArrayStore({
				fields: ['k', 'v'],
				data: [[0, '全部'], [1, '最近一月'], [3, '最近一季'], [6, '最近半年'], [12, '最近一年']]
			}),
			valueField: 'k', displayField: 'v',
			// Modified by hetao on 2017-02-17 默认统计“最近一月”的设备故障提票信息
			value: 1,
			triggerAction: 'all',
			mode: 'local',
			width: comboWidth,
			listeners: {
				// 监听combo选择事件，重新加载表格数据源
				select: function( combo, record, index ) {
					FaultStatistics.grid.store.load();
				}
			}
		}],
		
		// 设置表格分页大小为10条记录一页
		pageSize: 10,
		
		viewConfig: null,
		
		fields: [{
			dataIndex: 'equipmentIdx', header: '设备idx主键', hidden: true
		}, {
			dataIndex: 'equipmentName', header: '设备名称(编号)', hidden: true, renderer: function(v, m, r) {
				return [v, "(", r.get('equipmentCode'), ")"].join('');
			}, width: 260
		}, {
			dataIndex: 'equipmentCode', header: '设备名称(编码)', renderer: function(v, m, r) {
				// 如有疑问可以参加FaultStatistics.grid.store的load事件监听器处理方法
				return [r.get('equipmentName').split('\r')[0], "(", v , ")"].join('');
			}, width: 260
		}, {
			dataIndex: 'faultCount', header: '故障次数', width: 80, align: 'center', renderer: function(value, metadata) {
				metadata.attr="style=color:red;";
//				metadata.css = 'unEditCell';
				return ['<a href="#" onclick="FaultStatistics.showDetail()">', value, '</a>'].join('');
			}
		}, {
			dataIndex: 'makeFactory', header: '制造工厂', width: 200
		}, {
			dataIndex: 'specification', header: '规格'
		}, {
			dataIndex: 'model', header: '型号'
		}],
		
		// 取消表格的行双击事件
		toEditFn: function(grid, rowIndex, e) {
			FaultStatistics.showDetail(this.store.getAt(rowIndex), rowIndex);
		}
	});
	
	// 查询条件查询传值
	FaultStatistics.grid.store.on('beforeload', function() {
		var flag = Ext.getCmp('id_statistics_date').getValue();
		var entityJson = {};
		
		if ('' != reloadStartDate && '' != reloadEndDate) {
			entityJson.startDate = reloadStartDate;
			entityJson.endDate = reloadEndDate;
		}
		// 获取系统当前日期
		else if (0 != flag) {
			var now = new Date();
			now.setMonth(now.getMonth() - flag);
			entityJson.startDate = now;
		}
		this.baseParams.entityJson = Ext.encode(entityJson);
	});
	// 数据源加载完成后，拼接设备名称和设备编号，因为在Ext图形统计控件中，针对名称相同的设备只会显示一个柱状图
	FaultStatistics.grid.store.on('load', function( me, records, options ) {
		me.each(function(record) {
			record.set('equipmentName', record.get('equipmentName') + '\r' +　record.get('equipmentCode') )
		});
		
		reloadStartDate = '';
		reloadEndDate = '';
	});
	/** **************** 定义统计表格结束 **************** */
	
	/** **************** 定义统计柱状图开始 **************** */
	FaultStatistics.chart = new Ext.chart.ColumnChart({
		// 柱状图数据源引用表格数据源
		store: FaultStatistics.grid.store,
//        yField: 'faultCount',
        xField: 'equipmentName',
        // 必要
        url: ctx + '/frame/resources/ext-3.4.0/resources/charts.swf',
//        xAxis: new Ext.chart.CategoryAxis({
//            title: '设备名称'
//        }),
        yAxis: new Ext.chart.NumericAxis({
            title: '故障次数'/*,
            labelRenderer : Ext.util.Format.numberRenderer('0,0')*/
        }),
        extraStyle: {
            xAxis: {
                 labelRotation: -45
            },
            font: {
            	size: 15
            }
        },
        tipRenderer : function(chart, record, index, series) {
        	if ('全部' === Ext.getCmp('id_statistics_date').getRawValue()) {
        		return record.data.equipmentName + '\r发生故障：' + record.data.faultCount + '次！'
        	} else {
        		return record.data.equipmentName + '\r' + Ext.getCmp('id_statistics_date').getRawValue() + ' 故障：' + record.data.faultCount + '次！'
        	}
        	
        },
        series: [{
            type: 'column',
            yField: 'faultCount',
            style: {
                image:'bar.gif',
                mode: 'stretch',
                color:0x99BBE8
            }
        }]
	})
	/** **************** 定义统计柱状图结束 **************** */
	
	/** **************** 定义统计图(月度)开始 **************** */
	// 柱状图数据源引用表格数据源
	FaultStatistics.store = new Ext.data.JsonStore({
		autoLoad: true,
		root: 'root',
		idProperty: 'month',
		totalProperty:"totalProperty", 
		fields: ['month', 'count'],
		url: ctx + '/faultOrder!queryPageStatisticsByMonth.action',
		listeners: {
			beforeload: function() {
				var year = Ext.getCmp('id_fault_year').getValue();
				this.baseParams.entityJson = Ext.encode({
					year: year
				});
			}
		}
	});
	/** **************** 定义统计柱图(月度)结束 **************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		defaults: {
			layout: 'fit', region: 'center', border: false
		},
		items: [{
			title: '设备故障统计',
			width: 600, region: 'west', collapsible: true, collapseMode: 'mini',
			border: true, split: true,
			items: FaultStatistics.grid
		}, {
			items: [{
				xtype: 'tabpanel',
				activeTab: 0,
				items: [{
					title: '故障统计',
					tbar: ['<span style="color:gray;">按设备故障次数从高到低查询统计！</span>'],
					items: FaultStatistics.chart
				}, {
					title: '故障统计_<span style="color:red;display:inline;font-weight:bold;">月度</span>',
					tbar: ['<span style="color:gray;">按月度统计设备故障发生次数！</span>', '->', '-', '年份：', {
						id: 'id_fault_year',
						width: comboWidth,
						fieldLabel: '年度',
						hiddenName: 'planYear',
						xtype: 'combo',
				        store: function() {
				        	var data = [];
				        	var year = new Date().getFullYear();
				        	data.push([year + 1, year + 1 + '年']);			// 下一年
				        	for (var i = 0; i < 2; i++) {
				        		data.push([year - i, year - i + '年']);
				        	}
				        	return new Ext.data.SimpleStore({
							    fields: ['k', 'v'],
								data: data
							})
				        }(),
						valueField:'k', displayField:'v',
						value: new Date().getFullYear(),
						editable: false,
						triggerAction:'all',
						mode:'local',
						listeners: {
							select: function( combo, record, index ) {
								FaultStatistics.store.load();
							}
						}
					}],
					layout: {
			            type:'vbox', align:'stretch'
			        },
					defaults: { border: true, flex: 1 },
					items: [{
						/** 设备故障月度统计-柱状图 */
						xtype: 'columnchart',
						store: FaultStatistics.store,
						yField: 'count',
						xField: 'month',
				        // 必要
				        url: ctx + '/frame/resources/ext-3.4.0/resources/charts.swf',
				        yAxis: new Ext.chart.NumericAxis({
				            title: '故障次数'
				        }),
				        extraStyle: {
				            xAxis: { labelRotation: -45 },
				            font: { size: 15 }
				        },
				        tipRenderer : function(chart, record, index, series) {
				        	return record.data.month + '\r发生故障：' + record.data.count + '次！';
				        }
					}, {
						/** 设备故障月度统计-饼形图 */
						xtype: 'piechart',
						store: FaultStatistics.store,
				        // 必要
				        url: ctx + '/frame/resources/ext-3.4.0/resources/charts.swf',
						dataField: 'count',
			            categoryField: 'month',
						extraStyle: {
			                legend: {
			                	display: 'right', font: { family: 'Tahoma',  size: 12 }
			                }
			            }/*,
			            tipRenderer : function(chart, record, index, series) {
			            	if (Ext.isEmpty(record.data.count)) {
			            		return record.data.month + '无故障发生！';
			            	}
			            	return record.data.month + '\r发生故障：' + record.data.count + '次！';
			            }*/
					}]
				}]
			}]
		}]
	});
	
});