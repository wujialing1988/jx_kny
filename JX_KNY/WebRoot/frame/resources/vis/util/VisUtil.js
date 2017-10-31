

Ext.ns('VisUtil');

/**
 * 获取时间轴的开始时间
 * @param {} offset 时间偏差，单位毫秒
 */
VisUtil.newTime = function(offset) {
	var now = new Date();
	if (offset) {
		return new Date(now.getTime() + offset);
	}
	return now;
}

/**
 * 时间轴Timeline基础配置
 * @type JSON
 */
VisUtil.options = {
	
  	start: VisUtil.newTime(-1000*60*10),
  	
//    orientation: 'both',
  	orientation: {
        axis: 'top', // axis orientation: 'bottom', 'top', or 'both' or 'none'
        item: 'bottom' // not relevant
    },
    editable: {
	    add: true,         		// 允许通过双击时间轴新增一个任务项
	    updateTime: true,  		// 允许水平拖动任务项进行更新
	    updateGroup: false, 	// 不允许将选中的任务项从一个分组拖动到另一个分组
	    remove: true       		// 允许通过点击任务项右上角的删除按钮从时间轴上移除任务项
	},
		
	min: new Date(new Date().getFullYear(), 0, 1),          // 时间轴范围最小值
	max: new Date(new Date().getFullYear(), 11, 31),        // 时间轴范围最大值
	zoomMin: 1000 * 60 * 50,             					// 时间轴缩小的最小精度（五分钟）
	zoomMax: 1000 * 60 * 60 * 24 * 30 * 12,     			// 时间轴放大的最大精度（一年）
	
	locales: {							  // 自定义本地化
		"zh-cn": {
			current: 'current',
			time: 'time'
		},
		"en": {
			current: 'current',
		    time: 'time'
		}
  	},
  	locale: 'zh-cn',
  	align: 'center',
  	
     margin: {
      item: 0, // minimal margin between items
      axis: 1   // minimal margin between items and the axis
    }
}
