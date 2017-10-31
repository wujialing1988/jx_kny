Ext.onReady(function(){	
	Ext.namespace('Sign');	
	Sign.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		tbar : tbar,
		items: [
		        {
		        	align: 'center',
		    		layout : 'form',
		    		baseCls: 'x-plain',
		    		style : 'padding : 10px',
		    		defaults : { anchor : '98%'},
		    		labelWidth:70,
		    		buttonAlign : 'center', 
		    		items: [
		    			{
		    				xtype:'fieldset',
		    				title: '基本信息',
		    				collapsible: false,
		    				autoHeight:true,
		    				frame : true,
		    				items :[
		    					{xtype:"hidden", name:"trainTypeIdx"},
		    					{
		    						align : 'center',
		    						layout : 'column',
		    						baseCls : 'x-plain',
		    						items:[
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'workItemName', 
		    										fieldLabel : '工作项名称', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							},
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'processInstName', 
		    										fieldLabel : '流程名称', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							},
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'processInstID', 
		    										fieldLabel : '流程实例', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true,
		    										hidden : true,
		    										value: processInstID
		    									}
		    								]
		    							}
		    						]
		    					},
		    					{
		    						align : 'center',
		    						layout : 'column',
		    						baseCls : 'x-plain',
		    						items:[
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'trainType', 
		    										fieldLabel : '车型车号', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							},
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'repairClassName', 
		    										fieldLabel : '修程修次', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							}
		    						]
		    					}
		    				]
		    			},
		    			{
		    				xtype:'fieldset',
		    				title: '签名信息',
		    				collapsible: false,
		    				autoHeight:true,
		    				frame : true,
		    				items :[
		    					{
		    						align : 'center',
		    						layout : 'column',
		    						baseCls : 'x-plain',
		    						items:[
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .5,
		    								items : [
		    									{
		    										name : 'disposePerson', 
		    										fieldLabel : '姓名',
		    										style:"color:gray",
		    										readOnly:true,
		    										width:200,
		    										value:uname
		    									},
		    									{
		    										name : 'disposePersonID', 
		    										fieldLabel : '人员ID',
		    										xtype:"hidden",
		    										value:empid
		    									},
		    									{
		    										name : 'activityInstID', 
		    										fieldLabel : 'activityInstID',
		    										xtype:"hidden",
		    										value:actInstId
		    									},
		    									{
		    										name : 'workItemID', 
		    										fieldLabel : 'workItemID',
		    										xtype:"hidden",
		    										value:workitemId
		    									},
		    									{
		    										name : 'signType', 
		    										fieldLabel : 'signType',
		    										xtype:"hidden"
		    									},
		    									{
		    										name : 'rdpIdx', 
		    										fieldLabel : 'rdpIdx',
		    										value : rdpIdx,
		    										xtype:"hidden"
		    									},
		    									{
		    										name : 'businessIDX', 
		    										fieldLabel : 'businessIDX',
		    										xtype:"hidden",
		    										value:idx
		    									}
		    								]
		    							},{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : .5,
		    								items : [
		    									{
		    										name : 'disposeTime', 
		    										fieldLabel : '时间', 
		    										xtype:"my97date",
		    										format: "Y-m-d H:i",
		    										my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},
		    										width:140
		    									}
		    								]
		    							}
		    						]
		    					},
		    					{
		    						align : 'center',
		    						layout : 'column',
		    						baseCls : 'x-plain',
		    						items:[
		    							{
		    								align : 'center',
		    								layout : 'form',
		    								defaultType : 'textfield',
		    								baseCls : 'x-plain',
		    								columnWidth : 1,
		    								items : [
		    									{
		    										name : 'disposeOpinion', 
		    										fieldLabel : '备注', 
		    										xtype : 'textarea',
		    										width:200,
		    										maxLength:500
		    									}
		    								]
		    							}
		    						]
		    					},
		    					{
		    						align : 'center',
		    						layout : 'column',
		    						baseCls : 'x-plain',
		    						style:"display:" + outOrg,
		    						items:[{
		    						    	   align : 'center',
		    						    	   layout : 'form',
		    						    	   defaultType : 'textfield',
		    						    	   baseCls : 'x-plain',
		    						    	   columnWidth : 1,
		    						    	   items : [{
		    						    	            	name : 'outTime', 
		    												fieldLabel : '出段时间', 
		    												xtype:"my97date",
		    												format: "Y-m-d H:i",
		    												my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},
		    												width:140,
		    												disabled:outOrg == "none"
		    						    	            }]
		    						       }]
		    					}
		    				]
		    			}
		    		]
		        }
		]
	});
	
	jQuery.ajax({
		url: ctx + "/processTask!getBeseInfo.action",
		data:{workitemid: workitemId},
		dataType:"json",
		type:"post",
		success:function(data){
			/*if(data.xcIdx != sxIdx){
				tpType = jt28;
				tpTypeName = dict[jt28];
			} else {
				tpType = jt6;
				tpTypeName = dict[jt6];
			}*/
			var record=new Ext.data.Record();
			for(var i in data){
				record.set(i,data[i]);
			}
			Sign.baseForm.getForm().loadRecord(record);
		}
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items: Sign.baseForm
	});
	
	Sign.confirm = function(){
		
		if(!Sign.baseForm.getForm().isValid()) return;
		var data = Sign.baseForm.getForm().getValues();
		//删除多余的字段
		delete data.repairClassName;
		delete data.processInstName;
		delete data.trainType;
		delete data.trainTypeIdx;
		 
		Ext.Msg.confirm("提示","确认提交",function(btn){
			if(btn == "yes"){
				parent.showtip();
				Ext.Ajax.request({
                   url: ctx +"/disposeOpinion!newSign.action?ctrl="+ctrl,
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
	
	parent.confirm = Sign.confirm;
});