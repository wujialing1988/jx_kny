/**
 * 通用人员选择下拉控件，支持使用名称的首字母进行快速检索 控件使用方法： 
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/OmEmpBaseCombo.js"></script>
 * 2.在表单页面js添加下例标签代码 {
 * 		id:自定义, 
 * 		xtype: 'Om_Emp_combo',
 * 		hiddenName: 自定义,
 * 		fieldLabel:自定义,
 * 		displayField:自定义, valueField:自定义
 * 	}
 * 3.配置项说明： 
 * 		a.id：控件id，选填 
 * 		b.hiddenName：提交表单域名称，必填 
 * 		c.fieldLabel: 在组件旁边那里显示的label文本，必填
 * 		d.displayField：配置项：配置控件显示字段名，必填
 *  	e.valueField：配置项：配置控件提交值，必填
 *  	f.returnField：配置项: widgetId为需回显信息的field标签的id, propertyName为需回显信息的字段名称，选填
 * 		g.queryParams：配置项：Hql的where条件，选填,注意：格式必须为json格式。
 */
Ext.namespace("jx.js.component.pjwz");
jx.js.component.pjwz.Om_Emp_combo = function() {
	this.entity = 'x';
	this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
	this.store = new Ext.data.SimpleStore({ fields : [], data : [[]] });
	this.mode = 'remote';
	this.triggerAction = 'all';
	this.editable = true;
	this.emptyText = "";
	this.returnField;
	this.pageSize = 20;// 设置分页,默认为20条记录分页
	this.minListWidth = 260;// 设置下拉列表显示宽度
	this.queryHql = '';// 查询Hql
	this.fields = ['empid', 'empcode', 'empname', 'gender', 'orgid', 'orgname', 'orgseq'];
	this.queryParams;
	this.queryName = ''; // 用于控件输入值时,要查询的"字段名称"
	this.minChars = 1;// 设置字符个数开始触发
	this.selectOnFocus = true;
	// this.isSeleced = true;
	this.business = "";
	this.isAll = false; // 查询所有还是部分数据配置项【用于查询作业任务单是否是全部查询，true为全部查询】
	this.action = ctx + "/omEmployeeSelect!pageList.action";
	this.typeAhead = false;
	this.forceSelection = false; // 强制选择 默认为false否
	this.hasEmpty = false; // 是否显示一条空记录，默认为false否
	this.idProperty = 'empid';
	jx.js.component.pjwz.Om_Emp_combo.superclass.constructor.apply(this, arguments);
}
Ext.extend(jx.js.component.pjwz.Om_Emp_combo, Ext.form.ComboBox, {
	setStore : function() {
		var e = this.entity;
		if (!Ext.isEmpty(e)) {
			this.store = new Ext.data.JsonStore({
						// 是否显示一条空记录，默认为false否
						hasEmpty : this.hasEmpty,

						idProperty : this.idProperty,

						root : "root",
						totalProperty : "totalProperty",
						url : this.action
								+ ((this.action.indexOf("?") == -1) ? "?" : "&")
								+ 'queryName=' + this.queryName + '&entity='
								+ this.entity + '&queryHql=' + this.queryHql
								+ '&queryParams='
								+ Ext.util.JSON.encode(this.queryParams)
								+ '&manager=' + this.business + '&isAll='
								+ this.isAll,
						fields : this.fields,

						/**
						 * Modified by hetao at 2015-09-24
						 * 如果配置了显示一条空记录（hasEmpty:
						 * true），则在下拉框中增加一条名为“请选择...”的空记录，用于清空字段值
						 */
						listeners : {
							load : function(store, records, options) {
								if (this.hasEmpty && this.getCount() > 0) {
									var obj = {};
									var fields = this.fields.keys;
									for (var i = 0; i < fields.length; i++) {
										obj[fields[i]] = "请选择...";
									}
									this.insert(0, [new Ext.data.Record(obj)]);
								}
							}
						}
					});
			if (this.pageSize != 0) {
				this.store.load({
							params : {
								start : 0,
								limit : this.pageSize
							}
						});
			} else {
				this.getStore().reload();
			}
		}
	},
	// 重置store，用于级联查询
	cascadeStore : function() {
		this.store.proxy = new Ext.data.HttpProxy({
					url : this.action
							+ ((this.action.indexOf("?") == -1) ? "?" : "&")
							+ 'queryName=' + this.queryName + '&entity='
							+ this.entity + '&queryHql=' + this.queryHql
							+ '&queryParams='
							+ Ext.util.JSON.encode(this.queryParams)
							+ '&manager=' + this.business + '&isAll='
							+ this.isAll
				});
		this.getStore().load();
	},
	// 重写onRender方法
	onRender : function(ct, position) {
		// 设置store
		this.setStore();

		if (this.hiddenName && !Ext.isDefined(this.submitValue)) {
			this.submitValue = false;
		}
		Ext.form.ComboBox.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
						tag : 'input',
						type : 'hidden',
						name : this.hiddenName,
						id : (this.hiddenId || Ext.id())
					}, 'before', true);

		}
		if (Ext.isGecko) {
			this.el.dom.setAttribute('autocomplete', 'off');
		}

		if (!this.lazyInit) {
			this.initList();
		} else {
			this.on('focus', this.initList, this, {
						single : true
					});
		}
	},
	onSelect : function(record, index) {
		if (this.fireEvent('beforeselect', this, record, index) !== false) {
			this.setValue(record);
			this.setReturnValue(record);
			this.isSelected = true;
			this.collapse();
			this.fireEvent('select', this, record, index);
		}
	},
	collapse : function(record, index) {
		if (!this.isExpanded()) {
			return;
		}
		this.list.hide();
		this.assertValue();
		Ext.getDoc().un('mousewheel', this.collapseIf, this);
		Ext.getDoc().un('mousedown', this.collapseIf, this);
		if (!this.isSelected) {
			if (this.getValue() != null && this.getValue() != '') {
				record = this.getStore().getAt(0);
				this.setValue(record);
				this.setReturnValue(record);
				this.isSelected = false;
			}
		}
		this.fireEvent('collapse', this);
	},
	assertValue : function() {
		var val = this.getRawValue(), rec;

		if (this.valueField && Ext.isDefined(this.value)) {
			rec = this.findRecord(this.valueField, this.value);
		}
		if (!rec || rec.get(this.displayField) != val) {
			rec = this.findRecord(this.displayField, val);
		}
		if (!rec && this.forceSelection) {
			if (val.length > 0 && val != this.emptyText) {
				this.el.dom.value = Ext.value(this.lastSelectionText, '');
				this.applyEmptyText();
			} else {
				this.clearValue();
			}
		} else {
			if (rec && this.valueField) {
				// onSelect may have already set the value and by doing so
				// set the display field properly. Let's not wipe out the
				// valueField here by just sending the displayField.
				if (this.value == val) {
					return;
				}
				val = rec.get(this.valueField || this.displayField);
			}
			this.setValue(rec);
		}
	},
	// 设值
	setValue : function(r) {
		if (r == null || r == '' || typeof(r) == 'undefined')
			return this;
		if (typeof(r) == 'object') {
			if (this.hiddenField && typeof(this.hiddenField) != 'undefined') {

				this.hiddenField.value = typeof(r.get(this.valueField)) == 'undefined'
						|| r.get(this.valueField) == 'undefined' ? '' : r
						.get(this.valueField);
			}
			this.lastSelectionText = r.get(this.displayField);
			Ext.form.ComboBox.superclass.setValue.call(this, r
							.get(this.displayField));
			this.value = typeof(r.get(this.valueField)) == 'undefined'
					|| r.get(this.valueField) == 'undefined' ? '' : r
					.get(this.valueField);
		} else if (typeof(r) == 'string') {
			var text = r;
			if (this.valueField) {
				var record = this.findRecord(this.valueField, r);
				if (record) {
					text = record.data[this.displayField];
				} else if (Ext.isDefined(this.valueNotFoundText)) {
					text = this.valueNotFoundText;
				}
			}
			this.lastSelectionText = text;
			if (this.hiddenField) {
				this.hiddenField.value = Ext.value(r, '');
			}
			Ext.form.ComboBox.superclass.setValue.call(this, text);
			this.value = r;
		}
		return this;
	},
	// 设置自定义配置项的回显值
	setReturnValue : function(r) {
		if (Ext.isEmpty(r) || typeof(r) != 'object'
				|| Ext.isEmpty(this.returnField)) {
			return;
		}
		var returnField = this.returnField;
		for (var i = 0; i < returnField.length; i++) {
			// 属性名称
			var propertyName = returnField[i].propertyName;
			if (Ext.isEmpty(propertyName)) {
				continue;
			}
			// 属性值
			var propertyValue = r.data[propertyName];
			if (Ext.isEmpty(propertyValue)) {
				continue;
			}

			var field = Ext.getCmp(returnField[i].widgetId);
			if (field) {
				field.setValue(propertyValue);
				continue;
			}
			field = Ext.get(returnField[i].widgetId);
			if (field)
				field.dom.value = propertyValue;
		}
	},
	// (不建议使用)根据record回显值,参数说明：r：表单记录集record、componentId：该控件id、id_field：该控件id域名、name_field：该控件显示名称域名
	loadRecord : function(r, id_field, name_field, componentId) {
		var p_record = new Ext.data.Record();
		// 设置默认值
		if (r == null || r == '' || typeof(r) == 'undefined' || r == 'null') {
			p_record.set(this.valueField, id_field);
			p_record.set(this.displayField, name_field);
		}
		// 设置回显值
		else if (r != null && r != '' && r != 'null'
				&& typeof(r) != 'undefined') {
			if (r.get(id_field) != null) {
				p_record.set(this.valueField, r.get(id_field));
				p_record.set(this.displayField, r.get(name_field));
			}
		}
		this.setValue(p_record);
	},
	// (不建议使用)清空，参数说明：componentId:控件Id
	clear : function(componentId) {
		var p_record = new Ext.data.Record();
		p_record.set(this.valueField, "");
		p_record.set(this.displayField, "");
		Ext.getCmp(componentId).setValue(p_record);
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
});
Ext.reg('Om_Emp_combo', jx.js.component.pjwz.Om_Emp_combo);