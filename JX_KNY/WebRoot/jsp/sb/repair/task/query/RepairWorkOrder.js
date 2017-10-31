Ext.onReady(function() {
	Ext.ns('RepairWorkOrder');
	
	/** **************** 定义全局变量开始 **************** */
	RepairWorkOrder.searchParams = {};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
     * 查看照片，查看该检修工单关联的照片
     */
	RepairWorkOrder.showImage = function() {
    	ImageView.show(RepairWorkOrder.grid, {
			title: '设备检修照片'
		});
    }
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义全局表格开始 **************** */
	var sm, cm, store;
	sm = new Ext.grid.RowSelectionModel({singleSelect:true});
	cm = new Ext.grid.ColumnModel({
		defaults: { sortable: true },
		columns: [new Ext.grid.RowNumberer(), {
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'scopeCaseIdx', header: '范围实例主键', hidden: true
		}, {
			dataIndex: 'defineIdx', header: '作业项定义主键', hidden: true
		}, {
			dataIndex: 'sortNo', header: '序号', hidden: true
		}, {
			dataIndex: 'orderStatus', header: '工单状态', renderer: function(value, mataDate, record) {
				return 1 == value ? '<marquee direction="right" scrollamount="1">未处理</marquee>' : '已处理';
			}, width: 60, align: 'center'
		}, {
			dataIndex: 'workContent', header: '作业内容', width: 180
		}, {
			header: "照片数量", dataIndex: "imageCount", width: 60, renderer: com.yunda.Common.imageCountRenderer2JXGD
		}, {
			dataIndex: 'workerId', header: '施修人id', hidden: true
		}, {
			dataIndex: 'workerName', header: '施修人', width: 70
		}, {
			dataIndex: 'repairRecord', header: '施修记录',
			renderer: function(v, m) {
				return v;
			}
		}, {
			dataIndex: 'otherWorkerName', header: '辅修人员', width: 160, 
			renderer: function(v, m) {
				return v;
			}
		}, {
			dataIndex: 'processTime', header: '处理时间', width: 110, renderer: function(v) {
				if (!Ext.isEmpty(v)) {
					return Ext.util.Format.date(new Date(v), 'Y-m-d H:i');
				}
				return v;
			}
		}, {
			dataIndex: 'processStandard', header: '工艺标准', width: 440
		}, {
			dataIndex: 'remark', header: '备注', hidden: true
		}]
	});
	store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: true,
        remoteSort: true,
        url: ctx + '/repairWorkOrder!queryPageList.action',
        fields: ['idx', 'scopeCaseIdx', 'defineIdx', 'sortNo', 'workContent', 'processStandard', 'workerId', 'workerName', 'otherWorkerName', 'processTime', 'repairRecord', 'remark', 'orderStatus', 'imageCount']
    }); 
	grid = new Ext.grid.EditorGridPanel({
		cm: cm,
		sm: sm,
        store: store,
        remoteSort: true,
        stripeRows: true,
        clicksToEdit: 1,
    	tbar: ['-', {
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: RepairWorkOrder.showImage
		}],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store}),
        listeners: {
        	afteredit: function(e) {
        		e.grid.fireEvent('update', this, e.record);
        	},
        	update: function( me, record, operation ) {
        		var orderStatus = record.data.orderStatus;
        		$yd.request({
        			url: ctx + '/repairWorkOrder!updateFinished.action',
        			params: {
        				idx: record.id,
        				otherWorkerName: record.data.otherWorkerName,
        				repairRecord: record.data.repairRecord
        			}
        		}, function(result) {
        			var entity = result.entity;
        			if (orderStatus != entity.orderStatus) {
        				RepairScopeCase.hasChanged = true;
        				RepairScopeCase.grid.store.reload();
        			}
        		});
        	}
        }
    });
	RepairWorkOrder.grid = grid;
	/** **************** 定义全局表格结束 **************** */
	
	RepairWorkOrder.grid.getSelectionModel().on('rowselect', function( me, rowIndex, r ) {
		var record = me.getSelected();
		if (!record) {
			return;
		}
		RepairWorkOrderStuff.repairWorkOrderIdx = record.id;
		RepairWorkOrderStuff.grid.store.load();
	});
	
	RepairWorkOrder.grid.store.on('beforeload', function() {
		// Modified by hetao on 2016-12-05 修改检修作业工单点击列表头通过排序方式重新加载数据时，可能出现的数据加载错误
		if (0 >= RepairScopeCase.grid.store.getCount()) {
			return false;
		}
		this.baseParams.entityJson = Ext.encode(RepairWorkOrder.searchParams);
	});
	
	RepairWorkOrder.grid.store.on('load', function(me, records, options) {
		if (0 < records.length) {
			// 设备检修作业工单表格数据加载完成后，自动默认选择首行记录
			RepairWorkOrder.grid.getSelectionModel().selectRow(0);
		} else if (0 < RepairWorkOrderStuff.grid.store.getCount()) {
			// 如果作业工单没有数据，则清空设备检修用料表格数据
			RepairWorkOrderStuff.grid.store.removeAll();
		}
	});
});