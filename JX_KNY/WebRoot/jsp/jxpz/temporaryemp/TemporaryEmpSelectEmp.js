Ext.onReady(function(){
Ext.namespace('EmployeeSelect');                       //定义命名空间
EmployeeSelect.searchParam = {} ;  //定义全局查询条件
EmployeeSelect.orgSeq = "";//机构序列
//机构选择树
EmployeeSelect.OrgTree = new Ext.tree.TreePanel( {
	tbar :new Ext.Toolbar(),
	plugins: ['multifilter'],
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/omOrganizationSelect!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text : systemOrgname,
		id : systemOrgid,
		leaf : false,
		orgseq : ''
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	EmployeeSelect.orgSeq = node.attributes["orgseq"];
        	EmployeeSelect.grid.store.reload();
        }
    }    
});
EmployeeSelect.OrgTree.on('beforeload', function(node){
    EmployeeSelect.OrgTree.loader.dataUrl = ctx + '/omOrganizationSelect!tree.action?parentIDX=' + node.id;
});
//EmployeeSelect.OrgTree.getRootNode().expand();
EmployeeSelect.grid = new Ext.yunda.Grid({ 
    loadURL: ctx + '/omEmployeeSelect!pageList.action',                 //装载列表数据的请求URL
    storeAutoLoad: false, storeId : "empid",
    tbar:[{
		xtype:"label",
		text:"人员姓名："
	},{	            
        xtype:"textfield",  id:"empName_Id", width: 100
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			EmployeeSelect.grid.getStore().load();
		}
	},{
		text : "确定",iconCls : "saveIcon", handler: function(){
			EmployeeSelect.submit(); 
		}
	},{
		text : "取消",iconCls : "closeIcon", handler: function(){
			EmployeeSelect.selectWin.hide(); 
		}
	}],
	fields: [{
		header:'人员主键', dataIndex:'empid', hidden:true, editor:{  maxLength:8 } 
	},{
		header:"人员代码", dataIndex:"empcode",width:80,editor:{ } 
	},{
		header:"人员名称", dataIndex:"empname",width:80,editor:{ } 
	},{
		header:"性别",dataIndex:"gender",width:60,editor:{ },
		renderer : function(v){
			if(v == 'm') return "男"
			else if(v == 'f') return "女"
			else return null
		}
	},{
		header:"单位ID",hidden:true, dataIndex:"orgid",editor:{ } 
	},{
		header:"所属班组",dataIndex:"orgname",width:100,editor:{ } 
	},{
		header:"人员状态",hidden:true, dataIndex:"empstatus",editor:{ } 
	}]
});
EmployeeSelect.grid.store.on('beforeload',function(){
	     this.baseParams.emp = Ext.getCmp("empName_Id").getValue();
	     this.baseParams.orgseq = EmployeeSelect.orgSeq;
	});
EmployeeSelect.selectWin = new Ext.Window({
	title:"选择人员信息", maximizable:true,width:650, height:350, closeAction:"hide", modal:true, layout : "border",
			items : [new Ext.Panel({
						width : 200,
						minSize : 160,
						maxSize : 280,
						region : "west",
						collapsible: true,
						title : '<span style="font-weight:normal">机构树</span>',
			        	iconCls : 'icon-expand-all',
			        	tools : [ {
				            id : 'refresh',
				            handler: function() {
				            	EmployeeSelect.OrgTree.getRootNode().reload();
				            }
				        }],
						items : [EmployeeSelect.OrgTree],
						autoScroll : true
					}), new Ext.Panel({
							region : "center",
							layout : "fit",
							items : [EmployeeSelect.grid]
						})]
});
//移除监听
EmployeeSelect.grid.un('rowdblclick', EmployeeSelect.grid.toEditFn, EmployeeSelect.grid);
//确认提交
EmployeeSelect.submit = function(){
	   var records=EmployeeSelect.grid.selModel.getSelections();
	   if(records.length<1){
	   	  MyExt.Msg.alert("尚未选择一条记录！");
	   	  return ;
	   }
	   var count=records.length;
	   var datas = new Array();
		for (var i = 0; i < count; i++) {
			var record = records[i];
			var data = {} ;
			data.idx = "" ;
			data.temporaryTreamId = TemporaryEmp.orgId ;  //临时班组id
			data.temporaryTreamName = TemporaryEmp.orgName ;  //临时班组名称
			data.temporaryTreamSeq = TemporaryEmp.orgSeq ;  //临时班组序列
			data.oldTream = record.data.orgname;  //原所属班组
			data.empid = record.data.empid ;
			data.empcode = record.data.empcode ;
			data.empname = record.data.empname ;
			data.gender = record.data.gender ;
			data.empstatus = record.data.empstatus ;
			datas.push(data);
		}
	   Ext.Ajax.request({
	        url: ctx + '/temporaryEmp!saveFromEmployee.action',
	        jsonData: datas,
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	                alertSuccess();
	                EmployeeSelect.grid.store.reload();
	                TemporaryEmp.grid.store.reload();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
	    });
};

});