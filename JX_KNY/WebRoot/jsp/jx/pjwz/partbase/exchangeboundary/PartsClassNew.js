/**
 * 修程配件分类对应车型规格型号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsClassNew');                       //定义命名空间
PartsClassNew.searchParam = {};  //全局查询变量
PartsClassNew.searchParamParts = {};  //全局查询变量
PartsClassNew.trainTypeIDX = "" ; //全局车型查询条件
PartsClassNew.repairClassIDX = "" ; //全局修程查询条件
PartsClassNew.partsClass = "" ; //全局配件分类查询条件
/**车型基础信息form*/
PartsClassNew.baseTrainForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
            	{ fieldLabel:"车型主键", name:"trainTypeIDX", xtype:"hidden"},
        		{ fieldLabel:"车型编号", name:"trainTypeShortName", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
            	{ fieldLabel:"车型名称", name:"trainTypeName", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        }]
    }]
});
/**车型对应的配件列表*/
PartsClassNew.trainGrid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/trainTypeToParts!findPageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTypeToParts!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainTypeToParts!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,	searchFormColNum:2,
    tbar:['search',{
    	text:'新增',iconCls:'addIcon',handler:function(){
    		if(PartsClassNew.trainTypeIDX == ""){
    			MyExt.Msg.alert("请先选择分类！");
    			return ;
    		}
    		PartsType.selectWin.show();
    		PartsType.grid.store.load();
    	}
    },'delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'配件型号表主键', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'规格型号编码', dataIndex:'specificationModelCode', editor:{ disabled:true}
	},{
		header:'规格型号', dataIndex:'specificationModel',editor:{disabled:true}
	},{
		header:'配件名称', dataIndex:'partsName', editor:{ disabled:true }
	},{
		header:'物料编码', dataIndex:'matCode', editor:{ disabled:true }
	},{
		header:'计量单位', dataIndex:'unit',width:50, editor:{ disabled:true },searcher:{disabled:true}
	},{
		header:'数量', dataIndex:'standardQty',width:38, editor:{allowBlank:false, vtype: "positiveInt", maxLength:8 },searcher:{disabled:true}
	}],
	searchFn: function(searchParam){ 
		PartsClassNew.searchParam = searchParam ;
        PartsClassNew.trainGrid.store.load();
	}
});
//车型对应布局
PartsClassNew.trainPanle = new Ext.Panel({
	layout: "border",
	items:[{
		layout:'fit',height: 75,region: 'north', frame:true, style:'padding-bottom:2px;',
		items:[{
			xtype: "fieldset",title: "车型信息", layout: "fit", items:[PartsClassNew.baseTrainForm]
		}]
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ PartsClassNew.trainGrid ]
    }]
});

