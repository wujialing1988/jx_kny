/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('ZtgzDesign');
	
	/**
	 * 车辆状态变化
	 */
	ZtgzDesign.ZtgzRecordPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZtgzRecord.ZtgzRecordVis ]
	    }, {
	         region : 'south',   height: 350,  layout: 'fit', bodyBorder: false, items: [ZtgzRecord.Grid]
	    }]
	});
	
	
	//页面适应布局
	ZtgzDesign.viewport = new Ext.Viewport({
		layout:'fit', 
		items:[{
			layout: 'border',
			items: [{
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZtgzDesign.ZtgzRecordPanel ]
	    	},{ 		
		     	 region: 'west',  title: '车辆信息',  bodyBorder: false, split: true, width: 400, layout: 'fit',   minSize: 160, maxSize: 400, 
		     	 collapsible : true,   items:[ZtgzList.Grid]
			}]
		}]
	});
	

});