/**
 * 下车配件登记单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsFixRegisterNew');  //定义命名空间
	PartsFixRegisterNew.fieldWidth = 160;
	PartsFixRegisterNew.labelWidth = 60;
	PartsFixRegisterNew.aboardTrainType = "";  //下车车型
	PartsFixRegisterNew.aboardTrainNo = "";  //下车车号
	PartsFixRegisterNew.aboardRepairClassIdx = "";   //下车修程
	
	PartsFixRegisterNew.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", 
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsFixRegisterNew.labelWidth,
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
					xtype: 'compositefield', fieldLabel : '车辆选择', combineErrors: false,
			        items: [{ 
			        	id:"trainType_comb",
			        	hidden:true,
			        	xtype: "Base_combo",
			        	fieldLabel: "上车车型",
						fields:['trainTypeIDX','trainTypeShortName'],
						hiddenName: "aboardTrainTypeIdx",
					  	returnField: [{widgetId:"PartsFixRegisterNew_aboardTrainType",propertyName:"shortName"}],
						displayField: "trainTypeShortName", 
						valueField: "trainTypeIDX",
						width:100,
						pageSize: 20, 
						minListWidth: 200, 
						editable:false,
						readOnly: true,
						allowBlank:false			
                    },{
                     	id:"PartsFixRegisterNew_aboardTrainType",xtype:"hidden", name:"aboardTrainType"
                    },{
		               		xtype: 'button',
		               		text: '请选择',
		               		tooltip:'选择车辆',
		              		width: 90,
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
					fieldLabel: "上车车号",
				  	hiddenName: "aboardTrainNo",
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
	        			fieldLabel: "上车修程",
	        			hiddenName: "aboardRepairClassIdx", 
	        			returnField: [{widgetId:"PartsFixRegisterNew_aboardRepairClass",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: 200,
	        			queryHql: 'from UndertakeRc',
	        			readOnly: true
	        		},{
	        			id:"PartsFixRegisterNew_aboardRepairClass",xtype:"hidden", name:"aboardRepairClass"
	        		}]
	        },{
	            items: [{
		    			id:"rt_comb",
		    			hidden:true,
		    			xtype: "Base_combo",
		    			fieldLabel: "上车修次", 	readOnly: true,
		    			hiddenName: "aboardRepairTimeIdx", 
		    			returnField: [{widgetId:"PartsFixRegisterNew_aboardRepairTime",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,
		    			minListWidth: 200,
		    			fields: ["repairtimeIDX","repairtimeName"],
	    				business: 'rcRt'
		    		},{
		    			id:"PartsFixRegisterNew_aboardRepairTime",xtype:"hidden", name:"aboardRepairTime"
		    		}]
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
	        			id:"PartsFixRegisterNew_rdpIdx",xtype:"hidden", name:"rdpIdx"
	        }]
	    }]
	});
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
			Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
			Ext.getCmp("PartsFixRegisterNew_aboardTrainType").setValue(node.attributes["trainTypeShortName"]);
			Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
			Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
			Ext.getCmp("PartsFixRegisterNew_aboardRepairClass").setValue(node.attributes["repairClassName"]);
			Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
			Ext.getCmp("PartsFixRegisterNew_aboardRepairTime").setValue(node.attributes["repairTimeName"]);
			Ext.getCmp("PartsFixRegisterNew_rdpIdx").setValue(node.id);//机车兑现单id
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
	        PartsFixRegisterNew.aboardTrainTypeIdx = node.attributes["trainTypeIDX"] ;
	        PartsFixRegisterNew.aboardTrainNo = node.attributes["trainNo"] ;
	        PartsFixRegisterNew.aboardRepairClassIdx = node.attributes["repairClassIDX"] ;
	        
	        // 设置显示值
	        Ext.getCmp("display_field").setValue(node.attributes["trainTypeShortName"]+""+node.attributes["trainNo"]+"  "+node.attributes["repairClassName"]+""+node.attributes["repairTimeName"]);
	        
	        
	        PartsFixRegisterNewDetail.rdpIdx = node.id ;
	        PartsFixRegisterNewDetail.store.load();
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
			items:[PartsFixRegisterNew.form]
		},{
			title:"登记明细",region : 'center', layout : 'border', bodyBorder: false,xtype:'fieldset',
			margins:'5 10 0 10', 
			items:[{
					layout: "fit",
					region : 'center',
			        bodyBorder: false,
			        autoScroll : true,
			        id: "nGridId",
			        items : [ PartsFixRegisterNewDetail.grid ]
				},{
					layout: "fit",
					region : 'south',
			        bodyBorder: false,
			        height:40,
			        buttonAlign:"center",
				    buttons:[{text: "登记",  handler: function(){PartsFixRegisterNewDetail.saveFun()}}] 
				}]
		}]
	});
});