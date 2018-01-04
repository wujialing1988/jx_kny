
DeportSelect_comboTree = function(){
	this.tree =  new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + '/omOrganizationSelect!deportTree.action'
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:this.orgid,text:this.orgname}),
	    autoScroll : true,
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){	        	
	            this.tree.getLoader().dataUrl = ctx + '/omOrganizationSelect!deportTree.action?parentIDX=' + node.id + '&queryHql='+this.queryHql;
	        },
	        scope:this
	    }
	});
	this.fieldLabel = i18n.DeportSelect.belong;
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
    this.queryHql = '';
    this.orgid = 0;
    this.orgname = '';
    this.minListWidth = 200;//设置下拉树显示宽度
    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';
    
    DeportSelect_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(DeportSelect_comboTree,Ext.form.ComboBox, {
    
    expand : function(){
    	var ts = this;
        DeportSelect_comboTree.superclass.expand.call(this);
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
            //this.tree.height = this.maxHeight;
            this.tree.border=false;
            this.tree.autoScroll=true;
            this.tree.containerScroll = true;
            if(this.tree.xtype){
                this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
            }
	        var root = this.tree.getRootNode();
	        if(this.orgid != "" && this.orgid != null && this.orgid != "null") {
	        	root.id = this.orgid;
	        }
            if(this.orgname != "" && this.orgname != null && this.orgname != "null") {
	        	root.setText(this.orgname);
	        }	 
            this.tree.render(this.treeId);
            var combox = this;  
            
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
				
                combox.setValue(node);
                combox.setReturnValue(node);                
                combox.collapse();  
                combox.fireEvent('select', this);
            });
                  	
            if(!root.isLoaded())
                root.reload();
        }else{
        	var root = this.tree.getRootNode();
	        if(this.orgid != "" && this.orgid != null && this.orgid != "null") {
	        	root.id = this.orgid;
	        }
            if(this.orgname != "" && this.orgname != null && this.orgname != "null") {
	        	root.setText(this.orgname);
	        }	
        	this.tree.getRootNode().reload();
        }
    },
    
    setValue : function(node){
        if (!node || !node.text || node.text == '') {
            return;
        }
        var text = node.text == " "?'':node.text;
        this.lastSelectionText = text;
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        //显示单位全名
//        if(typeof(node.attributes) != 'undefined' && node.attributes != null && typeof(node.attributes["orgname"]) != 'undefined'){
//        	this.lastSelectionText = node.attributes["orgname"];
//        	Ext.form.ComboBox.superclass.setValue.call(this, node.attributes["orgname"]);
////        	this.lastSelectionText = node.attributes["text"];
////        	Ext.form.ComboBox.superclass.setValue.call(this, node.attributes["text"]);
//        }               
        if(this.hiddenField){
            this.hiddenField.value = node.id;
        }        
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
    //回显值,参数说明：idx：该控件idx值、orgname：该控件orgname值
    setDisplayValue : function(idx, orgname){
    	var node = {id: idx, text: orgname};
        this.setValue(node);
    },
    //清空
    clearValue :  function(){
    	var node = {id: " ", text: " "};
		this.setValue(node);
    }
});

Ext.reg('DeportSelect_comboTree', DeportSelect_comboTree);
