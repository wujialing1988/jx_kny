/**
 * 生产厂家选择
 */
Ext.onReady(function(){
	Ext.namespace('OutSoucingFactorySelect');                       //定义命名空间
		
	//已选择人员Grid
	OutSoucingFactorySelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/partsOutSourcingFactory!OutSourcingFactoryList.action",                 //装载列表数据的请求URL    
	    tbar : ['委外厂家名称',{	            
            xtype:"textfield",								                
            name : "parts",
	        width: 240,
            id:"partsId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var searchText = Ext.getCmp("partsId").getValue();
				var sp = {};
				sp.specificationModel = searchText;
				OutSoucingFactorySelect.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
				OutSoucingFactorySelect.grid.getStore().load();
				
			},
			title : "按输入框条件查询",
			scope : this
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				var sp = {};
				OutSoucingFactorySelect.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
				OutSoucingFactorySelect.grid.getStore().load();
				
				Ext.getCmp("partsId").setValue("");
			},
			scope : this
		}], singleSelect: true,		
		fields: [{
			header:'委外厂家编号', dataIndex:'id', editor:{  maxLength:2 }
		},{
			header:'委外厂家名称', dataIndex:'factoryName', editor:{ maxLength:18 }
		}]
	});
	
	
	
	//移除事件
	OutSoucingFactorySelect.grid.un('rowdblclick',OutSoucingFactorySelect.grid.toEditFn,OutSoucingFactorySelect.grid);
	
	//定义点击确定按钮的操作
	OutSoucingFactorySelect.submit = function(){
		alert("请覆盖方法（OutSoucingFactorySelect.submit）！");
	}
	
	//定义选择窗口
	OutSoucingFactorySelect.selectWin = new Ext.Window({
		title:"生产厂家选择", width:600, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ OutSoucingFactorySelect.grid ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				OutSoucingFactorySelect.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ OutSoucingFactorySelect.selectWin.hide(); }
		}]
	});
});