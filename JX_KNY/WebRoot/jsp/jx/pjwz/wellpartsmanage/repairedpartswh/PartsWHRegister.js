/**
 * 修竣配件入库 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsWHRegister');                       //定义命名空间
	PartsWHRegister.fieldWidth = 150;
	PartsWHRegister.labelWidth = 70;
	PartsWHRegister.form = new Ext.form.FormPanel({
		    baseCls: "x-plain",
		    align: "center",
		    defaultType: "textfield",
		    defaults: { anchor: "98%" },
		    layout: "form",
		    border: false,
		    style: "padding:10px",
		    labelWidth: PartsWHRegister.labelWidth,
		    defaults:{
		    	xtype: "panel",
		    	border: false,
		    	baseCls: "x-plain",
		    	layout: "column",
		    	align: "center", 
		    	defaults:{
		    		 baseCls:"x-plain",
		    		 align:"center", 
		    		 layout:"form",
		    		 defaultType:"textfield",
    				 columnWidth: 0.25
		    	}
		    },
		    items: [{
		        items: [{
		            items: [{
				        	id:"whName_comb", fieldLabel:"接收库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"PartsWHRegister_whIdx",propertyName:"idx"}]        
				        },{
				        	id:"PartsWHRegister_whIdx",xtype:"hidden", name:"whIdx"
			        	}]
		        },{
		            items: [{ 
		            		id:"takeOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "接收人",hiddenName:'takeOverEmpId',
							returnField:[{widgetId:"PartsWHRegister_takeOverEmp",propertyName:"empname"}]
						},{
							id:"PartsWHRegister_takeOverEmp",xtype:"hidden", name:"takeOverEmp"
						}]
		        },{
		            items: [{ 
		            	id:"handOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "交件人",hiddenName:'handOverEmpId',
						returnField:[{widgetId:"PartsWHRegister_handOverEmp",propertyName:"empname"},
									 {widgetId:"PartsWHRegister_handOverOrgId",propertyName:"orgid"},
									 {widgetId:"PartsWHRegister_handOverOrg",propertyName:"orgname"}]},
						{id:"PartsWHRegister_handOverEmp",xtype:"hidden", name:"handOverEmp"},
						{id:"PartsWHRegister_handOverOrgId",xtype:"hidden", name:"handOverOrgId"},
						{id:"PartsWHRegister_handOverOrg",xtype:"hidden", name:"handOverOrg"}]
		        },{
		            items: [{ 
		            	id:"whTime",name: "whTime", fieldLabel: "交件日期",allowBlank: false, xtype: "my97date",format: "Y-m-d"
					}]
		        }]
		    }]
		});
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		 layout: "border",
		 items:[{
						region:"north",
						layout:"fit",
						height:70,
						frame: true,bodyBorder: false, 
						items:[PartsWHRegister.form]
					},{
						layout: "fit",
						region : 'center',
				        bodyBorder: false,
				        autoScroll : true,
				        id: "nGridId",
				        items : [ PartsWHRegisterDetail.grid ]
					},{
						region : 'south', layout:"fit",
						height:40, bodyBorder: false, buttonAlign:"center",frame: true,
						buttons:[{text: "登帐并新增",  handler: function(){PartsWHRegisterDetail.saveFun()}}]
					}
		]
	});
	PartsWHRegister.init = function(){
		Ext.getCmp("takeOver_select").setDisplayValue(empId,empName);
		Ext.getCmp("PartsWHRegister_takeOverEmp").setValue(empName);
		var whName_comb =  Ext.getCmp("whName_comb");
		whName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("PartsWHRegister_whIdx").setValue(records[0].get('idx'));
			}
		});
	};
	PartsWHRegister.init();
});