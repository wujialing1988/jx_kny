/**
 * 组织机构弹出多选树
 * 控件使用方法：
 * 1.在页面jsp中引入jspf文件:<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
 *   在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationWin.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * {id:自定义, xtype: 'OmOrganization_Win',hiddenName: 'orgId', returnField:[{widgetId:"textId",propertyName:"text"},
						{widgetId:"statusId",propertyName:"status"}], rootId:"1",rootText:"哈尔滨铁路局"}
 * 3.配置项说明：
 * a.id：控件id，非必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,必填
 * d.注意选择根节点时只有id和text两个返回值！returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显信息字段列表：{id:机构id,text:机构名称,orgcode：机构代码,orgname:机构名称,isleaf是否是叶子节点,orgtype:机构类型}
 * e.其他配置项参照EXTJS的Trigger组件
 * f.queryHql配置项：自定义Hql，暂只接受OmOrganization实体对象查询，配置queryHql优先，必须为带where子句的hql,如'from OmOrgainization where 1=1',选填
 *   1.如queryHql包含[degree]，则可实现车间（plant）班组（tream）的配置显示，实例：[degree]plant显示车间、[degree]tream显示班组。
 *     判断是否显示全名：有|fullName或是叶子节点则显示全名.
 * g.fullNameDegree配置项：设置单位全名到哪一级，如oversea到段、plant到车间 
 * h.rootId配置项：设置树的根节点id，默认为JXConfig.xml系统文件设置的段级orgid
 * I.rootText配置项：设置树的根节点名称，默认为JXConfig.xml系统文件设置的段级orgname
 * J.forDictHql配置项：根据数据字典查询
 * K.valueField配置项：本控件提交值，默认为"orgid"，可自定义所需字段
 * L.displayField配置项：本控件显示值，默认为"orgname",可自定义所需字段
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(idx,orgname);
 *   componentId 控件id，参数说明：idx：该控件idx值、orgname：该控件orgname值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id  
 * 
 */
Ext.namespace("jx.js.component");
//组织机构树
jx.js.component.orgTree = Ext.extend(Ext.tree.TreePanel, {
	parentObj:null,
	queryHql:'',
	forDictHql:'',
	fullNameDegree:'',
	constructor : function() {
		jx.js.component.orgTree.superclass.constructor.call(this, {
			autoScroll : true,
			loader : new Ext.tree.TreeLoader({
				dataUrl : ctx + '/omOrganizationSelect!customTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				text : systemOrgid,
				id : systemOrgname,
				leaf : false,
				orgseq : ''
			}),
			listeners : {
				 beforeload: function(node, e){	        	
	            	this.getLoader().dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' + node.id + '&isChecked=true'
	            								   + '&queryHql='+this.queryHql + '&forDictHql=' + this.forDictHql 
	            								   + '&fullNameDegree=' + this.fullNameDegree;
		        },
		        render : function() {		        	
	    			this.root.reload();
				    this.getRootNode().expand();
	    		},
	        	scope:this
			},
			rootVisible : true
		});
	}
});	

/** ****************************************************************************************** */
//弹出窗口
jx.js.component.orgWin = Ext.extend(Ext.Window, {
	tree : new jx.js.component.orgTree(),
	parentObj:null,
	returnField:[],
	modal:true,	
	constructor : function() {		
		jx.js.component.orgWin.superclass.constructor.call(this, {
			title: i18n.OmOrganizationWin.institutionalChoice,
			width: 280,
			height: 305,			
			plain: true,
			closeAction: "hide",
			layout: "fit",
			items: [this.tree],
			buttonAlign: "center",
			buttons: [{text: i18n.OmOrganizationWin.confirm, iconCls: "saveIcon", scope: this, handler: function(){				
				var list = new Array();
            	list = this.tree.getChecked();
            	if(list.length == 0){
            		MyExt.Msg.alert(i18n.OmOrganizationWin.noChoiceRecording);
            		return ;
            	}
            	var ids = "";
            	var texts = "";
            	var returnField = this.returnField;//回显字段
            	var fieldArray = {};
            	for(var i=0;i<list.length;i++){
            		ids = ids +　";" + list[i].attributes[this.parentObj.valueField] ;
            		texts = texts + ";" +list[i].attributes[this.parentObj.displayField];
            		if(returnField != null && returnField.length > 0){   
            			var displaytext = "";
    					var displayId = "";
    					for(var j = 0;j < returnField.length;j++){
    						displaytext = returnField[j].propertyName;
    						displayId = returnField[j].widgetId;
    						if(Ext.isEmpty(fieldArray[displayId])) fieldArray[displayId] = "";
    						fieldArray[displayId] = fieldArray[displayId] + ";" + list[i].attributes[displaytext];        						
    					}
            		}
            	}
            	ids = ids.substring(1);
            	ids = ids.replace(/\,/g,"';'");
//                	ids = "'"+ids+"'";
            	texts = texts.substring(1);
		        this.parentObj.lastSelectionText = texts;
		        if(this.parentObj.hiddenField){
		            this.parentObj.hiddenField.value = ids;
		        }
		        Ext.form.TriggerField.superclass.setValue.call(this.parentObj, texts);
		        this.parentObj.value = ids;
		        //回显字段赋值
		        for(prop in fieldArray){
		        	if(typeof(Ext.getCmp(prop)) != 'undefined' && Ext.getCmp(prop) != null)
		        		Ext.getCmp(prop).setValue(fieldArray[prop].substring(1));
		        	else if(Ext.get(prop) != 'undefined' && Ext.get(prop) != null)
		        		Ext.get(prop).setValue(fieldArray[prop].substring(1));
		        }
		        this.hide();
			}}]		
		});
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
jx.js.component.orgSelect = Ext.extend(Ext.form.TriggerField, {
	valueField : 'orgid',//控件值
	displayField : 'orgname',//控件显示值
	hiddenName : '',//控件表单提交值
	queryHql:'',//自定义查询hql
	editable :false,//不可编辑，不能重置此配置项		
	rootId : systemOrgid,//树根节点id
    rootText : systemOrgname,//树根节点名称
    forDictHql : '',//根据数据字典查询
    fullNameDegree : '',//设置单位全名显示到哪一级，如oversea到段、plant到车间
	win : new jx.js.component.orgWin(),
	triggerClass:'x-form-search-trigger',			
	returnField:[],
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;
			this.win.tree.parentObj = this;
			this.win.returnField = this.returnField;
			this.win.tree.queryHql = this.queryHql;
			this.win.tree.forDictHql = this.forDictHql;
			this.win.tree.fullNameDegree = this.fullNameDegree;
			if(!Ext.isEmpty(this.rootId))
				this.win.tree.root.id = this.rootId;
			if(!Ext.isEmpty(this.rootText))
				this.win.tree.root.setText(this.rootText);
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		jx.js.component.orgSelect.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		jx.js.component.orgSelect.superclass.onRender.call(this, ct, position);
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
		jx.js.component.orgSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		jx.js.component.orgSelect.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| jx.js.component.orgSelect.superclass.getName.call(this);
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
			jx.js.component.orgSelect.superclass.setValue.call(this, r.get(this.displayField));
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
    //回显值,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
    setDisplayValue : function(valueField, displayField){
    	var p_record = new Ext.data.Record();
    	p_record.set(this.valueField,valueField);
        p_record.set(this.displayField,displayField);
        this.setValue(p_record);
    },
    //清空
    clearValue :  function(){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		this.setValue(p_record);
    }
});
Ext.reg('OmOrganization_Win', jx.js.component.orgSelect);