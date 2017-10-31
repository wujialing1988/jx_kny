Ext.onReady(function(){

	Ext.ns('TrainWorkPlanLeafNodeWin');                       //定义命名空间
	TrainWorkPlanLeafNodeWin.fieldWidth = 120;
	TrainWorkPlanLeafNodeWin.labelWidth = 80;
	TrainWorkPlanLeafNodeWin.node = {};
	TrainWorkPlanLeafNodeWin.idxValue = '';
	TrainWorkPlanLeafNodeWin.items = [{
		title: "下级节点", border: false, layout: "fit",
        items:[LeafNodeListGrid.grid]
	},{
	   	title: "流程节点", border: false, layout: "border",
	    items: [{
			region: 'center', layout: "fit",bodyStyle:'padding-left:20px;',
			 bodyBorder: false,
			items:[LeafNodeAndWorkCardEditWin.form], frame: true
			, buttonAlign : "center",
			buttons: [{
				id: "nodeSaveBtnEditWin", text: "保存", iconCls: "saveIcon", handler: function() {
					LeafNodeAndWorkCardEditWin.saveFn();
				}
			},{
				text: '取消', iconCls: 'closeIcon', handler: function() {
					this.findParentByType('window').hide();
				}
			}]
		},{
			region : 'south', height: 180, layout : 'fit', bodyBorder: false, items : [ NodeApplyDelayList.grid ]
		}]
	},{
        title: "作业工单", border: false, layout: "fit", //2
        items:[WorkCardEditWin.grid]
    },{
		title: "扩展配置", border: false, layout: "fit",
		items:[JobNodeExtConfigEditWin.form]
	},{
	   	title: "节点信息", border: false, layout: "border", buttonAlign : "center",
	    items: [{
			region: 'center', layout: "fit",bodyStyle:'padding-left:20px;',
				 bodyBorder: false,
				items:[JobProcessLeafNodeSearch.form], frame: true
			},{
				region : 'south', height: 180, layout : 'fit', bodyBorder: false, items : [ NodeApplyDelayList.gridSearch]
		}]
	},{
        title: "作业工单", border: false, layout: "fit", //5
        items:[WorkCardEditSearch.grid]
	},{
		title: "扩展配置", border: false, layout: "fit",
		items:[JobNodeExtConfigSearch.form]
    }];
	
	TrainWorkPlanLeafNodeWin.infoForm = new Ext.form.FormPanel({
		labelWidth:TrainWorkPlanLeafNodeWin.labelWidth, border: false,
		labelAlign:"left", layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
	        items: [
	        	{ fieldLabel:"车型车号", name:"trainTypeAndNo"}
	        ]
		},{
	        items: [
	        	{ fieldLabel:"修程修次", name:"xcxc"}
	        ]
		},{
	        items: [
	        	{ fieldLabel:"计划开始时间", name:"planBeginTime"}
	        ]
		},{
	        items: [
	        	{ fieldLabel:"计划完成时间", name:"planEndTime"}
	        ]
		}]
	});
	
	//树
	TrainWorkPlanLeafNodeWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jobProcessNodeQuery!getChildTree.action?parentIDX=" + TrainWorkPlanLeafNodeWin.node.idx
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: '',
	        id: TrainWorkPlanLeafNodeWin.node.idx,
	        leaf: false,
	        expanded :false,
	        status: TrainWorkPlanLeafNodeWin.status
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	listeners : {
			 beforeload: function(node, e){	 
            	TrainWorkPlanLeafNodeWin.tree.getLoader().dataUrl = ctx + '/jobProcessNodeQuery!getChildTree.action?parentIDX=' + node.id;
	        },
    		click: function(node, e) {
    		
				if(node.attributes.status == PROCESSTYPE){
					MyExt.Msg.alert("请选择非流程任务的节点！");
					return;			
				}
    			//完成状态
    			if(node.attributes.status == NODE_STATUS_COMPLETE || node.attributes.status == NODE_STATUS_STOP){
//				    Ext.getCmp("nodeSaveBtnEditWin").setVisible(false);  //  隐藏保存按键
    				if(node.leaf){
						var valueArray = [true,true,true,true,false,false,false];
						var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
						TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
						
						//加载工单
						WorkCardEditSearch.nodeCaseIDX = node.id;
						WorkCardEditSearch.trainWorkPlanEditWin = TrainWorkPlanLeafNodeWin;
						WorkCardEditSearch.grid.store.load();
						
						TrainWorkPlanLeafNodeWin.tabPanel.activate(5);
	    			}else{	    				
						var valueArray = [false,true,true,true,false,true,false];
						var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
						TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
//						TrainWorkPlanLeafNodeWin.items[4].items[1].setHeight(0); 
						//加载下级节点
						LeafNodeListGrid.nodeIDX = node.id;
						LeafNodeListGrid.workPlanStatus = TrainWorkPlanLeafNodeWin.workPlanStatus;
						LeafNodeListGrid.grid.store.load();
						TrainWorkPlanLeafNodeWin.tabPanel.activate(0);
	    			}
	    			//加载流程节点
	    			JobProcessLeafNodeSearch.idx = node.id;
    				LeafNodeAndWorkCardSearch.showEditWin();
	    			//隐藏上面调用编辑界面弹框
// 	    			LeafNodeAndWorkCardSearch.win.hide();
	    			NodeApplyDelayList.nodeIDX = node.id;
    				NodeApplyDelayList.gridSearch.store.load();
//	    			if(node.parentNode){
//						JobProcessNodeRelSearch.parentIDX = node.parentNode.id;
//	    			}
//					
					//NodeAndWorkCardEditWin.showEditWin();
//					NodeAndWorkCardEditSearch.showEditWin();
	    			
	    			//加载扩展配置
	    			JobNodeExtConfigSearch.trainWorkPlanEditWin = TrainWorkPlanLeafNodeWin;
					JobNodeExtConfigSearch.loadFn(node.id);
	    			TrainWorkPlanLeafNodeWin.tabPanel.show();
    			
    			}else{
    				if(node.leaf){
   					    				
						var valueArray = [true,false,false,false,true,true,true];
						var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
						TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
						
						//加载工单
						WorkCardEditWin.nodeCaseIDX = node.id;
						WorkCardEditWin.grid.store.load();
						//初始化自定义工单的节点ID
						TrainWork.nodeCaseIDX = node.id;
						
						TrainWorkPlanLeafNodeWin.tabPanel.activate(2);
	    			}else{	    				
						var valueArray = [false,true,true,true,true,true,false];
						var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
						TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
						
						//加载下级节点
						LeafNodeListGrid.nodeIDX = node.id;
						LeafNodeListGrid.workPlanStatus = TrainWorkPlanLeafNodeWin.workPlanStatus;
						LeafNodeListGrid.grid.store.load();
						TrainWorkPlanLeafNodeWin.tabPanel.activate(0);
	    			}
	    			// 加载流程节点
	    			LeafNodeAndWorkCardEditWin.idx = node.id;
	    			LeafNodeAndWorkCardEditWin.nodeIDX = node.id;
	    			LeafNodeAndWorkCardEditWin.showEditWin();
	    			// 加载申请列表 
	    			NodeApplyDelayList.nodeIDX = node.id;
	    			NodeApplyDelayList.grid.store.load();
	    			if(node.parentNode){
						JobProcessNodeRelEditWin.parentIDX = node.parentNode.id;
	    			}
	    			RepairProjectSelect.pTrainTypeIdx = workPlanEntity.trainTypeIDX;
					RepairProjectSelect.nodeCaseIDX = node.id;
	    			
	    			JobProcessNodeTreeWin.processName = workPlanEntity.processName;
					JobProcessNodeTreeWin.workPlanIDX = TrainWorkPlanLeafNodeWin.idxValue;
					JobProcessNodeTreeWin.nodeIDX = node.id;
				 			
	    			//加载扩展配置
					JobNodeExtConfigEditWin.loadFn(node.id);
	    			TrainWorkPlanLeafNodeWin.tabPanel.show();
    			}
	        }
		}
	});
	
	//中心中心panel
	TrainWorkPlanLeafNodeWin.tabPanel = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:TrainWorkPlanLeafNodeWin.items
    }); 
    
	 //中心panel
	TrainWorkPlanLeafNodeWin.centerPanel = new Ext.Panel({
		layout: "border",
		items: [{
			region: "west",
			width: 200,
			layout: "fit",
			items: TrainWorkPlanLeafNodeWin.tree
		},{
			region: "center",
			layout: "fit",
			items: TrainWorkPlanLeafNodeWin.tabPanel
		}]
	});
	
	TrainWorkPlanLeafNodeWin.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region: 'north', collapsible:true, collapsed: false,  height: 70, 
	        items:[TrainWorkPlanLeafNodeWin.infoForm], frame: true, title: "检修基本信息"
	    }
	    ,{
	        region : 'center', items: TrainWorkPlanLeafNodeWin.centerPanel
	    }
	    ]
	});
	TrainWorkPlanLeafNodeWin.win = new Ext.Window({
	    title:"车辆检修作业计划编辑", 
	    layout: 'fit',
		height: 600, width: 1000,
		maximized : true,
		closeAction: 'hide',
		items:TrainWorkPlanLeafNodeWin.panel
	});
	
	TrainWorkPlanLeafNodeWin.showWin = function(data,idx,parentIdx,isLeaf,nodeName,planEndTime,workPlanStatus,status) {
		TrainWorkPlanLeafNodeWin.node.idx = idx;
		TrainWorkPlanLeafNodeWin.node.parentIdx = parentIdx;
		TrainWorkPlanLeafNodeWin.node.isLeaf = isLeaf;
		TrainWorkPlanLeafNodeWin.node.nodeName = nodeName;
		LeafNodeAndWorkCardEditWin.parentPlanEndTime = planEndTime; // 设置父节点的结束时间
		var dataArray = data.split(",");
		workPlanIDX = dataArray[0];
		TrainWorkPlanLeafNodeWin.idxValue = workPlanIDX;
		TrainWorkPlanLeafNodeWin.workPlanStatus = workPlanStatus;
		TrainWorkPlanLeafNodeWin.status = status;
		LeafNodeListGrid.parentIDX = idx;
		LeafNodeListGrid.status = status;
		LeafNodeListGrid.workPlanIDX = workPlanIDX;
		if (!Ext.isEmpty(workPlanIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		                LeafNodeListGrid.processIDX = workPlanEntity.processIDX; // 流程idx
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
		

		var form = TrainWorkPlanLeafNodeWin.infoForm.getForm();		
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);	
		
		TrainWorkPlanLeafNodeWin.tree.getRootNode().setId(TrainWorkPlanLeafNodeWin.node.idx);
		TrainWorkPlanLeafNodeWin.tree.getRootNode().setText(TrainWorkPlanLeafNodeWin.node.nodeName);
		TrainWorkPlanLeafNodeWin.tree.getRootNode().attributes.status = TrainWorkPlanLeafNodeWin.status;
		
		//报错  根节点问题
		//TypeError: this.ctNode is undefined
		//this.ctNode.style.display = "none";
		//这个问题是由于该树的节点没有下级叶子，所以不能配置expanded :true（有下级叶子，展开）
		TrainWorkPlanLeafNodeWin.tree.root.reload();
		TrainWorkPlanLeafNodeWin.tree.getRootNode().expand();
		
		TrainWorkPlanLeafNodeWin.tabPanel.hide();
		
		//加载下级节点
		LeafNodeListGrid.nodeIDX = TrainWorkPlanLeafNodeWin.node.idx;
		LeafNodeListGrid.workPlanStatus = TrainWorkPlanLeafNodeWin.workPlanStatus;
		LeafNodeListGrid.grid.store.load();
		TrainWorkPlanLeafNodeWin.centerPanel.items.items[0].setWidth(200); // 展示节点下拉树
		TrainWorkPlanLeafNodeWin.tabPanel.show();		
		TrainWorkPlanLeafNodeWin.win.show();
		//写在show后面，show的方法中才会初始化tabpanel
	
		var valueArray = [false,true,true,true,true,true,true];
		var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
		TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
		TrainWorkPlanLeafNodeWin.tabPanel.activate(0);
		
	}
});

//设置tab是否可见
//TrainWorkPlanLeafNodeWin tabpanel隶属于的对象
//valueArray 放入是否显示，注意传入的值个数和tabpanel个数一致 var valueArray = [false,true,true,true,true,true,true];
function setTabPanelIsHidden(trainWorkPlanLeafNodeWin,valueArray){
	for(var a = 0;a < valueArray.length;a++){
		trainWorkPlanLeafNodeWin.tabPanel.getTabEl(a).hidden = valueArray[a];
	}
	return trainWorkPlanLeafNodeWin;
}
