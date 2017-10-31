Ext.onReady(function(){
	
	if(typeof(PlanningDetail) === 'undefined')
		Ext.ns("PlanningDetail");
	
	var form, win, initialized = false;
	
	function createForm(){
		form = defineFormPanel({
			labelWidth: 80,
			rows:[{
				cw: .5,
				cols: [{
					xtype: "TrainType_combo",
					fieldLabel: "车型",
					hiddenName: "unloadTrainTypeIdx",
					// returnField: [{widgetId:"unloadTrainType",propertyName:"shortName"}],
					displayField: "shortName", valueField: "typeID",
					pageSize: 20, minListWidth: 200,
					editable:false,
					listeners: {   
						"select" : function() {   
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
					fieldLabel: "车号",
					name: "trainNo"
				},{
					id:"rc_comb",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "修程",
					hiddenName: "unloadRepairClassIdx", 
//						returnField: [{widgetId:"unloadRepairClass",propertyName:"xcName"}],
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					editable:false,
					listeners : {  
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
	    			id:"rt_comb",
	    			xtype: "Base_combo",
	    			fieldLabel: "修次",
	    			hiddenName: "repairtimeIDX", 
	    			displayField: "repairtimeName",
	    			valueField: "repairtimeIDX",
	    			pageSize: 0,
	    			minListWidth: 200,width: 120,
	    			fields: ["repairtimeIDX","repairtimeName"],
    				business: 'rcRt',
	    			listeners : {
	    				"beforequery" : function(){
	    					//选择修次前先选修程
	                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
	    					if(rcIdx == "" || rcIdx == null){
	    						MyExt.Msg.alert("请先选择修程！");
	    						return false;
	    					}
	                	}
	    			}
	    		},{
	    			id: "comboTree_bId", xtype: "BureauSelect_comboTree",
	    			hiddenName: "bId", fieldLabel: "配属局", allowBlank:false,
	    		    selectNodeModel: "leaf" ,
	    		    returnField: [{widgetId: "bName_add", propertyName: "text"},
	    			  			  {widgetId: "bShortName_add", propertyName: "orgname"}],
	    		    listeners : {
	    			  	"select" : function() {
	    			  		Ext.getCmp("comboTree_dId").reset();
	    	                Ext.getCmp("comboTree_dId").clearValue();
	    			  		Ext.getCmp("comboTree_dId").orgid = this.getValue();
	    			  		Ext.getCmp("comboTree_dId").orgname = this.lastSelectionText;
	    			  	}
	    			  }
	    		},{
	    			id: "comboTree_dId", xtype: "DeportSelect_comboTree",
	    			hiddenName: "dId", fieldLabel: "配属段", allowBlank:false,
	    		    selectNodeModel: "leaf" ,
	    		    returnField: [{widgetId: "dName_add", propertyName: "text"},
	    			  			  {widgetId: "dShortName_add", propertyName: "orgname"}],
	    		    listeners : {
	    				"beforequery" : function(){
	    					//选择段前先选局
	    					var comboTree_bId =  Ext.getCmp("comboTree_bId").getValue();
	    					if(comboTree_bId == "" || comboTree_bId == null){
	    						MyExt.Msg.alert("请先选择配属局！");
	    						return false;
	    					}
	    				}
	    			}
	    		},{
					fieldLabel:'委修单位',
					id: "comboTree_usedDId",
					xtype: "DominateSection_comboTree",
					hiddenName: "usedDId",
					orgid: "0",
					orgname: orgRootName, 
					selectNodeModel: "leaf",
					disabled:false/*,
					returnField: [{widgetId: "usedDName", propertyName: "text"},
			  			  {widgetId: "usedDShortName", propertyName: "orgname"}]*/
				},{
					fieldLabel: "走行公里",
					name: "trainNo",
					xtype: "numberfield",
					maxLength: 8
				},{
					fieldLabel: "计划开始日期",
					name: "trainNo",
					xtype: "my97date"
				},{
					fieldLabel: "计划结束日期",
					name: "trainNo",
					xtype: "my97date"
				}]
			},{
				cw: 1,
				cols: [{
					xtype: "textarea",
					fieldLabel: "备注",
					name: "remark",
					maxLength: 100
				}]
			}]
		}, {
			labelAlign: "right",
			border: true
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "计划明细编辑",width:500, height:270, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: false, resizable: false,
			items: form,
			buttons: [{
				text: "保存",
				iconCls: "saveIcon",
				handler: function(){
				
				}
			},{
				text: "关闭",
				iconCls: "closeIcon",
				handler: function(){
					win.hide();
				}
			}]
		});
	}
	
	function initialize(){
		createForm();
		createWin();
		initialized = true;
	}
	
	PlanningDetail.showWin = function(_record){		
		if(initialized == false) initialize();
		win.show();
	}
});