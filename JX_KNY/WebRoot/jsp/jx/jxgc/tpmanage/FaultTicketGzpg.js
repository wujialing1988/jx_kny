var processTips;
function showtip(){
	processTips = new Ext.LoadMask(FaultRepairEmp.selectWin.getEl(),{
		msg:"正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}

function hidetip(){
	processTips.hide();
}
Ext.onReady(function(){
	Ext.Ajax.timeout = 10000000000000;
	Ext.namespace('FaultTicketGzpg');                       //定义命名空间	
	FaultTicketGzpg.searchLabelWidth = 90;
	FaultTicketGzpg.searchAnchor = '95%';
	FaultTicketGzpg.searchFieldWidth = 270;
	FaultTicketGzpg.randomGrid;								//存放未派工或已派工Grid
	
	//flag 区分是否已派工列表，mode区分是否批量派工TODO有问题
	FaultTicketGzpg.dispatcher = function(idx, flag, mode) {
		FaultRepairEmp.idx = "";
		if (mode) { // 批量派工
			FaultRepairEmp.idx = "";
		} else {// 非批量派工
			FaultRepairEmp.idx = idx;
		}	
		
		//取得操作的Grid
		if(flag == 1){
			FaultTicketGzpg.randomGrid = FaultTicketGzpg.grid;		
		}else if (flag == 0) {
			FaultTicketGzpg.randomGrid = FaultTicketGzpg.NoDispatcherGrid;
		}
		//重新加载Grid
		FaultRepairEmp.grid.store.load();
		FaultRepairEmp.NoDispatchGrid.store.load();	
		FaultRepairEmp.selectWin.show();	
	}
	
	/*** 未派工查询表单 start ***/	
	FaultTicketGzpg.searchNoDispatchParam = {};
	FaultTicketGzpg.NoDispatcherSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: FaultTicketGzpg.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultTicketGzpg.searchFieldWidth, labelWidth: FaultTicketGzpg.searchLabelWidth, defaults:{anchor:FaultTicketGzpg.searchAnchor},
				items:[{ 
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 200,
					editable:true,
					forceSelection: true,
					xtype: "Base_combo",
		        	business: 'trainType',													
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = FaultTicketGzpg.NoDispatcherSearchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultTicketGzpg.searchFieldWidth, labelWidth: FaultTicketGzpg.searchLabelWidth, defaults:{anchor:FaultTicketGzpg.searchAnchor},
				items:[{
					id: "trainNo_comb_search",
					fieldLabel: "车号",
					hiddenName: "trainNo", 
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200,
					minChars : 1, 
					maxLength : 5,
					anchor: "95%", 				
					xtype: "Base_combo",
					business: 'trainNo',
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
					fields:["trainNo","makeFactoryIDX","makeFactoryName",
					{name:"leaveDate", type:"date", dateFormat: 'time'},
					"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
					"bId","dId","bName","dName","bShortName","dShortName"],
					queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
					isAll: 'yes',
					editable:true
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = FaultTicketGzpg.NoDispatcherSearchForm.getForm();	
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					FaultTicketGzpg.NoDispatcherGrid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = FaultTicketGzpg.NoDispatcherSearchForm;
	            	form.getForm().reset();
	            	//清空自定义组件的值
	                var componentArray = ["Base_combo"];
	                for (var j = 0; j < componentArray.length; j++) {
	                	var component = form.findByType(componentArray[j]);
	                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
							for (var i = 0; i < component.length; i++) {
								component[i].clearValue();
							}						
						}	                    
	                }
	                var trainNo_comb = form.getForm().findField("trainNo");   
	                delete trainNo_comb.queryParams.trainTypeIDX;
	                trainNo_comb.cascadeStore();
	            	searchParam = {};
	            	FaultTicketGzpg.NoDispatcherGrid.searchFn(searchParam);
	            }
			}]
	});
	
	/*** 未派工查询表单 end ***/
	/*** 未派工列表 start ***/ 
	FaultTicketGzpg.NoDispatcherGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultTicket!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: null,
	    tbar:[{
	    	text:'派工', iconCls:'addIcon', handler:function(){
	    		var grid = FaultTicketGzpg.NoDispatcherGrid;
	    		if(!$yd.isSelectedRecord(grid)) return;	    		
	    		FaultTicketGzpg.dispatcher(null,0,true);
	    	}
	    },'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'派工', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
			renderer:function(v,x,r){			
				return "<img src='" + img + "' alt='派工' style='cursor:pointer' onclick='FaultTicketGzpg.dispatcher(\"" + v + "\",0)'/>";
			}
		},
		{
			header:'提票单号', dataIndex:'ticketCode', editor:{  maxLength:50 }
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
		},{
			header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', editor:{  maxLength:500 }, width : 300
		},{
			header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
		},{
			header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
		},{
			header:'提票类型', dataIndex:'type', editor:{  maxLength:25 }, width : 150
		},{
			header:'提票状态', dataIndex:'status', editor:{  maxLength:20 },
	        renderer: function(v){
	            switch(v){
	                case STATUS_DRAFT:
	                    return STATUS_DRAFT_CH;
	                case STATUS_OPEN:
	                    return STATUS_OPEN_CH;
	                case STATUS_OVER:
	                    return STATUS_OVER_CH;
	                default:
	                    return v;
	            }
	        }
		},{
			header:'提票人', dataIndex:'ticketEmp', editor:{  maxLength:25 }, width : 80
		},{
			header:'提票时间', dataIndex:'ticketTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
		},{
			header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
		},{
			header:'故障位置编码', dataIndex:'fixPlaceFullCode', hidden:true, editor:{  maxLength:200 }
		},{
			header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
		},{
			header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
		},{
			header:'提票人编码', dataIndex:'ticketEmpId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'处理班组编码', dataIndex:'repairTeam', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'处理班组序列', dataIndex:'repairTeamOrgseq', hidden:true, editor:{  maxLength:300 }
		},{
			header:'施修方法', dataIndex:'methodDesc', hidden:true, editor:{  maxLength:200 }
		},{
			header:'处理结果', dataIndex:'repairResult', hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
		},{
			header:'销票人编码', dataIndex:'completeEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'销票人名称', dataIndex:'completeEmp', hidden:true, editor:{  maxLength:25 }
		}],
		toEditFn: function(grid, rowIndex, e) {
			var r = grid.store.getAt(rowIndex);
			FaultTicketGzpg.dispatcher(r.get("idx"),0);
			return false;
		},
	    searchFn: function(searchParam){
	    	FaultTicketGzpg.searchNoDispatchParam = searchParam;
	    	this.store.load();
	    }
	});
	FaultTicketGzpg.NoDispatcherGrid.store.on("beforeload", function(){	
		var searchParam = FaultTicketGzpg.searchNoDispatchParam;
		searchParam.repairTeam = teamOrgId;
		var whereList = [] ;
		var statusArray = [];
		statusArray.push(STATUS_DRAFT);
		statusArray.push(STATUS_OPEN);
		whereList.push({propName:'status', propValues: statusArray ,compare : Condition.IN}) ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}		
		var sqlStr = " DISPATCH_EMP_ID IS NULL" ;
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	/*** 未派工列表 end ***/
	
	/*** 已派工查询表单 start ***/	
	FaultTicketGzpg.searchParam = {};
	FaultTicketGzpg.DispatcherSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: FaultTicketGzpg.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultTicketGzpg.searchFieldWidth, labelWidth: FaultTicketGzpg.searchLabelWidth, defaults:{anchor:FaultTicketGzpg.searchAnchor},
				items:[{ 
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 200,
					editable:true,
					forceSelection: true,
					xtype: "Base_combo",
		        	business: 'trainType',													
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = FaultTicketGzpg.DispatcherSearchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultTicketGzpg.searchFieldWidth, labelWidth: FaultTicketGzpg.searchLabelWidth, defaults:{anchor:FaultTicketGzpg.searchAnchor},
				items:[{
					id: "trainNo_comb_search1",
					fieldLabel: "车号",
					hiddenName: "trainNo", 
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200,
					minChars : 1,
	//				minLength : 4, 
					maxLength : 5,
	//				vtype: "numberInt",
					anchor: "95%", 				
					xtype: "Base_combo",
					business: 'trainNo',
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
					fields:["trainNo","makeFactoryIDX","makeFactoryName",
					{name:"leaveDate", type:"date", dateFormat: 'time'},
					"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
					"bId","dId","bName","dName","bShortName","dShortName"],
					queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
					isAll: 'yes',
					editable:true
				},{
					id: "repairEmp_search",
					fieldLabel:"作业人员",
		    		xtype: 'TeamEmployee_SelectWin',
		    		hiddenName: 'repairEmp',
		    		displayField:'empname',
		    		valueField:'empname',
		    		editable:true,
		    		orgid:teamOrgId		    		
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = FaultTicketGzpg.DispatcherSearchForm.getForm();	
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search1").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search1").dom.value;
					}
					if (!Ext.isEmpty(Ext.get("repairEmp_search").dom.value)) {
						searchParam.repairEmp = Ext.get("repairEmp_search").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					FaultTicketGzpg.grid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = FaultTicketGzpg.DispatcherSearchForm;
	            	form.getForm().reset();
	            	//清空自定义组件的值
	                var componentArray = ["Base_combo","TeamEmployee_SelectWin"];
	                for (var j = 0; j < componentArray.length; j++) {
	                	var component = form.findByType(componentArray[j]);
	                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
							for (var i = 0; i < component.length; i++) {
								component[i].clearValue();
							}						
						}	                    
	                }
	                var trainNo_comb = form.getForm().findField("trainNo");   
	                delete trainNo_comb.queryParams.trainTypeIDX;
	                trainNo_comb.cascadeStore();
	            	searchParam = {};
	            	FaultTicketGzpg.grid.searchFn(searchParam);
	            }
			}]
	});
	
	/*** 已派工查询表单 end ***/
	/*** 已派工列表 start ***/ 
	FaultTicketGzpg.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultTicket!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: null,
	    tbar:[{
	    	text:'派工', iconCls:'addIcon', handler:function(){
	    		var grid = FaultTicketGzpg.grid;
	    		if(!$yd.isSelectedRecord(grid)) return;
	    		FaultTicketGzpg.dispatcher(null,1,true);
	    	}
	    },'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'派工', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
			renderer:function(v,x,r){			
				return "<img src='" + img + "' alt='派工' style='cursor:pointer' onclick='FaultTicketGzpg.dispatcher(\"" + v + "\",1)'/>";
			}
		},
		{
			header:'提票单号', dataIndex:'ticketCode', editor:{  maxLength:50 }
		},{
			header:'作业人员', dataIndex:'dispatchEmp'
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
		},{
			header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', editor:{  maxLength:500 }, width : 300
		},{
			header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
		},{
			header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
		},{
			header:'提票类型', dataIndex:'type', editor:{  maxLength:25 }, width : 150
		},{
			header:'提票状态', dataIndex:'status', editor:{  maxLength:20 },
	        renderer: function(v){
	            switch(v){
	                case STATUS_DRAFT:
	                    return STATUS_DRAFT_CH;
	                case STATUS_OPEN:
	                    return STATUS_OPEN_CH;
	                case STATUS_OVER:
	                    return STATUS_OVER_CH;
	                default:
	                    return v;
	            }
	        }
		},{
			header:'提票人', dataIndex:'ticketEmp', editor:{  maxLength:25 }, width : 80
		},{
			header:'提票时间', dataIndex:'ticketTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
		},{
			header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
		},{
			header:'故障位置编码', dataIndex:'fixPlaceFullCode', hidden:true, editor:{  maxLength:200 }
		},{
			header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
		},{
			header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
		},{
			header:'提票人编码', dataIndex:'ticketEmpId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'处理班组编码', dataIndex:'repairTeam', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'处理班组序列', dataIndex:'repairTeamOrgseq', hidden:true, editor:{  maxLength:300 }
		},{
			header:'施修方法', dataIndex:'methodDesc', hidden:true, editor:{  maxLength:200 }
		},{
			header:'处理结果', dataIndex:'repairResult', hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
		},{
			header:'销票人编码', dataIndex:'completeEmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'销票人名称', dataIndex:'completeEmp', hidden:true, editor:{  maxLength:25 }
		}],
		toEditFn: function(grid, rowIndex, e) {
			var r = grid.store.getAt(rowIndex);
			FaultTicketGzpg.dispatcher(r.get("idx"),1);
			return false;			
		},
	    searchFn: function(searchParam){
	    	FaultTicketGzpg.searchParam = searchParam;
	    	this.store.load();
	    }
	});
	FaultTicketGzpg.grid.store.on("beforeload", function(){	
		var searchParam = FaultTicketGzpg.searchParam;
		searchParam.repairTeam = teamOrgId;
		var whereList = [] ;
		var statusArray = [];
		statusArray.push(STATUS_DRAFT);
		statusArray.push(STATUS_OPEN);
		whereList.push({propName:'status', propValues: statusArray ,compare : Condition.IN}) ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		
		var sqlStr = " DISPATCH_EMP_ID IS NOT NULL" ;
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	
	});
	
	/*** 已派工列表 end ***/
	
	FaultRepairEmp.submit = function(){
	    if(!FaultRepairEmp.grid.store.getAt(0)){
	    	MyExt.Msg.alert("尚未选择作业人员");
	    	return;
	    }
	    var empids = [];
	    var record = FaultRepairEmp.grid.store.getAt(0);
	    var i = 1;
	    while(record){
	    	empids.push(record.get("empid"));
	    	record = FaultRepairEmp.grid.store.getAt(i++);
	    }
	    
	    var ids = $yd.getSelectedIdx(FaultTicketGzpg.randomGrid);
	    showtip();
	    var cfg = {
	        url: ctx + "/faultTicket!updateForGzpg.action", 
	        params: {
	        	ids: ids,
	        	empids: Ext.util.JSON.encode(empids)
	        },
	        success: function(response, options){
	            hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.success == true ) {
	                FaultTicketGzpg.grid.store.load();
	                FaultTicketGzpg.NoDispatcherGrid.store.load();
	                alertSuccess();
	                FaultRepairEmp.selectWin.hide();				
	            } else {
	                alertFail(result.errMsg);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	FaultTicketGzpg.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "未派工", border: false, xtype: "panel", layout: "border", 
	            items: [{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 172, bodyBorder: false,
		            items:[FaultTicketGzpg.NoDispatcherSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultTicketGzpg.NoDispatcherGrid ]
		        }]
	        },{
	            title: "已派工", border: false, xtype: "panel", layout: "border", 
	            items:[{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 200, bodyBorder: false,
		            items:[FaultTicketGzpg.DispatcherSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultTicketGzpg.grid ]
		        }]
	        }]
	});
	var viewport = new Ext.Viewport({layout:"fit", items:[ FaultTicketGzpg.tabs ]});
});