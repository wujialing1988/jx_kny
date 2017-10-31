/**
 * 配件检修工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsWorkStation');                       //定义命名空间
PartsWorkStation.searchParam = {};
PartsWorkStation.repairLineIdx = "" ;//流水线主键
PartsWorkStation.repairLineName = "" ;//流水线名称
PartsWorkStation.workStationName = "" ;//工位名称
PartsWorkStation.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/partsWorkStation!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsWorkStation!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsWorkStation!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar : ['工位名称',{	            
            xtype:"textfield",								                
            name : "workStationName",
            id:"workStationNameId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				PartsWorkStation.workStationName = Ext.getCmp("workStationNameId").getValue();				
				PartsWorkStation.grid.getStore().load();
				
			},
			title : "按输入框条件查询",
			scope : this
		},'add','delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位编码', dataIndex:'workStationCode', width:80, editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'工位名称', dataIndex:'workStationName',width:100,  editor:{ allowBlank:false, maxLength:100 }
	},{
		header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'流水线名称', dataIndex:'repairLineName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}],
	beforeAddButtonFn: function(){
		if(null == PartsWorkStation.repairLineIdx || "" == PartsWorkStation.repairLineIdx){
			MyExt.Msg.alert("请选择流水线记录");
			return false;
		}
		return true;   	
    },
	beforeSaveFn: function(rowEditor, changes, record, rowIndex){
		record.data.repairLineIdx = PartsWorkStation.repairLineIdx ;
		record.data.repairLineName = PartsWorkStation.repairLineName ;
        return true;
    }
});
PartsWorkStation.grid.store.on('beforeload',function(){
	     var searchParam = PartsWorkStation.searchParam;
	     searchParam.repairLineIdx = PartsWorkStation.repairLineIdx ;
	     searchParam.repairLineName = PartsWorkStation.repairLineName ;
	     searchParam.workStationName = PartsWorkStation.workStationName ;
	     searchParam = MyJson.deleteBlankProp(searchParam);
	     this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
});