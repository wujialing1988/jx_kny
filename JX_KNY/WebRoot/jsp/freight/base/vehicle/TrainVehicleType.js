/**
 * Jcgy_train_type 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
Ext.namespace('TrainType');                       //定义命名空间

//定义全局变量保存查询条件
TrainType.searchParam = {} ;
TrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainVehicleType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainVehicleType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainVehicleType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:1,
    labelWidth: 125,                                     //查询表单中的标签宽度
    fieldWidth: 200,
	fields: [{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	},{
		header:i18n.TrainVehicleType.typeCode, dataIndex:'typeCode',editor:{ allowBlank:false ,maxLength:20  }
	},{
		header:i18n.TrainVehicleType.typeName, dataIndex:'typeName', editor:{allowBlank:false , maxLength:50  }
	},{
		header:i18n.TrainVehicleType.shortName, dataIndex:'shortName', editor:{ maxLength:20  },searcher: { hidden: true }
	},{
   		header:i18n.TrainVehicleType.vehicleKindCode, dataIndex:'vehicleKindCode', hidden:true, editor: { xtype:'hidden',id:'vehicleKindCode' }
	},{
		header:i18n.TrainVehicleType.vehicleKindName, dataIndex:'vehicleKindName',editor:{
				id:'vehicleKind_combo',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'vehicleKindName',
				dicttypeid:vehicleKindCombo,
				displayField:'dictname',valueField:'dictname',
				allowBlank:false,
				hasEmpty:"false",
				returnField: [{widgetId:"vehicleKindCode",propertyName:"dictid"}]
	        }, searcher: {anchor:'98%'}
	},{
		header:i18n.TrainVehicleType.description, dataIndex:'description', editor:{ maxLength:20,xtype:'textarea' },searcher: { hidden: true }
	},{
		header:i18n.TrainVehicleType.vehicleType, dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
	}],
	searchFn: function(searchParam){ 
		TrainType.searchParam = searchParam ;
        TrainType.grid.store.load();
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainType.grid });
	
//查询前添加过滤条件
TrainType.grid.store.on('beforeload' , function(){
		TrainType.searchParam.vehicleType = vehicleType ;
		var searchParam = TrainType.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});