/**
 * 设备点检统计分析 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */

Ext.onReady(function() {
	
	Ext.ns('PointCheckStatistic');		// 自定义命名空间
	
	/** **************** 定义私有变量开始 **************** */
	var labelWidth = 100, fieldWidth = 140;
	PointCheckStatistic.rowIndex = 0;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 显示设备点检统计报表
	 */
	PointCheckStatistic.showReport = function() {
		var sm = PointCheckStatistic.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			return;
		}
		// 报表参数 - 设备编号
		var equipmentCode = sm.getSelections()[0].data.equipmentCode;
		// 报表参数 - 年月，格式形如：2016-09
		var year = PointCheckStatistic.searchForm.find('hiddenName', 'year')[0].getValue();
		var month = PointCheckStatistic.searchForm.find('hiddenName', 'month')[0].getValue();
		if (month.toString().length < 2) {
			month = "0" + month.toString();
		}
		var queryMonth = year + "-" + month;
		
		// 显示报表打印页面
		var cpt = "pointCheck/equipmentPointCheck2.cpt";
	   	report.view(cpt, "设备点检、运转记录", {
	   		queryMonth: queryMonth,
	   		equipmentCode: equipmentCode
   		});
	};
	
	PointCheckStatistic.equipInfo = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td style="width:60%;"><span>设备名称：</span>{equipmentName}</td>',
			'<td style="width:60%;"><span>设备编号：</span>{equipmentCode}</td>',
			'</tr>',
			'<tr>',
			'<td style="width:40%;"><span>点检次数：</span>{checkCount}</td>',
			'<td style="width:40%;"><span>漏检次数：</span>{omitCount}</td>',
			'</tr>',
		'</table>', {
	        comipled: true
	    }
	);
	
	/** 显示漏检详情 */
	PointCheckStatistic.omitWin = new Ext.Window({
		title: '设备点检（漏检）情况',
		width: 640,
		height: 583,
		resizable:false,
		closeAction:'hide',
		modal:true,
		layout: 'border',
		tbar: [{
			text: '返回', iconCls: 'backIcon', handler: function() {
				PointCheckStatistic.omitWin.hide();
			}
		}, '->', '-', {
			text: '上一条', iconCls:'moveUpIcon', handler: function() {
				if (--PointCheckStatistic.rowIndex < 0) {
					PointCheckStatistic.rowIndex = 0;
					MyExt.Msg.alert('已经是第一条记录！');
					return;
				}
				
				PointCheckStatistic.showOmit(PointCheckStatistic.rowIndex);
			}
		}, {
			text: '下一条', iconCls:'moveDownIcon', handler: function() {
				if (++PointCheckStatistic.rowIndex > PointCheckStatistic.grid.getStore().getCount() - 1) {
					PointCheckStatistic.rowIndex = PointCheckStatistic.grid.getStore().getCount() - 1;
					MyExt.Msg.alert('已经是最后一条记录！');
					return;
				}
				
				PointCheckStatistic.showOmit(PointCheckStatistic.rowIndex);
			}
		}],
		items:[{
			layout: 'fit', region: 'north', height: 85, baseCls: 'plain',
			items: [{
				xtype: 'fieldset', title: '基本信息', html: '<div id="omit_base_info"></div>'
			}]
		}, {
			region: 'center', xtype: 'container',
			html: '<iframe id="omitIframe" src="" scrolling="no" frameborder="0" height="100%" width="100%"></iframe>'
		}, {
			region: 'south', height: 95, xtype: 'container',
			style: 'padding:10px;font-family:华文楷体;color:gray;',
			html: [
			       '<div style="height:20px;">说明：</div>',
                   '<div style="height:20px;padding-left:30px;"><span style="border-radius:2px;padding:0 5px;background:#00ff00;">绿色</span>标识：表示该日期已进行点检；</div>',
                   '<div style="height:20px;padding-left:30px;"><span style="border-radius:2px;padding:0 5px;background:#ffdc35;">黄色</span>标识：表示该日期扫描设备生成了点检任务单，但未进行处理；</div>',
                   '<div style="height:20px;padding-left:30px;"><span style="border-radius:2px;padding:0 5px;background:#ff0000;color:#fff;">红色</span>标识：表示该日期漏检（未进行点检）；</div>'
                   ].join('')
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '点检记录', iconCls: 'printerIcon', handler: function() {
				PointCheckStatistic.showReport();
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				PointCheckStatistic.omitWin.hide();
			}
		}]
	});
	PointCheckStatistic.showOmit = function(index) {
		PointCheckStatistic.rowIndex = index;
		
		var record = PointCheckStatistic.grid.store.getAt(index);
		PointCheckStatistic.omitWin.show();
		PointCheckStatistic.equipInfo.overwrite(Ext.get('omit_base_info'), record.data);
		var searchForm = PointCheckStatistic.searchForm.getForm();
		var queryMonth = searchForm.findField('month').value + '';
		if (queryMonth && queryMonth.length == 1) {
			queryMonth = '0' + queryMonth;
		}
		var queryDate = searchForm.findField('year').value + '-' + queryMonth;
		var url = 'PointCheckOmit.jsp?equipmentCode=' + record.data.equipmentCode + '&queryDate=' + queryDate;
		document.getElementById('omitIframe').src = url;
	};
	
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	PointCheckStatistic.searchForm = new Ext.form.FormPanel({
		padding: 10,
		labelWidth: labelWidth,
		layout: 'column', 
		buttonAlign: 'center',
		defaults: {
			columnWidth: .33, layout: 'form', defaults: { 
				xtype: 'textfield', width: fieldWidth, enableKeyEvents: true, listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							PointCheckStatistic.grid.store.load();
						}
					}
				}
			}
		},
		items: [{
			items: [{
				fieldLabel: '年度',
				hiddenName: 'year',
				xtype: 'combo',
		        store: function(){
		        	var data = [];
		        	var year = new Date().getFullYear();
		        	for (var i = 0; i < 3; i++) {
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
						PointCheckStatistic.grid.store.load();
					}
				}
			}]
		}, {
			items: [{
				fieldLabel: '月份',
				hiddenName: 'month',
				xtype: 'combo',
				store: function(){
					var data = [];
					var year = new Date().getFullYear();
					for (var i = 1; i <= 12; i++) {
						data.push([i, i + '月']);
					}
					return new Ext.data.SimpleStore({
						fields: ['k', 'v'],
						data: data
					})
				}(),
				valueField:'k', displayField:'v',
				value: new Date().getMonth() + 1,
				editable: false,
				triggerAction:'all',
				mode:'local',
				listeners: {
					select: function( combo, record, index ) {
						PointCheckStatistic.grid.store.load();
					}
				}
			}]
		}, {
			items: [{
				fieldLabel: '设备名称（编号）', name: 'equipmentCode'
			}]
		}],
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				PointCheckStatistic.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				this.findParentByType('form').getForm().reset();
				PointCheckStatistic.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义表格开始 **************** */
	var timer;		// 通过【上一月】【下一月】解析会计查询时的定时器，防止频繁的发起Ajax请求
	PointCheckStatistic.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/pointCheck!queryPage2Statistic.action',
