 /**
  * 车派人
  */
function dispatcher(){
	
	if(ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED){
		MyExt.Msg.alert("计划已完成！");
		return;
	}
	
	var smplan = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().getSelections();
	var smrecord = ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getSelectionModel().getSelections();
	if(smplan.length == 0){
		MyExt.Msg.alert("列检计划未选择！");
		return;
	}
	if(smrecord.length == 0){
		MyExt.Msg.alert("列检车辆未选择！");
		return;		
	}else if(smrecord.length == 1){
		WorkStationEmp.idx = smrecord[0].data.idx;
	}else{
		WorkStationEmp.idx = "" ;
	}
	
	// 作业班组
	var workTeamSeq = smplan[0].data.workTeamSeq ;
	WorkStationEmp.orgseq = workTeamSeq;
	//重新加载Grid
	WorkStationEmp.grid.store.load();
	WorkStationEmp.NoDispatchGrid.store.load();	
	WorkStationEmp.selectWin.show();	
}

/**
 * 人派车
 */
function disrecord(){
	
	if(ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED){
		MyExt.Msg.alert("计划已完成！");
		return;
	}
	
	var smplan = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().getSelections();
	var smperson = ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid.getSelectionModel().getSelections();
	if(smplan.length == 0){
		MyExt.Msg.alert("列检计划未选择！");
		return;
	}
	if(smperson.length == 0){
		MyExt.Msg.alert("作业人员未选择！");
		return;		
	}else if(smperson.length == 1){
		ZbglRdpPlanRecordWin.recordIds = smperson[0].data.recordIdxs ;
	}else{
		ZbglRdpPlanRecordWin.recordIds = "" ;
	}
	ZbglRdpPlanRecordWin.rdpPlanIdx = smplan[0].data.idx ;
	ZbglRdpPlanRecordWin.selectWin.show();
	ZbglRdpPlanRecordWin.grid.store.load();
}

