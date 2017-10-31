Ext.onReady(function(){
	Ext.namespace("TP_TecVerification");
	DisposeOpinionView.seeWorkflow = function(){
		window.open(ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID=" + processInstID +"&rdpId=" + rdpIdx);
	}
	TP_TecVerification.form = new Ext.form.FormPanel({
	    layout: "anchor", 	border: false, 		style: "padding:5px", 		labelWidth: 80,
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" }, buttonAlign:"center",
	    items: [{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", 
	        items: { name: "handleScheme", fieldLabel: "处理方案",xtype:"textarea",  width: 400, allowBlank: false }		    	
	    },
	    { xtype: "hidden", name : 'faultIDX', value: idx},
	    { xtype: "hidden", name : 'presentPerson', value: uname},
	    { xtype: "hidden", name : 'presentDep', value: orgName}],
	    buttons:[{
			text: "保存", iconCls:"saveIcon",
			handler:function(){
				TP_TecVerification.save();
	    	}
		}]
	})
	TP_TecVerification.save = function(){
		if(!TP_TecVerification.form.getForm().isValid()) return;
		var data = TP_TecVerification.form.getForm().getValues();
		parent.showtip();
		Ext.Ajax.request({
	       url: ctx +"/faultScheme!saveOrUpdate.action",
	       jsonData: data,
	       success: function(response, options){
			parent.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
					alertSuccess();	                
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
	TP_TecVerification.win = new Ext.Window({
		title:"处理意见", width:550, height:360, closeAction:"hide", modal:true, layout:"fit",
		items:[DisposeOpinion.form], buttonAlign: "center",
		buttons:[{
			text: "确定", iconCls:"saveIcon",
			handler:function(){
				Adjust.workDateSubmit();
	    	}
		},{
			text: "关闭", iconCls:"closeIcon",
			handler:function(){
				Adjust.workDateWin.hide();
			}
		}]
	});
	parent.confirm = function(){
		if(!TP_TecVerification.form.getForm().isValid()) return;
		if(!DisposeOpinion.form.getForm().isValid()) return;
		var data = TP_TecVerification.form.getForm().getValues();
		var doData = DisposeOpinion.form.getForm().getValues();
		Ext.Msg.confirm("提示","确认提交",function(btn){
			if(btn == "yes"){
				parent.showtip();
				Ext.Ajax.request({
			       url: ctx +"/faultScheme!saveAndFinishWorkItem.action",
			       jsonData: data,
			       params:{
			       		workitemId: workitemId,
			       		actInstId: actInstId,
			       		processInstID: processInstID,
			       		dataJson: Ext.util.JSON.encode(doData)
			       },
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
	TP_TecVerification.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    deferredRender: true,
	    items:[{
	            title: "处理方案", layout: "fit", border: false, frame: true, items:
	            	[{
			    		xtype: "fieldset",
			    		title: "处理方案", autoHeight: true,
			    		items: {
								layout: "form", collapsible:false, frame: true,
								items: TP_TecVerification.form
							}
						},{
			    		xtype: "fieldset",
			    		title: "处理意见", autoHeight: true,
			    		items: {
								layout: "form", collapsible:false, frame: true,
								items: DisposeOpinion.form
							}
						},{
			    		xtype: "fieldset",
			    		title: "流程查看", autoHeight: true,
			    		items: {
								layout: "fit", height: 280, autoScroll: true,
								items: DisposeOpinionView.grid
							}
					   }]	            
	          }/*,{
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
								layout: "form", collapsible:false, frame: true,
								items: DisposeOpinionView.panel
							}
					}]	            
	           }*/,{
	        	title: "提票信息", layout: "fit", border: false, items: FaultInfo.faultHandleForm
	        }]
	});	
	
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items: TP_TecVerification.tabs
	});
	DisposeOpinionView.grid.store.baseParams.processInstID = processInstID;
	DisposeOpinionView.grid.store.load();
});