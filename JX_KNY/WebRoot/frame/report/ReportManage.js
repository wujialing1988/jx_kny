/**
 * 报表文件对象 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ReportManage.js');                       //定义命名空间
	
	/** **************** 定义全局函数开始 **************** */ 
	// 报表模板目录树【新增】按钮触发的函数处理
	ReportManage.treeBtnAddFn = function() {
		FileCatalog.isEditFn = false;
		var El = FileCatalog.saveForm.getForm().getEl();
		if (null != El) {
			El.dom.reset();
		}
		
		FileCatalog.saveForm.find('name', 'parentIDX')[0].setValue(FileCatalog.idx);
		FileCatalog.saveForm.find('name', 'parentFolderFullPath')[0].setValue(FileCatalog.folderFullPath);
		FileCatalog.saveForm.find('name', 'editable')[0].setValue(CONST_STR_T);
		FileCatalog.saveWin.setTitle('新增');
		FileCatalog.saveWin.show();
	}
	// 报表模板目录树【编辑】按钮触发的函数处理
	ReportManage.treeBtnEditFn = function() {
		FileCatalog.isEditFn = true;
		// 获取当前选中的树节点
		var sm = FileCatalog.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (null == node) {
			MyExt.Msg.alert("请先选择要编辑的报表部署目录节点！");
			return;
		}
		
		// 将树节点保护的报表部署目录属性赋值到保存表单
		FileCatalog.saveForm.find('name', 'idx')[0].setValue(node.id);
		FileCatalog.saveForm.find('name', 'parentIDX')[0].setValue(node.attributes["parentIDX"]);
		FileCatalog.saveForm.find('name', 'editable')[0].setValue(node.attributes["editable"]);
		FileCatalog.saveForm.find('name', 'folderDesc')[0].setValue(node.attributes["folderDesc"]);
		FileCatalog.saveForm.find('name', 'folderNameEN')[0].setValue(node.attributes["folderNameEN"]);
		FileCatalog.saveForm.find('name', 'folderNameCH')[0].setValue(node.attributes["folderNameCH"]);
		var index = FileCatalog.folderFullPath.lastIndexOf(".");
		FileCatalog.saveForm.find('name', 'parentFolderFullPath')[0].setValue(FileCatalog.folderFullPath.substring(0, index));
		FileCatalog.saveWin.setTitle('编辑');
		FileCatalog.saveWin.show();
	}
	// 报表模板目录树【删除】按钮触发的函数处理
	ReportManage.treeBtnDelFn = function() {
		// 获取当前选中的树节点
		var sm = FileCatalog.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (null == node) {
			MyExt.Msg.alert("请先选择要删除的报表部署目录节点！");
			return;
		}
		Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn) {
			if ('yes' == btn) {
				Ext.Ajax.request({
					url:ctx + '/fileCatalog!logicDelete.action',
					params:{ids:[node.id]},
		
				    //请求成功后的回调函数
					success: function(response, options){
			        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            FileCatalog.reloadTreeFn(FileCatalog.treePath);
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		});
	}
	
	/** **************** 定义全局函数结束 **************** */ 
	
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout:'border', 
		defaults: {
			layout: 'fit'
		},
		items:[{
			region: 'west',
			title : '<span style="font-weight:blod">报表模板目录树</span>',
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	FileCatalog.treePath = "";						// 报表目录树路径
					FileCatalog.folderFullPath = "";				// 当前报表目录的全路径信息
					FileCatalog.idx = "";							// 当前报表目录的idx主键
					FileCatalog.isEditFn = false;					// 是否是“编辑”操作
					FileCatalog.tree.root.reload();
	            }
	        } ],
			collapsible: true,
			width: 250,
			tbar: [{
				text:'新增', id:'treeBtnAdd', iconCls:'addIcon', handler: ReportManage.treeBtnAddFn
			}, {
				text:'编辑', id:'treeBtnEdit', iconCls:'editIcon', handler: ReportManage.treeBtnEditFn
			}, {
				text:'删除', id:'treeBtnDel', iconCls:'deleteIcon', handler: ReportManage.treeBtnDelFn
			}, '->', {
				text:'初始化目录', iconCls:'resetIcon', handler: function(){
//					MyExt.Msg.alert('功能完善中！请稍候');
					Ext.Msg.confirm('提示', '是否确认初始化目录？', function(btn){
						if ('yes' == btn) {
							self.loadMask.show();
							// Ajax后台请求
							Ext.Ajax.request({
								url: ctx + '/fileCatalog!initialize.action',
								//请求成功后的回调函数
							    success: function(response, options){
							        if(self.loadMask)    self.loadMask.hide();
							        var result = Ext.util.JSON.decode(response.responseText);

							        if (result.success === false) {       //操作成功     
							            alertFail(result.errMsg);
							        } else {                           //操作失败
							            alertSuccess();
				          				FileCatalog.reloadTreeFn(FileCatalog.treePath);
							        }
							    },
							    //请求失败后的回调函数
							    failure: function(response, options){
							        if(self.loadMask)    self.loadMask.hide();
							        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
							    }
							});
						}
					});
				}
			}], 
			items:FileCatalog.tree
		},{
			region:'center',
			layout:'border',
			defaults:{
				layout:'fit'
			},
			items:[{
				region:'north',
				title:'查询',
				collapsible: true,
				height: 110,
				frame:'true',
				items:[PrinterModule.searchForm]
			}, {
				region:'center',
				items:[PrinterModule.grid]
			}]
		}]
	});
	
});