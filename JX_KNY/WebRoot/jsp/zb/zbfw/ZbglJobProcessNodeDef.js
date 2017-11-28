/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglJobProcessNodeDef');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	ZbglJobProcessNodeDef.labelWidth = 100;
	ZbglJobProcessNodeDef.fieldWidth = 140;
	
	ZbglJobProcessNodeDef.isRender = false;
	ZbglJobProcessNodeDef.treePath = "";
	
	ZbglJobProcessNodeDef.zbfwIDX = "";
	ZbglJobProcessNodeDef.parentIDX = "ROOT_0";
	ZbglJobProcessNodeDef.nodeIDX; //工位节点
	
	ZbglJobProcessNodeDef.isSaveAndAdd = false;			// 是否是保存并新增的标识
	ZbglJobProcessNodeDef.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:i18n.common.tip.loading});
	
	ZbglJobProcessNodeDef.workstationReturnFn = function(stationObject) {
		var form = ZbglJobProcessNodeDef.saveForm;
		form.find("name", "workStationIDX")[0].setValue(stationObject.workStationIDX);
		form.find("name", "workStationName")[0].setValue(stationObject.workStationName);
	}
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 重新加载【作业节点树】
	ZbglJobProcessNodeDef.reloadTree = function(path) {
        ZbglJobProcessNodeDef.tree.root.reload(function() {
        	if (!path) {
				ZbglJobProcessNodeDef.tree.getSelectionModel().select(ZbglJobProcessNodeDef.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			ZbglJobProcessNodeDef.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        ZbglJobProcessNodeDef.tree.expandPath(path);
	        ZbglJobProcessNodeDef.tree.selectPath(path);
        }
	}
	// 手动排序 
    ZbglJobProcessNodeDef.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/zbglJobProcessNodeDef!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload({
		            	callback: function(){
				            // 重新加载树
		            		/** Modified by hetao at 2015-08-24 【来源20150812检修培训】作业流程维护，任务节点移动，目前移动节点会刷新整个任务树。建议上移或者下移任务节点，只在“下级作业节点”页签上动态更新顺序，不刷新整个维护页面。  */
				            ZbglJobProcessNodeDef.reloadTree(ZbglJobProcessNodeDef.treePath);
		            	}
		            });
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
	/** **************** 定义全局函数结束 **************** */
    
    /** ************** 定义节点编辑表单开始 ************** */
	ZbglJobProcessNodeDef.saveForm = new Ext.form.FormPanel({
		padding: "10px", frame: true, labelWidth: ZbglJobProcessNodeDef.labelWidth,
		layout:"column",
		defaults: {
			layout:"form",
			columnWidth:0.5
		},
		items:[{
			columnWidth:1,
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				name:"nodeName", fieldLabel:i18n.ZbFw.nodeName
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea", name:"nodeDesc", fieldLabel:i18n.ZbFw.nodeDesc, maxLength:500, anchor:"97%", height: 55
			}]
		}, {
			columnWidth:1,
			items:[{
		    	fieldLabel: '作业工位', name: 'workStationName',hidden:true,
    			xtype: 'JobNodeStationDefSelect', nodeIDX: ZbglJobProcessNodeDef.nodeIDX,
				editable: false, returnFn: ZbglJobProcessNodeDef.workstationReturnFn
		    }]
		}, {
			// 【作业节点】保存表单的隐藏字段
			columnWidth:1,
			defaultType:'hidden',
			items:[
				{ fieldLabel:"idx主键", name:"idx" },
				{ fieldLabel:"顺序号", name:"seqNo" },
				{ fieldLabel:"是否子节点", name:"isLeaf", value: IS_LEAF_YES },
				{ fieldLabel:"作业流程主键", name:"zbfwIDX" },
				{ fieldLabel:"上级作业节点主键", name:"parentIDX" },
				{ fieldLabel:"工位id", name:"workStationIDX" }
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: i18n.common.button.save, iconCls: 'saveIcon', handler: function() {
				ZbglJobProcessNodeDef.isSaveAndAdd = false;
				ZbglJobProcessNodeDef.grid.saveFn();
			}
		}, {
			text: i18n.common.button.add, iconCls: 'addIcon', handler: function() {
				ZbglJobProcessNodeDef.isSaveAndAdd = true;
				ZbglJobProcessNodeDef.grid.saveFn();
			}
		}, {
			text: i18n.common.button.close, iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** ************** 定义节点编辑表单结束 ************** */
	
	ZbglJobProcessNodeDef.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglJobProcessNodeDef!pageList.action',             //装载列表数据的请求URL
	    saveURL: ctx + '/zbglJobProcessNodeDef!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/zbglJobProcessNodeDef!logicDelete.action',            //删除数据的请求URL
	    saveForm: ZbglJobProcessNodeDef.saveForm,
	    saveWinWidth: 650,        
    	saveWinHeight: 215,    
    	storeAutoLoad: false,
    	viewConfig:null,
	    tbar:['delete', '->', {
			text:i18n.common.button.moveTop, iconCls:'moveTopIcon', handler: function() {
				ZbglJobProcessNodeDef.moveOrder(ZbglJobProcessNodeDef.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:i18n.common.button.moveUp, iconCls:'moveUpIcon', handler: function() {
				ZbglJobProcessNodeDef.moveOrder(ZbglJobProcessNodeDef.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:i18n.common.button.moveDown, iconCls:'moveDownIcon', handler: function() {
				ZbglJobProcessNodeDef.moveOrder(ZbglJobProcessNodeDef.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:i18n.common.button.moveBottom, iconCls:'moveBottomIcon', handler: function() {
				ZbglJobProcessNodeDef.moveOrder(ZbglJobProcessNodeDef.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'范围主键', dataIndex:'zbfwIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'上级主键', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:i18n.ZbFw.seqNo, dataIndex:'seqNo', width: 60, hidden:false, editor:{ xtype:'numberfield', maxLength:8 }
		},{
			header:i18n.ZbFw.nodeName, dataIndex:'nodeName', width: 200, editor:{  maxLength:100 }
		},{
			header:i18n.ZbFw.nodeDesc, dataIndex:'nodeDesc', width: 460, editor:{  maxLength:1000 }
		},{
			header:'作业工位', dataIndex:'workStationName', hidden:true, editor:{  xtype:'hidden' }
		},{
			header:i18n.ZbFw.isLeaf, dataIndex:'isLeaf', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'前置节点主键', dataIndex:'preNodeIDX', hidden:true, editor:{ disabled: true }
		},{
			header:i18n.ZbFw.preNodeName, dataIndex:'preNodeName', width:70, hidden:true,  editor:{ disabled: true }
		},{
			header:i18n.ZbFw.preNodeSeqNo, dataIndex:'preNodeSeqNo', width:125, hidden:false,  editor:{ disabled: true }
		}],
		
		// 删除成功后的函数处理
		afterDeleteFn: function(){ 
        	// 重新加载【作业节点树】
            ZbglJobProcessNodeDef.reloadTree(ZbglJobProcessNodeDef.treePath);
		},
		
		// 保存成功后的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 重新加载【作业节点树】
            ZbglJobProcessNodeDef.reloadTree(ZbglJobProcessNodeDef.treePath);
            // 回显字段值
            var entity = result.entity;
            
            // 启用前后置节点关系表格工具栏
            ZbglJobProcessNodeRelDef.grid.getTopToolbar().enable();
	    	ZbglJobProcessNodeRelDef.nodeIDX = entity.idx;				// 设置作业节点主键
	    	
	    	// 扩展配置选项卡设置为可用
//	    	Ext.getCmp("node_tabs").getItem("extConfigDef_tab").enable();
//	    	this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(1);
	    	// 设置扩展配置的节点主键
	    	ZbglJobNodeExtConfigDef.nodeIDX = entity.idx;
	    	if (ZbglJobProcessNodeDef.isSaveAndAdd) {
			    this.saveForm.getForm().reset();
		    	this.afterShowSaveWin();
	    	} else {
	            this.saveForm.find('name', 'idx')[0].setValue(entity.idx);
	            this.saveForm.find('name', 'seqNo')[0].setValue(entity.seqNo);
	    	}
	    },
	    
	    afterShowEditWin: function(record, rowIndex){
	    	this.saveWin.setTitle(i18n.common.button.edit);
	    	
	    	// 初始化前置节点关系表格数据
	    	// 重新加载【节点编辑】节点前后置关系表格数据
	    	ZbglJobProcessNodeRelDef.zbfwIDX = record.get('zbfwIDX');						// 设置作业流程主键
	    	ZbglJobProcessNodeRelDef.nodeIDX = record.get('idx');								// 设置作业节点主键
	    	ZbglJobProcessNodeRelDef.parentIDX = record.get('parentIDX');						// 设置上级作业节点主键
	    	ZbglJobProcessNodeRelDef.grid.store.load();
	    	
	    	// 启用前后置节点关系表格工具栏
	    	ZbglJobProcessNodeRelDef.grid.getTopToolbar().enable();
	    	// 扩展配置选项卡设置不可用
//	    	Ext.getCmp("node_tabs").getItem("extConfigDef_tab").enable();
	    	// 显示扩展配置选项卡
//	    	this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(1);
	    	// 设置扩展配置的节点主键
	    	ZbglJobNodeExtConfigDef.nodeIDX = record.get('idx');
	    	ZbglJobNodeExtConfigDef.loadFn(record.get('idx'));
	    },
		
	    createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
				width: 650, height: 500,
				closeAction: 'hide',
				layout: 'border',
				modal: false,
				defaults: {layout: 'fit', border: false},
				items: [{
					region: 'north', height: 220,
					items: [{
						xtype: 'tabpanel', activeTab:0, 
						defaults: {layout:'fit'},
						items: [{
							title: i18n.ZbFw.baseInfoTitle,
							items: [ZbglJobProcessNodeDef.saveForm]
						}]
					}]
//					
				}, {
					region: 'center',
					
					items: [{
						id:"node_tabs",xtype: 'tabpanel', activeTab:0, 
						defaults: {layout:'fit'},
						items: [{
							id:"nodeRelDef_tab",title: i18n.ZbFw.preNodeTitle,
							items: [ZbglJobProcessNodeRelDef.grid]
						}/*, {
							id:"extConfigDef_tab",title: '扩展配置',
							items: [ZbglJobNodeExtConfigDef.form]
						}*/]
					}]
				}],
				listeners: {
					hide: function() {
						// 隐藏前后置节点关系编辑窗口
						if (ZbglJobProcessNodeRelDef.grid.saveWin) ZbglJobProcessNodeRelDef.grid.saveWin.hide();
					}
				}
	        });
	    },
	    
	    afterShowSaveWin: function() {
	    	// 清空前后置节点关系表格数据
	    	if (ZbglJobProcessNodeRelDef.grid.store.getCount() > 0) {
	    		ZbglJobProcessNodeRelDef.grid.store.removeAll();
	    	}
	    	// 禁用前后置节点关系表格工具栏
	    	ZbglJobProcessNodeRelDef.grid.getTopToolbar().disable();
	    	
	    	// 设置保存表单默认值
	    	this.saveForm.find('name', 'parentIDX')[0].setValue(ZbglJobProcessNodeRelDef.parentIDX);
	    	this.saveForm.find('name', 'zbfwIDX')[0].setValue(ZbglJobProcessNodeRelDef.zbfwIDX);
	    	// 扩展配置选项卡设置为不可用
//	    	Ext.getCmp("node_tabs").getItem("extConfigDef_tab").disable();
			Ext.getCmp("node_tabs").activate("nodeRelDef_tab");
//	    	this.saveWin.find('xtype', 'tabpanel')[0].hideTabStripItem(1);
//	    	this.saveWin.find('xtype', 'tabpanel')[0].setActiveTab(0);
	    	// 重置扩展配置表单
	    	ZbglJobNodeExtConfigDef.form.getForm().reset();
    		// 隐藏前后置节点关系的保存窗口
    		if(ZbglJobProcessNodeRelDef.grid.saveWin) ZbglJobProcessNodeRelDef.grid.saveWin.hide();
	    	// 重置扩展配置保存表单
	    	ZbglJobNodeExtConfigDef.resetFn();
	    	// 如果选择了一条记录，则在这条记录之前新增节点
	    	var sm = ZbglJobProcessNodeDef.grid.getSelectionModel();
	    	if (sm.getCount() > 0) {
	    		var firstRecord = sm.getSelections()[0];
	    		this.saveForm.find('name', 'seqNo')[0].setValue(firstRecord.get('seqNo'));
	    	}
	    },
	    
	    beforeAddButtonFn: function() {
	    	var treeNode = ZbglJobProcessNodeDef.tree.getSelectionModel().getSelectedNode();
	    	if (treeNode.leaf) {
	    		if (ZbFwWi.grid.store.getCount() > 0) {
	    			MyExt.Msg.alert(i18n.ZbFw.andNodeTip);
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	});
	// 设置默认排序字段为“顺序号（升序）”
	ZbglJobProcessNodeDef.grid.store.setDefaultSort("seqNo", "ASC");
	
	ZbglJobProcessNodeDef.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.zbfwIDX = ZbglJobProcessNodeDef.zbfwIDX;
		searchParams.parentIDX = ZbglJobProcessNodeDef.parentIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** **************** 定义作业流程节点树开始 **************** */
	// 选择一个流程节点时的初始化函数处理
	ZbglJobProcessNodeDef.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
    	// 获取当前节点的路径信息
    	ZbglJobProcessNodeDef.treePath = node.getPath();	
    	ZbglJobProcessNodeDef.parentIDX = node.id;
    	
    	ZbglJobProcessNodeRelDef.parentIDX = node.id;		// 设置上级作业节点主键
    	
		// 如果是叶子节点，则仅能进行检修工艺卡和检修记录卡的设置
    	if (node.leaf) {
			Ext.getCmp('tabpanel').hideTabStripItem(0);
			Ext.getCmp('tabpanel').unhideTabStripItem(1);
			Ext.getCmp('tabpanel').unhideTabStripItem(2);
			
    		// 获取当前活动的Tab选项卡页
    		var activeTab = Ext.getCmp('tabpanel').getActiveTab();
    		// 如果当前活动的Tab选项卡页是“下级作业节点”页，则设置当前活动的Tab选项卡页为“检修工艺卡”页
        	Ext.getCmp('tabpanel').setActiveTab(1);
//	        		
    		// 设置【作业项目】基础信息
    		ZbFwWi.nodeIDX = node.id;							// 作业流程节点主键
    		ZbFwWi.zbfwIDX = ZbglJobProcessNodeDef.zbfwIDX ;   //整备范围主键
			ZbFwWi.grid.store.load();
			
			// 设置【作业项目】基础信息
			Ext.getCmp('tabpanel_project').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - "+i18n.ZbFw.tabpanelProjectTitle);
			
		// 如果是树干节点，则仅能编辑下级作业节点信息
    	} else if (!node.leaf) {
			Ext.getCmp('tabpanel').unhideTabStripItem(0);
			Ext.getCmp('tabpanel').hideTabStripItem(1);
			Ext.getCmp('tabpanel').hideTabStripItem(2);
			Ext.getCmp('tabpanel').setActiveTab(0);
			
			// 重新加载作业节点表格数据
    		ZbglJobProcessNodeDef.grid.store.load();
    		
    		// 重命名Tab - 只取序列号
    		if (node.text.lastIndexOf(".") <= 0) {
        		Ext.getCmp('tabpanel_node').setTitle(node.text + " - "+i18n.ZbFw.tabpanelNodeTitle)
    		} else {
        		Ext.getCmp('tabpanel_node').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - "+i18n.ZbFw.tabpanelNextNodeTitle)
    		}
    	}
	}
	
	// 机车检修作业流程节点树型列表
	ZbglJobProcessNodeDef.tree =  new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/zbglJobProcessNodeDef!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		ZbglJobProcessNodeDef.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		ZbglJobProcessNodeDef.reloadTree();
	    	},
	        click: function(node, e) {
//	        	ZbglJobProcessNodeDef.initFn(node);
	        },
	        beforeload:  function(node){
			    ZbglJobProcessNodeDef.tree.loader.dataUrl = ctx + '/zbglJobProcessNodeDef!tree.action?parentIDX=' + node.id + '&zbfwIDX=' + ZbglJobProcessNodeDef.zbfwIDX;
			},
			load: function(node) {
//				ZbglJobProcessNodeDef.initFn(node);
			},
			movenode: function( tree, node, oldParent, newParent, index ) {
				// Ajax请求
				ZbglJobProcessNodeDef.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/zbglJobProcessNodeDef!moveNode.action',
					params: {
						node: node.id,
						oldParent: oldParent.id,
						newParent: newParent.id,
						index: index
					},
					success: function(response, options){
						ZbglJobProcessNodeDef.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 重新加载树
				            ZbglJobProcessNodeDef.reloadTree();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				   	failure: function(response, options){
				        ZbglJobProcessNodeDef.loadMask.hide();
				        Ext.Msg.alert(i18n.common.tip.prompt, i18n.common.tip.RequestFailed + response.status + "\n" + response.responseText);
				    }
				});
			}
	    },
	    enableDD:true
	});
	
	// 选中的树节点变化时的事件监听函数
	ZbglJobProcessNodeDef.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
    	ZbglJobProcessNodeDef.initFn(node);
	});
	/** **************** 定义作业流程节点树结束 **************** */
	
	/** **************** 定义作业流程编辑窗口开始 **************** */
	ZbglJobProcessNodeDef.win = new Ext.Window({
		title:i18n.ZbFw.nodeEditTitle, maximized:true,
		layout:"border", closeAction:"hide",
		items:[{
			title : '<span style="font-weight:normal">'+i18n.ZbFw.treeNodeTitle+'</span>',
			tbar: [{
				text: i18n.ZbFw.addLowerNodeBtn, iconCls: 'addIcon', handler: function() {
					if(ZbglJobProcessNodeDef.grid.beforeAddButtonFn() == false)   return;
			        //判断新增删除窗体是否为null，如果为null则自动创建后显示
			        if(ZbglJobProcessNodeDef.grid.saveWin == null)  ZbglJobProcessNodeDef.grid.createSaveWin();
			        if(ZbglJobProcessNodeDef.grid.searchWin)  ZbglJobProcessNodeDef.grid.searchWin.hide();
			        if(ZbglJobProcessNodeDef.grid.saveWin.isVisible())    ZbglJobProcessNodeDef.grid.saveWin.hide();
			        if(ZbglJobProcessNodeDef.grid.beforeShowSaveWin() == false)   return;
			        
			        ZbglJobProcessNodeDef.grid.saveWin.setTitle(i18n.common.button.add);
			        ZbglJobProcessNodeDef.grid.saveWin.show();
			        ZbglJobProcessNodeDef.grid.saveForm.getForm().reset();
			        ZbglJobProcessNodeDef.grid.saveForm.getForm().setValues(this.defaultData);
			        
			        ZbglJobProcessNodeDef.grid.afterShowSaveWin();
				}
			}, {
				text: i18n.common.button.del, iconCls: 'deleteIcon', handler: function() {
					var treeNode = ZbglJobProcessNodeDef.tree.getSelectionModel().getSelectedNode();
					if (null == treeNode) {
						MyExt.Msg.alert(i18n.common.tip.notSelectRecords);
						return;
					}
					Ext.Msg.confirm(i18n.common.tip.prompt, i18n.common.tip.dataNotRecover, function(btn){
						if ('yes' == btn) {
							Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								url: ctx + '/zbglJobProcessNodeDef!logicDelete.action',
								params: {ids: [treeNode.id]},
								success: function(response, options){
							        if(self.loadMask)    self.loadMask.hide();
							        var result = Ext.util.JSON.decode(response.responseText);
							        if (result.errMsg == null) {       //操作成功     
							            alertSuccess();
							            ZbglJobProcessNodeDef.reloadTree();
							        } else {                           //操作失败
							            alertFail(result.errMsg);
							        }
							    }
							}));
						}
					});
				}
			}],
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	ZbglJobProcessNodeDef.reloadTree(ZbglJobProcessNodeDef.treePath);
	            }
	        } ],
			region:"west", collapsible: true,
			width:279,
			layout:"fit",
			items:[ZbglJobProcessNodeDef.tree]
		}, {
			xtype:"tabpanel",
			id:"tabpanel",
			activeTab:0,
			region:"center",
			items:[{
				id:"tabpanel_node",
				title:i18n.ZbFw.tabpanelNodeTitle,
				layout:"fit",
				items:[ZbglJobProcessNodeDef.grid]
			}, {
				id:"tabpanel_project",
				title:i18n.ZbFw.tabpanelProjectTitle,
				layout:"border",
				defaults: {layout: 'fit', border: false},
				items:[{
					region: 'center',
					items: [ZbFwWi.grid]
				}]
			}],
			listeners : {
				render : function(){
					this.unhideTabStripItem(0);
					this.hideTabStripItem(1);
					this.hideTabStripItem(2);
					this.setActiveTab(0);
				}
			}
		}],
		buttonAlign:'center',
		buttons:[{
			text:i18n.common.button.close, iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			show: function(window) {
				ZbglJobProcessNodeDef.parentIDX = "ROOT_0";
				ZbglJobProcessNodeDef.grid.store.load();
				ZbglJobProcessNodeDef.reloadTree();
				ZbglJobProcessNodeRelDef.zbfwIDX = ZbglJobProcessNodeDef.zbfwIDX;
				Ext.getCmp('tabpanel').unhideTabStripItem(0);
				Ext.getCmp('tabpanel').hideTabStripItem(1);
				Ext.getCmp('tabpanel').hideTabStripItem(2);
				Ext.getCmp('tabpanel').setActiveTab(0);
			},
			hide: function() {
				// 隐藏机车检修作业流程节点编辑窗口
				if (ZbglJobProcessNodeDef.grid.saveWin) ZbglJobProcessNodeDef.grid.saveWin.hide();
			}
		}
	});
	/** **************** 定义作业流程编辑窗口结束 **************** */
	
});