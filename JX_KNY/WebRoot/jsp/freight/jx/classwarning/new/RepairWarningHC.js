/**
 * 修程预警
 */
Ext.onReady(function(){
	
Ext.namespace('RepairWarningHC');                       //定义命名空间

//定义全局变量保存查询条件
RepairWarningHC.searchParam = {} ;

/**
 * 同步车辆信息
 */
RepairWarningHC.synchronizeData = function(){
	var cfg = {
        url: ctx + "/repairWarningHC!synchronizeData.action", 
		params: {
			
		},
        timeout: 600000,
        success: function(response, options){
        	if(processTips) hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                RepairWarningHC.grid.store.load();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
        	if(processTips) hidetip();
	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	    }
    };
    Ext.Msg.confirm("提示  ", "确定同步车辆信息？ ", function(btn){
        if(btn != 'yes')    return;
        showtip();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });	
}

RepairWarningHC.queryTimeout ;

RepairWarningHC.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairWarningHC!findHCRepairWarningList.action',                 //装载列表数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    viewConfig: {forceFit: true},    
    tbar:[
        '车型车号：',
    	{	
			xtype:'textfield', id:'queryInput', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
	    		keyup: function(filed, e) {
	    			if (RepairWarningHC.queryTimeout) {
	    				clearTimeout(RepairWarningHC.queryTimeout);
	    			}
	    			
	    			RepairWarningHC.queryTimeout = setTimeout(function(){
	    				RepairWarningHC.grid.store.load();
	    			}, 1000);					
	    		}
			}	
		},'-','下次厂修：',{
			id : "planDayFx",width:90, xtype: 'my97date',format:'Y-m', my97cfg: {dateFmt:'yyyy-MM'}, initNow: false
		},'-','下次段修：',{
			id : "planDayDx",width:90, xtype: 'my97date',format:'Y-m', my97cfg: {dateFmt:'yyyy-MM'}, initNow: false
		},'-',{
	    	text: "查询" ,iconCls:"searchIcon", handler: function(){
	    		RepairWarningHC.grid.store.load();
	    		}
	    },'-',{
	    	text: "重置" ,iconCls:"resetIcon", handler: function(){
	    		Ext.getCmp('queryInput').reset();
	    		Ext.getCmp('planDayFx').reset();
	    		Ext.getCmp('planDayDx').reset();
	    		RepairWarningHC.grid.store.load();
	    		}
	    },'-',{
			text : '同步车辆信息',		
			iconCls:"tbar_synchronizeIcon",
	    	handler: function(){
	    		RepairWarningHC.synchronizeData();
	    	}
		}
    ],    
	fields: [
     	{
		header:'车型ID', dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车型', dataIndex:'trainType',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return value;
			}
	},{
		header:'车号', dataIndex:'trainNo',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return value;
			}
	},{
		header:'出厂时间', dataIndex:'leaveDate',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'上次厂修时间', dataIndex:'beforeCxDate',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'下次厂修时间', dataIndex:'nextCxDate',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'上次段修时间', dataIndex:'beforeCxDate',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'下次段修时间', dataIndex:'nextDxDate',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		RepairWarningHC.searchParam = searchParam ;
        RepairWarningHC.grid.store.load();
	}
});

// 取消表格双击进行编辑的事件监听
RepairWarningHC.grid.un('rowdblclick', RepairWarningHC.grid.toEditFn, RepairWarningHC.grid);

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:RepairWarningHC.grid });
	
//查询前添加过滤条件
RepairWarningHC.grid.store.on('beforeload' , function(){
		var queryInput = Ext.getCmp('queryInput').getValue();
		var planDayFx = Ext.getCmp('planDayFx').getValue();
		var planDayDx = Ext.getCmp('planDayDx').getValue();
		this.baseParams.queryInput = queryInput ;
		this.baseParams.planDayFx = planDayFx ;
		this.baseParams.planDayDx = planDayDx ;
});

});