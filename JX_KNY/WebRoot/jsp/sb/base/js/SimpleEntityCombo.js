Ext.ns('Ext.yunda');

/**
 * **** **** **** **** **** **** **** **** **** **** *
 * 	简单实体动态下拉框控件[xtype : simpleentitycombo]
 * **** **** **** **** **** **** **** **** **** **** *
 * @author 何涛
 * Created on 2016-10-14 16:50
 * @class Ext.yunda.SimpleEntityCombo
 * @extends Ext.form.ComboBox
 * 说明：分页查询下拉框数据源，获取指定实体的某一个字段值，如果查询的实体entity实现了IPyFilter.java接口，该控件还支持拼音首字母过滤的功能
 * 
 * 配置说明：
 *  1、entity：指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
 *  2、fields：指定实体的字段（属性）名称数组，用于JsonStore进行解析
 *  3、returnField：回显字段配置json对象数组，如：[{widgetId: 'widgetId', fieldName: 'fieldName'}]
 *  4、displayFields：设置控件显示的字段内容，使用场景：displayField只能显示一个字段，但需要显示多个字段时，可以使用该配置项拼接要显示的多个字段，
 *  5、whereList：hql查询条件对象数组，如：[{propName: 'idx', propValue: '8a8284f257e0d8d00157e0db7ef60000'}]
 *  【重要说明：如果配置了displayField，则displayFields配置无效】
 * 
 * 基本用法示例：
 *  1、引入文件：<script type="text/javascript" src="<%= ctx %>/frame/component/SimpleEntityCombo.js"></script>
 * 	2、{
 * 		xtype: 'simpleentitycombo',
		// 指定实体的全路径类名称
		entity: 'com.yunda.sbgl.certificate.entity.CertificateClass', 
		hiddenName: 'certificateIdx',
		valueField: 'idx', displayField: 'certificateName',
		fields: ['idx', 'certificateName', 'certificateNamePY', 'awardInstitution'],
		returnField: [{
			widgetId: 'id_text', propertyName: 'certificateNamePY'
		}]
	}
 */
