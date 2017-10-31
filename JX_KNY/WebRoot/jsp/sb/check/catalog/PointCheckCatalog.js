Ext.onReady(function() {

	Ext.ns('PointCheckCatalog'); // 自定义命名空间
	
	/** **************** 定义私有变量开始 **************** */
	var sm = new Ext.grid.CheckboxSelectionModel(),
		cm, store;
	var labelWidth = 100, fieldWidth = 140;
	var showInitDialog = true;		// 保证页面加载完成后只显示一次初始化数据的对话框
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 显示设备点检统计报表
	 */
	PointCheckCatalog.showReport = function() {
		var sm = PointCheckCatalog.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			return;
		}
		// 报表参数 - 设备编号
		var equipmentCode = sm.getSelections()[0].data.equipmentCode;
		// 报表参数 - 年月，格式形如：2016-09
		var latestCheckDate = sm.getSelections()[0].data.latestCheckDate;
		var queryMonth = new Date(latestCheckDate).format('Y-m');
		
		// 显示报表打印页面
		var cpt = "pointCheck/equipmentPointCheck2.cpt";
	   	report.view(cpt, "设备点检、运转记录", {
	   		queryMonth: queryMonth,
	   		equipmentCode: equipmentCode
   		});
	}
	/**
	 * 使用设备点检任务单初始化点检目录
	 */
	PointCheckCatalog.initDataByPointCheck = function() {
		Ext.Msg.confirm('提示', '是否确认使用设备点检任务单初始化点检目录？', function(btn) {
			// 在当前页面如果已经显示过初始化数据的对话框，则不进行第二次的自动显示
			showInitDialog = false;
			if ('yes' == btn) {
				$yd.request({
					url: ctx + '/pointCheckCatalog!initDataByPointCheck.action'
				}, function() {
					alertSuccess();
					PointCheckCatalog.grid.store.reload();
				});
			}
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	PointCheckCatalog.searchForm = new Ext.form.FormPanel({
		padding: 10,
		labelWidth: labelWidth,
		layout: 'column', 
		buttonAlign: 'center',
		defaults: {
			columnWidth: .33, layout: 'form', defaults: { 
				xtype: 'textfield', width: fieldWidth, enableKeyEvents: true, listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							PointCheckCatalog.grid.store.load();
						}
					}
				}
			}
		},
		items: [{
			items: [{
				fieldLabel: '设备名称', name: 'equipmentName'
			}]
		}, {
			items: [{
				fieldLabel: '设备编码', name: 'equipmentCode' 
			}]
		}, {
			items: [{
				fieldLabel: '设备类别', name: 'className'
			}]
		}],
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				PointCheckCatalog.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				this.findParentByType('form').getForm().reset();
				PointCheckCatalog.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 保存点检目录明细，添加已选择的设备到点检目录中
	 * @param equipmentIds 设备信息idx主键数组
	 */
	PointCheckCatalog.saveFn = function(equipmentIds, callback) {
		if (self.loadMask)
			self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/pointCheckCatalog!save.action',
			params : {
				equipmentIds : Ext.encode(equipmentIds)
			},
			scope : PointCheckCatalog.grid,
			success : function(response, options) {
				if (self.loadMask)
					self.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) { // 操作成功
					alertSuccess();
					this.store.reload({
						callback : callback
					});
				} else { // 操作失败
					alertFail(result.errMsg);
				}
			},
			// 请求失败后的回调函数
			failure : function(response, options) {
				if (self.loadMask)
					self.loadMask.hide();
				Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n"
						+ ('' || response.responseText));
			}
		});
	}
	/** **************** 定义全局函数结束 **************** */

	/** **************** 定义列表开始 **************** */
	PointCheckCatalog.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/pointCheckCatalog!queryPageList.action',
		saveURL : ctx + '/pointCheckCatalog!saveOrUpdate.action',
		deleteURL : ctx + '/pointCheckCatalog!logicDelete.action',
		tbar : [ 'add', 'delete', 'refresh', '-', {
			iconCls: 'messageIcon', tooltip: "I'll tell you!", handler: function() {
				Ext.Msg.alert('提示', ['<span style="color:red;">红色</span>记录表示该设备已超过<span style="color:red;">7天</span>未进行设备点检！'].join('<br/>'));
			}
		
		}, '-', {
			text: '设备点检情况（日）', iconCls: 'chartBarIcon', handler: function() {
				PointCheckStatistics.win.show();
			}
		}, '->', {
			text: '初始化数据', iconCls: 'dataRefreshIcon', tooltip: '使用设备点检任务单初始化点检目录！', handler: PointCheckCatalog.initDataByPointCheck,
			listeners: {
				render: function() {
					this.btnEl.setStyle('color', 'gray');
				}
			}
		}],
		viewConfig: false,
		fields : [ {
			dataIndex : 'idx', header : 'idx', hidden : true
		}, {
			dataIndex : 'equipmentIdx', header : 'equipmentIdx', hidden : true
		}, {
			dataIndex : 'orgName', header : '单位名称', width : 160, renderer: function(v, m) {
				m.attr = 'style="font-weight:bold"';
				return v;
			}
		}, {
			dataIndex : 'equipmentName', header : '设备名称', width : 220, renderer: function(v, m, r) {
				var latestCheckDate = r.get('latestCheckDate');
				if (Ext.isEmpty(latestCheckDate)) {
					m.attr = 'style="font-weight:bold"';
				} else {
					var now = new Date();
					now.setDate(now.getDate() - 7);
					if (now > new Date(latestCheckDate)) {
						m.attr = 'style="color:red;font-weight:bold"';
					}
				}
				return v;
			}
		}, {
			dataIndex : 'equipmentCode', header : '设备编码', width : 140
		}, {
			dataIndex : 'useWorkshop', header : '使用车间', width : 140
		}, {
			dataIndex : 'usePerson', header : '使用人', width : 80
		}, {
			dataIndex : 'latestCheckDate', header : '最近点检日期', align: 'center', renderer: function(v, m) {
				if (Ext.isEmpty(v)) {
					return '<div style="background:red;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;color:#fff;margin-left:10px;">' + '未点检' + '</div>';
				}
				var now = new Date();
				now.setDate(now.getDate() - 7);
				if (now > new Date(v)) {
					return '<a href="#" onclick="PointCheckCatalog.showReport()">' + v + '</a>';
				}
				return '<a href="#" onclick="PointCheckCatalog.showReport()">' + v + '</a>';
			}
		}, {
			dataIndex : 'className', header : '设备类别', width : 160, renderer: function(v, m, r) {
				return v + "(" + r.get('classCode') + ")";
			}
		}, {
			dataIndex : 'classCode', header : '类别编码', hidden : true
		}, {
			dataIndex : 'model', header : '型号', width : 80
		}, {
			dataIndex : 'specification', header : '规格', width : 80
		}, {
			dataIndex : 'mechanicalCoefficient', header : '机械系数', width : 65
		}, {
			dataIndex : 'electricCoefficient', header : '电气系数', width : 65
		}, {
			dataIndex : 'usePlace', header : '设置地点', width : 140
		}, {
			dataIndex : 'mechanicalRepairTeam', header : '机械维修班组', width : 80, hidden : true
		}, {
			dataIndex : 'electricRepairTeam', header : '电气维修班组', width : 80, hidden : true
		} ],

		addButtonFn : function() {
			EquipmentPrimaryInfoSelect.win.show();
		},
		
		toEditFn : function() {
			MyExt.Msg.alert('点击[<span style="color:blue;">最近点检日期</span>]可查看设备点检记录统计报表！');
		}

	});
	PointCheckCatalog.grid.store.on('beforeload', function() {
		var searchParams = PointCheckCatalog.searchForm.getForm().getValues();
		MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.encode(searchParams);
	});
	/**
	 * 设备点检目录没有数据时，提示使用设备点检任务单进行数据初始化
	 */
	PointCheckCatalog.grid.store.on('load', function(me, records, options) {
		var sp = PointCheckCatalog.searchForm.getForm().getValues();
		MyJson.deleteBlankProp(sp);
		if (showInitDialog && 0 >= records.length && 0 >= MyJson.getJsonLength(sp)) {
			PointCheckCatalog.initDataByPointCheck();
		}
	});
	/** **************** 定义列表结束 **************** */

	new Ext.Viewport({
		layout: 'border',
		defaults: {
			layout: 'fit', border: false, region: 'center'
		},
		items:[{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: [PointCheckCatalog.searchForm]
		},{
			items: [PointCheckCatalog.grid]
		}]
	});
	
});