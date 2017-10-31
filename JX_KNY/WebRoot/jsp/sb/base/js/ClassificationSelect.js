Ext.ns('Ext.yunda');

/**
 * 节点选择模型
 */ 
Ext.yunda.selectNodeModel = {
	all : 'all', 				// 所有结点都可选中
	exceptRoot :'exceptRoot',	// 除根结点，其它结点都可选
	folder : 'folder', 			// 只有目录（非叶子和非根结点）可选
	leaf : 'leaf' 				// 只有叶子结点可选
};
Ext.yunda.snm = Ext.yunda.selectNodeModel;

/**
 * **** **** **** **** **** **** **** **** **** **** *
 * 	设备类别树选择控件[xtype : classificationSelect]
 * **** **** **** **** **** **** **** **** **** **** *
 * @author 何涛
 * Cteated on 2016-09-07 22:24
 * Modified by hetao on 2017-02-17 14:50 扩展控件，增加设备类别列表选择模式
 * @class Ext.yunda.ClassificationSelect
 * @extends Ext.form.TriggerField
 * 基本用法示例：
 *  1、引入文件：<script type="text/javascript" src="<%= ctx %>/frame/component/ClassificationSelect.js"></script>
 * 	2、{
		xtype: 'classificationSelect',  hiddenName: 'classCode',
		returnField: [{
			widgetId: 'id_class_name', propertyName: 'className'
		}]
	}, {
		fieldLabel: '设备类别名称', xtype: 'textfield', 
		id: 'id_class_name', name: 'className'
	}
 */

/**
 * 设备类别选择树
 */
Ext.yunda.ClassificationTree = Ext.extend(Ext.tree.TreePanel, {
	parentObj : null,			// Ext.yunda.ClassificationSelect 的对象引用，用于设置控件的值
	autoScroll : true,
	animate : true,
	title : '树形模式',
	
	/** 构造函数 */
	constructor : function(options) {
		/** 必要 */
		Ext.apply(this, options);
		
		Ext.yunda.ClassificationTree.superclass.constructor.call(this, {
			loader : new Ext.tree.TreeLoader({
				url : ctx + "/classification!findTree.action"
			}),
			root : new Ext.tree.AsyncTreeNode({
				text : "设备类别",
				id : "root",
				expanded : true
			}),
			listeners : {
				beforeload : function(node) {
					this.loader.baseParams.parentCode = node.id;
				},
				// 双击确认选择设备类别
				dblclick : function( node, e ) {
					// 如果节点选择模型不是可以选择所有节点（包括根节点）
					if (this.parentObj.selectNodeModel != Ext.yunda.snm.all) {
						// 只选择叶子节点
						if (this.parentObj.selectNodeModel == Ext.yunda.snm.leaf && !node.leaf) {
							return;
						}
						// 除根结点，其它结点都可选
						if (this.parentObj.selectNodeModel == Ext.yunda.snm.exceptRoot && node.isRoot) {
							return;
						}
						// 只有目录（非叶子和非根结点）可选
						if (this.parentObj.selectNodeModel == Ext.yunda.snm.folder && (node.leaf || node.isRoot)) {
							return;
						}
					}
					// 声明一个Ext.data.Record对象
					var r = new Ext.data.Record();
					r.set(this.parentObj.valueField, node.id);
			        r.set(this.parentObj.displayField, node.attributes.className);
			        
            		this.parentObj.setReturnValue(r);
			        this.parentObj.setValue(r);
            		this.parentObj.fireEvent('select', r);
            		// 隐藏设备类别树选择控件窗体
			        this.ownerCt.ownerCt.hide();
				}
			}
		});
	}
});

/**
 * 设备类别选择列表
 */
