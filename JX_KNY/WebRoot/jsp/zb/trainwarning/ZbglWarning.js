/**
 * 机车检测预警 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglWarning');                       //定义命名空间
ZbglWarning.searchParam = {};
/*** 查询表单 start ***/
ZbglWarning.searchLabelWidth = 90;
ZbglWarning.searchAnchor = '95%';
ZbglWarning.searchFieldWidth = 270;
/** 获取当前日期及上个月当天的日期*/
ZbglWarning.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay = new Date(currentYear,currentMonth-1,currentDay);
	if(arg == 'begin'){
		return MonthFirstDay.format('Y-m-d');
	}
	else if (arg == 'end'){
		return Nowdate.format('Y-m-d');
	}
}
ZbglWarning.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglWarning.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglWarning.searchFieldWidth, labelWidth: ZbglWarning.searchLabelWidth, defaults:{anchor:ZbglWarning.searchAnchor},
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
		                var trainNo_comb = ZbglWarning.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			},{
				id:"beginDate", fieldLabel: '报活日期<br>(开始)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
				value: ZbglWarning.getCurrentMonth('begin'), width:ZbglWarning.searchFieldWidth
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglWarning.searchFieldWidth, labelWidth: ZbglWarning.searchLabelWidth, defaults:{anchor:ZbglWarning.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
//				minLength : 4, 
				maxLength : 4,
				vtype: "numberInt",
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","holdOrgId","holdOrgName","holdOrgSeq","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				editable:true
			},{
				id:"endDate", fieldLabel: '报活日期<br>(结束)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
				value: ZbglWarning.getCurrentMonth('end'), width:ZbglWarning.searchFieldWidth
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: ZbglWarning.searchFieldWidth, labelWidth: ZbglWarning.searchLabelWidth, defaults:{anchor:ZbglWarning.searchAnchor},
			items:[{
				xtype: "textfield", name: 'warningSource', fieldLabel: '报活来源'
			},{				
				fieldLabel: '站场',
				name: 'siteName',
				xtype:"Base_combo",
				business:'workPlace',
				entity:"com.yunda.jxpz.workplace.entity.WorkPlace",		
				queryHql:"from WorkPlace",
				fields:['workPlaceName'],
				displayField:"workPlaceName",
				valueField: "siteName",
				isAll: 'yes'			
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglWarning.searchForm.getForm();				
				var beginDate = form.findField("beginDate").getValue();
		        var endDate =form.findField("endDate").getValue();
		        if(endDate < beginDate){
		        	MyExt.TopMsg.msg('提示',"报活结束日期不能比报活开始日期早！", false, 1);
    				return;
		        }
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglWarning.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglWarning.searchForm;
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
            	searchParam = {};
            	ZbglWarning.grid.searchFn(searchParam);
            }
		}]
});
/*** 查询表单 end ***/

/*** 提票窗口 start ***/
ZbglWarning.editTpWin = new Ext.Window({
	title: "故障提票编辑", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: [ZbglTpWin.panel]
});
ZbglTpWin.closeWin = function() {
	ZbglWarning.editTpWin.hide();
	ZbglWarning.grid.store.load();
}

/*** 提票窗口 end ***/

