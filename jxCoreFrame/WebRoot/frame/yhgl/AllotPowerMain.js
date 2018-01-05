/** 系统角色权限分配 */
Ext.onReady(function() {
	Ext.namespace('AllotPowerMain');
	AllotPowerMain.searchParams = {}; 
	
	/** 选项卡页面 */
	AllotPowerMain.tab = new Ext.Panel({
		layout : 'fit', border : false, activeItem : 0, baseCls: "x-plain",
		items : [{
			layout : 'fit', border : true, height : 450, 
			items : {
				id:'_tabPanel', xtype : "tabpanel", activeTab : 0, enableTabScroll : true, border : false,
				items : [{
					title : i18n.AllotPowerMain.operator, order : false, layout : "fit", items : [AllotPower.Operator.grid],
					listeners : {
				 		"activate" : function() {
				 			AllotPower.Operator.grid.store.load();
					 	}
					}
				}, {
					title : i18n.AllotPowerMain.organization, border : false, layout : "fit", items : [AllotPower.Organization.grid],
					listeners : {
						"activate" : function() {
							AllotPower.Organization.grid.store.load();
					 	}
					}
				}, {
					title : i18n.AllotPowerMain.workGroup, border : false, layout : "fit", items : [AllotPower.Group.grid],
					listeners : {
						"activate" : function() {
							AllotPower.Group.grid.store.load();
						}
					}
				}, { 
					title : i18n.AllotPowerMain.posit, border : false, layout : "fit", items : [AllotPower.Position.grid],
					listeners : {
						"activate" : function() {
							AllotPower.Position.grid.store.load();
						}
					}
				}, {
					title : i18n.AllotPowerMain.job, border : false, layout : "fit", items : [AllotPower.Duty.grid],
					listeners : {
						"activate" : function() {
							AllotPower.Duty.grid.store.load();
					 	}
					}
				}]
			}
		}]
	});
	
	/** 权限分配窗口 */
	AllotPowerMain.win = new Ext.Window({
		title: i18n.AllotPowerMain.AuthAlloc,
		width: 750, height: 500, 
		layout: "fit", 
		plain: true, border:false, maximizable: false, 
		closeAction: "hide", 
		items : [AllotPowerMain.tab],
		buttonAlign: 'center', 
		buttons:[{
			text:i18n.AllotPowerMain.close, iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
//		,
//		listeners: {
//			'render' : function(){
//				alert('render');
//			},
//			'beforeshow' : function(){
//				alert('show');
//				SysRole.Mask.show();
//			},
//			beforehide : function(){
//				alert('hide');
//				SysRole.Mask.hide();
//			}
//		}
	});

});