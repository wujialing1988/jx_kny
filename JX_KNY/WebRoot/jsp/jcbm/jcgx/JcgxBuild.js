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
					MyExt.Msg.alert(i18n.JcgxBuild.InvalidOper);
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
				        Ext.Msg.alert(i18n.JcgxBuild.prompt, i18n.JcgxBuild.false+"\n" + response.status + "\n" + response.responseText);
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
			text: i18n.JcgxBuild.choiceSysClass,
			iconCls: "editIcon",
			handler: function() {
				JcxtflBuild.shortName = JcgxBuild.shortName;
				JcxtflBuild.tree.root.setText(JcgxBuild.shortName);
				JcxtflBuild.win.show();
			}
		}, {
			text: i18n.JcgxBuild.delete,
			iconCls: "deleteIcon",
			handler: function() {
				if (!$yd.isSelectedRecord(JcgxBuild.grid)) return;
				Ext.Msg.confirm(i18n.JcgxBuild.prompt, i18n.JcgxBuild.Norepair, function(btn) {
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
								MyExt.Msg.alert(i18n.JcgxBuild.deleteOk);
							} else {
								alertFail(result.errMsg);
							}
						},
						failure: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							MyExt.Msg.alert(i18n.JcgxBuild.false+"\n" + response.status + "\n" + response.responseText);
						}
					});
				});
			}
		}, '->', {
			text:i18n.JcgxBuild.moveTopIcon, iconCls:'moveTopIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:i18n.JcgxBuild.moveUpIcon, iconCls:'moveUpIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:i18n.JcgxBuild.moveDownIcon, iconCls:'moveDownIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:i18n.JcgxBuild.moveBottom, iconCls:'moveBottomIcon', handler: function() {
				JcgxBuild.moveOrder(JcgxBuild.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header: i18n.JcgxBuild.coID, dataIndex: 'coID', hidden: true
		}, {
			header: i18n.JcgxBuild.fjdID, dataIndex: 'fjdID', hidden: true
		}, {
			header: i18n.JcgxBuild.seqNo, dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: i18n.JcgxBuild.flbm, dataIndex: 'flbm', editor: { readOnly: true }, width: 80
		}, {
			header: i18n.JcgxBuild.flmc, dataIndex: 'flmc', editor: { readOnly: true }, width: 200
		}, {
			header: i18n.JcgxBuild.fljc, dataIndex: 'fljc', editor: { readOnly: true },  width: 100
		}, {
			header: i18n.JcgxBuild.wzmc, dataIndex: 'wzmc', width: 100,
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
			header: i18n.JcgxBuild.wzdm, dataIndex: 'wzdm',hidden:true,
			editor: { id: "wzdm_id" }
		}, {
			header: i18n.JcgxBuild.zylxID,
			dataIndex: 'zylxID',
			editor: {
				id: 'id_zylx_id',
				xtype: "ProfessionalType_comboTree",
				fieldLabel: i18n.JcgxBuild.zylxID,
				hiddenName: "zylxID",
				returnField: [{ widgetId: "id_zylx", propertyName: "text" }],
				selectNodeModel: "all"
			},
			renderer: function(value, mateData, record) {
				return record.get('zylx');
			}
		}, {
			header: i18n.JcgxBuild.zylxID, dataIndex: 'zylx', hidden: true, editor: { id: "id_zylx" }
		}, {
			header: i18n.JcgxBuild.jxzy, dataIndex: 'jxzy', width: 60, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'jxzy',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, i18n.JcgxBuild.Y], [0, i18n.JcgxBuild.N]]
				}),
				valueField: 'v',
				displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return i18n.JcgxBuild.Y;
				if (v == 0) return i18n.JcgxBuild.N;
				return "";
			}
		}, {
			header: i18n.JcgxBuild.sfzbzy, dataIndex: 'sfzbzy', width: 60, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'sfzbzy',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, i18n.JcgxBuild.Y], [0, i18n.JcgxBuild.N]]
				}),
				valueField: 'v',
				displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return i18n.JcgxBuild.Y;
				if (v == 0) return i18n.JcgxBuild.N;
				return "";
			}
		}, {
			header: i18n.JcgxBuild.sfsyDzda, dataIndex: 'sfsyDzda', width: 80, align: 'center',
			editor: {
				xtype: 'combo',
				fieldLabel: '',
				width: 20,
				hiddenName: 'sfsyDzda',
				store: new Ext.data.SimpleStore({
					fields: ['v', 't'],
					data: [[1, i18n.JcgxBuild.Y], [0, i18n.JcgxBuild.N]]
				}),
				valueField: 'v', displayField: 't',
				triggerAction: 'all',
				mode: 'local',
				editable: false
			},
			renderer: function(v) {
				if (v == 1) return i18n.JcgxBuild.Y;
				if (v == 0) return i18n.JcgxBuild.N;
				return "";
			}
		}, {
			header: i18n.JcgxBuild.wzqm, dataIndex: 'wzqm',editor: { readOnly: true }, width: 320
		}, {
			header: i18n.JcgxBuild.lbjbm, dataIndex: 'lbjbm', hidden: true
		}, {
			header: i18n.JcgxBuild.pyjc, dataIndex: 'pyjc', hidden: true
		}, {
			header: i18n.JcgxBuild.coHaschild, dataIndex: 'coHaschild', hidden: true
		}, {
			header: i18n.JcgxBuildgxwzbm, dataIndex: 'gxwzbm', hidden: true
		}, {
			header: i18n.JcgxBuild.sycx, dataIndex: 'sycx', hidden: true
		}, {
			header: i18n.JcgxBuild.recordStatus, dataIndex: 'recordStatus', hidden: true
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
			text: i18n.JcgxBuild.choicemalfunction, iconCls: 'addIcon', handler: function() {
				JcxtflFault.win.show();
			}
		},{
			text: i18n.JcgxBuild.addmalfunction, iconCls: 'addIcon',hidden:true, handler: function() {
				JcxtflFault.formWin.show();
			}
		}, 'delete', '->',{
			text:i18n.JcgxBuild.moveTopIcon, iconCls:'moveTopIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_TOP);
			}
		}, {
			text:i18n.JcgxBuild.moveUpIcon, iconCls:'moveUpIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_PRE);
			}
		}, {
			text:i18n.JcgxBuild.moveDownIcon, iconCls:'moveDownIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_NEX);
			}
		}, {
			text:i18n.JcgxBuild.moveBottom, iconCls:'moveBottomIcon', handler: function() {
				JcgxBuild.moveOrderFault(JcgxBuild.jcxtflFaultGrid, ORDER_TYPE_BOT);				
			}
		}],
		fields: [{
			header: i18n.JcgxBuild.coID, dataIndex: 'idx', hidden: true
		}, {
			header: i18n.JcgxBuild.seqNo, dataIndex: 'seqNo',editor: { readOnly: true }
		}, {
			header: i18n.JcgxBuild.flbm, dataIndex: 'flbm'
		}, {
			header: i18n.JcgxBuild.faultId, dataIndex: 'faultId'
		}, {
			header: i18n.JcgxBuild.faultName, dataIndex: 'faultName'
		}, {
			header: i18n.JcgxBuild.faultTypeName, dataIndex: 'faultTypeName'
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
		Ext.Msg.confirm(i18n.JcgxBuild.prompt, i18n.JcgxBuild.confirmPosition, function(btn) {
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
							alertSuccess(i18n.JcgxBuild.success);
						} else {
							alertFail(result.errMsg);
						}
					},
					failure: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						MyExt.Msg.alert(i18n.JcgxBuild.false+"\n" + response.status + "\n" + response.responseText);
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
		title:i18n.JcgxBuild.loadTrain,
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
				fieldLabel:i18n.JcgxBuild.choiceFile,
				name:'jcgx',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: i18n.JcgxBuild.browseFile
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
			text: i18n.JcgxBuild.load, iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var filePath = window.find('name', 'jcgx')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert(i18n.JcgxBuild.msg+'<span style="color:red;"> Excel2003（*.xls） </span>'+i18n.JcgxBuild.msg1);
					return;
				}
				form.submit({
                	url: ctx+'/jcgxBuild!saveImport.action?shortName=' + JcgxBuild.shortName,
                	waitTitle:i18n.JcgxBuild.wait,
               	 	waitMsg: i18n.JcgxBuild.loading, 
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
				       	 	Ext.Msg.alert(i18n.JcgxBuild.prompt, i18n.JcgxBuild.false+"\n" + action.response.status + "\n" + action.response.responseText);
                		}
				    }
            	}); 
			}
		}, {
			text:i18n.JcgxBuild.close, iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	/** 打开机车构型导入窗口 */
	JcgxBuild.openImportWin = function() {
		Ext.Msg.confirm(i18n.JcgxBuild.prompt, i18n.JcgxBuild.loadingOrNot, function(btn) {
			if (btn == 'yes') {
				JcgxBuild.importWin.show();
			}
		});
	};

	// 定义机车构型维护窗口
	JcgxBuild.win = new Ext.Window({
		title: i18n.JcgxBuild.lcm, maximized: true, layout: "border", closeAction: "hide",
		items: [{
			region: "west",
			width: 279,
			collapsible: true,
			iconCls: 'icon-expand-all',
			title: '<span style="font-weight:normal">'+i18n.JcgxBuild.lcnt+'<span style="color:gray;">（*'+i18n.JcgxBuild.cbd+'）</span></span>',
			bbar: [
			{
				text: '<span style="color:gray;">'+i18n.JcgxBuild.td+'</span>', iconCls: 'downloadIcon', handler: JcgxBuild.downloadGxTemp
			},
			'-',
			{
				text: '<span style="color:gray;">'+i18n.JcgxBuild.import+'</span>', iconCls: 'page_excelIcon', handler: JcgxBuild.openImportWin
			},
			'-',
			{
				text: '<span style="color:gray;">'+i18n.JcgxBuild.bnfn+'</span>', iconCls: 'configIcon', handler: JcgxBuild.updateWzqm
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
				title: i18n.JcgxBuild.ssc, layout: "fit", items: [JcgxBuild.grid]
			}, {
				title: i18n.JcgxBuild.sym, layout: "fit", items: [JcgxBuild.jcxtflFaultGrid]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: i18n.JcgxBuild.close, iconCls: 'closeIcon', handler: function() {
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