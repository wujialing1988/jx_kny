/**
 * 常用数据收藏夹 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('DataCollect');                       //定义命名空间
DataCollect.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/dataCollect!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/dataCollect!saveDataCollect.action',             //保存数据的请求URL
    deleteURL: ctx + '/dataCollect!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'收藏数据实体', dataIndex:'id.dataEntity', editor:{  maxLength:50 }
	},{
		header:'收藏数据主键', dataIndex:'id.dataIdx', editor:{  maxLength:50 }
	},{
		header:'收藏者主键', dataIndex:'id.collectEmpId', editor:{ xtype:'numberfield', maxLength:18 }
	}],
	saveFn: function(rowEditor, changes, record, rowIndex){
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(rowEditor, changes, record, rowIndex)) {
            rowEditor.stopEditing(false);
            return;
        }
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: record.data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:DataCollect.grid });
});