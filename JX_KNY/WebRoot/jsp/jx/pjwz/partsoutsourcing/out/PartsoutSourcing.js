/**
 * 下车配件登记单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsoutSourcing');  //定义命名空间
	PartsoutSourcing.fieldWidth = 160;
	PartsoutSourcing.labelWidth = 70;
	PartsoutSourcing.unloadTrainTypeIdx = "";  //下车车型
	PartsoutSourcing.unloadTrainNo = "";  //下车车号
	PartsoutSourcing.unloadRepairClassIdx = "";   //下车修程
	
	PartsoutSourcing.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", 
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsoutSourcing.labelWidth,
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
	    	 	 columnWidth: 0.20
	    	 }
	    },
	    items: [{
	        items: [{
	            items: [{
					xtype: 'compositefield', fieldLabel : '下车车型', combineErrors: false,
			        items: [{ 
			        	id:"trainType_comb",
			        	hidden:true,
			        	xtype: "Base_combo",
			        	fieldLabel: "下车车型",
						entity:'com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType',
						fields:['trainTypeIDX','trainTypeShortName'],
						hiddenName: "unloadTrainTypeIdx",
						returnField: [{widgetId:"PartsoutSourcing_unloadTrainType",propertyName:"trainTypeShortName"}],
						displayField: "trainTypeShortName", 
						valueField: "trainTypeIDX",
						width:100,
						pageSize: 20, 
						minListWidth: 200, 
						queryHql: 'from UndertakeTrainType where recordStatus=0',
						editable:false,
						readOnly: true,
						allowBlank:false
		                    },{
		                     	id:"PartsoutSourcing_unloadTrainType",xtype:"hidden", name:"unloadTrainType"
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
						id:"trainNo_comb",
						hidden:true,
						xtype: "TrainNo_combo",
						fieldLabel: "下车车号",
					  	hiddenName: "unloadTrainNo",
					  	displayField: "trainNo",
					  	valueField: "trainNo",
					  	pageSize: 20,
					  	minListWidth: 200, 
					 	editable:true,
					 	readOnly: true
				 }]
	        },{
	            items: [{
	        			id:"rc_comb",
	        			hidden:true,
	        			xtype: "Base_combo",
	        			business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
	        			fieldLabel: "下车修程",
	        			hiddenName: "unloadRepairClassIdx", 
	        			returnField: [{widgetId:"PartsoutSourcing_unloadRepairClass",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: 200,
	        			queryHql: 'from UndertakeRc',
	        			readOnly: true
	        		},{
	        			id:"PartsoutSourcing_unloadRepairClass",xtype:"hidden", name:"unloadRepairClass"
	        		}]
	        },{
	            items: [{
		    			id:"rt_comb",
		    			hidden:true,
		    			xtype: "Base_combo",
		    			fieldLabel: "下车修次",
		    			hiddenName: "unloadRepairTimeIdx", 
		    			returnField: [{widgetId:"PartsoutSourcing_unloadRepairTime",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,
		    			minListWidth: 200,
		    			fields: ["repairtimeIDX","repairtimeName"],
	    				business: 'rcRt',
	    				readOnly: true
		    		},{
		    			id:"PartsoutSourcing_unloadRepairTime",xtype:"hidden", name:"unloadRepairTime"
		    		}]
	        	},{
	        		items: [
	        		{
	        		 xtype:"button",
                     fieldLabel: '超范围登记',
                     text: '选择...',
				  	 editable:false,
				  	 handler: function() {
							   var trainType_comb = Ext.getCmp("trainType_comb").getValue();
				           	   if(trainType_comb==""){
				           	   	MyExt.Msg.alert("请选择车型");
				           	      return ;
				           	   }
				           	   PartsUnloadRegisterForOutSourcing.grid.store.load();
				           	   PartsUnloadRegisterForOutSourcing.batchWin.show();
					    }
	        		}
	        		]
	        	},{
	            items: [{ 
						id:"display_field",
						xtype: "displayfield",
						//fieldLabel: "已选车",
						style: {
				            "font-weight":"bold"
				        }
				 }]
	        },{
	        			id:"PartsoutSourcing_rdpIdx",xtype:"hidden", name:"rdpIdx"
	        }]
	    }]
	});
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
			Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
			Ext.getCmp("PartsoutSourcing_unloadTrainType").setValue(node.attributes["trainTypeShortName"]);
			Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
			Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
			Ext.getCmp("PartsoutSourcing_unloadRepairClass").setValue(node.attributes["repairClassName"]);
			Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
			Ext.getCmp("PartsoutSourcing_unloadRepairTime").setValue(node.attributes["repairTimeName"]);
			Ext.getCmp("PartsoutSourcing_rdpIdx").setValue(node.id);//机车兑现单id
			PartsUnloadRegisterForOutSourcing.rdpIdx = node.id ;
			// 设置显示值
	        Ext.getCmp("display_field").setValue(node.attributes["trainTypeShortName"]+""+node.attributes["trainNo"]+"  "+node.attributes["repairClassName"]+""+node.attributes["repairTimeName"]);
	        
	        PartsoutSourcingDetail.rdpIdx = node.id ;
	        PartsoutSourcingDetail.store.load();
	        
	    };
	    
	//页面自适应布局
	var viewport = new Ext.Viewport({
		 layout:'border',frame:true,
		 items:[{
					region:"north",
					layout:"fit",
					height:60,
					split:true,
					maxSize:70,
					minSize:70,
					frame: true,bodyBorder: false, 
					items:[PartsoutSourcing.form]
				},{
					title:"登记明细",region : 'center', layout : 'border', bodyBorder: false,xtype:'fieldset',
					margins:'5 10 0 10', 
					items:[{
							layout: "fit",
							region : 'center',
					        bodyBorder: false,
					        autoScroll : true,
					        id: "nGridId",
					        items : [ PartsoutSourcingDetail.grid ]
						},{
							layout: "fit",
							region : 'south',
					        bodyBorder: false,
					        height:40,
					        buttonAlign:"center",
						    buttons:[{text: "登记",  handler: function(){PartsoutSourcingDetail.saveFun()}}] 
						}]
				}]
	});
});