/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {

	Ext.ns('ZbglRdpPlanRecord');
	
	ZbglRdpPlanRecord.rdpPlanStatus = "###" ; // 列检计划状态
	
	ZbglRdpPlanRecord.rdpPlanIdx = "###" ; // 列检计划ID
	
	ZbglRdpPlanRecord.railwayTime = "###" ; // 车次名称

	ZbglRdpPlanRecord.parentNodes = new vis.DataSet(); // 机车检修作业流程计划数据集对象

	ZbglRdpPlanRecord.edges = new vis.DataSet();

	ZbglRdpPlanRecord.network = null; // 时间轴network对象

	ZbglRdpPlanRecord.options = {
		groups : {
			usergroups : {
				shape : 'image',
				image : '1'
			},
			users : {
				shape : 'square'
			},
			dot : {
				shape : 'dot'
			},
			main : {
				main : "列车图",
				width : '100%'
			}
		}
//		layout : {
//			hierarchical : {
//				direction : 'LR'
//			}
//		}
	};

	ZbglRdpPlanRecord.data = {
		nodes : ZbglRdpPlanRecord.parentNodes,
		edges : ZbglRdpPlanRecord.edges
	};
	
	ZbglRdpPlanRecord.statusArrays = {"INITIALIZATION":"未处理","ONGOING":"处理中","COMPLETE":"已完成"} ;
	ZbglRdpPlanRecord.statusColorArrays = {"INITIALIZATION":"#999999","ONGOING":"#00BFFF","COMPLETE":"#008000"} ;
	
	
	// 填写车号窗口 this.baseParams.entityJson = Ext.util.JSON.encode(searchParams)
	ZbglRdpPlanRecord.writeTrainNoWin = new Ext.Window({
		title:"填写车号", width:400, height:250, plain:true, closeAction:"hide", buttonAlign:'center',padding:15,
    	maximizable:false,  modal:true,
    	items:[{
        	xtype: 'form',
        	defaultType: "textfield",
        	layout: "form",
        	baseCls: "x-plain",
            items: [
                { 
                	id:'writeTrainNo',
                	fieldLabel: '车号',
                	name:'trainNo',
                	allowBlank:false,
                	maxLength:20
                }
            ]
		}],
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				var idx = $yd.getSelectedIdx(ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid)[0];
				var writeTrainNo = Ext.getCmp("writeTrainNo").getValue();
				if(Ext.isEmpty(writeTrainNo)){
					return ;
				}
				var entityJson = {
					idx:idx,
					trainNo:writeTrainNo
				}
				Ext.Ajax.request({
					url: ctx + "/zbglRdpPlanRecord!writeTrainNo.action",
					params: {entityJson:Ext.util.JSON.encode(entityJson)},
					success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
							alertSuccess();
							Ext.getCmp("writeTrainNo").reset();
							ZbglRdpPlanRecord.writeTrainNoWin.hide();
							ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.reload();
						}else{
							alertFail(result.errMsg);
						}
					},
					failure: function(response, options){
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ ZbglRdpPlanRecord.writeTrainNoWin.hide(); }
		}]
	});	

	/**
	 * 列检计划列表
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid = new Ext.yunda.Grid({
				loadURL : ctx + "/zbglRdpPlanRecord!pageList.action",
				singleSelect : false,
				isEdit:true,
				tbar : [
				{
					text : '批量指派人员',		
					disabled:ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED,
					iconCls:"chart_organisationIcon",
			    	handler: function(){
			    		dispatcher();
			    	}
		    	},'-','<div><span style="color:green;">*双击填写车号</span></div>'
				],
				fields : [{
							header:'指派',hidden:ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED, dataIndex:'idx', editor: { xtype:'hidden' }, width:15, sortable:false,
							renderer:function(v,x,r){			
								return "<img src='" + img + "' alt='指派列检人员' style='cursor:pointer' onclick='dispatcher()'/>";
							}
						},{
							header : 'idx主键',
							dataIndex : 'idx',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						}, {
							header : '<div>编号<span style="color:green;">【车型车号】</span></div>',
							dataIndex : 'seqNum',
							editor : {
								fieldLabel:'编号',
								maxLength : 50
							},
							renderer: function(value, metaData, record, rowIndex, colIndex, store) {
								var trainTypeCode = Ext.isEmpty(record.data.trainTypeCode) ? "" : record.data.trainTypeCode ;
								var trainNo =  Ext.isEmpty(record.data.trainNo)?"":record.data.trainNo;
								var trainInfo = Ext.isEmpty(trainNo) ? "未录入" : trainTypeCode + " " + trainNo ;
								return "第"+value+"辆:【"+trainInfo+"】";
							},
							searcher : {
								anchor : '98%'
							}
						}, {
							header : '车辆车型',
							dataIndex : 'trainTypeName',
							hidden : true,
							editor : {
								maxLength : 50
							},
							searcher : {
								anchor : '98%'
							}
						},
						{header : '车辆编码',dataIndex : 'trainTypeCode',hidden : true,editor : { xtype:"hidden" }},
						{header : '车辆列检实例ID',dataIndex : 'rdpIdx',hidden : true,editor : { xtype:"hidden" }},
							{
							header : '车辆车号',
							dataIndex : 'trainNo',
							hidden : true,
							editor : {
								maxLength : 50
							},
							searcher : {
								anchor : '98%'
							}
						}, {
							header : '作业人员',
							dataIndex : 'workPersonName',
							editor : {
								maxLength : 50
							},
							searcher : {
								anchor : '98%'
							}
						},{
							header : '确认人',
							dataIndex : 'startPersonName',
							editor : {
								maxLength : 50
							},
							searcher : {
								anchor : '98%'
							}							
						},
						{
							header:'确认时间', dataIndex:'rdpEndTime', xtype:'datecolumn',editor:{hidden:true,xtype:'my97date',format: 'Y-m-d H:i'}
						},
						{header : '车辆状态',dataIndex : 'rdpRecordStatus',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
								return '<div style="background:'+ ZbglRdpPlanRecord.statusColorArrays[value] +';color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + ZbglRdpPlanRecord.statusArrays[value] + '</div>';
						},
						editor : { xtype:"hidden" }}						
						],
				beforeShowEditWin: function(record, rowIndex){  
					return true;
				},
				toEditFn: function(grid, rowIndex, e){
					ZbglRdpPlanRecord.writeTrainNoWin.show();
				}
			});
			
	/**
	 * 人员指派车辆
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid = new Ext.yunda.Grid({
				loadURL : ctx + "/zbglRdpPlanRecord!findWorkerList.action",
				singleSelect : false,
				isEdit:true,
				tbar : [
				{
					text : '指派车辆',		
					disabled:ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED,
					iconCls:"chart_organisationIcon",
			    	handler: function(){
			    		disrecord();
			    	}
		    	}
				],
				fields : [{
							header:'指派',hidden:ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED, dataIndex:'idx', editor: { xtype:'hidden' }, width:10, sortable:false,
							renderer:function(v,x,r){			
								return "<img src='" + img + "' alt='指派车辆' style='cursor:pointer' onclick='disrecord()'/>";
							}
						},{
							header : 'idx主键',
							dataIndex : 'idx',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						},{
							header : '作业人员ID',
							dataIndex : 'workPersonIdx',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						}, {
							header : '作业人员',
							dataIndex : 'workPersonName',
							searcher : {
								anchor : '98%'
							}
						}, {
							header : '作业车辆ID',
							dataIndex : 'recordIdxs',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						},{
							header : '作业车辆',
							dataIndex : 'recordNames',
							searcher : {
								anchor : '98%'
							}
						}],
				beforeShowEditWin: function(record, rowIndex){  
					return true;
				},
				toEditFn: function(grid, rowIndex, e){
					return false;
				}
			});
			
	// 人员指派车辆
	ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid.store.on('beforeload', function() {
		this.baseParams.rdpPlanIdx = ZbglRdpPlanRecord.rdpPlanIdx;
		var smplan = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().getSelections();
		if(!Ext.isEmpty(smplan)){
			this.baseParams.orgIdx = smplan[0].data.workTeamID ;
		}
	});

	// 默认排序		
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.setDefaultSort("seqNum", "ASC");
			
	// 数据加载前
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.rdpPlanIdx = ZbglRdpPlanRecord.rdpPlanIdx;
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});

	// 添加加载结束事件
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().addListener('load',
			function(me, records, options) {
				if (records.length > 0) {
					ZbglRdpPlanRecord.setNetwork(records);
				}else{
					// 清空节点
					ZbglRdpPlanRecord.parentNodes.clear();
				}
			});

	/**
	 * 设置车辆图
	 */
	ZbglRdpPlanRecord.setNetwork = function(records) {
		
		// 清空节点
		ZbglRdpPlanRecord.parentNodes.clear();
		
		var headImgUrl = getHeadImgUrl(ZbglRdpPlanRecord.railwayTime,getHeadColorByStatus(ZbglRdpPlanRecord.rdpPlanStatus),true);
		
		// 添加车头
		ZbglRdpPlanRecord.parentNodes.add({
			id : "id_train_head",
			label : "车头",
			size : 50,
			x : -50 ,
			y : -500,
			value : "车头",
			fixed : true,
			shape : 'image',
			image : headImgUrl,
			font : {
				size : 10,
				strokeWidth : 3,
				'face' : 'Monospace',
				align : 'left'
			}
		});
		
		// 添加车辆
		for (var k = 0; k < records.length; k++) {
			var heightY =parseInt(k / 10);
			var widthX = (k+1) - heightY * 10 ;
			var record = records[k];
			var trainTypeCode = Ext.isEmpty(record.data.trainTypeCode) ? "" : record.data.trainTypeCode ;
			var trainNo = Ext.isEmpty(record.data.trainNo) ? "" : record.data.trainNo ;
			var title = trainTypeCode + "" + trainNo ;
			var workPersonName = Ext.isEmpty(record.data.workPersonName) ? "" : record.data.workPersonName ;
			var bodyImgUrl = getBodyImgUrl(title,getBodyColorByStatus(record.data.rdpRecordStatus),true,workPersonName);
			// 添加【配件流程节点显示】
			ZbglRdpPlanRecord.parentNodes.add({
						id : record.data.idx,
						label : record.data.seqNum + "辆",
						size : 60,
						x : -50 + widthX * 153,
						y : -500 + heightY * 115,
						value : record.data.idx,
						fixed : true,
						shape : 'image',
						image : bodyImgUrl,
						font : {
							size : 10,
							strokeWidth : 3,
							'face' : 'Monospace',
							align : 'left'
						}
					});
		}
		// 创建实例
		if(!ZbglRdpPlanRecord.network){
			ZbglRdpPlanRecord.network = new vis.Network(document.getElementById('visualization'),
					ZbglRdpPlanRecord.data, ZbglRdpPlanRecord.options);
		}
	}

	/**
	 * vis图
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanRecordVis = new Ext.Panel({
				region : 'center',
				border : false,
				autoScroll : false,
				anchor : '100%',
				tbar : ['->', '图例：',{
							xtype : 'label',
							text : '未处理',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_wkg_zb'
						}, '-', {
							xtype : 'label',
							text : '处理中',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_ykg_zb'
						}, '-', {
							xtype : 'label',
							text : '已完成',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_ywg_zb'
						}],
				html : [
						'<div style="background-color:black" id= "visualization">',
						'</div>'].join(""),
				listeners : {
					afterrender : function(window) {

					}
				}
			});
			
			
	// 选择作业车辆处理事件
	ZbglRdpPlanRecordWin.submit = function(){
	
		var recordIdxs = [];				
		//未选择记录，直接返回
	    if(!ZbglRdpPlanRecordWin.grid.store.getAt(0)){
	    	MyExt.Msg.alert("尚未选择作业车辆");
	    	return;
	    }
	    var records = ZbglRdpPlanRecordWin.grid.getSelectionModel().getSelections();
	    for (var i = 0; i < records.length; i++) {
	    	recordIdxs.push(records[i].get("idx"));
	    }
	    
	    var workPersonIdxs = [];
	    var workPersonNames = [];
	    var smperson = ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid.getSelectionModel().getSelections();
	    for (var j = 0; j < smperson.length; j++) {
	    	workPersonIdxs.push(smperson[j].data.idx);
	    	workPersonNames.push(smperson[j].data.workPersonName);
	    }
	    
		 Ext.Msg.confirm("提示","确认指派", function(btn){
			if(btn == 'yes'){
				showtip();
		    	$.ajax({
			    	url: ctx + "/zbglRdpPlanRecord!disrecord.action",
			    	data:{recordIdxs:recordIdxs+"", workPersonIdxs:workPersonIdxs+"" ,workPersonNames:workPersonNames+"",rdpPlanIdx:ZbglRdpPlanRecord.rdpPlanIdx},
			    	type:"post",
			    	dataType:"json",
			    	success:function(data){
			    		hidetip();
			    		if (data.errMsg != null) {
			    			MyExt.Msg.alert(data.errMsg);
		    				return;
			    		}
			    		//刷新GRID
			    		ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid.store.reload();
			    		ZbglRdpPlanRecordWin.selectWin.hide();
			    		alertSuccess();
			    	}
			    });	
			}
		  });
	}
			
	// 选择作业人员处理事件
	WorkStationEmp.submit = function(){
		var idxs = [];				
		//未选择记录，直接返回
	    if(!WorkStationEmp.grid.store.getAt(0)){
	    	MyExt.Msg.alert("尚未选择作业人员");
	    	return;
	    }
	    var empids = [];
	    var empnames = [];
	    var record = WorkStationEmp.grid.store.getAt(0);
	    var i = 0;
	    while(record){
	    	empids.push(record.get("empid"));
	    	empnames.push(record.get("empname"));
	    	record = WorkStationEmp.grid.store.getAt(++i);	    	
	    }
	    if(empids.toString().length > 499){
	    	 MyExt.Msg.alert("选择作业人员超过最大限制,请重新选择");
	    	 return;
	    }
	    
	    var smrecord = ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getSelectionModel().getSelections();
	    for (var j = 0; j < smrecord.length; j++) {
	    	idxs.push(smrecord[j].data.idx);
	    }
	    
		 Ext.Msg.confirm("提示","确认指派", function(btn){
			if(btn == 'yes'){
				showtip();
		    	$.ajax({
			    	url: ctx + "/zbglRdpPlanRecord!dispatcher.action",
			    	data:{idxs:idxs+"", empids:empids+"" ,empnames:empnames+""},
			    	type:"post",
			    	dataType:"json",
			    	success:function(data){
			    		hidetip();
			    		if (data.errMsg != null) {
			    			MyExt.Msg.alert(data.errMsg);
		    				return;
			    		}
			    		//刷新GRID
			    		ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.reload();
			    		WorkStationEmp.selectWin.hide();
			    		alertSuccess();
			    	}
			    });	
			}
		  });
	}

});