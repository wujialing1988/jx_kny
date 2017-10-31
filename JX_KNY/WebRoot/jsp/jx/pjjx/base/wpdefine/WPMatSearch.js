

Ext.onReady(function() {
	Ext.namespace('WPMatSearch');
	
	/** ************* 定义全局变量开始 ************* */
	WPMatSearch.searchParam = {};
	WPMatSearch.wPIDX = "###";										// 作业流程主键
	WPMatSearch.parentWPNodeIDX = "###";									// 上级作业节点主键
	WPMatSearch.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用工艺卡表格开始 ************* */
	WPMatSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wPMat!pageQuery.action',           //装载列表数据的请求URL
	    tbar:['物料描述',{	            
	            xtype:"textfield",								                
		        width: 180,
	            id:"matDesc"
			},{
				text : "查询",
				iconCls : "searchIcon",
				handler : function(){
					//var searchText = Ext.getCmp("matDesc").getValue();
					//WPMatSearch.searchParam.matDesc = searchText ;
					//WPMatSearch.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(WPMatSearch.searchParam);			
					WPMatSearch.grid.getStore().load();
					
				},
				title : "按输入框条件查询",
				scope : this
			},{
				text : "重置",
				iconCls : "resetIcon",
				handler : function(){
					//WPMatSearch.searchParam.matDesc = "";
					//WPMatSearch.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(WPMatSearch.searchParam);
					Ext.getCmp("matDesc").setValue("");
					WPMatSearch.grid.getStore().load();					
				},
				scope : this
			}],	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 10
		},{
			header:'流程节点', dataIndex:'wpNodeName', width: 20
		},{
			header:'物料编码', dataIndex:'matCode', width: 20
		},{
			header:'物料描述', dataIndex:'matDesc', width: 30
		},{
			header:'计数单位', dataIndex:'unit', width: 10
		},{
			header:'数量', dataIndex:'qty', width: 20
		}],
		storeAutoLoad: false,
		afterShowSaveWin: function() {
	    	WPMatSearch.grid.store.load();
	    },
		
		searchFn: function(searchParam){
			WPMatSearch.searchParam = searchParam ;
			this.store.baseParams.entityJson = Ext.util.JSON.encode(WPMatSearch.searchParam);
        	this.store.load();
    	}
	});
	WPMatSearch.grid.un('rowdblclick', WPMatSearch.grid.toEditFn, WPMatSearch.grid);
	WPMatSearch.grid.store.setDefaultSort("wpNodeIDX", "ASC");
		
	//查询前添加过滤条件
	WPMatSearch.grid.store.on('beforeload' , function(){
		var searchText = Ext.getCmp("matDesc").getValue();
		var whereList = [];
		if(!Ext.isEmpty(searchText)){
			whereList.push({propName:"matDesc", compare:Condition.EQ, propValue:searchText});
		}
		whereList.push({propName:"wpIDX", compare:Condition.EQ, propValue:WPMat.wPIDX});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});	
});