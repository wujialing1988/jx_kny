//车号选择
Ext.onReady(function(){
Ext.namespace('TrainNoSelectWin'); 
TrainNoSelectWin.idx = '';//普查整治项idx
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
TrainNoSelectWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
TrainNoSelectWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!findJczlTrainInfoList.action',                 //装载列表数据的请求URL
    page:false,
    hideRowNumberer: true,
    tbar: [{
        xtype:"label", text:"  车型： " 
    },{
    	id : "TrainType_combo_idx",
		xtype: "TrainType_combo",
		fieldLabel: "下车车型",
		hiddenName: "unloadTrainTypeIdx",
		displayField: "shortName", valueField: "typeID",
		pageSize: 20, minListWidth: 50,
		editable:false
	},{
        xtype:"label", text:"  车号： " 
    },{
        id:"trainNo",xtype: "textfield" ,maxLength: 50,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
			TrainNoSelectWin.grid.store.load();
    	}
    },'-',{
    	text: "重置", iconCls: "saveIcon", handler: function(){
			Ext.getCmp("trainNo").setValue("");
            Ext.getCmp("TrainType_combo_idx").clearValue();
    		TrainNoSelectWin.grid.store.load();
    	}
   	},'-',{
	   	text: "确定", iconCls: "saveIcon", handler: function(){
	   		var grid = TrainNoSelectWin.grid;
    		if(!$yd.isSelectedRecord(grid)) return;//æªéæ©è®°å½ï¼ç´æ¥è¿å
	   		TrainNoSelectWin.submit();
	   		TrainNoSelectWin.selectWin.hide();
	   	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName' ,searcher: { hidden: true }
	},{
		header:'车号', dataIndex:'trainNo', editor:{   }
	},{
		header:'车型idx', dataIndex:'trainTypeIDX',hidden:true, editor:{   }
	}]
});

// 表格数据加载前的参数设置
TrainNoSelectWin.grid.store.on('beforeload', function(){
	var trainNo = Ext.getCmp("trainNo").getValue();
	var trainTypeIDX = Ext.getCmp("TrainType_combo_idx").getValue();
	var entityJson = {};
	entityJson.trainNo = trainNo;
	entityJson.idx = TrainNoSelectWin.idx;
	entityJson.trainTypeIDX = trainTypeIDX;
	this.baseParams.entityJson = Ext.util.JSON.encode(entityJson);
});	

TrainNoSelectWin.selectWin = new Ext.Window({
	title:"选择车号", maximizable:true, width:600, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:TrainNoSelectWin.grid
});
//移除监听
TrainNoSelectWin.grid.un('rowdblclick', TrainNoSelectWin.grid.toEditFn, TrainNoSelectWin.grid);
//确认提交方法，后面可覆盖此方法完成查询
TrainNoSelectWin.submit = function(){alert("请覆盖，TrainNoSelectWin.submit 方法完成自己操作业务！");};
});