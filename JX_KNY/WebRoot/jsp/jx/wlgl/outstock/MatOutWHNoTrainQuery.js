/**
 * 
 * 机车外用料查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatOutWHNoTrainQuery');
	
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
	MatOutWHNoTrainQuery.searchParams = {};
	MatOutWHNoTrainQuery.labelWidth = 80;
	MatOutWHNoTrainQuery.fieldWidth = 120;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 显示入库单明细
	MatOutWHNoTrainQuery.showDetail = function(rowIndex) {
		// 重新加载入库单基本信息
		var record = MatOutWHNoTrainQuery.grid.store.getAt(rowIndex);
		MatOutWHNoTrainQueryDetail.baseForm.getForm().loadRecord(record);
		// 根据入库单主键，重新加载入库单明细
		MatOutWHNoTrainQueryDetail.matOutWHNoTrainIDX = record.get('idx');
		MatOutWHNoTrainQueryDetail.grid.store.load();
		if (record.get('status') == STATUS_ZC) {
			MatOutWHNoTrainQueryDetail.batchWin.setTitle("单据信息<font color='green'>(暂存)</font>");
		} else if (record.get('status') == STATUS_DZ) {
			MatOutWHNoTrainQueryDetail.batchWin.setTitle("单据信息<font color='red'>(已登账)</font>");
		}
		// 显示入库单明细窗口
		MatOutWHNoTrainQueryDetail.batchWin.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatOutWHNoTrainQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatOutWHNoTrainQuery.labelWidth,
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
					fieldLabel: '领用库房',
					hiddenName: 'whIdx',
					id: 'whIdx_s',
					width: MatOutWHNoTrainQuery.fieldWidth,
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
					width: MatOutWHNoTrainQuery.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '领用日期', combineErrors: false,
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
					name: 'getEmp',
					fieldLabel: '领用人',
					xtype: 'textfield',
					width: MatOutWHNoTrainQuery.fieldWidth
				}]
			}, {													// 第2行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					name: 'getOrg',
					fieldLabel: '领用班组',
					xtype: 'textfield',
					width: MatOutWHNoTrainQuery.fieldWidth
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatOutWHNoTrainQuery.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatOutWHNoTrainQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatOutWHNoTrainQuery.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('whIdx_s').clearValue();
						// 重新加载表格
						MatOutWHNoTrainQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatOutWHNoTrainQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWHNoTrain!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matOutWHNoTrain!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matOutWHNoTrain!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo', width: 60,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='MatOutWHNoTrainQuery.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'单据摘要', dataIndex:'billSummary'
		},{
			header:'领用库房主键', dataIndex:'whIdx', hidden:true
		},{
			header:'领用库房', dataIndex:'whName', width: 30
		},{
			header:'出库人主键', dataIndex:'exWhEmpId', hidden:true
		},{
			header:'出库人', dataIndex:'exWhEmp', width: 30, hidden:true
		},{
			header:'领用人主键', dataIndex:'getEmpId', hidden:true
		},{
			header:'领用人', dataIndex:'getEmp', width: 25
		},{
			header:'领用机构id', dataIndex:'getOrgId', hidden:true
		},{
			header:'领用机构', dataIndex:'getOrg', width: 50
		},{
			header:'领用机构序列', dataIndex:'getOrgSeq', hidden:true, width: 30
		},{
			header:'领用日期', dataIndex:'getDate', width: 30, xtype:'datecolumn'
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登账人', dataIndex:'registEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'登账日期', dataIndex:'registDate', hidden:true,  xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'用途', dataIndex:'purpose', width: 60
		},{
			header:'状态', dataIndex:'status', width: 30, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_DZ) return "已登账";
				return "错误！未知状态";
			}
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatOutWHNoTrainQuery.grid.un('rowdblclick', MatOutWHNoTrainQuery.grid.toEditFn, MatOutWHNoTrainQuery.grid);
	
	MatOutWHNoTrainQuery.grid.store.on('beforeload', function() {
		MatOutWHNoTrainQuery.searchParams = MatOutWHNoTrainQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatOutWHNoTrainQuery.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		this.baseParams.isQueryPage = true;
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatOutWHNoTrainQuery.viewport = new Ext.Viewport({
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
			items: [MatOutWHNoTrainQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatOutWHNoTrainQuery.grid]
		}]
	});
});