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
	            title: i18n.TrainInspectionPlan.unfinished, border: false, xtype: "panel", layout: "fit", 
	            items: [
	            	ZbglRdpPlan.ZbglRdpPlanGrid
	            ],
	           	listeners:{
	        		activate:function(){
	        			ZbglRdpPlan.ZbglRdpPlanGrid.getStore().reload();
	        		}	        	
	        	}
	        },{
	            title: i18n.TrainInspectionPlan.finished, border: false, xtype: "panel", layout: "fit", 
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
	            title: i18n.TrainInspectionPlan.accoMajor, border: false, xtype: "panel", layout: "fit", 
	            items: [
	            		ZbglRdpPlanRecord.grid
	            ],
	           	listeners:{
	        		activate:function(){
	        			ZbglRdpPlanRecord.grid.getStore().load();
	        		}	        	
	        	}
	        },{
	            title: i18n.TrainInspectionPlan.accoVehicle, border: false, xtype: "panel", layout: "fit", 
	            items: [
	            		ZbglRdpPlanRecordCL.grid
	            ],
	           	listeners:{
	        		activate:function(){
	        			ZbglRdpPlanRecordCL.grid.getStore().load();
	        		}	        	
	        	}
	        }]
	});
	
	
	
	/**
	 * 专业车辆tab
	 */
	ZbglRdpPlanDesign.ZbglRdpPlanRecordPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZbglRdpPlanDesign.recordTabs ]
	    }]
	});
	
	
	//页面适应布局
	ZbglRdpPlanDesign.viewport = new Ext.Viewport({
		layout:'fit', 
		items:[{
			layout: 'border',
			items: [{
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ZbglRdpPlanDesign.ZbglRdpPlanRecordPanel ]
	    	},{ 		
		     	 region: 'west',  title: i18n.TrainInspectionPlan.inspectionPlanTitle,  bodyBorder: false, split: true, width: 400, layout: 'fit',   minSize: 160, maxSize: 400, 
		     	 collapsible : true,   items:[ZbglRdpPlanDesign.ZbglRdpPlanPanel]
			}]
		}]
	});
	

});