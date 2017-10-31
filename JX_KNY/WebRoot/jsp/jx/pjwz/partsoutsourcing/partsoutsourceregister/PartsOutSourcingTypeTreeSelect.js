/** 配件规格型号选择公用js */
Ext.onReady(function(){
	Ext.namespace('PartsOutSourcingTypeTreeSelect');                       //定义命名空间
	
	PartsOutSourcingTypeTreeSelect.panel =  new Ext.TabPanel( {
		activeTab: 0,
	    items : [  {
	        title: '委外目录', layout: 'fit',  items: [ new jx.pjwz.partbase.component.PartsTypeTree.tree() ]
	    	
	    },{
	        title: '全部', layout: 'fit', items : [ jx.pjwz.partbase.component.PartsTypeTree.panel]
	    }]
	});
	
	   //刷新委外目录的配件树方法，参数madeFactoryID为委外厂家ID
	PartsOutSourcingTypeTreeSelect.loadOutsSourcingTree = function(madeFactoryID) {
		jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
		var partsTypeTree = PartsOutSourcingTypeTreeSelect.panel.items.itemAt(0).items.itemAt(0);
		jx.pjwz.partbase.component.PartsTypeTree.searchParams.madeFactoryID = madeFactoryID;
		partsTypeTree.root.reload();
		jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	}
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:PartsOutSourcingTypeTreeSelect.panel });
});