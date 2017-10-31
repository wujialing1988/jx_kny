/**
 * 可安装组成型号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FixBuildUpType');                       //定义命名空间
//过滤【选择组成型号列表】(带查询条件)
FixBuildUpType.buildUpSelectListLoad = function(grid, searchParam){
	grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	grid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
	grid.store.baseParams.type = type;
	grid.store.baseParams.partsTypeIDX = partsTypeIDX; 
	grid.store.baseParams.trainTypeIDX = trainTypeIDX;//只用于选择虚拟组成时过滤 
	grid.store.load({
		params:{
			entityJson:Ext.util.JSON.encode(searchParam),
			fixPlaceIdx:fixPlaceIdx,//位置主键
			type:type,//组成类型,
			partsTypeIDX:partsTypeIDX,
			trainTypeIDX:trainTypeIDX
		}																
	});				
}
//过滤【选择组成型号列表】(不带查询条件)
FixBuildUpType.buildUpSelectListLoad1 = function(grid){
	grid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
	grid.store.baseParams.type = type;
	grid.store.baseParams.partsTypeIDX = partsTypeIDX; 
	grid.store.baseParams.trainTypeIDX = trainTypeIDX;//只用于选择虚拟组成时过滤 
	grid.store.load({
		params:{
			fixPlaceIdx:fixPlaceIdx,//位置主键
			type:type,//组成类型,
			partsTypeIDX:partsTypeIDX,
			trainTypeIDX:trainTypeIDX
		}																
	});				
}
/*************************1.安装位置-配件位置编辑*********************************/
//组成型号选择列表
FixBuildUpType.buildUpTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "buildUpTypeName_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("buildUpTypeName_searchId").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.buildUpTypeGrid, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.buildUpTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				FixBuildUpType.buildUpTypeGrid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.buildUpTypeGrid.store.baseParams.type = type;
				FixBuildUpType.buildUpTypeGrid.store.baseParams.partsTypeIDX = partsTypeIDX; 
				FixBuildUpType.buildUpTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型,
						partsTypeIDX:partsTypeIDX
					}																
				});	*/
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.buildUpTypeGrid)) return;
        		var tempData = FixBuildUpType.buildUpTypeGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.buildUpTypeGrid.getStore().reload();
	                        FixBuildUpType.fixBuildUpTypeGrid.getStore().reload();
	                        FixBuildUpType.buildUpTypeWin.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.buildUpTypeGrid.un('rowdblclick', FixBuildUpType.buildUpTypeGrid.toEditFn, FixBuildUpType.buildUpTypeGrid);
//组成型号选择窗口
FixBuildUpType.buildUpTypeWin =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.buildUpTypeGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.buildUpTypeWin.hide(); }
            }]
});
//设为缺省可安装组成型号
FixBuildUpType.setDefaultFixBuildType = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
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
//可安装组成型号tab
FixBuildUpType.fixBuildUpTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		FixBuildUpType.buildUpTypeWin.show();
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(FixBuildUpType.fixBuildUpTypeGrid)) return;
	    		var ids = $yd.getSelectedIdx(FixBuildUpType.fixBuildUpTypeGrid);
	    		FixBuildUpType.setDefaultFixBuildType(ids[0],FixBuildUpType.fixBuildUpTypeGrid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.buildUpTypeGrid.store.reload();
    }
});
//移除侦听器
FixBuildUpType.fixBuildUpTypeGrid.un('rowdblclick', FixBuildUpType.fixBuildUpTypeGrid.toEditFn, FixBuildUpType.fixBuildUpTypeGrid);
/**********************************************2.组成维护中-新增配件位置****************************************************************/
//组成型号选择列表
FixBuildUpType.buildUpTypeGrid1 = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "buildUpTypeName_searchId1"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("buildUpTypeName_searchId1").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;	
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.buildUpTypeGrid1, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.buildUpTypeGrid1.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				FixBuildUpType.buildUpTypeGrid1.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.buildUpTypeGrid1.store.baseParams.type = type;
				FixBuildUpType.buildUpTypeGrid1.store.baseParams.partsTypeIDX = partsTypeIDX; 
				FixBuildUpType.buildUpTypeGrid1.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型,
						partsTypeIDX:partsTypeIDX
					}																
				});		*/		
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.buildUpTypeGrid1)) return;
        		var tempData = FixBuildUpType.buildUpTypeGrid1.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.buildUpTypeGrid1.getStore().reload();
	                        FixBuildUpType.fixBuildUpTypeGrid1.getStore().reload();
	                        FixBuildUpType.buildUpTypeWin1.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.buildUpTypeGrid1.un('rowdblclick', FixBuildUpType.buildUpTypeGrid1.toEditFn, FixBuildUpType.buildUpTypeGrid1);