/*** 机车检测预警列表 start ***/
ZbglWarning.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglWarning!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    tbar: [{
    	text:"下发班组", iconCls:"editIcon", handler: function() {
    		var grid = ZbglWarning.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var datas = grid.selModel.getSelections();
    		for (var i = 0; i < datas.length; i++) {
    			if (datas[i].get("warningStatus") != STATUS_TODO) {
    				MyExt.Msg.alert("只有未处理的预警活才能下发班组，请重新选择");
	        		return;
    			}
    		}
    		$yd.confirmAndDelete({
	            scope: grid, url: ctx + "/zbglWarning!updateForXfbz.action", params: {ids: $yd.getSelectedIdx(grid)}
	        });
    	}
    }, {
    	text:"转提票", iconCls:"editIcon", handler: function() {
    		var grid = ZbglWarning.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var datas = grid.selModel.getSelections();
    		if (datas.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;
    		}
    		if (datas[0].get("warningStatus") != STATUS_TODO) {
				MyExt.Msg.alert("只有未处理的预警活才能转提票，请重新选择");
        		return;
			}
			trainTypeIDX = datas[0].get("trainTypeIDX");
		    trainNo = datas[0].get("trainNo");
		    trainTypeShortName = datas[0].get("trainTypeShortName");
		    warningDesc = datas[0].get("warningDesc");
		    ZbglTpWin.warningIDX = datas[0].get("idx");
		    ZbglWarning.editTpWin.show();
		    ZbglTpWin.initSaveWin();
    	}
    }, {
    	text:"取消预警", iconCls:"cancelIcon", handler: function() {
    		var grid = ZbglWarning.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var datas = grid.selModel.getSelections();
    		for (var i = 0; i < datas.length; i++) {
    			if (datas[i].get("warningStatus") != STATUS_TODO) {
    				MyExt.Msg.alert("只有未处理的预警活才能取消预警，请重新选择");
	        		return;
    			}
    		}
    		$yd.confirmAndDelete({
	            scope: grid, url: ctx + "/zbglWarning!updateForCancel.action", params: {ids: $yd.getSelectedIdx(grid)}
	        });
    	}
    }, {
    	text:"打印", iconCls:"printerIcon", handler: function() {
    		var form = ZbglWarning.searchForm.getForm();
			if (!form.isValid()) {
				return;
			}
			var data = form.getValues();
			var startDate = "";
	        var overDate = "";
	        if(!Ext.isEmpty(Ext.getCmp("beginDate").getValue())){
	        	startDate = Ext.getCmp("beginDate").getValue().format('Y-m-d H:i');
	        }
	        if(!Ext.isEmpty(Ext.getCmp("endDate").getValue())){
	        	overDate = Ext.getCmp("endDate").getValue().format('Y-m-d') + " 23:59:59";
	        }
	        var warningStatus = ZbglWarning.warningStatus;
	        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
				data.trainNo = Ext.get("trainNo_comb_search").dom.value;
			}
			var reportUrl = "/zb/ZbglWarning.cpt?ctx=" + ctx.substring(1);
			var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeIDX=" + data.trainTypeIDX +
			"&trainNo=" + data.trainNo + "&siteName=" + data.siteName + "&warningSource=" + data.warningSource + "&warningStatus=" + warningStatus ;
            window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(dataUrl))+"&title=" + encodeURI("机车检测预警报活"));
    	}
    }, 'refresh', '-', {
    	    xtype:'label', text: '活票状态: '
    },{   
	        xtype:'checkbox', name:'warningStatus', boxLabel: STATUS_TODO_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_TODO, checked:true,
		    handler: function(){
		    	ZbglWarning.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'warningStatus', boxLabel: STATUS_RELEASE_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_RELEASE,
		    handler: function(){
		    	ZbglWarning.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'warningStatus', boxLabel: STATUS_RELEASED_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_RELEASED,
		    handler: function(){
		    	ZbglWarning.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'warningStatus', boxLabel: STATUS_NOTICE_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue: STATUS_NOTICE,
		    handler: function(){
		    	ZbglWarning.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'warningStatus', boxLabel: STATUS_CANCEL_CH , inputValue: STATUS_CANCEL,
		    handler: function(){
		    	ZbglWarning.checkQuery();
		    }
	    }
],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width: 60
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width: 60
	},{
		header:'机车出入段台账主键', dataIndex:'trainAccessAccountIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'报活来源', dataIndex:'warningSource', editor:{  maxLength:20 }, width: 200
	},{
		header:'报活时间', dataIndex:'warningTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'报活位置', dataIndex:'fixPlace', editor:{  maxLength:200 }, width : 300
	},{
		header:'活票描述', dataIndex:'warningDesc', editor:{  maxLength:500 }, width : 300
	},{
		header:'活票状态', dataIndex:'warningStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case STATUS_TODO:
                    return STATUS_TODO_CH;
                case STATUS_RELEASE:
                    return STATUS_RELEASE_CH;
                case STATUS_RELEASED:
                    return STATUS_RELEASED_CH;
                case STATUS_NOTICE:
                    return STATUS_NOTICE_CH;
                case STATUS_CANCEL:
                    return STATUS_CANCEL_CH;
                default:
                    return v;
            }
        }
	},{
		header:'处理人编码', dataIndex:'handlePersonID', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'处理时间', dataIndex:'handleTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'处理人名称', dataIndex:'handlePersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	},{
		header:'处理ID', dataIndex:'relIDX', hidden:true, editor:{  maxLength:50 }
	}],	
    searchFn: function(searchParam){
    	ZbglWarning.searchParam = searchParam;
    	this.store.load();
    },
    toEditFn: function(grid, rowIndex, e) {}
});
ZbglWarning.grid.store.setDefaultSort('warningTime', 'DESC');

ZbglWarning.warningStatus = STATUS_TODO;
ZbglWarning.checkQuery = function() {
	ZbglWarning.warningStatus = "-1";
	var checkBoxGroup = ZbglWarning.grid.getTopToolbar().findByType("checkbox");
	for(var i = 0; i < checkBoxGroup.length; i++) {
		if(checkBoxGroup[i].checked) {
			ZbglWarning.warningStatus = ZbglWarning.warningStatus + "," + checkBoxGroup[i].inputValue;
		}
	}
	ZbglWarning.grid.store.load();
}

ZbglWarning.grid.store.on("beforeload", function(){	
	var searchParam = ZbglWarning.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        if(!Ext.isEmpty(searchParam[prop]) && searchParam[prop] != " "){
        	switch(prop){
			 	//报活日期(起) 运算符为">="
			 	case 'beginDate':
			 		whereList.push({propName:'warningTime',propValue:searchParam[prop],compare:Condition.GE});
			 		break;
			 	//报活日期(止) 运算符为"<="
			 	case 'endDate':
			 		whereList.push({propName:'warningTime',propValue:searchParam[prop]+' 23:59:59',compare:Condition.LE});
			 		break;	 		
			 	default:
	         		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
        	}
        }
	}	
	whereList.push({propName:'warningStatus', propValues: ZbglWarning.warningStatus.split(",") ,compare : Condition.IN}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
/*** 机车检测预警列表 end ***/

/*** 界面布局 start ***/
ZbglWarning.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true,  height: 172, bodyBorder: false,
        items:[ZbglWarning.searchForm], frame: true, title: "查询", xtype: "panel"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglWarning.grid ]
    }]
};
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglWarning.panel });
});