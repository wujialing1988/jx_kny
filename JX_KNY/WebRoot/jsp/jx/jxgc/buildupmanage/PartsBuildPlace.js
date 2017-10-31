/** 配件组成维护主界面*/
Ext.onReady(function(){
Ext.namespace('PartsBuildUp');                       //定义命名空间
//配件组成查询集合对象
PartsBuildUp.searchPartsParams = {};
//虚拟组成查询集合对象
PartsBuildUp.searchVirtualParams = {};
//配件规格型号列表
PartsBuildUp.partsTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + "/partsType!findpageList.action?statue="+partsStatus_use,
    singleSelect: true, 
    autoScroll: true,
    //remoteSort: false,
    tbar:[{
        xtype:"combo", id:"queryType_Id_parts", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"配件名称", mode:"local",triggerAction: "all",
        store: new Ext.data.SimpleStore({
            fields: ["type"],
            data: [ ["配件名称"], ["规格型号"] ]
        })
    },{             
        xtype:"textfield",  id:"typeName_Id_parts", width: 70
    },{
        text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
        handler : function(){
            var typeName = Ext.getCmp("typeName_Id_parts").getValue();
            var querytype = Ext.getCmp("queryType_Id_parts").getValue();
            var searchParam = {status:partsStatus_use};
            if(querytype == '配件名称'){
                searchParam.partsName = typeName;
                PartsBuildUp.partsTypeGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
                PartsBuildUp.partsTypeGrid.getStore().load({
                    params:{
                        entityJson:Ext.util.JSON.encode(searchParam)
                    }                                                               
                });
            }else{
                searchParam.specificationModel = typeName;
                PartsBuildUp.partsTypeGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
                PartsBuildUp.partsTypeGrid.getStore().load({
                    params:{
                        entityJson:Ext.util.JSON.encode(searchParam)
                    }                                                               
                });
            }
        }
    },{
        text: "重置", iconCls: "resetIcon",
        handler : function(){
            var searchParam = {status:partsStatus_use};
            PartsBuildUp.partsTypeGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
            PartsBuildUp.partsTypeGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(searchParam)
                }                                                               
            });
            //清空搜索输入框
            Ext.getCmp("typeName_Id_parts").setValue("");
            Ext.getCmp("queryType_Id_parts").setValue("配件名称");
            //清空配件组成查询集合
            PartsBuildUp.searchPartsParams = {};
        }
    }],
        fields: [{
            header:'配件名称', dataIndex:'partsName', editor:{ }, sortable: true
        },{
            header:'规格型号', dataIndex:'specificationModel', editor:{ }
        },{
            dataIndex:'idx', hidden:true
        }]  
        
});
//单击配件记录过滤配件组成和虚拟组成、安装位置列表
PartsBuildUp.partsTypeGrid.on("rowclick", function(grid, rowIndex, e){
			var record = grid.getStore().getAt(rowIndex);
			//过滤配件组成列表
            PartsBuildUp.searchPartsParams.partsTypeIDX = record.get("idx");//配件类型
            PartsBuildUp.searchPartsParams.type = type_parts;//配件组成型号
            PartsBuildUp.searchPartsParams.status = status_use;//启用状态
            //PartsBuildUp.searchPartsParams.isDefault = isDefault_yes;//标准组成
            PartsBuildUp.partsBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
            PartsBuildUp.partsBuildUpGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(PartsBuildUp.searchPartsParams)
                }
            });
            PartsBuildUp.searchVirtualParams.partsTypeIDX = record.get("idx");//配件类型
			PartsBuildUp.searchVirtualParams.type = type_virtual;//虚拟组成型号
			PartsBuildUp.searchVirtualParams.status = status_use;//启用状态
			PartsBuildUp.virtualBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams);
			PartsBuildUp.virtualBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams)
				}
			});
			//过滤配件位置列表
			partsTypeIDX = record.get("idx");//设置配件类型全局变量
			BuildUpPlace.grid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
			BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件类型
			BuildUpPlace.grid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
			BuildUpPlace.grid.store.load();
			//屏蔽【新增虚拟位置按钮】
			Ext.getCmp("virtualPlace_button1").setVisible(false);
		});
