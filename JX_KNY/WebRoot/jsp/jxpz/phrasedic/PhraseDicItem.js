/**
 * 常用短语字典项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PhraseDicItem');                       //定义命名空间
PhraseDicItem.searchParam = {};
PhraseDicItem.dictTypeId = "" ;//字典类型编码
PhraseDicItem.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/phraseDicItem!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/phraseDicItem!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/phraseDicItem!delete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    storeId: "dictItemId",
    tbar: ['add','delete','refresh'],
	fields: [{
		header:'字典项ID', dataIndex:'dictItemId', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'字典编码', dataIndex:'dictTypeId', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'短语描述', dataIndex:'dictItemDesc', editor:{  maxLength:200 }
	}],
	beforeSaveFn: function(rowEditor, changes, record, rowIndex){
		record.data.dictTypeId = PhraseDicItem.dictTypeId ;
        return true;
    }
});
PhraseDicItem.grid.store.on('beforeload',function(){
	     var searchParam = PhraseDicItem.searchParam;
	     searchParam.dictTypeId = PhraseDicItem.dictTypeId ;
	     searchParam = MyJson.deleteBlankProp(searchParam);
	     this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
});