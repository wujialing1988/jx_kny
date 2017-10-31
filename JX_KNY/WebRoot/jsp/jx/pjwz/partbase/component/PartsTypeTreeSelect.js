/** 配件规格型号选择公用js */
Ext.onReady(function(){
	Ext.namespace('PartsTypeTreeSelect');                       //定义命名空间
	
	PartsTypeTreeSelect.panel =  new Ext.TabPanel( {
		activeTab: 0,
	    items : [ {
	        title: '全部', layout: 'fit', items : [ jx.pjwz.partbase.component.PartsTypeTree.panel]
	    }, {
	        title: '按承修部门', layout: 'fit',  items: [ PartsRepairListTree.orgPanel ]
	    }/*, {
	        title: '按车型', layout: 'fit',  items: [ TrainTypeToPartsTree.trainPanel ]
	    }*/
//	    , {
//	        title: '委外目录', layout: 'fit',  items: [ new jx.pjwz.partbase.component.PartsTypeTree.tree() ]
//	    	
//	    }
	    ]
	});
	//刷新委外目录的配件树方法，参数madeFactoryID为委外厂家ID
	PartsTypeTreeSelect.loadOutsSourcingTree = function(madeFactoryID) {
		jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
		var partsTypeTree = PartsTypeTreeSelect.panel.items.itemAt(3).items.itemAt(0);
		jx.pjwz.partbase.component.PartsTypeTree.searchParams.madeFactoryID = madeFactoryID;
	}
	
	//页面自适应布局
//	var viewport = new Ext.Viewport({ layout:'fit', items:PartsTypeTreeSelect.panel });
});