//移除侦听器
PartsBuildUp.partsTypeGrid.un('rowdblclick', PartsBuildUp.partsTypeGrid.toEditFn, PartsBuildUp.partsTypeGrid);
//配件组成-设置组成
PartsBuildUp.setBuildUp = function(){	
	//未选择记录，直接返回
	if(!$yd.isSelectedRecord(PartsBuildUp.partsBuildUpGrid)) return;
	//设置初始配件组成型号信息
	var data = PartsBuildUp.partsBuildUpGrid.selModel.getSelections();
	//设置树根节点id
	partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";//设置树根节点id
	partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";//设置树根节点名称
	parentIdx = partsBuildUpTypeIdx;//设置上级位置id全局变量
	buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键
	partsTypeIDX = data[0].get("partsTypeIDX");//设置配件类型主键全局变量
	//打开窗口时重新刷新树
	PartsBuild.tree.root.setId(partsBuildUpTypeIdx);
	PartsBuild.tree.root.setText(partsBuildUpTypeName);
	PartsBuild.tree.root.reload();
    PartsBuild.tree.getRootNode().expand();
	//设置下级结构位置列表
    PartsPlaceBuildUp.childPartsBuildUpGrid.getStore().baseParams = {
		parentIdx : partsBuildUpTypeIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : structure_place
	};
	PartsPlaceBuildUp.childPartsBuildUpGrid.getStore().load();
	//设置下级安装位置列表
	PartsPlaceBuildUp.childPartsFixGrid.getStore().baseParams = {
		parentIdx : partsBuildUpTypeIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : fix_place + "," + virtual_place
	};	
	PartsPlaceBuildUp.childPartsFixGrid.getStore().load();
	PartsBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
	PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
	//BuildUpPlace.loadGrid(BuildUpPlace.selectGrid,partsBuildUpTypeIdx);//设置【选择安装位置列表】
	//显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
	PartsPlaceBuildUp.win.show();
	PartsPlaceBuildUp.win.maximize();
	PartsBuild.isHideTab(false,false,false,true,true,false,true);
	PartsPlaceBuildUp.tabs.activate("childPartsBuildUpTab");
	Ext.getCmp("virtualPlace_button").setVisible(false);//屏蔽【下级安装位置】列表上的【新增虚拟位置】按钮
}
//配件组成列表
PartsBuildUp.partsBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2, 
    singleSelect: true,
    //storeAutoLoad: false,
    tbar: ['search',{
    	text : "设置组成",
		iconCls : "addIcon",
		handler : function(){
			PartsBuildUp.setBuildUp();
		}    
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  }, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
	},{
		header:'组成类型', dataIndex:'type', hidden:true,
		renderer:function(v){
			switch(v){
				case type_train:
					return "机车组成";
				case type_parts:
					return "配件组成";
				default :
					return "";
			}
		},
		editor:{ xtype: 'hidden', id:'_typeT'},
 		searcher:{ disabled: true}
	},{
		header:'配件名称', dataIndex:'partsName', 
        editor:{ xtype: 'hidden'},
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件规格型号', dataIndex:'partsTypeIDX', hidden:true,
        editor:{xtype: 'hidden'}
	},{
		header:'配件规格型号', dataIndex:'specificationModel', 
        editor:{ xtype: 'hidden' },
		searcher:{ xtype:"textfield"}
	},{
		header:'状态', dataIndex:'status', 
		renderer:function(v){
			switch(v){
				case status_new:
					return "新增";
					break;
				case status_use:
					return "启用";
					break;
				case status_nullify:
					return "作废";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{xtype:'hidden',id:'_statusT'}
	},{
		header:'是否标准组成', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault_yes:
					return "是";
					break;
				case isDefault_no:
					return "否";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{
			xtype:'hidden'
		},
		searcher:{ disabled: true}
	}],
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){    	
        PartsBuildUp.searchPartsParams = {};
        
    	for(prop in searchParam){
	    	PartsBuildUp.searchPartsParams[prop] = searchParam[prop];
		}
		PartsBuildUp.searchPartsParams.type = type_parts;
		PartsBuildUp.searchPartsParams.status = status_use;
		this.store.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(PartsBuildUp.searchPartsParams) }       
        });	

    }
});
//移除侦听器
PartsBuildUp.partsBuildUpGrid.un('rowdblclick', PartsBuildUp.partsBuildUpGrid.toEditFn, PartsBuildUp.partsBuildUpGrid);
//配件组成列表查询处理
PartsBuildUp.partsBuildUpGrid.getStore().on("beforeload",function(){
	PartsBuildUp.searchPartsParams.type = type_parts;
	PartsBuildUp.searchPartsParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
});
//虚拟组成-设置组成
PartsBuildUp.setVirtualBuildUp = function(){	
	//未选择记录，直接返回
	if(!$yd.isSelectedRecord(PartsBuildUp.virtualBuildUpGrid)) return;
	//设置初始虚拟组成型号信息
	var data = PartsBuildUp.virtualBuildUpGrid.selModel.getSelections();	
	partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";//设置树根节点id
	partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";//设置树根节点名称
	parentIdx = partsBuildUpTypeIdx;//设置上级位置id全局变量
	partsTypeIDX = data[0].get("partsTypeIDX");//设置车型主键全局变量
	//打开窗口时重新刷新树
	VirtualBuild.tree.root.setId(data[0].get("idx"));
	VirtualBuild.tree.root.setText(data[0].get("buildUpTypeName"));
	VirtualBuild.tree.root.reload();
    //VirtualBuild.tree.getRootNode().expand();
    //设置下级结构位置列表
    VirtualPlaceBuildUp.childTrainBuildUpGrid.getStore().baseParams = {
		parentIdx : partsBuildUpTypeIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : structure_place
	};
	VirtualPlaceBuildUp.childTrainBuildUpGrid.getStore().load();
	//设置下级安装位置列表
	VirtualPlaceBuildUp.childTrainFixGrid.getStore().baseParams = {
		parentIdx : partsBuildUpTypeIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : fix_place + "," + virtual_place
	};	
	VirtualPlaceBuildUp.childTrainFixGrid.getStore().load();
	//设置【选择安装位置列表】
	BuildUpPlace.loadGrid(BuildUpPlace.v_selectGrid,partsBuildUpTypeIdx);
	//显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
	VirtualPlaceBuildUp.win.show();
	VirtualPlaceBuildUp.win.maximize();
	VirtualBuild.isHideTab(false,false,false,true,true,false,false);
	VirtualPlaceBuildUp.tabs.activate("v_childTrainBuildUpTab");
}
//虚拟组成列表
PartsBuildUp.virtualBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
    storeAutoLoad: false,
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
    	text : "设置组成",
		iconCls : "addIcon",
		handler : function(){
			PartsBuildUp.setVirtualBuildUp();
		}    
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
	},{
		header:'组成类型', dataIndex:'type', hidden:true,
		editor:{ xtype: 'hidden', id:'_typeV'},
 		searcher:{ disabled: true}
	},{
		header:'配件规格型号', dataIndex:'partsTypeIDX', hidden:true,
        editor:{xtype: 'hidden'}
	},{
		header:'配件名称', dataIndex:'partsName', 
        editor:{ xtype: 'hidden'},
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件规格型号', dataIndex:'specificationModel', 
        editor:{ xtype: 'hidden' },
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'状态', dataIndex:'status', 
		renderer:function(v){
			switch(v){
				case status_new:
					return "新增";
					break;
				case status_use:
					return "启用";
					break;
				case status_nullify:
					return "作废";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{xtype:'hidden',id:'_statusV'}
	}]
});
//移除侦听器
PartsBuildUp.virtualBuildUpGrid.un('rowdblclick', PartsBuildUp.virtualBuildUpGrid.toEditFn, PartsBuildUp.virtualBuildUpGrid);
//虚拟组成列表查询处理
PartsBuildUp.virtualBuildUpGrid.getStore().on("beforeload",function(){
	PartsBuildUp.searchVirtualParams.type = type_virtual;
	PartsBuildUp.searchVirtualParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams);
});
//tab选项卡布局
PartsBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "partsBuildUpTab", title: '配件组成型号', layout:'fit', items: [PartsBuildUp.partsBuildUpGrid]
    },
//    { 
//      id: "virtualBuildUpTab", title: '关联虚拟组成型号', layout: 'fit', items: [PartsBuildUp.virtualBuildUpGrid]
//    },
    { 
      id: "fixPlaceTab", title: '安装位置', layout: 'fit', items: [BuildUpPlace.grid]
    }]
});
//点击【安装位置】时屏蔽【新增虚拟位置按钮】
Ext.getCmp("fixPlaceTab").on("activate", function(){
	Ext.getCmp("virtualPlace_button1").setVisible(false);
})

//配件组成页面
PartsBuildUp.partsBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '配件类型', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
        collapsible : true,
        autoScroll: true, layout: 'fit', items : [ PartsBuildUp.partsTypeGrid ]
    }, {
        region : 'center', layout: 'fit', bodyBorder: false, items: [ PartsBuildUp.tabs ]
    } ]
});
//页面自适应布局
//var viewport = new Ext.Viewport({ layout:'fit', items:PartsBuildUp.partsBuildUpPanel });
var viewport = new Ext.Viewport({ layout:'fit', items:PartsBuildUp.partsBuildUpGrid });

});