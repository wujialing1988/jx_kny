//车型定义
Ext.onReady(function(){
//修程定义
Ext.namespace('XC');                       //定义命名空间
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
XC.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
XC.grid = new Ext.yunda.Grid({ 
//    loadURL: ctx + '/xC!pageList.action',                 //装载列表数据的请求URL
    loadURL: ctx + '/trainRC!pageListForRCCombo.action',                 //装载列表数据的请求URL
    tbar: [{
        xtype:"label", text:"  修程名称： " 
    },{
        id:"xcName",xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		var searchParam = {} ;
			searchParam.xcName = Ext.getCmp("xcName").getValue();
			XC.grid.store.load(
//				{
//				params: { queryParams: Ext.util.JSON.encode(searchParam) }
//			}
			);
    	}
    },'-',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		XC.submit();
    	}
    }],
	fields: [{
		header:'修程编码', dataIndex:'xcID', editor:{   }
	},{
		header:'修程名称', dataIndex:'xcName', editor:{   }
	}],
	searchFn: function(searchParam){ 
        XC.grid.store.load();
	}
});
XC.selectWin = new Ext.Window({
	title:"选择修程", width:380, height:280, closeAction:"hide", modal:true, layout:"fit", items:XC.grid
});
//移除监听
XC.grid.un('rowdblclick', XC.grid.toEditFn, XC.grid);
//确认提交方法，后面可覆盖此方法完成查询
XC.submit = function(){alert("请覆盖，XC.submit 方法完成自己操作业务！");};

/*XC.submit = function(){
	var sm = XC.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.undertakeTrainTypeIDX = UndertakeTrainType.idx ; //承修车型主键
    	data.rcIDX = objData[i].get("xcID");  //修程编码
        dataAry.push(data);
    }
    XC.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/undertakeTrainTypeRC!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
          	XC.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                XC.grid.store.reload();
                UndertakeTrainTypeRC.grid.store.reload();
                UndertakeTrainType.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            XC.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};
//修程选择前的过滤
XC.grid.store.on('beforeload' , function(){
	this.baseParams.undertakeTrainTypeIDX = UndertakeTrainType.idx;  //承修车型主键
});*/

});