/**
 * 机车交验上砂查询--机车整备合格交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
  Ext.namespace('ZbglSandingSearch');
	  ZbglSandingSearch.fieldWidth = 120;
	  ZbglSandingSearch.labelWidth = 90;
  
	  ZbglSandingSearch.form = new Ext.form.FormPanel({
		labelWidth:ZbglSandingSearch.labelWidth,
		border: false,
		labelAlign:"left",
		layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
	        items: [
	        	{ fieldLabel:"上砂人员", name:"dutyPersonName"},
	        	{ fieldLabel:"上砂开始时间", name:"startTime" },
	        	{ fieldLabel:"标准用时(分钟)", name:"standardSandingTime"}
	        ]
		},{
	        items: [
	        	{ fieldLabel:"上砂用时(分钟)", name:"sandingTime"},
	        	{ fieldLabel:"结束上砂时间", name:"endTime" }
	        ]
		}]
	});

});