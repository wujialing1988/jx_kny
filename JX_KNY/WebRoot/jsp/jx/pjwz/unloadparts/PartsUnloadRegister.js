/**
 * 下车配件登记单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsUnloadRegister');  //定义命名空间
	PartsUnloadRegister.fieldWidth = 160;
	PartsUnloadRegister.labelWidth = 70;
	PartsUnloadRegister.unloadTrainTypeIdx = "";  //下车车型
	PartsUnloadRegister.unloadTrainNo = "";  //下车车号
	PartsUnloadRegister.unloadRepairClassIdx = "";   //下车修程
	
	PartsUnloadRegister.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", 
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsUnloadRegister.labelWidth,
	    defaults:{
	    	 xtype: "panel",
	    	 border: false,
	    	 baseCls: "x-plain",
	    	 layout: "column",
	    	 align: "center",
	    	 defaults:{
	    	 	 baseCls:"x-plain",
	    	 	 align:"center",
	    	 	 layout:"form", 
	    	 	 defaultType:"textfield",
	    	 	 columnWidth: 0.25
	    	 }
	    },
	    items: [{
	        items: [{
	            items: [{
		        	id:"orgDic_comb",
		        	xtype: "Base_combo",
		        	fieldLabel: "接收部门",
					fields:['orgid','orgSeq','orgName'],
					hiddenName: "takeOverDeptId",
					business: 'orgDicItem',
					queryParams: {dictTypeId: 'accountorg'},
					returnField:[
						 {widgetId:"PartsUnloadRegister_takeOverDept",propertyName:"orgName"},
				  		 {widgetId:"PartsUnloadRegister_takeOverDeptOrgseq",propertyName:"orgSeq"}
				  	], 
				  	idProperty: 'orgid',
					displayField: "orgName", 
					valueField: "orgid",
					pageSize: 20, 
					minListWidth: 200, 
					editable:false,
					allowBlank: false,
					isAll:true
	            },{
					id:"PartsUnloadRegister_takeOverDept",xtype:"hidden", name:"takeOverDept"
				},{
					id:"PartsUnloadRegister_takeOverDeptOrgseq",xtype:"hidden", name:"takeOverDeptOrgseq"
				},{
					xtype: 'compositefield', fieldLabel : '下车车型', combineErrors: false,
			        items: [{ 
			        	id:"trainType_comb",
			        	xtype: "Base_combo",
			        	fieldLabel: "下车车型",
						entity:'com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType',
						fields:['trainTypeIDX','trainTypeShortName'],
						hiddenName: "unloadTrainTypeIdx",
						returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"trainTypeShortName"}],
						displayField: "trainTypeShortName", 
						valueField: "trainTypeIDX",
						width:100,
						pageSize: 20, 
						minListWidth: 200, 
						queryHql: 'from UndertakeTrainType where recordStatus=0',
						editable:false,
						allowBlank: false,
						listeners : {   
				        	"select" : function() {   
				            	//重新加载车号下拉数据
				                var trainNo_comb = Ext.getCmp("trainNo_comb");   
				                trainNo_comb.reset();  
				                trainNo_comb.clearValue(); 
				                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
				                trainNo_comb.cascadeStore();
				                //重新加载修程下拉数据
				                var rc_comb = Ext.getCmp("rc_comb");
				                rc_comb.reset();
				                rc_comb.clearValue();
				                rc_comb.getStore().removeAll();
				                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
				                rc_comb.cascadeStore();
								//重新加载修次数据
        	                	var rt_comb = Ext.getCmp("rt_comb");
        	                	rt_comb.clearValue();
        	                 	rt_comb.reset();
        	                 	rt_comb.getStore().removeAll();
        	                 	rt_comb.cascadeStore();
        	                 	PartsUnloadRegister.unloadTrainTypeIdx = this.getValue() ;
        	                 	PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.load();
        	                 	PartsUnloadRegisterDetail.form.findByType("PartsTypeTreeSelect")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsName")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].setValue("");
						        Ext.getDom("spUnit").innerHTML="";
						        Ext.getCmp("checkQty").setValue("");
							    Ext.getCmp("unCheckQty").setValue("");
				        		}   
				    		  }
		                    },{
		                     	id:"PartsUnloadRegister_unloadTrainType",xtype:"hidden", name:"unloadTrainType"
		                    },{
			               		xtype: 'button',
			               		text: '在修机车',
			              		width: 45,
			              		handler: function(){
			               	    	jx.jxgc.TrainTypeTreeSelect.createWin();
			               			jx.jxgc.TrainTypeTreeSelect.win.show();
			           	   		}
		           		}]
				}]
	        },{
	            items: [{ 
	                 id:"takeOver_select",
	                 xtype:'OmEmployee_SelectWin',
	                 editable:false,
	                 allowBlank: false,
	                 fieldLabel : "接收人",
	                 hiddenName:'takeOverEmpId',
					 returnField:[{widgetId:"PartsUnloadRegister_takeOverEmp",propertyName:"empname"}]
					},{
						id:"PartsUnloadRegister_takeOverEmp",xtype:"hidden", name:"takeOverEmp"
					},{ 
						id:"trainNo_comb",
						xtype: "TrainNo_combo",
						fieldLabel: "下车车号",
					  	hiddenName: "unloadTrainNo",
					  	displayField: "trainNo",
					  	valueField: "trainNo",
					  	pageSize: 20,
					  	minListWidth: 200, 
					 	editable:true,
					 	allowBlank: false,
					  	listeners : {
	    				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择下车车型！");
	    						return false;
	    					}
	                	},
	                	"select" : function() {   
	    	                 	PartsUnloadRegister.unloadTrainNo = this.getValue() ;
	    	                 	PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.load();
	    	                 	PartsUnloadRegisterDetail.form.findByType("PartsTypeTreeSelect")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsName")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].setValue("");
						        Ext.getDom("spUnit").innerHTML="";
						        Ext.getCmp("checkQty").setValue("");
							    Ext.getCmp("unCheckQty").setValue("");
				        	} 
	    				}
				 }]
	        },{
	            items: [{ 
		            	id:"handOver_select",
		            	xtype:'OmEmployee_SelectWin',
		            	editable:false,allowBlank: false,
		            	fieldLabel : "交件人",
		            	hiddenName:'handOverEmpId',
						returnField:[{widgetId:"PartsUnloadRegister_handOverEmp",propertyName:"empname"}]
					},{
						id:"PartsUnloadRegister_handOverEmp",xtype:"hidden", name:"handOverEmp"
					},{
	        			id:"rc_comb",
	        			xtype: "Base_combo",
	        			business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
	        			fieldLabel: "下车修程",
	        			hiddenName: "unloadRepairClassIdx", 
	        			returnField: [{widgetId:"PartsUnloadRegister_unloadRepairClass",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: 200,
	        			queryHql: 'from UndertakeRc',
	        			allowBlank: false,
	        			listeners : {  
	        				"beforequery" : function(){
		    					//选择修次前先选车型
		                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
		    					if(trainTypeId == "" || trainTypeId == null){
		    						MyExt.Msg.alert("请先选择下车车型！");
		    						return false;
		    					}
		                	},
	                    	"select" : function() {   
	                    		//重新加载修次数据
	                        	var rt_comb = Ext.getCmp("rt_comb");
	                        	rt_comb.clearValue();
	                         	rt_comb.reset();
	                            rt_comb.queryParams = {"rcIDX":this.getValue()};
	                            rt_comb.cascadeStore();  
	    	                 	PartsUnloadRegister.unloadRepairClassIdx = this.getValue() ;
	    	                 	PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.load();
	    	                 	PartsUnloadRegisterDetail.form.findByType("PartsTypeTreeSelect")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsName")[0].setValue("");
							    PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].setValue("");
						        Ext.getDom("spUnit").innerHTML="";
						        Ext.getCmp("checkQty").setValue("");
							    Ext.getCmp("unCheckQty").setValue("");
	                    	}
	                    }
	        		},{
	        			id:"PartsUnloadRegister_unloadRepairClass",xtype:"hidden", name:"unloadRepairClass"
	        		}]
	        },{
	            items: [{ 
		            	id:"takeOverTime",name: "takeOverTime", 
		            	fieldLabel: "收件日期",
		            	allowBlank: false, 
		            	xtype: "my97date",
		            	format: "Y-m-d"  
					},{
		    			id:"rt_comb",
		    			xtype: "Base_combo",
		    			fieldLabel: "下车修次",
		    			hiddenName: "unloadRepairTimeIdx", 
		    			returnField: [{widgetId:"PartsUnloadRegister_unloadRepairTime",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,
		    			minListWidth: 200,
		    			fields: ["repairtimeIDX","repairtimeName"],
	    				business: 'rcRt',
		    			listeners : {
		    				"beforequery" : function(){
		    					//选择修次前先选修程
		                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
		    					if(rcIdx == "" || rcIdx == null){
		    						MyExt.Msg.alert("请先选择下车修程！");
		    						return false;
		    					}
		                	}
		    			}
		    		},{
		    			id:"PartsUnloadRegister_unloadRepairTime",xtype:"hidden", name:"unloadRepairTime"
		    		}]
	        	},{
	        			id:"PartsUnloadRegister_rdpIdx",xtype:"hidden", name:"rdpIdx"
	        }]
	    }]
	});
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
			Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
			Ext.getCmp("PartsUnloadRegister_unloadTrainType").setValue(node.attributes["trainTypeShortName"]);
			Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
			Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
			Ext.getCmp("PartsUnloadRegister_unloadRepairClass").setValue(node.attributes["repairClassName"]);
			Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
			Ext.getCmp("PartsUnloadRegister_unloadRepairTime").setValue(node.attributes["repairTimeName"]);
			Ext.getCmp("PartsUnloadRegister_rdpIdx").setValue(node.id);//机车兑现单id
			//重新加载车号下拉数据
	        var trainNo_comb = Ext.getCmp("trainNo_comb");   
	        trainNo_comb.queryParams = {"trainTypeIDX":node.attributes["trainTypeIDX"]};
	        trainNo_comb.cascadeStore();
	        //重新加载修程下拉数据
	        var rc_comb = Ext.getCmp("rc_comb");
	        rc_comb.queryParams = {"TrainTypeIdx":node.attributes["trainTypeIDX"]};
	        rc_comb.cascadeStore();
			//重新加载修次数据
	    	var rt_comb = Ext.getCmp("rt_comb");
	     	rt_comb.queryParams = {"rcIDX":node.attributes["repairClassIDX"]};
	        rt_comb.cascadeStore(); 
	        PartsUnloadRegister.unloadTrainTypeIdx = node.attributes["trainTypeIDX"] ;
	        PartsUnloadRegister.unloadTrainNo = node.attributes["trainNo"] ;
	        PartsUnloadRegister.unloadRepairClassIdx = node.attributes["repairClassIDX"] ;
	        PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.load();
	        
	        PartsUnloadRegisterDetail.form.findByType("PartsTypeTreeSelect")[0].setValue("");
		    PartsUnloadRegisterDetail.form.find("name","partsName")[0].setValue("");
		    PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].setValue("");
	        Ext.getDom("spUnit").innerHTML="";
	        Ext.getCmp("checkQty").setValue("");
		    Ext.getCmp("unCheckQty").setValue("");
	    }
	//页面自适应布局
	var viewport = new Ext.Viewport({
		 layout:'border',frame:true,
		 items:[{
					region:"north",
					layout:"fit",
					height:100,
					split:true,
					maxSize:70,
					minSize:70,
					frame: true,bodyBorder: false, 
					items:[PartsUnloadRegister.form]
				},{
					title:"登记明细",region : 'center', layout : 'border', bodyBorder: false,xtype:'fieldset',
					margins:'5 10 0 10', 
					items:[{
							layout: "fit",
							region : 'north',
							height:70,
							frame: true,
					        bodyBorder: false,
					        items : [ PartsUnloadRegisterDetail.form ]
						},{
							layout: "fit",
							region : 'center',
					        bodyBorder: false,
					        autoScroll : true,
					        id: "nGridId",
					        items : [ PartsUnloadRegisterDetail.grid ]
						},{
							layout: "fit",
							region : 'south',
					        bodyBorder: false,
					        height:40,
					        buttonAlign:"center",
						    buttons:[{text: "登帐并新增",  handler: function(){PartsUnloadRegisterDetail.saveFun()}}] 
						}]
				},{
					title : "配件登记信息查询",
					region : 'south', layout:"fit",
					height:240, collapsible :true,collapsed :true,frame:true,
					items:[PartsUnloadRegisterDetail.trainTypeToPartsGrid]
			}]
	});
	PartsUnloadRegister.init = function(){
		Ext.getCmp("takeOver_select").setDisplayValue(empId,empName);
		Ext.getCmp("PartsUnloadRegister_takeOverEmp").setValue(empName);
/*		Ext.getCmp("OmOrganizationCustom_comboTree_Id").setDisplayValue(teamOrgId , teamOrgName);
		Ext.getCmp("PartsUnloadRegister_takeOverDept").setValue(teamOrgName);
		Ext.getCmp("PartsUnloadRegister_takeOverDeptOrgseq").setValue(teamOrgSeq);*/
	};
	PartsUnloadRegister.init();
});