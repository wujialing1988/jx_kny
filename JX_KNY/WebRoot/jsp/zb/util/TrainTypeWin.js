//车型定义
Ext.onReady(function(){
Ext.namespace('TrainType'); 
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
TrainType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//TrainType.searchParam = {} ;
TrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainType!pageQuery.action',                 //装载列表数据的请求URL
    tbar: [{
        xtype:"label", text:"  车型简称： " 
    },{
        id:"shortName",xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		//var searchParam = {} ;
    		var shortName = Ext.getCmp("shortName").getValue();
           //TrainType.searchParam.shortName = Ext.getCmp("shortName").getValue();
			var whereList = [{
				propName: 'shortName', propValue: shortName, compare:Condition.LIKE
			}]; 
			TrainType.grid.store.load({
				/*params : {
					entityJson: Ext.util.JSON.encode(TrainType.searchParam)
				}*/
				params : {
					whereListJson: Ext.util.JSON.encode(whereList)
				}
				
			});
    	}
    },'-',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		TrainType.submit();
    	}
    }],
	fields: [{
		header:'车型代码', dataIndex:'typeID' ,searcher: { hidden: true }
	},{
		header:'车型名称', dataIndex:'typeName', editor:{   }
	},{
		header:'简称', dataIndex:'shortName', editor:{   }
	}]
});
TrainType.selectWin = new Ext.Window({
	title:"选择车型", maximizable:true, width:500, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:TrainType.grid
});
//移除监听
TrainType.grid.un('rowdblclick', TrainType.grid.toEditFn, TrainType.grid);
//确认提交方法，后面可覆盖此方法完成查询
TrainType.submit = function(){alert("请覆盖，TrainType.submit 方法完成自己操作业务！");};
});