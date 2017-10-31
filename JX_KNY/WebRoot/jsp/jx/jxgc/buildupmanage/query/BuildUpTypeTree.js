/** 组成树界面*/
Ext.onReady(function(){
//定义命名空间-机车组成树
Ext.namespace("TrainBuild");

//设置结构位置信息编辑表单
TrainBuild.setPlaceForm = function(node){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					trainTypeIDX : node.attributes["trainTypeIDX"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	TrainPlaceBuildUp.structurePlaceForm.getForm().loadRecord(new Ext.data.Record(data));
	Ext.getCmp("ProfessionalType_form_SId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_SId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件、虚拟位置编辑表单
TrainBuild.setFixPlaceForm = function(node, form){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					//trainTypeIDX : node.attributes["trainTypeIDX"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	form.getForm().loadRecord(new Ext.data.Record(data));
}
//设置是否屏蔽
//参数：【机构位置信息】【配件位置信息】【虚拟位置信息】【下级结构位置】【下级安装位置】【可安装组成型号】【故障现象】
TrainBuild.isHideTab = function(structureTab,partsTab,virtualTab,childBuildTab,childFixTab,fixBuildTab,faultTab){
	if(structureTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("structurePlaceTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("structurePlaceTab");
	}
	if(partsTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("partsPlaceTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("partsPlaceTab");
	}
	if(virtualTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("virtualPlaceTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("virtualPlaceTab");
	}
	if(childBuildTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("childTrainBuildUpTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("childTrainBuildUpTab");
	}
	if(childFixTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("childTrainFixTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("childTrainFixTab");
	}
	if(fixBuildTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("fixBuildUpTypeTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("fixBuildUpTypeTab");
	}
	if(faultTab){
		TrainPlaceBuildUp.tabs.unhideTabStripItem("faultTab");
	}else{
		TrainPlaceBuildUp.tabs.hideTabStripItem("faultTab");
	}
}
//下级位置页面加载数据
TrainBuild.childlistLoad = function(grid,params){
	grid.getStore().baseParams = params;
	grid.getStore().load();
}
//故障现象页面加载数据
TrainBuild.faultlistLoad = function(fixPlaceIdx){
	
	//故障现象列表加载数据
	var whereList = [] ;
	whereList.push({propName:"buildUpPlaceIdx", propValue: fixPlaceIdx, stringLike: false}) ;
	PlaceFault.grid.getStore().baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	PlaceFault.grid.getStore().load();
}
//机车组成配置树
TrainBuild.tree = new Ext.tree.TreePanel( {
	tbar :new Ext.Toolbar(),
	plugins: ['multifilter'],
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/buildUpTypeQuery!allTreeForQuery.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: partsBuildUpTypeName,
        id: 'ROOT_0',
        leaf: false,
        icon: ctx + "/frame/resources/images/toolbar/train.gif"
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    collapsed:false,
    listeners: {
        beforeload: function(node,e) {
        	//展开根节点时处理
        	if(node == TrainBuild.tree.getRootNode()){
        		TrainBuild.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTreeForQuery.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}  
        	else if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
        		//展开【结构位置】和【无缺省安装组成、未安装配件的安装位置节点】
        		if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
        			TrainBuild.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTreeForQuery.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] ;
            		if(typeof(node.attributes["isVirtual"] != 'undefined')){
        				TrainBuild.tree.loader.dataUrl+= '&isVirtual=' + node.attributes["isVirtual"];
        				if(node.attributes["isVirtual"] == 'true'){
        					TrainBuild.tree.loader.dataUrl+= '&buildUpPlaceFullCode=' + node.getPath("buildUpPlaceCode").substring(1);
        				}
        			}
        		}
        		//展开【虚拟位置】、【有缺省安装组成型号的安装位置节点】、【安装了配件的安装位置节点】
        		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
        				&& typeof(node.attributes["partsBuildUpTypeIdx"]) != 'undefined'){
        			TrainBuild.tree.loader.dataUrl = ctx + '/buildUpTypeQuery!allTreeForQuery.action?parentIDX=' + node.attributes["partsBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["partsBuildUpTypeIdx"] ;
            		//展开【安装了配件的位置节点】
        			if(typeof(node.attributes["partsAccountIDX"]) != 'undefined'){
        				TrainBuild.tree.loader.dataUrl+= '&parentPartsAccountIdx=' + node.attributes["partsAccountIDX"];
        			}
        			//展开【虚拟位置节点】
        			if(typeof(node.attributes["isVirtual"] != 'undefined')){
        				TrainBuild.tree.loader.dataUrl+= '&isVirtual=' + node.attributes["isVirtual"];
        				if(node.attributes["isVirtual"] == 'true'){
        					TrainBuild.tree.loader.dataUrl+= '&buildUpPlaceFullCode=' + node.getPath("buildUpPlaceCode").substring(1);
        				}
        			}
        		}
        		
        	}
        },
        click: function(node,e){
        	nodeId = node.id;//点击节点id
        	var isRoot = (node == TrainBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){
        		parentIdx = "ROOT_0";
        		buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键
    		 	//下级结构位置列表加载数据
    			var params = {
	        		parentIdx : "ROOT_0",
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : structure_place
	        	};        			
    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainBuildUpGrid,params);
    			//下级安装位置列表加载数据
    			var params1 = {
	        		parentIdx : "ROOT_0",
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : fix_place + "," + virtual_place
	        	};        			
    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainFixGrid,params1);
    			
	             TrainBuild.isHideTab(false,false,false,true,true,false,false);
	             TrainPlaceBuildUp.tabs.activate("childTrainBuildUpTab");
        	}else{
        		//结构位置节点，显示【结构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;//点击节点id
        			parentNodeId = node.parentNode.id;//点击节点上级id
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			//设置结构位置信息编辑表单
        			TrainBuild.setPlaceForm(node);
        			//下级结构位置列表加载数据
        			var params = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : structure_place
		        	};        			
        			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainBuildUpGrid,params);
        			//下级安装位置列表加载数据
        			var params1 = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : fix_place + "," + virtual_place
		        	};		        		
        			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainFixGrid,params1);
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			TrainBuild.faultlistLoad(fixPlaceIdx);
        			TrainBuild.isHideTab(true,false,false,true,true,false,false);
        			TrainPlaceBuildUp.tabs.activate("structurePlaceTab");
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){        			
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置配件位置信息表单
        			TrainBuild.setFixPlaceForm(node, TrainPlaceBuildUp.partsPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			TrainBuild.faultlistLoad(fixPlaceIdx);
        			TrainBuild.isHideTab(false,true,false,false,false,true,false);
        			TrainPlaceBuildUp.tabs.activate("partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置虚拟位置信息表单
        			TrainBuild.setFixPlaceForm(node, TrainPlaceBuildUp.virtualPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			TrainBuild.faultlistLoad(fixPlaceIdx);
        			TrainBuild.isHideTab(false,false,true,false,false,true,false);
        			TrainPlaceBuildUp.tabs.activate("virtualPlaceTab");
        		}
        	}
        }
    }
});
//定义命名空间-配件组成树
Ext.namespace("PartsBuild");
//设置结构位置信息编辑表单
PartsBuild.setPlaceForm = function(node){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					trainTypeIDX : node.attributes["trainTypeIDX"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	PartsPlaceBuildUp.structurePlaceForm.getForm().loadRecord(new Ext.data.Record(data));
	Ext.getCmp("ProfessionalType_form_SId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_SId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件、虚拟位置编辑表单
PartsBuild.setFixPlaceForm = function(node, form){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					//trainTypeIDX : node.attributes["trainTypeIDX"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	form.getForm().loadRecord(new Ext.data.Record(data));
}
//设置是否屏蔽
//参数：【机构位置信息】【配件位置信息】【虚拟位置信息】【下级结构位置】【下级安装位置】【可安装组成型号】【故障现象】
PartsBuild.isHideTab = function(structureTab,partsTab,virtualTab,childBuildTab,childFixTab,fixBuildTab,faultTab){
	if(structureTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("structurePlaceTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("structurePlaceTab");
	}
	if(partsTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("partsPlaceTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("partsPlaceTab");
	}
	if(virtualTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("virtualPlaceTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("virtualPlaceTab");
	}
	if(childBuildTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("childPartsBuildUpTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("childPartsBuildUpTab");
	}
	if(childFixTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("childPartsFixTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("childPartsFixTab");
	}
	if(fixBuildTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("fixBuildUpTypeTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("fixBuildUpTypeTab");
	}
	if(faultTab){
		PartsPlaceBuildUp.tabs.unhideTabStripItem("faultTab");
	}else{
		PartsPlaceBuildUp.tabs.hideTabStripItem("faultTab");
	}
}
//故障现象页面加载数据
PartsBuild.faultlistLoad = function(fixPlaceIdx){
	//故障现象列表加载数据
	var searchParam = {};
	searchParam.buildUpPlaceIdx = fixPlaceIdx;	
	PlaceFault.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	PlaceFault.grid.getStore().load({
		params:{
			entityJson:Ext.util.JSON.encode(searchParam)
		}																
	});
}
//配件组成配置树
PartsBuild.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/buildUpType!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: partsBuildUpTypeName,
        id: partsBuildUpTypeIdx,
        leaf: false,
        icon: ctx + "/frame/resources/images/toolbar/train.gif"
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    collapsed:false,
    listeners: {
        beforeload: function(node,e) {
        	  
        	if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
        		//结构位置和安装位置节点
        		if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
        			PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] ;
        		}
        		//虚拟位置节点
        		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
        				&& typeof(node.attributes["virtualBuildUpTypeIdx"]) != 'undefined'){
        			PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["virtualBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["virtualBuildUpTypeIdx"] ;
        		}
        	}
        	//根节点
        	else{
        		PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}           
        },
        click: function(node,e){
        	nodeId = node.id;
        	var isRoot = (node == PartsBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){        		
        		parentIdx = partsBuildUpTypeIdx;
        		buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键
    		 	//下级结构位置列表加载数据
    			var params = {
	        		parentIdx : partsBuildUpTypeIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : structure_place
	        	};        			
    			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsBuildUpGrid,params);
    			//下级安装位置列表加载数据
    			var params1 = {
	        		parentIdx : partsBuildUpTypeIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : fix_place + "," + virtual_place
	        	};        			
    			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsFixGrid,params1);
    			//设置【选择安装位置列表】
				BuildUpPlace.loadGrid(BuildUpPlace.selectGrid,partsBuildUpTypeIdx);
	            PartsBuild.isHideTab(false,false,false,true,true,false,false);
	            PartsPlaceBuildUp.tabs.activate("childPartsBuildUpTab");
        	}else{
        		//结构位置节点，显示【机构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;
        			parentNodeId = node.parentNode.id;
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			//设置结构位置信息编辑表单
        			PartsBuild.setPlaceForm(node);
        			//下级结构位置列表加载数据
        			var params = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : structure_place
		        	};        			
        			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsBuildUpGrid,params);
        			//下级安装位置列表加载数据
        			var params1 = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : fix_place + "," + virtual_place
		        	};        			
        			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsFixGrid,params1);
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			PartsBuild.faultlistLoad(fixPlaceIdx);
        			PartsBuild.isHideTab(true,false,false,true,true,false,false);
        			PartsPlaceBuildUp.tabs.activate("structurePlaceTab");
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置配件位置信息表单
        			PartsBuild.setFixPlaceForm(node, PartsPlaceBuildUp.partsPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			PartsBuild.faultlistLoad(fixPlaceIdx);
        			PartsBuild.isHideTab(false,true,false,false,false,true,false);
        			PartsPlaceBuildUp.tabs.activate("partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置虚拟位置信息表单
        			PartsBuild.setFixPlaceForm(node, PartsPlaceBuildUp.virtualPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			PartsBuild.faultlistLoad(fixPlaceIdx);
        			PartsBuild.isHideTab(false,false,true,false,false,true,false);
        			PartsPlaceBuildUp.tabs.activate("virtualPlaceTab");
        		}
        	}
        }
    }
}); 
//新增删除位置后联动展开树节点
//参数：nodeId点击节点的id，parentIdx 上级位置id
PartsBuild.expandNode = function(nodeId, parentIdx, parentNodeId){
	if(parentIdx == partsBuildUpTypeIdx){
    	PartsBuild.tree.root.reload();
		PartsBuild.tree.getRootNode().expand();
    }else{
    	var node = PartsBuild.tree.getNodeById(nodeId);
    	var path = node.getPath('id');
	    PartsBuild.tree.getLoader().load(node,function(treeNode){  
	        //展开路径,并在回调函数里面选择该节点  
	        PartsBuild.tree.expandPath(path,'id',function(bSuccess,oLastNode){  
	              if(!bSuccess){
	              	return;				              	
	              }
				  //focus 节点，并选中节点！
				  oLastNode.ensureVisible();
				  oLastNode.select();
				  //oLastNode.fireEvent('click', oLastNode);  
	          }); 
	    },this); 
    }    
}
//定义命名空间-虚拟组成树
Ext.namespace("VirtualBuild");
//设置结构位置信息编辑表单
VirtualBuild.setPlaceForm = function(node){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					trainTypeIDX : node.attributes["trainTypeIDX"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	VirtualPlaceBuildUp.structurePlaceForm.getForm().loadRecord(new Ext.data.Record(data));
	Ext.getCmp("ProfessionalType_form_VSId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_VSId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件、虚拟位置编辑表单
VirtualBuild.setFixPlaceForm = function(node, form){
	var data = {
					idx : node.attributes["buildUpPlaceIdx"],
					buildUpPlaceCode : node.attributes["buildUpPlaceCode"],					
					chartNo : node.attributes["chartNo"],
					buildUpPlaceSEQ : node.attributes["buildUpPlaceSEQ"],
					buildUpPlaceName : node.attributes["text"],
					buildUpPlaceShortName : node.attributes["buildUpPlaceShortName"],
					professionalTypeName : node.attributes["professionalTypeName"],
					partName : node.attributes["partName"],
					//trainTypeIDX : node.attributes["trainTypeIDX"],
					buildUpPlaceFullName : node.attributes["buildUpPlaceFullName"]
			   };
	form.getForm().loadRecord(new Ext.data.Record(data));
}
//设置是否屏蔽
//参数：【机构位置信息】【配件位置信息】【虚拟位置信息】【下级结构位置】【下级安装位置】【可安装组成型号】【故障现象】
VirtualBuild.isHideTab = function(structureTab,partsTab,virtualTab,childBuildTab,childFixTab,fixBuildTab,faultTab){
	if(structureTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_structurePlaceTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_structurePlaceTab");
	}
	if(partsTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_partsPlaceTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_partsPlaceTab");
	}
	if(virtualTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_virtualPlaceTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_virtualPlaceTab");
	}
	if(childBuildTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_childTrainBuildUpTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_childTrainBuildUpTab");
	}
	if(childFixTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_childTrainFixTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_childTrainFixTab");
	}
	if(fixBuildTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_fixBuildUpTypeTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_fixBuildUpTypeTab");
	}
	if(faultTab){
		VirtualPlaceBuildUp.tabs.unhideTabStripItem("v_faultTab");
	}else{
		VirtualPlaceBuildUp.tabs.hideTabStripItem("v_faultTab");
	}
}
//故障现象页面加载数据
VirtualBuild.faultlistLoad = function(fixPlaceIdx){
	//故障现象列表加载数据
	var searchParam = {};
	searchParam.buildUpPlaceIdx = fixPlaceIdx;		
	PlaceFault.v_grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	PlaceFault.v_grid.getStore().load({
		params:{
			entityJson:Ext.util.JSON.encode(searchParam)
		}																
	});
}
//虚拟组成配置树
VirtualBuild.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/buildUpType!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: partsBuildUpTypeName,
        id: partsBuildUpTypeIdx,
        leaf: false,
        icon: ctx + "/frame/resources/images/toolbar/train.gif"
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    collapsed:false,
    listeners: {
        beforeload: function(node,e) {
        	
        	if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
        		//结构位置和安装位置节点
        		if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
        			VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] ;
        		}
        		//虚拟位置节点
        		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
        				&& typeof(node.attributes["virtualBuildUpTypeIdx"]) != 'undefined'){
        			VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["virtualBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["virtualBuildUpTypeIdx"] ;
        		}
        	}
        	//根节点
        	else{
        		VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}           
        },
        click: function(node,e){
        	nodeId = node.id;
        	var isRoot = (node == VirtualBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){        		
        		parentIdx = partsBuildUpTypeIdx;
        		buildUpTypeIdx = partsBuildUpTypeIdx;
    		 	//下级结构位置列表加载数据
    			var params = {
	        		parentIdx : partsBuildUpTypeIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : structure_place
	        	};        			
    			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainBuildUpGrid,params);
    			//下级安装位置列表加载数据
    			var params1 = {
	        		parentIdx : partsBuildUpTypeIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : fix_place + "," + virtual_place
	        	};        			
    			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainFixGrid,params1);
	            VirtualBuild.isHideTab(false,false,false,true,true,false,false);
	            VirtualPlaceBuildUp.tabs.activate("v_childTrainBuildUpTab");
        	}else{
        		//结构位置节点，显示【机构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;
        			parentNodeId = node.parentNode.id;
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];
        			//设置结构位置信息编辑表单
        			VirtualBuild.setPlaceForm(node);
        			//下级结构位置列表加载数据
        			var params = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : structure_place
		        	};        			
        			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainBuildUpGrid,params);
        			//下级安装位置列表加载数据
        			var params1 = {
		        		parentIdx : node.attributes["buildUpPlaceIdx"],
		        		buildUpTypeIDX : node.attributes["buildUpTypeIdx"],
		        		placeTypes : fix_place + "," + virtual_place
		        	};        			
        			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainFixGrid,params1);
        								
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
		        	//故障现象列表加载数据
        			VirtualBuild.faultlistLoad(fixPlaceIdx);
        			VirtualBuild.isHideTab(true,false,false,true,true,false,false);
        			VirtualPlaceBuildUp.tabs.activate("v_structurePlaceTab");
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置配件位置信息表单
        			VirtualBuild.setFixPlaceForm(node, VirtualPlaceBuildUp.partsPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});					
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			VirtualBuild.faultlistLoad(fixPlaceIdx);
        			VirtualBuild.isHideTab(false,true,false,false,false,true,false);
        			VirtualPlaceBuildUp.tabs.activate("v_partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];
        			//设置虚拟位置信息表单
        			VirtualBuild.setFixPlaceForm(node, VirtualPlaceBuildUp.virtualPlaceForm);
        			//设置可安装组成型号查看列表
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	//故障现象列表加载数据
        			VirtualBuild.faultlistLoad(fixPlaceIdx);
        			VirtualBuild.isHideTab(false,false,true,false,false,true,false);
        			VirtualPlaceBuildUp.tabs.activate("v_virtualPlaceTab");
        		}
        	}
        }
    }
}); 
//新增删除位置后联动展开树节点
//参数：nodeId点击节点的id，parentIdx 上级位置id
VirtualBuild.expandNode = function(nodeId, parentIdx){
	if(parentIdx == partsBuildUpTypeIdx){
		VirtualBuild.tree.root.reload();
		VirtualBuild.tree.getRootNode().expand();
	}else{
		var node = VirtualBuild.tree.getNodeById(nodeId);
		var path = node.getPath('id');
	    VirtualBuild.tree.getLoader().load(node,function(treeNode){  
	        //展开路径,并在回调函数里面选择该节点  
	        VirtualBuild.tree.expandPath(path,'id',function(bSuccess,oLastNode){  
	              if(!bSuccess){
	              	return;				              	
	              }
				  //focus 节点，并选中节点！
				  oLastNode.ensureVisible();
				  oLastNode.select();
				  //oLastNode.fireEvent('click', oLastNode);  
	          }); 
	    },this); 
	}
}
});