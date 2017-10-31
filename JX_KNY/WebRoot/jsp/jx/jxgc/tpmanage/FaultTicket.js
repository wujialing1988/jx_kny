/**
 * 提票 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FaultTicket');                       //定义命名空间
FaultTicket.searchParam = {};
/*** 查询表单 start ***/
FaultTicket.searchLabelWidth = 90;
FaultTicket.searchAnchor = '95%';
FaultTicket.searchFieldWidth = 270;
FaultTicket.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: FaultTicket.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
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
			},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
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
    tbar:[{
    	text:'故障提票', iconCls:'addIcon', handler:function(){
    		FaultTicket.showSaveWin();
    	}
    },'delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
//	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
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
	},/*{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},*/{
		header:'故障现象', dataIndex:'faultDesc', editor:{  maxLength:500 }
	},{
		header:'提票类型', dataIndex:'type', editor:{  maxLength:20 }, width : 100
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
		header:'发现人', dataIndex:'discover', editor:{  maxLength:25 }, width : 80
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	}
	],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	FaultTicket.searchParam = searchParam;
    	this.store.load();
    },
	beforeDeleteFn: function(){  
		var data = FaultTicket.grid.selModel.getSelections();
		for (var i = 0; i < data.length; i++){
	        if (!Ext.isEmpty(data[ i ].get("status")) && data[ i ].get("status") != STATUS_DRAFT) {
	        	MyExt.Msg.alert("只能删除未处理状态的提票记录");
	        	return false;
	        }
	    }
        return true;
    },
    sortInfo: {field: "status", direction: "ASC"}//默认按状态排序
});
FaultTicket.grid.store.on("beforeload", function(){	
	var searchParam = FaultTicket.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	}
	var statusArray = [];
	statusArray.push(STATUS_DRAFT);
	statusArray.push(STATUS_OPEN);
	whereList.push({propName:'status', propValues: statusArray ,compare : Condition.IN}) ;
	whereList.push({propName:'ticketEmpId', propValue: empid ,compare : Condition.EQ}) ;
	whereList.push({propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 提票详细信息window end ***/
FaultTicket.editWin = new Ext.Window({
	title: "故障提票编辑", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: [FaultTicketWin.panel]
});
FaultTicketWin.closeWin = function() {
	FaultTicket.editWin.hide();
	FaultTicket.saveWin.hide();
	FaultTicket.grid.store.load();
}
/*** 提票详细信息window end ***/

/*** 提票window start ***/
FaultTicket.saveForm = new Ext.form.FormPanel({
    layout: "form",     border: false,      style: "padding:10px",      labelWidth: FaultTicket.grid.labelWidth,
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "98%" },
    items: [{xtype:"panel", border:false, baseCls:"x-plain", layout:"column", align:"center", 
    		items:[{
		    	baseCls:"x-plain", align:"center", style:"padding:3px", layout:"form", defaultType:"textfield", 
		    	labelWidth:FaultTicket.grid.labelWidth, columnWidth: 1, 
		    	items:[{
						allowBlank:false,
						xtype: 'EosDictEntry_combo',
						hiddenName: 'type',
						dicttypeid:'JCZL_FAULT_TYPE',
						displayField:'dictname',valueField:'dictname',
						width: FaultTicket.grid.fieldWidth,
						fieldLabel: "提票类型"
		    		},{
	    				fieldLabel: "车型",
	    				xtype: "Base_combo",
	    				hiddenName: "trainTypeIDX",
	    			    business: 'trainVehicleType',
	    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
	                    fields:['idx','typeName','typeCode'],
	                    returnField:[{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
	                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
	        		    displayField: "typeCode", valueField: "idx",
	                    pageSize: 20, minListWidth: 200,
	                    width: FaultTicket.grid.fieldWidth,
	                    allowBlank: false,
	                    editable:false,	
						listeners : {   
			                "collapse" : function() {   
			                   //重新加载车号数据
			                	var trainNo_combo = FaultTicket.saveForm.getForm().findField("trainNo");
			                    trainNo_combo.reset();
			                    trainNo_combo.clearValue();
			                    trainNo_combo.queryParams.vehicleType = vehicleType ;
			                    trainNo_combo.queryParams.trainTypeIDX = this.getValue();
			                    trainNo_combo.cascadeStore();
			                }   
			            }
		    		},{    
	    				fieldLabel: "车号",
	    				width:FaultTicket.grid.fieldWidth,
	    				xtype: "Base_combo",
	    				name:'trainNo',
	    				hiddenName: "trainNo",
	    			    business: 'jczlTrain',
	    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
	                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
	                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
	        		    displayField: "trainNo", valueField: "trainNo",
	                    pageSize: 20, minListWidth: 200,
	                    allowBlank: false,
	                    disabled:false,
	                    editable:true			              
					},{ id:'trainTypeShortNameId', xtype:'hidden', name: 'trainTypeShortName' },
					 { id:'buildUpTypeIDXId', xtype:'hidden', name: 'buildUpTypeIDX' },
					 { id:'buildUpTypeCodeId', xtype:'hidden', name: 'buildUpTypeCode' },
					 { id:'buildUpTypeNameId', xtype:'hidden', name: 'buildUpTypeName' },
					 { id:'vehicleType', xtype:'hidden', name: 'vehicleType',value:vehicleType }
					]
    		}
    ]}]
});
FaultTicket.saveWin = new Ext.Window({
    title:"故障提票", width: (FaultTicket.grid.labelWidth + FaultTicket.grid.fieldWidth + 8) + 60,
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
    items:FaultTicket.saveForm, 
    buttons: [{
        text: "确定", iconCls: "saveIcon", handler: function() {        	
		    var form = FaultTicket.saveForm.getForm(); 
		    if (!form.isValid()) return;
		    var data = form.getValues();	    
		    trainTypeIDX = data.trainTypeIDX;
		    trainTypeShortName = data.trainTypeShortName;
		    trainNo = data.trainNo; 
		    type = data.type;
		    FaultTicket.editWin.show();
		    FaultTicketWin.vehicleType = vehicleType;
		    FaultTicketWin.initSaveWin();
        }
    }, {
        text: "取消", iconCls: "closeIcon", handler: function(){ FaultTicket.saveWin.hide(); }
    }]
});
FaultTicket.showSaveWin = function(rowIndex) {
	FaultTicket.saveWin.show();
	
}
/*** 临碎修提票window end ***/

/*** 界面布局 start ***/
FaultTicket.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, collapsed: true,  height: 172, bodyBorder: false,
        items:[FaultTicket.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultTicket.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:FaultTicket.panel });
});