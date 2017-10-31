/**
 * 编组信息维护
 */
Ext.onReady(function(){
	Ext.namespace('marshalling');                       //定义命名空间
	
	//定义全局变量保存查询条件
	marshalling.searchParam = {} ;
	marshalling.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/marshalling!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/marshalling!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/marshalling!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true, saveFormColNum:1,
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
		fields: [{
			header:'编组编号', dataIndex:'marshallingCode',width: 40,editor: {xtype:"hidden"}
		},{
			header:'编组名称', dataIndex:'marshallingName', width: 60 ,editor: {allowBlank:false}, searcher: { hidden: true }
		},{
			header:'车辆总数', dataIndex:'trainCount', width: 30, editor: {allowBlank:false, maxLength:20 },searcher: { hidden: true }
		},{
			header:'备注', dataIndex:'remark',width: 120,editor:{ xtype:'textarea', maxLength:1000 },searcher: { hidden: true }
		},{
			header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		}],
		searchFn: function(searchParam){ 
			marshalling.searchParam = searchParam ;
	        marshalling.grid.store.load();
		}
	});
	//查询前添加过滤条件
	marshalling.grid.store.on('beforeload' , function(){
		var searchParam = marshalling.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
		// 添加加载结束事件
	marshalling.grid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			marshalling.grid.getSelectionModel().selectFirstRow();
	       	var sm = marshalling.grid.getSelectionModel();
	       	var records = sm.getSelections();
	       	MarshallingTrain.setNetwork(records);
			MarshallingTrain.grid.getStore().reload();	
		}else{
			MarshallingTrain.marshallingCode = "###" ; // 编组编号
			MarshallingTrain.marshallingStatus = "###" ; 
			MarshallingTrain.grid.getStore().reload();
		}
	});
	marshalling.grid.on("rowclick", function(grid, rowIndex, e){
       	var sm = marshalling.grid.getSelectionModel();
   	 	var records = sm.getSelections();
       	MarshallingTrain.setNetwork(records);
		MarshallingTrain.grid.getStore().reload();	
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({
				layout:'fit',
				items:[{
				layout: 'border',
				items: [{
					 region: 'west', layout: 'fit',  title: '编组基础维护',  bodyBorder: false, split: true,width: 500,minSize: 400, maxSize: 800, 
			     	 collapsible : true,   items:[marshalling.grid]
		    	},{ 		
			     	 region: 'center', bodyBorder: false,title: '编组车辆',
		       		 layout: 'fit', items : [MarshallingTrain.panel]
				}]
			}] 
	});


});