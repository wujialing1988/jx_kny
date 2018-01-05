/**
 * 工作组 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WGTab');
	
	WGTab.GroupNodeId;//当前选中节点所属工作组节点ID
	WGTab.PosNodeId;//当前选中节点所属岗位节点ID
	WGTab.currentNodeType; //当前选中节点类型

	//页面自适应布局
	WGTab.topTab = new Ext.Panel({
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
					title : i18n.WorkGroup_Tab.teamInfor,
					border : false,
					layout : "fit",
					items : [WGForm.panel],
					listeners : {
				 		"activate" : function() {
				 			if(WGTab.GroupNodeId!=null){
				 				WGForm.findCurrentGroupInfo();
				 			}
					 	}
					}
				}, {
					//index：1
					title : i18n.WorkGroup_Tab.SuborGroup,
					border : false,
					layout : "fit",
					items : [WGList.grid],
					listeners : {
						"activate" : function() {
							WGList.grid.store.load();
					 	}
					}
				}, {
					//index：2
					title : i18n.WorkGroup_Tab.postInfor,
					border : false,
					layout : "fit",
					items : [posForm.panel],
					listeners : {
						"activate" : function() {
							if(WGTab.GroupNodeId!=null){
								posForm.findCPosInfo();
				 			}
						}
					}
				}, {
					//index：3
					title : i18n.WorkGroup_Tab.LowerPosit,
					border : false,
					layout : "fit",
					items : [positionlist.grid],
					listeners : {
						"activate" : function() {
							if(WGTab.GroupNodeId!=null){
								positionlist.grid.store.load();
				 			}
						}
					}
				}, {
					title : i18n.WorkGroup_Tab.Subordinates,
					border : false,
					layout : "fit",
					items : [emplist.grid],
					listeners : {
						"activate" : function() {
							emplist.grid.store.load();
							//因工作组/岗位下的人员列表采用同一个表格组件，这里根据用户点击的节点类型动态改变表头标题
							//工作组的下属人员
							if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'gop'){
								emplist.grid.getColumnModel().setColumnHeader(7,i18n.WorkGroup_Tab.workingGroup);
							}
							//岗位的下属人员
							else if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'pos'){
								emplist.grid.getColumnModel().setColumnHeader(7,i18n.WorkGroup_Tab.TheirPositions);
							}
					 	}
					}
				}, {
					title : i18n.WorkGroup_Tab.empInfor,
					border : false,
					layout : "fit",
					items : [empForm.panel],
					listeners : {
						"activate" : function() {
							if(WGTab.GroupNodeId!=null){
								empForm.findCEmpInfo();
				 			}
						}
					}
				}],
				listeners : {
					render : function(){
						WGTab.hideTabPanelMethod(0);
					}
				}
			}
		}]
	});
	
	/**
	 * 页面tabPanel显示与隐藏方法
	 */
	WGTab.hideTabPanelMethod = function(arg){
		if(arg == 1){
			Ext.getCmp('_tabPanel').unhideTabStripItem(0); //显示 本工作组信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(1); //显示 下级工作组列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(3);   //显示 下级岗位列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(4);   //显示 下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(0);
		} else if (arg == 2){
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本工作组信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(1);   //隐藏 下级工作组列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(2); //显示 本岗位信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(3);   //显示 下级岗位列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(4);   //显示 下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(2);
		} else if (arg == 3){
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本工作组信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(1);   //隐藏 下级工作组列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(3);   //隐藏 下级岗位列表
			Ext.getCmp('_tabPanel').hideTabStripItem(4);   //隐藏 下级人员信息列表
			Ext.getCmp('_tabPanel').unhideTabStripItem(5);   //显示 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(5);
		} else {
			Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 本工作组信息表单
			Ext.getCmp('_tabPanel').unhideTabStripItem(1); //显示 下级工作组列表
			Ext.getCmp('_tabPanel').hideTabStripItem(2);   //隐藏 本岗位信息表单
			Ext.getCmp('_tabPanel').hideTabStripItem(3);   //隐藏 下级岗位列表
			Ext.getCmp('_tabPanel').hideTabStripItem(4);   //隐藏 下级人员信息列表
			Ext.getCmp('_tabPanel').hideTabStripItem(5);   //隐藏 人员信息表单
			Ext.getCmp('_tabPanel').setActiveTab(1);
		}
	}
});