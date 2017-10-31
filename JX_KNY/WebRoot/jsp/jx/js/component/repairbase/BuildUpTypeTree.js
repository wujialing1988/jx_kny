/**
 * 位置选择下拉树
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype: 'BuildUpType_comboTree',hiddenName: '',
 * returnField:[{widgetId:"textId",propertyName:"text"}]}
 * 3.配置项说明：
 * a.id：控件id，必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：{id,text：专业类型名称,isLeaf：是否子类,status:状态,parentIDX:父节点id,professionalTypeID:专业类型id}
 * e.selectNodeModel配置项：默认配置为all,all:所有结点都可选中、exceptRoot：除根结点，其它结点都可选、folder:只有目录（非叶子和非根结点）可选、leaf：只有叶子结点可选
 * f.其他配置项参照EXTJS的combo组件
 * 
 
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(idx,buildUpTypeName);
 *   componentId 控件id，参数说明：idx：该控件idx值、buildUpTypeName：该控件buildUpTypeName值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id
 *   				 
 */
 
BuildUpType_comboTree = function(){
	this.tree = new Ext.tree.TreePanel({
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/buildUpTypeQuery!allTree.action"
    }),
    root : new Ext.tree.AsyncTreeNode({id:'ROOT_0',text:this.partsBuildUpTypeName,icon: ctx + "/frame/resources/images/toolbar/train.gif",buildUpPlaceCode: ''}),
    rootVisible: true,
    autoScroll : false,
    listeners: {
        beforeload: function(node, e){
            
        	//展开根节点时处理
        	if(node == this.tree.getRootNode()){
        		this.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + this.partsBuildUpTypeIdx ;
        	}  
        	else if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
        		//展开【结构位置】和【无缺省安装组成、未安装配件的安装位置节点】
        		if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
        			this.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&trainNo=' + this.trainNo ;
            		if(typeof(node.attributes["isVirtual"] != 'undefined')){
        				this.tree.loader.dataUrl+= '&isVirtual=' + node.attributes["isVirtual"];
        				if(node.attributes["isVirtual"] == 'true'){
        					this.tree.loader.dataUrl+= '&buildUpPlaceFullCode=' + node.getPath("buildUpPlaceCode").substring(1);
        				}
        			}
        		}
        		//展开【虚拟位置】、【有缺省安装组成型号的安装位置节点】、【安装了配件的安装位置节点】
        		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
        				&& typeof(node.attributes["partsBuildUpTypeIdx"]) != 'undefined'){
        			this.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTree.action?parentIDX=' + node.attributes["partsBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["partsBuildUpTypeIdx"]  + '&trainNo=' + this.trainNo ;;
            		//展开【安装了配件的位置节点】
        			if(typeof(node.attributes["partsAccountIDX"]) != 'undefined'){
        				this.tree.loader.dataUrl+= '&parentPartsAccountIdx=' + node.attributes["partsAccountIDX"];
        			}
        			//展开【虚拟位置节点】
        			if(typeof(node.attributes["isVirtual"] != 'undefined')){
        				this.tree.loader.dataUrl+= '&isVirtual=' + node.attributes["isVirtual"];
        				if(node.attributes["isVirtual"] == 'true'){
        					this.tree.loader.dataUrl+= '&buildUpPlaceFullCode=' + node.getPath("buildUpPlaceCode").substring(1);
        				}
        			}
        		}
        		
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
    this.minListWidth = 200;//设置下拉树显示宽度
    this.partsBuildUpTypeName = '';
    this.partsBuildUpTypeIdx = '';
    this.configType = '';
    this.trainNo = '';
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';    
    BuildUpType_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(BuildUpType_comboTree,Ext.form.ComboBox, {
    
    expand : function(){
    	var ts = this;
        BuildUpType_comboTree.superclass.expand.call(this);
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
            if(this.partsBuildUpTypeName != "" && this.partsBuildUpTypeName != null && this.partsBuildUpTypeName != "null") {
	        	root.text = this.partsBuildUpTypeName;
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
            if(this.partsBuildUpTypeName != "" && this.partsBuildUpTypeName != null && this.partsBuildUpTypeName != "null") {
	        	root.setText(this.partsBuildUpTypeName);
	        }	
        	this.tree.getRootNode().reload();
        }
    },    
    setValue : function(node){
        if (!node || !node.text || node.text == "" ) {
            return;
        }
        //var realValue = this.partsBuildUpTypeName + node.getPath("text").substring(1);
        var text = "";
        if(node.id != " " && node.text != " "){
        	var realValue = this.partsBuildUpTypeName + node.getPath("text").substring(1);
        	text = node.text == " "?'':realValue;
        }else{
            text = node.text == " "?'':node.text;
        }
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = text;
        }
        
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = text == " "?'':text;
    },
    //设置自定义配置项的回显值
	setReturnValue : function(node) {
		if (!node || !node.text || node.text == '') {
            return;
        }        
        var returnField = this.returnField;
        if(returnField != null && returnField.length > 0){        	
        	for(var i = 0;i < returnField.length;i++){
				var displaytext = returnField[i].propertyName;
				var fieldName = '';
				if(typeof(node.attributes) != 'undefined' && node.attributes != null && typeof(node.attributes[displaytext]) != 'undefined'){
					fieldName = node.attributes[displaytext];
					if(displaytext == 'buildUpPlaceFullName'){
						fieldName = this.partsBuildUpTypeName + node.getPath("text").substring(1);
					}
					if(displaytext == 'buildUpPlaceFullCode'){
						fieldName = node.getPath("buildUpPlaceCode").substring(1);
					}
				}
				
				if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
					&& fieldName != null && fieldName != 'null'){
					Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
				}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
					&& fieldName != null && fieldName != 'null'){
					Ext.get(returnField[i].widgetId).dom.value = fieldName;
				}
        		
        	}        	
        }
        
	},
    getValue : function(){
        return typeof this.value != 'undefined' ? this.value : '';
    },
    //回显值,参数说明：fixPlaceFullName：该控件fixPlaceFullName值
    setDisplayValue : function(fixPlaceFullName){
    	this.lastSelectionText = fixPlaceFullName;
        if(this.hiddenField){
            this.hiddenField.value = fixPlaceFullName;
        }
        
        Ext.form.ComboBox.superclass.setValue.call(this, fixPlaceFullName);
        this.value = fixPlaceFullName == " "?'':fixPlaceFullName;        
    },
    //清空
    clearValue :  function(){
    	var node = {id: " ", text: " "};
		this.setValue(node);
    }
});

Ext.reg('BuildUpType_comboTree', BuildUpType_comboTree);
