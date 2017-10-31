Ext.onReady(function(){
  Ext.namespace('ParentNodeTreeWin');
    ParentNodeTreeWin.workPlanIDX = '';//作业计划idx
    ParentNodeTreeWin.processName = '';//流程节点名称
    ParentNodeTreeWin.nodeIDX = '';//选择的节点IDX
    ParentNodeTreeWin.parentNodeIDX = '';//选择的节点的父节点IDX
    ParentNodeTreeWin.selectNodeModel = 'all';
    ParentNodeTreeWin.returnFn = function(nodeIDX, changeNode){
		
	}
    
   	ParentNodeTreeWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jobProcessNodeQuery!getParentTree.action"
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: '',
	        id: 'ROOT_0',
	        leaf: false,
	        expanded :true
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	collapsed : false,
    	selectNodeModel: ParentNodeTreeWin.selectNodeModel,
    	listeners : {
			 beforeload: function(node, e){	        	
            	ParentNodeTreeWin.tree.getLoader().dataUrl = ctx + '/jobProcessNodeQuery!getParentTree.action?parentIDX=' + node.id + '&workPlanIDX=' + ParentNodeTreeWin.workPlanIDX + '&nodeIDX=' + ParentNodeTreeWin.nodeIDX + '&parentNodeIDX=' + ParentNodeTreeWin.parentNodeIDX;
	        },
    		click: function(node, e) {    			
    			var isRoot = (node == ParentNodeTreeWin.tree.getRootNode());
    			if (ParentNodeTreeWin.selectNodeModel == 'exceptRoot' && isRoot) {
    				MyExt.Msg.alert("该节点已在作业流程根节点下，不用再调整！");
    				return;
    			}
	        	ParentNodeTreeWin.returnFn(ParentNodeTreeWin.nodeIDX, node);
	        }
		}
	});	
	
	ParentNodeTreeWin.win = new Ext.Window({
        title:"选择流程节点", 
        width:300, 
        height:400, 
        plain:true, 
        autoScroll : true,
        closeAction:"hide", 
        buttonAlign:'center', 
        maximizable:false, 
        modal: true,
        items:[ParentNodeTreeWin.tree], 
        buttons: [{
               text: "取消", iconCls: "cancelIcon",handler: function(){               	
               	ParentNodeTreeWin.win.hide();
            }
        }],
       listeners:{
	          show: function (window) {
	            ParentNodeTreeWin.tree.root.setText(ParentNodeTreeWin.processName);
	           }
	       }
	});
	
});