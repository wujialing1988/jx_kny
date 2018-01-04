/**
 * 常用部门字典 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('OrgDicType');                       //定义命名空间
OrgDicType.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/orgDicType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/orgDicType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/orgDicType!delete.action',            //删除数据的请求URL
    storeId: "dictTypeId",
    tbar: ['add','delete','refresh'],
	fields: [{
		header:i18n.OrgDicType.dicCode, dataIndex:'dictTypeId', editor: { id:"dictTypeId",maxLength:50,allowBlank: false }
	},{
		header:i18n.OrgDicType.dicDesc, dataIndex:'dictTypeDesc', editor:{  maxLength:200,allowBlank: false }
	}],
	listeners: {
		"rowclick" : function(grid, index, e){
			var r = grid.store.getAt(index);
			OrgDicItem.dictTypeId = r.get("dictTypeId");
			OrgDicItem.grid.store.load();
		}
	},
	afterAddButtonFn: function(){
		Ext.getCmp("dictTypeId").enable();
    },
	beforeEditFn: function(rowEditor, rowIndex){
		Ext.getCmp("dictTypeId").disable();
        return true;
    },
	afterDeleteFn: function(){ 
		OrgDicItem.grid.store.load();
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"fit", border: false,
    items: {
        xtype: "panel", layout: "border", 
        items:[{
        	title:i18n.OrgDicType.comDicType,
            region: 'west', layout: "fit", autoScroll : true, 
            width: 400, minSize: 150, maxSize: 280, split: true, bodyBorder: false,
            items: [OrgDicType.grid]
        },{
        	title:i18n.OrgDicType.comDicItem,region : 'center', layout : 'fit', bodyBorder: false, items : [ OrgDicItem.grid ]
        }]
    }
});
});