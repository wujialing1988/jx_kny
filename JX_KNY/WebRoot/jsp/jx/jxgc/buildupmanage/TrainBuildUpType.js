/**已作废*/
Ext.onReady(function(){
Ext.namespace('TrainBuildUpType');                       //定义命名空间
//机车组成查询集合对象
TrainBuildUpType.searchTrainParams = {};
//机车组成列表
TrainBuildUpType.trainBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    searchFormColNum: 2,
    singleSelect: true,
    tbar: ['search',{
    	text: '操作',
    	iconCls: 'editIcon',
    	handler: function(){
    		//未选择记录，直接返回
        	if(!$yd.isSelectedRecord(TrainBuildUpType.trainBuildUpGrid)) return;
        	//设置初始机车组成型号信息
        	var data = TrainBuildUpType.trainBuildUpGrid.selModel.getSelections();
        	var array = {idx: data[0].get("idx"),buildUpTypeCode: data[0].get("buildUpTypeCode"),
    						buildUpTypeName: data[0].get("buildUpTypeName")};
    		//设置树根节点id
			partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";
			partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";
			//打开窗口时重新刷新树
			TrainBuild.tree.root.setText(partsBuildUpTypeName);
			TrainBuild.tree.root.attributes["buildUpPlaceCode"] = data[0].get("buildUpTypeCode");
        	TrainBuild.tree.root.reload();
            TrainBuild.tree.expand();
            //获取下级位置列表
//            UpdateTrainBuild.childTrainBuildUpGrid.getStore().load({
//            	params:{
//            		parentIdx : "ROOT_0",
//            		partsBuildUpTypeIdx : partsBuildUpTypeIdx
//            	}
//            });
            UpdateTrainBuild.childTrainBuildUpGrid.getStore().baseParams = {
        		parentIdx : "ROOT_0",
        		partsBuildUpTypeIdx : partsBuildUpTypeIdx
        	};
        	UpdateTrainBuild.childTrainBuildUpGrid.getStore().load();
            //设置下级安装位置列表的新增form的上级安装位置id及所属组成型号id
            UpdateTrainBuild.childTrainBuildUpGrid.defaultData = {parentIdx:"ROOT_0", partsBuildUpTypeIdx: data[0].get("idx") };
            
    		UpdateTrainBuild.win.show(); 
    		UpdateTrainBuild.tabs.hideTabStripItem("trainBuildUpTab");
    		UpdateTrainBuild.tabs.hideTabStripItem("fixBuildUpTypeTab");
    		UpdateTrainBuild.tabs.unhideTabStripItem("childTrainBuildUpTab");
			UpdateTrainBuild.tabs.activate("childTrainBuildUpTab");
    	}
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'车型', dataIndex:'trainTypeShortName', 
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
		editor:{
			xtype: 'combo',
			hiddenName: 'status',
			store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[status_new,'新增'],[status_use,'启用'],[status_nullify,'作废']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
 			value: status_use, 
 			allowBlank: false 
        },
		searcher:{ disabled: true}
	},{
		header:'是否缺省为标准组成', dataIndex:'isDefault', 
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
		searcher:{ disabled: true}
	}],
	searchFn: function(searchParam){
		TrainBuildUpType.searchTrainParams.buildUpTypeCode = '';
		TrainBuildUpType.searchTrainParams.buildUpTypeName = '';
		TrainBuildUpType.searchTrainParams.trainTypeShortName = '';
		for(prop in searchParam){
	    	TrainBuildUpType.searchTrainParams[prop] = searchParam[prop];
		}
		searchParam.type = type_train;
		searchParam.status = status_use;
		this.store.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUpType.searchTrainParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    }
});
TrainBuildUpType.trainBuildUpGrid.getStore().on("beforeload",function(){	
	TrainBuildUpType.searchTrainParams.type = type_train;
	TrainBuildUpType.searchTrainParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUpType.searchTrainParams);
});
//移除侦听器
TrainBuildUpType.trainBuildUpGrid.un('rowdblclick', TrainBuildUpType.trainBuildUpGrid.toEditFn, TrainBuildUpType.trainBuildUpGrid);
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainBuildUpType.trainBuildUpGrid });
});