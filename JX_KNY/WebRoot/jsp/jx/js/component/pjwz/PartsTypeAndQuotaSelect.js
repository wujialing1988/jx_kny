/**
 * 互换配件规格型号选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsTypeAndQuotaSelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'PartsTypeAndQuota_SelectWin',editable:false,fieldLabel : 自定义,hiddenName:自定义,returnField:[{widgetId:"自定义",propertyName:"自定义"}]}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * f.valueField配置项：本控件提交值，默认为"idx"，可自定义所需字段
 * g.displayField配置项：本控件显示值，默认为"partsName",可自定义所需字段
 * 回显字段名称列表：参见PartsType实体类
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

//全局互换配件规格型号数据源
PartsTypeAndQuota_PartsTypestore = new Ext.data.JsonStore({
    id:"idx", root:"root", totalProperty:"totalProperty",remoteSort: true,autoLoad:true,
    url: ctx + "/partsType!findpageList.action?statue=1",
    fields: [ "specificationModel","partsName","unit","isHasSeq","timeLimit","limitKm","limitYears","status","recordStatus","siteID","creator","createTime","updator","updateTime","idx","partsClassIdx","className","professionalTypeIdx","professionalTypeName" ]
});
//全局互换配件规格型号列表
PartsTypeAndQuota_Type_List = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		PartsTypeAndQuota_Type_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,	
			colModel : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(), 
				{ sortable:true, header:"规格型号", dataIndex:"specificationModel" },	
				{ sortable:true, header:"配件名称", dataIndex:"partsName" },			
		        { sortable:true, header:"计量单位", dataIndex:"unit" },
		        { sortable:true, header:"带序列号", dataIndex:"isHasSeq",renderer:function(v){ if(v==1){return '是'}else{ return "否"}} }]),
			store : PartsTypeAndQuota_PartsTypestore,
			bbar: Ext.yunda.createPagingToolbar({store: PartsTypeAndQuota_PartsTypestore}),
			tbar: [{
						text : "规格型号：",
						xtype: "label"
					},
					{		                           
		                xtype:"textfield",width:120
		                
					},{
						text : " 配件名称：",
						xtype: "label"
					},
					{		                           
		                xtype:"textfield",width:120
		                
					},{
						text : "搜索",
						iconCls : "searchIcon",
						handler : function(){
							var specificationModel = this.getTopToolbar().get(1).getValue();
							var partsName = this.getTopToolbar().get(3).getValue();;
							var searchParam = {};								            
				            searchParam.specificationModel = specificationModel;								            
				            searchParam.partsName = partsName;
				            this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
							this.store.load();																						
						},
						title : "按输入框条件查询",
						scope: this
					},{
						text : "重置",
						iconCls : "resetIcon",
						handler : function(){
							this.store.baseParams.entityJson = Ext.util.JSON.encode({});
							this.store.load();	
							this.getTopToolbar().get(1).setValue("");
							this.getTopToolbar().get(3).setValue("");														
						},
						scope: this
					}],
			listeners:{
				//双击选择配件
				"rowdblclick": {
	            	fn: function(grid, idx, e){
	            		var r = grid.store.getAt(idx);
	            		this.parentObj.parentObj.setValue(r);
	            		this.parentObj.parentObj.setReturnValue(r);
	            		this.parentObj.parentObj.fireEvent('change', this.parentObj.parentObj);
	            		this.parentObj.parentObj.fireEvent('select', r);
	            		this.parentObj.close();
	            	}
				}
			}
		});
	}
});
//选择控件tab面板
PartsTypeAndQuota_viewport = Ext.extend(Ext.Panel,{ 
	parentObj : null,
	typeGrid : new PartsTypeAndQuota_Type_List(),
	constructor : function() {
		PartsTypeAndQuota_viewport.superclass.constructor.call(this, {
			region : "center",
			layout : "fit",
			items : [this.typeGrid]
    	});
	}
});
	
/** ****************************************************************************************** */
//弹出窗口
PartsTypeAndQuota_SelectWin = Ext.extend(Ext.Window, {	
	viewport : new PartsTypeAndQuota_viewport(),
	parentObj:null,	
	modal:true,
	// private
    beforeShow : function(){
    	this.viewport.typeGrid.parentObj = this;
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
		PartsTypeAndQuota_SelectWin.superclass.constructor.call(this, {
			title : "配件规格型号选择",
			width : 550,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			items : [this.viewport]
		});
	},
	close : function() {
		this.hide();
	}	
});
//选择控件
PartsTypeAndQuota_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'idx',
	displayField : 'partsName',
	hiddenName : 'PartsTypeAndQuota_SelectId',
	editable :true,			
	win : new PartsTypeAndQuota_SelectWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;
			this.win.show(this.el);
		}		
	},
	/*select : function(){
	},*/
	initComponent : function() {
		PartsTypeAndQuota_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PartsTypeAndQuota_Select.superclass.onRender.call(this, ct, position);
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
		PartsTypeAndQuota_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PartsTypeAndQuota_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PartsTypeAndQuota_Select.superclass.getName.call(this);
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
			PartsTypeAndQuota_Select.superclass.setValue.call(this, r.get(this.displayField));
			this.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);			
		}				
		return this;				
	},
	//设置自定义配置项的回显值
	setReturnValue : function(r) {		
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{
			//设置自定义配置项的回显值
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
Ext.reg('PartsTypeAndQuota_SelectWin', PartsTypeAndQuota_Select);