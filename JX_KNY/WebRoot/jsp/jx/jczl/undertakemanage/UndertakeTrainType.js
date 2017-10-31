/**
 * 承修车型 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('UndertakeTrainType');                       //定义命名空间
//定义全局变量承修车型主键
UndertakeTrainType.idx = "" ;
UndertakeTrainType.trainTypeIDX = "" ;
UndertakeTrainType.trainShortName = "" ;
UndertakeTrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/undertakeTrainType!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/undertakeTrainType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2 ,
    tbar: [{
    	text:"选择车型", iconCls: "addIcon" , handler: function(){
    		TrainType.selectWin.show();
    		TrainType.grid.store.load();
    	}
    },'delete','refresh','-',{
    	text:"双击行设置承修机车和承修修程", xtype:"label"
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'承修车型简称', dataIndex:'trainTypeShortName', editor:{ 
			fieldLabel:'车型简称', readOnly:true, style:'border:none;background:none;font-weight:bold;'
		}
	},{
		header:'承修车型名称', dataIndex:'typeName', editor:{ 
			fieldLabel:'车型名称', readOnly:true, style:'border:none;background:none;font-weight:bold;' 
		}
	},{
		header:'承修修程', dataIndex:'rcGroup', editor:{ xtype:'hidden' , maxLength:32 }
	},{
		header:'承修车型编码', dataIndex:'trainTypeIDX', hidden:true, editor: { xtype:'hidden' }
	}],
	afterShowEditWin: function(record, rowIndex){  //双击查看显示之后编辑页面之后的操作
		UndertakeTrainType.idx = record.get("idx");
		UndertakeTrainType.trainTypeIDX = record.get("trainTypeIDX");
		UndertakeTrainType.trainShortName = record.get("trainTypeShortName");
		this.saveWin.setTitle('设置承修修程');
//		this.disableAllColumns();
		UndertakeTrain.grid.store.load();
		UndertakeTrainTypeRC.grid.store.load();
	}
});
//移除侦听器
//UndertakeTrainType.grid.un('rowdblclick', UndertakeTrainType.grid.toEditFn, UndertakeTrainType.grid);
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:UndertakeTrainType.grid });
UndertakeTrainType.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title:"承修机车", border:false, layout:"fit", items: UndertakeTrain.grid
	        },{
	            title:"承修修程", border:false, layout:"fit", items: UndertakeTrainTypeRC.grid
	        }]
	});
//覆盖创建的窗口方法
UndertakeTrainType.grid.createSaveWin = function(){
    if(UndertakeTrainType.grid.saveForm == null) UndertakeTrainType.grid.createSaveForm();
	UndertakeTrainType.grid.saveWin = new Ext.Window({
		title: "设置承修机车和承修修程", maximizable:true, width:800, height:600, layout: "border", 
		closeAction: "hide", modal: false, 
		defaults: {
			layout : 'fit', border: false
		},
		items: [{
            region: 'north', frame:true, height: 58, bodyBorder: false,items:[UndertakeTrainType.grid.saveForm]
        },{
            region : 'center', items : [ UndertakeTrainType.tabs ]
        }],
        buttonAlign: "center",
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){UndertakeTrainType.grid.saveWin.hide();}
		}]
	});
}

//确认提交方法，后面可覆盖此方法完成查询
TrainType.submit = function(){
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
//	this.baseParams.undertakeOrgId = systemOrgid; //承修单位主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//承修车型查询
UndertakeTrainType.grid.store.on('beforeload' , function(){
	var searchParam = {};
//	searchParam.undertakeOrgId = systemOrgid ; //不同的承修单位有不同的承修车型
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});