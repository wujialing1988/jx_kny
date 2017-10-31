/**
 * BPS流程选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/WfprocessdefineSelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'Wfprocessdefine_SelectWin',editable:false,fieldLabel : 自定义,hiddenName:自定义,returnField:[{widgetId:"自定义",propertyName:"自定义"}]}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * f.valueField配置项：本控件提交值，默认为"idx"，可自定义所需字段
 * g.displayField配置项：本控件显示值，默认为"partsName",可自定义所需字段
 * 回显字段名称列表：参见Wfprocessdefine实体类
 * h.queryHql配置项：自定义Hql，暂只接受Wfprocessdefine实体类对象查询，必须为带where子句的hql,如"from Wfprocessdefine where 1=1",可选
 * I.entity配置项：默认配置为"com.yunda.flow.core.entity.Wfprocessdefine"，可选
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(valueField,displayField);
 *   componentId 控件id，参数说明：valueField：该控件valueField值、displayField：该控件displayField值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id           
 */

Wfprocessdefine_store = new Ext.data.JsonStore({
	 id:"processdefid", root:"root", totalProperty:"totalProperty",autoLoad:true,
    url: ctx + "/wfprocessdefineSelect!findListForSelect.action",
    fields: [ "processdefid","processdefname","processchname" ]
});
//分页工具
Wfprocessdefine_pagingToolbar = Ext.yunda.createPagingToolbar({store: Wfprocessdefine_store});
//互换配件列表
Wfprocessdefine_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		Wfprocessdefine_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			// 偶数行变色
			stripeRows : true,
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{ sortable:true, header:"流程定义名称", dataIndex:"processdefname" },			
		        { sortable:true, header:"流程显示名称", dataIndex:"processchname" }]),
			store : Wfprocessdefine_store,
			bbar: Wfprocessdefine_pagingToolbar,
			tbar : [{
						xtype:"label", text:"流程显示名： "
					},{	            
		                xtype:"textfield",								                
		                name : "processchname",
				        width: 240
					},{
						text : "搜索",
						iconCls : "searchIcon",
						handler : function(){
							var searchText = this.getTopToolbar().get(1).getValue();
							var searchParam = {};								            
				            searchParam.processchname = searchText;
				            Wfprocessdefine_store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							Wfprocessdefine_store.load();
						},
						scope: this
					},{
						text : "重置",
						iconCls : "resetIcon",
						handler : function(){
							var searchParam = {};	
							Wfprocessdefine_store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							Wfprocessdefine_store.load();
							this.getTopToolbar().get(1).setValue("");
						},
						scope : this
					}],
			listeners:{
				//双击选择配件
				"rowdblclick": {
	            	fn: function(grid, idx, e){
	            		var r = grid.store.getAt(idx);
	            		this.parentObj.parentObj.setValue(r);
	            		this.parentObj.parentObj.setReturnValue(r);
	            		this.parentObj.close();
	            	}
				}
			}
		});
	}
});

/** ****************************************************************************************** */
//弹出窗口
Process_SelectWin = Ext.extend(Ext.Window, {
	grid : new Wfprocessdefine_List(),
	parentObj:null,	
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
    	this.grid.getStore().load({
			params:{
				entity:this.entity,
				queryHql:this.queryHql
			}			
		});
		this.grid.getStore().baseParams.entity = this.entity;
		this.grid.getStore().baseParams.queryHql = this.queryHql;
    	//Ext.Window源代码
        delete this.el.lastXY;
        delete this.el.lastLT;
        if(this.x === undefined || this.y === undefined){
            var xy = this.el.getAlignToXY(this.container, 'c-c');
            var pos = this.el.translatePoints(xy[0], xy[1]);
            this.x = this.x === undefined? pos.left : this.x;
            this.y = this.y === undefined? pos.top : this.y;
        }
        this.el.setLeftTop(this.x, this.y);

        if(this.expandOnShow){
            this.expand(false);
        }

        if(this.modal){
            Ext.getBody().addClass('x-body-masked');
            this.mask.setSize(Ext.lib.Dom.getViewWidth(true), Ext.lib.Dom.getViewHeight(true));
            this.mask.show();
        }
    },
	constructor : function() {		
		Process_SelectWin.superclass.constructor.call(this, {
			title : "流程选择",
			width : 550,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "fit",
			items : [this.grid]
		});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
Wfprocessdefine_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'processdefname',
	displayField : 'processchname',
	hiddenName : 'Wfprocessdefine_SelectId',
	editable :true,			
	win : new Process_SelectWin(),
	triggerClass:'x-form-search-trigger',			
	returnField:[],
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;	
			this.win.entity = this.entity;
			this.win.queryHql = this.queryHql;
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		Wfprocessdefine_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		Wfprocessdefine_Select.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
						tag : 'input',
						type : 'hidden',
						name : this.hiddenName,
						id : (this.hiddenId || Ext.id())
					}, 'before', true);
		}
		if(!this.editable){
            this.editable = true;
            this.setEditable(false);
        }
	},
	
	initValue : function() {
		Wfprocessdefine_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		Wfprocessdefine_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| Wfprocessdefine_Select.superclass.getName.call(this);
	},			
	//获取返回值			
	getValue : function() {				
		return typeof this.value != 'undefined'&& this.value != 'undefined'? this.value : '';
	},
	//设置值，r为Ext.data.Record类型的参数
	setValue : function(r) {				
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{					
			if (this.hiddenField && typeof(this.hiddenField) != 'undefined') {
				this.hiddenField.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);
			}
			Wfprocessdefine_Select.superclass.setValue.call(this, r.get(this.displayField));
			this.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);				
		}				
		return this;				
	},
	//设置自定义配置项的回显值
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
	//设置是否可编辑
	setEditable : function(value){
        if(value == this.editable){
            return;
        }
        this.editable = value;
        if(!value){
            this.getEl().dom.setAttribute('readOnly', true);
            this.el.on('mousedown', this.onTriggerClick,  this);
            this.el.addClass('x-combo-noedit');
        }else{
            this.el.dom.setAttribute('readOnly', false);
            this.el.un('mousedown', this.onTriggerClick,  this);
            this.el.removeClass('x-combo-noedit');
        }
    },
    //回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
    setDisplayValue : function(valueField, displayField){
    	var p_record = new Ext.data.Record();
    	p_record.set(this.valueField,valueField);
        p_record.set(this.displayField,displayField);
        this.setValue(p_record);
    },
    //清空(new)
    clearValue :  function(){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		this.setValue(p_record);
    }
});
Ext.reg('Wfprocessdefine_SelectWin', Wfprocessdefine_Select);