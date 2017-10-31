/**
 * 配件校验登记
 */
Ext.onReady(function(){
	Ext.namespace('PartsCheckRegister');                       //定义命名空间
	PartsCheckRegister.fieldWidth = 150;
	PartsCheckRegister.labelWidth = 70;
	PartsCheckRegister.form = new Ext.form.FormPanel({
		    baseCls: "x-plain",
		    align: "center",
		    defaultType: "textfield",
		    defaults: { anchor: "98%" },
		    layout: "form",
		    border: false,
		    style: "padding:10px",
		    labelWidth: PartsCheckRegister.labelWidth,
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
		            		id:"takeOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "校验人",hiddenName:'checkEmpId',
							returnField:[{widgetId:"PartsCheckRegister_handOverEmp",propertyName:"empname"}]
						},{
							id:"PartsCheckRegister_handOverEmp",xtype:"hidden", name:"handOverEmp"
						}]
		        },{
		            items: [{ 
		            	id:"checkTime",name: "checkTime", fieldLabel: "校验日期",allowBlank: false, xtype: "my97date",format: "Y-m-d"
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
						items:[PartsCheckRegister.form]
					},{
						layout: "fit",
						region : 'center',
				        bodyBorder: false,
				        autoScroll : true,
				        id: "nGridId",
				        items : [ PartsCheckRegisterDetail.grid ]
					},{
						region : 'south', layout:"fit",
						height:40, bodyBorder: false, buttonAlign:"center",frame: true,
						buttons:[{text: "校验",  handler: function(){PartsCheckRegisterDetail.saveFun()}}]
					}
		]
	});
	
	PartsCheckRegister.init = function(){
		Ext.getCmp("takeOver_select").setDisplayValue(empId,empName);
		Ext.getCmp("PartsCheckRegister_handOverEmp").setValue(empName);
	};
	
	PartsCheckRegister.init();
});