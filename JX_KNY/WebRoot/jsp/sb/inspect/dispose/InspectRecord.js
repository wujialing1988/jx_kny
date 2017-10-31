/**
 * 设备巡检记录Ext页面，该文件依赖于InspectPlanEquipment.js文件，引用时请注意
 */
Ext.onReady(function() {
	
	Ext.ns('InspectRecord');
	
	/** **************** 定义私有变量开始 **************** */
	var dom0, dom1, myChart0, myChart1;
	var option0 = {
		series: [{
			type: 'liquidFill',
			name: '巡检进度',
			radius: '96%',
			center: ['50%', '48%'],
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
						fontSize: 30,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	var option1 = {
		series: [{
			type: 'liquidFill',
			name: '巡检进度',
			radius: '96%',
			center: ['50%', '48%'],
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
						fontSize: 30,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局变量开始 **************** */
	InspectRecord.repairType = REPAIR_TYPE_JX;
	var record;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
	 */
	InspectRecord.initFn = function() {
		if (InspectPlanEquipment) {
			record = InspectPlanEquipment.grid.store.getById(InspectRecord.planEquipmentIdx);
		}
		
		// 重新加载巡检记录列表数据源
		InspectRecord.grid.store.load();
		
		// 渲染巡检目录明细展示信息
		$yd.request({
			url: ctx + '/inspectPlanEquipmentEmp!getModelsByPlanEquipmentIdx.action',
			params: {planEquipmentIdx: InspectRecord.planEquipmentIdx}
		}, function(result) {
			var list = result.list;
			Ext.get('inspect_plan_mac').dom.innerHTML = '<div style="padding:35px 0 0 20px;">无机械巡检标准<div>';
			Ext.get('inspect_plan_elc').dom.innerHTML = '<div style="padding:35px 0 0 20px;">无电气巡检标准<div>';
			Ext.each(list, function(entity) {
				if (record) {
					entity.mechanicalCoefficient = record.get('mechanicalCoefficient');
					entity.electricCoefficient = record.get('electricCoefficient');
				}
				if (REPAIR_TYPE_JX == entity.repairType) {
					InspectRecord.tpl2Mac.overwrite(Ext.get('inspect_plan_mac'), entity);
				} else if (REPAIR_TYPE_DQ == entity.repairType) {
					InspectRecord.tpl2Elc.overwrite(Ext.get('inspect_plan_elc'), entity);
				}
			});
		});
	}
	/**
	 * 查看照片
	 */
	InspectRecord.showImage = function() {
		ImageView.show(InspectRecord.grid, {
			title: '设备巡检照片'
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	
	/** **************** 定义巡检情况展示模板开始 **************** */
	var tpl = ['<table>',
				'<tr>',
					// 后续需替换会不同检修类型的系数
					'<td><span>系数：</span><b><u>{coefficient}</u></b></td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检人：</span>{inspectEmp}&nbsp;{entrustInspectEmp}</td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检结果：</span>',
						'<tpl if="checkResult == \'已巡检\'">',
							'<span style="color:#000;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + Constants.YclColor + '">{checkResult}</span>',
						'</tpl>',
						'<tpl if="checkResult == \'未巡检\'">',
							'<span style="color:#000;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + Constants.WclColor + '">{checkResult}</span>',
						'</tpl>',
					'</td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检情况描述：</span>{checkResultDesc}</td>',
				'</tr>',
			'</table>'];
	
	// 机械巡检情况基本信息显示模板
	InspectRecord.tpl2Mac = new Ext.XTemplate(tpl.join('').replace('系数', '机械系数').replace('coefficient', 'mechanicalCoefficient'));
	// 电气巡检情况基本信息显示模板
	InspectRecord.tpl2Elc = new Ext.XTemplate(tpl.join('').replace('系数', '电气系数').replace('coefficient', 'electricCoefficient'));
	/** **************** 定义巡检情况展示模板结束 **************** */
	
	/** **************** 定义可编辑列表开始 **************** */
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel({
		columns: [new Ext.grid.RowNumberer(), {
			dataIndex : 'idx', header : 'idx主键', hidden: true
		}, {
			dataIndex : 'checkItem', header : '检查项目', width: 3
		}, {
			dataIndex : 'checkResult', header : '检查结果', width: 1, editor: {
				xtype: 'combo',
		        store:new Ext.data.SimpleStore({
				    fields: ['k', 'v'],
					data: [[CHECK_RESULT_HG, CHECK_RESULT_HG], [CHECK_RESULT_BHG, CHECK_RESULT_BHG]]
				}),
				valueField:'k', displayField:'v',
				value: CHECK_RESULT_HG,
				editable: true,
				triggerAction:'all',
				mode:'local'
			},
			renderer: function(v, m) {
				m.css = 'editCell';
				return v;
			}
		}, {
			dataIndex: "checkTime", header: "巡检时间", width: 1, renderer: function(v) {
				if (v) return Ext.util.Format.date(new Date(v), 'Y-m-d H:i');
			}
		}, {
			dataIndex : 'inspectWorker', header : '巡检人', width: 1
		}, {
			header: "照片数量", dataIndex: "imageCount", width: 1, renderer: function(v, m, r) {
				if (v) {
					return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="InspectRecord.showImage()">' + v + '</a></div>';
				}
			}
		}]
	});
	
	/**
	 * 数据源
	 */
	store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: false,
        remoteSort: true,
        url: ctx + '/inspectRecord!queryPageList.action',
        fields: ['idx', 'inspectWorker', 'checkItem', 'checkTime', 'checkResult', 'imageCount'],
        listeners: {
        	beforeload: function() {
        		this.baseParams.entityJson = Ext.encode({
        			planEquipmentIdx: InspectRecord.planEquipmentIdx,
        			repairType: InspectRecord.repairType
        		});
        	}
        }
    }); 
	InspectRecord.grid = new Ext.grid.EditorGridPanel({
		entity: 'com.yunda.sb.inspect.record.entity.InspectRecord',
		cm: cm,
		sm: sm,
        store: store,
        loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        clicksToEdit: 1,
        stripeRows: true,
        viewConfig: {
        	forceFit: true
        },
        tbar : [{
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械&nbsp;&nbsp;', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectRecord.repairType = me.getRawValue();
						InspectRecord.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectRecord.repairType = me.getRawValue();
						InspectRecord.grid.store.load();
					}
				}
			}
		}, '->', {
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: InspectRecord.showImage
		}],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store}),
        listeners: {
        	afteredit: function(e) {
        		e.grid.fireEvent('update', this, e.record);
        	},
        	update: function( me, record, operation ) {
        		var values = record.getChanges();
        		// 获取最后一次编辑的字段名称
        		var checkResult;
        		checkResult = values.checkResult;
        		var idx = record.id;
        			
        		// 提交后台进行数据存储
        		$yd.request({
        			scope: this,
        			url: ctx + '/inspectRecord!updateFinished.action',
        			params: {
        				idx: idx,
        				checkResult: checkResult
        			}}, function(result) {
        				alertSuccess();
	        			InspectRecord.grid.store.reload();
	        			InspectDispose.tree.root.reload();
        			}
        		);
        	}
        }
    });
	InspectRecord.grid.store.on('load', function() {
		// 加载巡检记录_机械饼图数据源
		InspectRecord.piechartStore_jx.load();
		// 加载巡检记录_电气饼图数据源
		InspectRecord.piechartStore_dq.load();
	});
	/** **************** 定义可编辑列表结束 **************** */
		
	/** **************** 定义巡检记录饼图数据源开始 **************** */
	// 机械
	InspectRecord.piechartStore_jx = new Ext.data.JsonStore({
		fields: ['K', 'V'],
		idProperty: 'K',
		url: ctx + '/inspectRecord!queryChartData.action',
		listeners: {
			beforeload: function() {
				this.baseParams.repairType = REPAIR_TYPE_JX;
				this.baseParams.planEquipmentIdx = InspectRecord.planEquipmentIdx;
			},
			// 设置巡检进度“水球图”数据
			load: function(store, records) {
				var yxjCount = records[0].data.V;
				var wxjCount = records[1].data.V;
				
				if (0 === (yxjCount + wxjCount)) {
					option0.series[0].data[0].name = "无";
					delete option0.series[0].data[0].value;
				} else {
					option0.series[0].data[0].value = yxjCount / (yxjCount + wxjCount);
				}
				// 计算是否已经延期
				if (record) {
					var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), wxjCount);
					if (0 < delayDays) {
						option0.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
					} else {
						option0.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
					}
				}
				if (option0 && typeof option0 === "object") {
					myChart0.setOption(option0, true);
				}
			}
		}
	})
	// 电气
	InspectRecord.piechartStore_dq = new Ext.data.JsonStore({
		fields: ['K', 'V'],
		idProperty: 'K',
		url: ctx + '/inspectRecord!queryChartData.action',
		listeners: {
			beforeload: function() {
				this.baseParams.repairType = REPAIR_TYPE_DQ;
				this.baseParams.planEquipmentIdx = InspectRecord.planEquipmentIdx;
			},
			// 设置巡检进度“水球图”数据
			load: function(store, records) {
				var yxjCount = records[0].data.V;
				var wxjCount = records[1].data.V;
				
				if (0 === (yxjCount + wxjCount)) {
					option1.series[0].data[0].name = "无";
					delete option1.series[0].data[0].value;
				} else {
					option1.series[0].data[0].value = yxjCount / (yxjCount + wxjCount);
				}
				// 计算是否已经延期
				if (record) {
					var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), wxjCount);
					if (0 < delayDays) {
						option1.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
					} else {
						option1.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
					}
				}
				if (option1 && typeof option0 === "object") {
					myChart1.setOption(option1, true);
				}
			}
		}
	})
	/** **************** 定义巡检记录饼图数据源结束 **************** */

	InspectRecord.win = new Ext.Window({
		title: '设备巡检记录',
		width: 900, height: 550,
		closeAction: 'hide',
		plain: true, modal: true,
		layout: 'border',
		defaults: {layout: 'fit', border: false},
		items: [{
			region: 'north', height: 140, layout: 'hbox', baseCls: 'plain', style: 'background:rgb(223, 232, 246);', 
			defaults: {flex: 1, xtype: 'fieldset', height: 140},
			items: [{
				title: '机械',
				layout: 'border', items: [{
					region: 'center', xtype: 'container',
					html: '<div id = "inspect_plan_mac">无机械巡检标准</div>'
				}, {
					region: 'east',	width: 180, xtype: 'container',
					html: '<div id="piechart_jx" style="height:105px;width:160px;"></div>',
					listeners: {
						afterrender: function() {
							dom0 = Ext.get('piechart_jx').dom;
							myChart0 = echarts.init(dom0);
						}
					}
				}]
			}, {
				title: '电气',
				layout: 'border', items: [{
					region: 'center', xtype: 'container',
					html: '<div id = "inspect_plan_elc">无电气巡检标准</div>'
				}, {
					region: 'east',	width: 180, xtype: 'container',
					html: '<div id="piechart_dq" style="height:105px;width:160px;"></div>',
					listeners: {
						afterrender: function() {
							dom1 = Ext.get('piechart_dq').dom;
							myChart1 = echarts.init(dom1);
						}
					}
				}]
			}]
		}, {
			region: 'center',
			items: InspectRecord.grid
		}],
		listeners: {
			show: function() {
				// 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
				InspectRecord.initFn();
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '一键处理', iconCls: 'wrenchIcon', handler: function() {
				Ext.Msg.confirm('提示', '一键处理将会设置所有未处理的巡检记录状态为：<span style="color:red">合格</span>，是否确认继续？', function(btn) {
					if ('yes' == btn) {
						$yd.request({
		        			url: ctx + '/inspectPlanEquipment!updateFinishedByOneKey.action',
		        			params: {
		        				idx: InspectRecord.planEquipmentIdx
		        			}
		        		}, function () {
		        			alertSuccess();
		        			InspectDispose.tree.root.reload();
		        			InspectRecord.grid.store.reload();
		        		});
					}
				});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				InspectPlanEquipment.rowIndex = -1;
				this.findParentByType('window').hide();
			}
		}]
	});
	
});