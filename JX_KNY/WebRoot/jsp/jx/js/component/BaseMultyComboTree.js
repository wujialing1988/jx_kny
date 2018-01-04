/**
 * 
 * 通用多选下拉树
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script> 
 * 2.在表单页面js添加下例标签代码 
 * { xtype: 'Base_multyComboTree',hiddenName: 'professionalType',fieldLabel:'专业类型',
 * returnField:[{widgetId:"textId",propertyName:"text"}],business: 'professionalType', rootText: '专业类型'}
 * 3.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：根据配置业务类服务business里的getBaseComboTree业务方法具体配置,注意：字段名大小写与对应业务方法中一致(nodeMap.put("professionalTypeID", type.getProfessionalTypeID()))
 * 4.business配置项：对应业务类服务名，如专业类型为professionalType,必填
 * 5.rootText配置项：根节点显示名，必填
 * 6.queryParams配置项：配置需传递的参数集合，如{"rdpIDX":rdpIDX,"node":node}
 * 7.queryHql配置项：自定义Hql，根据配置业务类服务business里的getBaseComboTree业务方法具体配置
 * 8.valueField配置项：本控件提交值，默认为"id"，
 * 可自定义所需字段(根据配置业务类服务business里的getBaseComboTree业务方法具体配置,注意：字段名大小写与对应业务方法中一致(nodeMap.put("professionalTypeID", type.getProfessionalTypeID()))) 
 * 10.displayField配置项：本控件显示值，默认为"text",
 * 可自定义所需字段(根据配置业务类服务business里的getBaseComboTree业务方法具体配置,注意：字段名大小写与对应业务方法中一致(nodeMap.put("professionalTypeID", type.getProfessionalTypeID())))
 * 11.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(idx, text);
 *   componentId 控件id，参数说明：idx：该控件提交值、text：该控件显示值
 * 12.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id  
 */
Ext.namespace("jx.js.component");
function setChkBox (node, isChk){
		//向上遍历
		//setParentNode(node, isChk);
		//向下遍历
		setChildNode(node, isChk);
	}
	
	//设置父节点的勾选状态
	function setParentNode(node, isChk){
		if(node.attributes.realid == '0') return false;
		if(node.parentNode.id == 'ROOT_0') return;
		if(isChk) {
			node.parentNode.getUI().checkbox.checked = isChk;
			node.parentNode.attributes.checked = isChk;
			setParentNode(node.parentNode, isChk);
		} else {
			var childs = node.parentNode.childNodes; //找到与之同级的所有节点
			var hasChked = false;
			for(var i = 0; i<childs.length; i++){
				if(childs[i].getUI().checkbox.checked){
					hasChked = true;
					break;
				}
			}
			if(hasChked){
				return false;
			} else {
				node.parentNode.getUI().checkbox.checked = isChk;
				node.parentNode.attributes.checked = isChk;
				setParentNode(node.parentNode, isChk);
			}
		}
	}
	//设置子节点的勾选状态
	function setChildNode(node, isChk){
		if(typeof(node) == 'undefined') {
			return false;
		}
		else if(node.leaf) {
			return false;
		}
		else {
			var childs = node.childNodes; //找到该节点的所有子节点
			for(var i = 0; i<childs.length; i++){
				childs[i].getUI().checkbox.checked = isChk;
				childs[i].attributes.checked = isChk;
				setChildNode(childs[i],isChk);
			}
		}
	}
