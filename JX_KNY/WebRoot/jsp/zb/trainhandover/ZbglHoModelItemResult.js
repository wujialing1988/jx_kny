/**
 * 机车交接模板—交接项情况 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoModelItemResult');                       //定义命名空间
ZbglHoModelItemResult.searchParam = {};
ZbglHoModelItemResult.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/zbglHoModelItemResult!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglHoModelItemResult!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglHoModelItemResult!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
	fields: [{
		header:i18n.TrainHandoverItem.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:i18n.TrainHandoverItem.handoverItemID, dataIndex:'handOverItemIDX',hidden: true, editor:{ xtype:'hidden' }
	},{
		header:i18n.TrainHandoverItem.handoverDetail, dataIndex:'handOverResultDesc', editor:{  maxLength:100 }
	}],
	beforeSaveFn: function(rowEditor, changes, record, rowIndex){
		record.data.handOverItemIDX = ZbglHoModelItem.idx;
		return true;		
	},
	searchFn: function(searchParam){
    	ZbglHoModelItemResult.searchParam = searchParam ;
        this.store.load();
    },
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
    }
});
ZbglHoModelItemResult.grid.store.on("beforeload",function(){
	var searchParam = ZbglHoModelItemResult.searchParam;
	searchParam.handOverItemIDX = ZbglHoModelItem.idx;
	var whereList = [];
	for (prop in searchParam) {
	   if(prop == 'handOverItemIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
	    	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);	
})
});