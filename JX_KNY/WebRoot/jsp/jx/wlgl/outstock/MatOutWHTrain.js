/**
 * 机车外用料单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
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
	Ext.namespace('MatOutWHTrain');                       //定义命名空间
	
	/** ************ 定义全局函数开始 ************ */
	// 【新增】按钮触发的页面初始化操作（初始化页面）
	MatOutWHTrain.resetFn = function() {
		Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
        // 清空【单据信息】列表
		if (MatOutWHTrainDetail.grid.store.getCount() > 0) {
	        MatOutWHTrainDetail.grid.store.removeAll();
		}
        // 重新设置MatOutWHNoTrainIdx
        MatOutWHTrainDetail.matOutWHTrainIDX = "###";
		MatOutWHTrain.isTemporary = true;
		MatOutWHTrainDetail.grid.getTopToolbar().enable();
		Ext.getCmp('whName_k').enable();
		Ext.getCmp('getDate_k').enable();
		Ext.getCmp('getEmpId_k').enable();
		Ext.getCmp('exWhEmpId_k').enable();
		// 激活“车型”“车号”“修程”“修次”组件
		Ext.getCmp('trainType_comb').enable().clearValue();
		Ext.getCmp('trainNo_comb').enable().clearValue();
		Ext.getCmp('rc_comb').enable().clearValue();
		Ext.getCmp('rt_comb').enable().clearValue();
		MatOutWHTrain.baseForm.getForm().reset();
		
		// 设置默认【领用人】信息
		Ext.getCmp("getEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【出库人】信息
		Ext.getCmp("exWhEmpId_k").setDisplayValue(empId,empName);
		// 设置默认【出库库房】信息
		var whName_comb = Ext.getCmp("whName_k");
		whName_comb.setDisplayValue(whName_comb.getStore().getAt(0).get('wareHouseName'), whName_comb.getStore().getAt(0).get('wareHouseName'))
    	Ext.getCmp("whIdx_k").setValue(whName_comb.getStore().getAt(0).get('idx'));
		MatOutWHTrain.setMatOutWHNoTrainBillNo();
	}
	/** ************ 定义全局函数结束 ************ */
	
	/** ************ 定义全局变量开始 ************ */
	MatOutWHTrain.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	MatOutWHTrain.fieldWidth = 100;
	MatOutWHTrain.labelWidth = 70;
	MatOutWHTrain.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	MatOutWHTrain.isTemporary = true;					// 当前操作的记录是否为“未登账”记录的标识
	// 查询表单
	MatOutWHTrain.searchForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHTrain.labelWidth,
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
					width: MatOutWHTrain.fieldWidth
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
							var form = MatOutWHTrain.searchForm.getForm();
							if (!form.isValid()) {
								return;	
							}
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatOutWHTrain.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'新增', iconCls:'addIcon', width: 80, handler: MatOutWHTrain.resetFn
					}]
				}, {
					columnWidth:0.25, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
					xtype:'button', text:'删除', iconCls:'deleteIcon', width: 80, handler: function() {
							var sm = MatOutWHTrain.grid.getSelectionModel();
							if (sm.getCount() <= 0) {
									MyExt.Msg.alert("尚未选择任何记录！");
									return;
							}
							if (!MatOutWHTrain.isTemporary) {
								MyExt.Msg.alert("已登账的记录单不能删除！");
								return;
							} else {
								Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
							        if(btn == 'yes') {
								        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatOutWHTrain.grid,
								        	url: MatOutWHTrain.grid.deleteURL,
											params: {ids: $yd.getSelectedIdx(MatOutWHTrain.grid, MatOutWHTrain.grid.storeId)},
											success: function(response, options){
												// 重新加载主表格
												MatOutWHTrain.grid.store.reload();
										        // 初始化页面
										        MatOutWHTrain.resetFn();
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
							var sm = MatOutWHTrain.grid.getSelectionModel();
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
						        		MatOutWHTrain.loadMask.show();
						        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								        	scope: MatOutWHTrain.grid,
								        	url: ctx + '/matOutWHTrain!updateRollBack.action',
											params: {ids: $yd.getSelectedIdx(MatOutWHTrain.grid, MatOutWHTrain.grid.storeId)},
											success: function(response, options){
								              	MatOutWHTrain.loadMask.hide();
								                var result = Ext.util.JSON.decode(response.responseText);
								                if (result.errMsg == null) {
								                    alertSuccess();
											        // 撤销成功后的一些页面初始化方法
													MatOutWHTrain.isTemporary = true;
													MatOutWHTrainDetail.grid.getTopToolbar().enable();
													MatOutWHTrain.grid.store.reload();
													Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
													Ext.getCmp('whName_k').enable();
													Ext.getCmp('getDate_k').enable();
													Ext.getCmp('getEmpId_k').enable();
													Ext.getCmp('exWhEmpId_k').enable();
													Ext.getCmp('trainType_comb').enable();
													Ext.getCmp('trainNo_comb').enable();
													Ext.getCmp('rc_comb').enable();
													Ext.getCmp('rt_comb').enable();
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
	MatOutWHTrain.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWHTrain!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matOutWHTrain!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matOutWHTrain!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo', width: 80
		},{
			header:'单据摘要', dataIndex:'billSummary', width: 170
		},{
			header:'领用库房主键', dataIndex:'whIdx', hidden:true
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', width: 30
		},{
			header:'车号', dataIndex:'trainNo', width: 30
		},{
			header:'修程编码', dataIndex:'xcId', hidden:true
		},{
			header:'修程', dataIndex:'xcName', width: 30
		},{
			header:'修次编码', dataIndex:'rtId', hidden:true
		},{
			header:'修次', dataIndex:'rtName', width: 30
		},{
			header:'领用库房', dataIndex:'whName', width: 40
		},{
			header:'出库人主键', dataIndex:'exWhEmpId', hidden:true
		},{
			header:'出库人', dataIndex:'exWhEmp', hidden:true
		},{
			header:'领用人主键', dataIndex:'getEmpId', hidden:true
		},{
			header:'领用人', dataIndex:'getEmp', width: 40
		},{
			header:'领用机构id', dataIndex:'getOrgId', hidden:true
		},{
			header:'领用班组', dataIndex:'getOrg', width: 80
		},{
			header:'领用机构序列', dataIndex:'getOrgSeq', hidden:true
		},{
			header:'领用日期', dataIndex:'getDate', xtype:'datecolumn', width: 40
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登账人', dataIndex:'registEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'登账日期', dataIndex:'registDate', hidden:true,  xtype:'datecolumn', editor:{ xtype:'my97date' }
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
				var data = record.data;
				MatOutWHTrainDetail.matOutWHTrainIDX = data.idx;
				MatOutWHTrainDetail.grid.store.load();
				// 重新加载【单据信息】表单
				MatOutWHTrain.baseForm.getForm().loadRecord(record);
				// 回显“领用人”字段
				Ext.getCmp("getEmpId_k").setDisplayValue(data.getEmpId, data.getEmp);
				// 回显“出库人”字段
				Ext.getCmp("exWhEmpId_k").setDisplayValue(data.exWhEmpId, data.exWhEmp);
				Ext.getCmp('whName_k').setDisplayValue(data.whName, data.whName);
				// 回显“车型”
				Ext.getCmp("trainType_comb").setDisplayValue(data.trainTypeIDX, data.trainTypeShortName);
				// 回显“车号”
				Ext.getCmp("trainNo_comb").setDisplayValue(data.trainNo, data.trainNo);
				// 回显“修程”
				Ext.getCmp("rc_comb").setDisplayValue(data.xcId, data.xcName);
				// 回显“修次”
				Ext.getCmp("rt_comb").setDisplayValue(data.rtId, data.rtName);
				// 设置单据状态的显示标识
				var status = record.get('status');
				if (status == STATUS_ZC) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='green'>(暂存)</font>");
					MatOutWHTrain.isTemporary = true;
					MatOutWHTrainDetail.grid.getTopToolbar().enable();
					Ext.getCmp('whName_k').enable();
					Ext.getCmp('getDate_k').enable();
					Ext.getCmp('getEmpId_k').enable();
					Ext.getCmp('exWhEmpId_k').enable();
					Ext.getCmp('trainType_comb').enable();
					Ext.getCmp('trainNo_comb').enable();
					Ext.getCmp('rc_comb').enable();
					Ext.getCmp('rt_comb').enable();
				} else if (status == STATUS_DZ) {
					Ext.getCmp('basePanel').setTitle("单据信息<font color='red'>(已登账)</font>");
					MatOutWHTrain.isTemporary = false;
					MatOutWHTrainDetail.grid.getTopToolbar().disable();
					Ext.getCmp('whName_k').disable();
					Ext.getCmp('getDate_k').disable();
					Ext.getCmp('getEmpId_k').disable();
					Ext.getCmp('exWhEmpId_k').disable();
					Ext.getCmp('trainType_comb').disable();
					Ext.getCmp('trainNo_comb').disable();
					Ext.getCmp('rc_comb').disable();
					Ext.getCmp('rt_comb').disable();
				}
			}
		}
	});
	MatOutWHTrain.grid.un('rowdblclick', MatOutWHTrain.grid.toEditFn, MatOutWHTrain.grid);
	MatOutWHTrain.grid.store.on('beforeload', function() {
		var searchParams = MatOutWHTrain.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	
	// 【单据信息】表单
	MatOutWHTrain.baseForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHTrain.labelWidth,
		layout:"column",
		padding:"10px",
		items:[
			{
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"billNo_k",
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width: MatOutWHTrain.fieldWidth + 80
				}]
			},
			{
				layout:"form",
				columnWidth:0.33,
				items:[
					{
			        	id:"whName_k", fieldLabel:"领用库房",hiddenName:"whName",
						xtype:"Base_combo",allowBlank: false,width:MatOutWHTrain.fieldWidth + 20,
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
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getDate_k",
					name: "getDate", 
					fieldLabel: "领用日期",
					allowBlank: false, 
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatOutWHTrain.fieldWidth
				}]
			},
			{
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
					width:MatOutWHTrain.fieldWidth
				}, {
					id:"getEmp_k",xtype:"hidden", name:"getEmp",　value: empName
				}]
			},
			{
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getOrg_k",
					name: "getOrg", 
					fieldLabel: "领用班组",
					disabled: true,
					maxLength: 64,
					xtype: "textfield",
					value: orgName,
					width: MatOutWHTrain.fieldWidth + 80
				}, {
					id:"getOrgId_k",xtype:"hidden", name:"getOrgId", value:　orgId
				}, {
					id:"getOrgSeq_k",xtype:"hidden", name:"getOrgSeq"
				}]
			},
			{
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
					width:MatOutWHTrain.fieldWidth
				}, {
					id:"exWhEmp_k",xtype:"hidden", name:"exWhEmp",　value: empName
				}]
			},
			{
				layout:"column",
				columnWidth:1,
				items:[{
					layout:"form",
					columnWidth:.25,
					items:[{
						xtype: 'compositefield', fieldLabel : '下车车型', combineErrors: false,
			        	items: [{
							id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
							hiddenName: "trainTypeIDX",
							returnField: [{widgetId:"PartsAboardExWh_aboardTrainType",propertyName:"shortName"}],
							displayField: "shortName", valueField: "typeID",width: MatOutWHTrain.fieldWidth,
							pageSize: 20, //minListWidth: 200,   //isCx:'no',
							editable:true  ,allowBlank: false,
							listeners : {   
					        	"select" : function() {   
					            	//重新加载车号下拉数据
					                var trainNo_comb = Ext.getCmp("trainNo_comb");   
					                trainNo_comb.reset();  
					                trainNo_comb.clearValue(); 
					                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
					                trainNo_comb.cascadeStore();
					                //重新加载修程下拉数据
					                var rc_comb = Ext.getCmp("rc_comb");
					                rc_comb.reset();
					                rc_comb.clearValue();
					                rc_comb.getStore().removeAll();
					                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
					                rc_comb.cascadeStore();
									//重新加载修次数据
		    	                	var rt_comb = Ext.getCmp("rt_comb");
		    	                	rt_comb.clearValue();
		    	                 	rt_comb.reset();
		    	                 	rt_comb.getStore().removeAll();
		    	                 	rt_comb.cascadeStore();
		    	                 	//重置“修次”名称字段
		    	                 	Ext.getCmp('PartsAboardExWh_aboardRepairTime').reset();
					        	}   
					    	}
						}, {
							id:"PartsAboardExWh_aboardTrainType",xtype:"hidden", name:"trainTypeShortName"
		                }, {
			                xtype: 'button',
			                text: '在修机车',
			                width: 45,
			                handler: function(){
			               	    jx.jxgc.TrainTypeTreeSelect.createWin();
			               		jx.jxgc.TrainTypeTreeSelect.win.show();
			           	    }
			           }]
					}, {
						id:"makeBillEmp_k",
						fieldLabel : "制单人", value: empName,
						name:"makeBillEmp", 
						xtype:"textfield",
						width: MatOutWHTrain.fieldWidth,
						disabled:true
					}]
				},{
					layout:"form",
					columnWidth:.25,
					items:[{
						id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
						hiddenName: "trainNo",width:MatOutWHTrain.fieldWidth,
						displayField: "trainNo", valueField: "trainNo",
						pageSize: 20, minListWidth: 200, 
						editable:true, allowBlank: false,
						listeners: {
							"beforequery" : function(){
		    					//选择修次前先选车型
								var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
		    					if(trainTypeId == "" || trainTypeId == null){
		    						MyExt.Msg.alert("请先选择上车车型！");
		    						return false;
		    					}
		                	}
		    			}
					}, {
						id:"makeBillDate_k",
						name: "makeBillDate", 
						fieldLabel: "制单日期",
						xtype: "my97date",
						format: "Y-m-d", 
						width: MatOutWHTrain.fieldWidth,
						disabled:true
					}]
				},{
					layout:"form",
					columnWidth:.25,
					items:[{
						id:"rc_comb",
	        			xtype: "Base_combo",
	        			business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
	        			fieldLabel: "修程",
	        			hiddenName: "xcId", 
	        			returnField: [{widgetId:"PartsAboardExWh_aboardRepairClass",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: 200,
	        			queryHql: 'from UndertakeRc',
	        			width: MatOutWHTrain.fieldWidth,
	        			allowBlank: false,
	        			listeners : {  
	        				"beforequery" : function(){
		    					//选择修次前先选车型
		                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
		    					if(trainTypeId == "" || trainTypeId == null){
		    						MyExt.Msg.alert("请先选择上车车型！");
		    						return false;
		    					}
		                	},
	                    	"select" : function() {   
	                    		//重新加载修次数据
	                        	var rt_comb = Ext.getCmp("rt_comb");
	                        	rt_comb.clearValue();
	                         	rt_comb.reset();
	                            rt_comb.queryParams = {"rcIDX":this.getValue()};
	                            rt_comb.cascadeStore();  
	                    	}
	                    }
	        		}, {
	        			id:"PartsAboardExWh_aboardRepairClass",xtype:"hidden", name:"xcName"
	                }, {
						id:"registEmp_k",
						fieldLabel : "登账人",  value: empName,
						name:"registEmp", 
						xtype:"textfield", 
						width: MatOutWHTrain.fieldWidth,
						disabled:true
					}]
				}, {
				layout:"form",
					columnWidth:.25,
					items:[{
		    			id:"rt_comb",
		    			xtype: "Base_combo",
		    			fieldLabel: "修次",
		    			hiddenName: "rtId", 
		    			returnField: [{widgetId:"PartsAboardExWh_aboardRepairTime",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,
		    			minListWidth: 200,
		    			fields: ["repairtimeIDX","repairtimeName"],
	    				business: 'rcRt',
		    			listeners : {
		    				"beforequery" : function(){
		    					//选择修次前先选修程
		                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
		    					if(rcIdx == "" || rcIdx == null){
		    						MyExt.Msg.alert("请先选择上车修程！");
		    						return false;
		    					}
		                	}
		    			},
		    			width: MatOutWHTrain.fieldWidth
		    		}, {
		    			id:"PartsAboardExWh_aboardRepairTime",xtype:"hidden", name:"rtName"
		    		}, {
						id:"registDate_k",
						name: "registDate", 
						fieldLabel: "登账日期",
						xtype: "my97date",format: "Y-m-d",  
						width: MatOutWHTrain.fieldWidth,
						disabled:true
					}]
				}]
			}
		]
	});
	
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
		Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
		Ext.getCmp("PartsAboardExWh_aboardTrainType").setValue(node.attributes["trainTypeShortName"]);
		Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
		Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
		Ext.getCmp("PartsAboardExWh_aboardRepairClass").setValue(node.attributes["repairClassName"]);
		Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
		Ext.getCmp("PartsAboardExWh_aboardRepairTime").setValue(node.attributes["repairTimeName"]);
		//重新加载车号下拉数据
        var trainNo_comb = Ext.getCmp("trainNo_comb");   
        trainNo_comb.queryParams = {"trainTypeIDX":node.attributes["trainTypeIDX"]};
        trainNo_comb.cascadeStore();
        //重新加载修程下拉数据
        var rc_comb = Ext.getCmp("rc_comb");
        rc_comb.queryParams = {"TrainTypeIdx":node.attributes["trainTypeIDX"]};
        rc_comb.cascadeStore();
		//重新加载修次数据
    	var rt_comb = Ext.getCmp("rt_comb");
     	rt_comb.queryParams = {"rcIDX":node.attributes["repairClassIDX"]};
        rt_comb.cascadeStore(); 
    }
	/** ************ 定义全局变量开始 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	// 自动设置【单据编号】字段值
	MatOutWHTrain.setMatOutWHNoTrainBillNo = function() {
		Ext.Ajax.request({
            url: ctx + "/codeRuleConfig!getConfigRule.action",
            params: {ruleFunction: "WLGL_MAT_OUT_WH_TRAIN_BILL_NO"},
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
	MatOutWHTrain.beforeGetBaseDate = function() {
		Ext.getCmp('billNo_k').enable();
		Ext.getCmp('getOrg_k').enable();
		Ext.getCmp('makeBillEmp_k').enable();
		Ext.getCmp('makeBillDate_k').enable();
		Ext.getCmp('registEmp_k').enable();
		Ext.getCmp('registDate_k').enable();
	}
	// 获取表单数据后禁用被激活的字段
	MatOutWHTrain.afterGetBaseDate = function() {
		Ext.getCmp('billNo_k').disable();
		Ext.getCmp('getOrg_k').disable();
		Ext.getCmp('makeBillEmp_k').disable();
		Ext.getCmp('makeBillDate_k').disable();
		Ext.getCmp('registEmp_k').disable();
		Ext.getCmp('registDate_k').disable();
	}
	// 【暂存】按钮事件触发成功后的页面初始化事件处理
	MatOutWHTrain.afterSaveTemporayFn = function() {
        // 重新加载【单据列表】列表
        MatOutWHTrain.grid.store.reload();
        // 清空【单据信息】列表
        MatOutWHTrainDetail.grid.store.removeAll();
        // 重新设置单据编号
        MatOutWHTrain.setMatOutWHNoTrainBillNo();
        // 重新设置MatOutWHNoTrainIdx
        MatOutWHTrainDetail.matOutWHTrainIDX = "###";
	}
	// 【暂存】按钮触发的函数操作
	MatOutWHTrain.SaveTemporaryFn = function(isTemporary) {
		// 机车外用料单基本信息
		var form = MatOutWHTrain.baseForm.getForm();
		// 验证表单数据的合法性
		if (!form.isValid()) {
			return;
		}
		MatOutWHTrain.beforeGetBaseDate();
		var matOutWHTrain = form.getValues();
		if (MatOutWHTrainDetail.matOutWHTrainIDX != "###" && !Ext.isEmpty(MatOutWHTrainDetail.matOutWHTrainIDX)) {
			matOutWHTrain.idx = MatOutWHTrainDetail.matOutWHTrainIDX;
		}
		// 判断是【暂存】还是【登账并新增】操作
		if (isTemporary) {
			// 设置单据状态为【暂存（temporary）】
			matOutWHTrain.status = STATUS_ZC;
		} else {
			// 设置单据状态为【暂存（entryAccount）】
			matOutWHTrain.status = STATUS_DZ;
		}
		MatOutWHTrain.afterGetBaseDate();
		// 获取添加的明细数据
		var store = MatOutWHTrainDetail.store;
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
		
		MatOutWHTrain.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/matOutWHTrain!saveMatOutAndDetail.action',
            jsonData: datas,
            params : {matOutWHTrain : Ext.util.JSON.encode(MyJson.deleteBlankProp(matOutWHTrain))},
            success: function(response, options){
              	MatOutWHTrain.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    MatOutWHTrain.afterSaveTemporayFn();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatOutWHTrain.loadMask.hide();
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
						items: MatOutWHTrain.searchForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						items: MatOutWHTrain.grid
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
						items: MatOutWHTrain.baseForm
					},
					{
						xtype:"panel", border: false,
						region:"center",
						layout:"fit",
						buttonAlign:"center",
						items:MatOutWHTrainDetail.grid,
						buttons:[{
							text:'暂存',handler: function() {
								if (MatOutWHTrain.isTemporary) {
									MatOutWHTrain.SaveTemporaryFn(true);
								} else {
									MyExt.Msg.alert("已登账的记录单不能再次操作");
								}
							}
						}, {
							text:'登账并新增',handler: function() {
								if (MatOutWHTrain.isTemporary) {
									Ext.Msg.confirm("提示  ", "是否确认登账？  ", function(btn){
								        if(btn == 'yes') {
											MatOutWHTrain.SaveTemporaryFn(false);
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
	MatOutWHTrain.init = function(){
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
		MatOutWHTrain.setMatOutWHNoTrainBillNo();
	};
	MatOutWHTrain.init();

});