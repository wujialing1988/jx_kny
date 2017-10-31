/**
 * 人员选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入jspf文件:<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
 *   在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/partsCheckItemSelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'OmEmployee_SelectWin',editable:false,fieldLabel : "选择人员",hiddenName:'emp',returnField:[{widgetId:"empCode",propertyName:"empcode"}}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：{empid:人员id,empcode：人员编码,empname：人员姓名,gender:人员性别}
 * 
 * 4.在双击弹出编辑表单时，为回显人员选择控件值需在页面js的rowdblclick事件方法中加入以下代码
                
 * a.在双击弹出编辑表单时，为回显专业类型选择控件值需在页面js的rowdblclick事件方法中加入以下代码  
 * 				Ext.getCmp(componentId).loadRecord(r,id_field,name_field,componentId);              
	            参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
   b.设置默认值，代码如下：
   				Ext.getCmp(componentId).loadRecord('',id_field,name_field,componentId);
   				参数说明：r:设为空，id_field：需要设置的empid值、name_field：需要设置的empname值、componentId：该控件id
   					            
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

Ext.namespace("partsCheckItemSelect");
// 可视化检测项编码数据容器
partsCheckItemSelect.checkItemStore = new Ext.data.JsonStore({
	url : ctx + "/partsCheckItemDefult!pageList.action",
	autoLoad : true,
	root : "root",
	storeId:"checkItemID",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : ["checkID", "name", "desc","unit"]
});

//分页工具
partsCheckItemSelect.pagingToolbar = Ext.yunda.createPagingToolbar({store: partsCheckItemSelect.checkItemStore});
//可视化检测项编码列表
partsCheckItemSelect.CheckItemList = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		partsCheckItemSelect.CheckItemList.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,	
			// 工具栏
			tbar : [{
					xtype:"label",
					text:"可视化检测项名称："
				},{	            
	                xtype:"textfield",								                
	                name : "name",
			        width: 140,
			        enableKeyEvents: true,
			        listeners: {
			        	keyup: function( me, e ) {
			        		var name = me.getValue();
							var store = partsCheckItemSelect.checkItemStore;
							var searchParam = {};
							searchParam.name = name;
							store.load({
								params: { entityJson: Ext.util.JSON.encode(searchParam)}       
							});
			        	}
			        }
				},'->',{
					text : "搜索",
					iconCls : "searchIcon",
					handler : function(){
						var name = this.getTopToolbar().get(1).getValue();
						var store = partsCheckItemSelect.checkItemStore;
						var searchParam = {};
						searchParam.name = name;
						store.load({
							params: { entityJson: Ext.util.JSON.encode(searchParam)}       
						});
						
					},
					scope : this
				},{
					text : "重置",
					iconCls : "resetIcon",
					handler : function(){
//						this.store.baseParams.searchParam.name = "";
						this.store.load();
						this.getTopToolbar().get(1).setValue("");
					},
					scope : this
				},{
					text : "同步刷新数据",
					iconCls : "resetIcon",
					handler : function(){
						Ext.Ajax.request({
							url: ctx + "/partsCheckItemDefult!synPartsCheckItemDefult.action",
							success: function(r){
								var retn = Ext.util.JSON.decode(r.responseText);
								if(retn.success){
									partsCheckItemSelect.checkItemStore.load();
								}else{
									alertFail(retn.errMsg);
								}
							},
							failure: function(){
								alertFail("请求超时！");
							}
						});
					},
					scope : this
				}],
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), {
					sortable : true,
					header : "检测项编号",
					title : "双击该行记录选择人员",
					dataIndex : "checkID",
					width:50
				}, {
					sortable : true,
					header : "检测值",
					title : "双击该行记录选择人员",
					dataIndex : "name",
					width:60
				}, {
					sortable : true,
					header : "检测项描述",
					dataIndex : "desc",
					title : "双击该行记录选择人员",
					width:100
				}, {
					sortable : true,
					header : "检测项单位",
					dataIndex : "unit",
					title : "双击该行记录选择人员",
					width:100
				}
			]),
			store : partsCheckItemSelect.checkItemStore,
			bbar: partsCheckItemSelect.pagingToolbar,
			listeners:{
				//双击选择人员
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
partsCheckItemSelect.CheckItemWin = Ext.extend(Ext.Window, {
	grid : new partsCheckItemSelect.CheckItemList(),
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
		partsCheckItemSelect.CheckItemWin.superclass.constructor.call(this, {
			title : "选择检测编码",
			width : 535,
			height : 325,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			defaults: { layout: "fit", border: false },
			items : [new Ext.Panel({
							region : "center",
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
partsCheckItemSelect.CheckItemSelect = Ext.extend(Ext.form.TriggerField, {
	valueField : 'checkID',
	displayField : 'checkID',
	hiddenName : 'checkID_id',
	editable :true,		
	win : new partsCheckItemSelect.CheckItemWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;			
			this.win.show(this.el);
		}
	},
	initComponent : function() {
		partsCheckItemSelect.CheckItemSelect.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		partsCheckItemSelect.CheckItemSelect.superclass.onRender.call(this, ct, position);        
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
		partsCheckItemSelect.CheckItemSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		partsCheckItemSelect.CheckItemSelect.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, _r) {
		this.setValue(_r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| partsCheckItemSelect.CheckItemSelect.superclass.getName.call(this);
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
			partsCheckItemSelect.CheckItemSelect.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('PartsCheckItem_SelectWin', partsCheckItemSelect.CheckItemSelect);