Ext.yunda.SimpleEntityCombo = Ext.extend(Ext.form.ComboBox, {
	
	/** ******** 自定义配置开始 ******** */
	entity: null,			// 指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
	fields: [],				// 指定实体的字段（属性）名称数组，用于JsonStore进行解析
	returnField: [],		// 回显字段配置json对象数组，如：[{widgetId: 'widgetId', fieldName: 'fieldName'}]
	displayFields: [],		// 设置控件显示的字段内容，使用场景：displayField只能显示一个字段，但需要显示多个字段时，可以使用该配置项拼接要显示的多个字段
	whereList: [],			// hql查询条件对象数组，如：[{propName: 'idx', propValue: '8a8284f257e0d8d00157e0db7ef60000'}]
	emptyObjcet: false,		// 是否在查询出的下拉列表中增加一个空选项，默认为false，不增加
	/** ******** 自定义配置结束 ******** */
	
	/** ******** 重写父类配置开始 ******** */
	fieldLabel: '简单实体动态下拉框',
	forceSelection: true,
	triggerAction: 'all',
	pageSize: 10,
	minListWidth: 260,		// 设置下拉列表显示宽度
	minChars: 1,			// 设置字符个数开始触发后端查询
	/** ******** 重写父类配置结束 ******** */
	
	/** ******** 重写父类函数开始 ******** */
	// private
    onSelect : function(record, index){
        if(this.fireEvent('beforeselect', this, record, index) !== false){
            this.setValue(record);
            // 根据配置的回显字段（returnField）信息，回显字段数据
            this.setReturnValue(record);
            this.collapse();
            this.fireEvent('select', this, record, index);
        }
    },
	
	// private
	initComponent : function() {
		Ext.yunda.SimpleEntityCombo.superclass.initComponent.call(this);
		
		var me = this;
		
		// 如果配置了displayFields，则按displayFields配置的字段显示空间字面值
		if (Ext.isEmpty(me.displayFields) && null == me.displayField) {
			me.displayField = "TEXT";
		}
		
		this.store = new Ext.data.JsonStore({
			idProperty: 'idx',
			root: 'root',
			totalProperty: 'totalProperty',
			url: ctx + '/dynamicCombo!simpleEntityDataSource.action',		// com.yunda.cmp.biz.combo.DynamicCombo.java
			fields: this.fields,
			baseParams: {
				entity: this.entity,
				displayFields: Ext.isEmpty(this.displayFields) ? Ext.encode([this.displayField]) : Ext.encode(this.displayFields)
			},
			listeners: {
				// 拼接控件显示的多个字段
				load: function(store, records, options) {
					// 如果配置了displayField，则displayFields配置无效
					if (!Ext.isEmpty(me.displayFields) && Ext.isEmpty(me.displayField)) {
						store.each(function(record) {
							var displayValues = [];
							for (var i = 0; i < me.displayFields.length; i++) {
								displayValues.push(record.get(this.displayFields[i]));
							}
							record.set(me.displayField, displayValues.join(' '));
						}, me);
					}
					// 如果配置了在下拉列表中增加一个空选项，则在数据加载完成后，在索引0处增加空选项
					if (me.emptyObjcet === true) {
						var r = new Ext.data.Record();
						r.set(me.valueField, "");
						r.set(me.displayField, "无");
						store.insert( 0, [r] )
					}
				},
				beforeload: function() {
					this.baseParams.whereListJson = Ext.encode(me.whereList);
				}
			}
		});
	},
	
	// private
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
				this.el.dom.value = Ext.value(
						this.lastSelectionText, '');
				this.applyEmptyText();
			} else {
				this.clearValue();
			}
		} else {
			// Modified by hetao on 2016-12-12 优化控件，在控件可编辑，非强制选中时，可以将控件字面值进行存储
			// 注意这样使用的前提是：displayField == valueField && forceSelection == true && editable == true
			if (rec) {
				this.setValue(rec);
			} else {
				this.setDisplayValue(val, val);
			}
		}
	},
    
    // private
	setValue : function(r) {
		if (r == null || r == '' || typeof (r) == 'undefined') return this;
		if (typeof (r) == 'object') {
			if (this.hiddenField && typeof (this.hiddenField) != 'undefined') {
				this.hiddenField.value = typeof (r.get(this.valueField)) == 'undefined' || r.get(this.valueField) == 'undefined' ? '' : r.get(this.valueField);
			}
			this.lastSelectionText = r.get(this.displayField);
			Ext.form.ComboBox.superclass.setValue.call(this, r.get(this.displayField));
			this.value = typeof (r.get(this.valueField)) == 'undefined' || r.get(this.valueField) == 'undefined' ? '' : r.get(this.valueField);
		} else if (typeof (r) == 'string') {
			Ext.form.ComboBox.superclass.setValue.call(this, r);
		}
		return this;
	},
   
	//清空(new)
	clearValue : function() {
		var p_record = new Ext.data.Record();
		p_record.set(this.valueField, "");
		p_record.set(this.displayField, "");
		this.setValue(p_record);
	},
	/** ******** 重写父类函数结束 ******** */
	
	/** ******** 自定义函数开始 ******** */
	/**
	 * 重新加载控件数据源
	 */
	reload: function() {
		var store = this.getStore();
		var lastOptions = store.lastOptions;
		if (!lastOptions) {
			return;
		}
		Ext.apply(lastOptions.params, {
			whereListJson: Ext.encode(this.whereList),
			start: 0,
			limit: this.pageSize
		});
		store.reload(lastOptions);
	},
	
	/**
	 * 根据配置的回显字段（returnField）信息，回显字段数据
	 */
	setReturnValue: function(r) {
		if (Ext.isEmpty(this.returnField)) {
			return;
		}
		Ext.each(this.returnField, function(obj){
			if (Ext.getCmp(obj.widgetId)) {
				Ext.getCmp(obj.widgetId).setValue(r.get(obj.propertyName));
			}
		});
	},
	
	/**
	 * 回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
	 */
	setDisplayValue : function(valueField, displayField) {
		var p_record = new Ext.data.Record();
		p_record.set(this.valueField, valueField);
		p_record.set(this.displayField, displayField);
		this.setValue(p_record);
	}
	/** ******** 自定义函数结束 ******** */
	
})

Ext.reg('simpleentitycombo', Ext.yunda.SimpleEntityCombo);