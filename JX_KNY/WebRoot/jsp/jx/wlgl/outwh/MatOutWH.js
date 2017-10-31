/**
 * 出库到机车出库单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatOutWH');                       //定义命名空间
	MatOutWH.matTypeItems = [] ;
	MatOutWH.labelWidth = 80;
	MatOutWH.fieldWidth = 70;
	MatOutWH.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 自动生成“物料类型”radiogroup内容
	MatOutWH.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	//        editor.id = "checkItemId";
	//        editor.xtype = "radiogroup";
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatOutWH.matTypeItems.push(editor);
		}
	}();
	MatOutWH.form = new Ext.form.FormPanel({
	    style: "padding:2px",		labelWidth: MatOutWH.labelWidth,
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
		        	                	if(null != Ext.getCmp("matTypeItems_0").getValue()){
		        	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
		        	                	}
						        		//重新加载库位下拉数据
						                var locationComb = Ext.getCmp("locationComb_0");   
						                locationComb.reset();  
						                locationComb.clearValue(); 
						                if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
		        	                		locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
						                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
						                	//查询库存信息、保有量信息
						                	MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
		        	                	}else{
		        	                		locationComb.queryParams = {"wareHouseIDX":whIdx};
		        	                	}
						                locationComb.cascadeStore();
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
						editable: true, typeAhead: true,forceSelection:true,
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
					                if(null != Ext.getCmp("matTypeItems_0").getValue()){
	        	                	   matTypeItems = Ext.getCmp("matTypeItems_0").getValue().inputValue;
	        	                	}
	        	                	//重新加载库位下拉数据
					                var locationComb = Ext.getCmp("locationComb_0");   
					                locationComb.reset();  
					                locationComb.clearValue(); 
					                if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
	        	                		locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
					                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
					                	locationComb.cascadeStore();
					                	//查询库存信息
					                	MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
	        	                	}
				        		} ,
				        		// Modified by hetao on 2016-05-19 修改使用汉字首拼进行数据源过滤
				        		beforequery: function( queryEvent ) {
				        			var value = this.getEl().dom.value;
				        			this.queryName = "matDescEn";
				        			this.cascadeStore();
				        		}  
			    		  }
			  },{ id:"matCode_0",xtype:"hidden", name:"matCode" }
            ]
        },{
        	columnWidth: 0.25,
            items: [
				{id:"totalQty_0", fieldLabel:"库存总量",name:"totalQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true},
				{
					xtype: 'compositefield', fieldLabel : '车号', combineErrors: false,
			        items: [{
							id:"trainType_comb",xtype: "Base_combo", fieldLabel: "车型",
							hiddenName: "trainTypeIDX", width:MatOutWH.fieldWidth,
							editable: true, typeAhead: false,forceSelection:true,
							entity:'com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType',
							fields:['trainTypeIDX','trainTypeShortName'],
							returnField: [{widgetId:"trainTypeShortName",propertyName:"trainTypeShortName"}],
							displayField: "trainTypeShortName", valueField: "trainTypeIDX",
							pageSize: 0, minListWidth: MatOutWH.fieldWidth, 
							allowBlank:false ,
							listeners : {   
					        	"collapse" : function() {   
					            	//重新加载车号下拉数据
					                var trainNo_comb = Ext.getCmp("trainNo_comb");   
					                trainNo_comb.reset();  
					                Ext.getCmp("trainNo_comb").clearValue(); 
					                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
					                trainNo_comb.cascadeStore();
					                //重新加载修程下拉数据
					                var rc_comb = Ext.getCmp("rc_comb");   
					                rc_comb.reset();  
					                Ext.getCmp("rc_comb").clearValue(); 
					                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
					                rc_comb.cascadeStore();
					                Ext.getCmp("rt_comb").clearValue(); 
					        	},
					        	beforequery: function( queryEvent ) {
				        			this.queryName = "trainTypeShortName";
				        			this.cascadeStore();
				        		}   
					    	}
						},{ id:"trainTypeShortName",xtype:"hidden", name:"trainTypeShortName" },
				        {
							id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
							editable: true, typeAhead: false,forceSelection:true,
							hiddenName: "trainNo", width:MatOutWH.fieldWidth,
							displayField: "trainNo", valueField: "trainNo",
							pageSize: 20, allowBlank:false  ,
		        			listeners : {  
		        				"beforequery" : function(){
			    					//选择修次前先选车型
			                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
			    					if(trainTypeId == "" || trainTypeId == null){
			    						MyExt.Msg.alert("请先选择车型！");
			    						return false;
			    					}
			                	}
		                    }
			            }]
				}
            ]
        },{
        	columnWidth: 0.25,
            items: [
            	{ id:"unit_0", fieldLabel:"计量单位",name: "unit", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true},
            	{
					xtype: 'compositefield', fieldLabel : '修程', combineErrors: false,
				    items: [{
	        			id:"rc_comb",
	        			xtype: "Base_combo",
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],width:MatOutWH.fieldWidth,
	        			fieldLabel: "修程",editable: true, typeAhead: false,forceSelection:true,
	        			hiddenName: "xcID", 
	        			returnField: [{widgetId:"xcName",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: MatOutWH.fieldWidth,
	        			allowBlank: false,
	        			listeners : {  
	        				"beforequery" : function(){
		    					//选择修次前先选车型
		                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
		    					if(trainTypeId == "" || trainTypeId == null){
		    						MyExt.Msg.alert("请先选择车型！");
		    						return false;
		    					}
		    					this.queryName = "xcName";
			        			this.cascadeStore();
		                	},
	                    	"collapse" : function() {   
	                    		//重新加载修次数据
	                        	var rt_comb = Ext.getCmp("rt_comb");
	                        	rt_comb.clearValue();
	                         	rt_comb.reset();
	                            rt_comb.queryParams = {"rcIDX":this.getValue()};
	                            rt_comb.cascadeStore();  
	                    	}
	                    }
	        		},{
	        			id:"xcName",xtype:"hidden", name:"xcName"
	        		},
					{
			    			id:"rt_comb",
			    			xtype: "Base_combo",
			    			fieldLabel: "修次",editable: true, typeAhead: false,forceSelection:true,
			    			hiddenName: "rtID", width:MatOutWH.fieldWidth,
			    			returnField: [{widgetId:"rtName",propertyName:"repairtimeName"}],
			    			displayField: "repairtimeName",
			    			valueField: "repairtimeIDX",
			    			pageSize: 0,allowBlank:false,
			    			minListWidth: MatOutWH.fieldWidth,
			    			fields: ["repairtimeIDX","repairtimeName"],
		    				entity: 'com.yunda.jxpz.rcrtset.entity.RcRt',
			    			listeners : {
			    				"beforequery" : function(){
			    					//选择修次前先选修程
			                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
			    					if(rcIdx == "" || rcIdx == null){
			    						MyExt.Msg.alert("请先选择修程！");
			    						return false;
			    					}
			    					this.queryName = "repairtimeName";
			        				this.cascadeStore();
			                	}
			    			}
			    		},{
	    					id:"rtName",xtype:"hidden", name:"rtName"
			    	}]
					}
            ]
        },{
        	columnWidth: 0.2,
            items: [
				{ id:"price_0", fieldLabel:"单价",name: "price", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true},
				{ 
	        		allowBlank: false,
	        		id:"getEmp_select",xtype:'Om_Emp_combo', 
	        		fieldLabel: "领件人", hiddenName:'getEmp',
					displayField:'empname', valueField:'empname',
					width:120,
//					minListWidth:120,
					listeners : {   
						"select" : function(me, record, index) {  
							Ext.getCmp("getOrg_select").setDisplayValue(record.get("orgid"),record.get("orgname"));	//领件班组
							Ext.getCmp("getOrg").setValue(record.get("orgname"));
							Ext.getCmp("getOrgSeq").setValue(record.get("orgseq"));
							Ext.getCmp("getEmpID").setValue(record.get("empid"));
						}}
					},{
						id:"getEmpID",xtype:"hidden", name:"getEmpID"
				}
            ]
        },{
        	columnWidth: 0.3,
            items: [
            {
					xtype: 'compositefield', fieldLabel:"低储",combineErrors: false,
			        items: [{ id:"mixQty_0", xtype:"textfield",name: "mixQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
			        	,{
							xtype: 'label',
							text: '高储',
							style: 'height:23px; line-height:23px;'	
						},{id:"maxQty_0", xtype:"textfield",name: "maxQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}]
				},
				{ 
	            		id:"getOrg_select",xtype:'OmOrganizationCustom_comboTree',
	            		editable:false,allowBlank: false,fieldLabel : "领件班组",hiddenName:'getOrgID',
						returnField:[{widgetId:"getOrg",propertyName:"orgname"},
									{widgetId:"getOrgSeq",propertyName:"orgseq"}],
						editable:false
				    },{
				    	id:"getOrg",xtype:"hidden", name:"getOrg"
				},{
				    	id:"getOrgSeq",xtype:"hidden", name:"getOrgSeq"
				}
            	
            ]
        },{
        	columnWidth: 0.5,
            items: [
            	{
					xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
			        items: [{
					        	id:"locationComb_0",fieldLabel:"库位",hiddenName:"locationName",
								xtype:"Base_combo",allowBlank: false,
								entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",						  
								fields:["idx","locationName","status"],
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
        	columnWidth: 0.2,
            items: [
				{id:"locationQty_0",fieldLabel:"库存数量",name:"locationQty", style: 'background:none;',width:MatOutWHTab2.qtyWidth,readOnly:true}
            ]
        },{
        	columnWidth: 0.3,
            items: [
            	{ id:"qty_0",fieldLabel:"出库数量",allowBlank:false,vtype: "positiveInt",width:MatOutWHTab2.qtyWidth,name: "qty"}
            ]
        }],
    	buttons: [{
			text : "确认出库",iconCls : "checkIcon", handler: function(){
				var form = MatOutWH.form.getForm();
			    if (!form.isValid()) return;
			    var data = form.getValues();
			    if(data.qty-data.locationQty > 0){
			    	MyExt.Msg.alert("出库数量不能大于库存数量！");
	                return ;
			    }
			    data.wHType = TYPE_CKDJC ;//出库类型--出库到机车
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
			    
				MatOutWH.loadMask.show();
		        Ext.Ajax.request({
		            url: ctx + '/matOutWH!saveMatOutWH.action',
		            jsonData: data,
		            success: function(response, options){
		              	MatOutWH.loadMask.hide();
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
		                MatOutWH.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
			}
		}]
	});
	
	// 物料入库Tab页
	MatOutWH.tab = new Ext.Container({
		layout: 'form',
		items: [{
			xtype: 'form',style: 'background:red;',
			id:"qcForm_0",
			labelAlign:"right",
			baseCls:"x-plain", border:false,
			labelWidth: MatOutWH.labelWidth,
			height:30,
			style: 'padding:0px 2px 2px 2px;',
			items:[
				{
					id:"matTypeItems_0",xtype: 'radiogroup', fieldLabel: '物料类型', items: MatOutWH.matTypeItems, anchor:'50%',
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
    	                	if(null != whIdx && "" != whIdx && null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
	    	                		//重新加载库位下拉数据
				               		var locationComb = Ext.getCmp("locationComb_0");  
	    	                		locationComb.reset();  
				                	locationComb.clearValue();
    	                			locationComb.queryHql = encodeURIComponent("from WarehouseLocation where recordStatus=0 and locationName in " +
			                		"(select locationName from MatStock where whIdx = '"+whIdx+"' and matCode='"+matCode+"' and matType='"+matTypeItems+"' and qty>0 ) and wareHouseIDX='"+whIdx+"'");
        	                		locationComb.cascadeStore();
			                		//查询库存总量、保有量信息
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 0);
        	                		//查询库位库存信息
        	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 0);
    	                			Ext.getCmp("qty_0").setValue("");
        	                	}
	            		}
	            	}
				}]
		}, MatOutWH.form]
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
						title:"出库到机车",
						layout:"fit",
						frame:true,
						items: [MatOutWH.tab]
					},{
						title:"其他原因出库",
						layout:"fit",
						frame:true,
						items: [MatOutWHTab1.tab]
					},{
						title:"移库出库",
						layout:"fit",
						frame:true,
						items: [MatOutWHTab2.tab]
					}]
			}]
		}]
	});
	MatOutWH.init = function(){
		Ext.getCmp("matTypeItems_0").setValue(objList[ 0 ].dictname);
	};
	MatOutWH.init();
});