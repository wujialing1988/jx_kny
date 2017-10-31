/**
 * 承修车型对应修程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('UndertakeTrainTypeRC');                       //定义命名空间
UndertakeTrainTypeRC.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/undertakeTrainTypeRC!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/undertakeTrainTypeRC!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar: [{
    	text:"选择修程", iconCls: "addIcon" , handler: function(){
    		XC.selectWin.show();
    		XC.grid.store.load();
    	}
    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'修程编码', dataIndex:'rcIDX', editor:{ xtype:'textfield', maxLength:50 }
	},{
		header:'修程名称', dataIndex:'xcName', editor:{ xtype:'textfield', maxLength:50 }
	}],
	searchFn: function(searchParam){ 
        UndertakeTrainTypeRC.grid.store.load();
	},
	afterDeleteFn: function(){  //删除后调用的函数方法
		UndertakeTrainType.grid.store.load();
	}
});
//移除侦听器
UndertakeTrainTypeRC.grid.un('rowdblclick', UndertakeTrainTypeRC.grid.toEditFn, UndertakeTrainTypeRC.grid);
//查询前添加过滤条件
UndertakeTrainTypeRC.grid.store.on('beforeload' , function(){
	var searchParam = {} ;
	searchParam.undertakeTrainTypeIDX = UndertakeTrainType.idx ; //承修车型主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

//确认提交方法，后面可覆盖此方法完成查询
XC.submit = function(){
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
    	data.xcName = objData[i].get("xcName");  //修程编码
    	data.trainShortName = UndertakeTrainType.trainShortName;  //车型简称
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
	var queryParams = {} ;
	queryParams.TrainTypeIdx = UndertakeTrainType.trainTypeIDX ; //承修车型主键
	queryParams.xcName = Ext.getCmp("xcName").getValue() ; //修程名称
	this.baseParams.undertakeTrainTypeIDX = UndertakeTrainType.idx;  //承修车型主键
	this.baseParams.queryParams =  Ext.util.JSON.encode(queryParams);  
});

});