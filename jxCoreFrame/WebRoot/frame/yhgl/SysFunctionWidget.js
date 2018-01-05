Ext.namespace("FunctionWidget");
FunctionWidget.searchParams = {};

// 人员列表数据容器
FunctionWidget.funcstore = new Ext.data.JsonStore({
	url : ctx + "/sysFunction!pageList.action",
	autoLoad : false,
	root : "root",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : ["funccode", "funcname", "funcaction", "funcgroupid","funcgroupname","appid","appname"]
});


//分页工具
FunctionWidget.pagingToolbar = Ext.yunda.createPagingToolbar({store: FunctionWidget.funcstore});
//功能列表
FunctionWidget.PosiList = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		FunctionWidget.PosiList.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
//			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,			
			id:"FunctionWidgetList",
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), {
					sortable : true,
					header : i18n.SysFuncionWidget.functionName,
					title : "双击该行记录选择功能",
					dataIndex : "funcname"
				}, {
					sortable : true,
					header : i18n.SysFuncionWidget.functionInterf,
					title : i18n.SysFuncionWidget.D_clickToSel,
					dataIndex : "funcaction"
				}, {
					sortable : true,
					header : i18n.SysFuncionWidget.functionGroup,
					title :i18n.SysFuncionWidget.D_clickToSel,
					dataIndex : "funcgroupname"
				}, {
					sortable : true,
					header : i18n.SysFuncionWidget.TheApplication,
					title : i18n.SysFuncionWidget.D_clickToSel,
					dataIndex : "appname"
				}]),
			store : FunctionWidget.funcstore,
			bbar: FunctionWidget.pagingToolbar,
			listeners:{
				//双击选择功能
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
FunctionWidget.FuncSelectWin = Ext.extend(Ext.Window, {
	grid : new FunctionWidget.PosiList(),
	parentObj:null,
	modal:true,
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
        Ext.getCmp("FunctionWidgetList").getStore().on('beforeload',function(){
			this.baseParams.start = 0;
			this.baseParams.limit = FunctionWidget.pagingToolbar.pageSize;
			this.baseParams.funcname = Ext.getCmp("_funcname_1").getValue();
//			this.baseParams.posiname = Ext.getCmp("FunctionWidget_funcname").getValue();
//			if(typeof(EmployeeWidget.chsEmpid)!='undefined'&&EmployeeWidget.chsEmpid!=null&&EmployeeWidget.chsEmpid!='')
			//this.baseParams.empid = EmployeeWidget.chsEmpid;
		});
    },
	constructor : function() {		
		FunctionWidget.FuncSelectWin.superclass.constructor.call(this, {
			title :i18n.SysFuncionWidget.choiceFunction,
			width : 520,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			items : [
					new Ext.Panel({
							region : "north",
							height : 30,
							border : false,
							items : 
							{								
							// 工具栏
							tbar : [{
									xtype:"label",
									text:i18n.SysFuncionWidget.functionName+"："
								},{	            
					                xtype:"textfield",								                
					                name : "funcname",
							        width: 200,							        
					                id:"_funcname_1"
								},{
									text :i18n.SysFuncionWidget.search,
									iconCls : "searchIcon",
									handler : function(){
										var funcname = Ext.getCmp("_funcname_1").getValue();
										this.grid.getStore().load({
											params:{
												funcname:funcname
											}																
										})
									},
									scope : this
								},{
									text : i18n.SysFuncionWidget.reset,
									iconCls : "resetIcon",
									handler : function(){
										this.grid.getStore().load({
											params:{
												funcname:""
											}																
										});
										Ext.getCmp("_funcname_1").setValue("");
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
FunctionWidget.FuncSelect = Ext.extend(Ext.form.TriggerField, {
	valueField : 'funccode',
	displayField : 'funccode',
	hiddenName : 'funccode',
	editable :true,			
	win : new FunctionWidget.FuncSelectWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;			
			this.win.show(this.el);
			//Ext.getCmp("FunctionWidgetList").getStore().load();
			Ext.getCmp("FunctionWidgetList").getStore().load();
		}
	},
	initComponent : function() {
		FunctionWidget.FuncSelect.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {		
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		FunctionWidget.FuncSelect.superclass.onRender.call(this, ct, position);        
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
		FunctionWidget.FuncSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		FunctionWidget.FuncSelect.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, _r) {
		this.setValue(_r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| FunctionWidget.FuncSelect.superclass.getName.call(this);
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
			FunctionWidget.FuncSelect.superclass.setValue.call(this, r.get(this.displayField));
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
						&& (fieldName != '' || fieldName == 0) && fieldName != null && fieldName != 'null'){
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
    //根据record回显值（old）,参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
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
    //回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
    setDisplayValue : function(valueField, displayField){
    	var p_record = new Ext.data.Record();
    	p_record.set(this.valueField,valueField);
        p_record.set(this.displayField,displayField);
        this.setValue(p_record);
    },
    //清空(old)，componentId组件Id
    clear : function(componentId){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		Ext.getCmp(componentId).setValue(p_record);
    },
    //清空(new)
    clearValue :  function(){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		this.setValue(p_record);
    }
});
Ext.reg('SysFunction_SelectWin', FunctionWidget.FuncSelect);