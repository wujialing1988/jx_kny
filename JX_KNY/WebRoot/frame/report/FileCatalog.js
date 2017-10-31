/**
 * 报表模板管理类型标识目录 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FileCatalog');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	FileCatalog.labelWidth = 100;
	FileCatalog.fieldWidth = 140;
	FileCatalog.searchParams = {};
	FileCatalog.treePath = "";						// 报表目录树路径
	FileCatalog.folderFullPath = "";				// 当前报表目录的全路径信息
	FileCatalog.idx = PATH_ROOT;					// 当前报表目录的idx主键
	FileCatalog.isEditFn = false;					// 是否是“编辑”操作
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 重新加载【报表目录树】
	FileCatalog.reloadTreeFn = function(path) {
        FileCatalog.tree.root.reload();
        if (path == undefined || path == "" || path == "###") {
			FileCatalog.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        FileCatalog.tree.expandPath(path);
        }
	
	}
	
	// 【报表目录树】选择新节点时的初始化操作
	FileCatalog.initFn = function(node) {
    	// 记录当前报表模板根目录的展开路径信息
    	FileCatalog.treePath = node.getPath();
    	
    	FileCatalog.idx = node.id;
    	
    	var folderFullPath = node.getPath('folderNameEN').substring(2);
    	while (folderFullPath.indexOf("/") >= 0) {
    		folderFullPath = folderFullPath.replace("/", ".");
    	}
    	FileCatalog.folderFullPath = folderFullPath;
    	
    	// 重新加载报表打印模板列表
    	if (Ext.isEmpty(folderFullPath)) {
    		PrinterModule.deployCatalog = "";
    	} else {
    		PrinterModule.deployCatalog = folderFullPath;
    	}
    	PrinterModule.grid.store.load();
    	
    	// 如果不可编辑，则禁用工具栏的【编辑】【删除】按钮
    	if (CONST_STR_F == node.attributes["editable"] || node.id == PATH_ROOT) {
    		Ext.getCmp('treeBtnEdit').disable();
    		Ext.getCmp('treeBtnDel').disable();
    	} else {
    		Ext.getCmp('treeBtnEdit').enable();
    		Ext.getCmp('treeBtnDel').enable();
    	}
	}
	// 保存函数
	FileCatalog.saveFn = function() {
		var form = FileCatalog.saveForm.getForm();
		if (!form.isValid()) {
			return;	
		}
		var entityJson = form.getValues();
		// 清除json对象的空属性
		entityJson = MyJson.deleteBlankProp(entityJson);
		// Ajax请求
		Ext.Ajax.request({
			url:ctx + '/fileCatalog!saveOrUpdate.action',
			jsonData: entityJson,
			
		    //请求成功后的回调函数
			success: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            FileCatalog.reloadTreeFn(FileCatalog.treePath);
		            
		            // 回显idx主键字段，使支持多次保存
		            FileCatalog.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	
	
	/** **************** 定义保存表单开始 **************** */
	FileCatalog.saveForm = new Ext.form.FormPanel({
		border:false, baseCls:'x-plain',
		padding:'10px',
		labelWidth:FileCatalog.labelWidth,
		defaults:{
			xtype:'textfield',
			anchor:'95%'
		},
		items:[{
			name:'parentIDX', fieldLabel:'上级主键', hidden:true
		}, {
			name:'parentFolderFullPath', fieldLabel:'上级文件夹', disabled:true
		}, {
			name:'folderNameEN', vtype:'alphanum2', allowBlank:false, fieldLabel:'英文名称', maxLength:50
		}, {
			name:'folderNameCH', fieldLabel:'中文名称', maxLength:50
		}, {
			name:'folderDesc', fieldLabel:'描述', xtype:'textarea', height:75, maxLength:200
		}, {
			name:'idx', hidden:true, fieldLabel:'idx主键'
		}, {
			name:'editable', hidden:true, fieldLabel:'是否可编辑'
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	
	/** **************** 定义新增窗口开始 **************** */
	FileCatalog.saveWin = new Ext.Window({
		title:'报表部署目录新增',
		plain:true, modal: true,
		closeAction:'hide',
		layout:'fit',
		width:400, height:250,
		items:[FileCatalog.saveForm],
		buttonAlign: 'center',
		buttons:[{
			text:"保存", iconCls:'saveIcon', handler:function() {
				FileCatalog.saveFn();
			}
		}, {
			text:"取消", iconCls:'cancelIcon', handler:function() {
				this.findParentByType('window').hide();
			}
		}, {
			text:"重置", iconCls:'resetIcon', hidden:true, handler:function() {
				FileCatalog.saveForm.getForm().getEl().dom.reset();
			}
		}]
	});
	/** **************** 定义新增窗口结束 **************** */
	
	/** **************** 定义模板目录树开始 **************** */
	FileCatalog.tree = new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/fileCatalog!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '报表模板目录',
	        id: PATH_ROOT,
	        leaf: false,
	        iconCls: 'folderIcon'
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		FileCatalog.tree.root.expand()
	    	},
	    	beforeload: function(node) {
			    FileCatalog.tree.loader.dataUrl = ctx + '/fileCatalog!tree.action?parentIdx=' + node.id
	    		
	    	},
	        click: function(node, e) {
				FileCatalog.initFn(node);
			},
			dblclick: function(node, e) {
			}
	    }    
	});
	
	// 选中的树节点变化时的事件监听函数
	FileCatalog.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
		if (node == null || node.id == null || node.id == "ROOT_0") {
			return;
		}
		FileCatalog.initFn(node);
	});
	/** **************** 定义模板目录树开始 **************** */
	
	/*FileCatalog.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/fileCatalog!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/fileCatalog!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/fileCatalog!logicDelete.action',            //删除数据的请求URL
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'是否可编辑', dataIndex:'editable', editor:{  maxLength:1 }
		},{
			header:'文件夹描述', dataIndex:'folderDesc', editor:{  maxLength:200 }
		},{
			header:'文件夹英文名称', dataIndex:'folderNameEN', editor:{  maxLength:50 }
		},{
			header:'文件夹英文全路径', dataIndex:'folderFullPath', editor:{  maxLength:200 }
		},{
			header:'上级文件夹英文全路径', dataIndex:'parentFolderFullPath', editor:{  maxLength:200 }
		},{
			header:'文件夹中文名称', dataIndex:'folderNameCH', editor:{  maxLength:50 }
		}]
	});*/
});