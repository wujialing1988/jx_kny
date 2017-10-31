Ext.onReady(function(){
	Ext.namespace('JcxtflFault');
	
	JcxtflFault.searchParams = {};	
	
	JcxtflFault.faultSelectGrid = new Ext.yunda.Grid({
    	loadURL: ctx + '/equipFault!pageList.action',                 //装载列表数据的请求URL
    	tbar: [{
        	xtype:"label", text:"  故障名称：" 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultName = Ext.getCmp("fault_searchId").getValue();
				var searchParam = {};
			    searchParam.FaultName = faultName;	
			    JcxtflFault.faultSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		    	JcxtflFault.faultSelectGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});	
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
        		if(!$yd.isSelectedRecord(JcxtflFault.faultSelectGrid)) return;
        		var tempData = JcxtflFault.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		if(JcgxBuild.flbm =="" ||  JcgxBuild.flbm == null){
        			 MyExt.Msg.alert("请选择节后再进行故障添加");
        			 return;
        		}
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.flbm = JcgxBuild.flbm;//点击树上的分类编码
        			data.faultId = tempData[i].get("FaultID");
        			data.faultName = tempData[i].get("FaultName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFault.action",
	                params: {entityJson : Ext.util.JSON.encode(dataAry)},
	               // jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                       // JcxtflFault.faultSelectGrid.getStore().reload();
	                        JcgxBuild.jcxtflFaultGrid.getStore().reload();
	                        JcxtflFault.win.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
			}
		
			
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }	
		},{
			header:'故障编号', dataIndex:'FaultID', editor:{   }, sortable: true
		},{
			header:'故障名称', dataIndex:'FaultName', editor:{   }
		},{
			header:'故障类别', dataIndex:'FaultTypeID', editor:{ xtype:'hidden'   }
		}]
	});
	
	
	// 故障选择窗口
	JcxtflFault.win = new Ext.Window({
	    title: "故障现象选择", width: 600, height:400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFault.faultSelectGrid],
	    buttons: [{
            text: "关闭", iconCls: "closeIcon", 
            handler: function(){ 
            	JcxtflFault.win.hide();
            }
        }]
	});
	
	// 新增故障现象名称
	JcxtflFault.form = new Ext.form.FormPanel({	 
		layout:"column", padding:"10px", frame: true,
		defaults: {
			layout:"form", columnWidth:1
		},
		items: [{
			items: [{
				fieldLabel: '故障名称', name: 'faultName', xtype: 'textfield',
				maxLength:250, allowBlank: false, width:300
			}]
		}]
	});
	
	//故障新增窗口
	JcxtflFault.formWin = new Ext.Window({
	    title: "新增", width: 600, height:200, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFault.form],
	    buttons: [{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){ 
				if(JcgxBuild.flbm =="" ||  JcgxBuild.flbm == null){
        			 MyExt.Msg.alert("请选择节后再进行故障添加");
        			 return;
        		}
    			var form = JcxtflFault.form.getForm();
				if (!form.isValid()) {
					return;
				}
    			var data = form.getValues();
    			data.flbm = JcgxBuild.flbm;//点击树上的分类编码
        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFaultCus.action",
	                jsonData: Ext.util.JSON.encode(data),
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        JcxtflFault.form.getForm().reset();
	                        JcgxBuild.jcxtflFaultGrid.getStore().reload();
	                        JcxtflFault.formWin.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
			}
		},{
            text: "关闭", iconCls: "closeIcon", 
            handler: function(){ 
            	JcxtflFault.formWin.hide();
            }
        }]
	});
});