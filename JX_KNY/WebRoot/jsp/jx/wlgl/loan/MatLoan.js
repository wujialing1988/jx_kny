/**
 * 
 * 库位调整
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatLoan');
	/** ************ 定义全局变量开始 ************ */
	MatLoan.fieldWidth = 70;
	MatLoan.labelWidth =  140;
	MatLoan.matTypeItems = [];
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	//查询库房信息
	MatLoan.getMatInfo = function(matType,whIdx, matCode){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
			//选择物料库房后带出库位信息
			var locationComb = Ext.getCmp("locationName_l");   
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
						 Ext.getCmp("totalQty_l").setValue(result.matStock.qty);										
	    			} else {
	    				 Ext.getCmp("totalQty_l").setValue(0);		
	    			}
					if (null != result.whMatQuota) {
						 Ext.getCmp("mixQty_l").setValue(result.whMatQuota.minQty);
	    				 Ext.getCmp("maxQty_l").setValue(result.whMatQuota.maxQty);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	//查询库位信息
	MatLoan.getModelStockByLocation = function(whIdx, matType, matCode, locationName){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
    		Ext.Ajax.request({
				url: ctx + "/matStockQuery!getModelStockByLocation.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType, locationName : locationName },
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("loanQty_l").setValue(result.matStock.qty);
						 Ext.getCmp("locationQty_l").setValue(result.matStock.qty);
					} else {
						 Ext.getCmp("locationQty_l").setValue(0);
						  Ext.getCmp("loanQty_l").setValue(0);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
    	}
	}
	/** ************ 定义全局函数线束 ************ */
	
	/** ************ 定义表单开始 ************ */
	MatLoan.form = new Ext.form.FormPanel({
		layout: "column", padding:"10px", buttonAlign:'center', labelWidth: MatLoan.labelWidth,
	    defaults: {
	    	layout:"form",  defaultType:"textfield",labelWidth: 60
	    },
		items:[{
				xtype:"panel",
				columnWidth:1,
				items:[{
					id:"matType_l",	xtype:'radiogroup',
					fieldLabel:"领料类型", allowBlank: false, 
					items: MatLoanBack.matTypeItems, anchor:'50%',
					listeners: {
						"change": function(){
					   		var whIdx = Ext.getCmp("whIdx_l").getValue();
			        		//查询库存信息、保有量信
    	                    matType= Ext.getCmp("matType_l").getValue().inputValue;
    	                	var matCode = Ext.getCmp("matCode_l").getValue();
    	                	var locationName = Ext.getCmp("locationName_l").getValue();
    	                	MatLoan.getMatInfo(matType,whIdx, matCode);
    	                	if(null != locationName){
    	                		MatLoan.getModelStockByLocation(whIdx, matType, matCode, locationName);
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
					xtype:"Base_combo",allowBlank: false, anchor:'72%',
					entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName", valueField:"wareHouseName",
					returnField:[{widgetId:"whIdx_l", propertyName:"idx"}],
					listeners : { 
						"beforequery" : function(){
	    				//选择前先选择领料类型
	                		if(null == Ext.getCmp("matType_l").getValue()){
				                MyExt.Msg.alert("请先选择领料类型！");  
				                return false;
	                		}
                		},
			        	"select" : function() {   
			        		var whIdx = Ext.getCmp("whIdx_l").getValue();
			            	//重新加载库位下拉数据
			                var locationName_comb = Ext.getCmp("locationName_l");   
			                locationName_comb.reset();  
			                locationName_comb.clearValue();
			                locationName_comb.queryParams = {"wareHouseIDX": whIdx};
			                locationName_comb.cascadeStore();	
			            
			               //查询库存信息、保有量信
    	                    matType= Ext.getCmp("matType_l").getValue().inputValue;
    	                	var matCode = Ext.getCmp("matCode_l").getValue();
    	                	var locationName = Ext.getCmp("locationName_l").getValue();
    	                	MatLoan.getMatInfo(matType,whIdx, matCode);
    	                	if(null != locationName){
    	                		MatLoan.getModelStockByLocation(whIdx, matType, matCode, locationName);
    	                	}
			        	}   
			    	}
			    },{
		        	id:"whIdx_l", xtype:"hidden", name:"whIDX"
			    }]
			},{
			    xtype:"panel",
				columnWidth:0.2,
				items:[{
					id:"totalQty_l",
					xtype:"textfield",  
					fieldLabel:"库存总量",
					disabled: true,
					width:MatLoan.fieldWidth
				}]
			},{
				columnWidth: 0.4,labelWidth: 30,
				 items: [{
				 	xtype: 'compositefield', fieldLabel:"低储", combineErrors: false,
					items:[{ 
						id:"mixQty_l", xtype:"textfield",  disabled: true, width:MatLoan.fieldWidth-20
						},{	xtype: 'label', text: '高储:', style: 'height:23px; line-height:23px;'	
						},{ id:"maxQty_l", xtype:"textfield",disabled: true, width: MatLoan.fieldWidth-20
					}]	
				 }]
            },{
                xtype:"panel",
				columnWidth:0.6,
				items:[{
                	id:"matDesc_l",
                	xtype:"Base_combo", allowBlank: false, anchor:'80%',
					name:"matDesc", 
					fieldLabel:"物料描述",  editable: true, typeAhead: false,
					entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
					fields:["matCode","matDesc","unit","price"],
					displayField:"matDesc",valueField:"matDesc",
					returnField:[{widgetId:"matCode_l",propertyName:"matCode"},
					             {widgetId:"unit_l",propertyName:"unit"}],
					listeners : {   
			        	"select" : function() {   
							//查询库存信息、保有量信息
    	                	var matCode = Ext.getCmp("matCode_l").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_l").getValue();
    	                	var matType = Ext.getCmp("matType_l").getValue().inputValue;
    	                	var locationName = Ext.getCmp("locationName_l").getValue();
    	                	MatLoan.getMatInfo(matType,whIdx, matCode);
    	                	if(null != locationName){
    	                		MatLoan.getModelStockByLocation(whIdx, matType, matCode, locationName);
    	                	}	       
			        	 },
			        	 beforequery: function( queryEvent ) {
		        			 var value = this.getEl().dom.value;
		        			 this.queryName = "matDescEn";
		        			 this.cascadeStore();
		        		 }   
		    		  }
				},{
			        id:"matCode_l",xtype:"hidden", name:"matCode"
				}]
			},{ 
				xtype:"panel", columnWidth:0.4,labelWidth: 60,
				items:[{
					id:"unit_l",
					xtype:"textfield", 
					name:"unit", 
					disabled: true,
					fieldLabel:"计量单位",
					width:MatLoan.fieldWidth }]
			},{
				xtype:"panel",
				columnWidth:0.6,
				items:[{
			    	xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
               		items: [{
	                	id:"locationName_l",
	                	fieldLabel:"库位",hiddenName:"locationName",
						xtype:"Base_combo", allowBlank: false,width:130,
						entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",	
						fields:["idx","locationName","status"],
						displayField:"locationName", valueField:"locationName",
						returnField:[{widgetId:"ywwIdx_l", propertyName:"idx"},{widgetId:"status_l",propertyName:"status"}],
						listeners : {
		    				"beforequery" : function(){
		    					//选择库位前先选择库房
		                		var whName =  Ext.getCmp("whName_comb").getValue();
		    					if(whName == "" || whName == null){
		    						MyExt.Msg.alert("请先选择库房！");
		    						return false;
	    						}
	                		},
	                		"select" : function() {   
	    	                	var matCode = Ext.getCmp("matCode_l").getValue();
	    	                	var whIdx = Ext.getCmp("whIdx_l").getValue();
	    	                	var matType = "" ;
								//查询库存信息
				                if(null != Ext.getCmp("matType_l").getValue()){
	    	                	   matType = Ext.getCmp("matType_l").getValue().inputValue;
	    	                	}
	    	                	locationName = this.getValue();
	    	                	MatLoan.getModelStockByLocation(whIdx, matType, matCode, locationName);
						     }   
		    			}
		    		},{
		    			id:"ywwIdx_l",name:"locationIdx",xtype:"hidden"	
		    		},{
		            	xtype: 'radiogroup',
		            	id:"status_k",
		                fieldLabel: '',
		                width:150,
		                items: [
		                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
		                    { boxLabel: '已满', name: 'status', inputValue: '已满' }]
		    		}]
	    		},{
	    			id:"loanOrg_l",
                	xtype:"textfield", 
					name:"loanOrg", maxLength:50,
					fieldLabel:"借出单位",
					allowBlank: false,
					anchor:'72%'
	               
                }]
			},{
				xtype:"panel",
				columnWidth:0.4,labelWidth: 60,
				items:[{
					id:"locationQty_l",
					xtype:"textfield", 
//					name:"qty", 
					fieldLabel:"库存数量",
					disabled: true,
					width:MatLoan.fieldWidth
				},{
	                id:"loanQty_l",
                	vtype: "positiveInt",
					name:"loanQty", 
					fieldLabel:"借出数量",
					allowBlank: false,
					width:MatLoan.fieldWidth
				}]
				
			}],
			buttonAlign: 'center',
			buttons: [{		
				text: '确认借出', iconCls: 'saveIcon', handler: function() {
					Ext.getCmp('unit_l').enable();
					var locationQty = Ext.getCmp('locationQty_l').getValue();
					var form = MatLoan.form.getForm();
		        	if (!form.isValid()) {
						return;
					}
					if(parseInt(locationQty)<=0){
						 MyExt.Msg.alert("库位【"+ Ext.getCmp("locationName_l").getValue()+ "】中物料【"+ Ext.getCmp("matDesc_l").getValue()+ "】数量为0，不能借出！");
						 return;
					}			
					var values =  form.getValues();
					Ext.getCmp('unit_l').disable();
					if (values.loanQty <= 0 || isNaN(values.loanQty)) {
						MyExt.Msg.alert("请输入大于0的有效数字！");
						return;
					}
					if(values.loanQty > parseInt(locationQty)){
						MyExt.Msg.alert("借出量大于库存量，请重新输入");
						return;
					}
					Ext.Msg.confirm("提示  ", "是否借出物料？  ", function(btn){
				        if(btn == 'yes') {
					        Ext.Ajax.request({
					            url: ctx + '/matLoan!matLoanOut.action',
					            jsonData: Ext.util.JSON.encode(values),
					            success: function(response, options){
					                if(self.loadMask)   self.loadMask.hide();
					                var result = Ext.util.JSON.decode(response.responseText);
					                if (result.errMsg == null) {	             // 操作成功
										alertSuccess("借出成功");
										MatLoan.form.getForm().reset();
										Ext.getCmp('whName_comb').clearValue();
										Ext.getCmp('locationName_l').clearValue();
										Ext.getCmp('matDesc_l').clearValue();
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
	// 物料借出Tab页
	MatLoan.tab = new Ext.Container({
		layout: 'form',
		items: [{
			height: 30, style: 'background:red;',
			labelAlign:"left",
			baseCls:"x-plain", border:false,
			labelWidth: MatLoan.labelWidth,
			height:300,
			style: 'padding:0px 10px 10px 10px;',
			items:[MatLoan.form]
		}]
	});
	/** ************ 布局 ************ */
	MatLoan.viewport = new Ext.Viewport({
	layout:"border", defaults: {layout:"fit"},
		items:[{
			xtype:"container", region:"center",
			items:[{
				xtype:"tabpanel",
				activeTab:0,
				frame:true,
				border:false,
				items:[{
						title:"物料借出",
						frame:true,
						items: [MatLoan.tab]
					},{
						title:"物料归还",
						frame:true,
						items: [MatLoanBack.tab]
					}]
			}]
		}]
	});
	
	MatLoan.init = function(){
		Ext.getCmp("matType_l").setValue(objList[ 0 ].dictname);
	};
	MatLoan.init();
	
	
});