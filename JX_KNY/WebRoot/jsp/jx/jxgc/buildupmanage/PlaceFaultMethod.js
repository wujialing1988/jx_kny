Ext.onReady(function(){
Ext.namespace('PlaceFaultMethod');                       //定义命名空间
//故障处理方法选择
PlaceFaultMethod.faultMethodSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/faultMethod!methodList.action',                 //装载列表数据的请求URL
    //remoteSort: false,
    storeAutoLoad: false,
    tbar: [{
        	xtype:"label", text:"  故障处理方法名称：" 
    	},{			
            xtype: "textfield",    
            id: "faultMethod_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultMethodName = Ext.getCmp("faultMethod_searchId").getValue();
				var searchParam = {};
				searchParam.methodName = faultMethodName;	
				PlaceFaultMethod.faultMethodSelectGrid.getStore().baseParams.placeFaultIDX = placeFaultIDX;
				PlaceFaultMethod.faultMethodSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				PlaceFaultMethod.faultMethodSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						placeFaultIDX:placeFaultIDX//故障现象主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(PlaceFaultMethod.faultMethodSelectGrid)) return;
        		var tempData = PlaceFaultMethod.faultMethodSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.placeFaultIDX = placeFaultIDX;
        			data.methodID = tempData[ i ].get("methodID");
        			data.methodName = tempData[ i ].get("methodName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeFaultMethod!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        PlaceFaultMethod.faultMethodSelectGrid.getStore().reload();
	                        PlaceFaultMethod.grid.getStore().reload();
	                        PlaceFaultMethod.win.hide();
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
		header:'处理方法编号', dataIndex:'methodID', editor:{   }, sortable: true
	},{
		header:'处理方法名称', dataIndex:'methodName', editor:{   }
	}]
});
//故障选择窗口
PlaceFaultMethod.win = new Ext.Window({
    title: "故障处理方法选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [PlaceFaultMethod.faultMethodSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", 
                handler: function(){ 
                	PlaceFaultMethod.win.hide();
                }
            }]
});
//设为默认处理方法
PlaceFaultMethod.setDefault = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/placeFaultMethod!setIsDefault.action', 
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
                grid.store.reload(); 
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Ajax.request(cfg);
}
//故障现象处理方法列表
PlaceFaultMethod.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/placeFaultMethod!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/placeFaultMethod!logicDelete.action',            //删除数据的请求URL
    //remoteSort: false,
    storeAutoLoad: false,
    viewConfig: null,
    tbar: [{
	    	text: '选择处理方法',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if (PlaceFault.grid.selModel.getCount() == 0 || PlaceFault.grid.selModel.getCount() > 1) {
	        		MyExt.Msg.alert("请选择一条故障现象！");
			        return
			    }
	    		PlaceFaultMethod.win.show();
	    		var searchParam = {};
				PlaceFaultMethod.faultMethodSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	    		PlaceFaultMethod.faultMethodSelectGrid.getStore().baseParams.placeFaultIDX = placeFaultIDX;
				//故障现象选择列表加载数据
				PlaceFaultMethod.faultMethodSelectGrid.getStore().load({
					params:{
						placeFaultIDX:placeFaultIDX//故障现象主键
					}																
				});
	    	}
	    },{
	    	text: '设为默认',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(PlaceFaultMethod.grid)) return;
	    		var ids = $yd.getSelectedIdx(PlaceFaultMethod.grid);
	    		if(ids.length > 1){
	    			MyExt.Msg.alert("只能设置一条处理方法为默认，请重新选择！");
			        return
	    		}
	    		PlaceFaultMethod.setDefault(ids[0],PlaceFaultMethod.grid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'故障现象主键', dataIndex:'placeFaultIDX', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'处理方法编码', dataIndex:'methodID', editor:{  maxLength:8 }, sortable: true, width: 100
	},{
		header:'处理方法名称', dataIndex:'methodName', editor:{  maxLength:100 }, width: 100
	},{
		header:'默认', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case method_default:
					return "是";
					break;
				case method_nodefault:
					return "否";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }, width: 70
	}]
});
//移除侦听器
PlaceFaultMethod.faultMethodSelectGrid.un('rowdblclick', PlaceFaultMethod.faultMethodSelectGrid.toEditFn, PlaceFaultMethod.faultMethodSelectGrid);
PlaceFaultMethod.grid.un('rowdblclick', PlaceFaultMethod.grid.toEditFn, PlaceFaultMethod.grid);
/************************************虚拟组成维护-处理方法********************************************************/
//故障处理方法选择
PlaceFaultMethod.v_faultMethodSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/faultMethod!methodList.action',                 //装载列表数据的请求URL
    //remoteSort: false,
    storeAutoLoad: false,
    tbar: [{
        	xtype:"label", text:"  故障处理方法名称：" 
    	},{			
            xtype: "textfield",    
            id: "faultMethod_v_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultMethodName = Ext.getCmp("faultMethod_v_searchId").getValue();
				var searchParam = {};
				searchParam.methodName = faultMethodName;	
				PlaceFaultMethod.v_faultMethodSelectGrid.getStore().baseParams.placeFaultIDX = placeFaultIDX;
				PlaceFaultMethod.v_faultMethodSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				PlaceFaultMethod.v_faultMethodSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						placeFaultIDX:placeFaultIDX//故障现象主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(PlaceFaultMethod.v_faultMethodSelectGrid)) return;
        		var tempData = PlaceFaultMethod.v_faultMethodSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.placeFaultIDX = placeFaultIDX;
        			data.methodID = tempData[ i ].get("methodID");
        			data.methodName = tempData[ i ].get("methodName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeFaultMethod!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        PlaceFaultMethod.v_faultMethodSelectGrid.getStore().reload();
	                        PlaceFaultMethod.v_grid.getStore().reload();
	                        PlaceFaultMethod.v_win.hide();
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
		header:'处理方法编号', dataIndex:'methodID', editor:{   }, sortable: true
	},{
		header:'处理方法名称', dataIndex:'methodName', editor:{   }
	}]
});
//故障选择窗口
PlaceFaultMethod.v_win = new Ext.Window({
    title: "故障处理方法选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [PlaceFaultMethod.v_faultMethodSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", 
                handler: function(){ 
                	PlaceFaultMethod.v_win.hide();
                }
            }]
});

