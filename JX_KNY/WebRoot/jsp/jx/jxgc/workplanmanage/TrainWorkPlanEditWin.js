Ext.onReady(function(){

	Ext.namespace('TrainWorkPlanEditWin');                       //定义命名空间
	TrainWorkPlanEditWin.fieldWidth = 120;
	TrainWorkPlanEditWin.labelWidth = 80;
	TrainWorkPlanEditWin.node = {};
	TrainWorkPlanEditWin.idxValue = '';
	TrainWorkPlanEditWin.items = [{
		title: "下级节点", border: false, layout: "fit",
        items:[ChildNodeListGrid.grid]
	},{
	   	title: "流程节点", border: false, layout: "border", buttonAlign : "center",
	    items: [{
			region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
				height: 200, bodyBorder: false,
				items:[JobProcessNodeEditWin.form], frame: true
			},{
				region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRelEditWin.grid ]
		}]
	},{
        title: "作业工单", border: false, layout: "fit", //2
        items:[WorkCardEditWin.grid]
    },{
		title: "扩展配置", border: false, layout: "fit",
		items:[JobNodeExtConfigEditWin.form]
	},{
	   	title: "流程节点", border: false, layout: "border", buttonAlign : "center",
	    items: [{
			region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
				height: 200, bodyBorder: false,
				items:[JobProcessNodeSearch.form], frame: true
			},{
				region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRelSearch.grid ]
		}]
	},{
        title: "作业工单", border: false, layout: "fit", //5
        items:[WorkCardEditSearch.grid]
	},{
		title: "扩展配置", border: false, layout: "fit",
		items:[JobNodeExtConfigSearch.form]
    }];
	
	TrainWorkPlanEditWin.infoForm = new Ext.form.FormPanel({
		labelWidth:TrainWorkPlanEditWin.labelWidth, border: false,
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
	TrainWorkPlanEditWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jobProcessNodeQuery!getChildTree.action?parentIDX=" + TrainWorkPlanEditWin.node.idx
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: '',
	        id: TrainWorkPlanEditWin.node.idx,
	        leaf: false,
	        expanded :false
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	listeners : {
			 beforeload: function(node, e){	 
            	TrainWorkPlanEditWin.tree.getLoader().dataUrl = ctx + '/jobProcessNodeQuery!getChildTree.action?parentIDX=' + node.id;
	        },
    		click: function(node, e) {
    		
				if(node.attributes.status == PROCESSTYPE){
					MyExt.Msg.alert("请选择非流程任务的节点！");
					return;			
				}
    			//完成状态
    			if(node.attributes.status == NODE_STATUS_COMPLETE || node.attributes.status == NODE_STATUS_STOP){
    				if(node.leaf){
    					/*
						TrainWorkPlanEditWin.tabPanel.getTabEl(0).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(1).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(2).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(3).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(4).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(5).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(6).hidden = false;
						*/
						var valueArray = [true,true,true,true,false,false,false];
						var trainWorkPlanEditWin = setTabPanelIsHidden(TrainWorkPlanEditWin,valueArray);
						TrainWorkPlanEditWin = trainWorkPlanEditWin;
						
						//加载工单
						WorkCardEditSearch.nodeCaseIDX = node.id;
						WorkCardEditSearch.trainWorkPlanEditWin = TrainWorkPlanEditWin;
						WorkCardEditSearch.grid.store.load();
						
						TrainWorkPlanEditWin.tabPanel.activate(5);
	    			}else{
	    				
	    				/*
						TrainWorkPlanEditWin.tabPanel.getTabEl(0).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(1).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(2).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(3).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(4).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(5).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(6).hidden = false;
						*/
						
						var valueArray = [false,true,true,true,false,true,false];
						var trainWorkPlanEditWin = setTabPanelIsHidden(TrainWorkPlanEditWin,valueArray);
						TrainWorkPlanEditWin = trainWorkPlanEditWin;
						
						//加载下级节点
						ChildNodeListGrid.nodeIDX = node.id;
						ChildNodeListGrid.workPlanStatus = TrainWorkPlanEditWin.workPlanStatus;
						ChildNodeListGrid.grid.store.load();
						TrainWorkPlanEditWin.tabPanel.activate(0);
	    			}
	    			//加载流程节点
	    			JobProcessNodeSearch.idx = node.id;
	    			NodeAndWorkCardEditSearch.showEditWin();
	    			//隐藏上面调用编辑界面弹框
	    			NodeAndWorkCardEditSearch.win.hide();
	    			JobProcessNodeRelSearch.nodeIDX = node.id;
	    			if(node.parentNode){
						JobProcessNodeRelSearch.parentIDX = node.parentNode.id;
	    			}
					
					//NodeAndWorkCardEditWin.showEditWin();
					//NodeAndWorkCardEditSearch.showEditWin();
	    			
	    			//加载扩展配置
	    			JobNodeExtConfigSearch.trainWorkPlanEditWin = TrainWorkPlanEditWin;
					JobNodeExtConfigSearch.loadFn(node.id);
	    			TrainWorkPlanEditWin.tabPanel.show();
    			
    			}else{
    				if(node.leaf){
    					
    					/*
						TrainWorkPlanEditWin.tabPanel.getTabEl(0).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(1).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(2).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(3).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(4).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(5).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(6).hidden = true;
						*/
						var valueArray = [true,false,false,false,true,true,true];
						var trainWorkPlanEditWin = setTabPanelIsHidden(TrainWorkPlanEditWin,valueArray);
						TrainWorkPlanEditWin = trainWorkPlanEditWin;
						
						//加载工单
						WorkCardEditWin.nodeCaseIDX = node.id;
						WorkCardEditWin.grid.store.load();
						//初始化自定义工单的节点ID
						TrainWork.nodeCaseIDX = node.id;
						
						TrainWorkPlanEditWin.tabPanel.activate(2);
	    			}else{
	    				/*
						TrainWorkPlanEditWin.tabPanel.getTabEl(0).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(1).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(2).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(3).hidden = false;
						TrainWorkPlanEditWin.tabPanel.getTabEl(4).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(5).hidden = true;
						TrainWorkPlanEditWin.tabPanel.getTabEl(6).hidden = true;
						*/
						
						var valueArray = [false,false,true,false,true,true,true];
						var trainWorkPlanEditWin = setTabPanelIsHidden(TrainWorkPlanEditWin,valueArray);
						TrainWorkPlanEditWin = trainWorkPlanEditWin;
						
						//加载下级节点
						ChildNodeListGrid.nodeIDX = node.id;
						ChildNodeListGrid.workPlanStatus = TrainWorkPlanEditWin.workPlanStatus;
						ChildNodeListGrid.grid.store.load();
						TrainWorkPlanEditWin.tabPanel.activate(0);
	    			}
	    			//加载流程节点
	    			JobProcessNodeEditWin.idx = node.id;
	    			JobProcessNodeRelEditWin.nodeIDX = node.id;
	    			if(node.parentNode){
						JobProcessNodeRelEditWin.parentIDX = node.parentNode.id;
	    			}
	    			RepairProjectSelect.pTrainTypeIdx = workPlanEntity.trainTypeIDX;
					RepairProjectSelect.nodeCaseIDX = node.id;
	    			
	    			JobProcessNodeTreeWin.processName = workPlanEntity.processName;
					JobProcessNodeTreeWin.workPlanIDX = TrainWorkPlanEditWin.idxValue;
					JobProcessNodeTreeWin.nodeIDX = node.id;
					
					NodeAndWorkCardEditWin.showEditWin();
	    			
	    			//加载扩展配置
					JobNodeExtConfigEditWin.loadFn(node.id);
	    			TrainWorkPlanEditWin.tabPanel.show();
    			}
	        }
		}
	});
	
	//中心中心panel
	TrainWorkPlanEditWin.tabPanel = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:TrainWorkPlanEditWin.items
    }); 
    
	 //中心panel
	TrainWorkPlanEditWin.centerPanel = new Ext.Panel({
		layout: "border",
		items: [{
			region: "west",
			width: 300,
			layout: "fit",
			items: TrainWorkPlanEditWin.tree
		},{
			region: "center",
			layout: "fit",
			items: TrainWorkPlanEditWin.tabPanel
		}]
	});
	
	TrainWorkPlanEditWin.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region: 'north', collapsible:true, collapsed: false,  height: 70, 
	        items:[TrainWorkPlanEditWin.infoForm], frame: true, title: "检修基本信息"
	    }
	    ,{
	        region : 'center', items: TrainWorkPlanEditWin.centerPanel
	    }
	    ]
	});
	TrainWorkPlanEditWin.win = new Ext.Window({
	    title:"车辆检修作业计划编辑", 
	    layout: 'fit',
		height: 600, width: 1000,
		maximized : true,
		closeAction: 'hide',
		items:TrainWorkPlanEditWin.panel,
		buttonAlign: 'center',
		buttons: [{
			id: "nodeSaveBtnEditWin", text: "保存", iconCls: "saveIcon", handler: function() {
				NodeAndWorkCardEditWin.saveFn();
			}
		},{
			text: '取消', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	TrainWorkPlanEditWin.showWin = function(data,idx,parentIdx,isLeaf,nodeName,idxValue,workPlanStatus,status) {
	
		/*//如果是已完成或者已经终止
		if(status == NODE_STATUS_COMPLETE || status == NODE_STATUS_STOP){
			JobNodeExtConfigEditWin.form = JobNodeExtConfigSearch.form
			WorkCardEditWin.grid = WorkCardEditSearch.grid
			JobProcessNodeRelEditWin.grid = JobProcessNodeRelSearch.grid
		
		}*/
	
		TrainWorkPlanEditWin.node.idx = idx;
		TrainWorkPlanEditWin.node.parentIdx = parentIdx;
		TrainWorkPlanEditWin.node.isLeaf = isLeaf;
		TrainWorkPlanEditWin.node.nodeName = nodeName;
		TrainWorkPlanEditWin.idxValue = idxValue;
		TrainWorkPlanEditWin.workPlanStatus = workPlanStatus;
		TrainWorkPlanEditWin.status = status;
		
		if (!Ext.isEmpty(idxValue)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: idxValue},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
		
		var dataArray = data.split(",");
		var form = TrainWorkPlanEditWin.infoForm.getForm();
		var workPlanIDX = dataArray[0];
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);	
		
		TrainWorkPlanEditWin.tree.getRootNode().setId(TrainWorkPlanEditWin.node.idx);
		TrainWorkPlanEditWin.tree.getRootNode().setText(TrainWorkPlanEditWin.node.nodeName);
		
		//报错  根节点问题
		//TypeError: this.ctNode is undefined
		//this.ctNode.style.display = "none";
		//这个问题是由于该树的节点没有下级叶子，所以不能配置expanded :true（有下级叶子，展开）
		TrainWorkPlanEditWin.tree.root.reload();
		TrainWorkPlanEditWin.tree.getRootNode().expand();
		
		TrainWorkPlanEditWin.tabPanel.hide();
		
		//加载下级节点
		ChildNodeListGrid.nodeIDX = TrainWorkPlanEditWin.node.idx;
		ChildNodeListGrid.workPlanStatus = TrainWorkPlanEditWin.workPlanStatus;
		ChildNodeListGrid.grid.store.load();
		
		
		TrainWorkPlanEditWin.tabPanel.show();
		
		TrainWorkPlanEditWin.win.show();
		
		//写在show后面，show的方法中才会初始化tabpanel
		
		/*
		TrainWorkPlanEditWin.tabPanel.getTabEl(0).hidden = false;
   		TrainWorkPlanEditWin.tabPanel.getTabEl(1).hidden = true;
		TrainWorkPlanEditWin.tabPanel.getTabEl(2).hidden = true;
		TrainWorkPlanEditWin.tabPanel.getTabEl(3).hidden = true;
		TrainWorkPlanEditWin.tabPanel.getTabEl(4).hidden = true;
		TrainWorkPlanEditWin.tabPanel.getTabEl(5).hidden = true;
		TrainWorkPlanEditWin.tabPanel.getTabEl(6).hidden = true;
		*/
		var valueArray = [false,true,true,true,true,true,true];
		var trainWorkPlanEditWin = setTabPanelIsHidden(TrainWorkPlanEditWin,valueArray);
		TrainWorkPlanEditWin = trainWorkPlanEditWin;
		TrainWorkPlanEditWin.tabPanel.activate(0);
	}
});

//设置tab是否可见
//trainWorkPlanEditWin tabpanel隶属于的对象
//valueArray 放入是否显示，注意传入的值个数和tabpanel个数一致 var valueArray = [false,true,true,true,true,true,true];
function setTabPanelIsHidden(trainWorkPlanEditWin,valueArray){
	for(var a = 0;a < valueArray.length;a++){
		trainWorkPlanEditWin.tabPanel.getTabEl(a).hidden = valueArray[a];
	}
	return trainWorkPlanEditWin;
}
