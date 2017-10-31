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
	Ext.getCmp("ProfessionalType_form_SId").clearValue();
	Ext.getCmp("partID_form_SId").clearValue();
	Ext.getCmp("ProfessionalType_form_SId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_SId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件位置编辑表单
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
	Ext.getCmp("ProfessionalType_form_Id2").clearValue();
	Ext.getCmp("partID_formId2").clearValue();
	Ext.getCmp("ProfessionalType_form_Id2").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_formId2").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
}
//设置虚拟位置编辑表单
TrainBuild.setVirtualPlaceForm = function(node, form){
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
	Ext.getCmp("v_ProfessionalType_form_Id2").clearValue();
	Ext.getCmp("v_partID_formId2").clearValue();
	Ext.getCmp("v_ProfessionalType_form_Id2").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("v_partID_formId2").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
}
//设置是否屏蔽tab
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
//列表页面加载数据
TrainBuild.childlistLoad = function(grid,params){
	grid.getStore().baseParams = params;
	grid.getStore().load();
}
//故障现象页面加载数据--根据位置
TrainBuild.faultlistLoad = function(fixPlaceIdx){	
	//故障现象列表加载数据
	var whereList = [] ;
	whereList.push({propName:"buildUpPlaceIdx", propValue: fixPlaceIdx, stringLike: false}) ;
	PlaceFault.grid.getStore().baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	PlaceFault.grid.getStore().load();
}
//故障现象页面加载数据--根据组成
TrainBuild.faultlistLoadByBuild = function(buildUpTypeIdx){	
	//故障现象列表加载数据
	var whereList = [] ;
	whereList.push({propName:"buildUpTypeIdx", propValue: buildUpTypeIdx, stringLike: false}) ;
	PlaceFault.grid.getStore().baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	PlaceFault.grid.getStore().load();
}
//机车组成配置树
TrainBuild.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/buildUpType!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: partsBuildUpTypeName,
        id: rootParentIdx,
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
        	//根节点
        	if (node == TrainBuild.tree.getRootNode()){
        		TrainBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}
        	else if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
	        	//结构位置节点
	        	if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
	    			TrainBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
	    		}
	        	//虚拟位置节点
	    		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
	        				&& typeof(node.attributes["virtualBuildUpTypeIdx"]) != 'undefined'){
	    			TrainBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["virtualBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
	    		
	    		//安装位置节点
	    		else if(node.attributes["icon"] == ctx + "/jsp/jx/images/builduptree/place.png"){
	    			TrainBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
        	}
        	//组成节点
        	else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        		TrainBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["partsBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["partsBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
        	}
        },
        click: function(node,e){
        	nodeId = node.id;//点击节点id
        	var isRoot = (node == TrainBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){
        		parentIdx = rootParentIdx;//设置上级位置主键全局变量
        		buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键全局变量
        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
        		partsTypeIDX = "";//设置配件主键全局变量为空
    		 	//下级结构位置列表加载数据
    			var params = {
	        		parentIdx : rootParentIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : structure_place//位置类型：结构位置
	        	};        			
    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainBuildUpGrid,params);
    			//下级安装位置列表加载数据
    			var params1 = {
	        		parentIdx : rootParentIdx,
	        		buildUpTypeIDX : partsBuildUpTypeIdx,
	        		placeTypes : fix_place + "," + virtual_place//位置类型：配件位置+虚拟位置
	        	};        			
    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainFixGrid,params1);    			
    			TrainBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
    			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
                TrainBuild.isHideTab(false,false,false,true,true,false,true);
                TrainPlaceBuildUp.tabs.activate("childTrainBuildUpTab");
                Ext.getCmp("virtualPlace_button").setVisible(true);//显示【下级安装位置】列表上的【安装虚拟位置】按钮
                Ext.getCmp("selectPlace_Button").setVisible(true);//显示【下级安装位置】列表上的【选择安装位置】按钮
                
        	}else{
        		//结构位置节点，显示【结构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;//点击节点id
        			parentNodeId = node.parentNode.id;//点击节点上级id
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			TrainBuild.setPlaceForm(node);//设置【结构位置信息】编辑表单
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
        			TrainBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据--根据位置
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			TrainBuild.isHideTab(true,false,false,true,true,false,true);
        			TrainPlaceBuildUp.tabs.activate("structurePlaceTab");
        			//根据此位置是否是配件组成下位置显示或屏蔽【新增虚拟位置】按钮和【选择安装位置】按钮，配件组成下位置（partsTypeIDX不为空）屏蔽【新增虚拟位置】按钮及【选择安装位置】按钮
        			if(partsTypeIDX == '' || partsTypeIDX == null || partsTypeIDX == 'null'){
        				Ext.getCmp("virtualPlace_button").setVisible(true);
        				Ext.getCmp("selectPlace_Button").setVisible(true);        				
        			}else{
        				Ext.getCmp("virtualPlace_button").setVisible(false);
        				Ext.getCmp("selectPlace_Button").setVisible(false);
        			}
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){        			
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_parts;//配件组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			TrainBuild.setFixPlaceForm(node, TrainPlaceBuildUp.partsPlaceForm);//设置配件位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});		        	
        			TrainBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			TrainBuild.isHideTab(false,true,false,false,false,true,true);
        			TrainPlaceBuildUp.tabs.activate("partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_virtual;//虚拟组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			TrainBuild.setVirtualPlaceForm(node, TrainPlaceBuildUp.virtualPlaceForm);//设置虚拟位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});		        	
        			TrainBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			TrainBuild.isHideTab(false,false,true,false,false,true,true);
        			TrainPlaceBuildUp.tabs.activate("virtualPlaceTab");
        		}
        		//配件或虚拟组成节点
        		else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        			parentIdx = node.attributes["partsBuildUpTypeIdx"];//设置上级位置主键为组成主键
	        		buildUpTypeIdx = node.attributes["partsBuildUpTypeIdx"];//设置组成主键
	        		partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
	        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
	    		 	//下级结构位置列表加载数据
	    			var params = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : structure_place
		        	};        			
	    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainBuildUpGrid,params);
	    			//下级安装位置列表加载数据
	    			var params1 = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : fix_place + "," + virtual_place
		        	};        			
	    			TrainBuild.childlistLoad(TrainPlaceBuildUp.childTrainFixGrid,params1);	    			
    				TrainBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据
    				PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
		            TrainBuild.isHideTab(false,false,false,true,true,false,true);
		            TrainPlaceBuildUp.tabs.activate("childTrainBuildUpTab");
		            //根据此位置是否是配件组成下位置显示或屏蔽【新增虚拟位置】按钮和【选择安装位置】按钮，配件组成下位置（partsTypeIDX不为空）屏蔽【新增虚拟位置】按钮及【选择安装位置】按钮
        			if(partsTypeIDX == '' || partsTypeIDX == null || partsTypeIDX == 'null'){
        				Ext.getCmp("virtualPlace_button").setVisible(true);
        				Ext.getCmp("selectPlace_Button").setVisible(true);        				
        			}else{
        				Ext.getCmp("virtualPlace_button").setVisible(false);
        				Ext.getCmp("selectPlace_Button").setVisible(false);
        			}
        		}
        	}
        }
    }
}); 
//新增删除位置后联动展开树节点
//参数：nodeId点击节点的id，parentIdx 上级位置id
TrainBuild.expandNode = function(nodeId, parentIdx){
//	if(parentIdx == rootParentIdx){
//    	TrainBuild.tree.root.reload();
//		TrainBuild.tree.getRootNode().expand();
//    }else{
//    	var node = TrainBuild.tree.getNodeById(nodeId);
//    	var path = node.getPath('id');
//	    TrainBuild.tree.getLoader().load(node,function(treeNode){  
//	        //展开路径,并在回调函数里面选择该节点  
//	        TrainBuild.tree.expandPath(path,'id',function(bSuccess,oLastNode){  
//	              if(!bSuccess){
//	              	return;				              	
//	              }
//				  //focus 节点，并选中节点！
//				  oLastNode.ensureVisible();
//				  oLastNode.select();  
//	          }); 
//	    },this); 
//    }    
	TrainBuild.tree.root.reload();
	TrainBuild.tree.getRootNode().expand();
}
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
	Ext.getCmp("ProfessionalType_form_SId").clearValue();
	Ext.getCmp("partID_form_SId").clearValue();
	Ext.getCmp("ProfessionalType_form_SId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_SId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件位置编辑表单
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
	Ext.getCmp("ProfessionalType_form_Id2").clearValue();
	Ext.getCmp("partID_formId2").clearValue();
	Ext.getCmp("ProfessionalType_form_Id2").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_formId2").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
}
//设置虚拟位置编辑表单
PartsBuild.setVirtualPlaceForm = function(node, form){
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
	Ext.getCmp("v_ProfessionalType_form_Id2").clearValue();
	Ext.getCmp("v_partID_formId2").clearValue();
	Ext.getCmp("v_ProfessionalType_form_Id2").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("v_partID_formId2").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
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
//故障现象页面加载数据--根据位置
PartsBuild.faultlistLoad = function(fixPlaceIdx){
	//故障现象列表加载数据
	var whereList = [] ;
	whereList.push({propName:"buildUpPlaceIdx", propValue: fixPlaceIdx, stringLike: false}) ;
	PlaceFault.grid.getStore().baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	PlaceFault.grid.getStore().load();
}
//故障现象页面加载数据--根据组成
PartsBuild.faultlistLoadByBuild = function(buildUpTypeIdx){	
	//故障现象列表加载数据//故障现象列表加载数据
	var whereList = [] ;
	whereList.push({propName:"buildUpTypeIdx", propValue: buildUpTypeIdx, stringLike: false}) ;
	PlaceFault.grid.getStore().baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	PlaceFault.grid.getStore().load();
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
        	//根节点
        	if (node == PartsBuild.tree.getRootNode()){
        		PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}
        	else if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
	        	//结构位置节点
	        	if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
	    			PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
	    		}
	        	//虚拟位置节点
	    		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
	        				&& typeof(node.attributes["virtualBuildUpTypeIdx"]) != 'undefined'){
	    			PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["virtualBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
	    		
	    		//安装位置节点
	    		else if(node.attributes["icon"] == ctx + "/jsp/jx/images/builduptree/place.png"){
	    			PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
        	}
        	//组成节点
        	else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        		PartsBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["partsBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["partsBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
        	}
        },
        click: function(node,e){
        	nodeId = node.id;//点击节点id
        	var isRoot = (node == PartsBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){        		
        		parentIdx = partsBuildUpTypeIdx;//设置上级位置主键全局变量
        		buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键全局变量
        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
        		partsTypeIDX = "";//设置配件主键全局变量为空
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
				//BuildUpPlace.loadGrid(BuildUpPlace.selectGrid,partsBuildUpTypeIdx);
    			PartsBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
    			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
	            PartsBuild.isHideTab(false,false,false,true,true,false,true);
	            PartsPlaceBuildUp.tabs.activate("childPartsBuildUpTab");
	            Ext.getCmp("virtualPlace_button").setVisible(false);//屏蔽【下级安装位置】列表上的【安装虚拟位置】按钮
        	}else{
        		//结构位置节点，显示【机构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;//点击节点id
        			parentNodeId = node.parentNode.id;//点击节点上级id
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量         			
        			PartsBuild.setPlaceForm(node);//设置结构位置信息编辑表单
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
        			PartsBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据--根据位置        			
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			PartsBuild.isHideTab(true,false,false,true,true,false,true);
        			PartsPlaceBuildUp.tabs.activate("structurePlaceTab");
        			Ext.getCmp("virtualPlace_button").setVisible(false);//屏蔽【新增虚拟位置】按钮
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_parts;//配件组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			PartsBuild.setFixPlaceForm(node, PartsPlaceBuildUp.partsPlaceForm);//设置配件位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
        			PartsBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据        			
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			PartsBuild.isHideTab(false,true,false,false,false,true,true);
        			PartsPlaceBuildUp.tabs.activate("partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_virtual;//虚拟组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			PartsBuild.setVirtualPlaceForm(node, PartsPlaceBuildUp.virtualPlaceForm);//设置虚拟位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});		        	
        			PartsBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
        			PartsBuild.isHideTab(false,false,true,false,false,true,true);
        			PartsPlaceBuildUp.tabs.activate("virtualPlaceTab");
        		}
        		//配件组成节点
        		else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        			parentIdx = node.attributes["partsBuildUpTypeIdx"];//设置上级位置主键为组成主键
	        		buildUpTypeIdx = node.attributes["partsBuildUpTypeIdx"];//设置组成主键
	        		partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
	        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
	    		 	//下级结构位置列表加载数据
	    			var params = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : structure_place
		        	};        			
	    			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsBuildUpGrid,params);
	    			//下级安装位置列表加载数据
	    			var params1 = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : fix_place + "," + virtual_place
		        	};        			
	    			TrainBuild.childlistLoad(PartsPlaceBuildUp.childPartsFixGrid,params1);	    			
    				PartsBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据
    				PlaceFaultMethod.grid.store.removeAll();//清空故障现象处理方法列表
		            PartsBuild.isHideTab(false,false,false,true,true,false,true);
		            PartsPlaceBuildUp.tabs.activate("childPartsBuildUpTab");		            
		            Ext.getCmp("virtualPlace_button").setVisible(false);//屏蔽【下级安装位置】列表上的【新增虚拟位置】按钮
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
	Ext.getCmp("ProfessionalType_form_VSId").clearValue();
	Ext.getCmp("partID_form_VSId").clearValue();
	Ext.getCmp("ProfessionalType_form_VSId").setDisplayValue(node.attributes["professionalTypeIDX"],node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_form_VSId").setDisplayValue(node.attributes["partID"],node.attributes["partName"]);
}
//设置配件位置编辑表单
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
	Ext.getCmp("ProfessionalType_form_Id12").clearValue();
	Ext.getCmp("partID_formId12").clearValue();
	Ext.getCmp("ProfessionalType_form_Id12").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("partID_formId12").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
}
//设置虚拟位置编辑表单
VirtualBuild.setVirtualPlaceForm = function(node, form){
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
	Ext.getCmp("v_ProfessionalType_form_Id12").clearValue();
	Ext.getCmp("v_partID_formId12").clearValue();
	Ext.getCmp("v_ProfessionalType_form_Id12").setDisplayValue(node.attributes["professionalTypeIDX"], node.attributes["professionalTypeName"]);
	Ext.getCmp("v_partID_formId12").setDisplayValue(node.attributes["partID"], node.attributes["partName"]);
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
//故障现象页面加载数据--根据位置
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
//故障现象页面加载数据--根据组成
VirtualBuild.faultlistLoadByBuild = function(buildUpTypeIdx){	
	//故障现象列表加载数据
	var searchParam = {};
	searchParam.buildUpTypeIdx = buildUpTypeIdx;
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
        	//根节点
        	if (node == VirtualBuild.tree.getRootNode()){
        		VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.id  + 
            			'&partsBuildUpTypeIdx=' + partsBuildUpTypeIdx ;
        	}
        	else if(typeof(node.attributes["buildUpTypeIdx"]) != 'undefined'){
	        	//结构位置节点
	        	if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'false'){
	    			VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
	    		}
	        	//虚拟位置节点
	    		else if(typeof(node.attributes["isPartsBuildUp"]) != 'undefined' && node.attributes["isPartsBuildUp"] == 'true'
	        				&& typeof(node.attributes["virtualBuildUpTypeIdx"]) != 'undefined'){
	    			VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["virtualBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
	    		
	    		//安装位置节点
	    		else if(node.attributes["icon"] == ctx + "/jsp/jx/images/builduptree/place.png"){
	    			VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["buildUpPlaceIdx"] + 
	        			'&partsBuildUpTypeIdx=' + node.attributes["buildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"] ;
	    		}
        	}
        	//组成节点
        	else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        		VirtualBuild.tree.loader.dataUrl = ctx + '/buildUpType!tree.action?parentIDX=' + node.attributes["partsBuildUpTypeIdx"] + 
            			'&partsBuildUpTypeIdx=' + node.attributes["partsBuildUpTypeIdx"] + '&isPartsBuildUp=' + node.attributes["isPartsBuildUp"];
        	}
        },
        click: function(node,e){
        	nodeId = node.id;
        	var isRoot = (node == VirtualBuild.tree.getRootNode());
        	//根节点，显示【下级结构位置】【下级安装位置】【故障现象】，屏蔽【机构位置信息】【配件位置信息】【虚拟位置信息】【可安装组成型号】
        	if(isRoot){        		
        		parentIdx = partsBuildUpTypeIdx;//设置上级位置主键全局变量
        		buildUpTypeIdx = partsBuildUpTypeIdx;//设置组成主键全局变量
        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
        		partsTypeIDX = "";//设置配件主键全局变量为空
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
    			VirtualBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据--根据组成
    			PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
	            VirtualBuild.isHideTab(false,false,false,true,true,false,true);
	            VirtualPlaceBuildUp.tabs.activate("v_childTrainBuildUpTab");
	            Ext.getCmp("virtualPlace_button2").setVisible(true);//显示【下级安装位置】列表上的【安装虚拟位置】按钮
	            Ext.getCmp("selectPlace_Button2").setVisible(true);//屏蔽【下级安装位置】列表上的【选择安装位置】按钮
        	}else{
        		//结构位置节点，显示【机构位置信息】【下级结构位置】【下级安装位置】【故障现象】，屏蔽【配件位置信息】【虚拟位置信息】【可安装组成型号】
        		if(node.attributes["placeType"] == structure_place){
        			nodeId = node.id;//点击节点id
        			parentNodeId = node.parentNode.id;//点击节点上级id
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			VirtualBuild.setPlaceForm(node);//设置结构位置信息编辑表单
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
        			VirtualBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
        			VirtualBuild.isHideTab(true,false,false,true,true,false,true);
        			VirtualPlaceBuildUp.tabs.activate("v_structurePlaceTab");
        			//根据此位置是否是配件组成下位置显示或屏蔽【新增虚拟位置】按钮和【选择安装位置】按钮，配件组成下位置（partsTypeIDX不为空）屏蔽【新增虚拟位置】按钮及【选择安装位置】按钮
        			if(partsTypeIDX == '' || partsTypeIDX == null || partsTypeIDX == 'null'){
        				Ext.getCmp("virtualPlace_button2").setVisible(true);
        				Ext.getCmp("selectPlace_Button2").setVisible(true);
        			}else{
        				Ext.getCmp("virtualPlace_button2").setVisible(false);
        				Ext.getCmp("selectPlace_Button2").setVisible(false);
        			}
        		}
        		//安装位置节点，显示【配件位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【虚拟位置信息】
        		else if(node.attributes["placeType"] == fix_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_parts;//配件组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量        			
        			VirtualBuild.setFixPlaceForm(node, VirtualPlaceBuildUp.partsPlaceForm);//设置配件位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});		        	
        			VirtualBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
        			VirtualBuild.isHideTab(false,true,false,false,false,true,true);
        			VirtualPlaceBuildUp.tabs.activate("v_partsPlaceTab");
        		}
        		//虚拟位置节点，显示【虚拟位置信息】【可安装组成型号】【故障现象】，屏蔽【机构位置信息】【下级结构位置】【下级安装位置】【配件位置信息】
        		else if(node.attributes["placeType"] == virtual_place){
        			parentIdx = node.attributes["buildUpPlaceIdx"];//点击节点位置id
        			partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
        			buildUpTypeIdx = node.attributes["buildUpTypeIdx"];//设置组成主键
        			type = type_virtual;//虚拟组成类型
        			fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量         			
        			VirtualBuild.setVirtualPlaceForm(node, VirtualPlaceBuildUp.virtualPlaceForm);//设置虚拟位置信息表单
        			//设置可安装组成型号列表
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().baseParams.fixPlaceIdx = node.attributes["buildUpPlaceIdx"];
        			FixBuildUpType.v_fixBuildUpTypeQueryGrid.getStore().load({
						params: {fixPlaceIdx : node.attributes["buildUpPlaceIdx"]} 
					});
					fixPlaceIdx = node.attributes["buildUpPlaceIdx"];//设置安装位置主键全局变量
		        	VirtualBuild.faultlistLoad(fixPlaceIdx);//故障现象列表加载数据
        			PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
        			VirtualBuild.isHideTab(false,false,true,false,false,true,true);
        			VirtualPlaceBuildUp.tabs.activate("v_virtualPlaceTab");
        		}
        		//配件或虚拟组成节点
        		else if(node.attributes["icon"] == ctx + "/frame/resources/images/toolbar/train.gif"){
        			parentIdx = node.attributes["partsBuildUpTypeIdx"];//设置上级位置主键为组成主键
	        		buildUpTypeIdx = node.attributes["partsBuildUpTypeIdx"];//设置组成主键
	        		partsTypeIDX = node.attributes["partsTypeIDX"];//设置配件组成的配件主键
	        		fixPlaceIdx = "";//设置安装位置主键全局变量为空（为保存故障现象时不保存位置主键）
	    		 	//下级结构位置列表加载数据
	    			var params = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : structure_place
		        	};        			
	    			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainBuildUpGrid,params);
	    			//下级安装位置列表加载数据
	    			var params1 = {
		        		parentIdx : parentIdx,
		        		buildUpTypeIDX : buildUpTypeIdx,
		        		placeTypes : fix_place + "," + virtual_place
		        	};        			
	    			TrainBuild.childlistLoad(VirtualPlaceBuildUp.childTrainFixGrid,params1);	    			
    				VirtualBuild.faultlistLoadByBuild(buildUpTypeIdx);//故障现象列表加载数据
    				PlaceFaultMethod.v_grid.store.removeAll();//清空故障现象处理方法列表
		            VirtualBuild.isHideTab(false,false,false,true,true,false,true);
		            VirtualPlaceBuildUp.tabs.activate("childTrainBuildUpTab");
		            
		            //根据此位置是否是配件组成下位置显示或屏蔽【新增虚拟位置】按钮和【选择安装位置】按钮，配件组成下位置（partsTypeIDX不为空）屏蔽【新增虚拟位置】按钮及【选择安装位置】按钮
        			if(partsTypeIDX == '' || partsTypeIDX == null || partsTypeIDX == 'null'){
        				Ext.getCmp("virtualPlace_button2").setVisible(true);
        				Ext.getCmp("selectPlace_Button2").setVisible(true);
        			}else{
        				Ext.getCmp("virtualPlace_button2").setVisible(false);
        				Ext.getCmp("selectPlace_Button2").setVisible(false);
        			}
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