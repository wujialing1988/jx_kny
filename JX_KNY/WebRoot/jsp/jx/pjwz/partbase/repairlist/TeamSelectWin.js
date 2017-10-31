/**
 * 施修班组选择
 */
Ext.onReady(function(){
	Ext.namespace('TeamSelect');                       //定义命名空间
	
	TeamSelect.orgname;
	//已选择人员Grid
	TeamSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/omOrganizationSelect!pageList.action",                 //装载列表数据的请求URL    
	    tbar : ['班组名称',{	            
            xtype:"textfield",								                
            name : "parts",
	        width: 240,
            id:"partsId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				TeamSelect.orgname = Ext.getCmp("partsId").getValue();				
				TeamSelect.grid.getStore().load();
				
			},
			title : "按输入框条件查询",
			scope : this
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				delete TeamSelect.orgname;
				TeamSelect.grid.getStore().load();
				
				Ext.getCmp("partsId").setValue("");
			},
			scope : this
		}], singleSelect: true,		
		fields: [{
			header:'orgid', dataIndex:'orgid', hidden:true, editor:{  maxLength:2 }
		},{
			header:'班组编号', dataIndex:'orgcode', editor:{ maxLength:18 }
		},{
			header:'班组名称', dataIndex:'orgname', editor:{ maxLength:18 }
		}]
	});
	
	
	
	//移除事件
	TeamSelect.grid.un('rowdblclick',TeamSelect.grid.toEditFn,TeamSelect.grid);
	
	//定义点击确定按钮的操作
	TeamSelect.submit = function(){
		alert("请覆盖方法（TeamSelect.submit）！");
	}
	
	TeamSelect.grid.store.on("beforeload", function(){
		
		var sp = {};
		if(TeamSelect.orgname){
			sp.orgname = TeamSelect.orgname;
		}
		sp.orgdegree = orgdegree;
		this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
	
	//定义选择窗口
	TeamSelect.selectWin = new Ext.Window({
		title:"施修班组选择", width:600, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ TeamSelect.grid ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				TeamSelect.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ TeamSelect.selectWin.hide(); }
		}]
	});
});