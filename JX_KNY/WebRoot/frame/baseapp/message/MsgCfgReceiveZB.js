Ext.onReady(function(){
//消息服务配置-功能点定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
Ext.namespace('MsgCfgFunction');                       //定义命名空间
MsgCfgFunction.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/msgCfgFunction!pageList.action',                 //装载列表数据的请求URL
    singleSelect:true,
    fields: [{
        header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
    },{
        header:'功能点编码', dataIndex:'funCode', editor:{ allowBlank:false, maxLength:50 }
    },{
        header:'功能点名称', dataIndex:'funName', editor:{ allowBlank:false, maxLength:50 }
    }],
    //覆盖双击编辑时间，禁止编辑动作
    toEditFn: function(grid, rowIndex, e){}
});
MsgCfgFunction.grid.on('rowclick', function(grid, rowIndex, event){
    var record = grid.store.getAt(rowIndex);
    MsgCfgFunction.selIdx = record.get('idx');
    MsgCfgReceive.grid.store.load();
});
//消息服务配置-接收方定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
Ext.namespace('MsgCfgReceive');                       //定义命名空间
MsgCfgReceive.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/msgCfgReceive!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/msgCfgReceive!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/msgCfgReceive!delete.action',            //删除数据的请求URL
    saveFormColNum:1,	searchFormColNum:1, page:false, storeAutoLoad:false,
    tbar: ['add','delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'功能点定义主键', dataIndex:'funidx', hidden:true, editor:{ xtype:'hidden', id:'funidx', maxLength:50 }
	},{
		header:'接收方ID', dataIndex:'receiverID', hidden:true, editor:{ 
            id: 'OmOrganizationCustom_comboTree_Id', xtype:'OmOrganizationCustom_comboTree', fieldLabel:'选择部门',
            hiddenName: 'receiverID', returnField: [{widgetId:"receiverName",propertyName:"orgname"}], selectNodeModel:'all',
            orgid:orgID,orgname:orgName,               
            editable:false, maxLength:100 
        }
	},{
		header:'通知对象', dataIndex:'receiverName', editor:{  
            xtype:'hidden', id:'receiverName'
        }
	},{
	
		header:'接收方类型', dataIndex:'type', hidden:true, editor:{ xtype:'hidden', value:ORG, maxLength:2 }
	}],
//     * 点击新增按钮触发事件前调用的方法。
    beforeAddButtonFn: function(){
        if(!$yd.isSelectedRecord(MsgCfgFunction.grid, true, '请先点击选择一条【功能点】记录！'))    return false;
        return true;
    },
    afterShowSaveWin: function(){
        Ext.getCmp('funidx').setValue(MsgCfgFunction.selIdx);    
    }, beforeSaveFn: function(data){
  	   data.workplaceCode = siteID; 
  	   data.workplaceName = siteName; 
       return true;
    }
});
//在store.load前，组装查询参数
MsgCfgReceive.grid.store.on('beforeload', function(){
    var searchParam = {};
    searchParam.funidx = MsgCfgFunction.selIdx;
    if(siteID!=null)
	    searchParam.workplaceCode = siteID;
    this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"fit", border: false,
    items: {
        xtype: "panel", layout: "border", 
        items:[{
            region: 'west', layout: "fit", 
            width: 700, minSize: 500, maxSize: 900, split: true, bodyBorder: false,
            items:[ MsgCfgFunction.grid ]
        },{
            region : 'center', layout : 'fit', bodyBorder: false, items : [ MsgCfgReceive.grid ]
        }]
    }
});
MsgCfgFunction.grid.createSaveWin();
MsgCfgFunction.grid.saveWin.modal = true;
MsgCfgFunction.grid.createSearchWin();
MsgCfgFunction.grid.searchWin.modal = true;

MsgCfgReceive.grid.createSaveWin();
MsgCfgReceive.grid.saveWin.modal = true;
//MsgCfgReceive.grid.createSearchWin();
//MsgCfgReceive.grid.searchWin.modal = true;
});