Ext.yunda.ClassificationGrid = Ext.extend(Ext.yunda.Grid, {
	parentObj : null,			// Ext.yunda.ClassificationSelect 的对象引用，用于设置控件的值
	title : '列表模式',
	
	/** 构造函数 */
	constructor : function(options) {
		/** 必要 */
		Ext.apply(this, options);
		
		Ext.yunda.ClassificationGrid.superclass.constructor.call(this, {
			loadURL: ctx + '/classification!pageQuery.action',
			singleSelect: true,
			tbar: [{
				xtype: 'textfield', emptyText: '按编码或名称检索', enableKeyEvents: true, listeners: {
					keyup: function ( me, e ) {
						var grid = this.ownerCt.ownerCt;
						if (e.keyCode == e.ENTER) {
							grid.store.load();
						}
						if (Ext.isEmpty(me.getValue())) {
							grid.store.load();
						}
					}
				}
			}, {
				text: '查询', iconCls: 'searchIcon', handler: function() {
					var grid = this.ownerCt.ownerCt;
					grid.store.load();
				}
			}, {
				text: '重置', iconCls: 'resetIcon', handler: function() {
					var grid = this.ownerCt.ownerCt;
					var field = grid.getTopToolbar().findByType('textfield')[0];
					if (Ext.isEmpty(field.getValue())) {
						return;
					}
					field.reset();
					grid.store.load();
				}
			}, '->', '-', {
				xtype: 'button', iconCls: 'messageIcon', tooltip: "I'll tell you!", handler: function() {
					Ext.Msg.show({
					   title: '提示',
					   msg: '双击一个设备类别可以快速添加！',
					   buttons: Ext.MessageBox.OK,
					   icon: Ext.MessageBox.INFO
					})		
				}
			
			}],
			
			fields: [{
				header: "设备类别编码", dataIndex: "classCode", width: 2
			}, {
				header: "设备类别名称", dataIndex: "className", width: 3
			}],
			
			toEditFn: function(grid, rowIndex, e) {
				var record = grid.store.getAt(rowIndex);
				// 声明一个Ext.data.Record对象
				var r = new Ext.data.Record();
				r.set(this.parentObj.valueField, record.data.classCode);
		        r.set(this.parentObj.displayField, record.data.className);
		        
        		this.parentObj.setReturnValue(r);
		        this.parentObj.setValue(r);
        		this.parentObj.fireEvent('select', r);
        		// 隐藏设备类别树选择控件窗体
		        this.ownerCt.ownerCt.hide();
			}
		})
	}
});
	
// 设备类别树容器窗口
Ext.yunda.ClassificationWindow = Ext.extend(Ext.Window, {
	title : '设备类别选择',
	tree : null,
	parentObj : null, 			// 设备类别树对象引用
	width : 420, height : 500,
	modal : true,
	closeAction : 'hide',
	buttonAlign : 'center',
	xtype : 'classificationwindow',
	layout: 'fit',
	buttons : [{
		text : '关闭', iconCls : 'closeIcon', handler: function() {
			this.ownerCt.ownerCt.hide();
		}
	}, {
		text : '确定', iconCls : 'yesIcon', handler: function() {
			var win = this.ownerCt.ownerCt;
			var tab = win.findByType('tabpanel')[0];
			var activeTab = tab.getActiveTab();
			// 声明一个Ext.data.Record对象
			var r = new Ext.data.Record();
			// 列表模式选择
			if ('列表模式' === activeTab.title) {
				var sm = win.grid.getSelectionModel();
				if (0 >= sm.getCount()) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				var record = sm.getSelections()[0];
				r.set(win.parentObj.valueField, record.data.classCode);
		        r.set(win.parentObj.displayField, record.data.className);
			} else {
				var tree = win.tree;
				var sm = tree.selModel;
				var node = sm.getSelectedNode();
				if (Ext.isEmpty(node)) {
					MyExt.Msg.alert('尚未选择任何设备类别！');
					return;
				}
				if (!node.leaf) {
					MyExt.Msg.alert('请选择设备类别树叶子节点！');
					return;
				}
				r.set(win.parentObj.valueField, node.id);
		        r.set(win.parentObj.displayField, node.attributes.className);
			} 
	        
	        win.parentObj.setReturnValue(r);
	        win.parentObj.setValue(r);
	        win.parentObj.fireEvent('select', r);
    		win.hide();
		}
	}],
	
	/** 构造函数 */
	constructor : function(options) {
		/** 必要 */
		Ext.apply(this, options);
		var me = this;
		
		// 设备类别树对象引用
		me.tree = new Ext.yunda.ClassificationTree({
			parentObj : me.parentObj
		});
		// 设备类别树对象引用
		me.grid = new Ext.yunda.ClassificationGrid({
			parentObj : me.parentObj
		});
		me.grid.store.on('beforeload', function() {
			var tbar = me.grid.getTopToolbar();
			var whereList = [];
			var classCodeOrName = tbar.findByType('textfield')[0].getValue();
			if (!Ext.isEmpty(classCodeOrName)) {
				whereList.push({
					compare: Condition.SQL, sql: "(CLASS_CODE LIKE '%"+ classCodeOrName + "%' OR CLASS_NAME LIKE '%" + classCodeOrName + "%')"
				});
			}
			this.baseParams.whereListJson = Ext.encode(whereList);
			this.baseParams.sort = 'classCode';
			this.baseParams.dir = 'ASC';
		});
		Ext.yunda.ClassificationWindow.superclass.constructor.call(me, {
			plain: true,
			items: [{
				xtype: 'tabpanel', defaults: {border: false},
				activeTab: 0,
				items: [ me.grid, me.tree ]
			}]
		})
	}
});

