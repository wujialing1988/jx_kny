/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('TrainWorkPlanForLeafNode');	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanForLeafNode.day = timelineWindowsLen;
	TrainWorkPlanForLeafNode.nodeLen = timelineNodeLen;
	TrainWorkPlanForLeafNode.args = [];
	TrainWorkPlanForLeafNode.trainInfo = [];
	TrainWorkPlanForLeafNode.workPlanStatus = '';
	
	TrainWorkPlanForLeafNode.parentNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	TrainWorkPlanForLeafNode.leafNodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	TrainWorkPlanForLeafNode.timeline = null;					// 时间轴timeline对象
	TrainWorkPlanForLeafNode.planSuffixChar = '_';			// 计划对比数据主键字符后缀
	TrainWorkPlanForEditLeafNode.item = {};
	TrainWorkPlanForLeafNode.start = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate())
	TrainWorkPlanForLeafNode.status = PLAN_STATUS_NEW + "," + PLAN_STATUS_HANDLING;	
	var data1 =[[4],[7],[14],[21]];
	var data2 =[[12],[24],[48],[72]];	
	/** **************** 定义全局变量结束 **************** */

	/** **************** 更新时间轴的条件定义开始 **************** */
 	 function customOrder (a, b) {
	    // order by planBeginTime asc
	    return b.planBeginTime - a.planBeginTime;
	  }
  
	TrainWorkPlanForLeafNode.options = {
		order: customOrder,
		groupOrder: function (a, b) {
	       return a.value - b.value;
	     },     // 分组排序
	   
		editable:{
			updateTime:true				   // 设置timeline不可编辑,可更新时间
		},
		stack: true,						// 相邻项目时间有重叠时，以堆栈形式显示
  		align: 'left',						// 设置时间轴上条目的文本内容以“居中”方式显示
  		zoomable:false,
  		 verticalScroll: true,  // 垂直滚动
//  		horizontalScroll: true,  // 水平滚动
  		zoomKey: 'ctrlKey',
  		maxHeight:'100%',
  		  orientation: 'top',
  		// 时间轴可显示的最小、最大时间
		min: new Date(nowYear - 1, nowMonth, nowDay),   // 时间轴范围最小值(以当前时间为截止的过去一年)
		max: getYearEndDate(),        					// 时间轴范围最大值(本年末)
  		
		// 时间轴初始化时显示的起、止时间
  		start: TrainWorkPlanForLeafNode.start,				// 当前时间的18分钟
  		end: getDay(TrainWorkPlanForLeafNode.day-0.99),					// 天
  		
  		// 修改时间
		onMove: function (item, callback) {
		    var parentNodeEndTime = new Date(new Date(item.parentNodeEndTime).getFullYear(), new Date(item.parentNodeEndTime).getMonth(), new Date(item.parentNodeEndTime).getDate());
            var oldStartTime = new Date(new Date(item.planBeginTime).getFullYear(), new Date(item.planBeginTime).getMonth(), new Date(item.planBeginTime).getDate());
            var newStartTime = new Date(new Date(item.start).getFullYear(), new Date(item.start).getMonth(), new Date(item.start).getDate());
            var oldEndTime = new Date(new Date(item.planEndTime).getFullYear(), new Date(item.planEndTime).getMonth(), new Date(item.planEndTime).getDate());
            var newEndTime = new Date(new Date(item.end).getFullYear(), new Date(item.end).getMonth(), new Date(item.end).getDate());
            if(NODE_STATUS_YWG == item.status){
            	 Ext.Msg.alert('提示', '节点【'+ item.nodeName +'】已完工不能调整时间！');
        		 callback(null);
            }
            // 判断是否是延期申请中
            else if(item.editStatus == EDIT_STATUS_WAIT){
            	 Ext.Msg.alert('提示', '节点【'+ item.nodeName +'】延期申请待确认，请批复后再修改！');
        		 callback(null);
            }else if(NODE_STATUS_YKG == item.status && newStartTime.getTime() != oldStartTime.getTime()){
   				Ext.Msg.alert('提示', '节点【'+ item.nodeName +'】已开工不能修改开始时间！');
   				 callback(null);
            }else if(newStartTime.getTime() != oldStartTime.getTime() || newEndTime.getTime() != oldEndTime.getTime()){
            
            	// 判断是否是以天为单位拖动节点时间
        		if(newStartTime.getTime() != item.start.getTime() && (newEndTime.getTime()+1000 * 60 * 60* (TrainWorkPlanForLeafNode.nodeLen)-1) != item.end.getTime()){
        			// 如果开始天数不变，则不进行任何操作
        			if(newStartTime.getTime() ==  oldStartTime.getTime()){
   					 	callback(null);
   						return;
        			}else {
        				var newPlanBeginTime =  new Date(new Date(item.start).getFullYear(), new Date(item.start).getMonth(), new Date(item.start).getDate(), 
        												new Date(item.planBeginTime).getHours(), new Date(item.planBeginTime).getMinutes());
        				item.newPlanEndTime = newPlanBeginTime;
						var days = new Date(item.planEndTime).getDate() - new Date(item.planBeginTime).getDate();
						var newPlanEndDate =  new Date(new Date(item.start).getFullYear(), new Date(item.start).getMonth(), new Date(item.start).getDate()+ days);
						newEndTime = newPlanEndDate;
						item.end =  newPlanEndDate;
        			}  			
        		}
				if(newEndTime.getTime() > parentNodeEndTime.getTime()){
					Ext.Msg.confirm('提示', '确认要延期申请吗？', function(btn, id) {
			      		if('yes'== btn){
			      			TrainWorkPlanForEditLeafNode.item = item;
		                	TrainWorkPlanForEditLeafNode.applyWin.show(); 
			      		}
		      			callback(null);
			      	});
				       			
		      	}else {
		      		Ext.Msg.confirm('提示', '确认要修改节点时间吗？', function(btn, id) {
			      		if('yes'== btn){
					    	item.start = newStartTime;	   						
	   						item.end = newEndTime;
	   						TrainWorkPlanForEditLeafNode.changeNode(item);  
			      		}else{
			      			callback(null);
			      			return;
		      			}
		      		});
		      	}
			}else{
				callback(null);
			}
		 }
          	
            	
            	
            	
//    	     	Ext.Msg.confirm('提示','确定要修改节点时间吗？', savefunction);
//		      	function savefunction(btn, id){
//	      			if('yes'== btn){
//			       		if (newEndTime.getTime() > parentNodeEndTime.getTime()){		       			
//			       			TrainWorkPlanForEditLeafNode.item = item;
//			                TrainWorkPlanForEditLeafNode.applyWin.show(); 	  
//			   			}else if(newEndTime.getTime() <= parentNodeEndTime.getTime()){
//			   				if(1== item.editStatus){
//			   					Ext.Msg.alert('提示', '节点【'+ item.nodeName + '】延期申请待确认，请批复后再修改！');
//			   					callback(null);
//			   				}else if(newStartTime.getTime() != oldStartTime.getTime() || newEndTime.getTime() != oldEndTime.getTime()){			   						
//	   							if(newStartTime.getTime() != item.start.getTime() && newEndTime.getTime() != item.end.getTime()){
//	   								// 如果结束天数不变，则不进行任何操作
//	   								if(newEndTime.getTime() ==  oldEndTime.getTime()){
//	   									callback(null);
//	   									return;
//	   								}else{
//	   									item.start = new Date(oldStartTime.getTime()+ 1000*60*60*24);
//	   								}
//   									
//	   							}else{
//   									item.start = newStartTime;
//	   							}	   						
//	   							item.end = newEndTime;
//	   							TrainWorkPlanForEditLeafNode.changeNode(item);
//			   				}
//   						}  			
//			        }
//			      callback(null);
//		      	}
//            }
	  

	};
	/** **************** 更新时间轴的条件定义结束 **************** */

	
	/** **************** 定义全局函数开始 **************** */		
	// 查看详情
	TrainWorkPlanForLeafNode.showDetailWin = function(idx,parentIdx,isLeaf,nodeName,workPlanIDX,workPlanStatus,status) {
		if (!Ext.isEmpty(idx)) {
			var data = {};
			data.idx = idx;
			data.workPlanIDX = workPlanIDX;
			var cfg = {
		        scope: this, url: ctx + '/jobProcessNodeNew!getFirstNodeListNew.action',
		        jsonData: data,
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		           alert(result);
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	}
	// 清空数据集
	TrainWorkPlanForLeafNode.clearFn = function() {
		TrainWorkPlanForLeafNode.parentNodes.clear();
		TrainWorkPlanForLeafNode.leafNodes.clear();
	}
	
	// 格式化分组信息content的显示内容
	TrainWorkPlanForLeafNode.formatParentNodeContentFn = function(parentNode, items,isPlan) {
//		var content = parentNode.get('nodeName'); 			//节点简称
		var content = "<a style='width:150px; display:block;' href='#' onclick='TrainWorkPlanLeafNodeWin.showWin(\"" 
			+ TrainWorkPlanForLeafNode.args.join(",") +													// [0]
			"\",\""+ parentNode.get('idx') +									 	// [1]
			"\",\""+ parentNode.get('parentIdx') +									 	// [1]
			"\",\""+ parentNode.get('isLeaf') +									 	// [1]
			"\",\""+ parentNode.get('nodeName') +									 	// [1]
			"\",\""+ new Date(parentNode.get('planEndTime')) +									 	// [1]
			"\",\""+ TrainWorkPlanForLeafNode.workPlanStatus +									 	// [1]
			"\",\""+ parentNode.get('status') + 								 	// [3]
			"\")'>";
			if (Ext.isEmpty(parentNode.get('nodeName'))) {
				content += "子节点信息";
			} else {
				content += parentNode.get('nodeName');
			}
		content += "</a>";
		if(!Ext.isEmpty(parentNode.get('ratedWorkMinutes'))&& 0 != parentNode.get('ratedWorkMinutes')){
			content += "<span class='smallFont'>[" + formatTimeForHours(parentNode.get('ratedWorkMinutes'), 'm') + "]</span>"
		}	
		// 计划
		var planBeginTime = new Date(parentNode.get('planBeginTime'));
		var planEndTime = new Date(parentNode.get('planEndTime'));
		// 实际
		var beginTime, endTime;
		if (parentNode.get('realBeginTime')) realBeginTime = new Date(parentNode.get('realBeginTime'));
		if (parentNode.get('realBeginTime')) realBeginTime = new Date(parentNode.get('realBeginTime')); 		
		content += "<span class='smallFont'>";
		// 计划时间显示
	 	if (planBeginTime && planEndTime) {
			content += "<br/>" + planBeginTime.format('m-d H:i');
			content += " ~ " + planEndTime.format('m-d H:i');	
		}
		content += "<br/>"
		content += "</span>";
		
//		content += "<a href='#' class='smallFont' onclick='TrainWorkPlanForLeafNode.showDetailWin(\""+  parentNode.get('idx') +									 	// [1]
//			"\",\""+ parentNode.get('parentIdx') +									 	// [1]
//			"\",\""+ parentNode.get('isLeaf') +									 	// [1]
//			"\",\""+ parentNode.get('nodeName') +									 	// [1]
//			"\",\""+ TrainWorkPlanForLeafNode.args[0] +									 	// [1]
//			"\",\""+ TrainWorkPlanForLeafNode.workPlanStatus +									 	// [1]
//			"\",\""+ parentNode.get('status') + 								 	// [3]
//			"\")'>";
//			content += "展开详情" + "</a>";
//		content += "&nbsp&nbsp <a href='#'  class='smallFont' onclick='TrainWorkPlanForLeafNode.hideWin(\"" 												// [0]
//			"\",\""+ parentNode.get('idx') +									 	// [1]
//			"\",\""+ parentNode.get('parentIdx') +									 	// [1]
//			"\",\""+ parentNode.get('isLeaf') +									 	// [1]
//			"\",\""+ parentNode.get('nodeName') +									 	// [1]
//			"\",\""+ TrainWorkPlanForLeafNode.args[0] +									 	// [1]
//			"\",\""+ TrainWorkPlanForLeafNode.workPlanStatus +									 	// [1]
//			"\",\""+ parentNode.get('status') + 								 	// [3]
//			"\")'>";
//			content += "缩略" + "</a>";
		return content;
	}
	
	
	// 格式化分组信息title的显示内容
	TrainWorkPlanForLeafNode.formatParentNodeTitleFn = function(parentNode) {
		var planBeginTime = new Date(parentNode.get('planBeginTime'));		// 计划开始时间
		var planEndTime = new Date(parentNode.get('planEndTime'));			// 计划结束时间
		var title = [];
		title.push("名称：" + parentNode.get('nodeName'));
		title.push("耗时：" + formatTimeForHours((planEndTime - planBeginTime) / 60 / 1000 , 'm'));
		title.push("开始时间：" + planBeginTime.format('Y-m-d H:i'));
		title.push("结束时间：" + planEndTime.format('Y-m-d H:i'));
		if(parentNode.get('applyCount')>0)
			title.push("延期申请节点数：" + parentNode.get('applyCount'));
		if(parentNode.get('delayCount') > 0)
			title.push("超时节点数：" + parentNode.get('delayCount'));
		// 状态
		if (parentNode.get('status') == NODE_STATUS_WKG) {
			title.push("状态：未开工");
		} else if (parentNode.get('status') == NODE_STATUS_YKG) {
			title.push("状态：已开工");
		} else if (parentNode.get('status') == NODE_STATUS_YWG){
			title.push("状态：已完工");
		}
		return title.join('\r\n');
	}
	
	// 格式化机车检修作业计划-流程节点在timeline上的title信息
	TrainWorkPlanForLeafNode.formatNodeTitleFn = function(parentNode, node) {
		var title = [];
		// 名称
		title.push("名称：" + node.nodeName);
		// 工位
	    if(!Ext.isEmpty(node.workStationName)){	    	
			title.push("工位：" + node.workStationName);
		 }
		// 工期
		title.push("工期：" + formatTimeForHours(node.ratedWorkMinutes, 'm'));
		// 计划
		title.push("计划：" + new Date(node.planBeginTime).format('m-d H:i') + " ~ " + new Date(node.planEndTime).format('m-d H:i'));
		// 实际
		if (!Ext.isEmpty(node.realBeginTime)) {
			var realTime = "实际：" + new Date(node.realBeginTime).format('m-d H:i') + " ~ ";
			if (!Ext.isEmpty(node.realEndTime)) {
				realTime += new Date(node.realEndTime).format('m-d H:i');
			}
			title.push(realTime);
		}
	
		// 状态
		if (node.status == NODE_STATUS_WKG) {
			title.push("状态：未开工");
		} else if (node.status == NODE_STATUS_YKG) {
			title.push("状态：已开工");
		} else if (node.status == NODE_STATUS_YWG){
			title.push("状态：已完工");
		}
		// 状态
		if (node.editStatus == EDIT_STATUS_WAIT ) {
			title.push("申请延期：未确认");
		}
			// 延期
		if (!Ext.isEmpty(node.delayTime)) {
			title.push("超时：" + formatTimeForHours(node.delayTime, 'm'));
		}
		// 超时原因
		if (!Ext.isEmpty(node.delayReason)) {
			title.push("超时原因：" + node.delayReason);
		}
		return title.join('\r\n');
	}
	
	/**
	 * 格式化机车检修作业计划-流程节点在timeline上的显示信息
	 * @param node 机车检修作业流程节点数据实体
	 * @param isPlan 是否显示为计划对比样式
	 * @param plan 机车检修作业计划数据实体
	 * @param className 根据显示样式来确定任务完成状态
	 */
	TrainWorkPlanForLeafNode.formatNodeContentFn = function(node, isPlan, parentNode, className) {
		// 1 根据“计划开始时间”和“计划结束时间”计算单个作业流程节点的作业时长（分钟）
		var planBeginDate = new Date(node.planBeginTime);			// 计划开始时间的Date对象
		var planEndDate = new Date(node.planEndTime);				// 计划结束时间的Date对象
		
		var planBeginTime = planBeginDate.getTime();
		var planEndTime = planEndDate.getTime();
		/**
		 * // TODO 完善编辑机车检修作业节点
		 * 编辑机车检修作业节点
		 */
		//方法参数
		var workPlanIDX = parentNode.get('workPlanIDX');
		
		// 与chengr接口对接的传入参数
		var args = [];
		args.push(parentNode.get('idx'));
		args.push(parentNode.get('nodeName'));

		
		// 计划
		var planBeginTime1 = new Date(parentNode.get('planBeginTime'));
		var planEndTime1 = new Date(parentNode.get('planEndTime'));
		
		args.push(planBeginTime1.format('Y-m-d H:i'));
		args.push(planEndTime1.format('Y-m-d H:i'));			
		
		var content = "";
		// 如果任务状态为“已超时”，则要提供填写任务超时原因的链接
		if (!Ext.isEmpty(node.delayTime) && node.status != NODE_STATUS_YWG) {
			content += "<a class='smallFont' href='#' onclick='NodeCaseDelayNew.showEditWin(\"" 
			+ node.idx +													// [0]
			"\",\""+ node.nodeName +									 	// [1]
			"\",\""+ node.workPlanIDX + 								 	// [2]
			"\",\""+ node.realBeginTime + 								 	// [3]
			"\",\""+ node.planBeginTime + 								 	// [4]
			"\",\""+ node.planEndTime + 								 	// [5]
			// 机车信息，如：SS4B_0006_辅修_一次 						 		// [6]
			"\",\""+ TrainWorkPlanForLeafNode.trainInfo[1] + "_" + TrainWorkPlanForLeafNode.trainInfo[2] + "_" + TrainWorkPlanForLeafNode.trainInfo[3] + "_" + TrainWorkPlanForLeafNode.trainInfo[4] + "_" + TrainWorkPlanForLeafNode.trainInfo[5]+
			// 作业流程节点主键
			"\",\""+ node.nodeIDX +						 					// [7]
			// 机车检修作业计划主键
			"\",\""+ node.workPlanIDX +					 					// [8]
			"\",\""+ className +					 						// [9]
			"\")'>" 
			content += "超○</a>&nbsp";			
		}
		// 如果任务状态为“已超时”，则要提供填写任务超时原因的链接
		else if (!Ext.isEmpty(node.delayReason) && node.status == NODE_STATUS_YWG) {
			content += "<a class='smallFont' href='#' onclick='NodeCaseDelayNew.showEditWin(\"" 
			+ node.idx +													// [0]
			"\",\""+ node.nodeName +									 	// [1]
			"\",\""+ node.workPlanIDX + 								 	// [2]
			"\",\""+ node.realBeginTime + 								 	// [3]
			"\",\""+ node.planBeginTime + 								 	// [4]
			"\",\""+ node.planEndTime + 								 	// [5]
			// 机车信息，如：SS4B_0006_辅修_一次 						 		// [6]
			"\",\""+ TrainWorkPlanForLeafNode.trainInfo[1] + "_" + TrainWorkPlanForLeafNode.trainInfo[2] + "_" + TrainWorkPlanForLeafNode.trainInfo[3] + "_" + TrainWorkPlanForLeafNode.trainInfo[4] + "_" + TrainWorkPlanForLeafNode.trainInfo[5]+
			// 作业流程节点主键
			"\",\""+ node.nodeIDX +						 					// [7]
			// 机车检修作业计划主键
			"\",\""+ node.workPlanIDX +					 					// [8]
			"\",\""+ className +					 						// [9]
			"\")'>" 
			content += "超○</a>&nbsp";			
		}
		//判断是否是完成的节点 NODE_STATUS_COMPLETE 是完成节点，如果是完成的，只能看不能修改
		if(node.status == NODE_STATUS_COMPLETE){
			content += "<a class='smallFont' href='#' onclick='LeafNodeAndWorkCardSearch.showNode(\"" 
			+ TrainWorkPlanForLeafNode.args.join(",") +		
			"\",\""+ node.idx +													// [0]
			"\",\""+ node.parentIDX +									 	// [1]
			"\",\""+ node.isLeaf + 								 	// [2]
			"\",\""+ node.status + 								 	// [3]
			"\",\""+ workPlanIDX + 								 	// [4]
			"\")'>" 
			if (Ext.isEmpty(node.nodeName)) {
				content += "节点信息";
			} else {
				content += node.nodeName;
			}
			content += "</a>&nbsp";
		}else{
			content += "<a class='smallFont' href='#' onclick='TrainWorkPlanForEditLeafNode.editNode(\"" 
			+ TrainWorkPlanForLeafNode.args.join(",") +		
			"\",\""+ node.idx +													// [0]
			"\",\""+ node.parentIDX +									 	// [1]
			"\",\""+ node.isLeaf + 								 	// [2]
			"\",\""+ planEndTime1 + 								 	// [2]
			"\",\""+ node.status + 								 	// [3]
			"\",\""+ workPlanIDX + 								 	// [4]
			"\")'>" 
			if (Ext.isEmpty(node.nodeName)) {
				content += "节点信息";
			} else {
				content += node.nodeName;
			}
			content += "</a>&nbsp";
		}
	    if(!Ext.isEmpty(node.workStationName)){	    	
			content += "<span  class='xsmallFont' >" + node.workStationName + "</span>";	
	    }
		content += "<br/><span class='xsmallFont'>" + planBeginDate.format('m-d H:i') + " ~ " + planEndDate.format('m-d H:i') + "</span>";	
		
		return '<div style="height:15px;">' + content + '</div>';
	}
	

	
	// 如果作业流程节点未设置计划开始时间或者计划结束时间的提示信息
	TrainWorkPlanForLeafNode.errorInfo = function(parentNode, node) {
		var trainInfo = [];
		trainInfo.push(parentNode.get('nodeName'));			// 车型名称
		return trainInfo.join('') + " - " + node.nodeName;
	}
	
	// 数据加载完成后的函数处理
	TrainWorkPlanForLeafNode.storeLoadFn = function(store, records, options ) {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var parentNode = this.getAt(i);
			var groupId = parentNode.get('idx')
			// 根据任务完成状态设置不同的显示样式
			var color = "";
			if (parentNode.get('status') == NODE_STATUS_WKG) {
				color = status_wkg;
			} else if (parentNode.get('status') == NODE_STATUS_YKG) {
				color = status_ykg;
			} else {
				color = status_ywg;
			}
			if (parentNode.get('delayCount')> 0 && parentNode.get('status') != NODE_STATUS_YWG) {
				color = status_yyq;
			}

			var items = parentNode.get('leafNodes');
			// 添加分组信息【父节点】
			TrainWorkPlanForLeafNode.parentNodes.update({
				id : groupId + TrainWorkPlanForLeafNode.planSuffixChar,
				title :  TrainWorkPlanForLeafNode.formatParentNodeTitleFn(parentNode),
				style : color,
				value: i,
				content :  i + 1 + "、" + TrainWorkPlanForLeafNode.formatParentNodeContentFn(parentNode,items, true)
			});

			for (var j = 0; j < items.length; j++) {
				var node = items[j];								
				// 根据任务完成状态设置不同的显示样式
				var className = "";
				if (node.status == NODE_STATUS_WKG) {
					className = 'wkg';
				} else if (node.status == NODE_STATUS_YKG) {
					className = 'ykg';
				} else {
					className = 'ywg';
				}
				if (!Ext.isEmpty(node.delayTime) && node.status != NODE_STATUS_YWG) {
					className = 'yyq';
				}
				if(!Ext.isEmpty(node.editStatus)&& EDIT_STATUS_WAIT == node.editStatus){
					className = 'sqyq';
				}
				
				// 添加检修作业流程节点信息（子节点）
				TrainWorkPlanForLeafNode.leafNodes.update({
					id : node.idx + TrainWorkPlanForLeafNode.planSuffixChar,
					title : TrainWorkPlanForLeafNode.formatNodeTitleFn(parentNode, node),
					className : className,
					nodeName: node.nodeName,
					status: node.status,
					editStatus: node.editStatus,
					parentNodeEndTime: new Date( parentNode.get('planEndTime')),
					planBeginTime: new Date(node.planBeginTime),
					planEndTime: new Date(node.planEndTime),
					group : groupId + TrainWorkPlanForLeafNode.planSuffixChar,
					content : TrainWorkPlanForLeafNode.formatNodeContentFn(node, true, parentNode, className),
					// 计划开始时间
					start : EDIT_STATUS_WAIT == node.editStatus ? new Date(new Date(node.newPlanBeginTime).getFullYear(), new Date(node.newPlanBeginTime).getMonth(), new Date(node.newPlanBeginTime).getDate()): new Date(new Date(node.planBeginTime).getFullYear(), new Date(node.planBeginTime).getMonth(), new Date(node.planBeginTime).getDate()),
//       		start :  new Date(node.planBeginTime),
					// 计划结束时间
					end : EDIT_STATUS_WAIT == node.editStatus ? (new Date(new Date(node.newPlanEndTime).getFullYear(), new Date(node.newPlanEndTime).getMonth(), new Date(node.newPlanEndTime).getDate()).getTime()  + 1000 * 60 * 60* (TrainWorkPlanForLeafNode.nodeLen)- 6*1000*60 ): (new Date(new Date(node.planEndTime).getFullYear(), new Date(node.planEndTime).getMonth(), new Date(node.planEndTime).getDate()).getTime()  + 1000 * 60 * 60* (TrainWorkPlanForLeafNode.nodeLen)-6*1000*60)
//				end :new Date(node.planEndTime)
				});
			}

		}
		// 声明一个timeline对象实例
		if (!TrainWorkPlanForLeafNode.timeline) {		
			TrainWorkPlanForLeafNode.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				TrainWorkPlanForLeafNode.leafNodes, 
				TrainWorkPlanForLeafNode.parentNodes, 
				Ext.apply({}, TrainWorkPlanForLeafNode.options, VisUtil.options)
			);
		}

	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义数据容器开始 **************** */
	TrainWorkPlanForLeafNode.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/jobProcessNodeNew!getFirstNodeListNew.action',
		fields : [
			"idx", 
			"processIDX", "nodeIDX", "workPlanIDX",
			"nodeName", "nodeDesc", 
			"ratedWorkMinutes", "realWorkMinutes", 
			"planBeginTime", "planEndTime", 
			"realBeginTime", "realEndTime", "status","parentIDX","workCalendarIDX","planMode","leafNodes","applyCount","delayCount"
		],
		sortInfo : {
			field : 'planBeginTime',
			direction : 'ASC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : TrainWorkPlanForLeafNode.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},
			
			beforeload: function(store, options) {
//	       	 	if(!TrainWorkPlanForLeafNode.loadMask)   
//	       	 		TrainWorkPlanForLeafNode.loadMask = new Ext.LoadMask(Ext.getBody(), {
//	       	 			msg: '正在加载，请稍候32423 ...',
//	       	 			store: TrainWorkPlanForLeafNode.store
//	       	 		});
//       	 		TrainWorkPlanForLeafNode.loadMask.show();
   	 			TrainWorkPlanForLeafNode.clearFn(); // 清空数据集
				var searchParams = {};
				var sm = TrainWorkPlanRecords.trainTypeGrid.getSelectionModel();
				if (sm.getCount() > 0) {
					var records = sm.getSelections();
					searchParams.workPlanIDX = records[0].data.idx;			
					var args = [];
					// 延期原因页面的设置参数
					TrainWorkPlanForLeafNode.trainInfo.push(records[0].data.idx);
					TrainWorkPlanForLeafNode.trainInfo.push(records[0].data.trainTypeShortName);
					TrainWorkPlanForLeafNode.trainInfo.push(records[0].data.trainNo);
					TrainWorkPlanForLeafNode.trainInfo.push(records[0].data.repairClassName);
					TrainWorkPlanForLeafNode.trainInfo.push( records[0].data.repairtimeName);
					TrainWorkPlanForLeafNode.trainInfo.push( records[0].data.dNAME);
					// 节点编辑详情的设置参数
					args.push(records[0].data.idx); 
					args.push(records[0].data.trainTypeShortName + "|" + records[0].data.trainNo);
					var xcxc = records[0].data.repairClassName;
					if (!Ext.isEmpty(records[0].data.repairtimeName))
						xcxc+= "|" + records[0].data.repairtimeName;
					args.push(xcxc);					
					// 计划
					var planBeginTime1 = new Date(records[0].data.planBeginTime);
					var planEndTime1 = new Date(records[0].data.planEndTime);					
					args.push(planBeginTime1.format('Y-m-d H:i'));
					args.push(planEndTime1.format('Y-m-d H:i'));			
					TrainWorkPlanForLeafNode.workPlanStatus = records[0].data.workPlanStatus;
					TrainWorkPlanForLeafNode.args = args;
				}		
				searchParams = MyJson.deleteBlankProp(searchParams);
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});
		
	
	// 创建分页工具栏
	TrainWorkPlanForLeafNode.createPagingToolbarFn = function(){
	 	cfg = {pageSize:10, store: TrainWorkPlanForLeafNode.store};
	    //配置分页工具栏，表格默认每页显示记录数
	    var pageSize = cfg.pageSize || 10;  
	    //每页显示条数下拉选择框
	    var pageComboBox = new Ext.form.ComboBox({
	        name: 'pagesize',     triggerAction: 'all',  mode : 'local',   width: 75,
	        valueField: 'value',  displayField: 'text',  value: pageSize,  editable: false,
	        store: new Ext.data.ArrayStore({
	            fields: ['value', 'text'],
	            data: [[10, '10条/页'], [20, '20条/页'], [50, '50条/页'], [100, '100条/页']]
	        }),
	        listeners: {
			    // 改变每页显示条数reload数据
	        	select: function(comboBox) {
			    	// 清空数据集
		    		TrainWorkPlanForLeafNode.clearFn();
			        pagingToolbar.pageSize = parseInt(comboBox.getValue());
			        pagingToolbar.store.reload({
			            params: {
			                start: 0,    limit: pagingToolbar.pageSize
			            }
			        });
	        	}
	        }
	    });
	    
    //一个新实例化表格的分页工具栏
    var pagingToolbar = new Ext.PagingToolbar({
	        pageSize: pageSize,   emptyMsg: "没有符合条件的记录",
	        displayInfo: true,    displayMsg: '显示 {0} 条到 {1} 条,共 {2} 条',    
	        items: ['-', '&nbsp;&nbsp;', pageComboBox, '-'],
	        listeners: {
	        	beforechange : function() {
	        		// 清空数据集
					TrainWorkPlanForLeafNode.clearFn();
	        	}
	        }
	    });
	    pagingToolbar.pageComboBox = pageComboBox;
	    //分页工具栏绑定数据源
	    if(cfg.store != null) {
	        pagingToolbar.bind(cfg.store);
	        cfg.store.on('beforeload', function(store, options){
	            store.baseParams.limit = pagingToolbar.pageSize;
	        });
	    }
	    return pagingToolbar;
	 };
	/** **************** 定义数据容器结束 **************** */
	//机车详情页面
	TrainWorkPlanForLeafNode.TrainRecordPanel =  new Ext.Panel({		
				region: 'center',
				border: false, autoScroll : false, anchor:'100%',
				tbar: [{
					text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						self.location.reload();
					}			
				}, '-',  {
					emptyText:'默认为'+ timelineWindowsLen + '天',
					name:"deckId", 
					xtype: 'combo',	
					width:80,
			        store:new Ext.data.SimpleStore({
					    fields: ['K'],
						data : data1
					}),
					valueField:'K',
					displayField:'K',
					triggerAction:'all',
					mode:'local',		
					listeners: {
						keyup: function(filed, e) {
							// 如果敲下Enter（回车键），则触发添加按钮的函数处理
							if (e.getKey() == e.ENTER){
								TrainWorkPlanForLeafNode.day = this.getValue();
								TrainWorkPlanForLeafNode.timeline.setWindow({
						           	start: TrainWorkPlanForLeafNode.start,				            // 当前天
  									end: getDay(TrainWorkPlanForLeafNode.day-0.99)					// 第TrainWorkPlanForLeafNode.day天
						        });
//								TrainWorkPlanForLeafNode.timeline.setOptions(options);
							}
			    		}
		    		}
				}, '-',  {
				text : '批量派工',				
				iconCls:"chart_organisationIcon",
		    	handler: function(){
		    		LeafNodeListGrid.updateWorkCardForDispatchByRepairLine();
		    	}
				
//					emptyText:'节点显示长度默认为' + timelineNodeLen + '小时',
//					name:"deckId", 
//					xtype: 'combo',	
//			        store:new Ext.data.SimpleStore({
//					    fields: ['K'],
//						data : data2
//					}),
//					valueField:'K',
//					displayField:'K',
//					triggerAction:'all',
//					mode:'local',		
//					listeners: {
//						keyup: function(filed, e) {
//							// 如果敲下Enter（回车键），则触发添加按钮的函数处理
//							if (e.getKey() == e.ENTER){
//								// 清空数据集
//								TrainWorkPlanForLeafNode.clearFn();
//								TrainWorkPlanForLeafNode.nodeLen = this.getValue();
//								TrainWorkPlanForLeafNode.store.load();
//							}
//			    		}
//		    		}
				}, '->', '图例：', '-', {
					xtype : 'label',
					text : '未开工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_wkg'
				}, '-', {
					xtype : 'label',
					text : '已开工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_ykg'
				}, '-', {
					xtype : 'label',
					text : '已超时',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_yyq'
				}, '-', {
					xtype : 'label',
					text : '已完工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_ywg'
				}, '-', {
					xtype : 'label',
					text : '延期申请',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_sqyq'
				}],			
				html : [
					'<div id= "visualization">',
					'</div>'
				].join("")				
//				bbar: TrainWorkPlanForLeafNode.createPagingToolbarFn()
			
	});
	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var timerTask = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
				// 加载数据
				TrainWorkPlanForLeafNode.store.reload();
			},
			interval : 1000 * 60 * 10		// 每10分钟更新一次
		});
	});
	// 延迟30秒
	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */
});