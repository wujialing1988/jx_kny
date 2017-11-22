/**
 * 班次班组维护
 */
Ext.onReady(function(){
	
	Ext.ns('ClassMaintainDesign');
	
	/**
	 * 班组及对应分队
	 */
	ClassMaintainDesign.ClassOrganizationPanel = new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ClassOrganizationUser.grid ]
	    }, {
	        region: 'west',  title: '班组选择',  bodyBorder: false, split: true, width: 400, layout: 'fit',   minSize: 160, maxSize: 400, 
		    collapsible : true, items: [ ClassOrganization.grid ]
	    }]
	});
	
	
	/**
	 * 班次
	 */
	ClassMaintainDesign.ClassMaintainPanel =  new Ext.Panel( {
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ClassMaintain.grid ]
	    }, {
	         region : 'south',   height: 300,  layout: 'fit', bodyBorder: false, items: [ ClassMaintainDesign.ClassOrganizationPanel ]
	    }]
	});
	
	/**
	 * 维护数据tab
	 */
	ClassMaintainDesign.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "班次维护", border: false, xtype: "panel", layout: "fit",id:'ClassMaintainDesign_tab',
	            items: [
	            	ClassMaintainDesign.ClassMaintainPanel
	            ],
	           	listeners:{
	        		activate:function(){
	        			ClassMaintain.grid.getStore().reload();
	        		}	        	
	        	}
	        }/*,{
	            title: "物料维护", border: false, xtype: "panel", layout: "fit", id:'WhMatQuota_tab',
	            items:[
	            	WhMatQuota.grid
	            ],
		        listeners:{
	        		activate:function(){
	        			WhMatQuota.grid.getStore().reload();
	        		}	        	
	        	}
	        }*/]
	});
	
	
	
	//页面适应布局
	ClassMaintainDesign.viewport = new Ext.Viewport({
		layout:'fit', 
		items:[{
			layout: 'border',
			items: [{
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [ ClassMaintainDesign.tabs ]
	    	},{ 		
		     	 region: 'west',  title: '站点',  bodyBorder: false, split: true, width: 400, layout: 'fit',   minSize: 160, maxSize: 400, 
		     	 collapsible : true,   items:[ClassMaintain.WorkPlaceGrid]
			}]
		}]
	});
	

});