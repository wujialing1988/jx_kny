/**
 * 扣车管理布局页面
 */
Ext.onReady(function(){
	
	Ext.ns('DetainTrainDesign');
	
	
	/**
	 * 未审批 已审批的扣车列表
	 */
	DetainTrainDesign.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "未检修", border: false, xtype: "panel", layout: "fit", 
	            items: [
	            	DetainTrain.grid
	            ],
	           	listeners:{
	        		activate:function(){
	        			DetainTrain.grid.getStore().reload();
	        		}	        	
	        	}
	        },{
	            title: "已检修", border: false, xtype: "panel", layout: "fit", 
	            items:[
	            	DetainTrainCompleted.grid
	            ],
		        listeners:{
	        		activate:function(){
	        			DetainTrainCompleted.grid.getStore().reload();
	        		}	        	
	        	}
	        }]
	});
	
	
	
	//页面适应布局
	DetainTrainDesign.viewport = new Ext.Viewport({
		layout:'fit', 
		items:DetainTrainDesign.tabs
	});
	

});