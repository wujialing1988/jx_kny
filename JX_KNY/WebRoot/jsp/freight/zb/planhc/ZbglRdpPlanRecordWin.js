/**
 * 车辆选择窗口
 */
Ext.onReady(function(){
	
Ext.namespace('ZbglRdpPlanRecordWin');                       //定义命名空间

//定义全局变量保存查询条件
ZbglRdpPlanRecordWin.searchParam = {};

// 已选的数据
ZbglRdpPlanRecordWin.recordIds = "" ;

// 计划ID
ZbglRdpPlanRecordWin.rdpPlanIdx = "###" ;

ZbglRdpPlanRecordWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpPlanRecord!pageList.action',                 //装载列表数据的请求URL
    singleSelect: false, 
    saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    isEdit:false,
   	page: false,                               
    pageSize: 999,
    tbar:[],
	fields: [{
				header : 'idx主键',
				dataIndex : 'idx',
				hidden : true,
				editor : {
					xtype : 'hidden'
				}
			}, {
				header : '<div>编号<span style="color:green;">【车型车号】</span></div>',
				dataIndex : 'seqNum',
				editor : {
					fieldLabel:'编号',
					maxLength : 50
				},
				renderer: function(value, metaData, record, rowIndex, colIndex, store) {
					var trainTypeCode = Ext.isEmpty(record.data.trainTypeCode) ? "" : record.data.trainTypeCode ;
					var trainNo =  Ext.isEmpty(record.data.trainNo)?"":record.data.trainNo;
					var trainInfo = Ext.isEmpty(trainNo) ? "未录入" : trainTypeCode + " " + trainNo ;
					return "第"+value+"辆:【"+trainInfo+"】";
				},
				searcher : {
					anchor : '98%'
				}
			}, {
				header : '车辆车型',
				dataIndex : 'trainTypeName',
				hidden : true,
				editor : {
					maxLength : 50
				},
				searcher : {
					anchor : '98%'
				}
			},
			{header : '车辆编码',dataIndex : 'trainTypeCode',hidden : true,editor : { xtype:"hidden" }},
			{header : '车辆状态',dataIndex : 'rdpRecordStatus',hidden : true,editor : { xtype:"hidden" }},
			{header : '车辆列检实例ID',dataIndex : 'rdpIdx',hidden : true,editor : { xtype:"hidden" }},
				{
				header : '车辆车号',
				dataIndex : 'trainNo',
				hidden : true,
				editor : {
					maxLength : 50
				},
				searcher : {
					anchor : '98%'
				}
			}],
	searchFn: function(searchParam){ 
		ZbglRdpPlanRecordWin.searchParam = searchParam ;
        ZbglRdpPlanRecordWin.grid.store.load();
	}
});

ZbglRdpPlanRecordWin.grid.un('rowdblclick',ZbglRdpPlanRecordWin.grid.toEditFn,ZbglRdpPlanRecordWin.grid);
ZbglRdpPlanRecordWin.grid.store.setDefaultSort("seqNum", "ASC");
//定义点击确定按钮的操作
ZbglRdpPlanRecordWin.submit = function(){
	alert("请覆盖方法（ZbglRdpPlanRecordWin.submit）！");
}


//定义选择窗口
ZbglRdpPlanRecordWin.selectWin = new Ext.Window({
	title:"作业车辆选择", width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
	maximizable:false, items:[ZbglRdpPlanRecordWin.grid],modal:true,
	buttons: [{
		text : "确定",iconCls : "saveIcon", handler: function(){
			ZbglRdpPlanRecordWin.submit(); 
		}
	},{
        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ ZbglRdpPlanRecordWin.selectWin.hide(); }
	}],
	listeners:{
		"show":function(){
			
		}
	}
});
	
//查询前添加过滤条件
ZbglRdpPlanRecordWin.grid.store.on('beforeload' , function(){
	var searchParam = ZbglRdpPlanRecordWin.searchParam ;
	searchParam.rdpPlanIdx = ZbglRdpPlanRecordWin.rdpPlanIdx;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

// 添加加载结束事件
ZbglRdpPlanRecordWin.grid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0 && Ext.isEmpty(ZbglRdpPlanRecordWin.recordIds)){
		ZbglRdpPlanRecordWin.grid.getSelectionModel().clearSelections();
	}
	// 选中已选的数据
	if(records.length > 0 && !Ext.isEmpty(ZbglRdpPlanRecordWin.recordIds)){
		var recordIds = ZbglRdpPlanRecordWin.recordIds.split(",");
	    var selectedRecords = [];
    	ZbglRdpPlanRecordWin.grid.getStore().each(function(record){
    		for(var i = 0 ; i < recordIds.length; i++){
    			if(recordIds[i] == record.get('idx')){
    				this.push(record);
    				break;
    			}
    		}
    	}, selectedRecords);
    	if(selectedRecords.length > 0){
    		ZbglRdpPlanRecordWin.grid.getSelectionModel().selectRecords(selectedRecords);
    	}else{
    		ZbglRdpPlanRecordWin.grid.getSelectionModel().clearSelections();
    	}
	}
});

});