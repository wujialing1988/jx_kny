Ext.ns('Ext.yunda');

/**
 * **** **** **** **** **** **** **** **** **** **** *
 * 	动态下拉框控件[xtype : singlefieldcombo]
 * **** **** **** **** **** **** **** **** **** **** *
 * @author 何涛
 * Created on 2016-10-13 21:15
 * @class Ext.yunda.SingleFieldCombo
 * @extends Ext.form.ComboBox
 * 说明：分页查询下拉框数据源，获取指定实体的某一个字段值，如果查询的实体entity实现了IPyFilter.java接口，该控件还支持拼音首字母过滤的功能
 * 
 * 配置说明：
 *  1、entity：指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
 *  2、xfield：指定的查询的字段名称，如：“awardInstitution”
 *  3、whereList：hql查询条件对象数组，如：[{propName: 'idx', propValue: '8a8284f257e0d8d00157e0db7ef60000'}]
 *  
 * 基本用法示例：
 *  1、引入文件：<script type="text/javascript" src="<%= ctx %>/frame/component/SingleFieldCombo.js"></script>
 * 	2、{
		xtype: 'singlefieldcombo',
		// 指定实体的全路径类名称
		entity: 'com.yunda.sbgl.certificate.entity.CertificateClass', 
		// 指定的查询的字段名称
		xfield: 'awardInstitution'
	}
 */
Ext.yunda.SingleFieldCombo = Ext.extend(Ext.form.ComboBox, {
	
	/** ******** 自定义配置开始 ******** */
	entity: null,			// 指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
	xfield: null,			// 指定的查询的字段名称，如：“awardInstitution”
	whereList: [],			// hql查询条件对象数组，如：[{propName: 'idx', propValue: '8a8284f257e0d8d00157e0db7ef60000'}]
	/** ******** 自定义配置结束 ******** */
	
	/** ******** 重写父类配置开始 ******** */
	width: 140,
	fieldLabel: '动态下拉框',
	triggerAction: 'all',
	pageSize: 10,
	minListWidth: 260,		// 设置下拉列表显示宽度
	minChars: 1,			// 设置字符个数开始触发后端查询
	/** ******** 重写父类配置结束 ******** */
	
	// private
	constructor: function(config){
		Ext.yunda.SingleFieldCombo.superclass.constructor.call(this, Ext.apply(config, {
			// 初始化组件属性
            name: config.name || config.xfield,
            valueField: config.valueField || config.xfield,
            displayField: config.displayField || config.xfield
        }));
    },
	
	// private
	initComponent : function() {
		Ext.yunda.SingleFieldCombo.superclass.initComponent.call(this);
		
		var me = this;
		
		this.store = new Ext.data.JsonStore({
			idProperty: this.xfield,
			root: 'root',
			totalProperty: 'totalProperty',
			url: ctx + '/dynamicCombo!singleFieldDataSource.action',		// com.yunda.cmp.biz.combo.DynamicCombo.java
			fields: [this.xfield],
			baseParams: {
				entity: this.entity,
				xfield: this.xfield
			},
			listeners: {
				beforeload: function() {
					this.baseParams.whereListJson = Ext.encode(me.whereList);
				}
			}
		});
	},
	
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
	}
	/** ******** 自定义函数结束 ******** */
})

Ext.reg('singlefieldcombo', Ext.yunda.SingleFieldCombo);