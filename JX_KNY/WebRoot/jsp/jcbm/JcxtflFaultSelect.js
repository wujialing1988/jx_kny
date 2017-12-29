Ext.onReady(function(){
	Ext.namespace('JcxtflFaultSelect');
	
	JcxtflFaultSelect.flbm = '###' ;	// 构型分类id
	
	JcxtflFaultSelect.faultSelectGrid = new Ext.yunda.Grid({
    	loadURL: ctx + '/jcxtflFault!pageQuery.action',                 //装载列表数据的请求URL
    	isEdit:false,
    	singleSelect: true,  
    	tbar: [{
        	xtype:"label", text:i18n.TruckFaultReg.searching 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId",
            emptyText:i18n.TruckFaultReg.enText
		},{
			text : i18n.TruckFaultReg.search1,
			iconCls : "searchIcon",
			handler : function(){
				JcxtflFaultSelect.faultSelectGrid.getStore().load();
			},			
			width : 40
		},{
			text : i18n.TruckFaultReg.ok,
			iconCls : "saveIcon",
			handler : function(){
        		if(!$yd.isSelectedRecord(JcxtflFaultSelect.faultSelectGrid)) return;
        		var tempData = JcxtflFaultSelect.faultSelectGrid.selModel.getSelections();
        		JcxtflFaultSelect.submit(tempData[0].data);
			}
		}],
		fields: [{
			header: i18n.TruckFaultReg.idx1, dataIndex: 'idx', hidden: true
		}, {
			header: i18n.TruckFaultReg.oderNumber, dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: i18n.TruckFaultReg.typeCode, dataIndex: 'flbm', hidden: true
		}, {
			header: i18n.TruckFaultReg.faultDesCode, dataIndex: 'faultId'
		}, {
			header: i18n.TruckFaultReg.faultDesName, dataIndex: 'faultName'
		}, {
			header: i18n.TruckFaultReg.faultTypeCode,hidden: true, dataIndex: 'faultTypeID'
		}, {
			header: i18n.TruckFaultReg.faultType, dataIndex: 'faultTypeName'
		}],
		toEditFn: function(grid, rowIndex, e){
			return false;
		}
	});
	
	JcxtflFaultSelect.whereList = [];
	// 数据加载前
	JcxtflFaultSelect.faultSelectGrid.store.on('beforeload', function() {
		// 清空查询条件
		JcxtflFaultSelect.whereList.length = 0;
		JcxtflFaultSelect.whereList.push({ propName: 'flbm', propValue:JcxtflFaultSelect.flbm, compare: Condition.EQ});
		var faultName = Ext.getCmp("fault_searchId").getValue();
		if (!Ext.isEmpty(faultName)) {
			var sql = " (FAULT_ID LIKE '%" + faultName + "%'"
					+ " OR FAULT_NAME LIKE '%" + faultName + "%') ";
			JcxtflFaultSelect.whereList.push({ sql: sql, compare: Condition.SQL});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(JcxtflFaultSelect.whereList);
		this.baseParams.sort = 'seqNo';
		this.baseParams.dir = 'ASC';
		
	});
	
	// 被覆盖的方法
	JcxtflFaultSelect.submit = function(data){
		alert("请覆盖此方法");
	};
	
	// 故障选择窗口
	JcxtflFaultSelect.win = new Ext.Window({
	    title: i18n.TruckFaultReg.selectFaultLibrary, width: 600, height:400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFaultSelect.faultSelectGrid],
	    buttons: [{
            text: i18n.TruckFaultReg.close, iconCls: "closeIcon", 
            handler: function(){ 
            	JcxtflFaultSelect.win.hide();
            }
        }]
	});
	
});