/**
 * 
 * 消耗配件移库查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatMoveStock');
	
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
	MatMoveStock.searchParams = {};
	MatMoveStock.labelWidth = 100;
	MatMoveStock.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 显示入库单明细
	MatMoveStock.showDetail = function(rowIndex) {
		// 重新加载入库单基本信息
		var record = MatMoveStock.grid.store.getAt(rowIndex);
		MatMoveStockDetail.baseForm.getForm().loadRecord(record);
		// 根据入库单主键，重新加载入库单明细
		MatMoveStockDetail.matMoveStockIdx = record.get('idx');
		MatMoveStockDetail.grid.store.load();
		if (record.get('status') == STATUS_ZC) {
			MatMoveStockDetail.batchWin.setTitle("单据信息<font color='green'>(暂存)</font>");
		}else if(record.get('status') == STATUS_IN){
			MatMoveStockDetail.batchWin.setTitle("单据信息<font color='red'>(移入)</font>");
		}else{
			MatMoveStockDetail.batchWin.setTitle("单据信息<font color='red'>(移出)</font>");
		}
		// 显示入库单明细窗口
		MatMoveStockDetail.batchWin.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatMoveStock.searchForm = new Ext.form.FormPanel({
		labelWidth: MatMoveStock.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{													// 第1行第3列
				columnWidth: .25,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '移出日期', combineErrors: false,
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
			}, {													// 第1行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_ALL, "所有"], [STATUS_ZC, "暂存"], [STATUS_OUT, "移出"], [STATUS_IN, "移入"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_ALL,
					mode:'local',
					width: MatMoveStock.fieldWidth
				}]
			},{												
				columnWidth: .25,
				layout: 'form',
				items: [{
					fieldLabel: '移出库房',
					hiddenName: 'exWhIdx',
					id: 'exWhIdx_s',
					width: MatMoveStock.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			},{												
				columnWidth: .25,
				layout: 'form',
				items: [{
					fieldLabel: '移入库房',
					hiddenName: 'inWhIdx',
					id: 'inWhIdx_s',
					width: MatMoveStock.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatMoveStock.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatMoveStock.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatMoveStock.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('inWhIdx_s').clearValue();
						// 重置“移出库房”字段值
						Ext.getCmp('exWhIdx_s').clearValue();
						// 重新加载表格
						MatMoveStock.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatMoveStock.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matMoveStock!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matMoveStock!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matMoveStock!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'单据编号', dataIndex:'billNo', width: 65, editor:{  maxLength:25 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='MatMoveStock.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'单据摘要', dataIndex:'billSummary',width:200, editor:{  maxLength:200 }
		},{
			header:'移出库房主键', dataIndex:'exinWhIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'移出库房', dataIndex:'exWhName', width: 60, editor:{  maxLength:50 }
		},{
			header:'出库人主键', dataIndex:'exWhEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'移出人', dataIndex:'exWhEmp', width: 40, editor:{  maxLength:25 }
		},{
			header:'移出日期', dataIndex:'exWhDate', width: 40, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'移入库房主键', dataIndex:'inWhIdx', hidden:true,editor:{  maxLength:50 }
		},{
			header:'移入库房', dataIndex:'inWhName', width: 60, editor:{  maxLength:50 }
		},{
			header:'移料人主键', dataIndex:'getEmpID',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'移料人', dataIndex:'getEmp', width: 40, editor:{  maxLength:25 }
		},{
			header:'入库人主键', dataIndex:'exWhEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'入库人名称', dataIndex:'exWhEmp',hidden:true, editor:{  maxLength:25 }
		},{
			header:'入库日期', dataIndex:'exWhDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'单据状态', dataIndex:'status', width: 30, editor:{  maxLength:20 }, renderer: function(v) {
			if (v == STATUS_ZC) return "暂存";
			if (v == STATUS_IN) return "移入";
			if (v == STATUS_OUT) return "移出";
			return "错误！未知状态";
		}
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatMoveStock.grid.un('rowdblclick', MatMoveStock.grid.toEditFn, MatMoveStock.grid);
	
	MatMoveStock.grid.store.on('beforeload', function() {
		MatMoveStock.searchParams = MatMoveStock.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatMoveStock.searchParams);
		MatMoveStock.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatMoveStock.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 130,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatMoveStock.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatMoveStock.grid]
		}]
	});
});