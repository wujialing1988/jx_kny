/**
 * 本文件定义了系统页面公用js脚本
 * @author hetao
 * Created on 2017-01-12 13:50
 * @class com.yunda.Common
 * 
 * 基本用法，通过以下语句在jsp页面进行引用：
 * <script type="text/javascript" src="<%= ctx %>/com/yunda/Common.js"></script>
 */
Ext.ns('com.yunda.Common');

/**
 * 当前系统操作员所在班组包含在班组名称中，则高亮其班组名称
 * Added by hetao on 2017-01-12 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.orgNameRenderer = function(value, metadata, record, rowIndex, colIndex, store) {
	// 空值验证
	if (Ext.isEmpty(value)) {
		return value;
	}
	var array = value.split(',');
	for (var i = 0; i < array.length; i++) {
		// 高亮显示匹配到的操作员所在班组
		if (teamOrgName == array[i]) {			// teamOrgName 为系统操作员所在班组，/frame/jspf/header.jspf
			array[i] = '<span style="background:#0f0;border-radius:8px;padding:1px 4px;">' + array[i] + '</span>';
			break;
		}
	}
	return array.join(',');
}

/**
 * 当前系统操作员名称包含在人员名称中，则高亮其人员名称
 * Added by hetao on 2017-01-13 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.empNameRenderer = function(value, metadata, record, rowIndex, colIndex, store) {
	// 空值验证
	if (Ext.isEmpty(value)) {
		return value;
	}
	var array = value.split(',');
	for (var i = 0; i < array.length; i++) {
		// 高亮显示匹配到的操作员所在班组
		if (uname == array[i]) {		// uname 为系统操作员所在班组，定义请参见/SBGL3/WebRoot/sys/biz_value.js.jsp
			array[i] = '<span style="background:#0f0;border-radius:8px;padding:1px 4px;">' + array[i] + '</span>';
			break;
		}
	}
	return array.join(',');
}

/**
 * 提示信息框
 * Added by hetao on 2017-01-13 
 * @param msg : 提示信息
 * @param cfg0 : 按钮可选配置
 * @param cfg1 : 提示框可选配置
 */
com.yunda.Common.INFO = function(msg, cfg0, cfg1) {
	return Ext.apply({
		xtype: 'button', iconCls: 'messageIcon', tooltip: "I'll tell you!", handler: function() {
			Ext.Msg.show(Ext.apply({
				   title: '提示',
				   msg: msg,
				   buttons: Ext.MessageBox.OK,
				   icon: Ext.MessageBox.INFO
				}, cfg1))		// 提示框可选配置
			}
	}, cfg0)		// 按钮可选配置
}

/**
 * 根据“计划结束日期”和“当前日期”计算延期天数，如果返回值大于0则表示该任务已延期
 * @param planEndTime : 计划结束日期，Date类型
 * @param wclCount : 该任务下属未处理子任务数量
 */
com.yunda.Common.getDelayDays = function(planEndTime, wclCount) {
	var now = new Date();
	// 延期天数
	var delayDays = Math.floor((now - planEndTime) / (1000 * 60 * 60 * 24));
	if (Ext.isEmpty(wclCount)) {
		return delayDays;
	}
	return 0 >= wclCount ? 0 : delayDays;
}

