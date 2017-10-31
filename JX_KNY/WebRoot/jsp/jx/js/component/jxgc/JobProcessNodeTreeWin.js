Ext.onReady(function(){
  Ext.namespace('JobProcessNodeTreeWin');
    JobProcessNodeTreeWin.workPlanIDX = '';//作业计划idx
    JobProcessNodeTreeWin.processName = '';//流程名称
    JobProcessNodeTreeWin.nodeIDX = '';//流程节点IDX
    JobProcessNodeTreeWin.thisObj = {};//调用对象
    JobProcessNodeTreeWin.returnFn = function(node){
		
	}
    
   	JobProcessNodeTreeWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jobProcessNodeQuery!getTree.action"
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
    	listeners : {
			 beforeload: function(node, e){	        	
            	JobProcessNodeTreeWin.tree.getLoader().dataUrl = ctx + '/jobProcessNodeQuery!getTree.action?parentIDX=' + node.id + '&workPlanIDX=' + JobProcessNodeTreeWin.workPlanIDX + '&nodeIDX=' + JobProcessNodeTreeWin.nodeIDX;
	        },
    		click: function(node, e) {
	        	JobProcessNodeTreeWin.returnFn(node,JobProcessNodeTreeWin.thisObj);
	        }
		}
	});	
	
	JobProcessNodeTreeWin.win = new Ext.Window({
        title:"选择流程节点", 
        width:300, 
        height:400, 
        plain:true, 
        autoScroll : true,
        closeAction:"hide", 
        buttonAlign:'center', 
        maximizable:false, 
        items:[JobProcessNodeTreeWin.tree], 
        buttons: [{
               text: "取消", iconCls: "cancelIcon",handler: function(){               	
               	JobProcessNodeTreeWin.win.hide();
            }
        }],
       listeners:{
	          show: function (window) {
	            JobProcessNodeTreeWin.tree.root.setText(JobProcessNodeTreeWin.processName);
	           }
	       }
	});
	
});