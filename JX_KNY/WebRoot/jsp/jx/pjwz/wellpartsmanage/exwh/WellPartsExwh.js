/**
 * 配件上车领用 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WellPartsExwh');                       //定义命名空间
	WellPartsExwh.fieldWidth = 160;
	WellPartsExwh.labelWidth = 70;
	WellPartsExwh.form = new Ext.form.FormPanel({
		    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
		    layout: "form",		border: false,		style: "padding:10px",		labelWidth: WellPartsExwh.labelWidth,
		    items: [{
		        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
		        items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:WellPartsExwh.labelWidth,
		            columnWidth: 0.25, 
		            items: [
						{
				        	id:"whName_comb", fieldLabel:"出库库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,width:WellPartsExwh.fieldWidth,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"WellPartsExwh_whIdx",propertyName:"idx"}]        
				        },
	                    {id:"WellPartsExwh_whIdx",xtype:"hidden", name:"whIdx"}
						
		            ]
		        },{
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:WellPartsExwh.labelWidth,
		            columnWidth: 0.25, 
		            items: [
		            	{ id:"exWH_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "出库人",hiddenName:'handOverEmpId',
						returnField:[{widgetId:"WellPartsExwh_handOverEmp",propertyName:"empname"}],width:WellPartsExwh.fieldWidth},
						{id:"WellPartsExwh_handOverEmp",xtype:"hidden", name:"handOverEmp"}
		            ]
		        },{
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:WellPartsExwh.labelWidth,
		            columnWidth: 0.25, 
		            items: [
		            	{ id:"handOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "领件人",hiddenName:'acceptEmpId',
						returnField:[{widgetId:"WellPartsExwh_acceptEmp",propertyName:"empname"},
									 {widgetId:"WellPartsExwh_acceptOrgID",propertyName:"orgid"},
									 {widgetId:"WellPartsExwh_acceptOrg",propertyName:"orgname"}],width:WellPartsExwh.fieldWidth},
						{id:"WellPartsExwh_acceptEmp",xtype:"hidden", name:"acceptEmp"},
						{id:"WellPartsExwh_acceptOrgID",xtype:"hidden", name:"acceptOrgID"},
						{id:"WellPartsExwh_acceptOrg",xtype:"hidden", name:"acceptOrg"}
		            ]
		        },{
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:WellPartsExwh.labelWidth,
		            columnWidth: 0.25, 
		            items: [
		            	{ id:"whTime",name: "whTime", fieldLabel: "领件日期",allowBlank: false, xtype: "my97date",format: "Y-m-d",  
						width: WellPartsExwh.fieldWidth}
		            ]
		        }
		        ]
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
			items:[WellPartsExwh.form]
		},{
			layout: "fit",
			region : 'center',
	        bodyBorder: false,
	        autoScroll : true,
	        id: "nGridId",
	        items : [ WellPartsExwhDetail.grid ],
	        buttonAlign:"center",
	        buttons:[{
	        	text: "登帐并新增",  handler: function(){WellPartsExwhDetail.saveFun()}
	    	}]
		}]
	});
	WellPartsExwh.init = function(){
		Ext.getCmp("exWH_select").setDisplayValue(empId,empName);
		Ext.getCmp("WellPartsExwh_handOverEmp").setValue(empName);
		var whName_comb =  Ext.getCmp("whName_comb");
		whName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("WellPartsExwh_whIdx").setValue(records[0].get('idx'));
			}
		});
	}();
});