/**
 * 通知人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('smsReceiver');                       //定义命名空间
smsReceiver.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/smsReceiver!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/smsReceiver!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/smsReceiver!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2,	searchFormColNum: 2,singleSelect:true,
    tbar: ['search'/*,'add','delete'*/,'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'人员编号', dataIndex:'receiverId', hidden: true, editor:{xtype: 'hidden',  maxLength:50 }
	},{
		header:'人员名称', dataIndex:'receiverName', editor:{  maxLength:50 }
	},{
		header:'手机号码', dataIndex:'receiverPhoneNum', editor:{  maxLength:11 }
	},{
		header:'消息通知主键', dataIndex:'noticeIdx', editor:{  maxLength:50, hidden : true}, hidden : true
	},{
		header:'通知时间', dataIndex:'notifyDate', xtype:'datecolumn', editor:{format: "Y-m-d H:i",my97cfg: {dateFmt:"Y-m-d H:i",maxDate:'%y-%M-%d' } }
	}],
    toEditFn : function(){}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:smsReceiver.grid });
});