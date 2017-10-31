/**
 * 设备类别 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	
	Ext.ns("Classification");		//	自定义命名空间
	
	/** **************** 定义私有变量开始 **************** */
	var treePath, treeNode;
	var labelWidth = 100, fieldWidth = 140;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义设备类别树开始 **************** */
	Classification.tree = new Ext.tree.TreePanel({
		title: '设备类别树',
		loader : new Ext.tree.TreeLoader({
			url : ctx + "/classification!findTree.action"
		}),
		root : new Ext.tree.AsyncTreeNode({
			id : "root", text : "设备类别", expanded : true,
			listeners: {
				load: function() {
					if (treePath) {
						Classification.tree.expandPath(treePath);
						Classification.tree.selectPath(treePath, null, function(bSuccess, oSelNode) {
							if (!bSuccess) {
								Classification.tree.root.select();
							}
						});
					} else {
						this.select();
					}
				}
			}
		}),
		autoScroll : true,
		animate : true,
		listeners : {
			"beforeload" : function(node) {
				this.loader.baseParams.parentCode = node.id;
			}
		}
	});
	Classification.tree.getSelectionModel().on('selectionchange', function(me, node) {
		if (!node) {
			return;
		}
		treePath = node.getPath();
		treeNode = node;
		Classification.grid.store.load();
	});
	/** **************** 定义设备类别树结束 **************** */
	
	/** **************** 定义设备类别列表开始 **************** */
	Classification.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/classification!pageQuery.action',
		saveURL: ctx + '/classification!saveOrUpdate.action',
		deleteURL: ctx + '/classification!logicDelete.action',
		storeAutoLoad: false,
		border: true,
		saveWinWidth: 560,
		tbar: ['add', 'delete', 'refresh'],
		saveForm: new Ext.form.FormPanel({
			padding: '10',
			baseCls: 'plain',
			labelWidth: labelWidth, defaultType: 'hidden',
			items: [{
				xtype: 'container', layout: 'column', defaults: {
					xtype: 'container', columnWidth: .5, layout: 'form', labelAlign: 'left',
					defaults: {
						xtype: 'textfield', width: fieldWidth, maxLength: 6
					}
				},
				items: [{
					items: [{
						fieldLabel: '父类编码', name: 'parentCode', xtype: 'displayfield'
					}, {
						fieldLabel: '设备类别编码', name: 'classCode', maxLength: 30, allowBlank: false
					}, {
						fieldLabel: '折旧率', name: 'depercitionRate', allowBlank: false, vtype: 'positiveFloat',
						validator: function(v) {
							if (v >= 1000) {
								return "请输入小于1000的浮点数！";
							}
							return true;
						}
					}, {
						fieldLabel: '预计使用年限', name: 'expectUseYear', maxLength: 3, allowBlank: false
					}, {
						fieldLabel: '规格表达式', name: 'specificationExpress', maxLength: 50
					}]
				}, {
					items: [{
						fieldLabel: '父类名称', name: 'parentName', xtype: 'displayfield'
					}, {
						fieldLabel: '设备类别名称', name: 'className', maxLength: 30, allowBlank: false
					}, {
						fieldLabel: '残值率', name: 'residualRate', allowBlank: false, vtype: 'positiveFloat',
						validator: function(v) {
							if (v >= 1000) {
								return "请输入小于1000的浮点数！";
							}
							return true;
						}
					}, {
						fieldLabel: '规格型号', name: 'specification', maxLength: 20
					}, {
						fieldLabel: '型号表达式', name: 'modelExpress', maxLength: 50
					}]
				}]
			}, {
				xtype: 'textarea', fieldLabel: '备注', name: 'remark', width: 402, maxLength: 200
			}, {
				name: 'idx'
			}, {
				name: 'leaf', /*xtype: 'textfield',*/ fieldLabel: '是否叶子'
			}, {
				name: 'parentIdx'
			}]
		}),
		fields: [{
			header: 'idx主键', dataIndex: 'idx', hidden: true
		},{
			header: "叶子节点", dataIndex: "leaf", hidden: true
		},{
			header: "父类编码", dataIndex: "parentIdx", hidden: true
		},{
			header: "设备类别编码", dataIndex: "classCode", editor: {xtype: 'displayfield'}
		},{
			header: "设备类别名称", dataIndex: "className"
		},{
			header: "折旧率", dataIndex: "depercitionRate"
		},{
			header: "残值率", dataIndex: "residualRate"
		},{
			header: "预计使用年限", dataIndex: "expectUseYear"
		},{
			header: "规格型号", dataIndex: "specification"
		},{
			header: "规格表达式", dataIndex: "specificationExpress"
		},{
			header: "型号表达式", dataIndex: "modelExpress"
		},{
			header: "备注", dataIndex: "remark"
		}],
		
		afterDeleteFn: function() {
			Classification.tree.root.reload();
		},
	
		afterSaveSuccessFn: function() {
			Classification.tree.root.reload();
			Classification.grid.store.reload();
		},
		
		afterShowSaveWin: function() {
			this.saveForm.find('name', 'parentIdx')[0].setValue(treeNode.attributes['idx']);
			var parentCode = treeNode.attributes['classCode'];
			var parentName = treeNode.attributes['className'];
			if ('root' == treeNode.id) {
				parentCode = parentName = "<span style=\"color:red;\">无</span>";
			} else {
				this.saveForm.find('name', 'classCode')[0].setValue(treeNode.id).focus(true);
			}
			this.saveForm.find('name', 'parentCode')[0].setValue(parentCode);
			this.saveForm.find('name', 'parentName')[0].setValue(parentName);
			this.saveForm.find('name', 'leaf')[0].setValue(IS_LEAF_YES);
		},
		
		afterShowEditWin: function() {
			var parentCode = treeNode.attributes['classCode'];
			var parentName = treeNode.attributes['className'];
			if (treeNode.leaf) {
				parentCode = treeNode.parentNode.attributes['classCode'];
				parentName = treeNode.parentNode.attributes['className'];
			} else if ('root' == treeNode.id) {
				parentCode = parentName = "<span style=\"color:red;\">无</span>";
			}
			this.saveForm.find('name', 'parentCode')[0].setValue(parentCode);
			this.saveForm.find('name', 'parentName')[0].setValue(parentName);
		}
	
	});
	
	Classification.grid.store.on('beforeload', function() {
		var whereList = [];
		var sp = {};
		if (treeNode.id == 'root') {
			whereList.push({
				compare: Condition.SQL, sql: 'PARENT_IDX IS NULL'
			});
		} else {
			if (treeNode.leaf) {
				sp.idx = treeNode.attributes['idx']
			} else {
				sp.parentIdx = treeNode.attributes['idx']
			}
			for (var prop in sp) {
				whereList.push({
					propName: prop, propValue: sp[prop]
				});
			}
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
		this.baseParams.sort = "classCode";
		this.baseParams.dir = 'ASC';
	});
	/** **************** 定义设备类别列表结束 **************** */
	
	new Ext.Viewport({
		layout: "border",
		defaults: {
			region: 'center', xtype: 'container', layout: 'fit'
		},
		items:[{
			region: 'west', width: 300, split: true,
			items: Classification.tree
		}, {
			items: Classification.grid
		}]
	});
	
});