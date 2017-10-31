/**
 * 机车检修作业计划列表
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlan');                       //定义命名空间
	TrainWorkPlan.searchParams = {} ;                    //定义查询条件
	TrainWorkPlan.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlan!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    searchFormColNum:2,
	    labelWidth: 90,  
	    tbar:[],
		fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, searcher: {anchor:'98%'}
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
	},{
		header:'修程主键', dataIndex:'repairClassIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'修程', dataIndex:'repairClassName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
	},{
		header:'修次主键', dataIndex:'repairtimeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'修次', dataIndex:'repairtimeName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
	},{
		header:'备注', dataIndex:'remarks', hidden:true,editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'日历', dataIndex:'workCalendarIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'配属段ID', dataIndex:'dID', hidden:true,editor:{  maxLength:20 }
	},{
		header:'配属段名称', dataIndex:'dNAME',hidden:true, editor:{  maxLength:50 }
	},{
		header:'委托维修段ID', dataIndex:'delegateDID', hidden:true,editor:{  maxLength:20 }
	},{
		header:'委托维修段名称', dataIndex:'delegateDName', hidden:true,editor:{  maxLength:50 }
	},{
		header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn', hidden:true,editor:{ xtype:'my97date' }
	},{
		header:'实际完成时间', dataIndex:'endTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'工艺流程', dataIndex:'processIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'工艺流程', dataIndex:'processName', editor:{  maxLength:50 },searcher:{  disabled:true}
	},{
		header:'状态', dataIndex:'workPlanStatus', editor:{  maxLength:64 },
		renderer : function(v){
			if(v == rdp_status_new)return "新兑现";
			else if(v == rdp_status_handling)return "处理中";
			else if(v == rdp_status_handled) return "已处理";
			else if(v == rdp_status_nullify)return "终止";
			else return "";
		}, width: 60
	}],
	toEditFn: function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		QR.rpdIdx = record.get("idx") ;  //施修任务兑现单主键
        QR.toEditFn(grid, rowIndex, e);
    }
});
	
	TrainWorkPlan.status = rdp_status_handling + "," + rdp_status_handled + ",";
	//状态多选按钮
	TrainWorkPlan.checkQuery = function(status){
		TrainWorkPlan.status = "-1";
		if(Ext.getCmp("status_handling").checked){
			TrainWorkPlan.status = TrainWorkPlan.status + "," + rdp_status_handling;
		} 
		if(Ext.getCmp("status_handled").checked){
			TrainWorkPlan.status = TrainWorkPlan.status + "," + rdp_status_handled;
		}
		TrainWorkPlan.grid.store.load();
	}
	
//加载前过滤
TrainWorkPlan.grid.store.on('beforeload',function(){
	var searchParam = TrainWorkPlan.searchParams ;
	searchParam = MyJson.deleteBlankProp(searchParam); 
	delete searchParam.status ;
	var whereList = [] ;
	for(porp in searchParam){
		switch(porp){
		 	//进车时间(起) 运算符为">="
		 	case 'planBeginTime':
		 		whereList.push({propName:'planBeginTime',propValue:searchParam[porp],compare:Condition.GE});
		 		break;
		 	//进车时间(止) 运算符为"<="
		 	case 'planBeginTime_end':
		 		whereList.push({propName:'planBeginTime',propValue:searchParam[porp]+' 23:59:59',compare:Condition.LE});
		 		break;
			//其他文本类型查询框 运算符为"like"		 		
		 	default:
         		whereList.push({propName:porp, propValue: searchParam[porp] }) ; 
		 }
	}
	if(siteID !=null){
    	 whereList.push({propName:'siteID',propValue:siteID});
    }
    whereList.push({propName:'workPlanStatus', propValues: TrainWorkPlan.status.split(",") ,compare : Condition.IN}) ; 
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
TrainWorkPlan.searchForm=new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: TrainWorkPlan.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
		     columnWidth:.25,labelWidth: TrainWorkPlan.labelWidth,
		     layout:'form',
		     baseCls:'x-plain',
		     items:[
		     	  { id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
						  hiddenName: "trainTypeIDX",
						  displayField: "shortName", valueField: "typeID",width:120,
						  pageSize: 20, minListWidth: 200,   //isCx:'no',
						  editable:false  ,
							listeners : {   
					        	"select" : function() {   
					                //重新加载修程下拉数据
					                var rc_comb_s = Ext.getCmp("rc_comb");
					                rc_comb_s.reset();
					                rc_comb_s.clearValue();
					                rc_comb_s.getStore().removeAll();
					                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
					                rc_comb_s.cascadeStore();
					                //重新加载修次数据
	        	                	var rt_comb = Ext.getCmp("rt_comb");
	        	                	rt_comb.clearValue();
	        	                 	rt_comb.reset();
	        	                 	rt_comb.getStore().removeAll();
	        	                 	rt_comb.cascadeStore();
					        	}   
					    	 }
		                    }
		        ]},{
			     columnWidth:.25,
			     layout:'form',labelWidth: TrainWorkPlan.labelWidth,
			     baseCls:'x-plain',
			     items:[
			     		{xtype:'textfield',fieldLabel:'车号',name:"trainNo"}
			   	 	
				]},{
			     columnWidth:.25,
			     layout:'form',labelWidth: TrainWorkPlan.labelWidth,
			     baseCls:'x-plain',
			     items:[
			         {
						id:"rc_comb",
						xtype: "Base_combo",
						business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
						fieldLabel: "修程",
						hiddenName: "repairClassIDX", 
						displayField: "xcName",
						valueField: "xcID",
						pageSize: 20, minListWidth: 200,
						queryHql: 'from UndertakeRc',
						width: 120,
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
					}
			     ]
	        },{
			     columnWidth:.25,
			     layout:'form',labelWidth: TrainWorkPlan.labelWidth,
			     baseCls:'x-plain',
			     items:[
			         {
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
//		                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
//		    					if(rcIdx == "" || rcIdx == null){
//		    						MyExt.Msg.alert("请先选择下车修程！");
//		    						return false;
//		    					}
		                	}
		    			}
		    		}
			     ]
	        }
	        ]
	    },{
		        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
		        items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:100,
		            columnWidth: .5, 
		            items: [
	                    {
						xtype: 'compositefield', fieldLabel : '计划开始日期', combineErrors: false,
			        	items: [
				           { name: "planBeginTime",  xtype: "my97date",format: "Y-m-d", initNow:false,
							width: 120},
							{
					    	    xtype:'label', text: '结束：'
						    },
							{ name: "planBeginTime_end", xtype: "my97date",format: "Y-m-d", initNow:false,
							width: 120}]
					}
		            ]
		        },{
			     columnWidth:.5,
			     layout:'form',labelWidth: TrainWorkPlan.labelWidth,
			     baseCls:'x-plain',
			     items:[
			        {
							xtype: 'compositefield', fieldLabel : '状态', combineErrors: false,
				        	items: [
							  {   
							    xtype:'checkbox', name:'status', id: 'status_handling', boxLabel:'处理中&nbsp;&nbsp;&nbsp;&nbsp;', 
							    	inputValue:rdp_status_handling, checked:true,
								    handler: function(){
								    	TrainWorkPlan.checkQuery(rdp_status_handling);
								    }
							  },{   
							    xtype:'checkbox', name:'status', id: 'status_handled', boxLabel:'已处理&nbsp;&nbsp;&nbsp;&nbsp;', 
							    	inputValue:rdp_status_handled,checked:true,
								    handler: function(){
								    	TrainWorkPlan.checkQuery(rdp_status_handled);
								    }
							  }]
						}
			     ]
	        }
		        ]
		    }],
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	TrainWorkPlan.searchParams = TrainWorkPlan.searchForm.getForm().getValues();
            	TrainWorkPlan.searchParams = MyJson.deleteBlankProp(TrainWorkPlan.searchParams);
	 			TrainWorkPlan.grid.searchFn(TrainWorkPlan.searchParams);
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	TrainWorkPlan.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb").clearValue();
            	Ext.getCmp("rt_comb").clearValue();
            	TrainWorkPlan.searchParams = {};
			    TrainWorkPlan.grid.store.load();
            }
        }]
	});
// 页面自适应布局
var viewport = new Ext.Viewport({
			layout : 'border',
			defaults : {
				layout : 'fit'
			},
			items : [{
						region : 'north',
						height : 160,
						title : '查询	',
						collapsible : true,
						frame : true,
						items : TrainWorkPlan.searchForm
					}, {
						region : 'center',
						items : TrainWorkPlan.grid

					}]
		});
});