Ext.onReady(function(){	
Ext.namespace('TrainPlanSelect');
TrainPlanSelect.searchParams = {};

TrainPlanSelect.vehicleType = null ; //客货类型

//机车施修计划选择列表
TrainPlanSelect.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!findPageListForRdp.action',                 //装载列表数据的请求URL    
    viewConfig: null,
    searchFormColNum: 2,
    singleSelect: true,
    storeAutoLoad: false,
    tbar: ['search',{
	    	text:"生成作业计划", iconCls:"acceptIcon", handler:function(){
	    		//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(TrainPlanSelect.grid)) return;
        		var data = TrainPlanSelect.grid.selModel.getSelections(); 
        		TrainPlanSelect.showWorkPlanWin(data[0]);  
    	}
    },{
	text:"关闭", iconCls:"closeIcon",handler:function(){
		TrainPlanSelect.win.hide();
	}
}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden: true, 
		editor:{ }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ },
		searcher:{ 
				fieldLabel: "车型",
				xtype: "Base_combo",
				hiddenName: "trainTypeShortName",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode'],
                queryParams: {'vehicleType':TrainPlanSelect.vehicleType},// 表示客货类型
    		    displayField: "typeCode", valueField: "typeCode",
                pageSize: 20, minListWidth: 200,
                editable:false,					
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = TrainPlanSelect.grid.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			}
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{
		},
		searcher:{
				id:"trainNo_comb_search",	
				fieldLabel: "车号",
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':TrainPlanSelect.vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                allowBlank: false,
                disabled:false,
                editable:true			
			}
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true,
		editor:{}
	},{
		header:'修程', dataIndex:'repairClassName', editor:{  xtype:'hidden' }
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true,
		editor:{}
	},{
		header:'修次', dataIndex:'repairtimeName', editor:{  xtype: 'hidden' }
	},{
		header:'计划修车日期', dataIndex:'planStartDate', xtype:'datecolumn', 
		editor:{ },
		searcher:{disabled: true}
	},{
		header:'计划交车日期', dataIndex:'planEndDate', xtype:'datecolumn', 
		editor:{ },
		searcher:{disabled: true}
	}],
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){	    
	    //清空查询条件参数
    	TrainPlanSelect.searchParams = {};
    	if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
			searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
		}
    	//将查询表单数据设置到全局查询参数集
    	for(prop in searchParam){
	    	TrainPlanSelect.searchParams[prop] = searchParam[prop];
		}
		//将全局查询参数集设置到baseParams，解决分页控件刷新
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(TrainPlanSelect.searchParams);
    	this.store.load();
    }
});


//移除侦听器
TrainPlanSelect.grid.un('rowdblclick', TrainPlanSelect.grid.toEditFn, TrainPlanSelect.grid);

//施修计划选择窗口
TrainPlanSelect.win = new Ext.Window({
    title: "车辆生产计划选择", width: 600, height: 300, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [TrainPlanSelect.grid]
});
//设置查询窗口为模态窗口
TrainPlanSelect.grid.createSearchWin();
TrainPlanSelect.grid.searchWin.modal = true;
TrainPlanSelect.grid.store.on("beforeload", function(){
	if(!Ext.isEmpty(TrainPlanSelect.vehicleType)){
		TrainPlanSelect.searchParams.vehicleType = TrainPlanSelect.vehicleType ;
	}
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainPlanSelect.searchParams);
});
TrainPlanSelect.showWin = function() {
	TrainPlanSelect.win.show();
    TrainPlanSelect.grid.store.reload();
}
TrainPlanSelect.showWorkPlanWin = function(data) {
	var form = TrainWorkPlanForm.form.getForm();
	form.reset();
	var componentArray = ["Base_combo","DeportSelect2_comboTree"];
    for(var j = 0; j < componentArray.length; j++){
    	var component = TrainWorkPlanForm.win.findByType(componentArray[j]);
    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
    		continue;
    	}else{
    		for(var i = 0; i < component.length; i++){
                component[ i ].clearValue();
            }
    	}	                    
    }
	var cfg = {
        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.defInfo != null) {
                form.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
    TrainWorkPlanForm.win.show();
    form.findField("trainTypeIDX").setDisplayValue(data.get("trainTypeIDX"), data.get("trainTypeShortName"));
    form.findField("trainNo").setDisplayValue(data.get("trainNo"), data.get("trainNo"));
    form.findField("trainTypeShortName").setValue(data.get("trainTypeShortName"));
    form.findField("repairClassIDX").setDisplayValue(data.get("repairClassIDX"), data.get("repairClassName"));
    form.findField("repairClassName").setValue(data.get("repairClassName"));
    form.findField("repairtimeName").setValue(data.get("repairtimeName"));
    form.findField("repairtimeIDX").setDisplayValue(data.get("repairtimeIDX"), data.get("repairtimeName"));
    form.findField("enforcePlanDetailIDX").setValue(data.get("idx"));
    jx.jxgc.JobProcessDefSelect.trainTypeIDX = data.get("trainTypeIDX");
    jx.jxgc.JobProcessDefSelect.rcIDX = data.get("repairClassIDX");
    TrainWorkPlanForm.disableColumns(['trainTypeIDX','trainNo','repairClassIDX','repairtimeIDX']);
}
});