/**
 * 检修任务设备“实际完工时间”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.realEndTimeRenderer2JX = function(value, metadata, record, rowIndex, colIndex, store) {
	// 如果“实际完工时间”不为空，即：检修任务已处理完成，直接回显时间完工时间
	if (value) {
		return new Date(value).format('Y-m-d H:i');
	}
//	if (0 >= parseInt(record.get('wclCount'))) {
//		return;
//	}
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('endTime')), record.get('wclCount'));
	// 已延期检修任务高亮提示信息
	if (delayDays > 0) {
		return '<div style="background:' + Constants.YyqColor + ';width:85px;height:15px;line-height:15px;text-align:center;border-radius:8px;color:#fff;margin-left:10px;" title="该检修任务已延期，请尽快处理！">已延期&nbsp;<span style="font-weight:bold;">'+ delayDays +'</span>&nbsp;天</div>';
	}
	// 未延期
	var displayValue = '处理中';
	var dispalyColor = Constants.YclColor;
	if (Ext.isEmpty(record.get('realBeginTime'))) {
		displayValue = '未开工';
		dispalyColor = Constants.WclColor;
	}
	return [
        '<div style="background:', dispalyColor, ';width:48px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;">', 
        	displayValue, 
        '</div>'
    ].join('');
}

/**
 * 巡检任务设备“实际完工时间”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.realEndTimeRenderer2XJ = function(value, metadata, record, rowIndex, colIndex, store) {
	// 如果“实际完工时间”不为空，即：检修任务已处理完成，直接回显时间完工时间
	if (value) {
		return new Date(value).format('Y-m-d H:i');
	}
//	if (0 >= parseInt(record.get('wclCount'))) {
//		return;
//	}
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), record.get('wclCount'));
	// 已延期检修任务高亮提示信息
	if (delayDays > 0) {
		return '<div style="background:' + Constants.YyqColor + ';width:85px;height:15px;line-height:15px;text-align:center;border-radius:8px;color:#fff;margin-left:10px;" title="该巡检任务已延期，请尽快处理！">已延期&nbsp;<span style="font-weight:bold;">'+ delayDays +'</span>&nbsp;天</div>';
	}
	// 未延期
	var displayValue = '处理中';
	var dispalyColor = Constants.YclColor;
	if (Ext.isEmpty(record.get('realBeginTime'))) {
		displayValue = '未开工';
		dispalyColor = Constants.WclColor;
	}
	return [
        '<div style="background:', dispalyColor, ';width:48px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;">', 
        	displayValue, 
        '</div>'
    ].join('');
}

/**
 * 检修任务设备“计划完工时间”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.planEndDatetRenderer2JX = function(value, metadata, record, rowIndex, colIndex, store) {
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('endTime')), record.get('wclCount'));
	if (0 < delayDays) {
		metadata.attr = 'style="font-weight:bold"';
		metadata.css = 'yyq-color';
	}
	return value ? new Date(value).format('Y-m-d') : '';
}

/**
 * 巡检任务设备“计划完工时间”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.planEndDatetRenderer2XJ = function(value, metadata, record, rowIndex, colIndex, store) {
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), record.get('wclCount'));
	if (0 < delayDays) {
		metadata.attr = 'style="font-weight:bold"';
		metadata.css = 'yyq-color';
	}
	return value ? new Date(value).format('Y-m-d') : '';
}

/**
 * 检修任务设备“检修任务进度”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.yclCountRenderer2JX = function(value, metadata, record, rowIndex, colIndex, store) {
	var jdBg = Constants.YclColor,
	wclCount = record.data.wclCount,
	yclCount = record.data.yclCount;
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('endTime')), record.get('wclCount'));
	if (0 < delayDays) {
		jdBg = Constants.YyqColor;
	}
	// 使用进度条样式显示巡检记录处理情况
	var total = parseInt(yclCount) + parseInt(wclCount);
	var width = 100;
	var displyValue = '';
	if (0 !== total) {
		width = 100 * parseInt(yclCount) / total;
		width = width == 0 ? 1 : width;
		displyValue = yclCount + '/' + total;
	}
	return [
	    '<div style = "background:rgb(239, 239, 239);margin-left:-5px;height:13px;width:100px;border-radius:8px;border:1px solid rgb(133, 139, 173);">',
	        '<div style = "background:' + jdBg + ';font-weight:bold;height:13px;text-align:right;border-radius:7px;width:'+ width +'px;">',
	        	'<a href="#" onclick="RepairTaskList.showRepairScopeCase()">', displyValue, '</a>',
	        '</div>',
	    '</div>'
	].join('');
}

/**
 * 巡检任务设备“项目检查进度”列渲染函数
 * Added by hetao on 2017-03-07 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.yclCountRenderer2XJ = function(value, metadata, record, rowIndex, colIndex, store) {
	var jdBg = Constants.YclColor,
	wclCount = record.data.wclCount,
	yclCount = record.data.yclCount;
	// 验证检修任务是否已经延期，并返回延期天数，返回0则表示未延期（状态正常）
	var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), record.get('wclCount'));
	if (0 < delayDays) {
		jdBg = Constants.YyqColor;
	}
	// 使用进度条样式显示巡检记录处理情况
	var total = parseInt(yclCount) + parseInt(wclCount);
	var width = 100;
	var displyValue = '';
	if (0 !== total) {
		width = 100 * parseInt(yclCount) / total;
		width = width == 0 ? 1 : width;
		displyValue = yclCount + '/' + total;
	}
	return [
	    '<div style = "background:rgb(239, 239, 239);margin-left:-5px;height:13px;width:100px;border-radius:8px;border:1px solid rgb(133, 139, 173);">',
	        '<div style = "background:' + jdBg + ';font-weight:bold;height:13px;text-align:right;border-radius:7px;width:'+ width +'px;">',
	        	'<a href="#" onclick="InspectPlanEquipment.showInspectRecord()">', displyValue, '</a>',
	        '</div>',
	    '</div>'
    ].join('');
}

/**
 * 检修任务“照片数量”列渲染函数
 * Added by hetao on 2017-04-24
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.imageCountRenderer2JX = function(value, metadata, record, rowIndex, colIndex, store) {
	if (value) {
		return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="RepairTaskList.showImage()">' + value + '</a></div>';
	}
}

/**
 * 检修任务工单“照片数量”列渲染函数
 * Added by hetao on 2017-04-24
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.imageCountRenderer2JXGD = function(value, metadata, record, rowIndex, colIndex, store) {
	if (value) {
		return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="RepairWorkOrder.showImage()">' + value + '</a></div>';
	}
}

/**
 * 巡检任务“照片数量”列渲染函数
 * Added by hetao on 2017-04-24 
 * @param value : Object
 * @param metadata : Object
 * @param record : Ext.data.record
 * @param rowIndex : Number
 * @param colIndex : Number
 * @param store : Ext.data.Store
 */
com.yunda.Common.imageCountRenderer2XJ = function(value, metadata, record, rowIndex, colIndex, store) {
	if (value) {
		return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="InspectPlanEquipment.showImage()">' + value + '</a></div>';
	}
}