/**配件分类基础信息form*/
PartsClassNew.anchor = "90%";
PartsClassNew.basePartsForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, defaults: { anchor: PartsClassNew.anchor },
            items: [
            	{ fieldLabel:"修程主键", name:"repairClassIDX", xtype:"hidden"},
        		{ fieldLabel:"车型编号", name:"trainTypeShortName",  style:"border:none;background:none;", readOnly:true},
        		{ fieldLabel:"车型主键", name:"trainTypeIDX", xtype:"hidden"}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, defaults: { anchor: PartsClassNew.anchor },
            items: [
            	{ fieldLabel:"车型名称", name:"trainTypeName", style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, defaults: { anchor: PartsClassNew.anchor },
            items: [
            	{ fieldLabel:"修程名称", name:"repairClassName", style:"border:none;background:none;", readOnly:true}
            ]
        }]
    }]
});
/**配件分类对应的配件列表*/
PartsClassNew.partsGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/rcClassToParts!findPageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/rcClassToParts!updateStandardQty.action',             //保存数据的请求URL
    deleteURL: ctx + '/rcClassToParts!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,	searchFormColNum:1,
    tbar:['search',{
    	text:'新增',id:'partsGridButton',iconCls:'addIcon',handler:function(){
    		if(PartsClassNew.trainTypeIDX == ""){
    			MyExt.Msg.alert("请先选择分类！");
    			return ;
    		}
    		PartsTypeTrain.selectWin.show();
    		PartsTypeTrain.grid.store.load();
    	}
    },'delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ disabled:true, maxLength:8 },searcher:{disabled:true}
	},{
		header:'规格型号编码', dataIndex:'specificationModelCode', editor:{ disabled:true}
	},{
		header:'规格型号', dataIndex:'specificationModel',editor:{disabled:true}
	},{
		header:'配件名称', dataIndex:'partsName', editor:{ disabled:true }
	},{
		header:'物料编码', dataIndex:'matCode', editor:{ disabled:true }
	},{
		header:'计量单位', dataIndex:'unit',width:50, editor:{ disabled:true },searcher:{disabled:true}
	},{
		header:'数量', dataIndex:'standardQty',width:38, editor:{allowBlank:false, vtype: "positiveInt", maxLength:12 },searcher:{disabled:true}
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8,hidden:true }
	},{
		header:'修程主键', dataIndex:'repairClassIDX', hidden:true, editor:{ xtype:'textfield', maxLength:50 ,hidden:true}
	},{
		header:'修程名称', dataIndex:'repairClassName', hidden:true, editor:{ xtype:'textfield', maxLength:50,disabled:true }
	},{
		header:'配件型号表主键', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'textfield', maxLength:50 ,hidden:true}
	},{
		header:'车型对应规格型号', dataIndex:'trainTypeToPartsIDX',hidden:true,editor:{hidden:true},searcher:{disabled:true}
	}],
	searchFn: function(searchParam){ 
		PartsClassNew.searchParamParts = searchParam ;
        PartsClassNew.partsGrid.store.load();
	}
});

//取消编辑监听
PartsClassNew.partsGrid.un('rowdblclick', PartsClassNew.partsGrid.toEditFn, PartsClassNew.partsGrid); 
//配件分类对应布局
PartsClassNew.partsPanle = new Ext.Panel({
	layout: "border",
	items:[{
		layout:'fit',height: 75,region: 'north', frame:true, style:'padding-bottom:2px;',
		items:[{
			xtype: "fieldset",title: "车型及修程信息", layout: "fit", items:[PartsClassNew.basePartsForm]
		}]
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ PartsClassNew.partsGrid ]
    }]
});

