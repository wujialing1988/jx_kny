Ext.onReady(function(){
//定义命名空间
Ext.namespace("PartsWin");

	//当前点击的树节点id
	PartsWin.currentNodeId = "ROOT_0";
	//零部件名称树 
	PartsWin.tree = new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jcpjzdBuild!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	        text: '零部件名称',
	        id: "ROOT_0",
	        expanded: true,
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : false,
	//    autoHeight : true,
	    animate : true,
	    useArrows : false,
	    border : false,
	    listeners: {
	        click: function(node, e) {
	        	PartsWin.currentNodeId = node.id ;
	        	if(PartsWin.currentNodeId != "ROOT_0"){
	        		if(node.attributes.jcpjbm != ""){
	        			Ext.getCmp("search_pjmc").setValue(node.text.replace("("+node.attributes.jcpjbm+")",""));
	        		}else{
		        		Ext.getCmp("search_pjmc").setValue(node.text);
	        		}
	        		PartsWin.win.hide();
	        	}else{
	        		MyExt.Msg.alert("不能选择根节点！");
	        	}
	          }
	       }
	   });    

	PartsWin.tree.on('beforeload', function(node){
	    var fjdId = node.id;
	    PartsWin.tree.loader.dataUrl = ctx + '/jcpjzdBuild!tree.action?fjdId=' + fjdId;
	});
	//配件名称选择弹出窗口
	PartsWin.win = new Ext.Window({
	 	width: 370, height: 500, 
	    title: "零部件名称", items:PartsWin.tree, autoScroll : true, layout:"fit",
	   	plain: true, closeAction: "hide", buttonAlign:'center'
	});

});