/**
 * 视频监控信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('SiteVideoNvr');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	SiteVideoNvr.labelWidth = 100;
	SiteVideoNvr.fieldWidth = 140;
	SiteVideoNvr.nvrIP = "127.0.0.1";					// 默认NVR网络IP地址
	SiteVideoNvr.nvrPort = "80";						// 默认NVR端口号
	SiteVideoNvr.username = "admin";					// 默认NVR登录用户名
	SiteVideoNvr.password = "12345678a";				// 默认NVR登录密码
	SiteVideoNvr.nvrName = "";							// 当前列表选中的NVR名称
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义视频监控NVR表格开始 **************** */
	SiteVideoNvr.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/siteVideoNvr!pageList.action',                 // 装载列表数据的请求URL
	    saveURL: ctx + '/siteVideoNvr!saveOrUpdate.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/siteVideoNvr!delete.action',            		// 删除数据的请求URL
	    tbar: ['add', 'delete', 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'名称', dataIndex:'nvrName', editor:{  maxLength:50, allowBlank: false }
		},{
			header:'IP地址', dataIndex:'nvrIP', editor:{  id: 'id_nvrIP', maxLength:50, allowBlank: false, vtype:'ip' }
		},{
			header:'端口号', dataIndex:'nvrPort', editor:{ id: 'id_nvrPort', maxLength:20, allowBlank: false, value: SiteVideoNvr.nvrPort }
		},{
			header:'用户名', dataIndex:'username', editor:{ id: 'id_username', maxLength:50, allowBlank: false, value: SiteVideoNvr.username  }
		},{
			header:'密码', dataIndex:'password', editor:{ id: 'id_password', maxLength:50, allowBlank: false  }
		}],
		
		beforeAddButtonFn: function() {
			SiteVideoNvrChanel.grid.getTopToolbar().disable();
			return true;
		},
		
		afterAddButtonFn: function(){
			Ext.getCmp('id_nvrIP').setValue(SiteVideoNvr.nvrIP);
			Ext.getCmp('id_nvrPort').setValue(SiteVideoNvr.nvrPort);
			Ext.getCmp('id_username').setValue(SiteVideoNvr.username);
			Ext.getCmp('id_password').setValue(SiteVideoNvr.password);
		},
		
		afterSaveSuccessFn: function(result, response, options){
	        // 新增完成后，默认选择刚被新增的记录
	        SiteVideoNvr.nvrName = result.entity.nvrName;
	        this.store.reload();
	        alertSuccess();
	    },
	    
		beforeEditFn: function(rowEditor, rowIndex){
	    	// 禁用工位组明细工具栏
	    	SiteVideoNvrChanel.grid.getTopToolbar().disable();
	        return true;
	    },
	    
	    cancelFn: function(rowEditor, pressed){
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	        }
	        rowEditor.grid.view.refresh();
	        // 
	        var sm = SiteVideoNvr.grid.getSelectionModel();
	        if (sm.getCount() > 0) {
	    		SiteVideoNvrChanel.grid.getTopToolbar().enable();
	        } else {
	    		SiteVideoNvrChanel.grid.getTopToolbar().disable();
	        }
	    }
	});
	// 默认以NVR名称升序排序
	SiteVideoNvr.grid.store.setDefaultSort('nvrName', 'ASC');
	
	// 数据加载完成后，默认选择一条记录
	SiteVideoNvr.grid.store.on('load', function(store, records, options){
		if (store.getCount() <= 0) {
			return;
		}
		if (Ext.isEmpty(SiteVideoNvr.nvrName)) {
			SiteVideoNvr.grid.getSelectionModel().selectRow(0);
		} else {
			for (var i = 0; i < records.length; i++) {
				if(records[i].get('nvrName') == SiteVideoNvr.nvrName) {
					SiteVideoNvr.grid.getSelectionModel().selectRow(i);
					break;
				}
			}
		}
	});
	
	// 选择一条记录时，自动通道号明细表格数据
	SiteVideoNvr.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, record){
    	// 重新加载【工位组明细】表格数据
    	SiteVideoNvrChanel.videoNvrIDX = record.get('idx');						// 设置作业流程主键
    	if (Ext.isEmpty(SiteVideoNvrChanel.videoNvrIDX)) {
	    	SiteVideoNvrChanel.grid.store.removeAll();
	    	return;
    	} 
		SiteVideoNvrChanel.grid.getTopToolbar().enable();
    	SiteVideoNvrChanel.grid.store.load();
	});
	/** **************** 定义视频监控NVR表格结束 **************** */
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout:"ux.row",
		defaults:{
			layout:"fit", rowHeight:'.34'
		},
		items: [{
			items: SiteVideoNvr.grid
		}, {
			rowHeight:'.66',
			items: SiteVideoNvrChanel.grid
		}]
	});
});