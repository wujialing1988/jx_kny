Ext.onReady(function(){
//定义命名空间
Ext.namespace("TrainFormTreeSelectWin");
	TrainFormTreeSelectWin.sycx = "";
	//当前点击的树节点id
	TrainFormTreeSelectWin.currentNodeId = "";
	TrainFormTreeSelectWin.treePath = "";
		// 选择一个流程节点时的初始化机车构型Grid
//	TrainFormTreeSelectWin.initFn = function(node) {
//		if (node == null || node.id == null) {
//			return;
//		}
//		// 获取当前节点的路径信息
//		TrainFormTreeSelectWin.treePath = node.getPath();
//		TrainFormTreeSelectWin.nodeIDX = node.id;
//		TrainFormTreeSelectWin.sycx = JcgxBuild.tree.root.text;
//		TrainFormTreeSelectWin.flbm = node.attributes["flbm"];
//		TrainFormTreeSelectWin.path = node.getPath('text');
//	}
	// 重新加载树
	TrainFormTreeSelectWin.reloadTree = function(path) {
		TrainFormTreeSelectWin.tree.root.reload(function() {
			if (!path) TrainFormTreeSelectWin.tree.getSelectionModel().select(TrainFormTreeSelectWin.tree.root);
		});
		if (path == undefined || path == "" || path == "###") {
			TrainFormTreeSelectWin.tree.root.expand();
		} else {
			// 展开树到指定节点
			TrainFormTreeSelectWin.tree.expandPath(path);
			TrainFormTreeSelectWin.tree.selectPath(path);
		}
	}
	
	//零部件名称树 
	TrainFormTreeSelectWin.tree = new Ext.tree.TreePanel({
		tbar :[{			
	           fieldLabel: "车型",
				hiddenName: "trainTypeIDX",allowBlank: false,
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				xtype: "Base_combo", emptyText:'请先选择车型！',
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		               	TrainFormTreeSelectWin.sycx = this.getValue();
	               		  // 重新加载树	            		
			            TrainFormTreeSelectWin.reloadTree(TrainFormTreeSelectWin.treePath);
		        	}
				}
			},'-', new Ext.Toolbar()],
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jcgxBuildQuery!getJczcmcBuildTree.action" 
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	        text: TrainFormTreeSelectWin.sycx,
	        id: "1",
	        expanded: true,
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
//	    autoHeight : true,
	    collapsed: false,
	    animate : true,
	    useArrows : false,
	    border : false,
	    listeners: {
	        click: function(node, e) {
	        	TrainFormTreeSelectWin.currentNodeId = node.id ;
	        	if(TrainFormTreeSelectWin.currentNodeId != "0"){
	        		if(node.attributes.pjmc != ""){
	        			Ext.getCmp("flmc_2").setValue(node.text.replace("("+node.attributes.pjmc+")",""));
	        		}else{
		        		Ext.getCmp("flmc_2").setValue(node.text);
	        		}
	        		TrainFormTreeSelectWin.win.hide();
	        	}else{
	        		MyExt.Msg.alert("不能选择根节点！");
	        	}
	          }
	       }
	   });    

	TrainFormTreeSelectWin.tree.on('beforeload', function(node){
	    var fjdID = node.id;	   
	    TrainFormTreeSelectWin.tree.loader.dataUrl = ctx + '/jcgxBuildQuery!getJczcmcBuildTree.action?sycx=' + TrainFormTreeSelectWin.sycx + '&fjdID=' + fjdID;
	});
	//配件名称选择弹出窗口
	TrainFormTreeSelectWin.win = new Ext.Window({
	 	width: 370, height: 500, 
	    title: "机车组成", items:TrainFormTreeSelectWin.tree, autoScroll : true, layout:"fit",
	   	plain: true, closeAction: "hide", buttonAlign:'center',
	   	buttons:[{
			text:'关闭', iconCls:'closeIcon', 
			handler: function(){ 
				TrainFormTreeSelectWin.win.hide();
			}
	   	}]
	});

});