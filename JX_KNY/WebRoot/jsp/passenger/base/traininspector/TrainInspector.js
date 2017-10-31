/**
 * 人员基本信息维护
 */
Ext.onReady(function(){
	Ext.namespace('TrainInspector');                       //定义命名空间
	
	//定义全局变量保存查询条件
	TrainInspector.searchParam = {} ;
	TrainInspector.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainInspector!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/trainInspector!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/trainInspector!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
		fields: [{
			header:'人员名称', dataIndex:'empname', width: 60 ,
			editor: {}, searcher: { hidden: true }
		},{
			header:'人员代码', dataIndex:'empcode', width: 60, editor: {id:'empcode', name: 'empcode', xtype:"hidden" }
		},{
			header:'班组', dataIndex:'orgname', width: 60, editor: {id:'orgname', name: 'orgname', xtype:"hidden" }
		},{
			header:'人员id', dataIndex:'empid', hidden:true, editor: {id:'empid', xtype:"hidden" },searcher: { hidden: true }
		},{
			header:'orgid', dataIndex:'orgid',hidden:true ,editor: {id:'orgid',  xtype:"hidden" },searcher: { hidden: true }
		},{
			header:'主键IDx', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		}],
		addButtonFn: function(){
			if(this.beforeAddButtonFn() == false)   return;
			EmployeeSelect.selectWin.show();
			EmployeeSelect.OrgTree.getRootNode().expand();
			EmployeeSelect.grid.store.load();
		},
		searchFn: function(searchParam){ 
			TrainInspector.searchParam = searchParam ;
	        TrainInspector.grid.store.load();
		}
	});
	//移除监听
	TrainInspector.grid.un('rowdblclick', TrainInspector.grid.toEditFn, TrainInspector.grid);
	//查询前添加过滤条件
	TrainInspector.grid.store.on('beforeload' , function(){
		var searchParam = TrainInspector.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});

	
	//页面自适应布局
	var viewport = new Ext.Viewport({
			layout:'fit',
			items:[{
				 region: 'west', layout: 'fit',  title: '编组基础维护',  bodyBorder: false, split: true,width: 600,minSize: 400, maxSize: 800, 
		     	 collapsible : true,   items:[TrainInspector.grid]
			}] 
	});


});