/**
 * 组成实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('BuildUpTypeCase');                       //定义命名空间
BuildUpTypeCase.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpTypeCase!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpTypeCase!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpTypeCase!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'组成位置编码', dataIndex:'buildUpPlaceCode', editor:{  maxLength:10 }
	},{
		header:'组成型号主键', dataIndex:'buildUpTypeIdx', editor:{  maxLength:50 }
	},{
		header:'组成位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'组成位置编码全名', dataIndex:'buildUpPlaceFullCode', editor:{  maxLength:200 }
	},{
		header:'组成位置名称全名', dataIndex:'buildUpPlaceFullName', editor:{  maxLength:500 }
	},{
		header:'位置类型', dataIndex:'placeType', editor:{ xtype:'numberfield', maxLength:2 }
	},{
		header:'版本号', dataIndex:'buildUpVersion', editor:{  maxLength:50 }
	},{
		header:'是否最新版本', dataIndex:'isLastVsersion', editor:{  maxLength:2 }
	},{
		header:'上一版本号', dataIndex:'preVersion', editor:{  maxLength:50 }
	},{
		header:'下一版本号', dataIndex:'nextVersion', editor:{  maxLength:50 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:BuildUpTypeCase.grid });
});