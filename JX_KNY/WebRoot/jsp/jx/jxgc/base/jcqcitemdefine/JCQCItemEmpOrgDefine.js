/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JCQCItemEmpOrgDefine');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	JCQCItemEmpOrgDefine.searchParams = {};
	JCQCItemEmpOrgDefine.qcEmpIDX = "###";
	JCQCItemEmpOrgDefine.treePath = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义质量检查人员所辖机构表格开始 ************** */
	JCQCItemEmpOrgDefine.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jCQCItemEmpOrgDefine!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/jCQCItemEmpOrgDefine!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/jCQCItemEmpOrgDefine!logicDelete.action',            //删除数据的请求URL
	    tbar: ['delete'],
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'质量检查人员主键', dataIndex:'qcEmpIDX', hidden:true
		},{
			header:'检查机构ID', dataIndex:'checkOrgID', hidden:true
		},{
			header:'检查机构', dataIndex:'checkOrgName'
		},{
			header:'检查机构序列', dataIndex:'checkOrgSeq', hidden:true
		}],
		afterDeleteFn: function(){
        	// 重新加载【质量检查人员表格】
			JCQCEmpView.grid.store.reload();
//            // 重新加载树
//			JCQCItemEmpOrgDefine.tree.root.reload();
//            // 刷新并展开树到当前编辑的节点
//			JCQCItemEmpOrgDefine.tree.expandPath(JCQCItemEmpOrgDefine.treePath); 
		},
		toEditFn: function() {
			
		}
	});
	JCQCItemEmpOrgDefine.grid.store.setDefaultSort('checkOrgID', 'ASC');
	// 取消表格双击进行编辑的事件监听
	JCQCItemEmpOrgDefine.grid.on('rowdblclick', JCQCItemEmpOrgDefine.grid.toEditFn, JCQCItemEmpOrgDefine.grid);
	// 数据加载时的参数设置
	JCQCItemEmpOrgDefine.grid.store.on('beforeload', function(){
		var searchParams = JCQCItemEmpOrgDefine.searchParams;
		searchParams.qcEmpIDX = JCQCItemEmpOrgDefine.qcEmpIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员所辖机构表格结束 ************** */
	
	/** ************** 定义组织机构树开始 ************** */
	JCQCItemEmpOrgDefine.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + '/omOrganizationSelect!customTree.action'
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: systemOrgname,
	        id: systemOrgid,
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : true,
	    border : false,
	    listeners: {
	        dblclick: function(node) {
	        	JCQCItemEmpOrgDefine.treePath = node.getPath();
	        	// 声明一个QCEmpOrg的JSON对象
	        	var entityJson = {};
	        	entityJson.checkOrgID = node.id;					// 检查机构ID
	        	entityJson.checkOrgName = node.text;				// 检查机构名称
	        	entityJson.checkOrgSeq = node.attributes.orgseq;	// 检查机构序列
	        	entityJson.qcEmpIDX = JCQCItemEmpOrgDefine.qcEmpIDX;			// 质量检查人员主键
	        	// Ajax后台数据处理
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
		            url: ctx + '/jCQCItemEmpOrgDefine!saveOrUpdate.action',
		            jsonData: entityJson,
		            success: function(response, options){
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                	// 重新加载【检查人员所辖机构表格】
		                    JCQCItemEmpOrgDefine.grid.store.reload();
		                	// 重新加载【质量检查人员表格】
		                    JCQCEmpView.grid.store.reload();
		                    
//		                    // 重新加载树
//		                    JCQCItemEmpOrgDefine.tree.root.reload();
//		                    // 刷新并展开树到当前编辑的节点
//							JCQCItemEmpOrgDefine.tree.expandPath(JCQCItemEmpOrgDefine.treePath); 
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        }));
	        },
	        beforeload: function(node){
//	        	var queryHql = "From OmOrganization where status = 'running' And orgid Not In (Select checkOrgID From JCQCItemEmpOrgDefine Where recordStatus = 0 And qcEmpIDX = \'"+JCQCItemEmpOrgDefine.qcEmpIDX+"\')";
	        	var queryHql = "";
	        	this.loader.dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' + node.id + "&queryHql=" + queryHql;
	        }
	    }    
	});
	/** ************** 定义组织机构树结束 ************** */
	
	/** ************** 定义质量检查机构维护窗口开始 ************** */
	JCQCItemEmpOrgDefine.win =new Ext.Window({
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
				items:JCQCItemEmpOrgDefine.tree				// 组织机构树
			},
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: JCQCItemEmpOrgDefine.grid			// 已添加的组织机构列表
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