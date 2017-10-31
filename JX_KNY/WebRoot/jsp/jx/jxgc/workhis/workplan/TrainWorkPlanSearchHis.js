/**
 * 机车检修作业计划列表
 */
Ext.onReady(function(){
	Ext.ns('TrainWorkPlan');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlan.labelWidth = 100;
	TrainWorkPlan.fieldWidth = 140;
	TrainWorkPlan.status = [rdp_status_handling, rdp_status_handled]; 	// 默认查询“处理中”和“已处理”的工单
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	
	//机车检修记录单结果双击显示界面
	TrainWorkPlan.toEditFn = function(grid, rowIndex, e){
		
		if(this.searchWin != null)  this.searchWin.hide();  
		var record = grid.store.getAt(rowIndex);
	    // 检修记录单界面显示信息
		TrainWorkPlanCommHis.setParams(record.json);
		TrainWorkPlanCommHis.saveWin.show();
	} 
	
	/**
	 * 状态多选按钮触发的函数处理
	 */
	TrainWorkPlan.checkQuery = function(){
		TrainWorkPlan.status = [-1];
		if(Ext.getCmp("status_handling").checked){
			TrainWorkPlan.status.push(rdp_status_handling);		// 处理中
		} 
		if(Ext.getCmp("status_handled").checked){
			TrainWorkPlan.status.push(rdp_status_handled);		// 已处理
		}
		TrainWorkPlan.grid.store.load();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	TrainWorkPlan.searchForm = new Ext.form.FormPanel({
	    padding:10, labelWidth: TrainWorkPlan.labelWidth,
	    layout: 'column',
	    defaults: { layout: 'form', columnWidth: .25, defaults: {
	    	width: TrainWorkPlan.fieldWidth
	    }},
	    items: [{
	    	items: [{
	    		fieldLabel: "车型",
	    		id:"trainType_comb", xtype: "TrainType_combo", 
			  	hiddenName: "trainTypeIDX",
			  	displayField: "shortName", valueField: "typeID", 
			  	pageSize: 20, minListWidth: 200,   //isCx:'no',
			  	editable:false  ,
				listeners: {   
		        	"select" : function() {   
		                //重新加载修程下拉数据
		                var rc_comb_s = Ext.getCmp("rc_comb");
		                rc_comb_s.reset();
		                rc_comb_s.clearValue();
		                rc_comb_s.getStore().removeAll();
		                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
		                rc_comb_s.cascadeStore();
		        	}   
	    	 	}
	    	}, {
	    		name: "planBeginTime", xtype: "my97date", format: "Y-m-d", initNow: false, fieldLabel: '起始(计划开始时间)'
	    	}]
	    }, {
	    	items: [{
	    		xtype:'textfield', fieldLabel:'车号', name:"trainNo"
	    	}, {
	    		name: "planBeginTime_end",  xtype: "my97date", format: "Y-m-d", initNow:false, fieldLabel : '截止(计划开始时间)'
	    	}]
	    }, {
	    	items: [{
				fieldLabel: "修程",
	    		id: "rc_comb",
				xtype: "Base_combo",
				business: 'trainRC',
				entity: 'com.yunda.jx.base.jcgy.entity.TrainRC',
				fields: ['xcID','xcName'],
				hiddenName: "repairClassIDX", 
				displayField: "xcName",
				valueField: "xcID",
				pageSize: 20, minListWidth: 200,
				queryHql: 'from UndertakeRc',
				editable: false,
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
	    		id: "wxid", xtype: "DeportSelect2_comboTree",
				hiddenName: "delegateDID", fieldLabel: "委修段", 
				returnField: [{widgetId:"wxname",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
	    	}, { id: "wxname", name: "delegateDName", xtype:"hidden"}]
	    }, {
	    	items: [{
				id:"rt_comb",
				xtype: "Base_combo",
				fieldLabel: "修次",
				hiddenName: "repairtimeIDX", 
				returnField: [{widgetId:"repairtimeName_Id",propertyName:"repairtimeName"}],
				displayField: "repairtimeName",
				valueField: "repairtimeIDX",
				fields: ["repairtimeIDX","repairtimeName"],
				business: 'rcRt',
				pageSize: 0,
				minListWidth: 200,
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
	    		id: "psId", xtype: "DeportSelect2_comboTree",
				hiddenName: "dID", fieldLabel: "配属段", 
				returnField: [{widgetId:"psNAME",propertyName:"delegateDName"}],
			    selectNodeModel: "leaf"
	    	}]
	    	
	    }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			TrainWorkPlan.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	TrainWorkPlan.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb").clearValue();
            	Ext.getCmp("psId").clearValue();
            	Ext.getCmp("wxid").clearValue();
			    TrainWorkPlan.grid.store.load();
            }
        }]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义工单列表开始 **************** */
	TrainWorkPlan.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlanHis!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    tbar : ['机车检修作业状态：', {
			xtype : 'checkbox', name : 'status',
			id : 'status_handling',
			boxLabel : '在修&nbsp;&nbsp;&nbsp;&nbsp;', inputValue : rdp_status_handling,
			checked : true,
			handler :  TrainWorkPlan.checkQuery
		}, {
			xtype : 'checkbox', name : 'status',
			id : 'status_handled',
			boxLabel : '修竣&nbsp;&nbsp;&nbsp;&nbsp;', inputValue : rdp_status_handled,
			checked : true,
			handler :  TrainWorkPlan.checkQuery
		}],
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
			header:'配属段', dataIndex:'dNAME', editor:{  maxLength:50 }
		},{
			header:'委托维修段ID', dataIndex:'delegateDID', hidden:true,editor:{  maxLength:20 }
		},{
			header:'委修段', dataIndex:'delegateDName',editor:{  maxLength:50 }
		},{
			header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn',editor:{ xtype:'my97date' }
		},{
			header:'实际完成时间', dataIndex:'endTime', xtype:'datecolumn',  editor:{ xtype:'my97date' }
		},{
			header:'工艺流程', dataIndex:'processIDX',hidden:true, hidden:true, editor:{  maxLength:50 }
		},{
			header:'工艺流程', dataIndex:'processName',  hidden:true,editor:{  maxLength:50 },searcher:{  disabled:true}
		},{
			header:'是否转历史', dataIndex:'transLogStatus',  hidden:true, searcher:{  disabled:true}
		},{
			header:'状态', dataIndex:'workPlanStatus', editor:{  maxLength:64 }, width: 60,
			renderer : function(v){
				if(v == rdp_status_new) return "待修";
				if(v == rdp_status_handling)return "在修";
				if(v == rdp_status_handled) return "修竣";
				if(v == rdp_status_nullify)return "终止";
				return "错误！未知的工单状态：" + v;
			}
		},{
			header:'停时图', dataIndex:'idx', width: 60,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var startTime = new Date( record.get('planBeginTime')).format('Y-m-d');
	     		var lastTime = new Date(record.get('planEndTime')).format('Y-m-d');
//	     		var startTime = planBeginTime, lastTime = planEndTime;
//	     		if (record.get('beginTime')) {		
//					startTime <= new Date(record.get('beginTime'))? planBeginTime:new Date(record.get('beginTime'));
//				}
//				if(record.get('endTime')){
//					lastTime <= new Date(record.get('endTime'))? new Date(record.get('endTime')):planEndTime;
//				}
	     		var trainTypeAndNo = record.get('trainTypeShortName')+"|"+record.get('trainNo');
				var args = [value,startTime,lastTime,trainTypeAndNo].join(',');	
	  		    return "<img src='" + diaImg + "' alt='停时图' style='cursor:pointer' onclick='TrainWorkStopAnasys.showStopAnasys(\""+ args +"\")'/>";
			},searcher:{disabled:true}
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);	
	        TrainWorkPlan.toEditFn(grid, rowIndex, e);
	    }
	});
	//加载前过滤
	TrainWorkPlan.grid.store.on('beforeload',function(){
		var searchParam = TrainWorkPlan.searchForm.getForm().getValues();
		searchParam = MyJson.deleteBlankProp(searchParam); 
		
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
		if(siteID != null){
	    	 whereList.push({propName:'siteID', propValue:siteID});
	    }
	    whereList.push({propName:'workPlanStatus', propValues: TrainWorkPlan.status ,compare : Condition.IN}) ; 
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** **************** 定义工单列表结束 **************** */
	
	// 页面自适应布局
	var viewport = new Ext.Viewport({
		layout : 'border',
		defaults : { layout : 'fit' },
		items : [{
			region : 'north',
			height : 150,
			title : '查询',
			collapsible : true, frame : true,
			items : TrainWorkPlan.searchForm
		}, {
			region : 'center',
			items : TrainWorkPlan.grid
		}]
	});
	
});