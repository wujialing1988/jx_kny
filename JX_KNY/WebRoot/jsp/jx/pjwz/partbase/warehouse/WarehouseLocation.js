/**
 * 库位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("WarehouseLocation");
//定义库房的全局变量，用于新增库位、库管员时录入值
WarehouseLocation.wareHouseIDX = "" ;
WarehouseLocation.wareHouseID = "" ;
WarehouseLocation.wareHouseName = "" ;
WarehouseLocation.searchParam = {};
//保存前触发函数
WarehouseLocation.beforeSaveFn = function(rowEditor, changes, record, rowIndex){
    record.data.wareHouseIDX = WarehouseLocation.wareHouseIDX;
    record.data.warehouseID = WarehouseLocation.wareHouseID;
    record.data.warehouseName = WarehouseLocation.wareHouseName;
    record.data.locationID = record.data.locationID.trim();
    record.data.locationName = record.data.locationName.trim();
    return true;
}
WarehouseLocation.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/warehouseLocation!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/warehouseLocation!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/warehouseLocation!logicDelete.action',            //删除数据的请求URL
    beforeSaveFn:WarehouseLocation.beforeSaveFn,
    labelWidth:WarehouseLocation.labelWidth,
    storeAutoLoad:false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'库位编码', dataIndex:'locationID',width:100, editor:{id:'locationID',validator:function(v){if(v.trim()=='')return "不能为空!" ;if(v.length>50)return "该输入项的最大长度为50!" ; },maxLength:30 }
	},{
		header:'库位名称', dataIndex:'locationName',width:100, editor:{ validator:function(v){if(v.trim()=='')return "不能为空!" ;if(v.length>50)return "该输入项的最大长度为50!" ; },  maxLength:50 }
	},{
		header:'状态', dataIndex:'status',
		editor: {
			id:"status_s",
        	xtype: 'combo',
            fieldLabel: '状态',
            hiddenName:'status',
            store:new Ext.data.SimpleStore({
			    fields: ['v'],
				data : [[STATUS_NO],[STATUS_YES]]
			}),
			valueField:'v',
			displayField:'v',
			mode:'local',
			value: STATUS_NO,
			editable: false,
			allowBlank: false
		} 
	}],
	defaultData: {status: STATUS_NO ,idx: ''},
	tbar: [ {
	        xtype:"label", text:"  库位名称： " 
	    },{
	        id:"locationName",xtype: "textfield" ,maxLength: 100
	    },{
	    	text: "搜索", iconCls:"searchIcon", handler: function(){
	    		var searchParam = WarehouseLocation.searchParam;
				searchParam.locationName = Ext.getCmp("locationName").getValue();
				WarehouseLocation.grid.store.load();
	    	}
	    },'add','delete','refresh']
});
WarehouseLocation.grid.store.on('beforeload',function(){
	var searchParam = WarehouseLocation.searchParam;
	searchParam.wareHouseIDX = WarehouseLocation.wareHouseIDX;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});