//故障现象处理方法列表
PlaceFaultMethod.v_grid = new Ext.yunda.Grid({
    loadURL: ctx + '/placeFaultMethod!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/placeFaultMethod!logicDelete.action',            //删除数据的请求URL
    //remoteSort: false,
    storeAutoLoad: false,
    height: 270,
    viewConfig: null,
    tbar: [{
	    	text: '选择处理方法',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if (PlaceFault.v_grid.selModel.getCount() == 0 || PlaceFault.v_grid.selModel.getCount() > 1) {
	        		MyExt.Msg.alert("请选择一条故障现象！");
			        return
			    }
	    		PlaceFaultMethod.v_win.show();
	    		var searchParam = {};
				PlaceFaultMethod.v_faultMethodSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	    		PlaceFaultMethod.v_faultMethodSelectGrid.getStore().baseParams.placeFaultIDX = placeFaultIDX;
				//故障现象选择列表加载数据
				PlaceFaultMethod.v_faultMethodSelectGrid.getStore().load({
					params:{
						placeFaultIDX:placeFaultIDX//故障现象主键
					}																
				});
	    	}
	    },{
	    	text: '设为默认',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(PlaceFaultMethod.v_grid)) return;
	    		var ids = $yd.getSelectedIdx(PlaceFaultMethod.v_grid);
	    		if(ids.length > 1){
	    			MyExt.Msg.alert("只能设置一条处理方法为默认，请重新选择！");
			        return
	    		}
	    		PlaceFaultMethod.setDefault(ids[0],PlaceFaultMethod.v_grid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'故障现象主键', dataIndex:'placeFaultIDX', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'处理方法编码', dataIndex:'methodID', editor:{  maxLength:8 }, sortable: true, width: 100
	},{
		header:'处理方法名称', dataIndex:'methodName', editor:{  maxLength:100 }, width: 100
	},{
		header:'默认', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case method_default:
					return "是";
					break;
				case method_nodefault:
					return "否";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }, width: 70
	}]
});
//移除侦听器
PlaceFaultMethod.v_faultMethodSelectGrid.un('rowdblclick', PlaceFaultMethod.v_faultMethodSelectGrid.toEditFn, PlaceFaultMethod.v_faultMethodSelectGrid);
PlaceFaultMethod.v_grid.un('rowdblclick', PlaceFaultMethod.v_grid.toEditFn, PlaceFaultMethod.v_grid);
});