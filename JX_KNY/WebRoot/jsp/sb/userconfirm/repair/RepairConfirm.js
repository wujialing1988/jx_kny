/**
 * 计划检修确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	// 定义命名空间
	Ext.namespace('RepairConfirm'); 
	
	// 显示检修工单详细信息
	RepairConfirm.showTaskRecord = function(grid, rowIndex, e) {
		var sm = RepairConfirm.grid.getSelectionModel();
		if (0 >= sm.getCount()) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		var record = sm.getSelections()[0];
		RepairRecord.record = record;
		RepairRecord.rowIndex = rowIndex;
		RepairRecord.win.show();
	}
	
	// 检修工单列表
	RepairConfirm.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/repairTaskList!taskListForUser.action',
	    storeAutoLoad: true,
	    tbar:['refresh', '-', {text: '确认', iconCls: 'wrenchIcon', handler: function() {
	    		var data = RepairConfirm.grid.selModel.getSelections();
	    		if (Ext.isEmpty(data)) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    	    var ids = [];
	    	    for (var i = 0; i < data.length; i++){
	    	        ids.push(data[ i ].get("idx"));
	    	    }
				Ext.Msg.confirm('提示', '确认所选的检修工单，是否确认继续？', function(btn) {
					if ('yes' == btn) {
						$yd.request({
		        			url: ctx + '/repairTaskList!updateUser.action',
		        			params: {
		        				ids: Ext.encode(ids)
		        			}
		        		}, function (result) {
		        			alertSuccess();
		        			RepairConfirm.grid.store.reload();
		        		});
					}
				});
			}
		}, '-', {
			xtype: 'singlefieldcombo',
			entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo', 
			xfield: 'equipmentCode',
			enableKeyEvents: true,
			width: 140, emptyText: '输入设备编码查询...',
			listeners: {
				beforequery: function() {
					this.whereList = [{sql: "idx IN (Select L.equipmentIdx From RepairTaskList L, RepairPlanMonth M Where M.idx = L.planMonthIdx And L.recordStatus = 0)", compare: Condition.SQL}]
				},
				select: function() {
					RepairConfirm.grid.store.load();
				},
				change: function() {
					RepairConfirm.grid.store.load();
				},
				keyup: function(me, e) {
					if (e.keyCode === e.ENTER) {
						RepairConfirm.grid.store.load();
					}
				}
			}
		}, {
	    	text: '重置', iconCls: 'resetIcon', handler: function() {
	    		var tbar = this.findParentByType('toolbar');
	    		tbar.findByType('singlefieldcombo')[0].reset();
	    		RepairConfirm.grid.store.load();
	    	}
	    }, '->', '<span style="color:gray;">&nbsp;双击查看设备检修记录详情！</span>'],
	    fields: [{
	    	dataIndex : 'idx', header : 'idx主键', hidden : true
		},{
			dataIndex: 'equipmentCode', header: '设备编码'
		},{
			dataIndex: 'equipmentName', header: '设备名称'
		},{
			dataIndex: 'specification', header: '规格'
		},{
			dataIndex: 'model', header: '型号'
		},{
			dataIndex: 'mechanicalCoefficient', header: '机械系数'
		},{
			dataIndex: 'electricCoefficient', header: '电气系数'
		},{
			dataIndex: 'usePerson', header: '使用人'
		},{
			dataIndex: 'gzSignMac', header: '机械工长签名'
		},{
			dataIndex: 'gzSignElc', header: '电气工长签名'
		},{
			dataIndex: 'realBeginTime', header: '开工时间', xtype: "datecolumn", format: 'Y-m-d H:i'
		},{
			dataIndex: 'realEndTime', header: '完工时间', xtype: "datecolumn", format: 'Y-m-d H:i'
		}],
		// 双击显示设备工单机械、电气详细信息
		toEditFn : RepairConfirm.showTaskRecord
	});
	
	RepairConfirm.grid.store.on('beforeload', function() {
		var equipmentCode = RepairConfirm.grid.getTopToolbar().find('xtype', 'singlefieldcombo')[0].getRawValue();
		var entityJson = {
			equipmentCode: equipmentCode
		};
		this.baseParams.entityJson = Ext.encode(MyJson.deleteBlankProp(entityJson));
	});
	
	// 在title部分增加待处理记录数提醒
	RepairConfirm.grid.store.on('load', function(me, records, options) {
		if (0 < records.length) {
			RepairConfirm.grid.ownerCt.setTitle([
            '计划检修<span class="count_tip_red">',
            me.getTotalCount(),
            '</span>'].join(''));
		} else {
			RepairConfirm.grid.ownerCt.setTitle('计划检修');
		}
	});
});