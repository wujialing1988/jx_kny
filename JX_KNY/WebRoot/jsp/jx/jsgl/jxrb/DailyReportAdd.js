/**
 * 生产调度—机车检修日报 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('DailyReportAdd');
	
	/** **************** 定义全局变量开始 **************** */
	DailyReportAdd.labelWidth = 100;
	DailyReportAdd.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	DailyReportAdd.saveForm = new Ext.form.FormPanel({
		padding: 10, layout: 'column', frame: true,
		labelWidth: DailyReportAdd.labelWidth,
		defaults: {columnWidth: .5, layout: 'form', defaults: {
			width: DailyReportAdd.fieldWidth,
			xtype: 'my97date', format: 'Y-m-d', initNow: false
		}},
		items: [{
			items: [{
					id:"id_cxbm",	
					xtype: "Base_combo",
					fieldLabel: "车型",
					hiddenName: "cxbm", 
					returnField: [{widgetId:"id_cxjc",propertyName:"shortName"}],
					business: 'trainType',
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 200,
					editable:true,
					allowBlank: false,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();
			                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
			                rc_comb.store.proxy = new Ext.data.HttpProxy( {   
					            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(rc_comb.queryParams)+'&manager='+rc_comb.business 
					        });    
					        
					        //修程下拉选择框默认选中第一条记录
							rc_comb.store.on("load",function(store, records){ 
								if(records.length > 0){
							    	rc_comb.setDisplayValue(records[0].get('xcID'), records[0].get('xcName'));
							    	Ext.getCmp('id_repair_class_name').setValue(records[0].get('xcName'));
							    	Ext.getCmp("rt_comb").setDisplayValue(records[0].get('xcName'), records[0].get('xcName'));
							    	//重新加载修次数据
		    	                	var rt_comb = Ext.getCmp("rt_comb");
		    	                	rt_comb.clearValue();
		    	                 	rt_comb.reset();
		    	                 	rt_comb.getStore().removeAll();
		    	                    rt_comb.queryParams = {"rcIDX":rc_comb.getValue()};
		    	                    rt_comb.store.proxy = new Ext.data.HttpProxy( {   
		    				            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(rt_comb.queryParams)+'&manager='+rt_comb.business 
		    				        });
		    						 //修次下拉选择框默认选中第一条记录
		    						rt_comb.getStore().on("load",function(store, records){ 
		    							if(records.length > 0){
		    						    	rt_comb.setDisplayValue(records[0].get('repairtimeName'),records[0].get('repairtimeName'));
		    							}
		    						});
		    						rt_comb.getStore().load();
								}
							});
							rc_comb.store.load();
			        	}   
			    	}
			}, {
				fieldLabel: '车型简称', name: 'cxjc', xtype: 'hidden', maxLength: 10, id: 'id_cxjc'
			}, {
				fieldLabel: '修程名称', name: 'repairClassName', xtype: 'hidden', maxLength: 10, id: 'id_repair_class_name'
			}, {
				id:"rc_comb",
				xtype: "Base_combo",
				fieldLabel: "修程",
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName", valueField: "xcID",
				returnField: [{widgetId:"id_repair_class_name",propertyName:"xcName"}],
				pageSize: 0, minListWidth: 200,
				allowBlank: false,
				listeners : {   
	            	"select" : function() {   
	            		//重新加载修次数据
	                	var rt_comb = Ext.getCmp("rt_comb");
	                	rt_comb.clearValue();
	                 	rt_comb.reset();
	                    rt_comb.queryParams = {"rcIDX":this.getValue()};
	                    rt_comb.cascadeStore();
	            	},
	            	"beforequery" : function(){
	            		var trainTypeIdx =  Ext.getCmp("id_cxbm").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
	            	}
	            }
			}, {
				fieldLabel: '委修单位', name: 'wxdmc', xtype: 'hidden', maxLength: 10, id: 'id_wxdmc'
			}, {
				id: "id_wxdbm", xtype: "DeportSelect2_comboTree",
				hiddenName: "wxdbm", fieldLabel: "委修单位名称", allowBlank: false,
				returnField: [{widgetId:"id_wxdmc",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
			}, {
				fieldLabel: '走行公里', name: 'zxgl', xtype: 'numberfield', vtype: 'positiveInt', maxLength: 18
			}, {
				fieldLabel: '离段日期', name: 'ldrq'
			}, {
				fieldLabel: '开工日期', name: 'kgrq'
			}, {
				fieldLabel: '离厂日期', name: 'lcrq'
			}]
		}, {
			items: [{
				id:"trainNo_comb",	
				fieldLabel: "车号",
				hiddenName: "ch", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				minLength : 4, 
				maxLength : 5,
				forceSelection:false,    				
				xtype: "Base_combo",
				business: 'trainNoSelect',				
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
						{name:"leaveDate", type:"date", dateFormat: 'time'},
						"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse",
						"trainUseName","bId","dId","bName","dName","bShortName","dShortName"],	
				queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				returnField: [
		              {widgetId:"id_wxdmc", propertyName:"dName"},
		              {widgetId:"id_cxdmc", propertyName:"bName"}
				],
				editable:true,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeIdx =  Ext.getCmp("id_cxbm").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					},
					"select" : function(me, record){
						//带出配属段的Id及简称回显
						var data = record.data;
						Ext.getCmp("id_cxdbm").setDisplayValue(data.bId, data.bName);
						Ext.getCmp("id_wxdbm").setDisplayValue(data.dId, data.dName);
					}
				}
			
			}, {
				id:"rt_comb",
				hiddenName: "repairTimeName",
				xtype: "Base_combo",
				fieldLabel: "修次",
				displayField: "repairtimeName", valueField: "repairtimeName",
				fields: ["repairtimeIDX", "repairtimeName"],
				business: 'rcRt',
				pageSize: 0,
				minListWidth: 200,
				listeners : {
					"beforequery" : function(){
						//选择修次前先选修程
	            		var rcIdx =  Ext.getCmp("rc_comb").getValue();
						if(rcIdx == "" || rcIdx == null){
							MyExt.Msg.alert("请先选择修程！");
							return false;
						}
	            	}
				}
			
			}, {
				fieldLabel: '承修段', name: 'cxdmc', xtype: 'hidden', maxLength: 10, id: 'id_cxdmc'
			}, {
				id: "id_cxdbm", xtype: "DeportSelect2_comboTree",
				hiddenName: "cxdbm", fieldLabel: "承修段名称", allowBlank: false,
				returnField: [{widgetId:"id_cxdmc", propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
			}, {
				fieldLabel: '检修状态', name: 'jxzt', xtype: 'combo', maxLength: 10,
				typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			        fields: [ 'k', 'v' ],
			        data: [[1, '待修'], [2, '检修中'], [3, '修竣']]
			    }),
			    value: '检修中',
			    valueField: 'v', displayField: 'v'
			}, {
				fieldLabel: '到厂日期', name: 'dcrq'
			}, {
				fieldLabel: '竣工日期', name: 'jgrq'
			}, {
				fieldLabel: '回段日期', name: 'hdrq'
			}]
		}, {
			columnWidth: 1,
			items: [{
				fieldLabel: '备注', xtype: 'textarea', name: 'bz', width: 417, height: 70, maxLength: 200
			}, {
				fieldLabel: 'idx主键', xtype: 'hidden', name: 'idx'
			}, {
				fieldLabel: '检修作业计划ID', xtype: 'hidden', name: 'workPlanID', value: 'null'
			}]
		}]
	});
	
	DailyReportAdd.win = new Ext.Window({
 		title: '机车检修日报新增',
 		modal: true,
 		closeAction: 'hide',
 		width:600, height: 380,
 		layout: 'fit',
 		items: DailyReportAdd.saveForm,
 		buttonAlign: 'center',
 		buttons: [{
 			text: '保存', iconCls: 'saveIcon', handler: function() {
 				var form = DailyReportAdd.saveForm.getForm();
 				var values = form.getValues();
 				// 验证数据有效性
 				if (!form.isValid() || !DailyReport.grid.beforeSaveFn(values)) {
 					return;
 				}
 				delete values.rc_comb;
 				if (self.loadMask) self.loadMask.show();
 				Ext.Ajax.request({
					url: ctx + '/dailyReport!saveOrUpdate.action',
					jsonData: values,
					scope: DailyReportAdd.saveForm,
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            this.find('name', 'idx')[0].setValue(result.entity.idx);
				            DailyReport.grid.store.reload();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
 				});
 			}
 		}, {
 			text: '关闭', iconCls: 'closeIcon', handler: function() {
 				this.findParentByType('window').hide();
 			}
 		}],
 		listeners: {
 			show: function() {
 				// 重置表单
 				DailyReportAdd.saveForm.getForm().reset();
 				Ext.getCmp('id_cxbm').clearValue();
 				Ext.getCmp('trainNo_comb').clearValue();
 				Ext.getCmp('rc_comb').clearValue();
 				Ext.getCmp('rt_comb').clearValue();
 				Ext.getCmp('id_wxdbm').clearValue();
 				Ext.getCmp('id_cxdbm').clearValue();
 			}
 		}
	});
	
});