Ext.onReady(function(){//ZbglTpTrackThisTime-开始
	Ext.namespace('ZbglTpTrackThisTime');                       //定义命名空间
	ZbglTpTrackThisTime.searchParam = {};
	ZbglTpTrackThisTime.zbglTpTrackIDX;//


    //form表单组件，用于展示本次跟踪需要的信息
	ZbglTpTrackThisTime.trackForm = new Ext.form.FormPanel({//ZbglTpTrackThisTime.trackForm-开始
	
	    layout:"form",
		frame:true,
		closable : false,
		plain:true,
		border:false, //style:"padding:10px" ,
		labelWidth:120,
		labelAlign:"right",
		buttonAlign:"center",
		//baseCls: "x-plain",
		items:[{
			layout:"column",
			items:[{
				columnWidth:0.8,
				layout:"form",
				items:[{
					id:"trackReason_idx",
					xtype:"textarea",
					fieldLabel:"本次跟踪意见",
					height:250,
					anchor:"98%"
				}]
			}]
		}],
		buttons:[{
		        text:'本次提票记录查看', 
				handler: function(){
					ZbglTpShowWin.showWin();
				}
		    },{
				text:'继续跟踪', 
				handler: function(){
					Ext.Ajax.request({
					 //保存数据到跟踪记录单，并坐在后台操作
					    url: ctx + "/zbglTpTrackRdpRecord!saveAndTrack.action",
					    //从全局变量中获得操作行跟踪单的idx
					    params:{
					    	trackRdpIDX : ZbglTpTrackThisTime.zbglTpTrackIDX ,
					        trackReason : Ext.getCmp("trackReason_idx").getValue(),
					        flag : "0"
					    },
					    success: function(r){
					        var retn = Ext.util.JSON.decode(r.responseText);
					        if(retn.success){
					        	MyExt.Msg.alert("操作成功！");
					        	ZbglTpTrack.trackIngGrid.store.load();
					        	var searchParam = {};
					        	searchParam.trackRdpIDX = ZbglTpTrackThisTime.zbglTpTrackIDX;
	          	                ZbglTpTrack.deteilgrid.store.load({
					            params: { entityJson: Ext.util.JSON.encode(searchParam) }     
				                });
					        	ZbglTpTrackThisTime.trackForm.getForm().reset();
					        }
					    }
					})
					ZbglTpTrackThisTime.trackWin.hide();
				}
			},{
				text: "结束跟踪", 
				handler: function(){ 
					Ext.Ajax.request({
					 //保存数据到跟踪记录单，并坐在后台操作
					    url: ctx + "/zbglTpTrackRdpRecord!saveAndTrack.action",
					    //从全局变量中获得操作行跟踪单的idx
					    params:{
					    	trackRdpIDX : ZbglTpTrackThisTime.zbglTpTrackIDX ,
					        trackReason : Ext.getCmp("trackReason_idx").getValue(),
					        flag : "1"
					    },
					    success: function(r){
					        var retn = Ext.util.JSON.decode(r.responseText);
					        if(retn.success){
					        	MyExt.Msg.alert("操作成功！");
					        	ZbglTpTrack.trackIngGrid.store.load();
					        	ZbglTpTrack.trackEndGrid.store.load();
					        	ZbglTpTrackThisTime.trackForm.getForm().reset();
					        }
					    }
					})
					ZbglTpTrackThisTime.trackWin.hide();
				}
	        },{
	            text: "关闭窗口",
	            handler: function(){
	                ZbglTpTrackThisTime.trackForm.getForm().reset();
	             
	                ZbglTpTrackThisTime.trackWin.hide();
	                ZbglTpTrack.trackIngGrid.store.load();
	            }
	        }]
	        
	})//ZbglTpTrackThisTime.trackForm-结束
	
	
	ZbglTpTrackThisTime.trackWin = new Ext.Window({
	    title:"故障过程", 
	    layout: 'fit',
		height: 330, width: 600,
		items:ZbglTpTrackThisTime.trackForm,
		closable : true,
		plain:true,
		closeAction:"hide",
		buttonAlign: 'center'
	});
	
	
});//ZbglTpTrackThisTime-结束
	