/**
 * 机车交接情况模板 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoModelItemResultSelect');                       //定义命名空间
ZbglHoModelItemResultSelect.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglHoModelItemResult!pageQuery.action',                 //装载列表数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    tbar:[{
    	text: "确定", iconCls: "application_goIcon", handler: function(){
    		if(!$yd.isSelectedRecord(ZbglHoModelItemResultSelect.grid)) return;//未选择记录，直接返回
	        var data = ZbglHoModelItemResultSelect.grid.selModel.getSelections();
	        var dataStr = "";
        	for(var i = 0; i < data.length; i++){	
        		var record = data[i];
        		dataStr+= data[i].get("handOverResultDesc")+";";	
        	}
        	dataStr = dataStr.substring(0,dataStr.length-1);
        	var record = HandOverModelList.grid.getStore().getAt(rowNum);
        	if(!Ext.isEmpty(record.data.handOverResultDesc))
        		record.data.handOverResultDesc+= ";" + dataStr;
        	else
        		record.data.handOverResultDesc = dataStr;
			HandOverModelList.grid.getView().refreshRow(record);
        	ZbglHoModelItemResultSelect.selectWin.hide();
    	}
    },{
    	text: "关闭", iconCls: "closeIcon", handler: function(){
    		ZbglHoModelItemResultSelect.selectWin.hide();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车交接项ID', dataIndex:'handOverItemIDX', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'机车交接情况', dataIndex:'handOverResultDesc', editor:{  maxLength:100 }
	}]
});
ZbglHoModelItemResultSelect.grid.un('rowdblclick', ZbglHoModelItemResultSelect.grid.toEditFn, ZbglHoModelItemResultSelect.grid);
ZbglHoModelItemResultSelect.selectWin = new Ext.Window({
    title:"选择交接情况", width:250, height:300, plain:true, closeAction:"hide",  items: ZbglHoModelItemResultSelect.grid, layout: 'fit'
});
});