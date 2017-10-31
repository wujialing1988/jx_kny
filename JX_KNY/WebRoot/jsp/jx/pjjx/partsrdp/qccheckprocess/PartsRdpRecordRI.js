/**
 * 配件检修检测项实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordRI');                       //定义命名空间
	
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpRecordRI.labelWidth = 100;
	PartsRdpRecordRI.fieldWidth = 100;
	PartsRdpRecordRI.rdpRecordCardIDX = "###";				// 记录卡实例主键
	PartsRdpRecordRI.searchParams = {};						// 查询实体
	PartsRdpRecordRI.rowIndex = -1;							// 记录当前正在被操作的【检修检测项】的索引值
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数结束 ************** */
	
	// 【检修检测项】表格选中一行记录后的初始化操作
	PartsRdpRecordRI.initFn = function(rowIndex) {
		var record = PartsRdpRecordRI.grid.store.getAt(rowIndex);
		// 右下角表单数据的初始化
		PartsRdpRecordRI.form.getForm().loadRecord(record);
		
		// 重新加载【检测项】表格
		PartsRdpRecordDI.rdpRecordRIIDX = record.get('idx');
		PartsRdpRecordDI.grid.store.load();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义检修检测项表格开始 ************** */
	PartsRdpRecordRI.grid = new Ext.yunda.Grid({
		border: false,
	    loadURL: ctx + '/partsRdpRecordRI!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpRecordRI!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpRecordRI!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true,
	    tbar:null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'记录卡实例主键', dataIndex:'rdpRecordCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检修检测项主键', dataIndex:'recordRIIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检修检测项编号', dataIndex:'repairItemNo', hidden:true, editor:{  maxLength:30 }
		},{
			header:'顺序号', dataIndex:'seqNo', width: 20, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'检修检测项名称', dataIndex:'repairItemName', width:35, editor:{  maxLength:50 }
		},{
			header:'技术要求', dataIndex:'repairStandard', editor:{  maxLength:500 }
		},{
			header:'处理情况', dataIndex:'handleResult', editor:{  maxLength:100 }
		},{
			header:'检测结果', dataIndex:'repairResult', hidden:true, editor:{  maxLength:30 }
		},{
			header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:500 }
		},{
			header:'状态', dataIndex:'status', hidden:true, editor:{  maxLength:20 }
		}],
		listeners: {
			rowclick: function(grid, rowIndex, e) {
				PartsRdpRecordRI.rowIndex = rowIndex;
				// 初始化页面数据
				PartsRdpRecordRI.initFn(PartsRdpRecordRI.rowIndex);
			}
		}
	});
	// 取消表格默认的行双击进行编辑的事件监听
	PartsRdpRecordRI.grid.un('rowdblclick', PartsRdpRecordRI.grid.toEditFn, PartsRdpRecordRI.grid);
	// 默认按顺序号正序排序
	PartsRdpRecordRI.grid.store.setDefaultSort('seqNo', 'ASC');
	// 列表数据容器加载时的过滤条件设置
	PartsRdpRecordRI.grid.store.on('beforeload', function() {
		var searchParams = PartsRdpRecordRI.searchParams;
		searchParams.rdpRecordCardIDX = PartsRdpRecordRI.rdpRecordCardIDX;							// 记录卡实例主键
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 列表数据容器加载后的函数事件监听
	PartsRdpRecordRI.grid.store.on('load', function() {
		var count = PartsRdpRecordRI.grid.store.getCount();
		if (count <= 0) {
			return;
		}
		var sm = PartsRdpRecordRI.grid.getSelectionModel();
		if (PartsRdpRecordRI.rowIndex < 0 || PartsRdpRecordRI.rowIndex >= count) {
			PartsRdpRecordRI.rowIndex =  0;
		}
		sm.selectRow(PartsRdpRecordRI.rowIndex);
        PartsRdpRecordRI.initFn(PartsRdpRecordRI.rowIndex);
	});
	/** ************** 定义检修检测项表格结束 ************** */
	
	/** ************** 定义检修检测项表单开始 ************** */
	PartsRdpRecordRI.form = new Ext.form.FormPanel({
		labelWidth:PartsRdpRecordRI.labelWidth,
		labelAlign:"left",
		padding:"0 10px",
		defaultType:"textfield",
		defaults:{xtype:'textfield', style: 'border:none; background:none;', readOnly: true, anchor:"100%"},
		items:[{
			fieldLabel:"检修检测项名称",
			name:'repairItemName'
		}, {
			fieldLabel:"技术要求",
			name:'repairStandard'
		}, {
			fieldLabel:"处理情况",
			name:'handleResult'
		}, {
	    	fieldLabel: '检修检测结果',
	    	name:'repairResult'
	    }, {
			xtype:"textarea",
			name:"remarks",
			fieldLabel:"备注", height: 53
		}, {
			xtype:'hidden', name:'idx', fieldLabel: 'idx主键'
		}, {
			xtype:'hidden', name:'status', fieldLabel: '状态'
		}]

	});
	/** ************** 定义检修检测项表单结束 ************** */
	
});