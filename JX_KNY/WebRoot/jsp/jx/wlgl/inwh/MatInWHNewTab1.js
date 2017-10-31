/**
 * 机车退回入库单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatInWHNewTab1');                       //定义命名空间
	MatInWHNewTab1.matTypeItems = [] ;
	MatInWHNewTab1.labelWidth = 80;
	MatInWHNewTab1.fieldWidth = 70;
	MatInWHNewTab1.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 自动生成“物料类型”radiogroup内容
	MatInWHNewTab1.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	//        editor.id = "checkItemId";
	//        editor.xtype = "radiogroup";
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatInWHNewTab1.matTypeItems.push(editor);
		}
	}();
	MatInWHNewTab1.form = new Ext.form.FormPanel({
	    style: "padding:2px",		labelWidth: MatInWHNewTab1.labelWidth,
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
					        		//重新加载库位下拉数据
					                var locationComb = Ext.getCmp("locationComb_1");   
					                locationComb.reset();  
					                locationComb.clearValue(); 
					                locationComb.queryParams = {"wareHouseIDX":whIdx};
					                locationComb.cascadeStore();
									//查询库存信息、保有量信息
					                if(null != Ext.getCmp("matTypeItems_1").getValue()){
	        	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
	        	                	}
	        	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
	        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
	        	                	}
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
				},
				{
			        	fieldLabel:"物料描述",hiddenName:"matDesc",
						xtype:"Base_combo",allowBlank: false,
						editable: true, typeAhead: false,forceSelection:true,
						entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
						fields:["matCode","matDesc","unit","price"],anchor:'60%',
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
        	                	//查询库存信息
				                if(null != Ext.getCmp("matTypeItems_1").getValue()){
        	                	   matTypeItems = Ext.getCmp("matTypeItems_1").getValue().inputValue;
        	                	}
        	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
        	                	}
				        		},
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
        	columnWidth: 0.25,
            items: [
				{id:"totalQty_1", fieldLabel:"库存总量",name:"totalQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true},
				{
					xtype: 'compositefield', fieldLabel : '车号', combineErrors: false,
			        items: [{
								id:"trainType_comb",xtype: "Base_combo", fieldLabel: "车型",
								hiddenName: "trainTypeIDX", 
								editable: true, typeAhead: false,forceSelection:true,
								entity:'com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType',
								fields:['trainTypeIDX','trainTypeShortName'],
								returnField: [{widgetId:"trainTypeShortName",propertyName:"trainTypeShortName"}],
								displayField: "trainTypeShortName", valueField: "trainTypeIDX",
								pageSize: 0, minListWidth: MatInWHNewTab1.fieldWidth, width:MatInWHNewTab1.fieldWidth,
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
						        	},
						        	beforequery: function( queryEvent ) {
					        			this.queryName = "trainTypeShortName";
					        			this.cascadeStore();
					        		}
						    	}
							},{ id:"trainTypeShortName",xtype:"hidden", name:"trainTypeShortName" },
				        	{
									id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
									hiddenName: "trainNo", width:MatInWHNewTab1.fieldWidth,
									editable: true, typeAhead: false,forceSelection:true,
									displayField: "trainNo", valueField: "trainNo",
									pageSize: 20, allowBlank:false ,
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
            	{ id:"unit_1", fieldLabel:"计量单位",name: "unit", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true},
            	{
					xtype: 'compositefield', fieldLabel : '修程', combineErrors: false,
			        items: [{
        			id:"rc_comb",
        			xtype: "Base_combo",
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
        			fieldLabel: "修程",
        			hiddenName: "xcID", 
        			returnField: [{widgetId:"xcName",propertyName:"xcName"}],
        			displayField: "xcName",
        			valueField: "xcID",editable: true, typeAhead: false,forceSelection:true,
        			pageSize: 20, minListWidth: MatInWHNewTab1.fieldWidth,width:MatInWHNewTab1.fieldWidth,
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
		    			fieldLabel: "修次",
		    			hiddenName: "rtID", 
		    			returnField: [{widgetId:"rtName",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,allowBlank:false,editable: true, typeAhead: false,forceSelection:true,
		    			minListWidth: MatInWHNewTab1.fieldWidth,width:MatInWHNewTab1.fieldWidth,
		    			fields: ["repairtimeIDX","repairtimeName"],
		    			entity: 'com.yunda.jxpz.rcrtset.entity.RcRt',
		    			listeners : {
		    				"beforequery" : function(){
		    					//选择修次前先选修程
		                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
		    					if(rcIdx == "" || rcIdx == null){
		    						MyExt.Msg.alert("请先选择修程！");
		    						return false;
		    					};
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
				{ id:"price_1", fieldLabel:"单价",name: "price", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true},
				{ 
            		id:"handOver_select",xtype:'Om_Emp_combo',
            		fieldLabel : "交件人",hiddenName:'handOverEmp',
					displayField:'empname',valueField:'empname', 
					width:120,
					listeners : {   
						"select" : function(me, record, index) { 
						Ext.getCmp("handOverOrg_select").setDisplayValue(record.get("orgid"),record.get("orgname"));//交件班组
						Ext.getCmp("handOverOrg").setValue(record.get("orgname"));
						Ext.getCmp("handOverOrgSeq").setValue(record.get("orgseq"));
						Ext.getCmp("handOverEmpID").setValue(record.get("empid"));
					}}
				},{
					id:"handOverEmpID",xtype:"hidden", name:"handOverEmpID"
			}
            ]
        },{
        	columnWidth: 0.3,
            items: [
            {
					xtype: 'compositefield', fieldLabel:"低储",combineErrors: false,
			        items: [{ id:"mixQty_1", xtype:"textfield",name: "mixQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}
			        	,{
							xtype: 'label',
							text: '高储',
							style: 'height:23px; line-height:23px;'	
						},{id:"maxQty_1", xtype:"textfield",name: "maxQty", style: 'background:none;',width:MatInWHNewTab2.qtyWidth,readOnly:true}]
				},
				{ 
	            		id:"handOverOrg_select",xtype:'OmOrganizationCustom_comboTree',
	            		editable:false,allowBlank: false,fieldLabel : "交件班组",hiddenName:'handOverOrgID',
						returnField:[{widgetId:"handOverOrg",propertyName:"orgname"}],
						editable:false
				    },{
				    	id:"handOverOrg",xtype:"hidden", name:"handOverOrg"
				},{
				    	id:"handOverOrgSeq",xtype:"hidden", name:"handOverOrgSeq"
				}
            	
            ]
        },{
        	columnWidth: 0.5,
            items: [
            	{
					xtype: 'compositefield', fieldLabel : '库位', combineErrors: false,
			        items: [{
					        	id:"locationComb_1",fieldLabel:"库位",hiddenName:"locationName",
								xtype:"Base_combo",allowBlank: false,editable: true, typeAhead: true,
								entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",						  
								fields:["idx","locationName","status"],forceSelection:true,
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
				    					this.queryName = "locationName";
				        				this.cascadeStore();
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
				}
            ]
        },{
        	columnWidth: 0.2,
            items: [
            	{id:"locationQty_1",fieldLabel:"库存数量",name:"locationQty",width:MatInWHNewTab2.qtyWidth, style: 'background:none;',readOnly:true}
            ]
        },{
        	columnWidth: 0.3,
            items: [
            	{ id:"qty_1",fieldLabel:"入库数量",allowBlank:false,vtype: "positiveInt",width:MatInWHNewTab2.qtyWidth,name: "qty"}
            ]
        }],
    	buttons: [{
			text : "确认入库",iconCls : "checkIcon", handler: function(){
				var form = MatInWHNewTab1.form.getForm();
			    if (!form.isValid()) return;
			    var data = form.getValues();
			    data.inWhType = TYPE_JCTH ;//入库类型--机车退回入库
			    data.matType = Ext.getCmp("matTypeItems_1").getValue().inputValue ;
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
			    
				MatInWHNewTab1.loadMask.show();
		        Ext.Ajax.request({
		            url: ctx + '/matInWHNew!saveMatInWHNew.action',
		            jsonData: data,
		            success: function(response, options){
		              	MatInWHNewTab1.loadMask.hide();
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
		                MatInWHNewTab1.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
			}
		}]
	});
	
	// 物料入库Tab页
	MatInWHNewTab1.tab = new Ext.Container({
		layout: 'form',
		items: [{
			xtype: 'form', style: 'background:red;',
			id:"qcForm_1",
			labelAlign:"right",
			baseCls:"x-plain", border:false,
			labelWidth: MatInWHNewTab1.labelWidth,
			height:28,
			style: 'padding:0px 2px 2px 2px;',
			items:[
				{
					id:"matTypeItems_1",xtype: 'radiogroup', fieldLabel: '物料类型', items: MatInWHNewTab1.matTypeItems, anchor:'50%',
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
    	                	//查询库存总量、保有量信息
    	                	if(null != whIdx && "" != whIdx && null != matTypeItems && "" != matTypeItems){
        	                		MatOutWHFun.selectTotalQty(whIdx, matCode, matTypeItems, 1);
        	                	}
        	                //查询库位库存信息
    	                	if(null != matCode && "" != matCode && null != matTypeItems && "" != matTypeItems){
    	                		MatOutWHFun.selectLocationQty(whIdx, matCode, matTypeItems, locationName, 1);
    	                	}
	            		}
	                }
				}]
		}, MatInWHNewTab1.form]
	});
	MatInWHNewTab1.init = function(){
		Ext.getCmp("matTypeItems_1").setValue(objList[ 0 ].dictname);
	};
	MatInWHNewTab1.init();
});