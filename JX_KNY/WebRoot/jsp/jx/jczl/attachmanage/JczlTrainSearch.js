/**
 * 机车信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JczlTrain');                       //定义命名空间
Ext.namespace('Organization');
JczlTrain.searchParam = {}
//Organization.currentNodeId = 0;
JczlTrain.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!searchTrainList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/jczlTrain!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/jczlTrain!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    singleSelect: true,
    tbar: ['search','refresh'],
	fields: [{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'资产状态', dataIndex:'assetState', editor:{ xtype:'numberfield', maxLength:2 },searcher:{disabled: true},
		renderer:function(v){
			switch(v){
				case assetStateUse:
					return "使用中";
				case assetStateScrap:
					return "报废";
				default:
					return v;
			}
		},
		searcher:{
			xtype: 'combo',	        	        
	        hiddenName:'assetState',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [['','全部'],[assetStateUse,"使用中"],[assetStateScrap,"报废"]]
			}),
			valueField:'v',
			editable:false,
			displayField:'t',
			triggerAction:'all',
			value:'',
			mode:'local'
		}
	},{
		header:'机车状态', dataIndex:'trainState', editor:{ xtype:'numberfield', maxLength:2 },
		renderer:function(v){
			switch(v){
				case trainStateRepair:
					return "检修";
				case trainStateUse:
					return "运用";
				case trainStateSpare:
					return "备用";
				default:
					return v;
			}
		},
		searcher:{
			xtype: 'combo',	        	        
	        hiddenName:'trainState',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [['','全部'],[trainStateRepair,"检修"],[trainStateUse,"运用"],[trainStateSpare,"备用"]]
			}),
			valueField:'v',
			editable:false,
			displayField:'t',
			triggerAction:'all',
			value:'',
			mode:'local'
		}
	},{
		header:'使用别', dataIndex:'trainUse', editor:{  maxLength:50 },searcher:{ disabled:true }
	},{
		header:'配属单位', dataIndex:'holdOrgName', editor:{ xtype:'numberfield', maxLength:10 },searcher:{disabled: true}
	},{
		header:'支配单位', dataIndex:'usedOrgName', editor:{ xtype:'numberfield', maxLength:10 },searcher:{disabled: true}
	/*},{
		header:'原配属单位', dataIndex:'oldHoldOrgName', editor:{ xtype:'numberfield', maxLength:10 },searcher:{disabled: true}
	},{
		header:'制造厂家主键', dataIndex:'makeFactoryIDX', editor:{  maxLength:5 },searcher:{disabled: true}*/
	},{
		header:'制造厂家名', dataIndex:'makeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	/*},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50 },searcher:{disabled: true}*/
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },searcher:{disabled: true}
	/*},{
		header:'登记人', dataIndex:'registerPerson', editor:{ xtype:'numberfield', maxLength:10 },searcher:{disabled: true}
	},{
		header:'登记人名称', dataIndex:'registerPersonName', editor:{  maxLength:25 },searcher:{disabled: true}
	},{
		header:'登记时间', dataIndex:'registerTime', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	},{
		header:'是否有履历', dataIndex:'isHaveResume', editor:{ xtype:'numberfield', maxLength:1 },searcher:{disabled: true}*/
	}],
	searchFn:function(searchParam){
		JczlTrain.searchParam = searchParam;
		this.store.load();
	}
});
JczlTrain.trainGridSearch = {};
JczlTrain.trainGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!pageTrainStatistics.action',                 //装载列表数据的请求URL
//    组织机构需要根据当前登录人员所在单位进行过滤，如果当前登录人员属于机务段
//    ，则组织机构只显示当前登录人员所在机务段段，右侧查询机车时只能查询当前登录人员所在段配属的机车。
    tbar: ['search','refresh'],
    singleSelect: true,
	fields: [{
		header:'车型名称', dataIndex:'trainTypeName', editor:{  maxLength:8 } 
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'数量', dataIndex:'assetState', editor:{ xtype:'numberfield', maxLength:2 },searcher:{disabled: true}
	},{
		header:'USED', dataIndex:'usedOrgId', width:0, hidden:true, editor:{ xtype:'numberfield', maxLength:2 },searcher:{disabled: true}
	}],
	searchFn:function(searchParam){
		JczlTrain.trainGridSearch = searchParam;
		this.store.load();
	}
});
JczlTrain.trainGrid.store.on('beforeload',function(){
	var searchParam = MyJson.clone(JczlTrain.trainGridSearch);
	if(Organization.currentNode){
		if(Organization.currentNode.attributes.type == 'b'){
			searchParam.bId = Organization.currentNode.id;
		}else if(Organization.currentNode.attributes.type == 'd'){
			searchParam.dId = Organization.currentNode.id;
		}
	}
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
/*
 *  
 */
JczlTrain.grid.store.on('beforeload',function(){
	var searchParam = MyJson.clone(JczlTrain.searchParam);
	if(Organization.currentNode){
		if(Organization.currentNode.attributes.type == 'b'){
			searchParam.bId = Organization.currentNode.id;
		}else if(Organization.currentNode.attributes.type == 'd'){
			searchParam.dId = Organization.currentNode.id;
		}
	}
	//searchParam.usedOrgId = Organization.currentNodeId;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//移除侦听器
JczlTrain.grid.un('rowdblclick', JczlTrain.grid.toEditFn, JczlTrain.grid);
JczlTrain.trainGrid.un('rowdblclick', JczlTrain.trainGrid.toEditFn, JczlTrain.trainGrid);
//当前点击的树节点id
//组织机构树 
Organization.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/jczlTrain!jczlTrainSearchTree.action?id=" + orgId
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: orgRoot,
        id: "0",
        leaf: false,
        expanded:true
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
	
			Organization.currentNode = node;
			
			JczlTrain.trainGrid.store.load();
			JczlTrain.grid.store.load();
        }
    }    
});
Organization.tree.on('beforeload', function(node){
    Organization.tree.loader.dataUrl = ctx + '/jczlTrain!jczlTrainSearchTree.action?id=' + node.id;
});
//Organization.tree.getRootNode().expand();
//tab选项卡布局
JczlTrain.tabs = new Ext.TabPanel({
    activeTab: 0,
    items:[{  
           id: "baseinfoTab",
           title: '机车统计',
           layout:'fit',
           items: [ JczlTrain.trainGrid ]
        },{ 
          id: "childTypeTab",
          title: '机车明细',
          layout: 'fit' ,
          items: [ JczlTrain.grid ]
       }]
});

//页面布局
var viewport = new Ext.Viewport( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal">组织机构</span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                Organization.tree.root.reload();
                Organization.tree.getRootNode().expand();
            }
        } ],
        collapsible : true,
        width : 210,
        minSize : 160,
        maxSize : 280,
        split : true,
        region : 'west',
        bodyBorder: false,
        autoScroll : true,
//        collapseMode:'mini',
        items : [ Organization.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ JczlTrain.tabs ]
    } ]
});

/*setTimeout(function(){ //延迟加载该方法体
	if(!Ext.isEmpty(pOrgId)){
		Organization.tree.getNodeById(pOrgId).expand();
		setTimeout(function(){ //延迟加载该方法体
			if(!Ext.isEmpty(systemOrgid)){
				Organization.tree.getNodeById(systemOrgid).expand(); //展开节点
				Organization.tree.getNodeById(systemOrgid).select(); //选择节点
				Organization.currentNodeId = systemOrgid ;
				JczlTrain.trainGrid.store.load({params:{orgid:Organization.currentNodeId}});
				JczlTrain.grid.store.load({params:{orgid:Organization.currentNodeId}});
			}
		},500)
	}
},1000)*/

});