//		pageSize: 10,
		singleSelect: true,
		tbar: ['refresh', '<span style="color:gray;">双击一条记录查看该设备的点检（漏检）情况！</span>', '->', '-', {
			text: '上一月', iconCls: 'moveUpIcon', handler: function() {
				var yearField = PointCheckStatistic.searchForm.find('hiddenName', 'year')[0]; 
				var yearValue = yearField.getValue();
				var monthField = PointCheckStatistic.searchForm.find('hiddenName', 'month')[0];
				var monthValue = monthField.getValue();
				monthValue -= 1;
				if (0 >= monthValue) {
					monthValue = 12;
					yearValue -= 1;
				}
				yearField.setValue(yearValue);
				monthField.setValue(monthValue);
				
				// 利用定时器，防止频繁的发起Ajax请求
				if (timer) {
					clearTimeout(timer);
				}
				timer = setTimeout(function() {
					PointCheckStatistic.grid.store.load();
				}, 500);
			}
		}, {
			text: '下一月', iconCls: 'moveDownIcon', handler: function() {
				var yearField = PointCheckStatistic.searchForm.find('hiddenName', 'year')[0]; 
				var yearValue = yearField.getValue();
				var monthField = PointCheckStatistic.searchForm.find('hiddenName', 'month')[0];
				var monthValue = monthField.getValue();
				monthValue += 1;
				if (12 < monthValue) {
					monthValue = 1;
					yearValue += 1;
				}
				yearField.setValue(yearValue);
				monthField.setValue(monthValue);
				
				// 利用定时器，防止频繁的发起Ajax请求
				if (timer) {
					clearTimeout(timer);
				}
				timer = setTimeout(function() {
					PointCheckStatistic.grid.store.load();
				}, 500);
			}
		}],
		fields: [{
			dataIndex: 'equipmentName', header: '设备名称'
		}, {
			dataIndex: 'equipmentCode', header: '设备编码'
		}, {
			dataIndex: 'checkCount', header: '点检次数', renderer: function(v) {
				if (Ext.isEmpty(v)) {
					return '<div style="background:red;width:48px;height:15px;line-height:15px;text-align:center;border-radius:8px;color:#fff;margin-left:10px;">' + '未点检' + '</div>';
				}
				return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="PointCheckStatistic.showReport()">' + v + '</a></div>';
			}
		},{
			dataIndex: 'omitCount', header: '漏检次数', renderer: function(v, metaData, record, rowIndex, colIndex, store) {
				if (Ext.isEmpty(v)) {
					v = 0;
				}
				return '<div style="background:red;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" style="color:white;" onclick="PointCheckStatistic.showOmit(\''+ rowIndex +'\')">' + v + '</a></div>';
			}
		}],
		
		toEditFn : function(grid, rowIndex, e) {
			PointCheckStatistic.showOmit(rowIndex);
		}
	});
	PointCheckStatistic.grid.store.on('beforeload', function() {
		var searchParams = PointCheckStatistic.searchForm.getForm().getValues();
		MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.encode(searchParams);
	});
	/** **************** 定义表格结束 **************** */
	
	new Ext.Viewport({
		layout: 'border', 
		defaults: {
			region: 'center', layout: 'fit', border: false
		},
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: PointCheckStatistic.searchForm
		}, {
			items: PointCheckStatistic.grid
		}]	
	});
});