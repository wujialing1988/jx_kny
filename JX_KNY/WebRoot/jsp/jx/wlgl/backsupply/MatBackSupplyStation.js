/**
 * 质量反馈单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('MatBackSupplyStation');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	MatBackSupplyStation.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	MatBackSupplyStation.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【回滚】触发的函数操作
	MatBackSupplyStation.rollBackFn = function() {
		var sm = MatBackSupplyStation.grid.getSelectionModel();
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
	        		MatBackSupplyStation.loadMask.show();
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			        	scope: MatBackSupplyStation.grid,
			        	url: ctx + '/matBackSupplyStation!updateRollBack.action',
						params: {ids: $yd.getSelectedIdx(MatBackSupplyStation.grid, MatBackSupplyStation.grid.storeId)},
						success: function(response, options){
			              	MatBackSupplyStation.loadMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
						        // 撤销成功后的一些页面初始化方法
								MatBackSupplyStation.grid.store.reload();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        }));
		        }
		    });    
		}
	}
	/** ************** 定义全局函数结束 ************** */
		
	MatBackSupplyStation.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matBackSupplyStation!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matBackSupplyStation!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matBackSupplyStation!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    tbar:[{
					text:'添加', iconCls:'addIcon', handler:function() {
						//表单验证是否通过
				        var form = MatBackSupplyStation.grid.saveForm.getForm(); 
				        if (!form.isValid()) return;
						MatBackSupplyStation.grid.saveFn();
						MatBackSupplyStation.grid.store.load();
						form.reset();
						MatBackSupplyStation.init();
						Ext.getCmp("whName_k").getStore().reload();
						Ext.getCmp("matCode_k").clearValue();
		           }},{
					text:'删除', iconCls:'deleteIcon', handler:function() {
		                var sm = MatBackSupplyStation.grid.getSelectionModel();
					    if (sm.getCount() < 1) {
					        MyExt.Msg.alert("尚未选择一条记录！");
					        return;
					    }
					    var data = sm.getSelections();
					    var ids = new Array();
					    for (var i = 0; i < data.length; i++){
					    	if(data[ i ].get("status") != STATUS_ZC){
					    		MyExt.Msg.alert("只有状态为【待登帐】的数据才能删除！");
					        	return;
					    	}
				    		ids.push(data[ i ].get("idx"));
					    }
						Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
					        if(btn == 'yes') {
						        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
						        	scope: MatBackSupplyStation.grid,
						        	url: MatBackSupplyStation.grid.deleteURL,
									params: {ids: ids}
						        }));
						        MatBackSupplyStation.grid.store.load();
					        }
					    });    
		           }},{
					text:'登帐', iconCls:'checkIcon', handler:function() {
		                var sm = MatBackSupplyStation.grid.getSelectionModel();
					    if (sm.getCount() < 1) {
					        MyExt.Msg.alert("尚未选择一条记录！");
					        return;
					    }
					    var data = sm.getSelections();
					    var ids = new Array();
					    for (var i = 0; i < data.length; i++){
					    	if(data[ i ].get("status") != STATUS_ZC){
					    		MyExt.Msg.alert("只有状态为【待登帐】的数据才能进行登帐操作！");
					        	return;
					    	}
				    		ids.push(data[ i ].get("idx"));
					    }
					    Ext.Msg.confirm("提示  ", "是否确认登账？", function(btn){
					        if(btn == 'yes') {
						    Ext.Ajax.request({
						        url: ctx + "/matBackSupplyStation!updateMatBackSupplyStation.action",
						        params: {ids: ids},
						        success: function(response, options){
						            var result = Ext.util.JSON.decode(response.responseText);
						            if (result.errMsg == null) {
						                alertSuccess();
						                MatBackSupplyStation.grid.store.reload(); 
						            } else {
						                alertFail(result.errMsg);
						            }
						        },
						        failure: function(response, options){
						            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						        }
						     });   
					        }
					    })
		           }},{
		           	text:'回滚', iconCls:'resetIcon', handler: MatBackSupplyStation.rollBackFn
		           }, 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'库房名称', dataIndex:'whName', editor:{  maxLength:50 }
		},{
			header:'反馈人主键', dataIndex:'feedBackEmpID',hidden:true,  editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'反馈人名称', dataIndex:'feedBackEmp', editor:{  maxLength:25 }
		},{
			header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50 }
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:100 }
		},{
			header:'单位', dataIndex:'unit', editor:{  maxLength:20 }
		},{
			header:'数量', dataIndex:'qty', editor:{ xtype:'numberfield' }
		},{
			header:'反馈日期', dataIndex:'feedBackDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'存在原因', dataIndex:'feedBackReason', editor:{  maxLength:200 }
		},{
			header:'单据状态', dataIndex:'status', editor:{  maxLength:20 },renderer: function(v) {
					if (v == STATUS_ZC) return "待登帐";
					if (v == STATUS_DZ) return "已登账";
					return "错误！未知状态";
				}
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true,editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登帐人', dataIndex:'registEmp',hidden:true, editor:{  maxLength:25 }
		},{
			header:'登帐日期', dataIndex:'registDate', hidden:true,xtype:'datecolumn', editor:{ xtype:'my97date' }
		}]
	});
	MatBackSupplyStation.grid.store.on('beforeload', function() {
			var searchParams = MatBackSupplyStation.searchParams;
			searchParams = MyJson.deleteBlankProp(searchParams);
			MatBackSupplyStation.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		})
	// 【单据信息】表单
		MatBackSupplyStation.grid.saveForm = new Ext.form.FormPanel({
			labelWidth:MatBackSupplyStation.labelWidth,
			layout:"column",
			padding:"10px",
			items:[
				{
					xtype:"panel",
					layout:"form",
					columnWidth:0.33,
					items:[
						{
				        	id:"whName_k", fieldLabel:"库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,width:MatBackSupplyStation.fieldWidth,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"whIdx_k",propertyName:"idx"}],
							listeners:{
						    	"select" : function(combo, record){
							    	whIdx = record.get("idx");
							    	}
							    }
				        },
	                    {id:"whIdx_k",xtype:"hidden", name:"whIdx"}
					]
				},
				{
					xtype:"panel",
					layout:"form",
					columnWidth:0.33,
					items:[{
						id:"feedBackEmpID_k",
						xtype:'OmEmployee_SelectWin',
						editable:false,
						allowBlank: false,
						fieldLabel : "反馈人",
						hiddenName:'feedBackEmpID',
						returnField:[{
							widgetId:"feedBackEmp_k",
							propertyName:"empname"
						}],
						width:MatBackSupplyStation.fieldWidth
					}, {
						id:"feedBackEmp_k",xtype:"hidden", name:"feedBackEmp"
					}, {
						id:"makeBillEmp_k",xtype:"hidden", name:"makeBillEmp"
					}, {
						id:"makeBillDate_k",xtype:"hidden", name:"makeBillDate"
					}, {
						id:"status_k",xtype:"hidden", name:"status"
					}]
				},
				{
					xtype:"panel",
					layout:"form",
					columnWidth:0.33,
					items:[{
						id:"feedBackDate_k",
						name: "feedBackDate", 
						fieldLabel: "反馈日期",
						allowBlank: false, 
						xtype: "my97date",
						format: "Y-m-d",  
						width: MatBackSupplyStation.fieldWidth
					}]
				},{
					xtype:"panel",
					columnWidth:0.33,
					layout:"form",
					items:[{
						id:"matCode_k",
						xtype:"MatStock_SelectWin", 
						hiddenName:"matCode",
						allowBlank: false,
						fieldLabel:"物料编码",
						valueField : 'matCode',
						displayField : 'matCode',
						returnField:[{widgetId:"unit_k",propertyName:"unit"},
									 {widgetId:"matDesc_k",propertyName:"matDesc"}],
						width:MatBackSupplyStation.fieldWidth
					}]
				},{
					xtype:"panel",
					columnWidth:0.33,
					layout:"form",
					items:[{
						id:"unit_k",
						xtype:"textfield", 
						name:"unit", 
						fieldLabel:"计量单位",
						width:MatBackSupplyStation.fieldWidth
					}]
				},{
					xtype:"panel",
					columnWidth:0.33,
					layout:"form",
					items:[{
						xtype:"textfield", 
						name:"qty", 
						maxLength:8,
						allowBlank: false,
						fieldLabel:"数量",
						vtype:"positiveInt",
						width:MatBackSupplyStation.fieldWidth
					}]
				},{
					xtype:"panel",
					columnWidth:1,
					layout:"form",
					items:[{
						id:"matDesc_K",
						xtype:"textfield", 
						name:"matDesc", 
						fieldLabel:"物料描述",
						width:580
					}]
				},{
					xtype:"panel",
					columnWidth:1,
					layout:"form",
					items:[{
						xtype:"textfield", 
						name:"feedBackReason", 
						fieldLabel:"存在问题",
						width:580
					}]
				}
			]
		});
	MatBackSupplyStation.grid.un("rowdblclick",MatBackSupplyStation.grid.toEditFn,MatBackSupplyStation.grid);
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'border', 
		items:[{
					xtype:"panel", border: false, 
					region:"north",
					height:142,
					layout:"fit",frame:true,
					items: MatBackSupplyStation.grid.saveForm
				},
				{
					xtype:"panel", border: false,
					region:"center",
					layout:"fit",
					items: MatBackSupplyStation.grid
				}
	] });
	// 页面初始化操作
	MatBackSupplyStation.init = function(){
		// 设置默认【反馈人】信息
		Ext.getCmp("feedBackEmpID_k").setDisplayValue(empId,empName);
		Ext.getCmp("feedBackEmp_k").setValue(empName);
		Ext.getCmp("makeBillEmp_k").setValue(empName);
		Ext.getCmp("makeBillDate_k").setValue(new Date().format('Y-m-d'));
		Ext.getCmp("status_k").setValue(STATUS_ZC);//状态默认为【暂存】
		// 设置默认【库房】信息
		var whName_comb = Ext.getCmp("whName_k");
		whName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("whIdx_k").setValue(records[0].get('idx'));
		    	whIdx = records[0].get('idx');//库房id
				}
			});
		};
		MatBackSupplyStation.init();
});