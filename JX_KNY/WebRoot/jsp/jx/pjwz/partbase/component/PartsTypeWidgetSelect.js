/** 配件规格型号选择控件 */
Ext.ns('jx.pjwz.PartsTypeSelect');
jx.pjwz.PartsTypeSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.pjwz.PartsTypeSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
        jx.pjwz.PartsTypeSelect.returnFn = this.returnFn;
        jx.pjwz.PartsTypeSelect.init();
        if(jx.pjwz.PartsTypeSelect.win == null)   jx.pjwz.PartsTypeSelect.createWin();
        jx.pjwz.PartsTypeSelect.win.show();
    },
    returnFn:function(node, e){    //选择确定后触发函数，用于处理返回记录
    }
});
jx.pjwz.PartsTypeSelect.init = function(){
	jx.pjwz.PartsTypeSelect.win = null;
	jx.pjwz.PartsTypeSelect.panel = null;
	jx.pjwz.PartsTypeSelect.orgPanel = null;
	jx.pjwz.PartsTypeSelect.trainPanel = null;	
	jx.pjwz.PartsTypeSelect.searchParams = {};
}

jx.pjwz.PartsTypeSelect.partsTypeTree = Ext.extend(Ext.tree.TreePanel, {
	selectFn: function(node, e) {
		jx.pjwz.PartsTypeSelect.returnFn(node, e);
		jx.pjwz.PartsTypeSelect.win.hide();
	},
	constructor : function() {
		jx.pjwz.PartsTypeSelect.partsTypeTree.superclass.constructor.call(this, {
			tbar :new Ext.Toolbar(),
			plugins: ['multifilter'],
		    loader : new Ext.tree.TreeLoader( {
		        dataUrl : ctx + "/partsType!tree.action"
		    }),
		    root: new Ext.tree.AsyncTreeNode({
		       	text: '配件规格型号',
		        id: 'ROOT_0',
		        leaf: false,
		        expanded :true
		    }),
		    rootVisible: true,
		    autoScroll : true,
		    animate : false,
		    useArrows : false,
		    border : false,
		    listeners: {
		        beforeload: function(node){
					this.loader.dataUrl = ctx + '/partsType!tree.action?searchParams=' + Ext.util.JSON.encode(jx.pjwz.PartsTypeSelect.searchParams);
				}
		    }
		    
		});
		this.on("click", this.selectFn, this);
	}
});	
jx.pjwz.PartsTypeSelect.typeTree = new jx.pjwz.PartsTypeSelect.partsTypeTree();
jx.pjwz.PartsTypeSelect.reloadFn = function(){
   	// 物料编码
   	var matCode = Ext.getCmp('matCode_k').getValue();
	jx.pjwz.PartsTypeSelect.searchParams = {matCode: matCode};
	jx.pjwz.PartsTypeSelect.searchParams = MyJson.deleteBlankProp(jx.pjwz.PartsTypeSelect.searchParams);
	var partsTypeTree = jx.pjwz.PartsTypeSelect.typeTree;
	partsTypeTree.root.reload();
	partsTypeTree.root.expand();
	jx.pjwz.PartsTypeSelect.searchParams = {};
};
jx.pjwz.PartsTypeSelect.searchForm = new Ext.form.FormPanel({
		style:'padding:5px;', 
		baseCls: "x-plain", border: false,labelWidth:70,
				fieldWidth:80,
		items:[{
        	xtype: 'compositefield', fieldLabel : '物料编码', 
			combineErrors: false,
        	items: [
           {
				xtype:"textfield",
				id:"matCode_k",
				fieldLabel:"物料编码"
				
			},
           {
               xtype: 'button',
               text: '查找',
               width: 40,
               handler: jx.pjwz.PartsTypeSelect.reloadFn
           }]
		}]
	});
	
	jx.pjwz.PartsTypeSelect.typeTreePanel = new Ext.Panel({
		layout:"border",
		items:[
				{
					region:"north",
					layout:"fit",
					height:35,
					border: false,
					items: jx.pjwz.PartsTypeSelect.searchForm
				},
				{
					region:"center",
					layout:"fit",
					border: false,
					items: jx.pjwz.PartsTypeSelect.typeTree
				}
		]
	});
	
jx.pjwz.PartsTypeSelect.orgTree = Ext.extend(Ext.tree.TreePanel, {
	constructor : function() {
		jx.pjwz.PartsTypeSelect.orgTree.superclass.constructor.call(this, {
		    loader : new Ext.tree.TreeLoader( {
		        dataUrl : ctx + "/partsRepairList!tree.action"
		    }),
		    root: new Ext.tree.AsyncTreeNode({
		       	text: '承修部门',
		        id: 'ROOT_0',
		        leaf: false,
		        expanded :true
		    }),
		    rootVisible: true,
		    autoScroll : true,
		    animate : false,
		    useArrows : false,
		    border : false,
		    listeners: {
		        click: function(node, e) {
		        	jx.pjwz.PartsTypeSelect.searchParams = {};
		        	if(node != this.getRootNode()) 	jx.pjwz.PartsTypeSelect.searchParams.repairOrgID = node.id;
		        	var partsTypeTree = jx.pjwz.PartsTypeSelect.orgPanel.items.itemAt(1).items.itemAt(0);
		        	partsTypeTree.root.reload();
		        	partsTypeTree.root.expand();
		        	
		        },
		        beforeload: function(node){
					this.loader.dataUrl = ctx + '/partsRepairList!tree.action';
				}
		    }    
		});
	}
});	

