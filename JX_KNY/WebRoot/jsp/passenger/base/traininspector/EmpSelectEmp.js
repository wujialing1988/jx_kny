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
    autoScroll : true,
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
		text:i18n.EmpSelectEmp.empName
	},{	            
        xtype:"textfield",  id:"empName_Id", width: 100
	},{
		text : i18n.EmpSelectEmp.search, iconCls : "searchIcon", title: i18n.EmpSelectEmp.searchBy, width: 40,
		handler : function(){
			EmployeeSelect.grid.getStore().load();
		}
	},{
		text :i18n.EmpSelectEmp.confirm,iconCls : "saveIcon", handler: function(){
			EmployeeSelect.submit(); 
		}
	},{
		text :i18n.EmpSelectEmp.cancle,iconCls : "closeIcon", handler: function(){
			EmployeeSelect.selectWin.hide(); 
		}
	}],
	fields: [{
		header:i18n.EmpSelectEmp.empid, dataIndex:'empid', hidden:true, editor:{  maxLength:8 } 
	},{
		header:i18n.EmpSelectEmp.empcode, dataIndex:"empcode",width:80,editor:{ } 
	},{
		header:i18n.EmpSelectEmp.empName1, dataIndex:"empname",width:80,editor:{ } 
	},{
		header:i18n.EmpSelectEmp.gender,dataIndex:"gender",width:60,editor:{ },
		renderer : function(v){
			if(v == 'm') return i18n.EmpSelectEmp.M
			else if(v == 'f') return i18n.EmpSelectEmp.W
			else return null
		}
	},{
		header:i18n.EmpSelectEmp.orgid,hidden:true, dataIndex:"orgid",editor:{ } 
	},{
		header:i18n.EmpSelectEmp.orgname,dataIndex:"orgname",width:100,editor:{ } 
	},{
		header:i18n.EmpSelectEmp.empStatus,hidden:true, dataIndex:"empstatus",editor:{ } 
	}]
});
EmployeeSelect.grid.store.on('beforeload',function(){
	     this.baseParams.emp = Ext.getCmp("empName_Id").getValue();
	     this.baseParams.orgseq = EmployeeSelect.orgSeq;
	});
EmployeeSelect.selectWin = new Ext.Window({
	title:i18n.EmpSelectEmp.empInforChoice, maximizable:true,width:650, height:350, closeAction:"hide", modal:true, layout : "border",
			items : [new Ext.Panel({
						width : 200,
						minSize : 160,
						maxSize : 280,
						region : "west",
						collapsible: true,
						title : '<span style="font-weight:normal">'+i18n.EmpSelectEmp.instituteTree+'</span>',
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
	   	  MyExt.Msg.alert(i18n.EmpSelectEmp.Nochoice);
	   	  return ;
	   }
	   var count=records.length;
	   var datas = new Array();
		for (var i = 0; i < count; i++) {
			var record = records[i];
			var data = {} ;
			data.idx = "" ;
			data.orgid = record.data.orgid;
			data.orgname = record.data.orgname;  //所属班组
			data.empid = record.data.empid ;
			data.empcode = record.data.empcode ;
			data.empname = record.data.empname ;
			data.gender = record.data.gender ;
			data.empstatus = record.data.empstatus ;
			datas.push(data);
		}
	   Ext.Ajax.request({
	        url: ctx + '/trainInspector!saveFromEmployee.action',
	        params : {datas : Ext.util.JSON.encode(datas)},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	                alertSuccess();
	                EmployeeSelect.grid.store.reload();
	                TrainInspector.grid.store.reload();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	            MyExt.Msg.alert(i18n.EmpSelectEmp.false+"\n" + response.status + "\n" + response.responseText);
	        }
	    });
};

});