/**
 * 人员选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入jspf文件:<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
 *   在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/PositionWidget.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'OmPosition_SelectWin',editable:false,fieldLabel : "选择人员",hiddenName:'emp',returnField:[{widgetId:"empCode",propertyName:"empcode"}}
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

Ext.namespace("PositionWidget");
PositionWidget.searchParams = {};

// 人员列表数据容器
PositionWidget.posistore = new Ext.data.JsonStore({
	url : ctx + "/position!findPosiListToWidget.action",
	autoLoad : false,
	root : "root",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : ["positionid", "posiname", "positype", "isleaf"]
});


//分页工具
PositionWidget.pagingToolbar = Ext.yunda.createPagingToolbar({store: PositionWidget.posistore});
//人员列表
PositionWidget.PosiList = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		PositionWidget.PosiList.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			loadMask: {msg: i18n.PositionWidget.wait},
			// 偶数行变色
			stripeRows : true,			
			id:"PositionWidget_posiList",
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), {
					sortable : true,
					header : i18n.PositionWidget.positionName,
					title :i18n.PositionWidget.choicePosition,
					dataIndex : "posiname"
				}, {
					sortable : true,
					header : i18n.PositionWidget.positionType,
					title : i18n.PositionWidget.choicePosition,
					dataIndex : "positype"
				}, {
					sortable : true,
					header : i18n.PositionWidget.YN,
					title : i18n.PositionWidget.choicePosition,
					dataIndex : "isleaf"
				}
			]),
			store : PositionWidget.posistore,
			bbar: PositionWidget.pagingToolbar,
			listeners:{
				//双击选择岗位
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
PositionWidget.PosiSelectWin = Ext.extend(Ext.Window, {
	grid : new PositionWidget.PosiList(),
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
        Ext.getCmp("PositionWidget_posiList").getStore().on('beforeload',function(){
			this.baseParams.start = 0;
			this.baseParams.limit = PositionWidget.pagingToolbar.pageSize;
			this.baseParams.posiname = Ext.getCmp("PositionWidget_empId").getValue();
			if(typeof(EmployeeWidget.chsEmpid)!='undefined'&&EmployeeWidget.chsEmpid!=null&&EmployeeWidget.chsEmpid!='')
			this.baseParams.empid = EmployeeWidget.chsEmpid;
		});
//		
    },
	constructor : function() {		
		PositionWidget.PosiSelectWin.superclass.constructor.call(this, {
			title : i18n.PositionWidget.choiceEmp,
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
									text:i18n.PositionWidget.positionName+"："
								},{	            
					                xtype:"textfield",								                
					                name : "posiName",
							        width: 200,							        
					                id:"PositionWidget_empId"
								},{
									text : i18n.PositionWidget.search,
									iconCls : "searchIcon",
									handler : function(){
										var posiname = Ext.getCmp("PositionWidget_empId").getValue();
										this.grid.getStore().load({
											params:{
												posiname:posiname
											}																
										})
									},
									scope : this
								},{
									text : i18n.PositionWidget.reset,
									iconCls : "resetIcon",
									handler : function(){
										this.grid.getStore().load({
											params:{
												posiname:""
											}																
										});
										Ext.getCmp("PositionWidget_empId").setValue("");
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
PositionWidget.PosiSelect = Ext.extend(Ext.form.TriggerField, {
	valueField : 'positionid',
	displayField : 'posiname',
	hiddenName : 'posiselect_id',
	editable :true,			
	win : new PositionWidget.PosiSelectWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;			
			this.win.show(this.el);
			Ext.getCmp("PositionWidget_posiList").getStore().load();
		}
	},
	initComponent : function() {
		PositionWidget.PosiSelect.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {		
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PositionWidget.PosiSelect.superclass.onRender.call(this, ct, position);        
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
		PositionWidget.PosiSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PositionWidget.PosiSelect.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, _r) {
		this.setValue(_r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PositionWidget.PosiSelect.superclass.getName.call(this);
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
			PositionWidget.PosiSelect.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('OmPosition_SelectWin', PositionWidget.PosiSelect);