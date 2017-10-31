/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('TrainWorkPlanForLeafNode');	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanForLeafNode.args = [];
	TrainWorkPlanForLeafNode.trainInfo = [];
	TrainWorkPlanForLeafNode.workPlanStatus = '';
	TrainWorkPlanForLeafNode.idx ='';
	TrainWorkPlanForLeafNode.parentNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	TrainWorkPlanForLeafNode.edges = new vis.DataSet();
	TrainWorkPlanForLeafNode.leafNodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	TrainWorkPlanForLeafNode.network = null;					// 时间轴network对象
	TrainWorkPlanForLeafNode.planSuffixChar = '_';			// 计划对比数据主键字符后缀
	TrainWorkPlanForLeafNode.value = 3;			// 流程显示形状大小设置

	TrainWorkPlanForLeafNode.start = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate())
	
	/** **************** 定义全局变量结束 **************** */

	/** **************** 条件定义开始 **************** */
	TrainWorkPlanForLeafNode.options = {
        groups: {
	          usergroups: {
	            shape: 'image',  image: '1'
	          },
	          users: {
	            shape: 'square'
	          },
	          dot: {
	            shape: 'dot'
	         },
	         main:{main:"adsf adsf asd",width:'100%'}
        }
//        ,layout: {
//                hierarchical: {
//                    direction: 'LR'
//                }
//            }
 };

	TrainWorkPlanForLeafNode.data = {		
		 nodes: TrainWorkPlanForLeafNode.parentNodes,
         edges: TrainWorkPlanForLeafNode.edges
	};		
	/** **************** 条件定义结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */	
   TrainWorkPlanForLeafNode.getNodeEntity = function(nodeIDX, workPlanIDX){
   		if (!Ext.isEmpty(nodeIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/jobProcessNodeNew!getEntityByIDX.action',
		        params: {nodeIDX: nodeIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                nodeEntity = result.entity;
						var parentIdx = nodeEntity.parentIdx ;
						var isLeaf = nodeEntity.isLeaf;
						var nodeName = nodeEntity.nodeName ;
						var planEndTime = new Date(nodeEntity.planEndTime); 
						var status = nodeEntity.status;
						TrainWorkPlanLeafNodeWin.showWin(TrainWorkPlanForLeafNode.args.join(","),nodeIDX,parentIdx,isLeaf,nodeName,planEndTime,TrainWorkPlanForLeafNode.workPlanStatus,status);
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
   }
   TrainWorkPlanForLeafNode.getPartNodeEntity = function(partIDX, workPlanIDX ){
   		if (!Ext.isEmpty(partIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/partsRdpNode!getEntityByPartIDX.action',
		        params: {partIDX: partIDX, workPlanIDX: workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null && result.entity.length > 0) {		            	
		                nodeEntity = result.entity;
						PartsRdpNodeWin.showFlowWin(nodeEntity);
		            }else {
		            	 TrainWorkPlanForLeafNode.getNodeEntity(partIDX, workPlanIDX);
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
   }
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
		TrainWorkPlanForLeafNode.edges.clear();
	}
		
	// 格式化机车检修作业计划-流程节点在network上的title信息
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
		if (node.editStatus == EDIT_STATUS_WAIT) {
			title.push("申请延期：未确认");
		}
			// 延期
		if (!Ext.isEmpty(node.delayTime)) {
			title.push("超时：" + formatTimeForHours(node.delayTime, 'm'));
		}
		// 延期原因
		if (!Ext.isEmpty(node.delayReason)) {
			title.push("超时原因：" + node.delayReason);
		}
		return title.join('\r\n');
	}
	
	// 格式化机车流程节点信息label的显示内容
	TrainWorkPlanForLeafNode.formatParentNodeContentFn = function(parentNode,isPlan) {
		var content = parentNode.nodeName; 			//节点简称		
		// 计划
		var planBeginTime = new Date(parentNode.planBeginTime);
		var planEndTime = new Date(parentNode.planEndTime);
		// 实际
		var beginTime, endTime;
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);		
		// 计划时间显示
	 	if (planBeginTime && planEndTime) {
			content += "\n" + planBeginTime.format('m-d H:i');
			content += " ~ " + planEndTime.format('m-d H:i');	
		}
		content += "\n"
			return content;
	}
	
	// 格式化配件流程节点信息label的显示内容
	TrainWorkPlanForLeafNode.formatwpNodeLabelFn = function(wpNode) {
		var content = wpNode.nodeName; 			//节点简称		
		// 计划
		var planBeginTime = new Date(parentNode.planBeginTime);
		var planEndTime = new Date(parentNode.planEndTime);
		// 实际
		var beginTime, endTime;
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);		
		// 计划时间显示
	 	if (planBeginTime && planEndTime) {
			content += "\n" + planBeginTime.format('m-d H:i');
			content += " ~ " + planEndTime.format('m-d H:i');	
		}
		content += "\n"
			return content;
	}
	
	
	// 格式化分组信息title的显示内容
	TrainWorkPlanForLeafNode.formatParentNodeTitleFn = function(parentNode) {
		var planBeginTime = new Date(parentNode.planBeginTime);		// 计划开始时间
		var planEndTime = new Date(parentNode.planEndTime);			// 计划结束时间
		var title = [];
		title.push("<p>名称：" + parentNode.nodeName);
		title.push("耗时：" + formatTimeForHours((planEndTime - planBeginTime) / 60 / 1000 , 'm'));
		title.push("开始时间：" + planBeginTime.format('Y-m-d H:i'));
		title.push("结束时间：" + planEndTime.format('Y-m-d H:i'));
		if(parentNode.applyCount>0)
			title.push("延期申请节点数：" + parentNode.applyCount);
		if(parentNode.delayCount > 0)
			title.push("超时节点数：" + parentNode.delayCount);
		// 状态
		if (parentNode.status == NODE_STATUS_WKG) {
			title.push("状态：未开工");
		} else if (parentNode.status == NODE_STATUS_YKG) {
			title.push("状态：已开工");
		} else if (parentNode.status == NODE_STATUS_YWG){
			title.push("状态：已完工");
		}
		return title.join('<br></p>');  // 换行
	}
	
	/**
	 * 格式化配件检修计划-流程节点在network上的显示信息
	 * @param offPartList 大部件检修数据实体列表
	 * @param i 第i个大部件
	 * @param flag 显示在上半还是下半部分，1 表示上半部分，2表示下半部分
	 * @param partsLen 大部件列表实体长度
	 */
	TrainWorkPlanForLeafNode.addOffPartListContentFn = function(offPartList,i, flag, partsLen){
			var part = offPartList[i];							
			// 根据任务完成状态设置不同的显示样式					
			var shapeD = 'text';  // 文本框
			var colorD = "";
			if (part.status == NODE_STATUS_WKG) {
			colorD = status_wkg;
			} else if (part.status == NODE_STATUS_YKG) {
				colorD = status_ykg;
			} else {
				colorD = status_ywg;
			}
			if (part.delayCount> 0 && part.status != NODE_STATUS_YWG) {
				colorD = status_yyq;
			}
			var y =  i*200/partsLen - 50;
			if(flag == 2){
				 y =  -(i-1)*200/partsLen + 150;
			}
			// 添加【配件名称流程显示】
			TrainWorkPlanForLeafNode.parentNodes.update({
				id : part.partsIDX,
				label : part.partsName,
				shape: shapeD,
				size:20,
			    color:colorD,
				x:	-150,
				y: y,
				fixed:true,			
				value: TrainWorkPlanForLeafNode.value,
				title : "点击配件查询详情",
				 font: {strokeWidth: 3,'face': 'Monospace', align: 'left'}
			});
			// 流程中的第一个节点时从下车节点起
			 var fromID = '';
			 if(i==0){ 
				fromID = part.offNodeCaseIDX;
			 } else{
			 	fromID = offPartList[i-1].partsIDX;
			 }
			TrainWorkPlanForLeafNode.edges.add({
				  from: fromID,
				  label: '',
				  length:100,
				  smooth:false,
       			  to: part.partsIDX			
			});
			var wpNodes = part.wpNodes;
			// 定义的流程节点
			for(var k=0; k < wpNodes.length; k++){
			   // 添加【配件流程节点显示】
				TrainWorkPlanForLeafNode.parentNodes.add({
					id : wpNodes[k].idx,
					label : wpNodes[k].wpNodeName + "\n" +  wpNodes[k].startTime + "~"+ wpNodes[k].endTime,
					shape: wpNodes[k].showFlag,
					size:20,
				    color:colorD,
	   	     		x:	-50 + k*100,
					y: y,
//					fixed:true,			
					value: TrainWorkPlanForLeafNode.value,
					font: {size:10,strokeWidth: 3,'face': 'Monospace', align: 'left'}
				});
				// 如果为流程中的第一个节点时则从配件名称开始指向节点
				 var fromwpNodesID = '';
				 if(null == part.preWPNodeIDX){ 
					 	fromwpNodesID = part.partsIDX;
				 } else{
				 	fromwpNodesID = part.preWPNodeIDX;
				 }
				TrainWorkPlanForLeafNode.edges.add({
					  from: fromwpNodesID,
					  label: k+1,
					  length:80,
					  smooth:false,
	       			  to: wpNodes[k].idx, arrows:'to'			
				});
			}
			// 最后一个节点时指向上车节点
			for(var k=0; k < wpNodes.length; k++){
				 var towpNodesID = '';
				 if(null == part.nextWPNodeIDX){ 
					 	towpNodesID = part.onNodeCaseIDX;
				 } else{
				 	towpNodesID = part.idx;
				 }
				TrainWorkPlanForLeafNode.edges.add({
						  from: wpNodes[k].idx,
						  label: '',
						  smooth:false,
		       			  to: towpNodesID, arrows:'to'			
					});	
				}
			}

	/**  添加配件流程显示节点  */		
	TrainWorkPlanForLeafNode.addwpNodeContentFn = function(offPartList) {
		var partsLen = offPartList.length;
		for (var i = 0; i < partsLen/2; i++) {
			TrainWorkPlanForLeafNode.addOffPartListContentFn(offPartList,i,1,partsLen);
		 }
		// 流程中的配件另一部分显示在节点下方
		if(partsLen > 1){
			for (var i = partsLen/2+1; i < partsLen; i++) {
				TrainWorkPlanForLeafNode.addOffPartListContentFn(offPartList,i,2,partsLen);
			}
		}
	}
	/** 添加机车流程显示内容及流程线 */	
	TrainWorkPlanForLeafNode.addFlowSheetContentFn = function(workPlan) {
		var workPlanId = workPlan.get('idx');
		var fisrtNodes = workPlan.get('fisrtNodes');
		var offPartList = workPlan.get('offPartList');
		for (var i = 0; i < fisrtNodes.length; i++) {
			var node = fisrtNodes[i];								
			// 根据任务完成状态设置不同的显示样式
			var colorD = "";
			if (node.status == NODE_STATUS_WKG) {
			colorD = status_wkg;
			} else if (node.status == NODE_STATUS_YKG) {
				colorD = status_ykg;
			} else {
				colorD = status_ywg;
			}
			if (node.delayCount> 0 && node.status != NODE_STATUS_YWG) {
				colorD = status_yyq;
			}						
//			var shapeD = 'diamond';  // 凌形
//			if(i==0){
//				shapeD = 'square'
//			}else if(i == fisrtNodes.length-1){
//				shapeD = 'dot'
//			}
			// 添加【父节点】
			TrainWorkPlanForLeafNode.parentNodes.update({
				id : node.idx,
				label :  i + 1 + "、" + TrainWorkPlanForLeafNode.formatParentNodeContentFn(node, true),
				shape: node.showFlag,
				size:20,
			    color:colorD,
//			    image:  "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(TrainWorkPlanForLeafNode.formatParentNodeContentFn(parentNode,items, true)),
				x:i*200-400,	
				y:0,
				fixed:true,			
				value: TrainWorkPlanForLeafNode.value,
				length:130,
				title :  TrainWorkPlanForLeafNode.formatParentNodeTitleFn(node)
				, font: {size:15, strokeWidth: 3,'face': 'Monospace', align: 'left'}
			});
			TrainWorkPlanForLeafNode.edges.add({
				  from:node.preNodeIDX ,
				  label: i, smooth:false,
       			  to:node.idx, arrows:'to'
			});			
		}	
//		for(var j = 0; j < fisrtNodes.length-1; j++) {
//			var groupId1 = fisrtNodes[j].idx;
//			var groupId2 = fisrtNodes[j+1].idx;			
//			TrainWorkPlanForLeafNode.edges.add({
//				  from: groupId1,
//				  label: j+1, smooth:false,
//       			  to: groupId2, arrows:'to'
//			});			
//		}
		// 添加配件流程显示节点
		TrainWorkPlanForLeafNode.addwpNodeContentFn(offPartList);
	}
	
	// 如果作业流程节点未设置计划开始时间或者计划结束时间的提示信息
	TrainWorkPlanForLeafNode.errorInfo = function(parentNode, node) {
		var trainInfo = [];
		trainInfo.push(parentNode.get('nodeName'));			// 车型名称
		return trainInfo.join('') + " - " + node.nodeName;
	}
	
	// 数据加载完成后的函数处理
	TrainWorkPlanForLeafNode.storeLoadFn = function(store, records, options ) {
//		var count = this.getCount();
		var workPlan = this.getAt(0);
		TrainWorkPlanForLeafNode.idx = workPlan.get('idx');
		TrainWorkPlanForLeafNode.addFlowSheetContentFn(workPlan);			
		// 声明一个network对象实例
		if (!TrainWorkPlanForLeafNode.network) {
			TrainWorkPlanForLeafNode.network = new vis.Network(
				document.getElementById('visualization'),  
				TrainWorkPlanForLeafNode.data, 				
				TrainWorkPlanForLeafNode.options
			);
		}
		// 点击事件
     	TrainWorkPlanForLeafNode.network.on("selectNode", function(params, boolens) {
	        if (params.nodes.length == 1) {
	                TrainWorkPlanForLeafNode.getPartNodeEntity(params.nodes[0], TrainWorkPlanForLeafNode.idx );
		
	            }
		});
		
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义数据容器开始 **************** */
	TrainWorkPlanForLeafNode.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/trainWorkPlanEditNew!getFirstNodeListByRdpIDX.action',
		fields : [
			"idx", "fisrtNodes","offPartList"
//			"processIDX", "nodeIDX", "workPlanIDX",
//			"nodeName", "nodeDesc", 
//			"ratedWorkMinutes", "realWorkMinutes", 
//			"planBeginTime", "planEndTime", 
//			"realBeginTime", "realEndTime", "status","parentIDX","workCalendarIDX","planMode","leafNodes","applyCount","delayCount"
		],
		sortInfo : {
			field : 'idx',
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
					searchParams.idx = records[0].data.idx;			
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
				}, '-', {
				text : '批量派工',				
				iconCls:"chart_organisationIcon",
		    	handler: function(){
		    		LeafNodeListGrid.updateWorkCardForDispatchByRepairLine();
		    	}}, '->', '图例：', '-', {
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
				}],			
				html : [
					'<div id= "visualization">',
					'</div>'
				].join("")				
//				bbar: TrainWorkPlanForLeafNode.createPagingToolbarFn()
			
	});
	
		/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
//	var timerTask = new Ext.util.DelayedTask(function() {
//		Ext.TaskMgr.start({
//			run : function() {
//				// 加载数据
//				TrainWorkPlanForLeafNode.store.reload();
//			},
//			interval : 1000 * 60 * 10		// 每10分钟更新一次
//		});
//	});
//	// 延迟30秒
//	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */
});