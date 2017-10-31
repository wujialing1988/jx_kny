
PartsTypeByBuild_store = new Ext.data.JsonStore({
	url : ctx + "/partsTypeByBuildSelect!buildUpTypeList.action",
	autoLoad : false,
	root : "root",
	remoteSort : false,
	totalProperty : "totalProperty",
	fields : [ "partsName","specificationModel","professionalTypeIdx","professionalTypeName",
        "partsClassIdx","className","matCode","specificationModelCode","recordStatus",
        "buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","buildUpTypeDesc","chartNo","idx", "isVirtual" ]
});
//分页工具
PartsTypeByBuild_pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsTypeByBuild_store});
//互换配件列表
PartsTypeByBuild_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		PartsTypeByBuild_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : false
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,	
			// 工具栏
			tbar : [{
					xtype:"combo",
					hiddenName:"queryType",
					store: new Ext.data.SimpleStore({
						fields: ["type"],
						data: [ ["按配件名称"], ["按规格型号"],["按图号"], ["按专业类型"] ]
					}),
					displayField:"type",
					width: 110,
					valueField:"type",
					value:"按配件名称",
					mode:"local",
					editable: false,
					triggerAction: "all"
				},{	            
	                xtype:"textfield",								                
	                name : "parts",
			        width: 240
				},{
					text : "搜索",
					iconCls : "searchIcon",
					handler : function(){
						var searchText = this.getTopToolbar().get(1).getValue();
						var querytype = this.getTopToolbar().get(0).getValue();
						if(querytype == '按规格型号'){
							var searchParam = {};
							searchParam.buildUpTypeName = searchText;
							this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);							
							this.store.load();
						}else if(querytype == '按配件名称'){
							var searchParam = {};
							searchParam.buildUpTypeDesc = searchText;
							this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							this.store.load();
						}else if(querytype == '按图号'){
							var searchParam = {};
							searchParam.chartNo = searchText;
							this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							this.store.load();
						}else if(querytype == '按专业类型'){
							var searchParam = {};
							searchParam.professionalTypeName = searchText;
							this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							this.store.load();
						}
						
					},
					title : "按输入框条件查询",
					scope : this
				},{
					text : "重置",
					iconCls : "resetIcon",
					handler : function(){
						var searchParam = {};						
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
						this.store.load();
						this.getTopToolbar().get(1).setValue("");
						this.getTopToolbar().get(0).setValue("按配件名称");
					},
					scope : this
				}],
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{ sortable: true, header:"配件名称", dataIndex:"buildUpTypeDesc", width: 100 },			
		        { sortable: true, header:"规格型号", dataIndex:"buildUpTypeName", width: 130 },			
		        { sortable: true, header:"图号", dataIndex:"chartNo", width: 100 },			
		        { sortable: true, header:"专业类型", dataIndex:"professionalTypeName", width: 100 },
		        { sortable: true, header:"是否虚拟配件", dataIndex:"isVirtual", width: 80 }]),
			store : PartsTypeByBuild_store,
			bbar: PartsTypeByBuild_pagingToolbar,
			listeners:{
				//双击选择配件
				"rowdblclick": {
	            	fn: function(grid, idx, e){
	            		var r = grid.store.getAt(idx);
	            		this.parentObj.parentObj.setValue(r);
	            		this.parentObj.parentObj.setReturnValue(r);
	            		this.parentObj.parentObj.fireEvent('select', r);
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
	grid : new PartsTypeByBuild_List(),
	parentObj:null,
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
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
			title : "规格型号选择",
			width : 550,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "fit",
			items : [ this.grid]
		});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
PartsTypeByBuild_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'idx',
	displayField : 'partsName',
	hiddenName : 'PartsTypeByBuild_SelectId',
	queryHql:'',
	editable :true,			
	win : new Parts_SelectWin(),
	triggerClass:'x-form-search-trigger',			
	returnField:[],
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;
			this.win.queryHql = this.queryHql;
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		PartsTypeByBuild_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PartsTypeByBuild_Select.superclass.onRender.call(this, ct, position);
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
		PartsTypeByBuild_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PartsTypeByBuild_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PartsTypeByBuild_Select.superclass.getName.call(this);
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
			PartsTypeByBuild_Select.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('PartsTypeByBuild_SelectWin', PartsTypeByBuild_Select);