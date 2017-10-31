/**
 * 显示配件详情信息
 */
Ext.onReady(function(){
	Ext.namespace('PartsDetail');                       //定义命名空间

	/** **************** 定义全局函数开始 **************** */
	PartsDetail.getPartsDetail = function() {
		if (Ext.isEmpty(PartsDetail.tpl)) {
			PartsDetail.tpl = new Ext.XTemplate(
			'<table class="pjjx-show-info-table">',
				'<tr>',         
	                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%" colspan="3">{partsName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%" colspan="2">{specificationModel}</td>',
	               
	            '</tr>',
	            '<tr>',
	             '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
	                 '<td class="pjjx-show-info-table-label" width="12%">责任部门：</td><td width="13%">{manageDept}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">存放地点：</td><td width="13%">{location}</td>',
	                
	            '</tr>',
	            '<tr>',
	             '<td class="pjjx-show-info-table-label" width="12%">是否新品：</td><td width="13%">{isNewParts}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件旧编号：</td><td width="13%">{oldPartsNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">详细配置：</td><td width="13%">{configDetail}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">计量单位：</td><td width="13%">{unit}</td>',
	               
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车车型：</td><td width="13%">{unloadTrainType}{unloadTrainNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车修程：</td><td width="13%">{unloadRepairClass}</td>',            
	                '<td class="pjjx-show-info-table-label" width="12%">下车修次：</td><td width="13%">{unloadRepairTime}</td>',	               
	                '<td class="pjjx-show-info-table-label" width="12%">下车时间：</td><td width="13%">{unloadDate}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车位置：</td><td width="13%">{unloadPlace}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车原因：</td><td width="13%">{unloadReason}</td>',               
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">上车车型：</td><td width="13%">{aboardTrainType}{aboardTrainNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">上车修程：</td><td width="13%">{aboardRepairClass}</td>',            
	                '<td class="pjjx-show-info-table-label" width="12%">上车修次：</td><td width="13%">{aboardRepairTime}</td>',	               
	                '<td class="pjjx-show-info-table-label" width="12%">上车时间：</td><td width="13%">{aboardDate}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">上车位置：</td><td width="13%">{aboardPlace}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">生产厂家：</td><td width="13%">{repairOrgName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">出厂日期：</td><td width="13%">{factoryDate}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件状态：</td><td width="13%">{partsStatusName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">状态修改日期：</td><td width="13%">{partsStatusUpdateDate}</td>',
	            '</tr>',
			'</table>');
		}
	    return PartsDetail.tpl;
	}
	/**
	 * 初始化配件详情查看页面
	 * @param data 配件周转实体
	 */
	PartsDetail.initFn = function(data) {
		var values = {};
		// 格式化“开始时间”和“结束时间”
		if (!Ext.isEmpty(data.partsStatusUpdateDate)) {
			if (Ext.isString(data.partsStatusUpdateDate)) {
				values.partsStatusUpdateDate = data.partsStatusUpdateDate;
			} else {
				values.partsStatusUpdateDate = new Date(data.partsStatusUpdateDate).format('Y-m-d H:i');
			}
		}
		if (!Ext.isEmpty(data.unloadDate)) {
			if (Ext.isString(data.unloadDate)) {
				values.unloadDate = data.unloadDate;
			} else {
				values.unloadDate = new Date(data.unloadDate).format('Y-m-d H:i');
			}
		}
		if (!Ext.isEmpty(data.factoryDate)) {
			if (Ext.isString(data.factoryDate)) {
				values.factoryDate = data.factoryDate;
			} else {
				values.factoryDate = new Date(data.factoryDate).format('Y-m-d H:i');
			}
		}
		Ext.applyIf(values, data);
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsDetail.getPartsDetail();
		tpl.overwrite(Ext.get('id_parts_detail_base'), values);
	}
	/** **************** 定义全局函数结束 **************** */

	
	PartsDetail.win = new Ext.Window({
		title: '配件详情信息',
		maximized: true,
		layout: 'fit',
		closeAction: 'hide',
		items: [{		
				region: 'center', layout: 'fit',frame:true,collapsible: true,
				items: {
					xtype: 'fieldset', 
					html: '<div id="id_parts_detail_base"></div>'
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
				// 配件周转详情实体
				var data = PartsDetail.record;
				// 初始化配件检修作业任务查看详情页面
				PartsDetail.initFn(data);
			}
				
		}	
	});
});
