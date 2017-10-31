/**
 * 配件上车登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsFixRegister');                       //定义命名空间
	PartsFixRegister.fieldWidth = 160;
	PartsFixRegister.labelWidth = 70;
	PartsFixRegister.form = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsFixRegister.labelWidth,
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
	            columnWidth: 0.33
	    	}
	    },
	    items: [{
        	items:[{
	            items:[{
					xtype: 'compositefield', fieldLabel : '上车车型', combineErrors: false,
		        	items:[{ 
	        			id:"trainType_comb",
	        			xtype: "TrainType_combo",
	        			fieldLabel: "上车车型",
					  	hiddenName: "aboardTrainTypeIdx",
					  	returnField: [{widgetId:"PartsFixRegister_aboardTrainType",propertyName:"shortName"}],
					  	displayField: "shortName", valueField: "typeID",width:100,
					  	pageSize: 20, minListWidth: 200,
					  	editable:true  ,allowBlank: false,
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
				        		}   
				    		}
		              	},{
              				id:"PartsFixRegister_aboardTrainType",xtype:"hidden", name:"aboardTrainType"
		              	},{
			               	xtype: 'button',
			               	text: '在修机车',
	              	 		width: 50,
	              	 		handler: function(){
			               	    jx.jxgc.TrainTypeTreeSelect.createWin();
			               		jx.jxgc.TrainTypeTreeSelect.win.show();
		           	     		}
		           			}]
						},{
			    			id:"rt_comb",
			    			xtype: "Base_combo",
			    			fieldLabel: "上车修次",
			    			hiddenName: "aboardRepairTimeIdx", 
			    			returnField: [{widgetId:"PartsFixRegister_aboardRepairTime",propertyName:"repairtimeName"}],
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
			    						MyExt.Msg.alert("请先选择上车修程！");
			    						return false;
			    					}
			                	}
			    			}
			    		},{
	    					id:"PartsFixRegister_aboardRepairTime",xtype:"hidden", name:"aboardRepairTime"
			    	}]
	        },{
            items:[{ 
            		id:"trainNo_comb",
            		xtype: "TrainNo_combo",	
            		fieldLabel: "上车车号",
				  	hiddenName: "aboardTrainNo",
				  	displayField: "trainNo", 
				  	valueField: "trainNo",
				  	pageSize: 20, minListWidth: 200, 
				  	editable:true,allowBlank: false,
				 	listeners : {
    				"beforequery" : function(){
    					//选择修次前先选车型
                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
    					if(trainTypeId == "" || trainTypeId == null){
    						MyExt.Msg.alert("请先选择上车车型！");
    						return false;
    						}
                		}
	    			}
			  },{ 
			  		id:"fixEmp_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "上车人",hiddenName:'fixEmpId',
					returnField:[{widgetId:"PartsFixRegister_fixEmp",propertyName:"empname"}]
				},{
					id:"PartsFixRegister_fixEmp",xtype:"hidden", name:"fixEmp"
				}]
	        },{
            items:[{
        			id:"rc_comb",
        			xtype: "Base_combo",
        			business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
        			fieldLabel: "上车修程",
        			hiddenName: "aboardRepairClassIdx", 
        			returnField: [{widgetId:"PartsFixRegister_aboardRepairClass",propertyName:"xcName"}],
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
	    						MyExt.Msg.alert("请先选择上车车型！");
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
                    	}
                    }
        		},{
        			id:"PartsFixRegister_aboardRepairClass",xtype:"hidden", name:"aboardRepairClass"
        		},{
        			id:"PartsFixRegister_rdpIdx",xtype:"hidden", name:"rdpIdx"
        		}]
        	}]
    	}]
	});
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
			Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
			Ext.getCmp("PartsFixRegister_aboardTrainType").setValue(node.attributes["trainTypeShortName"]);
			Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
			Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
			Ext.getCmp("PartsFixRegister_aboardRepairClass").setValue(node.attributes["repairClassName"]);
			Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
			Ext.getCmp("PartsFixRegister_aboardRepairTime").setValue(node.attributes["repairTimeName"]);
			Ext.getCmp("PartsFixRegister_rdpIdx").setValue(node.id);//机车兑现单id
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
	    }
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout: "border",
		items:[{
			region:"north",
			layout:"fit",
			height:85,
			frame: true, 
			items:[PartsFixRegister.form]
		},{
			layout: "fit",
			region : 'center',
	        bodyBorder: false,
	        autoScroll : true,
	        id: "nGridId",
	        items : [ PartsFixRegisterDetail.grid ],
	        buttonAlign:"center",
	        buttons:[{
	        	text: "登帐并新增",  handler: function(){PartsFixRegisterDetail.saveFun()}
	    	}]
		}]
	});
	PartsFixRegister.init = function(){
		Ext.getCmp("fixEmp_select").setDisplayValue(empId,empName);
		Ext.getCmp("PartsFixRegister_fixEmp").setValue(empName);
	}();
});