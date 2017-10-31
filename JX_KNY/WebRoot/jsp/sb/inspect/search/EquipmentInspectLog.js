/**
 * 设备巡检日志 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	
	Ext.ns('EquipmentInspectLog');			// 自定义命名空间
	
	/** **************** 定义设备列表开始 **************** */
	var grid = new Ext.yunda.Grid({
		loadURL: ctx + '/equipmentPrimaryInfo!pageQuery.action',
		tbar: ['refresh'],
		viewConfig: null,
		singleSelect: true,
		fields: [{
			header: 'idx主键', dataIndex: 'idx', hidden: true
		}, {
			header: '设备编号', dataIndex: 'equipmentCode', width: 100
		}, {
			header: '设备名称', dataIndex: 'equipmentName', width: 240
		}, {
			header: '设置地点', dataIndex: 'usePlace', width: 120
		}, {
			header: '机械系数', dataIndex: 'mechanicalCoefficient', width: 60, align: 'center'
		}, {
			header: '电气系数', dataIndex: 'electricCoefficient', width: 60, align: 'center'
		}],
		toEditFn: Ext.emptyFn
	});
	grid.store.on('beforeload', function() {
		var whereList = [];
		whereList.push({
			compare: Condition.SQL,
			sql: "IDX IN (SELECT DISTINCT EQUIPMENT_IDX FROM E_INSPECT_ROUTE_DETAILS WHERE RECORD_STATUS = 0)"
		});
		this.baseParams.whereListJson = Ext.encode(whereList);
		this.baseParams.sort = 'equipmentCode';
		this.baseParams.dir = 'ASC';
	});
	grid.store.on('load', function(me, records, options) {
		if (0 < records.length) {
			grid.getSelectionModel().selectFirstRow();
		}
	});
	grid.getSelectionModel().on('rowselect', function( me, rowIndex, r ) {
		InspectPlanEquipment.equipmentIdx = r.id;
		InspectPlanEquipment.grid.store.load();
	});
	/** **************** 定义设备列表结束 **************** */
	
	new Ext.Viewport({
		layout: 'border',
		defaults: {
			region: 'center', layout: 'fit'
		},
		items: [{
			region: 'west', width: 500,
			items: grid
		}, {
			items: InspectPlanEquipment.grid
		}]
	});
	
});