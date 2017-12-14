Ext.onReady(function(){
	Ext.namespace('JcxtflFaultSelect');
	
	JcxtflFaultSelect.flbm = '###' ;	// 构型分类id
	
	JcxtflFaultSelect.faultSelectGrid = new Ext.yunda.Grid({
    	loadURL: ctx + '/jcxtflFault!pageQuery.action',                 //装载列表数据的请求URL
    	isEdit:false,
    	singleSelect: true,  
    	tbar: [{
        	xtype:"label", text:"  检索：" 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId",
            emptyText:'故障编码，名称检索'
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				JcxtflFaultSelect.faultSelectGrid.getStore().load();
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
        		if(!$yd.isSelectedRecord(JcxtflFaultSelect.faultSelectGrid)) return;
        		var tempData = JcxtflFaultSelect.faultSelectGrid.selModel.getSelections();
        		JcxtflFaultSelect.submit(tempData[0].data);
			}
		}],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true
		}, {
			header: '顺序号', dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: '分类编码', dataIndex: 'flbm', hidden: true
		}, {
			header: '故障现象编码', dataIndex: 'faultId'
		}, {
			header: '故障现象名称', dataIndex: 'faultName'
		}, {
			header: '故障类型编码',hidden: true, dataIndex: 'faultTypeID'
		}, {
			header: '故障类型', dataIndex: 'faultTypeName'
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
	    title: "故障库选择", width: 600, height:400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
	    items: [JcxtflFaultSelect.faultSelectGrid],
	    buttons: [{
            text: "关闭", iconCls: "closeIcon", 
            handler: function(){ 
            	JcxtflFaultSelect.win.hide();
            }
        }]
	});
	
});