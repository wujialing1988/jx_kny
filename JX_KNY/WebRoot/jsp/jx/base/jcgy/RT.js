/**
 * Jcgy_rt 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RT');                       //定义命名空间
RT.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/rT!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/rT!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/rT!logicDelete.action',            //删除数据的请求URL
    tbar:['search','add',{
    	text:"删除",
    	iconCls:"deleteIcon",
    	handler:function(){
    		if(!$yd.isSelectedRecord(RT.grid)) return;
    		var r = RT.grid.selModel.getSelections();
    		var ids = [];
    		for(var i = 0; i < r.length; i++){
    			ids.push(r[i].get("rtID"));
    		}
    		$yd.confirmAndDelete({
                scope: RT.grid, url: RT.grid.deleteURL, params: {ids: ids}
            });
    	}
    },'refresh'],
	fields: [{
		header:'修次编码', dataIndex:'rtID', editor:{ allowBlank:false  }
	},{
		header:'修次名称', dataIndex:'rtName', editor:{ allowBlank:false }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:RT.grid });
});