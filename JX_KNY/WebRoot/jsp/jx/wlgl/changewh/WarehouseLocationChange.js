/**
 * 
 * 库位调整
 * 
 */

Ext.onReady(function() {
	Ext.namespace('WarehouseLocationChange');
	/** ************ 定义全局变量开始 ************ */
	WarehouseLocationChange.fieldWidth = 70;
	WarehouseLocationChange.matTypeItems = [];
	/** ************ 定义全局变量线束 ************ */

	// 自动生成“物料类型”radiogroup内容
	WarehouseLocationChange.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        WarehouseLocationChange.matTypeItems.push(editor);
		}
	}();
	
	/** ************ 定义全局函数开始 ************ */
	//查询库房信息
	WarehouseLocationChange.getMatInfo = function(matType,whIdx, matCode){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
			var locationComb = Ext.getCmp("locationName_comb");   
    		locationComb.reset();  
       		locationComb.clearValue(); 
			locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
					                "(select locationName from MatStock where whIdx = '"+whIdx
					                +"' and matCode='"+matCode
					                +"' and matType='"+matType
					                +"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
			locationComb.cascadeStore();
			Ext.Ajax.request({
				url: ctx + "/matTypeList!getMatInfo.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("totalQty_k").setValue(result.matStock.qty);										
	    			} else {
	    				 Ext.getCmp("totalQty_k").setValue(0);					    				
	    			}
					if (null != result.whMatQuota) {
						 Ext.getCmp("mixQty_k").setValue(result.whMatQuota.minQty);
	    				 Ext.getCmp("maxQty_k").setValue(result.whMatQuota.maxQty);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	//查询库位信息
	WarehouseLocationChange.getModelStockByLocation = function(whIdx, matType, matCode, locationName){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
    		Ext.Ajax.request({
				url: ctx + "/matStockQuery!getModelStockByLocation.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType, locationName : locationName},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("Qty_k").setValue(result.matStock.qty);
						 Ext.getCmp("changeQty_k").setValue(result.matStock.qty);
					} else {
						 Ext.getCmp("Qty_k").setValue(0);
						  Ext.getCmp("changeQty_k").setValue(0);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
    	}
	}
	
	/** ************ 定义全局函数结束 ************ */
	
	/** ************ 定义表单开始 ************ */
	WarehouseLocationChange.baseForm = new Ext.form.FormPanel({
		layout:"column", padding:"10px",buttonAlign:'center',
		defaults: {
	    	layout:"form",  defaultType:"textfield",labelWidth: 70
	    },
		items:[
			{
				xtype:"panel",
				columnWidth:1,
				items:[{
					id:"matType_k",	xtype:'radiogroup',
					fieldLabel:"领料类型", allowBlank: false, 
					items: WarehouseLocationChange.matTypeItems, anchor:'50%',
					listeners : {   	
			        	"change" : function() {
			        		var whIdx = Ext.getCmp("whIdx_k").getValue();
			        		var matType = Ext.getCmp("matType_k").getValue().inputValue;		        		
    	                	var matCode = Ext.getCmp("matCode_k").getValue();
			        		WarehouseLocationChange.getMatInfo(matType, whIdx, matCode);
			        		var locationName = Ext.getCmp("locationName_comb").getValue();
    	                	if(null != locationName){
    	                		WarehouseLocationChange.getModelStockByLocation(whIdx, matType, matCode, locationName);
    	                	}
			        	}
					}
				}]
			},{
				xtype:"panel",
				columnWidth:0.4,
				items:[{
					id:"whName_comb",
		          	fieldLabel:"库房",hiddenName:"whName", allowBlank: false, 
					xtype:"Base_combo",allowBlank: false, width:WarehouseLocationChange.fieldWidth+100,
					entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName", valueField:"wareHouseName",
					returnField:[{widgetId:"whIdx_k", propertyName:"idx"}],
					listeners : {   
			        	"select" : function() {   
			        		var whIdx = Ext.getCmp("whIdx_k").getValue();
			            	//重新加载库位下拉数据
			                var locationName_comb = Ext.getCmp("locationName_comb");   
			                locationName_comb.reset();  
			                locationName_comb.clearValue();
			                locationName_comb.queryParams = {"wareHouseIDX": whIdx};
			                locationName_comb.cascadeStore();
			            	//重新加载库位下拉数据
			                var locationNameChange_comb = Ext.getCmp("locationNameChange_comb");   
			                locationNameChange_comb.reset();  
			                locationNameChange_comb.clearValue(); 
			                locationNameChange_comb.queryParams = {"wareHouseIDX": whIdx};
			                locationNameChange_comb.cascadeStore();	
			            
			               //查询库存信息、保有量信息
			                if(null == Ext.getCmp("matType_k").getValue()){
			                	MyExt.Msg.alert("请先选择领料类型！");  
			                	return false;
    	                	}
    	                    matType= Ext.getCmp("matType_k").getValue().inputValue;
    	                	var matCode = Ext.getCmp("matCode_k").getValue();
    	                	WarehouseLocationChange.getMatInfo(matType,whIdx, matCode);
    	                	var locationName = Ext.getCmp("locationName_comb").getValue();
    	                	if(null != locationName){
    	                	WarehouseLocationChange.getModelStockByLocation(whIdx, matType, matCode, locationName);
    	                	}
			        	}   
			    	}
		        },{
		        	id:"whIdx_k", xtype:"hidden", name:"whIDX"
		        	}]
			},{
		        xtype:"panel",
				layout:"form",
				columnWidth:0.2,labelWidth: 60,
				items:[{
					id:"totalQty_k",
					xtype:"textfield",  
					fieldLabel:"库存总量",
//					value:0,
					disabled: true,
					width:WarehouseLocationChange.fieldWidth
				}]
			},{
				columnWidth: 0.4,labelWidth: 40,
				items: [{
					xtype: 'compositefield', fieldLabel:"低储", combineErrors: false,
					items:[{ 
						id:"mixQty_k",
						xtype:"textfield", 
						disabled: true,
						width:WarehouseLocationChange.fieldWidth-10
					},{
						xtype: 'label', text: '高储:', style: 'height:23px; line-height:23px;'	
					},{
						id:"maxQty_k",
						xtype:"textfield", 
						disabled: true,
						width:WarehouseLocationChange.fieldWidth-10
					}]
				 }]
            },{
                xtype:"panel",
				columnWidth:0.6,
				items:[{
                	id:"matDesc_k",
                	xtype:"Base_combo", allowBlank: false, anchor:'80%',
					name:"matDesc", 
					fieldLabel:"物料描述", editable: true,typeAhead: false,
					entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
					fields:["matCode","matDesc","unit","price"],
					displayField:"matDesc",valueField:"matDesc",
					returnField:[{widgetId:"matCode_k",propertyName:"matCode"},
					             {widgetId:"unit_k",propertyName:"unit"}],
					listeners : {   
			        	"select" : function() {   
							//查询库存信息、保有量信息
    	                	var matCode = Ext.getCmp("matCode_k").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_k").getValue();
    	                	var matType = Ext.getCmp("matType_k").getValue().inputValue;
    	                	WarehouseLocationChange.getMatInfo(matType,whIdx, matCode);	
    	                	var locationName = Ext.getCmp("locationName_comb").getValue();
    	                	if(null != locationName){
    	                		WarehouseLocationChange.getModelStockByLocation(whIdx, matType, matCode, locationName);
    	                	}
			        	 },
		        		// Modified by hetao on 2016-05-19 修改使用汉字首拼进行数据源过滤
		        		beforequery: function( queryEvent ) {
		        			 var value = this.getEl().dom.value;
		        			 this.queryName = "matDescEn";
		        			 this.cascadeStore();
		        		 }	        		   
		    		  }
				},{
			        id:"matCode_k",xtype:"hidden", name:"matCode"}]
            },{
			        xtype:"panel",
					layout:"form",
					columnWidth:0.3,
					items:[{	
						id:"unit_k",
						xtype:"textfield", 
						name:"unit", 
						disabled: true,
						fieldLabel:"计量单位",
						width:WarehouseLocationChange.fieldWidth
					}]
             },{
                xtype:"panel",
				columnWidth:0.6,
				items:[{
                	xtype: 'compositefield', fieldLabel : '原库位', combineErrors: false,
               		items: [{
	                	id:"locationName_comb",
	                	fieldLabel:"原库位",hiddenName:"locationName", editable: true,typeAhead: false,
						xtype:"Base_combo", allowBlank: false, width:WarehouseLocationChange.fieldWidth+100,
						entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",	
						fields:["idx","locationName","status"],
						displayField:"locationName", valueField:"locationName",
						returnField:[{widgetId:"ywwIdx_k", propertyName:"idx"},{widgetId:"status_k",propertyName:"status"}],
						listeners : {
		    				"beforequery" : function(){
		    					//选择库位前先选择库房
		                		var whName =  Ext.getCmp("whName_comb").getValue();
		    					if(whName == "" || whName == null){
		    						MyExt.Msg.alert("请先选择库房！");
		    						return false;
	    						}
	    						 this.queryName = "locationName";
	    						 this.cascadeStore();
	                		},
	                		"select" : function() {   
	    	                	var matCode = Ext.getCmp("matCode_k").getValue();
	    	                	var whIdx = Ext.getCmp("whIdx_k").getValue();
	    	                	var matType = "" ;
								//查询库存信息
				                if(null != Ext.getCmp("matType_k").getValue()){
	    	                	   matType = Ext.getCmp("matType_k").getValue().inputValue;
	    	                	}
	    	                	WarehouseLocationChange.getModelStockByLocation(whIdx, matType, matCode, this.getValue()); 
						     }   
		    			}
		    		},{
		    			id:"ywwIdx_k", name:"locationIdx", xtype:"hidden"
		    		},{
		            	xtype: 'radiogroup',
		            	id:"status_k", name:"status",
		                fieldLabel: '',
		                width:150,
		                items: [
		                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
		                    { boxLabel: '已满', name: 'status', inputValue: '已满' }
		                ]
                	}]
                },{
                	xtype: 'compositefield', fieldLabel : '调整库位', combineErrors: false,
                	items: [{
	                	id:"locationNameChange_comb",
	                	fieldLabel:"调整库位",hiddenName:"locationNameChange", editable: true,typeAhead: false,
						xtype:"Base_combo", allowBlank: false, width:WarehouseLocationChange.fieldWidth+100,
						entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",						  
						fields:["status","locationName","idx"],
						displayField:"locationName",valueField:"locationName",
						returnField:[{widgetId:"tzIdx_k",propertyName:"idx"}, {widgetId:"changeStatus_k",propertyName:"status"}],
						listeners : {
		    				"beforequery" : function(){
		    					//选择库位前先选择库房
		                		var wareHouseID =  Ext.getCmp("whName_comb").getValue();
		    					if(wareHouseID == "" || wareHouseID == null){
		    						MyExt.Msg.alert("请先选择库房！");
		    						return false;
		    					}
		    					 this.queryName = "locationName";
		    					 this.cascadeStore();
		                	}
		                }
		    		},{
		    			id:"tzIdx_k", name:"changeLocationIdx",xtype:"hidden"
		    		},{
		            	xtype: 'radiogroup',
		            	id:"changeStatus_k", name:"changeStatus",
		                fieldLabel: '',
		                width:150,
		                items: [
		                    { boxLabel: '未满', name: 'changeStatus', inputValue: '未满', checked : true },
		                    { boxLabel: '已满', name: 'changeStatus', inputValue: '已满' }
		                ]       
	                }]
	            }]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.4,
				items:[{
					id:"Qty_k",
					xtype:"textfield", 
//					name:"Qty", 
					fieldLabel:"库存数量",
					disabled: true,
					width:WarehouseLocationChange.fieldWidth
				},{
                	id:"changeQty_k",
                	vtype: "positiveInt",
					name:"changeQty", 
					fieldLabel:"调整数量",
					allowBlank: false,
					width:WarehouseLocationChange.fieldWidth
				}]
			
			},{
				columnWidth:0.2,
				items:[{
					xtype:"hidden", name:"changeEmp", value:empName,
					width:WarehouseLocationChange.fieldWidth				
				},{	
					name:"changeEmpId", value:empId, xtype:"hidden"				
				}]	
			}],
			buttonAlign: 'center',
			buttons: [{		
				text: '确认调整', iconCls: 'saveIcon', handler: function() {
										debugger;
					Ext.getCmp('unit_k').enable();
					var form = WarehouseLocationChange.baseForm.getForm();
		        	if (!form.isValid()) {
						return;
					}
					var values =  form.getValues();
					Ext.getCmp('unit_k').disable();
					if(values.locationNameChange == values.locationName){
						MyExt.Msg.alert("调整库位和原库位不能相同！");
						return ;
					}
					if (values.changeQty <= 0 || isNaN(values.changeQty)) {
						MyExt.Msg.alert("请输入大于0的有效数字！");
						return;
					} 

					Ext.getCmp('Qty_k').enable();
					var qty = Ext.getCmp("Qty_k").getValue();
					Ext.getCmp('Qty_k').disable();
					if(parseInt(values.changeQty) >  parseInt(qty)){
						MyExt.Msg.alert("调整数量大于原库位可调整数量，请重新输入！");
						return;
					}
					Ext.Msg.confirm("提示  ", "是否调整物料？  ", function(btn){
				        if(btn == 'yes') {
					        Ext.Ajax.request({
					            url: ctx + '/warehouseLocationChange!changeWhl.action',
					            jsonData: Ext.util.JSON.encode(values),
					            success: function(response, options){
					                if(self.loadMask)   self.loadMask.hide();
					                var result = Ext.util.JSON.decode(response.responseText);
					                if (result.errMsg == null) {
					                	// 操作成功       	
										alertSuccess("调整成功");
										WarehouseLocationChange.baseForm.getForm().reset();
										Ext.getCmp('whName_comb').clearValue();
										Ext.getCmp('locationName_comb').clearValue();
										Ext.getCmp("locationNameChange_comb").clearValue();   
										Ext.getCmp('matDesc_k').clearValue();
					                } else {
					                    alertFail(result.errMsg);
					                }
					            }
					        });
				        }
    				});
				}
			}]
	});		
	/** ************ 定义表单结束 ************ */
	
	//设置领料类型的初始值
	WarehouseLocationChange.init = function(){
		Ext.getCmp("matType_k").setValue(objList[0].dictname);
	};
	WarehouseLocationChange.init();
	
	/** ************ 布局 ************ */
	WarehouseLocationChange.viewport = new Ext.Viewport({
		layout: 'fit',
		items:{
			frame:true,
			title:'库位调整',
			region: 'center',
			border: true,
			split: true,
			items: [WarehouseLocationChange.baseForm]
		}
	});
	
	
});