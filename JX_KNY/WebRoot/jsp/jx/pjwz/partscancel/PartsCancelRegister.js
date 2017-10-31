/**
 * 配件销账单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsCancelRegister');  //定义命名空间
	PartsCancelRegister.fieldWidth = 200;
	PartsCancelRegister.labelWidth = 100;
	//页面加载之后，显示报废人为当前登录用户
  	PartsCancelRegister.init=function(){
       Ext.getCmp("canceledEmp_select").setDisplayValue(empId,empName);
	   Ext.getCmp("canceledEmp").setValue(empName);
    }
	PartsCancelRegister.form = new Ext.FormPanel({
	      layout:'border',frame:true,
	      items:[{
			        region:'north', 
			        height:140,
			        layout:'border',
			        xtype:'fieldset',
			        align:'center',
			        frame:false,
			        margins:'10 5 20 5',
			        title:'配件销账单',
					defaults:{
                        region:'north', 
						layout:'column',
			        	baseCls:'x-plain',
			        	height:30,
						defaults:{
						  columnWidth:.33,
			        	  layout:'form',
			        	  baseCls:'x-plain',
			        	  labelWidth:62,
			        	  defaults:{anchor:'50%'}
						}
					},
			        items:[{
			        	items:[{
			        	    items:[{ 
			        	   		 xtype: 'EosDictEntry_combo', hiddenName: 'canceledType',allowBlank:false, fieldLabel: '销账方式',
					  			displayField:'dictname',valueField:'dictname', dicttypeid: 'PJWZ_PARTS_CANCELED'
					       	}]
			        	},{
			        		items:[{
		        		     		xtype:'textfield',id:'canceledEmp',fieldLabel:'销账人',hidden:true
		        		     	},{
	          		 				id:"canceledEmp_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,
	        	    			 	fieldLabel : "销账人",hiddenName:'canceledEmpId',
	        	     				returnField:[{widgetId:"canceledEmp",propertyName:"empname"}]
			        		    }]
			        	},{
			        	    items:[{
			        	    	xtype:'my97date',format:'Y-m-d',id:'canceledDate',fieldLabel:'销账日期',allowBlank:false
			        	    }]
			        	}]
			        },{
			            region:'center',
			            baseCls:'x-plain',
			            labelWidth:62,
			            layout:'form',
			            items:[{
			                xtype:'textarea',id:'canceledReason',name:'canceledReason',fieldLabel:'销账理由',maxLength:500,anchor:'80%'
			              }]
			        }]
	      	 },{
		        region:'center',
		        xtype:'fieldset',
		        margins:'5 5 20 5',
		        layout:'fit',
		        title:'配件销账明细',
		        items:[PartsCancelRegisterDetail.grid]
	      	},{
	          	region:'south',
	            buttonAlign:'center',
	            buttons:[{
	                text:'登帐并新增',handler:function(){
	             		PartsCancelRegisterDetail.savePartsCancel();
	             	}
	             }]
	        }]
	});

	//页面自适应布局
  var viewport = new Ext.Viewport({ layout:'fit', items:PartsCancelRegister.form });
  PartsCancelRegister.init();
});