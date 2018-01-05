/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglItemEmpDefine');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	ZbglItemEmpDefine.searchParams = {};
	ZbglItemEmpDefine.qcItemIDX = "###";
	ZbglItemEmpDefine.treePath = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义质量检查人员所辖机构表格开始 ************** */
	ZbglItemEmpDefine.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglQualityControlItemEmpDefine!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/zbglQualityControlItemEmpDefine!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/zbglQualityControlItemEmpDefine!delete.action',            //删除数据的请求URL
	    tbar: ['delete'],
	    storeAutoLoad: false,
		fields: [{
			header:i18n.QualityInspectionConfig.idx, dataIndex:'idx', hidden:true
		},{
			header:i18n.QualityInspectionConfig.qualityCtrIdx, dataIndex:'qCItemIDX', hidden:true
		},{
			header:i18n.QualityInspectionConfig.checkEmpID, dataIndex:'checkEmpID', hidden:true
		},{
			header:i18n.QualityInspectionConfig.checkEmpName, dataIndex:'checkEmpName'
		}],
		afterDeleteFn: function(){
        	// 重新加载【质量检查人员表格】
			QualityControlItemEmpDefine.grid.store.reload();
		},
		toEditFn: function() {
			
		}
	});
	ZbglItemEmpDefine.grid.store.setDefaultSort('checkEmpName', 'ASC');
	// 取消表格双击进行编辑的事件监听
	ZbglItemEmpDefine.grid.on('rowdblclick', ZbglItemEmpDefine.grid.toEditFn, ZbglItemEmpDefine.grid);
	// 数据加载时的参数设置
	ZbglItemEmpDefine.grid.store.on('beforeload', function(){
		var searchParams = ZbglItemEmpDefine.searchParams;
		searchParams.qcItemIDX = ZbglItemEmpDefine.qcItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员所辖机构表格结束 ************** */
	
	/** ************** 定义组织机构树开始 ************** */
	ZbglItemEmpDefine.tree =  new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.QualityInspectionConfig.orgPerson,
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
    			ZbglItemEmpDefine.tree.root.reload();
			    ZbglItemEmpDefine.tree.getRootNode().expand();
    		},
	        dblclick: function(node) {
	        	if (node.attributes.nodetype == 'emp'){
		        	ZbglItemEmpDefine.treePath = node.getPath();
		        	// 声明一个QCEmp的JSON对象
		        	var entityJson = {};
		        	entityJson.checkEmpID = node.attributes.empid;					// 检查人员ID
		        	entityJson.checkEmpName = node.attributes.empname;				// 检查人员名称
		        	entityJson.qcItemIDX = ZbglItemEmpDefine.qcItemIDX;							// 质量检查项主键
		        	entityJson.siteID = siteID;							// 站场标示
		        	// Ajax后台数据处理
					Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
			            url: ctx + '/zbglQualityControlItemEmpDefine!saveOrUpdate.action',
			            jsonData: entityJson,
			            success: function(response, options){
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                	// 重新加载【检查人员所辖机构表格】
			                    ZbglItemEmpDefine.grid.store.reload();
			                	// 重新加载【质量检查人员表格】
			                    QualityControlItemEmpDefine.grid.store.reload();
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
	ZbglItemEmpDefine.win =new Ext.Window({
		title:i18n.QualityInspectionConfig.QualityInsOrg,
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
				items:ZbglItemEmpDefine.tree				// 组织机构树
			},
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: ZbglItemEmpDefine.grid			// 已添加的组织机构列表
			}
		],
		buttonAlign: 'center',
		buttons:[{
			text: i18n.QualityInspectionConfig.close, iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
//				// 隐藏窗口后，重新加载【质量检查人员】表格
//				qualityControlItemEmpDefine.grid.store.reload();
			}
		}]
	})
	/** ************** 定义质量检查机构维护窗口结束 ************** */
	
});