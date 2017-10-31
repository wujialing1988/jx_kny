/**
 * 配件信息 通过车查询配件检修记录及周转详情
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpDetailInfo');                       //定义命名空间
	PartsRdpDetailInfo.rdpIDX = "";
/** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修单信息显示模板
	 */
	PartsRdpDetailInfo.getPartsRdpInfoTpl = function() {
		if (Ext.isEmpty(PartsRdpDetailInfo.tpl)) {
			PartsRdpDetailInfo.tpl = new Ext.XTemplate(
			'<table class="pjjx-show-info-table">',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="10%">识别码：</td><td width="10%">{identificationCode}</td>',
	                '<td class="pjjx-show-info-table-label" width="10%">配件编号：</td><td width="10%">{partsNo}</td>',                   
	                '<td class="pjjx-show-info-table-label" width="10%">规格型号：</td><td width="10%">{specificationModel}</td>',	
	                '<td class="pjjx-show-info-table-label" width="10%">上车车型：</td><td width="10%">{aboardTrainType}{aboardTrainNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="10%">上车修程：</td><td width="10%">{aboardRepairClass}{aboardRepairTime}</td>',
	            '</tr>',
				'<tr>',
				 	'<td class="pjjx-show-info-table-label" width="10%">配件名称：</td><td width="20%" colspan="3">{partsName}</td>',
	                '<td class="pjjx-show-info-table-label" width="10%">生产厂家：</td><td width="10%">{madeFactoryName}</td>',
	                '<td class="pjjx-show-info-table-label" width="10%">出厂日期：</td><td width="10%">{factoryDate}</td>',
	            '</tr>',
			'</table>');
		}
	    return PartsRdpDetailInfo.tpl;
	}
	
	/**
	 * 根据配件检修作业任务实体初始化详情查看页面
	 * @param data 配件检修作业任务实体
	 */
	PartsRdpDetailInfo.initFn = function(data) {
		var values = {};
		// 格式化“开始时间”和“结束时间”
		if (!Ext.isEmpty(data.factoryDate)) {
			if (Ext.isString(data.factoryDate)) {
				values.factoryDate = data.factoryDate;
			} else {
				values.factoryDate = new Date(data.factoryDate).format('Y-m-d H:i');
			}
		}
//		if (!Ext.isEmpty(data.realEndTime)) {
//			if (Ext.isString(data.realStartTime)) {
//				values.realEndTime = data.realEndTime;
//			} else {
//				values.realEndTime = new Date(data.realEndTime).format('Y-m-d H:i');
//			}
//		}
		Ext.applyIf(values, data);
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpDetailInfo.getPartsRdpInfoTpl();
		tpl.overwrite(Ext.get('id_parts_rdp_detail_base'), values);
		
		// -------------->> 加载“配件检修作业任务”树
		var text = [data.partsName, "（", data.specificationModel, "_", data.partsNo, "）"];

		// -------------->> 加载“拆卸安装配件”列表
		var parentPartsAccountIDX = data.idx;

		
		// -------------->> 加载“检修记录单”列表
		PartsRdpRecord.rdpIDX = PartsRdpDetailInfo.rdpIDX;
		PartsRdpRecord.grid.store.load();
		PartsRdpRecord.specificationModel = data.specificationModel;

		// -------------->> 加载“配件周转记录详情”列表
		PartsAcountTurnOver.partsAccountIdx = data.idx;
		PartsAcountTurnOver.grid.store.load();
	}
	/** **************** 定义全局函数结束 **************** */
	
	PartsRdpDetailInfo.win = new Ext.Window({
		title: '作业计划单查看',
		maximized: true,
		layout: 'border',
		closeAction: 'hide',
		items: [{
				region: 'north', height: 118, layout: 'fit', frame: true, collapsible: true,
				items: {
					xtype: 'fieldset', title: '配件检修兑现单信息',
					html: '<div id="id_parts_rdp_detail_base"></div>'
				}
			}, {
				region: 'center', layout: 'fit',
				items: {
					xtype: 'tabpanel', activeItem: 0, border: false,
					items: [{
						title: '检修记录单', layout: 'fit',
						items: [PartsRdpRecord.grid]				
					},{
						title: '配件周转记录', layout: 'fit',
						items: [PartsAcountTurnOver.grid]
					}]
				}
			}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],	
		listeners: {
			show: function() {
				// 配件检修作业任务实体
				var data = PartsRdpDetailInfo.record;	
				// 初始化配件检修作业任务查看详情页面			
				PartsRdpDetailInfo.initFn(data);
			}				
		}
	});
});
