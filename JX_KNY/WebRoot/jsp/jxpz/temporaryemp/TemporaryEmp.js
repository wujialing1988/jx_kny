/**
 * 临时人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TemporaryEmp');                       //定义命名空间
TemporaryEmp.orgId = "";
TemporaryEmp.orgName = "";
TemporaryEmp.orgSeq = "";
TemporaryEmp.searchParam = {} ;  //定义全局查询条件
//机构选择树
TemporaryEmp.OrgTree = new Ext.tree.TreePanel( {
	tbar :new Ext.Toolbar(),
	plugins: ['multifilter'],
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/omOrganizationSelect!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text : systemOrgname,
		id : systemOrgid,
		leaf : false,
		orgseq : ''
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	if("undefined" != node.attributes["orgdegree"]){
        		TemporaryEmp.orgId = node.id;
        		TemporaryEmp.orgName = node.text;
        		TemporaryEmp.orgSeq = node.attributes["orgseq"];
        		TemporaryEmp.grid.store.load();
        	}
        }
    }    
});
TemporaryEmp.OrgTree.on('beforeload', function(node){
    TemporaryEmp.OrgTree.loader.dataUrl = ctx + '/omOrganizationSelect!tree.action?parentIDX=' + node.id;
});
TemporaryEmp.OrgTree.getRootNode().expand();
TemporaryEmp.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/temporaryEmp!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/temporaryEmp!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/temporaryEmp!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'人员id', dataIndex:'empid', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'人员代码', dataIndex:'empcode', editor:{  maxLength:30 }
	},{
		header:'人员名称', dataIndex:'empname', editor:{  maxLength:50 }
	},{
		header:'性别', dataIndex:'gender', searcher:{disabled : true},
		renderer : function(v){
			if(v == 'm') return "男"
			else if(v == 'f') return "女"
			else return null
		}
	},{
		header:'人员状态', dataIndex:'empstatus', hidden:true, editor:{  maxLength:255 }
	},{
		header:'原所属车间', dataIndex:'oldPlant',hidden:true,  editor:{  maxLength:50 }
	},{
		header:'原所属班组', dataIndex:'oldTream', editor:{  maxLength:50 }, searcher:{disabled : true}
	},{
		header:'临时班组id', dataIndex:'temporaryTreamId',hidden:true, editor:{  maxLength:50 }
	},{
		header:'临时班组名称', dataIndex:'temporaryTreamName', editor:{  maxLength:50 }, searcher:{disabled : true}
	}],
	beforeAddButtonFn: function(){
		if("" == TemporaryEmp.orgId){
			MyExt.Msg.alert("请选选择班组！");
	   	  	return false;
		}
    	return true;
    },
	addButtonFn: function(){
		if(this.beforeAddButtonFn() == false)   return;
		EmployeeSelect.selectWin.show();
		EmployeeSelect.OrgTree.getRootNode().expand();
		EmployeeSelect.grid.store.load();
	},
	searchFn: function(searchParam){
    	TemporaryEmp.searchParam = TemporaryEmp.grid.searchForm.getForm().getValues();
    	TemporaryEmp.grid.store.load();
    }
});
//移除监听
TemporaryEmp.grid.un('rowdblclick', TemporaryEmp.grid.toEditFn, TemporaryEmp.grid);
TemporaryEmp.grid.store.on('beforeload',function(){
	     var searchParam = TemporaryEmp.searchParam;
	     searchParam.temporaryTreamId = TemporaryEmp.orgId ;
	     searchParam = MyJson.deleteBlankProp(searchParam);
	     this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
//页面主框架布局，左：机构树，右：临时人员设置列表
var viewport = new Ext.Viewport({ 
	layout : 'border', 
	border : false,
	items : [new Ext.Panel({
				width : 200,
				minSize : 160,
				maxSize : 280,
				region : "west",
				collapsible: true,
				title : '<span style="font-weight:normal">机构树</span>',
	        	iconCls : 'icon-expand-all',
	        	tools : [ {
		            id : 'refresh',
		            handler: function() {
		            	TemporaryEmp.OrgTree.getRootNode().reload();
		            }
		        }],
				items : [TemporaryEmp.OrgTree],
				autoScroll : true
			}), new Ext.Panel({
					region : "center",
					layout : "fit",
					items : [TemporaryEmp.grid]
				})]
});
});