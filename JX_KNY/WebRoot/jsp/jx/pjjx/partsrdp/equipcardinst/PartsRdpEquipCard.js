/**
 * 机务设备工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpEquipCard');                       //定义命名空间
PartsRdpEquipCard.idx = '-123345'; //设备工单实例主键
PartsRdpEquipCard.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsRdpEquipCard!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum: 3,	singleSelect: true, fieldWidth: 220,
    viewConfig: null,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'设备工单定义主键', dataIndex:'equipCardIDX', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'配件编号', dataIndex:'partsNo',width: 130, editor:{  maxLength:30 }
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:30 }
	},{
		header:'规格型号', dataIndex:'specificationModel', width: 180,editor:{  maxLength:30 }
	},{
		header:'工单编号', dataIndex:'equipCardNo',width: 130, editor:{  maxLength:30 }
	},{
		header:'工单描述', dataIndex:'equipCardDesc', width: 200, editor:{  maxLength:500 }
	},{
		header:'设备分类编码', dataIndex:'deviceTypeCode',width: 150, editor:{  maxLength:50 }
	},{
		header:'设备分类名称', dataIndex:'deviceTypeName',width: 150, editor:{  maxLength:100 }
	},{
		header:'设备编码', dataIndex:'deviceInfoCode', editor:{  maxLength:100 }
	},{
		header:'顺序号', dataIndex:'seqNo', hidden:true, editor:{ xtype:'hidden', maxLength:3 }
	},{
		header:'作业人', dataIndex:'workEmpID', hidden:true,  editor:{  xtype:'hidden',maxLength:100 }
	},{
		header:'作业人', dataIndex:'workEmpName', width: 100, editor:{  maxLength:250 }
	},{
		header:'作业开始时间', dataIndex:'workStartTime', xtype:'datecolumn',format:'Y-m-d H:i',width: 120, editor:{ xtype:'my97date' },searcher:{disabled:true}
	},{
		header:'作业结束时间', dataIndex:'workEndTime', xtype:'datecolumn',format:'Y-m-d H:i',width: 120, editor:{ xtype:'my97date' },searcher:{disabled:true}
	},{
		header:'数据生成时间', dataIndex:'dataGenTime',hidden:true, xtype:'datecolumn',format:'Y-m-d H:i',width: 120, editor:{ xtype:'my97date' },searcher:{disabled:true}
	},{
		header:'作业结果', dataIndex:'workResult',width: 130, editor:{  maxLength:50 },searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks',width: 180, editor:{ xtype:'textarea', maxLength:500 },searcher:{disabled:true}
	},{
		header:'状态', dataIndex:'status', editor:{ xtype:'hidden', maxLength:20 },
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			if (value == STATUS_WCL) return "未处理";				
			if (value == STATUS_YCL) return "已处理";				
			return value ;
		}
	}],
	afterShowEditWin: function(record, rowIndex){
		this.saveWin.setTitle("查看设备工单信息");
		PartsRdpEquipCard.grid.disableAllColumns();
		PartsRdpEquipCard.idx = record.get('idx');
		PartsRdpEquipDI.grid.store.load();
	}
});
//覆盖创建的窗口方法
PartsRdpEquipCard.grid.createSaveWin = function(){
    if(PartsRdpEquipCard.grid.saveForm == null) PartsRdpEquipCard.grid.createSaveForm();
	PartsRdpEquipCard.grid.saveWin = new Ext.Window({
		title: "查看设备工单信息", maximizable:false,maximized:true, layout: "fit", 
		closeAction: "hide", modal: true, buttonAlign: "center",
		items: {
			xtype: "panel", layout: "border",
			items:[{
	            region: 'north', layout: "fit",frame:true, height: 220, split :true,  bodyBorder: false, items:[PartsRdpEquipCard.grid.saveForm]
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ PartsRdpEquipDI.grid ]
	        }]
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){PartsRdpEquipCard.grid.saveWin.hide();}
		}]
	});
}
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsRdpEquipCard.grid });
});