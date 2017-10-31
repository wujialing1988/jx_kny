/**
 * 班次交接
 */
Ext.onReady(function(){
	
Ext.namespace('Runningkm');                       //定义命名空间

//定义全局变量保存查询条件
Runningkm.searchParam = {} ;

Runningkm.labelWidth = 100;                        //表单中的标签名称宽度
Runningkm.fieldWidth = 130;                       //表单中的标签宽度

/**
 * 设置初始走行公里表单
 */
Runningkm.setRunningKmForm = new Ext.form.FormPanel({
	border:false, baseCls:'x-plain',
	padding:'10px',
	labelWidth:Runningkm.labelWidth,
	defaults:{
		xtype:'numberfield',
		anchor:'95%'
	},
	items:[{
		name:'newRunningKm', 
		id:'id_newRunningKm',
		fieldLabel:'初始走行公里',
        allowDecimals:true,
        allowNegative:false,
        minValue:0,
        maxValue:999999999,
        allowBlank:false		
	}]
});

/**
 * 设置初始化走行公里窗口
 */
Runningkm.setRunningKmWin = new Ext.Window({
		title:'设置初始化走行公里',
		plain:true, 
		modal:true,
		closeAction:'hide',
		layout:'fit',
		width:400, height:120,
		items:[Runningkm.setRunningKmForm],
		buttonAlign: 'center',
		buttons:[{
			text:"保存", iconCls:'saveIcon', handler:function() {
				//表单验证是否通过
	            var form = Runningkm.setRunningKmForm.getForm();
	            if (!form.isValid()) return;
				var newRunningKm = Ext.getCmp('id_newRunningKm').getValue();
				//判断是否选择了数据
				var grid = Runningkm.grid;
				if(!$yd.isSelectedRecord(grid)) {
					return;
				}
				var ids = $yd.getSelectedIdx(grid);	
				var cfg = {
			        url: ctx + "/runningKM!setRunningKm.action", 
					params: {ids: Ext.util.JSON.encode(ids),newRunningKm:newRunningKm},
			        timeout: 600000,
			        success: function(response, options){
			        	if(processTips) hidetip();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null && result.success == true) {
			                alertSuccess();
			                Runningkm.setRunningKmWin.hide();
			                Runningkm.grid.store.load();
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			        	if(processTips) hidetip();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
			    };
			    Ext.Msg.confirm("提示  ", "确定批量设置初始走行公里？ ", function(btn){
			        if(btn != 'yes')    return;
			        showtip();
			        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			    });				
			}
		}, {
			text:"关闭", iconCls:'colseIcon', hidden:true, handler:function() {
				Runningkm.setRunningKmWin.hide();
			}
		}]
	});

/**
 * 批量设置初始走行公里
 */
Runningkm.setRunningKm = function(){
	//判断是否选择了数据
	var grid = Runningkm.grid;
	if(!$yd.isSelectedRecord(grid)) {
		return;
	}
	Ext.getCmp('id_newRunningKm').reset();
	Runningkm.setRunningKmWin.show();
};

/**
 * 同步车辆信息
 */
Runningkm.synchronizeData = function(){
	var cfg = {
        url: ctx + "/runningKM!synchronizeData.action", 
		params: {
			
		},
        timeout: 600000,
        success: function(response, options){
        	if(processTips) hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                Runningkm.grid.store.load();
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

Runningkm.queryTimeout ;

Runningkm.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/runningKM!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/runningKM!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/runningKM!logicDelete.action',            //删除数据的请求URL
    labelWidth:Runningkm.labelWidth,
    singleSelect: false, 
    tbar:[
    	{	
			xtype:'textfield', id:'query_running_km', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
	    		keyup: function(filed, e) {
	    			if (Runningkm.queryTimeout) {
	    				clearTimeout(Runningkm.queryTimeout);
	    			}
	    			
	    			Runningkm.queryTimeout = setTimeout(function(){
	    				Runningkm.grid.store.load();
	    			}, 1000);					
	    		}
			}	
		},'-',{
					text : '批量设置初始走行公里',		
					iconCls:"chart_organisationIcon",
			    	handler: function(){
			    		Runningkm.setRunningKm();
			    	}
		},'-',{
					text : '同步车辆信息',		
					iconCls:"tbar_synchronizeIcon",
			    	handler: function(){
			    		Runningkm.synchronizeData();
			    	}
		},'-','<div><span style="color:green;">*双击编辑设置初始化走行公里!</span></div>'
    ],    
	fields: [{
			dataIndex:"idx", hidden:true,editor: { xtype:'hidden' }
		},{
			dataIndex:"repairType", hidden:true,editor: { xtype:'hidden' }
		},{
			dataIndex:"trainTypeIdx", hidden:true,editor: { xtype:'hidden' }
		},{
			header: "车型", dataIndex: "trainType"
		},{
			header: "车号", dataIndex: "trainNo"
		},{
			header: "初始化走行公里(km)", dataIndex: "newRunningKm",editor: {    
									fieldLabel:'初始走行公里',
									xtype:'numberfield',
									allowDecimals:true,
							        allowNegative:false,
							        minValue:0,
							        maxValue:999999999,
							        allowBlank:false	 
        					}
		},{
			header: "行车走行公里(km)", dataIndex: "recentlyRunningKm",editor: {    
									fieldLabel:'行车走行公里',
									xtype:'numberfield',
									allowDecimals:true,
							        allowNegative:false,
							        minValue:0,
							        maxValue:999999999,
							        allowBlank:false	 
        					}
		},{
			header: "累计走行公里(km)", dataIndex: "recentlyRunningKm",renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				var newRunningKm = record.get('newRunningKm');
				var result = parseFloat(value) + parseFloat(newRunningKm) ;
				return result ;
			},editor: { xtype:'hidden',disabled:true }
		}],
	searchFn: function(searchParam){ 
		Runningkm.searchParam = searchParam ;
        Runningkm.grid.store.load();
	},
	afterShowEditWin: function(record, rowIndex){
		 // 解除字段只读
		 this.setReadOnlyColumns(['trainType','trainNo','recentlyRunningKm'],true);
	},	
	afterSaveSuccessFn: function(result, response, options){
		Runningkm.grid.saveWin.hide();
        Runningkm.grid.store.reload();
        alertSuccess();
    }
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:Runningkm.grid });
	
//查询前添加过滤条件
Runningkm.grid.store.on('beforeload' , function(){
		var whereList = [] ;
		var queryKey = Ext.getCmp('query_running_km').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (TRAIN_TYPE LIKE '%" + queryKey + "%'"
					+ " OR TRAIN_NO LIKE '%" + queryKey + "%') ";
			whereList.push({ sql: sql, compare: Condition.SQL});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

});