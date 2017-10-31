
/*
 *工长派工flag=0与重派工flag=1
 * mode True 批量
 */ 
function dispatcher(idx, flag, mode){
	isFullDipatch = 'false';
	if(mode){	//批量派工
		var store;//用于判断是否选择的都是同一班组
		var workcardIdx;
		if(flag){
			if(!$yd.isSelectedRecord(Foreman.grid,true)){
				return;
			}
			workcardIdx = $yd.getSelectedIdx(Foreman.grid);//取得被勾选的作业卡IDX
			store =  Foreman.grid.store;
			
		}else{
			if(!$yd.isSelectedRecord(Foreman.NoDispatcherGrid,true)){
				return;
			}
			workcardIdx = $yd.getSelectedIdx(Foreman.NoDispatcherGrid);
			store =  Foreman.NoDispatcherGrid.store;
		}
		
		var record = store.getById(workcardIdx[0]);
		for(var i = 1; i < workcardIdx.length; i++){
			var rdx = store.getById(workcardIdx[i]);			
			if(rdx.get("workStationBelongTeam") != record.get("workStationBelongTeam")){
				MyExt.Msg.alert("批量派工只针对同一班组");
				return;
			}
		}
		WorkStationEmp.idx = "";
		WorkStationEmp.team = record.get("workStationBelongTeam");
	}else{//非批量派工
		WorkStationEmp.idx = idx;//记录派工作业卡
		var record;
		if(flag){
			record = Foreman.grid.store.getById(idx);		
		}else{
			record = Foreman.NoDispatcherGrid.store.getById(idx);
		}	
	}	
	
	//取得操作的Grid
	if(flag){
		Foreman.randomGrid = Foreman.grid;		
	}else{
		Foreman.randomGrid = Foreman.NoDispatcherGrid;
	}
	//重新加载Grid
	WorkStationEmp.grid.store.load();
	WorkStationEmp.NoDispatchGrid.store.load();	
	WorkStationEmp.selectWin.show();	
}

