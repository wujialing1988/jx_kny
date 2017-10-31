/**
 * 配件检修记录单实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpRecord');                       //定义命名空间
PartsRdpRecord.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpRecord!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpRecord!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpRecord!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', editor:{  maxLength:50 }
	},{
		header:'记录单主键', dataIndex:'recordIDX', editor:{  maxLength:50 }
	},{
		header:'记录单编号', dataIndex:'recordNo', editor:{  maxLength:30 }
	},{
		header:'记录单名称', dataIndex:'recordName', editor:{  maxLength:50 }
	},{
		header:'记录单描述', dataIndex:'recordDesc', editor:{  maxLength:500 }
	},{
		header:'报表主键', dataIndex:'reportTmplManageIDX', editor:{  maxLength:50 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpRecord.grid });
});