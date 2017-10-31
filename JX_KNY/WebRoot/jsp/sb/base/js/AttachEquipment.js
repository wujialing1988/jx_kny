/**
 * 设备附属件
 */
Ext.onReady(function(){
	
	var id = "AttachEquipment", idx;
	Ext.ns(id);
	
	window[id].load = function(_idx){
		idx = _idx;
		grid.store.load();
	};
	
	var grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/affiliatedEquipment!pageList.action',                 //装载列表数据的请求URL
	    modalWindow:true, viewConfig:null,
	    storeAutoLoad: false,
	    tbar: ['search'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'主设备主键', dataIndex:'equipmentIdx', hidden:true, editor: {xtype: "hidden"}
		},{
			header:'设备编号', dataIndex:'equipmentNo', editor:{  maxLength:20, allowBlank: false }, hidden:true
		},{
			header:'设备名称', dataIndex:'equipmentName', editor:{  maxLength:15, allowBlank: false }
		},{
			header:'设备型号', dataIndex:'modal', editor:{  maxLength:10, allowBlank: false }
		},{
			header:'设备规格', dataIndex:'specification', editor: specification("aff_specification_id", "设备规格")
		},{
			header:'机械系数', dataIndex:'mechanicalcoefficient', editor:{ xtype:'numberfield' }, width: 60
		},{
			header:'电气系数', dataIndex:'electriccoefficient', editor:{ xtype:'numberfield' }, width: 60
		},{
			header:'数量', dataIndex:'count', editor:{ xtype:'numberfield' }, width: 60
		},{
			header:'单位', dataIndex:'unit', editor:{  maxLength:4 }, width: 60
		},{
			header:'功率', dataIndex:'power', editor:{ xtype:'numberfield' }, hidden:true
		},{
			header:'单价', dataIndex:'price', editor:{ xtype:'numberfield', fieldLabel: "单价（元）" }, hidden:true
		},{
			header:'生产厂家', dataIndex:'makeFactory', editor:{}
		},{
			header:'备注', dataIndex:'remark', editor:{ xtype:'textarea', maxLength:100 }
		}],
		toEditFn: function(){},
		searchFn: function(sp){
			this.store.sp = sp;
			this.store.load();
		}
	});
	// 表格数据加载前的参数设置
	grid.store.on('beforeload', function(){
		var sp = MyJson.clone(this.sp || {});
		sp.equipmentIdx = idx;
		this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
	SR.addContent(id, grid);
});