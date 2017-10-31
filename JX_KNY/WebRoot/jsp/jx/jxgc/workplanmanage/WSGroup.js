/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('WSGroup');
	
	/** **************** 定义全局变量开始 **************** */
	WSGroup.labelWidth = 100;
	WSGroup.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	WSGroup.grid = new Ext.yunda.RowEditorGrid({
		title:'工位组',
	    loadURL: ctx + '/wSGroup!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wSGroup!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wSGroup!delete.action',            //删除数据的请求URL
	    tbar:['add', 'delete', '->', {
	    	id:'tf_name',
	    	xtype: 'textfield', emptyText: '输入工位组名称快速检索', enableKeyEvents:true, width: WSGroup.fieldWidth, listeners: {
//	    		change: function(field, newValue, oldValue ){
//	    			if (newValue != oldValue) {
//	    				WSGroup.grid.store.load();
//	    			}
//	    		},
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
	    				WSGroup.grid.store.load();
					}
				}
	    	}
	    }, {
	    	text:"查询", iconCls:'searchIcon', handler:function(){
	    		WSGroup.grid.store.load();
	    	}
	    }, {
	    	text:"重置", iconCls:'resetIcon', handler:function(){
	    		Ext.getCmp('tf_name').reset();
	    		WSGroup.grid.store.load();
	    	}
	    }],
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工位组名称', dataIndex:'name', editor: { xtype:'textfield', maxLength:50, allowBlank: false }
		}],
		
		beforeAddButtonFn: function() {
	    	// 禁用工位组明细工具栏
			WSGroupItem.grid.getTopToolbar().disable();
			return true;
		},
		
	    afterSaveSuccessFn: function(result, response, options){
	    	// 启用工位组明细工具栏
	    	WSGroupItem.grid.getTopToolbar().enable();
	        this.store.reload();
	        alertSuccess();
	    },
	    
	    cancelFn: function(rowEditor, pressed){
	    	// 启用工位组明细工具栏
	    	WSGroupItem.grid.getTopToolbar().enable();
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	        }
	        rowEditor.grid.view.refresh();
	        // 
	        var sm = WSGroup.grid.getSelectionModel();
	        if (sm.getCount() > 0) {
	    		WSGroupItem.grid.getTopToolbar().enable();
	        } else {
	    		WSGroupItem.grid.getTopToolbar().disable();
	        }
	    },
	    
	     beforeEditFn: function(rowEditor, rowIndex){
	    	// 禁用工位组明细工具栏
	    	WSGroupItem.grid.getTopToolbar().disable();
	        return true;
	    }
	    
	})
	
	// 选择一条记录时，自动加载工位组明细表格数据
	WSGroup.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, record){
    	// 重新加载【工位组明细】表格数据
    	WSGroupItem.wsGroupIDX = record.get('idx');						// 设置作业流程主键
    	if (Ext.isEmpty(WSGroupItem.wsGroupIDX)) {
	    	WSGroupItem.grid.store.removeAll();
	    	return;
    	} 
		WSGroupItem.grid.getTopToolbar().enable();
    	WSGroupItem.grid.store.load();
	});
	
	// 工位组表格数据加载成功后，自动选择第一条记录
	WSGroup.grid.store.on('load', function(store, records, options){ 
		if (store.getCount() > 0) {
			// 禁用工位组明细表格工具栏
			WSGroupItem.grid.getTopToolbar().enable();
			WSGroup.grid.getSelectionModel().selectRow(0);
		} else {
			// 启用工位组明细表格工具栏
			WSGroupItem.grid.getTopToolbar().disable();
			// 如果工位组数据，清空【工位组明细】表格数据
			if (WSGroupItem.grid.store.getCount() > 0) {
			 	WSGroupItem.grid.store.removeAll();
			}
		}
	});
	
	// 工位组数据表格加载前的查询条件设置
	WSGroup.grid.store.on('beforeload', function(){
		var name = Ext.getCmp('tf_name').getValue();
		var searchParams = {};
		if (!Ext.isEmpty(name)) {
			searchParams.name = name;			// 根据工位组名称进行查询
		}
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** **************** 定义设置工位组窗口开始 **************** */
	WSGroup.win=new Ext.Window({
		title:"设置工位组",
		width:1200,
		height:600,
		layout:"border",
		closeAction:"hide",
		maximized:true,
		defaults: { layout: 'fit' },
		items:[{
			region: 'west', width: 400, split: true, items: [WSGroup.grid]
		}, {
			region: 'center', items: [WSGroupItem.grid]
		}],
		listeners: {
			beforeshow: function(window) {
				if (!Ext.isEmpty(WSGroup.name)) {
					Ext.getCmp('tf_name').setValue(WSGroup.name);
				}
			},
			show: function(window) {
				// 加载工位组信息表格
				WSGroup.grid.store.load(/*{
					callback: function(records) {
						if (records.length > 0) {
						}
					}
				}*/);
			},
			hide: function(window) {
				// 隐藏行编辑器
				WSGroup.grid.rowEditor.slideHide(); 
				// 动态加载Base_Combo控件的候选数据
				if (Ext.getCmp('WSGroup_Combo')) {
		    		Ext.getCmp('WSGroup_Combo').cascadeStore();
				}
			}
		},
		buttonAlign: 'center', buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** **************** 定义设置工位组窗口结束 **************** */
	
	// 用于解决弹出窗口中含有“行编辑表格”在保存时，显示self的遮罩元素后不能隐藏遮罩效果的变相处理
	self.loadMask = null;
	
});