/**
 * 
 * 消耗配件盘点查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatCheckProfitQuery');
	
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
	MatCheckProfitQuery.searchParams = {};
	MatCheckProfitQuery.labelWidth = 100;
	MatCheckProfitQuery.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 显示盘点单明细
	MatCheckProfitQuery.showDetail = function(rowIndex) {
		// 重新加载盘点单基本信息
		var record = MatCheckProfitQuery.grid.store.getAt(rowIndex);
		MatCheckProfitQueryDetail.baseForm.getForm().loadRecord(record);
		// 根据盘点单主键，重新加载盘点单明细
		MatCheckProfitQueryDetail.matCheckProfitIDX = record.get('idx');
		MatCheckProfitQueryDetail.grid.store.load();
		if (record.get('status') == STATUS_ZC) {
			MatCheckProfitQueryDetail.batchWin.setTitle("单据信息<font color='green'>(暂存)</font>");
		} else if (record.get('status') == STATUS_DZ) {
			MatCheckProfitQueryDetail.batchWin.setTitle("单据信息<font color='red'>(已登账)</font>");
		}
		// 显示盘点单明细窗口
		MatCheckProfitQueryDetail.batchWin.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatCheckProfitQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatCheckProfitQuery.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '盘点库房',
					hiddenName: 'whIdx',
					id: 'whIdx_s',
					width: MatCheckProfitQuery.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			}, {													// 第1行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_SY, "所有"], [STATUS_ZC, "暂存"], [STATUS_DZ, "已登账"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_SY,
					mode:'local',
					width: MatCheckProfitQuery.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '盘点日期', combineErrors: false,
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
			}]
		}, {
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第2行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '盘点人',
					name: 'checkEmp',
					xtype:"textfield", 
					width: MatCheckProfitQuery.fieldWidth
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatCheckProfitQuery.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatCheckProfitQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatCheckProfitQuery.searchForm.getForm().reset();
						// 重置“盘点库房”字段值
						Ext.getCmp('whIdx_s').clearValue();
						// 重新加载表格
						MatCheckProfitQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatCheckProfitQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matCheckProfit!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matCheckProfit!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matCheckProfit!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'单据编号', dataIndex:'billNo', width: 30, editor:{  maxLength:25 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='MatCheckProfitQuery.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'单据摘要', dataIndex:'billSummary', editor:{  maxLength:200 }
		},{
			header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{  maxLength:50 }
		},{
			header:'盘点库房', dataIndex:'whName', width: 30, editor:{  maxLength:50 }
		},{
			header:'盘点人主键', dataIndex:'checkEmpid', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'盘点人', dataIndex:'checkEmp', width: 30, editor:{  maxLength:25 }
		},{
			header:'盘点日期', dataIndex:'checkDate', width: 30, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'状态', dataIndex:'status', width: 30, editor:{  maxLength:20 }, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_DZ) return "已登账";
				return "错误！未知状态";
			}
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登账人', dataIndex:'registEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'登账日期', dataIndex:'registDate', hidden:true,  xtype:'datecolumn', editor:{ xtype:'my97date' }
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatCheckProfitQuery.grid.un('rowdblclick', MatCheckProfitQuery.grid.toEditFn, MatCheckProfitQuery.grid);
	
	MatCheckProfitQuery.grid.store.on('beforeload', function() {
		MatCheckProfitQuery.searchParams = MatCheckProfitQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatCheckProfitQuery.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		this.baseParams.isQueryPage = true;
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatCheckProfitQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 150,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatCheckProfitQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatCheckProfitQuery.grid]
		}]
	});
});