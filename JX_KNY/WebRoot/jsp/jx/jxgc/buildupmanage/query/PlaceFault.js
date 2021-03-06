/**
 * 故障现象 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PlaceFault');                       //定义命名空间
//故障选择
PlaceFault.faultSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/equipFault!faultList.action',                 //装载列表数据的请求URL
    //remoteSort: false,
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
				PlaceFault.faultSelectGrid.getStore().baseParams.fixPlaceIdx = fixPlaceIdx;
				PlaceFault.faultSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				PlaceFault.faultSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx//位置主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(PlaceFault.faultSelectGrid)) return;
        		var tempData = PlaceFault.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.buildUpPlaceIdx = fixPlaceIdx;
        			data.faultId = tempData[ i ].get("FaultID");
        			data.faultName = tempData[ i ].get("FaultName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeFault!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        PlaceFault.faultSelectGrid.getStore().reload();
	                        PlaceFault.grid.getStore().reload();
	                        PlaceFault.win.hide();
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

//故障选择窗口
PlaceFault.win = new Ext.Window({
    title: "故障现象选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [PlaceFault.faultSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", 
                handler: function(){ 
                	PlaceFault.win.hide();
                }
            }]
});
//故障现象列表
PlaceFault.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/placeFault!pageQuery.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/placeFault!logicDelete.action',            //删除数据的请求URL
    //remoteSort: false,
    singleSelect: true,
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成位置主键', dataIndex:'buildUpPlaceIdx', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'现象编码', dataIndex:'faultId', editor:{  maxLength:8 }, sortable: true
	},{
		header:'现象名称', dataIndex:'faultName', editor:{  maxLength:100 }
	}]
});
//移除侦听器
PlaceFault.faultSelectGrid.un('rowdblclick', PlaceFault.faultSelectGrid.toEditFn, PlaceFault.faultSelectGrid);
PlaceFault.grid.un('rowdblclick', PlaceFault.grid.toEditFn, PlaceFault.grid);
//虚拟组成-故障选择
PlaceFault.v_faultSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/equipFault!faultList.action',                 //装载列表数据的请求URL
    //remoteSort: false,
    tbar: [{
        	xtype:"label", text:"  故障名称：" 
    	},{			
            xtype: "textfield",    
            id: "fault_v_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultName = Ext.getCmp("fault_v_searchId").getValue();
				var searchParam = {};
				searchParam.FaultName = faultName;		
				PlaceFault.v_faultSelectGrid.getStore().baseParams.fixPlaceIdx = fixPlaceIdx;
				PlaceFault.v_faultSelectGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				PlaceFault.v_faultSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx//位置主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(PlaceFault.v_faultSelectGrid)) return;
        		var tempData = PlaceFault.v_faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.buildUpPlaceIdx = fixPlaceIdx;
        			data.faultId = tempData[ i ].get("FaultID");
        			data.faultName = tempData[ i ].get("FaultName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeFault!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        PlaceFault.v_faultSelectGrid.getStore().reload();
	                        PlaceFault.v_grid.getStore().reload();
	                        PlaceFault.v_win.hide();
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
//虚拟组成-故障选择窗口
PlaceFault.v_win = new Ext.Window({
    title: "故障现象选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [PlaceFault.v_faultSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ PlaceFault.v_win.hide(); }
            }]
});
//故障现象列表
PlaceFault.v_grid = new Ext.yunda.Grid({
    loadURL: ctx + '/placeFault!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/placeFault!logicDelete.action',            //删除数据的请求URL
    //remoteSort: false,
    singleSelect: true,    
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成位置主键', dataIndex:'buildUpPlaceIdx', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'现象编码', dataIndex:'faultId', editor:{  maxLength:8 }, sortable: true
	},{
		header:'现象名称', dataIndex:'faultName', editor:{  maxLength:100 }
	}]
});
//移除侦听器
PlaceFault.v_faultSelectGrid.un('rowdblclick', PlaceFault.v_faultSelectGrid.toEditFn, PlaceFault.v_faultSelectGrid);
PlaceFault.v_grid.un('rowdblclick', PlaceFault.v_grid.toEditFn, PlaceFault.v_grid);
});