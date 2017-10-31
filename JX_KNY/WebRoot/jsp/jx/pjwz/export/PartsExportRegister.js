/**
 * 配件调出登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsExportRegister');  //定义命名空间
	PartsExportRegister.form = new Ext.FormPanel({
	      layout:'border',frame:true,
	      items:[{
			        region:'north', 
			        height:140,
			        layout:'border',
			        xtype:'fieldset',
			        align:'center',
			        frame:false,
			        margins:'10 5 20 5',
			        title:'配件调出单',
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
		        	        	xtype:'textfield',name:'acceptDep',id:'acceptDep',hidden:true,fieldLabel:'接收单位ID'
			        	      },{
		        	       		  xtype: 'Base_combo',	fieldLabel: '接收单位',
								  hiddenName: 'acceptDepCode', returnField: [{widgetId:"acceptDep",propertyName:"dName"}],
								  displayField: 'dName', valueField: 'dId',
								  entity: 'com.yunda.jcgy.entity.JgyjcDeport', 
								  fields: ["dId","dName"],
								  pageSize: 20, minListWidth: 200, editable: false
			        	      }]
			        	},{
			        		items:[{
		        		     	xtype:'my97date',name:'exportDate',format:'Y-m-d',id:'exportDate',fieldLabel:'调出日期',allowBlank:false
			        		  }]
			        	}]
			        },{
			            region:'center',
			            baseCls:'x-plain',
			            labelWidth:62,
			            layout:'form',
			            items:[{
			                xtype:'textarea',id:'exportOrder',name:'exportOrder',anchor:'49.5%',fieldLabel:'调出命令',height:70,maxLength:100
			              }]
			        }]
	      	 },{
		        region:'center',
		        xtype:'fieldset',
		        margins:'5 5 20 5',
		        layout:'fit',
		        title:'配件调出明细',
		        items:[PartsExportRegisterDetail.grid]
	      	},{
	          	region:'south',
	            buttonAlign:'center',
	            buttons:[{
	                text:'登帐并新增',handler:function(){
	             		PartsExportRegisterDetail.savePartsExport();
	             	}
	             }]
	        }]
	  });
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:PartsExportRegister.form });
});