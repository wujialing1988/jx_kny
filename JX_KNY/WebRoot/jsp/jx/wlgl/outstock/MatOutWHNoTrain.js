/**
 * 机车外用料js
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
	Ext.namespace('MatOutWHNoTrain');                       //定义命名空间
	
	/** ************ 定义全局函数开始 ************ */
	// 【新增】按钮触发的页面初始化操作（初始化页面）
	MatOutWHNoTrain.resetFn = function() {
		Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
        // 清空【单据信息】列表
		if (MatOutWHNoTrainDetail.grid.store.getCount() > 0) {
	        MatOutWHNoTrainDetail.grid.store.removeAll();
		}
        // 重新设置MatOutWHNoTrainIdx
        MatOutWHNoTrainDetail.matOutWHNoTrainIDX = "###";
		MatOutWHNoTrain.isTemporary = true;
		MatOutWHNoTrainDetail.grid.getTopToolbar().enable();
		Ext.getCmp('whName_k').enable();
		Ext.getCmp('getDate_k').enable();
		Ext.getCmp('getEmpId_k').enable();
		Ext.getCmp('getOrg_k').enable();
		Ext.getCmp('exWhEmpId_k').enable();
		Ext.getCmp('purpose_k').enable();
		MatOutWHNoTrain.baseForm.getForm().reset();
		
		// 设置默认【领用人】信息
		Ext.getCmp("getEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【出库人】信息
		Ext.getCmp("exWhEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【出库库房】信息
		var whName_comb = Ext.getCmp("whName_k");
		whName_comb.setDisplayValue(whName_comb.getStore().getAt(0).get('wareHouseName'), whName_comb.getStore().getAt(0).get('wareHouseName'))
    	Ext.getCmp("whIdx_k").setValue(whName_comb.getStore().getAt(0).get('idx'));
		MatOutWHNoTrain.setMatOutWHNoTrainBillNo();
	}
	/** ************ 定义全局函数结束 ************ */
	
	/** ************ 定义全局变量开始 ************ */
	MatOutWHNoTrain.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	MatOutWHNoTrain.fieldWidth = 100;
	MatOutWHNoTrain.labelWidth = 70;
	MatOutWHNoTrain.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	MatOutWHNoTrain.isTemporary = true;					// 当前操作的记录是否为“未登账”记录的标识
	// 查询表单
	MatOutWHNoTrain.searchForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHNoTrain.labelWidth,
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
						data : [[STATUS_SY, "所有"], [STATUS_ZC, "暂存"], [STATUS_DZ, "已登账"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_SY,
					mode:'local',
					width: MatOutWHNoTrain.fieldWidth
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
							var form = MatOutWHNoTrain.searchForm.getForm();
							if (!form.isValid()) {
								return;	
							}
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatOutWHNoTrain.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'新增', iconCls:'addIcon', width: 80, handler: MatOutWHNoTrain.resetFn
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'删除', iconCls:'deleteIcon', width: 80, handler: function() {
							var sm = MatOutWHNoTrain.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							if (!MatOutWHNoTrain.isTemporary) {
								MyExt.Msg.alert("已登账的记录单不能删除！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
							        if(btn == 'yes') {
								        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatOutWHNoTrain.grid,
								        	url: MatOutWHNoTrain.grid.deleteURL,
											params: {ids: $yd.getSelectedIdx(MatOutWHNoTrain.grid, MatOutWHNoTrain.grid.storeId)},
											success: function(response, options){
												// 重新加载主表格
												MatOutWHNoTrain.grid.store.reload();
										        // 初始化页面
										        MatOutWHNoTrain.resetFn();
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
							var sm = MatOutWHNoTrain.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							var data = sm.getSelections()[0].data;
							if (data.status == STATUS_ZC) {
								MyExt.Msg.alert("只能回滚已登账的记录！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "是否确认回滚？  ", function(btn){
							        if(btn == 'yes') {
						        		MatOutWHNoTrain.loadMask.show();
						        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatOutWHNoTrain.grid,
								        	url: ctx + '/matOutWHNoTrain!updateRollBack.action',
											params: {ids: $yd.getSelectedIdx(MatOutWHNoTrain.grid, MatOutWHNoTrain.grid.storeId)},
											success: function(response, options){
								              	MatOutWHNoTrain.loadMask.hide();
								                var result = Ext.util.JSON.decode(response.responseText);
								                if (result.errMsg == null) {
								                    alertSuccess();
											        // 撤销成功后的一些页面初始化方法
													MatOutWHNoTrain.isTemporary = true;
													MatOutWHNoTrainDetail.grid.getTopToolbar().enable();
													MatOutWHNoTrain.grid.store.reload();
													Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
													Ext.getCmp('whName_k').enable();
													Ext.getCmp('getDate_k').enable();
													Ext.getCmp('getEmpId_k').enable();
													Ext.getCmp('getOrg_k').enable();
													Ext.getCmp('exWhEmpId_k').enable();
													Ext.getCmp('purpose_k').enable();
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
	MatOutWHNoTrain.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWHNoTrain!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matOutWHNoTrain!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matOutWHNoTrain!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo', width: 50
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
			header:'领用人', dataIndex:'getEmp', width: 20
		},{
			header:'领用机构id', dataIndex:'getOrgId', hidden:true
		},{
			header:'领用机构', dataIndex:'getOrg', width: 40
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
			header:'用途', dataIndex:'purpose', width: 30
		},{
			header:'状态', dataIndex:'status', width: 30, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_DZ) return "已登账";
				return "错误！未知状态";
			}
		}],
		listeners: {
			'rowclick': function(grid, rowIndex, e) {
				var record = grid.store.getAt(rowIndex);
				MatOutWHNoTrainDetail.matOutWHNoTrainIDX = record.data.idx;
				MatOutWHNoTrainDetail.grid.store.load();
				// 重新加载【单据信息】表单
				MatOutWHNoTrain.baseForm.getForm().loadRecord(record);
				// 回显“领用人”字段
				Ext.getCmp("getEmpId_k").setDisplayValue(record.data.getEmpId, record.data.getEmp);
				// 回显“出库人”字段
				Ext.getCmp("exWhEmpId_k").setDisplayValue(record.data.exWhEmpId, record.data.exWhEmp);
				Ext.getCmp('whName_k').setDisplayValue(record.data.whName, record.data.whName);
				// 设置单据状态的显示标识
				var status = record.get('status');
				if (status == STATUS_ZC) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
					MatOutWHNoTrain.isTemporary = true;
					MatOutWHNoTrainDetail.grid.getTopToolbar().enable();
					Ext.getCmp('whName_k').enable();
					Ext.getCmp('getDate_k').enable();
					Ext.getCmp('getEmpId_k').enable();
					Ext.getCmp('getOrg_k').enable();
					Ext.getCmp('exWhEmpId_k').enable();
					Ext.getCmp('purpose_k').enable();
				} else if (status == STATUS_DZ) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='red'>(已登账)</font>");
					MatOutWHNoTrain.isTemporary = false;
					MatOutWHNoTrainDetail.grid.getTopToolbar().disable();
					Ext.getCmp('whName_k').disable();
					Ext.getCmp('getDate_k').disable();
					Ext.getCmp('getEmpId_k').disable();
					Ext.getCmp('getOrg_k').disable();
					Ext.getCmp('exWhEmpId_k').disable();
					Ext.getCmp('purpose_k').disable();
				}
			}
		}
	});
	MatOutWHNoTrain.grid.un('rowdblclick', MatOutWHNoTrain.grid.toEditFn, MatOutWHNoTrain.grid);
	MatOutWHNoTrain.grid.store.on('beforeload', function() {
		var searchParams = MatOutWHNoTrain.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	
	// 【单据信息】表单
	MatOutWHNoTrain.baseForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHNoTrain.labelWidth,
		layout:"column",
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"billNo_k",
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width: MatOutWHNoTrain.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[
					{
			        	id:"whName_k", fieldLabel:"领用库房",hiddenName:"whName",
						xtype:"Base_combo",allowBlank: false,width:MatOutWHNoTrain.fieldWidth + 20,
						entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
						queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
						fields:["wareHouseID","wareHouseName","idx"],
						displayField:"wareHouseName",valueField:"wareHouseName",
						returnField:[{widgetId:"whIdx_k",propertyName:"idx"}]        
			        },
                    {id:"whIdx_k",xtype:"hidden", name:"whIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getDate_k",
					name: "getDate", 
					fieldLabel: "领用日期",
					allowBlank: false, 
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatOutWHNoTrain.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getEmpId_k",
					xtype:'OmEmployee_SelectWin',
					editable:false,
					allowBlank: false,
					fieldLabel : "领用人",
					hiddenName:'getEmpId',
					returnField:[{
						widgetId:"getEmp_k", propertyName:"empname"
					}, {
						widgetId:"getOrgId_k", propertyName:"orgid"
					}, {
						widgetId:"getOrg_k", propertyName:"orgname"
					}],
					width:MatOutWHNoTrain.fieldWidth
				}, {
					id:"getEmp_k",xtype:"hidden", name:"getEmp",　value: empName
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getOrg_k",
					name: "getOrg", 
					fieldLabel: "领用班组",
					disabled: true,
					xtype: "textfield",
					value: orgName,
					width: MatOutWHNoTrain.fieldWidth + 80
				}, {
					id:"getOrgId_k",xtype:"hidden", name:"getOrgId", value:　orgId
				}, {
					id:"getOrgSeq_k",xtype:"hidden", name:"getOrgSeq"
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"exWhEmpId_k",
					xtype:'OmEmployee_SelectWin',
					editable:false,
					allowBlank: false,
					fieldLabel : "出库人",
					hiddenName:'exWhEmpId',
					returnField:[{
						widgetId:"exWhEmp_k", propertyName:"empname"
					}],
					width:MatOutWHNoTrain.fieldWidth
				}, {
					id:"exWhEmp_k",xtype:"hidden", name:"exWhEmp",　value: empName
				}]
			},
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"purpose_k", 
					name:"purpose", 
					fieldLabel:"用途",
					maxLength: 200,
					width:580
				}]
			},
//			{
//				xtype:"panel",
//				columnWidth:1,
//				layout:"form",
//				items:[{
//					xtype:"textfield", 
//					id:"billSummary_k", 
//					name:"billSummary", 
//					fieldLabel:"单据摘要",
//					width:580
//				}]
//			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillEmp_k",
					fieldLabel : "制单人", value: empName,
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatOutWHNoTrain.fieldWidth,
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
					xtype: "my97date",
					format: "Y-m-d", 
					width: MatOutWHNoTrain.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registEmp_k",
					fieldLabel : "登账人",  value: empName,
					name:"registEmp", 
					xtype:"textfield", 
					width: MatOutWHNoTrain.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registDate_k",
					name: "registDate", 
					fieldLabel: "登账日期",
					xtype: "my97date",format: "Y-m-d",  
					width: MatOutWHNoTrain.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
	/** ************ 定义全局变量开始 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	// 自动设置【单据编号】字段值
	MatOutWHNoTrain.setMatOutWHNoTrainBillNo = function() {
		Ext.Ajax.request({
            url: ctx + "/codeRuleConfig!getConfigRule.action",
            params: {ruleFunction: "WLGL_MAT_OUT_WH_NOTRAIN_BILL_NO"},
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
	MatOutWHNoTrain.beforeGetBaseDate = function() {
		Ext.getCmp('billNo_k').enable();
		Ext.getCmp('getOrg_k').enable();
		Ext.getCmp('makeBillEmp_k').enable();
		Ext.getCmp('makeBillDate_k').enable();
		Ext.getCmp('registEmp_k').enable();
		Ext.getCmp('registDate_k').enable();
	}
	// 获取表单数据后禁用被激活的字段
	MatOutWHNoTrain.afterGetBaseDate = function() {
		Ext.getCmp('billNo_k').disable();
		Ext.getCmp('getOrg_k').disable();
		Ext.getCmp('makeBillEmp_k').disable();
		Ext.getCmp('makeBillDate_k').disable();
		Ext.getCmp('registEmp_k').disable();
		Ext.getCmp('registDate_k').disable();
	}
	// 【暂存】按钮事件触发成功后的页面初始化事件处理
	MatOutWHNoTrain.afterSaveTemporayFn = function() {
        // 重新加载【单据列表】列表
        MatOutWHNoTrain.grid.store.reload();
        // 清空【单据信息】列表
        MatOutWHNoTrainDetail.grid.store.removeAll();
        // 重新设置单据编号
        MatOutWHNoTrain.setMatOutWHNoTrainBillNo();
        // 重新设置MatOutWHNoTrainIdx
        MatOutWHNoTrainDetail.matOutWHNoTrainIDX = "###";
	}
	// 【暂存】按钮触发的函数操作
	MatOutWHNoTrain.SaveTemporaryFn = function(isTemporary) {
		// 机车外用料单基本信息
		var form = MatOutWHNoTrain.baseForm.getForm();
		// 验证表单数据的合法性
		if (!form.isValid()) {
			return;
		}
		MatOutWHNoTrain.beforeGetBaseDate();
		var matOutWHNoTrain = form.getValues();
		if (MatOutWHNoTrainDetail.matOutWHNoTrainIDX != "###" && !Ext.isEmpty(MatOutWHNoTrainDetail.matOutWHNoTrainIDX)) {
			matOutWHNoTrain.idx = MatOutWHNoTrainDetail.matOutWHNoTrainIDX;
		}
		// 判断是【暂存】还是【登账并新增】操作
		if (isTemporary) {
			// 设置单据状态为【暂存（temporary）】
			matOutWHNoTrain.status = STATUS_ZC;
		} else {
			// 设置单据状态为【暂存（entryAccount）】
			matOutWHNoTrain.status = STATUS_DZ;
		}
		MatOutWHNoTrain.afterGetBaseDate();
		// 获取添加的明细数据
		var store = MatOutWHNoTrainDetail.store;
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
		
		MatOutWHNoTrain.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/matOutWHNoTrain!saveMatOutAndDetail.action',
            jsonData: datas,
            params : {matOutWHNoTrain : Ext.util.JSON.encode(MyJson.deleteBlankProp(matOutWHNoTrain))},
            success: function(response, options){
              	MatOutWHNoTrain.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    MatOutWHNoTrain.afterSaveTemporayFn();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatOutWHNoTrain.loadMask.hide();
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
						items: MatOutWHNoTrain.searchForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						items: MatOutWHNoTrain.grid
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
						height:160,
						items: MatOutWHNoTrain.baseForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						buttonAlign:"center",
						items:MatOutWHNoTrainDetail.grid,
						buttons:[{
							text:'暂存',handler: function() {
								if (MatOutWHNoTrain.isTemporary) {
									MatOutWHNoTrain.SaveTemporaryFn(true);
								} else {
									MyExt.Msg.alert("已登账的记录单不能再次操作");
								}
							}
						}, {
							text:'登账并新增',handler: function() {
								if (MatOutWHNoTrain.isTemporary) {
									Ext.Msg.confirm("提示  ", "是否确认登账？  ", function(btn){
								        if(btn == 'yes') {
											MatOutWHNoTrain.SaveTemporaryFn(false);
								        }
							        });
								} else {
									MyExt.Msg.alert("已登账的记录单不能再次操作");
								}
							}
						}]
					}
				]
			}
		]
	})
	
	// 页面初始化操作
	MatOutWHNoTrain.init = function(){
		// 设置默认【领用人】信息
		Ext.getCmp("getEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【出库人】信息
		Ext.getCmp("exWhEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【入库库房】信息
		var whName_comb = Ext.getCmp("whName_k");
		whName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("whIdx_k").setValue(records[0].get('idx'));
			}
		});
		MatOutWHNoTrain.setMatOutWHNoTrainBillNo();
	};
	MatOutWHNoTrain.init();

});