/**
 * 生产调度—机车检修日报 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('DailyReport');
	
	/** **************** 定义全局变量开始 **************** */
	DailyReport.labelWidth = 100;
	DailyReport.fieldWidth = 140;
	
	// 生产调度—机车检修日报基本信息显示模板
	DailyReport.tpl2 = new Ext.XTemplate(
		'<table>',
			'<tr>',
				'<td><span>机车型号：</span>{cxjc}{ch}</td>',
				'<td><span>修程修次：</span>{repairClassName}{repairTimeName}</td>',
			'</tr>',
			'<tr>',
				'<td><span>委修单位名称：</span>{wxdmc}</td>',
				'<td><span>承修段名称：</span>{cxdmc}</td>',
			'</tr>',
		'</table>'
	);
	
	/**
	 * 根据机车检修作业计划生成机车检修日报
	 * @param workPlanIDs 机车检修作业计划idx主键数组
	 */
	DailyReport.saveFn = function(workPlanIDs, callback) {
		if (self.loadMask) self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/dailyReport!insertDailyReport.action',
			params: {workPlanIDs: Ext.encode(workPlanIDs)},
			scope: DailyReport,
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.grid.store.reload({
		            	callback: callback
		            }); 
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	DailyReport.searchForm = new Ext.form.FormPanel({
		frame: true, padding: 10,
		labelWidth: DailyReport.labelWidth,
		buttonAlign: 'center',
		layout: 'column',
		defaults: {
			columnWidth: .25, layout: 'form', defaults: {
				width: DailyReport.fieldWidth - 40, xtype: 'textfield'
			}
		},
		items: [{
			items: [{
				fieldLabel: "车型", xtype: "TrainType_combo",	
				hiddenName: "cxjc",
				displayField: "shortName", valueField: "shortName",
				pageSize: 20
			}]		
		}, {
			items: [{
				fieldLabel: '车号', xtype: 'TrainNo_combo',
				hiddenName: "ch", 
				displayField: "trainNo", valueField: "trainNo",
				editable:true
			}]		
		}, {
			items: [{
				fieldLabel: '修程', xtype: "Base_combo",
    			business: 'trainRC',
				fields:['xcID','xcName'],
    			hiddenName: "repairClassName", 
    			displayField: "xcName", valueField: "xcName",
    			pageSize: 20,
    			queryHql: 'From UndertakeRc'
			}]		
		}, {
			items: [{
				fieldLabel: '修次', xtype: "Base_combo",
    			hiddenName: "repairTimeName", 
    			displayField: "rtName", valueField: "rtName",
    			fields: ["rtID","rtName"],
				entity: 'RT'
			}]		
		}],
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				DailyReport.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				var form = this.findParentByType('form');
				form.find('hiddenName', 'cxjc')[0].clearValue();
				form.find('hiddenName', 'ch')[0].clearValue();
				form.find('hiddenName', 'repairClassName')[0].clearValue();
				form.find('hiddenName', 'repairTimeName')[0].clearValue();
				DailyReport.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义日报表格开始 **************** */
	DailyReport.saveForm = new Ext.form.FormPanel({
		padding: 10, layout: 'column', frame: true,
		labelWidth: DailyReport.labelWidth,
		defaults: {columnWidth: .5, layout: 'form', defaults: {
			width: DailyReport.fieldWidth,
			xtype: 'my97date', format: 'Y-m-d', initNow: true
		}},
		items: [{
			items: [{
				fieldLabel: '走行公里', name: 'zxgl', xtype: 'numberfield', vtype: 'positiveInt', maxLength: 18
			}, {
				fieldLabel: '离段日期', name: 'ldrq'
			}, {
				fieldLabel: '开工日期', name: 'kgrq'
			}, {
				fieldLabel: '离厂日期', name: 'lcrq'
			}]
		}, {
			items: [{
				fieldLabel: '检修状态', name: 'jxzt', xtype: 'combo', maxLength: 10,
				typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			        fields: [ 'k', 'v' ],
			        data: [[1, '待修'], [2, '检修中'], [3, '修竣']]
			    }),
			    value: '检修中',
			    valueField: 'v', displayField: 'v'
			}, {
				fieldLabel: '到厂日期', name: 'dcrq'
			}, {
				fieldLabel: '竣工日期', name: 'jgrq'
			}, {
				fieldLabel: '回段日期', name: 'hdrq'
			}]
		}, {
			columnWidth: 1,
			items: [{
				fieldLabel: '备注', xtype: 'textarea', name: 'bz', width: 417, height: 70, maxLength: 200
			}, {
				fieldLabel: 'idx主键', xtype: 'hidden', name: 'idx'
			}]
		}]
	});
	
	DailyReport.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/dailyReport!pageQuery.action',
		saveURL: ctx + '/dailyReport!saveDailyReport.action',
		deleteURL: ctx + '/dailyReport!logicDelete.action',
		tbar: [{
			text: '根据机车检修作业计划生成', iconCls: 'addIcon', handler: function() {
				TrainWorkPlan.win.show();
			}
		}, 'add', 'delete', 'refresh'],
		saveForm: DailyReport.saveForm,
		viewConfig: null,
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'workPlanID', header: '检修作业计划ID', hidden: true
		}, {
			dataIndex: 'cxbm', header: '车型编码', hidden: true
		}, {
			dataIndex: 'cxjc', header: '车型', renderer: function(value, metadata, record){
				if (!Ext.isEmpty(record.get('ch'))){
					value += record.get('ch');
				};
				if (!Ext.isEmpty(record.get('jxzt'))){
					value += '<span style="color:green;">（' + record.get('jxzt') + '）</span>';
				};
				return value;
			}, width: 150
		}, {
			dataIndex: 'ch', header: '车号', hidden: true
		}, {
			dataIndex: 'repairClassName', header: '修程', renderer: function(value, metadata, record){
				if (Ext.isEmpty(record.get('repairTimeName'))) return value;
				return value + record.get('repairTimeName');
			}, width: 100
		}, {
			dataIndex: 'repairTimeName', header: '修次', hidden: true
		}, {
			dataIndex: 'zxgl', header: '走行公里', width: 80
		}, {
			dataIndex: 'wxdbm', header: '委修单位编码', hidden: true
		}, {
			dataIndex: 'wxdmc', header: '委修单位名称', width: 100
		}, {
			dataIndex: 'cxdbm', header: '承修段编码', hidden: true
		}, {
			dataIndex: 'cxdmc', header: '承修段名称', width: 100
		}, {
			dataIndex: 'ldrq', header: '离段日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'dcrq', header: '到厂日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'kgrq', header: '开工日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'jgrq', header: '竣工日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'lcrq', header: '离厂日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'hdrq', header: '回段日期',  xtype: "datecolumn", format:"Y-m-d"
		}, {
			dataIndex: 'jxzt', header: '检修状态', hidden: true
		}, {
			header: '入修在途周期', renderer: function(v, metaData, record) {
				// 入修在途周期 = 到厂日期 - 离段日期
				var dcrq = record.get('dcrq');
				var ldrq = record.get('ldrq');
				if (!Ext.isEmpty(dcrq) && !Ext.isEmpty(ldrq)) {
					return (dcrq.getTime() - ldrq.getTime()) / (24 * 60 * 60 * 1000);
				}
			}
		}, {
			header: '在修周期', renderer: function(v, metaData, record) {
				// 在修周期 = 竣工日期 - 到厂日期
				var jgrq = record.get('jgrq');
				var dcrq = record.get('dcrq');
				if (!Ext.isEmpty(jgrq) && !Ext.isEmpty(dcrq)) {
					return (jgrq.getTime() - dcrq.getTime()) / (24 * 60 * 60 * 1000);
				}
			}
		}, {
			header: '修竣在厂停时', renderer: function(v, metaData, record) {
				// 修竣在厂停时 = 离厂日期 - 竣工日期
				var lcrq = record.get('lcrq');
				var jgrq = record.get('jgrq');
				if (!Ext.isEmpty(lcrq) && !Ext.isEmpty(jgrq)) {
					return (lcrq.getTime() - jgrq.getTime()) / (24 * 60 * 60 * 1000);
				}
			}
		}, {
			header: '回送在途周期', renderer: function(v, metaData, record) {
				// 回送在途周期 = 回段日期 - 离厂日期
				var hdrq = record.get('hdrq');
				var lcrq = record.get('lcrq');
				if (!Ext.isEmpty(hdrq) && !Ext.isEmpty(lcrq)) {
					return (hdrq.getTime() - lcrq.getTime()) / (24 * 60 * 60 * 1000);
				}
			}
		}, {
			header: '全周期', renderer: function(v, metaData, record) {
				// 全周期 = 入修在途周期 + 在修周期 + 修竣在厂停时 + 回送在途周期
				var zq0 = 0, zq1 = 0, zq2 = 0, zq3 = 0;
				// 入修在途周期 = 到厂日期 - 离段日期
				var dcrq = record.get('dcrq');
				var ldrq = record.get('ldrq');
				if (!Ext.isEmpty(dcrq) && !Ext.isEmpty(ldrq)) {
					zq0 = (dcrq.getTime() - ldrq.getTime()) / (24 * 60 * 60 * 1000);
				}
				// 在修周期 = 竣工日期 - 到厂日期
				var jgrq = record.get('jgrq');
				var dcrq = record.get('dcrq');
				if (!Ext.isEmpty(jgrq) && !Ext.isEmpty(dcrq)) {
					zq1 = (jgrq.getTime() - dcrq.getTime()) / (24 * 60 * 60 * 1000);
				}
				// 修竣在厂停时 = 离厂日期 - 竣工日期
				var lcrq = record.get('lcrq');
				var jgrq = record.get('jgrq');
				if (!Ext.isEmpty(lcrq) && !Ext.isEmpty(jgrq)) {
					zq2 = (lcrq.getTime() - jgrq.getTime()) / (24 * 60 * 60 * 1000);
				}
				// 回送在途周期 = 回段日期 - 离厂日期
				var hdrq = record.get('hdrq');
				var lcrq = record.get('lcrq');
				if (!Ext.isEmpty(hdrq) && !Ext.isEmpty(lcrq)) {
					zq3 = (hdrq.getTime() - lcrq.getTime()) / (24 * 60 * 60 * 1000);
				}
				var allPeriod = zq0 + zq1 + zq2 + zq3;;
				return 0 === allPeriod ? "" : allPeriod;
			}
		}, {
			dataIndex: 'bz', header: '备注', width: 300
		}],
		afterShowEditWin: function(record, rowIndex){
			var data = record.data;
			if (Ext.isEmpty(data.jxzt)) {
				// 默认设置机车的检修状态为“检修中”
				this.saveForm.find('name', 'jxzt')[0].setValue('检修中');
			}
			// 回显机车检修日报的机车检修基本信息
			DailyReport.tpl2.overwrite(Ext.get('id_train_work_plan_base_info'), data);
			this.saveWin.setTitle('机车检修日报编辑');
		},
	    createSaveWin: function(){
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	            title:"新增", width:600, height: 380, closeAction:"hide",
	            layout: 'border',
	            iconCls: 'editIcon',
	            defaults: {layout: 'fit', border: false},
	            items: [{
					region: 'north', height: 100, layout: 'fit', frame: true,
					items: {
						xtype: 'fieldset', title: '基本信息',
						html: '<div id="id_train_work_plan_base_info"></div>'
					}
	            }, {
	            	region: 'center',
	            	items: this.saveForm
	            }], 
	            
	            buttonAlign:'center', 
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	    },
	    // 数据保存前验证各个日期的有效性
	    beforeSaveFn: function(data){ 
	    	// 入修在途周期 = 到厂日期 - 离段日期
			var dcrq = data.dcrq;
			var ldrq = data.ldrq;
			if (dcrq && ldrq && dcrq < ldrq) {
				MyExt.Msg.alert('到厂日期不能小于离段日期');
				return false;
			}
			// 在修周期 = 竣工日期 - 到厂日期
			var jgrq = data.jgrq;
			var dcrq = data.dcrq;
			if (jgrq && dcrq && jgrq < dcrq) {
				MyExt.Msg.alert('竣工日期不能小于到厂日期');
				return false;
			}
			// 修竣在厂停时 = 离厂日期 - 竣工日期
			var lcrq = data.lcrq;
			var jgrq = data.jgrq;
			if (lcrq && jgrq && lcrq < jgrq) {
				MyExt.Msg.alert('离厂日期不能小于竣工日期');
				return false;
			}
			// 回送在途周期 = 回段日期 - 离厂日期
			var hdrq = data.hdrq;
			var lcrq = data.lcrq;
			if (hdrq && lcrq && hdrq < lcrq) {
				MyExt.Msg.alert('回段日期不能小于离厂日期');
				return false;
			}
	    	return true; 
	    },
	    addButtonFn: function() {
	    	DailyReportAdd.win.show();
	    }
	});
	DailyReport.grid.store.on('beforeload', function(){
		var searchParams = DailyReport.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		var whereList = [];
		for(var prop in searchParams) {
			whereList.push({propName: prop, propValue: searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	/** **************** 定义日报表格结束 **************** */
	
	DailyReport.viewport = new Ext.Viewport({
		layout: 'border', defaults: {border: false, layout: 'fit'},
		items: [{
			title: '查询',
			collapsible: true,
			region: 'north', height: 120,
			items: DailyReport.searchForm
		}, {
			region: 'center',
			items: DailyReport.grid
		}]
	});
});