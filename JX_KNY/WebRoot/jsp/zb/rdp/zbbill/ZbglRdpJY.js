/**
 * 机车整备合格交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpJY');                       //定义命名空间
ZbglRdpJY.searchParam = {};
ZbglRdpJY.searchLabelWidth = 90;
ZbglRdpJY.searchAnchor = '95%';
ZbglRdpJY.searchFieldWidth = 270;
/*** 机车整备合格交验查询表单 start ***/
ZbglRdpJY.searchParam = {};
//最近一个月
ZbglRdpJY.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	return MonthFirstDay.format('Y-m-d');
}
ZbglRdpJY.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglRdpJY.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdpJY.searchFieldWidth, labelWidth: ZbglRdpJY.searchLabelWidth, defaults:{anchor:ZbglRdpJY.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName",
				displayField: "shortName", valueField: "shortName",
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
		                var trainNo_comb = ZbglRdpJY.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			},{
				xtype: 'compositefield', fieldLabel : '入段日期', combineErrors: false,
	        	items: [
		           { id:"inTime_Id",name: "inTime",  xtype: "my97date",format: "Y-m-d",  
		             value : ZbglRdpJY.getCurrentMonth(),
					width: 100},
					{
			    	    xtype:'label', text: '至：'
				    },
					{ id:"toWarehousingTime",name: "inTime", xtype: "my97date",format: "Y-m-d",  
					width: 100}]
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdpJY.searchFieldWidth, labelWidth: ZbglRdpJY.searchLabelWidth, defaults:{anchor:ZbglRdpJY.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				maxLength : 4,
				vtype: "numberInt",
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
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: ZbglRdpJY.searchFieldWidth, labelWidth: ZbglRdpJY.searchLabelWidth, defaults:{anchor:ZbglRdpJY.searchAnchor},
			items:[{
				xtype: "textfield", name: 'dName', fieldLabel: '配属段'
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglRdpJY.searchForm.getForm();
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglRdpJY.ongoingGrid.searchFn(searchParam); 
				ZbglRdpJY.completeGrid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglRdpJY.searchForm;
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
                delete trainNo_comb.queryParams.trainTypeShortName;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	ZbglRdpJY.ongoingGrid.searchFn(searchParam);
            	ZbglRdpJY.completeGrid.searchFn(searchParam);
            }
		}]
});
/*** 机车整备合格交验查询表单 end ***/
/*** 机车整备合格交验列表 start ***/
ZbglRdpJY.setColor = function(handledCount, allCount) {
	var value = handledCount + '/' + allCount;
	if (handledCount < allCount)
		return '<font color=blue></font><span style="color:red">' + value + '</span>';
	return value;	 
}
//未交验
ZbglRdpJY.ongoingGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpJY!findRdpListForJY.action',                 //装载列表数据的请求URL
    viewConfig:null,
    singleSelect: true,
    tbar:[{
    	text:"交验/查看", iconCls:"addIcon", handler: function() {
    		var grid = ZbglRdpJY.ongoingGrid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		if (data.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;	        	
    		}
    		if (data[0].get("rdpStatus") == STATUS_HANDLING)
    			ZbglRdpJYWin.editWin(data[0].data);   
    		else if (data[0].get("rdpStatus") == STATUS_HANDLED)
    			ZbglRdpJYWin.searchWin(data[0].data);  
    	}
    },{
    	text:"打印", iconCls:"printerIcon", handler: function() {    		
    		var sel = ZbglRdpJY.ongoingGrid.selModel.getSelections();
            if(sel.length != 1){
                MyExt.Msg.alert("请选择一条记录!");
                return ;
            }
            // 检查任务
            var handledRdpCount = sel[0].get("handledRdpCount");
			var allRdpCount = sel[0].get("allRdpCount");
			if(handledRdpCount != allRdpCount){
				MyExt.Msg.alert("检查任务尚未完成!");
                return ;
			}
            // 提票任务
            var handledTpCount = sel[0].get("handledTpCount");
			var allTpCount = sel[0].get("allTpCount");
			if(handledTpCount != allTpCount){
				MyExt.Msg.alert("提票任务尚未完成!");
                return ;
			}
            var rdpIdx = sel[0].get("idx"); 
            var accountIdx = sel[0].get("trainAccessAccountIDX");                             
            var url = "/zb/rdp/ZbglRdpHGJY.cpt";
            if(rdpIdx){
                url += "&rdpIdx=" + rdpIdx + "&accountIdx=" + accountIdx;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(url))+"&title=" + encodeURI("整备合格交验记录"));
            }
    	}
    },{text:"导出", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglRdpJY.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("inTime_Id").getValue() != ""){
		        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d') + " 00:00:00";
		        }
		        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
		        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/rdp/ZbglRdpJYList.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeShortName=" + data.trainTypeShortName +
				"&trainNo=" + data.trainNo + "&dName=" + data.dName + "&siteID=" + siteID + "&rdpStatus="+STATUS_HANDLING;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车整备合格未交验列表"));
    	     }
     },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'dname', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', width: 80,
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		}
	},{
		header:'整备开始时间', dataIndex:'rdpStartTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'检修类型', dataIndex:'repairClass', width: 80,
        renderer: function(v){
            switch(v){
            	case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_LX:
                    return "临修";
                default:
                    return v;
            }
        }
	},{
		header:'检查任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledRdpCount");
			var allCount = record.get("allRdpCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'提票任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledTpCount");
			var allCount = record.get("allTpCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'普查整治任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledPczzCount");
			var allCount = record.get("allPczzCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'状态', dataIndex:'rdpStatus',
        renderer: function(v){
            switch(v){
            	case STATUS_HANDLING:
                    return "未交验";
                case STATUS_HANDLED:
                    return "已交验";
            }
        }
	},{
		header:'整备后去向', dataIndex:'afterToGo', width: 80, sortable: false, menuDisabled : false
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	},{
		header:'trainAccessAccountIDX', dataIndex:'trainAccessAccountIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonId', dataIndex:'fromPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonName', dataIndex:'fromPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonId', dataIndex:'toPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonName', dataIndex:'toPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'outOrder', dataIndex:'outOrder', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'remarks', dataIndex:'remarks', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'rdpEndTime', dataIndex:'rdpEndTime', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'handledRdpCount', dataIndex:'handledRdpCount', hidden:true
	},{
		header:'allRdpCount', dataIndex:'allRdpCount', hidden:true
	},{
		header:'handledTpCount', dataIndex:'handledTpCount', hidden:true
	},{
		header:'allTpCount', dataIndex:'allTpCount', hidden:true
	},{
		header:'handledPczzCount', dataIndex:'handledPczzCount', hidden:true
	},{
		header:'allPczzCount', dataIndex:'allPczzCount', hidden:true
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglRdpJY.searchParam = searchParam;
    	this.store.load();
    }
});
ZbglRdpJY.ongoingGrid.store.on('beforeload', function() {
	ZbglRdpJY.searchParam = ZbglRdpJY.searchForm.getForm().getValues();
	var searchParam = ZbglRdpJY.searchParam;
	searchParam.rdpStatus = STATUS_HANDLING;
	delete searchParam.inTime;
	searchParam = MyJson.deleteBlankProp(searchParam);
	var startDate = "";
    var overDate = "";
    if(Ext.getCmp("inTime_Id").getValue() != ""){
    	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d H:i');
    }
    if(Ext.getCmp("toWarehousingTime").getValue() != ""){
    	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
    }
    //用整备开始、结束时间作为入段开始、结束时间来过滤
    searchParam.rdpStartTime = startDate ;
    searchParam.rdpEndTime = overDate ;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);	
	
});

