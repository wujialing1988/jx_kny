/**
 * 物料移库单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
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
	
Ext.onReady(function(){
	Ext.namespace('MatMoveStock');                       //定义命名空间
	
	/** ************ 定义全局变量开始 ************ */
	MatMoveStock.fieldWidth = 100;
	MatMoveStock.labelWidth = 70;
	MatMoveStock.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	MatMoveStock.isTemporary = true;					// 当前操作的记录是否为“未登账”记录的标识
	
	/** ************ 定义全局函数开始 ************ */
	// 【新增】按钮触发的页面初始化操作（初始化页面）
	MatMoveStock.resetFn = function() {
		Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
        // 清空【单据信息】列表
		if (MatMoveStockDetail.grid.store.getCount() > 0) {
	        MatMoveStockDetail.grid.store.removeAll();
		}
        // 重新设置MatMoveStockIdx
        MatMoveStockDetail.matMoveStockIdx = "###";
		MatMoveStock.isTemporary = true;
		Ext.getCmp('exWhName_k').enable();
		Ext.getCmp('exWhEmpId_k').enable();
		Ext.getCmp('exWhDate_k').enable();
		MatMoveStockDetail.grid.getTopToolbar().enable();
		MatMoveStock.baseForm.getForm().reset();
		
		// 设置默认【移出人】信息
		Ext.getCmp("exWhEmpId_k").setDisplayValue(empId,empName);
		Ext.getCmp("exWhEmp_k").setValue(empName);
		// 设置默认【移出库房】信息
		var exWhName_comb = Ext.getCmp("exWhName_k");
		exWhName_comb.setDisplayValue(exWhName_comb.getStore().getAt(0).get('wareHouseName'), exWhName_comb.getStore().getAt(0).get('wareHouseName'))
    	Ext.getCmp("exWhIdx_k").setValue(exWhName_comb.getStore().getAt(0).get('idx'));
		MatMoveStock.setMatMoveStockBillNo();
	}
	/** ************ 定义全局函数结束 ************ */
	// 查询表单
	MatMoveStock.searchForm = new Ext.form.FormPanel({
		labelWidth:MatMoveStock.labelWidth,
		labelAlign:"left",
		baseCls:"x-plain", border: false, 
		style: "padding-left:20px;", 
		layout:"column",
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[
					{
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
				}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[{
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
			},
			{
				xtype:"panel",
				layout:"column",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[{
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'查询', iconCls:'searchIcon', width: 80, handler: function() {
							var form = MatMoveStock.searchForm.getForm();
							if (!form.isValid()) {
								return;	
							}
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatMoveStock.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'新增', iconCls:'addIcon', width: 80, handler: MatMoveStock.resetFn
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'删除', iconCls:'deleteIcon', width: 80, handler: function() {
							var sm = MatMoveStock.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							if (!MatMoveStock.isTemporary) {
								MyExt.Msg.alert("已移出的记录单不能删除！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
							        if(btn == 'yes') {
								        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatMoveStock.grid,
								        	url: MatMoveStock.grid.deleteURL,
											params: {ids: $yd.getSelectedIdx(MatMoveStock.grid, MatMoveStock.grid.storeId)},
											success: function(response, options){
												// 重新加载主表格
												MatMoveStock.grid.store.reload();
										        // 初始化页面
										        MatMoveStock.resetFn();
											}
								        }));
							        }
							    });    
							}
						}
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'回滚', iconCls:'resetIcon', width: 80, handler: function() {
							var sm = MatMoveStock.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							var data = sm.getSelections()[0].data;
							if (data.status == STATUS_ZC) {
								MyExt.Msg.alert("只能回滚已移出的记录！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "是否确认回滚？  ", function(btn){
							        if(btn == 'yes') {
						        		MatMoveStock.loadMask.show();
						        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatMoveStock.grid,
								        	url: ctx + '/matMoveStock!updateRollBack.action',
											params: {ids: $yd.getSelectedIdx(MatMoveStock.grid, MatMoveStock.grid.storeId)},
											success: function(response, options){
								              	MatMoveStock.loadMask.hide();
								                var result = Ext.util.JSON.decode(response.responseText);
								                if (result.errMsg == null) {
								                    alertSuccess();
											        // 撤销成功后的一些页面初始化方法
													MatMoveStock.isTemporary = true;
													MatMoveStockDetail.grid.getTopToolbar().enable();
													MatMoveStock.grid.store.reload();
													Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
													Ext.getCmp('exWhEmpId_k').enable();
													Ext.getCmp('exWhDate_k').enable();
													Ext.getCmp('inWhName_k').enable();
													Ext.getCmp('getEmpID_k').enable();
								                } else {
								                    alertFail(result.errMsg);
								                }
								            }
								        }));
							        }
							    });    
							}
						}
					}]
				}]
			}
		]
	})
	// 单据列表
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
				header:'单据编号', dataIndex:'billNo',width:120, editor:{  maxLength:25 }
			},{
				header:'单据摘要', dataIndex:'billSummary',width:200, editor:{  maxLength:200 }
			},{
				header:'移出库房主键', dataIndex:'exWhIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
			},{
				header:'移出库房', dataIndex:'exWhName', editor:{  maxLength:50 }
			},{
				header:'出库人主键', dataIndex:'exWhEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
			},{
				header:'移出人', dataIndex:'exWhEmp', editor:{  maxLength:25 }
			},{
				header:'移出日期', dataIndex:'exWhDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
			},{
				header:'移入库房主键', dataIndex:'inWhIdx', hidden:true,editor:{  maxLength:50 }
			},{
				header:'移入库房', dataIndex:'inWhName', editor:{  maxLength:50 }
			},{
				header:'移料人主键', dataIndex:'getEmpID',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
			},{
				header:'移料人', dataIndex:'getEmp', editor:{  maxLength:25 }
			},{
				header:'入库人主键', dataIndex:'exWhEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
			},{
				header:'入库人名称', dataIndex:'exWhEmp',hidden:true, editor:{  maxLength:25 }
			},{
				header:'入库日期', dataIndex:'exWhDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
			},{
				header:'单据状态', dataIndex:'status', editor:{  maxLength:20 }, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_IN) return "移入";
				if (v == STATUS_OUT) return "移出";
				return "错误！未知状态";
			}
			},{
				header:'制单人', dataIndex:'makeBillEmp',hidden:true, editor:{  maxLength:25 }
			},{
				header:'制单日期', dataIndex:'makeBillDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
			}],
		listeners: {
			'rowclick': function(grid, rowIndex, e) {
				var record = grid.store.getAt(rowIndex);
				MatMoveStockDetail.matMoveStockIdx = record.data.idx;
				MatMoveStockDetail.grid.store.load();
				// 重新加载【单据信息】表单
				MatMoveStock.baseForm.getForm().loadRecord(record);
				Ext.getCmp("exWhEmpId_k").setDisplayValue(record.data.exWhEmpId ,record.data.exWhEmp);
				Ext.getCmp('exWhName_k').setDisplayValue(record.data.exWhName, record.data.exWhName);
				Ext.getCmp("inWhName_k").setDisplayValue(record.data.inWhName ,record.data.inWhName);
				Ext.getCmp('getEmpID_k').setDisplayValue(record.data.getEmpID, record.data.getEmp);
				Ext.getCmp('exWhName_k').disable();
				// 设置单据状态的显示标识
				var status = record.get('status');
				if (status == STATUS_ZC) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
					MatMoveStock.isTemporary = true;
//					Ext.getCmp('exWhName_k').enable();
					Ext.getCmp('exWhEmpId_k').enable();
					Ext.getCmp('exWhDate_k').enable();
					Ext.getCmp('inWhName_k').enable();
					Ext.getCmp('getEmpID_k').enable();
//					Ext.getCmp('billSummary_k').enable();
					MatMoveStockDetail.grid.getTopToolbar().enable();
				} else{
					Ext.getCmp('basePanel').setTitle("单据信息<font color='red'>(转出)</font>");
					MatMoveStock.isTemporary = false;
//					Ext.getCmp('exWhName_k').disable();
					Ext.getCmp('exWhEmpId_k').disable();
					Ext.getCmp('exWhDate_k').disable();
					Ext.getCmp('inWhName_k').disable();
					Ext.getCmp('getEmpID_k').disable();
//					Ext.getCmp('billSummary_k').disable();
					MatMoveStockDetail.grid.getTopToolbar().disable();
				}
			}
		}
	});
	MatMoveStock.grid.un('rowdblclick', MatMoveStock.grid.toEditFn, MatMoveStock.grid);
	MatMoveStock.grid.store.on('beforeload', function() {
		var searchParams = MatMoveStock.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	
	// 【单据信息】表单
	MatMoveStock.baseForm = new Ext.form.FormPanel({
		labelWidth:MatMoveStock.labelWidth,
		layout:"column",
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"billNo_k",
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width:140
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[
					{
			        	id:"exWhName_k", fieldLabel:"移出库房",hiddenName:"exWhName",
						xtype:"Base_combo",allowBlank: false,width:MatMoveStock.fieldWidth,
						entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
						queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
						fields:["wareHouseID","wareHouseName","idx"],
						displayField:"wareHouseName",valueField:"wareHouseName",
						returnField:[{widgetId:"exWhIdx_k",propertyName:"idx"}]        
			        },
                    {id:"exWhIdx_k",xtype:"hidden", name:"exWhIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"exWhEmpId_k",
					xtype:'OmEmployee_SelectWin',
					editable:false,
					allowBlank: false,
					fieldLabel : "移出人",
					hiddenName:'exWhEmpId',
					returnField:[{
						widgetId:"exWhEmp_k",
						propertyName:"empname"
					}],
					width:MatMoveStock.fieldWidth
				}, {
					id:"exWhEmp_k",xtype:"hidden", name:"exWhEmp"
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"exWhDate_k",
					name: "exWhDate", 
					fieldLabel: "移出日期",
					allowBlank: false, 
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatMoveStock.fieldWidth
				}]
			},/*
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"billSummary_k", 
					name:"billSummary", 
					fieldLabel:"单据摘要",
					width:580
				}]
			},
			*/{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[
					{
			        	id:"inWhName_k", fieldLabel:"移入库房",hiddenName:"inWhName",
						xtype:"Base_combo",allowBlank: false,width:MatMoveStock.fieldWidth,
						entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",	
						// and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')
						queryHql:"from Warehouse where recordStatus=0 and status = 1 ",	
						fields:["wareHouseID","wareHouseName","idx"],
						displayField:"wareHouseName",valueField:"wareHouseName",
						returnField:[{widgetId:"inWhIdx_k",propertyName:"idx"}]        
			        },
                    {id:"inWhIdx_k",xtype:"hidden", name:"inWhIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"getEmpID_k",
					xtype:'OmEmployee_SelectWin',
					editable:false,
					allowBlank: false,
					fieldLabel : "移料人",
					hiddenName:'getEmpID',
					returnField:[{
						widgetId:"getEmp_k",
						propertyName:"empname"
					}],
					width:MatMoveStock.fieldWidth
				}, {
					id:"getEmp_k",xtype:"hidden", name:"getEmp"
				}]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillEmp_k",
					fieldLabel : "制单人", value: empName,
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatMoveStock.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillDate_k",
					name: "makeBillDate", 
					fieldLabel: "制单日期",
					allowBlank: false, 
					xtype: "my97date",
					format: "Y-m-d", 
					width: MatMoveStock.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
	/** ************ 定义全局变量开始 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	// 自动设置【单据编号】字段值
	MatMoveStock.setMatMoveStockBillNo = function() {
		Ext.Ajax.request({
            url: ctx + "/codeRuleConfig!getConfigRule.action",
            params: {ruleFunction: "WLGL_MAT_MOVE_STOCK_BILL_NO"},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    Ext.getCmp("billNo_k").setValue(result.rule);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	// 获取表单数据前激活被禁用的字段
	MatMoveStock.beforeGetBaseDate = function() {
		Ext.getCmp('billNo_k').enable();
		Ext.getCmp('makeBillEmp_k').enable();
		Ext.getCmp('makeBillDate_k').enable();
		Ext.getCmp("exWhName_k").enable();
	}
	// 获取表单数据后禁用被激活的字段
	MatMoveStock.afterGetBaseDate = function() {
		Ext.getCmp('billNo_k').disable();
		Ext.getCmp('makeBillEmp_k').disable();
		Ext.getCmp('makeBillDate_k').disable();
		Ext.getCmp("exWhName_k").disable();
	}
	// 【暂存】按钮事件触发成功后的页面初始化事件处理
	MatMoveStock.afterSaveTemporayFn = function() {
        // 重新加载【单据列表】列表
        MatMoveStock.grid.store.reload();
        // 清空【单据信息】列表
        MatMoveStockDetail.grid.store.removeAll();
        // 重新设置单据编号
        MatMoveStock.setMatMoveStockBillNo();
        // 重新设置MatMoveStockIdx
        MatMoveStockDetail.matMoveStockIdx = "###";
	}
	// 【暂存】按钮触发的函数操作
	MatMoveStock.SaveTemporaryFn = function(isTemporary) {
		// 物料移库单基本信息
		var form = MatMoveStock.baseForm.getForm();
		// 验证表单数据的合法性
		if (!form.isValid()) {
			return;
		}
		MatMoveStock.beforeGetBaseDate();
		var matMoveStock =  form.getValues();
		MatMoveStock.afterGetBaseDate();
		if(matMoveStock.exWhIdx == matMoveStock.inWhIdx){
			MyExt.Msg.alert("移出库房和移入库房不能相同！");
			return ;
		}
		if (MatMoveStockDetail.matMoveStockIdx != "###" && !Ext.isEmpty(MatMoveStockDetail.matMoveStockIdx)) {
			matMoveStock.idx = MatMoveStockDetail.matMoveStockIdx;
		}
		// 判断是【暂存】还是【移出】操作
		if (isTemporary) {
			// 设置单据状态为【暂存（temporary）】
			matMoveStock.status = STATUS_ZC;
		} else {
			// 设置单据状态为【转出（transOut）】
			matMoveStock.status = STATUS_OUT;
		}
		// 获取添加的明细数据
		var store = MatMoveStockDetail.store;
		// 验证添加的明细数据是否为空
		if(store.getCount() == 0){
			MyExt.Msg.alert("请先添加明细！");
			return ;
		}
		var datas = new Array();
		for (var i = 0; i < store.getCount(); i++) {
			var data = store.getAt(i).data;
			if (data.qty == 0) {
				MyExt.Msg.alert("数量不能为0，请输入有效数字！");
				return;
			}
			datas.push(data);
		}
		
		MatMoveStock.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/matMoveStock!saveWhAndDetail.action',
            jsonData: datas,
            params : {matMoveStock : Ext.util.JSON.encode(MyJson.deleteBlankProp(matMoveStock))},
            success: function(response, options){
              	MatMoveStock.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    MatMoveStock.afterSaveTemporayFn();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatMoveStock.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	/** ************ 定义全局函数结束 ************ */
	
	new Ext.Viewport({
		layout:"border",
		items:[
			{
				height:269,
				title:"单据列表",
				collapsible : true,
				collapsed :true,
				region:"north",
				layout:"border",
				items:[
					{
						xtype:"panel", border: false, baseCls:"x-plain", 
						region:"north",
						height:42,
						layout:"fit",
						items: MatMoveStock.searchForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						items: MatMoveStock.grid
					}
				]
			}, {
				region:"center",
				layout:"border",
				items:[
					{
						xtype:"panel", border: false, frame:true,
						id:"basePanel",
						title:"单据信息<font color='green'>(暂存)</font>",
						region:"north",
						layout:"fit",
						height:106,
						items: MatMoveStock.baseForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						buttonAlign:"center",
						items:MatMoveStockDetail.grid,
						buttons:[{
							text:'暂存',handler: function() {
								if (MatMoveStock.isTemporary) {
									MatMoveStock.SaveTemporaryFn(true);
								} else {
									MyExt.Msg.alert("已移出的记录单不能再次操作");
								}
							}
						}, {
							text:'移出',handler: function() {
								if (MatMoveStock.isTemporary) {
									MatMoveStock.SaveTemporaryFn(false);
								} else {
									MyExt.Msg.alert("已移出的记录单不能再次操作");
								}
							}
						}]
					}
				]
			}
		]
	})
	
	// 页面初始化操作
	MatMoveStock.init = function(){
		// 设置默认【移出人】信息
		Ext.getCmp("exWhEmpId_k").setDisplayValue(empId,empName);
		Ext.getCmp("exWhEmp_k").setValue(empName);
		// 设置默认【移出库房】信息
		var exWhName_comb = Ext.getCmp("exWhName_k");
		exWhName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	exWhName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("exWhIdx_k").setValue(records[0].get('idx'));
			}
		});
		MatMoveStock.setMatMoveStockBillNo();
	};
	MatMoveStock.init();

});