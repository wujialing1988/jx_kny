/**
 * 物料申请单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
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
	Ext.namespace('MatPlan');                       //定义命名空间
	
	/** ************ 定义全局变量开始 ************ */
	MatPlan.fieldWidth = 100;
	MatPlan.labelWidth = 70;
	MatPlan.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	MatPlan.isTemporary = true;					// 当前操作的记录是否为“未登账”记录的标识
	// 查询表单
	MatPlan.searchForm = new Ext.form.FormPanel({
		labelWidth:MatPlan.labelWidth,
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
					xtype: 'compositefield', fieldLabel: '申请日期', combineErrors: false,
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
						data : [[STATUS_SY, "所有"], [STATUS_ZC, "待提交"], [STATUS_DZ, "已提交"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_SY,
					mode:'local',
					width: MatPlan.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"column",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[{
					columnWidth:0.33, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'查询', iconCls:'searchIcon', width: 80, handler: function() {
							var form = MatPlan.searchForm.getForm();
							if (!form.isValid()) {
								return;	
							}
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatPlan.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.33, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'新增', iconCls:'addIcon', width: 80, handler: function() {
							Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(待提交)</font>");
					        // 清空【单据信息】列表
							if (MatPlanDetail.grid.store.getCount() > 0) {
						        MatPlanDetail.grid.store.removeAll();
							}
					        // 重新设置matPlanIdx
					        MatPlanDetail.matPlanIdx = "###";
							MatPlan.isTemporary = true;
							Ext.getCmp('applyEmpId_k').enable();
							Ext.getCmp('applyDate_k').enable();
							Ext.getCmp('useTeam_k').enable();
							Ext.getCmp('planName_k').enable();
							MatPlanDetail.grid.getTopToolbar().enable();
							MatPlan.baseForm.getForm().reset();
							
							// 设置默认【申请人】信息
							Ext.getCmp("applyEmpId_k").setDisplayValue(empId,empName);
							Ext.getCmp("applyEmp_k").setValue(empName);
							// 重新生成单据编号
							MatPlan.setMatInWHBillNo();
						}
					}]
				}, {
					columnWidth:0.33, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'删除', iconCls:'deleteIcon', width: 80, handler: function() {
							var sm = MatPlan.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							if (!MatPlan.isTemporary) {
								MyExt.Msg.alert("已提交的记录单不能删除！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
							        if(btn == 'yes') {
								        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatPlan.grid,
								        	url: MatPlan.grid.deleteURL,
											params: {ids: $yd.getSelectedIdx(MatPlan.grid, MatPlan.grid.storeId)}
								        }));
								        // 重新设置matPlanIdx
								        MatPlanDetail.matPlanIdx = "###";
								        MatPlanDetail.grid.store.load();
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
	MatPlan.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matPlan!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matPlan!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matPlan!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo'
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
		}],
		listeners: {
			'rowclick': function(grid, rowIndex, e) {
				var record = grid.store.getAt(rowIndex);
				MatPlanDetail.matPlanIdx = record.data.idx;
				MatPlanDetail.grid.store.load();
				// 重新加载【单据信息】表单
				MatPlan.baseForm.getForm().loadRecord(record);
				Ext.getCmp("applyEmpId_k").setDisplayValue(record.data.applyEmpId ,record.data.applyEmp);
				// 设置单据状态的显示标识
				var status = record.get('status');
				if (status == STATUS_ZC) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(待提交)</font>");
					MatPlan.isTemporary = true;
					Ext.getCmp('applyEmpId_k').enable();
					Ext.getCmp('applyDate_k').enable();
					Ext.getCmp('useTeam_k').enable();
					Ext.getCmp('planName_k').enable();
					MatPlanDetail.grid.getTopToolbar().enable();
				} else if (status == STATUS_DZ) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='red'>(已提交)</font>");
					MatPlan.isTemporary = false;
					Ext.getCmp('applyEmpId_k').disable();
					Ext.getCmp('applyDate_k').disable();
					Ext.getCmp('useTeam_k').disable();
					Ext.getCmp('planName_k').disable();
					MatPlanDetail.grid.getTopToolbar().disable();
				}
			}
		}
	});
	MatPlan.grid.un('rowdblclick', MatPlan.grid.toEditFn, MatPlan.grid);
	MatPlan.grid.store.on('beforeload', function() {
		var searchParams = MatPlan.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	
	// 【单据信息】表单
	MatPlan.baseForm = new Ext.form.FormPanel({
		labelWidth:MatPlan.labelWidth,
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
					width: MatPlan.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"applyEmpId_k",
					xtype:'OmEmployee_SelectWin',
					editable:false,
					allowBlank: false,
					fieldLabel : "申请人",
					hiddenName:'applyEmpId',
					returnField:[{
						widgetId:"applyEmp_k",
						propertyName:"empname"
					}],
					width:MatPlan.fieldWidth
				}, {
					id:"applyEmp_k",xtype:"hidden", name:"applyEmp"
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.5,
				items:[{
					id:"applyDate_k",
					name: "applyDate", 
					fieldLabel: "申请日期",
					allowBlank: false, 
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatPlan.fieldWidth
				}]
			},
			{
				xtype:"panel",
				columnWidth:0.25,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"useTeam_k", 
					name:"useTeam", 
					fieldLabel:"使用班组",
					width: MatPlan.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				columnWidth: 0.75,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"planName_k", 
					name:"planName", 
					fieldLabel:"计划名称",
					width: MatPlan.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillEmp_k",
					fieldLabel : "制单人", value: empName,
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatPlan.fieldWidth,
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
					width: MatPlan.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registEmp_k",
					fieldLabel : "提交人",  value: empName,
					name:"registEmp", 
					xtype:"textfield", 
					width: MatPlan.fieldWidth,
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
					fieldLabel: "提交日期",
					xtype: "my97date",format: "Y-m-d",  
					width: MatPlan.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
	/** ************ 定义全局变量开始 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	// 自动设置【单据编号】字段值
	MatPlan.setMatInWHBillNo = function() {
		Ext.Ajax.request({
            url: ctx + "/codeRuleConfig!getConfigRule.action",
            params: {ruleFunction: "WLGL_MAT_PLAN_BILL_NO"},
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
	MatPlan.beforeGetBaseDate = function() {
		Ext.getCmp('billNo_k').enable();
		Ext.getCmp('makeBillEmp_k').enable();
		Ext.getCmp('makeBillDate_k').enable();
		Ext.getCmp('registEmp_k').enable();
		Ext.getCmp('registDate_k').enable();
	}
	// 获取表单数据后禁用被激活的字段
	MatPlan.afterGetBaseDate = function() {
		Ext.getCmp('billNo_k').disable();
		Ext.getCmp('makeBillEmp_k').disable();
		Ext.getCmp('makeBillDate_k').disable();
		Ext.getCmp('registEmp_k').disable();
		Ext.getCmp('registDate_k').disable();
	}
	// 【保存】按钮事件触发成功后的页面初始化事件处理
	MatPlan.afterSaveTemporayFn = function() {
        // 重新加载【单据列表】列表
        MatPlan.grid.store.reload();
        // 清空【单据信息】列表
        MatPlanDetail.grid.store.removeAll();
        // 重新设置单据编号
        MatPlan.setMatInWHBillNo();
        // 重新设置matPlanIdx
        MatPlanDetail.matPlanIdx = "###";
	}
	// 【保存】按钮触发的函数操作
	MatPlan.SaveTemporaryFn = function(isTemporary) {
		// 物料申请单基本信息
		var form = MatPlan.baseForm.getForm();
		// 验证表单数据的合法性
		if (!form.isValid()) {
			return;
		}
		MatPlan.beforeGetBaseDate();
		var matPlan =  form.getValues();
		if (MatPlanDetail.matPlanIdx != "###" && !Ext.isEmpty(MatPlanDetail.matPlanIdx)) {
			matPlan.idx = MatPlanDetail.matPlanIdx;
		}
		// 判断是【待提交】还是【登账并新增】操作
		if (isTemporary) {
			// 设置单据状态为【待提交（temporary）】
			matPlan.status = STATUS_ZC;
		} else {
			// 设置单据状态为【待提交（entryAccount）】
			matPlan.status = STATUS_DZ;
		}
		MatPlan.afterGetBaseDate();
		// 获取添加的明细数据
		var store = MatPlanDetail.store;
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
		
		MatPlan.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/matPlan!savePlanAndDetail.action',
            jsonData: datas,
            params : {matPlan : Ext.util.JSON.encode(MyJson.deleteBlankProp(matPlan))},
            success: function(response, options){
              	MatPlan.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    MatPlan.afterSaveTemporayFn();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatPlan.loadMask.hide();
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
						items: MatPlan.searchForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						items: MatPlan.grid
					}
				]
			}, {
				region:"center",
				layout:"border",
				items:[
					{
						xtype:"panel", border: false, frame:true,
						id:"basePanel",
						title:"单据信息<font color='green'>(待提交)</font>",
						region:"north",
						layout:"fit",
						height:130,
						items: MatPlan.baseForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						buttonAlign:"center",
						items:MatPlanDetail.grid,
						buttons:[{
							text:'保存',handler: function() {
								if (MatPlan.isTemporary) {
									MatPlan.SaveTemporaryFn(true);
								} else {
									MyExt.Msg.alert("已提交的记录单不能再次操作");
								}
							}
						}, {
							text:'提交',handler: function() {
								if (MatPlan.isTemporary) {
									Ext.Msg.confirm("提示  ", "是否确认提交？  ", function(btn){
								        if(btn == 'yes') {
											MatPlan.SaveTemporaryFn(false);
								        }
							        });
								} else {
									MyExt.Msg.alert("已提交的记录单不能再次操作");
								}
							}
						}]
					}
				]
			}
		]
	})
	
	// 页面初始化操作
	MatPlan.init = function(){
		// 设置默认【申请人】信息
		Ext.getCmp("applyEmpId_k").setDisplayValue(empId,empName);
		Ext.getCmp("applyEmp_k").setValue(empName);
		MatPlan.setMatInWHBillNo();
	};
	MatPlan.init();

});