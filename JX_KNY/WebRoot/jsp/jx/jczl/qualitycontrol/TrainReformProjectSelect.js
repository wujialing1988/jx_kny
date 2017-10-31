/**
 * 机车改造项目选择 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TechReformProject');                       //定义命名空间
TechReformProject.searchParam = {} ;                    //定义查询条件
TechReformProject.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/techReformProject!pageList.action',                 //装载列表数据的请求URL
    tbar: ['search',{
    	text:'确定',iconCls:'saveIcon',handler:function(){
    		TechReformProject.submit();
    	}
    }],
	fields: [
//	Attachment.createColModeJson({ attachmentKeyName:'JCZL_Tech_Reform_Project' }),
	{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {  xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{id:"trainTypeID",xtype:'hidden', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName',width: 50, editor:{},searcher: {}
	},{
		header:'文件编号', dataIndex:'reformReason', width: 80,editor:{ allowBlank: false,  maxLength:50 }
	},{
		header:'改造项目名称', dataIndex:'projectName', editor:{allowBlank: false,height:100, xtype:'textarea', maxLength:50 },searcher: {}
	},{
		header:'改造具体内容', dataIndex:'reformContent', editor:{allowBlank: false,xtype:'textarea',height:125, maxLength:50 },searcher: {}
	},{
		header:'备注', dataIndex:'reformTarget',editor:{ xtype:'textarea',height:100, maxLength:500 },searcher: {disabled: true}
	}],
	searchFn: function(searchParam){ 
		TechReformProject.searchParam = searchParam ;
        this.store.load();
	}
});
//加载前过滤
TechReformProject.grid.store.on('beforeload',function(){
	var searchParam = TechReformProject.searchParam;
	searchParam.projectType = PRO_TYPE_TRAIN ;
	searchParam.status = STATUS_USE ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//取消编辑监听
TechReformProject.grid.un('rowdblclick', TechReformProject.grid.toEditFn, TechReformProject.grid); 

TechReformProject.selectWin = new Ext.Window({
	title:"机车改造项目通知列表", maximizable:true,width:700, height:325, closeAction:"hide", modal:true, layout:"fit", items:TechReformProject.grid
});
//确认提交方法，后面可覆盖此方法完成查询
TechReformProject.submit = function(){alert("请覆盖，TechReformProject.submit 方法完成自己操作业务！");};


});