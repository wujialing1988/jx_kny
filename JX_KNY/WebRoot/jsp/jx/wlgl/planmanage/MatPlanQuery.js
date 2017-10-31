/**
 * 
 * 消耗配件入库查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatPlanQuery');
	
	/** 获取最近的一个月 */
	var dateNow = new Date();
	var month = dateNow.getMonth();
	var year = dateNow.getFullYear();
	if (month == 0) {
		dateNow.setFullYear(year - 1);
		dateNow.setMonth(11);
	} else {
		dateNow.setMonth(month-1);
	}
	var lastMonth = dateNow.format('Y-m-d');
	
	/** ************** 定义全局变量开始 ************** */
	MatPlanQuery.searchParams = {};
	MatPlanQuery.labelWidth = 100;
	MatPlanQuery.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 显示入库单明细
	MatPlanQuery.showDetail = function(rowIndex) {
		// 重新加载入库单基本信息
		var record = MatPlanQuery.grid.store.getAt(rowIndex);
		MatPlanQueryDetail.baseForm.getForm().loadRecord(record);
		// 根据入库单主键，重新加载入库单明细
		MatPlanQueryDetail.matPlanIdx = record.get('idx');
		MatPlanQueryDetail.grid.store.load();
		if (record.get('status') == STATUS_ZC) {
			MatPlanQueryDetail.batchWin.setTitle("单据信息<font color='green'>(待提交)</font>");
		} else if (record.get('status') == STATUS_DZ) {
			MatPlanQueryDetail.batchWin.setTitle("单据信息<font color='red'>(已提交)</font>");
		}
		// 显示入库单明细窗口
		MatPlanQueryDetail.batchWin.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatPlanQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatPlanQuery.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 10px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{											// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '入库日期', combineErrors: false,
					items: [{
						xtype:'my97date', name: 'startDate', id: 'startDate_d', format:'Y-m-d', value: lastMonth, width: 100, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "开始日期不能大于结束日期";
							}
						}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {							
						xtype:'my97date', name: 'endDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "结束日期不能小于开始日期";
							}
						}
					}]
				}]
			}, {												// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_SY, "所有"], [STATUS_ZC, "待提交"], [STATUS_DZ, "已提交"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_SY,
					mode:'local',
					width: MatPlanQuery.fieldWidth
				}]
			}, {												// 第1行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					name:'applyEmp',
					xtype:"textfield", 
					fieldLabel: '申请人',
					width: MatPlanQuery.fieldWidth
				}]
			}]
		}, {
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{											// 第2行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					name:'useTeam',
					xtype:"textfield", 
					fieldLabel: '使用班组',
					width: 222
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatPlanQuery.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatPlanQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatPlanQuery.searchForm.getForm().reset();
						// 重新加载表格
						MatPlanQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatPlanQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matPlan!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matPlan!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matPlan!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo',
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='MatPlanQuery.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'计划名称', dataIndex:'planName'
		},{
			header:'申请人主键', dataIndex:'applyEmpId', hidden:true
		},{
			header:'申请人名称', dataIndex:'applyEmp'
		},{
			header:'申请日期', dataIndex:'applyDate', xtype:'datecolumn'
		},{
			header:'使用班组', dataIndex:'useTeam'
		},{
			header:'单据状态', dataIndex:'status', renderer: function(v) {
					if (v == STATUS_ZC) return "待提交";
					if (v == STATUS_DZ) return "已提交";
					return "错误！未知状态";
				}
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden: true
		},{
			header:'制单日期', dataIndex:'makeBillDate', xtype:'datecolumn', hidden: true
		},{
			header:'提交人', dataIndex:'registEmp', hidden: true
		},{
			header:'提交日期', dataIndex:'registDate', xtype:'datecolumn', hidden: true
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatPlanQuery.grid.un('rowdblclick', MatPlanQuery.grid.toEditFn, MatPlanQuery.grid);
	
	MatPlanQuery.grid.store.on('beforeload', function() {
		MatPlanQuery.searchParams = MatPlanQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatPlanQuery.searchParams);
		MatPlanQuery.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatPlanQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 140,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatPlanQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatPlanQuery.grid]
		}]
	});
});