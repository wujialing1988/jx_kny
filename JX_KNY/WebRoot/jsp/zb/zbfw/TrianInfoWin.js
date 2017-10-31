//选择机车维护机车
Ext.onReady(function(){
Ext.namespace('TrianInfoWin'); 
TrianInfoWin.trainTypeIDX = '';//车型idx
TrianInfoWin.trainTypeShortName = '';//车型名称
TrianInfoWin.ZbfwTrianCenterIDX = '';//绑定中间表idx
TrianInfoWin.zbFwIdx = '';//范围idx
TrianInfoWin.fwName = '';//范围名称
TrianInfoWin.flag;//0==本段 1==非本段 查询条件
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
TrianInfoWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
TrianInfoWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!findJczlTrainInfoPageListByholdOrg.action',                 //装载列表数据的请求URL
    hideRowNumberer: true,
    tbar: [{
        xtype:"label", text:"  车型： " 
    },{
        id:"trainTypeShortNameShow",xtype: "label" ,readOnly:true,maxLength: 50
    },'-',{
        xtype:"label", text:"  车号： " 
    },{
        id:"trainNoInfo",xtype: "textfield" ,maxLength: 50,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
			TrianInfoWin.grid.store.load();
    	}
    },'-',{
    	text: "重置", iconCls: "saveIcon", handler: function(){
			Ext.getCmp("trainNoInfo").setValue("");
    		TrianInfoWin.grid.store.load();
    	}
   	},'-',{
	   	text: "确定", iconCls: "saveIcon", handler: function(){
	   		var grid = TrianInfoWin.grid;
    		if(!$yd.isSelectedRecord(grid)) return;//æªéæ©è®°å½ï¼ç´æ¥è¿å
	   		TrianInfoWin.submit();
	   		TrianInfoWin.selectWin.hide();
	   	}
    },{   
        xtype:'checkbox', name:'flag', boxLabel: '本段&nbsp;&nbsp;&nbsp;&nbsp;', 
        inputValue:0,
	    handler: function(){
	    	TrianInfoWin.checkQuery();
	    }
    },{   
        xtype:'checkbox', name:'flag', boxLabel: '非本段&nbsp;&nbsp;&nbsp;&nbsp;', 
        inputValue:1,
	    handler: function(){
	    	TrianInfoWin.checkQuery();
	    }
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车号', dataIndex:'trainNo', editor:{   }
	},{
		header:'配属段', dataIndex:'dName', editor:{   }
	}]
});

// 表格数据加载前的参数设置
TrianInfoWin.grid.store.on('beforeload', function(){
	var trainNo = Ext.getCmp("trainNoInfo").getValue();
	var entityJson = {};
	entityJson.trainNo = trainNo;
	entityJson.trainTypeIDX = TrianInfoWin.trainTypeIDX;
	entityJson.idx = TrianInfoWin.ZbfwTrianCenterIDX;
	this.baseParams.entityJson = Ext.util.JSON.encode(entityJson);
	//flag 1==本段 2==非本段
	this.baseParams.flag = TrianInfoWin.flag;
});	

TrianInfoWin.selectWin = new Ext.Window({
	title:"选择车号", maximizable:true, width:600, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:TrianInfoWin.grid
});
//移除监听
TrianInfoWin.grid.un('rowdblclick', TrianInfoWin.grid.toEditFn, TrianInfoWin.grid);
//确认提交方法，后面可覆盖此方法完成查询
TrianInfoWin.submit = function(){
	var sm = TrianInfoWin.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    //list数据对象
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.trainTypeShortName = TrianInfoWin.trainTypeShortName;  //车型编码（主键）
    	data.trainNo = objData[i].get("trainNo") ;
    	data.trainTypeIDX = TrianInfoWin.trainTypeIDX;
    	data.zbfwIDX = TrianInfoWin.zbFwIdx;
    	data.fwName = TrianInfoWin.fwName;
        dataAry.push(data);
    }
    TrianInfoWin.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/zbfwTrainCenter!saveOrUpdateZbfwTrainCenterInfo.action',
        jsonData: dataAry,
        success: function(response, options){
          	TrianInfoWin.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                TrianInfoWin.grid.store.load();
                ZbfwTrianCenterWin.grid.store.load();
                alertSuccess();
            } else {
                alertFail(result.errMsg);
            }	
        },
        failure: function(response, options){
            TrianInfoWin.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};

TrianInfoWin.checkQuery = function() {
	TrianInfoWin.flag = "-1";
	var checkBoxGroup = TrianInfoWin.grid.getTopToolbar().findByType("checkbox");
	for(var i = 0; i < checkBoxGroup.length; i++) {
		if(checkBoxGroup[i].checked) {
			TrianInfoWin.flag = TrianInfoWin.flag + "," + checkBoxGroup[i].inputValue;
		}
	}
	TrianInfoWin.grid.store.load();
}

});