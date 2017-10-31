Ext.onReady(function(){
//修程定义
Ext.namespace('Train');                       //定义命名空间
Train.searchParam = {} ;  //定义全局查询条件
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
Train.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
Train.grid = new Ext.yunda.Grid({ 
    loadURL: ctx + '/jczlTrainView!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    storeAutoLoad: false,
    tbar:[{
		xtype:"combo", id:"queryTrain_Id", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"车号", mode:"local",triggerAction: "all",
		store: new Ext.data.SimpleStore({
			fields: ["type"],
			data: [ ["车号"], ["车型"], ["配属单位"] ]
		})
	},{	            
        xtype:"textfield",  id:"trainName_Id", width: 100
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			var typeName = Ext.getCmp("trainName_Id").getValue();
			var querytype = Ext.getCmp("queryTrain_Id").getValue();
			if(querytype == '车号'){
				Train.searchParam.trainNo = typeName;
				Train.grid.getStore().load();
			}else if(querytype == '车型'){
				Train.searchParam.trainTypeShortName = typeName;
				Train.grid.getStore().load();
			}else{
				Train.searchParam.holdOrgName = typeName;
				Train.grid.getStore().load();
			}
		}
	},{
		text : "重置",
		iconCls : "resetIcon",
		handler : function(){
			Train.searchParam = {} ;
			Train.grid.getStore().load();
			//清空搜索输入框
			Ext.getCmp("trainName_Id").setValue("");
			Ext.getCmp("queryTrain_Id").setValue("车号");
			//清空机车组成查询集合
		}
	},{
		text : "确定",iconCls : "saveIcon", handler: function(){
			Train.submit(); 
		}
	}],
	fields: [{
		header:'主键', dataIndex:'idx', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'机车状态', dataIndex:'trainState', editor:{  maxLength:50 },
		renderer:function(v){
			switch(v){
				case trainStateRepair:
					return "检修";
				case trainStateUse:
					return "运用";
				case trainStateSpare:
					return "备用";
				default:
					return v;
			}
		}		
	},{
		header:'使用别ID', dataIndex:'trainUse',hidden:true, editor:{  maxLength:50 }
	},{
		header:'使用别', dataIndex:'trainUseName', editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'holdOrgName', editor:{ xtype:'hidden' }
	},{
		header:'制造厂家', dataIndex:'makeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	}]
});
Train.selectWin = new Ext.Window({
	title:"选择机车", maximizable:true,width:500, height:350, closeAction:"hide", modal:true, layout:"fit", items:Train.grid
});
//移除监听
Train.grid.un('rowdblclick', Train.grid.toEditFn, Train.grid);
//确认提交方法，后面可覆盖此方法完成查询
Train.submit = function(){alert("请覆盖，Train.submit 方法完成自己操作业务！");};

});