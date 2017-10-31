/** 机车组成维护主界面*/
Ext.onReady(function(){
Ext.namespace('TrainBuildUp');                       //定义命名空间
//机车组成查询集合对象
TrainBuildUp.searchTrainParams = {};
//虚拟组成查询集合对象
TrainBuildUp.searchVirtualParams = {};
//车型列表
TrainBuildUp.trainTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/trainType!findPageList.action",
	singleSelect: true, 
	//remoteSort: false,
	tbar:[{
		xtype:"combo", id:"queryType_Id", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"车型名称", mode:"local",triggerAction: "all",
		store: new Ext.data.SimpleStore({
			fields: ["type"],
			data: [ ["车型名称"], ["车型简称"] ]
		})
	},{	            
        xtype:"textfield",  id:"typeName_Id", width: 70
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			var typeName = Ext.getCmp("typeName_Id").getValue();
			var querytype = Ext.getCmp("queryType_Id").getValue();
			var searchParam = {};
			if(querytype == '车型名称'){
				searchParam.typeName = typeName;
				TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				TrainBuildUp.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}else{
				searchParam.shortName = typeName;
				TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				TrainBuildUp.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}
		}
	},{
		text : "重置",
		iconCls : "resetIcon",
		handler : function(){
			TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode({});
			TrainBuildUp.trainTypeGrid.getStore().load({
				params:{
					typeName : "",
					shortName : ""
				}																
			});
			//清空搜索输入框
			Ext.getCmp("typeName_Id").setValue("");
			Ext.getCmp("queryType_Id").setValue("车型名称");
			//清空机车组成查询集合
			TrainBuildUp.searchTrainParams = {};
		}
	}],
		fields: [{
			header:'车型名称', dataIndex:'typeName', editor:{ }, sortable: true
		},{
			header:'车型简称', dataIndex:'shortName', editor:{ }
        },{
            header:'车型代码', dataIndex:'typeID', hidden:true          
		}]
});
//单击车型记录过滤机车和虚拟组成、安装位置列表
TrainBuildUp.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
			var record = grid.getStore().getAt(rowIndex);
			//过滤机车组成列表
			TrainBuildUp.searchTrainParams.trainTypeIDX = record.get("typeID");//车型
			TrainBuildUp.searchTrainParams.type = type_train;//机车组成类型
			TrainBuildUp.searchTrainParams.status = status_use;//启用状态
			TrainBuildUp.trainBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchTrainParams);
			TrainBuildUp.trainBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(TrainBuildUp.searchTrainParams)
				}
			});
			//过滤虚拟组成列表
			TrainBuildUp.searchVirtualParams.trainTypeIDX = record.get("typeID");//车型
			TrainBuildUp.searchVirtualParams.type = type_virtual;//虚拟组成类型
			TrainBuildUp.searchVirtualParams.status = status_use;//启用状态
			TrainBuildUp.virtualBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams);
			TrainBuildUp.virtualBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams)
				}
			});
			//过滤安装位置列表
			trainTypeIDX = record.get("typeID");//设置车型全局变量
			BuildUpPlace.grid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
			BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
			BuildUpPlace.grid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
			BuildUpPlace.grid.store.load();
			Ext.getCmp("virtualPlace_button1").setVisible(true);
		});
