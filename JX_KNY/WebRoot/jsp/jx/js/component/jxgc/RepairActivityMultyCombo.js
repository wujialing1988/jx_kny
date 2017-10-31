/**
 * TODO V3.2.1代码重构
 * 工长派工查询-检修活动多选控件
 * 程锐
 * 2013-11-15
 */
RepairActivityMulty_comboTree = function(){
	this.dataurl = ctx + '/repairActivity!comboTree.action';
	this.tree =  new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : this.dataurl
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:this.orgid,text:this.orgname}),
	    autoScroll : true,
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){	        	
	            this.tree.getLoader().dataUrl = this.dataurl + '?queryParam='+Ext.util.JSON.encode(this.queryParam)+"&parentIDX="+node.id+"&queryJson="+Ext.util.JSON.encode(this.queryJson)+ '&isChecked=true';
	        },
	        scope:this
	    },
	    tbar :new Ext.Toolbar([
	    	{
                text: "确定", iconCls: "saveIcon", scope: this, handler: function(){ 
                	var list = new Array();
                	list = this.tree.getChecked();
                	if(list.length < 0){
                		MyExt.Msg.alert("尚未选择记录!");
                		return ;
                	}
                	var ids = "";
                	var texts = "";
                	var returnField = this.returnField;//回显字段
                	var fieldArray = {};
                	for(var i=0;i<list.length;i++){
                		ids = ids +　";" + list[i].id ;
                		texts = texts + ";" +list[i].text;
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
			        this.lastSelectionText = texts;
			        if(this.hiddenField){
			            this.hiddenField.value = texts;
			        }
			        
			        Ext.form.ComboBox.superclass.setValue.call(this, texts);
			        this.value = texts;
			        //回显字段赋值
			        for(prop in fieldArray){
			        	if(typeof(Ext.getCmp(prop)) != 'undefined' && Ext.getCmp(prop) != null)
			        		Ext.getCmp(prop).setValue(fieldArray[prop].substring(1));
			        	else if(Ext.get(prop) != 'undefined' && Ext.get(prop) != null)
			        		Ext.get(prop).setValue(fieldArray[prop].substring(1));
			        }
			        this.collapse();
                }                
            },{
                text: "清空", iconCls: "resetIcon", scope: this, handler: function(){ 
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
                }                
            }
	    ])
	});
	this.fieldLabel = '检修活动';
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
    this.orgid = 0;
    this.orgname = '检修活动名称';
    this.minListWidth = 200;//设置下拉树显示宽度
    this.queryParam = '';
    this.queryJson = '';
    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';
    
    RepairActivityMulty_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(RepairActivityMulty_comboTree,Ext.form.ComboBox, {
    
    expand : function(){
    	var ts = this;
        RepairActivityMulty_comboTree.superclass.expand.call(this);
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
            
            /*this.tree.on('click',function(node){
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
            });*/
                  	
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

Ext.reg('RepairActivityMulty_comboTree', RepairActivityMulty_comboTree);
