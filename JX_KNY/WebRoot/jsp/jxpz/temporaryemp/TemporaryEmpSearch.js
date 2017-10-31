/**
 * 临时人员查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TemporaryEmp');                       //定义命名空间
TemporaryEmp.searchParam = {} ;  //定义全局查询条件
TemporaryEmp.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/temporaryEmp!pageList.action',                 //装载列表数据的请求URL
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
	searchFn: function(searchParam){
    	TemporaryEmp.searchParam = TemporaryEmp.grid.searchForm.getForm().getValues();
    	TemporaryEmp.grid.store.load();
    }
});
//移除监听
TemporaryEmp.grid.un('rowdblclick', TemporaryEmp.grid.toEditFn, TemporaryEmp.grid);
TemporaryEmp.grid.store.on('beforeload',function(){
	     var searchParam = TemporaryEmp.searchParam;
	     searchParam = MyJson.deleteBlankProp(searchParam);
	     this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
//页面主框架布局
var viewport = new Ext.Viewport({ 
	layout : 'fit', 
	items : [TemporaryEmp.grid]
});
});