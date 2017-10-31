/**
 * 
 * 机车用料消耗记录js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatTrainExpendAccountModify');
	
	/** ************** 定义全局变量开始 ************** */
	MatTrainExpendAccountModify.searchParams = {};
	MatTrainExpendAccountModify.labelWidth = 80;
	MatTrainExpendAccountModify.fieldWidth = 120;
	MatTrainExpendAccountModify.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自定义“物料编码”组件的返回值方法
	MatTypeListSelect.returnFn = function(record) {
		var data = record.data;
		// 回显“物料编码”
		Ext.getCmp('matCode_k').setDisplayValue(data.matCode, data.matCode);
		// 回显“物料描述”
		Ext.getCmp('matDesc_k').setValue(data.matDesc);
		// 回显“物料计量单位”
		Ext.getCmp('unit_k').setValue(data.unit);
		// 回显“物料单价”
		Ext.getCmp('price_k').setValue(data.price);
	}
	// 保存方法执行前，启用“物料描述”，“物料计量单位”，“物料单价”字段
	MatTrainExpendAccountModify.beforeGetFormDataFn = function() {
		Ext.getCmp('matDesc_k').enable();
		Ext.getCmp('unit_k').enable();
		Ext.getCmp('price_k').enable();
	}
	// 保存方法执行后，禁用“物料描述”，“物料计量单位”，“物料单价”字段
	MatTrainExpendAccountModify.afterGetFormDataFn = function() {
		Ext.getCmp('matDesc_k').disable();
		Ext.getCmp('unit_k').disable();
		Ext.getCmp('price_k').disable();
	}
	// 保存方法
	MatTrainExpendAccountModify.saveTemporaryFn = function(isTemporary){
		var form = MatTrainExpendAccountModify.form.getForm();
		if (!form.isValid()) {
			return;
		}
		
		// 获取数据前启用被禁用的字段
		MatTrainExpendAccountModify.beforeGetFormDataFn();
		var jsonData = form.getValues();
		// 获取数据后禁用被启用的字段
		MatTrainExpendAccountModify.afterGetFormDataFn();
		
		// 判断是【暂存】还是【登账并新增】操作
		if (isTemporary) {
			// 设置单据状态为【暂存（temporary）】
			jsonData.status = STATUS_ZC;
		} else {
			// 设置单据状态为【暂存（entryAccount）】
			jsonData.status = STATUS_DZ;
		}
		jsonData = MyJson.deleteBlankProp(jsonData);
		Ext.Ajax.request({
            url: ctx + '/matTrainExpendAccount!saveTemporary.action',
            jsonData: Ext.util.JSON.encode(jsonData),
            success: function(response, options){
              	MatTrainExpendAccountModify.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    // 隐藏【修改消耗记录】窗口
                    MatTrainExpendAccountModify.win.hide();
                    // 重新加载主界面表格数据
                    MatTrainExpendAccount.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatTrainExpendAccountModify.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义修改表单开始 ************** */
	MatTrainExpendAccountModify.form = new Ext.form.FormPanel({
		labelWidth: MatTrainExpendAccountModify.labelWidth,
		border: false, baseCls: 'x-plain',
		layout:"form", style: 'padding: 15px;',
		items:[{
			xtype:"panel", border: false, baseCls: 'x-plain',
			layout:"column",
			items:[{
				columnWidth:0.5, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
					id:"trainType_comb", xtype: "TrainType_combo", fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					returnField: [{widgetId:"PartsAboardExWh_aboardTrainType",propertyName:"shortName"}],
					displayField: "shortName", valueField: "typeID",width:MatTrainExpendAccountModify.fieldWidth,
					pageSize: 20,
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
        			width: MatTrainExpendAccountModify.fieldWidth,
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
					fieldLabel:"消耗班组",
					allowBlank: false,
					width: MatTrainExpendAccountModify.fieldWidth,
        			id: 'OmOrganizationCustom_comboTree_Id', 
        			xtype: 'OmOrganizationCustom_comboTree', 
					hiddenName: 'expendOrgId', 
					returnField: [{
						widgetId:"expendOrg_k",propertyName:"orgname"
					}, {
						widgetId:"expendOrgSeq_k",propertyName:"orgseq"
					}], 
					selectNodeModel:'all',							   
					//queryHql: "from OmOrganization where 1=1 and status = 'running' and orgdegree='oversea'",
					queryHql: '[degree]tream',
					editable: false,
					listeners: {
						"select": function() {
							// 重新加载“物料信息选择组件”数据
							MatTypeListSelect.orgId = this.getValue();
							MatTypeListSelect.grid.store.load();
						}
					}
				}, {
					xtype:"MatTypeList_SelectWin",
					id: 'matCode_k',
					name: 'matCode',
					fieldLabel:"物料编码", maxLength: 50,
					editable: false,
					allowBlank: false,
					width: MatTrainExpendAccountModify.fieldWidth
				}, {
					xtype:"textfield",
					id: 'unit_k',
					name: 'unit',
					disabled: true,
					fieldLabel:"计量单位",
					width: MatTrainExpendAccountModify.fieldWidth
				}]
			}, {
				columnWidth:0.5, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
					id:"trainNo_comb", xtype: "TrainNo_combo", fieldLabel: "车号",
					hiddenName: "trainNo",width:MatTrainExpendAccountModify.fieldWidth,
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
    			width: MatTrainExpendAccountModify.fieldWidth
    		}, {
				xtype:"my97date", format: 'Y-m-d',
				name: 'expendDate',
				allowBlank: false,
				fieldLabel:"消耗日期",
				width: MatTrainExpendAccountModify.fieldWidth
			}, {
				xtype:"textfield",
				name: 'qty',
				allowBlank: false,
				fieldLabel:"数量", maxLength: 4,
				width: MatTrainExpendAccountModify.fieldWidth
			}, {
				xtype:"textfield",
				id: 'price_k',
				name: 'price',
				disabled: true,
				allowBlank: false,
				fieldLabel:"单价（元）",
				width: MatTrainExpendAccountModify.fieldWidth
			}]
			}
		]}, {
			fieldLabel:"物料描述", id:'matDesc_k', name: 'matDesc',  maxLength: 100, xtype:"textfield", width: 357, disabled: true
		}, {
			name: 'idx', xtype:"hidden", fieldLabel:"idx主键"
		}, {
			fieldLabel: '车型简称', xtype:"hidden", id:"PartsAboardExWh_aboardTrainType", name:"trainTypeShortName"
		}, {
			fieldLabel: '修程名称', xtype:"hidden", id:"PartsAboardExWh_aboardRepairClass", name:"xcName"
		}, {
			fieldLabel: '修次名称', xtype:"hidden", id:"PartsAboardExWh_aboardRepairTime", name:"rtName"
		}, {
			fieldLabel: '消耗班组序列', xtype:"hidden", id:"expendOrgSeq_k", name:"expendOrgSeq"
		}, {
			fieldLabel: '消耗班组名称', xtype:"hidden", id:"expendOrg_k", name:"expendOrg" 
		}]
	})
	/** ************** 定义修改表单结束 ************** */
	
	/** ************** 定义修改窗口开始 ************** */
	MatTrainExpendAccountModify.win = new Ext.Window({
		title:"修改消耗记录",
		width:520, height:260,
		layout:"fit",
		plain: true,
		closeAction: 'hide',
		items: MatTrainExpendAccountModify.form,
		buttonAlign: 'center',
		buttons: [{
			text: '暂存', handler: function() {
				MatTrainExpendAccountModify.saveTemporaryFn(true);
			}
		}, {
			text: '登账', handler: function() {
				Ext.Msg.confirm("提示  ", "是否确认登账？  ", function(btn){
			        if(btn == 'yes') {
						MatTrainExpendAccountModify.saveTemporaryFn(false);
			        }
		        });
			}
		}, {
			text: '关闭', handler: function() {
				MatTrainExpendAccountModify.win.hide();
			}
		}]
	});
	/** ************** 定义修改窗口结束 ************** */
	
});