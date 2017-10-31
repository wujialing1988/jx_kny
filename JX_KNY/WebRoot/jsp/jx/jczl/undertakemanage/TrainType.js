/**
 * 承修车型 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('UndertakeTrainType');                       //定义命名空间
UndertakeTrainType.trainTypeIDX = "" ; //承修车型主键
UndertakeTrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/undertakeTrainType!pageList.action',                 //装载列表数据的请求URL
    tbar: [],                                                            //配置工具栏按钮
    singleSelect: true,                                                   //选择模式为单选
    hideRowNumberer: true,                                              //隐藏行号
//    colModel:new Ext.grid.ColumnModel([
//    	{ header:'承修车型简称', dataIndex:'trainTypeShortName' }
//    ]),
	fields: [{
		header:'承修车型主键', dataIndex:'trainTypeIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'承修车型简称', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	}],
	listeners: {
		"rowclick" : function(grid, index, e){
			var r = grid.store.getAt(index);
			UndertakeTrainType.trainTypeIDX = r.get("trainTypeIDX");
			JczlTrain.grid.store.load();
		}
	}
});
//移除侦听器
UndertakeTrainType.grid.un('rowdblclick', UndertakeTrainType.grid.toEditFn, UndertakeTrainType.grid);
//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"fit", border: false,
    items: {
        xtype: "panel", layout: "border", 
        items:[{
        	title:'承修车型列表',
            region: 'west', layout: "fit", autoScroll : true, 
            width: 200, minSize: 150, maxSize: 280, split: true, bodyBorder: false,
            items: [UndertakeTrainType.grid]
        },{
           title:'机车信息列表',region : 'center', layout : 'fit', bodyBorder: false, items : [ JczlTrain.grid ]
        }]
    }
});

//承修车型查询
UndertakeTrainType.grid.store.on('beforeload' , function(){
	var searchParam = {};
	searchParam.undertakeOrgId = systemOrgid ; //不同的承修单位有不同的承修车型
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});