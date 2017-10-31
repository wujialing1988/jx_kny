/**
 * 配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpDetail');                       //定义命名空间

/** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修单信息显示模板
	 */
	PartsRdpDetail.getPartsRdpInfoTpl = function() {
		if (Ext.isEmpty(PartsRdpDetail.tpl)) {
			PartsRdpDetail.tpl = new Ext.XTemplate(
			'<table class="pjjx-show-info-table">',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',	        
	                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
	                  '<td class="pjjx-show-info-table-label" width="12%">检修班组：</td><td width="13%">{repairOrgName}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车车型：</td><td width="13%">{unloadTrainType}{unloadTrainNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车修程：</td><td width="13%">{unloadRepairClass}{unloadRepairTime}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">开始时间：</td><td width="13%" >{realStartTime}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">结束时间：</td><td width="13%">{realEndTime}</td>',
	            '</tr>',
				'<tr>',
	              
                    '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%" colspan="3">{partsName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">检修需求单：</td><td width="13%" colspan="3">{wpDesc}</td>',
	            '</tr>',
			'</table>');
		}
	    return PartsRdpDetail.tpl;
	}
	
	/**
	 * 根据配件检修作业任务实体初始化详情查看页面
	 * @param data 配件检修作业任务实体
	 */
	PartsRdpDetail.initFn = function(data) {
		var values = {};
		// 格式化“开始时间”和“结束时间”
		if (!Ext.isEmpty(data.realStartTime)) {
			if (Ext.isString(data.realStartTime)) {
				values.realStartTime = data.realStartTime;
			} else {
				values.realStartTime = new Date(data.realStartTime).format('Y-m-d H:i');
			}
		}
		if (!Ext.isEmpty(data.realEndTime)) {
			if (Ext.isString(data.realStartTime)) {
				values.realEndTime = data.realEndTime;
			} else {
				values.realEndTime = new Date(data.realEndTime).format('Y-m-d H:i');
			}
		}
		Ext.applyIf(values, data);
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpDetail.getPartsRdpInfoTpl();
		tpl.overwrite(Ext.get('id_parts_rdp_detail_base'), values);
		
		// -------------->> 加载“配件检修作业任务”树
		var text = [data.partsName, "（", data.specificationModel, "_", data.partsNo, "）"];

		// -------------->> 加载“拆卸安装配件”列表
		var parentPartsAccountIDX = data.partsAccountIDX;
		// 上级配件idx主键
		PartsDismantleHis.parentPartsAccountIDX = parentPartsAccountIDX;
		// 根据上级配件idx主键查询该配件已拆卸的配件
		PartsDismantleHis.grid.store.load();
		
		// 上级配件idx主键
		PartsInstallHis.parentPartsAccountIDX = parentPartsAccountIDX;
		// 根据上级配件idx主键查询该配件已拆卸的配件
		PartsInstallHis.grid.store.load();
		
		// -------------->> 加载“检修记录单”列表
		PartsRdpRecord.rdpIDX = data.idx;
		PartsRdpRecord.grid.store.load();
		PartsRdpRecord.specificationModel = data.specificationModel;
		
		// -------------->> 加载“提票工单”列表
		PartsRdpNoticeNew.rdpIDX = data.idx;
		PartsRdpNoticeNew.grid.store.load();
		PartsRdpNoticeDetail.partsNo = data.partsNo;
		PartsRdpNoticeDetail.partsName = data.partsName;
		PartsRdpNoticeDetail.specificationModel = data.specificationModel;
		PartsRdpNoticeDetail.identificationCode = data.identificationCode;
		// -------------->> 加载“物料消耗情况”列表
		PartsRdpExpendMatQuery.rdpIDX = data.idx;
		PartsRdpExpendMatQuery.grid.store.load();
	}
	/** **************** 定义全局函数结束 **************** */
	
	PartsRdpDetail.win = new Ext.Window({
		title: '配件履历查看',
		maximized: true,
		layout: 'border',
		closeAction: 'hide',
		items: [{
				region: 'north', height: 138, layout: 'fit', frame: true, collapsible: true,
				items: {
					xtype: 'fieldset', title: '配件信息',
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
						title: '拆卸安装配件',
						layout: 'ux.row',
						defaults: { rowHeight: .5 },
						items: [{
							
							title: '拆卸配件', layout: 'fit',
							items: [PartsDismantleHis.grid]
						}, {
							title: '安装配件', layout: 'fit',
							items: [PartsInstallHis.grid]
						}]
					},{					
						title: '配件提票查看', layout: 'fit',
						items: [PartsRdpNoticeNew.grid]
					},{
						title: '物料消耗情况', layout: 'fit',
						items: [PartsRdpExpendMatQuery.grid]
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
				var data = PartsRdpDetail.record;	
				// 初始化配件检修作业任务查看详情页面			
				PartsRdpDetail.initFn(data);
			}				
		}
	});
});
