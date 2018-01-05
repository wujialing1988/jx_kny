/**
 * 职务信息主js文件，本文件构建主体页面布局
 */
Ext.onReady(function(){
	Ext.namespace('dutyFrame');                       //职务树命名空间
	
	/**
	 * 职务树图例
	 */
	dutyFrame.dutyIconCase = new Ext.Panel({
		layout : 'column',
		frame : false,
		xtype : 'panel',	
		border : false,	
		align : 'center',	
		items : [{
			xtype: 'panel',	border:false, layout:'column', align:'center', columnWidth:0.2, 
			items : []
		},{
			xtype: 'panel',	border:false, layout:'column', align:'center', columnWidth:0.4, 
			items : [{
				border : false, height : 3
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/folder_user.png"/><label style="font-size:13px"> '+i18n.WorkDuty.classification+' </label>'
			}]
		},{
			xtype: 'panel',	border:false,	layout:'column',	align:'center', columnWidth:0.4, 
			items : [{
				border : false, height : 3
			},{
				border : false,
				height : 28,				
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/user_gray.png"/><label style="font-size:13px"> '+i18n.WorkDuty.posit+' </label>'
			}]
		}]
	});
	
	//职务树及图例布局
	dutyFrame.dutyTreePanel = new Ext.Panel( {
	    layout : 'border',
	    border : false,
	    items : [{
	        region : 'center',  //border布局主区域
	        bodyBorder: false,  //不显示边框
	        autoScroll : true,  //自动滚动条
	        layout : 'fit',     //自动填充
	        items : [dutytree.tree]
	    }, {
	    	collapsible : true, //面板可收缩
	    	title : '图示标识',	//标题
	        region : 'south',   //位于主区域下部
			height : 140,       //图示区域高度
			bodyBorder: false,	//自动滚动条
//	        baseCls: "x-plain",
	        items : [dutyFrame.dutyIconCase]
	    } ]
	});
	
	
	//页面主框架布局，左：职务树及图例，右：多tab列表
	var viewport = new Ext.Viewport({ 
		layout : 'border', 
		border : false,
		items : [{
			region : 'west',
			xtype : 'panel',
			width : 200,
			border : true,
			layout : 'fit',
			items : dutyFrame.dutyTreePanel
		},{
			id: 'censusPanel',
			region:'center',
			layout:'fit',
			xtype:'panel',
			border:true,
			bodyStyle : "background:#dae7f6;border-style:solid;border-top:1px solid #99bbe8;",
			autoScroll:true,
			items:[DutyTab.topTab]//dutyTab.topTab
		}]
	});
});