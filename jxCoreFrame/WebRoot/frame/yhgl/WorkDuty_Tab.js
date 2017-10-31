/**
 * 职务 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('DutyTab');
	
	DutyTab.DutyTypeNodeId;//当前选中节点职务类别ID
	DutyTab.DutyNodeId;//当前选中职务节点ID
	DutyTab.currentNodeType; //当前选中节点类型(dict职务类型，duty职务)
	DutyTab.currentNode; //node

	//页面自适应布局
	DutyTab.topTab = new Ext.Panel({
		layout : 'fit',
		border : false,
		activeItem : 0,
		baseCls: "x-plain",
		items : [{
			layout : 'fit',
			border : false,
			height : 28,
			items : {
				id:'_tabPanel',
				xtype : "tabpanel",
				activeTab : 0,
				enableTabScroll : true,
				border : false,
				items : [{
					//index：0
					title : "全部职务",
					border : false,
					layout : "fit",
					items : [DutyList.grid],
					listeners : {
				 		"activate" : function() {
				 			DutyList.grid.store.load();
					 	}
					}
				}, {
					//index：1
					title : "本职务信息",
					border : false,
					layout : "fit",
					items : [DutyForm.panel],
					listeners : {
						"activate" : function() {
							DutyForm.findCurrentDutyInfo();
					 	}
					}
				}, {
					//index：2
					title : "下级职务",
					border : false,
					layout : "fit",
					items : [LowerDutyList.grid],
					listeners : {
						"activate" : function() {
							if(DutyTab.DutyTypeNodeId!=null){
								LowerDutyList.grid.store.load();
				 			}
						}
					}
				}, {
					//index：3
					title : "职务人员",
					border : false,
					layout : "fit",
					items : [emplist.grid],
					listeners : {
						"activate" : function() {
							if(DutyTab.DutyTypeNodeId!=null){
								emplist.grid.store.load();
				 			}
						}
					}
				}],
				listeners : {
					render : function(){
						DutyTab.hideTabPanelMethod(0);
					}
				}
			}
		}]
	});
	
	/**
	 * 页面tabPanel显示与隐藏方法
	 */
	DutyTab.hideTabPanelMethod = function(arg){
		if(arg == 1){
			Ext.getCmp('_tabPanel').unhideTabStripItem(0);   //显示 全部职务
			Ext.getCmp('_tabPanel').hideTabStripItem(1);     //隐藏 本职务信息
			Ext.getCmp('_tabPanel').hideTabStripItem(2);     //隐藏 下级职务
			Ext.getCmp('_tabPanel').hideTabStripItem(3);     //隐藏 职务人员
			Ext.getCmp('_tabPanel').setActiveTab(0);
		} else if (arg == 2){
			Ext.getCmp('_tabPanel').hideTabStripItem(0);     //隐藏 全部职务
			Ext.getCmp('_tabPanel').unhideTabStripItem(1);     //显示 本职务信息
			Ext.getCmp('_tabPanel').unhideTabStripItem(2);     //显示 下级职务
			Ext.getCmp('_tabPanel').unhideTabStripItem(3);     //显示 职务人员
			Ext.getCmp('_tabPanel').setActiveTab(1);
		} else {
			Ext.getCmp('_tabPanel').unhideTabStripItem(0);   //显示 全部职务
			Ext.getCmp('_tabPanel').hideTabStripItem(1);     //隐藏  本职务信息
			Ext.getCmp('_tabPanel').hideTabStripItem(2);     //隐藏 下级职务
			Ext.getCmp('_tabPanel').hideTabStripItem(3);     //隐藏 职务人员
			Ext.getCmp('_tabPanel').setActiveTab(0);
		}
	}
});