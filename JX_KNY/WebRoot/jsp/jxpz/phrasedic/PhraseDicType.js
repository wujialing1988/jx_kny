/**
 * 常用短语字典类型 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PhraseDicType');                       //定义命名空间
PhraseDicType.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/phraseDicType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/phraseDicType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/phraseDicType!delete.action',            //删除数据的请求URL
    storeId: "dictTypeId",
    tbar: ['add','delete','refresh'],
	fields: [{
		header:'字典编码', dataIndex:'dictTypeId', editor: { id:"dictTypeId",maxLength:50,allowBlank: false }
	},{
		header:'字典描述', dataIndex:'dictTypeDesc', editor:{  maxLength:200 ,allowBlank: false}
	}],
	listeners: {
		"rowclick" : function(grid, index, e){
			var r = grid.store.getAt(index);
			PhraseDicItem.dictTypeId = r.get("dictTypeId");
			PhraseDicItem.grid.store.load();
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
		PhraseDicItem.grid.store.load();
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"fit", border: false,
    items: {
        xtype: "panel", layout: "border", 
        items:[{
        	title:'常用短语字典类型',
            region: 'west', layout: "fit", autoScroll : true, 
            width: 400, minSize: 150, maxSize: 280, split: true, bodyBorder: false,
            items: [PhraseDicType.grid]
        },{
           title:'常用短语字典项',region : 'center', layout : 'fit', bodyBorder: false, items : [ PhraseDicItem.grid ]
        }]
    }
});
});