//组成型号选择窗口
FixBuildUpType.buildUpTypeWin1 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.buildUpTypeGrid1],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.buildUpTypeWin1.hide(); }
            }]
});
//设为缺省可安装组成型号
FixBuildUpType.setDefaultFixBuildType1 = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
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
//可安装组成型号tab
FixBuildUpType.fixBuildUpTypeGrid1 = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		FixBuildUpType.buildUpTypeWin1.show();
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(FixBuildUpType.fixBuildUpTypeGrid1)) return;
	    		var ids = $yd.getSelectedIdx(FixBuildUpType.fixBuildUpTypeGrid1);
	    		FixBuildUpType.setDefaultFixBuildType1(ids[0],FixBuildUpType.fixBuildUpTypeGrid1);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.buildUpTypeGrid1.store.reload();
    }
});
//移除侦听器
FixBuildUpType.fixBuildUpTypeGrid1.un('rowdblclick', FixBuildUpType.fixBuildUpTypeGrid1.toEditFn, FixBuildUpType.fixBuildUpTypeGrid1);
/**********************************3.组成维护-可安装组成型号tab*******************************************************************/
//组成型号选择列表
FixBuildUpType.buildUpTypeQueryGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	//remoteSort: false,
	storeAutoLoad: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "buildUpTypeName_searchId2"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("buildUpTypeName_searchId2").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;	
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.buildUpTypeQueryGrid, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.type = type;
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.partsTypeIDX = partsTypeIDX; 
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.trainTypeIDX = trainTypeIDX;//车型主键
				FixBuildUpType.buildUpTypeQueryGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型,
						partsTypeIDX:partsTypeIDX,
						trainTypeIDX:trainTypeIDX//车型主键
					}																
				});		*/		
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.buildUpTypeQueryGrid)) return;        		
        		var tempData = FixBuildUpType.buildUpTypeQueryGrid.selModel.getSelections();
        		if(type == type_virtual && tempData.length > 1){
        			MyExt.Msg.alert("该虚拟位置只能安装一种组成,请重新选择！");
	    			return;
        		}
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.buildUpTypeQueryGrid.getStore().reload();
	                        FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().reload();
	                        FixBuildUpType.buildUpTypeWin2.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.buildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.buildUpTypeQueryGrid.toEditFn, FixBuildUpType.buildUpTypeQueryGrid);
