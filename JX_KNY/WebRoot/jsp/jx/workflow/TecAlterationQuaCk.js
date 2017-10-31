Ext.onReady(function(){	
	Ext.namespace('Sign');	
	Sign.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		items: [
		        {
		        	align: 'center',
		    		layout : 'form',
		    		baseCls: 'x-plain',
		    		style : 'padding : 10px',
		    		defaults : { anchor : '98%'},
		    		labelWidth:90,
		    		buttonAlign : 'center', 
		    		items: [
		    			{
		    				xtype:'fieldset',
		    				title: '基本信息',
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
		    								columnWidth : .3,
		    								items : [
		    									{
		    										name : 'workitemName', 
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
		    										readOnly:true
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
		    										name : 'trainTypeShortName', 
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
		    										name : 'reformOrgName', 
		    										fieldLabel : '承改部门', 
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
		    										name : 'reformTeamOrgName', 
		    										fieldLabel : '承改班组', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							}
		    						]
		    					},{
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
		    										name : 'techReformName', 
		    										fieldLabel : '改造项目名称', 
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
		    										name : 'techReformReason', 
		    										fieldLabel : '改造依据', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true
		    									}
		    								]
		    							}
		    						]
		    					},{
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
		    										name : 'techReformItemContent', 
		    										fieldLabel : '改造内容', 
		    										width : "99%",
		    										style:"border:none;background:none;",
		    										readOnly:true,
		    										xtype: "textarea"
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
		    					}
		    				]
		    			}
		    		]
		        }
		]
	});
	
	jQuery.ajax({
		url: ctx + "/trainTechReformAccount!getBasicInfo.action",
		data:{workitemid: workitemId},
		dataType:"json",
		type:"post",
		success:function(data){			
			var record=new Ext.data.Record();
			var entity = data.entity;
			for(var i in entity){
				record.set(i,entity[i]);
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
		data.workItemName = data.workitemName;
		//删除多余的字段
		delete data.workitemName;
		delete data.trainTypeShortName;
		delete data.reformOrgName;
		delete data.reformTeamOrgName;
		delete data.techReformName;
		delete data.techReformReason;
		delete data.techReformItemContent;
		delete data.processInstName;
		 
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
	
	parent.confirm = Sign.confirm;
});