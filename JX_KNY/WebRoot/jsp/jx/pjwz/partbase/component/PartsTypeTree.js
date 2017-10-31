/** 配件规格型号公用树 */
Ext.onReady(function(){
	Ext.namespace('jx.pjwz.partbase.component.PartsTypeTree');                       //定义命名空间
	jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	jx.pjwz.partbase.component.returnFn = function(node){
		//自定义实现功能
		alert(node.attributes["text"]);
	}
	jx.pjwz.partbase.component.PartsTypeTree.tree = Ext.extend(Ext.tree.TreePanel, {
		constructor : function() {
			jx.pjwz.partbase.component.PartsTypeTree.tree.superclass.constructor.call(this, {
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
			        click: function(node, e) {
			        	jx.pjwz.partbase.component.returnFn(node);
			        },
			        beforeload: function(node){
						this.loader.dataUrl = ctx + '/partsType!tree.action?searchParams=' + Ext.util.JSON.encode(jx.pjwz.partbase.component.PartsTypeTree.searchParams);
					}
			    }    
			});
		}
	});
	jx.pjwz.partbase.component.PartsTypeTree.typeTree = new jx.pjwz.partbase.component.PartsTypeTree.tree();
	jx.pjwz.partbase.component.reloadFn = function(){
   		// 物料编码
   		var matCode = Ext.getCmp('matCode_k').getValue();
		jx.pjwz.partbase.component.PartsTypeTree.searchParams = {matCode: matCode};
		jx.pjwz.partbase.component.PartsTypeTree.searchParams = MyJson.deleteBlankProp(jx.pjwz.partbase.component.PartsTypeTree.searchParams);
    	var partsTypeTree = jx.pjwz.partbase.component.PartsTypeTree.typeTree;
    	partsTypeTree.root.reload();
    	partsTypeTree.root.expand();
    	jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	};
	jx.pjwz.partbase.component.PartsTypeTree.searchForm = new Ext.form.FormPanel({
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
               handler: jx.pjwz.partbase.component.reloadFn
           }]
		}]
	});
	
	jx.pjwz.partbase.component.PartsTypeTree.panel = new Ext.Panel({
		layout:"border",
		items:[
				{
					region:"north",
					layout:"fit",
					height:35,
					border: false,
					items: jx.pjwz.partbase.component.PartsTypeTree.searchForm
				},
				{
					region:"center",
					layout:"fit",
					border: false,
					items: jx.pjwz.partbase.component.PartsTypeTree.typeTree
				}
		]
	});
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
});