//组成型号选择窗口
FixBuildUpType.buildUpTypeWin2 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.buildUpTypeQueryGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.buildUpTypeWin2.hide(); }
            }]
});
//设为缺省可安装组成型号
FixBuildUpType.setDefaultFixBuildType2 = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
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
//可安装组成型号编辑
FixBuildUpType.fixBuildUpTypeQueryGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: false,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if(type == type_virtual && FixBuildUpType.fixBuildUpTypeQueryGrid.store.getCount() > 0){
	    			MyExt.Msg.alert("该虚拟位置已安装组成，不能再安装！");
	    			return;
	    		}
	    		FixBuildUpType.buildUpTypeWin2.show();
	    		FixBuildUpType.buildUpSelectListLoad1(FixBuildUpType.buildUpTypeQueryGrid);//过滤【选择组成型号列表】
				/*FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.type = type;
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.partsTypeIDX = partsTypeIDX; 
				FixBuildUpType.buildUpTypeQueryGrid.store.baseParams.trainTypeIDX = trainTypeIDX;//车型主键
				FixBuildUpType.buildUpTypeQueryGrid.getStore().load({
					params: {fixPlaceIdx : fixPlaceIdx, type: type, partsTypeIDX: partsTypeIDX, trainTypeIDX: trainTypeIDX}        			
				});*/
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(FixBuildUpType.fixBuildUpTypeQueryGrid)) return;
	    		var ids = $yd.getSelectedIdx(FixBuildUpType.fixBuildUpTypeQueryGrid);
	    		FixBuildUpType.setDefaultFixBuildType2(ids[0],FixBuildUpType.fixBuildUpTypeQueryGrid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.buildUpTypeQueryGrid.store.reload();
    }
});
//移除侦听器
FixBuildUpType.fixBuildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.fixBuildUpTypeQueryGrid.toEditFn, FixBuildUpType.fixBuildUpTypeQueryGrid);
/***************************4.安装位置-新增虚拟位置**********************************************/
//虚拟组成型号选择列表
FixBuildUpType.v_buildUpTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	singleSelect: true,
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "v_buildUpTypeName_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("v_buildUpTypeName_searchId").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.v_buildUpTypeGrid, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.v_buildUpTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型
						trainTypeIDX:trainTypeIDX//车型主键
					}																
				});*/				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.v_buildUpTypeGrid)) return;
        		var tempData = FixBuildUpType.v_buildUpTypeGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.v_buildUpTypeGrid.getStore().reload();
	                        FixBuildUpType.v_fixBuildUpTypeGrid.getStore().reload();
	                        FixBuildUpType.v_buildUpTypeWin.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true 
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.v_buildUpTypeGrid.un('rowdblclick', FixBuildUpType.v_buildUpTypeGrid.toEditFn, FixBuildUpType.v_buildUpTypeGrid);
//虚拟组成型号选择窗口
FixBuildUpType.v_buildUpTypeWin =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.v_buildUpTypeGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.v_buildUpTypeWin.hide(); }
            }]
});

//可安装虚拟组成型号列表
FixBuildUpType.v_fixBuildUpTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if(FixBuildUpType.v_fixBuildUpTypeGrid.store.getCount() > 0){
	    			MyExt.Msg.alert("该虚拟位置已安装组成，不能再安装！");
	    			return;
	    		}
	    		FixBuildUpType.v_buildUpTypeWin.show();
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.v_buildUpTypeGrid.store.reload();
    }
});
//移除侦听器
FixBuildUpType.v_fixBuildUpTypeGrid.un('rowdblclick', FixBuildUpType.v_fixBuildUpTypeGrid.toEditFn, FixBuildUpType.v_fixBuildUpTypeGrid);
/***************************5.组成维护-新增虚拟位置****************************************/
//虚拟组成型号选择列表
FixBuildUpType.v_buildUpTypeGrid1 = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	singleSelect: true,
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "v_buildUpTypeName_searchId1"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("v_buildUpTypeName_searchId1").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;		
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.v_buildUpTypeGrid1, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.v_buildUpTypeGrid1.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型
						trainTypeIDX:trainTypeIDX//车型主键
					}																
				});*/				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.v_buildUpTypeGrid1)) return;
        		var tempData = FixBuildUpType.v_buildUpTypeGrid1.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.v_buildUpTypeGrid1.getStore().reload();
	                        FixBuildUpType.v_fixBuildUpTypeGrid1.getStore().reload();
	                        FixBuildUpType.v_buildUpTypeWin1.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true 
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.v_buildUpTypeGrid1.un('rowdblclick', FixBuildUpType.v_buildUpTypeGrid1.toEditFn, FixBuildUpType.v_buildUpTypeGrid1);
//虚拟组成型号选择窗口
FixBuildUpType.v_buildUpTypeWin1 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.v_buildUpTypeGrid1],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.v_buildUpTypeWin1.hide(); }
            }]
});

