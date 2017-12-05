/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpPlanDesign');
	
	
	
	/**
	 * 已完成 未完成的列检计划
	 */
	ZbglRdpPlanDesign.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "未完成", border: false, xtype: "panel", layout: "fit", 
	            items: [
	            	ZbglRdpPlan.ZbglRdpPlanGrid
	            ],
	           	listeners:{
	        		activate:function(){
	        			ZbglRdpPlan.ZbglRdpPlanGrid.getStore().reload();
	        		}	        	
	        	}
	        },{
	            title: "已完成", border: false, xtype: "panel", layout: "fit", 
	            items:[
	            	ZbglRdpPlan.ZbglRdpPlanCompletedGrid
	            ],
		        listeners:{
	        		activate:function(){
	        			ZbglRdpPlan.ZbglRdpPlanCompletedGrid.getStore().reload();
	        		}	        	
	        	}
	        }]
	});
	
	/**
	 * 列检计划信息
	 */
	ZbglRdpPlanDesign.ZbglRdpPlanPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZbglRdpPlanDesign.tabs ]
	    }, {
	         region : 'south',   height: 200,  layout: 'fit', bodyBorder: false, items: [ZbglRdpPlan.infoForm]
	    }]
	});
	
	/**
	 * 车辆tab
	 */
	ZbglRdpPlanDesign.recordTabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "作业车辆", border: false, xtype: "panel", layout: "fit", 
	            items: [
	            	ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid
	            ],
	           	listeners:{
	        		activate:function(){
	        			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();
	        		}	        	
	        	}
	        },{
	            title: "作业人员", border: false, xtype: "panel", layout: "fit", 
	            items:[
	            	ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid
	            ],
		        listeners:{
	        		activate:function(){
	        			ZbglRdpPlanRecord.ZbglRdpPlanPersonGrid.getStore().reload();
	        		}	        	
	        	}
	        }]
	});
	
	
	
	/**
	 * 车辆计划
	 */
	ZbglRdpPlanDesign.ZbglRdpPlanRecordPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZbglRdpPlanRecord.ZbglRdpPlanRecordVis ]
	    }, {
	         region : 'south', title: '作业情况查看', collapsible:true,  height: 350,  layout: 'fit', bodyBorder: false, items: [ZbglRdpPlanDesign.recordTabs]
	    }]
	});
	
	
	//页面适应布局
	ZbglRdpPlanDesign.viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
        region: 'center', bodyBorder: false,
        layout: 'fit', items : [ ZbglRdpPlanDesign.ZbglRdpPlanRecordPanel ]
    	},{ 		
	     	 region: 'west',  title: '列检计划',  bodyBorder: false, split: true, collapseMode: 'mini',
	     	defaults: {border: false},
	     	 width: 400, layout: 'fit',   minSize: 160, maxSize: 400, 
	     	 collapsible : true,   items:[ZbglRdpPlanDesign.ZbglRdpPlanPanel]
		}]
	});
	

});