//默认上次派工操作
function defaultDispatcher(){
	
	if(!$yd.isSelectedRecord(Foreman.NoDispatcherGrid,true)) return;
	var records = Foreman.NoDispatcherGrid.selModel.getSelections();
	var idx = [];
	var filter = 0;
	for(var i = 0; i < records.length; i++){
		
		if(!records[i].get("lastTimeWorker")){
			Foreman.NoDispatcherGrid.selModel.deselectRow(Foreman.NoDispatcherGrid.store.indexOfId(records[i].get("idx")));
			//不能派工过滤掉
			filter++;
			continue;
		}
		idx.push(records[i].get("idx"));
	}
	if(filter == records.length){
		MyExt.Msg.alert("所选数据均无上次派工记录");
		return;
	}else if(filter > 0){
		MyExt.Msg.alert(filter + "条数据无上次派工记录被过滤");
	}
	Ext.Msg.confirm("提示","确认派工", function(btn){
		if(btn == 'yes'){
			showtip();
			jQuery.ajax({
            	url:ctx + "/workCard!defaultLastTimeWorker.action",
            	data:{ workCardIdx : idx + ""},
            	type:"post",
            	dataType:"json",
            	success:function(data){
            		hidetip();
            		if(data.status == 'success'){
            			//成功后刷新Grid
	            		Foreman.NoDispatcherGrid.store.load();
	    	    		Foreman.grid.store.load();
	            		alertSuccess();
	            		Ext.getCmp("Baserdp_combo").reset();
						Ext.getCmp("Baserdp_combo").clearValue();
						Ext.getCmp("Baserdp_combo").cascadeStore();
						Ext.getCmp("Baserdp_combo2").reset();
						Ext.getCmp("Baserdp_combo2").clearValue();
						Ext.getCmp("Baserdp_combo2").cascadeStore();
            		}else{
            			alertFail(data.errMsg);
            		}
            	}
            });
		}
	});
}
Ext.onReady(function(){
	Ext.Ajax.timeout = 10000000000000;
	Ext.namespace('Foreman');                       //定义命名空间	
	Foreman.randomGrid;								//存放未派工或已派工Grid
	Foreman.searchParam = {};
	Foreman.NoDispatcherSearchParam = {};
	
	/**
	 * 新增时,设置默认的车型, 默认车型取可承修车型的第一条
	 */
	Foreman.setDefaultRdp = function (){
		Ext.Ajax.request({
			url: ctx + '/baseCombo!pageList.action',//数据源路径
			params: {manager : 'trainWorkPlanQuery','isDispatcher':'n'}, 
			success: function(response, options){
			       var result = Ext.util.JSON.decode(response.responseText);
			       if (result.errMsg == null) {
		           		if(result!=null&&result.root.length>0&&result.root[0]!=null){
		           			//查询窗口打开时,默认的生产任务单第一条记录
		           			Ext.getCmp("Baserdp_combo").setDisplayValue(result.root[0].idx,result.root[0].groupRdpInfo);
							//流程节点
							var tpn = Ext.getCmp("tecprocessnode_node");
							tpn.reset(); 
							tpn.clearValue();
							tpn.rootText = result.root[0].trainTypeShortName;
							Ext.getCmp("tecprocessnode_node").queryParams = {'workPlanIDX':result.root[0].idx,'isDispatcher':'n'};
							//查询窗口打开时,根据默认的生产任务单第一条,联动"位置"控件
//							Ext.getCmp("position").reset();
//                            Ext.getCmp("position").clearValue();
//							Ext.getCmp("position").tree.root.setText(result.root[0].buildupTypeName);
//                            Ext.getCmp("position").partsBuildUpTypeIdx = result.root[0].buildUpTypeIDX;
//                            Ext.getCmp("position").tree.root.attributes["buildUpPlaceCode"] = result.root[0].buildUpTypeCode;
                            
		           		}
			       } else {
			              alertFail(result.errMsg);
			       }
			},
			failure: function(response, options){
			       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	//已派工Grid
	Foreman.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workCard!queryWorkCard.action?isDispatcher=1',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workCard!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar: [{
	    	text:"批量重派工", iconCls:"pjglIcon",
	    	handler:function(){
	    		dispatcher(null,1,true);
	    	}
	    },'refresh'/*,{
	    	text:"重新加载数据",
	    	iconCls:"refreshgif",
	    	handler:function(){
	    		Foreman.grid.store.load();
	    	}
	    }*/],
	    saveFormColNum:3, searchFormColNum:2, viewConfig: null,
		fields: [{
			header:'重派工', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
			renderer:function(v,x,r){			
				return "<img src='" + img + "' alt='重派工' style='cursor:pointer' onclick='dispatcher(\"" + v + "\",1)'/>";
			}
		},{
			header:'作业人员', dataIndex:'workers', editor: { }, width:200/*, searcher:{disabled:true}*/
		},{
			header:'状态', dataIndex:'status', editor: { },
			renderer:function(v){				 	
			    switch(v){
				    case status_new:
				    	return "初始化";				   
				    case status_open:
				    	return "已开放";
				    case status_handling:
				    	return "处理中";
				    case status_handled:
				    	return "已处理";
				    case status_finished:
				    	return "终止";
			    }
			}, searcher:{disabled:true}
		},{
			header:'工序卡主键', dataIndex:'workSeqCardIDX', hidden:true, editor: { }
		},{
			header:'作业编码', dataIndex:'workCardCode', editor: { }, searcher:{disabled:true}
		},{
			header:'车型', dataIndex:'trainSortName', editor: { }
		},{
			header:'车号', dataIndex:'trainNo', editor: { }
		},{
			header:'修程', dataIndex:'repairClassRepairTime', width:50, editor: { },
			renderer:function(v){
				if(v && v.indexOf('|')!=-1){
					return v.substring(0, v.indexOf('|'));
				}
			}
		},/*{
			header:'工序卡类型', dataIndex:'workSeqType', hidden:true, editor: { }
		},*/{
			header:'检修活动主键', dataIndex:'repairActivityIDX', hidden:true, editor: { }
		},{
			header:'检修活动类型', dataIndex:'repairActivityTypeName', editor: { }, searcher:{disabled:true}, hidden:true
		},{
			header:'施修任务兑现单主键', dataIndex:'rdpIDX', hidden:true, editor: { }
		},/*{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX', hidden:true, editor: { }
		},{
			header:'组成型号', dataIndex:'buildUpTypeName', hidden:true, editor: { }
		},*/{
			header:'作业名称', dataIndex:'workCardName', width:200, editor: { }
		},/*{
			header:'额定工时（单位：分钟）', dataIndex:'ratedWorkHours', hidden:true, editor: { }
		},*/{
			header:'作业内容', dataIndex:'workScope', width:250, editor: { }, hidden:true
		},/*{
			header:'位置', dataIndex:'fixPlaceFullName', width:250, editor: { }
		},*/{
			header:'流程节点', dataIndex:'nodeCaseName', width:200, editor: { }
		},{
			header:'检修活动', dataIndex:'repairActivityName', hidden:true, width:200, editor: { }
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements', hidden:true, editor: { }
		},/*{
			header:'互换配件信息主键', dataIndex:'partsAccountIDX', hidden:true, editor: { }
		},{
			header:'互换配件型号主键', dataIndex:'partsTypeIDX', hidden:true, editor: { }
		},{
			header:'配件名称', dataIndex:'partsName', hidden:true, editor: { }
		},{
			header:'规格型号', dataIndex:'specificationModel', hidden:true, editor: { }
		},{
			header:'铭牌号', dataIndex:'nameplateNo', hidden:true, editor: { }
		},{
			header:'配件编号', dataIndex:'partsNo', hidden:true, editor: { }
		},{
			header:'安装位置主键', dataIndex:'fixPlaceIDX', hidden:true, editor: { }
		},{
			header:'安装位置编码全名', dataIndex:'fixPlaceFullCode', hidden:true, editor: { }
		},*/{
			header:'工位所属班组', dataIndex:'workStationBelongTeam', hidden:true, editor: { }
		},{
			header:'工位所属班组', dataIndex:'workStationBelongTeamName', width:200, editor: { }, searcher:{disabled:true}
		},{
			header:'工位主键', dataIndex:'workStationIDX', hidden:true, editor: { }
		},{
			header:'工位名称', dataIndex:'workStationName', editor: { }, searcher:{disabled:true}
		},/*{
			header:'工位负责人主键', dataIndex:'dutyPersonId', hidden:true, editor: { }
		},{
			header:'工位负责人名称', dataIndex:'dutyPersonName', hidden:true, editor: { }
		},*/{
			header:'备注', dataIndex:'remarks', hidden:true, editor: { }
		}],/*,{
			header:'调度派工人员', dataIndex:'dWorkerName'
		},{
			header:'调度派工日期', dataIndex:'dDesignateTime', xtype:'datecolumn', format: "Y-m-d",searcher:{disabled:true}
		},{
			header:'工长派工人员', dataIndex:'hWorkerName'
		},{
			header:'工长派工日期', dataIndex:'hDesignateTime', xtype:'datecolumn', format: "Y-m-d",searcher:{disabled:true}
		}*/
		searchFn:function(searchParam){
			Foreman.searchParam = searchParam ;
	        this.store.load();
		},
		toEditFn:function(g,ri){
			var r = this.store.getAt(ri);
			dispatcher(r.get("idx"),1);
			return false;
		}
	});
	
	Foreman.labelWidth = 90;
	Foreman.anchor = '95%';
	Foreman.fieldWidth = 270;
	
	Foreman.DispatcherSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: Foreman.labelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'生产任务单',
					allowBlank:true,
					width:Foreman.fieldWidth,
					id:"Baserdp_combo2",
					xtype: 'Base_combo',
					hiddenName: 'workPlanIDX',
					queryParams:{'isDispatcher':'y'},
					business: 'trainWorkPlanQuery',
					entity:'com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan',
					fields: ['groupRdpInfo','idx','trainTypeShortName'],
					displayField:'groupRdpInfo',
					valueField:'idx',
					listeners : {
						"select" : function() {
							//重新加载流程节点数据
							var rdpIdx = this.getValue(); //兑现单的Idx, 联动触发流程节点名称选择控件的值重加载
							var rootName = this.findRecord(this.valueField,this.value).data.trainTypeShortName;
							var tpn = Ext.getCmp("tecprocessnode_node2");
							tpn.reset(); 
							tpn.clearValue();
							tpn.rootText = rootName;
							Ext.getCmp("tecprocessnode_node2").queryParams = {'workPlanIDX':this.getValue(),'isDispatcher':'y'};
							//重新加载位置数据 
//							Ext.getCmp("position2").reset();
//                            Ext.getCmp("position2").clearValue();
//                            Ext.getCmp("position2").tree.root.setText(this.findRecord(this.valueField,this.value).data.buildupTypeName);
//                            Ext.getCmp("position2").partsBuildUpTypeIdx = this.findRecord(this.valueField,this.value).data.buildUpTypeIDX;
//                            Ext.getCmp("position2").tree.root.attributes["buildUpPlaceCode"] = this.findRecord(this.valueField,this.value).data.buildUpTypeCode;//record.get("buildUpTypeCode");							
							
						}
					}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'流程节点名称',
					id: "tecprocessnode_node2",
					xtype: "Base_multyComboTree",
					hiddenName: "nodeCaseIDX",					
					business: 'jobProcessNodeQuery',
					rootText: '流程节点名称',
					width:Foreman.fieldWidth,
					disabled:false,
					returnField: [{widgetId: "nodeCaseName", propertyName: "text"}, //名称
			  			  		  {widgetId: "nodeIdx", propertyName: "id"}], //简称
		    		selectNodeModel: "all",
		    		listeners : {
		    			"beforequery" : function(){
		    				var baseRdp =  Ext.getCmp("Baserdp_combo2").getValue();
							if(baseRdp == "" || baseRdp == null){
								MyExt.Msg.alert("请先选择生产任务单！");
								return false;
							}
		    			},
			  			"select" : function() {
			  			}
			  		}
				}]
			},/*{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					id: 'position2', xtype: 'BuildUpType_comboTree', fieldLabel: '位置',
						  width: 380,  hiddenName: 'fixplace_fullname', selectNodeModel: 'all',						  
						  returnField: [{widgetId:"realSpecificationModel_Id",propertyName:"specificationModel"},
						  				{widgetId:"realNameplateNo_Id",propertyName:"nameplateNo"},
						  				{widgetId:"realFixPlaceFullCode_Id",propertyName:"buildUpPlaceFullCode"},
						  				{widgetId:"realPartsNo_Id",propertyName:"partsNo"},
						  				{widgetId:"realPartsAccountIDX_Id",propertyName:"partsAccountIDX"},
						  				{widgetId:"realPartsTypeIDX_Id",propertyName:"partsTypeIDX"},
						  				{widgetId:"realPartsName_Id",propertyName:"partsName"},
						  				{widgetId:"dropPartsNo_Id",propertyName:"partsNo"},
						  				{widgetId:"realFixPlaceIDX_Id",propertyName:"buildUpPlaceIdx"}						  				
						  				],
						  listeners:{
						  	"beforequery" : function(){
			    				var baseRdp =  Ext.getCmp("Baserdp_combo2").getValue();
								if(baseRdp == "" || baseRdp == null){
									MyExt.Msg.alert("请先选择生产任务单！");
									return false;
								}
		    				},
							"select": function(){
							}
						}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{					
					fieldLabel:'检修活动名称',
					width:Foreman.fieldWidth,
					id:"Baseactivity_combo2",
					xtype: 'Base_multyComboTree',
					hiddenName: 'activity_name',
					//queryJson:'',
					displayField:'activityName',
					valueField:'activityName',
					business: 'repairActivity',
					rootText: '检修活动',
					listeners : {
						"beforequery" : function(){
			    				var baseRdp =  Ext.getCmp("Baserdp_combo2").getValue();
								if(baseRdp == "" || baseRdp == null){
									MyExt.Msg.alert("请先选择生产任务单！");
									return false;
								}
		    			}
					}
				}]
			},*/{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'作业工单名称',
					allowBlank:true,
					width:Foreman.fieldWidth,
					name:"workCardName",
					xtype: 'textfield'
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					id:"jcjx_person",
					xtype:'TeamEmployee_SelectWin',
					editable:false,
					fieldLabel : "作业人员",
					hiddenName:'workers',
					valueField : 'empname',
					displayField : 'empname',
					returnField:[{widgetId:"workers",propertyName:"empname"}],
		    		orgid:teamOrgId	,width:Foreman.fieldWidth
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{					
					xtype:'checkboxgroup',id:'status_searchId', name:'status',fieldLabel:'状态',
				    items: [{   
					        xtype:'checkbox', name:'status', boxLabel:'初始化', inputValue:status_new
					    },/*{   
					        xtype:'checkbox', name:'status', boxLabel:'已开放', inputValue:status_open
					    },*/{   
					        xtype:'checkbox', name:'status', boxLabel:'处理中', inputValue:status_handling 
					    }]

				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var searchParam = Foreman.DispatcherSearchForm.getForm().getValues();
                    searchParam = MyJson.deleteBlankProp(searchParam);
                    for (prop in searchParam) {
                    	if (prop == 'status') {
                    		var status = searchParam["status"];
                    		var statusStr = "";
                    		if (Ext.isArray(status)) {
                    			for (var i = 0; i < status.length; i++) {
                    				statusStr += status[i] + ",";
                    			}
                    			statusStr = statusStr.substring(0, statusStr.length - 1);
                    		} else {
                    			statusStr = status;
                    		}
                    		searchParam.status = statusStr;
                    	}
                    }
					Foreman.grid.searchFn(searchParam); 
				}
			},{
                text: "重置", iconCls: "resetIcon", handler: function(){ 
                	Foreman.DispatcherSearchForm.getForm().reset();
                	//清空自定义组件的值
                    var componentArray = ["Base_combo","TeamEmployee_SelectWin","Base_multyComboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = Foreman.DispatcherSearchForm.findByType(componentArray[j]);
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
                	searchParam = {};
                	Foreman.grid.searchFn(searchParam);
                }
			}]
	});
	
	Foreman.NoDispatcherSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: Foreman.labelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'生产任务单',
					allowBlank:true,
					width:Foreman.fieldWidth,
					id:"Baserdp_combo",
					xtype: 'Base_combo',
					hiddenName: 'workPlanIDX',
					business: 'trainWorkPlanQuery',
					entity:'com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan',
					queryParams:{'isDispatcher':'n'},
					fields: ['groupRdpInfo','idx','trainTypeShortName'],
					displayField:'groupRdpInfo',
					valueField:'idx',
					listeners : {
						"select" : function() {
							//重新加载流程节点数据
							var rdpIdx = this.getValue(); //兑现单的Idx, 联动触发流程节点名称选择控件的值重加载
							var rootName = this.findRecord(this.valueField,this.value).data.trainTypeShortName;
							var tpn = Ext.getCmp("tecprocessnode_node");
							tpn.reset(); 
							tpn.clearValue();
							tpn.rootText = rootName;
							Ext.getCmp("tecprocessnode_node").queryParams = {'workPlanIDX':this.getValue(),'isDispatcher':'n'};
							//重新加载位置数据
//							Ext.getCmp("position").reset();
//                            Ext.getCmp("position").clearValue();
//                            Ext.getCmp("position").tree.root.setText(this.findRecord(this.valueField,this.value).data.buildupTypeName);
//                            Ext.getCmp("position").partsBuildUpTypeIdx = this.findRecord(this.valueField,this.value).data.buildUpTypeIDX;
//                            Ext.getCmp("position").tree.root.attributes["buildUpPlaceCode"] = this.findRecord(this.valueField,this.value).data.buildUpTypeCode;//record.get("buildUpTypeCode");
							
						}
					}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{			  		
			  		fieldLabel:'流程节点名称',
					id: "tecprocessnode_node",
					xtype: "Base_multyComboTree",
					hiddenName: "nodeCaseIDX",					
					business: 'jobProcessNodeQuery',
					rootText: '流程节点名称',
					width:Foreman.fieldWidth,
					disabled:false,
					returnField: [{widgetId: "nodeCaseName", propertyName: "text"}, //名称
			  			  		  {widgetId: "nodeIdx", propertyName: "id"}], //简称
		    		selectNodeModel: "all",
		    		listeners : {
		    			"beforequery" : function(){
		    				var baseRdp =  Ext.getCmp("Baserdp_combo").getValue();
							if(baseRdp == "" || baseRdp == null){
								MyExt.Msg.alert("请先选择生产任务单！");
								return false;
							}
		    			},
			  			"select" : function() {
			  			}
			  		}
				}]
			},/*{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					id: 'position', xtype: 'BuildUpType_comboTree', fieldLabel: '位置',
						  width: 380,  hiddenName: 'fixplace_fullname', selectNodeModel: 'all',						  
						  returnField: [{widgetId:"realSpecificationModel_Id",propertyName:"specificationModel"},
						  				{widgetId:"realNameplateNo_Id",propertyName:"nameplateNo"},
						  				{widgetId:"realFixPlaceFullCode_Id",propertyName:"buildUpPlaceFullCode"},
						  				{widgetId:"realPartsNo_Id",propertyName:"partsNo"},
						  				{widgetId:"realPartsAccountIDX_Id",propertyName:"partsAccountIDX"},
						  				{widgetId:"realPartsTypeIDX_Id",propertyName:"partsTypeIDX"},
						  				{widgetId:"realPartsName_Id",propertyName:"partsName"},
						  				{widgetId:"dropPartsNo_Id",propertyName:"partsNo"},
						  				{widgetId:"realFixPlaceIDX_Id",propertyName:"buildUpPlaceIdx"}						  				
						  				],
						  listeners:{
						  	"beforequery" : function(){
			    				var baseRdp =  Ext.getCmp("Baserdp_combo").getValue();
								if(baseRdp == "" || baseRdp == null){
									MyExt.Msg.alert("请先选择生产任务单！");
									return false;
								}
		    				},
							"select": function(){
							}
						}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'检修活动名称',
					width:Foreman.fieldWidth,
					id:"Baseactivity_combo",
					xtype: 'RepairActivityMulty_comboTree',
					hiddenName: 'activity_name',
					queryJson:'',
					displayField:'activityName',
					valueField:'activityName',
					listeners : {
						"beforequery" : function(){
			    				var baseRdp =  Ext.getCmp("Baserdp_combo").getValue();
								if(baseRdp == "" || baseRdp == null){
									MyExt.Msg.alert("请先选择生产任务单！");
									return false;
								}
		    			}
					}
				}]
			},*/{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				fieldWidth: Foreman.fieldWidth, labelWidth: Foreman.labelWidth,	columnWidth:0.5,	defaults:{anchor:Foreman.anchor},
				items:[{
					fieldLabel:'作业工单名称',
					allowBlank:true,
					width:Foreman.fieldWidth,
					id:"workCardName",
					name: "workCardName",
					xtype: 'textfield'
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var searchParam = Foreman.NoDispatcherSearchForm.getForm().getValues();
                    searchParam = MyJson.deleteBlankProp(searchParam);
					Foreman.NoDispatcherGrid.searchFn(searchParam); 
				}
			},{
                text: "重置", iconCls: "resetIcon", handler: function(){ 
                	Foreman.NoDispatcherSearchForm.getForm().reset();
                	//清空自定义组件的值
                    var componentArray = ["Base_combo","Base_multyComboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = Foreman.NoDispatcherSearchForm.findByType(componentArray[j]);
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
                	searchParam = {};
                	Foreman.NoDispatcherGrid.searchFn(searchParam);
                }
			},{
				text:'全部批量派工', iconCls:'saveIcon', 
				handler: function(){ 
					isFullDipatch = 'true';
					var searchParam = Foreman.NoDispatcherSearchForm.getForm().getValues();
            		searchParam = MyJson.deleteBlankProp(searchParam);
            		Foreman.NoDispatcherGrid.searchFn(searchParam); 
					WorkStationEmp.idx = "";
					WorkStationEmp.grid.store.load();
					WorkStationEmp.NoDispatchGrid.store.load();
					WorkStationEmp.selectWin.show();
				}
			}]
	});
	
	//未派工Grid
	Foreman.NoDispatcherGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/workCard!queryWorkCard.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workCard!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar: [{
	    	text:"批量派工", iconCls:"pjglIcon",
	    	handler:function(){
	    		dispatcher(null,0,true);
	    	}
	    },{
	    	text:"默认上次派工", iconCls:"pjglIcon",
	    	handler:function(){
	    		defaultDispatcher();
	    	}
	    },'refresh'/*,{
	    	text:"重新加载数据",
	    	iconCls:"refreshgif",
	    	handler:function(){
	    		Foreman.NoDispatcherGrid.store.load();
	    	}
	    }*/],
	    saveFormColNum:3, searchFormColNum:2, viewConfig: null,
	    beforeSaveFn: Foreman.beforeSaveFn ,
		fields: [{
			header:'派工', dataIndex:'idx', editor: { xtype:'hidden' }, width:40, sortable:false,
			renderer:function(v,x,r){			
				return "<img src='" + img + "' alt='派工' style='cursor:pointer' onclick='dispatcher(\"" + v + "\",0)'/>";
			}
		},{
			header:'上次作业人员', dataIndex:'workers', width:200, hidden:true, editor: { }
		},{
			header:'上次作业人员', dataIndex:'lastTimeWorker', width:200, editor: { }
		},{
			header:'工序卡主键', dataIndex:'workSeqCardIDX', hidden:true, editor: { }
		},{
			header:'作业编码', dataIndex:'workCardCode', editor: { }
		},{
			header:'车型', dataIndex:'trainSortName', editor: { }
		},{
			header:'车号', dataIndex:'trainNo', editor: { }
		},{
			header:'修程', dataIndex:'repairClassRepairTime', width:50, editor: { },
			renderer:function(v){
				if(v && v.indexOf('|')!=-1){
					return v.substring(0, v.indexOf('|'));
				}
			}
		},/*{
			header:'工序卡类型', dataIndex:'workSeqType', hidden:true, editor: { }
		},*/{
			header:'检修活动主键', dataIndex:'repairActivityIDX', hidden:true, editor: { }
		},{
			header:'检修活动类型', dataIndex:'repairActivityTypeName', editor: { }, searcher:{disabled:true}, hidden:true
		},{
			header:'施修任务兑现单主键', dataIndex:'rdpIDX', hidden:true, editor: { }
		},/*{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX', hidden:true, editor: { }
		},{
			header:'组成型号', dataIndex:'buildUpTypeName', hidden:true, editor: { }
		},*/{
			header:'作业名称', dataIndex:'workCardName', width:200, editor: { }
		},/*{
			header:'额定工时（单位：分钟）', dataIndex:'ratedWorkHours', hidden:true, editor: { }
		},*/{
			header:'作业内容', dataIndex:'workScope', width:250, editor: { }, hidden:true
		},/*{
			header:'位置', dataIndex:'fixPlaceFullName', width:250, editor: { }
		},*/{
			header:'流程节点', dataIndex:'nodeCaseName', width:200, editor: { }
		},{
			header:'检修活动', dataIndex:'repairActivityName', hidden:true, width:200, editor: { }
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements', hidden:true, editor: { }
		},/*{
			header:'互换配件信息主键', dataIndex:'partsAccountIDX', hidden:true, editor: { }
		},{
			header:'互换配件型号主键', dataIndex:'partsTypeIDX', hidden:true, editor: { }
		},{
			header:'配件名称', dataIndex:'partsName', hidden:true, editor: { }
		},{
			header:'规格型号', dataIndex:'specificationModel', hidden:true, editor: { }
		},{
			header:'铭牌号', dataIndex:'nameplateNo', hidden:true, editor: { }
		},{
			header:'配件编号', dataIndex:'partsNo', hidden:true, editor: { }
		},{
			header:'安装位置主键', dataIndex:'fixPlaceIDX', hidden:true, editor: { }
		},{
			header:'安装位置编码全名', dataIndex:'fixPlaceFullCode', hidden:true, editor: { }
		},*/{
			header:'状态', dataIndex:'status', editor: { },
			renderer:function(v){				 	
			    switch(v){
				    case status_new:
				    	return "初始化";				   
				    case status_open:
				    	return "已开放";
				    case status_handling:
				    	return "处理中";
				    case status_handled:
				    	return "已处理";
				    case status_finished:
				    	return "终止";
			    }
			}, searcher:{disabled:true}
		},{
			header:'备注', dataIndex:'remarks', hidden:true, editor: { }
		/*},{
			header:'调度派工人员', dataIndex:'dWorkerName'
		},{
			header:'调度派工日期', dataIndex:'dDesignateTime', xtype:'datecolumn', format: "Y-m-d",searcher:{disabled:true}*/
		}],
		searchFn:function(searchParam){
			Foreman.NoDispatcherSearchParam = searchParam ;
	        this.store.load();
		},
		toEditFn:function(g,ri){
			var r = this.store.getAt(ri);
			dispatcher(r.get("idx"),0);
			return false;
		},
		searchButtonFn: function(){                         //点击查询按钮触发的函数
	        //判断查询窗体是否为null，如果为null则自动创建后显示
	        if(this.searchWin == null)  this.createSearchWin();
	        if(this.saveWin)    this.saveWin.hide();
	        this.searchWin.show();
	        Foreman.setDefaultRdp(); //打开查询窗口时默认选择第一个兑现单
    	}
	});
	
	
	//页面布局
	Foreman.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "未派工", border: false, xtype: "panel", layout: "border", 
	            items: [{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 172, bodyBorder: false,
		            items:[Foreman.NoDispatcherSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ Foreman.NoDispatcherGrid ]
		        }]
	        },{
	            title: "已派工", border: false, xtype: "panel", layout: "border", 
	            items:[{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 200, bodyBorder: false,
		            items:[Foreman.DispatcherSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ Foreman.grid ]
		        }],
		        listeners:{
	        		activate:function(){
	        			if(!isClickForeman) Foreman.grid.store.load();
	        			isClickForeman = true;
	        		}	        	
	        	}
	        }]
	});
	
	//已派工过滤
	Foreman.grid.store.on("beforeload",function(){
		if(!teamOrgId){
			return false;
		}	
		var searchParam = Foreman.searchParam;
		searchParam.haveDefaultPerson = "1";
		searchParam.workStationBelongTeam = teamOrgId;
        
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		this.baseParams.foreman = 1;	//表示已派工
	});
	//未派工过滤
	Foreman.NoDispatcherGrid.store.on("beforeload",function(){
		if(!teamOrgId){
			return false;
		}	
		var searchParam = MyJson.clone(Foreman.NoDispatcherSearchParam);
		searchParam.haveDefaultPerson = "$[OR]$0]|[$IS NULL$";
		searchParam.workStationBelongTeam = teamOrgId;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		this.baseParams.foreman = 0;	//表示未派工
	});
	
	//工长派工处理事件
	WorkStationEmp.submit = function(){
		var idxs = '';				
		//未选择记录，直接返回
	    if(!WorkStationEmp.grid.store.getAt(0)){
	    	MyExt.Msg.alert("尚未选择作业人员");
	    	return;
	    }
	    var empids = [];
	    var record = WorkStationEmp.grid.store.getAt(0);
	    var i = 0;
	    while(record){
	    	empids.push(record.get("empid"));
	    	record = WorkStationEmp.grid.store.getAt(++i);	    	
	    }
	    if(empids.toString().length > 499){
	    	 MyExt.Msg.alert("选择作业人员超过最大限制,请重新选择");
	    	 return;
	    }
	   
	    //非全部派工
	    if(isFullDipatch == 'false'){
			idxs = $yd.getSelectedIdx(Foreman.randomGrid);
			showtip();
		    jQuery.ajax({
		    	url: ctx + "/workCard!foremanDispatcher.action",
		    	data:{workCardIdx:idxs+"", empids:empids+""},
		    	type:"post",
		    	dataType:"json",
		    	success:function(data){
		    		hidetip();
		    		if (data.errMsg != null) {
		    			MyExt.Msg.alert(data.errMsg);
	    				return;
		    		}
		    		//刷新GRID
		    		Foreman.NoDispatcherGrid.store.load();
		    		Foreman.grid.store.load();
	  		
		    		WorkStationEmp.selectWin.hide();
		    		alertSuccess();
					Ext.getCmp("Baserdp_combo").cascadeStore();
					Ext.getCmp("Baserdp_combo2").cascadeStore();
		    	}
		    });
		}
		//全部派工
		else if(isFullDipatch == 'true'){
			var searchParam = Foreman.NoDispatcherSearchForm.getForm().getValues();
            searchParam = MyJson.deleteBlankProp(searchParam);
            searchParam.haveDefaultPerson = "0";
			searchParam.workStationBelongTeam = teamOrgId;
			var entityJson = Ext.util.JSON.encode(searchParam);
			showtip();
		    Ext.Ajax.request({
	            url:  ctx + "/workCard!foremanAllDispatcher.action",
	            timeout: 6000000,
	            params:{entityJson:entityJson, empids:empids+""},
	            success: function(response, options){              	
	                var result = Ext.util.JSON.decode(response.responseText);	                
	                if (result.errMsg == null) {
			    		hidetip();
			    		//刷新GRID
			    		Foreman.NoDispatcherGrid.store.load();
			    		Foreman.grid.store.load();
			    		WorkStationEmp.selectWin.hide();
			    		alertSuccess();
			    		Ext.getCmp("Baserdp_combo").reset();
						Ext.getCmp("Baserdp_combo").clearValue();
						Ext.getCmp("Baserdp_combo").cascadeStore();
						Ext.getCmp("Baserdp_combo2").reset();
						Ext.getCmp("Baserdp_combo2").clearValue();
						Ext.getCmp("Baserdp_combo2").cascadeStore();
	                }else{
	                	alertFail(result.errMsg);
	                }
	            },
	            failure: function(response, options){	                    
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	       });
		}
	}
	
	//页面自适应布局
	var viewport = new Ext.Viewport({layout:"fit", items:[ Foreman.tabs ]});
	
	//初始化加载第一条兑现单数据，并load Grid
	function loadFirst(store, record, options){
		if(record.length > 0){
			Ext.getCmp("Baserdp_combo").setDisplayValue(record[0].get("idx"), record[0].get("groupRdpInfo"));
			var searchParam = Foreman.NoDispatcherSearchForm.getForm().getValues();
            searchParam = MyJson.deleteBlankProp(searchParam);
			Foreman.NoDispatcherGrid.searchFn(searchParam); 
			Ext.getCmp("Baserdp_combo").getStore().un("load", loadFirst)
			var rdpIdx = record[0].get("idx"); //兑现单的Idx, 联动触发流程节点名称选择控件的值重加载
			var rootName = record[0].get("trainTypeShortName");
			var tpn = Ext.getCmp("tecprocessnode_node");
			tpn.reset(); 
			tpn.clearValue();
			tpn.rootText = rootName;
			Ext.getCmp("tecprocessnode_node").queryParams = {'workPlanIDX':rdpIdx,'isDispatcher':'n'};
			//重新加载位置数据
//			Ext.getCmp("position").reset();
//            Ext.getCmp("position").clearValue();
//            Ext.getCmp("position").tree.root.setText(record[0].get("buildupTypeName"));
//            Ext.getCmp("position").partsBuildUpTypeIdx = record[0].get("buildUpTypeIDX");
//            Ext.getCmp("position").tree.root.attributes["buildUpPlaceCode"] = record[0].get("buildUpTypeCode");
			
		}
	}
	Ext.getCmp("Baserdp_combo").getStore().on("load", loadFirst);
	Ext.getCmp("Baserdp_combo").getStore().load();
});