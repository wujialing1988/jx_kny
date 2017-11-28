/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobProcessNodeDefWin');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	JobProcessNodeDefWin.labelWidth = 100;
	JobProcessNodeDefWin.fieldWidth = 140;
	
	JobProcessNodeDefWin.isRender = false;
	JobProcessNodeDefWin.treePath = "";
	
	JobProcessNodeDefWin.processIdx = "";
	JobProcessNodeDefWin.parentIDX = "ROOT_0";
	JobProcessNodeDefWin.flag = "";
	
	JobProcessNodeDefWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请稍候..."});
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 重新加载【作业节点树】
	JobProcessNodeDefWin.reloadTree = function(path) {
        JobProcessNodeDefWin.tree.root.reload(function() {
        	if (!path) {
				JobProcessNodeDefWin.tree.getSelectionModel().select(JobProcessNodeDefWin.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			JobProcessNodeDefWin.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        JobProcessNodeDefWin.tree.expandPath(path);
	        JobProcessNodeDefWin.tree.selectPath(path);
        }
	}

	/** **************** 定义全局函数结束 **************** */
   
	
	JobProcessNodeDefWin.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessNodeDef!pageList.action',             //装载列表数据的请求URL	       
    	storeAutoLoad: false,
    	viewConfig:null,
    	width:400,
	    tbar:[],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'流程主键', dataIndex:'processIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'上级主键', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'顺序号', dataIndex:'seqNo', width: 60, hidden:false, editor:{ xtype:'numberfield', maxLength:8 }
		},{
			header:'节点编码', dataIndex:'nodeCode', width: 90, editor:{  maxLength:50 }
		},{	
			header:'日历', dataIndex:'workCalendarIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'计划模式', dataIndex:'planMode', hidden:true, editor:{  maxLength:50 }
		},{
			header:'节点描述', dataIndex:'nodeDesc', width: 460,hidden:true, editor:{  maxLength:1000 }
		},{
			header:'是否叶子节点', dataIndex:'isLeaf', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'前置节点主键', dataIndex:'preNodeIDX', hidden:true, editor:{ disabled: true }
		},{
			header:'节点名称', dataIndex:'nodeName', width:150,  editor:{ disabled: true }
		},{
			header:'前置节点(序号)', dataIndex:'preNodeSeqNo', width:125, hidden:true,  editor:{ disabled: true }
		}],
		toEditFn: Ext.emptyFn //覆盖双击编辑事件
	});
		
	    
	
	  
	// 设置默认排序字段为“顺序号（升序）”
	JobProcessNodeDefWin.grid.store.setDefaultSort("seqNo", "ASC");
	
	JobProcessNodeDefWin.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.processIDX = JobProcessNodeDefWin.processIdx;
		searchParams.parentIDX = JobProcessNodeDefWin.parentIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** **************** 定义作业流程节点树开始 **************** */
	// 选择一个流程节点时的初始化函数处理
	JobProcessNodeDefWin.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
    	// 获取当前节点的路径信息
    	JobProcessNodeDefWin.treePath = node.getPath();	
    	JobProcessNodeDefWin.parentIDX = node.id;
    	
    	JobProcessNodeRelDef.parentIDX = node.id;		// 设置上级作业节点主键	
			
			// 重新加载作业节点表格数据
    		JobProcessNodeDefWin.grid.store.load();
    		
    		// 重命名Tab - 只取序列号
    		if (node.text.lastIndexOf(".") <= 0) {
        		Ext.getCmp('tabpanel_node').setTitle(node.text + " - 作业节点")
    		} else {
        		Ext.getCmp('tabpanel_node').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 下级作业节点")
    		}
    	}
	
	// 机车检修作业流程节点树型列表
	JobProcessNodeDefWin.tree =  new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jobProcessNodeDef!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '作业流程节点',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		JobProcessNodeDefWin.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		JobProcessNodeDefWin.reloadTree();
	    	},
	        click: function(node, e) {
//	        	JobProcessNodeDefWin.initFn(node);
	        },
	        beforeload:  function(node){
			    JobProcessNodeDefWin.tree.loader.dataUrl = ctx + '/jobProcessNodeDef!tree.action?parentIDX=' + node.id + '&processIDX=' + JobProcessNodeDefWin.processIdx;
			},
			load: function(node) {
//				JobProcessNodeDefWin.initFn(node);
			}			
	    },
	    enableDD:true
	});
	
	// 选中的树节点变化时的事件监听函数
	JobProcessNodeDefWin.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
    	JobProcessNodeDefWin.initFn(node);
	});
	/** **************** 定义作业流程节点树结束 **************** */
	
	/** **************** 定义作业流程编辑窗口开始 **************** */
	JobProcessNodeDefWin.win = new Ext.Window({
		title:"作业流程节点", width:650, height:350,
		layout:"border", closeAction:"hide",
		items:[{
			title : '<span style="font-weight:normal">作业流程节点树</span>',
			tbar: [],
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	JobProcessNodeDefWin.reloadTree(JobProcessNodeDefWin.treePath);
	            }
	        } ],
			region:"west", collapsible: true,
			width:250,
			layout:"fit",
			items:[JobProcessNodeDefWin.tree]
		}, {
			xtype:"tabpanel",
			id:"tabpanel",
			activeTab:0,
			region:"center",
			items:[{
				id:"tabpanel_node",
				title:"流程节点",
				layout:"fit",
				items:[JobProcessNodeDefWin.grid]		
			}],
			listeners : {
				render : function(){			
					this.setActiveTab(0);
				}
			}
		}],
		buttonAlign:'center',
		buttons:[{text:'确认', iconCls: 'checkIcon', handler:function(){
				JobProcessNodeDefWin.submit();
			}
		}, {		
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			show: function(window) {
				JobProcessNodeDefWin.parentIDX = "ROOT_0";
				JobProcessNodeDefWin.grid.store.load();
				JobProcessNodeDefWin.reloadTree();
				JobProcessNodeRelDef.processIDX = JobProcessNodeDefWin.processIDX;	
				Ext.getCmp('tabpanel').setActiveTab(0);
			}
		}
	});
	/** **************** 定义作业流程窗口结束 **************** */
	//确认提交方法，后面可覆盖此方法完成查询
	JobProcessNodeDefWin.submit = function(){alert("请覆盖，TechReformProject.submit 方法完成自己操作业务！");}
});