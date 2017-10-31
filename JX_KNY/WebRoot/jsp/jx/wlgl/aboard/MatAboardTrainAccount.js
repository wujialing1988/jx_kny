/**
 * 
 * 大型消耗配件上车js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatAboardTrainAccount');
	
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
	MatAboardTrainAccount.searchParams = {};
	MatAboardTrainAccount.labelWidth = 100;
	MatAboardTrainAccount.fieldWidth = 140;
	MatAboardTrainAccount.isAddAndNew = false;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatAboardTrainAccount.searchForm = new Ext.form.FormPanel({
		labelWidth: MatAboardTrainAccount.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第2行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID", width:MatAboardTrainAccount.fieldWidth,
					pageSize: 20,
					editable:true,
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
			        	}   
			    	}
				}]
			}, {													// 第2行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
					hiddenName: "trainNo",width:MatAboardTrainAccount.fieldWidth,
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200, 
					editable:true,
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
				}]
			}, {													// 第2行第3列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"rc_comb",
        			xtype: "Base_combo",
        			business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
        			fieldLabel: "修程",
        			hiddenName: "xcId", 
        			displayField: "xcName",
        			valueField: "xcID",
        			pageSize: 20, minListWidth: 200,
        			queryHql: 'from UndertakeRc',
        			width: MatAboardTrainAccount.fieldWidth,
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
        		}]
			}, {													// 第2行第4列
				columnWidth: .25,
				layout: 'form',
				items: [{
	    			id:"rt_comb",
	    			xtype: "Base_combo",
	    			fieldLabel: "修次",
	    			hiddenName: "rtId", 
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
	    			width: MatAboardTrainAccount.fieldWidth
	    		}]
			}]
		}, {
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					xtype: 'EosDictEntry_combo',
					fieldLabel: '物料类型',
					id: 'matClass_comb',
					name:'matClass', editable: false,
					hiddenName: 'matClass',
					dicttypeid:'WLGL_MAT_CLASS',
					displayField:'dictname',valueField:'dictname',
					width: MatAboardTrainAccount.fieldWidth
				}]
			}, {													// 第1行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					fieldLabel: '领用人',
					name: 'getEmp',
					width: MatAboardTrainAccount.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '上车日期', combineErrors: false,
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
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatAboardTrainAccount.searchForm.getForm();
						if (form.isValid()) {
							MatAboardTrainAccount.grid.store.load();
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatAboardTrainAccount.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('trainType_comb').clearValue();
						Ext.getCmp('trainNo_comb').clearValue();
						Ext.getCmp('rc_comb').clearValue();
						Ext.getCmp('rt_comb').clearValue();
						Ext.getCmp('matClass_comb').clearValue();
						// 重新加载表格
						MatAboardTrainAccount.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatAboardTrainAccount.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matAboardTrainAccount!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matAboardTrainAccount!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matAboardTrainAccount!logicDelete.action',            //删除数据的请求URL
	    tbar: ['add', 'delete', 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'配件种类', dataIndex:'matClass'
		},{
			header:'编号', dataIndex:'matNo'
		},{
			header:'上车日期', dataIndex:'aboardDate', xtype: 'datecolumn', format: 'Y-m-d'
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName'
		},{
			header:'车号', dataIndex:'trainNo'
		},{
			header:'修程编码', dataIndex:'xcId', hidden:true
		},{
			header:'修程', dataIndex:'xcName'
		},{
			header:'修次编码', dataIndex:'rtId', hidden:true
		},{
			header:'修次', dataIndex:'rtName'
		},{
			header:'位置', dataIndex:'position'
		},{
			header:'领用人主键', dataIndex:'getEmpId', hidden:true
		},{
			header:'领用人', dataIndex:'getEmp'
		},{
			header:'备注', dataIndex:'remarks'
		},{
			header:'维护人', dataIndex:'registEmp'
		},{
			header:'维护日期', dataIndex:'registDate', xtype: 'datecolumn', format: 'Y-m-d'
		}],
		saveWinWidth: 1000,
		saveWinHeight: 245,    
		saveForm: MatAboardTrainAccountSave.form,
		afterShowSaveWin: function(){
			// 设置默认【领用人】信息
			Ext.getCmp("getEmpId_k").setDisplayValue(empId, empName);
		},
		beforeGetFormData: function(){
			Ext.getCmp('registEmp_k').enable();
			Ext.getCmp('registDate_k').enable();
		},
		afterGetFormData: function(){
			Ext.getCmp('registEmp_k').disable();
			Ext.getCmp('registDate_k').disable();
		},
		afterShowEditWin: function(record, rowIndex){
			// 回显”车型“、”车号“、”修程“、”修次“
			Ext.getCmp('trainTypeIDX_k').setDisplayValue(record.get('trainTypeIDX'), record.get('trainTypeShortName'));
			Ext.getCmp('trainNo_k').setDisplayValue(record.get('trainNo'), record.get('trainNo'));
			Ext.getCmp('xcId_k').setDisplayValue(record.get('xcId'), record.get('xcName'));
			Ext.getCmp('rtId_k').setDisplayValue(record.get('rtId'), record.get('rtName'));
			// 回显”领用人“
			Ext.getCmp('getEmpId_k').setDisplayValue(record.get('getEmpId'), record.get('getEmp'));
		},
		createSaveWin: function(){
	        this.saveWin = new Ext.Window({
	            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: function() {
	                	MatAboardTrainAccount.isAddAndNew = false;
	                	this.saveFn();
	                }
	            }, {
	                text: "保存并新增", iconCls: "addIcon", scope: this, handler: function() {
	                	MatAboardTrainAccount.isAddAndNew = true;
	                	this.saveFn();
	                }
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	    },
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        if (!MatAboardTrainAccount.isAddAndNew) {
	        	this.saveWin.hide();
	        	return;
	        }
	        this.saveForm.getForm().reset();
	        Ext.getCmp('trainTypeIDX_k').clearValue();
			Ext.getCmp('trainNo_k').clearValue();
			Ext.getCmp('xcId_k').clearValue();
			Ext.getCmp('rtId_k').clearValue();
			// 设置默认【领用人】信息
			Ext.getCmp("getEmpId_k").setDisplayValue(empId, empName);
	    }
	});
	
	MatAboardTrainAccount.grid.store.on('beforeload', function() {
		MatAboardTrainAccount.searchParams = MatAboardTrainAccount.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatAboardTrainAccount.searchParams);
		MatAboardTrainAccount.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatAboardTrainAccount.viewport = new Ext.Viewport({
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
			items: [MatAboardTrainAccount.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatAboardTrainAccount.grid]
		}]
	});
});