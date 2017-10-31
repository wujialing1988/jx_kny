/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JCQCItemEmpDefine');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	JCQCItemEmpDefine.searchParams = {};
	JCQCItemEmpDefine.qCItemIDX = "###";
	JCQCItemEmpDefine.treePath = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义质量检查人员所辖机构表格开始 ************** */
	JCQCItemEmpDefine.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jCQCItemEmpDefine!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/jCQCItemEmpDefine!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/jCQCItemEmpDefine!logicDelete.action',            //删除数据的请求URL
	    tbar: ['delete'],
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'质量检查项主键', dataIndex:'qCItemIDX', hidden:true
		},{
			header:'检查人员ID', dataIndex:'checkEmpID', hidden:true
		},{
			header:'检查人员名称', dataIndex:'checkEmpName'
		}],
		afterDeleteFn: function(){
        	// 重新加载【质量检查人员表格】
			JCQCEmpView.grid.store.reload();
		},
		toEditFn: function() {
			
		}
	});
	JCQCItemEmpDefine.grid.store.setDefaultSort('checkEmpName', 'ASC');
	// 取消表格双击进行编辑的事件监听
	JCQCItemEmpDefine.grid.on('rowdblclick', JCQCItemEmpDefine.grid.toEditFn, JCQCItemEmpDefine.grid);
	// 数据加载时的参数设置
	JCQCItemEmpDefine.grid.store.on('beforeload', function(){
		var searchParams = JCQCItemEmpDefine.searchParams;
		searchParams.qCItemIDX = JCQCItemEmpDefine.qCItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员所辖机构表格结束 ************** */
	
	/** ************** 定义组织机构树开始 ************** */
	JCQCItemEmpDefine.tree =  new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : '机构人员',
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'org',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
//    	enableDrag:true,
	    listeners: {
    		render : function() {
    			JCQCItemEmpDefine.tree.root.reload();
			    JCQCItemEmpDefine.tree.getRootNode().expand();
    		},
	        dblclick: function(node) {
	        	if (node.attributes.nodetype == 'emp'){
		        	JCQCItemEmpDefine.treePath = node.getPath();
		        	// 声明一个QCEmp的JSON对象
		        	var entityJson = {};
		        	entityJson.checkEmpID = node.attributes.empid;					// 检查人员ID
		        	entityJson.checkEmpName = node.attributes.empname;				// 检查人员名称
		        	entityJson.qCItemIDX = JCQCItemEmpDefine.qCItemIDX;							// 质量检查项主键
		        	// Ajax后台数据处理
					Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
			            url: ctx + '/jCQCItemEmpDefine!saveOrUpdate.action',
			            jsonData: entityJson,
			            success: function(response, options){
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                	// 重新加载【检查人员所辖机构表格】
			                    JCQCItemEmpDefine.grid.store.reload();
			                	// 重新加载【质量检查人员表格】
			                    JCQCEmpView.grid.store.reload();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        }));
    			}
	        },
	        beforeload: function(node){
		        var tempid;
				if(node.id=='ROOT_0') tempid = node.id;
				else tempid = node.id.substring(2,node.id.length);
		    	this.loader.dataUrl = ctx + '/organization!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;}
		    }    
	});
	/** ************** 定义组织机构树结束 ************** */
	
	/** ************** 定义质量检查机构维护窗口开始 ************** */
	JCQCItemEmpDefine.win =new Ext.Window({
		title:"质量检查机构",
		width:560,
		height:402,
		layout:"border",
		closeAction: 'hide',
		modal: true,
		items:[
			{
				xtype:"panel",
				region:"west",
				layout:"fit",
				width:200, 
				items:JCQCItemEmpDefine.tree				// 组织机构树
			},
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: JCQCItemEmpDefine.grid			// 已添加的组织机构列表
			}
		],
		buttonAlign: 'center',
		buttons:[{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
//				// 隐藏窗口后，重新加载【质量检查人员】表格
//				JCQCEmpView.grid.store.reload();
			}
		}]
	})
	/** ************** 定义质量检查机构维护窗口结束 ************** */
	
});