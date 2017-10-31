/**
 * 提票查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FaultTicketAnasys');                       //定义命名空间
	FaultTicketAnasys.searchParam = {};
	/*** 查询表单 start ***/
	FaultTicketAnasys.searchLabelWidth = 70;
	FaultTicketAnasys.fieldWidth = 180;
	FaultTicketAnasys.searchAnchor = '95%';
	FaultTicketAnasys.isChecked = 1;
	FaultTicketAnasys.workPlanStatus = "";

	// 【统计】按钮点击触发的函数
	FaultTicketAnasys.statisticsFn = function(value) {
		var form = FaultTicketAnasys.searchForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var data = form.getValues();
		var url = "";
		var reportUrl = "/jxgc/FaultTicketAnasys.cpt";
		var dataUrl = "";
		var startTime = data.startTime;			// 开始时间
		var endTime = data.endTime;		// 时间
		var ticketEmp = data.ticketEmp;
		var isFinished = data.isFinished;
		var isAccount = data.isAccount;
		dataUrl = reportUrl + "&start_time=" + startTime + "&end_time=" + endTime + "&ticketEmp=" + ticketEmp
				+ "&isFinished=" + isFinished+ "&isAccount=" + isAccount;  
		if(!Ext.isEmpty(FaultTicketAnasys.workPlanStatus)){
			dataUrl+= "&workPlanStatus=" + FaultTicketAnasys.workPlanStatus;
		}
	
		var type="";
//		if("undefined"!=value && null != value ){
			switch(FaultTicketAnasys.isChecked){
				case 1: if("" != data.faultType){type = "&type=" + data.faultType;} break;
				case 2: if("" != data.flmc){type = "&flmc=" + data.flmc;} break;
				case 3: if("" != data.repairTeamName){type = "&repairTeamName=" + data.repairTeamName;} break;
				case 4: if("" != data.reasonAnalysisID){
					var bqArray = data.reasonAnalysisID.split(";");
					var reasonAnalysisID = "";
						for(var i = 0; i< bqArray.length;i++){
							reasonAnalysisID += " and t.REASON_ANALYSIS like %27%25" + bqArray[i]+ "%25%27"
						}
					type = "&reasonAnalysisID=" + reasonAnalysisID;
					type += "&reasonAnalysis=" + data.reasonAnalysisID;
				}
				break;
			}
//		}
		dataUrl += type;
		url = getReportEffectivePath(dataUrl);
		var h = jQuery("#report").height();
		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
	};
	
	
	FaultTicketAnasys.searchForm = new Ext.form.FormPanel({
		layout:"column", border:false, style:"padding:10px",
	    align:'center',baseCls: "x-plain",
		defaults: { 
		    layout:'form', align:'center', baseCls: "x-plain", xtype: 'panel',columnWidth: 0.33,
		    border:false
	    },
		items:[{ labelWidth: 60,
			 items: [{
			 	xtype: 'compositefield', fieldLabel:"开始时间",  combineErrors: false,
				items:[{ 
					id:"startTime", xtype:"my97date",  width: 110, hiddenName: 'startTime',format: "Y-m-d", allowBlank: false,
					my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: true
				},{	xtype: 'label', text: '  结束时间:', style: 'height:23px; line-height:23px;'	
				},{ id:"endTime", xtype:"my97date", width: 110, hiddenName: 'endTime', format: "Y-m-d", 	my97cfg: {dateFmt:"yyyy-MM-dd"},allowBlank: false,
					initNow: true
				}]	
			 }]
		},{ labelWidth:70,
			items: [{
			 	xtype: 'textfield',  width: FaultTicketAnasys.fieldWidth+20, fieldLabel:"提票人", name:'ticketEmp'
			}]
		},{   columnWidth: 0.1,labelWidth: 1,
			items: [{ 
			 	text:'查询', iconCls:'searchIcon', xtype: 'button', width: 80,
				handler: function(){ 
					var value = Ext.getCmp("reasonAnalysisID_4").getValue();  // 如果为原因分析则查询id						
					FaultTicketAnasys.statisticsFn(value);
				}	
			}]				
		},{ columnWidth: 0.23,   
			items: [{ 
					text:'重置', iconCls:'resetIcon', xtype: 'button', width: 80,
					handler: function(){ 				
					var form = FaultTicketAnasys.searchForm.getForm();
	            	form.reset();
	            	Ext.getCmp("type_1").clearValue();
	            	Ext.getCmp("reasonAnalysisID_4").clearValue();
	            	Ext.getCmp("repairTeamName_3").clearValue();
            		FaultTicketAnasys.isChecked = 1;
					FaultTicketAnasys.statisticsFn();
				}
			}]				
	
	
		},{
			columnWidth: 1    // 空一行
		},{ labelWidth: 1,
			items: [{ 
			   xtype: 'compositefield',
			   items: [{ 
			    	xtype:"radio",name:"type", 
			    	boxLabel:'按提票类型', 
			    	checked:true, 
			    	handler: function(radio, checked){
			    		if(checked){
			    			FaultTicketAnasys.isChecked = 1;
			    			var value = Ext.getCmp("type_1").getValue();
			    			FaultTicketAnasys.statisticsFn(value);
			    		}
			    	}
	    		},{ id:"type_1",
		    		xtype: 'EosDictEntry_combo',
					name: 'faultType',
					dicttypeid:'JCZL_FAULT_TYPE',
					displayField:'dictname',valueField:'dictname',
					width: FaultTicketAnasys.fieldWidth +80,
					listeners : {   
		        	   "select" : function() {   
		        		   FaultTicketAnasys.statisticsFn(this.getValue());
		        		}
		        	}
		    	}]
			}]
	    },{ labelWidth: 1,	    	
			items: [{  
		 	   xtype: 'compositefield',labelWidth: 50,
			   items: [{ 
			    	xtype:"radio",name:"type", boxLabel:'按配件'+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
			    	handler: function(radio, checked){
			    		if(checked){
			    			FaultTicketAnasys.isChecked = 2
			    			var value = Ext.getCmp("flmc_2").getValue();
			    			FaultTicketAnasys.statisticsFn(value);		
			    		}
			    	}
		    	},{ id:"flmc_2",
		    		xtype:"JobProcessDefSelect", width:FaultTicketAnasys.fieldWidth+10,
					name: 'flmc', editable:false, 
					onTriggerClick:function(){
						TrainFormTreeSelectWin.win.show();
					},
					listeners : {   
		        	   "change" : function() {  
		        		   FaultTicketAnasys.statisticsFn(this.getValue());
		        		}
		        	}
		    	}]
			 }]
	  	},{  labelWidth: 80,
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					hiddenName:"isFinished", fieldLabel:"是否修竣", 
					xtype: 'combo',	
			        store:new Ext.data.SimpleStore({
					    fields: ['K','V'],
						data : [['1','是'],['2','否'],['0','全部']]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: "0",
					mode:'local',
					width:50,
					listeners : { 
		        	   "select" : function() {   
		        		   FaultTicketAnasys.statisticsFn();
		        		}
		        	}
				}]
		  },{  labelWidth: 1,
		    	 items: [{  
				 	 xtype: 'compositefield',	    	
					 items: [{  
				    	xtype:"radio",name:"type", boxLabel:'按标签'+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
				    	handler: function(radio, checked){
				    		if(checked){
				    			FaultTicketAnasys.isChecked = 4
				    			var value = Ext.getCmp("reasonAnalysisID_4").getValue();
				    			FaultTicketAnasys.statisticsFn(value);	
				    		}
				    	}
			    	},{  id:"reasonAnalysisID_4",
			    		 xtype: 'Base_multyComboTree',name: 'reasonAnalysisID', isExpandAll: false,
			    		 displayField:'text',valueField:'id',
						 returnField:[{widgetId:"reasonAnalysis_id",propertyName:"text"}],
						 selectNodeModel:'leaf',
						 treeUrl: ctx + '/eosDictEntrySelect!queryChildTree.action', rootText: '标签', 
						 queryParams: {'dicttypeid':'JXGC_Fault_Ticket_YYFX'},
						 width: 260,
						 listeners : {   
			        	   "select" : function() { 
			        		   FaultTicketAnasys.statisticsFn(this.getValue());
			        		}
			        	}					 
			    	}]
		    	 }]
	    },{ 
	    	labelWidth: 1,
	    	items: [{  
		 	   xtype: 'compositefield', 
			   items: [{ 
			    	xtype:"radio",name:"type", boxLabel:'按处理部门',
			    	handler: function(radio, checked){
			    		if(checked){
			    			FaultTicketAnasys.isChecked = 3
			    			var value = Ext.getCmp("repairTeamName_3").getValue();
			    			FaultTicketAnasys.statisticsFn(value);			    	
			    		}
			    	}
		    	},{ id:"repairTeamName_3",
	               	name:"repairTeamName", editable: true, typeAhead:false, selectOnFocus:false,forceSelection:false,
					xtype:"Base_combo", width: FaultTicketAnasys.fieldWidth+10,
					queryHql:" from FaultTicketTeamView ", 
					idProperty: "repairTeamName",
					fields:["repairTeamName"],
					displayField:"repairTeamName", valueField:"repairTeamName",
					returnField:[{widgetId:"repairTeamName", propertyName:"repairTeamName"}],
					listeners : { 
						"beforequery" : function(){		
							this.queryName = "repairTeamName";
	    					this.cascadeStore();
	            		},
		        	   "select" : function() {   
		        		   FaultTicketAnasys.statisticsFn(this.getValue());
		        		}
		        	}
		    	}]
	    	}]
    	  },{   
				xtype:"panel",
				layout:"form",labelWidth: 80,
				columnWidth:0.25,
				items:[{
					hiddenName:"isAccount", fieldLabel:"是否在段", 
					xtype: 'combo',	
			        store:new Ext.data.ArrayStore({
					    fields: ['K','V'],
						data : [['0','全部'],['1','是'],['2','否']]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: '0',
					mode:'local',
					width:50,
					listeners : { 
		        	   "select" : function() {   
		        		   FaultTicketAnasys.statisticsFn();
		        		}
		        	}
				}]
		}]
	});
	
	/*** 查询表单 end ***/



	// 页面自适应布局
	FaultTicketAnasys.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			title: '提票分析', frame: true,
			region: 'north',
			height: 130,
			border: true,
			collapsible: true,
			collapseMode:'mini',
			split: true,
			items: [FaultTicketAnasys.searchForm]
		}, {
			// 统计报表结果显示区域
			id:"report",
			region : 'center', layout : 'fit', bodyBorder: false, 
			split:true,
			items:[]
		}]
	});
			// 页面初始化操作
	FaultTicketAnasys.init = function(){
		 FaultTicketAnasys.statisticsFn();
	};
	FaultTicketAnasys.init();
});