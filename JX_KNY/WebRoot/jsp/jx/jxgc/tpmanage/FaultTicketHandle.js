/**
 * 提票处理 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
var processTips;
function showtip(o){
	var dom = o || Ext.getBody();		
	if(!o && !FaultTicketHandleWin.win.hidden){
		dom = FaultTicketHandleWin.win.getEl();
	}
	processTips = new Ext.LoadMask(dom,{
		msg:"正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}

function hidetip(){
	processTips.hide();
}
Ext.onReady(function(){
Ext.namespace('FaultTicketHandle');                       //定义命名空间
FaultTicketHandle.searchParam = {};
/*** 查询表单 start ***/
FaultTicketHandle.searchLabelWidth = 90;
FaultTicketHandle.searchAnchor = '95%';
FaultTicketHandle.searchFieldWidth = 270;
FaultTicketHandle.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: FaultTicketHandle.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: FaultTicketHandle.searchFieldWidth, labelWidth: FaultTicketHandle.searchLabelWidth, defaults:{anchor:FaultTicketHandle.searchAnchor},
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
		                var trainNo_comb = FaultTicketHandle.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			},{ fieldLabel: "提票单号", name: 'ticketCode', xtype: 'textfield'}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: FaultTicketHandle.searchFieldWidth, labelWidth: FaultTicketHandle.searchLabelWidth, defaults:{anchor:FaultTicketHandle.searchAnchor},
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
				var form = FaultTicketHandle.searchForm.getForm();	
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				FaultTicketHandle.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = FaultTicketHandle.searchForm;
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
            	FaultTicketHandle.grid.searchFn(searchParam);
            }
		}]
});

/*** 查询表单 end ***/

FaultTicketHandle.showHandleWin = function(rowIndex) {
	var record = FaultTicketHandle.grid.store.getAt(rowIndex);
	var cfg = {
        url: ctx + '/faultTicket!getWorkerAndQcByTP.action',
        params: {id: record.get("idx"), empid: empid},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
	            var workerList = null;
	            var qcList = null;
	            if (result.workerList)
	            	workerList = Ext.util.JSON.decode(result.workerList);
	            if (result.qcList)
	            	qcList = Ext.util.JSON.decode(result.qcList);
	            FaultTicketHandleWin.createForm(qcList, workerList);		            
	            FaultTicketHandleWin.createWin();
	            FaultTicketHandleWin.win.show();
	            var titleForm = FaultTicketHandleWin.titleForm.getForm();
		    	titleForm.reset();		    	
		    	titleForm.loadRecord(record);
		    	titleForm.findField("trainTypeTrainNo").setValue(record.get("trainTypeShortName") + "|" + record.get("trainNo"));
		    	titleForm.findField("faultOccurDate").setValue(new Date(record.get("faultOccurDate")).format('Y-m-d'));		    	
		    	
		    	FaultTicketHandleWin.baseForm.getForm().reset();
		    	FaultTicketHandleWin.baseForm.getForm().loadRecord(record);
		    	FaultTicketHandleWin.baseForm.getForm().findField("professionalTypeIdx").setDisplayValue(record.get("professionalTypeIdx"), record.get("professionalTypeName"));
	        }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

FaultTicketHandle.submit = function() {
	var baseForm = FaultTicketHandleWin.baseForm;
	var idx = baseForm.getForm().findField("idx").getValue();
	if (Ext.isEmpty(idx)) {
		MyExt.Msg.alert("提票工单主键为空，操作失败！");
		return;
	}
	var data = baseForm.getForm().getValues();
	if (!baseForm.getForm().isValid()) return;
	var qcArray = baseForm.findByType("TeamEmployee_SelectWin");
	var qcDatas = [];
	if (qcArray != null && qcArray.length > 0) {		
		for(var i = 0; i < qcArray.length; i++) {
			var field = qcArray[ i ];
			var qcData = {};
			qcData.checkItemCode = field.hiddenName;
			if(!baseForm.getForm().findField(field.hiddenName).getValue()) {
	        	MyExt.Msg.alert("请选择" + field.fieldLabel + "人员！");
				return;
	        }
	        qcData.qcEmpID = baseForm.getForm().findField(field.hiddenName).getValue();
	        qcDatas.push(qcData);			        
		}
	}
	var completeEmp = "";
    var workerValue = data.completeEmp;
    if (Ext.isArray(workerValue)) {
		for (var i = 0; i < workerValue.length; i++) {
			completeEmp += workerValue[i] + ",";
		}
		completeEmp = completeEmp.substring(0, completeEmp.length - 1);
	} else {
		completeEmp = workerValue;
	}
	Ext.Msg.confirm("提示","确认销票", function(btn){
		if(btn == 'yes'){
			showtip(FaultTicketHandleWin.win.getEl());
			//批量则取批量的IDX，否则取作业卡唯一标识
			var entityJson = {};
			entityJson.idx = idx;
			entityJson.completeEmp = completeEmp;
			entityJson.faultName = data.faultName ;
			entityJson.methodID = data.methodID;
			entityJson.methodName = data.methodName;
			entityJson.methodDesc = data.methodDesc;
			entityJson.repairResult = data.repairResult;
			entityJson.completeTime = data.completeTime;
			entityJson.fixPlaceFullName = data.fixPlaceFullName;
			entityJson.professionalTypeIdx = data.professionalTypeIdx;
			entityJson.professionalTypeName = data.professionalTypeName;
			
			var cfg = { 
		        url: ctx + "/faultTicket!handle.action",
		        jsonData: qcDatas, 
		        params: {
		        	entityJson: Ext.util.JSON.encode(entityJson)
		        },
		        success: function(response, options){
		            hidetip();
		            var result = Ext.util.JSON.decode(response.responseText);
					if(result.errMsg == null){
						alertSuccess("操作成功！");
					}else{
						alertFail(result.ex);
					}
					//重新加载数据
					FaultTicketHandle.grid.store.load();
					//关闭窗口
					FaultTicketHandleWin.win.close();
					FaultTicketHandleWin.win = null;
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	});
}

/*** 提票列表 start ***/
FaultTicketHandle.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/faultTicket!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    singleSelect: true,
    tbar:['refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'操作', dataIndex:'idx', editor: { xtype:'hidden' }, width:50, sortable:false,
		renderer:function(v,x,r,rowIndex){			
			return "<img src='" + img + "' alt='处理' style='cursor:pointer' onclick='FaultTicketHandle.showHandleWin(\"" + rowIndex + "\")'/>";
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
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
	}],
	toEditFn: function(grid, rowIndex, e) {
		FaultTicketHandle.showHandleWin(rowIndex);		
	},
    searchFn: function(searchParam){
    	FaultTicketHandle.searchParam = searchParam;
    	this.store.load();
    }
});
FaultTicketHandle.grid.store.on("beforeload", function(){	
	var searchParam = FaultTicketHandle.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	}
	var statusArray = [];
	statusArray.push(STATUS_OPEN);
	whereList.push({propName:'status', propValues: statusArray ,compare : Condition.IN}) ;
	
	var sqlStr = " REPAIR_TEAM = " + teamOrgId + " AND DISPATCH_EMP_ID LIKE '%" + empid + "%'" ;	
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	whereList.push({propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 界面布局 start ***/
FaultTicketHandle.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, collapsed: true,  height: 150, bodyBorder: false,
        items:[FaultTicketHandle.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ FaultTicketHandle.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:FaultTicketHandle.panel });
});