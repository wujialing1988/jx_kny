Ext.onReady(function() {
	
	Ext.ns('PointCheckStatistics');			// 自定义命名空间
	
	/** **************** 定义私有变量开始 **************** */
	var myChart0, myChart1;
	var option0 = {
		title : {
	    	text: '设备点检进度',
	        x:'center'
	    },
		series: [{
			type: 'liquidFill',
//			name: '点检进度',
			radius: '73%',
			outline: {
				show: false
			},
			data: [{
				name: '完成度',
				itemStyle: {
					normal: {
						color: Constants.YclColor
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
	option1 = {
		color: [Constants.YclColor, Constants.YyqColor],
	    title : {
	    	text: '设备点检统计',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c}台次 ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'left',
	        data: ['已点检','未点检']
	    },
	    series : [
	        {
	            name: '设备点检',
	            type: 'pie',
	            selectedMode: 'single',
	            radius : '73%',
	            center: ['50%', '60%'],
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	};
	var timer;
	/**
	 * 日点检统计情况展示模板
	 */
	var infoTpl = new Ext.XTemplate(
		'<marquee direction="down" height="25px" scrollamount="5" behavior="slide">',
			'<b>{[this.date(values.checkDate)]}</b>&nbsp;',
			'计划点检设备<span class="count_tip">{[values.yclCount + values.wclCount]}</span>台，',
			'已点检<span class="count_tip">{yclCount}</span>台，',
			'未点检<span class="count_tip">{wclCount}</span>台，',
			'整体完成度<span class="count_tip">{[this.calc(values.yclCount, values.wclCount)]}</span>！',
		'</marquee>', {
			compiled: true,
			calc: function(yclCount, wclCount){
				return Math.round(yclCount / (yclCount + wclCount) * 100) + "%";	
	        },
	        date: function(checkDate) {
	        	var day = checkDate.getDay();
	        	var dayFormat = null;
	        	switch (day) {
				case 0: dayFormat = "星期日"; break;
				case 1: dayFormat = "星期一"; break;
				case 2: dayFormat = "星期二"; break;
				case 3: dayFormat = "星期三"; break;
				case 4: dayFormat = "星期四"; break;
				case 5: dayFormat = "星期五"; break;
				case 6: dayFormat = "星期六"; break;
				default: break;
				}
	        	return Ext.util.Format.date(checkDate, "Y年m月d日") + "（" + dayFormat + "）"; 
	        }
		}
	);
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义统计表格开始 **************** */
	var grid = new Ext.yunda.Grid({
		loadURL: ctx + '/pointCheckCatalog!statisticsByDaily.action',
		page: false,
		singleSelect: true,
		storeAutoLoad: false,
		remoteSort: false,
		tbar: ['<b>点检日期：</b>', {
			text: '后一天', iconCls: 'moveLeftIcon', handler: function() {
				var tbar = grid.getTopToolbar();
				var checkDate = tbar.findByType('my97date')[0].getValue();
				checkDate.setDate(checkDate.getDate() - 1);
				tbar.findByType('my97date')[0].setValue(checkDate);
				if (timer) {
					window.clearTimeout(timer);
				}
				timer = setTimeout(function() {
					grid.store.load();
				}, 500);
			}
		}, '-', {
			xtype: 'my97date', id: 'pointCheckDate', my97cfg: {
				onpicked: function(me) {
					grid.store.load();
				}
			}
		}, '-', {
			text: '前一天', iconCls: 'moveRightIcon', handler: function() {
				var tbar = grid.getTopToolbar();
				var checkDate = tbar.findByType('my97date')[0].getValue();
				checkDate.setDate(checkDate.getDate() + 1);
				tbar.findByType('my97date')[0].setValue(checkDate);
				if (timer) {
					window.clearTimeout(timer);
				}
				timer = setTimeout(function() {
					grid.store.load();
				}, 500);
			}
		}, '->', '-', {
			text: '刷新', iconCls: 'refreshIcon', tooltip: '重新加载设备点检情况查询结果！',  handler: function() {
				grid.store.load();
			}
		}/*{
			text: '隐藏统计图表', iconCls: 'switchIcon', handler: function() {
				PointCheckStatistics.win.find('region', 'east')[0].toggleCollapse(true);
				if ('隐藏统计图表' == this.getText()) {
					this.setText('显示统计图表');
				} else {
					this.setText('隐藏统计图表');
				}
			}
		}*/],
		fields: [{
			dataIndex : 'idx', header : 'idx', hidden : true
		}, {
			dataIndex : 'equipmentCode', header : '设备编码', width : 140
		}, {
			dataIndex : 'equipmentName', header : '设备名称', width : 180
		}, {
			dataIndex : 'usePerson', header : '使用人', width : 80
		}, {
			dataIndex : 'state', header : '状态', renderer: function(v) {
				if ('已处理' == v) {
					return '<div style="background:'+ Constants.YclColor +';width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + '已点检' + '</div>';
				}
				return '<div style="background:red;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;color:#fff;margin-left:10px;">' + '未点检' + '</div>';
			}
		}, {
			dataIndex : 'checkEmp', header : '点检人'
		}],
		
		toEditFn: Ext.emptyFn
	});
	
	grid.store.on('beforeload', function() {
		var tbar = grid.getTopToolbar();
		var checkDate = tbar.findByType('my97date')[0].getValue();
		this.baseParams.entityJson = Ext.encode({
			checkDate: checkDate
		});
	});
	
	grid.store.on('load', function(store, records) {
		if (0 >= records) {
			MyExt.Msg.alert("设备点检目录为空，未查询到设备点检情况！");
			PointCheckStatistics.win.hide();
			return;
		}
		var yclCount = 0;		// 已点检总数
		var wclCount = 0;		// 未点检总数
		store.each(function(r) {
			if ('已处理' == r.get('state')) {
				yclCount++;
			} else {
				wclCount++;
			}
		});
		// 第一统计图表
		var dom = document.getElementById("chart0");
		if (!myChart0) {
			myChart0 = echarts.init(dom);
		}
		option0.series[0].data[0].value = yclCount / records.length;
		if (option0 && typeof option0 === "object") {
			myChart0.setOption(option0, true);
		}
		// 第二统计图表
		option1.series[0].data = [{
			value:yclCount, name:'已点检'
		}, {
			value:wclCount, name:'未点检'
		}]
		dom = document.getElementById("chart1");
		if (!myChart1) {
			myChart1 = echarts.init(dom);
		}
		if (option1 && typeof option1 === "object") {
			myChart1.setOption(option1, true);
		}
		
		// Added by hetao on 2017-05-03 在列表底部增加日点检情况展示
		infoTpl.overwrite(Ext.get('inspect-info'), {
			checkDate: (function() {
				var tbar = grid.getTopToolbar();
				return tbar.findByType('my97date')[0].getValue();
			})(),
			yclCount: yclCount,
			wclCount: wclCount
		});
	});
	/** **************** 定义统计表格结束 **************** */
	
	PointCheckStatistics.win = new Ext.Window({
		title: '设备点检情况（日）',
		iconCls: 'chartBarIcon',
		width: 1000, height: 650,
		layout: 'border',
		plain: true, autoShow: true, resizable: false, modal: true,
		closeAction: 'hide',
		defaults: {
			layout: 'fit'
		},
		items: [{
			region: 'center',
			layout: 'border', defaults: {
				xtype: 'container', region: 'center', layout: 'fit'
			},
			items: [{
				items: grid
			}, {
				region: 'south', height: 25,
				html: '<div id="inspect-info"></div>'
			}]
		}, {
			baseCls: 'plain',
			region: 'east', width: 400,
			layout: 'ux.row',
			defaults: {
				rowHeight: .5, xtype: 'container'
			},
			items: [{
				html: '<div id="chart1" style="height:100%;width:100%;"></div>'
			}, {
				html: '<div id="chart0" style="height:100%;width:100%;"></div>'
			}]
		}],
		listeners: {
			show: function() {
				grid.store.load();
			}
		}
	});
	PointCheckStatistics.win.show();
});