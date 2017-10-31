Ext.onReady(function(){
//修程定义
Ext.namespace('Employee');                       //定义命名空间
Employee.searchParam = {} ;  //定义全局查询条件
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
Employee.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
Employee.grid = new Ext.yunda.Grid({ 
    loadURL: ctx + '/omEmployeeSelect!pageQuery.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,
    tbar:[{
		xtype:"combo", id:"queryEmp_Id", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"人员代码", mode:"local",triggerAction: "all",
		store: new Ext.data.SimpleStore({
			fields: ["type"],
			data: [ ["人员代码"], ["人员名称"] ]
		})
	},{	            
        xtype:"textfield",  id:"empName_Id", width: 100
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			var typeName = Ext.getCmp("empName_Id").getValue();
			var querytype = Ext.getCmp("queryEmp_Id").getValue();
			if(querytype == '人员代码'){
				Employee.searchParam.empcode = typeName;
				Employee.grid.getStore().load();
			}else{
				Employee.searchParam.empname = typeName;
				Employee.grid.getStore().load();
			}
		}
	},{
		text : "重置",
		iconCls : "resetIcon",
		handler : function(){
			Employee.searchParam = {} ;
			Employee.grid.getStore().load();
			//清空搜索输入框
			Ext.getCmp("empName_Id").setValue("");
			Ext.getCmp("queryEmp_Id").setValue("人员代码");
			//清空机车组成查询集合
		}
	},{
		text : "确定",iconCls : "saveIcon", handler: function(){
			Employee.submit(); 
		}
	}],
	fields: [{
		header:'人员主键', dataIndex:'empid', hidden:true, editor:{  maxLength:8 } 
	},{
		header:"人员代码", dataIndex:"empcode",editor:{ } 
	},{
		header:"人员名称", dataIndex:"empname",editor:{ } 
	},{
		header:"单位ID",hidden:true, dataIndex:"orgid",editor:{ } 
	},{
		header:"单位名称",hidden:true, dataIndex:"orgname",editor:{ } 
	}]
});
Employee.selectWin = new Ext.Window({
	title:"选择人员信息", maximizable:true,width:520, height:350, closeAction:"hide", modal:true, layout:"fit", items:Employee.grid
});
//移除监听
Employee.grid.un('rowdblclick', Employee.grid.toEditFn, Employee.grid);
//确认提交方法，后面可覆盖此方法完成查询
Employee.submit = function(){alert("请覆盖，Employee.submit 方法完成自己操作业务！");};

});