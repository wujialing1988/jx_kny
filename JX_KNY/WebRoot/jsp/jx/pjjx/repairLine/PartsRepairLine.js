/**
 * 配件检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRepairLine');                       //定义命名空间
	
	PartsRepairLine.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/partsRepairLine!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRepairLine!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRepairLine!logicDelete.action',            //删除数据的请求URL
	    tbar: ['流水线名称',{	            
            xtype:"textfield",								                
            name : "repairLineName",
            id:"repairLineNameId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				PartsRepairLine.grid.getStore().load();
			},
			title : "按输入框条件查询",
			scope : this
		},'add','delete','refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'流水线编码', dataIndex:'repairLineCode',width:80, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'流水线名称', dataIndex:'repairLineName', width:100,editor:{allowBlank:false,  maxLength:100 }
		},{
			header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
		}],
		listeners: {
			"rowclick" : function(grid, index, e){
				var r = grid.store.getAt(index);
				PartsWorkStation.repairLineIdx = r.get("idx");
				PartsWorkStation.repairLineName = r.get("repairLineName");
				//取消行编辑
				PartsWorkStation.grid.rowEditor.stopEditing(false); 
				PartsWorkStation.grid.store.load();
			}
		}
	});
	
	PartsRepairLine.grid.store.on('beforeload',function(){
	     var repairLineName = Ext.getCmp("repairLineNameId").getValue();
	     if (Ext.isEmpty(repairLineName)) return;
	     this.baseParams.entityJson = Ext.util.JSON.encode({
	     	repairLineName: repairLineName
	     });
	});
	
	// 重新加载配件检修流水线数据后，清空配件检修工位数据
	PartsRepairLine.grid.store.on('load',function(){
		PartsWorkStation.grid.store.removeAll();
	});
		
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout: "border", 
        items:[{
        	title:'流水线',
            region: 'west', layout: "fit", autoScroll : true, 
            width: 500, /*minSize: 150, maxSize: 280, */split: true, bodyBorder: false,
            items: [PartsRepairLine.grid]
        },{
           title:'工位',region : 'center', layout : 'fit', bodyBorder: false, 
           items : [ PartsWorkStation.grid ]
        }]
	});
	
});