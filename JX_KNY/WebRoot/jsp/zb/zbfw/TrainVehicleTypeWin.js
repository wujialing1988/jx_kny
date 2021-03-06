/**
 * Jcgy_train_type 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
Ext.namespace('TrainType');                       //定义命名空间

//定义全局变量保存查询条件
TrainType.searchParam = {};

// 已选的数据
TrainType.trainVehicleCode = "" ;

TrainType.whereList = [];	// 其他条件

TrainType.queryTimeout;

TrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainVehicleType!pageQuery.action',                 //装载列表数据的请求URL
    singleSelect: false, 
    saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    tbar : ['&nbsp;&nbsp;',
    {
    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:i18n.ZbFw.QuickSearch, width:250,
    	listeners: {
    		keyup: function(filed, e) {
    			if (TrainType.queryTimeout) {
    				clearTimeout(TrainType.queryTimeout);
    			}
    			
    			TrainType.queryTimeout = setTimeout(function(){
					TrainType.grid.store.load();
    			}, 1000);
    		}
		}
    }],
	fields: [{
		header:i18n.TrainVehicleTypeWin.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	},{
		header:i18n.ZbFw.typeCode, dataIndex:'typeCode',editor:{ allowBlank:false ,maxLength:20  }
	},{
		header:i18n.ZbFw.trainType, dataIndex:'typeName', editor:{ maxLength:50  }
	},{
		header:i18n.ZbFw.trainTypeShortName, dataIndex:'shortName', editor:{ maxLength:20  },searcher: { hidden: true }
	},{
   		header:i18n.TrainVehicleTypeWin.vehicleKindCode, dataIndex:'vehicleKindCode', hidden:true, editor: { xtype:'hidden',id:'vehicleKindCode' }
	},{
		header:i18n.ZbFw.vehicleKindName, dataIndex:'vehicleKindName', searcher: {anchor:'98%'}
	},{
		header:i18n.ZbFw.description, dataIndex:'description', editor:{ maxLength:20,xtype:'textarea' },searcher: { hidden: true }
	},{
		header:i18n.TrainVehicleTypeWin.vehicleType, dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
	}],
	searchFn: function(searchParam){ 
		TrainType.searchParam = searchParam ;
        TrainType.grid.store.load();
	}
});

TrainType.grid.un('rowdblclick',TrainType.grid.toEditFn,TrainType.grid);

//定义点击确定按钮的操作
TrainType.submit = function(){
	alert(i18n.TrainVehicleTypeWin.msg);
}


//定义选择窗口
TrainType.selectWin = new Ext.Window({
	title:i18n.ZbFw.ApplicableTrainSelect, width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
	maximizable:false, items:[TrainType.grid],modal:true,
	buttons: [{
		text : i18n.common.button.confirm,iconCls : "saveIcon", handler: function(){
			TrainType.submit(); 
		}
	},{
        text: i18n.common.button.close, iconCls: "closeIcon", scope: this, handler: function(){ TrainType.selectWin.hide(); }
	}],
	listeners:{
		"show":function(){
			
		}
	}
});
	
//查询前添加过滤条件
TrainType.grid.store.on('beforeload' , function(){
	var whereList = [];
	if(vehicleType){
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
	}
	var queryKey = Ext.getCmp('query_input').getValue();
	
	// 过滤
	if(!Ext.isEmpty(TrainType.whereList) && TrainType.whereList.length > 0){
		for (var i = 0; i < TrainType.whereList.length; i++) {
			whereList.push(TrainType.whereList[i]);
		}
	}
	
	if (!Ext.isEmpty(queryKey)) {
		var sql = " (T_TYPE_CODE LIKE '%" + queryKey + "%'"
				+ " OR T_TYPE_NAME LIKE '%" + queryKey + "%') ";
	    whereList.push({ sql: sql, compare: Condition.SQL});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	
});

// 添加加载结束事件
TrainType.grid.getStore().addListener('load',function(me, records, options ){
	// 选中已选的数据
	if(records.length > 0 && !Ext.isEmpty(TrainType.trainVehicleCode)){
		var trainVehicleCodeArray = TrainType.trainVehicleCode.split(",");
	    var selectedRecords = [];
    	TrainType.grid.getStore().each(function(record){
    		for(var i = 0 ; i < trainVehicleCodeArray.length; i++){
    			if(trainVehicleCodeArray[i] == record.get('typeCode')){
    				this.push(record);
    				break;
    			}
    		}
    	}, selectedRecords);
    	if(selectedRecords.length > 0){
    		TrainType.grid.getSelectionModel().selectRecords(selectedRecords);
    	}else{
    		TrainType.grid.getSelectionModel().clearSelections();
    	}
	}else{
		//TrainType.grid.getSelectionModel().clearSelections();
	}
});

});