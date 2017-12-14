/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {

	Ext.ns('ZbglRdpPlanRecord');
	
	ZbglRdpPlanRecord.rdpPlanStatus = "###" ; // 列检计划状态
	
	ZbglRdpPlanRecord.rdpPlanIdx = "###" ; // 列检计划ID

	ZbglRdpPlanRecord.statusArrays = {"INITIALIZATION":"未处理","ONGOING":"处理中","COMPLETE":"已完成"} ;
	ZbglRdpPlanRecord.statusColorArrays = {"INITIALIZATION":"#999999","ONGOING":"#00BFFF","COMPLETE":"#008000"} ;
	
	
	// 填写车号窗口 
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
	
	
	// 设置互换左右
	ZbglRdpPlanRecord.setDirect = function(rdpPlanIdx,queueNo){
		Ext.Ajax.request({
			url: ctx + "/zbglRdpPlanRecord!setDirect.action",
			params: {
				"rdpPlanIdx":rdpPlanIdx,
				"queueNo":queueNo
			},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					alertSuccess();
					ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.load();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	/**
	 * 队列列表
	 */
	ZbglRdpPlanRecord.SelectQueueGrid = new Ext.yunda.Grid({
		loadURL : ctx + "/zbglRdpPlanRecord!findSelectQueneList.action",
		singleSelect : true,
		isEdit:true,
		page: false,
		tbar : [
		        '-',
		        '<div><span style="color:green;">*双击选择队列</span></div>'
		],
		fields : [{
					header : '分队id',
					dataIndex : 'queueNo',
					hidden : true
				},{
					header : '分队',
					dataIndex : 'queueName',
					renderer: function(value, metaData, record) {
			      		return value;
					}
				}					
				],
		toEditFn: function(grid, rowIndex, e){
			return false;
		}
	});
	
	/**
	 * 双击事件
	 */
	ZbglRdpPlanRecord.SelectQueueGrid.on("dblclick", function(grid, rowIndex, e){
		var sm = ZbglRdpPlanRecord.SelectQueueGrid.getSelectionModel();
		var records = sm.getSelections();
		var recordIds = $yd.getSelectedIdx(ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid);
		Ext.Ajax.request({
			url: ctx + "/zbglRdpPlanRecord!changeRecordQueue.action",
			params: {
				"recordIds":recordIds,
				"queueNo":records[0].data.queueNo
			},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					alertSuccess();
					ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.load();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	});
	
	ZbglRdpPlanRecord.SelectQueueGrid.store.on('beforeload', function() {
		var sm = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel();
		var records = sm.getSelections();
		if(records.length > 0){
			this.baseParams.classNo = records[0].data.classNo;
			this.baseParams.orgIdx = records[0].data.workTeamID;
		}
	});
	
	// 设置队列窗口
	ZbglRdpPlanRecord.selectQueueWin = new Ext.Window({
		title:"队列", width:400, height:250, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ ZbglRdpPlanRecord.SelectQueueGrid ], modal:true,    	
    	buttons: [{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ ZbglRdpPlanRecord.selectQueueWin.hide(); }
		}],
		listeners: {
			show:function(){
				ZbglRdpPlanRecord.SelectQueueGrid.store.load();
			}
		}
	});	
	

	/**
	 * 列检计划列表
	 */
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid = new Ext.yunda.Grid({
				loadURL : ctx + "/zbglRdpPlanRecord!findZbglRecordAndQueue.action",
				singleSelect : false,
				isEdit:true,
				page: false,
				tbar : [
				{
					text : '指派分队',		
					disabled:ZbglRdpPlanRecord.rdpPlanStatus == STATUS_HANDLED,
					iconCls:"chart_organisationIcon",
			    	handler: function(){
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
			    			MyExt.Msg.alert("作业车辆未选择！");
			    			return;		
			    		}
			    		ZbglRdpPlanRecord.selectQueueWin.show();
			    	}
		    	},'-','<div><span style="color:green;">*双击填写车号</span></div>'
				],
				fields : [{
							header : 'idx主键',
							dataIndex : 'idx',
							hidden : true,
							editor : {
								xtype : 'hidden'
							}
						},{
							header : '分队id',
							dataIndex : 'queueNo',
							hidden : true
						},{
							header : '分队',
							dataIndex : 'queueName',
							renderer: function(value, metaData, record) {
					     		var html = "";
					  			html = "<span style='font-weight:bold;color:red;'>"+value+"</span>";
					      		return html;
							}
						},  {
							header : '作业人员',
							dataIndex : 'workPersonName',
							renderer: function(value, metaData, record, rowIndex) {
								var queueNo = record.get('queueNo');
					     		var html = "";
					  			html = "<span><a style='text-decoration:none;color:blue;' href='#' onclick='ZbglRdpPlanRecord.setDirect(\""+ ZbglRdpPlanRecord.rdpPlanIdx +"\",\""+ queueNo +"\")'>"+value+"</a></span>";
					      		return html;
								
							}
						},{
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
							header:'确认时间', dataIndex:'rdpEndTime',editor:{}
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
			
	// 数据加载前
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.on('beforeload', function() {
		this.baseParams.rdpPlanIdx = ZbglRdpPlanRecord.rdpPlanIdx;
	});
	
	// 数据加载前
	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.store.on('load', function() {
		gridSpan(ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid,"row","[queueName],[workPersonName]");
	});
	
			
});