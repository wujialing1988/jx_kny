/**
 * 修程预警
 */
Ext.onReady(function(){
	
Ext.namespace('RepairClassWarning');                       //定义命名空间

//定义全局变量保存查询条件
RepairClassWarning.searchParam = {} ;

RepairClassWarning.queryTimeout ;

RepairClassWarning.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairClassWarning!findHCRepairClassWarningList.action',                 //装载列表数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    viewConfig: { 
    	getRowClass : function(record, rowIndex, p, store){
//    		var totalrm = record.get('totalrm'); // 累计
//    		var maxRunningKm = record.get('maxRunningKm'); // 最大
//    		if(parseFloat(maxRunningKm) > parseFloat(totalrm)){
//				return 'Warning_row_highlight';
//    		}else{
//				return 'Warning_row_red';
//    		}
        } 
    },    
    tbar:[
    	{	
			xtype:'textfield', id:'query_train_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
	    		keyup: function(filed, e) {
	    			if (RepairClassWarning.queryTimeout) {
	    				clearTimeout(RepairClassWarning.queryTimeout);
	    			}
	    			
	    			RepairClassWarning.queryTimeout = setTimeout(function(){
	    				RepairClassWarning.grid.store.load();
	    			}, 1000);					
	    		}
			}	
		},'-','<div><span style="color:red;">*红色</span>表示该车辆累计天数超限，未进行检修;<span style="color:#FF7F00;">*橙色</span>表示该车辆累计天数到达警戒线，需进行检修！</div>'
    ],    
	fields: [
     	{
		header:'车型ID', dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车型', dataIndex:'trainType',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
	    		var totalrm = record.get('totalrm'); // 累计
	    		var maxRunningKm = record.get('maxRunningKm'); // 最大
	    		if(parseFloat(maxRunningKm) > parseFloat(totalrm)){
					metaData.css = 'Warning_row_orange';
	    		}else{
					metaData.css = 'Warning_row_red';
	    		}
				return value;
			}
	},
     	{
		header:'车号', dataIndex:'trainNo',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
	    		var totalrm = record.get('totalrm'); // 累计
	    		var maxRunningKm = record.get('maxRunningKm'); // 最大
	    		if(parseFloat(maxRunningKm) > parseFloat(totalrm)){
					metaData.css = 'Warning_row_orange';
	    		}else{
					metaData.css = 'Warning_row_red';
	    		}
				return value;
			}
	},
     	{
		header:'修程编码', dataIndex:'repairClass',hidden:true,width: 120,editor: {}
	},
     	{
		header:'修程', dataIndex:'repairClassName',width: 120,editor: {}
	},
     	{
		header:'修次编码', dataIndex:'repairOrder',hidden:true,width: 120,editor: {}
	},
     	{
		header:'修次', dataIndex:'repairOrderName',width: 120,editor: {}
	}, {
		header:'累计天数(d)', dataIndex:'totalrm',width: 120,editor: {}
	}, {
		header:'最大天数(d)', dataIndex:'maxRunningKm',width: 120,editor: {}
	}, {
		header:'超限/临限天数(d)', dataIndex:'maxRunningKm',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				var totalrm = record.get('totalrm');
				var result = parseFloat(value) - parseFloat(totalrm) ;
				return Math.abs(result);
			}
	}, {
		header:'预警提示', dataIndex:'maxRunningKm',width: 200,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				var msg = "" ;
				var totalrm = record.get('totalrm'); // 累计
	    		var maxRunningKm = record.get('maxRunningKm'); // 最大
	    		if(parseFloat(maxRunningKm) > parseFloat(totalrm)){
					msg = "<span style='color:#FF7F00;'>车辆累计天数已达到警戒标准，请提前安排检修！</span>" ;
	    		}else{
					msg = "<span style='color:red;'>车辆累计天数已超过修程限度，请尽快安排检修！</span>" ;
	    		}
				return '<marquee direction="left" scrollamount="1">'+msg+'</marquee>';
			}
	}, 
			{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		RepairClassWarning.searchParam = searchParam ;
        RepairClassWarning.grid.store.load();
	}
});

// 取消表格双击进行编辑的事件监听
RepairClassWarning.grid.un('rowdblclick', RepairClassWarning.grid.toEditFn, RepairClassWarning.grid);

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:RepairClassWarning.grid });
	
//查询前添加过滤条件
RepairClassWarning.grid.store.on('beforeload' , function(){
		var searchParam = RepairClassWarning.searchParam ;
		if(!Ext.isEmpty(Ext.getCmp('query_train_no').getValue())){
			searchParam.trainNo = Ext.getCmp('query_train_no').getValue();
		}
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});