/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('TrainWorkPlanFlowsheet');	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanFlowsheet.args = [];
	TrainWorkPlanFlowsheet.trainInfo = [];
	TrainWorkPlanFlowsheet.workPlanStatus = '';
	TrainWorkPlanFlowsheet.idx ='';
	TrainWorkPlanFlowsheet.parentNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	TrainWorkPlanFlowsheet.edges = new vis.DataSet();
	TrainWorkPlanFlowsheet.leafNodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	TrainWorkPlanFlowsheet.network = null;					// 时间轴network对象
	TrainWorkPlanFlowsheet.planSuffixChar = '-';			// 计划对比数据主键字符后缀
	TrainWorkPlanFlowsheet.size = 20;			// 流程显示形状大小设置

	TrainWorkPlanFlowsheet.start = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate())
	
	/** **************** 定义全局变量结束 **************** */

	/** **************** 条件定义开始 **************** */
	TrainWorkPlanFlowsheet.options = {
		 //        ,layout: {
//                hierarchical: {
//                    direction: 'LR'
//                }
//            }
 };

	TrainWorkPlanFlowsheet.data = {		
		 nodes: TrainWorkPlanFlowsheet.parentNodes,
         edges: TrainWorkPlanFlowsheet.edges
	};		
	/** **************** 条件定义结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */	
   TrainWorkPlanFlowsheet.getNodeEntity = function(nodeIDX, workPlanIDX){
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
						var status = NODE_STATUS_YWG;
						TrainWorkPlanFirstNodeEditWin.showWin(TrainWorkPlanFlowsheet.args.join(","),nodeIDX, parentIdx, isLeaf, nodeName, workPlanIDX, TrainWorkPlanFlowsheet.workPlanStatus, status);
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
   }
   TrainWorkPlanFlowsheet.getPartNodeEntity = function(partsOrNodeIDX, wpIDX, workPlanIDX ){
   		if (!Ext.isEmpty(partsOrNodeIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/partsRdpNode!getEntityByPartIDX.action',
		        params: {partsTypeIDX: partsOrNodeIDX, wpIDX : wpIDX, workPlanIDX: workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null && result.entity.length > 0) {		            	
		                nodeEntity = result.entity;
						PartsRdpNodeWin.showFlowWin(nodeEntity);
		            }else {
		            	 TrainWorkPlanFlowsheet.getNodeEntity(partsOrNodeIDX, workPlanIDX);
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
   }

   // 格式化字体标题显示长度
   TrainWorkPlanFlowsheet.formatContentLenFn = function(temp, len){
        var content = temp  + "\n";
   		// 每行显示len个字
		if(temp.length > len){
			var i = 0;
			for(; i < parseInt(temp.length/len); i++){
				content = temp.substring(i*len, i*len + len) + "\n"; 	
			}
			content +=  temp.substring(i*len, i*len + temp.length%len) + "\n";
		}
		return content;
   }
	// 清空数据集
	TrainWorkPlanFlowsheet.clearFn = function() {
		TrainWorkPlanFlowsheet.parentNodes.clear();
		TrainWorkPlanFlowsheet.edges.clear();
	}
		
	// 格式化机车流程节点信息label的显示内容
	TrainWorkPlanFlowsheet.formatParentNodeContentFn = function(parentNode,isPlan) {
		//节点简称
		var content = TrainWorkPlanFlowsheet.formatContentLenFn(parentNode.nodeName, 4);
		// 计划
		var planBeginTime = new Date(parentNode.planBeginTime);
		var planEndTime = new Date(parentNode.planEndTime);
		// 实际
		var beginTime, endTime;
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);
		if (parentNode.realBeginTime) realBeginTime = new Date(parentNode.realBeginTime);		
		// 计划时间显示
	 	if (planBeginTime && planEndTime) {
//			content += "\n" + planBeginTime.format('m-d H:i') + "~" ;
			content +=  planEndTime.format('Y-m-d') + "\n";	
		}
		return content;
	}
	
	// 格式化配件流程节点信息label的显示内容
	TrainWorkPlanFlowsheet.formatwpNodeLabelFn = function(wpNode) {
		var content = TrainWorkPlanFlowsheet.formatContentLenFn(wpNode.wpNodeName, 5);; 			//节点简称		
		content +=  wpNode.startTime + "~"+ wpNode.endTime;
		return content;
	}
	
	
	// 格式化机车检修作业计划-流程节点在network上的title信息
	TrainWorkPlanFlowsheet.formatParentNodeTitleFn = function(parentNode) {
		var planBeginTime = new Date(parentNode.planBeginTime);		// 计划开始时间
		var planEndTime = new Date(parentNode.planEndTime);			// 计划结束时间
		var title = [];
		title.push("<p>名称：" + parentNode.nodeName);
		// 工位
	    if(!Ext.isEmpty(parentNode.workStationName)){	    	
			title.push("工位：" + parentNode.workStationName);
		 }
		// 工期
		title.push("工期：" + formatTimeForHours(parentNode.ratedWorkMinutes, 'm'));
//		title.push("耗时：" + formatTimeForHours((planEndTime - planBeginTime) / 60 / 1000 , 'm'));
		title.push("开始时间：" + planBeginTime.format('Y-m-d H:i'));
		title.push("结束时间：" + planEndTime.format('Y-m-d H:i'));
		// 实际
		if (!Ext.isEmpty(parentNode.realBeginTime)) {
			var realTime = "实际：" + new Date(parentNode.realBeginTime).format('m-d H:i') + " ~ ";
			if (!Ext.isEmpty(parentNode.realEndTime)) {
				realTime += new Date(parentNode.realEndTime).format('m-d H:i');
			}
			title.push(realTime);
		}
		if(parentNode.applyCount>0)
			title.push("延期申请节点数：" + parentNode.applyCount);
		if(parentNode.delayCount > 0){
			title.push("超时节点数：" + parentNode.delayCount);
		}
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
	TrainWorkPlanFlowsheet.addOffPartListContentFn = function(offPartList,i, flag, partsLen){
			var part = offPartList[i];	
			// 根据任务完成状态设置不同的显示样式					
			var shapeD = 'text';  // 文本框
			var layout_y =  i*100+100;
			if(flag == 2){
				layout_y =  -(i-parseInt(partsLen/2))*100 - 100;
			}
			var layout_x = -150;
			var partsID = part.rowNum + TrainWorkPlanFlowsheet.planSuffixChar  + part.partsTypeIdx + TrainWorkPlanFlowsheet.planSuffixChar + part.wpIDX;
			// 添加【流程中配件名称显示】
			TrainWorkPlanFlowsheet.parentNodes.update({
				id : partsID,
				label : part.partsName,
				shape: shapeD,
				size:20,
				x: layout_x,
				y: layout_y,
				fixed:true,			
				value: TrainWorkPlanFlowsheet.value,
				title : "双击配件查看【" + part.partsName + "(" + part.specificationModel + ")】详情",
				font: {strokeWidth: 1,'face': 'Monospace', align: 'left'}
			});
			// 流程中的配件时从 下车节点 指向 配件
			var fromID = part.offNodeCaseIDX;
			TrainWorkPlanFlowsheet.edges.add({
			 	 from: fromID,
			 	 label: '', length:100, smooth:false,
   				 to: partsID, arrows:'to'			
			});
			if(i>0){
				var prePartsID = offPartList[i-1].rowNum + TrainWorkPlanFlowsheet.planSuffixChar + offPartList[i-1].partsTypeIdx + TrainWorkPlanFlowsheet.planSuffixChar + offPartList[i-1].wpIDX;
			 	TrainWorkPlanFlowsheet.edges.add({
				 	 from: prePartsID ,
				 	 label: '', length:100, smooth:false,
       				 to: partsID		
				});
			 }
			 // 定义的流程节点
			var wpNodes = part.wpNodes;
			var cou = 1;
			for(var k=0; k < wpNodes.length; k++){
				var wpNode = wpNodes[k];
				var colorD = "";
				if (wpNode.status == PARTS_NODE_STATUS_WKG) {
					colorD = status_wkg;
				} else if (wpNode.status == PARTS_NODE_STATUS_YKG) {
					colorD = status_ykg;
				} else if (wpNode.status == PARTS_NODE_STATUS_YWG) {
					colorD = status_ywg;
				}
			    // 添加【配件定义的流程节点显示】
				TrainWorkPlanFlowsheet.parentNodes.update({
					id : i + TrainWorkPlanFlowsheet.planSuffixChar  + wpNode.idx,
					label : TrainWorkPlanFlowsheet.formatwpNodeLabelFn(wpNode),
					shape: null == wpNode.showFlag?'dot':wpNode.showFlag,
					font: {size:12, face: 'arial', align: 'left'},
					size: TrainWorkPlanFlowsheet.size-5,
				    color: colorD,
	   	     		x:	layout_x + 50 + k*100,
					y: layout_y,	
					title : "<p>节点名称：" +  wpNode.wpNodeName  + wpNode.countStr+ "</p>",
					fixed:true		
				});
				// 如果为流程中的第一个节点时则从 配件 开始指向流程节点
				 if(null == wpNode.preWPNodeIDX){ 
					 	TrainWorkPlanFlowsheet.edges.update({
							  from: partsID,
//							  label: k,
//							  length:80,
							  smooth:false,
			       			  to: i + TrainWorkPlanFlowsheet.planSuffixChar  + wpNode.idx, arrows:'to'			
						});
				 }else if(null != wpNode.preWPNodeIDX){
					TrainWorkPlanFlowsheet.edges.update({
						  from: i +  TrainWorkPlanFlowsheet.planSuffixChar + wpNode.preWPNodeIDX,
						  label: cou++,
//						  length:80,
						  smooth:false,
		       			  to: i + TrainWorkPlanFlowsheet.planSuffixChar  + wpNode.idx, arrows:'to'			
					});
				}
				// 如果为流程中的最后一个节点时则从 流程节点 开始指向 上车节点
				var towpNodesID = part.onNodeCaseIDX;
				if(null == wpNode.nextWPNodeIDX){ 
					TrainWorkPlanFlowsheet.edges.update({  
						 from: i + TrainWorkPlanFlowsheet.planSuffixChar  + wpNode.idx,
					 	 label: cou,
//					 	 length:80,
					 	 smooth:false,
	       			 	 to: towpNodesID, arrows:'to'	
					});
				}
			}
		}


	/**  添加配件流程显示节点  */		
	TrainWorkPlanFlowsheet.addwpNodeContentFn = function(offPartList) {
		var partsLen = offPartList.length;
		for (var i = 0; i < parseInt(partsLen/2); i++) {
			TrainWorkPlanFlowsheet.addOffPartListContentFn(offPartList,i,1,partsLen);
		 }
		// 流程中的配件另一部分显示在节点下方
		if(partsLen >= 1){
			for (var i = parseInt(partsLen/2); i < partsLen; i++) {
				TrainWorkPlanFlowsheet.addOffPartListContentFn(offPartList,i,2,partsLen);
			}
		}
	}
	/** 添加机车流程显示内容及流程线 */	
	TrainWorkPlanFlowsheet.addFlowSheetContentFn = function(workPlan, clientWidth) {
		var workPlanId = workPlan.get('idx');
		var fisrtNodes = workPlan.get('fisrtNodes');
		var offPartList = workPlan.get('offPartList');
		var nodeLen = fisrtNodes.length;
		if(0== nodeLen) return;
		var planBeginTime = new Date(fisrtNodes[0].planBeginTime);  // 取第一个节点的开始时间
		var showFromDate = new Date(planBeginTime.getYear(), planBeginTime.getMonth(), planBeginTime.getDate()-1);
		var timeline_y = (offPartList.length/2) *100+100;  // 取配件的纵坐标作为时间轴的坐标
		TrainWorkPlanFlowsheet.parentNodes.update({
			id : showFromDate.toString(), font: {multi: 'md', size:13, align: 'left'},
			label : '',
			shape: 'text',
			size: TrainWorkPlanFlowsheet.size,
		   	x: -clientWidth/nodeLen-600 + 50,	// 对应时间轴上的坐标错位显示
			y: timeline_y,
			fixed:true,			
			length:150
		});
		for (var i = 0; i < nodeLen; i++) {
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
			var showFlag = null==node.showFlag? 'dot' : node.showFlag;
			// 添加【机车检修流程父节点】
			TrainWorkPlanFlowsheet.parentNodes.update({
				id : node.idx, font: {multi: 'md', size:13, align: 'left'},
				label :  i + 1 + "、" + TrainWorkPlanFlowsheet.formatParentNodeContentFn(node, true),
				shape: showFlag,
				size: TrainWorkPlanFlowsheet.size,
			    color:colorD,
				x:i*clientWidth/nodeLen-600,	
				y:0,
				fixed:true,			
				length:150,
				title :  TrainWorkPlanFlowsheet.formatParentNodeTitleFn(node)
			});
			TrainWorkPlanFlowsheet.edges.add({
				  from: node.preNodeIDX ,
				  label: i, smooth:false,
       			  to: node.idx, arrows:'to'
			});	
			// 添加时间轴显示
			var planEndTime = new Date(node.planEndTime);
			var showDate = new Date(planEndTime.getYear(),planEndTime.getMonth(),planEndTime.getDate());
			var day = parseInt((showDate - showFromDate)/(24*60*60*1000));
			TrainWorkPlanFlowsheet.parentNodes.update({
				id : showDate.toString(), font: {multi: 'md', size:13, align: 'left'},
				label : "第\n" + day + '\n天',
				shape: 'text',
				size: TrainWorkPlanFlowsheet.size,
				x:i*clientWidth/nodeLen-600 + 50,	
				y: timeline_y,
				fixed:true,			
				length:150
			});
			TrainWorkPlanFlowsheet.edges.add({
				  from: showFromDate.toString() ,
				  label: '', smooth:false,
       			  to: showDate.toString(), arrows:{
							      to: {
							        type: 'circle'
							      }}
			});		
			if( i== nodeLen-1){
				var toShowDate = new Date(planEndTime.getYear(),planEndTime.getMonth(),planEndTime.getDate()+1);
				TrainWorkPlanFlowsheet.parentNodes.update({
					id : toShowDate.toString(), font: {multi: 'md', size:13, align: 'left'},
					label : '',
					shape: 'text', 
					size: TrainWorkPlanFlowsheet.size,
					x: nodeLen*clientWidth/nodeLen-600 + 50,	
					y: timeline_y,
					fixed:true,			
					length:150
				});
				TrainWorkPlanFlowsheet.edges.add({
				 	 from: showDate.toString(),
				 	 label: '', smooth:false,
		   			 	 to: toShowDate.toString(), arrows: 'to'
				});	
			}
		}	
			
		// 添加配件流程显示节点
		TrainWorkPlanFlowsheet.addwpNodeContentFn(offPartList);
	}
	
	// 如果作业流程节点未设置计划开始时间或者计划结束时间的提示信息
	TrainWorkPlanFlowsheet.errorInfo = function(parentNode, node) {
		var trainInfo = [];
		trainInfo.push(parentNode.get('nodeName'));			// 车型名称
		return trainInfo.join('') + " - " + node.nodeName;
	}
	
	// 数据加载完成后的函数处理
	TrainWorkPlanFlowsheet.storeLoadFn = function(store, records, options ) {
		var workPlan = this.getAt(0);
		TrainWorkPlanFlowsheet.idx = workPlan.get('idx');
		var netWork = document.getElementById('visualization');
		var clientWidth = netWork.clientWidth;
		TrainWorkPlanFlowsheet.addFlowSheetContentFn(workPlan, clientWidth);	
		// 声明一个network对象实例
		if (!TrainWorkPlanFlowsheet.network) {
			TrainWorkPlanFlowsheet.network = new vis.Network(
				netWork,  
				TrainWorkPlanFlowsheet.data, 				
				TrainWorkPlanFlowsheet.options
			);
		}

		var nodeList = workPlan.get('fisrtNodes');
		
		// 设置配件的显示位置
		var offPartList = workPlan.get('offPartList');
		if( offPartList.length > 0 && !Ext.isEmpty(offPartList[0].offNodeCaseIDX)){
	   		 var layout = TrainWorkPlanFlowsheet.network.getPositions(offPartList[0].offNodeCaseIDX);
	   		 var  s = offPartList[0].offNodeCaseIDX
	   		 var  x = layout[s].x;
	   		 for(var i= 1; i < offPartList.length; i++){
	   		 	layout = TrainWorkPlanFlowsheet.network.getPositions(offPartList[i].offNodeCaseIDX)
	   		 	s = offPartList[i].offNodeCaseIDX
	   		 	if(x < layout[s].x)  x = layout[s].x;
	   		 }
	   		 for(var i= 0; i < offPartList.length; i++){
	   		 	 var partsID = offPartList[i].rowNum + TrainWorkPlanFlowsheet.planSuffixChar + offPartList[i].partsTypeIdx + TrainWorkPlanFlowsheet.planSuffixChar + offPartList[i].wpIDX;
		   		 TrainWorkPlanFlowsheet.network.clustering.updateClusteredNode(partsID, {x : x+50});
		   		 var wpNodes = offPartList[i].wpNodes;
		   		 for(var k=0; k< wpNodes.length; k++){
		   		 	 TrainWorkPlanFlowsheet.network.clustering.updateClusteredNode(i + TrainWorkPlanFlowsheet.planSuffixChar + wpNodes[k].idx, {x : x+130+k*100});
		   		 }
			}
		}
		
		// selectNode 点击事件 d
     	TrainWorkPlanFlowsheet.network.on("doubleClick", function(params, boolens) {
	        if (params.nodes.length == 1) {
	        	var array = params.nodes[0].split(TrainWorkPlanFlowsheet.planSuffixChar);
	        	var partsOrNodeIDX = array[0]; var wpIDX = '';
	        	if(array.length > 1) { 
	        		partsOrNodeIDX = array[1];
	        		wpIDX = array[2];
	        	}
                TrainWorkPlanFlowsheet.getPartNodeEntity(partsOrNodeIDX, wpIDX, TrainWorkPlanFlowsheet.idx );
            }
		});
		
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义数据容器开始 **************** */
	TrainWorkPlanFlowsheet.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/trainWorkPlanEditNew!getFirstNodeListByRdpIDX.action',
		fields : [
			"idx", "fisrtNodes","offPartList"
		],
		sortInfo : {
			field : 'idx',
			direction : 'ASC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : TrainWorkPlanFlowsheet.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},
			
			beforeload: function(store, options) {
//	       	 	if(!TrainWorkPlanFlowsheet.loadMask)   
//	       	 		TrainWorkPlanFlowsheet.loadMask = new Ext.LoadMask(Ext.getBody(), {
//	       	 			msg: '正在加载，请稍候32423 ...',
//	       	 			store: TrainWorkPlanFlowsheet.store
//	       	 		});
//       	 		TrainWorkPlanFlowsheet.loadMask.show();
   	 			TrainWorkPlanFlowsheet.clearFn(); // 清空数据集
   	 			PartsRdpNodeWin.clearFn(); // 清空数据集
				var searchParams = {};
				var sm = TrainWorkPlanRecords.trainTypeGrid.getSelectionModel();
				if (sm.getCount() > 0) {
					var records = sm.getSelections();
					searchParams.idx = records[0].data.idx;			
					var args = [];
					// 延期原因页面的设置参数
					TrainWorkPlanFlowsheet.trainInfo.push(records[0].data.idx);
					TrainWorkPlanFlowsheet.trainInfo.push(records[0].data.trainTypeShortName);
					TrainWorkPlanFlowsheet.trainInfo.push(records[0].data.trainNo);
					TrainWorkPlanFlowsheet.trainInfo.push(records[0].data.repairClassName);
					TrainWorkPlanFlowsheet.trainInfo.push( records[0].data.repairtimeName);
					TrainWorkPlanFlowsheet.trainInfo.push( records[0].data.dNAME);
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
					TrainWorkPlanFlowsheet.workPlanStatus = records[0].data.workPlanStatus;
					TrainWorkPlanFlowsheet.args = args;
				}		
			
				searchParams = MyJson.deleteBlankProp(searchParams);
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});

	/** **************** 定义数据容器结束 **************** */
	//机车详情页面
	TrainWorkPlanFlowsheet.TrainRecordPanel =  new Ext.Panel({		
				region: 'center',
				border: false, autoScroll : false, anchor:'100%',
				tbar: [{
					text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						self.location.reload();
					}			
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
				}],			
				html : [
					'<div id= "visualization">',
					'</div>'
				].join("")				
//				bbar: TrainWorkPlanFlowsheet.createPagingToolbarFn()
			
	});
	
		/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
//	var timerTask = new Ext.util.DelayedTask(function() {
//		Ext.TaskMgr.start({
//			run : function() {
//				// 加载数据
//				TrainWorkPlanFlowsheet.store.reload();
//			},
//			interval : 1000 * 60 * 10		// 每10分钟更新一次
//		});
//	});
//	// 延迟30秒
//	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */
});