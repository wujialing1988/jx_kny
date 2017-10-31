/**
 * 提票质量检查 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FaultQCResult');                       //定义命名空间
	FaultQCResult.searchLabelWidth = 90;
	FaultQCResult.searchAnchor = '95%';
	FaultQCResult.searchFieldWidth = 270;
	
	/*** 必检查询表单 start ***/	
	FaultQCResult.searchBJParam = {};
	FaultQCResult.BJSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: FaultQCResult.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultQCResult.searchFieldWidth, labelWidth: FaultQCResult.searchLabelWidth, defaults:{anchor:FaultQCResult.searchAnchor},
				items:[{ 
					xtype: "Base_combo",
					fieldLabel: "车型",
					hiddenName: "trainTypeShortName",
				    business: 'trainVehicleType',
				    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		            fields:['idx','typeName','typeCode'],
		            returnField:[{widgetId:"trainTypeShortName",propertyName:"typeCode"}],
		            queryParams: {'vehicleType':vehicleType},// 表示客货类型
				    displayField: "typeCode", valueField: "typeCode",
		            pageSize: 20, minListWidth: 200,
		            editable:false,						
					listeners : {   
			        	"select" : function(me, record, index) {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = FaultQCResult.BJSearchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = record.data.idx ;
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultQCResult.searchFieldWidth, labelWidth: FaultQCResult.searchLabelWidth, defaults:{anchor:FaultQCResult.searchAnchor},
				items:[{
					id: "trainNo_comb_search",
					fieldLabel: "车号",
    				xtype: "Base_combo",
    				name:'trainNo',
    				hiddenName: "trainNo",
    			    business: 'jczlTrain',
    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "trainNo", valueField: "trainNo",
                    pageSize: 20, minListWidth: 200,
                    disabled:false,
                    editable:true
				},{ fieldLabel: "质量检验项", name: 'checkItemName', xtype: 'textfield'}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = FaultQCResult.BJSearchForm.getForm();	
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					FaultQCResult.BJGrid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = FaultQCResult.BJSearchForm;
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
	            	FaultQCResult.BJGrid.searchFn(searchParam);
	            }
			}]
	});
	
	/*** 必检查询表单 end ***/
	/*** 必检列表 start ***/
	FaultQCResult.BJGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultQCResultQuery!getQCPageList.action',                 //装载列表数据的请求URL
	    viewConfig: null,
	    tbar:[{
	    	text:'批量质检', iconCls:'addIcon', handler:function(){
	    		var grid = FaultQCResult.BJGrid;
	    		if(!$yd.isSelectedRecord(grid)) return;
	    		FaultQCResult.QCBatchSubmit(FaultQCResult.BJGrid);
	    	}
	    },'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'操作', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
			renderer:function(v,x,r,rowIndex){	
				return "<img src='" + img + "' alt='操作' style='cursor:pointer' onclick='FaultQCResult.showWin(\"" + rowIndex + "\",1)'/>";
			}
		},
		{
			header:'提票单号', dataIndex:'ticketCode', editor:{  maxLength:50 }
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
		},{
			header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
		},{
			header:'质量检验项', dataIndex:'checkItemName', editor:{  maxLength:8 }
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', editor:{  maxLength:500 }, width : 300
		},{
			header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
		},{
			header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
		},{
			header:'提票类型', dataIndex:'type', editor:{  maxLength:25 }, width : 150
		},{
			header:'提票人', dataIndex:'ticketEmp', editor:{  maxLength:25 }, width : 80
		},{
			header:'提票时间', dataIndex:'ticketTime', width : 150
		},{
			header:'故障发生日期', dataIndex:'faultOccurDate', hidden: true 
		},{
			header:'处理方法', dataIndex:'methodName', hidden:true
		},{
			header:'处理方法描述', dataIndex:'methodDesc', hidden:true
		},{
			header:'处理结果', dataIndex:'repairResult', hidden:true
		},{
			header:'修竣时间', dataIndex:'completeTime', hidden: true
		},{
			header:'处理人', dataIndex:'completeEmp', hidden:true
		}],
		toEditFn: function(grid, rowIndex, e) {
			var r = grid.store.getAt(rowIndex);
			FaultQCResultWin.showWin(r);
		},
	    searchFn: function(searchParam){
	    	FaultQCResult.searchBJParam = searchParam;
	    	this.store.load();
	    }
	});
	FaultQCResult.BJGrid.store.on("beforeload", function(){		
		FaultQCResult.searchBJParam.vehicleType = vehicleType ;
		var sp = FaultQCResult.searchBJParam;
		if(sp){
			this.baseParams.query = Ext.util.JSON.encode(sp);
		}
		this.baseParams.mode = CONST_INT_CHECK_WAY_BJ;
	});
	
	/*** 必检列表 end ***/
	
	/*** 抽检查询表单 start ***/	
	FaultQCResult.searchCJParam = {};
	FaultQCResult.CJSearchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: FaultQCResult.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultQCResult.searchFieldWidth, labelWidth: FaultQCResult.searchLabelWidth, defaults:{anchor:FaultQCResult.searchAnchor},
				items:[{ 
					xtype: "Base_combo",
					fieldLabel: "车型",
					hiddenName: "trainTypeShortName",
				    business: 'trainVehicleType',
				    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		            fields:['idx','typeName','typeCode'],
		            returnField:[{widgetId:"trainTypeShortName",propertyName:"typeCode"}],
		            queryParams: {'vehicleType':vehicleType},// 表示客货类型
				    displayField: "typeCode", valueField: "typeCode",
		            pageSize: 20, minListWidth: 200,
		            editable:false,						
					listeners : {   
			        	"select" : function(me, record, index) {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = FaultQCResult.CJSearchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = record.data.idx ;
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: FaultQCResult.searchFieldWidth, labelWidth: FaultQCResult.searchLabelWidth, defaults:{anchor:FaultQCResult.searchAnchor},
				items:[{
					id: "trainNo_comb_search1",
					fieldLabel: "车号",
    				xtype: "Base_combo",
    				name:'trainNo',
    				hiddenName: "trainNo",
    			    business: 'jczlTrain',
    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "trainNo", valueField: "trainNo",
                    pageSize: 20, minListWidth: 200,
                    disabled:false,
                    editable:true
				},{ fieldLabel: "质量检验项", name: 'checkItemName', xtype: 'textfield'}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = FaultQCResult.CJSearchForm.getForm();	
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search1").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search1").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					FaultQCResult.CJGrid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = FaultQCResult.CJSearchForm;
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
	            	FaultQCResult.CJGrid.searchFn(searchParam);
	            }
			}]
	});
	
	/*** 抽检查询表单 end ***/
	/*** 抽检列表 start ***/
	FaultQCResult.CJGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultQCResultQuery!getQCPageList.action',                 //装载列表数据的请求URL
	    viewConfig: null,
	    tbar:[{
	    	text:'批量质检', iconCls:'addIcon', handler:function(){
	    		var grid = FaultQCResult.CJGrid;
	    		if(!$yd.isSelectedRecord(grid)) return;
				FaultQCResult.QCBatchSubmit(FaultQCResult.CJGrid);
	    	}
	    },'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'操作', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
			renderer:function(v,x,r,rowIndex){	
				return "<img src='" + img + "' alt='操作' style='cursor:pointer' onclick='FaultQCResult.showWin(\"" + rowIndex + "\",0)'/>";
			}
		},
		{
			header:'提票单号', dataIndex:'ticketCode', editor:{  maxLength:50 }
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
		},{
			header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
		},{
			header:'质量检验项', dataIndex:'checkItemName', editor:{  maxLength:8 }
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', editor:{  maxLength:500 }, width : 300
		},{
			header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
		},{
			header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
		},{
			header:'提票类型', dataIndex:'type', editor:{  maxLength:25 }, width : 150
		},{
			header:'提票人', dataIndex:'ticketEmp', editor:{  maxLength:25 }, width : 80
		},{
			header:'提票时间', dataIndex:'ticketTime', width : 150
		},{
			header:'故障发生日期', dataIndex:'faultOccurDate', hidden: true 
		},{
			header:'处理方法', dataIndex:'methodName', hidden:true
		},{
			header:'处理方法描述', dataIndex:'methodDesc', hidden:true
		},{
			header:'处理结果', dataIndex:'repairResult', hidden:true
		},{
			header:'修竣时间', dataIndex:'completeTime', hidden: true
		},{
			header:'处理人', dataIndex:'completeEmp', hidden:true
		}],
		toEditFn: function(grid, rowIndex, e) {
			var r = grid.store.getAt(rowIndex);
			FaultQCResultWin.showWin(r);
		},
	    searchFn: function(searchParam){
	    	FaultQCResult.searchCJParam = searchParam;
	    	this.store.load();
	    }
	});
	FaultQCResult.CJGrid.store.on("beforeload", function(){		
		FaultQCResult.searchCJParam.vehicleType = vehicleType ;
		var sp = FaultQCResult.searchCJParam;
		if(sp){
			this.baseParams.query = Ext.util.JSON.encode(sp);
		}
		this.baseParams.mode = CONST_INT_CHECK_WAY_CJ;
	});
	
	/*** 抽检列表 end ***/
	
	FaultQCResult.showWin = function(rowIndex, mode) {
		var grid;
		if (mode == 1)
			grid = FaultQCResult.BJGrid;
		else if (mode == 0)
			grid = FaultQCResult.CJGrid;
		var record = grid.store.getAt(rowIndex);
		FaultQCResultWin.showWin(record);
	}
	
	FaultQCResult.QCBatchSubmit = function(grid){
		Ext.Msg.confirm("提示","确认提交", function(btn){
			if(btn == "yes"){
				var records = grid.selModel.getSelections();
				var qcDatas = [];
				for(var i = 0; i < records.length; i++){
					qcDatas.push({
						idx: records[i].get("idx"),
						qcEmpID: empid,
						qcEmpName: uname,
						qcTime: new Date()						
					});
				}
				Ext.Ajax.request({
					url: ctx +"/faultQCResult!updateFinishQCResult.action",
			        jsonData: qcDatas,
			        success: function(response, options){
			        	var result = Ext.util.JSON.decode(response.responseText);
			            if (result.success) {
			            	alertSuccess();
			            	FaultQCResultWin.reloadGrid();
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			        	MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    	}
				});
			}
		});
	}
	
	FaultQCResult.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "必检", border: false, xtype: "panel", layout: "border", 
	            items: [{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 172, bodyBorder: false,
		            items:[FaultQCResult.BJSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultQCResult.BJGrid ]
		        }]
	        },{
	            title: "抽检", border: false, xtype: "panel", layout: "border", 
	            items:[{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 172, bodyBorder: false,
		            items:[FaultQCResult.CJSearchForm], frame: true, title: "查询", xtype: "panel"
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultQCResult.CJGrid ]
		        }]
	        }]
	});
	var viewport = new Ext.Viewport({layout:"fit", items:[ FaultQCResult.tabs ]});
});