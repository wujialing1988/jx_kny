/**
 * 列检_故障提票查看
 */
Ext.onReady(function(){
	Ext.namespace('GztpTicketQuery');                       //定义命名空间
	
	GztpTicketQuery.queryTimeout;
	
	GztpTicketQuery.labelWidth = 100;                        //表单中的标签名称宽度
	GztpTicketQuery.fieldWidth = 130;                       //表单中的标签宽度
	
	GztpTicketQuery.grid = new Ext.yunda.Grid({
		title : i18n.FaultRegQuery.faultRegList,
		region: 'center',
	    loadURL: ctx + '/gztp!pageQuery.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: {forceFit: false , markDirty: false },
	    tbar : ['refresh','&nbsp;&nbsp;',
	    {
	    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:i18n.FaultRegQuery.quickSearch, width:200,
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
				header:i18n.FaultRegQuery.inspectionPlanId, dataIndex:'rdpPlanIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.registerType, dataIndex:'type',width: 120
			},
	     	{
				header:i18n.FaultRegQuery.registerNumber, dataIndex:'faultNoticeCode',width: 120
			},
	     	{
				header:i18n.FaultRegQuery.trainNumber, dataIndex:'railwayTime',width: 80
			},
	     	{
				header:i18n.FaultRegQuery.trainTypeIdx, dataIndex:'vehicleTypeIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.trainCode, dataIndex:'vehicleTypeCode',width: 100
			},
	     	{
				header:i18n.FaultRegQuery.trainPlanIdx, dataIndex:'rdpRecordPlanIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.trainNum, dataIndex:'trainNo',width: 100
			},
	     	{
				header:i18n.FaultRegQuery.inspectionNumber, dataIndex:'rdpNum',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.workRangeIdx, dataIndex:'scopeWorkIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.professionType, dataIndex:'scopeWorkFullname',width: 160
			},
	     	{
				header:i18n.FaultRegQuery.faultPartCode, dataIndex:'vehicleComponentFlbm',hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.faultPart, dataIndex:'vehicleComponentFullname',width: 160
			},
	     	{
				header:i18n.FaultRegQuery.faultDes, dataIndex:'faultDesc',width: 160
			},
			{
				header:i18n.FaultRegQuery.faultTypeKey, dataIndex:'faultTypeKey',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.faultType, dataIndex:'faultTypeValue',width: 100
			},
	     	{
				header:i18n.FaultRegQuery.noticePersonId, dataIndex:'noticePersonId',hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.noticePersonName, dataIndex:'noticePersonName',width: 80
			},
	        {
				header:i18n.FaultRegQuery.faultReDate, dataIndex:'noticeTime', xtype:'datecolumn', format:'Y-m-d H:i:s', width:100, xtype:'datecolumn',width: 170
			},
	     	{
				header:i18n.FaultRegQuery.noticeSite, dataIndex:'siteId',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.siteName, dataIndex:'siteName',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.handlePersonId, dataIndex:'handlePersonId',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.handlePersonName, dataIndex:'handlePersonName',width: 120,hidden:true
			},
	        {
				header:i18n.FaultRegQuery.handleDate, dataIndex:'handleTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.handleSite, dataIndex:'handleSite',width: 120
			},
	     	{
				header:i18n.FaultRegQuery.handleMethod, dataIndex:'handleWayValue',width: 120
			},
	     	{
				header:i18n.FaultRegQuery.taskListId, dataIndex:'rdpIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.FaultRegQuery.handleType, dataIndex:'handleType',width: 70,hidden:true,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (HANDLE_TYPE_REG == value) {
						return HANDLE_TYPE_REG_CH;
					}
					return '<span style="color:green;">' + HANDLE_TYPE_REP_CH + '</span>';
				}
			},
	     	{
				header:i18n.FaultRegQuery.status, dataIndex:'faultNoticeStatus',width: 70,hidden:true,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (STATUS_INIT == value) {
						return '<span style="color:red;">' + STATUS_INIT_CH + '</span>';
					}
					if (STATUS_CHECKED == value) {
						return '<span style="color:green;">' + STATUS_CHECKED_CH + '</span>';
					}
					return STATUS_OVER_CH;
				}
			},
			{
				header:i18n.FaultRegQuery.idx, dataIndex:'idx',hidden:true
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
		
		GztpTicketQuery.whereList.push({ propName: 'siteId', propValue:siteId, compare: Condition.EQ});
		GztpTicketQuery.whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ});
		var queryKey = Ext.getCmp('query_input').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (RAILWAY_TIME LIKE '%" + queryKey + "%'"
			        + " OR VEHICLE_TYPE_CODE LIKE '%" + queryKey + "%'"
					+ " OR TRAIN_NO LIKE '%" + queryKey + "%'"
					+ " OR NOTICE_PERSON_NAME LIKE '%" + queryKey + "%') ";
			GztpTicketQuery.whereList.push({ sql: sql, compare: Condition.SQL});
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
		        	{ fieldLabel:i18n.FaultRegQuery.faultPart, name:"vehicleComponentFullname"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.faultType, name:"faultTypeValue"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.faultDes, name:"faultDesc"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.handleMethod, name:"handleWayValue"}
		        ]
			},{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.handleSite, name:"handleSite"}
		        ]
			},
			{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.noticePersonName, name:"noticePersonName"}
		        ]
			},
			{
		        items: [
		        	{ fieldLabel:i18n.FaultRegQuery.registerDate, name:"noticeTime"}
		        ]
			}
			]
		});
		
	// 布局
	GztpTicketQuery.westPanel = new Ext.Panel({
		region : 'east',
	    layout : 'border',
		width:800,
		title : i18n.FaultRegQuery.basicInformation,
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
	
	//页面自适应布局
	new Ext.Viewport({ layout : 'fit', items : GztpTicketQuery.mainPanel});
});