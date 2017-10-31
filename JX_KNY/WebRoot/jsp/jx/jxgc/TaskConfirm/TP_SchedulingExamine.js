Ext.onReady(function(){
	Ext.namespace("TP_SchedulingExamine");
	DisposeOpinionView.seeWorkflow = function(){
		window.open(ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID=" + processInstID +"&rdpId=" + rdpIdx);
	}
	
	parent.confirm = function(){
		if(!DisposeOpinion.form.getForm().isValid()) return;
		var data = DisposeOpinion.form.getForm().getValues();		 
		Ext.Msg.confirm("提示","确认提交",function(btn){
			if(btn == "yes"){
				parent.showtip();
				Ext.Ajax.request({
			       url: ctx +"/disposeOpinion!newSign.action",
			       jsonData: data,
			       success: function(response, options){
					parent.hidetip();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                parent.hide();//关闭窗口		                
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			        	parent.hidetip();
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			 	});
			 }
		 });
	}
	TP_SchedulingExamine.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    deferredRender: true,
	    items:[{
	            title: "处理意见", layout: "fit", border: false, frame: true, items:
	            	[{
			    		xtype: "fieldset",
			    		title: "", autoHeight: true,
			    		items: {
								layout: "form", collapsible:false, frame: true,
								items: DisposeOpinion.form
							}
						},{
			    		xtype: "fieldset",
			    		title: "", autoHeight: true,
			    		items: {
								layout: "fit", height: 280, autoScroll: true,
								items: DisposeOpinionView.grid
							}
					}]	            
	        },{
	        	title: "提票信息", layout: "fit", border: false, items: FaultInfo.faultHandleForm
	        }]
	});	
	
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items: TP_SchedulingExamine.tabs
	});
	DisposeOpinionView.grid.store.baseParams.processInstID = processInstID;
    DisposeOpinionView.grid.store.load();
});