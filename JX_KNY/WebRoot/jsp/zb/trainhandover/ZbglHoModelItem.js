/**
 * 机车交接项模板 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglHoModelItem');                       //定义命名空间
ZbglHoModelItem.idx = '0';
ZbglHoModelItem.searchParam = {};
ZbglHoModelItem.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglHoModelItem!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglHoModelItem!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglHoModelItem!deleteModel.action',            //删除数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    fieldWidth: 300,
	fields: [{
		header:i18n.TrainHandoverItem.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:i18n.TrainHandoverItem.handoverItemName, dataIndex:'handOverItemName', editor:{  maxLength:100, allowBlank: false }
	},{
		header:i18n.TrainHandoverItem.handoverStatus, dataIndex:'handOverItemStatus', 
		editor:{ id:"handOverItemStatus_edit",xtype:'checkboxgroup', name: "handOverItemStatus", fieldLabel: i18n.TrainHandoverItem.handoverStatus, items: statusFields },
		searcher:{ id:"handOverItemStatus_search",xtype:'checkboxgroup', name: "handOverItemStatus", fieldLabel: i18n.TrainHandoverItem.handoverStatus, items: searchStatusFields }
	},{
		header:i18n.TrainHandoverItem.sort, dataIndex:'seqNo', editor:{    
			        xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:99,
			        allowBlank:false
			        }, 
			        searcher: {anchor:'98%'}
			},{
		header:i18n.TrainHandoverItem.parentID, dataIndex:'parentIDX', hidden: true, editor:{  xtype: "hidden"}
	}],
	afterShowSaveWin: function(){
		ZbglHoModelItem.grid.saveForm.getForm().findField("parentIDX").setValue(ZbglHoModelItem.idx);
	},
	afterShowEditWin: function(record, rowIndex){
		var handOverItemStatus = record.get("handOverItemStatus");
		if(!Ext.isEmpty(handOverItemStatus) ){
			var statusArray = handOverItemStatus.split(",");
			for (var i = 0; i < statusArray.length; i++) {
				for(var j = 0; j < objList.length; j++){
    				var checkobj = Ext.getCmp("statusId" + j);
        			if(checkobj.inputValue == statusArray[i]){
        				checkobj.setValue(true);  //设置该状态项为选择
        			}
    			}
			}
		}
	},
	beforeSaveFn: function(data){
		var statusArray = data.handOverItemStatus;		
		if(Ext.isArray(statusArray) && statusArray != null && statusArray.length > 0){
			data.handOverItemStatus = "";
			for (var i = 0; i < statusArray.length; i++) {
				data.handOverItemStatus+= statusArray[i]+ ",";
			}
			data.handOverItemStatus = data.handOverItemStatus.substring(0,data.handOverItemStatus.length-1);
		}		
		if(Ext.isEmpty(data.parentIDX)) data.parentIDX = "0";
		return true;		
	},
	searchFn: function(searchParam){
    	ZbglHoModelItem.searchParam = searchParam ;
        this.store.load();
    },
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
        this.saveWin.hide();
        ZbglHoModelItem.expandNode(result.entity.idx,result.entity.parentIDX);
    },
    afterDeleteFn: function(){
    	ZbglHoModelItem.expandNode();
    }
});
ZbglHoModelItem.grid.store.on("beforeload",function(){
	var searchParam = ZbglHoModelItem.searchParam;
	searchParam.parentIDX = ZbglHoModelItem.idx;
	var whereList = [];
	for (prop in searchParam) {
		if(prop == 'parentIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
	    	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);	
})
//新增删除位置后联动展开树节点
//参数：nodeId点击节点的id，parentIdx 上级位置id
ZbglHoModelItem.expandNode = function(nodeId, parentIdx){
    ZbglHoModelItem.tree.root.reload();
	ZbglHoModelItem.tree.getRootNode().expand();
}
ZbglHoModelItem.tree =  new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/trainHandOverItemModel!getTree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: i18n.TrainHandoverItem.handoverItem,
        id: "0",
        leaf: false,
        expanded :true
    }),
    rootVisible: true,
    autoScroll : true,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
    	beforeload: function(node,e){
    		ZbglHoModelItem.tree.loader.dataUrl = ctx + '/zbglHoModelItem!getTree.action?parentIDX=' + node.id ;
    	},
        click: function(node, e) {				
//			var isRoot = (node == ZbglHoModelItem.tree.getRootNode());//是否根节点
//			var isParent = node.hasChildNodes();//是否父节点
//			if(isRoot) ZbglHoModelItem.tabs.getItem("itemResult").disable();
//			if(isParent) ZbglHoModelItem.tabs.getItem("itemResult").disable();
//			else ZbglHoModelItem.tabs.getItem("itemResult").enable();
			ZbglHoModelItem.idx = node.id;
			ZbglHoModelItem.grid.store.load();
//			ZbglHoModelItemResult.grid.store.load();
        }
    }    
});
ZbglHoModelItem.tabs = new Ext.TabPanel({
    activeTab: 0,
    items:[{
        id: "itemModel", title: i18n.TrainHandoverItem.handoverItem, layout: "fit", border: false, items:[ ZbglHoModelItem.grid ]
    }/*,{
    	id: "itemResult", title: "交接情况", layout: "fit", border: false, items:[ ZbglHoModelItemResult.grid ], disabled : true
    }*/]
});
ZbglHoModelItem.panel = new Ext.Panel({
	layout:"border",
	buttonAlign:"center",
    items : [{
    	collapsible : true,
        width : 280,
        minSize : 160,
        maxSize : 350,
        split : true,
        autoScroll: true,
	    region : 'west',
	    title : '<span style="font-weight:normal">' + i18n.TrainHandoverItem.handoverItem + '</span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
        	id : 'refresh',
            handler : function() {
        		ZbglHoModelItem.tree.root.reload();
            }
        } ],
	    items :  ZbglHoModelItem.tree 
	},{
		layout: "fit",
		region : 'center',
	    bodyBorder: false,
	    items : [ ZbglHoModelItem.tabs ]
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglHoModelItem.panel });
});