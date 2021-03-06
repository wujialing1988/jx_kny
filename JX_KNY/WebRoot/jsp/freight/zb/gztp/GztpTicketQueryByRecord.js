/**
 * 列检_故障提票查看
 */
Ext.onReady(function(){
	Ext.namespace('GztpTicketQuery');                       //定义命名空间
	
	GztpTicketQuery.queryTimeout;
	
	GztpTicketQuery.labelWidth = 100;                        //表单中的标签名称宽度
	GztpTicketQuery.fieldWidth = 130;                       //表单中的标签宽度
	
	GztpTicketQuery.recordIdx ;								// 车辆计划id
	
	GztpTicketQuery.grid = new Ext.yunda.Grid({
		region: 'center',
	    loadURL: ctx + '/gztp!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: {forceFit: false , markDirty: false },
	    tbar : ['refresh','&nbsp;&nbsp;',
	    {
	    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:'快速检索（车次/车型/车号/提报人）', width:200,
	    	listeners: {
	    		keyup: function(filed, e) {
	    			if (GztpTicketQuery.queryTimeout) {
	    				clearTimeout(GztpTicketQuery.queryTimeout);
	    			}
	    			
	    			GztpTicketQuery.queryTimeout = setTimeout(function(){
	    				GztpTicketQuery.grid.store.load();
	    			}, 1000);
	    		}
			}
	    }],
		fields: [
	     	{
				header:'列检计划主键', dataIndex:'rdpPlanIdx',width: 120,hidden:true
			},
	     	{
				header:'登记类型', dataIndex:'type',width: 120
			},
	     	{
				header:'登记单号', dataIndex:'faultNoticeCode',width: 120
			},
	     	{
				header:'列车车次', dataIndex:'railwayTime',width: 80
			},
	     	{
				header:'车型主键', dataIndex:'vehicleTypeIdx',width: 120,hidden:true
			},
	     	{
				header:'车型代码', dataIndex:'vehicleTypeCode',width: 100
			},
	     	{
				header:'车辆计划主键', dataIndex:'rdpRecordPlanIdx',width: 120,hidden:true
			},
	     	{
				header:'车号', dataIndex:'trainNo',width: 100
			},
	     	{
				header:'列检车辆数量', dataIndex:'rdpNum',width: 120,hidden:true
			},
	     	{
				header:'作业范围主键', dataIndex:'scopeWorkIdx',width: 120,hidden:true
			},
	     	{
				header:'专业类型', dataIndex:'scopeWorkFullname',width: 160
			},
	     	{
				header:'故障部件分类编码', dataIndex:'vehicleComponentFlbm',hidden:true
			},
	     	{
				header:'故障部件', dataIndex:'vehicleComponentFullname',width: 160
			},
	     	{
				header:'故障描述', dataIndex:'faultDesc',width: 160
			},
			{
				header:'故障类型字典KEY', dataIndex:'faultTypeKey',width: 120,hidden:true
			},
	     	{
				header:'故障类型', dataIndex:'faultTypeValue',width: 100
			},
	     	{
				header:'提报人ID', dataIndex:'noticePersonId',hidden:true
			},
	     	{
				header:'提报人', dataIndex:'noticePersonName',width: 80
			},
	        {
				header:'故障登记时间', dataIndex:'noticeTime', xtype:'datecolumn', format:'Y-m-d H:i:s', width:100, xtype:'datecolumn',width: 170
			},
	     	{
				header:'提票站场', dataIndex:'siteId',width: 120,hidden:true
			},
	     	{
				header:'提票站场名称', dataIndex:'siteName',width: 120,hidden:true
			},
	     	{
				header:'销票人ID', dataIndex:'handlePersonId',width: 120,hidden:true
			},
	     	{
				header:'销票人名称', dataIndex:'handlePersonName',width: 120,hidden:true
			},
	        {
				header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', hidden:true
			},
	     	{
				header:'处理地点', dataIndex:'handleSite',width: 120
			},
	     	{
				header:'处理方式', dataIndex:'handleWayValue',width: 120
			},
	     	{
				header:'整备任务单ID', dataIndex:'rdpIdx',width: 120,hidden:true
			},
			{
				header:'主键ID', dataIndex:'idx',hidden:true
			}],
			beforeShowEditWin: function(){
				return false;
			}
	});
	
			// 作业范围显示隐藏
	GztpTicketQuery.grid.addListener('afterrender',function(me){
		if(vehicleType == '10'){
			GztpTicketQuery.grid.getColumnModel().setHidden(11,true);
		}else{
			GztpTicketQuery.grid.getColumnModel().setHidden(11,false);
		}
	});
	
	/**
	 * 单击车辆列表后，置到该行数据到form中以便进行修改
	 */
	GztpTicketQuery.grid.on("rowclick", function(grid, rowIndex, e){
		var sm = GztpTicketQuery.grid.getSelectionModel();
	    GztpTicketQuery.setInfoForm(sm);
	});

	//查询前添加过滤条件
	GztpTicketQuery.whereList = [];
	GztpTicketQuery.grid.store.on('beforeload' , function(){
		// 清空查询条件
		GztpTicketQuery.whereList.length = 0;
		
		GztpTicketQuery.whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ});
		var queryKey = Ext.getCmp('query_input').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (RAILWAY_TIME LIKE '%" + queryKey + "%'"
			        + " OR VEHICLE_TYPE_CODE LIKE '%" + queryKey + "%'"
					+ " OR TRAIN_NO LIKE '%" + queryKey + "%'"
					+ " OR NOTICE_PERSON_NAME LIKE '%" + queryKey + "%') ";
			GztpTicketQuery.whereList.push({ sql: sql, compare: Condition.SQL});
		}
		
		if(!Ext.isEmpty(GztpTicketQuery.recordIdx)){
			GztpTicketQuery.whereList.push({ propName: 'rdpRecordPlanIdx', propValue:GztpTicketQuery.recordIdx, compare: Condition.EQ});
		}
		
		this.baseParams.whereListJson = Ext.util.JSON.encode(GztpTicketQuery.whereList);
		this.baseParams.sort = 'faultNoticeStatus';
		this.baseParams.dir = 'DESC';
	});
	
	/**
	 * 设置基本信息
	 */
	GztpTicketQuery.setInfoForm = function(sm){
		var form = GztpTicketQuery.infoForm.getForm();		
		if (sm.getCount() > 0) {	
			// 设置基本信息
			var records = sm.getSelections();
			form.reset();
			form.findField("vehicleComponentFullname").setValue(records[0].data.vehicleComponentFullname);
			form.findField("faultTypeValue").setValue(records[0].data.faultTypeValue);
			form.findField("faultDesc").setValue(records[0].data.faultDesc);
			form.findField("handleWayValue").setValue(records[0].data.handleWayValue);
			form.findField("handleSite").setValue(records[0].data.handleSite);
			form.findField("noticePersonName").setValue(records[0].data.noticePersonName);
			var noticeTime = new Date(records[0].data.noticeTime).format('Y-m-d H:i');
			form.findField("noticeTime").setValue(noticeTime);
			// 加载物料
			GztpTicketQuery.westPanel.expand(true);
			MatTypeUseList.gztpIdx = records[0].data.idx ;
			MatTypeUseList.grid.store.reload();
		}		
	};
	
	/**
	 * 列检信息
	 */
	GztpTicketQuery.infoForm = new Ext.form.FormPanel({
			labelWidth:GztpTicketQuery.labelWidth, border: false,
			labelAlign:"left", layout:"column",
			frame:true,
			defaults:{
				xtype:"container", layout:"form", columnWidth:1, 
				defaults:{
					style: 'border:none; background:none;', 
					xtype:"textfield", readOnly: true,
					anchor:"100%"
				}
			},
			items:[{
		        items: [
		        	{ fieldLabel:"故障部件", name:"vehicleComponentFullname"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"故障类型", name:"faultTypeValue"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"故障描述", name:"faultDesc"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"处理方式", name:"handleWayValue"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:"处理地点", name:"handleSite"}
		        ]
			},
			{
		        items: [
		        	{ fieldLabel:"提报人", name:"noticePersonName"}
		        ]
			},
			{
		        items: [
		        	{ fieldLabel:"登记时间", name:"noticeTime"}
		        ]
			}
			]
		});
		
	// 布局
	GztpTicketQuery.westPanel = new Ext.Panel({
		region : 'east',
	    layout : 'border',
		width:800,
		title : '基本信息',
		collapsed: true,
        animCollapse: true,
        collapsible: true, 
	    items : [{
					region: 'north',
					height: 210,
					items: GztpTicketQuery.infoForm
				},{
					region: 'center',
					layout: 'fit',
					items:MatTypeUseList.grid
				}]
	});
	
	GztpTicketQuery.mainPanel = new Ext.Panel({
		border : false,
	    layout : 'border',
	    items : [GztpTicketQuery.westPanel, GztpTicketQuery.grid]
	});
	
	
	GztpTicketQuery.win = new Ext.Window({
		title:'故障情况查看', maximized:true,
		layout:"fit", closeAction:"hide",
		items:GztpTicketQuery.mainPanel,
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			
		}
	});
	
});