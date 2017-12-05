/**
 * 扣车信息选择管理
 */
Ext.onReady(function(){
	
Ext.namespace('DetainTrainSelect');                       //定义命名空间

//定义全局变量保存查询条件
DetainTrainSelect.searchParam = {} ;

DetainTrainSelect.vehicleType = null ; //客货类型

DetainTrainSelect.labelWidth = 40;                        //表单中的标签名称宽度
DetainTrainSelect.fieldWidth = 130;                       //表单中的标签宽度

DetainTrainSelect.queryTimeout;	// 间隔查询

DetainTrainSelect.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/detainTrain!pageQuery.action',                 //装载列表数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    saveForm:DetainTrainSelect.saveForm,
    tbar : ['&nbsp;&nbsp;',
    {
    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:'输入车号快速检索...', width:200,
    	listeners: {
    		keyup: function(filed, e) {
    			if (DetainTrainSelect.queryTimeout) {
    				clearTimeout(DetainTrainSelect.queryTimeout);
    			}
    			
    			DetainTrainSelect.queryTimeout = setTimeout(function(){
					DetainTrainSelect.grid.store.load();
    			}, 1000);
    		}
		}
    },{
    	text:"生成作业计划", iconCls:"acceptIcon", handler:function(){
    		//未选择记录，直接返回
    		if(!$yd.isSelectedRecord(DetainTrainSelect.grid)) return;
    		var data = DetainTrainSelect.grid.selModel.getSelections(); 
    		DetainTrainSelect.showWorkPlanWin(data[0]);  
		}
		},{
		text:"关闭", iconCls:"closeIcon",handler:function(){
			DetainTrainSelect.win.hide();
		}
		}],    
	fields: [
     	{
		header:'车辆车型ID', dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车型', dataIndex:'trainTypeCode',width: 120,editor: {}
	},
     	{
		header:'车辆车型名称', dataIndex:'trainTypeName',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车号', dataIndex:'trainNo',width: 120,editor: {}
	}, {
		header:'扣车类型编码', dataIndex:'detainTypeCode',width: 120,hidden:true,editor: {}
	},
     	{
		header:'扣车类型', dataIndex:'detainTypeName',width: 120,editor: {}
	},{
		header:'登记人ID', dataIndex:'proposerIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'登记人', dataIndex:'proposerName',width: 120,editor: {}
	},
       {
		header:'登记时间', dataIndex:'proposerDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date',format: 'Y-m-d H:i' }
	},
     	{
		header:'扣车原因', dataIndex:'detainReason',width: 120,editor: {}
	}, {
		header:'站点ID', dataIndex:'siteID',width: 120,hidden:true ,editor: {}
	},
     	{
		header:'站点名称', dataIndex:'siteName',width: 120,hidden:true , editor: {}
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		DetainTrainSelect.searchParam = searchParam ;
        DetainTrainSelect.grid.store.load();
	}
});

//移除侦听器
DetainTrainSelect.grid.un('rowdblclick', DetainTrainSelect.grid.toEditFn, DetainTrainSelect.grid);
	
// 查询前添加过滤条件
DetainTrainSelect.grid.store.on('beforeload' , function(){
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('query_input').getValue())){
			whereList.push({ propName: 'trainNo', propValue: Ext.getCmp('query_input').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'vehicleType', propValue:DetainTrainSelect.vehicleType, compare: Condition.EQ, stringLike: false});		
		whereList.push({ propName: 'detainStatus', propValue:"10", compare: Condition.EQ, stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

//施修计划选择窗口
DetainTrainSelect.win = new Ext.Window({
    title: "扣车车辆选择", width: 800, height: 300, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [DetainTrainSelect.grid]
});

DetainTrainSelect.showWin = function() {
	DetainTrainSelect.win.show();
	DetainTrainSelect.grid.store.reload();
};

DetainTrainSelect.showWorkPlanWin = function(data) {
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
    form.findField("trainTypeIDX").setDisplayValue(data.get("trainTypeIdx"), data.get("trainTypeCode"));
    form.findField("trainNo").setDisplayValue(data.get("trainNo"), data.get("trainNo"));
    form.findField("trainTypeShortName").setValue(data.get("trainTypeCode"));
    jx.jxgc.JobProcessDefSelect.trainTypeIDX = data.get("trainTypeIdx");
    TrainWorkPlanForm.disableColumns(['trainTypeIDX','trainNo']);
};



});