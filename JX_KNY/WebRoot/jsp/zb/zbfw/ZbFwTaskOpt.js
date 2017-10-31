/**
 * 整备作业项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbFwTaskOpt'); //定义命名空间

	/* ************* 定义全局变量开始 ************* */
	ZbFwTaskOpt.labelWidth = 80;
	ZbFwTaskOpt.fieldWidth = 140;
	/* ************* 定义全局变量开始 ************* */

	
  	 //作业项目和车型tab
function creattabs(){
   ZbFwTaskOpt.tabs = new Ext.TabPanel({
	  activeTab:0,
	  frame:true,
	  items:[{
	    title:"作业项目",layout:"fit",border:false,items:[ ZbFwWi.grid ]
	  },{  
	    title:"车型",layout:"fit",border:false,items:[ ZbFwcx.grid ]
	  }]
	});
 }

	
	//整备作业项维护基本信息
function creatbaseForm(){
	ZbFwTaskOpt.baseForm = new Ext.form.FormPanel({
		layout: "column",
		labelWidth:ZbFwTaskOpt.labelWidth,
		border: false,
		defaults:{
			 
			layout:'form', 
			defaultType:'textfield',
			defaults:{
				style:"border:none;background:none;", readOnly:true, anchor:'100%'
			}
		},
		items:[{
			columnWidth: 0.33,
            items: [
            	{ fieldLabel:"编号", name:"fwCode"},
            	{ name:"idx", xtype: "hidden"},
            	{ name:"siteID", xtype: "hidden"},
            	{ name:"siteName", xtype: "hidden"}
            ]
		},{
			columnWidth: 0.66,
            items: [
            	{fieldLabel:"名称", name:"fwName"}
            ]
		}, {
			columnWidth: 1,
            items: [
            	{ xtype:'textarea',fieldLabel:"描述", name:"fwDesc"}
            ]
		}]
  });
}
  
	//整备范围作业项维护win弹出框
function createditWin(){
	 ZbFwTaskOpt.editWin = new Ext.Window({
			title: "整备范围作业项维护",width:700, height:500, maximizable:false, layout: "fit", 
			closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
			items: {
				xtype: "panel", layout: "border",frame:true,
				items:[{
				        layout: "fit",
				        region : 'north',
				        height:120,
				        xtype: "panel",
		    		    title: "基本信息",
		    		    collapsible: true,
				        items:[ ZbFwTaskOpt.baseForm ]
					 },{
				        layout: "fit",
				        region : 'center',
				        items:[ ZbFwTaskOpt.tabs ]
					 }
		  ]},
		  buttons: [{
	           text: "关闭", iconCls: "closeIcon", handler: function(){ 
	           ZbFw.grid.store.reload();
	           ZbFwTaskOpt.editWin.hide();
	          
	           }
	        }]
	  });
	   
}

  // 加载整备范围作业项数据
  ZbFwTaskOpt.showWin = function(record){
  	if(ZbFwTaskOpt.tabs == null || ZbFwTaskOpt.tabs == undefined){
			 creattabs();
		}
    if(ZbFwTaskOpt.baseForm == null || ZbFwTaskOpt.baseForm == undefined){
			  creatbaseForm();
	 }
	if(ZbFwTaskOpt.editWin == null || ZbFwTaskOpt.editWin == undefined){
			 createditWin();
	 }
	ZbFwTaskOpt.baseForm.getForm().loadRecord(record);
	ZbFwTaskOpt.editWin.show();
	}	
});