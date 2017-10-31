/**
 * 机车零部件选择
 */
Ext.onReady(function(){
	Ext.namespace('JcpjzdBuildSelect');                       //定义命名空间
		
	//已选择人员Grid
	JcpjzdBuildSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/jcpjzdBuild!pageQuery.action",                 //装载列表数据的请求URL    
	    storeAutoLoad: false,
	    tbar : ['标准名称:',{	            
            xtype:"textfield",
            name : "parts",
	        width: 240,
            id:"parts_Id"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				JcpjzdBuildSelect.grid.getStore().load();
			},
			title : "按输入框条件查询",
			scope : this
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				Ext.getCmp("parts_Id").setValue("");
				JcpjzdBuildSelect.grid.getStore().load();
			},
			scope : this
		}],
		fields: [{
			header:'idx', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'jcpjbm', editor:{  maxLength:2 }
		},{
			header:'标准名称', dataIndex:'jcpjmc', editor:{  maxLength:2 }
		}]
	});
	//移除事件
	JcpjzdBuildSelect.grid.un('rowdblclick',JcpjzdBuildSelect.grid.toEditFn,JcpjzdBuildSelect.grid);
	JcpjzdBuildSelect.grid.store.on('beforeload',function(){
		 var searchText = Ext.getCmp("parts_Id").getValue();
	     //排除已添加是零部件
	     var sqlStr = " jcpjbm not in (select jcpjbm from PJWZ_Parts_Outsourcing_Catalog) ";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
	     whereList.push({propName:"jcpjmc",propValue:searchText,compare:Condition.LIKE});
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	//定义点击确定按钮的操作
	JcpjzdBuildSelect.submit = function(){
		alert("请覆盖方法（JcpjzdBuildSelect.submit）！");
	}
	//定义选择窗口
	JcpjzdBuildSelect.selectWin = new Ext.Window({
		title:"零部件名称选择", width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ JcpjzdBuildSelect.grid ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				JcpjzdBuildSelect.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ JcpjzdBuildSelect.selectWin.hide(); }
		}]
	});
});