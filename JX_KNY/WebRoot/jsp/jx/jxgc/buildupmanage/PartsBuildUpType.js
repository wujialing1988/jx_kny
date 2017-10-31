/**已作废*/
Ext.onReady(function(){
Ext.namespace('PartsBuildUpType');                       //定义命名空间
//配件组成查询集合对象
PartsBuildUpType.searchPartsParams = {};
//配件组成列表
PartsBuildUpType.PartsBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    searchFormColNum: 2,
    singleSelect: true,
    tbar: ['search',{
    	text: '操作',
    	iconCls: 'editIcon',
    	handler: function(){
    		//未选择记录，直接返回
        	if(!$yd.isSelectedRecord(PartsBuildUpType.PartsBuildUpGrid)) return;
        	//设置初始配件组成型号信息
        	var data = PartsBuildUpType.PartsBuildUpGrid.selModel.getSelections();
        	var array = {idx: data[0].get("idx"),buildUpTypeCode: data[0].get("buildUpTypeCode"),
    						buildUpTypeName: data[0].get("buildUpTypeName")};
    		//设置树根节点id
			partsBuildUpTypeIdx = data[0].get("idx") != null && data[0].get("idx") != 'null'? data[0].get("idx") : "";
			partsBuildUpTypeName =  data[0].get("buildUpTypeName") != null && data[0].get("buildUpTypeName") != 'null'? data[0].get("buildUpTypeName"): "";
			//打开窗口时重新刷新树
			PartsBuild.tree.root.setId(data[0].get("idx"));
			nodeId = data[0].get("idx");
			PartsBuild.tree.root.setText(partsBuildUpTypeName);
        	PartsBuild.tree.root.reload();
            PartsBuild.tree.getRootNode().expand();
            //获取下级位置列表
            UpdatePartsBuild.childPartsBuildUpGrid.getStore().baseParams = {
        		parentIdx : partsBuildUpTypeIdx,
            	partsBuildUpTypeIdx : partsBuildUpTypeIdx
        	};
        	UpdatePartsBuild.childPartsBuildUpGrid.getStore().load();
            //设置下级安装位置列表的新增form的上级安装位置id及所属组成型号id
            UpdatePartsBuild.childPartsBuildUpGrid.defaultData = {parentIdx: data[0].get("idx"), partsBuildUpTypeIdx: data[0].get("idx") };
            
    		UpdatePartsBuild.win.show(); 
    		UpdatePartsBuild.tabs.hideTabStripItem("PartsBuildUpTab");
    		UpdatePartsBuild.tabs.hideTabStripItem("fixBuildUpTypeTab");
    		UpdatePartsBuild.tabs.unhideTabStripItem("childPartsBuildUpTab");
			UpdatePartsBuild.tabs.activate("childPartsBuildUpTab");
    	}
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'配件名称', dataIndex:'partsName', 
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'规格型号', dataIndex:'specificationModel', 
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
	//查询方法
	searchFn: function(searchParam){
		PartsBuildUpType.searchPartsParams.buildUpTypeCode = '';
		PartsBuildUpType.searchPartsParams.buildUpTypeName = '';
		PartsBuildUpType.searchPartsParams.partsName = '';
		PartsBuildUpType.searchPartsParams.specificationModel = '';
		for(prop in searchParam){
	    	PartsBuildUpType.searchPartsParams[prop] = searchParam[prop];
		}
		searchParam.type = type_parts;
		searchParam.status = status_use;
		this.store.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUpType.searchPartsParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    }
});
PartsBuildUpType.PartsBuildUpGrid.getStore().on("beforeload",function(){
	PartsBuildUpType.searchPartsParams.type = type_parts;
	PartsBuildUpType.searchPartsParams.status = status_use;
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUpType.searchPartsParams);
});
//移除侦听器
PartsBuildUpType.PartsBuildUpGrid.un('rowdblclick', PartsBuildUpType.PartsBuildUpGrid.toEditFn, PartsBuildUpType.PartsBuildUpGrid);
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsBuildUpType.PartsBuildUpGrid });
});