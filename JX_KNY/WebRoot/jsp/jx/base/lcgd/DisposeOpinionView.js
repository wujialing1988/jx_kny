Ext.onReady(function(){
	Ext.namespace("DisposeOpinionView");
	DisposeOpinionView.seeWorkflow = function(){
		alert("请覆盖方法（DisposeOpinionView.seeWorkflow）！");
	}
	DisposeOpinionView.grid = new $yd.Grid({
		loadURL: ctx + "/disposeOpinion!getInfoPageList.action",
		page:false,
		singleSelect:true,
		storeAutoLoad:false,
		tbar:[{text:"查看流程图",iconCls:"chart_organisationIcon",
			   handler: function(){
			   		DisposeOpinionView.seeWorkflow();
			   }}],
		fields: [{
		        header:'工作项', dataIndex:'workItemName'
		    },{
		    	header:'处理人', dataIndex:'disposePerson'
		    },{
		    	header:'处理时间', dataIndex:'disposeTimeStr' 
		    },{
		    	header:'处理意见', dataIndex:'disposeIdea'
		    },{
		    	header:'处理意见描述', dataIndex:'disposeOpinion'
		    }]

	})
	//流程签名信息弹出框
	DisposeOpinionView.processInfoWin = new Ext.Window({
	//    title:"流程签名信息",
	    maximizable:true, width:850, height:400, layout:"fit", closeAction:"hide",
	    buttonAlign:'center',
	    items: DisposeOpinionView.grid,
	    buttons:[{
		text:"关闭", iconCls:"closeIcon",handler:function(){
			DisposeOpinionView.processInfoWin.hide();
		}
	}]
	});
});