//可安装虚拟组成型号列表
FixBuildUpType.v_fixBuildUpTypeGrid1 = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if(FixBuildUpType.v_fixBuildUpTypeGrid1.store.getCount() > 0){
	    			MyExt.Msg.alert("该虚拟位置已安装组成，不能再安装！");
	    			return;
	    		}
	    		FixBuildUpType.v_buildUpTypeWin1.show();
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.v_buildUpTypeGrid1.store.reload();
    }
});
//移除侦听器
FixBuildUpType.v_fixBuildUpTypeGrid1.un('rowdblclick', FixBuildUpType.v_fixBuildUpTypeGrid1.toEditFn, FixBuildUpType.v_fixBuildUpTypeGrid1);
/********************************6.虚拟组成维护-可安装组成型号tab*********************************************************/
//组成型号选择列表
FixBuildUpType.v_buildUpTypeQueryGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	//remoteSort: false,
	storeAutoLoad: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "v_buildUpTypeName_searchId11"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("v_buildUpTypeName_searchId11").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;	
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.v_buildUpTypeQueryGrid, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.type = type;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.partsTypeIDX = partsTypeIDX;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.trainTypeIDX = trainTypeIDX;
				FixBuildUpType.v_buildUpTypeQueryGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型,
						partsTypeIDX:partsTypeIDX,
						trainTypeIDX:trainTypeIDX
					}																
				});				*/
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.v_buildUpTypeQueryGrid)) return;        		
        		var tempData = FixBuildUpType.v_buildUpTypeQueryGrid.selModel.getSelections();
        		if(type == type_virtual && tempData.length > 1){
        			MyExt.Msg.alert("该虚拟位置只能安装一种组成,请重新选择！");
	    			return;
        		}
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.v_buildUpTypeQueryGrid.getStore().reload();
	                        FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().reload();
	                        FixBuildUpType.v_buildUpTypeWin2.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.v_buildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.v_buildUpTypeQueryGrid.toEditFn, FixBuildUpType.v_buildUpTypeQueryGrid);
//组成型号选择窗口
FixBuildUpType.v_buildUpTypeWin2 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.v_buildUpTypeQueryGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.v_buildUpTypeWin2.hide(); }
            }]
});
//设为缺省可安装组成型号
FixBuildUpType.setDefaultFixBuildType2v = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
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
//可安装组成型号编辑
FixBuildUpType.v_fixBuildUpTypeQueryGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: false,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if(type == type_virtual && FixBuildUpType.v_fixBuildUpTypeQueryGrid.store.getCount() > 0){
	    			MyExt.Msg.alert("该虚拟位置已安装组成，不能再安装！");
	    			return;
	    		}
	    		FixBuildUpType.v_buildUpTypeWin2.show();
	    		FixBuildUpType.buildUpSelectListLoad1(FixBuildUpType.v_buildUpTypeQueryGrid);//过滤【选择组成型号列表】	    		
				/*FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.type = type;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.partsTypeIDX = partsTypeIDX;
				FixBuildUpType.v_buildUpTypeQueryGrid.store.baseParams.trainTypeIDX = trainTypeIDX; 
				FixBuildUpType.v_buildUpTypeQueryGrid.getStore().load({
					params: {fixPlaceIdx : fixPlaceIdx, type: type, partsTypeIDX: partsTypeIDX, trainTypeIDX: trainTypeIDX}        			
				});*/
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(FixBuildUpType.v_fixBuildUpTypeQueryGrid)) return;
	    		var ids = $yd.getSelectedIdx(FixBuildUpType.v_fixBuildUpTypeQueryGrid);
	    		FixBuildUpType.setDefaultFixBuildType2v(ids[0],FixBuildUpType.v_fixBuildUpTypeQueryGrid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.v_buildUpTypeQueryGrid.store.reload();
    }
});
//移除侦听器
FixBuildUpType.v_fixBuildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.v_fixBuildUpTypeQueryGrid.toEditFn, FixBuildUpType.v_fixBuildUpTypeQueryGrid);

