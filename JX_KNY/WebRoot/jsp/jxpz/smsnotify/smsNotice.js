/**
 * 短信通知 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('smsNotice');                       //定义命名空间
smsNotice.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/smsNotice!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/smsNotice!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/smsNotice!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2,	searchFormColNum: 2,singleSelect:true,
    tbar: ['search'/*,'add','delete'*/,'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'短信通知内容', dataIndex:'smsMessageContent', width : 300, editor:{  maxLength:255 }
	},{
		header:'短信通知类型', dataIndex:'smsNotifyType', editor:{  maxLength:50 }, renderer:function(v){
            
            switch(v){
                case 'smsnotice_type_work_card':
                    return smsnotice_type_work_card_ch;
                case 'smsnotice_type_fault':
                    return smsnotice_type_fault_ch;
                default:
                    return v;
            }
        
        }, searcher:{disabled:true}
	},{
		header:'业务数据主键', dataIndex:'sourceIdx', hidden: true, editor:{xtype:'hidden',  maxLength:50 },searcher:{disabled:true}
	}],
    toEditFn : function(){}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:smsNotice.grid });
});