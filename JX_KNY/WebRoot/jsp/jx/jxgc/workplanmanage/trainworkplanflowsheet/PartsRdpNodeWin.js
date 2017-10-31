/**
 * 机车检修作业编辑超链接中的下级节点grid
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpNodeWin'); 
	PartsRdpNodeWin.labelWidth = 90;
	PartsRdpNodeWin.fieldWidth = 150;
	PartsRdpNodeWin.partNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	PartsRdpNodeWin.edges = new vis.DataSet();
	PartsRdpNodeWin.network = null;
	/** **************** 条件定义开始 **************** */
	PartsRdpNodeWin.options = {
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
	PartsRdpNodeWin.data = {		
		 nodes: PartsRdpNodeWin.partNodes,
         edges: PartsRdpNodeWin.edges
	};	
	
   PartsRdpNodeWin.getPartNodeEntity = function(nodeIDX){
   		if (!Ext.isEmpty(nodeIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/partsRdpNode!getModelByIDX.action',
		        params: {idx: nodeIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                PartsRdpNodeWin.nodeEntity = result.entity;
		                PartsRdpRecordCardSheet.record =  PartsRdpNodeWin.nodeEntity.partsRdp;
		                PartsRdpRecordCardSheet.record.nodeName = PartsRdpNodeWin.nodeEntity.wpNodeName;
		                PartsRdpRecordCardSheet.record.nodeIDX = PartsRdpNodeWin.nodeEntity.idx;
						PartsRdpRecordCardSheet.win.show();
		            }else {
		            	 Ext.Msg.alert('提示', "未找到节点的检修记录！");
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
   }

	// 双击事件 
	PartsRdpNodeWin.doubleClickFn = function(){
		if (PartsRdpNodeWin.network) {
		 	PartsRdpNodeWin.network.on("doubleClick", function(params, boolens) {
		        if (params.nodes.length == 1) {
		            PartsRdpNodeWin.getPartNodeEntity(params.nodes[0]);
		        }
			});
		}
	}
	/** **************** 条件定义结束 **************** */
	// 清空数据集
	PartsRdpNodeWin.clearFn = function() {
		PartsRdpNodeWin.partNodes.clear();
		PartsRdpNodeWin.edges.clear();
	}
	// 格式化字体标题显示长度
   PartsRdpNodeWin.formatContentLenFn = function(temp, len){
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
   // 格式化标题
	PartsRdpNodeWin.formatPartsTitleFn = function(node){
		var title = [];
		title.push("<p>名称：" + node.partsName);
		title.push("规格型号：" + node.specificationModel);
		title.push("配件编号：" + node.partsNo);
		title.push("配件识别码 ：" + Ext.isEmpty(node.identificationCode)?"":node.identificationCode);		
		return title.join('<br></p>');  // 换行
	}
		
	// 格式化配件流程节点信息label的显示内容
	PartsRdpNodeWin.formatPartsNodeLabelFn = function(node) {
		var content = PartsRdpNodeWin.formatContentLenFn(node.wpNodeName,4); 			//节点简称		
		// 计划
		var planStartTime = new Date(node.planStartTime);
		var planEndTime = new Date(node.planEndTime);
		// 计划时间显示
	 	if (planStartTime && planEndTime) {
//			content += "\n" + planStartTime.format('m-d H:i')+"~";
			content += planEndTime.format('Y-m-d');	
		}
		return content;
	}
	// 格式化配件检修计划-流程节点在network上的title信息
	PartsRdpNodeWin.formatNodeTitleFn = function(node) {
		var title = [];
		// 名称
		title.push("<P>名称：" + node.wpNodeName);
		// 工位
	    if(!Ext.isEmpty(node.handleOrgName)){	    	
			title.push("处理班组：" + node.handleOrgName);
		 }
		// 工期
		title.push("工期：" + formatTimeForHours(node.ratedPeriod, 'm'));
		// 计划
		title.push("计划：" + new Date(node.planStartTime).format('m-d H:i') + " ~ " + new Date(node.planEndTime).format('m-d H:i'));
		// 实际
		if (!Ext.isEmpty(node.realBeginTime)) {
			var realTime = "实际：" + new Date(node.realStartTime).format('m-d H:i') + " ~ ";
			if (!Ext.isEmpty(node.realEndTime)) {
				realTime += new Date(node.realEndTime).format('m-d H:i');
			}
			title.push(realTime);
		}
	
		// 状态
		if (node.status == PARTS_NODE_STATUS_WKG) {
			title.push("状态：未开工");
		} else if (node.status == PARTS_NODE_STATUS_YKG) {
			title.push("状态：已开工");
		} else if (node.status == PARTS_NODE_STATUS_YWG){
			title.push("状态：已完工");
		}
		return title.join('<br></p>');
	}
	// 添加单个配件的流程
	PartsRdpNodeWin.showFlowWin = function(object){
		PartsRdpNodeWin.clearFn();
		for(var j = 0; j< object.length; j++){
			var entityList = object[j];
			for (var i = 0; i < entityList.length; i++) {
				var node = entityList[i];	
				if(0==i){		
					// 添加【配件】
					PartsRdpNodeWin.partNodes.update({
						id : j + 1 +'_' + node.partsTypeIDX,
						label :  j + 1 + "、" + node.partsName,
						shape: 'box',
						size:25,
//					    color:'gray',
						x: -600,	
						y: j*200-200,
						fixed:true,			
						
						length:80,
						title :  PartsRdpNodeWin.formatPartsTitleFn(node),
						font: {size:20, face: 'Monospace', align: 'left'}
					});
				}
							
				// 根据任务完成状态设置不同的显示样式
				var colorD = "";
				if (node.status == PARTS_NODE_STATUS_WKG) {
					colorD = status_wkg;
				} else if (node.status == PARTS_NODE_STATUS_YKG) {
					colorD = status_ykg;
				} else if (node.status == PARTS_NODE_STATUS_YWG) {
					colorD = status_ywg;
				}
				// 如果为空则设为圆点形
				var showFlag = (null==node.showFlag)? 'dot' : node.showFlag;
				// 添加【配件流程节点】
				PartsRdpNodeWin.partNodes.update({
					id : node.idx,
					label :  i + 1 + "、" + PartsRdpNodeWin.formatPartsNodeLabelFn(node),
					shape: showFlag,
					size:25,
				    color: colorD,
					x: i*150-400,	
					y: j*200-200,
					fixed:true,			
//					value: 3,
					length:80,
					title :  PartsRdpNodeWin.formatNodeTitleFn(node),
					font: {face: 'Monospace', align: 'left',size:15}
				});
				var formID = (null == node.preNodeIDX)?  j + 1 +'_' + node.partsTypeIDX : node.preNodeIDX;
				PartsRdpNodeWin.edges.add({
					  from: formID,
					  label: i+1, smooth:false,
	       			  to: node.idx, arrows:'to'
				});			
			}
		}
		// 打开配件详情流程窗口
		PartsRdpNodeWin.win.show();
	}
	
	PartsRdpNodeWin.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "配件检修流程图", border: false, layout: "fit", buttonAlign : "center",
	            items: [{
		            layout: "fit",bodyStyle:'padding-left:20px;', frame: true, buttonAlign : "center",
		            height: 200, bodyBorder: false,
					html : [
						'<div id= "partsRdpNodeNetWork">',
						'</div>'
					].join(""),	
			        buttons: [{
				        	 text: "关闭", iconCls: "closeIcon", handler: function(){ PartsRdpNodeWin.win.hide(); }
					    }]	
		        	}]
   			 }]
	});
	PartsRdpNodeWin.win = new Ext.Window({
	    title:"详情", width: 1000, height:500, maximized : false, layout: 'fit',
	    plain:true, closeAction:"hide", buttonAlign : "center",
	    items:PartsRdpNodeWin.tabs, modal:true,
		listeners: {
			beforeshow: function(window) {
//				PartsRdpNodeWin.network.destroy();
				// 声明一个network对象实例
				if (!PartsRdpNodeWin.network) {
					PartsRdpNodeWin.network = new vis.Network(
						document.getElementById('partsRdpNodeNetWork'),  
						PartsRdpNodeWin.data, 				
						PartsRdpNodeWin.options
					);
				}
				PartsRdpNodeWin.doubleClickFn();
			}
		}
	});


});