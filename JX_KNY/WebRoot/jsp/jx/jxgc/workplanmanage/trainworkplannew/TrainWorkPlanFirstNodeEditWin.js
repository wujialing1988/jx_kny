Ext.onReady(function(){

	Ext.namespace('TrainWorkPlanFirstNodeEditWin');                       //定义命名空间
	TrainWorkPlanFirstNodeEditWin.fieldWidth = 120;
	TrainWorkPlanFirstNodeEditWin.labelWidth = 80;
	TrainWorkPlanFirstNodeEditWin.node = {};
	TrainWorkPlanFirstNodeEditWin.idxValue = '';
	TrainWorkPlanFirstNodeEditWin.processTips = null;
	TrainWorkPlanFirstNodeEditWin.showtip = function(win,msg){
		TrainWorkPlanFirstNodeEditWin.processTips = new Ext.LoadMask(win.getEl(),{
				msg: msg || "正在处理你的操作，请稍后...",
				removeMask:true
			});
			TrainWorkPlanFirstNodeEditWin.processTips.show();
		}
	/*
	 * 隐藏处理遮罩
	 */
	TrainWorkPlanFirstNodeEditWin.hidetip = function(){
		TrainWorkPlanFirstNodeEditWin.processTips.hide();
	}
	
	
	TrainWorkPlanFirstNodeEditWin.items = [{
		title: "下级节点", border: false, layout: "fit",
        items:[LeafNodeListGridSearch.grid]
	},{
	   	title: "流程节点", border: false, layout: "border", buttonAlign : "center",
	    items: [{
			region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
				height: 200, bodyBorder: false,
				items:[JobProcessNodeEditWin.form], frame: true
			},{
				region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRelEditWin.grid ]
		}],
		buttonAlign: 'center', frame: true,
		buttons: [{
			id: "nodeSaveBtnEditWin", text: "保存", iconCls: "saveIcon", handler: function() {
				TrainWorkPlanFirstNodeEditWin.saveOrApplyFn();
			}
		},{
			text: '取消', iconCls: 'closeIcon', handler: function() {
				TrainWorkPlanFirstNodeEditWin.win.hide();
			}
		}]
//	},{
//        title: "作业工单", border: false, layout: "fit", //2
//        items:[WorkCardEditWin.grid]
    },{
		title: "扩展配置", border: false, layout: "fit",
		items:[JobNodeExtConfigEditWin.form]
	},{
	   	title: "流程节点", border: false, layout: "border", buttonAlign : "center",
	    items: [{
			region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
				height: 350, bodyBorder: false,
				items:[JobProcessFirstNodeSearch.form], frame: true
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
	
	TrainWorkPlanFirstNodeEditWin.infoForm = new Ext.form.FormPanel({
		labelWidth:TrainWorkPlanFirstNodeEditWin.labelWidth, border: false,
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
	// 保存操作
	TrainWorkPlanFirstNodeEditWin.saveFn = function(){
		var nodeForm = JobProcessNodeEditWin.form.getForm();
		nodeForm.findField("planBeginTime").enable();
		nodeForm.findField("planEndTime").enable();
		nodeForm.findField("workHours").enable();
		nodeForm.findField("workMinutes").enable();
		var data = nodeForm.getValues();
			data.ratedWorkMinutes = nodeForm.findField("workHours").getValue()*60 + nodeForm.findField("workMinutes").getValue();
    	if(data.editStatus == EDIT_STATUS_WAIT){
			nodeForm.findField("planBeginTime").disable();
			nodeForm.findField("planEndTime").disable();
			nodeForm.findField("workHours").disable();
			nodeForm.findField("workMinutes").disable();	
    	}
	    var msg = "前置节点均不影响此节点的计划开始时间,计划开始时间以界面设置为准，";
		    if(data.status == NODE_STATUS_GOING){
				nodeForm.findField("planBeginTime").disable();
				msg = "节点已启动，节点工期将根据节点结束时间重新推算，";
		    }
		    delete data.workHours;
		    delete data.workMinutes;
		    var datas = new Array();
			for (var i = 0; i < JobProcessNodeRelEditWin.grid.store.getCount(); i++) {
				var relData = {} ;
				relData = JobProcessNodeRelEditWin.grid.store.getAt(i).data;
				if(Ext.isEmpty(relData.preNodeIDX)){
					continue;
				}
				relData.nodeIDX = JobProcessNodeEditWin.idx;
				datas.push(relData);
			}
			for (var i = 0; i < datas.length; i++) {
				var preNodeIDX = datas[i].preNodeIDX;
				for (var j = (i + 1); j < datas.length; j++) {
					var preNodeIDX1 = datas[j].preNodeIDX;
					if (preNodeIDX == preNodeIDX1) {
						MyExt.Msg.alert("不能重复选择同一节点为前置节点");
						return;
					}
				}
			}	    
		    var cfg = {
		        url: ctx + "/jobProcessNodeNew!saveOrUpdateFirstNodeAndRel.action", jsonData: data,
		        timeout: 600000,
		        params: {rels: Ext.util.JSON.encode(datas)},
		        success: function(response, options){
					if(TrainWorkPlanFirstNodeEditWin.processTips) TrainWorkPlanFirstNodeEditWin.hidetip();
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.errMsg == null && result.success == true) {
		                alertSuccess();
		                TrainWorkPlanFirstNodeEditWin.win.hide();
		                TrainWorkPlanEdit.store.reload();
	//	                if (data.idx)
	//	                	TrainWorkPlanFirstNodeEditWin.reloadParent(data.idx);
	//	                else if (data.parentIDX)
	//	                	TrainWorkPlanFirstNodeEditWin.reloadParent(data.parentIDX);
		            } else {
		                alertFail(result.errMsg);
		            }
		        },
		        failure: function(response, options){	
		        	if(TrainWorkPlanFirstNodeEditWin.processTips) TrainWorkPlanFirstNodeEditWin.hidetip();
			        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    }
		    };
		    Ext.Msg.confirm("提示  ", msg + "确定操作？  ", function(btn){
		        if(btn != 'yes')    return;
		        TrainWorkPlanFirstNodeEditWin.showtip(TrainWorkPlanFirstNodeEditWin.win);
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		    });
	}
	// 点击保存时判断是申请还是直接提交
	TrainWorkPlanFirstNodeEditWin.saveOrApplyFn = function() {
		var nodeForm = JobProcessNodeEditWin.form.getForm();
		if (!nodeForm.isValid()) return; 
		nodeForm.findField("planEndTime").enable();
	    var data = nodeForm.getValues();
	    var oldPlanEndTime = new Date(TrainWorkPlanFirstNodeEditWin.planEndTime).format('Y-m-d');
	    // 判断是否延期申请
	    if( oldPlanEndTime < data.planEndTime.substring(0,10)){
	    	JobProcessFirstNodeApplyWin.applyWin.show();
    	}else {
	       TrainWorkPlanFirstNodeEditWin.saveFn();
    	}
	}

	TrainWorkPlanFirstNodeEditWin.showEditWin = function() {
		var nodeFormEditWin = JobProcessNodeEditWin.form.getForm();
		nodeFormEditWin.reset();
		nodeFormEditWin.findField("workStationBelongTeam").clearValue();
		nodeFormEditWin.findField("workStationBelongTeamName").setValue("");
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNodeNew!getEntityByIDX.action',
	        params: {nodeIDX: JobProcessNodeEditWin.idx},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	var record = new Ext.data.Record();
	                var entity = result.entity;
	                for(var i in entity){
	                	if(i == 'planBeginTime'){
	                		record.set(i, new Date(entity[i]));
	                		JobProcessNodeEditWin.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
	                		continue;	                    		
	                	}
	                	if(i == 'planEndTime'){
	                		record.set(i, new Date(entity[i]));
	                		continue;	                    		
	                	}
	                	record.set(i, entity[i]);
	                }           	
	            	nodeFormEditWin.loadRecord(record);
	            	TrainWorkPlanFirstNodeEditWin.planEndTime = entity.planEndTime;
	                var hours = (entity.ratedWorkMinutes / 60).toString().split(".")[0];
					var minutes = entity.ratedWorkMinutes - hours * 60;
					// 用于记录编辑时修改之前的开始及结束时间
					nodeFormEditWin.findField("newPlanBeginTime").setValue(new Date(entity.planBeginTime).format('Y-m-d H:i'));
					nodeFormEditWin.findField("newPlanEndTime").setValue(new Date(entity.planEndTime).format('Y-m-d H:i'));
				 	nodeFormEditWin.findField("workHours").setValue(hours);
					nodeFormEditWin.findField("workMinutes").setValue(minutes);
				 	if(entity.editStatus == EDIT_STATUS_WAIT){
            			nodeFormEditWin.findField("planBeginTime").disable();
						nodeFormEditWin.findField("planEndTime").disable();
						nodeFormEditWin.findField("workHours").disable();
						nodeFormEditWin.findField("workMinutes").disable();
	            	}else{
	            		nodeFormEditWin.findField("planBeginTime").enable();
						nodeFormEditWin.findField("planEndTime").enable();
						nodeFormEditWin.findField("workHours").enable();
						nodeFormEditWin.findField("workMinutes").enable();
	            	}
					nodeFormEditWin.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
					nodeFormEditWin.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
					nodeFormEditWin.findField("workStationName").setValue(entity.workStationName);
					JobProcessNodeEditWin.nodeIDX = entity.nodeIDX;
					nodeFormEditWin.findField("workStationName").nodeIDX = JobProcessNodeEditWin.nodeIDX;
					nodeFormEditWin.findField("workCalendarIDX").clearValue();
					if (!Ext.isEmpty(entity.workCalendarIDX)) {			    
		                nodeFormEditWin.findField("workCalendarIDX").setDisplayValue(entity.workCalendarIDX, WorkCalendarInfo.getCalendarName(entity.workCalendarIDX));
					}
					if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
						nodeFormEditWin.findField("workStationBelongTeam").disable();
						nodeFormEditWin.findField("workStationName").disable();
					} else if (entity.status == NODE_STATUS_UNSTART) {
						nodeFormEditWin.findField("workStationBelongTeam").enable();
						nodeFormEditWin.findField("workStationName").enable();
					}
					if(entity.status == NODE_STATUS_GOING){
						nodeFormEditWin.findField("planBeginTime").disable();
					}
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		JobProcessNodeRelEditWin.grid.store.load();
		JobProcessFirstNodeApplyWin.chossForm = 1;// 延期申请还是直接提交的标识
	}
	//树
	TrainWorkPlanFirstNodeEditWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jobProcessNodeQuery!getChildTree.action?parentIDX=" + TrainWorkPlanFirstNodeEditWin.node.idx
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: '',
	        id: TrainWorkPlanFirstNodeEditWin.node.idx,
	        leaf: false,
	        expanded :false,
          status: TrainWorkPlanFirstNodeEditWin.status
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	listeners : {
			 beforeload: function(node, e){	 
            	TrainWorkPlanFirstNodeEditWin.tree.getLoader().dataUrl = ctx + '/jobProcessNodeQuery!getChildTree.action?parentIDX=' + node.id;
	        },
    		click: function(node, e) {
    		
				if(node.attributes.status == PROCESSTYPE){
					MyExt.Msg.alert("请选择非流程任务的节点！");
					return;			
				}
    			//完成状态
    			if(node.attributes.status == NODE_STATUS_COMPLETE || node.attributes.status == NODE_STATUS_STOP){
    				Ext.getCmp("nodeSaveBtnEditWin").setVisible(false);  //  隐藏保存按键
    				if(node.leaf){
    					/*
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(0).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(1).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(2).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(3).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(4).hidden = false;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(5).hidden = false;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(6).hidden = false;
						*/
						var valueArray = [true,true,true,true,false,false];
						var trainWorkPlanFirstNodeEditWin = setTabPanelIsHidden(TrainWorkPlanFirstNodeEditWin,valueArray);
						TrainWorkPlanFirstNodeEditWin = trainWorkPlanFirstNodeEditWin;
						
						//加载工单
						WorkCardEditSearch.nodeCaseIDX = node.id;
						WorkCardEditSearch.TrainWorkPlanFirstNodeEditWin = TrainWorkPlanFirstNodeEditWin;
						WorkCardEditSearch.grid.store.load();
						
						TrainWorkPlanFirstNodeEditWin.tabPanel.activate(5);
	    			}else{
						var valueArray = [false,true,true,false,true,false];
						var trainWorkPlanFirstNodeEditWin = setTabPanelIsHidden(TrainWorkPlanFirstNodeEditWin,valueArray);
						TrainWorkPlanFirstNodeEditWin = trainWorkPlanFirstNodeEditWin;
						
						//加载下级节点
						LeafNodeListGridSearch.nodeIDX = node.id;
						LeafNodeListGridSearch.workPlanStatus = TrainWorkPlanFirstNodeEditWin.workPlanStatus;
						LeafNodeListGridSearch.grid.store.load();
						TrainWorkPlanFirstNodeEditWin.tabPanel.activate(4);
	    			}
	    			//加载流程节点
	    			JobProcessFirstNodeSearch.idx = node.id;
	    			JobProcessFirstNodeSearch.showEditWin();
	    			JobProcessNodeRelSearch.nodeIDX = node.id;
	    			if(node.parentNode){
						JobProcessNodeRelSearch.parentIDX = node.parentNode.id;
	    			}
   			
	    			//加载扩展配置
	    			JobNodeExtConfigSearch.trainWorkPlanEditWin = TrainWorkPlanFirstNodeEditWin;
					JobNodeExtConfigSearch.loadFn(node.id);
	    			TrainWorkPlanFirstNodeEditWin.tabPanel.show();
    			
    			}else{
    				Ext.getCmp("nodeSaveBtnEditWin").setVisible(true);  //  显示保存按键
    				if(node.leaf){
						var valueArray = [true,true,true,true,false,false];
						var trainWorkPlanFirstNodeEditWin = setTabPanelIsHidden(TrainWorkPlanFirstNodeEditWin,valueArray);
						TrainWorkPlanFirstNodeEditWin = trainWorkPlanFirstNodeEditWin;
						
						//   加载工单
						WorkCardEditSearch.nodeCaseIDX = node.id;
						WorkCardEditSearch.TrainWorkPlanFirstNodeEditWin = TrainWorkPlanFirstNodeEditWin;
						WorkCardEditSearch.grid.store.load();
						//初始化自定义工单的节点ID
//						TrainWork.nodeCaseIDX = node.id;
						
						TrainWorkPlanFirstNodeEditWin.tabPanel.activate(2);
	    			}else{
	    				/*
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(0).hidden = false;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(1).hidden = false;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(2).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(3).hidden = false;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(4).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(5).hidden = true;
						TrainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(6).hidden = true;
						*/
						
						var valueArray = [false,false,false,true,true,true];
						var trainWorkPlanFirstNodeEditWin = setTabPanelIsHidden(TrainWorkPlanFirstNodeEditWin,valueArray);
						TrainWorkPlanFirstNodeEditWin = trainWorkPlanFirstNodeEditWin;
						
						//加载下级节点
						LeafNodeListGridSearch.nodeIDX = node.id;
						LeafNodeListGridSearch.workPlanStatus = TrainWorkPlanFirstNodeEditWin.workPlanStatus;
						TrainWorkPlanFirstNodeEditWin.tabPanel.activate(1);

	    			}
	    			//加载流程节点
	    			JobProcessNodeEditWin.idx = node.id;
	    			JobProcessNodeRelEditWin.nodeIDX = node.id;
	    			if(node.parentNode){
						JobProcessNodeRelEditWin.parentIDX = node.parentNode.id;
						JobProcessNodeEditWin.parentIDX =  node.parentNode.id;
	    			}
	    			RepairProjectSelect.pTrainTypeIdx = workPlanEntity.trainTypeIDX;
					RepairProjectSelect.nodeCaseIDX = node.id;
	    			
	    			JobProcessNodeTreeWin.processName = workPlanEntity.processName;
					JobProcessNodeTreeWin.workPlanIDX = TrainWorkPlanFirstNodeEditWin.idxValue;
					JobProcessNodeTreeWin.nodeIDX = node.id;					
					TrainWorkPlanFirstNodeEditWin.showEditWin();
	    			
	    			//加载扩展配置
					JobNodeExtConfigEditWin.loadFn(node.id);
	    			TrainWorkPlanFirstNodeEditWin.tabPanel.show();
    			}
	        }
		}
	});
	
	//中心中心panel
	TrainWorkPlanFirstNodeEditWin.tabPanel = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:TrainWorkPlanFirstNodeEditWin.items
    }); 
    
	 //中心panel
	TrainWorkPlanFirstNodeEditWin.centerPanel = new Ext.Panel({
		layout: "border",
		items: [{
			region: "west",
			width: 200,
			layout: "fit",
			items: TrainWorkPlanFirstNodeEditWin.tree
		},{
			region: "center",
			layout: "fit",
			items: TrainWorkPlanFirstNodeEditWin.tabPanel
		}]
	});
	
	TrainWorkPlanFirstNodeEditWin.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region: 'north', collapsible:true, collapsed: false,  height: 70, 
	        items:[TrainWorkPlanFirstNodeEditWin.infoForm], frame: true, title: "检修基本信息"
	    }
	    ,{
	        region : 'center', items: TrainWorkPlanFirstNodeEditWin.centerPanel
	    }
	    ]
	});
	TrainWorkPlanFirstNodeEditWin.win = new Ext.Window({
	    title:"车辆检修作业计划编辑", 
	    layout: 'fit',
		height: 600, width: 1000,
		maximized : true,
		closeAction: 'hide',
		items:TrainWorkPlanFirstNodeEditWin.panel
	
	});
	
	
	// 点击节点编辑节点信息
	TrainWorkPlanFirstNodeEditWin.showWin = function(data,idx,parentIdx,isLeaf,nodeName,idxValue,workPlanStatus,status) {
	
		/*//如果是已完成或者已经终止
		if(status == NODE_STATUS_COMPLETE || status == NODE_STATUS_STOP){
			JobNodeExtConfigEditWin.form = JobNodeExtConfigSearch.form
			WorkCardEditWin.grid = WorkCardEditSearch.grid
			JobProcessNodeRelEditWin.grid = JobProcessNodeRelSearch.grid
		
		}*/
		TrainWorkPlanFirstNodeEditWin.node.idx = idx;
		TrainWorkPlanFirstNodeEditWin.node.parentIdx = parentIdx;
		TrainWorkPlanFirstNodeEditWin.node.isLeaf = isLeaf;
		TrainWorkPlanFirstNodeEditWin.node.nodeName = nodeName;
		TrainWorkPlanFirstNodeEditWin.idxValue = idxValue;
		TrainWorkPlanFirstNodeEditWin.workPlanStatus = workPlanStatus;
		TrainWorkPlanFirstNodeEditWin.status = status;
		
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
		var form = TrainWorkPlanFirstNodeEditWin.infoForm.getForm();
	    workPlanIDX = dataArray[0];
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);	
		
		TrainWorkPlanFirstNodeEditWin.tree.getRootNode().setId(TrainWorkPlanFirstNodeEditWin.node.idx);
		TrainWorkPlanFirstNodeEditWin.tree.getRootNode().setText(TrainWorkPlanFirstNodeEditWin.node.nodeName);
		TrainWorkPlanFirstNodeEditWin.tree.getRootNode().attributes.status = TrainWorkPlanFirstNodeEditWin.status;
		//报错  根节点问题
		//TypeError: this.ctNode is undefined
		//this.ctNode.style.display = "none";
		//这个问题是由于该树的节点没有下级叶子，所以不能配置expanded :true（有下级叶子，展开）
		TrainWorkPlanFirstNodeEditWin.tree.root.reload();
		TrainWorkPlanFirstNodeEditWin.tree.getRootNode().expand();
		
		TrainWorkPlanFirstNodeEditWin.tabPanel.hide();
		
		//加载下级节点
		LeafNodeListGridSearch.nodeIDX = idx;
		LeafNodeListGridSearch.workPlanStatus = TrainWorkPlanFirstNodeEditWin.workPlanStatus;
		LeafNodeListGridSearch.grid.store.load();
	
		TrainWorkPlanFirstNodeEditWin.tabPanel.show();
		JobProcessNodeEditWin.idx = idx;
		JobProcessNodeEditWin.parentIDX = parentIdx;
//		TrainWorkPlanFirstNodeEditWin.showEditWin();
		TrainWorkPlanFirstNodeEditWin.win.show();
		
		//写在show后面，show的方法中才会初始化tabpanel

		var valueArray = [false,true,true,true,true,true];
		var trainWorkPlanFirstNodeEditWin = setTabPanelIsHidden(TrainWorkPlanFirstNodeEditWin,valueArray);
		TrainWorkPlanFirstNodeEditWin = trainWorkPlanFirstNodeEditWin;
		TrainWorkPlanFirstNodeEditWin.tabPanel.activate(0);
	}
});

//设置tab是否可见
//TrainWorkPlanFirstNodeEditWin tabpanel隶属于的对象
//valueArray 放入是否显示，注意传入的值个数和tabpanel个数一致 var valueArray = [false,true,true,true,true,true];
function setTabPanelIsHidden(trainWorkPlanFirstNodeEditWin,valueArray){
	for(var a = 0;a < valueArray.length;a++){
		trainWorkPlanFirstNodeEditWin.tabPanel.getTabEl(a).hidden = valueArray[a];
	}
	return trainWorkPlanFirstNodeEditWin;
}
