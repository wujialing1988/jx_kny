/** 车辆验收 */
Ext.onReady(function(){
	Ext.namespace('TrainAcceptance');                       //定义命名空间
	
	TrainAcceptance.labelWidth = 100;
	TrainAcceptance.fieldWidth = 140;
	
	var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	
	
	/** **************** 定义查询表单开始 **************** */
	TrainAcceptance.searchForm = new Ext.form.FormPanel({
	    padding:10, labelWidth: TrainAcceptance.labelWidth,
	    layout: 'column',
	    defaults: { layout: 'form', columnWidth: .25, defaults: {
	    	width: TrainAcceptance.fieldWidth
	    }},
	    items: [{
	    	items: [{
				fieldLabel: "车型",
				id:"trainType_comb",
				xtype: "Base_combo",
				hiddenName: "trainTypeIDX",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode'],
                returnField:[{widgetId:"trainTypeShortName_Id",propertyName:"typeCode"}],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:false,				  	
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
	    		name: "planBeginTime", xtype: "my97date", format: "Y-m-d", initNow: false, fieldLabel: '起始(计划开始)'
	    	}]
	    }, {
	    	items: [{
	    		xtype:'textfield', fieldLabel:'车号', name:"trainNo"
	    	}, {
	    		name: "planBeginTime_end",  xtype: "my97date", format: "Y-m-d", initNow:false, fieldLabel : '截止(计划开始)'
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
				queryParams: {'vehicleType':vehicleType},// 表示客货类型
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
	    	}]
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
	    	}]
	    }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			TrainAcceptance.TrainAcceptanceUnCompleteGrid.store.load();
	 			TrainAcceptance.TrainAcceptanceCompleteGrid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	TrainAcceptance.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb").clearValue();
            	TrainAcceptance.TrainAcceptanceUnCompleteGrid.store.load();
			    TrainAcceptance.TrainAcceptanceCompleteGrid.store.load();
            }
        }]
	});
	/** **************** 定义查询表单结束 **************** */	
	
	TrainAcceptance.toEditFn = function(grid, rowIndex, e){
		if(this.searchWin != null)  this.searchWin.hide();  
		var record = grid.store.getAt(rowIndex);
	    // 检修记录单界面显示信息
		TrainRecordRdpPlan.setParams(record.json);
		TrainRecordRdpPlan.saveWin.show();
	} 	
	
	
	/** **************** 未验证列表开始 **************** */
	TrainAcceptance.TrainAcceptanceUnCompleteGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlan!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    tbar : [{
    	text:"交验", iconCls:"addIcon", handler: function() {
    		var grid = TrainAcceptance.TrainAcceptanceUnCompleteGrid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		if (data.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;	        	
    		}
    		TrainAcceptanceWin.workPlanIdx = data[0].data.idx ;
    		TrainAcceptanceWin.saveWin.show();
    	}
    },'-','<div><span style="color:green;">*双击数据查看检修详情！</span></div>'],
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
			header:'状态', dataIndex:'workPlanStatus',hidden:true, editor:{  maxLength:64 }, width: 60,
			renderer : function(v){
				if(v == rdp_status_new) return "待修";
				if(v == rdp_status_handling)return "在修";
				if(v == rdp_status_handled) return "修竣";
				if(v == rdp_status_nullify)return "终止";
				return "错误！未知的工单状态：" + v;
			}
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);	
	        TrainAcceptance.toEditFn(grid, rowIndex, e);
	    }
	});
	//加载前过滤
	TrainAcceptance.TrainAcceptanceUnCompleteGrid.store.on('beforeload',function(){
		var searchParam = TrainAcceptance.searchForm.getForm().getValues();
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
	         		whereList.push({propName:porp, propValue: searchParam[porp],compare:Condition.EQ,stringLike: true }) ; 
			 }
		}
	    whereList.push({ propName: 'workPlanStatus', propValue:rdp_status_handled , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'fromStatus', propValue:"0" , compare: Condition.EQ, stringLike: false }); // 校验状态为【未校验】
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** **************** 未验证列表结束 **************** */	
	
	
	/** **************** 已验证列表开始 **************** */
	TrainAcceptance.TrainAcceptanceCompleteGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlan!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    tbar : [{
    	text:"打印", iconCls:"printerIcon", handler: function() {    		
    		var sel = TrainAcceptance.TrainAcceptanceCompleteGrid.selModel.getSelections();
            if(sel.length != 1){
                MyExt.Msg.alert("请选择一条记录!");
                return ;
            }
			var workPlanIdx = sel[0].get("idx"); 
            var url = "/scdd/TrainAcceptance.cpt";
            if(workPlanIdx){
                url += "&workPlanIdx=" + workPlanIdx;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(url))+"&title=" + encodeURI("车辆修竣通知单"));
            }
    	}
    },'-','<div><span style="color:green;">*双击数据查看检修详情！</span></div>'],
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
			header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'实际开始时间', dataIndex:'beginTime', xtype:'datecolumn',editor:{ xtype:'my97date' }
		},{
			header:'实际完成时间', dataIndex:'endTime', xtype:'datecolumn',  editor:{ xtype:'my97date' }
		},{
			header:'交车人', dataIndex:'fromPersonName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
		},{
			header:'验收员', dataIndex:'toPersonName', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
		},{
			header:'交验时间', dataIndex:'fromTime',xtype:'datecolumn', editor:{  xtype:'my97date' }
		},{
			header:'交验情况', dataIndex:'fromRemark', editor:{  maxLength:50 }, searcher: {anchor:'98%'}
		},{
			header:'工艺流程', dataIndex:'processIDX',hidden:true, hidden:true, editor:{  maxLength:50 }
		},{
			header:'工艺流程', dataIndex:'processName',  hidden:true,editor:{  maxLength:50 },searcher:{  disabled:true}
		},{
			header:'是否转历史', dataIndex:'transLogStatus',  hidden:true, searcher:{  disabled:true}
		},{
			header:'状态', dataIndex:'workPlanStatus',hidden:true, editor:{  maxLength:64 }, width: 60,
			renderer : function(v){
				if(v == rdp_status_new) return "待修";
				if(v == rdp_status_handling)return "在修";
				if(v == rdp_status_handled) return "修竣";
				if(v == rdp_status_nullify)return "终止";
				return "错误！未知的工单状态：" + v;
			}
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);	
	        TrainAcceptance.toEditFn(grid, rowIndex, e);
	    }
	});
	
	//加载前过滤
	TrainAcceptance.TrainAcceptanceCompleteGrid.store.on('beforeload',function(){
		var searchParam = TrainAcceptance.searchForm.getForm().getValues();
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
	         		whereList.push({propName:porp, propValue: searchParam[porp],compare:Condition.EQ,stringLike: true }) ; 
			 }
		}
	    whereList.push({ propName: 'workPlanStatus', propValue:rdp_status_handled , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'fromStatus', propValue:"1" , compare: Condition.EQ, stringLike: false }); // 校验状态为【已校验】
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** **************** 已验证列表结束 **************** */		
	
	
	
	// tab选项卡布局
	TrainAcceptance.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "TrainAcceptanceUnCompleteTab", title: '未验收', layout:'fit', items: [TrainAcceptance.TrainAcceptanceUnCompleteGrid],
	   	    listeners:{
	    		activate:function(){
	    			TrainAcceptance.TrainAcceptanceUnCompleteGrid.getStore().reload();
	    		}	        	
	    	}
	    },{  
	       id: "TrainAcceptanceCompleteTab", title: '已验收', layout:'fit', items: [TrainAcceptance.TrainAcceptanceCompleteGrid],
	   	    listeners:{
	    		activate:function(){
	    			TrainAcceptance.TrainAcceptanceCompleteGrid.getStore().reload();
	    		}	        	
	    	}	       
	    }]
	});
	
	// 页面自适应布局
	var viewport = new Ext.Viewport({
		layout : 'border',
		defaults : { layout : 'fit' },
		items : [{
			region : 'north',
			height : 150,
			title : '查询',
			collapsible : true, frame : true,
			items : TrainAcceptance.searchForm
		}, {
			region : 'center',
			items : TrainAcceptance.tabs
		}]
	});
});