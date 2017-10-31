/**
 * 提票查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FaultTicket');                       //定义命名空间
FaultTicket.searchParam = {};
/*** 查询表单 start ***/
FaultTicket.searchLabelWidth = 90;
FaultTicket.searchAnchor = '95%';
FaultTicket.searchFieldWidth = 270;

FaultTicket.status = STATUS_DRAFT + "," + STATUS_OPEN + "," + STATUS_OVER ;

FaultTicket.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: FaultTicket.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.25,
			fieldWidth: FaultTicket.searchFieldWidth, labelWidth: FaultTicket.searchLabelWidth, defaults:{anchor:FaultTicket.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				xtype: "Base_combo",
				hiddenName: "trainTypeIDX",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode'],
                returnField:[{widgetId:"trainTypeShortName",propertyName:"typeCode"}],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                editable:false,
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = FaultTicket.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.vehicleType = vehicleType ;
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
    		},{ id:'type',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'type',
				dicttypeid:'JCZL_FAULT_TYPE',
				displayField:'dictname',valueField:'dictname',
				width: FaultTicket.fieldWidth,
				fieldLabel: "提票类型"
//			},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.25,
			fieldWidth: FaultTicket.searchFieldWidth, labelWidth: FaultTicket.searchLabelWidth, defaults:{anchor:FaultTicket.searchAnchor},
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
			},{
				xtype:'checkboxgroup',    
	            fieldLabel:'提票状态',    
	            columns:3,//3列    
	            items:[{   
				    xtype:'checkbox', name:'status', id: 'STATUS_DRAFT', boxLabel:'未处理&nbsp;&nbsp;&nbsp;&nbsp;', 
				    	inputValue:STATUS_DRAFT, checked:true,
					    handler: function(){
					    	FaultTicket.checkQuery(STATUS_DRAFT);
					    }
				  },{   
				    xtype:'checkbox', name:'status', id: 'STATUS_OPEN', boxLabel:'处理中&nbsp;&nbsp;&nbsp;&nbsp;', 
				    	inputValue:STATUS_OPEN,checked:true,
					    handler: function(){
					    	FaultTicket.checkQuery(STATUS_OPEN);
					    }
				  },{   
				    xtype:'checkbox', name:'status', id: 'STATUS_OVER', boxLabel:'已处理&nbsp;&nbsp;&nbsp;&nbsp;', 
				    	inputValue:STATUS_OVER,checked:true,
					    handler: function(){
					    	FaultTicket.checkQuery(STATUS_OVER);
					    }
				  }
            	]    
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.25,
			fieldWidth: FaultTicket.searchFieldWidth, labelWidth: FaultTicket.searchLabelWidth, defaults:{anchor:FaultTicket.searchAnchor},
           	items:[{
				fieldLabel: "不良状态", name: 'faultDesc', xtype: 'textfield'
		
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = FaultTicket.searchForm.getForm();	
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				FaultTicket.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = FaultTicket.searchForm;
            	form.getForm().reset();
            	Ext.getCmp('type').clearValue();
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
                var trainNo_comb = FaultTicket.searchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeIDX;
                trainNo_comb.cascadeStore();	
            	searchParam = {};
            	FaultTicket.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/

/*** 提票列表 start ***/
FaultTicket.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/faultTicket!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/faultTicket!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/faultTicket!logicDelete.action',            //删除数据的请求URL
    viewConfig: null,
    tbar:['refresh',{
     text: "导出", iconCls: "resetIcon", handler: function(){ 
    	var url = ctx + "/faultTicket!exportFaultTicketListByParm.action";
    	var params = [];
        var grid = FaultTicket.grid;      
       var form = FaultTicket.searchForm.getForm();	
        var searchParam = form.getValues();
        searchParam.status = FaultTicket.status ;
        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
			searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
		}
		searchParam.vehicleType = vehicleType ;
        searchParam = MyJson.deleteBlankProp(searchParam);
        for(var p in searchParam){
        	data = {name : p,value : searchParam[p]}
        	params.push(data);
        }  
    
        params.push(getPatterns(grid,"patterns"));
    	exportExcel(url,params);
     }
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
//	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
	{
		header:'提票单号', dataIndex:'ticketCode',hidden:true,  editor:{  maxLength:50 }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
	},{
		header:'提票类型', dataIndex:'type', editor:{  maxLength:20 }, width : 150
	},{
		header:'故障位置', dataIndex:'fixPlaceFullName', hidden:true, editor:{  maxLength:500 }, width : 300
	},/*{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},*/{
		header:'不良状态描述', dataIndex:'faultDesc', width : 400,  editor:{  maxLength:500 }
//	},{
//		header:'故障原因', dataIndex:'faultReason', editor:{  maxLength:100 }
	},{
		header:'标签id', dataIndex:'reasonAnalysisId', hidden:true
	},{
		header:'标签', dataIndex:'reasonAnalysis', width: 350, editor:{  maxLength:100 }
	},{
		header:'处理方法', dataIndex:'methodName',hidden:true,  width: 350, editor:{  maxLength:100 }
	},{
		header:'处理方法描述', dataIndex:'methodDesc',hidden:true,  width: 350, editor:{  maxLength:100 }
	},{
		header:'施修方案', dataIndex:'faultReason',hidden:true,  width: 350, editor:{  maxLength:100 }
	
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
//	},{
//		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	},{
		header:'责任部门', dataIndex:'repairTeamName', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'处理人', dataIndex:'completeEmp', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'完成时间', dataIndex:'completeTime',  xtype:'datecolumn', format: "Y-m-d H:i:s",hidden:true, editor:{ xtype:'hidden', maxLength:50 }
//	},{
//		header:'专业类型', dataIndex:'professionalTypeName', editor:{  maxLength:100 }
	}
	],
    searchFn: function(searchParam){
    	FaultTicket.searchParam = searchParam;
    	this.store.load();
    },
   	toEditFn: function(grid, rowIndex, e){
		var r = grid.store.getAt(rowIndex);
		FaultTicketInfoWin.showWin(r);
    },
    sortInfo: {field: "status", direction: "ASC"}//默认按状态排序
});

//状态多选按钮
FaultTicket.checkQuery = function(status){
	FaultTicket.status = "-1";
	if(Ext.getCmp("STATUS_DRAFT").checked){
		FaultTicket.status = FaultTicket.status + "," + STATUS_DRAFT;
	} 
	if(Ext.getCmp("STATUS_OPEN").checked){
		FaultTicket.status = FaultTicket.status + "," + STATUS_OPEN;
	}
	if(Ext.getCmp("STATUS_OVER").checked){
		FaultTicket.status = FaultTicket.status + "," + STATUS_OVER;
	}
	FaultTicket.grid.store.load();
}
FaultTicket.grid.store.on("beforeload", function(){	
	var searchParam = FaultTicket.searchParam;
	delete searchParam.status ;
	var whereList = [] ;
	for (prop in searchParam) {			
        whereList.push({propName:prop, propValue: searchParam[prop],compare: Condition.EQ, stringLike: true}) ;
	}
	whereList.push({propName:'status', propValues: FaultTicket.status.split(",") ,compare : Condition.IN}) ;
	whereList.push({propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});
//	whereList.push({propName:'ticketEmpId', propValue: empid ,compare : Condition.EQ}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 界面布局 start ***/
FaultTicket.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, collapsed: false,  height: 162, bodyBorder: false,
        items:[FaultTicket.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultTicket.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:FaultTicket.panel });
});