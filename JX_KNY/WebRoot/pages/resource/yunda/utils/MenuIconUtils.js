/**
 * 图标样式
 */

// 获取对应菜单的图标样式
function getMenuIconClass(menuName){
	var iconClass = "fa-external-link-square" ;
	if( menuName == '机车出入段' 
	 || menuName == '生产计划' 
	 || menuName == '车间任务调度'){
		iconClass = "fa-hourglass-half" ;
	}else if(   menuName == '检查提票'){
		iconClass = "fa-calendar-check-o" ;
	}else if(   menuName == '月计划查询' 
		 	 || menuName == '今日检修计划' 
			 || menuName == '机车试运计划'){
		iconClass = "fa-bar-chart" ;
	}
	return iconClass ;
}