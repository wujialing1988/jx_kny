/**
 * 车辆检修履历
 */
Ext.onReady(function(){
	
	Ext.namespace('TrianWorkPlanRecord'); 
	
	TrianWorkPlanRecord.trainTypeIDX = '###' ; // 车型 
	TrianWorkPlanRecord.trainNo = '###' ; // 车号
	
	TrianWorkPlanRecord.tree = new Ext.tree.TreePanel({
		root: new Ext.tree.AsyncTreeNode({
			text: "车辆履历",
	        id: "ROOT_0",
	        expanded: true
		}),
		rootVisible: false,
		autoScroll:true,
		animate:true,
		border: false,
		listeners:{
			"click": function(node){
				if(node.leaf){
					// 10 检修记录 20故障记录 30 下车配件  40 上车配件 50列检作业情况 60列检故障登记
					var type = node.attributes.type ;
					var rdpIdx = node.attributes.parentID ;
					var url = "" ;
					if("10" == type){
						var recordIDX = node.attributes.repairProjectIDX ;
						var idx = node.id ;
						Ext.Ajax.request({
							url: ctx + '/printerModule!getModelForPreview.action',
							params:{
								businessIDX: recordIDX
							},
							//请求成功后的回调函数
						    success: function(response, options){
						        if(self.loadMask)    self.loadMask.hide();
						        var result = Ext.util.JSON.decode(response.responseText);
						        if (result.errMsg == null) {       //操作成功     
						        	var entity = result.entity
						        	var deployCatalog = entity.deployCatalog;		// 报表部署目录
									var displayName = entity.displayName;			// 报表显示名称
									var deployName = entity.deployName;				// 报表部署名称
									while(deployCatalog.indexOf('.') >= 0) {
										deployCatalog = deployCatalog.replace('.', '/');
									}
									var reportUrl = "/" + deployCatalog + "/" + deployName;
									var url = reportUrl + "?ctx=" + ctx.substring(1);
									var dataUrl = reportUrl + "&idx=" + idx;		// 机车检修记录单实例idx主键
				                	var reportUrl = ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI(displayName);
				                	document.getElementById("report").innerHTML = "<iframe style='width:100%;height:100%;overflow:auto;' frameborder='0' src=" + reportUrl + "></iframe>";
						        } else {                           //操作失败
						            alertFail(result.errMsg);
						        }
						    },
						    //请求失败后的回调函数
						    failure: function(response, options){
						        if(self.loadMask)    self.loadMask.hide();
						        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						    }
						});
						
					}else if("20" == type){
						url = "/kny/dzll/faultTicketDetail.cpt&rdpIdx="+rdpIdx;
					}else if("30" == type){
						url = "/kny/dzll/partsUnloadRegister.cpt&rdpIdx="+rdpIdx;
					}else if("40" == type){
						url = "/kny/dzll/partsFixRegister.cpt&rdpIdx="+rdpIdx;
					}else if("50" == type){
						url = "/kny/dzll/zbglRdpPlanRecordInfo.cpt&rdpIdx="+rdpIdx;
					}else if("60" == type){
						url = "/kny/dzll/zbglRdpPlanRecordGztp.cpt&rdpIdx="+rdpIdx;
					}else if("00" == type){
						var infoUrl = ctx+"/jsp/freight/dzll/hc/trainInfo.jsp?trainTypeIDX="+TrianWorkPlanRecord.trainTypeIDX+"&trainNo="+TrianWorkPlanRecord.trainNo
						+"&vehicleType="+vehicleType;
						document.getElementById("report").innerHTML = "<iframe style='width:100%;height:100%;overflow:auto;' frameborder='0' src=" + infoUrl + "></iframe>";
						
					}
					// 刷新报表
					if(url && '10' != type && '00' != type){
						var reportUrl = getReportEffectivePath(url);
						document.getElementById("report").innerHTML = "<iframe style='width:100%;height:100%;overflow:auto;' frameborder='0' src=" + reportUrl + "></iframe>";
					}
				}
			},
			beforeload:  function(node){
				TrianWorkPlanRecord.tree.loader.dataUrl = ctx + '/trainRecord!findWorkPlanTree.action?&trainTypeIDX='+TrianWorkPlanRecord.trainTypeIDX+'&trainNo='+TrianWorkPlanRecord.trainNo
				+'&vehicleType='+vehicleType;
			},
			load:function(node){
				if(node.id == 'ROOT_0' && node.childNodes.length > 0){
					var path = node.childNodes[0].getPath();
					TrianWorkPlanRecord.tree.selectPath(path);
					var infoUrl = ctx+"/jsp/freight/dzll/hc/trainInfo.jsp?trainTypeIDX="+TrianWorkPlanRecord.trainTypeIDX+"&trainNo="+TrianWorkPlanRecord.trainNo
				+"&vehicleType="+vehicleType;
					document.getElementById("report").innerHTML = "<iframe style='width:100%;height:100%;overflow:auto;' frameborder='0' src=" + infoUrl + "></iframe>";
				}
			}
		}
	});
	
	TrianWorkPlanRecord.tree.getSelectionModel().on('beforeselect', function(me, newNode, oldNode) {
		if(!newNode.leaf){
			return ;
		}
		newNode.setIconCls('tickIcon');
		if (oldNode && oldNode.leaf) {
			oldNode.setIconCls('groupCheckedIcon');
		}
	});
		

	// 车辆检修履历主面板 
	TrianWorkPlanRecord.mainPanel = new Ext.Panel({
		layout : 'border', border : false, 
		items : [{
			region: 'west',
			width : 250,
			minSize:200, 
			maxSize:500,
			bodyBorder: false, 
			split: true, 
			collapseMode: 'mini',
			layout:'fit',
			items :TrianWorkPlanRecord.tree
		},{
			id:'report',
			region:'center',
			border:false,
			layout:'fit'
		}],
		listeners: {
			render : function() {
				
			}
		}
	});
	
	
	TrianWorkPlanRecord.win = new Ext.Window({
		title:'车辆电子履历', maximized:true,
		layout:"fit", closeAction:"hide",
		items:TrianWorkPlanRecord.mainPanel,
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			
		}
	});
	
	/**
	 * 加载页面
	 */
	TrianWorkPlanRecord.load = function(trainTypeIDX,trainNo){
		TrianWorkPlanRecord.trainTypeIDX = trainTypeIDX ; // 车型 
		TrianWorkPlanRecord.trainNo = trainNo ; // 车号
		TrianWorkPlanRecord.tree.root.reload();
		TrianWorkPlanRecord.tree.expand(); //全部展开
//		TrianWorkPlanRecord.tree.expandAll(); //全部展开
	}
	
});