//移除侦听器
TrainBuildUp.trainTypeGrid.un('rowdblclick', TrainBuildUp.trainTypeGrid.toEditFn, TrainBuildUp.trainTypeGrid);
//机车组成-设置组成
TrainBuildUp.setBuildUp = function(){	
	//未选择记录，直接返回
	if(!$yd.isSelectedRecord(TrainBuildUp.trainBuildUpGrid)) return;
	//设置初始机车组成型号信息
	var data = TrainBuildUp.trainBuildUpGrid.selModel.getSelections();
	//设置树根节点id
	partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";//设置树根节点id
	partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";//设置树根节点名称
	parentIdx = rootParentIdx;//设置上级位置id全局变量
	buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键
	trainTypeIDX = data[0].get("trainTypeIDX");//设置车型主键全局变量
	partsTypeIDX = "";//设置配件主键全局变量为空
	//打开窗口时重新刷新树
	TrainBuild.tree.root.setText(partsBuildUpTypeName);
	TrainBuild.tree.root.reload();
    TrainBuild.tree.getRootNode().expand();
	//设置下级结构位置列表
    TrainPlaceBuildUp.childTrainBuildUpGrid.getStore().baseParams = {
		parentIdx : rootParentIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : structure_place
	};
	TrainPlaceBuildUp.childTrainBuildUpGrid.getStore().load();
	//设置下级安装位置列表
	TrainPlaceBuildUp.childTrainFixGrid.getStore().baseParams = {
		parentIdx : rootParentIdx,
		buildUpTypeIDX : partsBuildUpTypeIdx,
		placeTypes : fix_place + "," + virtual_place
	};	
	TrainPlaceBuildUp.childTrainFixGrid.getStore().load();	
	//BuildUpPlace.loadGrid(BuildUpPlace.selectGrid,partsBuildUpTypeIdx);//设置【选择安装位置列表】
	TrainBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
	PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
	//显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
	TrainPlaceBuildUp.win.show();
	TrainPlaceBuildUp.win.maximize();
	TrainBuild.isHideTab(false,false,false,true,true,false,true);
	TrainPlaceBuildUp.tabs.activate("childTrainBuildUpTab");
	Ext.getCmp("selectPlace_Button").setVisible(true);//显示【下级安装位置】列表上的【选择安装位置】按钮
	Ext.getCmp("virtualPlace_button").setVisible(true);//显示【下级安装位置】列表上的【安装虚拟位置】按钮
}
//复制标准组成
TrainBuildUp.copyBuildUp = function(){
	//未选择记录，直接返回
	if(!$yd.isSelectedRecord(TrainBuildUp.trainBuildUpGrid)) return;
	//设置初始机车组成型号信息
	var data = TrainBuildUp.trainBuildUpGrid.selModel.getSelections();
	if(data[0].get("isDefault") == isDefault_yes){
		MyExt.Msg.alert("请为非标准组成复制标准组成！");
		return;
	}
	Ext.Ajax.request({
		    url: ctx + "/placeBuildUpType!copyBuildUp.action",
		    params: {typeIDX: data[0].get("trainTypeIDX"), type: data[0].get("type"), buildUpTypeIDX: data[0].get("idx")},
		    success: function(response, options){	                  	
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.success == true ) {			                    	
		        		MyExt.Msg.alert("复制标准组成成功！");				
		            } else {
		            	alertFail(result.errMsg);
		            }
		    },
		    failure: function(response, options){
		        MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		}); 
}
//机车组成列表
TrainBuildUp.trainBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2, 
    singleSelect: true,
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: [{
    	text : "设置组成",
		iconCls : "addIcon",
		handler : function(){
			TrainBuildUp.setBuildUp();
		}    
    },{
    	text : "复制标准组成",
		iconCls : "addIcon",
		handler : function(){
			TrainBuildUp.copyBuildUp();
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
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,
		editor:{ id:"trainTypeIDX_Id", xtype:'hidden'},
		searcher:{ disabled: true}       
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{ id:"trainTypeShortName_Id", readOnly:true},
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
	}]    
});
//移除侦听器
TrainBuildUp.trainBuildUpGrid.un('rowdblclick', TrainBuildUp.trainBuildUpGrid.toEditFn, TrainBuildUp.trainBuildUpGrid);
//机车组成列表查询处理
TrainBuildUp.trainBuildUpGrid.getStore().on("beforeload",function(){
	TrainBuildUp.searchTrainParams.type = type_train;
	TrainBuildUp.searchTrainParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchTrainParams);
});
//虚拟组成-设置组成
TrainBuildUp.setVirtualBuildUp = function(){	
	//未选择记录，直接返回
	if(!$yd.isSelectedRecord(TrainBuildUp.virtualBuildUpGrid)) return;
	//设置初始虚拟组成型号信息
	var data = TrainBuildUp.virtualBuildUpGrid.selModel.getSelections();	
	partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";//设置树根节点id
	partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";//设置树根节点名称
	parentIdx = partsBuildUpTypeIdx;//设置上级位置id全局变量
	buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键
	trainTypeIDX = data[0].get("trainTypeIDX");//设置车型主键全局变量
	partsTypeIDX = "";//设置配件主键全局变量为空
	//打开窗口时重新刷新树
	VirtualBuild.tree.root.setId(data[0].get("idx"));
	VirtualBuild.tree.root.setText(data[0].get("buildUpTypeName"));
	VirtualBuild.tree.root.reload();
    VirtualBuild.tree.getRootNode().expand();
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
	VirtualBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
	PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
	//BuildUpPlace.loadGrid(BuildUpPlace.v_selectGrid,partsBuildUpTypeIdx);//设置【选择安装位置列表】
	//显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
	VirtualPlaceBuildUp.win.show();
	VirtualPlaceBuildUp.win.maximize();
	VirtualBuild.isHideTab(false,false,false,true,true,false,true);
	VirtualPlaceBuildUp.tabs.activate("v_childTrainBuildUpTab");
	Ext.getCmp("selectPlace_Button2").setVisible(true);//显示【下级安装位置】列表上的【选择安装位置】按钮
	Ext.getCmp("virtualPlace_button2").setVisible(true);//显示【下级安装位置】列表上的【安装虚拟位置】按钮
}
//虚拟组成列表
TrainBuildUp.virtualBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
    storeAutoLoad: false,
    singleSelect: true,
    //remoteSort: false,
    tbar: [{
    	text : "设置组成",
		iconCls : "addIcon",
		handler : function(){
			TrainBuildUp.setVirtualBuildUp();
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
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,
		editor:{ id:"trainTypeIDX_VId", xtype:'hidden'},
		searcher:{ disabled: true}       
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{ id:"trainTypeShortName_VId", readOnly:true},
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
TrainBuildUp.virtualBuildUpGrid.un('rowdblclick', TrainBuildUp.virtualBuildUpGrid.toEditFn, TrainBuildUp.virtualBuildUpGrid);
//机车组成列表查询处理
TrainBuildUp.virtualBuildUpGrid.getStore().on("beforeload",function(){
	TrainBuildUp.searchVirtualParams.type = type_virtual;
	TrainBuildUp.searchVirtualParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams);
});
//tab选项卡布局
TrainBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "trainBuildUpTab", title: '机车组成型号', layout:'fit', items: [TrainBuildUp.trainBuildUpGrid]
    },{ 
      id: "virtualBuildUpTab", title: '关联虚拟组成型号', layout: 'fit', items: [TrainBuildUp.virtualBuildUpGrid]
    },{ 
      id: "fixPlaceTab", title: '安装位置', layout: 'fit', items: [BuildUpPlace.grid]
    }]
});
//机车组成页面
TrainBuildUp.trainBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '车型', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
        collapsible : true,
        autoScroll: true, layout: 'fit', items : [ TrainBuildUp.trainTypeGrid ]
    }, {
        region : 'center', layout: 'fit', bodyBorder: false, items: [ TrainBuildUp.tabs ]
    } ]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainBuildUp.trainBuildUpPanel });
});