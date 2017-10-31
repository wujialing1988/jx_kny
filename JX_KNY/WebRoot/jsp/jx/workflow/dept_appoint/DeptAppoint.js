/**
 * 车间指派页面构造
 */
Ext.onReady(function(){	
	Ext.namespace('Appoint');
	
	var faultInfo = {};
	if(showTPInfo != "false"){
		faultInfo = {
			xtype:'fieldset',
			title: '提票信息',
			collapsible: false,
			autoHeight:true,
			frame : true,
			items :[{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
		    	   align : 'center',
		    	   layout : 'form',
		    	   defaultType : 'textfield',
		    	   baseCls : 'x-plain',
		    	   columnWidth : .3,
		    	   items : [{
    	            	name : 'partsName', 
    	            	fieldLabel : '配件名称', 
    	            	width : "99%",
    	            	style:"border:none;background:none;",
    	            	readOnly:true
    	            }]
		       },{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'specificationModel', 
						fieldLabel : '配件型号', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'nameplateNo', 
						fieldLabel : '配件编号', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				}]
			},{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'faultName', 
						fieldLabel : '故障现象', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .7,
					items : [{
						name : 'faultDesc', 
						fieldLabel : '故障描述', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				}]
			},{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'faultOccurDate', 
						fieldLabel : '故障发生日期', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .7,
					items : [{
						name : 'fixPlaceFullName', 
						fieldLabel : '故障位置', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				}]
			}]
		}
	}
	/**
	 * 基本表单显示
	 */
	Appoint.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 10px',
		autoScroll : true,
		defaults : { anchor : '98%'},
		labelWidth:80,
		buttonAlign : 'center', 
		items: [{
			xtype:'fieldset',
			title: '基本信息',
			collapsible: false,
			autoHeight:true,
			frame : true,
			items :[{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'workItemName', 
						fieldLabel : '工作项名称', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'processInstName', 
						fieldLabel : '流程名称', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'processInstID', 
						fieldLabel : '流程实例', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				}]
			},{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
							name : 'trainType', 
							fieldLabel : '车型车号', 
							width : "99%",
							style:"border:none;background:none;",
							readOnly:true
						}]
				},{
					align : 'center',
					layout : 'form',
					defaultType : 'textfield',
					baseCls : 'x-plain',
					columnWidth : .3,
					items : [{
						name : 'repairClassName', 
						fieldLabel : '修程修次', 
						width : "99%",
						style:"border:none;background:none;",
						readOnly:true
					}]
				}]
			}]
		},
		faultInfo]
	});
	
	/**
	 * 查询流程基本信息
	 */
	jQuery.ajax({
		url: ctx + "/processTask!getBeseInfo.action",
		data:{workitemid: workitemId},
		dataType:"json",
		type:"post",
		success:function(data){
			var record=new Ext.data.Record();
			for(var i in data){
				record.set(i,data[i]);
			}
			Appoint.baseForm.getForm().loadRecord(record);
		}
	});
	/**
	 * 查询提票基本信息
	 */
	jQuery.ajax({
		url: ctx + "/faultNotice!getFaultNoticeBaseInfo.action",
		data:{sourceIdx: sourceIdx},
		dataType:"json",
		type:"post",
		success:function(data){
			var record=new Ext.data.Record();	
			var json = data.entity;
			for(var i in json){
				if(i=='faultOccurDate'){
					record.set(i,new Date(json[i]).format('Y-m-d H:m'));
				}else{
					record.set(i,json[i]);
				}
			}
			Appoint.baseForm.getForm().loadRecord(record);
		}
	});
	
	
	Appoint.width = 220;
	if(jQuery("body").height() > 550){
		Appoint.formHeight = 290;
	}else{
		Appoint.formHeight = 260;
	}
	Appoint.height = jQuery("body").height()-Appoint.formHeight - 20;
	
	Appoint.tree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/faultDispatcher!deptAppoint.action"
	    }),
	    width:300,
	    height:Appoint.height,
	    style:'background-color:white',
	    root: new Ext.tree.AsyncTreeNode({
	       	text:"车间指派机构树",
			id:"ROOT_0",
			isLastLevel: 0,
			classID : "",
			parentIDX : "",
			status : 0,
			leaf:false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false
	});
	Appoint.tree.getRootNode().expand();
	
	/**
	 * 页面布局
	 */
	var viewport = new Ext.Viewport({
		xtype: "panel", 
		layout: "border",
		items:[{
			region: 'north', layout: "fit", collapsible : true, frame:true, autoScroll : true, height : Appoint.formHeight, bodyBorder: false,items:[Appoint.baseForm]
	    },{
	        region : 'center', frame:true, autoScroll : false, bodyBorder: false, 
	        items:[Appoint.tree], layout : {type: 'hbox',align: 'middle ',pack: 'center'}
	    }]
	});
		
	/**
	 * tree提交方法
	 */
	parent.confirm = function(){
		var node = Appoint.tree.getSelectionModel().selNode;
		if(node == null){
			MyExt.Msg.alert("尚未选择一个车间");
			return;
		}else if(node == Appoint.tree.root){
			MyExt.Msg.alert("不能选择根节点");
			return;
		}else if(node.leaf == false){
			MyExt.Msg.alert("请选择最后一级");
			return;
		}else{
			Appoint.submit(node.id);
		}
	};
	Appoint.submit = function(orgid){
		Ext.Msg.confirm("提示","确认提交",function(btn){
			if(btn == "yes"){
		    	parent.showtip();
				Ext.Ajax.request({
			    	url: ctx +"/faultHandle!deptAppoint.action",
			        params: {orgid: orgid, workItemID: workitemId, processInstID:processInstID, faultIdx: sourceIdx},
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
});