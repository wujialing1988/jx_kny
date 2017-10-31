// 机车构型维护
Ext.onReady(function() {
	Ext.namespace('JcgxBuild');

	JcgxBuild.shortName = "";
	JcgxBuild.nodeIDX = "";
	JcgxBuild.treePath = "";
	JcgxBuild.flbm = "";
	JcgxBuild.changed = false; // 用于标识是否增加了机车构型
	// 机车机型手动排序 
    JcgxBuild.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var data = grid.selModel.getSelections();
		var coID = data[0].id;
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/jcgxBuild!moveOrder.action',
			params: {coID: coID, orderType: orderType},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload({
		            	callback: function(){
				            // 重新加载树	            		
				            JcgxBuild.reloadTree(JcgxBuild.treePath);
		            	}
		            });
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    };
	// 故障现象手动排序 
    JcgxBuild.moveOrderFault = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var data = grid.selModel.getSelections();
		var idx = data[0].id;
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/jcxtflFault!moveOrder.action',
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
				            JcgxBuild.reloadTree(JcgxBuild.treePath);
		            	}
		            });
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    };
    
	// 重新加载树
	JcgxBuild.reloadTree = function(path) {
		JcgxBuild.tree.root.reload(function() {
			if (!path) JcgxBuild.tree.getSelectionModel().select(JcgxBuild.tree.root);
		});
		if (path == undefined || path == "" || path == "###") {
			JcgxBuild.tree.root.expand();
		} else {
			// 展开树到指定节点
			JcgxBuild.tree.expandPath(path);
			JcgxBuild.tree.selectPath(path);
		}
	};

	// 机车构型树型列表
	JcgxBuild.tree = new Ext.tree.TreePanel({
		tbar: new Ext.Toolbar(),
		plugins: ['multifilter'],
		loader: new Ext.tree.TreeLoader({
			dataUrl: ctx + "/jcgxBuild!tree.action"
		}),
		root: new Ext.tree.AsyncTreeNode({
			text: '',
			id: "0",
			leaf: false,
			expanded: true
		}),
		rootVisible: true,
		autoScroll: true,
		animate: false,
		useArrows: false,
		border: false,
		height: 330,
		collapsed: false,
		listeners: {
			load: function( node ){
				
				/*// Modified by hetao on 2016-05-26 增加机车构型节点大纲序列号的显示
				var childNodes = node.childNodes;
				if (Ext.isEmpty(childNodes)) {
					return;
				}
				var node;
				for (var i = 0; i < childNodes.length; i++) {
					node = childNodes[i];
					var sequence = node.getPath('index').substring(2);
					while (sequence.indexOf('/') >= 0) {
						sequence = sequence.replace('/', '.');
					}
					node.setText(sequence + '&nbsp;' + node.text);
				}*/
			},
			beforeload: function(node, e) {
				JcgxBuild.tree.getLoader().dataUrl = ctx
						+ '/jcgxBuild!tree.action?parentIDX=' + node.id
						+ '&shortName=' + JcgxBuild.shortName;
			},
			click: function(node, e) {
				// JcgxBuild.initFn(node)
			},
			movenode: function( tree, node, oldParent, newParent, index ) {
				if (oldParent === newParent) {
					MyExt.Msg.alert('无效操作，暂时不支持同级节点的顺序调整！');
					return;
				}
				// Ajax请求
				if (self.loadMask) self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/jcgxBuild!updateToMoveNode.action',
					params: {
						node: node.id,
						oldParent: oldParent.id,
						newParent: newParent.id,
						index: index
					},
					success: function(response, options){
						if (self.loadMask) self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				   	failure: function(response, options){
						if (self.loadMask) self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		},
		// Modified by hetao on 2016-05-26 增加树节点拖动调整机车构型的功能
		enableDD: true
	});
	JcgxBuild.tree.getSelectionModel().on('selectionchange', function(me, node) {
		JcgxBuild.initFn(node);
	});

	// 选择一个流程节点时的初始化机车构型Grid
	JcgxBuild.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
		// 获取当前节点的路径信息
		JcgxBuild.treePath = node.getPath();
		JcgxBuild.nodeIDX = node.id;
		JcgxBuild.sycx = JcgxBuild.tree.root.text;
		JcgxBuild.flbm = node.attributes["flbm"];
		JcgxBuild.grid.store.load();
		JcgxBuild.jcxtflFaultGrid.store.load();
		JcxtflBuild.path = node.getPath('text');
	};

	// 定义下节系统分类表格
	JcgxBuild.grid = new Ext.yunda.RowEditorGrid({	
		loadURL: ctx + "/jcgxBuild!pageQuery.action",
		saveURL: ctx + "/jcgxBuild!saveOrUpdate.action",
		storeAutoLoad: false,
		storeId: 'coID',
		beforeSaveFn: function(rowEditor, changes, record, rowIndex){
			// var record_v = JcgxBuild.grid.store.getAt(JcgxBuild.rowIndex );
 			var value = document.getElementById("wzmc_id").value;
 			record.data.wzmc = value;
 			record.data.wzdm = Ext.getCmp("wzdm_id").getValue();
	        return true;
	    },
		tbar: [{
			text: "选择系统分类",
			iconCls: "editIcon",
			handler: function() {
				JcxtflBuild.shortName = JcgxBuild.shortName;
				JcxtflBuild.tree.root.setText(JcgxBuild.shortName);
				JcxtflBuild.win.show();
			}
		}, {
			text: "删除",
			iconCls: "deleteIcon",
			handler: function() {
				if (!$yd.isSelectedRecord(JcgxBuild.grid)) return;
				Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn) {
					if (btn != 'yes') return;
					if (self.loadMask) self.loadMask.show();
					Ext.Ajax.request({
						url: ctx + "/jcgxBuild!deleteNode.action",
						params: {
							ids: $yd.getSelectedIdx( JcgxBuild.grid, JcgxBuild.grid.storeId)
						},
						success: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.success == true) {
								JcgxBuild.grid.store.reload();
								JcgxBuild.reloadTree();
								MyExt.Msg.alert("删除操作成功！");
							} else {
								alertFail(result.errMsg);
							}
						},
						failure: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						}
					});
				});
			}
		}, '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header: '主键', dataIndex: 'coID', hidden: true
		}, {
			header: '父类节点', dataIndex: 'fjdID', hidden: true
		}, {
			header: '顺序号', dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: '分类编码', dataIndex: 'flbm', editor: { readOnly: true }, width: 80
		}, {
			header: '分类名称', dataIndex: 'flmc', editor: { readOnly: true }, width: 200
		}, {
			header: '分类简称', dataIndex: 'fljc', editor: { readOnly: true },  width: 100
		}, {
			header: '位置名称', dataIndex: 'wzmc', width: 100,
			editor: {
				id: "wzmc_id",
				selectOnFocus:false, typeAhead:false,
//				triggerAction: 'query',
				xtype: "Base_combo",
//				name: "wzmc33",
				returnField: [{ widgetId: "wzdm_id", propertyName: "partId" }],
				displayField: "partName", valueField: "partName",
				entity: "com.yunda.jx.component.entity.EquipPart",
				fields: ["partId", "partName"],
				idProperty: "partId",
				hasFocus: false,
				pageSize: 20,
				minListWidth: 200, editable: true,
 				listeners:{
					"beforequery" : function(){
						Ext.getCmp("wzdm_id").setValue(""); // 清除之前的记录						
					},
 				 	'collapse' : function(record, index){
// 				 		Ext.getCmp("wzdm_id").clearValue();
 				 		Ext.getCmp("wzdm_id").setValue("");
						JcgxBuild.grid.getView().refresh();
 				 	}
 				 }
			}
		}, {
			header: '位置名称编码', dataIndex: 'wzdm',hidden:true,
			editor: { id: "wzdm_id" }
		}, {
			header: '专业类型',
			dataIndex: 'zylxID',
			editor: {
				id: 'id_zylx_id',
				xtype: "ProfessionalType_comboTree",
				fieldLabel: "专业类型",
				hiddenName: "zylxID",
				returnField: [{ widgetId: "id_zylx", propertyName: "text" }],
				selectNodeModel: "all"
			},
			renderer: function(value, mateData, record) {
				return record.get('zylx');
			}
		}, {
			header: '专业类型', dataIndex: 'zylx', hidden: true, editor: { id: "id_zylx" }
		}, {
			header: '检修专用', dataIndex: 'jxzy', width: 60, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'jxzy',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, '是'], [0, '否']]
				}),
				valueField: 'v',
				displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return "是";
				if (v == 0) return "否";
				return "";
			}
		}, {
			header: '整备专用', dataIndex: 'sfzbzy', width: 60, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'sfzbzy',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, '是'], [0, '否']]
				}),
				valueField: 'v',
				displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return "是";
				if (v == 0) return "否";
				return "";
			}
		}, {
			header: '电子档案专用', dataIndex: 'sfsyDzda', width: 80, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'sfsyDzda',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, '是'], [0, '否']]
				}),
				valueField: 'v', displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return "是";
				if (v == 0) return "否";
				return "";
			}
		}, {
			header: '构型位置全名', dataIndex: 'wzqm',editor: { readOnly: true }, width: 320
		}, {
			header: '零部件名称编码', dataIndex: 'lbjbm', hidden: true
		}, {
			header: '拼音简称 ', dataIndex: 'pyjc', hidden: true
		}, {
			header: '是否有子节点', dataIndex: 'coHaschild', hidden: true
		}, {
			header: '构型位置编码', dataIndex: 'gxwzbm', hidden: true
		}, {
			header: '车型', dataIndex: 'sycx', hidden: true
		}, {
			header: '记录状态', dataIndex: 'recordStatus', hidden: true
		}],
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "wzmc"){
					JcgxBuild.rowIndex = rowIndex;
				}			
			}
		}
	});
	JcgxBuild.grid.store.on('beforeload', function() {
		var whereList = [];
		whereList.push({ propName: 'fjdID', propValue: JcgxBuild.nodeIDX, compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'sycx', propValue: JcgxBuild.sycx, compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});

	// 以下代码用于重置“专业类型”字段
	JcgxBuild.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
		var record = JcgxBuild.grid.store.getAt(rowIndex);
		// 目前发现一个问题，在第一次触发行编辑时，不能正常的回显“专业类型”控件值，暂时未找到解决方案
		Ext.getCmp('id_zylx_id').setDisplayValue(record.get('zylxID'),
				record.get('zylx'));
	});
	JcgxBuild.grid.rowEditor.on('afteredit', function(me, rowIndex) {
		Ext.getCmp('id_zylx_id').clearValue();
		Ext.getCmp('wzmc_id').clearValue();
		Ext.getCmp("wzdm_id").setValue("");
//		Ext.getCmp('wzdm_id').clearValue();
	});
	JcgxBuild.grid.rowEditor.on('canceledit', function(me, rowIndex) {
		Ext.getCmp('id_zylx_id').clearValue();
		Ext.getCmp('wzmc_id').clearValue();
		Ext.getCmp("wzdm_id").setValue("");
//		Ext.getCmp('wzdm_id').clearValue();
	});

	// 定义构型故障编码表格
	JcgxBuild.jcxtflFaultGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/jcxtflFault!pageQuery.action",
		deleteURL: ctx + "/jcxtflFault!delete.action",
		storeAutoLoad: false,
		tbar: [{
			text: "选择故障现象", iconCls: 'addIcon', handler: function() {
				JcxtflFault.win.show();
			}
		},{
			text: "新增故障现象", iconCls: 'addIcon', handler: function() {
				JcxtflFault.formWin.show();
			}
		}, 'delete', '->',{
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_BOT);				
			}
		}],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true
		}, {
			header: '顺序号', dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: '分类编码', dataIndex: 'flbm'
		}, {
			header: '故障现象编码', dataIndex: 'faultId'
		}, {
			header: '故障现象名称', dataIndex: 'faultName'
		}],
		toEditFn: function() {
		}
	});
	JcgxBuild.jcxtflFaultGrid.store.on('beforeload', function() {
		var whereList = [];
		whereList.push({ propName: 'flbm', propValue: JcgxBuild.flbm, compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});

	/** 构建节点位置全名 */
	JcgxBuild.updateWzqm = function() {
		Ext.Msg.confirm("提示", "是否确认重新设置构型位置全名？", function(btn) {
			if (btn == 'yes') {
				if (self.loadMask) self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + "/jcgxBuild!updateWzqm.action",
					params: { shortName: JcgxBuild.shortName },
					timeout: 60000,
					success: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.success == true) {
							JcgxBuild.grid.store.reload();
							alertSuccess("构型位置全名构建成功！");
						} else {
							alertFail(result.errMsg);
						}
					},
					failure: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		});
	};
	
	/** 下载构型模板 */
	JcgxBuild.downloadGxTemp = function() {
		window.location.href = ctx + "/jcxtflBuild!exportClassification.action?shortName=" + JcgxBuild.shortName;
	};
	
	/** 导入机车构型 */
	JcgxBuild.importWin = new Ext.Window({
		title:"导入机车构型",
		width:450, height:120,
		plain:true, maximizable:false, modal: true,
		closeAction:"hide",
		layout:'fit',
		items: [{
			xtype:"form", id:'form', border:false, style:"padding:10px" ,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			baseCls: "x-plain", defaults:{anchor:"100%"},
			labelWidth:80,
			items:[{
				fieldLabel:'选择文件',
				name:'jcgx',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: '浏览文件...'
			}]
		}],
		listeners:{
			// 隐藏窗口时重置上传表单
			hide: function(window) {
				window.find('xtype', 'form')[0].getForm().getEl().dom.reset();
			}
		},
		buttonAlign:'center',
		buttons:[{
			text: "导入", iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var filePath = window.find('name', 'jcgx')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert('该功能仅支持<span style="color:red;"> Excel2003（*.xls） </span>版本文件！');
					return;
				}
				form.submit({
                	url: ctx+'/jcgxBuild!saveImport.action?shortName=' + JcgxBuild.shortName,
                	waitTitle:'请稍候',
               	 	waitMsg: '正在导入数据请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',
                	// 请求成功后的回调函数
                	success: function(form, action) {
                		var result = Ext.util.JSON.decode(action.response.responseText);
		                if(result.success == true){
							form.getEl().dom.reset();
							// 隐藏文件上传窗口
		                	window.hide();
							alertSuccess();
							JcgxBuild.grid.store.reload();
							JcgxBuild.reloadTree();
		                } else {
							alertFail(result.errMsg);
		                }
                	},
                	// 请求失败后的回调函数
				    failure: function(form, action){
                		var result = Ext.util.JSON.decode(action.response.responseText);
                		if (!Ext.isEmpty(result.errMsg)) {
 							alertFail(result.errMsg);
                		} else {
				       	 	Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + action.response.status + "\n" + action.response.responseText);
                		}
				    }
            	}); 
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	/** 打开机车构型导入窗口 */
	JcgxBuild.openImportWin = function() {
		Ext.Msg.confirm("提示", "导入构型将删除该车型已有构型数据，是否继续？", function(btn) {
			if (btn == 'yes') {
				JcgxBuild.importWin.show();
			}
		});
	};

	// 定义机车构型维护窗口
	JcgxBuild.win = new Ext.Window({
		title: "机车构型维护", maximized: true, layout: "border", closeAction: "hide",
		items: [{
			region: "west",
			width: 279,
			collapsible: true,
			iconCls: 'icon-expand-all',
			title: '<span style="font-weight:normal">机车构型节点树<span style="color:gray;">（*可拖动）</span></span>',
			bbar: [
			{
				text: '<span style="color:gray;">模板下载</span>', iconCls: 'downloadIcon', handler: JcgxBuild.downloadGxTemp
			},
			'-',
			{
				text: '<span style="color:gray;">导入</span>', iconCls: 'page_excelIcon', handler: JcgxBuild.openImportWin
			},
			'-',
			{
				text: '<span style="color:gray;">构建节点位置全名</span>', iconCls: 'configIcon', handler: JcgxBuild.updateWzqm
			}],
			tools: [{
				id: 'refresh', handler: function() {
					JcgxBuild.reloadTree(JcgxBuild.treePath);
				}
			}],
			layout: "fit",
			items: [JcgxBuild.tree]
		}, {
			xtype: "tabpanel",
			id: "tabpanel",
			activeTab: 0,
			region: "center",
			items: [{
				title: "下级系统分类", layout: "fit", items: [JcgxBuild.grid]
			}, {
				title: "故障现象", layout: "fit", items: [JcgxBuild.jcxtflFaultGrid]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				JcgxBuild.tree.getSelectionModel().clearSelections();
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function(window) {
				JcgxBuild.tree.root.setText(JcgxBuild.shortName);
				JcgxBuild.reloadTree();
			}
		}
	});
	
});