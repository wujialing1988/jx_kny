/**
 * 修程预警
 */
Ext.onReady(function(){
	
Ext.namespace('RepairWarningKC');                       //定义命名空间

//定义全局变量保存查询条件
RepairWarningKC.searchParam = {} ;

/**
 * 同步车辆信息
 */
RepairWarningKC.synchronizeData = function(){
	var cfg = {
        url: ctx + "/repairWarningKC!synchronizeData.action", 
		params: {
			
		},
        timeout: 600000,
        success: function(response, options){
        	if(processTips) hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                RepairWarningKC.grid.store.load();
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

/**
 * 重新计算修程
 */
RepairWarningKC.reCompute = function(){
	var cfg = {
	        url: ctx + "/repairWarningKC!reCompute.action", 
			params: {
				
			},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                RepairWarningKC.grid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "确定重新计算下次修程？ ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });	
}

RepairWarningKC.queryTimeout ;

RepairWarningKC.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairWarningKC!findKCRepairWarningList.action',                 //装载列表数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    viewConfig: false,   
    tbar:[
        '车型车号：',
    	{	
			xtype:'textfield', id:'queryInput', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
	    		keyup: function(filed, e) {
	    			if (RepairWarningKC.queryTimeout) {
	    				clearTimeout(RepairWarningKC.queryTimeout);
	    			}
	    			
	    			RepairWarningKC.queryTimeout = setTimeout(function(){
	    				RepairWarningKC.grid.store.load();
	    			}, 1000);					
	    		}
			}	
		},'-','只看预警：',{
			xtype: 'checkbox', checked: false, id:'repair_type',
			listeners: {
				check: function(me) {
					RepairWarningKC.grid.store.load();
				}
			}
		},{
	    	text: "查询" ,iconCls:"searchIcon", handler: function(){
	    		RepairWarningKC.grid.store.load();
	    		}
	    },'-',{
	    	text: "重置" ,iconCls:"resetIcon", handler: function(){
	    		Ext.getCmp('queryInput').reset();
	    		RepairWarningKC.grid.store.load();
	    		}
	    },'-',{
			text : '同步车辆信息',		
			iconCls:"tbar_synchronizeIcon",
	    	handler: function(){
	    		RepairWarningKC.synchronizeData();
	    	}
		},'-',{
			text: "计算下次修程",
			iconCls: "application-vnd-ms-excel",
			handler: function(){
				RepairWarningKC.reCompute();
			}
		}
    ],  
	plugins: [new Ext.ux.grid.ColumnHeaderGroup({
		rows: [[{
			header: "<span style='font-weight: bold;'>车辆信息</span>", colspan: 9, align: 'center'
		}, {
			header: "<span style='font-weight: bold;'>A1</span>", colspan: 3, align: 'center'
		}, {
			header: "<span style='font-weight: bold;'>A2</span>", colspan: 3, align: 'center'
		}, {
			header: "<span style='font-weight: bold;'>A3</span>", colspan: 3, align: 'center'
		}, {
			header: "<span style='font-weight: bold;'>A4</span>", colspan: 3, align: 'center'
		}, {
			header: "<span style='font-weight: bold;'>A5</span>", colspan: 3, align: 'center'
		}]],
		hierarchicalColMenu: true
	})],
	fields: [{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	},{
		header:'车型ID', dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},{
		header:'车型', dataIndex:'trainType',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return value;
			}
	},{
		header:'车号', dataIndex:'trainNo',width: 70,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return value;
			}
	},{
		header:'出厂日期', dataIndex:'leaveDate',width: 90,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'累计走行', dataIndex:'totalkm',width: 70,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'下次修程类型', dataIndex:'repairWarningType',hidden:true ,editor: { xtype:"hidden" }
	},{
		header:'下次修程', dataIndex:'repairClassName',width: 90,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			var repairWarningType = record.get("repairWarningType"); // 10 走行判断 20 时间判断
			if('10' == repairWarningType){
				return value;
			}else if('20' == repairWarningType){
				return value;
			}
			return value;
		}
	},{
		header:'检修', dataIndex:'beforeA1Date',width: 85,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'到期', dataIndex:'nextA1Date',width: 85,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'走行', dataIndex:'a1km',width: 75,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'检修', dataIndex:'beforeA2Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'到期', dataIndex:'nextA2Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'走行', dataIndex:'a2km',width: 75,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'检修', dataIndex:'beforeA3Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'到期', dataIndex:'nextA3Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'走行', dataIndex:'a3km',width: 75,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'检修', dataIndex:'beforeA4Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'到期', dataIndex:'nextA4Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'走行', dataIndex:'a4km',width: 75,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'检修', dataIndex:'beforeA5Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'到期', dataIndex:'nextA5Date',width: 80,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	},{
		header:'走行', dataIndex:'a5km',width: 75,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value;
		}
	}],
	searchFn: function(searchParam){ 
		RepairWarningKC.searchParam = searchParam ;
        RepairWarningKC.grid.store.load();
	}
});

// 取消表格双击进行编辑的事件监听
RepairWarningKC.grid.un('rowdblclick', RepairWarningKC.grid.toEditFn, RepairWarningKC.grid);

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:RepairWarningKC.grid });
	
//查询前添加过滤条件
RepairWarningKC.grid.store.on('beforeload' , function(){
		var repairTypeChecked = Ext.getCmp('repair_type').checked ;
		var queryInput = Ext.getCmp('queryInput').getValue();
		this.baseParams.repairType = repairTypeChecked ;
		this.baseParams.queryInput = queryInput ;
});

});