jx.js.component.Base_multyComboTree = function(){	
	this.tree = new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : this.treeUrl
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:this.rootId,text:this.rootText}),
	    autoScroll : true,
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){				
	            this.tree.getLoader().dataUrl = this.treeUrl + '?parentIDX=' + node.id 
	            								+ '&queryParams='+Ext.util.JSON.encode(this.queryParams)
	            								+ '&manager=' + this.business 
	            								+ '&queryHql='+this.queryHql + '&isChecked=true'+'&isCx='+this.isCx;
	        },
	        //树checkbox的勾选和取消事件
    		checkchange : function(node, e){
				setChkBox(node, e);
    		},
	        scope:this
	    },
	    tbar :new Ext.Toolbar([
	    	{    	
                text:i18n.BaseMultyComboTree.confirm, iconCls: "saveIcon", scope: this, handler: function(){ 
                	var list = new Array();
                	list = this.tree.getChecked();
                	if(list.length == 0){
                		MyExt.Msg.alert(i18n.BaseMultyComboTree.noChoiceRecording);
                		return ;
                	}
                	var ids = "";
                	var texts = "";
                	var returnField = this.returnField;//回显字段
                	var fieldArray = {};
                	for(var i=0;i<list.length;i++){
                		ids = ids +　";" + list[i].attributes[this.valueField] ;
                		texts = texts + ";" +list[i].attributes[this.displayField];
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
                	texts = texts.substring(1);
			        this.lastSelectionText = texts;
			        if(this.hiddenField)
			            this.hiddenField.value = ids;
			        			        
			        Ext.form.ComboBox.superclass.setValue.call(this, texts);
			        this.value = ids;
			        //回显字段赋值
			        for(prop in fieldArray){
			        	if(typeof(Ext.getCmp(prop)) != 'undefined' && Ext.getCmp(prop) != null)
			        		Ext.getCmp(prop).setValue(fieldArray[prop].substring(1));
			        	else if(Ext.get(prop) != 'undefined' && Ext.get(prop) != null)
			        		Ext.get(prop).setValue(fieldArray[prop].substring(1));
			        }
			        this.collapse();
			        this.fireEvent('select', this);
                }                
            },{
                text:i18n.BaseMultyComboTree.clear, iconCls: "resetIcon", scope: this, handler: function(){ 
                	//清空控件本身值
                	var node = {id: " ", text: " "};
					this.setValue(node);
					//清空控件回带值
					var returnField = this.returnField;
                	if(returnField != null && returnField.length > 0){
    					for(var j = 0;j < returnField.length;j++){
    						displayId = returnField[j].widgetId;
    						if(typeof(Ext.getCmp(displayId)) != 'undefined' && Ext.getCmp(displayId) != null)
			        			Ext.getCmp(displayId).setValue("");
			        		else if(Ext.get(displayId) != 'undefined' && Ext.get(displayId) != null)
			        			Ext.get(displayId).setValue("");     						
    					}
            		}
            		//取消选择的勾
					var checkedArray = this.tree.getChecked();
					for(var i = 0; i < checkedArray.length; i++){
						checkedArray[i].attributes["checked"] = false;
					}
					this.tree.getRootNode().reload();
//					this.tree.expandAll();
                }                
            }
	    ])
	});
	this.fieldLabel = '';
    this.treeId = Ext.id()+'-tree';
    this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
    this.tpl = new Ext.Template('<tpl for="."><div style="height:'+this.maxHeight+'px"><div id="'+this.treeId+'"></div></div></tpl>');
    this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
    this.selectedClass = '';
    this.mode = 'local';
    this.triggerAction = 'all';
    this.onSelect = Ext.emptyFn;
    this.editable = false;
    this.returnField;  
    this.queryParams;
    this.business;
    this.rootId = 'ROOT_0';
    this.rootText = '';
    this.queryHql = '';
    this.treeUrl = ctx + '/baseCombo!getBaseComboTree.action';
    this.valueField = 'id';
    this.displayField = 'text';
    this.minListWidth = 260;//设置下拉树显示宽度
    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';
    
    jx.js.component.Base_multyComboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(jx.js.component.Base_multyComboTree,Ext.form.ComboBox, {
    expand : function(){
    	var ts = this;
        jx.js.component.Base_multyComboTree.superclass.expand.call(this);
        var root = this.tree.getRootNode();
        if(!this.tree.rendered){
            //去除双滚动条
        	if(!this.height&&!this.tree.height){//没有设置高度，自适应
        		this.innerList.setOverflow('hidden');
				this.restrictHeight();
				this.on('expand',function(){
					var height=ts.innerList.getHeight();
					Ext.get(ts.treeId).parent().setHeight(height);
					ts.tree.setHeight(height);
				})				
				this.tree.height=ts.innerList.getHeight();
				Ext.get(ts.treeId).parent().setHeight(ts.innerList.getHeight());
			}
            this.tree.border=false;
            this.tree.autoScroll=true;
            this.tree.containerScroll = true;
            if(this.tree.xtype)
                this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
	        if(!Ext.isEmpty(this.rootId )) 
	        	root.id = this.rootId;	        
            if(!Ext.isEmpty(this.rootText )) 
	        	root.setText(this.rootText);
            this.tree.render(this.treeId);
            if(!root.isLoaded())
                root.reload();
        }else{        	
	        if(!Ext.isEmpty(this.rootId )) 
	        	root.id = this.rootId;	        
            if(!Ext.isEmpty(this.rootText )) 
	        	root.setText(this.rootText);	 
        	this.tree.getRootNode().reload();
        }
//        this.tree.expandAll(); 展开所有
    },
    setValue : function(node){
        if (!node || !node.text || node.text == "" ) {
            return;
        }
        var text = node.text == " "?'':node.text;
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = node.id;
        }
        
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = node.id == " "?'':node.id;
    },
    //设置自定义配置项的回显值
	setReturnValue : function(node) {
		if (!node || !node.text || node.text == '') {
            return;
        }   
        var isRoot = (node == this.tree.getRootNode()); 
        var selModel = this.selectNodeModel;
        var returnField = this.returnField;
        if(returnField != null && returnField.length > 0){        	
        	for(var i = 0;i < returnField.length;i++){
				var displaytext = returnField[i].propertyName;
				var fieldName = '';
				if(typeof(node.attributes) != 'undefined' && node.attributes != null && typeof(node.attributes[displaytext]) != 'undefined'){
					fieldName = node.attributes[displaytext];
				}
				//对选择根节点作特殊处理,取根节点的text值
				 if(isRoot && selModel == 'all'){
					fieldName = node.text;
				}
				if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
					&& fieldName != '' && fieldName != null && fieldName != 'null'){
					Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
				}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
					&& fieldName != '' && fieldName != null && fieldName != 'null'){
					Ext.get(returnField[i].widgetId).dom.value = fieldName;
				}
        		
        	}        	
        }
	},
    getValue : function(){
        return typeof this.value != 'undefined' ? this.value : '';
    },
    //回显值,参数说明：idx：该控件idx值、text：该控件text值
    setDisplayValue : function(idx, text){
    	var node = {id: idx, text: text};
        this.setValue(node);
    },
    //清空
    clearValue :  function(){
    	var node = {id: " ", text: " "};
		this.setValue(node);
    }
});

Ext.reg('Base_multyComboTree', jx.js.component.Base_multyComboTree);
