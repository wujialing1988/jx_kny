/**
 * 专业类型选择下拉树
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype: 'ProfessionalType_comboTree',hiddenName: 'professionalType',
 * returnField:[{widgetId:"textId",propertyName:"text"},{widgetId:"statusId",propertyName:"status"}]}
 * 3.配置项说明：
 * a.id：控件id，必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：{id,text：专业类型名称,isLeaf：是否子类,status:状态,parentIDX:父节点id,professionalTypeID:专业类型id}
 * e.selectNodeModel配置项：默认配置为all,all:所有结点都可选中、exceptRoot：除根结点，其它结点都可选、folder:只有目录（非叶子和非根结点）可选、leaf：只有叶子结点可选
 * f.其他配置项参照EXTJS的combo组件
 * 
 * 4.在双击弹出编辑表单时，为回显专业类型选择控件值需在页面js的rowdblclick事件方法中加入以下代码                
	Ext.getCmp(componentId).loadRecord(r,id_field,name_field,componentId);
	            参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
                
   
 * 5.在页面js的新增及重置方法中加入以下代码
 * 	Ext.getCmp(componentId).clear(componentId)以清除回显项;
 *  参数说明：componentId：该控件id
 * 6.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(idx,professionalTypeName);
 *   componentId 控件id，参数说明:idx：该控件idx值、professionalTypeName：该控件professionalTypeName值
 * 7.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id
 *   				 
 */
 
ProfessionalType_comboTree = function(){
	this.tree = new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + '/professionalType!comboTreeForCheck.action'
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:'ROOT_0',text:'专业类型'}),
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){			
	            this.tree.getLoader().dataUrl = ctx + '/professionalType!comboTreeForCheck.action?parentIDX=' + node.id ;
	        },
	        scope:this
//	        append: function(tree,parent,node,index){
//	            	if(node.attributes.isLeaf == '0'){
//						node.disable();
//					}
//	            }
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
                	for(var i=0;i<list.length;i++){
                		ids = ids +　"," + list[i].id ;
                		texts = texts + "," +list[i].text;
//                		alert(list[i].attributes["proSeq"]);   list[i].attributes["professionalTypeID"] ;
                	}
                	ids = ids.substring(1);
                	ids = ids.replace(/\,/g,"','");
//                	ids = "'"+ids+"'";
                	texts = texts.substring(1);
			        this.lastSelectionText = texts;
			        if(this.hiddenField){
			            this.hiddenField.value = ids;
			        }
			        
			        Ext.form.ComboBox.superclass.setValue.call(this, texts);
			        this.value = ids;
			        this.collapse();
                }                
            },{
                text: "清空", iconCls: "resetIcon", scope: this, handler: function(){ 
                	var node = {id: " ", text: " "};
					this.setValue(node);
					//取消选择的勾
					var checkedArray = this.tree.getChecked();
					for(var i = 0; i < checkedArray.length; i++){
						checkedArray[i].attributes["checked"] = false;
					}
					this.tree.getRootNode().reload();
//					this.tree.getChecked().checked(false);
//					this.tree.load();
                }                
            }
	    ])
	});
	this.fieldLabel = '专业类型';
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
    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';
    
    ProfessionalType_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(ProfessionalType_comboTree,Ext.form.ComboBox, {
    
    expand : function(){
    	var ts = this;
        ProfessionalType_comboTree.superclass.expand.call(this);
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
            this.tree.containerScroll = false;
            if(this.tree.xtype){
                this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
            }
            this.tree.render(this.treeId);
            var combox = this;
            
//            this.tree.on('click',function(node){
//                var isRoot = (node == combox.tree.getRootNode());
//                var selModel = combox.selectNodeModel;
//                var isLeaf = node.isLeaf();
//                if(isRoot && selModel != 'all'){
//                    return;
//                }else if(selModel=='folder' && isLeaf){
//                    return;
//                }else if(selModel=='leaf' && !isLeaf){
//                    return;
//                }
//				//对该选择树只能选择叶子节点或子类节点                
//                if(selModel !='leaf' && !isLeaf){
//                	return;
//                }
//                combox.setValue(node);
//                combox.setReturnValue(node);                
//                combox.collapse();
//            });
            var root = this.tree.getRootNode();
            if(!root.isLoaded())
                root.reload();
        }
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
//    //设置自定义配置项的回显值
//	setReturnValue : function(node) {
//		if (!node || !node.text || node.text == '') {
//            return;
//        }        
//        var returnField = this.returnField;
//        if(returnField != null && returnField.length > 0){        	
//        	for(var i = 0;i < returnField.length;i++){
//				var displaytext = returnField[i].propertyName;
//				var fieldName = '';
//				if(typeof(node.attributes) != 'undefined' && node.attributes != null && typeof(node.attributes[displaytext]) != 'undefined'){
//					fieldName = node.attributes[displaytext];
//				}
//				
//				if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
//					&& fieldName != '' && fieldName != null && fieldName != 'null'){
//					Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
//				}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
//					&& fieldName != '' && fieldName != null && fieldName != 'null'){
//					Ext.get(returnField[i].widgetId).dom.value = fieldName;
//				}
//        		
//        	}        	
//        }
//	},
    getValue : function(){
        return typeof this.value != 'undefined' ? this.value : '';
    },
    //根据record回显值,参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
    loadRecord : function(record,id_field,name_field,componentId){
    	var node = {id: " ", text: " "};
        if (record.get(id_field) != null){
        	node = {id: record.get(id_field), text: record.get(name_field)};
        }
        Ext.getCmp(componentId).setValue(node);//控件id
    },
    //清空,参数说明：componentId：该控件id
    clear : function(componentId){
    	var node = {id: " ", text: " "};
	    Ext.getCmp(componentId).setValue(node);
    },
    //回显值（new）,参数说明：idx：该控件idx值、professionalTypeName：该控件professionalTypeName值
    setDisplayValue : function(idx, professionalTypeName){
    	var node = {id: idx, text: professionalTypeName};
        this.setValue(node);
    },
    //清空(new)
    clearValue :  function(){
    	var node = {id: " ", text: " "};
		this.setValue(node);
    }
});

Ext.reg('ProfessionalType_comboTree', ProfessionalType_comboTree);
