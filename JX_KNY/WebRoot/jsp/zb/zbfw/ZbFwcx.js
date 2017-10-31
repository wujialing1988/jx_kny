/**
 * 整备范围适用车型 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbFwcx');                       //定义命名空间
   /* ************* 定义全局变量开始 ************* */
	ZbFwcx.zbfwIDX = "";	// 整备范围主键
	ZbFwcx.searchParams = {};
   /* ************* 定义全局变量结束 ************* */
	
ZbFwcx.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbFwcx!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbFwcx!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbFwcx!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar: [{text:"选择车型", iconCls: "addIcon" ,
    handler: function(){
    		UndertakeTrainTypeWin.selectWin.show();
    		UndertakeTrainTypeWin.grid.store.load();}
    },'delete'],
	fields: [{
		header:'主键', dataIndex:'idx',hidden:true,editor: { xtype:'hidden' }
	},{
		header:'整备范围主键', dataIndex:'zbfwIDX',hidden:true,editor:{  maxLength:50 },editor: { xtype:'hidden' }
	},{
		header:'车型编码', dataIndex:'cXBM', editor:{  maxLength:8 }
	},{
		header:'车型拼音码', dataIndex:'cXPYM', editor:{  maxLength:8 }
	}]
});
//确认提交方法，后面可覆盖此方法完成查询
UndertakeTrainTypeWin.submit = function(){
	var sm = UndertakeTrainTypeWin.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.cXBM = objData[i].get("trainTypeIDX");  //车型编码（主键）
    	data.cXPYM = objData[i].get("trainTypeShortName") ;
    	data.zbfwIDX = ZbFwcx.zbfwIDX;
        dataAry.push(data);
    }
    UndertakeTrainTypeWin.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/zbFwcx!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
          	UndertakeTrainTypeWin.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                UndertakeTrainTypeWin.grid.store.reload();
                ZbFwcx.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            UndertakeTrainTypeWin.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};
//车型选择前的过滤
UndertakeTrainTypeWin.grid.store.on('beforeload' , function(){
	var searchParam = UndertakeTrainTypeWin.searchParam ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
// 表格数据加载前的参数设置
	ZbFwcx.grid.store.on('beforeload', function(){
		ZbFwcx.searchParams.zbfwIDX = ZbFwcx.zbfwIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbFwcx.searchParams);
   });

//页面自适应布局
//var viewport = new Ext.Viewport({ layout:'fit', items:ZbFwcx.grid });
});