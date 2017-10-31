/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QCEmpOrg');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	QCEmpOrg.searchParams = {};
	QCEmpOrg.qcEmpIDX = "###";
	QCEmpOrg.treePath = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义质量检查人员所辖机构表格开始 ************** */
	QCEmpOrg.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/qCEmpOrg!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/qCEmpOrg!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/qCEmpOrg!logicDelete.action',            //删除数据的请求URL
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
			QCEmpView.grid.store.reload();
//            // 重新加载树
//			QCEmpOrg.tree.root.reload();
//            // 刷新并展开树到当前编辑的节点
//			QCEmpOrg.tree.expandPath(QCEmpOrg.treePath); 
		}
	});
	QCEmpOrg.grid.store.setDefaultSort('checkOrgID', 'ASC');
	// 取消表格双击进行编辑的事件监听
	QCEmpOrg.grid.on('rowdblclick', QCEmpOrg.grid.toEditFn, QCEmpOrg.grid);
	// 数据加载时的参数设置
	QCEmpOrg.grid.store.on('beforeload', function(){
		var searchParams = QCEmpOrg.searchParams;
		searchParams.qcEmpIDX = QCEmpOrg.qcEmpIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员所辖机构表格结束 ************** */
	
	/** ************** 定义组织机构树开始 ************** */
	QCEmpOrg.tree =  new Ext.tree.TreePanel({
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
	        	QCEmpOrg.treePath = node.getPath();
	        	// 声明一个QCEmpOrg的JSON对象
	        	var entityJson = {};
	        	entityJson.checkOrgID = node.id;					// 检查机构ID
	        	entityJson.checkOrgName = node.text;				// 检查机构名称
	        	entityJson.checkOrgSeq = node.attributes.orgseq;	// 检查机构序列
	        	entityJson.qcEmpIDX = QCEmpOrg.qcEmpIDX;			// 质量检查人员主键
	        	// Ajax后台数据处理
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
		            url: ctx + '/qCEmpOrg!saveOrUpdate.action',
		            jsonData: entityJson,
		            success: function(response, options){
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                	// 重新加载【检查人员所辖机构表格】
		                    QCEmpOrg.grid.store.reload();
		                	// 重新加载【质量检查人员表格】
		                    QCEmpView.grid.store.reload();
		                    
//		                    // 重新加载树
//		                    QCEmpOrg.tree.root.reload();
//		                    // 刷新并展开树到当前编辑的节点
//							QCEmpOrg.tree.expandPath(QCEmpOrg.treePath); 
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        }));
	        },
	        beforeload: function(node){
//	        	var queryHql = "From OmOrganization where status = 'running' And orgid Not In (Select checkOrgID From QCEmpOrg Where recordStatus = 0 And qcEmpIDX = \'"+QCEmpOrg.qcEmpIDX+"\')";
	        	var queryHql = "";
	        	this.loader.dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' + node.id + "&queryHql=" + queryHql;
	        }
	    }    
	});
	/** ************** 定义组织机构树结束 ************** */
	
	/** ************** 定义质量检查机构维护窗口开始 ************** */
	QCEmpOrg.win =new Ext.Window({
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
				items:QCEmpOrg.tree				// 组织机构树
			},
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: QCEmpOrg.grid			// 已添加的组织机构列表
			}
		],
		buttonAlign: 'center',
		buttons:[{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
//				// 隐藏窗口后，重新加载【质量检查人员】表格
//				QCEmpView.grid.store.reload();
			}
		}]
	})
	/** ************** 定义质量检查机构维护窗口结束 ************** */
	
});