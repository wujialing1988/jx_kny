/**
 * 设备信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('DeviceInfo');                       //定义命名空间
DeviceInfo.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/deviceInfo!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/deviceInfo!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/deviceInfo!delete.action',            //删除数据的请求URL
    tbar: ['add','delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'设备编码', dataIndex:'deviceInfoCode', editor:{  allowBlank: false,maxLength:50 }
	},{
		header:'设备名称', dataIndex:'deviceInfoName', editor:{  allowBlank: false,maxLength:50 }
	},{
		header:'设备分类编码', dataIndex:'deviceTypeCode',hidden:true, editor:{id:"deviceTypeCode"}
	},{
		header:'设备分类名称', dataIndex:'deviceTypeName', 
			editor:{ id:"location_comb", editable:true,selectOnFocus:false,forceSelection:false,typeAhead:false,
    	    		 xtype: 'Base_combo',hiddenName: "deviceTypeName",
	 				 entity:"com.yunda.jxpz.equipinfo.entity.DeviceType",displayField:"deviceTypeName",valueField:"deviceTypeName",fields:["deviceTypeCode","deviceTypeName"],			 
	 				 returnField:[{widgetId:"deviceTypeCode",propertyName:"deviceTypeCode"}], 
	 				 allowBlank: false
//	 				 listeners:{
//	 				 	'collapse' : function(record, index){
//	 				 		var record_v = OldPartsWHDetail.store.getAt(OldPartsWHDetail.rowIndex);
// 				 			var value = document.getElementById("location_comb").value;
// 				 			record_v.data.whLocationName = value;
// 				 			record_v.data.whLocationIDX = Ext.getCmp("whLocationIDX_id").getValue();
//							OldPartsWHDetail.grid.getView().refresh();
//	 				 	}
//	 				 }
	 				 }
	},{
		header:'设备描述', dataIndex:'deviceInfoDesc', editor:{  maxLength:500 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:DeviceInfo.grid });
});