/**
 * TODO 待删除，此业务无用
 * 承修部门多选下拉树
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/UnderTakeDepCombo.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { 
		id: 'undertakeDep_Id', xtype: 'UnderTakeDep_comboTree', fieldLabel: '承修部门',
		hiddenName: 'undertakeDep',
		selectNodeModel: "exceptRoot",
		returnField: [{widgetId:"textId",propertyName:"id"},{widgetId:"textId1",propertyName:"orgname"}],
		forDictHql:"[onlyFirstLevel]",
		queryHql:" and orgcode in (select e.id.dictid from EosDictEntry e where e.id.dicttypeid = 'SCDD_PRODUCTION_PLAN_REPAIR_DEP') ",//"[degree]plant",//|fullName
		allowBlank:false
	}
 * 3.配置项说明：
 * a.id：控件id，必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：{id 部门id,text：部门名称,orgcode:部门编码,orgname:部门名称,orgtype:部门类型,orgseq：部门序列,orgaddr:部门地址,sortno:排序编号}
 * e.selectNodeModel配置项：默认配置为all,all:所有结点都可选中、exceptRoot：除根结点，其它结点都可选、folder:只有目录（非叶子和非根结点）可选、leaf：只有叶子结点可选
 * f.其他配置项参照EXTJS的combo组件
 *  
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(idx,undertakeDepName);
 *   componentId 控件id，参数说明:idx：该控件idx值、undertakeDepName：该控件undertakeDepName值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id
 *   				 
 */
 
UnderTakeDep_comboTree = function(){
	this.tree = new Ext.tree.TreePanel({
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + '/omOrganizationSelect!customTree.action'
	    }),
	    root : new Ext.tree.AsyncTreeNode({id:this.orgid,text:this.orgname}),
	    rootVisible: true,
	    listeners: {
	        beforeload: function(node, e){			
	            this.tree.getLoader().dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' + node.id + '&queryHql='+this.queryHql + '&forDictHql=' + this.forDictHql + '&fullNameDegree=' + this.fullNameDegree + '&isChecked=true';
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
			            this.hiddenField.value = ids;
			        }
			        
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
	this.fieldLabel = '所属部门';
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
    this.orgid = systemOrgid;
    this.orgname = systemOrgname;
    this.minListWidth = 220;//设置下拉树显示宽度
    this.minChars = 1000;//设置字符个数开始触发
    this.forDictHql = '';
    this.fullNameDegree = '';//设置单位全名到哪一级如oversea到段plant到车间    
    //all:所有结点都可选中
    //exceptRoot：除根结点，其它结点都可选
    //folder:只有目录（非叶子和非根结点）可选
    //leaf：只有叶子结点可选
    this.selectNodeModel = arguments[0].selectNodeModel || 'all';
    
    UnderTakeDep_comboTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(UnderTakeDep_comboTree,Ext.form.ComboBox, {
    
    expand : function(){
    	var ts = this;
        UnderTakeDep_comboTree.superclass.expand.call(this);
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
            var root = this.tree.getRootNode();
	        if(this.orgid != "" && this.orgid != null && this.orgid != "null") {
	        	root.id = this.orgid;
	        }
            if(this.orgname != "" && this.orgname != null && this.orgname != "null") {
	        	root.setText(this.orgname);
	        }	 
            this.tree.render(this.treeId);
            var combox = this; 
            var root = this.tree.getRootNode();
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
    getValue : function(){
        return typeof this.value != 'undefined' ? this.value : '';
    },    
    //回显值（new）,参数说明：idx：该控件idx值、depName：该控件depName值
    setDisplayValue : function(idx, depName){
    	var node = {id: idx, text: depName};
        this.setValue(node);
    },
    //清空(new)
    clearValue :  function(){
    	var node = {id: " ", text: " "};
		this.setValue(node);
    }
});

Ext.reg('UnderTakeDep_comboTree', UnderTakeDep_comboTree);