jx.pjwz.PartsTypeSelect.trainTree = Ext.extend(Ext.tree.TreePanel, {
	constructor : function() {
		jx.pjwz.PartsTypeSelect.trainTree.superclass.constructor.call(this, {
		    loader : new Ext.tree.TreeLoader( {
		        dataUrl : ctx + "/trainTypeToParts!tree.action"
		    }),
		    root: new Ext.tree.AsyncTreeNode({
		       	text: '车型',
		        id: 'ROOT_0',
		        leaf: false,
		        expanded :true
		    }),
		    rootVisible: true,
		    autoScroll : true,
		    animate : false,
		    useArrows : false,
		    border : false,
		    listeners: {
		        click: function(node, e) {
		        	jx.pjwz.PartsTypeSelect.searchParams = {};
		        	if(node != this.getRootNode()){
			        	if(node.id.indexOf("X_")==0){ //选中了车型
			        		jx.pjwz.PartsTypeSelect.searchParams.trainTypeIDX = node.attributes.trainTypeIDX;		        		
			        	}
			        	if(node.id.indexOf("P_")==0){ //选择修程，两个条件过滤（车型，修程）
							jx.pjwz.PartsTypeSelect.searchParams.trainTypeIDX = node.parentNode.attributes.trainTypeIDX;
							jx.pjwz.PartsTypeSelect.searchParams.repairClassIDX = node.attributes.xcID;
			        	}
		        	}
		        	var partsTypeTree = jx.pjwz.PartsTypeSelect.trainPanel.items.itemAt(1).items.itemAt(0);
		        	partsTypeTree.root.reload();
		        	partsTypeTree.root.expand();
		        },
		        beforeload: function(node){
					this.loader.dataUrl = ctx + '/trainTypeToParts!tree.action?parentIDX=' + node.id;
				}
		    }    
		});
	}
});	


//创建弹出窗口
jx.pjwz.PartsTypeSelect.createWin = function(){
    if(jx.pjwz.PartsTypeSelect.panel == null)  jx.pjwz.PartsTypeSelect.createPanels();
    if(jx.pjwz.PartsTypeSelect.win == null){
	    jx.pjwz.PartsTypeSelect.win = new Ext.Window({
	        title:'规格型号选择', closeAction:"hide", width:300, height:500, layout:"fit", resizable:false, modal:true, 
            items:jx.pjwz.PartsTypeSelect.panel
	    });
    }
}
jx.pjwz.PartsTypeSelect.createPanels = function(){
	if(jx.pjwz.PartsTypeSelect.panel)  return;
	jx.pjwz.PartsTypeSelect.orgPanel = new Ext.Panel({ 
	   title : '', 
	   layout:'border',//表格布局
	   items: [ 
		   { 
		    region: 'north',            
		    height: 150,
		    layout: 'fit',
		    items: new jx.pjwz.PartsTypeSelect.orgTree()
		   },
		   { 
		    region: 'center',
		    layout: 'fit',
		    items: new jx.pjwz.PartsTypeSelect.partsTypeTree()
		   }
		]

	});
	jx.pjwz.PartsTypeSelect.trainPanel = new Ext.Panel({ 
	   title : '', 
	   layout:'border',//表格布局
	   items: [ 
		   { 
		    region: 'north',            
		    height: 150,
		    layout: 'fit',
		    items: new jx.pjwz.PartsTypeSelect.trainTree()
		   },
		   { 
		    region: 'center',
		    layout: 'fit',
		    items: new jx.pjwz.PartsTypeSelect.partsTypeTree()
		   }
		]
	
	});
	jx.pjwz.PartsTypeSelect.panel =  new Ext.TabPanel( {
		activeTab: 0,
	    items : [ {
	        title: '全部', layout: 'fit', items : [ jx.pjwz.PartsTypeSelect.typeTreePanel ]
	    }, {
	        title: '按承修部门', layout: 'fit',  items: [ jx.pjwz.PartsTypeSelect.orgPanel ]
	    }/*, {
	        title: '按车型', layout: 'fit',  items: [ jx.pjwz.PartsTypeSelect.trainPanel ]
	    }*/ ]
	});
}
//解决“IE8运行脚本报错的问题”
Ext.override(Ext.tree.AsyncTreeNode, {
    reload : function(callback, scope){
        this.collapse(false, false);
//        while(this.firstChild){
//            this.removeChild(this.firstChild).destroy();
//        }
        this.childrenRendered = false;
        this.loaded = false;
        if(this.isHiddenRoot()){
            this.expanded = false;
        }
        this.expand(false, false, callback, scope);
    }
});
//注册为Ext容器组件
Ext.reg('PartsTypeTreeSelect', jx.pjwz.PartsTypeSelect);
