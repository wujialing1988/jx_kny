/** 在修机车选择控件 */
Ext.ns('jx.jxgc.TrainTypeTreeSelect');
jx.jxgc.TrainTypeTreeSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.jxgc.TrainTypeTreeSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);        
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
        jx.jxgc.TrainTypeTreeSelect.returnFn = this.returnFn;
        jx.jxgc.TrainTypeTreeSelect.init();
        if(jx.jxgc.TrainTypeTreeSelect.win == null)   jx.jxgc.TrainTypeTreeSelect.createWin();
        jx.jxgc.TrainTypeTreeSelect.win.show();
    },
    returnFn:function(node, e){    //选择确定后触发函数，用于处理返回记录
    }
});
jx.jxgc.TrainTypeTreeSelect.init = function(){
	jx.jxgc.TrainTypeTreeSelect.win = null;
}
jx.jxgc.TrainTypeTreeSelect.trainTypeTree = Ext.extend(Ext.tree.TreePanel, {
	selectFn: function(node, e) {
		jx.jxgc.TrainTypeTreeSelect.returnFn(node, e);
		jx.jxgc.TrainTypeTreeSelect.win.hide();
	},
	constructor : function() {
		jx.jxgc.TrainTypeTreeSelect.trainTypeTree.superclass.constructor.call(this, {
			tbar :['-',{
	    	text: "", iconCls: "refreshIcon", handler: function(){
	    			jx.jxgc.TrainTypeTreeSelect.treeP.root.reload();
	    			jx.jxgc.TrainTypeTreeSelect.treeP.expandAll();
	    		}
	    	}],
			plugins: ['multifilter'],
		    loader : new Ext.tree.TreeLoader( {
		        dataUrl : ctx + "/trainWorkPlan!tree.action"
		    }),
		    root: new Ext.tree.AsyncTreeNode({
		       	text: i18n.TrainTypeTreeSelect.choiceTrain,
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
		        beforeload: function(node) {
		        	var searchParams = {};
		        	if(node == this.getRootNode()) {
		        		searchParams.parentIDX = node.id;
		        	}else {
		        		searchParams.trainTypeName = node.attributes["text"];
		        	}
					this.loader.dataUrl = ctx + '/trainWorkPlan!tree.action?searchParams=' + Ext.util.JSON.encode(searchParams);
				},
				beforeclick: function(node,e) {
					var isRoot = (node == this.getRootNode());
	                var isLeaf = node.isLeaf();
	                if(isRoot) {
	                    return false;
	                }else if(!isLeaf) {
	                    return false;
	                }
	                return true;
				},
				render: function(){
					this.expandAll();
				}
		    }
		    
		});
		this.on("click", this.selectFn, this);
		
	}	
});	

jx.jxgc.TrainTypeTreeSelect.treeP = new jx.jxgc.TrainTypeTreeSelect.trainTypeTree({
});

//创建弹出窗口
jx.jxgc.TrainTypeTreeSelect.createWin = function(){
    if(jx.jxgc.TrainTypeTreeSelect.win == null){
	    jx.jxgc.TrainTypeTreeSelect.win = new Ext.Window({
	        title:i18n.TrainTypeTreeSelect.choiceTrain, closeAction:"hide", width:300, height:500, layout:"fit", resizable:false, modal:true,
            items:jx.jxgc.TrainTypeTreeSelect.treeP
	    });
    }
}
//注册为Ext容器组件
Ext.reg('TrainTypeTreeSelect', jx.jxgc.TrainTypeTreeSelect);