//已交验grid
ZbglRdpJY.completeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpJY!findRdpListForJY.action',                 //装载列表数据的请求URL
    viewConfig:null,
    singleSelect: true,
    tbar:[{
    	text:"交验/查看", iconCls:"addIcon", handler: function() {
    		var grid = ZbglRdpJY.completeGrid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		if (data.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;	        	
    		}
    		if (data[0].get("rdpStatus") == STATUS_HANDLING)
    			ZbglRdpJYWin.editWin(data[0].data);   
    		else if (data[0].get("rdpStatus") == STATUS_HANDLED)
    			ZbglRdpJYWin.searchWin(data[0].data);  
    	}
    },{
    	text:"打印", iconCls:"printerIcon", handler: function() {    		
    		var sel = ZbglRdpJY.completeGrid.selModel.getSelections();
            if(sel.length != 1){
                MyExt.Msg.alert("请选择一条记录!");
                return ;
            }
            // 检查任务
            var handledRdpCount = sel[0].get("handledRdpCount");
			var allRdpCount = sel[0].get("allRdpCount");
			if(handledRdpCount != allRdpCount){
				MyExt.Msg.alert("检查任务尚未完成!");
                return ;
			}
            // 提票任务
            var handledTpCount = sel[0].get("handledTpCount");
			var allTpCount = sel[0].get("allTpCount");
			if(handledTpCount != allTpCount){
				MyExt.Msg.alert("提票任务尚未完成!");
                return ;
			}
            var rdpIdx = sel[0].get("idx"); 
            var accountIdx = sel[0].get("trainAccessAccountIDX");                             
            var url = "/zb/rdp/ZbglRdpHGJY.cpt";
            if(rdpIdx){
                url += "&rdpIdx=" + rdpIdx + "&accountIdx=" + accountIdx;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(url))+"&title=" + encodeURI("整备合格交验记录"));
            }
    	}
    },{text:"导出", iconCls:"printerIcon",
    	     handler: function(){
    	     	var form = ZbglRdpJY.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("inTime_Id").getValue() != ""){
		        	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d') + " 00:00:00";
		        }
		        if(Ext.getCmp("toWarehousingTime").getValue() != ""){
		        	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/rdp/ZbglRdpJYList.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeShortName=" + data.trainTypeShortName +
				"&trainNo=" + data.trainNo + "&dName=" + data.dName + "&siteID=" + siteID + "&rdpStatus="+STATUS_HANDLED;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车整备合格未交验列表"));
    	     }
     },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'dname', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', width: 80,
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		}
	},{
		header:'整备开始时间', dataIndex:'rdpStartTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'检修类型', dataIndex:'repairClass', width: 80,
        renderer: function(v){
            switch(v){
            	case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_LX:
                    return "临修";
                default:
                    return v;
            }
        }
	},{
		header:'检查任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledRdpCount");
			var allCount = record.get("allRdpCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false, hidden:true
	},{
		header:'提票任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledTpCount");
			var allCount = record.get("allTpCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false, hidden:true
	},{
		header:'普查整治任务',  width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledPczzCount");
			var allCount = record.get("allPczzCount");
			return ZbglRdpJY.setColor(handledCount, allCount);
		}, sortable: false, hidden:true
	},{
		header:'状态', dataIndex:'rdpStatus',
        renderer: function(v){
            switch(v){
            	case STATUS_HANDLING:
                    return "未交验";
                case STATUS_HANDLED:
                    return "已交验";
            }
        }
	},{
		header:'整备后去向', dataIndex:'afterToGo', width: 80, sortable: false, menuDisabled : false
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	},{
		header:'trainAccessAccountIDX', dataIndex:'trainAccessAccountIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonId', dataIndex:'fromPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonName', dataIndex:'fromPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonId', dataIndex:'toPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonName', dataIndex:'toPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'outOrder', dataIndex:'outOrder', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'remarks', dataIndex:'remarks', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'rdpEndTime', dataIndex:'rdpEndTime', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'handledRdpCount', dataIndex:'handledRdpCount', hidden:true
	},{
		header:'allRdpCount', dataIndex:'allRdpCount', hidden:true
	},{
		header:'handledTpCount', dataIndex:'handledTpCount', hidden:true
	},{
		header:'allTpCount', dataIndex:'allTpCount', hidden:true
	},{
		header:'handledPczzCount', dataIndex:'handledPczzCount', hidden:true
	},{
		header:'allPczzCount', dataIndex:'allPczzCount', hidden:true
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglRdpJY.searchParam = searchParam;
    	this.store.load();
    }
});
ZbglRdpJY.completeGrid.store.on('beforeload', function() {
	ZbglRdpJY.searchParam = ZbglRdpJY.searchForm.getForm().getValues();
	var searchParam = ZbglRdpJY.searchParam;
	searchParam.rdpStatus = STATUS_HANDLED;
	delete searchParam.inTime;
	searchParam = MyJson.deleteBlankProp(searchParam);
	var startDate = "";
    var overDate = "";
    if(Ext.getCmp("inTime_Id").getValue() != ""){
    	startDate = Ext.getCmp("inTime_Id").getValue().format('Y-m-d H:i');
    }
    if(Ext.getCmp("toWarehousingTime").getValue() != ""){
    	overDate = Ext.getCmp("toWarehousingTime").getValue().format('Y-m-d') + " 23:59:59";
    }
    //用整备开始、结束时间作为入段开始、结束时间来过滤
    searchParam.rdpStartTime = startDate ;
    searchParam.rdpEndTime = overDate ;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);	
	
});
/*** 机车整备合格交验列表 end ***/
/*** 界面布局 start ***/

//编辑选项卡列表
ZbglRdpJY.tabs = new Ext.TabPanel({
    activeTab: 0, enableTabScroll:true, border:false,
    items:[{
            title: "未交验", layout: "fit", border: false, items: [ ZbglRdpJY.ongoingGrid ]
        },{
            title: "已交验", layout: "fit", border: false, items: [ ZbglRdpJY.completeGrid ]
        }]
});

ZbglRdpJY.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, height: 150, bodyBorder: false,
        items:[ZbglRdpJY.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglRdpJY.tabs ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglRdpJY.panel });
});