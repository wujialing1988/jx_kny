/**
 * 入库单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatInWHNew');                       //定义命名空间
	MatInWHNew.matTypeItems = [] ;
	MatInWHNew.labelWidth = 80;
	MatInWHNew.fieldWidth = 140;
	MatInWHNew.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 自动生成“物料类型”radiogroup内容
	MatInWHNew.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	//        editor.id = "checkItemId";
	//        editor.xtype = "radiogroup";
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatInWHNew.matTypeItems.push(editor);
		}
	}();
	MatInWHNew.form = new Ext.form.FormPanel({
	    style: "padding:2px",		labelWidth: MatInWHNew.labelWidth,
	    layout: "column",buttonAlign:'center', 
	    defaults: {
	    	layout:"form",  defaultType:"textfield",
	    	labelAlign:"right"
	    },
	    items: [{
        	columnWidth: 1,
            items: [
            	{
					xtype: 'compositefield', fieldLabel:"库房",combineErrors: false,
			        items: [
			        	{ 
				        	fieldLabel:"库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"whIdx_0",propertyName:"idx"}],
							listeners : {   
					        	"select" : function() {   
	        	                	var matCode = Ext.getCmp("matCode_0").getValue();
	        	                	var whIdx = Ext.getCmp("whIdx_0").getValue();
	        	                	var matTypeItems = "" ;
					        		//重新加载库位下拉数据
					                var locationComb = Ext.getCmp("locationComb_0");   
					                locationComb.reset();  
					                locationComb.clearValue(); 
					                locationComb.queryParams = {"wareHouseIDX":whIdx};
					                locationComb.cascadeStore();
									//查询库存信息、保有量信息
					                if(null != Ext.getCmp("matTypeItems_0").getValue()){
	        	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
	        	                	}
	        	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
	        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
	        	                	}
					        		}   
				    		  }
				      	 },{
				        	id:"whIdx_0",xtype:"hidden", name:"whIDX"
				     },{
			               		xtype: 'button',
			               		text: '查看库位',
			              		width: 45,
			              		handler: function(){
			               	    	var whIdx = Ext.getCmp("whIdx_0").getValue();
			               	    	if(whIdx == "" || whIdx == null){
			    						MyExt.Msg.alert("请先选择库房！");
			    						return false;
			    					}
			    					var matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
			               	    	MatStockInfo.whIdx = whIdx ;
			               	    	MatStockInfo.matType = matTypeItems ;
			               	    	MatStockInfo.stockInfoWin.show();
			               	    	MatStockInfo.grid.store.load();
			           	   		}
		           		}]
				}
            ]
        },{
        	columnWidth: 1,
            items: [
				{
			        	fieldLabel:"物料描述",hiddenName:"matDesc",
						xtype:"Base_combo",allowBlank: false,
						editable: true, typeAhead: false,forceSelection:true,
						entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
						fields:["matCode","matDesc","unit","price"],anchor:'60%',
						displayField:"matDesc",valueField:"matDesc",
						returnField:[{widgetId:"matCode_0",propertyName:"matCode"},
						             {widgetId:"unit_0",propertyName:"unit"},
						             {widgetId:"price_0",propertyName:"price"}],
						listeners : {   
				        	"select" : function() {   
								//查询库存信息、保有量信息
        	                	var matCode = Ext.getCmp("matCode_0").getValue();
        	                	var whIdx = Ext.getCmp("whIdx_0").getValue();
        	                	var matTypeItems = "" ;
        	                	//查询库存信息
				                if(null != Ext.getCmp("matTypeItems_0").getValue()){
        	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
        	                	}
        	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
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
			        	id:"matCode_0",xtype:"hidden", name:"matCode"
			     }
            ]
        },{
        	columnWidth: 0.2,
            items: [
				{id:"totalQty_0", fieldLabel:"库存总量",name:"totalQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.2,
            items: [
            	{ id:"unit_0", fieldLabel:"计量单位",name: "unit", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.15,
            items: [
				{ id:"price_0", fieldLabel:"单价",name: "price", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.45,
            items: [
            {
					xtype: 'compositefield', fieldLabel:"低储",combineErrors: false,
			        items: [{ id:"mixQty_0", xtype:"textfield",name: "mixQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
			        	,{
							xtype: 'label',
							text: '高储',
							style: 'height:23px; line-height:23px;'	
						},{id:"maxQty_0", xtype:"textfield",name: "maxQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}]
				}
            	
            ]
        },{
        	columnWidth: 0.4,
            items: [
            	{
					xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
			        items: [{
					        	id:"locationComb_0",fieldLabel:"库位",hiddenName:"locationName",
								xtype:"Base_combo",allowBlank: false,editable: true, typeAhead: true,
								entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",						  
								fields:["idx","locationName","status"],forceSelection:true,
								displayField:"locationName",valueField:"locationName" ,
								returnField:[{widgetId:"locationIdx_0",propertyName:"idx"},
											{widgetId:"status_0",propertyName:"status"}],
								listeners : {   
									"beforequery" : function(){
				    					//选择库位前先选择库房
				                		var whIdx = Ext.getCmp("whIdx_0").getValue();
				    					if(whIdx == "" || whIdx == null){
				    						MyExt.Msg.alert("请先选择库房！");
				    						return false;
				    					}
				    					this.queryName = "locationName";
				        				this.cascadeStore();
				                	},
						        	"select" : function() {   
		        	                	var matCode = Ext.getCmp("matCode_0").getValue();
		        	                	var whIdx = Ext.getCmp("whIdx_0").getValue();
		        	                	var matTypeItems = "" ;
										//查询库存信息
						                if(null != Ext.getCmp("matTypeItems_0").getValue()){
		        	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
		        	                	}
		        	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
		        	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, this.getValue(),0);
		        	                	}
						        		}   
					    		  }
						  },{
				        		id:"locationIdx_0",xtype:"hidden", name:"locationIdx"
				     		},
				        	{
				            	xtype: 'radiogroup',
				            	id:"status_0",
				                fieldLabel: '',
				                width:150,
				                items: [
				                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
				                    { boxLabel: '已满', name: 'status', inputValue: '已满' }
				                ]
							}]
				}
            ]
        },{
        	columnWidth: 0.15,
            items: [
				{id:"locationQty_0",fieldLabel:"库存数量",name:"locationQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.15,
            items: [
            	{ id:"qty_0",fieldLabel:"入库数量",allowBlank:false,vtype: "positiveInt",width:MatInWHNewTab2.qtyWidth,name: "qty"}
            ]
        }],
    	buttons: [{
			text : "确认入库",iconCls : "checkIcon", handler: function(){
				var form = MatInWHNew.form.getForm();
			    if (!form.isValid()) return;
			    var data = form.getValues();
			    data.inWhType = TYPE_XPRK ;//入库类型--新品入库
			    data.matType = Ext.getCmp("matTypeItems_0").getValue().inputValue ;
			    delete data.totalQty ;
			    delete data.mixQty ;
			    delete data.maxQty ;
			    delete data.locationQty ;
			    
			    //判断页面输入的值是否超过了integer的最大范围
			    var value = parseInt(data.qty);
			    if(value > 2147483647){
			    	MyExt.Msg.alert("输入的数量超过了系统最大值 2147483647 ！请重新输入");
					return;
			    }
			    
				MatInWHNew.loadMask.show();
		        Ext.Ajax.request({
		            url: ctx + '/matInWHNew!saveMatInWHNew.action',
		            jsonData: data,
		            success: function(response, options){
		              	MatInWHNew.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
		                    Ext.getCmp("qty_0").setValue("");
		                    var matCode = Ext.getCmp("matCode_0").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_0").getValue();
    	                	var locationName = Ext.getCmp("locationComb_0").getValue();
    	                	var matTypeItems = "" ;
			                if(null != Ext.getCmp("matTypeItems_0").getValue()){
    	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
    	                	}
    	                	//查询库存总量、保有量信息
    	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
        	                	}
        	                //查询库位库存信息
    	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
    	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 0);
    	                	}
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                MatInWHNew.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
			}
		}]
	});
	
	// 物料入库Tab页
	MatInWHNew.tab = new Ext.Container({
		layout: 'form',
		items: [{
			xtype: 'form', style: 'background:red;',
			id:"qcForm_0",
			labelAlign:"right",
			baseCls:"x-plain", border:false,
			labelWidth: MatInWHNew.labelWidth,
			height:28,
			style: 'padding:0px 2px 2px 2px;',
			items:[
				{
					id:"matTypeItems_0",xtype: 'radiogroup', fieldLabel: '物料类型', items: MatInWHNew.matTypeItems, anchor:'50%',
	                listeners: {
	            		"render" : function(field){
	            			//将选中的数据标红
	            		},
	            		"change" : function(field,newValue,oldValue){
		                    var matCode = Ext.getCmp("matCode_0").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_0").getValue();
    	                	var locationName = Ext.getCmp("locationComb_0").getValue();
    	                	var matTypeItems = "" ;
			                if(null != Ext.getCmp("matTypeItems_0").getValue()){
    	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
    	                	}
    	                	//查询库存总量、保有量信息
    	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
        	                	}
        	                //查询库位库存信息
    	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
    	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 0);
    	                		Ext.getCmp("qty_0").setValue("");
    	                	}
	            		}
	            	}
				}]
		}, MatInWHNew.form]
	});
	
	var viewport = new Ext.Viewport({
	layout:"border", defaults: {layout:"fit"},
		items:[{
			xtype:"container", region:"center",
			items:[{
				xtype:"tabpanel",
				id:"id_tabpanel",
				activeTab:0,
				frame:true,
				border:false,
				items:[{
						title:"物料入库",
						layout:"fit",
						frame:true,
						items: [MatInWHNew.tab]
					},{
						title:"机车退回入库",
						layout:"fit",
						frame:true,
						items: [MatInWHNewTab1.tab]
					},{
						title:"其他原因入库",
						layout:"fit",
						frame:true,
						items: [MatInWHNewTab2.tab]
					}]
			}]
		}]
	});
	MatInWHNew.init = function(){
		Ext.getCmp("matTypeItems_0").setValue(objList[ 0 ].dictname);
	};
	MatInWHNew.init();
});