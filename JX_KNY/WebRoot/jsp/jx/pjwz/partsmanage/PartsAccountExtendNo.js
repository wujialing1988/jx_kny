/**
 * 配件信息扩展编号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsAccountExtendNo');                       //定义命名空间
PartsAccountExtendNo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsAccountExtendNo!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsAccountExtendNo!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsAccountExtendNo!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'配件信息主键', dataIndex:'partsAccountIDX', editor:{  maxLength:50 }
	},{
		header:'扩展编号1', dataIndex:'extendNo1', editor:{  maxLength:50 }
	},{
		header:'扩展编号2', dataIndex:'extendNo2', editor:{  maxLength:50 }
	},{
		header:'扩展编号3', dataIndex:'extendNo3', editor:{  maxLength:50 }
	},{
		header:'扩展编号4', dataIndex:'extendNo4', editor:{  maxLength:50 }
	},{
		header:'扩展编号5', dataIndex:'extendNo5', editor:{  maxLength:50 }
	},{
		header:'扩展编号6', dataIndex:'extendNo6', editor:{  maxLength:50 }
	},{
		header:'扩展编号7', dataIndex:'extendNo7', editor:{  maxLength:50 }
	},{
		header:'扩展编号8', dataIndex:'extendNo8', editor:{  maxLength:50 }
	},{
		header:'扩展编号9', dataIndex:'extendNo9', editor:{  maxLength:50 }
	},{
		header:'扩展编号10', dataIndex:'extendNo10', editor:{  maxLength:50 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsAccountExtendNo.grid });
});