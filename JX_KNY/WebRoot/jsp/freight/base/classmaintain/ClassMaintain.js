/**
 * 班次维护
 */
Ext.onReady(function(){
	
Ext.namespace('ClassMaintain');                       //定义命名空间

//定义全局变量保存查询条件
ClassMaintain.searchParam = {} ;

ClassMaintain.workplaceCode = "###" ; // 站点编码
ClassMaintain.workplaceName = "###" ; // 站点名称

ClassMaintain.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/classMaintain!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/classMaintain!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/classMaintain!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:1,
    labelWidth: 100,                                     //查询表单中的标签宽度
    fieldWidth: 180,
	fields: [
     	{
		header:'工作地点标识码', dataIndex:'workplaceCode',hidden:true,width: 120,editor: {xtype:"hidden"}
	},
     	{
		header:'工作地点名称', dataIndex:'workplaceName',hidden:true,width: 120,editor: {xtype:"hidden"}
	},
     	{
		header:'班次编码', dataIndex:'classNo',width: 120,editor: {allowBlank:false}
	},
     	{
		header:'班次名称', dataIndex:'className',width: 120,editor: {allowBlank:false}
	},
     	{
		header:'排序', dataIndex:'seqNo',width: 120,editor: {
					xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:99,
			        allowBlank:false},searcher: { hidden: true }
	},{
		header:'类型', dataIndex:'vehicleType',width: 120,editor: {
			id:'vehicleType_combo',
			xtype: 'EosDictEntry_combo',
			hiddenName: 'vehicleType',
			dicttypeid:'FREIGHT_TYPE',
			displayField:'dictname',valueField:'dictid',
			hasEmpty:"false"
		},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			var result = '' ;
			if('10' == value){
				result = '货车' ;
			}else if('20' == value){
				result = '客车' ;
			}
			return result;
		}
	},
     	{
		header:'备注', dataIndex:'remark',width: 120,editor: {xtype:'textarea', maxLength:1000 },searcher: { hidden: true }
	},{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		ClassMaintain.searchParam = searchParam ;
        ClassMaintain.grid.store.load();
	},
	// 保存之前存状态
	beforeSaveFn: function(data){ 
		if(Ext.isEmpty(data.workplaceCode)){
			data.workplaceCode = ClassMaintain.workplaceCode ;
			data.workplaceName = ClassMaintain.workplaceName ;
		}
		return true; 
	}
});
	
//查询前添加过滤条件
ClassMaintain.grid.store.on('beforeload' , function(){
		var searchParam = ClassMaintain.searchParam ;
		searchParam.workplaceCode = ClassMaintain.workplaceCode;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

/**
 * 双击事件 
 */
ClassMaintain.grid.on("rowclick", function(grid, rowIndex, e){
		var records = ClassMaintain.grid.getSelectionModel().getSelections();
		ClassOrganization.classIdx = records[0].data.idx ;
		ClassOrganization.grid.getStore().reload();
});


// 添加加载结束事件
ClassMaintain.grid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0){
		ClassMaintain.grid.getSelectionModel().selectFirstRow();
		var records = ClassMaintain.grid.getSelectionModel().getSelections();
		ClassOrganization.classIdx = records[0].data.idx ;
		ClassOrganization.grid.getStore().reload();
	}else{
		ClassOrganization.classIdx = "###" ;
		ClassOrganization.grid.getStore().reload();
	}
});

ClassMaintain.grid.store.setDefaultSort("seqNo", "ASC");

/**
 * 站点列表
 */
ClassMaintain.WorkPlaceGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workPlace!pageList.action',                 //装载列表数据的请求URL
	    storeId:'workPlaceCode', singleSelect: true,
		saveFormColNum:1, fieldWidth:200,isEdit:false,
		fields: [{
			header:'标识代码', dataIndex:'workPlaceCode', editor:{ allowBlank:false, maxLength:50}
		},{
			header:'名称', dataIndex:'workPlaceName', editor:{ allowBlank:false, maxLength:100 }, width: 150
		}],
		beforeShowEditWin: function(record, rowIndex){ 
			return false ;
		}
});
	
/**
 * 站点双击事件 
 */
ClassMaintain.WorkPlaceGrid.on("rowclick", function(grid, rowIndex, e){
		var records = ClassMaintain.WorkPlaceGrid.getSelectionModel().getSelections();
		ClassMaintain.workplaceCode = records[0].data.workPlaceCode ;
		ClassMaintain.workplaceName = records[0].data.workPlaceName ;
		ClassOrganization.workplaceCode = records[0].data.workPlaceCode ;
		WhMatQuota.workplaceCode = records[0].data.workPlaceCode;// 站点标识
		WhMatQuota.workplaceName = records[0].data.workPlaceName;// 站点名称
		var activeTab = ClassMaintainDesign.tabs.getActiveTab() ;
		if("WhMatQuota_tab" == activeTab.id){
			WhMatQuota.grid.getStore().reload();
		}else if("ClassMaintainDesign_tab" == activeTab.id){
			ClassMaintain.grid.getStore().reload();
		}
});

/**
 * 站场默认选中
 */
ClassMaintain.WorkPlaceGrid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0){
		ClassMaintain.WorkPlaceGrid.getSelectionModel().selectFirstRow();
		var records = ClassMaintain.WorkPlaceGrid.getSelectionModel().getSelections();
		ClassMaintain.workplaceCode = records[0].data.workPlaceCode ;
		ClassMaintain.workplaceName = records[0].data.workPlaceName ;
		ClassOrganization.workplaceCode = records[0].data.workPlaceCode ;
		// 物料
		WhMatQuota.workplaceCode = records[0].data.workPlaceCode;// 站点标识
		WhMatQuota.workplaceName = records[0].data.workPlaceName;// 站点名称
		ClassMaintain.grid.getStore().reload();
	}else{
		ClassMaintain.workplaceCode = "###" ;
		ClassMaintain.workplaceName = "###" ;
		ClassOrganization.workplaceCode = "###";
		// 物料
		WhMatQuota.workplaceCode = "";// 站点标识
		WhMatQuota.workplaceName = "";// 站点名称
		ClassMaintain.WorkPlaceGrid.getStore().reload();
	}
});

});