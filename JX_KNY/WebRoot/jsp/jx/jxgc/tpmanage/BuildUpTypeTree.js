Ext.onReady(function(){
//定义命名空间
Ext.namespace("TrainBuild");
//机车组成配置树
TrainBuild.tree = new Ext.tree.TreePanel( {
	tbar :new Ext.Toolbar(),
	plugins: ['multifilter'],
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/jcgxBuildQuery!getJcgxBuildTree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: partsBuildUpTypeName,
        id: 'ROOT_0',
        leaf: false,
        icon: ctx + "/frame/resources/images/toolbar/train.gif",
        buildUpPlaceCode: ""
    }),
    rootVisible: true,
    autoScroll : true,
    animate : false,
    useArrows : false,
    border : false,
    collapsed:false,
    listeners: {
        beforeload: function(node,e) {
        	var params = {};
        	if(node && node == TrainBuild.tree.getRootNode()){
        		TrainBuild.tree.loader.dataUrl = ctx + '/jcgxBuildQuery!getJcgxBuildTree.action';
        		params.sycx = node.text;
        		TrainBuild.tree.loader.baseParams.entityJson = Ext.util.JSON.encode(params);
        	}
        	else if(node && node != TrainBuild.tree.getRootNode()){
        		TrainBuild.tree.loader.dataUrl = ctx + '/jcgxBuildQuery!getJcgxBuildTree.action';
        		params.sycx = node.attributes.sycx;
        		params.fjdID = node.id;
            	TrainBuild.tree.loader.baseParams.entityJson = Ext.util.JSON.encode(params);
        	}
        },
        click: function(node,e){
        	TrainBuild.clickFn(node,e);
        }
    }
}); 
//单击事件方法
TrainBuild.clickFn = function(node,e){
	
}
});