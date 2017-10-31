
Ext.onReady(function() {

	Ext.namespace('MatTypeListSelect');
	
	/** ************* 定义全局变量开始 ************* */
	MatTypeListSelect.searchParam = {};
	MatTypeListSelect.orgId = "";
	/** ************* 定义全局变量结束 ************* */
	
	// 自定义选择物料信息后的set方法
	MatTypeListSelect.returnFn = function (record) {
		MyExt.Msg.alert(record.get('matCode'));
	}
	
	MatTypeListSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!findPageListForExpend.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTypeList!saveOrUpdate.action',             				//保存数据的请求URL
	    deleteURL: ctx + '/matTypeList!logicDelete.action',            				//删除数据的请求URL
	    singleSelect: true,
	    tbar:null,
		fields: [{
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200
		},{
			header:'计量单位', dataIndex:'unit', width: 60			
		},{
			header:'计划单价', dataIndex:'price', width: 60	
		}],
		storeId: 'matCode',
		storeAutoLoad: false,
		listeners: {
			"rowdblclick": function(grid, rowIndex, e) {
				var r = grid.store.getAt(rowIndex);
				// 自定义选择物料信息后的set方法
				MatTypeListSelect.returnFn(r);
				// 隐藏组件选择窗口
        		MatTypeListSelect.win.hide();
			}
		}
	});
	MatTypeListSelect.grid.un('rowdblclick', MatTypeListSelect.grid.toEditFn, MatTypeListSelect.grid);
	//查询前添加过滤条件
	MatTypeListSelect.grid.store.on('beforeload' , function(){
		var searchParam = MatTypeListSelect.searchParam;
		this.baseParams.orgId = MatTypeListSelect.orgId;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	MatTypeListSelect.win = new Ext.Window({
		title:"物料选择",
		width:605, height:320,
		layout:"border",
		closeAction:"hide",
		plain:true,
		items:[
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: MatTypeListSelect.grid
				
			},
			{
				region:"north", baseCls:"x-plain", style:"padding:10px;",
				height:43,
				layout:"fit",
				items:{
					xtype:"form",
					id: "searchForm_k",
					layout:"column",
					baseCls:"x-plain", 
					labelWidth: 60, 
					items:[
						{
							columnWidth:0.355,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:120, fieldLabel:"物料编码"
								}
							]
						},
						{
							columnWidth:0.355,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:120, fieldLabel:"物料描述"
								}
							]
						},
						{
							columnWidth:0.05, baseCls:"x-plain"
						},
						{
							columnWidth:0.12,
							layout:"form", baseCls:"x-plain",
							autoWidth:false,
							bodyStyle:"",
							items:[{
								xtype:"button", width:40,
								text:"查询", iconCls:"searchIcon", handler: function() {
									var form = Ext.getCmp('searchForm_k').getForm();
									var searchParams = form.getValues();
									MatTypeListSelect.searchParam = MyJson.deleteBlankProp(searchParams);
									// 重新加载 【物料选择】 窗口表格数据
									MatTypeListSelect.grid.store.load();
								}
							}]
						},
						{
							columnWidth:0.12, baseCls:"x-plain",
							layout:"form",
							items:[
								{
									xtype:"button", width:40,
									text:"重置", iconCls:"resetIcon", handler: function() {
										Ext.getCmp('searchForm').getForm().reset();
										// 重新加载 【物料选择】 窗口表格数据
										MatTypeListSelect.searchParam = {};
										MatTypeListSelect.grid.store.load();
									}
								}
							]
						}
					]
				}
			}
		]
	});
	
	MatTypeListSelect.MatTypeList_Select = Ext.extend(Ext.form.TriggerField, {
		valueField : 'matCode',
		displayField : 'matCode',
		hiddenName : 'matCode',
		editable :true,			
		win : MatTypeListSelect.win,
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
			MatTypeListSelect.MatTypeList_Select.superclass.initComponent.call(this);
		},
		submitValue : undefined,
		
		onRender : function(ct, position) {				
			if (this.hiddenName && typeof this.submitValue=='undefined') {
				this.submitValue = false;
			}
			MatTypeListSelect.MatTypeList_Select.superclass.onRender.call(this, ct, position);
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
			MatTypeListSelect.MatTypeList_Select.superclass.initValue.call(this);
			if (this.hiddenField) {
				this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
								? this.hiddenValue
								: this.value, '');
			}
		},
		constructor : function(options) {
			Ext.apply(this, options);
			MatTypeListSelect.MatTypeList_Select.superclass.constructor.call(this);
			this.win.on("submit", this.setOnSubmit, this);
		},
		
		setOnSubmit : function(_win, r) {
			this.setValue(r);
		},
		
		getName : function() {
			var hf = this.hiddenField;
			return hf && hf.name ? hf.name : this.hiddenName
					|| MatTypeListSelect.MatTypeList_Select.superclass.getName.call(this);
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
				MatTypeListSelect.MatTypeList_Select.superclass.setValue.call(this, r.get(this.displayField));
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
	})

	Ext.reg('MatTypeList_SelectWin', MatTypeListSelect.MatTypeList_Select);
});