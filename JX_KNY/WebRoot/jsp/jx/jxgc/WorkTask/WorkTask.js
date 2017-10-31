Ext.onReady(function(){
	Ext.namespace('WorkTask');
	
	WorkTask.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "待处理任务", border: false, xtype: "panel", layout: "border",
	            items:[{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
		            collapsible:true, collapsed: true, height: 180, bodyBorder: false,
		            items:[handler.searchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ handler.grid ]
		        }],
	            listeners:{
	    			"activate":function(){
	    				try{
	    				Attachment.enableButton(['新增','删除']);
	    				}catch(e){}
	    			}
	    		}
	        },{
	        	title: "已处理任务", border: false, xtype: "panel", layout: "border",
	        	items:[{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
		            collapsible:true, collapsed: true, height: 180, bodyBorder: false,
		            items:[handled.searchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ handled.grid ]
		        }],
	            listeners:{
	    			"activate":function(){
	        			try{
	        				Attachment.disableButton(['新增','删除']);
	        			}catch(e){}
	    			}
	    		}
	        }]
	});
	
	var viewport = new Ext.Viewport({layout:"fit", items:[ WorkTask.tabs ]});
});