/**
 * 设备类别数据选择控件
 * @extend Ext.form.TriggerField
 */
Ext.yunda.ClassificationSelect =  Ext.extend(Ext.form.TriggerField, {
	fieldLabel : '设备类别',
	valueField : 'id',
	displayField : 'className',
	triggerClass : 'x-form-search-trigger',
	win : null,
	editable : false,
	returnField : [],
	selectNodeModel: Ext.yunda.snm.leaf,		// 节点选择模型
	/**
	 * @Override 自定义控件必要方法
	 */
	onTriggerClick : function( e ){
		this.win.show();
	},
	// private
	submitValue : undefined,
	// private
	onRender : function(ct, position) {
		if (this.hiddenName && typeof this.submitValue == 'undefined') {
			this.submitValue = false;
		}
		Ext.yunda.ClassificationSelect.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
				tag : 'input',
				type : 'hidden',
				name : this.hiddenName,
				id : (this.hiddenId || Ext.id())
			}, 'before', true);
		}
		if (!this.editable) {
			this.editable = true;
			this.setEditable(false);
		}
	},
	// private
    initValue : function(){
    	Ext.yunda.ClassificationSelect.superclass.initValue.call(this);
        if(this.hiddenField){
            this.hiddenField.value =
                Ext.value(Ext.isDefined(this.hiddenValue) ? this.hiddenValue : this.value, '');
        }
    },
    // private
	initComponent : function() {
		this.win = new Ext.yunda.ClassificationWindow({
			parentObj : this
		});
		Ext.yunda.ClassificationSelect.superclass.initComponent.call(this);
	},
	// 设置自定义配置项的回显值
	setReturnValue : function(r) {		
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{
			var returnField = this.returnField;
	        if(returnField != null && returnField.length > 0){        	
	        	for(var i = 0;i < returnField.length;i++){
					var displaytext = returnField[i].propertyName;
					var fieldName = '';
					if( typeof(r) != 'undefined' && typeof(r.get(displaytext)) != 'undefined'){
						fieldName = r.get(displaytext);
					}							
					if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
						&& fieldName != '' && fieldName != null && fieldName != 'null'){
						Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
					}
					//针对html标签
					else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
							&& fieldName != '' && fieldName != null && fieldName != 'null'){
						Ext.get(returnField[i].widgetId).dom.value = fieldName;
					}	        		
	        	}        	
	        }
		}
	},
	// 设置值，r为Ext.data.Record类型的参数
	setValue : function(r) {
		if (Ext.isEmpty(r) || !Ext.isObject(r)) {
			return;
		}
		if (this.hiddenField && typeof (this.hiddenField) != 'undefined') {
			this.hiddenField.value = typeof (r.get(this.valueField)) == 'undefined' || r.get(this.valueField) == 'undefined' ? '' : r.get(this.valueField);
		}
		Ext.yunda.ClassificationSelect.superclass.setValue.call(this, r.get(this.displayField));
		this.value = typeof (r.get(this.valueField)) == 'undefined' || r.get(this.valueField) == 'undefined' ? '' : r.get(this.valueField);
		return this;
	},
	// 回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
	setDisplayValue : function(valueField, displayField) {
		var p_record = new Ext.data.Record();
		p_record.set(this.valueField, valueField);
		p_record.set(this.displayField, displayField);
		this.setValue(p_record);
	},
	// 清空(new)
    clearValue : function() {
		var p_record = new Ext.data.Record();
		p_record.set(this.valueField, "");
		p_record.set(this.displayField, "");
		this.setValue(p_record);
	}
})
Ext.reg('classificationSelect', Ext.yunda.ClassificationSelect);
