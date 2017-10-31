/**
 * 可安装组成型号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FixBuildUpType');                       //定义命名空间
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
				FixBuildUpType.buildUpTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
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
				});				
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
//可安装组成型号查看
FixBuildUpType.fixBuildUpTypeQueryGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [],
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
	}]
});
//移除侦听器
FixBuildUpType.fixBuildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.fixBuildUpTypeQueryGrid.toEditFn, FixBuildUpType.fixBuildUpTypeQueryGrid);
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
				FixBuildUpType.v_buildUpTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx,//位置主键
						type:type,//组成类型
						trainTypeIDX:trainTypeIDX//车型主键
					}																
				});				
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

//虚拟组成设置-可安装组成型号查看
FixBuildUpType.v_fixBuildUpTypeQueryGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    singleSelect: true,
    //remoteSort: false,
    tbar: [],
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
	}]
});
//移除侦听器
FixBuildUpType.v_fixBuildUpTypeQueryGrid.un('rowdblclick', FixBuildUpType.v_fixBuildUpTypeQueryGrid.toEditFn, FixBuildUpType.v_fixBuildUpTypeQueryGrid);
});