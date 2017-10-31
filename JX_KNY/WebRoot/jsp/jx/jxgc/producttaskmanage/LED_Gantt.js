Ext.onReady(function(){	
	var project = new Edo.project.Project();
	project.set({
	    width: document.documentElement.scrollWidth - 15, 
	    height: document.documentElement.scrollHeight - 25,
	    enableMenu:false, readOnly:true/*,    
	    render: document.getElementById('view')*/
	});
	project.render(document.getElementById('view'));
	Edo.managers.ResizeManager.reg({
	    target: project
	});
	
	//创建ProjectToolBar工具栏
	/*var toolbar = new ProjectToolBar();
	toolbar.set({
	    project: project,//将创建的project对象设置给ProjectToolBar
	    width: 120,
	    border: [1,1,0,1], 
	    render: document.getElementById('toolbar')
	});*/
	
	//获得任务树已有定义的列配置对象
	var columns = project.tree.groupColumns;
	columns[0].header = '检修活动甘特图';
	project.tree.groupColumns[0].columns[2].width = 200;
	columns[0].columns.removeAt(3);
	columns[0].columns.insert(3,{
	    header: '工期',
	    dataIndex: 'workDate',
	    width: 80
	});
	var workDate_column = project.tree.groupColumns[0].columns[3];
	workDate_column.renderer = function(v, r){
		if(v != null && v != '' && v != 'null'){
			var hours = v.toString().split(".")[0];
			var minutes = (v*60 - hours*60).toFixed(0);
			if(hours > 0){
				if(minutes > 0){
	    			return hours + "小时" + minutes + "分钟";
				}else{
					return hours + "小时";
				}
			}else{
				return minutes + "分钟"	;	
			}
		}else{
			return "0小时";
		}
	}
	//获取开始时间列
	var start_column = project.tree.groupColumns[0].columns[5];
	//获取结束时间列
	var end_column = project.tree.groupColumns[0].columns[6];
	start_column.width = 130;
	end_column.width = 130;
	start_column.header = "计划开始时间";
	end_column.header = "计划完成时间";
	//给开始日期列增加新的渲染器
	start_column.renderer = function(v, r){
		if(v != null && v != '' && v != 'null'){
	    	return v.format('Y-m-d H:i');
		}else{
			return "";
		}
	}
	//给结束日期列增加新的渲染器
	end_column.renderer = function(v, r){
	    if(v != null && v != '' && v != 'null'){
	    	return v.format('Y-m-d H:i');
		}else{
			return "";
		}
	}
	columns[0].columns.removeAt(8);
	//添加新的列
	columns[0].columns.add({
	    header: '实际开始时间',
	    dataIndex: 'realStart',
	    width: 130
	});
	columns[0].columns.add({
	    header:'实际结束时间',
	    dataIndex: 'realFinish',
	    width: 130
	});
	columns[0].columns.add({
	    header: '类型',
	    dataIndex: 'NodeType'
	});
	columns[0].columns.add({
	    header:'处理情况',
	    dataIndex: 'ProcessInfo'
	});
	//获得条形图对象
	var gantt = project.gantt;
	//•年/季：year-quarter
	//•年/月：year-month
	//•年/周：year-week
	//•年/日：year-day
	//•季/月：quarter-month
	//•季/周：quarter-week
	//•季/日：quarter-day
	//•月/周：month-week
	//•月/日：month-day
	//•周/日：week-day
	//•日/时：day-hour
	//•时/分：hour-minute
	gantt.set('dateView', 'day-hour');
	
	var dataProject = project.data;
	
	dataProject.enableDurationLimit = true;
	//将修改后的列配置对象, 设置给任务树的columns属性
	project.tree.set('columns', columns);
	
	//loadDataProject(projectData, project);
	//TODO V3.2.1代码重构
	var url = ctx + '/trainEnforcePlanRdp!ganttTree.action?rdpIdx=' + rdpIdx + '&displayNode=true';
	loadJSON(url, project);
});