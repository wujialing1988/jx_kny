/**
 * 其他原因出库 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatOutWHTab1');                       //定义命名空间
	MatOutWHTab1.matTypeItems = [] ;
	MatOutWHTab1.labelWidth = 80;
	MatOutWHTab1.fieldWidth = 120;
	MatOutWHTab1.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 自动生成“物料类型”radiogroup内容
	MatOutWHTab1.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	//        editor.id = "checkItemId";
	//        editor.xtype = "radiogroup";
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatOutWHTab1.matTypeItems.push(editor);
		}
	}();
	MatOutWHTab1.form = new Ext.form.FormPanel({
	    style: "padding:2px",		labelWidth: MatOutWHTab1.labelWidth,
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
							returnField:[{widgetId:"whIdx_1",propertyName:"idx"}],
							listeners : {   
					        	"select" : function() {   
	        	                	var matCode = Ext.getCmp("matCode_1").getValue();
	        	                	var whIdx = Ext.getCmp("whIdx_1").getValue();
	        	                	var matTypeItems = "" ;
					                if(null != Ext.getCmp("matTypeItems_1").getValue()){
	        	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
	        	                	}
	        	                	//重新加载库位下拉数据
					                var locationComb = Ext.getCmp("locationComb_1");   
					                locationComb.reset();  
					                locationComb.clearValue(); 
					                if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
	        	                		locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
					                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
					                	//查询库存信息、保有量信息
					                	MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
	        	                	}else{
	        	                		locationComb.queryParams = {"wareHouseIDX":whIdx};
	        	                	}
					                locationComb.cascadeStore();
							       
					        		}   
				    		  }
				      	 },{
				        	id:"whIdx_1",xtype:"hidden", name:"whIDX"
				     },{
			               		xtype: 'button',
			               		text: '查看库位',
			              		width: 45,
			              		handler: function(){
			               	    	var whIdx = Ext.getCmp("whIdx_1").getValue();
			               	    	if(whIdx == "" || whIdx == null){
			    						MyExt.Msg.alert("请先选择库房！");
			    						return false;
			    					}
			               	    	var matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
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
						xtype:"Base_combo",allowBlank: false,anchor:'60%',
						editable: true, typeAhead: false,forceSelection:true,
						entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
						fields:["matCode","matDesc","unit","price"],
						displayField:"matDesc",valueField:"matDesc",
						returnField:[{widgetId:"matCode_1",propertyName:"matCode"},
						             {widgetId:"unit_1",propertyName:"unit"},
						             {widgetId:"price_1",propertyName:"price"}],
						listeners : {   
				        	"select" : function() {   
								//查询库存信息、保有量信息
        	                	var matCode = Ext.getCmp("matCode_1").getValue();
        	                	var whIdx = Ext.getCmp("whIdx_1").getValue();
        	                	var matTypeItems = "" ;
				                if(null != Ext.getCmp("matTypeItems_1").getValue()){
        	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
        	                	}
        	                	//重新加载库位下拉数据
				                var locationComb = Ext.getCmp("locationComb_1");   
				                locationComb.reset();  
				                locationComb.clearValue(); 
				                if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
				                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
				                	locationComb.cascadeStore();
				                	//查询库存信息
				                	MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
        	                	}
				        		} ,
				        		// Modified by hetao on 2016-05-19 修改使用汉字首拼进行数据源过滤
				        		beforequery: function( queryEvent ) {
				        			var value = this.getEl().dom.value;
				        			this.queryName = "matDescEn";
				        			this.cascadeStore();
				        		}  
			    		  }
			  },{ id:"matCode_1",xtype:"hidden", name:"matCode" }
            ]
        },{
        	columnWidth: 0.2,
            items: [
				{id:"totalQty_1", fieldLabel:"库存总量",name:"totalQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.2,
            items: [
            	{ id:"unit_1", fieldLabel:"计量单位",name: "unit", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.2,
            items: [
				{ id:"price_1", fieldLabel:"单价",name: "price", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.3,
            items: [
            {
					xtype: 'compositefield', fieldLabel:"低储",combineErrors: false,
			        items: [{ id:"mixQty_1", xtype:"textfield",name: "mixQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
			        	,{
							xtype: 'label',
							text: '高储',
							style: 'height:23px; line-height:23px;'	
						},{id:"maxQty_1", xtype:"textfield",name: "maxQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}]
				}
            	
            ]
        },{
        	columnWidth: 0.4,
            items: [
            	{
					xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
			        items: [{
					        	id:"locationComb_1",fieldLabel:"库位",hiddenName:"locationName",
								xtype:"Base_combo",allowBlank: false,
								entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",						  
								fields:["idx","locationName","status"],
								displayField:"locationName",valueField:"locationName" ,
								returnField:[{widgetId:"locationIdx_1",propertyName:"idx"},
											{widgetId:"status_1",propertyName:"status"}],
								listeners : {   
									"beforequery" : function(){
				    					//选择库位前先选择库房
				                		var whIdx = Ext.getCmp("whIdx_1").getValue();
				    					if(whIdx == "" || whIdx == null){
				    						MyExt.Msg.alert("请先选择库房！");
				    						return false;
				    					}
				                	},
						        	"select" : function() {   
		        	                	var matCode = Ext.getCmp("matCode_1").getValue();
		        	                	var whIdx = Ext.getCmp("whIdx_1").getValue();
		        	                	var matTypeItems = "" ;
										//查询库存信息
						                if(null != Ext.getCmp("matTypeItems_1").getValue()){
		        	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
		        	                	}
		        	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
		        	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, this.getValue(),1);
		        	                	}
								       }   
					    		  }
						  },{
				        		id:"locationIdx_1",xtype:"hidden", name:"locationIdx"
				     		},
				        	{
				            	xtype: 'radiogroup',
				            	id:"status_1",
				                fieldLabel: '',
				                width:150,
				                items: [
				                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
				                    { boxLabel: '已满', name: 'status', inputValue: '已满' }
				                ]
							}]
				},
				{
						id:"exWhReason_comb",
						allowBlank:false,
						xtype: 'EosDictEntry_combo',
						hiddenName: 'exWhReason',editable:true,
						dicttypeid:'WLGL_OUTWH_REASON',anchor:'80%',
						displayField:'dictname',valueField:'dictname',
						fieldLabel: "出库原因"
		    		}
            ]
        },{
        	columnWidth: 0.2,
            items: [
            	{id:"locationQty_1",fieldLabel:"库存数量",name:"locationQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true},
            	{ 
	        		allowBlank: false,
	        		id:"getEmp_select_1",xtype:'Om_Emp_combo', 
	        		fieldLabel: "领件人", hiddenName:'getEmp',
					displayField:'empname', valueField:'empname',
					width:MatOutWHTab1.fieldWidth,
					listeners : {   
						"select" : function(me, record, index) {  
							Ext.getCmp("getOrg_select_1").setDisplayValue(record.get("orgid"),record.get("orgname"));	//领件班组
							Ext.getCmp("getOrg_1").setValue(record.get("orgname"));
							Ext.getCmp("getOrgSeq_1").setValue(record.get("orgseq"));
							Ext.getCmp("getEmpID_1").setValue(record.get("empid"));
						}}
				},{
					id:"getEmpID_1",xtype:"hidden", name:"getEmpID"
			}	
            ]
        },{
        	columnWidth: 0.3,
            items: [
            	{ id:"qty_1",fieldLabel:"出库数量",allowBlank:false,vtype: "positiveInt",width:MatOutWHTab2.qtyWidth,name: "qty"},
            	{ 
	            		id:"getOrg_select_1",xtype:'OmOrganizationCustom_comboTree',
	            		editable:false,allowBlank: false,fieldLabel : "领件班组",hiddenName:'getOrgID',
						returnField:[{widgetId:"getOrg_1",propertyName:"orgname"},
									{widgetId:"getOrgSeq_1",propertyName:"orgseq"}],
						editable:false
				    },{
				    	id:"getOrg_1",xtype:"hidden", name:"getOrg"
				},{
				    	id:"getOrgSeq_1",xtype:"hidden", name:"getOrgSeq"
				}
            ]
        }],
    	buttons: [{
			text : "确认出库",iconCls : "checkIcon", handler: function(){
				var form = MatOutWHTab1.form.getForm();
			    if (!form.isValid()) return;
			    var data = form.getValues();
			    if(data.qty-data.locationQty > 0){
			    	MyExt.Msg.alert("出库数量不能大于库存数量！");
	                return ;
			    }
			    data.wHType = TYPE_QTYY ;//出库类型--其他原因出库
			    data.matType = Ext.getCmp("matTypeItems_1").getValue().inputValue ;
			    data.exWhReason = Ext.get("exWhReason_comb").dom.value;
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
			    
				MatOutWHTab1.loadMask.show();
		        Ext.Ajax.request({
		            url: ctx + '/matOutWH!saveMatOutWH.action',
		            jsonData: data,
		            success: function(response, options){
		              	MatOutWHTab1.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
		                    Ext.getCmp("qty_1").setValue("");
		                    var matCode = Ext.getCmp("matCode_1").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_1").getValue();
    	                	var locationName = Ext.getCmp("locationComb_1").getValue();
    	                	var matTypeItems = "" ;
			                if(null != Ext.getCmp("matTypeItems_1").getValue()){
    	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
    	                	}
    	                	//查询库存总量、保有量信息
    	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
        	                	}
        	                //查询库位库存信息
    	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
    	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 1);
    	                	}
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                MatOutWHTab1.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
			}
		}]
	});
	
	// 其他原因出库Tab页
	MatOutWHTab1.tab = new Ext.Container({
		layout: 'form',
		items: [{
			xtype: 'form', style: 'background:red;',
			id:"qcForm_1",
			labelAlign:"right",
			baseCls:"x-plain", border:false,
			labelWidth: MatOutWHTab1.labelWidth,
			height:28,
			style: 'padding:0px 2px 2px 2px;',
			items:[
				{
					id:"matTypeItems_1",xtype: 'radiogroup', fieldLabel: '物料类型', items: MatOutWHTab1.matTypeItems, anchor:'50%',
	                listeners: {
	            		"render" : function(field){
	            			//将选中的数据标红
	            		},
	            		"change" : function(field,newValue,oldValue){
	            			Ext.getCmp("qty_1").setValue("");
		                    var matCode = Ext.getCmp("matCode_1").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_1").getValue();
    	                	var locationName = Ext.getCmp("locationComb_1").getValue();
    	                	var matTypeItems = "" ;
			                if(null != Ext.getCmp("matTypeItems_1").getValue()){
    	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
    	                	}
    	                	//重新加载库位下拉数据
			                var locationComb = Ext.getCmp("locationComb_1");   
			                locationComb.reset();  
			                locationComb.clearValue(); 
    	                	if(null != whIdx && "" != whIdx && null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
    	                			locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
			                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
        	                		locationComb.cascadeStore();
			                		//查询库存总量、保有量信息
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
        	                		//查询库位库存信息
        	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 1);
        	                	}
	            		}
	            	}
				}]
		}, MatOutWHTab1.form]
	});
	MatOutWHTab1.init = function(){
		Ext.getCmp("matTypeItems_1").setValue(objList[ 0 ].dictname);
	};
	MatOutWHTab1.init();
});