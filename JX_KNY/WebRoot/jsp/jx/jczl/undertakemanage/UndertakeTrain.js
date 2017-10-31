/**
 * Undertake_train 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('UndertakeTrain');                       //定义命名空间
UndertakeTrain.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/undertakeTrain!findpageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/undertakeTrain!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/undertakeTrain!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false, 
    tbar: [{
    	text:"选择机车", iconCls: "addIcon" , handler: function(){
    		Train.selectWin.show();
    		Train.grid.singleSelect = false;
    		Train.grid.store.load();
    	}
    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'承修车型主键', dataIndex:'undertakeTrainTypeIDX', hidden:true
	},{
		header:'车型id', dataIndex:'trainTypeIDX', hidden:true
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{   }
	},{
		header:'车号', dataIndex:'trainNo', editor:{   }
	},{
		header:'配属局', dataIndex:'bName', editor:{   }
	},{
		header:'配属段', dataIndex:'dName', editor:{   }
	},{
		header:'制造厂家', dataIndex:'makeFactoryName', editor:{   },
		renderer: function(v){if(Ext.isEmpty(v)||v =='null') return ""; return v;}
	},{
		header:'出厂日期', dataIndex:'leaveDate',xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'使用别', dataIndex:'trainUseName', editor:{   }
	}],
	afterDeleteFn: function(){  //删除后调用的函数方法
		UndertakeTrain.grid.store.load();
	}
});
//移除侦听器
UndertakeTrain.grid.un('rowdblclick', UndertakeTrain.grid.toEditFn, UndertakeTrain.grid);
//查询前添加过滤条件
UndertakeTrain.grid.store.on('beforeload' , function(){
	var searchParam = {} ;
	searchParam.undertakeTrainTypeIDX = UndertakeTrainType.idx ; //承修车型主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
Train.submit = function (){
	var sm = Train.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.undertakeTrainTypeIDX = UndertakeTrainType.idx ; //承修车型主键
    	data.trainTypeIDX = objData[i].get("trainTypeIDX");  //车型id 
    	data.trainTypeShortName = objData[i].get("trainTypeShortName");  //车型简称
    	data.trainNo = objData[i].get("trainNo");  //车号
        dataAry.push(data);
    }
    Train.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/undertakeTrain!saveFromTrain.action',
        jsonData: dataAry,
        success: function(response, options){
          	Train.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                Train.grid.store.reload();
                UndertakeTrain.grid.store.reload();
                UndertakeTrainType.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            Train.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
}
//车号选择前的过滤
Train.grid.store.on('beforeload' , function(){
	var queryParams = Train.searchParam ;
	queryParams.trainTypeIDX = UndertakeTrainType.trainTypeIDX ; //车型主键
	queryParams.trainNo = Ext.getCmp("trainNo").getValue() ; //车号
	Train.searchParam = queryParams;
	this.baseParams.undertakeTrainTypeIDX = UndertakeTrainType.idx;  //承修车型主键
	this.baseParams.entityJson =  Ext.util.JSON.encode(queryParams);  
});
});