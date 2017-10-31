/**
 * 巡检计划使用人确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	// 定义命名空间
	Ext.namespace('InspectPlanConfirm'); 
	
	/** **************** 定义全局函数开始 **************** */
	// 计划详情展示
	InspectPlanConfirm.showInspectDetail = function(grid, rowIndex, e) {
		InspectPlanRecord.rowIndex = rowIndex;
		var sm = InspectPlanConfirm.grid.getSelectionModel();
		if (0 >= sm.getCount()) {
			MyExt.Msg.alert('尚未选择任何记录！');
		}
		InspectPlanRecord.record = sm.selections.items[0];
		InspectPlanRecord.win.show();
	}
	/** **************** 定义全局函数结束 **************** */
	
	// 巡检计划列表
	InspectPlanConfirm.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/inspectPlan!queryPageList2User.action',
	    storeAutoLoad: true,
	    singleSelect: true,
	    tbar: ['refresh', '->', '<span style="color:gray;">&nbsp;双击查看设备巡检计划详情！</span>'],
	    fields: [{
	    	dataIndex : 'idx', header : 'idx主键', hidden : true
		},{
			dataIndex: 'routeName', header: '巡检计划'
		},{
			dataIndex: 'partrolWorker', header: '巡检人'
		},{
			dataIndex: 'periodType', header: '巡检周期'
		},{
			dataIndex: 'planStartDate', header: '计划开始日期', xtype: "datecolumn", format: 'Y-m-d'
		},{
			dataIndex: 'planEndDate', header: '计划结束日期', xtype: "datecolumn", format: 'Y-m-d'
		},{
			dataIndex: 'yxjCount', header: '已巡检完成的设备数', renderer: function(v, m, r, rowIndex) {
				m.attr =  'style="font-weight:bold"';
				if (0 != v) {
					return '<div class="count_tip_green"><p>' + v + '</p></div>';
				}
			}
		}],
		toEditFn : 	InspectPlanConfirm.showInspectDetail
	});
	
	// 在title部分增加待处理记录数提醒
	InspectPlanConfirm.grid.store.on('load', function(me, records, options) {
		if (0 < records.length) {
			InspectPlanConfirm.grid.ownerCt.setTitle([
            '设备巡检<span class="count_tip_red">',
            me.getTotalCount(),
            '</span>'].join(''));
		} else {
			InspectPlanConfirm.grid.ownerCt.setTitle('设备巡检');
		}
	});
});