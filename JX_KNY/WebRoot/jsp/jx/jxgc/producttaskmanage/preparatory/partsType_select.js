

PartsType_store = new Ext.data.JsonStore({
	url : ctx + "/partsType!findpageList.action",
	autoLoad : false,
	root : "root",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : [ "idx","partsName", "specificationModel","isHasSeq"]
});
//分页工具
PartsType_pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsType_store});
//互换配件列表
PartsType_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		PartsType_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : false
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,			
			id:"PartsType_List",
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{ sortable:true, header:"配件名称", dataIndex:"partsName", width: 150 },
		        { sortable:true, header:"配件型号", dataIndex:"specificationModel", width: 150 }	,
		        { sortable:true, header:"是否带序列号", dataIndex:"isHasSeq", width: 100, renderer: function(v){if(v != '1'){return '否'}else{return '是'}} }
		        ]),
			store : PartsType_store,
			bbar: PartsType_pagingToolbar,
			listeners:{
				//双击选择配件
				"rowdblclick": {
	            	fn: function(grid, idx, e){
	            		var r = grid.store.getAt(idx);
	            		this.parentObj.parentObj.setValue(r);
	            		this.parentObj.parentObj.setReturnValue(r);
				        this.parentObj.parentObj.fireEvent('select', r);
	            		var v = this.parentObj.parentObj.getValue();
	            		if(String(v) !== String(this.parentObj.parentObj.startValue)){
				            this.parentObj.parentObj.fireEvent('change', this.parentObj.parentObj, v, this.parentObj.parentObj.startValue);
				        }
	            		this.parentObj.close();
	            	}
				}
			}
		});
	}
});

/** ****************************************************************************************** */
//弹出窗口
Parts_SelectWin = Ext.extend(Ext.Window, {
	grid : new PartsType_List(),
	parentObj:null,	
	entity:'com.yunda.jx.pjwz.partsBase.entity.PartsType',
	queryHql:'',
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
		Parts_SelectWin.superclass.constructor.call(this, {
			title : "配件组成选择",
			width : 550,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			items : [ new Ext.Panel({
							region : "north",
							height : 30,
							border : false,
							items : 
							{								
							// 工具栏
							tbar : [{
									xtype:"combo",
									hiddenName:"queryType",
									store: new Ext.data.SimpleStore({
										fields: ["type"],
    									data: [ ["按配件名称"], ["按配件型号"] ]
									}),
									displayField:"type",
									width: 110,
									valueField:"type",
									value:"按配件名称",
									id:"queryTypeId",
									mode:"local",
									editable: false,
									triggerAction: "all"
								},{	            
					                xtype:"textfield",								                
					                name : "parts",
							        width: 240,
					                id:"partsId"
								},{
									text : "搜索",
									iconCls : "searchIcon",
									handler : function(){
										var searchText = Ext.getCmp("partsId").getValue();
										var querytype = Ext.getCmp("queryTypeId").getValue();
										if(querytype == '按配件型号'){
											var sp = {};
											sp.specificationModel = searchText;
											this.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
											this.grid.getStore().load();
										}else if(querytype == '按配件名称'){
											var sp = {};
											sp.partsName = searchText;
											this.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
											this.grid.getStore().load();
										}
									},
									title : "按输入框条件查询",
									scope : this
								},{
									text : "重置",
									iconCls : "resetIcon",
									handler : function(){
										var sp = {};
										this.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(sp);
										this.grid.getStore().load();
										
										Ext.getCmp("partsId").setValue("");
										Ext.getCmp("queryTypeId").setValue("按配件名称");
									},
									scope : this
								}]
						}
						}), new Ext.Panel({
							region : "center",
							layout : "fit",
							items : [this.grid]
						})]

				});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
PartsType_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'idx',
	displayField : 'PartsTypeName',
	hiddenName : 'PartsType_SelectId',
	entity:'com.yunda.jx.pjwz.partsBase.entity.PartsType',
	queryHql:'',
	editable :true,			
	win : new Parts_SelectWin(),
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
		PartsType_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PartsType_Select.superclass.onRender.call(this, ct, position);
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
		PartsType_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PartsType_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PartsType_Select.superclass.getName.call(this);
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
			PartsType_Select.superclass.setValue.call(this, r.get(this.displayField));
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
    //根据record回显值,参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
    loadRecord : function(r,id_field,name_field,componentId){
    	       
        var p_record = new Ext.data.Record();		
        //设置默认值
        if(r == null || r == '' || typeof(r) == 'undefined' || r == 'null'){
        	p_record.set(this.valueField,id_field);
        	p_record.set(this.displayField,name_field);
        }
        //设置回显值		
        else if(r != null && r != '' && r != 'null' && typeof(r) != 'undefined'){
        	if(r.get(id_field) != null){        		
        		p_record.set(this.valueField,r.get(id_field));
        		p_record.set(this.displayField,r.get(name_field));
        	}
        }             
        Ext.getCmp(componentId).setValue(p_record);
    },
    //清空，componentId组件Id
    clear : function(componentId){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		Ext.getCmp(componentId).setValue(p_record);
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
Ext.reg('PartsType_SelectWin', PartsType_Select);