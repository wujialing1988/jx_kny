/**
 * 
 * 大型消耗配件上车保存表单js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatAboardTrainAccountSave');
	
	/** ************ 定义全局变量开始 ************ */
	MatAboardTrainAccountSave.fieldWidth = 120;
	MatAboardTrainAccountSave.labelWidth = 70;
	/** ************ 定义全局变量结束 ************ */
	
	MatAboardTrainAccountSave.form = new Ext.form.FormPanel({
		labelWidth:MatAboardTrainAccountSave.labelWidth,
		layout: 'column',
		padding: '20px',
		baseCls: 'x-plain', border: false,
		items: [{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				xtype: 'EosDictEntry_combo',
				fieldLabel: '物料类型',
				name:'matClass', editable: false, allowBlank: false,
				hiddenName: 'matClass',
				dicttypeid:'WLGL_MAT_CLASS',
				displayField:'dictname',valueField:'dictname',
				width: MatAboardTrainAccountSave.fieldWidth
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth:0.75,
			items:[{
				xtype: 'textfield',
				fieldLabel: '编号',
				allowBlank: false,
				name:'matNo',
				width: MatAboardTrainAccountSave.fieldWidth
			}]
		},
		{
			columnWidth:.25,
			layout:"form", baseCls: 'x-plain', border: false,
			items:[{
				id:"trainTypeIDX_k",xtype: "TrainType_combo",	fieldLabel: "车型",
				hiddenName: "trainTypeIDX",
				returnField: [{widgetId:"PartsAboardExWh_aboardTrainType",propertyName:"shortName"}],
				displayField: "shortName", valueField: "typeID",width: MatAboardTrainAccountSave.fieldWidth,
				pageSize: 20, //minListWidth: 200,   //isCx:'no',
				editable:true  ,allowBlank: false,
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_k = Ext.getCmp("trainNo_k");   
		                trainNo_k.reset();  
		                trainNo_k.clearValue(); 
		                trainNo_k.queryParams = {"trainTypeIDX":this.getValue()};
		                trainNo_k.cascadeStore();
		                //重新加载修程下拉数据
		                var xcId_k = Ext.getCmp("xcId_k");
		                xcId_k.reset();
		                xcId_k.clearValue();
		                xcId_k.getStore().removeAll();
		                xcId_k.queryParams = {"TrainTypeIdx":this.getValue()};
		                xcId_k.cascadeStore();
						//重新加载修次数据
	                	var rtId_k = Ext.getCmp("rtId_k");
	                	rtId_k.clearValue();
	                 	rtId_k.reset();
	                 	rtId_k.getStore().removeAll();
	                 	rtId_k.cascadeStore();
	                 	//重置“修次”名称字段
	                 	Ext.getCmp('rtName_k').reset();
		        	}   
		    	}
			}, {
            	id:"PartsAboardExWh_aboardTrainType",xtype:"hidden", name:"trainTypeShortName"
            }]
		},
		{
			columnWidth:.25,
			layout:"form", baseCls: 'x-plain', border: false,
			items:[{
				id:"trainNo_k",xtype: "TrainNo_combo",	fieldLabel: "车号",
				hiddenName: "trainNo",width:MatAboardTrainAccountSave.fieldWidth,
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200, 
				editable:true, allowBlank: false,
				listeners: {
					"beforequery" : function(){
    					//选择修次前先选车型
						var trainTypeId =  Ext.getCmp("trainTypeIDX_k").getValue();
    					if(trainTypeId == "" || trainTypeId == null){
    						MyExt.Msg.alert("请先选择上车车型！");
    						return false;
    					}
                	}
    			}
			}]
		},
		{
			columnWidth:.25,
			layout:"form", baseCls: 'x-plain', border: false,
			items:[{
				id:"xcId_k",
    			xtype: "Base_combo",
    			business: 'trainRC',
				entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
				fields:['xcID','xcName'],
    			fieldLabel: "修程",
    			hiddenName: "xcId", 
    			returnField: [{widgetId:"xcName_k",propertyName:"xcName"}],
    			displayField: "xcName",
    			valueField: "xcID",
    			pageSize: 20, minListWidth: 200,
    			queryHql: 'from UndertakeRc',
    			width: MatAboardTrainAccountSave.fieldWidth,
    			allowBlank: false,
    			listeners : {  
    				"beforequery" : function(){
    					//选择修次前先选车型
                		var trainTypeId =  Ext.getCmp("trainTypeIDX_k").getValue();
    					if(trainTypeId == "" || trainTypeId == null){
    						MyExt.Msg.alert("请先选择上车车型！");
    						return false;
    					}
                	},
                	"select" : function() {   
                		//重新加载修次数据
                    	var rtId_k = Ext.getCmp("rtId_k");
                    	rtId_k.clearValue();
                     	rtId_k.reset();
                        rtId_k.queryParams = {"rcIDX":this.getValue()};
                        rtId_k.cascadeStore();  
                	}
                }
    		}, {
    			id:"xcName_k",xtype:"hidden", name:"xcName"
            }]
		},
		{
			columnWidth:.25,
			layout:"form", baseCls: 'x-plain', border: false,
			items:[{
    			id:"rtId_k",
    			xtype: "Base_combo",
    			fieldLabel: "修次",
    			hiddenName: "rtId", 
    			returnField: [{widgetId:"rtName_k",propertyName:"repairtimeName"}],
    			displayField: "repairtimeName",
    			valueField: "repairtimeIDX",
    			pageSize: 0,
    			minListWidth: 200,
    			fields: ["repairtimeIDX","repairtimeName"],
				business: 'rcRt',
    			listeners : {
    				"beforequery" : function(){
    					//选择修次前先选修程
                		var rcIdx =  Ext.getCmp("xcId_k").getValue();
    					if(rcIdx == "" || rcIdx == null){
    						MyExt.Msg.alert("请先选择上车修程！");
    						return false;
    					}
                	}
    			},
    			width: MatAboardTrainAccountSave.fieldWidth
    		}, {
    			id:"rtName_k",xtype:"hidden", name:"rtName"
    		}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				id:"getEmpId_k",
				xtype:'OmEmployee_SelectWin',
				editable:false,
				allowBlank: false,
				fieldLabel : "领用人",
				hiddenName:'getEmpId',
				returnField:[{
					widgetId:"getEmp_k", propertyName:"empname"
				}],
				width: MatAboardTrainAccountSave.fieldWidth
			}, {
				id:"getEmp_k",xtype:"hidden", name:"getEmp",　value: empName
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				xtype: 'textfield',
				fieldLabel: '位置',
				allowBlank: false,
				name:'position',
				width: MatAboardTrainAccountSave.fieldWidth
			
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				xtype: 'my97date', format: 'Y-m-d',
				fieldLabel: '上车日期',
				allowBlank: false,
				name:'aboardDate',
				width: MatAboardTrainAccountSave.fieldWidth
			
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth:1,
			items:[{
				xtype: 'textfield',
				fieldLabel: '备注',
				name:'remarks',
				width: MatAboardTrainAccountSave.fieldWidth + 432
			
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				xtype: 'textfield',
				id: 'registEmp_k',
				fieldLabel: '维护人',
				name:'registEmp', value: empName,
				disabled: true,
				width: MatAboardTrainAccountSave.fieldWidth
			
			}]
		}, 
		{
			layout:"form", baseCls: 'x-plain', border: false,
			columnWidth: .25,
			items:[{
				xtype: 'my97date', format: 'Y-m-d',
				fieldLabel: '维护日期',
				id: 'registDate_k',
				disabled: true,
				name:'registDate',
				width: MatAboardTrainAccountSave.fieldWidth
			}]
		},
		{
			layout:"form", baseCls: 'x-plain', border: false, defaultType: 'textfield',
			columnWidth: .25,
			items:[{
				xtype: 'hidden', name: 'idx'
			}]
		}]
	});
	
});