/**********************************************7.虚拟组成维护中-新增配件位置****************************************************************/
//组成型号选择列表
FixBuildUpType.buildUpTypeGrid2 = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "buildUpTypeName_searchId12"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("buildUpTypeName_searchId12").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;		
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.buildUpTypeGrid2, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.buildUpTypeGrid2.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				FixBuildUpType.buildUpTypeGrid2.store.baseParams.fixPlaceIdx = fixPlaceIdx;
				FixBuildUpType.buildUpTypeGrid2.store.baseParams.type = type;
				FixBuildUpType.buildUpTypeGrid2.store.baseParams.partsTypeIDX = partsTypeIDX; 
				FixBuildUpType.buildUpTypeGrid2.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型,
						partsTypeIDX:partsTypeIDX
					}																
				});	*/			
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.buildUpTypeGrid2)) return;
        		var tempData = FixBuildUpType.buildUpTypeGrid2.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.buildUpTypeGrid2.getStore().reload();
	                        FixBuildUpType.fixBuildUpTypeGrid2.getStore().reload();
	                        FixBuildUpType.buildUpTypeWin12.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.buildUpTypeGrid2.un('rowdblclick', FixBuildUpType.buildUpTypeGrid2.toEditFn, FixBuildUpType.buildUpTypeGrid2);
//组成型号选择窗口
FixBuildUpType.buildUpTypeWin12 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.buildUpTypeGrid2],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.buildUpTypeWin12.hide(); }
            }]
});
//设为缺省可安装组成型号
FixBuildUpType.setDefaultFixBuildType12 = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
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
//可安装组成型号tab
FixBuildUpType.fixBuildUpTypeGrid2 = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		FixBuildUpType.buildUpTypeWin12.show();
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(FixBuildUpType.fixBuildUpTypeGrid2)) return;
	    		var ids = $yd.getSelectedIdx(FixBuildUpType.fixBuildUpTypeGrid2);
	    		FixBuildUpType.setDefaultFixBuildType12(ids[0],FixBuildUpType.fixBuildUpTypeGrid2);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.buildUpTypeGrid2.store.reload();
    }
});
//移除侦听器
FixBuildUpType.fixBuildUpTypeGrid2.un('rowdblclick', FixBuildUpType.fixBuildUpTypeGrid2.toEditFn, FixBuildUpType.fixBuildUpTypeGrid2);
/***************************8.虚拟组成维护-新增虚拟位置****************************************/
//虚拟组成型号选择列表
FixBuildUpType.v_buildUpTypeGrid12 = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	singleSelect: true,
	//remoteSort: false,
	tbar: [{
        	xtype:"label", text:"  组成型号：" 
    	},{			
            xtype: "textfield",    
            id: "v_buildUpTypeName_searchId12"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("v_buildUpTypeName_searchId12").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;
				FixBuildUpType.buildUpSelectListLoad(FixBuildUpType.v_buildUpTypeGrid12, searchParam);//过滤【选择组成型号列表】
				/*FixBuildUpType.v_buildUpTypeGrid12.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型
						trainTypeIDX:trainTypeIDX//车型主键
					}																
				});	*/			
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FixBuildUpType.v_buildUpTypeGrid12)) return;
        		var tempData = FixBuildUpType.v_buildUpTypeGrid12.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FixBuildUpType.v_buildUpTypeGrid12.getStore().reload();
	                        FixBuildUpType.v_fixBuildUpTypeGrid12.getStore().reload();
	                        FixBuildUpType.v_buildUpTypeWin12.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode', sortable: true 
				},{
					header:'组成型号', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
FixBuildUpType.v_buildUpTypeGrid12.un('rowdblclick', FixBuildUpType.v_buildUpTypeGrid12.toEditFn, FixBuildUpType.v_buildUpTypeGrid12);
//虚拟组成型号选择窗口
FixBuildUpType.v_buildUpTypeWin12 =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FixBuildUpType.v_buildUpTypeGrid12],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FixBuildUpType.v_buildUpTypeWin12.hide(); }
            }]
});

//可安装虚拟组成型号列表
FixBuildUpType.v_fixBuildUpTypeGrid12 = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		if(FixBuildUpType.v_fixBuildUpTypeGrid12.store.getCount() > 0){
	    			MyExt.Msg.alert("该虚拟位置已安装组成，不能再安装！");
	    			return;
	    		}
	    		FixBuildUpType.v_buildUpTypeWin12.show();
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	FixBuildUpType.v_buildUpTypeGrid12.store.reload();
    }
});
//移除侦听器
FixBuildUpType.v_fixBuildUpTypeGrid12.un('rowdblclick', FixBuildUpType.v_fixBuildUpTypeGrid12.toEditFn, FixBuildUpType.v_fixBuildUpTypeGrid12);
});