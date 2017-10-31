/**
 * 机车退回入库单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatLoanBack');                       //定义命名空间
	MatLoanBack.matTypeItems = [] ;
	MatLoanBack.labelWidth = 100;
	MatLoanBack.fieldWidth = 70;
	MatLoanBack.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 自动生成“物料类型”radiogroup内容
	MatLoanBack.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatLoanBack.matTypeItems.push(editor);
		}
	}();
	/** ************ 定义全局函数开始 ************ */
	//查询库房信息
	MatLoanBack.getMatInfo = function(matType,whIdx, matCode){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
			Ext.Ajax.request({
				url: ctx + "/matTypeList!getMatInfo.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("totalQty_b").setValue(result.matStock.qty);										
	    			} else {
	    				 Ext.getCmp("totalQty_b").setValue(0);    				
	    			}
					if (null != result.whMatQuota) {
						 Ext.getCmp("mixQty_b").setValue(result.whMatQuota.minQty);
	    				 Ext.getCmp("maxQty_b").setValue(result.whMatQuota.maxQty);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	//查询库位信息
	MatLoanBack.getModelStockByLocation = function(whIdx, matType, matCode, locationName){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
    		Ext.Ajax.request({
				url: ctx + "/matStockQuery!getModelStockByLocation.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType, locationName : locationName},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("locationQty_b").setValue(result.matStock.qty);
					} else {
						 Ext.getCmp("locationQty_b").setValue(0);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
    	}
	}
	
	//查询单位借出信息
	MatLoanBack.getMatLoanOut = function(loanOrg, matType, matCode){
	if(null != matCode && "" != matCode && null != matType && "" != matType && null != loanOrg && "" != loanOrg){
    		Ext.Ajax.request({
				url: ctx + "/matLoan!getMatLoanOut.action",
				params: { matType : matType, matCode: matCode , loanOrg :loanOrg },
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matLoan) {
						 Ext.getCmp("loanOutQty_b").setValue(result.matLoan.loanQty);
						 Ext.getCmp("loanQty_b").setValue(result.matLoan.loanQty);
					} else {
						 Ext.getCmp("loanOutQty_b").setValue(0);
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
	MatLoanBack.form = new Ext.form.FormPanel({
	   layout: "column", padding:"10px", buttonAlign:'center', labelWidth: MatLoanBack.labelWidth,
	   defaults: {
	    	layout:"form",  defaultType:"textfield",labelWidth: 60
	   },
	   items:[{
			xtype:"panel",
			layout:"form",
			columnWidth:1,
			items:[{
				id:"matType_b",	xtype:'radiogroup',
				fieldLabel:"领料类型", allowBlank: false, 
				items: MatLoanBack.matTypeItems, anchor:'50%',
				listeners: {
					"change": function(){
				   		var whIdx = Ext.getCmp("whIdx_b").getValue();
		        		//查询库存信息、保有量信
	                    var matType= Ext.getCmp("matType_b").getValue().inputValue;
	                	var matCode = Ext.getCmp("matCode_b").getValue();
	                	var locationName = Ext.getCmp("locationName_b").getValue();
	                	var loanOrg  = Ext.getCmp("loanOrg_b").getValue();
	                	MatLoanBack.getMatInfo(matType,whIdx, matCode);
	                	if(null != locationName){
	                		MatLoanBack.getModelStockByLocation(whIdx, matType, matCode, locationName);
	                	}	
	                	MatLoanBack.getMatLoanOut(loanOrg, matType, matCode);
					}
				}
			}]			
		},{
			xtype:"panel",
			columnWidth:0.4,
			items:[{
			    id:"whName_comb_b", fieldLabel:"库房",hiddenName:"whName",
				xtype:"Base_combo",allowBlank: false, anchor:'72%',
				entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse",						  
				queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
				fields:["wareHouseID","wareHouseName","idx"],
				displayField:"wareHouseName", valueField:"wareHouseName",
				returnField:[{widgetId:"whIdx_b", propertyName:"idx"}],
				listeners : { 
					"beforequery" : function(){
    				//选择前先选择领料类型
                		if(null == Ext.getCmp("matType_b").getValue()){
			                MyExt.Msg.alert("请先选择领料类型！");  
			                return false;
                		}
            		},
		        	"select" : function() {   
		        		var whIdx = Ext.getCmp("whIdx_b").getValue();
		            	//重新加载库位下拉数据
		                var locationName_comb = Ext.getCmp("locationName_b");   
		                locationName_comb.reset();  
		                locationName_comb.clearValue();
		                locationName_comb.queryParams = {"wareHouseIDX": whIdx};
		                locationName_comb.cascadeStore();	
		            
		               //查询库存信息、保有量信
	                    var matType= Ext.getCmp("matType_b").getValue().inputValue;
	                	var matCode = Ext.getCmp("matCode_b").getValue();
	                	var locationName = Ext.getCmp("locationName_b").getValue();
	                	MatLoanBack.getMatInfo(matType,whIdx, matCode);
	                	if(null != locationName){
	                		MatLoanBack.getModelStockByLocation(whIdx, matType, matCode, locationName);
	                	}		
		        	}   
		    	}
		    },{
	        	id:"whIdx_b", xtype:"hidden", name:"whIDX"
	        }]
		},{
			xtype:"panel",columnWidth:0.2,
			items:[{
				id:"totalQty_b",
				xtype:"textfield",  
				fieldLabel:"库存总量",
				disabled: true,
				width:MatLoanBack.fieldWidth
			}]				
	    },{
			columnWidth: 0.4,labelWidth: 40,
			items: [{
			 	xtype: 'compositefield', fieldLabel:"低储", combineErrors: false,
				items:[{ 
					id:"mixQty_b", xtype:"textfield", disabled: true, width:MatLoanBack.fieldWidth-20
					},{	xtype: 'label', text: '高储:', style: 'height:23px; line-height:23px;'	
					},{ id:"maxQty_b",
						xtype:"textfield", 
						disabled: true,
						width:MatLoanBack.fieldWidth-20
					}]	
			 }]
         },{
        	xtype:"panel",
			columnWidth:0.6,
			items:[{ 
            	id:"matDesc_b",
            	xtype:"Base_combo", allowBlank: false, anchor:'80%',
				name:"matDesc", 
				fieldLabel:"物料描述", editable: true, typeAhead:true,
				entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
				fields:["matCode","matDesc","unit","price"],
				displayField:"matDesc",valueField:"matDesc",
				returnField:[{widgetId:"matCode_b",propertyName:"matCode"},
				             {widgetId:"unit_b",propertyName:"unit"}],
				listeners : {   
		        	"select" : function() {   
						//查询库存信息、保有量信息
	                	var matCode = Ext.getCmp("matCode_b").getValue();
	                	var whIdx = Ext.getCmp("whIdx_b").getValue();
	                	var matType = Ext.getCmp("matType_b").getValue().inputValue;
	                	var locationName = Ext.getCmp("locationName_b").getValue();
	                	var locationName = Ext.getCmp("locationName_b").getValue();
	                	var loanOrg  = Ext.getCmp("loanOrg_b").getValue();
	                	MatLoanBack.getMatInfo(matType,whIdx, matCode);
	                	if(null != locationName){
	                		MatLoanBack.getModelStockByLocation(whIdx, matType, matCode, locationName);
	                	}
	                	MatLoanBack.getMatLoanOut(loanOrg, matType, matCode);
		        	 },
		        	 beforequery: function( queryEvent ) {
	        			 var value = this.getEl().dom.value;
	        			 this.queryName = "matDescEn";
	        			 this.cascadeStore();
	        		 }
		    	}
			},{
		        id:"matCode_b",xtype:"hidden", name:"matCode"
			}]
		},{ 
				xtype:"panel", columnWidth:0.4,labelWidth: 60,
				items:[{
					id:"unit_b",
					xtype:"textfield", 
					name:"unit", 
					disabled: true,
					fieldLabel:"计量单位",
					width:MatLoanBack.fieldWidth
				}]
		},{
			xtype:"panel",
			columnWidth:0.6,
			items:[{
		    	xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
	       		items: [{
	            	id:"locationName_b",
	            	fieldLabel:"库位",hiddenName:"locationName", editable: true,typeAhead:true,
					xtype:"Base_combo", allowBlank: false,width:130,
					entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",	
					fields:["idx","locationName","status"],forceSelection:true,
					displayField:"locationName", valueField:"locationName",
					returnField:[{widgetId:"ywwIdx_b", propertyName:"idx"},{widgetId:"status_b",propertyName:"status"}],
					listeners : { 
	    				"beforequery" : function(){
	    					//选择库位前先选择库房
	                		var whName =  Ext.getCmp("whName_comb_b").getValue();
	    					if(whName == "" || whName == null){
	    						MyExt.Msg.alert("请先选择库房！");
	    						return false;
							}
							this.queryName = "locationName";
	    					this.cascadeStore();
	            		},
	            		"select" : function() {   
		                	var matCode = Ext.getCmp("matCode_b").getValue();
		                	var whIdx = Ext.getCmp("whIdx_b").getValue();
		                	var matType = "" ;
							//查询库存信息
			                if(null != Ext.getCmp("matType_b").getValue()){
		                	   matType = Ext.getCmp("matType_b").getValue().inputValue;
		                	}
		                	var locationName = this.getValue();          
		                	if(null != locationName){
		                		MatLoanBack.getModelStockByLocation(whIdx, matType, matCode, locationName);
		                	}	
					     }   
	    			}
	    	
	            },{
	            	id:"matCode_b",xtype:"hidden", name:"matCode"
	            },{
	            	xtype: 'radiogroup',
	            	id:"status_b",
	                fieldLabel: '',
	                width:150,
	                items: [
	                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
	                    { boxLabel: '已满', name: 'status', inputValue: '已满' }]   
	            }]
			},{
			    id:"loanOrg_b",
               	fieldLabel:"归还单位",hiddenName:"loanOrg", editable: true, typeAhead:true,
				xtype:"Base_combo", allowBlank: false, anchor:'72%',
				entity:"com.yunda.jx.wlgl.loan.entity.MatLoan",	
				idProperty: "loanOrg",
				fields:["loanOrg"],forceSelection:true,
				displayField:"loanOrg", valueField:"loanOrg",
				returnField:[{widgetId:"loanOrg", propertyName:"loanOrg"}], 
				listeners : {
					"beforequery" : function(){		
						this.queryName = "loanOrg";
    					this.cascadeStore();
            		},
            		"select" : function() {   
	                	var matCode = Ext.getCmp("matCode_b").getValue();
	                	var matType = "" ;
						//查询库存信息
		                if(null != Ext.getCmp("matType_b").getValue()){
	                	   matType = Ext.getCmp("matType_b").getValue().inputValue;
	                	}
	                	MatLoanBack.getMatLoanOut(this.getValue(), matType, matCode);
            		}
				}
			}]
		},{
			xtype:"panel",
			layout:"form",
			columnWidth:0.4,
			items:[{
				id:"locationQty_b",
				xtype:"textfield",  
				fieldLabel:"库存数量",
				disabled: true,
				width:MatLoanBack.fieldWidth
			},{
                id:"loanOutQty_b",
            	xtype:"textfield",  
				fieldLabel:"借出数量",
				disabled: true,
				width:MatLoanBack.fieldWidth
			}]
	   },{
        	xtype:"panel",
			layout:"form",
			columnWidth:0.2,items:[{    
                id:"loanQty_b",
            	vtype: "positiveInt",
				name:"loanQty", 
				fieldLabel:"归还数量",
				allowBlank: false,
				width:MatLoanBack.fieldWidth
            }]
		}],
		buttonAlign: 'center',
		buttons: [{		
			text: '确认归还', iconCls: 'saveIcon', handler: function() {
				Ext.getCmp('unit_b').enable();
				var loanOutQty = Ext.getCmp('loanOutQty_b').getValue();	
				var form = MatLoanBack.form.getForm();
	        	if (!form.isValid()) {
					return;
				}
				var values =  form.getValues();
				Ext.getCmp('unit_b').disable();
				if (values.loanQty <= 0 || isNaN(values.loanQty)) {
					MyExt.Msg.alert("请输入大于0的有效数字！");
					return;
				}
				if(values.loanQty > parseInt(loanOutQty)){
					MyExt.Msg.alert("归还数量大于借出量，请重新输入");
					return;
				}
				Ext.Msg.confirm("提示  ", "是否归还物料？  ", function(btn){
			        if(btn == 'yes') {
				        Ext.Ajax.request({
				            url: ctx + '/matLoan!matLoanBack.action',
				            jsonData: Ext.util.JSON.encode(values),
				            success: function(response, options){
				                if(self.loadMask)   self.loadMask.hide();
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {	             // 操作成功
									alertSuccess("归还成功");
									MatLoanBack.form.getForm().reset();
									Ext.getCmp('whName_comb_b').clearValue();
									Ext.getCmp('locationName_b').clearValue();
									Ext.getCmp('matDesc_b').clearValue();
									Ext.getCmp('loanOrg_b').clearValue();
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
	// 物料归还Tab页
	MatLoanBack.tab = new Ext.Container({
		layout: 'form',
		items: [{
			height: 30, style: 'background:red;',
			labelAlign:"left",
			baseCls:"x-plain", border:false,
			labelWidth: MatLoanBack.labelWidth,
			height:300,
			style: 'padding:0px 10px 10px 10px;',
			items:[MatLoanBack.form]
		}]
	});
	// 物料类型初始化
	MatLoanBack.init = function(){
		Ext.getCmp("matType_b").setValue(objList[ 0 ].dictname);
	};
	MatLoanBack.init();
});