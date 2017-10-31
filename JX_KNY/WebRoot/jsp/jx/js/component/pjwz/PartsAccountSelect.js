/**
 * 互换配件选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsAccountSelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'PartsAccount_SelectWin',editable:false,fieldLabel : 自定义,hiddenName:自定义,returnField:[{widgetId:"自定义",propertyName:"自定义"}]}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * f.valueField配置项：本控件提交值，默认为"idx"，可自定义所需字段
 * g.displayField配置项：本控件显示值，默认为"partsName",可自定义所需字段
 * 回显字段名称列表：参见PartsAccount实体类
 * h.queryHql配置项：自定义Hql，暂只接受PartsAccount实体类对象查询，必须为带where子句的hql,如"from PartsAccount where 1=1",可选
 * I.entity配置项：默认配置为"com.yunda.jx.pjwz.partsmanage.entity.PartsAccount"，可选
 * 4.回显及设置默认值
                
 * a.在双击弹出编辑表单时，为回显互换配件选择控件值需在页面js的rowdblclick事件方法中加入以下代码  
 * 				Ext.getCmp(componentId).loadRecord(r,id_field,name_field,componentId);              
	            参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
   b.设置默认值，代码如下：
   				Ext.getCmp(componentId).loadRecord('',id_field,name_field,componentId);
   				参数说明：r:设为空，id_field：需要设置的idx值、name_field：需要设置的partsName值、componentId：该控件id
   					            
 * 5.在页面js的新增及重置方法中加入以下代码
 * 	Ext.getCmp(componentId).clear(componentId)以清除回显项;
 *  参数说明：componentId：该控件id    
 * 6.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(valueField,displayField);
 *   componentId 控件id，参数说明：valueField：该控件valueField值、displayField：该控件displayField值
 * 7.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id           
 */
getStatusMean = function(v){
	if(v == 'WAITING_REPAIR') return '待修';		
	if(v == 'IN_REPAIR') return '在修';
	if(v == 'REPAIRED') return '修竣';
	if(v == 'WAITING_SCRAP') return '待报废';
	if(v == 'SCRAPPED') return '报废';
	if(v == 'TO_ASSEMBLED') return '待上配件/车';
	if(v == 'OUTSOURCED_NOT_BACK') return '委外未回厂/段';
	if(v == 'OUTSOURCED') return '委外修';
	if(v == 'IN_WAREHOUSE') return '在库';
	if(v == 'ASSEMBLED') return '在车/配件';
}
PartsAccount_store = new Ext.data.JsonStore({
	url : ctx + "/partsSelect!pageList.action",
	autoLoad : false,
	root : "root",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : [ "partsTypeIDX","partsName","specificationModel","nameplateNo","partsNo","fixedAssetsNo","fixedAssetsName",
        "fixedAssetsCardno","assetsStaus","installPartsStatus","installTrainStatus","warehouseStatus","healthStatus",
        "turnoverStatus","ownerUnit","ownerUnitName","useUnit","useUnitName","madeFactoryName","madeFactoryIdx",
        {name:"leaveDate", type:"date", dateFormat: 'time'},
        {name:"useDate", type:"date", dateFormat: 'time'},
        {name:"registerTime", type:"date", dateFormat: 'time'},
        "price","buildupTypeIdx","buildupTypeCode","buildupTypeName","empid","empname","achieveKM","achieveYEAR","remarks","idx","partsStatus" ]
});
//分页工具
PartsAccount_pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsAccount_store});
//互换配件列表
PartsAccount_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		PartsAccount_List.superclass.constructor.call(this, {
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
						data: [ ["按配件名称"], ["按规格型号"],["按配件编号"] ]
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
							this.store.baseParams.partsname = "";
							this.store.baseParams.nameplateNo = "";
							this.store.baseParams.specificationModel = searchText;
							this.store.load();
						}else if(querytype == '按配件名称'){
							this.store.baseParams.specificationModel = "";
							this.store.baseParams.nameplateNo = "";
							this.store.baseParams.partsname = searchText;
							this.store.load();
						}else if(querytype == '按配件编号'){
							this.store.baseParams.partsname = "";
							this.store.baseParams.specificationModel = "";
							this.store.baseParams.nameplateNo = searchText;
							this.store.load();
						}						
					},
					title : "按输入框条件查询",
					scope : this
				},{
					text : "重置",
					iconCls : "resetIcon",
					handler : function(){
						this.store.baseParams.partsname = "";
						this.store.baseParams.specificationModel = "";
						this.store.baseParams.nameplateNo = "";
						this.store.load();
						this.getTopToolbar().get(1).setValue("");
						this.getTopToolbar().get(0).setValue("按配件名称");
					},
					scope : this
				},{
					text : "清空",
					iconCls : "resetIcon",
					handler : function(){
						this.parentObj.parentObj.clearValue();
						var returnField = this.parentObj.parentObj.returnField;
				        if(returnField != null && returnField.length > 0){        	
				        	for(var i = 0;i < returnField.length;i++){															
								if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null){
									Ext.getCmp(returnField[i].widgetId).setValue("");
								}
								//针对html标签
								else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null ){
									Ext.get(returnField[i].widgetId).dom.value = "";
								}	        		
				        	}        	
				        }
					},
					scope : this
				}],
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{ sortable:true, header:"配件名称", dataIndex:"partsName", width: 100 },			
		        { sortable:true, header:"规格型号", dataIndex:"specificationModel", width: 100 },			
		        { sortable:true, header:"配件编号", dataIndex:"nameplateNo", width: 150 },		
		        { sortable:true, header:"配件状态", dataIndex:"partsStatus", width: 150,  
		          renderer : function(v){return getStatusMean(v);} }
		        ]),
			store : PartsAccount_store,
			bbar: PartsAccount_pagingToolbar,
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
	grid : new PartsAccount_List(),
	parentObj:null,	
	entity:'com.yunda.jx.pjwz.partsmanage.entity.PartsAccount',
	queryHql:'',
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
		this.grid.getStore().baseParams.entity = this.entity;
		this.grid.getStore().baseParams.queryHql = this.queryHql;
		this.grid.getStore().load();
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
			title : "配件选择",
			width : 550,
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
PartsAccount_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'idx',
	displayField : 'partsName',
	hiddenName : 'PartsAccount_SelectId',
	entity:'com.yunda.jx.pjwz.partsmanage.entity.PartsAccount',
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
		PartsAccount_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PartsAccount_Select.superclass.onRender.call(this, ct, position);
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
		PartsAccount_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PartsAccount_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PartsAccount_Select.superclass.getName.call(this);
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
			PartsAccount_Select.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('PartsAccount_SelectWin', PartsAccount_Select);