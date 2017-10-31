/**
 * 配件型号选择
 */
Ext.onReady(function(){
	Ext.namespace('PartsTypeSelect');                       //定义命名空间
		
	//已选择人员Grid
	PartsTypeSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/partsType!findpageList.action",                 //装载列表数据的请求URL    
	    tbar : ['配件名称:',{	            
            xtype:"textfield",
            name : "parts",
	        width: 240,
            id:"parts_Id"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var searchText = Ext.getCmp("parts_Id").getValue();
				var sp = {};
				sp.partsName = searchText;
				PartsTypeSelect.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
				PartsTypeSelect.grid.getStore().load();
			},
			title : "按输入框条件查询",
			scope : this
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				var sp = {};
				PartsTypeSelect.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
				PartsTypeSelect.grid.getStore().load();
				
				Ext.getCmp("parts_Id").setValue("");
			},
			scope : this
		}],
		fields: [{
			header:'idx', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'配件名称', dataIndex:'partsName', editor:{  maxLength:2 }
		},{
			header:'规格型号', dataIndex:'specificationModel', editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'是否带序列号', dataIndex:'isHasSeq', editor: { xtype:'hidden' },
			renderer : function(v){if(v==0)return "否";else return "是";}
		},{
			header:'状态',dataIndex:'status',editor:{},renderer:function(v){
			  if(v==0) return "新增";
			  if(v==1) return "启用";
			  if(v==2) return "作废";
			}
		}]
	});
	
	
	
	//移除事件
	PartsTypeSelect.grid.un('rowdblclick',PartsTypeSelect.grid.toEditFn,PartsTypeSelect.grid);
	
	//定义点击确定按钮的操作
	PartsTypeSelect.submit = function(){
		alert("请覆盖方法（PartsTypeSelect.submit）！");
	}
	
	//定义选择窗口
	PartsTypeSelect.selectWin = new Ext.Window({
		title:"配件型号选择", width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ PartsTypeSelect.grid ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				PartsTypeSelect.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ PartsTypeSelect.selectWin.hide(); }
		}]
	});
});