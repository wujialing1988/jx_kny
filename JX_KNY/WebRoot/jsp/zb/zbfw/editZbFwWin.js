//选择机车维护机车
Ext.onReady(function(){
	Ext.namespace('EditZbFwWin'); 
	EditZbFwWin.trainTypeIDX = '';//车型idx
	EditZbFwWin.trainNo = '';//车号
	EditZbFwWin.zbfwTrianCenterIDX = '';//中间表idx
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	EditZbFwWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	EditZbFwWin.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbFw!findZbFwNotInTrainNoAndTrainTypeIDX.action',                 //装载列表数据的请求URL
	    hideRowNumberer: true,
	    singleSelect: true,
	    tbar: [{
	        xtype:"label", text:"  车型： " 
	    },{
	        id:"trainTypeShortNameShow",xtype: "label" ,readOnly:true,maxLength: 50
	    },'-',{
	        xtype:"label", text:"  车号： " 
	    },{
	    	id:"trainNoShow",xtype: "label" ,readOnly:true,maxLength: 50
	    },'-',{
		   	text: "确定", iconCls: "saveIcon", handler: function(){
		   		var grid = EditZbFwWin.grid;
	    		if(!$yd.isSelectedRecord(grid)) return;//æªéæ©è®°å½ï¼ç´æ¥è¿å
		   		EditZbFwWin.submit();
		   		EditZbFwWin.selectWin.hide();
		   	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'范围编码', dataIndex:'fwCode'
		},{
			header:'车型', dataIndex:'trainTypeShortName'
		},{
			header:'范围名称', dataIndex:'fwName'
		},{
			header:'范围描述', dataIndex:'fwDesc'
		}]
	});
	
	// 表格数据加载前的参数设置
	EditZbFwWin.grid.store.on('beforeload', function(){
		this.baseParams.trainNo = EditZbFwWin.trainNo;
		this.baseParams.trainTypeIDX = EditZbFwWin.trainTypeIDX;
	});	
	
	EditZbFwWin.selectWin = new Ext.Window({
		title:"选择范围活流程", maximizable:true, width:600, height:280, 
	    plain:true, closeAction:"hide", modal:true, layout:"fit",
	    items:EditZbFwWin.grid
	});
	//移除监听
	EditZbFwWin.grid.un('rowdblclick', EditZbFwWin.grid.toEditFn, EditZbFwWin.grid);
	//确认提交方法，后面可覆盖此方法完成查询
	EditZbFwWin.submit = function(){
		var sm = EditZbFwWin.grid.getSelectionModel();
	    if (sm.getCount() < 1) {
	        MyExt.Msg.alert("尚未选择一条记录！");
	        return;
	    }
	    var objData = sm.getSelections();
	    var obj = objData[0];
	    var zbfwIDX = obj.get("idx") ;
	    var fwName = obj.get("fwName") ;
	    
	    EditZbFwWin.loadMask.show();
	    Ext.Ajax.request({
	        url: ctx + '/zbfwTrainCenter!updateZbfwTrainCenterInfo.action',
	        params:{zbfwIDX:zbfwIDX,zbfwTrianCenterIDX:EditZbFwWin.zbfwTrianCenterIDX,fwName:fwName},
	        success: function(response, options){
	          	EditZbFwWin.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	                ZbFwTrainCenter.grid.store.load();
	                alertSuccess();
	            } else {
	                alertFail(result.errMsg);
	            }	
	        },
	        failure: function(response, options){
	            EditZbFwWin.loadMask.hide();
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
	    })
	};
});