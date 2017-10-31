/**
 * 
 * 机车用料消耗记录js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatTrainExpendAccountAdd');
	
	/** ************** 定义全局变量开始 ************** */
	MatTrainExpendAccountAdd.searchParams = {};
	MatTrainExpendAccountAdd.labelWidth = 70;
	MatTrainExpendAccountAdd.fieldWidth = 100;
	MatTrainExpendAccountAdd.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 获取表单数据前激活被禁用的字段
	MatTrainExpendAccountAdd.beforeGetBaseDate = function() {
		Ext.getCmp('registEmp_a').enable();
		Ext.getCmp('registDate_a').enable();
	}
	// 获取表单数据后禁用被激活的字段
	MatTrainExpendAccountAdd.afterGetBaseDate = function() {
		Ext.getCmp('registEmp_a').disable();
		Ext.getCmp('registDate_a').disable();
	}
	// 【暂存】按钮触发的函数操作
	MatTrainExpendAccountAdd.saveTemporaryFn = function(isTemporary){
		// 机车外用料单基本信息
		var form = MatTrainExpendAccountAdd.form.getForm();
		// 验证表单数据的合法性
		if (!form.isValid()) {
			return;
		}
		MatTrainExpendAccountAdd.beforeGetBaseDate();
		var matTrainExpendAccount = form.getValues();
		// 判断是【暂存】还是【登账并新增】操作
		if (isTemporary) {
			// 设置单据状态为【暂存（temporary）】
			matTrainExpendAccount.status = STATUS_ZC;
		} else {
			// 设置单据状态为【暂存（entryAccount）】
			matTrainExpendAccount.status = STATUS_DZ;
		}
		MatTrainExpendAccountAdd.afterGetBaseDate();
		// 获取添加的明细数据
		var store = MatTrainExpendAccountDetail.store;
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
		
		MatTrainExpendAccountAdd.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/matTrainExpendAccount!saveTemporaryForAdd.action',
            jsonData: datas,
            params : {matTrainExpendAccount : Ext.util.JSON.encode(MyJson.deleteBlankProp(matTrainExpendAccount))},
            success: function(response, options){
              	MatTrainExpendAccountAdd.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    // 隐藏新增窗口
                    MatTrainExpendAccountAdd.win.hide();
                    // 重新加载主界面表格数据
                    MatTrainExpendAccount.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MatTrainExpendAccountAdd.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义修改表单开始 ************** */
	MatTrainExpendAccountAdd.form = new Ext.form.FormPanel({
		labelWidth: MatTrainExpendAccountAdd.labelWidth,
		border: false, baseCls: 'x-plain',
		layout:"form", style: 'padding: 10px;',
		items:[{
			xtype:"panel", border: false, baseCls: 'x-plain',
			layout:"column",
			items:[{
				columnWidth:0.25, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
					id:"trainTypeIDX_a", xtype: "TrainType_combo", fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					returnField: [{widgetId:"trainTypeShortName_a",propertyName:"shortName"}],
					displayField: "shortName", valueField: "typeID", width: MatTrainExpendAccountAdd.fieldWidth,
					pageSize: 20,
					editable:true  ,allowBlank: false,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_a = Ext.getCmp("trainNo_a");   
			                trainNo_a.reset();  
			                trainNo_a.clearValue(); 
			                trainNo_a.queryParams = {"trainTypeIDX":this.getValue()};
			                trainNo_a.cascadeStore();
			                //重新加载修程下拉数据
			                var xcId_a = Ext.getCmp("xcId_a");
			                xcId_a.reset();
			                xcId_a.clearValue();
			                xcId_a.getStore().removeAll();
			                xcId_a.queryParams = {"TrainTypeIdx":this.getValue()};
			                xcId_a.cascadeStore();
							//重新加载修次数据
    	                	var rtId_a = Ext.getCmp("rtId_a");
    	                	rtId_a.clearValue();
    	                 	rtId_a.reset();
    	                 	rtId_a.getStore().removeAll();
    	                 	rtId_a.cascadeStore();
    	                 	//重置“修次”名称字段
    	                 	Ext.getCmp('rtName_a').reset();
			        	}   
			    	}
				}, {
					fieldLabel:"消耗班组",
					allowBlank: false,
					width: MatTrainExpendAccountAdd.fieldWidth,
        			id: 'expendOrgId_a', 
        			xtype: 'OmOrganizationCustom_comboTree', 
					hiddenName: 'expendOrgId', 
					returnField: [{
						widgetId:"expendOrg_a",propertyName:"orgname"
					}, {
						widgetId:"expendOrgSeq_a",propertyName:"orgseq"
					}], 
					selectNodeModel:'all',							   
					//queryHql: "from OmOrganization where 1=1 and status = 'running' and orgdegree='oversea'",
					queryHql: '[degree]tream',
					editable: false
				}]
			}, {
				columnWidth:0.25, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
					id:"trainNo_a", xtype: "TrainNo_combo", fieldLabel: "车号",
					hiddenName: "trainNo",width:MatTrainExpendAccountAdd.fieldWidth,
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200, 
					editable:true, allowBlank: false,
					listeners: {
						"beforequery" : function(){
	    					//选择修次前先选车型
							var trainTypeId =  Ext.getCmp("trainTypeIDX_a").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
	    						return false;
	    					}
	                	}
	    			}
				}, {
					xtype:"my97date", format: 'Y-m-d',
					name: 'expendDate',
					allowBlank: false,
					fieldLabel:"消耗日期",
					width: MatTrainExpendAccountAdd.fieldWidth
				}]
			}, {
				columnWidth:0.25, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
					id:"xcId_a",
	    			xtype: "Base_combo",
	    			business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
	    			fieldLabel: "修程",
	    			hiddenName: "xcId", 
	    			returnField: [{widgetId:"xcName_a",propertyName:"xcName"}],
	    			displayField: "xcName",
	    			valueField: "xcID",
	    			pageSize: 20, minListWidth: 200,
	    			queryHql: 'from UndertakeRc',
	    			width: MatTrainExpendAccountAdd.fieldWidth,
	    			allowBlank: false,
	    			listeners : {  
	    				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainTypeIDX_a").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
	    						return false;
	    					}
	                	},
	                	"select" : function() {   
	                		//重新加载修次数据
	                    	var rtId_a = Ext.getCmp("rtId_a");
	                    	rtId_a.clearValue();
	                     	rtId_a.reset();
	                        rtId_a.queryParams = {"rcIDX":this.getValue()};
	                        rtId_a.cascadeStore();  
	                	}
	                }
	    		}, {
					xtype:"textfield",
					name: 'registEmp',
					id:'registEmp_a',
					disabled: true,
					fieldLabel:"登账人", value: empName,
					width: MatTrainExpendAccountAdd.fieldWidth
				}]
			}, {
				columnWidth:0.25, border: false, baseCls: 'x-plain',
				layout:"form",
				items:[{
	    			id:"rtId_a",
	    			xtype: "Base_combo",
	    			fieldLabel: "修次",
	    			hiddenName: "rtId", 
	    			returnField: [{widgetId:"rtName_a",propertyName:"repairtimeName"}],
	    			displayField: "repairtimeName",
	    			valueField: "repairtimeIDX",
	    			pageSize: 0,
	    			minListWidth: 200,
	    			fields: ["repairtimeIDX","repairtimeName"],
					business: 'rcRt',
	    			listeners : {
	    				"beforequery" : function(){
	    					//选择修次前先选修程
	                		var rcIdx =  Ext.getCmp("xcId_a").getValue();
	    					if(rcIdx == "" || rcIdx == null){
	    						MyExt.Msg.alert("请先选择上车修程！");
	    						return false;
	    					}
	                	}
	    			},
	    			width: MatTrainExpendAccountAdd.fieldWidth
	    		}, {
					xtype:"my97date", format: 'Y-m-d',
					name: 'registDate',
					id: 'registDate_a',
					disabled: true,
					fieldLabel:"登账日期",
					width: MatTrainExpendAccountAdd.fieldWidth
				}]
			}]
		}, {
			fieldLabel: '车型简称', xtype:"hidden", id:"trainTypeShortName_a", name:"trainTypeShortName"
		}, {
			fieldLabel: '修程名称', xtype:"hidden", id:"xcName_a", name:"xcName"
		}, {
			fieldLabel: '修次名称', xtype:"hidden", id:"rtName_a", name:"rtName"
		}, {
			fieldLabel: '消耗班组序列', xtype:"hidden", id:"expendOrgSeq_a", name:"expendOrgSeq", value: orgSeq
		}, {
			fieldLabel: '消耗班组名称', xtype:"hidden", id:"expendOrg_a", name:"expendOrg", value: orgName
		}]
	})
	/** ************** 定义修改表单结束 ************** */
	
	/** ************** 定义修改窗口开始 ************** */
	MatTrainExpendAccountAdd.win = new Ext.Window({
		title:"新增消耗记录",
		width:845,
		height:466,
		layout:"border",
		items:[
			{
				region:"north",
				border: false, baseCls: 'x-plain',
				layout:"fit",
				height:75,
				items: MatTrainExpendAccountAdd.form
			},
			{
				region:"center",
				layout:"fit",
				items: MatTrainExpendAccountDetail.grid
			}
		],
		plain: true,
		closeAction: 'hide',
		buttonAlign: 'center',
		buttons: [{
			text: '暂存', handler: function() {
				MatTrainExpendAccountAdd.saveTemporaryFn(true);
			}
		}, {
			text: '登账', handler: function() {
				Ext.Msg.confirm("提示  ", "是否确认登账？  ", function(btn){
			        if(btn == 'yes') {
						MatTrainExpendAccountAdd.saveTemporaryFn(false);
			        }
		        });
			}
		}, {
			text: '关闭', handler: function() {
				MatTrainExpendAccountAdd.win.hide();
			}
		}]
	});
	/** ************** 定义修改窗口结束 ************** */
	
});