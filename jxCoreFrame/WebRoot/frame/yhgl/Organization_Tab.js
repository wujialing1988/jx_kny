/**
 * 机构人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('OrgTab');
	
	OrgTab.OrgNodeId;//当前选中节点所属机构节点ID
	OrgTab.PosNodeId;//当前选中节点所属岗位节点ID
	OrgTab.currentNodeType; //当前选中节点类型
	OrgTab.OrgNodeName; //当前选中节点所属机构名称
	OrgTab.parentTreeNode;  

	//页面自适应布局
	OrgTab.topTab = new Ext.Panel({
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
					title : "本机构信息",
					border : false,
					layout : "fit",
					items : [orgForm.panel],
					listeners : {
				 		"activate" : function() {
				 			if(OrgTab.OrgNodeId!=null){
								orgForm.findCurrentOrgInfo();
				 			}
					 	}
					}
				}, {
					//index：1
					title : "下级机构",
					border : false,
					layout : "fit",
					items : [orglist.grid],
					listeners : {
						"activate" : function() {
							orglist.grid.store.load();
					 	}
					}
				}, {
					//index：2
					title : "本岗位信息",
					border : false,
					layout : "fit",
					items : [posForm.panel],
					listeners : {
						"activate" : function() {
							if(OrgTab.OrgNodeId!=null){
								posForm.findCPosInfo();
				 			}
						}
					}
				}, {
					//index：3
					title : "下级岗位",
					border : false,
					layout : "fit",
					items : [positionlist.grid],
					listeners : {
						"activate" : function() {
							if(OrgTab.OrgNodeId!=null){
								positionlist.grid.store.load(); //加载下级岗位列表
				 			}
						}
					}
				}, {
					title : "机构下属人员", //机构下属人员
					border : false,
					layout : "fit",
					items : [emplist.grid],
					listeners : {
						"activate" : function() {
							emplist.grid.store.load();
					 	}
					}
				}, {
					title : "岗位下属人员", //岗位下属人员
					border : false,
					layout : "fit",
					items : [emplist2.grid],
					listeners : {
						"activate" : function() {
							emplist2.grid.store.load();
					 	}
					}
				}, {
					title : "人员信息",
					border : false,
					layout : "fit",
					items : [empForm.panel],
					listeners : {
						"activate" : function() {
							if(OrgTab.OrgNodeId!=null){
								empForm.findCEmpInfo();
				 			}
						}
					}
				}],
				listeners : {
					render : function(){
						OrgTab.hideTabPanelMethod(0);
					}
				}
			}
		}]
	});
	
	/**
	 * 页面tabPanel显示与隐藏方法
	 */
	OrgTab.hideTabPanelMethod = function(arg){
		if(arg == 1){
			Ext.getCmp('_tabPanel').unhideTabStripItem(0); //显示 本机构信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(1); //显示 下级机构列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(3);   //显示 下级岗位列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(4);   //显示 机构下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 岗位下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(6);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(0);
		} else if (arg == 2){
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本机构信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(1);   //隐藏 下级机构列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(2); //显示 本岗位信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(3);   //显示 下级岗位列表
			Ext.getCmp('_tabPanel').hideTabStripItem(4);   //隐藏 机构下级人员信息列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(5);   //显示 岗位下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(6);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(2);
		} else if (arg == 3){
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本机构信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(1);   //隐藏 下级机构列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(3);   //隐藏 下级岗位列表
			Ext.getCmp('_tabPanel').hideTabStripItem(4);   //隐藏 机构下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 岗位下级人员信息列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(6);   //显示 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(6);
		} else {
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本机构信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(1);   //显示 下级机构列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(3);   //隐藏 下级岗位列表
			Ext.getCmp('_tabPanel').hideTabStripItem(4);   //隐藏 机构下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 岗位下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(6);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(1);
		}
	}
});