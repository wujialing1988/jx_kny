Ext.onReady(function(){
	Ext.namespace('JcxtflFault');
	
	JcxtflFault.searchParams = {};	
	
	JcxtflFault.faultSelectGrid = new Ext.yunda.Grid({
    	loadURL: ctx + '/equipFault!pageList.action',                 //装载列表数据的请求URL
    	isEdit:false,
    	tbar: [{
        	xtype:"label", text:i18n.JcxtflFault.faultName+"：" 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId"
		},{
			text : i18n.JcxtflFault.search,
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
			text : i18n.JcxtflFault.confirm,
			iconCls : "saveIcon",
			handler : function(){
        		if(!$yd.isSelectedRecord(JcxtflFault.faultSelectGrid)) return;
        		var tempData = JcxtflFault.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		if(JcgxBuild.flbm =="" ||  JcgxBuild.flbm == null){
        			 MyExt.Msg.alert(i18n.JcxtflFault.chioceAdd);
        			 return;
        		}
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.flbm = JcgxBuild.flbm;//点击树上的分类编码
        			data.faultId = tempData[i].get("FaultID");
        			data.faultName = tempData[i].get("FaultName");
        			data.faultTypeID = tempData[i].get("FaultTypeID");
        			data.faultTypeName = tempData[i].get("FaultTypeName");
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
	                    MyExt.Msg.alert(i18n.JcxtflFault.false+"\n" + response.status + "\n" + response.responseText);
	                }
	            });
			}
		
			
		}],
		fields: [{
			header:i18n.JcxtflFault.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }	
		},{
			header:i18n.JcxtflFault.FaultID, dataIndex:'FaultID', editor:{   }, sortable: true
		},{
			header:i18n.JcxtflFault.FaultName, dataIndex:'FaultName', editor:{   }
		},{
			header:i18n.JcxtflFault.FaultTypeID, dataIndex:'FaultTypeID',hidden:true, editor:{ xtype:'hidden'   }
		},{
			header:i18n.JcxtflFault.FaultType, dataIndex:'FaultTypeName', editor:{ xtype:'hidden'   }
		}]
	});
	
	
	// 故障选择窗口
	JcxtflFault.win = new Ext.Window({
	    title: i18n.JcxtflFault.faultChoice, width: 600, height:400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFault.faultSelectGrid],
	    buttons: [{
            text: i18n.JcxtflFault.close, iconCls: "closeIcon", 
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
				fieldLabel: i18n.JcxtflFault.FaultName, name: 'faultName', xtype: 'textfield',
				maxLength:250, allowBlank: false, width:300
			}]
		}]
	});
	
	//故障新增窗口
	JcxtflFault.formWin = new Ext.Window({
	    title: i18n.JcxtflFault.add, width: 600, height:200, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFault.form],
	    buttons: [{
			text : i18n.JcxtflFault.confirm,
			iconCls : "saveIcon",
			handler : function(){ 
				if(JcgxBuild.flbm =="" ||  JcgxBuild.flbm == null){
        			 MyExt.Msg.alert(i18n.JcxtflFault.chioceAdd);
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
	                    MyExt.Msg.alert(i18n.JcxtflFault.false+"\n" + response.status + "\n" + response.responseText);
	                }
	            });
			}
		},{
            text: i18n.JcxtflFault.close, iconCls: "closeIcon", 
            handler: function(){ 
            	JcxtflFault.formWin.hide();
            }
        }]
	});
});