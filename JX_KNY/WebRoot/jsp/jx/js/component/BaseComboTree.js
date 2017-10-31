/**
 * 
 * 通用单选下拉树
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script> 
 * 2.在表单页面js添加下例标签代码 
 * { xtype: 'Base_comboTree',hiddenName: 'professionalType',fieldLabel:'专业类型',
 * returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'all',business: 'professionalType', rootText: '专业类型'}
 * 3.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：根据配置业务类服务business里的getBaseComboTree业务方法具体配置,注意：字段名大小写与对应业务方法中一致(nodeMap.put("professionalTypeID", type.getProfessionalTypeID()))
 * 4.selectNodeModel配置项：默认配置为all,all:所有结点都可选中、exceptRoot：除根结点，其它结点都可选、folder:只有目录（非叶子和非根结点）可选、leaf：只有叶子结点可选
 * 5.business配置项：对应业务类服务名，如专业类型为professionalType,必填
 * 6.rootText配置项：根节点显示名，必填
 * 7.queryParams配置项：配置需传递的参数集合，如{"rdpIDX":rdpIDX,"node":node}
 * 8.queryHql配置项：自定义Hql，根据配置业务类服务business里的getBaseComboTree业务方法具体配置
 * 9.valueField配置项：本控件提交值，默认为"id"，
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
jx.js.component.Base_comboTree = function(){	
	this.tree = new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : this.treeUrl
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:this.rootId,text:this.rootText}),
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){				
	            this.tree.getLoader().dataUrl = this.treeUrl + '?parentIDX=' + node.id 
	                                            + '&queryParams=' + Ext.util.JSON.encode(this.queryParams)
	                                            + '&manager=' + this.business + '&queryHql=' + this.queryHql ;
	        },
	        render: function(){
	        	if (this.isExpandAll == true) {
	        		this.tree.expandAll();
	        	}
	        },
	        scope:this
	    }
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
    this.isExpandAll = false;//是否全部展开树
    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';    
    jx.js.component.Base_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(jx.js.component.Base_comboTree,Ext.form.ComboBox, {
    expand : function(){
    	var ts = this;
        jx.js.component.Base_comboTree.superclass.expand.call(this);
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
	        	this.tree.getRootNode().id = this.rootId;	        
            if(!Ext.isEmpty(this.rootText )) 
	        	this.tree.getRootNode().setText(this.rootText);
            this.tree.render(this.treeId);
            var combox = this;
            //下拉树点击事件
            this.tree.on('click',function(node){
                var isRoot = (node == combox.tree.getRootNode());
                var selModel = combox.selectNodeModel;
                var isLeaf = node.isLeaf();
                if(isRoot && selModel != 'all'){
                    return;
                }else if(selModel=='folder' && isLeaf){
                    return;
                }else if(selModel=='leaf' && !isLeaf){
                    return;
                }
                var nodeValue = {};
                if(!isRoot)
					nodeValue = {id: node.attributes[combox.valueField], text: node.attributes[combox.displayField]};
				else
					nodeValue = {id: combox.rootId, text: combox.rootText};
                combox.setValue(nodeValue);
                combox.setReturnValue(node);
                combox.collapse();
                combox.fireEvent('select', this);
            });
            if(!root.isLoaded())
                this.tree.getRootNode().reload();
        }else{        	
	        if(!Ext.isEmpty(this.rootId )) 
	        	this.tree.getRootNode().id = this.rootId;	        
            if(!Ext.isEmpty(this.rootText )) 
	        	this.tree.getRootNode().setText(this.rootText);	        	 
        	this.tree.getRootNode().reload();
        }
    },
    
    setValue : function(node){
        if (!node || !node.text || node.text == "" )
            return;        
        var text = node.text == " "?'':node.text;
        this.lastSelectionText = text;
        if(this.hiddenField)
            this.hiddenField.value = node.id;
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = node.id == " "?'':node.id;
    },
    //设置自定义配置项的回显值
	setReturnValue : function(node) {
		if (!node || !node.text || node.text == '') 
            return;           
        var isRoot = (node == this.tree.getRootNode()); 
        var selModel = this.selectNodeModel;
        var returnField = this.returnField;
        if(returnField != null && returnField.length > 0){        	
        	for(var i = 0;i < returnField.length;i++){
				var displaytext = returnField[i].propertyName;
				var fieldName = '';
				if(typeof(node.attributes) != 'undefined' && node.attributes != null && typeof(node.attributes[displaytext]) != 'undefined') {
					fieldName = node.attributes[displaytext];
				} else {
					fieldName = node.text;
				}
				if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
					&& fieldName != '' && fieldName != null && fieldName != 'null'){
						// 增加对日期类型字段的特殊处理
						if (Ext.getCmp(returnField[i].widgetId).getXType() == 'my97date') {
							Ext.getCmp(returnField[i].widgetId).setValue(new Date(parseInt(fieldName)).format('Y-m-d H:i'));
						} else {
							Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
						}
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

Ext.reg('Base_comboTree', jx.js.component.Base_comboTree);
