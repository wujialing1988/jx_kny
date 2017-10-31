/**
 * 人员选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入jspf文件:<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
 *   在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
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

Ext.namespace("omEmployeeSelect");
// 人员列表数据容器
omEmployeeSelect.userstore = new Ext.data.JsonStore({
	url : ctx + "/omEmployeeSelect!pageList.action",
	autoLoad : true,
	root : "root",
	storeId:"empid",
	remoteSort : true,
	totalProperty : "totalProperty",
	fields : ["empid", "empcode", "empname", "gender","orgname","orgid","payId","orgseq"]
});
/** ****************************************************************************************** */
//机构选择树
omEmployeeSelect.OrgTree = Ext.extend(Ext.tree.TreePanel, {
	parentObj:null,
	constructor : function() {
		omEmployeeSelect.OrgTree.superclass.constructor.call(this, {
			autoScroll:true,
			loader : new Ext.tree.TreeLoader({
				dataUrl : ctx + '/omOrganizationSelect!tree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				text : systemOrgname,
				id : systemOrgid,
				leaf : false,
				orgseq : ''
			}),
			listeners : {
				click : function(node, e) {
					this.parentObj.grid.store.baseParams.start = 0;
					this.parentObj.grid.store.baseParams.limit = this.parentObj.grid.bbar.pageSize;
					this.parentObj.grid.store.baseParams.orgid =  node.id;
					this.parentObj.grid.store.baseParams.orgcode = node.attributes["orgcode"];
					this.parentObj.grid.store.baseParams.orgseq = node.attributes["orgseq"];
					var empname = this.parentObj.grid.getTopToolbar().get(1).getValue();
					this.parentObj.grid.store.baseParams.emp = empname;
					this.parentObj.grid.store.load();
				},
				render : function() {		        	
	    			this.root.reload();
				    this.getRootNode().expand();
	    		},
				beforeload: function(node){
					this.loader.dataUrl = ctx + '/omOrganizationSelect!tree.action?parentIDX=' + node.id ;
				}
			},			
			rootVisible : true
		});
	}
});	
	
/** ****************************************************************************************** */

//分页工具
omEmployeeSelect.pagingToolbar = Ext.yunda.createPagingToolbar({store: omEmployeeSelect.userstore});
//人员列表
omEmployeeSelect.UserList = Ext.extend(Ext.grid.GridPanel, {
	parentObj:null,
	constructor : function() {
		omEmployeeSelect.UserList.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : true
			},
			loadMask: {msg: "正在处理，请稍侯..."},
			// 偶数行变色
			stripeRows : true,	
			// 工具栏
			tbar : [{
					xtype:"label",
					text:"人员姓名："
				},{	            
	                xtype:"textfield",								                
	                name : "empName",
			        width: 140,
			        enableKeyEvents: true,
			        listeners: {
			        	keyup: function( me, e ) {
			        		var empname = me.getValue();
							var store = omEmployeeSelect.userstore;
							store.baseParams.emp = empname;
							store.baseParams.orgid = null;
							store.baseParams.orgseq = null;
							store.load();
			        	}
			        }
				},'->',{
					text : "搜索",
					iconCls : "searchIcon",
					handler : function(){
						var empname = this.getTopToolbar().get(1).getValue();
						this.store.baseParams.emp = empname;
						this.store.baseParams.emp = empname;
						this.store.load();
					},
					scope : this
				},{
					text : "重置",
					iconCls : "resetIcon",
					handler : function(){
						this.store.baseParams.emp = "";
						this.store.load();
						this.getTopToolbar().get(1).setValue("");
					},
					scope : this
				}/*,{
					text : "选择",
					iconCls : "addIcon",
					handler : this.onSubmitClick,
					scope : this
				}*/],
			colModel : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), {
					sortable : true,
					header : "名称",
					title : "双击该行记录选择人员",
					dataIndex : "empname",
					width:50
				}, {
					sortable : true,
					header : "人员代码",
					title : "双击该行记录选择人员",
					dataIndex : "empcode",
					width:60
				}, {
					sortable : true,
					header : "empid",
					dataIndex : "empid",
					hidden : true
				}, {
					sortable : true,
					header : "orgid",
					dataIndex : "orgid",
					hidden : true
				}, {
					sortable : true,
					header : "所属班组",
					dataIndex : "orgname",
					title : "双击该行记录选择人员",
					width:100
				}, {
					sortable : true,
					header : "payId",
					dataIndex : "payId",
					hidden : true
				}
			]),
			store : omEmployeeSelect.userstore,
			bbar: omEmployeeSelect.pagingToolbar,
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
	}/*,
	getValues : function() {
		var sm = this.getSelectionModel();
		if (sm.getCount() < 1) {
			MyExt.Msg.alert("尚未选择一条记录！");
			return;
		}
		return sm.getSelected();
	}*/
});

/** ****************************************************************************************** */
//弹出窗口
omEmployeeSelect.UserSelectWin = Ext.extend(Ext.Window, {
	tree : new omEmployeeSelect.OrgTree(),
	grid : new omEmployeeSelect.UserList(),
	parentObj:null,
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
    	this.tree.parentObj = this;
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
		omEmployeeSelect.UserSelectWin.superclass.constructor.call(this, {
			title : "选择人员",
			width : 535,
			height : 325,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			defaults: { layout: "fit", border: false },
			items : [new Ext.Panel({
						width : 170,
						minSize : 160,
						maxSize : 280,
						region : "west",
						items : this.tree
					}), new Ext.Panel({
							region : "center",
							items : [this.grid]
						})]

				});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	//选择按钮点击事件
	/*onSubmitClick : function() {
		//this.fireEvent("submit",this,this.grid.getValues());	
		if(this.grid.getSelectionModel().getCount() < 1){
			MyExt.Msg.alert("尚未选择一条记录！");
			return;
		}
		this.parentObj.setValue(this.grid.getValues());
		this.close();
	}*/
});
/** ****************************************************************************************** */
omEmployeeSelect.UserSelect = Ext.extend(Ext.form.TriggerField, {
	valueField : 'empid',
	displayField : 'empname',
	hiddenName : 'userselect_id',
	editable :true,			
	win : new omEmployeeSelect.UserSelectWin(),			
	returnField:[],
	triggerClass:'x-form-search-trigger',
	orgid:systemOrgid,
	orgname:systemOrgname,
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;			
			this.win.show(this.el);
//			this.win.show();
		}
	},
	initComponent : function() {
		omEmployeeSelect.UserSelect.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		omEmployeeSelect.UserSelect.superclass.onRender.call(this, ct, position);        
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
		omEmployeeSelect.UserSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		omEmployeeSelect.UserSelect.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, _r) {
		this.setValue(_r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| omEmployeeSelect.UserSelect.superclass.getName.call(this);
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
			omEmployeeSelect.UserSelect.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('OmEmployee_SelectWin', omEmployeeSelect.UserSelect);