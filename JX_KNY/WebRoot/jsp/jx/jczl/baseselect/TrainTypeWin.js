//车型定义
Ext.onReady(function(){
Ext.namespace('TrainType'); 
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
TrainType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
TrainType.searchParam = {} ;
TrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainType!pageList.action',                 //装载列表数据的请求URL
    tbar: [{
        xtype:"label", text:"  车型简称： " 
    },{
        id:"shortName",xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		var searchParam = {} ;
			TrainType.searchParam.shortName = Ext.getCmp("shortName").getValue();
			TrainType.grid.store.load();
    	}
    },'-',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		TrainType.submit();
    	}
    }],
	fields: [{
		header:'车型代码', dataIndex:'typeID' ,searcher: { hidden: true }
	},{
		header:'车型名称', dataIndex:'typeName', editor:{   }
	},{
		header:'简称', dataIndex:'shortName', editor:{   }
	}]
});
TrainType.selectWin = new Ext.Window({
	title:"选择车型", maximizable:true, width:500, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:TrainType.grid
});

//查询前添加过滤条件
TrainType.grid.store.on('beforeload' , function(){
	var searchParam = TrainType.searchParam ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
})
//移除监听
TrainType.grid.un('rowdblclick', TrainType.grid.toEditFn, TrainType.grid);
//确认提交方法，后面可覆盖此方法完成查询
TrainType.submit = function(){alert("请覆盖，TrainType.submit 方法完成自己操作业务！");};
;

/*TrainType.submit = function(){
	var sm = TrainType.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.trainTypeIDX = objData[i].get("typeID");  //车型编码（主键）
    	data.trainTypeShortName = objData[i].get("shortName") ;
    	data.undertakeOrgId = systemOrgid ;  //当前登录人的单位IDX
    	data.undertakeOrgName = systemOrgname ;
        dataAry.push(data);
    }
    TrainType.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/undertakeTrainType!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
          	TrainType.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                TrainType.grid.store.reload();
                UndertakeTrainType.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            TrainType.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};
//车型选择前的过滤
TrainType.grid.store.on('beforeload' , function(){
	var searchParam = TrainType.searchParam ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.undertakeTrainTypeIDX = UndertakeTrainType.idx; //承修车型主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});*/

});