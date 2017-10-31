/**
 * 故障处理使用人确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	// 定义命名空间
	Ext.namespace('FaultOrderConfirm');   
	
	// 故障处理列表
	FaultOrderConfirm.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultOrder!queryPageList2User.action',
	    storeAutoLoad: true,
	    tbar:['refresh', '-', {text: '确认', iconCls: 'wrenchIcon', handler: function() {
		    	var data = FaultOrderConfirm.grid.selModel.getSelections();
	    		if (Ext.isEmpty(data)) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    	    var ids = [];
	    	    for (var i = 0; i < data.length; i++){
	    	        ids.push(data[ i ].get("idx"));
	    	    }
				Ext.Msg.confirm('提示', '处理所选故障工单，是否确认继续？', function(btn) {
					if ('yes' == btn) {
						$yd.request({
		        			url: ctx + '/faultOrder!confirm.action',
		        			params: {
		        				ids: Ext.encode(ids)
		        			}
		        		}, function (result) {
		        			alertSuccess();
		        			FaultOrderConfirm.grid.store.reload();
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
					this.whereList = [{sql: "idx IN (Select equipmentIdx From FaultOrder Where recordStatus = 0 And useWorkerId Is Null And state = '" + STATE_YCL + "')", compare: Condition.SQL}]
				},
				select: function() {
					FaultOrderConfirm.grid.store.load();
				},
				change: function() {
					FaultOrderConfirm.grid.store.load();
				},
				keyup: function(me, e) {
					if (e.keyCode === e.ENTER) {
						FaultOrderConfirm.grid.store.load();
					}
				}
			}
	    }, {
	    	text: '重置', iconCls: 'resetIcon', handler: function() {
	    		var tbar = this.findParentByType('toolbar');
	    		tbar.findByType('singlefieldcombo')[0].reset();
	    		FaultOrderConfirm.grid.store.load();
	    	}
	    }],
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
			dataIndex: 'submitEmp', header: '提报人'
		},{
			dataIndex: 'repairEmp', header: '修理人'
		},{
			dataIndex: 'faultOrderNo', header: '提票单号'
		},{
			dataIndex: 'faultOccurTime', header: '故障发生时间', xtype: "datecolumn", format: 'Y-m-d h:m'
		},{
			dataIndex: 'faultPlace', header: '故障发生地点及部位'
		},{
			dataIndex: 'faultPhenomenon', header: '故障现象'
		},{
			dataIndex: 'repairContent', header: '修理内容'
		}],
		toEditFn : Ext.emptyFn
	});
	
	FaultOrderConfirm.grid.store.on('beforeload', function() {
		var equipmentCode = FaultOrderConfirm.grid.getTopToolbar().find('xtype', 'singlefieldcombo')[0].getRawValue();
		var entityJson = {
			equipmentCode: equipmentCode
		};
		this.baseParams.entityJson = Ext.encode(MyJson.deleteBlankProp(entityJson));
	});
	
	// 在title部分增加待处理记录数提醒
	FaultOrderConfirm.grid.store.on('load', function(me, records, options) {
		if (0 < records.length) {
			FaultOrderConfirm.grid.ownerCt.setTitle([
            '故障处理<span class="count_tip_red">',
            me.getTotalCount(),
            '</span>'].join(''));
		} else {
			FaultOrderConfirm.grid.ownerCt.setTitle('故障处理');
		}
	});
});