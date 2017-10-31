/**
 * 消息记录表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('Message');                       //定义命名空间
Message.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/message!pageQuery.action',           //装载列表数据的请求URL
    saveURL: ctx + '/message!sendOnlineMsg.action',           //装载列表数据的请求URL
    deleteURL: ctx + '/message!delete.action',            //删除数据的请求URL
    isEdit: false, labelWidth:100, storeAutoLoad:false,
    tbar: ['search',
        {text:'发送即时消息', iconCls:"messageIcon", handler:function(){
            Message.grid.addButtonFn();
        }},
        'delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'标题', dataIndex:'title', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'发送者empid', dataIndex:'sender', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'发送人', dataIndex:'senderName', editor:{xtype:'hidden'}, searcher:{xtype:'textfield'} 
	},{
		header:'发送时间', dataIndex:'sendTime', xtype:'datecolumn', format:'Y-m-d H:i:s', 
        editor:{xtype:'hidden'},  
        searcher:{xtype:'my97date', initNow:false, fieldLabel:'发送时间(开始)'}
	},{
		header:'接收者empid', dataIndex:'receiver', hidden:true, editor:{xtype:'hidden', id:'receiver'}
	},{
		header:'接收人', dataIndex:'receiverName', 
        editor:{ 
            id: 'OmEmployee_SelectWin_Id', xtype: 'OmEmployee_SelectWin', allowBlank:false,
            hiddenName: 'receiverName', displayField:'empname', valueField: 'empname', editable:false,
            returnField:[{widgetId: "receiver", propertyName: "empid"}]
        },
        searcher:{xtype:'textfield'}
	},{
		header:'接收时间', dataIndex:'receiveTime', xtype:'datecolumn', format:'Y-m-d H:i:s', 
        editor:{xtype:'hidden'},  
        searcher:{xtype:'my97date', initNow:false, fieldLabel:'接收时间(开始)'}
	},{
		header:'内容', dataIndex:'content', width:300, editor:{xtype:'textarea', maxLength:1000, allowBlank:false}
	},{
		header:'链接页面地址', dataIndex:'url', hidden:true, editor:{xtype:'hidden'}
	}],
    searchOrder:['senderName',
        'sendTime', {xtype:'my97date', fieldLabel:'发送时间(结束)', initNow:false, format:'Y-m-d H:i:s', name:'sendTime_End'},
        'receiveTime', {xtype:'my97date', fieldLabel:'接收时间(结束)', initNow:false, format:'Y-m-d H:i:s', name:'receiveTime_End'},
        'content'],
    afterShowSaveWin:function(){
        this.saveWin.setTitle('即时消息');
    },
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
        this.saveWin.hide();
    },    
    toEditFn:function(){}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:Message.grid });
Message.grid.createSaveWin();
var sendBtn = Message.grid.saveWin.buttons[ 0 ];
sendBtn.setText('发送');
sendBtn.setIconClass('messageIcon');

Message.grid.store.setDefaultSort('sendTime','DESC');
//在store.load前，组装查询参数
Message.grid.store.on('beforeload', function(){
    var whereList = [];
    // 只接收当前系统登录人员的消息
    whereList.push({propName: 'receiver', propValue: empid, compare:Condition.EQ, stringLike: false});
	// 查询表单中的参数的组装
    if(Message.grid.searchForm != null){
        var searchParam = Message.grid.searchForm.getForm().getValues();
        
        Message.searchParam = searchParam;
        searchParam = MyJson.deleteBlankProp(searchParam);
        if(searchParam.sendTime){
            whereList.push({propName:'sendTime', compare:Condition.GE, propValue:searchParam.sendTime});
            delete searchParam.sendTime;
        }
        if(searchParam.sendTime_End){
            whereList.push({propName:'sendTime', compare:Condition.LE, propValue:searchParam.sendTime_End});
            delete searchParam.sendTime_End;
        }
        if(searchParam.receiveTime){
            whereList.push({propName:'receiveTime', compare:Condition.GE, propValue:searchParam.receiveTime});
            delete searchParam.receiveTime;
        }
        if(searchParam.receiveTime_End){
            whereList.push({propName:'receiveTime', compare:Condition.LE, propValue:searchParam.receiveTime_End});
            delete searchParam.receiveTime_End;
        }
        for(prop in searchParam){
            whereList.push({propName:prop, compare:Condition.EQ, propValue:searchParam[ prop ]});
        }
    }
    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
Message.grid.store.load();

});