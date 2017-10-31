/**
 * 人工节点映射配置，表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('Mapping');                       //定义命名空间
	Mapping.searchParam = {};
	Mapping.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/manualNodeMapping!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/manualNodeMapping!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/manualNodeMapping!logicDelete.action',            //删除数据的请求URL
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 250,
	    tbar:['search', 'add', 'delete', 'refresh',{
	    	text: '更新映射MAP',
	    	iconCls: 'tableRefreshIcon',
	    	handler: function(){
	    		Ext.Ajax.request({
	    			url: ctx + "/manualNodeMapping!refreshMappings.action",
	    			success: function(response, options){
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                RepairAccount.grid.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
	    		});
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'节点标识', dataIndex:'manualNode',editor:{allowBlank: false, maxLength: 50 }
		},{
			header:'动作路径', dataIndex:'mappingURL', editor:{allowBlank: false, maxLength:200 }
		},{
			header:'描述', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength: 500 },searcher:{disabled:true}
		}],
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        this.saveForm.getForm().reset();
	    }
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items: Mapping.grid });
});