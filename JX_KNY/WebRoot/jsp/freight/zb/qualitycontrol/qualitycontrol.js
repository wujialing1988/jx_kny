/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	//页面自适应布局
	new Ext.Viewport({
		layout:'border', 
		items:[{
			region:'west',
			width:550,
			layout:'fit',
			items:QualityControlItemDefine.grid,
			split:true
		}, {
			region:'center',
			layout:'fit',
			items:QualityControlItemEmpDefine.grid
		}]
	});
	
});