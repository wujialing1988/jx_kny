/**
 * 
 * 消耗配件移入js
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
		} else if(record.get('status') == STATUS_IN){
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
				columnWidth: .55,
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
				columnWidth: .45,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_ALL, "所有"], [STATUS_OUT, "移出"], [STATUS_IN, "移入"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_OUT,
					mode:'local',
					width: MatMoveStock.fieldWidth
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
					xtype: "button", text: '移入', iconCls: 'checkIcon', handler: function() {
						var records = MatMoveStock.grid.selModel.getSelections();
						if (records.length < 1) {
								MyExt.Msg.alert("尚未选择任何记录！");
								return;
						}
						if (records[0].data.status != STATUS_OUT) {
								MyExt.Msg.alert("只有状态为【移出】的数据才能进行移入操作！");
			        			return;
						}
						Ext.Ajax.request({
					        url: ctx + "/matMoveStock!updateMatMoveStock.action",
					        params: {id: records[0].data.idx},
					        success: function(response, options){
					            var result = Ext.util.JSON.decode(response.responseText);
					            if (result.errMsg == null) {
					                alertSuccess();
					                MatMoveStock.grid.store.reload(); 
					            } else {
					                alertFail(result.errMsg);
					            }
					        },
					        failure: function(response, options){
					            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					        }
					    });
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
			header:'移出库房', dataIndex:'exWhName', width:60, editor:{  maxLength:50 }
		},{
			header:'出库人主键', dataIndex:'exWhEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'移出人', dataIndex:'exWhEmp', width:40, editor:{  maxLength:25 }
		},{
			header:'移出日期', dataIndex:'exWhDate', width:40, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'移入库房主键', dataIndex:'inWhIdx', hidden:true,editor:{  maxLength:50 }
		},{
			header:'移入库房', dataIndex:'inWhName', width:60, editor:{  maxLength:50 }
		},{
			header:'移料人主键', dataIndex:'getEmpID',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'移料人', dataIndex:'getEmp', width:40, editor:{  maxLength:25 }
		},{
			header:'入库人主键', dataIndex:'exWhEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'入库人名称', dataIndex:'exWhEmp',hidden:true, editor:{  maxLength:25 }
		},{
			header:'入库日期', dataIndex:'exWhDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'单据状态', dataIndex:'status', width: 30, editor:{  maxLength:20 }, renderer: function(v) {
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
		MatMoveStock.grid.store.baseParams.empId = empId;
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