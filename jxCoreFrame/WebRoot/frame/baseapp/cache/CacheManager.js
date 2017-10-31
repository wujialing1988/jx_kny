/**
 * 缓存信息管理 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('CacheInfo');                       //定义命名空间
CacheInfo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/cache!getCacheInfo.action',           //装载列表数据的请求URL
    remoteSort: false, singleSelect: true, page: false,
    tbar: [
        {text:'清空所有缓存', iconCls:"cupIcon", handler:function(){
	        var cfg = {
	            scope: this, url: ctx + '/cache!evictAll.action', 
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    alertSuccess();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
        }}
     ],
	fields: [{
		header:'数据库表名', dataIndex:'tableName'
	},{
		header:'中文表名', dataIndex:'tableNameCN'
	},{
		header:'实体类名称', dataIndex:'entityClass'
//        renderer: function(v, celmeta, record){
//            return v.substring(6);
//        }		
	},{
		header:'基地版是否缓存', dataIndex:'cacheJD' 
	},{
		header:'机务段版缓存', dataIndex:'cacheJWD'
	},{
		header:'实现版本', dataIndex:'modifyVersion'
	},{
		header:'操作', dataIndex:'operator',
        renderer: function(v, celmeta, record){
            return ['<a href="#" onclick="CacheInfo.grid.evictEntityAndQueries(\'',
            	record.get("tableName"),
            	'\')">清空缓存</a>'].join('');
        }
	}],
	evictEntityAndQueries: function(tableName){
		url = ctx + '/cache!evictEntityAndQueries.action?tableName=' + tableName;
        var cfg = {
            url: url, 
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                } else {
                    alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));		
	},
    toEditFn:function(){}
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:CacheInfo.grid });

});