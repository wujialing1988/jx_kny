 Ext.onReady(function(){
    Ext.namespace('WellPartsWhBack'); 
    WellPartsWhBack.fieldWidth = 160;
	WellPartsWhBack.labelWidth = 80;
	WellPartsWhBack.form = new Ext.form.FormPanel({
		    baseCls: "x-plain",
		    align: "center",
		    defaultType: "textfield",
		    defaults: { anchor: "98%" },
		    layout: "form",	
		    border: false,
		    style: "padding:10px",
		    defaults:{
		         xtype: "panel",
		         border: false,
		         baseCls: "x-plain",
		         layout: "column",
		         align: "center", 
		         defaults:{
		         	 defaults:{anchor:'50%'},
		             baseCls:"x-plain",
		             align:"center", 
		             layout:"form",
		             defaultType:"textfield",
		             columnWidth: 0.33 
		         }
		    },
		    items: [{
		        items: [{
		            items: [{
				        	id:"whName_comb", fieldLabel:"库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"wellPartsWH_whIdx",propertyName:"idx"}]        
				      	 },{
				        	id:"wellPartsWH_whIdx",xtype:"hidden", name:"warehouseIdx"
				     }]
		        },{
		            items: [{ 
		            		id:"exWH_select",xtype:'OmEmployee_SelectWin',editable:false,
		            		allowBlank: false,fieldLabel : "库管员",hiddenName:'warehouseEmp',
							returnField:[{widgetId:"wellPartsWH_exWhEmp",propertyName:"empname"},
							{widgetId:"wellPartsWH_exWhEmpId",propertyName:"empid"}],
							displayField:'empname',valueField:'empname', editable:false
						},{
							id:"wellPartsWH_exWhEmpId",xtype:"hidden", name:"warehouseEmpId"
					}]
		      	},{
		            items: [{ 
		            		id:"wellPartsWH_backTime",name: "backTime", fieldLabel: "退库日期",
		            		allowBlank: false, xtype: "my97date",format: "Y-m-d",initNow: true
					}]
		       	},{
		            items: [{ 
		            		id:"handOver_select",xtype:'OmEmployee_SelectWin',
		            		editable:false,allowBlank: false,
		            		fieldLabel : "交件人",hiddenName:'handoverEmp',
							returnField:[{widgetId:"wellPartsWH_gethandOverEmpId",propertyName:"empid"},
									 {widgetId:"wellPartsWH_handoverOrgID",propertyName:"orgid"},
									 {widgetId:"wellPartsWH_handoverOrg",propertyName:"orgname"}],
							displayField:'empname',valueField:'empname', editable:false,
							listeners : {   
								"select" : function(record) {  
								Ext.getCmp("handOverOrg_select").setDisplayValue(record.get("orgid"),record.get("orgname"));//领件部门
								Ext.getCmp("wellPartsWH_handoverOrg").setValue(record.get("orgname"));
								Ext.getCmp("wellPartsWH_gethandOverEmpId").setValue(record.get("empid"));
							}}
						},{
							id:"wellPartsWH_gethandOverEmpId",xtype:"hidden", name:"handoverEmpId"
					}]
		        },{
		            items: [{ 
		            		id:"handOverOrg_select",xtype:'OmOrganizationCustom_comboTree',
		            		editable:false,allowBlank: false,fieldLabel : "交接部门",hiddenName:'handoverOrgID',
							returnField:[{widgetId:"wellPartsWH_handoverOrg",propertyName:"orgname"}],
							editable:false
					    },{
					    	id:"wellPartsWH_handoverOrg",xtype:"hidden", name:"handoverOrg"
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
			height:85,
			frame: true, 
			items:[WellPartsWhBack.form]
		},{
			layout: "fit",
			region : 'center',
	        bodyBorder: false,
	        autoScroll : true,
	        id: "nGridId",
	        items : [ WellPartsWhBackDetail.grid ],
	        buttonAlign:"center",
	        buttons:[{
	        	text: "登帐并新增",  
	        	handler: function(){
	        		WellPartsWhBackDetail.saveFun()
	           }
	    	}]
		}]
	});
	
	
	WellPartsWhBack.init = function(){
	var whName_Base_combo =  Ext.getCmp("whName_comb");
  	Ext.getCmp("exWH_select").setDisplayValue( empName ,empName);
  	Ext.getCmp("wellPartsWH_exWhEmpId").setValue(empId);
	whName_Base_combo.getStore().on("load",function(store, records){ 
		if(records.length > 0){
	    	whName_Base_combo.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
	    	Ext.getCmp("wellPartsWH_whIdx").setValue(records[0].get('idx'));
			}
		});
	};
	WellPartsWhBack.init();
	
 });