//选择生产计划明细之后执行确认的操作（生成一条作业进度记录）
PartsType.submit = function(){
	var sm = PartsType.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    var formData = PartsClassNew.baseTrainForm.getForm().getValues();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.partsTypeIDX = objData[i].get("idx");  //规格型号主键
    	data.trainTypeIDX = formData.trainTypeIDX;  //车型主键
    	data.trainTypeShortName = formData.trainTypeShortName;  //车型简称
    	data.specificationModel = objData[i].get("specificationModel");//规格型号
    	data.partsName = objData[i].get("partsName");//配件名称
    	data.standardQty = 1;  //默认数量
        dataAry.push(data);
    }
    Ext.Ajax.request({
        url: ctx + '/trainTypeToParts!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                PartsClassNew.trainGrid.store.load();
                PartsType.grid.store.load();
                PartsType.selectWin.hide();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            PartsType.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};

//选择生产计划明细之后执行确认的操作（生成一条作业进度记录）
PartsTypeTrain.submit = function(){
	var sm = PartsTypeTrain.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    var formData = PartsClassNew.basePartsForm.getForm().getValues();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.trainTypeToPartsIDX = objData[i].get("idx");  //车型对应规格型号表主键
    	data.repairClassIDX = formData.repairClassIDX;  //修程主键
    	data.repairClassName = formData.repairClassName;  //修程名称
    	data.partsClass = formData.partsClass;  //配件分类
    	data.specificationModel = objData[i].get("specificationModel");
    	data.partsName = objData[i].get("partsName");
        dataAry.push(data);
    }
    Ext.Ajax.request({
        url: ctx + '/rcClassToParts!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                PartsClassNew.partsGrid.store.load(); //刷新配件分类
                PartsTypeTrain.grid.store.load();
                PartsTypeTrain.selectWin.hide();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            PartsTypeTrain.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};

//查询前添加过滤条件
PartsType.grid.store.on('beforeload',function(){
	var searchParam = PartsType.searchParam;
	searchParam.status = status_use ; //启用状态的配件
	var sqlStr = " idx not in (select nvl(t.parts_type_idx,'-1') " +
			"partsTypeIDX from PJWZ_TrainType_To_Parts_TYPE t where t.record_status=0 and t.train_type_idx='"+PartsClassNew.trainTypeIDX+"') " ;
	var whereList = [
		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
		] ;
	for(porp in searchParam){
		whereList.push({propName:porp, propValue: searchParam[porp] }) ; 
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
//	this.baseParams.sort = "supplierLevel"; //服务端排序
//	this.baseParams.dir = "ASC";
});
//查询前添加过滤条件
PartsClassNew.trainGrid.store.on('beforeload' , function(){
	var searchParam = PartsClassNew.searchParam;
	searchParam.trainTypeIDX = PartsClassNew.trainTypeIDX ;
	searchParam = MyJson.deleteBlankProp(searchParam); //删除无用的
	var whereList=[];
	for(prop in searchParam){
	      whereList.push({propName:prop,propValue:searchParam[prop]});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

//查询前添加过滤条件
PartsTypeTrain.grid.store.on('beforeload',function(){
	var searchParam = PartsTypeTrain.searchParam;
	var sqlStr = " idx not in (select nvl(r.traintype_to_parts_idx,'-1121') idx " +
			"from pjwz_rcclass_to_parts_type r  " +
			"where r.repair_class_idx = '"+PartsClassNew.repairClassIDX+"'" +
					" and r.record_status=0)" ;
	var sqltemp = " trainTypeIDX='"+PartsClassNew.trainTypeIDX+"'" ;
	var whereList = [
		{sql: sqlStr, compare: Condition.SQL}, 
		{sql: sqltemp, compare: Condition.SQL} 
		] ;
	for(porp in searchParam){
		whereList.push({propName:porp, propValue: searchParam[porp] }) ; 
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
//查询前添加过滤条件
PartsClassNew.partsGrid.store.on('beforeload' , function(){
	var searchParam = PartsClassNew.searchParamParts;
	searchParam.trainTypeIDX = PartsClassNew.trainTypeIDX ; //车型主键
	searchParam.repairClassIDX = PartsClassNew.repairClassIDX ; //修程主键
	searchParam = MyJson.deleteBlankProp(searchParam); //删除无用的
	var whereList=[];
	for(prop in searchParam){
		whereList.push({propName:prop,propValue:searchParam[prop]});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

//当前点击的树节点id
PartsClassNew.currentNodeId = "ROOT_0";
//配件分类树 
PartsClassNew.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/trainTypeToParts!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
       	text: '车型及修程',
        id: PartsClassNew.currentNodeId,
        leaf: false,
        expanded :true
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	//取消行编辑
    	    PartsClassNew.trainGrid.rowEditor.stopEditing(false);
        	if(node.id.indexOf("X_")==0){ //选中了车型
        		PartsClassNew.tabs.setActiveTab(0);
				PartsClassNew.tabs.unhideTabStripItem(0);//显示
				PartsClassNew.tabs.hideTabStripItem(1);//隐藏
				PartsClassNew.trainTypeIDX = node.attributes.trainTypeIDX ; //车型主键赋值
				PartsClassNew.trainGrid.store.load(); //刷新
				var form = PartsClassNew.baseTrainForm ;
				form.find("name","trainTypeIDX")[0].setValue(node.attributes.trainTypeIDX);
				form.find("name","trainTypeShortName")[0].setValue(node.text);
				form.find("name","trainTypeName")[0].setValue(node.attributes.trainTypeName);
        	}
        	if(node.id.indexOf("P_")==0){ //选择修程，两个条件过滤（车型，修程）
        		PartsClassNew.tabs.setActiveTab(1);
				PartsClassNew.tabs.unhideTabStripItem(1);//显示
				PartsClassNew.tabs.hideTabStripItem(0);//隐藏
				var tempNode = node.parentNode;
				var partsForm = PartsClassNew.basePartsForm ;
				partsForm.find("name","trainTypeIDX")[0].setValue(tempNode.attributes.trainTypeIDX);
				partsForm.find("name","trainTypeShortName")[0].setValue(tempNode.text);
				partsForm.find("name","trainTypeName")[0].setValue(tempNode.attributes.trainTypeName);
				partsForm.find("name","repairClassIDX")[0].setValue(node.attributes.xcID); //修程主键
				partsForm.find("name","repairClassName")[0].setValue(node.text); //修程名称
				//partsForm.find("name","repairName")[0].setValue("");
        		//Ext.getCmp("partsGridButton").setVisible(false); //当选择修程时隐藏新增按钮
        		//PartsClassNew.partsGrid.getTopToolbar().get(2).setVisible(false); //隐藏删除
        		PartsClassNew.repairClassIDX = node.attributes.xcID ; //修程主键
        		PartsClassNew.trainTypeIDX = node.parentNode.attributes.trainTypeIDX ; //车型主键
        		PartsClassNew.partsClass = "" ; //当选择修程时清空配件分类的查询条件
        		PartsClassNew.partsGrid.store.load();
        	}
        }
    }    
});
PartsClassNew.tree.on('beforeload', function(node){
    PartsClassNew.tree.loader.dataUrl = ctx + '/trainTypeToParts!tree.action?parentIDX=' + node.id;
});
PartsClassNew.tree.getRootNode().expand();


//tab选项卡
PartsClassNew.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{  
           title: '互换范围',layout:'fit',items: [PartsClassNew.trainPanle]
        },{  
           title: '互换范围',layout:'fit',items: [ PartsClassNew.partsPanle ]
        }]
});

//页面自适应布局
var viewport = new Ext.Viewport({
	layout:'fit', 
	items:{
    	layout:"border",
	    items : [ {
	        title : '<span style="font-weight:normal">车型及修程树</span>',
	        iconCls : 'chart_organisationIcon',
	        tools : [ {
	            id : 'refresh',
	            handler : function() {
	                PartsClassNew.tree.root.reload();
	                PartsClassNew.tree.getRootNode().expand();
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
	        items : [ PartsClassNew.tree ]
	    }, {
	        region : 'center',
	        layout : 'fit',
	        bodyBorder: false,
	        items : [ PartsClassNew.tabs ]
	    } ]
    }
});

PartsClassNew.tabs.setActiveTab(0);
PartsClassNew.tabs.hideTabStripItem(1);//初始化隐藏修程类型适用配件面板

setTimeout(function(){ //延迟加载该方法体
	if(trainData != null && trainData.length != 0){
		PartsClassNew.tree.getNodeById("X_"+trainData[0]).select(); //选择节点
		PartsClassNew.trainTypeIDX = trainData[0] ; //车型主键赋值
		PartsClassNew.trainGrid.store.load(); //刷新
		var flag = 1 ;
		if(flag == 1){
			setTimeout(function(){
				PartsClassNew.trainGrid.store.load(); //刷新
				flag = 2 ;
			},200);
		}
		var form = PartsClassNew.baseTrainForm ;
		form.find("name","trainTypeIDX")[0].setValue(trainData[0]);
		form.find("name","trainTypeShortName")[0].setValue(trainData[1]);
		form.find("name","trainTypeName")[0].setValue(trainData[2]);
	}
},1000)

});


