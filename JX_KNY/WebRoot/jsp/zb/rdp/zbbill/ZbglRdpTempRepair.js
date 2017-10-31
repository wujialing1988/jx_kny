/**
 * 转临修 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpTempRepair');                       //定义命名空间
ZbglRdpTempRepair.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{
            title: "碎修", border: false, xtype: "panel", layout: "border", 
            items: [{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	            collapsible:true, collapsed: true, height: 120, bodyBorder: false,
	            items:[ZbglRdp.SXSearchForm], frame: true, title: "查询", xtype: "panel"
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglRdp.SXGrid ]
	        }]
        },{
            title: "临修", border: false, xtype: "panel", layout: "border", 
            items:[{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	            collapsible:true, collapsed: true, height: 120, bodyBorder: false,
	            items:[ZbglRdp.LXSearchForm], frame: true, title: "查询", xtype: "panel"
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglRdp.LXGrid ]
	        }]
        }]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglRdpTempRepair.tabs });
});