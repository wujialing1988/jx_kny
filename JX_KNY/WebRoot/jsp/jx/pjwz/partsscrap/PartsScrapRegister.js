Ext.onReady(function(){
    Ext.namespace("PartsScrapRegister");
	PartsScrapRegister.form = new Ext.FormPanel({
    layout:'border',frame:true,
  	items:[{
	        region:'north', 
	        height:140,
	        layout:'border',
	        xtype:'fieldset',
	        align:'center',
	        frame:false,
	        margins:'10 5 20 5',
	        title:'配件报废单',
	        items:[{
	        	region:'north',
	        	height:30,
	        	layout:'column',
	        	baseCls:'x-plain',
	        	defaults:{
	        	    columnWidth:.33,
	        	    layout:'form',
	        	    baseCls:'x-plain',
	        	    labelWidth:62,
	        	    defaults:{anchor:'50%'}
	        	},
	        	items:[{
	        	    items:[{
	        	       		xtype:'textfield',name:'scrapEmp',id:'scrapEmpId',hidden:true,fieldLabel:'报废人'
	        	       },{
	        	        	id:"scrapEmp_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,
	        	         	fieldLabel : "报废人",hiddenName:'scrapEmpId',
	        	         	returnField:[{widgetId:"scrapEmpId",propertyName:"empname"}] 
	        	      }]
	        	},{
	        		items:[{
	        		xtype:'my97date',name:'scrapDate',format:'Y-m-d',id:'scrapDate',fieldLabel:'报废日期',allowBlank:false
	        		}]
	        	}]
	        },{
	            region:'center',
	            baseCls:'x-plain',
	            labelWidth:62,
	            layout:'form',
	            items:[{
	                  xtype:'textarea',id:'scrapReason',name:'scrapReason',anchor:'49.5%',fieldLabel:'报废理由',height:70,maxLength:500
	            }]
	        }]
 		 },{
		        region:'center',
		        xtype:'fieldset',
		        margins:'5 5 20 5',
		        layout:'fit',
		        title:'配件报废明细',
		        items:[PartsScrapRegisterDetail.grid]
	   			},{
	           	region:'south',
          	 	buttonAlign:'center',
	           	buttons:[{  
	                text:'登帐并新增',handler:function(){
	             	PartsScrapRegisterDetail.savePartsScrap();
	             	}
	             }]
			}]
	 });
	
	var viewport=new Ext.Viewport({layout:'fit',items:[PartsScrapRegister.form]});
	
	//填充默认报废人
	Ext.getCmp("scrapEmp_select").setDisplayValue(empId, empName);
	Ext.getCmp("scrapEmpId").setValue(empName);
});