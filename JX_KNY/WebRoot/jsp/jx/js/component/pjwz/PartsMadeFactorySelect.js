/**
 * 配件生产厂家选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/GyjcFactorySelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'PartsMadeFactory_SelectWin',editable:false,fieldLabel : 自定义,hiddenName:自定义,returnField:[{widgetId:"自定义",propertyName:"自定义"}}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：参见GyjcFactory实体类
 * f.queryHql配置项：自定义Hql，暂只接受GyjcFactory生产厂家实体类查询，配置queryHql优先，必须为带where子句的sql,如"from GyjcFactory where 1=1",选填
 * 4.回显及设置默认值
                
 * a.在双击弹出编辑表单时，为回显生产厂家选择控件值需在页面js的rowdblclick事件方法中加入以下代码  
 * 				Ext.getCmp(componentId).loadRecord(r,id_field,name_field,componentId);              
	            参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
   b.设置默认值，代码如下：
   				Ext.getCmp(componentId).loadRecord('',id_field,name_field,componentId);
   				参数说明：r:设为空，id_field：需要设置的idx值、name_field：需要设置的partsName值、componentId：该控件id
   					            
 * 5.在页面js的新增及重置方法中加入以下代码
 * 	Ext.getCmp(componentId).clear(componentId)以清除回显项;
 *  参数说明：componentId：该控件id  
 * 6.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(id,madeFactoryName);
 *   componentId 控件id，参数说明：id：该控件fID值、madeFactoryName：该控件fName值
 * 7.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id             
 */

Factory_store = new Ext.data.JsonStore({
	url : ctx + "/partsMadeFactory!factoryList.action",
	autoLoad : true,
	root : "root",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : [ "id","madeFactoryName","madeFactoryShortname"]
});
//分页工具
Factory_pagingToolbar = Ext.yunda.createPagingToolbar({store: Factory_store});
//人员列表
Factory_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		Factory_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,
			tbar : [{	            
                xtype:"textfield",								                
		        width: 240,
                emptyText:"--请输入生产厂家名称模糊查询--"
			},{
				text : "搜索",
				iconCls : "searchIcon",
				handler : function(){
					var factoryname = this.getTopToolbar().get(0).getValue();
					if(factoryname==""){
						this.store.baseParams.factoryname = "";
						this.store.baseParams.query = "";
						this.store.load();
						this.getTopToolbar().get(0).setValue("");
					}else{
						this.store.baseParams.factoryname = factoryname;
						this.store.load();
					}
				},
				scope : this
			},{
				text : "重置",
				iconCls : "resetIcon",
				handler : function(){
					this.store.baseParams.factoryname = "";
					this.store.baseParams.query = "";
					this.store.load();
					this.getTopToolbar().get(0).setValue("");
				},
				scope : this
			}],
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{ sortable:true, header:"生产厂家编号", dataIndex:"id" },			
		        { sortable:true, header:"生产厂家名称", dataIndex:"madeFactoryName" }]),
			store : Factory_store,
			bbar: Factory_pagingToolbar,
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
Factory_SelectWin = Ext.extend(Ext.Window, {
	grid : new Factory_List(),
	parentObj:null,	
	queryHql:'',
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
    	this.grid.getStore().load({
			params:{				
				queryHql:this.queryHql
			}			
		});		
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
		Factory_SelectWin.superclass.constructor.call(this, {
			title : "生产厂家选择",
			width : 375,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "fit",
			items : [this.grid ]
		});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
Factory_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'id',
	displayField : 'madeFactoryName',
	hiddenName : 'factory_SelectId',	
	queryHql:'',
	editable :true,			
	win : new Factory_SelectWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	//点击控件触发事件
	onTriggerClick : function() {
		if(this.disabled != true){
			this.win.parentObj = this;	
			this.win.queryHql = this.queryHql;
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		Factory_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		Factory_Select.superclass.onRender.call(this, ct, position);
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
		Factory_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		Factory_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
//		this.setReturnValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| Factory_Select.superclass.getName.call(this);
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
				this.hiddenField.value = typeof(r.get("id")) == 'undefined' || r.get("id")=='undefined'?'':r.get("id");
			}
			Factory_Select.superclass.setValue.call(this, r.get("madeFactoryName"));
			this.value = typeof(r.get("id")) == 'undefined' || r.get("id")=='undefined'?'':r.get("id");			
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
    //回显值（new）,参数说明：id：该控件fID值、madeFactoryName：该控件fName值
    setDisplayValue : function(id, madeFactoryName){
    	var node = {id: id, madeFactoryName: madeFactoryName}; 
    	var p_record = new Ext.data.Record(node);
        this.setValue(p_record);
    },
    //清空(new)
    clearValue :  function(){
    	var node = {id: "", madeFactoryName: ""};
    	var p_record = new Ext.data.Record(node);
		this.setValue(p_record);
    }
});
Ext.reg('PartsMadeFactory_SelectWin', Factory_Select);